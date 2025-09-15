package com.ibm.wfm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.FutureSkill;
import com.ibm.wfm.configurations.Wf360DatasourceProperties;

@Component
public class Wf360FutureSkillDaoService extends AbstractDaoService {
	
	private Class t = FutureSkill.class;
	private String tableNm = "WF360_HR.FACT_TRAINTRACK_PSN_LKP_IBM";
	private String scdTableNm = "WF360_HR.FACT_TRAINTRACK_PSN_LKP_IBM";
	
	@Autowired
	private Wf360DatasourceProperties wf360Prop;
	
	public Wf360FutureSkillDaoService() {
		super.setT(t);
		super.setTableNm(tableNm);
		super.setScdTableNm(scdTableNm);
	}

	@Override
	public Connection getConnection() {
		System.out.println("Data manager created");
		Connection conn = null;
		try {
			System.out.println("Before Connection");	
			conn = DriverManager.getConnection(wf360Prop.getUrl().replace("{api-key}", System.getenv("wf360-api-key")));
			System.out.println("Connection established");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

}
