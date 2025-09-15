package com.ibm.wfm.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownToCsvConverter {

    public static void main(String[] args) {
        String inputFile = "/Users/steve/temp/markdown-models/pricing-case.txt"; // Input markdown file
        String outputFile = "/Users/steve/temp/markdown-models/output.txt"; // Output CSV file
        String outputType = "md"; //"csv";

        try {
            List<String[]> data = parseMarkdown(inputFile);
            if (outputType.equalsIgnoreCase("csv"))
            	writeCsv(data, outputFile);
            else
            	writeMd(data, outputFile);
            System.out.println("Conversion successful!");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static List<String[]> parseMarkdown(String inputFile) throws IOException {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            String className = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("class") && !line.trim().startsWith("classDiagram")) {
                    className = line.trim().split("\\s+")[1];
                } else if (line.trim().startsWith("+") || line.trim().startsWith("-")) {
                    String[] attribute = parseAttribute(line.trim(), className);
                    data.add(attribute);
                }
            }
        }

        return data;
    }

    public static String[] parseAttribute(String line, String className) {
        String[] attribute = new String[5];

        attribute[0] = className;

        String[] parts = line.split("\\s+");
        attribute[1] = parts[1].replace(":", "");
        attribute[2] = parts.length>3?parts[3]:parts[2];
        
        if (attribute[2].equalsIgnoreCase("Integer")) attribute[3]="4";
        else if (attribute[2].equalsIgnoreCase("Timestamp")) attribute[3]="10";
        else if (attribute[2].equalsIgnoreCase("Date")) attribute[3]="4";
        else if (attribute[2].equalsIgnoreCase("Double")) attribute[3]="4";
        else attribute[3]="128";

        if (line.startsWith("+")) {
            attribute[4] = "No";
        } else if (line.startsWith("-")) {
            attribute[4] = "Yes";
        }

        return attribute;
    }

    public static void writeCsv(List<String[]> data, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.append("Entity Name,Attribute Name,Data Type,Length,Nullable,Comments\n");

            for (String[] row : data) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }
        }
    }
    
    public static void writeMd(List<String[]> data, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
        	String header = "| **Attribute Name** | **Data Type** | **Length** | **Nullable** | **Comments** |\n"
        	              + "|--------------------|---------------|-----------:|:------------:|--------------|\n";
        	String lastEntity = null;
            //writer.append("Entity Name,Attribute Name,Data Type,Length,Nullable,Comments\n");

            for (String[] row : data) {
            	String currentEntity = row[0];
            	if (!currentEntity.equals(lastEntity)) {
            		writer.append("\n\n**Entity: "+currentEntity+"**\n\n");
            		writer.append(header);
            		lastEntity = currentEntity;
            	}
                writer.append("| "+String.join(" | ", Arrays.copyOfRange(row, 1, row.length))+" |");
                writer.append("\n");
            }
        }
    }
}
