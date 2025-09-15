package com.ibm.wfm.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FormatTaxonomyJson {

	public static void main(String[] args) {
		String inputFileName = "/Users/steve/$WFM/ReferenceData/gbs-short-list/GBS Taxonomy - Shortlist/jrs-tax-m373a.json";
		String outputFileName = "/Users/steve/$WFM/ReferenceData/gbs-short-list/jrss-m373a.json";
	    try {
	        File fileIn = new File(inputFileName);
	        FileWriter writer = new FileWriter(outputFileName);
	        Scanner reader = new Scanner(fileIn);
	        while (reader.hasNextLine()) {
	          String line = reader.nextLine();
	          if (line.contains("\"children\": [") || line.contains("\"description\":") || line.contains("\"code\":") ||
	        	  line.contains("\"cmpnstnGrdLst\":") || line.contains("\"incentiveFlg\":")	||
	        	  line.contains("{") || line.contains("}") || line.contains("],")
	        	  ) {
	        	  if (line.contains("\"description\":")) line=line.replace("description", "name");
	        	  if (line.contains("],")) line=line.replace("],", "]");
	        	  if (line.contains("\"incentiveFlg\":")) line=line.trim().substring(0, line.trim().length()-1);
	        	  if (line.contains("\"incentiveFlg\": null")) line=line.replace("null", "\"N\"");
	        	  if (line.contains("\"cmpnstnGrdLst\":")) line=line.replace("cmpnstnGrdLst", "compGrdList");
	        	  System.out.println(line);
	        	  writer.write(line+System.lineSeparator());
	          }
	        }
	        reader.close();
	        writer.flush();
	        writer.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }

	}

}
