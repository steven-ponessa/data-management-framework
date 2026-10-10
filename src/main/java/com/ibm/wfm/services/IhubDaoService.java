package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.configurations.IhubDatasourceProperties;

@Component
public class IhubDaoService extends AbstractDaoService {
	
	@Autowired
	private IhubDatasourceProperties ihubProp;
	
	@Autowired
    private Environment environment;
	
	public IhubDaoService() {

	}

	@Override
	public Connection getConnection() {
		System.out.println("Before Connection");	
		
		String[] activeProfiles = environment.getActiveProfiles();
		

		String jdbcUrlName = ihubProp.getUrl().replace("{userid}", System.getenv("ihub_userid")).replace("{password}", System.getenv("ihub_password")).replace("{sslTrustStoreLocationIHub}", System.getenv("sslTrustStoreLocationIHub"));
		
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