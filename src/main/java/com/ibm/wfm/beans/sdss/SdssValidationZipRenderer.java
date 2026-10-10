package com.ibm.wfm.beans.sdss;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ibm.wfm.utils.NonClosingOutputStream;

public final class SdssValidationZipRenderer {

    private SdssValidationZipRenderer() {}

    public static void write(
            SdssValidationResponse validationResponse,
            Path uploadedFile,
            OutputStream outputStream
    ) throws IOException {

        Set<Integer> invalidRowNumbers =
                getInvalidRowNumbers(validationResponse);

        try (
            ZipOutputStream zipOutputStream =
                    new ZipOutputStream(
                            new BufferedOutputStream(outputStream)
                    )
        ) {
            writeValidationErrors(
                    zipOutputStream,
                    validationResponse
            );

            writeValidRecordsCsv(
                    zipOutputStream,
                    uploadedFile,
                    invalidRowNumbers
            );

            writeManifest(
                    zipOutputStream,
                    validationResponse,
                    invalidRowNumbers
            );

            zipOutputStream.finish();
        }
    }

    private static void writeValidationErrors(
            ZipOutputStream zipOutputStream,
            SdssValidationResponse validationResponse
    ) throws IOException {

        ZipEntry zipEntry =
                new ZipEntry(
                        "sdss-validation-errors.xlsx"
                );

        zipOutputStream.putNextEntry(zipEntry);

        SdssValidationExcelRenderer.write(
                validationResponse,
                new NonClosingOutputStream(zipOutputStream)
        );

        zipOutputStream.closeEntry();
    }

    private static void writeValidRecordsCsv(
            ZipOutputStream zipOutputStream,
            Path uploadedFile,
            Set<Integer> invalidRowNumbers
    ) throws IOException {

        ZipEntry zipEntry =
                new ZipEntry(
                        "sdss-valid-records.csv"
                );

        zipOutputStream.putNextEntry(zipEntry);

        SdssValidRecordsCsvRenderer.write(
                uploadedFile,
                invalidRowNumbers,
                zipOutputStream
        );

        zipOutputStream.closeEntry();
    }

    private static void writeManifest(
            ZipOutputStream zipOutputStream,
            SdssValidationResponse response,
            Set<Integer> invalidRowNumbers
    ) throws IOException {

        ZipEntry zipEntry =
                new ZipEntry(
                        "validation-summary.json"
                );

        zipOutputStream.putNextEntry(zipEntry);

        String json = """
                {
                  "totalRecords": %d,
                  "validRecords": %d,
                  "invalidRecords": %d,
                  "invalidSourceRows": %d,
                  "validRecordsFile": "sdss-valid-records.csv",
                  "validationErrorsFile": "sdss-validation-errors.xlsx"
                }
                """.formatted(
                    response.summary().totalRecords(),
                    response.summary().validRecords(),
                    response.summary().invalidRecords(),
                    invalidRowNumbers.size()
                );

        zipOutputStream.write(
                json.getBytes(StandardCharsets.UTF_8)
        );

        zipOutputStream.closeEntry();
    }

    private static Set<Integer> getInvalidRowNumbers(
            SdssValidationResponse response
    ) {
        if (response == null || response.detail() == null) {
            return Set.of();
        }

        return response.detail()
                .stream()
                .map(SdssValidationRecordError::rowNumber)
                .collect(Collectors.toUnmodifiableSet());
    }
}