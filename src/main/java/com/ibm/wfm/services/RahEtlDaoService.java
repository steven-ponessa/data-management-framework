package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.EbuDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.configurations.RahEtlDatasourceProperties;

@Component
public class RahEtlDaoService extends AbstractDaoService {
	private Class t = EbuDim.class;
	private String tableNm = "METRICS.EBU_DIM";
	private String scdTableNm = "";
	
	@Autowired
	private RahEtlDatasourceProperties rahProp;
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@Override
	public Connection getConnection() {
		System.out.println("Before Connection");	
		String jdbcUrlName = rahProp.getUrl().replace("{userid}", System.getenv("rah_dao_userid"))
				.replace("{password}", System.getenv("rah_dao_password"))
				.replace("{sslCertLocation}", fileStorageProperties.getCertDir());

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
