package com.ibm.wfm.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.Count;
import com.ibm.wfm.beans.DataSourceDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.JsonNaryTreeNode;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.SelfReferenceTaxonomyNodeInterface;
import com.ibm.wfm.beans.SqlParameterDim;
import com.ibm.wfm.beans.SqlTemplateDim;
import com.ibm.wfm.beans.sdss.SdssBulkRequest;
import com.ibm.wfm.beans.sdss.SdssStagingDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.exceptions.EtlException;
import com.ibm.wfm.utils.AsyncHttpServletRequestWrapper;
import com.ibm.wfm.utils.DaoArtifactGenerator;
import com.ibm.wfm.utils.DataManagerType4;
import com.ibm.wfm.utils.DataMarshaller;
import com.ibm.wfm.utils.Helpers;
import com.ibm.wfm.utils.JsonNaryTree;
import com.ibm.wfm.utils.NaryTree;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.csv.CsvMapper;
import tools.jackson.dataformat.csv.CsvSchema;

/**
 * Contains the common data functions provided by services within the framework.  The abstract modifier in a class declaration to indicate that a 
 * class is intended only to be a base class of other classes, not instantiated on its own. The abstract method contains the functionality
 * that is inherited by the subclasses and applied to the data that class manages.
 * 
 * The {@code AbstractDaoService} abstract class implements the {@link DaoInterface} that contains the single abstract method, {@code getConnection()}. 
 * This method must be implemented in each concrete subclass that needs to perform database operations and must return a connection to the specific database.
 * 
 */
@Service
public abstract class AbstractDaoService implements DaoInterface {

	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	/**
	 * Receives any {@link java.util.List} of objects and returns a CSV file.
	 * 
	 * @param <T> - Type
	 * @param list - List of Type
	 * @param returnFileName - Name of CSV file to bee returned
	 * @param request - {@code HttpServletRequest} to get and set the content type of the {@code ResponseEntity}
	 * @return
	 * @throws IOException
	 */
	public <T> ResponseEntity<Resource> returnTax2Csv(List<T> list
			, String returnFileName
			, HttpServletRequest request) throws IOException {

		FileWriter fileWriter = new FileWriter(fileStorageProperties.getUploadDir()+"/" + returnFileName);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		for (T obj: list) {
			ArrayList<String> rows = new ArrayList<>();
			NaryTree.toCsv((NaryTreeNode) obj, rows);
			for (String row: rows) {
				printWriter.write(row);
				printWriter.write(System.lineSeparator());
			}
		}
		
		fileWriter.flush();
		fileWriter.close();
		printWriter.flush();
		printWriter.close();

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(returnFileName);

        // Try to determine file's content type
        String contentType = null;

        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}

	
	public <T> ResponseEntity<Resource> returnCsv(List<T> list
			, String returnFileName
			, HttpServletRequest request) throws IOException {
	    if (list == null || list.isEmpty()) {
	        throw new IllegalArgumentException("listObjects cannot be null or empty");
	    }

	    T myObject = list.get(0);

	    @SuppressWarnings("unchecked")
	    Class<T> type = (Class<T>) myObject.getClass();
	
		FileWriter fileWriter = new FileWriter(fileStorageProperties.getUploadDir()+"/" + returnFileName);
		PrintWriter printWriter = new PrintWriter(fileWriter);
	
		DataMarshaller.writeCsv2PrintWriter(type, printWriter, list);
		
		fileWriter.flush();
		fileWriter.close();
		printWriter.flush();
		printWriter.close();
	
	    // Load file as Resource
	    Resource resource = fileStorageService.loadFileAsResource(returnFileName);
	
	    // Try to determine file's content type
	    String contentType = null;
	
	    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	
	    // Fallback to the default content type if type could not be determined
	    if(contentType == null) {
	        contentType = "application/octet-stream";
	    }
	
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	            .body(resource);
	}
	
	public <T> ResponseEntity<Object> returnTax2CsvObject(List<T> list
			, String returnFileName
			, HttpServletRequest request) throws IOException {

		FileWriter fileWriter = new FileWriter(fileStorageProperties.getUploadDir()+"/" + returnFileName);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		String columnHeader = "";
		int level = 1;
		String delimiter = ",";
		ArrayList<String> headers = new ArrayList<>();
		NaryTree.toCsvHeader((NaryTreeNode) list.get(0), level, delimiter, columnHeader, headers);
		if (headers!=null && headers.size()>0)
			printWriter.write(headers.get(0));
		printWriter.write(System.lineSeparator());

		for (T obj: list) {
			ArrayList<String> rows = new ArrayList<>();
			NaryTree.toCsv((NaryTreeNode) obj, rows);
			for (String row: rows) {
				printWriter.write(row);
				printWriter.write(System.lineSeparator());
			}
		}
		
		fileWriter.flush();
		fileWriter.close();
		printWriter.flush();
		printWriter.close();

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(returnFileName);

        // Try to determine file's content type
        String contentType = null;

        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}
	
	public <T> ResponseEntity<Object> returnTax3CsvObject(List<JsonNaryTreeNode> list
			, String returnFileName
			, HttpServletRequest request) throws IOException, IllegalArgumentException, IllegalAccessException {

		FileWriter fileWriter = new FileWriter(fileStorageProperties.getUploadDir()+"/" + returnFileName);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		String columnHeader = "";
		int level = 1;
		String delimiter = ",";
		ArrayList<String> headers = new ArrayList<>();
		JsonNaryTree.toCsvHeader((JsonNaryTreeNode) list.get(0), level, delimiter, columnHeader, headers);
		if (headers!=null && headers.size()>0)
			printWriter.write(headers.get(0));
		printWriter.write(System.lineSeparator());

		for (JsonNaryTreeNode obj: list) {
			ArrayList<String> rows = new ArrayList<>();
			JsonNaryTree.toCsv((JsonNaryTreeNode) obj, rows);
			for (String row: rows) {
				printWriter.write(row);
				printWriter.write(System.lineSeparator());
			}
		}
		
		fileWriter.flush();
		fileWriter.close();
		printWriter.flush();
		printWriter.close();

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(returnFileName);

        // Try to determine file's content type
        String contentType = null;

        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}
	
	public <T> ResponseEntity<Object> returnCsvObject(List<T> list
			, String returnFileName
			, HttpServletRequest request) throws IOException {
	    if (list == null || list.isEmpty()) {
	        throw new IllegalArgumentException("listObjects cannot be null or empty");
	    }

	    T myObject = list.get(0);

	    @SuppressWarnings("unchecked")
	    Class<T> type = (Class<T>) myObject.getClass();
		
		FileWriter fileWriter = new FileWriter(fileStorageProperties.getUploadDir()+"/" + returnFileName);
		PrintWriter printWriter = new PrintWriter(fileWriter);
	
		DataMarshaller.writeCsv2PrintWriter(type, printWriter, list);
		
		fileWriter.flush();
		fileWriter.close();
		printWriter.flush();
		printWriter.close();
	
	    // Load file as Resource
	    Resource resource = fileStorageService.loadFileAsResource(returnFileName);
	
	    // Try to determine file's content type
	    String contentType = null;
	
	    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	
	    // Fallback to the default content type if type could not be determined
	    if(contentType == null) {
	        contentType = "application/octet-stream";
	    }
	
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	            .body(resource);
	}	
	
	public ResponseEntity connectToDatasource(String dataSourceNm) throws SQLException {
		//Call /dmf/data-sources
		String dataSourcesUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/dmf/data-sources/")
                .path(dataSourceNm)
                .toUriString();
		
		ResponseEntity<DataSourceDim[]> responseEntity = new RestTemplate().getForEntity(dataSourcesUri, DataSourceDim[].class); //uriVariables); 
		List<DataSourceDim> dataSources = Arrays.asList(responseEntity.getBody());
		
		if (dataSources==null || dataSources.size()==0) {
			System.out.println("Error: {data-source-name} = "+dataSourceNm+" not found.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"data-source "+dataSourceNm+" not defined to the framework.\"}");
		}
		
		Connection conn = DataManagerType4.getConnection(dataSources.get(0));
		
		if (conn==null) {
			System.out.println("Error: could not connect to {data-source} = "+dataSources.get(0).getName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Could not connect to {data-source} "+dataSourceNm+".\"}");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Successfully connected to {data-source} "+dataSourceNm+".\"}");
	}
	/*
	public ResponseEntity runDynamicSql(String dataSourceNm, String dynamicSqlNm, HttpServletRequest request) throws JacksonException, SQLException, IOException {
		return runDynamicSql(dataSourceNm, dynamicSqlNm, null, request, false, -1, -1);
	}
	
	public ResponseEntity runDynamicSql(String dataSourceNm, String dynamicSqlNm, HttpServletRequest request, boolean csv) throws JacksonException, SQLException, IOException {
		return runDynamicSql(dataSourceNm, dynamicSqlNm, null, request, csv, -1, -1);
	}
	
	public ResponseEntity runDynamicSql(String dataSourceNm, String dynamicSqlNm, HttpServletRequest request,  int offset, int maxResultSetSize) throws JacksonException, SQLException, IOException {
		return runDynamicSql(dataSourceNm, dynamicSqlNm, null, request, false, offset, maxResultSetSize);
	}

	@Async("wfmDmfAsyncExecutor")
    public CompletableFuture<ResponseEntity> runDynamicSqlAsync(String dataSourceNm, String dynamicSqlNm, String[] keys, HttpServletRequest request, boolean csv, int offset, int maxResultSetSize) throws JacksonException, SQLException, IOException {
		ResponseEntity re = runDynamicSql(dataSourceNm, dynamicSqlNm, keys, request, csv, offset, maxResultSetSize);
		return CompletableFuture.completedFuture(re);
	}
	*/

	
	public CompletableFuture<ResponseEntity<?>> runDynamicSqlAsync(String dataSourceNm, String dynamicSqlNm, boolean toCsv, String keyList, int offset, Integer resultSetMaxSizeObj, HttpServletRequest request) throws JacksonException, SQLException, IOException {
	    // Wrap the original HttpServletRequest before async execution
	    // Capture the current request attributes
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    AsyncHttpServletRequestWrapper requestWrapper = new AsyncHttpServletRequestWrapper(request);


	    return CompletableFuture.supplyAsync(() -> {
	    	// Restore the request attributes in the new thread
	        RequestContextHolder.setRequestAttributes(requestAttributes);
	    	
	        int resultSetMaxSize = (resultSetMaxSizeObj != null) ? resultSetMaxSizeObj : -1;
	        String[] keys = keyList.trim().isEmpty() ? null : keyList.split(",");

	        try {
	            return this.runDynamicSql(dataSourceNm, dynamicSqlNm, keys, requestWrapper, toCsv, offset, resultSetMaxSize);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	        }
	    });
	}
	
	public ResponseEntity runDynamicSql(String dataSourceNm, String dynamicSqlNm, String[] keys, HttpServletRequest request, boolean csv, int offset, int maxResultSetSize) throws JacksonException, SQLException, IOException {
		
		//Call /dmf/data-sources
		String dataSourcesUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/dmf/data-sources/")
                .path(dataSourceNm)
                .toUriString();
		
		ResponseEntity<DataSourceDim[]> responseEntity = new RestTemplate().getForEntity(dataSourcesUri, DataSourceDim[].class); //uriVariables); 
		List<DataSourceDim> dataSources = Arrays.asList(responseEntity.getBody());
		
		if (dataSources==null || dataSources.size()==0) {
			System.out.println("Error: {data-source-name} = "+dataSourceNm+" not found.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"data-source "+dataSourceNm+" not defined to the framework.\"}");
		}
		
		//Get the SQL Template for the requested dynamic query using API call (/dmf/sql-templates/{name})
		String sqlTemplateUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/dmf/sql-templates/")
                .path(dynamicSqlNm)
                .toUriString();
		
		ResponseEntity <SqlTemplateDim[]> templateResponseEntity = new RestTemplate().getForEntity(sqlTemplateUri, SqlTemplateDim[].class); //uriVariables); 
		List<SqlTemplateDim> sqlTemplates = Arrays.asList(templateResponseEntity.getBody());
		
		if (sqlTemplates==null || sqlTemplates.size()==0) {
			System.out.println("Error: {data-source-name} = "+dataSourceNm+" not found.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Dynamic query name (query-name) "+dynamicSqlNm+" not defined to the framework.\"}");
		}
		
		String sql = sqlTemplates.get(0).getTemplate();
		
		if (sql.indexOf("<#")>0) {

			Map<String, String[]> parameterMap = request.getParameterMap();
			Map<String, Object> model = parameterMap.entrySet()
	                .stream()
	                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));
			sql = DaoArtifactGenerator.renderTemplate(sql, model);
		}
		
		System.out.println("URL (before parameters): "+ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/v1/")
            .path("/dyn/sql/")
            .path(dataSourceNm)
            .path(dynamicSqlNm)
            .toUriString());
		
		//Get the Parameters for the SQL Template for the requested dynamic query using API call (/dmf/sql-templates/{name}/parameters)

		String sqlTemplateParametersUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("/dmf/sql-templates/")
                .path(dynamicSqlNm)
                .path("/parameters")
                .toUriString();
		
		ResponseEntity <SqlParameterDim[]> parametersResponseEntity = new RestTemplate().getForEntity(sqlTemplateParametersUri, SqlParameterDim[].class); //uriVariables); 
		List<SqlParameterDim> sqlParameters = Arrays.asList(parametersResponseEntity.getBody());
		
		for (SqlParameterDim sqlParameter : sqlParameters) {
			String parmValue = null;
			String[] parmValues = request.getParameterValues(sqlParameter.getName());
			if (parmValues==null || parmValues.length==0) {
				if (sqlParameter.getReqdInd() && sqlParameter.getDefaultValue()==null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Dynamic query "+dynamicSqlNm+" missing required parameter "+sqlParameter.getName()+" and no default value has been specified.\"}");
				}
				parmValue = sqlParameter.getDefaultValue();
				if (sqlParameter.getDataType()!=1) parmValue = (sqlParameter.getImplyOperatorInd()?"=":"")+ "'"+(parmValue==null?"":parmValue.trim())+"'";
				else parmValue = (sqlParameter.getImplyOperatorInd()?"=":"")+(parmValue==null?"":parmValue.trim());
			}
			else {
				parmValue = parmValues[0];
				if (sqlParameter.getImplyOperatorInd()) {
					if (parmValue.indexOf(",")>0) {
						String tmp = " IN (";
						String[] values = parmValue.split(",");
						for (int i=0; i<values.length; i++) {
							String value = values[i];
							if (i>0) tmp+=",";
							if (sqlParameter.getDataType()!=1) tmp+= "'"+value.trim()+"'";
							else tmp+= value.trim();
						}
						parmValue = tmp+")";
					}
					else if (parmValue.indexOf("*")>0) {
						String tmp = " LIKE ";
						if (sqlParameter.getDataType()!=1) tmp+= "'"+parmValue.trim()+"'";
						else tmp+= parmValue.trim();
						parmValue = tmp.replace("*", "%");
					}
					else {
						//sp 2025-05-18
						//String tmp = "="; //sp 2025-05-18
						String tmp = " IN ("; //sp 2025-05-18
						if (sqlParameter.getDataType()!=1) tmp+= "'"+parmValue.trim()+"'";
						else tmp+= parmValue.trim();
						//parmValue = tmp; //sp 2025-05-18
						parmValue = tmp + ")"; //sp 2025-05-18
					}
				}
				else {
					if (sqlParameter.getDataType()!=1) parmValue = "'"+parmValue.trim()+"'";
				}
			}
			
			sql = sql.replaceAll("\\{"+sqlParameter.getName()+"\\}", parmValue);
		}
		
		if (offset>0) sql+= " OFFSET "+String.valueOf(offset)+ " ROWS";
		if (maxResultSetSize>0) sql+= " FETCH FIRST "+String.valueOf(maxResultSetSize)+ " ROWS ONLY";
		
		String json = getForSql(dataSources.get(0), sql, keys);
		
		if (csv) {
			String returnFileName = dynamicSqlNm + ".csv";
			json = json.substring(11, json.length()-1);
			if (keys==null || keys.length==0) {
				
				JsonNode jsonTree = JsonMapper.builder().build().readTree(json);
				
				CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
				JsonNode firstObject = jsonTree.values().iterator().next();
				firstObject.propertyNames().iterator().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
				CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
				
				
				CsvMapper csvMapper = new CsvMapper();
				csvMapper.writerFor(JsonNode.class)
				  .with(csvSchema)
				  .writeValue(new File(fileStorageProperties.getUploadDir()+"/" + returnFileName), jsonTree);
				
		        // Load file as Resource
		        Resource resource = fileStorageService.loadFileAsResource(returnFileName);
	
		        // Try to determine file's content type
		        String contentType = null;
	
		        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	
		        // Fallback to the default content type if type could not be determined
		        if(contentType == null) {
		            contentType = "application/octet-stream";
		        }
	
		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(contentType))
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		                .body(resource);
			}
			else {
				JsonNaryTreeNode jsonTree =JsonMapper.builder().build().readValue(json.substring(1,json.trim().length()-1), JsonNaryTreeNode.class);
				try {
					return returnTax3CsvObject(Arrays.asList(jsonTree),returnFileName, request);
				} catch (IllegalArgumentException | IllegalAccessException | IOException e) {
					// TODO Auto-generated catch block
  					e.printStackTrace();
				}
			}
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(json);
	}
	
	public String getForSql(DataSourceDim dataSource, String sql) throws SQLException, JacksonException, IOException {
		return getForSql(dataSource, sql, null); //+" FOR READ ONLY");
	}
	
	public String getForSql(DataSourceDim dataSource, String sql, String[] keys) throws SQLException, JacksonException, IOException {
		Connection conn = DataManagerType4.getConnection(dataSource);
		
		if (conn==null) {
			System.out.println("Error: coult not connect to {data-source-name} = "+dataSource.getName());
			return null;
		}
		return getForSql(conn, sql, keys); //+" FOR READ ONLY");
	}
	
	public String getForSql(Connection conn, String sql) throws SQLException, JacksonException, IOException {
		return getForSql(conn,sql,null);
	}
	
	public String getForSql(Connection conn, String sql, String[] keys) throws SQLException, JacksonException, IOException {
		return DataManagerType4.getSelectQuery(conn,sql, keys);
	}
	
	//public <T> List<T> getListForSql(Class type, Connection conn, String sql, boolean returnCsv) throws SQLException, IOException {
	public <T> ResponseEntity<Object> getListForSql(
			Class type, 
			Connection conn, 
			String sql, 
			boolean returnCsv,
			HttpServletRequest request) throws SQLException, IOException {
		List<T> listObjects = DataManagerType4.getSelectQuery(type, conn, sql);
		if (returnCsv) {
			String csvFileName = Helpers.fromCamelCase(
					type.getSimpleName()).substring(0,
					type.getSimpleName().length() - 3);
			if (csvFileName.endsWith("y"))
				csvFileName = csvFileName.substring(0, csvFileName.length() - 1) + "ies.csv";
			else
				csvFileName += "s.csv";
			return this.returnCsvObject(listObjects, csvFileName, request);
		}
		return ResponseEntity.ok().body(listObjects);
	}
	
	/**
	 * Executes a parameterized SQL SELECT statement and maps the results
	 * to a list of Java objects.
	 * 
	 * <p><b>Security Note:</b> This method intentionally accepts SQL parameter 
	 * values separately from the SQL text. Callers must use JDBC parameter 
	 * placeholders (?) rather than concatenating user input into SQL strings. 
	 * 
	 * This design protects against SQL injection vulnerabilities and aligns 
	 * with JDBC best practices.
	 *
	 * <p>The supplied SQL statement may contain JDBC parameter placeholders
	 * ({@code ?}). The corresponding parameter values are supplied separately
	 * and are bound using a {@link java.sql.PreparedStatement}, protecting
	 * against SQL injection attacks.</p>
	 *
	 * <p>If {@code returnCsv} is {@code true}, the results are converted into
	 * a downloadable CSV file. Otherwise the mapped objects are returned
	 * as the response body.</p>
	 *
	 * @param <T>
	 *        The entity type being returned.
	 *
	 * @param type
	 *        The class representing the entity to be populated from the
	 *        result set.
	 *
	 * @param conn
	 *        The JDBC connection used to execute the query.
	 *
	 * @param sql
	 *        The SQL SELECT statement to execute. Parameter values should be
	 *        represented using {@code ?} placeholders.
	 *
	 * @param returnCsv
	 *        Indicates whether the results should be returned as CSV instead
	 *        of JSON/XML.
	 *
	 * @param request
	 *        The originating HTTP request.
	 *
	 * @param parameters
	 *        Zero or more parameter values corresponding to the SQL
	 *        placeholders.
	 *
	 * @return A {@link ResponseEntity} containing either the mapped objects
	 *         or a generated CSV file.
	 *
	 * @throws SQLException
	 *         If an error occurs executing the SQL statement.
	 *
	 * @throws IOException
	 *         If an error occurs generating the CSV response.
	 */
	public <T> ResponseEntity<Object> getListForSql(
	        Class<T> type,
	        Connection conn,
	        String sql,
	        List<?> parameters,
	        boolean returnCsv,
	        HttpServletRequest request
	) throws SQLException, IOException {

	    List<T> listObjects =
	        DataManagerType4.getSelectQuery(
	            type,
	            conn,
	            sql,
	            parameters
	        );

	    if (returnCsv) {
	        String csvFileName = Helpers.fromCamelCase(type.getSimpleName());

	        csvFileName = csvFileName.substring(0, csvFileName.length() - 3);

	        if (csvFileName.endsWith("y")) {
	            csvFileName = csvFileName.substring(0, csvFileName.length() - 1) + "ies.csv";
	        } 
	        else {
	            csvFileName += "s.csv";
	        }

	        return this.returnCsvObject(
	            listObjects,
	            csvFileName,
	            request
	        );
	    }

	    return ResponseEntity.ok(listObjects);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param type - Object type being retrieved/returned.
	 * @param pathVarMap - Map of parameter names (key) and values (String, Object>
	 * @param filters - filter in format of a SQL WHERE clause. Default="".
	 * @param orderByCols - Comma separatted "order by" columns in SQL format (comma separated database column names). "None" may also be specified. Default="" (orders by natural key).
	 * @param includeParentage - API includes target dimension and its parent dimensions up to the root. Default="false".
	 * @param topNodeNm - Only meaningful when includeParentage=true. For hierarchies where no single root (e.g., geographic topology), this is required.
	 * @param returnCsv - API to return results as CSV. Default=false.
	 * @param includePit - API to include Point in Time (PIT) data. Default=false.
	 * @param pit - Specific Point in Time (format: yyyy-MM-dd-hh.mm.ss.sssssss, example = 2021-06-28-00.00.00.0). Default="" (return CURRENT state).
	 * @param edsOnly - Return only the Enterprise Data Standards and exclude business unit extensions.
	 * @param resultSetMaxSize - Size of the result to be returned, us
	 * @param request - HttpServletRequest required to return CSV.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public <T> ResponseEntity<Object> find(Class type
			, Map<String, Object> pathVarMap
			, String filters
			, String orderByCols
			, boolean includeParentage
			, String topNodeNm
			, boolean returnCsv
			, boolean includePit
            , String pit
            , int offset
            , String resultSetMaxSize
            , HttpServletRequest request) 
            throws SQLException, ClassNotFoundException, IOException {
		return find(type
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, false                    //selfReferencing
				, topNodeNm
				, returnCsv
				, includePit
	            , pit
	            , false                    //edsOnly
	            , offset
	            , resultSetMaxSize
	            , null                     //String pathName
	            , null                     //Integer orgMask
	            , request);
	}
	
	public <T> ResponseEntity<Object> find(Class type
			, Map<String, Object> pathVarMap
			, String filters
			, String orderByCols
			, boolean includeParentage
			, String topNodeNm
			, boolean returnCsv
			, boolean includePit
            , String pit
            , String resultSetMaxSize
            , HttpServletRequest request) 
            throws SQLException, ClassNotFoundException, IOException {
		return find(type
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, false                    //selfReferencing
				, topNodeNm
				, returnCsv
				, includePit
	            , pit
	            , false                    //edsOnly
	            , -1                       //offset
	            , resultSetMaxSize
	            , null                     //String pathName
	            , null                     //Integer orgMask
	            , request);
	}
	
	public <T> ResponseEntity<Object> find(Class type
			, Map<String, Object> pathVarMap
			, String filters
			, String orderByCols
			, boolean includeParentage
			, String topNodeNm
			, boolean returnCsv
			, boolean includePit
            , String pit
            , boolean edsOnly
            , String resultSetMaxSize
            , HttpServletRequest request) 
            throws SQLException, ClassNotFoundException, IOException {
		return find(type
				, pathVarMap
				, filters
				, orderByCols
				, includeParentage
				, false                    //selfReferencing
				, topNodeNm
				, returnCsv
				, includePit
	            , pit
	            , edsOnly
	            , -1                       //offset
	            , resultSetMaxSize
	            , null                     //String pathName
	            , null                     //Integer orgMask
	            , request);
	}
	
	public <T> ResponseEntity<Object> find(Class type
            , Map<String, Object> pathVarMap
            , String filters
            , String orderByCols
            , boolean includeParentage
            , boolean selfReferencing
            , String topNodeNm
            , boolean returnCsv
            , boolean includePit
            , String pit
            , boolean edsOnly
            , int offset
            , String resultSetMaxSize
            , HttpServletRequest request
            ) 
           throws SQLException, ClassNotFoundException, IOException {
		return find(type
	            , pathVarMap
	            , filters
	            , orderByCols
	            , includeParentage
	            , selfReferencing
	            , topNodeNm
	            , returnCsv
	            , includePit
	            , pit
	            , edsOnly
	            , offset
	            , resultSetMaxSize
	            , null                     //String pathName
	            , null                     //Integer orgMask
	            , request);
		
	}
	
	public <T> ResponseEntity<Object> find(Class type
								 , Map<String, Object> pathVarMap
								 , String filters
								 , String orderByCols
								 , boolean includeParentage
								 , boolean selfReferencing
								 , String topNodeNm
								 , boolean returnCsv
								 , boolean includePit
								 , String pit
								 , boolean edsOnly
								 , int offset
								 , String resultSetMaxSize
								 , String pathName
								 , Integer orgMask
								 , HttpServletRequest request
			                             ) 
			                     throws SQLException, ClassNotFoundException, IOException {
		//this.t = type;
		boolean useScd = false;
		if (includePit==true || (pit!=null && pit.trim().length()>0)) useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all")) 
			size = Integer.parseInt(resultSetMaxSize);

		if (!useScd) pit=null;
		
		List<T> listObjects = null;
		if (includeParentage) {
			if (includePit && (pit==null || pit.trim().length()==0)) {
				pit = "CURRENT TIMESTAMP";
			}
			if (selfReferencing) {
				List<SelfReferenceTaxonomyNodeInterface> nodes= this.findAll(type		//Class<T> type
						                                                   , null		//Map<String, Object> pathVarMap
						                                                   , filters	//String filters
						                                                   , false		//boolean includePit
						                                                   , pit		//String pit
						                                                   , -1			//int offset
						                                                   , size		//int size
						                                                   , null		//String orderByCol
						                                                   , false		//boolean edsOnly
						                                                   , orgMask	//Integer orgMask
						                                                   );
				                                                   //findAll(type, null, filters, false, pit, -1, size, null, false, null);
			                                                       //  Class<T> type, Map<String, Object> pathVarMap, String filters, boolean includePit, String pit
				                                                  //, int offset, int size, String orderByCols, boolean edsOnly, Integer orgMask
				if (listObjects == null) listObjects = new ArrayList<T>();
				listObjects.add((T)NaryTree.buildTreeFromSelfReferencingTaxonomy(nodes)) ;
			}
			else
				listObjects =  this.findAllTax(type, pathVarMap, filters, includePit, pit, size, edsOnly, topNodeNm, pathName, orgMask); //SP - 2021-09-08
		}
		else {
			listObjects = this.findAll(type				//Class<T> type
					                 , pathVarMap		//Map<String, Object> pathVarMap
					                 , filters			//String filters
					                 , includePit		//boolean includePit
					                 , pit				//String pit
					                 , offset			//int offset
					                 , size				//int size
					                 , orderByCols		//String orderByCol
					                 , edsOnly			//boolean edsOnly
					                 , orgMask			//Integer orgMask
					                 );
		}
		
		if (listObjects==null || listObjects.size()==0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(listObjects);
		}
		
		if (returnCsv) {
			String csvFileName = Helpers.fromCamelCase(type.getSimpleName()).substring(0, type.getSimpleName().length()-3);
			if (csvFileName.endsWith("y")) csvFileName = csvFileName.substring(0,csvFileName.length()-1) + "ies.csv";
			else csvFileName+= "s.csv";
			if (includeParentage)
				return this.returnTax2CsvObject(listObjects, csvFileName, request);
			return this.returnCsvObject(listObjects, csvFileName, request);
		}
		return ResponseEntity.ok().body(listObjects);
	}
	
	public <T> ResponseEntity<Integer> insert(T object) throws IllegalArgumentException, IllegalAccessException, SQLException {

		return  insert(object, false, null);
	}
	
	public <T> ResponseEntity<Integer> insert(T object, boolean returnGeneratedKeys, String returningColumnName) throws IllegalArgumentException, IllegalAccessException, SQLException {
		//this.t = object.getClass();
		
	    if (object == null) {
	        throw new IllegalArgumentException("object cannot be null or empty");
	    }

	    @SuppressWarnings("unchecked")
	    Class<T> type = (Class<T>) object.getClass();
	
		int i = this.insertAll(type, Arrays.asList(object), returnGeneratedKeys, returningColumnName);

		if (i>0) return ResponseEntity.status(HttpStatus.CREATED).body(i);
		return  ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	public <T> ResponseEntity<Integer> delete(Class type, Object object) throws IllegalArgumentException, IllegalAccessException, SQLException {
		//this.t = type;
	
		int i =  -1;
		
		if (object==null || ((object instanceof String) && ((String)object).trim().length()==0))
			i = this.deleteAll(type);
		else if (object instanceof String)
			i = this.deleteAll(type, (String)object);
		else
			i = this.delete(Arrays.asList((T)object));
	
		if (i<1) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(i);
		return  ResponseEntity.status(HttpStatus.OK).body(i);
	}
	
	public ResponseEntity<EtlResponse> etl(Class<?> type
			, MultipartFile oldFile
            , MultipartFile newFile
            , int keyLength
            , String outputFileName) throws IOException, EtlException, CsvValidationException, IllegalArgumentException, IllegalAccessException, SQLException {
		
		//this.t = type;
		String oldFileName = null;
		if (oldFile!=null) oldFileName = fileStorageProperties.getUploadDir()+"/"+fileStorageService.storeFile(oldFile);
		String newFileName = fileStorageProperties.getUploadDir()+"/"+fileStorageService.storeFile(newFile);
		
		EtlResponse etlResponse = this.etl(type
				                         , oldFileName
				                         , newFileName
				                         , keyLength
				                         , fileStorageProperties.getUploadDir()+"/"+outputFileName);
		return ResponseEntity.ok().body(etlResponse);
		
	}
	
	public <T> List<T> findAllTax(Class<T> type, String filters, String pit, int size, String dummyNodeKey) throws SQLException, ClassNotFoundException, IOException {
		boolean includePit = false;
		if (pit!=null && pit.trim().length()>0) includePit = true;
		return findAllTax(type, null, filters, includePit, pit, size, false, dummyNodeKey, null, null);
	}
	
	public <T> List<T> findAllTax(Class<T> type, String filters, String pit, int size, boolean edsOnly, String dummyNodeKey) throws SQLException, ClassNotFoundException, IOException {
		boolean includePit = false;
		if (pit!=null && pit.trim().length()>0) includePit = true;
		return findAllTax(type, null, filters, includePit, pit, size, edsOnly, dummyNodeKey, null, null);
	}
	
	//SP - 2021-09-08
	public <T> List<T> findAllTax(Class<T> type, Map<String, Object> pathVarMap, String filters, boolean includePit, String pit, int size, boolean edsOnly, String dummyNodeKey, String pathName, Integer orgMask) throws SQLException, ClassNotFoundException, IOException {
		Connection conn = getConnection();
		System.out.println("Connection established");
		return  DataManagerType4.getSelectTaxonomyQuery(type, conn,  includePit, pit, pathVarMap, filters, size, edsOnly, dummyNodeKey, pathName, orgMask);
	}	
	
	public <T> List<T> findAll(Class<T> type) {
		return findAll(type, null, "", false, null, -1, 0, null, false, null);
	}
	
	public <T> List<T> findAll(Class<T> type, String filters, String pit) {
		return findAll(type, null, filters, false, pit, -1, 0, null, false, null);
	}
	
	public <T> List<T> findAll(Class<T> type, String filters, int size) {
		return findAll(type, null, filters, false, null, -1, size, null, false, null);
	}
	
	public <T> List<T> findAll(Class<T> type, String filters, int offset, int size) {
		return findAll(type, null, filters, false, null, offset, size, null, false, null);
	}
		
	//4-parms, called from find()
	public <T> List<T> findAll(Class<T> type, String filters, String pit, int size) {
		return findAll(type, null, filters, false, pit, -1, size, null, false, null);
	}
	
	public <T> List<T> findAll(Class<T> type, String filters, String pit, int offset, int size) {
		return findAll(type, null, filters, false, pit, offset, size, null, false, null);
	}
	
	public <T> List<T> findAll(Class<T> type, String filters, String pit, int size, boolean edsOnly) {
		return findAll(type, null, filters, false, pit, -1, size, null, edsOnly, null);
	}
	
	public <T> List<T> findAll(Class<T> type, String filters, String pit, int size, String orderByCols) {
		return findAll(type, null, filters, false, pit, -1, size, orderByCols, false, null);
	}
	
	public <T> List<T> findAll(Class<T> type, String filters, String pit, int offset, int size, String orderByCols) {
		return findAll(type, null, filters, false, pit, offset, size, orderByCols, false, null);
	}
	
	public <T> List<T> findAll(Class<T> type
			, Map<String, Object> pathVarMap
			, String filters
			, boolean includePit
			, String pit
			, int offset
			, int size
			, String orderByCols
			, boolean edsOnly
			, Integer orgMask) {
		
		List<T> fbsFootballDims = new ArrayList<>();
		
		try {
			filters = URLDecoder.decode(filters, "UTF-8");
			filters = filters.replace("*", "%");
			//filters = UriUtils.decode(filters, StandardCharsets.UTF_8);
			String xNm = null; //this.forceTableNm;
			String dimStr = "_DIM";
			if (xNm==null) {
				
				DbTable dbTable = (DbTable)type.getAnnotation(DbTable.class);
				//xNm = this.tableNm;
				xNm = dbTable.baseTableName();
				//if (includePit || pit!=null) xNm = this.scdTableNm;
				if (includePit || pit!=null) xNm = dbTable.baseTableName() + "_SCD_V";

				if (dbTable!=null && dbTable.baseTableName().length()>0) {
					if (!dbTable.isDimension()) dimStr="";
					if (dbTable.useTable()) xNm = dbTable.baseTableName();
					else if (edsOnly) {
						xNm = dbTable.baseTableName()+"_EDS"+dimStr+"_V";
						if (pit!=null) xNm = dbTable.baseTableName()+"_EDS_SCD_V";
					}
					else {
						xNm = dbTable.baseTableName()+dimStr+"_V";
						if (pit!=null) xNm = dbTable.baseTableName()+"_SCD_V";
					}
				}
			}

			Connection conn = getConnection();
			System.out.println("Connection established xx");
			
			int i=0;
			String selectClause = "";
			for (Field field : type.getDeclaredFields()) {
				DbColumn column = field.getAnnotation(DbColumn.class);
				if (column!=null) {
					if (!(pit==null && column.isScd())) {
						if (edsOnly && column.isExtension()) {}
						else {
							if(++i>1) selectClause+=",";
							selectClause+=column.columnName();
						}
					}
				}
			} //end - for (Field field : type.getDeclaredFields())
			
			String sql = "SELECT  "+selectClause+" FROM "+ xNm + " WHERE 1=1";
			
			if (pathVarMap!=null) {
				for (Map.Entry<String,Object> entry : pathVarMap.entrySet()) {
					String quote="";
					if ( entry.getValue() instanceof String) quote="'";
					
					sql+= " AND "+entry.getKey().toUpperCase().replaceAll("-", "_") +"=" +quote+ entry.getValue()+quote;
				}
			}
			
			if (filters.length()>0) {
				sql+=" AND "+filters.replace("%3D", "="); //Bad - SQL Injection
			}
			if (pit!=null && pit.trim().length()>0 && !pit.equalsIgnoreCase("all")) {
				String delim = pit.toUpperCase().startsWith("CURRENT TIMESTAMP")?"":"'";
				sql+=" AND "+delim+pit+delim+" BETWEEN EFF_TMS AND EXPIR_TMS";
			}
			
			//[CHANGE][2][2026-04-29][SP]
			// Add WHERE clause for orgMask
			if (orgMask!=null) {
				if (sql.toString().indexOf(" WHERE ")<=0) sql+=" WHERE ";
				else sql+=" AND ";
				
				sql+=" BITAND(ORG_MASK,"+String.valueOf(orgMask)+") <> 0 ";
			}
			
			StringBuffer orderBy = null;
			if (orderByCols==null || orderByCols.trim().length()==0) {
				for (Field field : type.getDeclaredFields()) {
					DbColumn column = field.getAnnotation(DbColumn.class);
					if (column!=null && column.keySeq()>0) {
						if (orderBy==null) {
							orderBy = new StringBuffer();
							orderBy.append(" ORDER BY ").append(column.columnName());
						}
						else orderBy.append(", ").append(column.columnName());
					}
				}
			}
			else {
				if (!orderByCols.equalsIgnoreCase("none")) {
					orderBy = new StringBuffer();
					orderBy.append(" ORDER BY ").append(orderByCols);
				}
			}
			if (orderBy!=null) sql+= orderBy.toString();
			if (offset>-1) sql+= " OFFSET "+offset+" ROWS";
			if (size>0) sql+= " FETCH FIRST "+size+" rows only";
			System.out.println("x "+sql);
			fbsFootballDims = DataManagerType4.getSelectQuery(type, conn, sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fbsFootballDims;
	}
	
	public <T> int insertAll(List<T> listObjects) throws SQLException, IllegalArgumentException, IllegalAccessException {
		//Class localT = fbsFootball.getClass();
	    if (listObjects == null || listObjects.isEmpty()) {
	        throw new IllegalArgumentException("listObjects cannot be null or empty");
	    }
	    
	    T myObject = listObjects.get(0);

	    @SuppressWarnings("unchecked")
	    Class<T> type = (Class<T>) myObject.getClass();
	    
		return insertAll(type, listObjects, false, null);
	}
	
	public <T> int insertAll(Class<T> type, List<T> fbsFootball, boolean returnGeneratedKeys, String returningColumnName) throws SQLException, IllegalArgumentException, IllegalAccessException {
		
		int insertCnt = -1;
		try {
			Connection conn = getConnection();
			System.out.println("Connection established");
			
			//String sourceTableNmOld = this.tableNm;
			DbTable dbTable = (DbTable)type.getAnnotation(DbTable.class);
			String sourceTableNm = dbTable.baseTableName();
			
			if (dbTable!=null && dbTable.baseTableName().length()>0) {
				String dimStr="_DIM";
				if (!dbTable.isDimension()) dimStr="";
				sourceTableNm = dbTable.baseTableName()+dimStr;
			}
			
			if (dbTable!=null && dbTable.insertToView()) sourceTableNm+= "_V";
			
			insertCnt = DataManagerType4.insert2Connection(type, conn, sourceTableNm, fbsFootball, returnGeneratedKeys, returningColumnName);
			conn.close();
			if (insertCnt>0) {
				System.out.println("Insert successfully completed.  Insert count="+String.valueOf(insertCnt));
				return insertCnt;
			}
			else {
				System.out.println("Insert failed.");
				return insertCnt;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public <T> int updateAll(List<T> listObjects) throws SQLException, IllegalArgumentException, IllegalAccessException {
		
		int insertCnt = -1;
		try {
		    if (listObjects == null || listObjects.isEmpty()) {
		        throw new IllegalArgumentException("listObjects cannot be null or empty");
		    }
		    
		    T myObject = listObjects.get(0);

		    @SuppressWarnings("unchecked")
		    Class<T> type = (Class<T>)myObject.getClass();
		    
		    DbTable dbTable = (DbTable)type.getAnnotation(DbTable.class);
		    
		    String tableNm = dbTable.baseTableName()+"_SCD_V";
		    
		    //throw new RuntimeException("DbTable annotation not found on class: " + type.getName());
		    
			Connection conn = getConnection();
			System.out.println("Connection established");
			insertCnt = DataManagerType4.update2Connection(type, conn, tableNm, listObjects);
			conn.close();
			if (insertCnt>0) {
				System.out.println("Insert successfully completed.  Insert count="+String.valueOf(insertCnt));
				return insertCnt;
			}
			else {
				System.out.println("Insert failed.");
				return insertCnt;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public <T> int deleteAll(Class<T> type) {
		return deleteAll(type, null);
	}
	
	public <T> int deleteAll(Class<T> type, String filter) {
		int deletedCnt = -1;
		Connection conn = getConnection();
		System.out.println("Connection established");

		DbTable dbTable = (DbTable)type.getAnnotation(DbTable.class);
		String tableName = dbTable.baseTableName();
		
		if (dbTable!=null && dbTable.baseTableName().length()>0) {
			String dimStr="_DIM";
			if (!dbTable.isDimension()) dimStr="";
			tableName = dbTable.baseTableName()+dimStr; //+"_TV";
		}
		
		if (dbTable!=null && dbTable.deleteToView()) tableName+= "_V";
		
		deletedCnt = DataManagerType4.deleteAll2Connection(conn, tableName, filter);
		return deletedCnt;
	}
	
	public <T> int delete(List<T> listObjects) {
	    if (listObjects == null || listObjects.isEmpty()) {
	        throw new IllegalArgumentException("listObjects cannot be null or empty");
	    }

	    T myObject = listObjects.get(0);

	    @SuppressWarnings("unchecked")
	    Class<T> type = (Class<T>) myObject.getClass();
	    
		Connection conn = getConnection();
		System.out.println("Connection established");
		
		DbTable dbTable = (DbTable)type.getAnnotation(DbTable.class);
		String tableName = dbTable.baseTableName();
		if (dbTable!=null && dbTable.baseTableName().length()>0) {
			String dimStr="_DIM";
			if (!dbTable.isDimension()) dimStr="";
			tableName = dbTable.baseTableName()+dimStr; //+"_TV";
		}
		if (dbTable!=null && dbTable.deleteToView()) tableName+= "_V";
		
		return DataManagerType4.delete2Connection(type, conn, tableName, listObjects);
	}
	
	/*
	 * 2026-07-22
	 * The current `deleteAll(Class<T> type, String filter)` method accepts either an arbitrary SQL fragment,'.
	 * Preserving the generic delete functionality, a better generic interface is to accept a fixed column name 
	 * controlled by application code and a separately bound value.
	 * 
	 */
	public <T> ResponseEntity<Integer> deleteBy(
	        Class<T> type,
	        String columnName,
	        Object value) {
	
		Connection conn = getConnection();
		System.out.println("Connection established");
		
		DbTable dbTable = (DbTable)type.getAnnotation(DbTable.class);
		String tableName = dbTable.baseTableName();
		if (dbTable!=null && dbTable.baseTableName().length()>0) {
			String dimStr="_DIM";
			if (!dbTable.isDimension()) dimStr="";
			tableName = dbTable.baseTableName()+dimStr;  
		}
		if (dbTable!=null && dbTable.deleteToView()) tableName+= "_V";
	
	    String safeColumnName = validateColumnName(type, columnName);
	
	    String sql =
	        "DELETE FROM " + tableName +
	        " WHERE " + safeColumnName + " = ?";
	
	    int i = DataManagerType4.putUpdateQuery(conn, sql, value);
	    if (i<1) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(i);
		return  ResponseEntity.status(HttpStatus.OK).body(i);
	}
	
	private Set<String> getMappedColumns(Class<?> type) {
	
	    return Arrays.stream(type.getDeclaredFields())
	            .map(Field::getName)
	            .map(String::toUpperCase)
	            .collect(Collectors.toUnmodifiableSet());
	}
	
	private String validateColumnName(Class<?> type, String columnName) {
	    Set<String> allowedColumns = getMappedColumns(type);
	
	    if (!allowedColumns.contains(columnName)) {
	        throw new IllegalArgumentException(
	            "Invalid column name: " + columnName
	        );
	    }
	
	    return columnName;
	}
	
	public <T> ResponseEntity<Object> findWithInheritence(Class type, Map<String, Object> pathVarMap, String filters,
			String orderByCols, boolean returnCsv, boolean includePit,
			String pit, boolean edsOnly, String resultSetMaxSize, HttpServletRequest request)
			throws SQLException, ClassNotFoundException, IOException {
		//this.t = type;
		boolean useScd = false;
		if (includePit == true || (pit != null && pit.trim().length() > 0))
			useScd = true;
		int size = -1;
		if (!resultSetMaxSize.equalsIgnoreCase("all"))
			size = Integer.parseInt(resultSetMaxSize);

		if (!useScd)
			pit = null;

		List<T> listObjects = null;

		listObjects = this.findAllWithInheritence(type, pathVarMap, filters, includePit, pit, size, orderByCols, edsOnly);


		if (listObjects == null || listObjects.size() == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(listObjects);
		}

		if (returnCsv) {
			String csvFileName = Helpers.fromCamelCase(type.getSimpleName()).substring(0,
					type.getSimpleName().length() - 3);
			if (csvFileName.endsWith("y"))
				csvFileName = csvFileName.substring(0, csvFileName.length() - 1) + "ies.csv";
			else
				csvFileName += "s.csv";
			return this.returnCsvObject(listObjects, csvFileName, request);
		}
		return ResponseEntity.ok().body(listObjects);
	}
	
	public <T> List<T> findAllWithInheritence(Class<T> type, Map<String, Object> pathVarMap, String filters, boolean includePit, String pit, int size, String orderByCols, boolean edsOnly) {
		
		List<T> fbsFootballDims = new ArrayList<>();
		try {
			filters = URLDecoder.decode(filters, "UTF-8");
			String xNm = null; //this.forceTableNm;
			if (xNm==null) {
				
				DbTable dbTable = (DbTable)type.getAnnotation(DbTable.class);
				if (dbTable!=null && dbTable.baseTableName().length()>0) {
					if (edsOnly) {
						xNm = dbTable.baseTableName()+"_EDS_DIM_V";
						if (pit!=null) xNm = dbTable.baseTableName()+"_EDS_SCD_V";
					}
					else {
						xNm = dbTable.baseTableName()+"_DIM_V";
						if (pit!=null) xNm = dbTable.baseTableName()+"_SCD_V";
					}
				}
			}

			Connection conn = getConnection();
			System.out.println("Connection established");
			
			int i=0;
			String selectClause = "";
			ArrayList<DbColumn> keys = null;
			for (Field field : type.getDeclaredFields()) {
				DbColumn column = field.getAnnotation(DbColumn.class);
				if (column!=null) {
					if (!(pit==null && column.isScd())) {
						if (edsOnly && column.isExtension()) {}
						else {
							if(++i>1) selectClause+=",";
							selectClause+=column.columnName();
							if (column.keySeq()>0) {
								if (keys==null) keys = new ArrayList<DbColumn>();
								keys.add(column);
							}
						}
					}
				}
			} //end - for (Field field : type.getDeclaredFields())
			
			if (keys==null) return null;
			
			String sql = ""; //"SELECT  "+selectClause+" FROM "+ xNm + " WHERE 1=1";
			
			int priority = 0;
			int numKeys = pathVarMap.size();
			for (int k=numKeys-1; k>=0; k--) {
				//DbColumn column = keys.remove(keys.size()-1);
				DbColumn column = keys.get(k);

				sql+="SELECT  " + ++priority + " AS PRIORITY, "+selectClause+" FROM "+ xNm + " WHERE 1=1";
				for (int l=0; l<keys.size(); l++) {
					DbColumn keyColumn = keys.get(l);
					String value = (String)pathVarMap.get(keyColumn.columnName().toLowerCase().replace("_","-"));
					if (value==null || l>k) sql+= " AND " + keyColumn.columnName()+ " IS NULL";
					else sql+= " AND " + keyColumn.columnName()+ "='"+value.trim()+"'";
				}
				if (k>0) sql+=" UNION ALL ";
					
			}
			sql+= " ORDER BY 1";
			if (size>0) sql+= " FETCH FIRST "+size+" rows only";
			System.out.println(sql);
			fbsFootballDims = DataManagerType4.getSelectQuery(type, conn, sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fbsFootballDims;
	}
	
	public <T> List<T> getObjectListFromExcel(Class<T> type, String excelFileName, String excelTabName) throws NoSuchMethodException, SecurityException, IOException {
		List<T> objectList = DataMarshaller.getObjectListFromExcel(type,  excelFileName,  excelTabName);
		return objectList;
	}
	
	public <T> EtlResponse etl(Class<T> type
			                , String oldFileName
			                , String newFileName
			                , int keyLength
			                , String outputFileName) throws IOException, EtlException, CsvValidationException, IllegalArgumentException, IllegalAccessException, SQLException {
		EtlResponse etlResponse = new EtlResponse();
	    int totalCnt=0;
	    int insertCnt=0;
	    int updateCnt=0;
	    int deleteCnt=0;
	    
		List<T> fbsFootballTeamsInserts = null;
		List<T> fbsFootballTeamsDeletes = null;

		if (LadderComparatorService.processCsv(oldFileName
				                             , newFileName
				                             , keyLength
				                             , outputFileName)) {
			CSVReader csvReader = new CSVReader(new FileReader(outputFileName));
		    String[] values = null;
		    while ((values = csvReader.readNext()) != null) {
		    	totalCnt++;
       		    	T fbsFootball = (T) DataMarshaller.buildObjectFromList(type, values, 1);
		    	/*
		    	 * Updates are treated like inserts within SCD. 
		    	 * T2 Expire previous record for the natural key (EXPIR_TMS = N.EFF_TMS - 1 MICROSECOND)
		    	 */
		        if (values[0].equalsIgnoreCase("I") || values[0].equalsIgnoreCase("U")) {
		        	if (values[0].equalsIgnoreCase("I")) insertCnt++;
		        	else updateCnt++;
		        	if (fbsFootballTeamsInserts==null) fbsFootballTeamsInserts = new ArrayList<>();
		        	fbsFootballTeamsInserts.add(fbsFootball);
		        }
		        else if (values[0].equalsIgnoreCase("D")) {
		        	deleteCnt++;
		        	if (fbsFootballTeamsDeletes==null) fbsFootballTeamsDeletes = new ArrayList<>();
		        	fbsFootballTeamsDeletes.add(fbsFootball);
		        }
		        else {
		        	throw new EtlException("Unrecognized type encountered: "+ values[0]);
		        }
		    }		
		}
		else {
			throw new EtlException("Ladder comparison failed");
		}
		
		etlResponse.setTotalCnt(totalCnt);
		etlResponse.setInsertCnt(insertCnt);
		etlResponse.setUpdateCnt(updateCnt);
		etlResponse.setDeleteCnt(deleteCnt);
		
		if (fbsFootballTeamsInserts!=null) {
			int icnt = this.insertAll(fbsFootballTeamsInserts);
			etlResponse.setInsertUpdateAppliedCnt(icnt);
		}
		
		if (fbsFootballTeamsDeletes!=null)
			etlResponse.setDeleteAppliedCnt(this.delete(fbsFootballTeamsDeletes));
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/downloadFile/")
                .path(outputFileName.substring(outputFileName.lastIndexOf('/') + 1))
                .toUriString();
		
		etlResponse.setDeltaFile(fileDownloadUri);
		
		return etlResponse;
	}
	
	public <T> EtlResponse deltaApply(Class<T> type, String deltaFileName) throws IOException, EtlException,
	CsvValidationException, IllegalArgumentException, IllegalAccessException, SQLException {
		return deltaApply(type, deltaFileName, null, null, null);
	}
	
	public <T> EtlResponse deltaApply(Class<T> type, String deltaFileName
			, String editorInet
			, String exceptionCd
	        , String exceptionReasonTxt
			) throws IOException, EtlException, CsvValidationException, IllegalArgumentException, IllegalAccessException, SQLException {
		EtlResponse etlResponse = new EtlResponse();
		int totalCnt = 0;
		int insertCnt = 0;
		int updateCnt = 0;
		int deleteCnt = 0;

		List<T> fbsFootballTeamsInserts = null;
		List<T> fbsFootballTeamsDeletes = null;

		CSVReader csvReader = new CSVReader(new FileReader(deltaFileName));
		String[] values = null;
		while ((values = csvReader.readNext()) != null) {
			totalCnt++;
			T fbsFootball = (T) DataMarshaller.buildObjectFromList(type, values, 1);
			
			/*
			 * Very Kluge .. very temporary
			 */
			if (fbsFootball instanceof SdssStagingDim sdss) {

			    if (editorInet != null) {
			        sdss.setIntranetId(editorInet);
			    }

			    if (exceptionCd != null && (sdss.getExceptionCd()==null || sdss.getExceptionCd().trim().length()==0)) {
			        sdss.setExceptionCd(exceptionCd);
			    }

			    if (exceptionReasonTxt != null && (sdss.getExceptionCd()==null || sdss.getExceptionCd().trim().length()==0)) {
			        sdss.setExceptionReasonTxt(exceptionReasonTxt);
			    }
			}
			
			/*
			 * Updates are treated like inserts within SCD. T2 Expire previous record for
			 * the natural key (EXPIR_TMS = N.EFF_TMS - 1 MICROSECOND)
			 */
			if (values[0].equalsIgnoreCase("I") || values[0].equalsIgnoreCase("U")) {
				if (values[0].equalsIgnoreCase("I"))
					insertCnt++;
				else
					updateCnt++;
				if (fbsFootballTeamsInserts == null)
					fbsFootballTeamsInserts = new ArrayList<>();
				fbsFootballTeamsInserts.add(fbsFootball);
			} else if (values[0].equalsIgnoreCase("D")) {
				deleteCnt++;
				if (fbsFootballTeamsDeletes == null)
					fbsFootballTeamsDeletes = new ArrayList<>();
				fbsFootballTeamsDeletes.add(fbsFootball);
			} else {
				throw new EtlException("Unrecognized type encountered: " + values[0]);
			}
		}

		etlResponse.setTotalCnt(totalCnt);
		etlResponse.setInsertCnt(insertCnt);
		etlResponse.setUpdateCnt(updateCnt);
		etlResponse.setDeleteCnt(deleteCnt);

		if (fbsFootballTeamsInserts != null) {
			int icnt = this.insertAll(fbsFootballTeamsInserts);
			etlResponse.setInsertUpdateAppliedCnt(icnt);
		}

		if (fbsFootballTeamsDeletes != null) {
			/*
			 * Very Kluge .. very temporary
			 */
			if ("com.ibm.wfm.beans.sdss.SdssStagingDim".equalsIgnoreCase(type.getName())) {
				Connection conn = getConnection();
				System.out.println("Connection established");
				int actualDeleteCnt = DataManagerType4.deleteSdss(conn, fbsFootballTeamsDeletes);
				if (deleteCnt != actualDeleteCnt) {
					throw new EtlException("Actual deletion count (" + actualDeleteCnt + ") does not match the number of identified deletions ("+ deleteCnt + ").");
				}
				else
					etlResponse.setDeleteAppliedCnt(actualDeleteCnt);
				
			}
			else
				etlResponse.setDeleteAppliedCnt(this.delete(fbsFootballTeamsDeletes));
		}

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/downloadFile/")
				.path(deltaFileName.substring(deltaFileName.lastIndexOf('/') + 1)).toUriString();

		etlResponse.setDeltaFile(fileDownloadUri);

		return etlResponse;
	}
	
	public ResponseEntity<Count> countAll(Class type, Map<String, Object> pathVarMap, String filters, boolean includePit, String pit, boolean edsOnly) {
		
		//this.t = type;
		List<Count> counts = null;
		try {
			filters = URLDecoder.decode(filters, "UTF-8");
			filters = filters.replace("*", "%");
			String xNm = null;
			String dimStr = "_DIM";
			if (xNm==null) {
				
				DbTable dbTable = (DbTable)type.getAnnotation(DbTable.class);
				if (dbTable!=null && dbTable.baseTableName().length()>0) {
					if (!dbTable.isDimension()) dimStr="";
					if (edsOnly) {
						xNm = dbTable.baseTableName()+"_EDS_DIM_V";
						if (pit!=null) xNm = dbTable.baseTableName()+"_EDS_SCD_V";
					}
					else {
						
						xNm = dbTable.baseTableName()+ dimStr+"_V";
						if (dbTable.useTable()) xNm = dbTable.baseTableName();
						if (pit!=null) xNm = dbTable.baseTableName()+"_SCD_V";
					}
				}
			}

			Connection conn = getConnection();
			System.out.println("Connection established xx");
			
			int i=0;
			String selectClause = "";
			for (Field field : type.getDeclaredFields()) {
				DbColumn column = field.getAnnotation(DbColumn.class);
				if (column!=null) {
					if (!(pit==null && column.isScd())) {
						if (edsOnly && column.isExtension()) {}
						else {
							if(++i>1) selectClause+=",";
							selectClause+=column.columnName();
						}
					}
				}
			} //end - for (Field field : type.getDeclaredFields())
			
			String sql = "SELECT COUNT(*) AS CNT FROM "+ xNm + " WHERE 1=1";
			
			if (pathVarMap!=null) {
				for (Map.Entry<String,Object> entry : pathVarMap.entrySet()) {
					String quote="";
					if ( entry.getValue() instanceof String) quote="'";
					
					sql+= " AND "+entry.getKey().toUpperCase().replaceAll("-", "_") +"=" +quote+ entry.getValue()+quote;
				}
			}
			
			if (filters.length()>0) {
				sql+=" AND "+filters.replace("%3D", "="); //Bad - SQL Injection
			}
			if (pit!=null && pit.trim().length()>0 && !pit.equalsIgnoreCase("all")) {
				String delim = pit.toUpperCase().startsWith("CURRENT TIMESTAMP")?"":"'";
				sql+=" AND "+delim+pit+delim+" BETWEEN EFF_TMS AND EXPIR_TMS";
			}

			System.out.println("x "+sql);
			
			counts = DataManagerType4.getSelectQuery(Count.class, conn, sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (counts==null || counts.size()==0) {
			ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.ok().body(counts.get(0));
	}
	

	public <T> ResponseEntity<Integer> idlExcel(Class type, MultipartFile excelFileNm, String tabNm) throws IllegalArgumentException, IllegalAccessException, SQLException {
		String excelFileName = fileStorageProperties.getUploadDir()+"/"+fileStorageService.storeFile(excelFileNm);
		List<Object> ebuXrefList = DataMarshaller.buildObjectListFromExcel(type.getCanonicalName(), excelFileName,  tabNm);
		
		// Casting List<Object> to List<AnotherObject>
        List<T> ebuXrefs = Helpers.castList(ebuXrefList, type);
        
        int insertCnt = this.insertAll(ebuXrefs);
        
		return ResponseEntity.ok().body(insertCnt);
	}
	
	public ResponseEntity<Integer> putUpdateQuery(String sql, Object... params) throws SQLException {
		Connection conn = getConnection();
		System.out.println("Connection established");
		System.out.println("sql: "+sql);
		System.out.println("params: "+Arrays.toString(params));
		int insertCnt = DataManagerType4.putUpdateQuery(conn, sql, params);
		conn.close();
		if (insertCnt>0) {
			System.out.println("Insert/update successfully completed.  Insert/update count="+String.valueOf(insertCnt));
			return ResponseEntity.ok().body(insertCnt);
		}
		else {
			System.out.println("Insert failed.");
			return ResponseEntity.badRequest().body(insertCnt);
		}
	}
	
	public int processBulkRequests(SdssBulkRequest request) {

	    if (request.getChangeIds() == null || request.getChangeIds().isEmpty()) {
	        throw new IllegalArgumentException(
	            "At least one changeId is required."
	        );
	    }

	    String action = request.getAction();

	    if ("approve".equalsIgnoreCase(action)) {

	        return approveRequests(request.getChangeIds());

	    } else if ("reject".equalsIgnoreCase(action)) {

	        if (request.getRejectionReason() == null ||
	            request.getRejectionReason().isBlank()) {

	            throw new IllegalArgumentException(
	                "A rejection reason is required when rejecting requests."
	            );
	        }

	        return rejectRequests(
	            request.getChangeIds(),
	            request.getRejectionReason()
	        );
	    }

	    throw new IllegalArgumentException(
	        "Unsupported bulk action: " + action
	    );
	}
	
	private int approveRequests(List<Long> changeIds) {
		return 1;
	}
	
	private int rejectRequests(List<Long> changeIds, String rejectionReason) {
		return 1;
	}
}
