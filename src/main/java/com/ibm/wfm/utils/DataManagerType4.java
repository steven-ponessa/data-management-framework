package com.ibm.wfm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbRelationship;
import com.ibm.wfm.annotations.DbRelationships;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.DataSourceDim;
import com.ibm.wfm.beans.JsonNaryTreeNode;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.sdss.SdssStagingDim;
import com.ibm.wfm.exceptions.EtlException;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

/**
 * DataManagerType4
 *
 * Core utility class responsible for:
 *  - Managing JDBC connections to DB2
 *  - Performing generic CRUD operations using reflection and annotations
 *  - Dynamically generating SQL (INSERT, UPDATE, DELETE, SELECT)
 *  - Mapping ResultSets to Java objects using @DbColumn and @DbTable
 *  - Supporting hierarchical (taxonomy) queries and tree construction
 *
 * Key Design Concepts:
 *  - Annotation-driven ORM-lite behavior (no Hibernate/JPA)
 *  - Reflection-based field mapping
 *  - Batch processing for performance
 *  - Dynamic SQL generation based on metadata
 *
 * WARNING:
 *  - This class mixes multiple responsibilities (connection mgmt, SQL generation, mapping, tree building)
 *  - Not inherently thread-safe (mutable state + shared Connection usage)
 *  - Heavy use of reflection may impact performance at scale
 *
 * Used extensively by WFM-DMF framework for metadata-driven APIs.
 * *******************************************************************************************************
 * CHANGE LOG
 *  - [CHANGE][1][2026-04-23][SP]
 *    Remove association entity attributes out of the result set and returned hierarchy.
 *    
 *  - [CHANGE][2][2026-04-29][SP]
 *    Use OrgMask in WHERE clause, if not null
 */
public class DataManagerType4 {

	/*
	 * Static initialization block to ensure DB2 JDBC driver is loaded.
	 *
	 * NOTE:
	 *  - Modern JDBC drivers auto-register; this may be redundant in newer environments.
	 *  - Explicit registration retained for backward compatibility.
	 */
	static {
		try {
			// register the driver with DriverManager
			// The newInstance() call is needed for the sample to work with
			// JDK 1.1.1 on OS/2, where the Class.forName() method does not
			// run the static initializer. For other JDKs, the newInstance
			// call can be omitted.
			// Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean connected = false;
	private String server = null;
	private int port = -1;
	private String database = null;
	private String url = null;
	private Connection con = null;
	private Map<String, String> jdbcParameters = null;

	public DataManagerType4(String server, int port, String database, Map<String, String> jdbcParameters) {
		this.server = server;
		this.port = port;
		this.database = database;
		this.url = "jdbc:db2://" + server + ":" + String.valueOf(port) + "/" + database;
		this.jdbcParameters = jdbcParameters;
	}

	public DataManagerType4(String server, int port, String database) {
		this(server, port, database, null);
	}

	public DataManagerType4(String dbUrl, Map<String, String> jdbcParameters) {
		this.url = dbUrl;
		this.jdbcParameters = jdbcParameters;
	}

	public DataManagerType4(String dbUrl) {
		this(dbUrl, null);
	}

	public static void main(String[] args) {

		String systemName = null;
		boolean validParams = true;
		
		//process input variables
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-system")) {
					systemName = args[++optind];
				} else {
					// validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			validParams = false;
		}

		System.out.println("Testing currently disabled.");
	}

	/**
	 * Builds a JDBC connection dynamically using environment variables and tokens.
	 *
	 * Supports:
	 *  - User/password substitution
	 *  - API key-based authentication
	 *  - SSL configuration injection
	 *
	 * WARNING:
	 *  - Relies on environment variables → runtime failures if missing
	 *  - String replacement approach is error-prone and not type-safe
	 *  - Logging masks password but still prints full connection string
	 */
	public static Connection getConnection(DataSourceDim dataSource) throws SQLException {
		String jdbcUrlName = null;
		if (dataSource.getApiKeyTokenNm()==null || dataSource.getApiKeyTokenNm().equals(""))
			
			jdbcUrlName = dataSource.getUrl()
			.replace("{userid}", System.getenv(dataSource.getUseridTokenNm()))
			.replace("{password}", System.getenv(dataSource.getPasswordTokenNm()))
			.replace("{sslCertLocation}", System.getenv("sslCertLocation"))
			.replace("{sslTrustStoreLocation}", System.getenv("sslTrustStoreLocation"))
			.replace("{sslTrustStorePassword}", System.getenv("sslTrustStorePassword"))
			;
		else
			jdbcUrlName = dataSource.getUrl().replace("{api-key}", System.getenv(dataSource.getApiKeyTokenNm()));
		
		jdbcUrlName = Helpers.replaceVariables(jdbcUrlName);
		
		String jdbcUrlOut = jdbcUrlName;
		jdbcUrlOut = jdbcUrlOut.replaceAll("(password=)[^;]+", "$1************");
		
		System.out.println("######## jdbcUrlName: "+jdbcUrlOut);
		return DriverManager.getConnection(jdbcUrlName);
	}
	
	public Connection connect(String userid, String password) throws SQLException {
		if (this.jdbcParameters != null) {
			for (Map.Entry<String, String> jdbcParameter : this.jdbcParameters.entrySet()) {
				System.setProperty(jdbcParameter.getKey(), jdbcParameter.getValue());
			}
		}
		con = DriverManager.getConnection(url, userid, password);
		return con;
	}

	public Connection getConnection() {
		return con;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Map<String, String> getJdbcParameters() {
		return jdbcParameters;
	}

	public void setJdbcParameters(Map<String, String> jdbcParameters) {
		this.jdbcParameters = jdbcParameters;
	}
	
	public static int deleteAll2Connection(Connection conn, String targetTableName) {
		return deleteAll2Connection(conn, targetTableName, null);
	}
	
	public static int deleteAll2Connection(Connection conn, String targetTableName, String filter) {
		try {
			String sql = "DELETE FROM "+targetTableName;
			if (filter!=null) sql += " WHERE "+filter;
			System.out.println(sql);
			Statement stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Deletes records using dynamically generated WHERE clause based on @DbColumn(keySeq).
	 *
	 * Behavior:
	 *  - Uses reflection to identify key fields
	 *  - Builds PreparedStatement with parameter binding
	 *  - Executes in batches (commit every 100 records)
	 *
	 * Risks:
	 *  - Assumes keySeq fields uniquely identify rows
	 *  - Uses "-1" as null fallback for numeric fields (may cause incorrect deletes)
	 *  - No rollback handling on failure
	 */
	public static <T> int delete2Connection(Class<T> type, Connection conn, String targetTableName, List<T>  objects) {
		StringBuffer pos = new StringBuffer();
		PreparedStatement ps = null;
		String prepStmt = "DELETE FROM "+targetTableName+" WHERE ";
		String valuesStr = "";
		int i=0;
		int deleteCnt=0;
		for (Field field : type.getDeclaredFields()) {
			DbColumn column = field.getAnnotation(DbColumn.class);
			if (column!=null && !column.isId() && column.keySeq()>0) {
				if(++i>1) {
					valuesStr+=" AND ";
				}
				valuesStr+=column.columnName()+"=?";
			}
		} //end - for (Field field : type.getDeclaredFields())

		try {
			conn.setAutoCommit(false);
			System.out.println(prepStmt+valuesStr);
			ps = conn.prepareStatement(prepStmt+valuesStr);
			for (Object object: objects) {
				i=0;
				Class<?> zclass = object.getClass();
				for (Field field : zclass.getDeclaredFields()) {
					field.setAccessible(true);
					DbColumn column = field.getAnnotation(DbColumn.class);
					if (column!=null && !column.isId() && column.keySeq()>0) {
						Class<?> fieldType = field.getType();
						if (fieldType==String.class) {
							ps.setString(++i, (String)field.get(object));
							pos.append((i>1?",":"")).append("'").append((String)field.get(object)).append("'");
						}
						else if (fieldType==Integer.class || fieldType==int.class || fieldType==short.class) {
							ps.setInt(++i,field.get(object)==null?-1:(Integer)field.get(object));
							pos.append((i>1?",":"")).append((Integer)field.get(object));
						}
						else if (fieldType==Long.class || fieldType==long.class) {
							ps.setLong(++i,field.get(object)==null?-1:(Long)field.get(object));
							pos.append((i>1?",":"")).append((Long)field.get(object));
						}
						else if (fieldType==Double.class || fieldType==double.class) {
							ps.setDouble(++i,(Double) field.get(object));
							pos.append((i>1?",":"")).append((Double)field.get(object));
						}
						else if (fieldType==java.util.Date.class || fieldType==java.sql.Date.class) {
							ps.setDate(++i,(Date)field.get(object));
							pos.append((i>1?",":"")).append("'").append((Date)field.get(object)).append("'");
						}
						else if (fieldType==java.time.LocalDate.class) {
							ps.setDate(++i,Date.valueOf((java.time.LocalDate)field.get(object)));
							pos.append((i>1?",":"")).append("'").append(Date.valueOf((java.time.LocalDate)field.get(object))).append("'");
						}
						else if (fieldType==Timestamp.class) {
							ps.setTimestamp(++i,(Timestamp)field.get(object));
							pos.append((i>1?",":"")).append("'").append((Timestamp)field.get(object)).append("'");
						}
						else {
							System.out.println(field.getName()+"="+(String)field.get(object));
							System.out.println("****field.getName()/fieldType="+field.getName()+"/"+fieldType);
						}
					} //end - if (column!=null)
				} //end - for (Field field : zclass.getDeclaredFields())
				
				//Temp instead of batching
				//ps.execute();
				pos.append(")");
				System.out.println(pos.toString());
				
				ps.addBatch();
				if (++deleteCnt%100 == 0) {
					ps.executeBatch();
					conn.commit();
					System.out.println(deleteCnt);
				}
				else if(deleteCnt%50 == 0) System.out.print("*");
				else if(deleteCnt%10 == 0) System.out.print(".");
				
			} //end - for (Object object: objects)
			
			ps.executeBatch();
			
			conn.commit();
			conn.close();
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return -1;
		}
		
		return deleteCnt;
	}
	
	public static <T> int deleteSdss(Connection conn, List<T>  objects) {
		StringBuffer pos = new StringBuffer();
		PreparedStatement ps = null;
		String prepStmt = "UPDATE REFT.SDSS_STAGING_DIM"
				        + "   SET EXPIR_TMS     = CURRENT TIMESTAMP,"
				        + "       ROW_STATUS_CD = 'D',"
				        + "       APRVL_CD      = 'P',"
				        + "       INTRANET_ID   = ?"
				        + " WHERE ESVF_ID = ?"
				        + "   AND EXPIR_TMS = '9999-12-31-23.59.59.999999';";
		int deleteCnt = 0;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(prepStmt);
			for (Object object: objects) {
				ps.setString(1, ((SdssStagingDim)object).getIntranetId());
				ps.setString(2, ((SdssStagingDim)object).getEsvfId());
				ps.addBatch();
				if (++deleteCnt%100 == 0) {
					ps.executeBatch();
					conn.commit();
					System.out.println(deleteCnt);
				}
				else if(deleteCnt%50 == 0) System.out.print("*");
				else if(deleteCnt%10 == 0) System.out.print(".");
				
			} //end - for (Object object: objects)
			
			ps.executeBatch();
			
			conn.commit();
			conn.close();
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
			return -1;
		}
		return deleteCnt;
	}
	
	/*

	 */
	public static <T> int insert2Connection(Class<T> type, Connection conn, String targetTableName, List<T>  objects) 
	throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		return insert2Connection(type, conn, targetTableName, objects, false, null);
	}

	/**
	 * Generic INSERT operation using reflection and @DbColumn annotations.
	 *
	 * Features:
	 *  - Batch insert support
	 *  - Optional auto-commit mode for single record
	 *  - Optional generated key retrieval
	 *  - Handles multiple data types (String, Integer, Date, etc.)
	 *
	 * Important Notes:
	 *  - Null handling is explicit (setNull vs default values)
	 *  - Strings are sanitized (quotes escaped)
	 *
	 * Risks:
	 *  - Reflection-heavy → performance overhead
	 *  - Manual SQL string building (autocommit mode) is error-prone
	 *  - Connection is closed inside method (caller loses control)
	 */
	public static <T> int insert2Connection(Class<T> type, Connection conn, String targetTableName, List<T>  objects, boolean returnGeneratedKeys, String returningColumnName) 
	throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		boolean autocommit = false; //sp, setting to true for debugging only. Setting to true (1) generates an insert statement (but quotes nulls and does not
		                            // format timestamp correctly) and bypassed 
		boolean debug = false; //true; //sp
		List<Object> params = null;
		
		int bufferSize = 100; //2; //sp 
		if (objects.size()==1) autocommit = true;
		boolean buildSql = true;
		StringBuffer sql = null;
		
		PreparedStatement ps = null;
		String prepStmt = "INSERT INTO "+targetTableName+" (";
		String valuesStr = ") VALUES(";
		
		int i=0;
		int insertCnt=0;
		for (Field field : type.getDeclaredFields()) {
			DbColumn column = field.getAnnotation(DbColumn.class);
			if (column!=null && !column.isId() && !column.isScd()  && !column.isExtension()) {
				if(++i>1) {
					prepStmt+=",";
					valuesStr+=",";
				}
				prepStmt+=column.columnName();
				valuesStr+="?";
			}
		} //end - for (Field field : type.getDeclaredFields())
		try {
			
			conn.setAutoCommit(autocommit);
			String sqlStr = prepStmt+valuesStr+")";
			System.out.println(sqlStr);
			
			if (returnGeneratedKeys)
				ps = conn.prepareStatement(prepStmt+valuesStr+")", Statement.RETURN_GENERATED_KEYS);
			else
				ps = conn.prepareStatement(prepStmt+valuesStr+")");

			
			for (Object object: objects) {
				if (buildSql) {
					sql = new StringBuffer();
					sql.append(prepStmt).append(") VALUES(");
				}
				i=0;
				Class<?> zclass = object.getClass();
				if (debug) params = new ArrayList<>();
				for (Field field : zclass.getDeclaredFields()) {
					field.setAccessible(true);
					DbColumn column = field.getAnnotation(DbColumn.class);
					if (column!=null && !column.isId() && !column.isScd() && !column.isExtension()) {
						Class<?> fieldType = field.getType();
						
						if (autocommit && i>0) sql.append(",");
						
						if (fieldType==String.class) {
							//if (debug) params.add(field.get(object));
							String x = null;
							if (field.get(object)!=null) {
								x = (String)field.get(object);
								if (x.trim().length()==0 || x.trim().equalsIgnoreCase("(null)") || x.trim().equalsIgnoreCase("null")) 
									x=null;
							}
							if (autocommit) {
								if (x==null) sql.append("null");
								else sql.append("'").append(((String)field.get(object)).replace("'", "''").trim()).append("'");
							}
							if (debug) params.add((x==null?null:(x.replace("'", "''").trim())));
							ps.setString(++i, (x==null?null:(x.replace("'", "''").trim())));
						}
						else if (fieldType==Integer.class || fieldType==int.class || fieldType==short.class) {

							if (autocommit) sql.append(field.get(object)==null?"null":(Integer)field.get(object));
							i++;
							if (field.get(object) == null) {
								if (debug) params.add("NULL");
								ps.setNull(i, Types.INTEGER);								
							}
							else {
								if (debug) params.add((Integer)field.get(object));
								ps.setInt(i, (Integer)field.get(object));								
							}
							/**/
						}
						else if (fieldType==Long.class || fieldType==long.class) {
							if (autocommit) sql.append(field.get(object)==null?"null":(Long)field.get(object));
							i++;
							if (field.get(object) == null) {
								if (debug) params.add("NULL");
								ps.setNull(i, Types.BIGINT);
							} else {
								if (debug) params.add((Long)field.get(object));
								ps.setLong(i, (Long)field.get(object));
							}
						}
						else if (fieldType==Short.class || fieldType==short.class) {
							if (autocommit) sql.append(field.get(object)==null?"null":(Short)field.get(object));
							i++;
							if (field.get(object) == null) {
								if (debug) params.add("NULL");
								ps.setNull(i, Types.SMALLINT);
							} else {
								if (debug) params.add((Short)field.get(object));
								ps.setShort(i, (Short)field.get(object));
							}
						}
						else if (fieldType==Double.class || fieldType==double.class) {
							if (autocommit) sql.append(field.get(object)==null?0.0:(Double)field.get(object));
							i++;
							if (field.get(object) == null) {
								if (debug) params.add("NULL");
								ps.setNull(i, Types.FLOAT);
							} else {
								if (debug) params.add((Double)field.get(object));
								ps.setDouble(i, (Double)field.get(object));
							}
						}
						else if (fieldType==java.util.Date.class || fieldType==java.sql.Date.class) {
							if (autocommit) {
								if (field.get(object)==null) {
									sql.append("null");
								}
								else {
									sql.append("'").append((Date)field.get(object)).append("'");
								}
							}
							if (debug) params.add((Date)field.get(object));
							ps.setDate(++i,(Date)field.get(object));
						}
						else if (fieldType==java.time.LocalDate.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append(Date.valueOf((java.time.LocalDate)field.get(object))).append("'");
							}
							if (field.get(object)==null) {
								if (debug) params.add("NULL");
								ps.setDate(++i,null);
							}
							else {
								if (debug) params.add(Date.valueOf((java.time.LocalDate)field.get(object)));
								ps.setDate(++i,Date.valueOf((java.time.LocalDate)field.get(object)));
							}
							//ps.setDate(++i,Date.valueOf((java.time.LocalDate)field.get(object)));
						}
						else if (fieldType==Timestamp.class) {
							if (autocommit) {
								if (field.get(object)==null) {
									sql.append("null");
								}
								else sql.append("'").append((Timestamp)field.get(object)).append("'");
							}
							if (debug) params.add((Timestamp)field.get(object));
							ps.setTimestamp(++i,(Timestamp)field.get(object));
						}
						else if (fieldType==BigDecimal.class) {
							if (autocommit) sql.append(field.get(object)==null?0.0:(BigDecimal)field.get(object));
							if (debug) params.add((BigDecimal)field.get(object));
							ps.setBigDecimal(++i,(BigDecimal)field.get(object));
						}
						else if (fieldType==Boolean.class || fieldType==boolean.class) {
							if (autocommit) sql.append(field.get(object)==null?false:(Boolean)field.get(object));
							if (debug) params.add(field.get(object)==null?false:(Boolean)field.get(object));
							ps.setBoolean(++i,field.get(object)==null?false:(Boolean)field.get(object));
						}
						else {
							System.out.println(field.getName()+"="+(String)field.get(object));
							System.out.println("****field.getName()/fieldType="+field.getName()+"/"+fieldType);
						}
					} //end - if (column!=null)
				} //end - for (Field field : zclass.getDeclaredFields())
				
				//Temp instead of batching for debugging
				if (autocommit) {
					System.out.println(sql.toString());
					//sp6 ps.execute();
					ps.executeUpdate();
					++insertCnt;
					if (returnGeneratedKeys) {
						ResultSet rs = ps.getGeneratedKeys();
						if (rs.next()) {
						    int generatedId = rs.getInt(1); // Retrieve the first column (the generated ID)
						    insertCnt = generatedId;
						    System.out.println("Generated RECORD_ID: " + generatedId+", insertCnt: "+insertCnt);
						}
						rs.close();
					}
					ps.close();
				}
				
				//for debugging only .. autocommit should be false
				if (!autocommit) {
					if (debug) System.out.println(SqlUtils.formatSql(sqlStr, params));
					ps.addBatch();
					if (++insertCnt%bufferSize == 0) {
						ps.executeBatch();
						System.out.println(insertCnt);
					}
					else if(insertCnt%(bufferSize/2) == 0) System.out.print("*");
					else if(insertCnt%(bufferSize/10) == 0) System.out.print(".");
				}
				
			} //end - for (Object object: objects)
			
			if (!autocommit) {
				ps.executeBatch();
				conn.commit();
				System.out.println(insertCnt);
			}
			
			//sp conn.commit();
			conn.close();
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		}
		
		return insertCnt;
	}
	
	/**
	 * Generic UPDATE operation using reflection.
	 *
	 * Logic:
	 *  - Non-key fields (keySeq == -1) → SET clause
	 *  - Key fields (keySeq > -1) → WHERE clause
	 *
	 * Behavior:
	 *  - Batch updates with periodic commits
	 *
	 * Risks:
	 *  - Uses default values (-1, 0.0) instead of NULL for primitives
	 *  - No optimistic locking or concurrency control
	 *  - Connection is closed internally
	 */
	public static <T> int update2Connection(Class<T> type, Connection conn, String targetTableName, List<T>  objects) 
	throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		boolean autocommit = false; //sp, setting to true for debugging only. Setting to true (1) generates an insert statement (but quotes nulls and does not
		                            // format timestamp correctly) and bypassed 
		if (objects.size()==1) autocommit = true;
		boolean buildSql = true;
		StringBuffer sql = new StringBuffer();
		
		PreparedStatement ps = null;
		String prepStmt = "UPDATE "+targetTableName+" SET ";
		String whereStr = " WHERE ";
		
		int i=0;
		int updateCnt=0;
		for (Field field : type.getDeclaredFields()) {
			DbColumn column = field.getAnnotation(DbColumn.class);
			if (column!=null && !column.isId() && column.keySeq()==-1 && !column.isScd()) {
				if(++i>1) {
					prepStmt+=",";
				}
				prepStmt+=column.columnName();
				prepStmt+="=?";
			}
		} //end - for (Field field : type.getDeclaredFields())
		i=0;
		for (Field field : type.getDeclaredFields()) {
			DbColumn column = field.getAnnotation(DbColumn.class);
			if (column!=null && column.keySeq()>-1 ) {
				if(++i>1) {
					whereStr+=" AND ";
				}
				whereStr+=column.columnName();
				whereStr+="=?";
			}
		} //end - for (Field field : type.getDeclaredFields())
		try {
			if (buildSql) sql.append(prepStmt).append(whereStr);
			
			conn.setAutoCommit(autocommit);

			ps = conn.prepareStatement(prepStmt+whereStr);
			for (Object object: objects) {
				i=0;
				Class<?> zclass = object.getClass();
				for (Field field : zclass.getDeclaredFields()) {
					field.setAccessible(true);
					DbColumn column = field.getAnnotation(DbColumn.class);
					if (column!=null && !column.isId() && column.keySeq()==-1 && !column.isScd()) {
						Class<?> fieldType = field.getType();
						
						if (autocommit && i>0) sql.append(",");
						
						if (fieldType==String.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append(((String)field.get(object)).trim()).append("'");
							}
							ps.setString(++i, ((String)field.get(object))==null?null:((String)field.get(object)).trim());
						}
						else if (fieldType==Integer.class || fieldType==int.class || fieldType==short.class) {
							if (autocommit) sql.append(field.get(object)==null?-1:(Integer)field.get(object));
							ps.setInt(++i,field.get(object)==null?-1:(Integer)field.get(object));
						}
						else if (fieldType==Double.class || fieldType==double.class) {
							if (autocommit) sql.append(field.get(object)==null?0.0:(Double)field.get(object));
							ps.setDouble(++i,field.get(object)==null?0.0:(Double) field.get(object));
						}
						else if (fieldType==java.util.Date.class || fieldType==java.sql.Date.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append((Date)field.get(object)).append("'");
							}
							ps.setDate(++i,(Date)field.get(object));
						}
						else if (fieldType==java.time.LocalDate.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append(Date.valueOf((java.time.LocalDate)field.get(object))).append("'");
							}
							ps.setDate(++i,Date.valueOf((java.time.LocalDate)field.get(object)));
						}
						else if (fieldType==Timestamp.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append((Timestamp)field.get(object)).append("'");
							}
							ps.setTimestamp(++i,(Timestamp)field.get(object));
						}
						else if (fieldType==BigDecimal.class) {
							if (autocommit) sql.append(field.get(object)==null?0.0:(BigDecimal)field.get(object));
							ps.setBigDecimal(++i,(BigDecimal)field.get(object));
						}
						else if (fieldType==Boolean.class || fieldType==boolean.class) {
							if (autocommit) sql.append(field.get(object)==null?false:(Boolean)field.get(object));
							ps.setBoolean(++i,field.get(object)==null?false:(Boolean)field.get(object));
						}
						else {
							System.out.println(field.getName()+"="+(String)field.get(object));
							System.out.println("****field.getName()/fieldType="+field.getName()+"/"+fieldType);
						}
					} //end - if (column!=null)
				} //end - for (Field field : zclass.getDeclaredFields())
				for (Field field : zclass.getDeclaredFields()) {
					field.setAccessible(true);
					DbColumn column = field.getAnnotation(DbColumn.class);
					if (column!=null && !column.isId() && column.keySeq()>-1 && !column.isScd()) {
						Class<?> fieldType = field.getType();
						
						if (autocommit && i>0) sql.append(",");
						
						if (fieldType==String.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append(((String)field.get(object)).trim()).append("'");
							}
							ps.setString(++i, ((String)field.get(object))==null?null:((String)field.get(object)).trim());
						}
						else if (fieldType==Integer.class || fieldType==int.class || fieldType==short.class) {
							if (autocommit) sql.append(field.get(object)==null?-1:(Integer)field.get(object));
							ps.setInt(++i,field.get(object)==null?-1:(Integer)field.get(object));
						}
						else if (fieldType==Double.class || fieldType==double.class) {
							if (autocommit) sql.append(field.get(object)==null?0.0:(Double)field.get(object));
							ps.setDouble(++i,field.get(object)==null?0.0:(Double) field.get(object));
						}
						else if (fieldType==java.util.Date.class || fieldType==java.sql.Date.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append((Date)field.get(object)).append("'");
							}
							ps.setDate(++i,(Date)field.get(object));
						}
						else if (fieldType==java.time.LocalDate.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append(Date.valueOf((java.time.LocalDate)field.get(object))).append("'");
							}
							ps.setDate(++i,Date.valueOf((java.time.LocalDate)field.get(object)));
						}
						else if (fieldType==Timestamp.class) {
							if (autocommit) {
								if (field.get(object)==null) sql.append("null");
								else sql.append("'").append((Timestamp)field.get(object)).append("'");
							}
							ps.setTimestamp(++i,(Timestamp)field.get(object));
						}
						else if (fieldType==BigDecimal.class) {
							if (autocommit) sql.append(field.get(object)==null?0.0:(BigDecimal)field.get(object));
							ps.setBigDecimal(++i,(BigDecimal)field.get(object));
						}
						else if (fieldType==Boolean.class || fieldType==boolean.class) {
							if (autocommit) sql.append(field.get(object)==null?false:(Boolean)field.get(object));
							ps.setBoolean(++i,field.get(object)==null?false:(Boolean)field.get(object));
						}
						else {
							System.out.println(field.getName()+"="+(String)field.get(object));
							System.out.println("****field.getName()/fieldType="+field.getName()+"/"+fieldType);
						}
					} //end - if (column!=null)
				} //end - for (Field field : zclass.getDeclaredFields())
				
				//Temp instead of batching for debugging
				if (autocommit) {
					System.out.println(sql.toString());
					ps.execute();
					++updateCnt;
				}
				
				//for debugging only .. autocommit should be false
				if (!autocommit) {
					ps.addBatch();
					if (++updateCnt%100 == 0) {
						ps.executeBatch();
						conn.commit();
						System.out.println(updateCnt);
					}
					else if(updateCnt%50 == 0) System.out.print("*");
					else if(updateCnt%10 == 0) System.out.print(".");
				}
				
			} //end - for (Object object: objects)
			
			if (!autocommit) ps.executeBatch();
			
			conn.commit();
			conn.close();
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		}
		
		return updateCnt;
	}	
	
	//SP - 2021-09-08
	public static <T> List<T> getSelectTaxonomyQuery(Class<T> type, Connection conn, String pit, String filters,int size) throws SQLException, ClassNotFoundException, IOException {
		return getSelectTaxonomyQuery(type, conn, false, pit, null, filters, size, false, null);
	}
	
	public static <T> List<T> getSelectTaxonomyQuery(Class<T> type, Connection conn, String pit, String filters,int size, boolean edsOnly, String dummyNodeKey) throws SQLException, ClassNotFoundException, IOException {
		return getSelectTaxonomyQuery(type, conn, false, pit, null, filters, size, edsOnly, dummyNodeKey);
	}
	
	
	public static <T> List<T> getSelectTaxonomyQuery(Class<T> type, Connection conn, String pit, String filters,int size, String dummyNodeKey) throws SQLException, ClassNotFoundException, IOException {
		return getSelectTaxonomyQuery(type, conn, false, pit, null, filters, size, false, dummyNodeKey);
	}
	
	public static <T> List<T> getSelectTaxonomyQuery(Class<T> type, Connection conn, String pit, String filters,int size, boolean edsOnly) throws SQLException, ClassNotFoundException, IOException {
		return getSelectTaxonomyQuery(type, conn, false, pit, null, filters, size, edsOnly, null);
	}
	
	public static <T> List<T> getSelectTaxonomyQuery(Class<T> type, Connection conn, boolean includePit, String pit, Map<String, Object> pathVarMap, String filters,
			int size, boolean edsOnly, String dummyNodeKey) throws SQLException, ClassNotFoundException, IOException {
		return getSelectTaxonomyQuery(type, conn, includePit, pit, pathVarMap, filters, size, edsOnly, dummyNodeKey, null, null);
	}
	
	/**
	 * Builds and executes a dynamic hierarchical (taxonomy) query.
	 *
	 * Capabilities:
	 *  - Traverses class hierarchy via @DbTable relationships
	 *  - Dynamically builds JOIN clauses
	 *  - Supports SCD (Slowly Changing Dimension) filtering via PIT
	 *  - Supports filtering, path variables, and EDS-only views
	 *  - Builds N-ary tree structure from flat result set
	 *
	 * Advanced Features:
	 *  - Supports multi-parent relationships via @DbRelationship
	 *  - Optional dummy root node
	 *  - Dynamic ORDER BY and FETCH FIRST clauses
	 *
	 * Risks / Complexity:
	 *  - Extremely complex logic (hard to debug/maintain)
	 *  - Tight coupling between annotations and SQL generation
	 *  - Assumes consistent naming conventions across schema
	 *  - Modifies selectColumns and class hierarchy dynamically (side effects)
	 */
	public static <T> List<T> getSelectTaxonomyQuery(Class<T> type, Connection conn, boolean includePit, String pit, Map<String, Object> pathVarMap, String filters,
			int size, boolean edsOnly, String dummyNodeKey, String pathName, Integer orgMaskInteger) throws SQLException, ClassNotFoundException, IOException {
		
		//sp-2026-03-25
		//pathName = "deliveryOrgMarketAssoc";
		//sp-end-2026-03-25
	
		List<T> list = new ArrayList<T>(); // Return an empty array, instead of null, if the query has no rows;
		
		if (dummyNodeKey!=null) {
			NaryTreeNode dummyNode = new NaryTreeNode(dummyNodeKey, "Dummy Root","",0);
			list.add((T)dummyNode);
		}
	
		if (pit!=null && pit.trim().length()>0) includePit=true;
		
		String tableSuffix = "_DIM_V";
		if (includePit)
			tableSuffix = "_SCD_V";
		if (edsOnly) tableSuffix = "_EDS"+tableSuffix;
	
		List<String> selectColumns = new ArrayList<>();
		List<String> tables = new ArrayList<>();
		List<String> keys = new ArrayList<>();
		List<String> foreignKeys = new ArrayList<>();
		List<String> orderBy = new ArrayList<>();
		List<Class>  zclassHierarchy = new ArrayList<>();
		
		//[CHANGE][1][2026-04-23][SP]
		//BEFORE: nothing
		//AFTER:  Create `associationClasses` List to store names of all association entities
		List<String> associationClasses = null;
		
		zclassHierarchy.add(type);
		int tableCnt = 0;
		try {
			Class<T> root = type;
			
			DbTable dbInfo = type.getAnnotation(DbTable.class);
			if (!dbInfo.isDimension()) tableSuffix = tableSuffix.replace("_DIM", "");
			if (dbInfo.useTable()) tableSuffix = "";
			tables.add(dbInfo.baseTableName() + tableSuffix + " T" + String.valueOf(tableCnt));
			
			//sp-2026-03-25
			//Assign DbRelationship IFF `DbRelationship.name()` matches `pathName`
			DbRelationships dbRelationshipContainer = type.getAnnotation(DbRelationships.class);
			DbRelationship[] dbRelationships = null; //dbRelationshipContainer.value();
			DbRelationship dbRelationship = null;
			
			if (dbRelationshipContainer!=null) {
				dbRelationships = dbRelationshipContainer.value();
				for (DbRelationship x: dbRelationships) {
					if (x.name().equalsIgnoreCase(pathName)) {
						dbRelationship = x;
						break;
					}
				}
			}
			//sp-end-2026-03-25
			
			boolean dbRelationship_joinParentKey_addedFlg = false;
			//[CHANGE][1][2026-04-23][SP]
			//BEFORE: columns for ALL DbTable types were included
			//AFTER:  - capture association entity class name to remove if from `zclassHierarchy` before final object is built
			//        - if added to exclude the inclusion of columns from tables that act as association entities
			if (dbInfo.type() == DbTable.Type.ASSOCIATION) {
				if (associationClasses==null) associationClasses = new ArrayList<>();
				associationClasses.add(type.getClass().getName());
			}
				
			for (Field field : type.getDeclaredFields()) {
				DbColumn column = field.getAnnotation(DbColumn.class);
				if (column != null) {
					if (!column.isScd() && column.foreignKeySeq() == -1 
							&& column.assocParentKey() == -1 && column.assocChildKey() == -1) {
						if (!(edsOnly && column.isExtension()) && dbInfo.type()!=DbTable.Type.ASSOCIATION)
							selectColumns.add("T" + String.valueOf(tableCnt) + "." + column.columnName());
					}
					if (column.keySeq()>0 && column.foreignKeySeq()>0 && dbInfo.type()!=DbTable.Type.ASSOCIATION)
						selectColumns.add("T" + String.valueOf(tableCnt) + "." + column.columnName());
					if (column.isScd() && includePit && dbInfo.type()!=DbTable.Type.ASSOCIATION)
						selectColumns.add("T" + String.valueOf(tableCnt) + "." + column.columnName());
					if (column.foreignKeySeq()>0 && dbRelationship==null) //sp-2026-03-05, added `&& dbRelationship==null`
						foreignKeys.add("T" + String.valueOf(tableCnt) + "." + column.columnName());
					//sp-2026-03-25
					if (dbRelationship!=null && !dbRelationship_joinParentKey_addedFlg) {
						foreignKeys.add("T" + String.valueOf(tableCnt) + "." + dbRelationship.joinParentKey());
						dbRelationship_joinParentKey_addedFlg = true;
					}
					//sp-end-2026-03-25
					if (column.keySeq() > 0)
						orderBy.add(0, "T" + String.valueOf(tableCnt) + "." + column.columnName());
				}
			}	

			Class zParentType = null;
			try {
				//sp-2026-03-25
				//Assign zParentType, use `target[Package]Bean` if dbRelationship exists for the table and `pathName`
				//zParentType = Class.forName(dbInfo.parentBeanPackageName()+"." + dbInfo.parentBeanName());
				if (dbRelationship!=null)
					zParentType = Class.forName(dbRelationship.targetBeanPackageName()+"."+dbRelationship.targetBeanName());
				else
					zParentType = Class.forName(dbInfo.parentBeanPackageName()+"." + dbInfo.parentBeanName());
				//sp-end-2026-03-25
				root = zParentType;
				zclassHierarchy.add(0, zParentType);
			} catch (ClassNotFoundException cnfe) {
			}
	
			while (zParentType != null) {
				tableCnt++;
				List<String> columns = new ArrayList<>();
				dbInfo = (DbTable) zParentType.getAnnotation(DbTable.class);
				
				//sp-2026-03-25
				//Assign DbRelationship IFF `DbRelationship.name()` matches `pathName`
				dbRelationshipContainer = (DbRelationships) zParentType.getAnnotation(DbRelationships.class);
				dbRelationships = null;
				dbRelationship = null;
				if (dbRelationshipContainer!=null) {
					dbRelationships = dbRelationshipContainer.value();
					for (DbRelationship x: dbRelationships) {
						if (x.name().equalsIgnoreCase(pathName)) {
							dbRelationship = x;
							break;
						}
					}
				}
				//sp-end-2026-03-25				
				
				if (!dbInfo.isDimension()) tableSuffix = tableSuffix.replace("_DIM", "");
				tables.add(dbInfo.baseTableName() + tableSuffix + " T" + String.valueOf(tableCnt));
				
				dbRelationship_joinParentKey_addedFlg = false;
				//[CHANGE][1][2026-04-23][SP]
				//BEFORE: columns for ALL DbTable types were included
				//AFTER:  - capture association entity class name to remove if from `zclassHierarchy` before final object is built
				//        - exclude the inclusion of table column into the columns (SELECT) array if table is as association entities
				//          if (!(... &&  && dbInfo.type()==DbTable.Type.ASSOCIATION))
				if (dbInfo.type() == DbTable.Type.ASSOCIATION) {
					if (associationClasses==null) associationClasses = new ArrayList<>();
					associationClasses.add(zParentType.getName());
				}
					
				for (Field field : zParentType.getDeclaredFields()) {
					DbColumn column = field.getAnnotation(DbColumn.class);
					if (column != null) {
						if (!column.isScd() && column.foreignKeySeq() == -1)
							if (!(edsOnly && column.isExtension()) && dbInfo.type()!=DbTable.Type.ASSOCIATION)
								columns.add("T" + String.valueOf(tableCnt) + "." + column.columnName());
						if (column.keySeq() > 0 && column.assocParentKey()==-1) {
							keys.add("T" + String.valueOf(tableCnt) + "." + column.columnName());
							orderBy.add(0, "T" + String.valueOf(tableCnt) + "." + column.columnName());
						}
						if (column.foreignKeySeq()>0 && dbRelationship==null) //sp-2026-03-05, added `&& dbRelationship==null`
							foreignKeys.add("T" + String.valueOf(tableCnt) + "." + column.columnName());
						//sp-2026-03-25
						if (dbRelationship!=null && !dbRelationship_joinParentKey_addedFlg) {
							foreignKeys.add("T" + String.valueOf(tableCnt) + "." + dbRelationship.joinParentKey());
							dbRelationship_joinParentKey_addedFlg = true;
						}
						//sp-end-2026-03-25
					}
				}
					
				selectColumns.addAll(0, columns);
				//dbInfo = (DbTable) zParentType.getAnnotation(DbTable.class);
				
				zParentType = null;
				if (dbInfo != null) {
					try {
						//sp-2026-03-25
						//Assign zParentType, use `target[Package]Bean` if dbRelationship exists for the table and `pathName`
						if (dbRelationship!=null)
							zParentType = Class.forName(dbRelationship.targetBeanPackageName()+"."+dbRelationship.targetBeanName());
						else
							zParentType = Class.forName(dbInfo.parentBeanPackageName()+"." + dbInfo.parentBeanName());
						//sp-end-2026-03-25
						root = zParentType;
						zclassHierarchy.add(0, zParentType);
					} catch (ClassNotFoundException cnfe) {
						System.out.println("ClassNotFoundException: "+cnfe.getMessage());
					}
				}
			}
			
			//SP - 2026-04-21. 
			//Test if removing Association Table entities from the select
			// will return a cleaner taxonomy.
			//selectColumns.removeIf(col -> col != null && col.startsWith("T3."));
	
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT ");
			int i = 0;
			for (String column : selectColumns) {
				sql.append((i++ == 0 ? "" : ",") + column);
			}
			i = 0;
			sql.append(" FROM ");
			for (String table : tables) {
				if (i == 0)
					sql.append(table);
				else {
					sql.append(" INNER JOIN " + table);
					if (i <= keys.size())
						sql.append(" ON " + keys.get(i - 1) + " = " + foreignKeys.get(i - 1));
				}
				i++;
			}
			
			if (pathVarMap!=null) {
				int parmCnt=0;
				if (filters!=null && filters.trim().length() > 0) parmCnt=1;
				for (Map.Entry<String,Object> entry : pathVarMap.entrySet()) {
					String quote="";
					if ( entry.getValue() instanceof String) quote="'";
					String column = Helpers.matchColum(selectColumns,entry.getKey());
					if (column==null) throw new EtlException("Parameter "+entry.getKey()+" not valid for query.");
					
					filters+= (parmCnt++>0?" AND ":"")+column+"=" +quote+ entry.getValue()+quote;
				}
			}
			
			if (filters != null && filters.trim().length() > 0)
				sql.append(" WHERE ").append(filters);
			
			i=0;
			String delim = "";
			if (includePit) {
				if (sql.toString().indexOf(" WHERE ")<=0) sql.append(" WHERE ");
				else i++;
				
				if (pit!=null && pit.trim().length()>0 && !pit.equalsIgnoreCase("all")) {
					delim = pit.toUpperCase().startsWith("CURRENT TIMESTAMP")?"":"'";
				}
				
				for (String table : tables) {
					String alias = table.trim().substring(table.indexOf(" ")+1);
					if (i++>0) sql.append(" AND ");
					sql.append(delim).append(pit).append(delim).append(" ")
					.append(" BETWEEN ").append(alias).append(".EFF_TMS AND ").append(alias).append(".EXPIR_TMS");
				}
			}
			
			//[CHANGE][2][2026-04-29][SP]
			// Add WHERE clause for orgMask
			if (orgMaskInteger!=null) {
				if (sql.toString().indexOf(" WHERE ")<=0) sql.append(" WHERE ");
				else sql.append(" AND ");
				
				sql.append(" BITAND(T0.ORG_MASK,").append(String.valueOf(orgMaskInteger)).append(") <> 0 ");
			}
			
			if (size > 0)
				sql.append(" FETCH FIRST ").append(String.valueOf(size)).append(" ROWS");
			i = 0;
			sql.append(" ORDER BY ");
			for (String column : orderBy) {
				sql.append((i++ == 0 ? "" : ",") + column);
			}
	
			System.out.println("SQL: " + sql.toString());
			
			//[CHANGE][1][2026-04-23][SP]
			//BEFORE: Nothing
			//AFTER:  Remove Association Table entities from the hierarchy.
			//        Note, this leaves it in the SQL but removes the class before the class is saved.
			if (associationClasses != null) {
				for (String associationClass : associationClasses) {
					zclassHierarchy.removeIf(zClass ->
			    	zClass != null &&
			    	associationClass.equals(zClass.getName())
					);
				}
			}
			
			//SP - 2026-04-21. 
			//Test if removing Association Table entities from the hierarchy
			// will return a cleaner taxonomy.  Note, this leaves it in the SQL but removes the class before the class is saved.
			//zclassHierarchy.removeIf(zClass ->
		    //	zClass != null &&
		    //	"com.ibm.wfm.beans.erdm.DeliveryOrgMarketAssocDim".equals(zClass.getName())
			//	);
			//}
	
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql.toString());
	
			T lastObjectFound = null;
			T lastRoot = null;
			while (rst.next()) {
				List<T> objectList = new ArrayList<>();
				
				if (dummyNodeKey!=null) {
					NaryTreeNode dummyNode = new NaryTreeNode(dummyNodeKey, "Dummy Root","",0);
					objectList.add((T)dummyNode);
				}
				
				String fullKey="";
				for (Class zclass : zclassHierarchy) {
					Constructor<T> constructor = zclass.getConstructor();
					T t = constructor.newInstance();
					loadResultSet2Object(rst, t, true);
					fullKey+= (fullKey.length()==0?"":":")+((NaryTreeNode)t).getCode().trim();
					((NaryTreeNode)t).setFullKey(fullKey);
					objectList.add(t);
				}
				i = 0;
				for (T object : objectList) {
					if (list.size() == 0) {
						list.add(object);
						lastObjectFound = object;
						lastRoot = object;
					} 
					else {
						NaryTreeNode tempObject = NaryTree.findStatic((NaryTreeNode) object, (NaryTreeNode) list.get(0), true); //true); //false);
						if (tempObject == null) {
							if (i == 0) {
								list.add(object);
								lastRoot = object;
							} 
							else ((NaryTreeNode) lastObjectFound).addChild((NaryTreeNode) object);
							if (i < objectList.size() - 1) lastObjectFound = object;
						} 
						else {
							if (i < objectList.size() - 1) lastObjectFound = (T) tempObject;
						}
					} //end - else - if (list.size() == 0)
					i++;
				} //end - for (T object : objectList)
				lastObjectFound = lastRoot;
			} //end - while (rst.next())
		} catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException
				| NoSuchMethodException e) {
			throw new RuntimeException("Unable to construct " + type.getName() + " object: " + e.getMessage(), e);
		}
		return list;
	}	
	
	public static String getSelectQuery(Connection conn, String query, String[] keys) throws SQLException, JacksonException, IOException {
	
		try {
			SimpleModule module = new SimpleModule();
			module.addSerializer(new ResultSetSerializer());
	
			ObjectMapper objectMapper = JsonMapper.builder()
				.addModule(module)
				.build();
	
			System.out.println("SQL: "+ query);
			Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = stmt.executeQuery(query);
			
			StringWriter stringWriter = null;
			String returnJson = null;
			try {
				// Use the DataBind Api
				ObjectNode objectNode = objectMapper.createObjectNode();
	
				// put the resultset in a containing structure
				objectNode.putPOJO("results", resultSet);
	
				stringWriter = new StringWriter();
				// generate all
				objectMapper.writeValue(stringWriter, objectNode);
				
				JsonNode root = objectMapper.readTree(stringWriter.toString());
				ArrayNode arrayNode =  (ArrayNode)root.path("results");
				
				if (keys!=null && keys.length>0) {
					JsonNaryTreeNode hierarchy = DataMarshaller.convertFlatHierarchy(arrayNode, keys, "WW Top");
					
					ObjectMapper objectMapper2 = JsonMapper.builder().build();
					stringWriter = new StringWriter();
					// generate all
					objectMapper2.writeValue(stringWriter, hierarchy);
					returnJson="{\"results\":["+stringWriter.toString()+"]}";
				}
				else returnJson = stringWriter.toString();
				
			}
			catch (Exception e) {
				System.out.println("******* e="+e.getMessage());
				e.printStackTrace();
			}
			
			stmt.close();
			conn.close();
	
			return returnJson;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Runtime error occurred " + e.getMessage(), e);
		}
	}
	
	public static <T> List<T> getSelectQuery(Class<T> type, Connection conn, String query) throws SQLException, IOException {
	    List<T> list = new ArrayList<T>(); //Return an empty array, instead of null, if the query has no rows;
	    try {
	       Statement stmt = conn.createStatement();
	       ResultSet rst = stmt.executeQuery(query);
	       Constructor<T> constructor = type.getConstructor();
	       while (rst.next()) {
	    	   T t = constructor.newInstance();
	    	   loadResultSet2Object(rst, t);
	    	   list.add(t);
	       }
	       conn.close();
	    } 
	    catch (InvocationTargetException | InstantiationException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException e) {
	    	throw new RuntimeException("Unable to construct "+type.getName()+ " object: " + e.getMessage(), e);
		} 
	    catch (Exception e) {
	    	System.out.println("$%$%$%$ Unable to construct "+type.getName()+ " object: " + e.getMessage());
	    	System.out.println("$%$%$%$ Query ="+query);
	    	System.out.println("$%$%$%$  list ="+ list==null?"null":String.valueOf(list.size()));
	    	throw new RuntimeException("Unable to construct "+type.getName()+ " object: " + e.getMessage()+"\nQuery="+query, e);
    	
	    }
	    return list;
	}
	
	/**
	 * Executes a parameterized SQL SELECT statement and maps each row
	 * of the result set into an instance of the specified Java class.
	 * 
	 * <p><b>Security Note:</b> This method intentionally accepts SQL parameter 
	 * values separately from the SQL text. Callers must use JDBC parameter 
	 * placeholders (?) rather than concatenating user input into SQL strings. 
	 * 
	 * This design protects against SQL injection vulnerabilities and aligns 
	 * with JDBC best practices.
	 *
	 * <p>The supplied SQL may contain JDBC parameter placeholders
	 * ({@code ?}). Each supplied parameter is bound using a
	 * {@link java.sql.PreparedStatement}, preventing SQL injection by
	 * separating SQL commands from user-supplied data.</p>
	 *
	 * <p>Each row returned by the query is instantiated using the entity's
	 * default constructor and populated using
	 * {@code loadResultSet2Object(ResultSet, Object)}.</p>
	 *
	 * @param <T>
	 *        The entity type to be returned.
	 *
	 * @param type
	 *        The class representing the entity to construct.
	 *
	 * @param conn
	 *        The JDBC connection used to execute the query.
	 *
	 * @param query
	 *        The SQL SELECT statement containing optional
	 *        {@code ?} parameter placeholders.
	 *
	 * @param parameters
	 *        Zero or more parameter values corresponding to the SQL
	 *        placeholders.
	 *
	 * @return A list of populated entity instances. An empty list is
	 *         returned if no rows match the query.
	 *
	 * @throws SQLException
	 *         If an error occurs executing the SQL statement.
	 *
	 * @throws IOException
	 *         If an I/O error occurs while processing the results.
	 *
	 * @throws RuntimeException
	 *         If the entity cannot be instantiated using its default
	 *         constructor.
	 */		
	public static <T> List<T> getSelectQuery(
	        Class<T> type,
	        Connection conn,
	        String query,
	        List<?> parameters
	) throws SQLException, IOException {

	    List<T> list = new ArrayList<>();

	    try (
	        Connection connection = conn;
	        PreparedStatement stmt =
	            connection.prepareStatement(query)
	    ) {
	        bindParameters(stmt, parameters);

	        try (ResultSet rst = stmt.executeQuery()) {
	            Constructor<T> constructor =
	                type.getConstructor();

	            while (rst.next()) {
	                T object = constructor.newInstance();

	                loadResultSet2Object(
	                    rst,
	                    object
	                );

	                list.add(object);
	            }
	        }
	    } catch (
	        InvocationTargetException
	        | InstantiationException
	        | IllegalArgumentException
	        | IllegalAccessException
	        | NoSuchMethodException e
	    ) {
	        throw new RuntimeException(
	            "Unable to construct "
	                + type.getName()
	                + " object: "
	                + e.getMessage(),
	            e
	        );
	    }

	    return list;
	}
	
	/**
	 * Binds parameter values to a {@link PreparedStatement}.
	 *
	 * <p>Each value is assigned to its corresponding JDBC parameter
	 * placeholder ({@code ?}) in the order supplied. Parameter numbering
	 * begins at one, as required by the JDBC specification.</p>
	 *
	 * <p>Binding parameter values rather than concatenating them directly
	 * into SQL statements protects against SQL injection attacks and
	 * allows the database engine to cache execution plans.</p>
	 *
	 * @param statement
	 *        The prepared statement whose parameters are to be populated.
	 *
	 * @param parameters
	 *        The ordered parameter values corresponding to the SQL
	 *        placeholders. May be empty but should not be {@code null}.
	 *
	 * @throws SQLException
	 *         If an error occurs assigning one or more parameter values.
	 */
	private static void bindParameters(
	        PreparedStatement statement,
	        List<?> parameters
	) throws SQLException {

	    if (parameters == null) {
	        return;
	    }

	    for (int index = 0; index < parameters.size(); index++) {
	        Object value = parameters.get(index);
	        int parameterIndex = index + 1;

	        statement.setObject(
	            parameterIndex,
	            value
	        );
	    }
	}	
	
	public static void loadResultSet2Object(ResultSet rst, Object object) 
			throws IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		loadResultSet2Object(rst, object, false);
	}
	
	/**
	 * Maps a ResultSet row to a Java object using reflection.
	 *
	 * Features:
	 *  - Supports primitive and wrapper types
	 *  - Handles type conversion (BigDecimal → Double, etc.)
	 *  - Supports multi-table queries (column disambiguation)
	 *  - Converts CLOB to String
	 *
	 * Notes:
	 *  - Uses field.setAccessible(true) → bypasses encapsulation
	 *  - Trims String values
	 *
	 * Risks:
	 *  - Reflection overhead
	 *  - Silent type coercion may mask data issues
	 */
	public static void loadResultSet2Object(ResultSet rst, Object object, boolean multiTable)
			throws IllegalArgumentException, IllegalAccessException, SQLException, IOException {
		
		// get the Class for the passed Object
		Class<?> zclass = object.getClass();

		// get the DbTable annotations for the Class 
		DbTable dbTable = zclass.getAnnotation(DbTable.class);
		
		/*
		 * Using Java Reflection, Iterate through all the Fields for the Java Class.
		 */
		for (Field field : zclass.getDeclaredFields()) {
			
			// get the DbColumn annotations for the field 
			DbColumn column = field.getAnnotation(DbColumn.class);
			
			// if the field has a DbColumn annotation, retrieve the result set value for this field
			if (column!=null) {
				// based on the column name for the field, determine if the column exists in the result set
				if (hasColumn(rst,column.columnName())) {
					field.setAccessible(true);
					
					Object value = null;
					if (multiTable) {
						
						int columnNumber = getColumnNumber(rst,column.columnName(), dbTable.baseTableName(), column.foreignKeySeq()>0);
						if (columnNumber>0) value = rst.getObject(columnNumber);
					}
					else {
						value = rst.getObject(column.columnName());
					}
					
					if (value instanceof Clob) {
						StringBuffer sb = new StringBuffer();
						Reader reader = ((Clob)value).getCharacterStream();
					    BufferedReader br = new BufferedReader(reader);
					    String line;
					    while (null != (line = br.readLine())) {
					    	sb.append(line);
					    }
					    br.close();
					    value = sb.toString();
					}
					Class<?> type = field.getType();
					if (isPrimitive(type)) {// check primitive type
						Class<?> boxed = boxPrimitiveClass(type);// box if primitive
						if (value==null && (type==int.class || type==short.class || type==long.class)) value=0;
						if (value==null && (type==double.class)) value=0.0;
						if (value==null && (type==float.class)) value=0.0f;
						if (value==null && (type==boolean.class)) value=false;
						
						if (value instanceof BigDecimal) {
							BigDecimal bd = (BigDecimal)value;
							value = boxed.cast(bd.doubleValue());
						}
						if (value instanceof Short) {
							Short bd = (Short)value;
							value = boxed.cast(bd.shortValue());
						}
						//if (value instanceof Double) {
						//	Double bd = (Double)value;
						//	value = boxed.cast(bd.doubleValue());
						//}
						else
							value = boxed.cast(value);
					}
					else if (type == String.class && value!=null) value = ((String)value).trim();
					
					if (value==null) field.set(object, value);
					else {
						// Explicitly converting the Integer value to a Short and other primitive wrapper 
						//   types (Byte, Short, Integer, Long, etc.), using Number:
						if (field.getType() == Short.class) {
						    field.set(object, ((Number) value).shortValue());
						} else if (field.getType() == Byte.class) {
						    field.set(object, ((Number) value).byteValue());
						} else if (field.getType() == Integer.class) {
						    field.set(object, ((Number) value).intValue());
						} else if (field.getType() == Long.class) {
						    field.set(object, ((Number) value).longValue());
						} else if (field.getType() == Double.class) {
						    field.set(object, ((Number) value).doubleValue());
						} else if (field.getType() == Float.class) {
						    field.set(object, ((Number) value).floatValue());
						} else if (value instanceof java.sql.Date && field.getType().equals(java.time.LocalDate.class)) {
							field.set(object,((java.sql.Date) value).toLocalDate());
						} else if (value instanceof java.sql.Timestamp) { // && field.getType().equals(java.time.LocalDateTime.class)) {
							field.set(object,((java.sql.Timestamp) value));
						}
						else {
						    // For other types, just set it directly
						    field.set(object, value);
						}
					}

				}
			}
		}
	}
	
	public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    for (int x = 1; x <= columns; x++) {
	        if (columnName.equals(rsmd.getColumnName(x))) {
	            return true;
	        }
	    }
	    return false;
	}
	
	//getColumnNumber(rst,column.columnName(), dbTable.baseTableName())
	public static int getColumnNumber(ResultSet rs, String columnName, String baseTableName, boolean foreignKey) throws SQLException {
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    
	    String[] baseTable = baseTableName.split("\\.");
	    if (baseTable.length<2) return -1;
	    
	    for (int x = 1; x <= columns; x++) {
	        if (baseTable[0].equals(rsmd.getSchemaName(x).trim()) &&
	        		rsmd.getTableName(x).startsWith(baseTable[1]) &&
	        		columnName.equals(rsmd.getColumnName(x).trim())) {
	            return x;
	        }
	        else if (foreignKey && columnName.equals(rsmd.getColumnName(x).trim())) return x;
	    }
	    return -1;
	}
	
	public static boolean isPrimitive(Class<?> type) {
		return (type == int.class || type == long.class || type == double.class || type == float.class
				|| type == boolean.class || type == byte.class || type == char.class || type == short.class);
	}
	
	public static Class<?> boxPrimitiveClass(Class<?> type) {
		if (type == int.class) {
			return Integer.class;
		} else if (type == long.class) {
			return Long.class;
		} else if (type == double.class) {
			return Double.class;
		} else if (type == float.class) {
			return Float.class;
		} else if (type == boolean.class) {
			return Boolean.class;
		} else if (type == byte.class) {
			return Byte.class;
		} else if (type == char.class) {
			return Character.class;
		} else if (type == short.class) {
			return Short.class;
		} else {
			String string = "class '" + type.getName() + "' is not a primitive";
			throw new IllegalArgumentException(string);
		}
	}

	/**
	 * Executes SQL statements from files in a directory.
	 *
	 * Behavior:
	 *  - Reads files matching pattern
	 *  - Splits into individual SQL statements
	 *  - Executes sequentially
	 *
	 * Risks:
	 *  - No transaction management (partial execution possible)
	 *  - No batching despite method name
	 *  - Errors do not stop execution flow
	 */
	public static void executeSqlBatch(Connection conn, String directoryName, String filePattern) throws SQLException, IOException {
		Statement stmt = conn.createStatement();
		String[] filePatterns = filePattern.split(",");
		for (String fp: filePatterns) {
			List<String> ddlFiles = FileHelpers.getFileList(directoryName+"/"+fp);
			for (String file: ddlFiles) {
				List<String> sqlStatements = FileHelpers.fileToSqlList(file);
				for (String sqlStatement: sqlStatements) {
					//stmt.addBatch(sqlStatement);
					stmt.execute(sqlStatement);
				}
			}
			//stmt.executeBatch();
		}
		stmt.close();
		conn.close();
	}
	
	public static int putUpdateQuery(Connection conn, String sql, Object... params) {
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
	        for (int i = 0; i < params.length; i++) {

	            Object value = params[i];

	            if (value == null) {
	                ps.setObject(i + 1, null);
	            }
	            else if (value instanceof String s) {
	                ps.setString(i + 1, s);
	            }
	            else if (value instanceof Integer n) {
	                ps.setInt(i + 1, n);
	            }
	            else if (value instanceof Long n) {
	                ps.setLong(i + 1, n);
	            }
	            else if (value instanceof Boolean b) {
	                ps.setBoolean(i + 1, b);
	            }
	            else if (value instanceof Timestamp t) {
	                ps.setTimestamp(i + 1, t);
	            }
	            else if (value instanceof Date d) {
	                ps.setDate(i + 1, d);
	            }
	            else {
	                ps.setObject(i + 1, value);
	            }
	            /*
	             * Future change
	             * Modern Db2 JDBC drivers correctly map:
	             * - String
	             * - Integer
	             * - Long
	             * - Short
	             * - Boolean
	             * - BigDecimal
	             * - Double
	             * - Float
	             * - Timestamp
	             * - Date
	             * - LocalDate
	             * - LocalDateTime
	             * 
	             * Without you having to write a large instanceof chain.  
	             * Only add explicit handling for null values (using setNull) or for unusual SQL types. 
	             * That will keep the DAO concise while still being flexible enough for a generic framework 
	             */
	        }
		
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}