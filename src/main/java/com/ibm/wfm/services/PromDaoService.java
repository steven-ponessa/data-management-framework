package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.prom.GrOpnsetPosTDim;
import com.ibm.wfm.beans.prom.OpnsetPosTDim;
import com.ibm.wfm.beans.prom.OpnsetTDim;
import com.ibm.wfm.configurations.PromDatasourceProperties;

@Component
public class PromDaoService extends AbstractDaoService {
	private Class t = OpnsetTDim.class;
	private String tableNm = "BCSPMPC.OPNSET_T";
	private String scdTableNm = "BCSPMPC.OPNSET_T_SCD_V";
	
	@Autowired
	private PromDatasourceProperties promProp;
	
	public PromDaoService() {
		super.setT(t);
		super.setTableNm(tableNm);
		super.setScdTableNm(scdTableNm);
	}

	/*
	 * **************************************************************************************
	 * Connects to ProM Datamart
	 * **************************************************************************************
	*/
	@Override
	public Connection getConnection() {
		System.out.println("Before Connection");

		String jdbcUrlName = promProp.getUrl().replace("{userid}", System.getenv("promdm_userid")).replace("{password}", System.getenv("promdm_password")).replace("{sslTrustStoreLocationIHub}", System.getenv("sslTrustStoreLocationIHub"));
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(jdbcUrlName);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("Connection established");
		return conn;
	}
	
	public <T> ResponseEntity<Integer> insert(T object) throws IllegalArgumentException, IllegalAccessException, SQLException {
		return  insert(object, false, null);
	}
	
	public <T> ResponseEntity<Integer> insert(T object, boolean returnGeneratedKeys, String returningColumnName) throws IllegalArgumentException, IllegalAccessException, SQLException {

		//Determine the object and, if the natural key of object is NULL, VALUES NEXT VALUE FOR BCSPMPC.OPNSET_ID_SEQ 
		
		if (object instanceof OpnsetTDim) {
			Integer opnsetId = -1;
			OpnsetTDim opnsetTDim = (OpnsetTDim) object;
			if (opnsetTDim.getOpnsetId() == null) {
				Connection conn = this.getConnection();
			    String seqSql = "VALUES NEXT VALUE FOR BCSPMPC.OPNSET_ID_SEQ";
			    try (Statement seqStmt = conn.createStatement();
			         ResultSet rs = seqStmt.executeQuery(seqSql)) {
			        if (rs.next()) {
			            opnsetId = rs.getInt(1);
			        }
			    }
			    opnsetTDim.setOpnsetId(Long.valueOf(opnsetId));
			}
			else
				opnsetId =   opnsetTDim.getOpnsetId().intValue();
			
			ResponseEntity<Integer> orgResponseInteger = super.insert(object, returnGeneratedKeys, returningColumnName);
			if (opnsetId > 0) {
				return ResponseEntity
					    .status(orgResponseInteger.getStatusCode())
					    .headers(orgResponseInteger.getHeaders())
					    .body(opnsetId);
			}
			System.out.println("@@@@@@@@@@@@@@@@@@@@ Should never get here!!!!! ");
			return orgResponseInteger;
		} 
		else if (object instanceof OpnsetPosTDim) {
			Integer opnsetPosId = -1;
			OpnsetPosTDim opnsetPosTDim = (OpnsetPosTDim) object;
			if (opnsetPosTDim.getOpnsetPosId() == null) {
				Connection conn = this.getConnection();
			    String seqSql = "VALUES NEXT VALUE FOR BCSPMPC.OPNSET_POS_ID_SEQ";
			    try (Statement seqStmt = conn.createStatement();
			         ResultSet rs = seqStmt.executeQuery(seqSql)) {
			        if (rs.next()) {
			            opnsetPosId = rs.getInt(1);
			        }
			    }
			    opnsetPosTDim.setOpnsetPosId(Long.valueOf(opnsetPosId));
			}
			else
				opnsetPosId =   opnsetPosTDim.getOpnsetPosId().intValue();
			
			ResponseEntity<Integer> orgResponseInteger = super.insert(object, returnGeneratedKeys, returningColumnName);
			if (opnsetPosId > 0) {
				return ResponseEntity
					    .status(orgResponseInteger.getStatusCode())
					    .headers(orgResponseInteger.getHeaders())
					    .body(opnsetPosId);
			}
			System.out.println("@@@@@@@@@@@@@@@@@@@@ Should never get here!!!!! ");
			return orgResponseInteger;
		}
		else if (object instanceof GrOpnsetPosTDim) {
			Integer grOpnsetPosId = -1;
			GrOpnsetPosTDim grOpnsetPosTDim = (GrOpnsetPosTDim) object;
			if (grOpnsetPosTDim.getGrOpnsetPosId() == null) {
				Connection conn = this.getConnection();
			    String seqSql = "VALUES NEXT VALUE FOR BCSPMPC.GR_OPNSET_POS_ID_SEQ";
			    try (Statement seqStmt = conn.createStatement();
			         ResultSet rs = seqStmt.executeQuery(seqSql)) {
			        if (rs.next()) {
			            grOpnsetPosId = rs.getInt(1);
			        }
			    }
			    grOpnsetPosTDim.setGrOpnsetPosId(Long.valueOf(grOpnsetPosId));
			}
			else
				grOpnsetPosId =   grOpnsetPosTDim.getGrOpnsetPosId().intValue();
			
			ResponseEntity<Integer> orgResponseInteger = super.insert(object, returnGeneratedKeys, returningColumnName);
			if (grOpnsetPosId > 0) {
				return ResponseEntity
					    .status(orgResponseInteger.getStatusCode())
					    .headers(orgResponseInteger.getHeaders())
					    .body(grOpnsetPosId);
			}
			System.out.println("@@@@@@@@@@@@@@@@@@@@ Should never get here!!!!! ");
			return orgResponseInteger;
		}		
		else {
		    //System.out.println("Type I don't care about: " + object.getClass().getName());
		    return  super.insert(object, returnGeneratedKeys, returningColumnName);
		}
	}

}
