package com.ibm.wfm.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.sdss.SdssReferenceData;
import com.ibm.wfm.beans.sdss.SdssValidationResponse;
import com.ibm.wfm.beans.taxonomies.TaxonomyBranchValidationResult;
import com.ibm.wfm.beans.taxonomies.TaxonomyMatchMode;
import com.ibm.wfm.beans.taxonomies.TaxonomyTestCase;
import com.ibm.wfm.beans.taxonomies.TaxonomyTestResult;
import com.ibm.wfm.services.SdssValidationService;

public class JrsTaxonomyPathValidatorDriver {
	
	private static final String TAXONOMY_URL =
	        "http://localhost:9090/api/v1/eds-ut-jrs-tax/jrss?includeParentage=true";

    public static void main(String[] args) throws Exception {
    	
		String mode = "tc";
		String testCaseFileName = "/Users/steve/$WFM/svss/branch-tests/jrs-taxonomy-test-cases.xlsx";
		String outputFileName = "/Users/steve/$WFM/svss/branch-tests/jrs-taxonomy-test-results.xlsx";
        //String excelFilePathName = "/Users/steve/$WFM/svss/upload-test-data/Enhanced-Skills-Value-Framework-2026-06-06.xlsx";
        String excelFilePathName = "/Users/steve/Downloads/SDSS Staging - Tue, 09 Jun 2026 18_05_07 GMT.xlsx";
        String excelTabName = "Data";
        
        String outputPathName = "/Users/steve/$WFM/svss/upload-test-data/sdss-output-2026-06-06.xlsx";
        
        boolean verbose = false;
        boolean validParams = true;
        
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-m") || args[optind].equalsIgnoreCase("-mode")) {
					mode = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-tcf")) {
					testCaseFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-o") || args[optind].equalsIgnoreCase("-output")) {
					outputFileName = args[++optind];
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
			// logger.error(msgs.getText("error.e0001", e.getMessage()));
			e.printStackTrace();
			validParams = false;
		}
		
		if (!validParams) {
			System.out.println("Show help here");
			return;
		}
		
    	System.out.println(new java.util.Date() + ": sdssStagings started.  Mode="+mode);
		if ("tc".equalsIgnoreCase(mode)) {
			
	        Path inputWorkbook = Path.of(testCaseFileName);
	        Path outputWorkbook = Path.of(outputFileName);
			
	        ResponseEntity<BrandDim[]> response =
	            new RestTemplate().getForEntity(TAXONOMY_URL, BrandDim[].class);

	        List<BrandDim> brands = Arrays.asList(response.getBody());

	        List<TaxonomyTestCase> testCases = readTestCases(inputWorkbook);

	        List<TaxonomyTestResult> results = new ArrayList<>();

	        for (TaxonomyTestCase testCase : testCases) {
	            results.add(runTestCase(brands, testCase));
	        }

	        writeResults(outputWorkbook, results);

	        long passed = results.stream().filter(TaxonomyTestResult::passed).count();

	        System.out.println("Completed");
	        System.out.println("Total : " + results.size());
	        System.out.println("Passed: " + passed);
	        System.out.println("Failed: " + (results.size() - passed));
			
		}
		else {
			/*
	    	SdssReferenceData referenceData = new SdssReferenceData();
	    	
	    	SdssValidationService validationService = new SdssValidationService(referenceData);
	    	
	    	SdssValidationResponse validationResponse = validationService.validateExcel(excelFilePathName, excelTabName);
	    	
	    	System.out.println("");
	    	System.out.println(validationResponse.toMarkdown());
	    	
	    	if (validationResponse.hasErrors()) {	    	
	    		validationResponse.writeExcel(Path.of(outputPathName));
	    	}
	    	*/

		}
    	
    	System.out.println(new java.util.Date() + ": sdssStagings completed");

    }
    
    private static TaxonomyTestResult runTestCase(
            List<BrandDim> brands,
            TaxonomyTestCase testCase
    ) {
        TaxonomyBranchValidationResult result = null;
        boolean actualValid = false;
        String actualStatus = null;
        Integer failedLevel = null;
        String failedCode = null;
        String message = null;

        switch (testCase.testType()) {
            case "NODE" -> {
                Optional<NaryTreeNode> found = Optional.empty();

                for (BrandDim brand : brands) {
                    found = brand.findByValueAndLevel(
                        testCase.nodeCode(),
                        testCase.nodeLevel(),
                        testCase.matchMode().equalsIgnoreCase("code")?TaxonomyMatchMode.CODE:TaxonomyMatchMode.DESCRIPTION
                    );

                    if (found.isPresent()) {
                    	NaryTreeNode foundObject = found.get();
                        break;
                    }
                }

                actualValid = found.isPresent();
                actualStatus = actualValid ? "VALID" : "INVALID_NODE";
                failedLevel = actualValid ? null : testCase.nodeLevel();
                failedCode = actualValid ? null : testCase.nodeCode();
                message = actualValid ? "Node found" : "Node not found";
            }

            case "BRANCH" -> {
                for (BrandDim brand : brands) {
                    result = brand.validateBranch(testCase.branchCodes(),testCase.matchMode().equalsIgnoreCase("code")?TaxonomyMatchMode.CODE:TaxonomyMatchMode.DESCRIPTION);

                    if (result.valid()) {
                        break;
                    }
                }

                actualValid = result != null && result.valid();
                actualStatus = result == null ? "INVALID_NODE" : result.status().name();
                failedLevel = result == null ? null : result.failedLevel();
                failedCode = result == null ? null : result.failedCode();
                message = result == null ? "No result" : result.message();
            }

            case "PARTIAL_BRANCH" -> {
                for (BrandDim brand : brands) {
                    result = brand.validatePartialBranch(
                        testCase.branchCodes(),
                        testCase.leafLevel(),
                        testCase.matchMode().equalsIgnoreCase("code")?TaxonomyMatchMode.CODE:TaxonomyMatchMode.DESCRIPTION
                    );

                    if (result.valid()) {
                        break;
                    }
                }

                actualValid = result != null && result.valid();
                actualStatus = result == null ? "INVALID_NODE" : result.status().name();
                failedLevel = result == null ? null : result.failedLevel();
                failedCode = result == null ? null : result.failedCode();
                message = result == null ? "No result" : result.message();
            }
            case "FIND" -> {
                for (BrandDim brand : brands) {
                	Optional<List<NaryTreeNode>> found = brand.findValidBranchForLeaf(
                        testCase.nodeCode(),
                        testCase.nodeLevel(),
                        testCase.matchMode().equalsIgnoreCase("code")?TaxonomyMatchMode.CODE:TaxonomyMatchMode.DESCRIPTION
                    );

                    if (found.isPresent()) {
                    	List<NaryTreeNode> foundObject = found.get();
                    	System.out.println(foundObject.size());
                        break;
                    }	
                }
            }

            default -> {
                actualValid = false;
                actualStatus = "INVALID_TEST_TYPE";
                message = "Unknown test type: " + testCase.testType();
            }
        }

        boolean passed =
            Objects.equals(testCase.expectedValid(), actualValid)
            && Objects.equals(testCase.expectedStatus(), actualStatus);

        return new TaxonomyTestResult(
            testCase.testCaseId(),
            testCase.testType(),
            testCase.matchMode(),
            testCase.expectedValid(),
            testCase.expectedStatus(),
            actualValid,
            actualStatus,
            failedLevel,
            failedCode,
            message,
            passed
        );
    } 
    
    private static List<TaxonomyTestCase> readTestCases(Path inputWorkbook) throws IOException {
        List<TaxonomyTestCase> testCases = new ArrayList<>();

        try (InputStream in = Files.newInputStream(inputWorkbook);
             Workbook workbook = WorkbookFactory.create(in)) {

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet == null) {
                return testCases;
            }

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> col = buildColumnMap(headerRow);

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                String testCaseId = getString(row, col, "testCaseId");
                String testType = getString(row, col, "testType");
                String matchMode = getString(row, col, "matchMode");
                Boolean expectedValid = getBoolean(row, col, "expectedValid");
                String expectedStatus = getString(row, col, "expectedStatus");
                String nodeCode = getString(row, col, "nodeCode");
                Integer nodeLevel = getInteger(row, col, "nodeLevel");
                Integer leafLevel = getInteger(row, col, "leafLevel");

                List<String> branchCodes = new ArrayList<>();

                for (int level = 0; level <= 10; level++) {
                    String value = getString(row, col, "level" + level);

                    if (value != null && !value.isBlank()) {
                        branchCodes.add(value.trim());
                    }
                }
                testCases.add(
                		new TaxonomyTestCase(
                    testCaseId,
                    testType == null ? null : testType.trim().toUpperCase(Locale.ROOT),
                    matchMode,
                    expectedValid,
                    expectedStatus == null ? null : expectedStatus.trim().toUpperCase(Locale.ROOT),
                    nodeCode,
                    nodeLevel,
                    leafLevel,
                    branchCodes
                ));

            }
        }

        return testCases;
    }    
    
    private static void writeResults(
            Path outputWorkbook,
            List<TaxonomyTestResult> results
    ) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet summarySheet = workbook.createSheet("Summary");
            Sheet detailSheet = workbook.createSheet("Detail");

            long total = results.size();
            long passed = results.stream().filter(TaxonomyTestResult::passed).count();
            long failed = total - passed;

            int r = 0;
            Row row = summarySheet.createRow(r++);
            row.createCell(0).setCellValue("Total Tests");
            row.createCell(1).setCellValue(total);

            row = summarySheet.createRow(r++);
            row.createCell(0).setCellValue("Passed");
            row.createCell(1).setCellValue(passed);

            row = summarySheet.createRow(r++);
            row.createCell(0).setCellValue("Failed");
            row.createCell(1).setCellValue(failed);

            Row header = detailSheet.createRow(0);
            String[] headers = {
                "testCaseId",
                "testType",
                "matchMode",
                "expectedValid",
                "expectedStatus",
                "actualValid",
                "actualStatus",
                "failedLevel",
                "failedCode",
                "message",
                "passed"
            };

            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;

            for (TaxonomyTestResult result : results) {
                Row detail = detailSheet.createRow(rowNum++);

                detail.createCell(0).setCellValue(nullToBlank(result.testCaseId()));
                detail.createCell(1).setCellValue(nullToBlank(result.testType()));
                detail.createCell(2).setCellValue(nullToBlank(result.matchMode()));
                detail.createCell(3).setCellValue(result.expectedValid() != null && result.expectedValid());
                detail.createCell(4).setCellValue(nullToBlank(result.expectedStatus()));
                detail.createCell(5).setCellValue(result.actualValid() != null && result.actualValid());
                detail.createCell(6).setCellValue(nullToBlank(result.actualStatus()));

                if (result.failedLevel() != null) {
                    detail.createCell(7).setCellValue(result.failedLevel());
                } else {
                    detail.createCell(7).setCellValue("");
                }

                detail.createCell(8).setCellValue(nullToBlank(result.failedCode()));
                detail.createCell(9).setCellValue(nullToBlank(result.message()));
                detail.createCell(10).setCellValue(result.passed());
            }

            for (int i = 0; i < headers.length; i++) {
                detailSheet.autoSizeColumn(i);
            }

            summarySheet.autoSizeColumn(0);
            summarySheet.autoSizeColumn(1);

            try (OutputStream out = Files.newOutputStream(outputWorkbook)) {
                workbook.write(out);
            }
        }
    }    
    
    private static Map<String, Integer> buildColumnMap(Row headerRow) {
        Map<String, Integer> col = new HashMap<>();

        if (headerRow == null) {
            return col;
        }

        for (Cell cell : headerRow) {
            String name = getCellString(cell);

            if (name != null && !name.isBlank()) {
                col.put(name.trim(), cell.getColumnIndex());
            }
        }

        return col;
    }    
    
    private static String getString(
            Row row,
            Map<String, Integer> col,
            String columnName
    ) {
        Integer index = col.get(columnName);

        if (index == null) {
            return null;
        }

        Cell cell = row.getCell(index);

        return getCellString(cell);
    }    
    
    private static Boolean getBoolean(
            Row row,
            Map<String, Integer> col,
            String columnName
    ) {
        String value = getString(row, col, columnName);

        if (value == null || value.isBlank()) {
            return null;
        }

        return Boolean.parseBoolean(value.trim());
    }    
    
    private static Integer getInteger(
            Row row,
            Map<String, Integer> col,
            String columnName
    ) {
        String value = getString(row, col, columnName);

        if (value == null || value.isBlank()) {
            return null;
        }

        return Integer.valueOf(value.trim());
    }    
    
    private static String getCellString(Cell cell) {
        if (cell == null) {
            return null;
        }

        DataFormatter formatter = new DataFormatter();

        String value = formatter.formatCellValue(cell);

        return value == null ? null : value.trim();
    }    
    
    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (Cell cell : row) {
            String value = getCellString(cell);

            if (value != null && !value.isBlank()) {
                return false;
            }
        }

        return true;
    }    
    
    private static String nullToBlank(Object value) {
        return value == null ? "" : String.valueOf(value);
    } 

}
