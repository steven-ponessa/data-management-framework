package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.KyndrylOrganizationMarketDim;
import com.ibm.wfm.configurations.BridgeDatasourceProperties;

@Component
public class KyndrylDaoService extends AbstractDaoService {
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;
	
	public KyndrylDaoService() {

	}

	@Override
	public Connection getConnection() {
		System.out.println("Before Connection");	
		String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge-dao-userid")).replace("{password}", System.getenv("bridge-dao-password"));
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
