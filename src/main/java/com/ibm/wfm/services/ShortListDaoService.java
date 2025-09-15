package com.ibm.wfm.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.GbsShortListDim;
import com.ibm.wfm.configurations.BridgeDatasourceProperties;
import com.ibm.wfm.utils.DataMarshaller;

@Component
public class ShortListDaoService extends AbstractDaoService {

	private Class t = GbsShortListDim.class;
	private String tableNm = "REFT.GBS_SHORT_LIST_DIM_V";
	private String scdTableNm = "REFT.GBS_SHORT_LIST_SCD_V";
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;
	
	public ShortListDaoService() {
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
	
	/* deprecated */
	public List<GbsShortListDim> getShortListFromExcel(String excelFileName, String excelTabName) throws NoSuchMethodException, SecurityException, IOException {
		List<GbsShortListDim> gbsJrsList = DataMarshaller.getObjectListFromExcel(GbsShortListDim.class,  excelFileName,  excelTabName);
		return gbsJrsList;
	}

}
