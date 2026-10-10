package com.ibm.wfm.services;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.EtlResponse;

@Service
public class ExcelUploadEtlService {
	
	/*

    private final PitExportService pitExportService;
    private final ExcelToCsvService excelToCsvService;
    private final DeltaApplyService deltaApplyService;

    public <T> EtlResponse processExcelUpload(
            Class<T> type,
            MultipartFile workbook,
            int keyLength,
            String outputFileName
    ) throws Exception {

        UploadMetadata metadata = excelToCsvService.readMetadata(workbook);

        validateMetadata(metadata);

        Path oldCsv = pitExportService.generatePitCsv(
            type,
            metadata.getFilters(),
            metadata.getDownloadTimestamp()
        );

        Path newCsv = excelToCsvService.extractDataSheetAsCsv(
            workbook,
            type
        );

        Path deltaCsv = Files.createTempFile("dmf-upload-delta-", ".csv");

        LadderComparatorService.processCsv(
            oldCsv.toString(),
            newCsv.toString(),
            keyLength,
            deltaCsv.toString()
        );

        return deltaApplyService.applyDeltaTransactional(
            type,
            deltaCsv,
            outputFileName
        );
    }

    private void validateMetadata(UploadMetadata metadata) {
        if (metadata.getFilters() == null || metadata.getFilters().isBlank()) {
            throw new IllegalArgumentException("Upload metadata is missing original filter.");
        }

        if (metadata.getDownloadTimestamp() == null) {
            throw new IllegalArgumentException("Upload metadata is missing download timestamp.");
        }
    }
    */
}