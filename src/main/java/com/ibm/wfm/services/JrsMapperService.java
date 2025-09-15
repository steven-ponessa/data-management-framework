package com.ibm.wfm.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.JrsDim;
import com.ibm.wfm.beans.JrsMapNode;
import com.ibm.wfm.beans.JrsMapperResponse;
import com.ibm.wfm.beans.ServiceAreaMapNode;
import com.ibm.wfm.utils.FileHelpers;
import com.ibm.wfm.utils.Helpers;

public class JrsMapperService {

	private static final int PRACTICE_LVL = 0;
	private static final int SERVICE_AREA_LVL = 1;
	private static final int JRS_LVL = 2;

	private static final int PRACTICE_NM = (int) Math.pow(2, PRACTICE_LVL);
	private static final int SERVICE_AREA_NM = (int) Math.pow(2, SERVICE_AREA_LVL);
	private static final int JRS_NM = (int) Math.pow(2, JRS_LVL);

	private static final int ID_NAME = 0;
	private static final int ID_CNUM = 1;
	private static final int ID_JOB_ROLE_SPECIALTY = 2;
	private static final int ID_LABOR_GROUP = 3;
	private static final int ID_GEOGRAPHY_TYPE = 4;
	private static final int ID_GEOGRAPHY = 5;
	private static final int ID_MARKET = 6;
	private static final int ID_MARKET_REGION = 7;
	private static final int ID_COUNTRY = 8;
	private static final int ID_COMPANY = 9;
	private static final int ID_BMDIV = 10;
	private static final int ID_DIVISION = 11;
	private static final int ID_CIC_CENTER_GROUP = 12;
	private static final int ID_CIC_CENTER = 13;
	private static final int ID_GROWTH_PLATFORM = 14;
	private static final int ID_SERVICE = 15;
	private static final int ID_PRACTICE = 16;
	private static final int ID_SERVICE_AREA = 17;
	private static final int ID_SECTOR = 18;
	private static final int ID_INDUSTRY = 19;
	private static final int ID_EBU_CODE = 20;
	private static final int ID_FIN_DEPT_ID = 21;
	private static final int ID_DEPT_CAT_CODE = 22;
	private static final int ID_HR_ORG_CODE = 23;
	private static final int ID_HR_DEPT = 24;
	private static final int ID_DEPARTMENT_NAME = 25;
	private static final int ID_EMF_STATUS = 26;
	private static final int ID_BAND = 27;
	private static final int ID_BILLABLE = 28;
	private static final int ID_JR_S_SERVICE = 29;
	private static final int ID_JR_S_PRACTICE = 30;
	private static final int ID_JR_S_SERVICE_AREA = 31;
	private static final int ID_CAPACITY_GROUP = 32;
	private static final int ID_ISO_CTRY_CD = 33;
	private static final int ID_LOB_CD = 34;
	private static final int ID_FA_CD = 35;
	private static final int ID_CHRG_GRP_CD = 36;
	private static final int ID_DPT_MAJR_CD = 37;
	private static final int ID_LCL_DPT_GRP_1_NM = 38;
	private static final int ID_LCL_DPT_GRP_2_NM = 39;
	private static final int ID_LCL_DPT_GRP_3_NM = 40;
	private static final int ID_MGR_INET_EMAIL_ID = 41;
	private static final int ID_MGR_LVL2_INET_EMAIL_ID = 42;
	private static final int ID_MGR_LVL3_INET_EMAIL_ID = 43;
	private static final int ID_MGR_NOTES_EMAIL_ID = 44;
	private static final int ID_MGR_LVL2_NOTES_EMAIL_ID = 45;
	private static final int ID_MGR_LVL3_NOTES_EMAIL_ID = 46;
	private static final int ID_MGR_FLG = 47;
	private static final int ID_RSA_NOTES_EMAIL_ID = 48;
	private static final int ID_LBR_POOL_LVL_1_NM = 49;
	private static final int ID_LBR_POOL_LVL_2_NM = 50;
	private static final int ID_BMS_CTRY_CD = 51;
	private static final int ID_INET_EMAIL_ID = 52;
	private static final int ID_CTRY_CMPNY_NM = 53;

	private static ArrayList<String> emeaMarketList = new ArrayList<String>();

	private static boolean debug = false;
	private static boolean addLi = false;
	
	private List<JrsDim> jrss = null;

	public static void main(String[] args) {
		//emeaMarketList.add("Benelux");
		//emeaMarketList.add("CEE");
		emeaMarketList.add("DACH");
		emeaMarketList.add("France");
		emeaMarketList.add("Italy");
		emeaMarketList.add("MEA");
		//emeaMarketList.add("Nordic");
		emeaMarketList.add("NCEE");
		emeaMarketList.add("SPGI");
		emeaMarketList.add("UKI");

		String mapFileName = null;
		String saMapFileName = null;

		/*
		 * METRICS PEOPLE_ALL_LK_V Dempgraphic
		 */
		String dataFileName = null;
		// String keyStr = "4,5,8";
		// String keyStr = "13,14,8"; // vs. JRS *
		String keyStr = "16,17,2";

		String returnOffsetStr = "0:32";
		// String returnOffsetStr = "1,4:7";

		String delimiter = ",";

		String outputFileName = null;

		boolean validParams = true;
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-mf") || args[optind].equalsIgnoreCase("--mapfilename")) {
					mapFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-samf") || args[optind].equalsIgnoreCase("--samapfilename")) {
					saMapFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-df") || args[optind].equalsIgnoreCase("--datafilename")) {
					dataFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-d") || args[optind].equalsIgnoreCase("--delimiter")) {
					delimiter = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-k") || args[optind].equalsIgnoreCase("--keyOffsets")) {
					keyStr = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-r") || args[optind].equalsIgnoreCase("--returnOffsets")) {
					returnOffsetStr = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-of") || args[optind].equalsIgnoreCase("--outputfilename")) {
					outputFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-d") || args[optind].equalsIgnoreCase("--debug")) {
					debug = true;
				} else if (args[optind].equalsIgnoreCase("-li") || args[optind].equalsIgnoreCase("--lineitem")) {
					addLi = true;
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

		if (keyStr == null)
			validParams = false;

		if (!validParams) {
			// logger.info(msgs.getText("info.usage"));
			System.out.println("Project: wfm-dmf-ga (Microservice Spring Server)");
			System.out.println("");
			System.out.println("Usage: com.ibm.wfm.services.JrsMapperService");
			System.out.println("[-mf   | --mapfilename]    - JR/S map file name  (default: data/JRS_MAP.csv)");
			System.out.println(
					"[-samf | --samapfilename]  - Service Area map file name  (default: data/SERVICE_AREA_MAP.csv)");
			System.out.println(
					"[-df   | --datafilename]   - Demographics Data File name  (default: data/GBS_Demographics.csv)");
			System.out.println(
					"[-d    | --delimiter]      - With format csv, specifies the delimiter to use  (default: comma (,))");
			System.out.println(
					" -k    | --keyOffsets      - Comma separated integer offsets for the keys for each node level of the taxonomy. ");
			System.out.println("                             Assumes a zero based array.");
			System.out.println(
					"[-r    | --returnOffsets]  - Specify the columns from the input file to be returned with the mapped data. If ");
			System.out.println("                             omitted the entire input record is returned.");
			System.out.println(
					"                             This can be a any combination of comma separated integer offsets or ranges of offsets ");
			System.out.println(
					"                             for the attribute from the source to be returned along with the map elements. ");
			System.out.println(
					"                             Ranges are specified as the low offset to the high offset, separated by a colon (:).");
			System.out.println(
					"                             So, to return elements 5 and 15 thru 20, you'd specify -r 5,15:20 ");
			System.out.println("                             Assumes a zero based array. (default: all columns)");
			System.out.println("[-of   | --outputFileName] - Output file name (default: data/JRS_TRANSFORMATION.csv)");
			System.out.println(" ");
			System.out.println("Debug Parameters");
			System.out.println("----------------");
			System.out.println("[-d  | --debug]            - Run in debug mode (includes additional output columns)");
			System.out.println("[-li | --lineitem]         - Include input line item in output");
			System.out.println("[-h  | --help]             - Display usage parameters");
			System.out.println("[-verbose]                 - Display verbose messages (not implemented)");

			System.exit(-99);
		}

		int[] keyOffsets = parseIntegerList(keyStr);
		int[] returnOffsets = null;

		if (returnOffsetStr != null)
			returnOffsets = parseIntegerList(returnOffsetStr);

		JrsMapperService jrsMapper = new JrsMapperService();
		System.out.println("Processing started at " + new java.util.Date());

		/*
		 * if (FileHelpers.echoCsv(dataFileName, echoDataFileName)) {
		 * System.out.println("Echo ready"); try {
		 * FileHelpers.compare2Files(dataFileName, echoDataFileName); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); } }
		 */

		JrsMapperResponse jmr = jrsMapper.mapFromFile(mapFileName, saMapFileName, delimiter, dataFileName, keyOffsets,
				returnOffsets, outputFileName);

		if (jmr == null || jmr.getStatusCd() < 0)
			System.out.println("Processing failed at " + new java.util.Date());
		else
			System.out.println("Processing completed at " + new java.util.Date());

	}

	public static int[] parseIntegerList(String keyStr) {

		String[] keysString = keyStr.split(",");
		int keysStringLength = keysString.length;
		for (int i = 0; i < keysString.length; i++) {
			String minMax[] = keysString[i].split(":");
			if (minMax.length == 2) {
				keysStringLength = keysStringLength + Integer.parseInt(minMax[1]) - Integer.parseInt(minMax[0]);
			}
		}

		int[] keyOffsets = new int[keysStringLength];
		int offset = 0;
		for (int i = 0; i < keysString.length; i++) {
			String minMax[] = keysString[i].split(":");
			if (minMax.length == 2) {
				int min = Integer.parseInt(minMax[0]);
				int max = Integer.parseInt(minMax[1]);
				for (int o = min; o <= max; o++) {
					keyOffsets[offset++] = o;
				}
			} else {
				keyOffsets[offset++] = Integer.parseInt(keysString[i]);
			}
		}
		return keyOffsets;
	}

	public JrsMapperResponse mapFromFile(String mapFileName, String saMapFileName, String delimiter,
			String dataFileName, int[] keyOffsets, int[] returnOffsets, String outputFileName) {
		
		
		//Call /ed-jrs-tax/service-lines  
		//String jrsUri = "http://localhost:8080/api/v1/eds-ut-jrs-tax/jrss"; 
		String jrsUri = "http://localhost:9090/api/v1/eds-ut-jrs-tax/jrss"; 
		  
		ResponseEntity<JrsDim[]> responseEntity = new RestTemplate().getForEntity(jrsUri, JrsDim[].class);   
		jrss = Arrays.asList(responseEntity.getBody()); 
		
		System.out.println(" JRS API Call completed at "+new java.util.Date());
		
		ArrayList<JrsMapNode> jrsMap = this.buildJrsMapFromFile(mapFileName, delimiter);
		if (jrsMap == null) {
			System.out.println("JRS map failed to be created from file.");
			return null;
		}
		System.out.println(" jrsMap build completed at "+new java.util.Date());
		
		ArrayList<ServiceAreaMapNode> serviceAreaMap = this.buildServiceAreaMapFromFile(saMapFileName, delimiter);
		if (serviceAreaMap == null) {
			System.out.println("JRS map failed to be created from file.");
			return null;
		}
		System.out.println(" serviceAreaMap build completed at "+new java.util.Date());

		return this.map(dataFileName, delimiter, jrsMap, serviceAreaMap, keyOffsets, returnOffsets, outputFileName);
	}

	public ArrayList<JrsMapNode> buildJrsMapFromFile(String mapFileName, String delimiter) {
		boolean echo = false;
		String header = "";
		ArrayList<JrsMapNode> jrsMaps = null;
		ArrayList<String[]> rows = FileHelpers.getRowsFromDelimitedFile(mapFileName, delimiter);

		if (rows == null)
			return null;
		int rowCnt = 0;
		try {
		for (String[] columns : rows) {
			if (++rowCnt > 1) {

				JrsMapNode jrsMap = new JrsMapNode(columns[0]							//growthPlatformNm
												, columns[1]							//serviceLineNm
												, columns[2]							//practiceNm
												, columns[3]							//serviceAreaNm
												, columns[4]							//capacityGroupNm
												, columns[5]							//jrsNm
												, columns.length>6  ? columns[6] : ""	//newGrowthPlatformNm
												, columns.length>7  ? columns[7] : ""	//newServiceLineNm
												, columns.length>8  ? columns[8] : ""	//newPracticeNm
												, ""									//economicModelNm
												, columns.length>9  ? columns[9] : ""	//newServiceAreaNm
												, columns.length>13 ? columns[13] : ""	//newJrsNm
												, columns.length>10 ? columns[10] : ""	//practiceTypeNm
												, columns.length>11 ? columns[11] : ""	//jrsActionNm
												, columns.length>14 ? columns[14] : "");  //comments
				if (jrsMaps == null)
					jrsMaps = new ArrayList<JrsMapNode>(rows.size());
				if (!jrsMap.getNewGrowthPlatformNm().trim().equalsIgnoreCase("#N/A")
						&& !jrsMap.getNewGrowthPlatformNm().trim().equalsIgnoreCase("Leadership & Business Support"))
					jrsMaps.add(jrsMap);

			} // end - if (++rowCnt>1)
			else {
				if (echo) {
					for (int i = 0; i < columns.length; i++) {
						if (i > 0)
							header += ",";
						header += Helpers.formatCsvField(columns[i]);
					}
					header += System.lineSeparator();
				}
			}
		} // end - for (String[] columns : rows)
		}
		catch (Exception e) {
			System.out.println("ERROR buildJrsMapFromFile at row number: "+ rowCnt);
			e.printStackTrace();
		}

		if (echo) {
			try {
				FileWriter fw = new FileWriter("/Users/steve/$WFM/infinity/practice_map/JRS_MAP_V2-echo.csv");
				fw.write(header);
				for (JrsMapNode jrsMap : jrsMaps) {
					fw.write(jrsMap.echo() + System.lineSeparator());
				}
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return jrsMaps;
	}

	public ArrayList<ServiceAreaMapNode> buildServiceAreaMapFromFile(String mapFileName, String delimiter) {
		boolean echo = false;
		String header = "";
		ArrayList<ServiceAreaMapNode> serviceAreaMap = null;
		ArrayList<String[]> rows = FileHelpers.getRowsFromDelimitedFile(mapFileName, delimiter);

		if (rows == null)
			return null;
		int rowCnt = 0;
		for (String[] columns : rows) {
			if (++rowCnt > 1) {
				//System.out.println("rowCnt="+rowCnt);
				ServiceAreaMapNode serviceAreaMapNode = 
						new ServiceAreaMapNode(columns[0]								//growthPlatformNm
											, columns[1]								//serviceLineNm
											, columns[4]								//practiceNm
											, columns[5]								//serviceAreaNm
											, columns.length > 10 ? columns[10] : ""	//newServiceAreaNm
											, columns.length > 9 ? columns[9] : ""		//newPracticeNm
											, columns.length > 7 ? columns[7] : ""		//newServiceLineNm
											, columns.length > 3 ? columns[3] : "" 		//Economic Model->newEconomicModel
											, columns.length > 6 ? columns[6] : ""		//newGrowthPlatformNm
											, columns.length > 11 ? columns[11] : ""	//practiceTypeNm
											, columns.length > 12 ? columns[12] : ""	//practiceConfirmedInd
											, columns.length > 13 ? columns[13] : "");	//comments
				if (serviceAreaMap == null)
					serviceAreaMap = new ArrayList<ServiceAreaMapNode>(rows.size());
				if (!serviceAreaMapNode.getNewGrowthPlatformNm().trim().equalsIgnoreCase("#N/A")
						&& !serviceAreaMapNode.getNewGrowthPlatformNm().trim().equalsIgnoreCase("Leadership & Business Support"))
					serviceAreaMap.add(serviceAreaMapNode);

			} // end - if (++rowCnt>1)
			else {
				if (echo) {
					for (int i = 0; i < columns.length; i++) {
						if (i > 0)
							header += ",";
						header += Helpers.formatCsvField(columns[i]);
					}
					header += System.lineSeparator();
				}
			}
		} // end - for (String[] columns : rows)

		if (echo) {
			try {
				FileWriter fw = new FileWriter("/Users/steve/$WFM/infinity/practice_map/SERVICE_AREA_V2-echo.csv");
				fw.write(header);
				for (ServiceAreaMapNode serviceAreaMapNode : serviceAreaMap) {
					fw.write(serviceAreaMapNode.echo() + System.lineSeparator());
				}
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return serviceAreaMap;
	}

	private JrsMapperResponse map(String dataFileName, String delimiter, ArrayList<JrsMapNode> jrsMap,
			ArrayList<ServiceAreaMapNode> serviceAreaMap, int[] keyOffsets, int[] returnOffsets,
			String outputFileName) {
		JrsMapperResponse jrsMapperResponse = new JrsMapperResponse();

		FileReader frdr;
		try {
			frdr = new FileReader(dataFileName);

			LineNumberReader lnrdr = new LineNumberReader(frdr);
			String lineBuffer = null;

			FileWriter fw = new FileWriter(outputFileName);

			int cnt = 0;
			int jrsCnt = 0;
			int saCnt = 0;
			int pCnt = 0;
			int fullKeyCnt = 0;
			int saJrsCnt = 0;
			int pSaCnt = 0;

			int foundCnt = 0;
			int notFoundCnt = 0;
			int genericJrsCnt = 0;

			int status = 0;
			int dummyCnt = 0;

			String liHeader = "";
			if (addLi)
				liHeader = "LI,";

			boolean serviceAreaJrsServiceAreaMatch = false;

			while ((lineBuffer = lnrdr.readLine()) != null) {
				status = 0;
				String[] columns = Helpers.parseLine(lineBuffer);
				if (cnt++ == 0) {
					// write out headers based on returnOffsets and columns and JrsMapNode structure
					fw.write(liHeader + this.toStringHeader(columns, returnOffsets));
					continue;
				}
				if (cnt % 10000 == 0)
					System.out.println(cnt);
				else if (cnt % 1000 == 0)
					System.out.print(".");

				String[] keys = getBranchKeys(columns, keyOffsets);

				/*
				 * if (columns[0].equals("112691724")) { dummyCnt++; }
				 */
				serviceAreaJrsServiceAreaMatch = false;
				if (columns[ID_SERVICE_AREA].equals(columns[ID_JR_S_SERVICE_AREA]))
					serviceAreaJrsServiceAreaMatch = true;

				// 1. Match on Practice + Service Area + JR/S
				JrsMapNode jrsMapNode = findFullKey(keys, jrsMap);
				if (jrsMapNode != null) {
					fullKeyCnt++;
					foundCnt++;
					status = JrsMapperService.JRS_NM + JrsMapperService.SERVICE_AREA_NM + JrsMapperService.PRACTICE_NM;
					fw.write((addLi ? String.valueOf(cnt) + "," : "")
							+ (debug ? String.valueOf(status) + ","
									+ Helpers.formatCsvField(this.getStatusDescription(status)) + "," : "")
							+ this.toStringDetailJrsMatch(columns, returnOffsets, jrsMapNode, status,
									serviceAreaJrsServiceAreaMatch));
				}

				// 2. Match on Service Area + JR/S
				if (jrsMapNode == null) {
					jrsMapNode = findSaJrs(keys, jrsMap);
					if (jrsMapNode != null) {
						saJrsCnt++;
						foundCnt++;
						status = JrsMapperService.SERVICE_AREA_NM + JrsMapperService.JRS_NM;
						fw.write((addLi ? String.valueOf(cnt) + "," : "")
								+ (debug ? String.valueOf(status) + ","
										+ Helpers.formatCsvField(this.getStatusDescription(status)) + "," : "")
								+ this.toStringDetailJrsMatch(columns, returnOffsets, jrsMapNode, status,
										serviceAreaJrsServiceAreaMatch));
					}
				}

				// 3. Match on JR/S
				if (jrsMapNode == null) {
					jrsMapNode = findJrs(keys[2], jrsMap);
					if (jrsMapNode != null && jrsMapNode.getGrowthPlatformNm().equals("Not Applicable")) {
						genericJrsCnt++;
						jrsMapNode=null; //Reset the jrsMapNode = null so that it continues down the precidence
					}
					if (jrsMapNode != null 
							&& !jrsMapNode.getGrowthPlatformNm().equals("Not Applicable")
							) {
						jrsCnt++;
						foundCnt++;
						status = JrsMapperService.JRS_NM;
						fw.write((addLi ? String.valueOf(cnt) + "," : "")
								+ (debug ? String.valueOf(status) + ","
										+ Helpers.formatCsvField(this.getStatusDescription(status)) + "," : "")
								+ this.toStringDetailJrsMatch(columns, returnOffsets, jrsMapNode, status,
										serviceAreaJrsServiceAreaMatch));
					}
				}

				/*
				 * Precidence 4, 5, and 6 will go vs. serviceAreaMap
				 */
				// 4. Match on Practice + Service Area
				ServiceAreaMapNode serviceAreaMapNode = null;
				if (jrsMapNode == null && serviceAreaMapNode == null) {
					serviceAreaMapNode = findPSa(keys, serviceAreaMap);
					if (serviceAreaMapNode != null) {
						pSaCnt++;
						foundCnt++;
						status = JrsMapperService.PRACTICE_NM + JrsMapperService.SERVICE_AREA_NM;
						fw.write((addLi ? String.valueOf(cnt) + "," : "")
								+ (debug ? String.valueOf(status) + ","
										+ Helpers.formatCsvField(this.getStatusDescription(status)) + "," : "")
								+ this.toStringDetailServiceAreaMatch(5, columns, returnOffsets, serviceAreaMapNode,
										serviceAreaMap, status, serviceAreaJrsServiceAreaMatch));
					}
				}

				// 5. Match on Service Area
				if (jrsMapNode == null && serviceAreaMapNode == null) {
					serviceAreaMapNode = findServiceArea(keys[1], serviceAreaMap);
					if (serviceAreaMapNode != null) {
						saCnt++;
						foundCnt++;
						status = JrsMapperService.SERVICE_AREA_NM;
						/*
						 * jrsMapNode needs to be updated .. jrsNm needs to be expanded to all valid
						 * jrsNms for the found service area, separated by colon.
						 */
						fw.write((addLi ? String.valueOf(cnt) + "," : "")
								+ (debug ? String.valueOf(status) + ","
										+ Helpers.formatCsvField(this.getStatusDescription(status)) + "," : "")
								+ this.toStringDetailServiceAreaMatch(6, columns, returnOffsets, serviceAreaMapNode,
										serviceAreaMap, status, serviceAreaJrsServiceAreaMatch));
					}
				}

				// 6. Match on Practice
				if (jrsMapNode == null && serviceAreaMapNode == null) {
					serviceAreaMapNode = findPractice(keys[0], serviceAreaMap);
					if (serviceAreaMapNode != null) {
						pCnt++;
						foundCnt++;
						status = JrsMapperService.PRACTICE_NM;
						fw.write((addLi ? String.valueOf(cnt) + "," : "")
								+ (debug ? String.valueOf(status) + ","
										+ Helpers.formatCsvField(this.getStatusDescription(status)) + "," : "")
								+ this.toStringDetailPracticeMatch(7, columns, returnOffsets, serviceAreaMapNode,
										serviceAreaMap, status, serviceAreaJrsServiceAreaMatch));
					}
				}

				// 7. Not found
				if (jrsMapNode == null && serviceAreaMapNode == null) {
					notFoundCnt++;
					status = 0;
					fw.write((addLi ? String.valueOf(cnt) + "," : "")
							+ (debug ? String.valueOf(status) + ","
									+ Helpers.formatCsvField(this.getStatusDescription(status)) + "," : "")
							+ this.toStringDetailNoMatch(-1, columns, returnOffsets, status,
									serviceAreaJrsServiceAreaMatch));
				}

			}
			frdr.close();
			lnrdr.close();
			fw.close();

			System.out.println("");
			System.out.println("Match Summary");
			System.out.println("-------------");
			System.out.println("Total count                                    : "
					+ Helpers.pad(Helpers.formatInt(--cnt), 8, Helpers.PAD_LEFT));
			System.out.println("Found count                                    : "
					+ Helpers.pad(Helpers.formatInt(foundCnt), 8, Helpers.PAD_LEFT));
			System.out.println("");
			System.out.println("   Status                                                 Subtotals");
			System.out.println("   ------                                                 ---------");
			System.out.println("  1. (7) Practice + Service Area + JR/S match count      : "
					+ Helpers.pad(Helpers.formatInt(fullKeyCnt), 8, Helpers.PAD_LEFT));
			System.out.println("  2. (6) Service Area + JR/S match count                 : "
					+ Helpers.pad(Helpers.formatInt(saJrsCnt), 8, Helpers.PAD_LEFT));
			System.out.println("  3. (4) JR/S match count                                : "
					+ Helpers.pad(Helpers.formatInt(jrsCnt), 8, Helpers.PAD_LEFT));
			System.out.println("  4. (3) Practice + Service Area count                   : "
					+ Helpers.pad(Helpers.formatInt(pSaCnt), 8, Helpers.PAD_LEFT));
			System.out.println("  5. (2) Service Area match count                        : "
					+ Helpers.pad(Helpers.formatInt(saCnt), 8, Helpers.PAD_LEFT));
			System.out.println("  6. (1) Practice match count                            : "
					+ Helpers.pad(Helpers.formatInt(pCnt), 8, Helpers.PAD_LEFT));
			System.out.println("");
			System.out.println("Not found count (status=0)                    : "
					+ Helpers.pad(Helpers.formatInt(notFoundCnt), 8, Helpers.PAD_LEFT));
			System.out.println("");
			System.out.println("Tie-Outs");
			System.out.println("--------");

			boolean matchTieOut = false;
			int matchSubtotalCnt = fullKeyCnt + saJrsCnt + jrsCnt + pSaCnt + saCnt + pCnt;
			if (foundCnt == matchSubtotalCnt) {
				System.out.println("Tie-out 1: Found count = Sum of counts for all found/matched status ("
						+ Helpers.formatInt(foundCnt) + ") - Pass");
				matchTieOut = true;
			} else
				System.out.println("Tie-out 1: Found count (" + Helpers.formatInt(foundCnt)
						+ ") <> Sum of counts for all found/matched status (" + Helpers.formatInt(matchSubtotalCnt)
						+ ") - Fail");

			boolean tieOut = false;
			int tieCnt = foundCnt + notFoundCnt;
			if (cnt == tieCnt) {
				System.out.println("Tie-out 2: Total count = Sum of counts for found/matched and not found ("
						+ Helpers.formatInt(cnt) + ") - Pass");
				tieOut = true;
			} else
				System.out.println("Tie-out 2: Total coount (" + Helpers.formatInt(cnt)
						+ " <> Sum of counts for found/matched and not found (" + Helpers.formatInt(tieCnt)
						+ ") - Fail");

			System.out.println("");
			System.out.println("Generic JRS count (JRS found but is generic (GP, SL, Practice <> match)): "+ Helpers.pad(Helpers.formatInt(genericJrsCnt), 8, Helpers.PAD_LEFT));
			System.out.println("");

			jrsMapperResponse.setTotalCnt(cnt);
			jrsMapperResponse.setMatchCnt(foundCnt);
			jrsMapperResponse.setFullKeyCnt(fullKeyCnt);
			jrsMapperResponse.setSaJrsCnt(saJrsCnt);
			jrsMapperResponse.setPSaCnt(pSaCnt);
			jrsMapperResponse.setJrsMatchCnt(jrsCnt);
			jrsMapperResponse.setServiceAreaMatchCnt(saCnt);
			jrsMapperResponse.setPracticeMatchCnt(pCnt);
			jrsMapperResponse.setNotMatchedCnt(notFoundCnt);
			jrsMapperResponse.setMatchTieOut(matchTieOut);
			jrsMapperResponse.setTieOut(tieOut);

			frdr = new FileReader(outputFileName);
			lnrdr = new LineNumberReader(frdr);
			int resolvedInd = -1;
			int resolvedCnt = 0;

			int resolvedJrsChg = 0;
			int resolvedJrsNoChg = 0;
			int resolvedJrsOrgChg = 0;
			int resolvedPSa = 0;
			int resolvedSa = 0;
			int resolvedP = 0;
			int resolvedJrsBlank = 0;
			int unresJrsDel = 0;
			int unresJrsMig = 0;
			int unresJrsOos = 0;
			int unresUnknown = 0;
			int unresUnmatched = 0;
			int unresError = 0;
			int unresNeg5 = 0;
			int unresNeg6 = 0;
			int unresNeg7 = 0;
			int unresNeg8 = 0;
			int unresNeg9 = 0;
			int unresNeg10 = 0;

			int resolvedIndOffset = 0;
			if (addLi)
				resolvedIndOffset++;
			if (debug)
				resolvedIndOffset += 2;

			while ((lineBuffer = lnrdr.readLine()) != null) {
				if (resolvedCnt++ == 0) {
					continue;
				}
				String[] columns = Helpers.parseLine(lineBuffer);
				resolvedInd = Integer.parseInt(columns[resolvedIndOffset]);
				switch (resolvedInd) {
				case (1):
					resolvedJrsChg++;
					break;
				case (2):
					resolvedJrsNoChg++;
					break;
				case (3):
					resolvedJrsOrgChg++;
					break;
				case (4):
					resolvedJrsBlank++;
					break;
				case (5):
					resolvedPSa++;
					break;
				case (6):
					resolvedSa++;
					break;
				case (7):
					resolvedP++;
					break;
				case (-1):
					unresUnmatched++;
					break;
				case (-2):
					unresJrsDel++;
					break;
				case (-3):
					unresJrsMig++;
					break;
				case (-4):
					unresJrsOos++;
					break;
				case (-5):
					unresNeg5++;
					break;
				case (-6):
					unresNeg6++;
					break;
				case (-7):
					unresNeg7++;
					break;
				case (-8):
					unresNeg8++;
					break;
				case (-9):
					unresNeg9++;
					break;
				case (-10):
					unresNeg10++;
					break;	
				case (-99):
					unresError++;
					break;
				default:
					unresUnknown++;

				}
			} // end - while ((lineBuffer = lnrdr.readLine()) != null)

			System.out.println("");
			System.out.println("Resolution Summary");
			System.out.println("------------------");
			System.out.println("Resolved:");
			System.out.println(" 1 JR/S Changed                  : "
					+ Helpers.pad(Helpers.formatInt(resolvedJrsChg), 8, Helpers.PAD_LEFT));
			System.out.println(" 2 JR/S No Change                : "
					+ Helpers.pad(Helpers.formatInt(resolvedJrsNoChg), 8, Helpers.PAD_LEFT));
			System.out.println(" 3 JR/S Organization Change      : "
					+ Helpers.pad(Helpers.formatInt(resolvedJrsOrgChg), 8, Helpers.PAD_LEFT));
			System.out.println(" 4 Blank (JR/S Action)           : "
					+ Helpers.pad(Helpers.formatInt(resolvedJrsBlank), 8, Helpers.PAD_LEFT));
			System.out.println(" 5 Practice + Service Area       : "
					+ Helpers.pad(Helpers.formatInt(resolvedPSa), 8, Helpers.PAD_LEFT));
			System.out.println(" 6 Service Area                  : "
					+ Helpers.pad(Helpers.formatInt(resolvedSa), 8, Helpers.PAD_LEFT));
			System.out.println(" 7 Practice                      : "
					+ Helpers.pad(Helpers.formatInt(resolvedP), 8, Helpers.PAD_LEFT));

			System.out.println("                                    --------");
			System.out
					.println(
							"                                   " + Helpers.pad(
									Helpers.formatInt(resolvedJrsChg + resolvedJrsNoChg + resolvedJrsOrgChg
											+ resolvedJrsBlank + resolvedPSa + resolvedSa + resolvedP),
									8, Helpers.PAD_LEFT));
			System.out.println("Unresolved:");
			System.out.println("-1 No match                      : "
					+ Helpers.pad(Helpers.formatInt(unresUnmatched), 8, Helpers.PAD_LEFT));
			System.out.println("-2 JR/S Deleted                  : "
					+ Helpers.pad(Helpers.formatInt(unresJrsDel), 8, Helpers.PAD_LEFT));
			System.out.println("-3 JR/S Deleted/Migrated         : "
					+ Helpers.pad(Helpers.formatInt(unresJrsMig), 8, Helpers.PAD_LEFT));
			System.out.println("-4 Out of Scope                  : "
					+ Helpers.pad(Helpers.formatInt(unresJrsOos), 8, Helpers.PAD_LEFT));
			System.out.println("-5 Change Service Area           : "
					+ Helpers.pad(Helpers.formatInt(unresNeg5), 8, Helpers.PAD_LEFT));
			System.out.println("-6 Move to exitsting             : "
					+ Helpers.pad(Helpers.formatInt(unresNeg6), 8, Helpers.PAD_LEFT));
			System.out.println("-7 Move to net new               : "
					+ Helpers.pad(Helpers.formatInt(unresNeg7), 8, Helpers.PAD_LEFT));
			System.out.println("-8 New                           : "
					+ Helpers.pad(Helpers.formatInt(unresNeg8), 8, Helpers.PAD_LEFT));
			System.out.println("-9 Rename                        : "
					+ Helpers.pad(Helpers.formatInt(unresNeg9), 8, Helpers.PAD_LEFT));
			System.out.println("-10 Shared                       : "
					+ Helpers.pad(Helpers.formatInt(unresNeg10), 8, Helpers.PAD_LEFT));
			System.out.println("-99 Other                        : "
					+ Helpers.pad(Helpers.formatInt(unresUnknown), 8, Helpers.PAD_LEFT));
			System.out.println("   Error                         : "
					+ Helpers.pad(Helpers.formatInt(unresError), 8, Helpers.PAD_LEFT));
			System.out.println("                                    --------");
			System.out.println("                                   " + Helpers.pad(
					Helpers.formatInt(
							unresUnmatched + unresJrsDel + unresJrsMig + unresJrsOos + unresNeg5+ unresNeg6+ unresNeg7 + unresNeg8 + unresNeg9 + unresNeg10 + unresUnknown + unresError),
					8, Helpers.PAD_LEFT));
			System.out.println("");

			System.out
					.println(
							"Total                              " + Helpers.pad(
									Helpers.formatInt(resolvedJrsChg + resolvedJrsNoChg + resolvedJrsOrgChg
											+ resolvedPSa + resolvedSa + resolvedP + resolvedJrsBlank + unresUnmatched
											+ unresJrsDel + unresJrsMig + unresJrsOos+ unresNeg5+ unresNeg6+ unresNeg7 + unresNeg8 + unresNeg9 + unresNeg10 + unresUnknown + unresError),
									8, Helpers.PAD_LEFT));

			jrsMapperResponse.setStatusCd(0);
			jrsMapperResponse.setMessage("Process completed successfully.");
			jrsMapperResponse.setCompletionTime(new java.util.Date());

		} catch (IOException e) {
			e.printStackTrace();
			jrsMapperResponse.setStatusCd(-9);
			jrsMapperResponse.setMessage(e.getMessage());
			return jrsMapperResponse;
		}
		return jrsMapperResponse;
	}

	public boolean burst(String outputFileName) {
		FileReader frdr;
		LineNumberReader lnrdr;
		try {
			frdr = new FileReader(outputFileName);
			lnrdr = new LineNumberReader(frdr);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isLevelValid(int level, int status) {
		int levelValue = (int) Math.pow(2, level);
		if ((status & levelValue) == levelValue)
			return false;
		else
			return true;
	}

	public JrsMapNode findFullKey(String[] keys, ArrayList<JrsMapNode> jrsMap) {
		String fullKey = "";
		for (String key : keys)
			fullKey += key + ":";
		for (JrsMapNode jmn : jrsMap) {
			if (fullKey.equals(jmn.getFullKey()))
				return jmn;
		}
		return null;
	}

	public JrsMapNode findSaJrs(String[] keys, ArrayList<JrsMapNode> jrsMap) {
		String saJrs = keys[1] + ":" + keys[2] + ":";
		for (JrsMapNode jmn : jrsMap) {
			if (saJrs.equals(jmn.getSaJrs()))
				return jmn;
		}
		return null;
	}

	public ServiceAreaMapNode findPSa(String[] keys, ArrayList<ServiceAreaMapNode> serviceAreaMap) {
		String pSa = keys[0] + ":" + keys[1] + ":";
		int i = 0;
		for (ServiceAreaMapNode san : serviceAreaMap) {
			if (pSa.equals(san.getPSa()))
				return san;
		}
		return null;
	}

	public JrsMapNode findJrs(String jrsNm, ArrayList<JrsMapNode> jrsMap) {
		for (JrsMapNode jmn : jrsMap) {
			if (jrsNm.trim().equalsIgnoreCase(jmn.getJrsNm().trim()))
				return jmn;
		}
		return null;
	}

	public ServiceAreaMapNode findServiceArea(String serviceAreaNm, ArrayList<ServiceAreaMapNode> serviceAreaMap) {
		for (ServiceAreaMapNode san : serviceAreaMap) {
			if (serviceAreaNm.trim().equalsIgnoreCase(san.getServiceAreaNm().trim()))
				return san;
		}
		return null;
	}

	public ServiceAreaMapNode findPractice(String practiceNm, ArrayList<ServiceAreaMapNode> serviceAreaMap) {
		for (ServiceAreaMapNode san : serviceAreaMap) {
			if (practiceNm.trim().equalsIgnoreCase(san.getPracticeNm().trim()))
				return san;
		}
		return null;
	}

	public String[] getBranchKeys(String[] columns, int[] keyOffsets) {
		String[] branchKeys = new String[keyOffsets.length];
		for (int i = 0; i < keyOffsets.length; i++) {
			branchKeys[i] = columns[keyOffsets[i]];
		}
		return branchKeys;
	}

	public String toStringDataDetail(String[] columns, int[] returnOffsets) {
		StringBuffer sb = new StringBuffer();

		// Print columns from the data
		for (int offset : returnOffsets) {
			sb.append(Helpers.formatCsvField(columns[offset])).append(",");
		}

		return sb.toString();
	}

	public String toStringDetailNoMatch(int resolutionInd, String[] columns, int[] returnOffsets, int status,
			boolean serviceAreaJrsServiceAreaMatch) {
		StringBuffer sb = new StringBuffer();

		sb.append(resolutionInd).append(",");

		// Print columns from the data
		sb.append(toStringDataDetail(columns, returnOffsets));

		sb.append(Helpers
				.formatCsvField(this.getResolutionMapping(status, resolutionInd, serviceAreaJrsServiceAreaMatch)));

		// Add the JRS Map Columns
		sb.append((debug ? "," : "") + ",,,,,,,,1");
		sb.append(",");

		sb.append(",").append(getBurstId(columns));
		sb.append(System.lineSeparator());
		return sb.toString();
	}

	public String toStringDetailServiceAreaMatch(int resolutionInd, String[] columns, int[] returnOffsets,
			ServiceAreaMapNode serviceAreaMapNode, ArrayList<ServiceAreaMapNode> serviceAreaMap, int status,
			boolean serviceAreaJrsServiceAreaMatch) {
		StringBuffer sb = new StringBuffer();

		sb.append(resolutionInd).append(",");

		// Print columns from the data
		sb.append(toStringDataDetail(columns, returnOffsets));

		sb.append(Helpers
				.formatCsvField(this.getResolutionMapping(status, resolutionInd, serviceAreaJrsServiceAreaMatch)))
				.append(",");

		// Add the JRS Map Columns
		sb
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewGrowthPlatformNm())).append(",")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewServiceLineNm())).append(",")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewPracticeNm())).append(",")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewEconomicModelNm())).append(",")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewServiceAreaNm())).append(",,")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getPracticeTypeNm())).append(",")
				;

		sb.append((debug ? "," : "") + ",1");
		//sb.append(",");
		sb.append(",").append(getBurstId(columns));

		sb.append(System.lineSeparator());
		return sb.toString();
	}

	public String toStringDetailPracticeMatch(int resolutionInd, String[] columns, int[] returnOffsets,
			ServiceAreaMapNode serviceAreaMapNode, ArrayList<ServiceAreaMapNode> serviceAreaMap, int status,
			boolean serviceAreaJrsServiceAreaMatch) {
		StringBuffer sb = new StringBuffer();

		sb.append(resolutionInd).append(",");

		// Print columns from the data
		sb.append(toStringDataDetail(columns, returnOffsets));

		sb.append(Helpers
				.formatCsvField(this.getResolutionMapping(status, resolutionInd, serviceAreaJrsServiceAreaMatch)))
				.append(",");

		// Add the JRS Map Columns
		sb
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewGrowthPlatformNm())).append(",")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewServiceLineNm())).append(",")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewPracticeNm())).append(",")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getNewEconomicModelNm())).append(",,,")
				.append(Helpers.formatCsvField(serviceAreaMapNode.getPracticeTypeNm())).append(",")
				;

		sb.append((debug ? "," : "") + ",1");
		//sb.append(",");
		sb.append(",").append(getBurstId(columns));

		sb.append(System.lineSeparator());
		return sb.toString();
	}

	/*
	 * This routine writed out the detail record when a JRS match has been found
	 * (i.e., status > 3). It determines the Resolved indicator (>0 means a
	 * resolution) and whether to use the current or new JR/S name. The mapping
	 * table, being derived from pivot table, has not been implemented as a true
	 * map, where unchanged JR/S values would be present in the newJrsNm column.
	 * Therefore, the following rules have been appied.
	 * 
	 * - New JR/S is non-blank → Resolved Indicator = 1, assign recommendation with
	 * New* attributes - New JR/S is blank - JR/S Action is no change → Resolved
	 * Indicator = 2, assign recommendation with New GP, SL, Practice, SA, and
	 * current JR/S. - JR/S Action is change → Resolved Indicator = 4, assign
	 * recommendation with New GP, SL, Practice, SA, and current JR/S. (the JR/S did
	 * not change but another attribute higher in the taxonomy (e.g. Service Line,
	 * Practice) did) - JR/S Action is something else (delete/migrated (3 of 991
	 * maps) or deleted (52)) → Resolved Indicator = 0
	 */
	public String toStringDetailJrsMatch(String[] columns, int[] returnOffsets, JrsMapNode jrsMapNode, int status,
			boolean serviceAreaJrsServiceAreaMatch) {
		int resolvedInd = -1;
		String useJrsNm = null;
		// New JR/S is blank
		if (jrsMapNode.getNewJrsNm() == null || jrsMapNode.getNewJrsNm().trim().length() == 0
				|| jrsMapNode.getNewJrsNm().trim().equalsIgnoreCase("n/a")) {
			if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("no change")) {
				resolvedInd = 2;
				useJrsNm = jrsMapNode.getJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().toLowerCase().contains("lift")) {
				resolvedInd = 3;
				useJrsNm = jrsMapNode.getJrsNm();
				;
			} else if (jrsMapNode.getJrsActionNm().trim().toLowerCase().contains("n/a")) {
				resolvedInd = 4;
				useJrsNm = jrsMapNode.getJrsNm();
				;
			} else if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("deletion")) {
				resolvedInd = -2;
				useJrsNm = ""; // jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("deletion & migration")) {
				resolvedInd = -3;
				useJrsNm = ""; // jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("Out of Scope")) {
				resolvedInd = -4;
				useJrsNm = ""; // jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("Change service area")) {
				resolvedInd = -5;
				useJrsNm = jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("move to existing")) {
				resolvedInd = -6;
				useJrsNm = jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().toLowerCase().startsWith("move to net new")) {
				resolvedInd = -7;
				useJrsNm = jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("New")) {
				resolvedInd = -8;
				useJrsNm = jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("rename")) {
				resolvedInd = -9;
				useJrsNm = jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().equalsIgnoreCase("shared")) {
				resolvedInd = -10;
				useJrsNm = jrsMapNode.getNewJrsNm();
			} else if (jrsMapNode.getJrsActionNm().trim().length() == 0) {
				resolvedInd = 4; // -5;
				// useJrsNm = jrsMapNode.getNewJrsNm();
				useJrsNm = jrsMapNode.getJrsNm();
			} else {
				resolvedInd = -99;
				useJrsNm = jrsMapNode.getNewJrsNm();
			}
		}
		// New JR/S has a value
		else {
			resolvedInd = 1;
			useJrsNm = jrsMapNode.getNewJrsNm();
		}
		StringBuffer sb = new StringBuffer();

		sb.append(String.valueOf(resolvedInd)).append(",");

		// Print columns from the data
		sb.append(toStringDataDetail(columns, returnOffsets));

		sb.append(
				Helpers.formatCsvField(this.getResolutionMapping(status, resolvedInd, serviceAreaJrsServiceAreaMatch)))
				.append(",");

		// Add the JRS Map Columns
		sb.append(jrsMapNode.toString(useJrsNm, debug));

		sb.append(",1");
		
		if (useJrsNm!=null && useJrsNm.trim().length()>0)
			sb.append(",").append(getCompensation(useJrsNm));
		else 
			sb.append(",");
			
		sb.append(",").append(getBurstId(columns));
		

		sb.append(System.lineSeparator());
		return sb.toString();
	}
	
	public String getCompensation(String jrsNm) {
		//this.jrss.forEach((jrs) -> {
		for (JrsDim jrs : this.jrss) {
            if (jrs.getJrsNm().equals(jrsNm)) return "\""+jrs.getCmpnstnGrdLst()+"\"";
		}
        //});
		return "";
	}

	public String toStringHeader(String[] columns, int[] returnOffsets) {
		StringBuffer sb = new StringBuffer();

		sb.append((debug ? "Status,Status Description,Resolved Ind," : "Resolved Ind,"));

		// Print columns from the data
		for (int offset : returnOffsets) {
			sb.append(columns[offset]).append(",");
		}

		sb.append("Alignment matching based on...,");

		// Add the JRS Map Columns (static JrsMapNode method)
		sb.append(JrsMapNode.getHeader(debug));

		sb.append(",JRS_TYPE_NM,count,Applicable Band(s),burst id");

		sb.append(System.lineSeparator());
		return sb.toString();
	}

	public String getResolutionMapping(int matchStatus, int resolvedInd, boolean serviceAreaJrsServiceAreaMatch) {
		String resolutionMatching = "";
		switch (resolvedInd) {
		case (1):
		case (2):
		case (3):
		case (4):
			resolutionMatching = "JR/S match";
			if (serviceAreaJrsServiceAreaMatch)
				resolutionMatching += ", Service Area = JR/S Service Area";
			else
				resolutionMatching += ", Service Area does not equal JR/S Service Area";
			break;

		case (5):
		case (6):
			resolutionMatching = "Service Area match";
			// if (serviceAreaJrsServiceAreaMatch)
			// resolutionMatching += ", Service Area = JR/S Service Area";
			// else
			// resolutionMatching += ", Service Area does not equal JR/S Service Area";
			break;

		case (7):
			resolutionMatching = "Practice match";
			// if (serviceAreaJrsServiceAreaMatch)
			// resolutionMatching += ", Service Area = JR/S Service Area";
			// else
			// resolutionMatching += ", Service Area does not equal JR/S Service Area";
			break;

		case (-1):
		case (-2):
		case (-3):
		case (-4):
		case (-5):
		case (-6):
		case (-7):
		case (-8):
		case (-9):
		case (-99):
			if (matchStatus >= this.JRS_NM) {
				resolutionMatching = "JR/S match";
				if (serviceAreaJrsServiceAreaMatch)
					resolutionMatching += ", Service Area = JR/S Service Area";
				else
					resolutionMatching += ", Service Area does not equal JR/S Service Area";
			} else
				resolutionMatching = "No Match - manual alignment required";
			break;

		default:
			resolutionMatching = "Unknown resolution indicator (" + String.valueOf(resolvedInd) + ")";

		}
		return resolutionMatching;
	}

	public String getStatusDescription(int status) {
		String statusDesc = "";
		switch (status) {
		// PRACTICE_NM+SERVICE_AREA_NM+JRS_NM
		case (7):
			statusDesc = "Practice + Service Area + JR/S";
			break;
		// SERVICE_AREA_NM+JRS_NM
		case (6):
			statusDesc = "Service Area + JR/S";
			break;
		// PRACTICE_NM+SERVICE_AREA_NM+JRS_NM
		case (5):
			statusDesc = "Practice + JR/S";
			break;
		// PRACTICE_NM+SERVICE_AREA_NM
		case (4):
			statusDesc = "JR/S";
			break;
		// PRACTICE_NM+SERVICE_AREA_NM
		case (3):
			statusDesc = "Practice + Service Area";
			break;
		// SERVICE_AREA_NM
		case (2):
			statusDesc = "Service Area";
			break;
		// PRACTICE_NM
		case (1):
			statusDesc = "Practice";
			break;
		// No attributes matched
		case (0):
			statusDesc = "No attributes matched";
			break;
		default:
			statusDesc = "Undefined status code (" + String.valueOf(status) + ")";
		}
		return statusDesc;
	}

	public int getBurstId(String[] columns) {
		int burstId = -1;
		/* Austria */
		if      (columns[ID_COUNTRY].equals("Austria") && columns[ID_GEOGRAPHY].equals("EMEA"))          burstId = 1;
		else if (columns[ID_COUNTRY].equals("Austria") && columns[ID_GEOGRAPHY].equals("GIC and BPOD"))  burstId = 2;
		else if (columns[ID_COUNTRY].equals("Austria") && columns[ID_GEOGRAPHY].equals("CoC")) 			 burstId = 3;
		
		/* EMEA */
		else if (columns[ID_GEOGRAPHY].equals("EMEA")        && columns[ID_GEOGRAPHY_TYPE].equals("Domestic")) 						   burstId = 4;
		else if (emeaMarketList.contains(columns[ID_MARKET]) && columns[ID_GEOGRAPHY_TYPE].equals("Globally Integrated Capabilities")) burstId = 5;
		else if (emeaMarketList.contains(columns[ID_MARKET]) && columns[ID_GEOGRAPHY_TYPE].equals("BPO Delivery"))                     burstId = 6;
		else if (emeaMarketList.contains(columns[ID_MARKET]) && columns[ID_GEOGRAPHY_TYPE].equals("CSS Global Delivery"))              burstId = 7;
		else if (emeaMarketList.contains(columns[ID_MARKET]) && columns[ID_GEOGRAPHY_TYPE].equals("Sales & Support") 
				                                             && columns[ID_GEOGRAPHY].equals("CoC"))                                   burstId = 8;
		else if (emeaMarketList.contains(columns[ID_MARKET]) && columns[ID_GEOGRAPHY_TYPE].equals("Sales & Support")
				                                             && columns[ID_GEOGRAPHY].equals("SIH"))                                   burstId = 9;
		else if (emeaMarketList.contains(columns[ID_MARKET]) && columns[ID_GEOGRAPHY_TYPE].equals("Sales & Support")
				                                             && columns[ID_GEOGRAPHY].equals("WW Top"))                                burstId = 10;
		
		/* Brazil */
		else if (columns[ID_COUNTRY].equals("Brazil") && columns[ID_GEOGRAPHY_TYPE].equals("Domestic"))                                burstId = 11;
		else if (columns[ID_COUNTRY].equals("Brazil") && columns[ID_GEOGRAPHY_TYPE].equals("Globally Integrated Capabilities"))        burstId = 12;
		else if (columns[ID_COUNTRY].equals("Brazil") && columns[ID_GEOGRAPHY_TYPE].equals("BPO Deliver"))                             burstId = 13;
		else if (columns[ID_COUNTRY].equals("Brazil") && columns[ID_GEOGRAPHY_TYPE].equals("CSS Global Delivery"))                     burstId = 14;
		else if (columns[ID_COUNTRY].equals("Brazil") && columns[ID_GEOGRAPHY_TYPE].equals("Sales & Support")
				                                      && columns[ID_GEOGRAPHY].equals("CoC"))                                          burstId = 15;
		/* Americas */
		else if (columns[ID_GEOGRAPHY].equals("Americas")) burstId = 16;
		/* Japan */
		else if (columns[ID_GEOGRAPHY].equals("Japan"))    burstId = 17;
		/* APAC */
		else if (columns[ID_GEOGRAPHY].equals("APAC"))     burstId = 18;
		/* GIC */
		else if (columns[ID_GEOGRAPHY_TYPE].equals("Globally Integrated Capabilities")) {
			if ((columns[ID_MARKET].equals("Canada") || columns[ID_MARKET].equals("LA") || columns[ID_MARKET].equals("United States"))
				//SP - 2021-10-25
				//&&  ! (columns[ID_CIC_CENTER_GROUP].equals("Baton Rouge")
				//	|| columns[ID_CIC_CENTER_GROUP].equals("East Lansing")
				//	|| columns[ID_CIC_CENTER_GROUP].equals("Halifax")
				//	|| columns[ID_CIC_CENTER_GROUP].equals("Monroe")
				//	|| columns[ID_CIC_CENTER_GROUP].equals("Montreal"))
				)
				burstId = 16;
			else if (columns[ID_MARKET].equals("GCG") 
				&&  ! (columns[ID_CIC_CENTER_GROUP].equals("China Export")
					|| columns[ID_CIC_CENTER_GROUP].equals("India")
					|| columns[ID_CIC_CENTER_GROUP].equals("Philippines")))
				burstId = 18;
			else
				burstId = 19;
		} 
		else if (columns[ID_GEOGRAPHY_TYPE].equals("BPO Delivery"))
			burstId = 20;
		else if (columns[ID_GEOGRAPHY_TYPE].equals("CSS Global Delivery"))
			burstId = 21;
		else if (columns[ID_GEOGRAPHY_TYPE].equals("Sales & Support"))
			burstId = 22;

		if (burstId == -1) {
			System.out.println("Could not assign burst id, Geo Type: " + columns[ID_GEOGRAPHY_TYPE] + ", Geo: "
					+ columns[ID_GEOGRAPHY] + ", Market: " + columns[ID_MARKET] + ", country: " + columns[ID_COUNTRY]);
		}
		return burstId;
	}

}
