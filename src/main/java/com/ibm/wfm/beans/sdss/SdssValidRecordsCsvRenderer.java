package com.ibm.wfm.beans.sdss;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.ibm.wfm.utils.NonClosingOutputStream;

public final class SdssValidRecordsCsvRenderer {

    private static final String DATA_SHEET_NAME = "Data";

    private SdssValidRecordsCsvRenderer() {}

    public static void write(
            Path uploadedFile,
            Set<Integer> invalidExcelRowNumbers,
            OutputStream outputStream
    ) throws IOException {

        try (
            InputStream inputStream =
                    new BufferedInputStream(
                            Files.newInputStream(uploadedFile)
                    );

            Workbook sourceWorkbook =
                    WorkbookFactory.create(inputStream);

            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(
                            		new NonClosingOutputStream(outputStream),
                                    StandardCharsets.UTF_8
                            )
                    )
        ) {
            Sheet sourceSheet =
                    sourceWorkbook.getSheet(DATA_SHEET_NAME);

            if (sourceSheet == null) {
                throw new IllegalArgumentException(
                        "Uploaded workbook does not contain a "
                                + DATA_SHEET_NAME
                                + " sheet"
                );
            }

            writeValidRows(
                    sourceWorkbook,
                    sourceSheet,
                    invalidExcelRowNumbers,
                    writer
            );

            /*
             * Flush, but do not close the underlying ZIP output stream.
             *
             * The BufferedWriter is technically closed by the try block,
             * so this method should only be used when the supplied stream
             * is safe to close.
             *
             * A ZIP-safe version is shown below.
             */
            writer.flush();
        }
    }

    private static void writeValidRows(
            Workbook sourceWorkbook,
            Sheet sourceSheet,
            Set<Integer> invalidExcelRowNumbers,
            BufferedWriter writer
    ) throws IOException {

        DataFormatter formatter = new DataFormatter();

        FormulaEvaluator evaluator =
                sourceWorkbook
                        .getCreationHelper()
                        .createFormulaEvaluator();

        Row headerRow = sourceSheet.getRow(0);

        if (headerRow == null) {
            throw new IllegalArgumentException(
                    "The Data sheet does not contain a header row"
            );
        }

        int columnCount = headerRow.getLastCellNum();

        if (columnCount <= 0) {
            throw new IllegalArgumentException(
                    "The Data sheet header does not contain any columns"
            );
        }

        for (Row sourceRow : sourceSheet) {
            int excelRowNumber =
                    sourceRow.getRowNum() + 1;

            boolean headerRowIndicator =
                    sourceRow.getRowNum() == 0;

            /*
             * Always retain the header row.
             *
             * This assumes SdssValidationRecordError.rowNumber()
             * contains the actual one-based Excel row number.
             */
            if (!headerRowIndicator
                    && invalidExcelRowNumbers.contains(excelRowNumber)) {
                continue;
            }

            writeCsvRow(
                    sourceRow,
                    columnCount,
                    formatter,
                    evaluator,
                    writer
            );
        }
    }

    private static void writeCsvRow(
            Row sourceRow,
            int columnCount,
            DataFormatter formatter,
            FormulaEvaluator evaluator,
            BufferedWriter writer
    ) throws IOException {

        for (int column = 0; column < columnCount; column++) {
            if (column > 0) {
                writer.write(',');
            }

            Cell cell = sourceRow.getCell(
                    column,
                    Row.MissingCellPolicy.RETURN_BLANK_AS_NULL
            );

            String value =
                    cell == null
                            ? ""
                            : formatter.formatCellValue(
                                    cell,
                                    evaluator
                            );

            writer.write(escapeCsvValue(value));
        }

        writer.newLine();
    }

    private static String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }

        boolean requiresQuotes =
                value.contains(",")
                        || value.contains("\"")
                        || value.contains("\n")
                        || value.contains("\r");

        if (!requiresQuotes) {
            return value;
        }

        return "\""
                + value.replace("\"", "\"\"")
                + "\"";
    }
}