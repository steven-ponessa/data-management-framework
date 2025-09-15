package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.DataSourceDim;
import com.ibm.wfm.configurations.BridgeDatasourceProperties;

@Component
public class DynamicSqlService extends AbstractDaoService {
	private Class t = DataSourceDim.class;
	private String tableNm = "DMF.DATA_SOURCE_DIM_V";
	private String scdTableNm = "";
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;

	@Override
	public Connection getConnection() {
		System.out.println("Before Connection");	
		String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge_dao_userid")).replace("{password}", System.getenv("bridge_dao_password"));
		Connection conn = null;
		///certs/for-api-client.kdb;SSLClientKeystash=/certs/for-api-client.sth;
	    System.setProperty("javax.net.ssl.SSLClientKeystoredb","/certs/for-api-client.kdb");
	    System.setProperty("javax.net.ssl.SSLClientKeystash","/certs/for-api-client.sth");
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
