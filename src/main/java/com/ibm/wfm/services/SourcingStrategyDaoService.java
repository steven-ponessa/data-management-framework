package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.SourcingStrategyDim;
import com.ibm.wfm.configurations.BridgeDatasourceProperties;

@Component
public class SourcingStrategyDaoService extends AbstractDaoService {
	private Class t = SourcingStrategyDim.class;
	private String tableNm = "REFT.SOURCING_STRATEGY_DIM_V";
	private String scdTableNm = "REFT.SOURCING_STRATEGY_SCD_V";
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;
	
	public SourcingStrategyDaoService() {
		super.setT(t);
		super.setTableNm(tableNm);
		super.setScdTableNm(scdTableNm);
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
