package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.ReftJrsDim;
import com.ibm.wfm.configurations.ErdmDatasourceProperties;

@Component
public class ErdmDaoService extends AbstractDaoService {

	private Class t = ReftJrsDim.class;
	private String tableNm = "ERDMPROD.REFT_JRS_DIM";
	private String scdTableNm = "ERDMPROD.REFT_JRS_DIM";
	
	@Autowired
	private ErdmDatasourceProperties erdmProp;
	
	public ErdmDaoService() {
		super.setT(t);
		super.setTableNm(tableNm);
		super.setScdTableNm(scdTableNm);
	}

	@Override
	public Connection getConnection() {
		System.out.println("Before Connection");	
		//String jdbcUrlName = erdmProp.getUrl().replace("{userid}", System.getenv("erdm-dao-userid")).replace("{password}", System.getenv("erdm-dao-password"));
		String jdbcUrlName = erdmProp.getUrl()
				.replace("{userid}", System.getenv("erdm-dao-userid"))
				.replace("{password}", System.getenv("erdm-dao-password"))
				.replace("{sslCertLocation}", System.getenv("sslCertLocation"))
				.replace("{sslTrustStoreLocation}", System.getenv("sslTrustStoreLocation"))
				.replace("{sslTrustStorePassword}", System.getenv("sslTrustStorePassword"))
				;
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
