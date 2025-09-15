package com.ibm.wfm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ZosDb2Tester {

	public static void main(String[] args) {
		String userid = null;
		String password = null;
		boolean verbose = false;
		boolean validParams = true;
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-u") || args[optind].equalsIgnoreCase("--userid")) {
					userid = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-p") || args[optind].equalsIgnoreCase("--password")) {
					password = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-v") || args[optind].equalsIgnoreCase("--verbose")) {
					verbose = true;
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("--help")) {
					validParams = false;
				} else {
					// logger.info("E0001: Unknown parameter specified: " + args[optind]);
					System.out.println("E0001: Unknown parameter specified: " + args[optind]);
					validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			// logger.error(msgs.getText("error.e0001", e.getMessage()));
			e.printStackTrace();
			validParams = false;
		}
		 if (userid==null || password== null)
			 validParams = false;

		if (!validParams) {
			// logger.info(msgs.getText("info.usage"));
			System.out.println("Put usage here"); // msgs.getText("Seat2PldbEntitlementEtl.usage"));
			System.exit(-99);
		}
		
		
		String jdbcUrlName = "jdbc:db2://bldbmsa.boulder.ibm.com:5031/MWNCDSNB:sslConnection=true;";
		jdbcUrlName = "jdbc:db2://bldbmsa.boulder.ibm.com:5508/MWNCDSNB:sslConnection=true;";
		Map<String, String> jdbcParameters = new HashMap<String, String>();
		jdbcParameters.put("db2.jcc.sslConnection", "true");
		
		DataManagerType4 dm4 = new DataManagerType4(jdbcUrlName, jdbcParameters);
		try {
			Connection conn = dm4.connect(userid, password);
			System.out.println("Connection established");
			
			String[] markets = {
					//"2A",
					//"2K",
					"2S"};
					//"2Z",
					//"6G"};

					/*
					"3B",
					"3C",
					"3D",
					"3F",
					"3I",
					"3K",
					"3M",
					"3N",
					"7A",

					"2J"};
					*/
			String geoCd = "TEMP";
			String sql = "SELECT * FROM UTILDM.EMF_UTIL_V A INNER JOIN BMSIW.GEO_RGN_CTRY_V B ON A.CTRY_CD = B.CTRY_CD WHERE EXPIR_DT>'2017-12-31' AND A.LOB_CD IN ('BIS', 'BTO', 'PRO') AND B.RGN_LVL2_CD=?"
					//+ " FETCH FIRST 10 ROWS ONLY"
					;
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			for (int i=0; i<markets.length; i++) {
				System.out.println(i+" of "+markets.length+". "+markets[i] + " started at: "+ new java.util.Date());
				preparedStatement.setString(1, markets[i]);
				
				OutputStream outputStream = new FileOutputStream("/Users/steve/temp/northstar-emf-util/emf-util-"+geoCd+"-"+markets[i]+".csv");
				
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
	
				ResultSet rst = preparedStatement.executeQuery();
				
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(rst));
                csvPrinter.printRecords(rst);
				//while (rst.next()) {
				//	
				//}
				rst.close();
				writer.close();
				outputStream.close();
				System.out.println(i+" of "+markets.length+". "+markets[i] + " completed at: "+ new java.util.Date());
			}
			conn.close();
			System.out.println("Process successfully completed at: "+ new java.util.Date());
		} catch (SQLException | IOException se) {
			System.out.println("Error: " + se.getMessage());
			System.out.println(se);
			se.printStackTrace();
			return;
		}
	}

}
