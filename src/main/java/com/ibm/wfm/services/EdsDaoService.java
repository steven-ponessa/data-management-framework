package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.configurations.BridgeDatasourceProperties;

@Component
public class EdsDaoService extends AbstractDaoService {
	private Class t = BrandDim.class;
	private String tableNm = "REFT.BRAND_DIM_V";
	private String scdTableNm = "REFT.BRAND_DIM_SCD_V";
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;
	
	public EdsDaoService() {
		super.setT(t);
		super.setTableNm(tableNm);
		super.setScdTableNm(scdTableNm);
	}

	@Override
	public Connection getConnection() {
		//System.out.println("Before Connection 2");	
		//System.getenv().forEach((k, v) -> System.out.println("ENV: " + k + " = " + v));
		
		String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge_dao_userid")).replace("{password}", System.getenv("bridge_dao_password"));
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

}
