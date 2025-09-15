package com.ibm.wfm.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.wfm.utils.FileHelpers;
import com.ibm.wfm.utils.Helpers;

public class FileBurstService {

	public static void main(String[] args) {
		String fileName = null;
		String rootDirectory = null;
		String targetsStr = null;
		String fileNameTemplate = null;
		String delimiter = ",";
		String version = null;
		boolean appendDate = true;
		boolean debug = false;
		boolean verbose = false;
		
		boolean validParams = true;
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-f") || args[optind].equalsIgnoreCase("--filename")) {
					fileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-r") || args[optind].equalsIgnoreCase("--rootdirectory")) {
					rootDirectory = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-t") || args[optind].equalsIgnoreCase("--targetsStr")) {
					targetsStr = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-fnt") || args[optind].equalsIgnoreCase("--fileNameTemplate")) {
					fileNameTemplate = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-d") || args[optind].equalsIgnoreCase("--delimiter")) {
					delimiter = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-v") || args[optind].equalsIgnoreCase("--version")) {
					version = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-debug") || args[optind].equalsIgnoreCase("--debug")) {
					debug = false;
				} else if (args[optind].equalsIgnoreCase("-verbose") || args[optind].equalsIgnoreCase("--verbose")) {
					verbose = false;
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("--help")) {
					validParams = false;
				} else if (args[optind].equalsIgnoreCase("--rootDirectoryLocal") || args[optind].equalsIgnoreCase("--rootDirectoryBox")) {
					System.out.println("W0001: Ignored parameter: " + args[optind]+", value="+ args[++optind]+".");
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
		
		
		if (fileName==null || fileNameTemplate==null || targetsStr==null) {
			System.out.println("E0002: File Name (-f), target directory string (-t), and File Name Template (-fnt) are both required.");
			validParams = false;
		}
		
		if (!validParams) {
			// logger.info(msgs.getText("info.usage"));
			System.out.println("Project: wfm-dmf-ga (Microservice Spring Server)");
			System.out.println("");
			System.out.println("Usage: com.ibm.wfm.services.FileBurstService");
			System.out.println("[-f   | --filename]    - File name to be split");
			System.out.println("...)");

			System.out.println(" ");
			System.out.println("Debug Parameters");
			System.out.println("----------------");
			System.out.println("[-d  | --debug]            - Run in debug mode (includes additional output columns)");
			System.out.println("[-h  | --help]             - Display usage parameters");
			System.out.println("[-verbose]                 - Display verbose messages (not implemented)");

			System.exit(-99);
		}
		
		String[] targets = targetsStr.split(",");
		FileBurstService fileBurst = new FileBurstService();
		System.out.println("Processing started at " + new java.util.Date());
		
		if (fileBurst.burst(fileName, rootDirectory, targets, fileNameTemplate, appendDate)) {
			System.out.println("Processing completed at " + new java.util.Date());
		}
		else {
			System.out.println("Processing failed at " + new java.util.Date());
		}

	}
	
	public boolean burst(String fileName, String rootDirectory, String[] targets, String fileNameTemplate, boolean appendDate) {
		FileReader frdr;
		try {
			int burstId = -1;
			
			String extension = "csv"; //"txt"
			
			frdr = new FileReader(fileName);
			
			String date = "";
			if (appendDate) {
				String pattern = "yyyy-MM-dd";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	
				date = "-"+simpleDateFormat.format(new Date());
			}
			
			FileWriter[] fw = new FileWriter[targets.length];
			int[] outCnt = new int[targets.length];

			LineNumberReader lnrdr = new LineNumberReader(frdr);
			String lineBuffer = null;
			int cnt = 0;
			String directory=null;
			while ((lineBuffer = lnrdr.readLine()) != null) {
				if (cnt++==0) {
					//write out headers
					for (int i=0; i<targets.length; i++) {
						directory=rootDirectory+"/"+targets[i];
						FileHelpers.makeDirectory(directory);
						fw[i] = new FileWriter(directory+"/"+fileNameTemplate+date+"." + extension);
						fw[i].write(lineBuffer+System.lineSeparator());
						outCnt[i]=0;
					}
					continue;
				}
				String[] columns = Helpers.parseLine(lineBuffer);
				
				burstId = Integer.parseInt(columns[columns.length-1]);
				burstId--;
				fw[burstId].write(lineBuffer+System.lineSeparator());
				outCnt[burstId]++;
			}
			frdr.close();
			for (int i=0; i<targets.length; i++) {
				fw[i].flush();
				fw[i].close();
			}
			
			System.out.println("Input File Count: "+Helpers.pad(Helpers.formatInt(cnt),8,Helpers.PAD_LEFT));
			int outTotal=0;
			for (int i=0; i<outCnt.length; i++) {
				System.out.println((i<10?" ":"")+i+".               "+Helpers.pad(Helpers.formatInt(outCnt[i]),8,Helpers.PAD_LEFT));
				outTotal+= outCnt[i];
			}
			System.out.println("                  --------");
			System.out.println("                  "+Helpers.pad(Helpers.formatInt(outTotal),8,Helpers.PAD_LEFT));
			
		} catch (IOException e) {
			e.printStackTrace();

			return false;
		}	
		return true;
	}

}
