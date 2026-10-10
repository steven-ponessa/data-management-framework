package com.ibm.wfm.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.ibm.wfm.utils.DbColumnUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class CsvSortService {
	
    private CsvSortService() {}
	
	public static void sortCsv(
	        Path inputFile,
	        Path outputFile,
	        int... keyColumns
	) throws IOException, CsvValidationException {

	    List<String[]> rows = new ArrayList<>();

	    try (CSVReader reader =
	            new CSVReader(new FileReader(inputFile.toFile()))) {

	        String[] row;

	        while ((row = reader.readNext()) != null) {
	            rows.add(row);
	        }
	    }

	    if (rows.isEmpty()) {
	        return;
	    }

	    String[] header = rows.remove(0);

	    rows.sort(buildComparator(keyColumns));

	    try (CSVWriter writer =
	            new CSVWriter(new FileWriter(outputFile.toFile()))) {

	        writer.writeNext(header);

	        for (String[] row : rows) {
	            writer.writeNext(row);
	        }
	    }
	}
	
	private static Comparator<String[]> buildComparatorOrig(
	        int... keyColumns
	) {

	    return (a, b) -> {

	        for (int column : keyColumns) {

	            String left =
	                    column < a.length ? a[column] : "";

	            String right =
	                    column < b.length ? b[column] : "";

	            int result =
	                    left.compareToIgnoreCase(right);

	            if (result != 0) {
	                return result;
	            }
	        }

	        return 0;
	    };
	}

    public static void sortCsvByDbColumnKeys(
            Path inputFile,
            Path outputFile,
            Class<?> type
    ) throws Exception {

        List<String> keyColumnNames =
                DbColumnUtil.getNaturalKeyColumnNames(type);

        if (keyColumnNames.isEmpty()) {
            throw new IllegalArgumentException(
                    "No @DbColumn keySeq values found for " + type.getName()
            );
        }

        sortCsvByColumnNames(inputFile, outputFile, keyColumnNames);
    }
    
    public static void sortCsvByColumnNames(
            Path inputFile,
            Path outputFile,
            List<String> keyColumnNames
    ) throws Exception {

        List<String[]> rows = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(inputFile.toFile()))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                rows.add(row);
            }
        }

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty: " + inputFile);
        }

        String[] header = rows.remove(0);

        Map<String, Integer> headerIndex = buildHeaderIndex(header);

        int[] keyIndexes = keyColumnNames.stream()
                .mapToInt(columnName -> {
                    Integer index = headerIndex.get(normalize(columnName));

                    if (index == null) {
                        throw new IllegalArgumentException(
                                "Natural key column '" + columnName
                                        + "' not found in CSV header. Available columns: "
                                        + Arrays.toString(header)
                        );
                    }

                    return index;
                })
                .toArray();

        rows.sort(buildComparator(keyIndexes));

        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile.toFile()))) {
            writer.writeNext(header);

            for (String[] row : rows) {
                writer.writeNext(row);
            }
        }
    }

    private static Map<String, Integer> buildHeaderIndex(String[] header) {

        Map<String, Integer> index = new HashMap<>();

        for (int i = 0; i < header.length; i++) {
            index.put(normalize(header[i]), i);
        }

        return index;
    }

    private static Comparator<String[]> buildComparator(int[] keyIndexes) {

        return (leftRow, rightRow) -> {

            for (int keyIndex : keyIndexes) {

                String left = getValue(leftRow, keyIndex);
                String right = getValue(rightRow, keyIndex);

                int result = left.compareToIgnoreCase(right);

                if (result != 0) {
                    return result;
                }
            }

            return 0;
        };
    }

    private static String getValue(String[] row, int index) {

        if (index < 0 || index >= row.length || row[index] == null) {
            return "";
        }

        return row[index].trim();
    }

    private static String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value.trim().toUpperCase(Locale.ROOT);
    }

}
