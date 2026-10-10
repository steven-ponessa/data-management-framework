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
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;
	
	public EdsDaoService() {

	}

	@Override
	public Connection getConnection() {
		
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
