package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.configurations.BridgeDatasourceProperties;

@Component
public class DbmsConnectionServices {
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;
	
	private static final String DBMS_WFM_EDS = "WFM-EDS";
	
	public Connection getConnectionForDbms(String dbmsNm) {
		String jdbcUrlName = null;
		System.out.println("Before Connection");
		if (dbmsNm.equalsIgnoreCase(DbmsConnectionServices.DBMS_WFM_EDS)) jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge-dao-userid")).replace("{password}", System.getenv("bridge-dao-password"));
		
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
