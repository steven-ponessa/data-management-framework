package com.ibm.wfm.services;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.ibm.wfm.utils.Helpers;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Component
public class LadderComparatorService {

	public static void main(String[] args) {
		boolean verbose = false;
		boolean validParams = true;
		String fileName0 = null;
		String fileName1 = null;
		int keyLength = 1;
		String outputFileName = null;

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
		
		                                                                //Required Timestamp format
		                                                                //yyyy-mm-dd hh:mm:ss[.fffffffff]
		Timestamp ts = Timestamp.valueOf("2021-06-23 22:07:18.611143"); //will not parse: 2021-06-23-22.07.18.611143
		java.sql.Date sql_dt = java.sql.Date.valueOf("9999-12-31");
		//java.util.Date util_dt = java.util.Date.valueOf("9999-12-31"); //there is not valueOf in java.util.Date
		
		System.out.println("Timestamp="+ts);
		System.out.println("Date="+sql_dt);

		if (LadderComparatorService.processCsv(fileName0, fileName1, keyLength, outputFileName))
			System.out.println("Comparison completed successfully at " + new java.util.Date());
		else
			System.out.println("Comparison failed at " + new java.util.Date());
	}
	
	public static int compareArrays(String[] array0, String[] array1, int numKeys) {
		int match=0;
		for (int i=0; i<numKeys; i++) {
			if (i==array0.length) return 1;
			if (i==array1.length) return -1;
			if (array0[i].compareTo(array1[i])!=0) return array0[i].compareTo(array1[i]);
		}
		return match;
	}

	public static boolean processCsv(String fileName0, String fileName1, int keyLength, String outputFileName) {
		try {
		    BufferedWriter writer = null;
		    if (outputFileName!=null) writer = new BufferedWriter(new FileWriter(outputFileName));
		    else  writer = new BufferedWriter(new OutputStreamWriter(System.out));
			
			boolean eof0 = false;
			boolean eof1 = false;
			String[] lineArray0;
			String[] lineArray1;
		    
			/* Define file readers */
			CSVReader csvReader0 = null;
			if (fileName0==null) {
				eof0 = true;
				lineArray0 = null;
			}
			else {
				csvReader0 = new CSVReader(new FileReader(fileName0));
				lineArray0 = csvReader0.readNext();
				lineArray0 = csvReader0.readNext();
				if (lineArray0 == null) eof0 = true;
			}
			
			CSVReader csvReader1 = new CSVReader(new FileReader(fileName1));
			lineArray1 = csvReader1.readNext();
			lineArray1 = csvReader1.readNext();
			if (lineArray1 == null) eof1 = true;
			
			int matchKey = -1;
			int matchBody = -1;
			int lineCnt=0;

			while (!eof0 && !eof1) {
				matchKey = compareArrays(lineArray0,lineArray1,keyLength);
				// If the keys match
				if (matchKey == 0) {
					matchBody = compareArrays(lineArray0,lineArray1,java.lang.Math.max(lineArray0.length,lineArray1.length));
					if (matchBody != 0) {
						writer.write((lineCnt++>0?System.lineSeparator():"")+"U," + Helpers.toCsvLine(lineArray1));
					}
					// Descend both rungs
					lineArray0 = csvReader0.readNext();
					if (lineArray0 == null) eof0 = true;

					lineArray1 = csvReader1.readNext();
					if (lineArray1 == null) eof1 = true;
				}
				// New file's key is < old
				else if (matchKey < 0) {
					// Write old as delete; read from old dataset;
					writer.write((lineCnt++>0?System.lineSeparator():"")+"D," + Helpers.toCsvLine(lineArray0));
					lineArray0 = csvReader0.readNext();
					if (lineArray0 == null) eof0 = true;
				}
				// New file's key is > old
				else {
					// Write new as insert; read from new dataset;
					writer.write((lineCnt++>0?System.lineSeparator():"")+"I," + Helpers.toCsvLine(lineArray1));
					lineArray1 = csvReader1.readNext();
					if (lineArray1 == null) eof1 = true;
				}

			}

			if (!eof0) {
				writer.write((lineCnt++>0?System.lineSeparator():"")+"D," + Helpers.toCsvLine(lineArray0));
				while ((lineArray0 = csvReader0.readNext()) != null) {
					writer.write((lineCnt++>0?System.lineSeparator():"")+"D," + Helpers.toCsvLine(lineArray0));
				}
			}
			if (!eof1) {
				writer.write((lineCnt++>0?System.lineSeparator():"")+"I," + Helpers.toCsvLine(lineArray1));
				while ((lineArray1 = csvReader1.readNext()) != null) {
					writer.write((lineCnt++>0?System.lineSeparator():"")+"I," + Helpers.toCsvLine(lineArray1));
				}
			}
			
			if (csvReader0!=null) csvReader0.close();
			csvReader1.close();
		    
		    writer.close();

		} catch (FileNotFoundException | CsvValidationException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean process(String fileName0, String fileName1, int keyLength, String outputFileName) {
		try {
			/* Define file readers */
			FileReader frdr0 = new FileReader(fileName0);
			FileReader frdr1 = new FileReader(fileName1);
			/* Prime the loop */
			LineNumberReader lnrdr0 = new LineNumberReader(frdr0);
			LineNumberReader lnrdr1 = new LineNumberReader(frdr1);

			String key0 = null;
			String key1 = null;
			String body0 = null;
			String body1 = null;
			boolean eof0 = false;
			boolean eof1 = false;
			String lineBuffer0 = null;
			String lineBuffer1 = null;

			lineBuffer0 = lnrdr0.readLine();
			lineBuffer0 = lnrdr0.readLine();
			if (lineBuffer0 == null)
				eof0 = true;
			else {
				key0 = lineBuffer0.substring(0, keyLength);
				body0 = lineBuffer0.substring(keyLength);
			}
			lineBuffer1 = lnrdr1.readLine();
			lineBuffer1 = lnrdr1.readLine();
			if (lineBuffer1 == null)
				eof1 = true;
			else {
				key1 = lineBuffer1.substring(0, keyLength);
				body1 = lineBuffer1.substring(keyLength);
			}
			int matchKey = -1;
			int matchBody = -1;

			while (!eof0 && !eof1) {
				matchKey = key0.compareTo(key1);
				// If the keys match
				if (matchKey == 0) {
					matchBody = body0.compareTo(body1);
					if (matchBody != 0) {
						System.out.println("U," + key1 + body1);
					}
					// Descend both rungs
					lineBuffer0 = lnrdr0.readLine();
					if (lineBuffer0 == null)
						eof0 = true;
					else {
						key0 = lineBuffer0.substring(0, keyLength);
						body0 = lineBuffer0.substring(keyLength);
					}
					lineBuffer1 = lnrdr1.readLine();
					if (lineBuffer1 == null)
						eof1 = true;
					else {
						key1 = lineBuffer1.substring(0, keyLength);
						body1 = lineBuffer1.substring(keyLength);
					}
				}
				// New file's key is < old
				else if (matchKey < 0) {
					// Write old as delete; read from old dataset;
					System.out.println("D," + key0 + body0);
					lineBuffer0 = lnrdr0.readLine();
					if (lineBuffer0 == null)
						eof0 = true;
					else {
						key0 = lineBuffer0.substring(0, keyLength);
						body0 = lineBuffer0.substring(keyLength);
					}
				}
				// New file's key is > old
				else {
					// Write new as insert; read from new dataset;
					System.out.println("I," + key1 + body1);
					lineBuffer1 = lnrdr1.readLine();
					if (lineBuffer1 == null)
						eof1 = true;
					else {
						key1 = lineBuffer1.substring(0, keyLength);
						body1 = lineBuffer1.substring(keyLength);
					}
				}

			}

			if (!eof0) {
				System.out.println("D," + lineBuffer0);
				while ((lineBuffer0 = lnrdr0.readLine()) != null) {
					System.out.println("D," + lineBuffer0);
				}
			}
			if (!eof1) {
				System.out.println("I," + lineBuffer1);
				while ((lineBuffer1 = lnrdr1.readLine()) != null) {
					System.out.println("I," + lineBuffer1);
				}
			}

			lnrdr0.close();
			lnrdr1.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean processDemographicMovementCsv(String fileName0, String fileName1, int keyLength, String outputFileName) {
		try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
			
			boolean eof0 = false;
			boolean eof1 = false;
			String[] headerArray0=null;
			String[] headerArray1;
			String[] lineArray0;
			String[] lineArray1;
		    
			/* Define file readers */
			CSVReader csvReader0 = null;
			if (fileName0==null) {
				eof0 = true;
				lineArray0 = null;
			}
			else {
				csvReader0 = new CSVReader(new FileReader(fileName0));
				headerArray0 = csvReader0.readNext();
				lineArray0 = csvReader0.readNext();
				if (lineArray0 == null) eof0 = true;
			}
			
			CSVReader csvReader1 = new CSVReader(new FileReader(fileName1));
			headerArray1 = csvReader1.readNext();
			lineArray1 = csvReader1.readNext();
			if (lineArray1 == null) eof1 = true;
			
			writer.write("Status,NM-GP,NM-SL,NM-P,Cnt,Action,"+Helpers.toCsvLine(headerArray0)+","+Helpers.toCsvLine(headerArray1)+System.lineSeparator());
			
			int matchKey = -1;
			int matchBody = -1;
			int lineCnt=0;

			while (!eof0 && !eof1) {
				matchKey = compareArrays(lineArray0,lineArray1,keyLength);
				// If the keys match
				if (matchKey == 0) {
					matchBody = compareArraysX(lineArray0,lineArray1,java.lang.Math.max(lineArray0.length,lineArray1.length));
					if (matchBody != 0) {
						writer.write((lineCnt++>0?System.lineSeparator():"")+matchBody+","
								+((Helpers.isSwitchOn(T2gDemographicAnalysisService.GROWTH_PLATFORM_NM, matchBody)?1:0))+","
						        +((Helpers.isSwitchOn(T2gDemographicAnalysisService.SERVICE_LINE_NM, matchBody)?1:0))+","
						        +((Helpers.isSwitchOn(T2gDemographicAnalysisService.PRACTICE_NM, matchBody)?1:0))+","
								+"1,U," + Helpers.toCsvLine(lineArray0) + "," + Helpers.toCsvLine(lineArray1));
					}
					else {
						writer.write((lineCnt++>0?System.lineSeparator():"")+matchBody+","
								+((Helpers.isSwitchOn(T2gDemographicAnalysisService.GROWTH_PLATFORM_NM, matchBody)?1:0))+","
						        +((Helpers.isSwitchOn(T2gDemographicAnalysisService.SERVICE_LINE_NM, matchBody)?1:0))+","
						        +((Helpers.isSwitchOn(T2gDemographicAnalysisService.PRACTICE_NM, matchBody)?1:0))+","
								+"1,M," + Helpers.toCsvLine(lineArray0) + "," + Helpers.toCsvLine(lineArray1));
					}
					// Descend both rungs
					lineArray0 = csvReader0.readNext();
					if (lineArray0 == null) eof0 = true;

					lineArray1 = csvReader1.readNext();
					if (lineArray1 == null) eof1 = true;
				}
				// New file's key is < old
				else if (matchKey < 0) {
					// Write old as delete; read from old dataset;
					writer.write((lineCnt++>0?System.lineSeparator():"")+"-16,0,0,0,1,D," + Helpers.toCsvLine(lineArray0));
					lineArray0 = csvReader0.readNext();
					if (lineArray0 == null) eof0 = true;
				}
				// New file's key is > old
				else {
					// Write new as insert; read from new dataset;
					writer.write((lineCnt++>0?System.lineSeparator():"")+"-8,0,0,0,1,I," + Helpers.toCsvLine(lineArray1));
					lineArray1 = csvReader1.readNext();
					if (lineArray1 == null) eof1 = true;
				}

			}

			if (!eof0) {
				writer.write((lineCnt++>0?System.lineSeparator():"")+"-16,0,0,0,1,D," + Helpers.toCsvLine(lineArray0));
				while ((lineArray0 = csvReader0.readNext()) != null) {
					writer.write((lineCnt++>0?System.lineSeparator():"")+"-16,0,0,0,1,D," + Helpers.toCsvLine(lineArray0));
				}
			}
			if (!eof1) {
				writer.write((lineCnt++>0?System.lineSeparator():"")+"-8,0,0,0,1,I," + Helpers.toCsvLine(lineArray1));
				while ((lineArray1 = csvReader1.readNext()) != null) {
					writer.write((lineCnt++>0?System.lineSeparator():"")+"-8,0,0,0,1,I," + Helpers.toCsvLine(lineArray1));
				}
			}
			
			if (csvReader0!=null) csvReader0.close();
			csvReader1.close();
		    
		    writer.close();

		} catch (FileNotFoundException | CsvValidationException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static int compareArraysX(String[] array0, String[] array1, int numKeys) {
		int growthPlatformIndex = 4;
		int serviceLineIndex = 5;
		int practiceIndex = 6;
		int match=0;
		if (!(array0[growthPlatformIndex].equals(array1[growthPlatformIndex]))) match+=T2gDemographicAnalysisService.GROWTH_PLATFORM_NM;
		if (!(array0[serviceLineIndex].equals(array1[serviceLineIndex]))) match+=T2gDemographicAnalysisService.SERVICE_LINE_NM;
		if (!(array0[practiceIndex].equals(array1[practiceIndex]))) {
			if (!(T2gDemographicAnalysisService.isPracticeMovementException(array0[practiceIndex],array1[practiceIndex])))
				match+=T2gDemographicAnalysisService.PRACTICE_NM;
		}
		return match;
	}	

}
