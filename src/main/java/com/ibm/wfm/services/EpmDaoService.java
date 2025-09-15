package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.ReftJrsDim;
import com.ibm.wfm.configurations.EpmDatasourceProperties;

@Component
public class EpmDaoService extends AbstractDaoService {
	private Class t = ReftJrsDim.class;
	private String tableNm = "EPM.GLOBAL_BUYING_GROUP";
	private String scdTableNm = "EPM.GLOBAL_BUYING_GROUP";
	
	@Autowired
	private EpmDatasourceProperties epmProp;

	public EpmDaoService() {
		super.setT(t);
		super.setTableNm(tableNm);
		super.setScdTableNm(scdTableNm);
	}
	
	@Override
	public Connection getConnection() {
		System.out.println("Before Connection");	
		String jdbcUrlName = epmProp.getUrl().replace("{userid}", System.getenv("epm-dao-userid")).replace("{password}", System.getenv("epm-dao-password"));
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
