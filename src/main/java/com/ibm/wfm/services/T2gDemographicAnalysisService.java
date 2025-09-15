package com.ibm.wfm.services;

import java.sql.Timestamp;

import com.ibm.wfm.utils.Helpers;

/*
 * How to process
 * --------------
 * One time and completed
 * 1. Dbeaver: gbs-demographics-0.sql
 * 
 * Weekly
 * 1. Dbeaver: gbs-demographics-movement.sql versus RAH-Production
 * 2. Export to: /Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_yyyy-MM-dd.csv
 * 3. Run 
 * 
 * Project: wfm-dmf
 * 
 * Class:   com.ibm.wfm.services.T2gDemographicAnalysisService
 * 
 * Parameters:
 *   -f0 "/Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_Baseline.csv"
 *   -f1 "/Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_yyyy-MM-dd.csv"
 *   -kl 1
 *   -o  "/Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_Updates-yyyy-MM-dd.csv"
 * 
 */
public class T2gDemographicAnalysisService {
	
	private static final int GROWTH_PLATFORM_LVL = 0;
	private static final int SERVICE_LINE_LVL = 1;
	private static final int PRACTICE_LVL = 2;

	public static final int PRACTICE_NM = (int) Math.pow(2, PRACTICE_LVL);
	public static final int SERVICE_LINE_NM = (int) Math.pow(2, SERVICE_LINE_LVL);
	public static final int GROWTH_PLATFORM_NM = (int) Math.pow(2, GROWTH_PLATFORM_LVL);
	
	public static String[] practiceMovementExceptions= {"Automation:DevSecOps & IT Automation"
			,"Blockchain:Blockchain"
			,"Cloud Advisory:Cloud Advisory"
			,"Cognitive & Analytics:AI & Analytics"
			,"Cognitive Process Automation:Automation"
			,"Connected Operations Strategy:Enterprise Strategy"
			,"Custom AMS:Custom & Exponential Tech"
			,"Customer Engagement & Design:Experience Design & Mobile"
			,"Data Platform Services:Data Services"
			,"Digital Business Strategy:Enterprise Strategy"
			,"Digital Commerce:SAP CX & Commerce"
			,"Finance:Finance Strategy & Core Process"
			,"Finance Process Services:Finance Process BPO"
			,"iGNITE (Test Innovation):Quality Engineering"
			,"Industry Core Processes:Finance Strategy & Core Process"
			,"Leadership & Support:Leadership & Support"
			,"Marketing Platforms:Adobe"
			,"Microsoft:Microsoft"
			,"Microsoft AMS:Incubating EA Managed Services"
			,"Migration Factory:Migration Factory"
			,"Mobile:Experience Design & Mobile"
			,"Oracle:Oracle"
			,"Oracle AMS:Oracle Managed Services"
			,"Procurement Process Services:Procurement BPO"
			,"Promontory Advisory Services:Promontory Financial Group (PFG)"
			,"Promontory Risk Review:Financial Crimes BPO (PRR)"
			,"Risk & Compliance:Financial Crimes BPO (PRR)"
			,"Salesforce:Salesforce"
			,"SAP:SAP"
			,"SAP AMS:SAP Managed Services"
			,"Security & Privacy:Security & Privacy"
			,"ServiceNow:ServiceNow"
			,"Shared Services:Shared Services"
			,"SIH Solutioning:SIH Solutioning"
			,"Supply Chain and Procurement:Supply Chain Strategy & Process"
			,"Talent & Transformation:Talent Transformation Strategy & Process"
			,"Talent & Transformation Process Services:Talent Transformation BPO"
			,"Talent & Transformation Strategy:Enterprise Strategy"
			,"Technology & Data Strategy:Enterprise Strategy"};

	public static void main(String[] args) {
		boolean verbose = false;
		boolean validParams = true;
		String fileName0 = "/Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_Baseline.csv";
		//String fileName0 = "/Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_2022-02-18b.csv";
		String fileName1 = "/Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_2022-02-27.csv";
		int keyLength = 1;
		String outputFileName = "/Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_Updates-2022-02-27.csv";
		//String outputFileName = "/Users/steve/$WFM/T2G-2022/demographics/data/GBS_Demographics_check_c.csv";

		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-f0") || args[optind].equalsIgnoreCase("--fileName0")) {
					fileName0 = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-f1") || args[optind].equalsIgnoreCase("--fileName1")) {
					fileName1 = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-kl") || args[optind].equalsIgnoreCase("--keyLength")) {
					keyLength = Integer.parseInt(args[++optind]);
				} else if (args[optind].equalsIgnoreCase("-o") || args[optind].equalsIgnoreCase("--output")) {
					outputFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-verbose")) {
					verbose = true;
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("-help")) {
					validParams = false;
				} else {
					// logger.info("E0001: Unknown parameter specified: " + args[optind]);
					System.out.println("E0001: Unknown parameter specified: " + args[optind]);
					validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			e.printStackTrace();
			validParams = false;
		}

		if (fileName0 == null || fileName1 == null || keyLength == -1) {
			System.out.println("Error: One or more required parameters have not been entered.");
			validParams = false;
		}

		if (!validParams) {
			// logger.info(msgs.getText("info.usage"));
			System.out.println("Usage: com.ibm.gbs.utils.LadderComparison"); // msgs.getText("Seat2PldbEntitlementEtl.usage"));
			System.out.println(" ");
			System.out.println("LadderComparison Parameters");
			System.out.println("---------------------------");
			System.out.println("[-f0 | --fileName0]  - The prior file");
			System.out.println("[-f1 | --fileName1]  - The current file");
			System.out.println("[-kl | --keyLength]  - The key length (sequential) or the number of columns that make up the key (delimited).");
			System.out.println(" ");
			System.out.println("Output Parameters");
			System.out.println("-----------------");
			System.out.println("[-o | --outputFileName]    - Output file name. (default: system output)");
			System.out.println(" ");
			System.out.println("Debug Parameters");
			System.out.println("----------------");
			System.out.println("[-h | -help]    - Display usage parameters.");
			System.out.println("[-verbose]      - Display verbose messages.");

			System.exit(-99);
		}

		System.out.println("Processing started at " + new java.util.Date());
		
		if (LadderComparatorService.processDemographicMovementCsv(fileName0, fileName1, keyLength, outputFileName))
			System.out.println("Comparison completed successfully at " + new java.util.Date());
		else
			System.out.println("Comparison failed at " + new java.util.Date());

	}
	
	public static boolean isPracticeMovementException(String fromPractice, String toPractice) {
		String target=fromPractice.trim()+":"+toPractice.trim();
		for (String practiceMovementException: practiceMovementExceptions) {
			if (practiceMovementException.equals(target)) return true;
		}
		return false;
	}

}
