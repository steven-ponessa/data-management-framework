package com.ibm.wfm.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.Certification;
import com.ibm.wfm.configurations.BridgeDatasourceProperties;
import com.ibm.wfm.configurations.Wf360DatasourceProperties;
import com.ibm.wfm.utils.DataManagerType4;

@Component
public class CertificationDaoService {
	/*
	 * A static block (runs at jre initialization) to ensure that the drivers
	 * are loaded. This is normal JDBC procedure.
	 */
	static {
		try {
			// register the driver with DriverManager
			// The newInstance() call is needed for the sample to work with
			// JDK 1.1.1 on OS/2, where the Class.forName() method does not
			// run the static initializer. For other JDKs, the newInstance
			// call can be omitted.
			//Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private Wf360DatasourceProperties wf360Prop;
	@Autowired
	private BridgeDatasourceProperties bridgeProp;

	public List<Certification> findAll() throws IOException {
		List<Certification> certifications = null;
		System.out.println("Data manager created");
		try {
			System.out.println("Before Connection");	
			Connection conn = DriverManager.getConnection(wf360Prop.getUrl().replace("{api-key}", System.getenv("wf360-api-key")));
			System.out.println("Connection established");
			System.out.println("getCeritifcation(): "+wf360Prop.getCertificationSql());
			certifications = DataManagerType4.getSelectQuery(Certification.class, conn, wf360Prop.getCertificationSql());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return certifications;
	}
	
	
	public int deleteAll() {
		System.out.println("Before Connection");	
		String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge-dao-userid")).replace("{password}", System.getenv("bridge-dao-password"));
		Connection conn;
		try {
			conn = DriverManager.getConnection(jdbcUrlName);
			System.out.println("Connection established");
			return DataManagerType4.deleteAll2Connection(conn, "CERT_DEV.CERTIFICATIONS_FACT");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int insertAll(List<Certification> certifications) throws IllegalArgumentException, IllegalAccessException {
		String jdbcUrlName = bridgeProp.getUrl().replace("{userid}", System.getenv("bridge-dao-userid")).replace("{password}", System.getenv("bridge-dao-password"));
		
		int insertCnt = -1;
		try {
			Connection conn = DriverManager.getConnection(jdbcUrlName);
			System.out.println("Connection established");
			insertCnt = DataManagerType4.insert2Connection(Certification.class, conn, "CERT_DEV.CERTIFICATIONS_FACT", certifications);
			if (insertCnt>0) {
				System.out.println("Yah");
				return insertCnt;
			}
			else {
				System.out.println("boo");
				return insertCnt;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return insertCnt;
		}
	}

}
