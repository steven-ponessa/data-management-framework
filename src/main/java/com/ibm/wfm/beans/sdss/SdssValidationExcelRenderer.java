package com.ibm.wfm.beans.sdss;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class SdssValidationExcelRenderer {

    private static final int DETAIL_LAST_COLUMN = 12;

    private SdssValidationExcelRenderer() {}

    public static void write(
            SdssValidationResponse response,
            Path outputFile
    ) throws IOException {

        try (OutputStream outputStream =
                     Files.newOutputStream(outputFile)) {

            write(response, outputStream);
        }
    }

    public static void write(
            SdssValidationResponse response,
            OutputStream outputStream
    ) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {
            CellStyle headerStyle =
                    createHeaderStyle(workbook);

            writeSummarySheet(
                    workbook,
                    response,
                    headerStyle
            );

            writeDetailSheet(
                    workbook,
                    response,
                    headerStyle
            );

            writeValidationCountSheet(
                    workbook,
                    response,
                    headerStyle
            );

            workbook.write(outputStream);
            outputStream.flush();
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex()
        );
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return headerStyle;
    }

    private static void writeSummarySheet(
            Workbook workbook,
            SdssValidationResponse response,
            CellStyle headerStyle
    ) {
        Sheet sheet = workbook.createSheet("Summary");

        int rowNum = 0;

        // Add a true column-header row.
        Row header = sheet.createRow(rowNum++);
        createHeaderCell(header, 0, "Metric", headerStyle);
        createHeaderCell(header, 1, "Value", headerStyle);

        rowNum = writeNameValue(
                sheet,
                rowNum,
                "Total Records",
                response.summary().totalRecords()
        );

        rowNum = writeNameValue(
                sheet,
                rowNum,
                "Valid Records",
                response.summary().validRecords()
        );

        writeNameValue(
                sheet,
                rowNum,
                "Invalid Records",
                response.summary().invalidRecords()
        );

        // Freeze the first row.
        sheet.createFreezePane(0, 1);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private static int writeNameValue(
            Sheet sheet,
            int rowNum,
            String name,
            Object value
    ) {
        Row row = sheet.createRow(rowNum++);

        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(
                value == null ? "" : String.valueOf(value)
        );

        return rowNum;
    }

    private static void writeDetailSheet(
            Workbook workbook,
            SdssValidationResponse response,
            CellStyle headerStyle
    ) {
        Sheet sheet = workbook.createSheet("Detail");

        Row header = sheet.createRow(0);

        String[] headers = {
                "Row Number",
                "Attribute Name",
                "Invalid Value",
                "Reason",
                "Valid Branch",
                "Growth Platform Code",
                "Growth Platform Name",
                "Service Line Code",
                "Service Line Name",
                "Practice Code",
                "Practice Name",
                "JRS Code",
                "JRS Name"
        };

        for (int column = 0; column < headers.length; column++) {
            createHeaderCell(
                    header,
                    column,
                    headers[column],
                    headerStyle
            );
        }

        int rowNum = 1;

        for (SdssValidationRecordError recordError : response.detail()) {
            for (SdssAttributeError attributeError
                    : recordError.invalidAttributes()) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0)
                        .setCellValue(recordError.rowNumber());

                row.createCell(1)
                        .setCellValue(nullToBlank(
                                attributeError.attributeName()
                        ));

                row.createCell(2)
                        .setCellValue(nullToBlank(
                                attributeError.value()
                        ));

                row.createCell(3)
                        .setCellValue(nullToBlank(
                                attributeError.reason()
                        ));

                row.createCell(4)
                        .setCellValue(nullToBlank(
                                attributeError.validBranchAsText()
                        ));

                String[] branchNodes =
                        attributeError.validBranchAsNodeArray();

                if (branchNodes != null) {
                    int startingColumn = 5;

                    for (int i = 0; i < branchNodes.length; i++) {
                        if (i > 1 && i != 8 && i != 9) {
                            row.createCell(startingColumn++)
                                    .setCellValue(
                                            nullToBlank(branchNodes[i])
                                    );
                        }
                    }
                }
            }
        }

        // Freeze the header row.
        sheet.createFreezePane(0, 1);

        /*
         * Apply the filter to the complete data range:
         * row 0 through the last populated row,
         * column 0 through column 12.
         */
        sheet.setAutoFilter(
                new CellRangeAddress(
                        0,
                        Math.max(0, rowNum - 1),
                        0,
                        DETAIL_LAST_COLUMN
                )
        );

        for (int column = 0; column <= DETAIL_LAST_COLUMN; column++) {
            sheet.autoSizeColumn(column);
        }
    }
    
    private static void writeValidationCountSheet(
            Workbook workbook,
            SdssValidationResponse response,
            CellStyle headerStyle
    ) {
        Sheet sheet = workbook.createSheet("Validation Counts");

        Row header = sheet.createRow(0);
        createHeaderCell(header, 0, "Attribute Name", headerStyle);
        createHeaderCell(header, 1, "Invalid Value", headerStyle);
        createHeaderCell(header, 2, "Reason", headerStyle);
        createHeaderCell(header, 3, "Invalid Record Count", headerStyle);

        /*
         * LinkedHashMap preserves the order in which combinations are first
         * encountered in the validation response.
         */
        Map<ValidationCountKey, Long> counts = new LinkedHashMap<>();

        if (response != null && response.detail() != null) {
            for (SdssValidationRecordError recordError : response.detail()) {
                if (recordError == null ||
                    recordError.invalidAttributes() == null) {
                    continue;
                }

                for (SdssAttributeError attributeError
                        : recordError.invalidAttributes()) {

                    if (attributeError == null) {
                        continue;
                    }

                    ValidationCountKey key = new ValidationCountKey(
                            nullToBlank(attributeError.attributeName()),
                            nullToBlank(attributeError.value()),
                            nullToBlank(attributeError.reason())
                    );

                    counts.merge(key, 1L, Long::sum);
                }
            }
        }

        /*
         * Sort primarily by the highest count, then by attribute, value,
         * and reason for predictable output.
         */
        List<Map.Entry<ValidationCountKey, Long>> sortedCounts =
                counts.entrySet()
                        .stream()
                        .sorted(
                                Map.Entry
                                        .<ValidationCountKey, Long>comparingByValue()
                                        .reversed()
                                        .thenComparing(
                                                entry -> entry.getKey().attributeName(),
                                                String.CASE_INSENSITIVE_ORDER
                                        )
                                        .thenComparing(
                                                entry -> entry.getKey().value(),
                                                String.CASE_INSENSITIVE_ORDER
                                        )
                                        .thenComparing(
                                                entry -> entry.getKey().reason(),
                                                String.CASE_INSENSITIVE_ORDER
                                        )
                        )
                        .toList();

        int rowNum = 1;

        for (Map.Entry<ValidationCountKey, Long> entry : sortedCounts) {
            ValidationCountKey key = entry.getKey();

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(key.attributeName());
            row.createCell(1).setCellValue(key.value());
            row.createCell(2).setCellValue(key.reason());
            row.createCell(3).setCellValue(entry.getValue());
        }

        sheet.createFreezePane(0, 1);

        sheet.setAutoFilter(
                new CellRangeAddress(
                        0,
                        Math.max(0, rowNum - 1),
                        0,
                        3
                )
        );

        for (int column = 0; column <= 3; column++) {
            sheet.autoSizeColumn(column);
        }
    }

    private static void createHeaderCell(
            Row row,
            int column,
            String value,
            CellStyle headerStyle
    ) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(headerStyle);
    }

    private static String nullToBlank(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}