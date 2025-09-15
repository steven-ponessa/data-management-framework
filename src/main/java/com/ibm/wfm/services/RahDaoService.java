package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.EbuDim;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.configurations.RahDatasourceProperties;

@Component
public class RahDaoService extends AbstractDaoService {
	private Class t = EbuDim.class;
	private String tableNm = "METRICS.EBU_DIM";
	private String scdTableNm = "";
	
	@Autowired
	private RahDatasourceProperties rahProp;
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@Override
	public Connection getConnection() {

		String jdbcUrlName_o = rahProp.getUrl().replace("{userid}", System.getenv("rah_dao_userid"))
				.replace("{password}", System.getenv("rah_dao_password"))
				//.replace("{sslCertLocation}", fileStorageProperties.getCertDir());
				.replace("{sslTrustStoreLocation}", System.getenv("sslTrustStoreLocation"));
		
		String jdbcUrlName = rahProp.getUrl().replace("{userid}", System.getenv("rah_dao_userid")).replace("{password}", System.getenv("rah_dao_password")).replace("{sslTrustStoreLocationIHub}", System.getenv("sslTrustStoreLocationIHub"));


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
