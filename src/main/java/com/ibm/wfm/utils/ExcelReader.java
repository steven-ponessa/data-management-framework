package com.ibm.wfm.utils;

import static org.apache.poi.ss.usermodel.CellType.STRING;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.ERROR;
import static org.apache.poi.ss.usermodel.CellType.BLANK;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A simplistic class that provides static utility methods to reads an Excel
 * file.
 * 
 * @author ponessa@us.ibm.com
 *
 */
public class ExcelReader {

	public static void main(String[] args) throws IOException {
		// String excelFilePath = "/Users/steve/Downloads/Shortlist publications/GBS
		// Taxonomy - Shortlist/M306 Short List.xlsx";
		// String tabName = "GBS Short List";

		System.out.println("Started at " + new java.util.Date());

		String excelFileName = null;
		String tabName = null;
		boolean validParams = true;

		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-e") || args[optind].equalsIgnoreCase("--excel")) {
					excelFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-t") || args[optind].equalsIgnoreCase("--tab")) {
					tabName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("--help")) {
					validParams = false;
				} else {
					System.out.println("Invalid parameter specfied, " + args[optind] + ".");
					validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			validParams = false;
		}

		if (excelFileName == null) {
			validParams = false;
		}

		if (!validParams) {
			// logger.info(msgs.getText("info.usage"));
			System.out.println("Usage: com.ibm.gbs.utils.ExcelReader");
			System.out.println(" ");
			System.out.println("ExcelReader Parameters");
			System.out.println("----------------------");
			System.out.println("-e | --excel     - Excel file path and name.");
			System.out.println("[-t | --tab]     - Tab name (default: all tabs).");
			System.out.println("[-id | -treeId]  - Id to be used for tree element (default: treeId)");
			System.out.println(" ");
			System.out.println("Debug Parameters");
			System.out.println("----------------");
			System.out.println("[-h | -help]    - Display usage parameters.");

			System.exit(-99);
		}

		if (tabName == null || tabName.equalsIgnoreCase("all")) {
			Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFileName));
			int numberOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				System.out.println("Sheet Name: " + sheetName);
				ArrayList<String[]> rows = ExcelReader.readExcelPage(excelFileName, sheetName);
				for (String[] row : rows) {
					for (String element : row) {
						System.out.print(element + ", ");
					}
					System.out.println();
				}
			}

		} else {
			ArrayList<String[]> rows = ExcelReader.readExcelPage(excelFileName, tabName);

			System.out.println("Sheet Name: " + tabName);
			for (String[] row : rows) {
				for (String element : row) {
					System.out.print(element + ", ");
				}
				System.out.println();
			}
		}

		System.out.println("Complete at " + new java.util.Date());
	}

	public static ArrayList<String[]> readExcelPage(String excelFilePath) throws IOException {
		return readExcelPage(excelFilePath, null);
	}

	public static ArrayList<String[]> readExcelPage(String excelFilePath, String tabName) throws IOException {

		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = null;
		if (tabName == null)
			sheet = workbook.getSheetAt(0);
		else
			sheet = workbook.getSheet(tabName);

		if (sheet == null) {
			System.out.println("Sheet " + tabName + " not found in spreadsheet " + excelFilePath);
			return null;
		}
		
		/*SP-Temporary

        for (Row row : sheet) {
            for (Cell cell : row) {
                //String cellValue = getCellValueAsString(cell);
                System.out.println(cell.getColumnIndex()+". cell type="+cell.getCellType()+", "+ getCellValueAsString(cell));
            }
        }

		*///end SP-Temporary

		// Iterator<Row> iterator = sheet.iterator();
		ArrayList<String[]> rowArray = readExcelSheet(sheet);

		workbook.close();
		inputStream.close();

		return rowArray;
	}
	
    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Handle date formatting if necessary
                    return cell.getDateCellValue().toString();
                } else {
                    // Convert numeric cell to string without decimal point
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((int) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: 
            	switch (cell.getCachedFormulaResultType()) {
				case BOOLEAN:
					return cell.getBooleanCellValue()+"";
				case NUMERIC:
					return String.valueOf(cell.getNumericCellValue());
				case STRING:
					return String.valueOf(cell.getRichStringCellValue());
				}
            	break;
            default: 
            	return "default";
        }
        return "";
    }
	
	public static void test(String fileLocation) throws IOException {
		FileInputStream file = new FileInputStream(new File(fileLocation));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		Map<Integer, List<String>> data = new HashMap<>();
		int i = 0;
		for (Row row : sheet) {
		    data.put(i, new ArrayList<String>());
		    for (Cell cell : row) {
		        switch (cell.getCellType()) {
		            case STRING: 
		            	data.get(new Integer(i)).add(cell.getRichStringCellValue().getString());
		            	break;
		            case NUMERIC:
		            	if (DateUtil.isCellDateFormatted(cell)) {
		            	    data.get(i).add(cell.getDateCellValue() + "");
		            	} else {
		            	    data.get(i).add(cell.getNumericCellValue() + "");
		            	}
		            	break;
		            case BOOLEAN:
		            	data.get(i).add(cell.getBooleanCellValue() + "");
		            	break;
		            case FORMULA: 
		            	switch (cell.getCachedFormulaResultType()) {
						case BOOLEAN:
							data.get(i).add(cell.getBooleanCellValue()+"");
							break;
						case NUMERIC:
							data.get(i).add(String.valueOf(cell.getNumericCellValue()));
							break;
						case STRING:
							data.get(i).add(String.valueOf(cell.getRichStringCellValue()));
							break;
						}
		            	break;
		            default: data.get(new Integer(i)).add(" ");
		        }
		    }
		    i++;
		}
		
	}

	public static ArrayList<String[]> readExcelSheet(Sheet sheet) throws IOException {
		ArrayList<String[]> rowArray = null;
		// while (iterator.hasNext()) {
		for (Row row : sheet) {
			// Row nextRow = iterator.next();
			// Iterator<Cell> cellIterator = row.cellIterator();

			if (row.getLastCellNum() > 0) {

				String[] values = new String[row.getLastCellNum()];

				// while (cellIterator.hasNext()) {
				// Cell cell = cellIterator.next();
				for (int cn = 0; cn < row.getLastCellNum(); cn++) {
					// If the cell is missing from the file, generate a blank one
					// (Works by specifying a MissingCellPolicy)
					Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

					switch (cell.getCellType()) {
					case STRING:
						values[cn] = cell.getStringCellValue();
						break;
					case BOOLEAN:
						values[cn] = String.valueOf(cell.getBooleanCellValue());
						break;
					case NUMERIC:
						//values[cn] = String.valueOf(cell.getNumericCellValue());
		                if (DateUtil.isCellDateFormatted(cell)) {
		                    // Handle date formatting if necessary
		                    values[cn] = cell.getDateCellValue().toString();
		                } else {
		                    // Convert numeric cell to string without decimal point
		                    double numericValue = cell.getNumericCellValue();
		                    if (numericValue == Math.floor(numericValue)) {
		                        values[cn] = String.valueOf((int) numericValue);
		                    } else {
		                        values[cn] = String.valueOf(numericValue);
		                    }
		                }
						break;
					// case _NONE:
					// values[cn]="";
					// break;
					case BLANK:
						values[cn] = "";
						break;
					case ERROR:
						values[cn] = "<error>";
						System.out.println("Cell Error: " + String.valueOf(cell.getErrorCellValue()));
						break;
					case FORMULA:
						// values[cn]="<formula - "+cell.getCellFormula()+">";
						// System.out.println("Cell Forumula: "+cell.getCellFormula());

						switch (cell.getCachedFormulaResultType()) {
						case BOOLEAN:
							values[cn] = String.valueOf(cell.getBooleanCellValue());
							break;
						case NUMERIC:
							values[cn] = String.valueOf(cell.getNumericCellValue());
							break;
						case STRING:
							values[cn] = String.valueOf(cell.getRichStringCellValue());
							break;
						}

						break;
					default:
						values[cn] = "<unknown cell type=" + cell.getCellType() + ">";
						System.out.println("Cell unknown type: " + cell.getCellType());
						break;
					} // end - switch (cell.getCellType())
				} // end - for(int cn=0; cn<row.getLastCellNum(); cn++)

				if (rowArray == null)
					rowArray = new ArrayList<String[]>(sheet.getLastRowNum());
				rowArray.add(values);
			}

		} // end - for (Row row : sheet)
		return rowArray;
	}

}