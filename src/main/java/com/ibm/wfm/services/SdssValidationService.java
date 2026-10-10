package com.ibm.wfm.services;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.SourcingStrategyDim;
import com.ibm.wfm.beans.sdss.SdssAttributeError;
import com.ibm.wfm.beans.sdss.SdssExceptionDim;
import com.ibm.wfm.beans.sdss.SdssReferenceData;
import com.ibm.wfm.beans.sdss.SdssStagingDim;
import com.ibm.wfm.beans.sdss.SdssValidationRecordError;
import com.ibm.wfm.beans.sdss.SdssValidationResponse;
import com.ibm.wfm.beans.sdss.SdssValidationSummary;
import com.ibm.wfm.beans.taxonomies.TaxonomyBranchValidationResult;
import com.ibm.wfm.beans.taxonomies.TaxonomyMatchMode;
import com.ibm.wfm.utils.DataMarshaller;
import com.opencsv.CSVReader;

/**
 * Service responsible for validating SDSS staging records before they are
 * accepted for downstream processing.
 * <p>
 * Validation is performed against both structural rules (required fields and
 * maximum lengths) and SDSS reference data such as allowed value lists,
 * sourcing strategies, and the JRS taxonomy hierarchy.
 * </p>
 * <p>
 * The service supports validation of records originating from either an Excel
 * worksheet or a CSV file. Both input paths ultimately call
 * {@link #validateRow(SdssStagingDim)} so that the same business validation
 * rules are applied regardless of source format.
 * </p>
 */
public class SdssValidationService {
	
	private static int TAXONOMY_JRS = 0;
	private static int TAXONOMY_SDSS_GEO = 6;

    /**
     * Maximum supported length for validated SDSS string attributes.
     */
    private static final int MAX_LEN = 128;

    /**
     * Snapshot of the reference data used to validate the current SDSS upload.
     * This includes the JRS taxonomy, SDSS Geography taxonomy, allowed-value sets, 
     * and sourcing strategy reference records.
     */
    private final SdssReferenceData referenceData;

    /**
     * Creates a validation service using the supplied SDSS reference data.
     *
     * @param referenceData reference data to use for all validation performed
     *                      by this service instance
     */
    public SdssValidationService(SdssReferenceData referenceData) {
        this.referenceData = referenceData;
    }

    /**
     * Validates all SDSS records contained in an Excel worksheet.
     * <p>
     * The worksheet is unmarshalled into {@link SdssStagingDim} objects and
     * each record is passed through the common row-level validation logic.
     * Validation errors are collected by source row and returned together with
     * a summary of valid and invalid records.
     * </p>
     *
     * @param excelFilePathName full path to the Excel workbook
     * @param excelTabName      worksheet containing the SDSS data
     * @return validation summary and detailed record-level validation errors
     * @throws Exception if the workbook cannot be read or converted into SDSS
     *                   staging records
     */
    public SdssValidationResponse validateExcel(
            String excelFilePathName,
            String excelTabName
    ) throws Exception {
        List<SdssValidationRecordError> detail = new ArrayList<>();
        int rowNumber = 0;
        int dataRows = 0;

        // Used only to provide lightweight progress output while large Excel
        // uploads are being validated.
        int bufferSize = 100;

        // Convert each row in the configured Excel tab directly into the SDSS
        // staging bean used by the validation routines below.
        List<SdssStagingDim> sdssStagings =
            DataMarshaller.getObjectListFromExcel(
                SdssStagingDim.class,
                excelFilePathName,
                excelTabName
            );

        for (SdssStagingDim sdssStaging : sdssStagings) {
            rowNumber++;

            // Apply all validation rules to the current SDSS record.
            List<SdssAttributeError> errors = validateRow(sdssStaging);

            // Only records containing one or more validation errors need to be
            // retained in the detailed response.
            if (!errors.isEmpty()) {
                detail.add(new SdssValidationRecordError(rowNumber, errors));
            }

            // Emit simple progress markers so long-running validation can be
            // observed without logging every individual record.
            if (rowNumber % bufferSize == 0) {
                System.out.println(rowNumber);
            }
            else if (rowNumber % (bufferSize / 2) == 0) {
                System.out.print("*");
            }
            else if (rowNumber % (bufferSize / 10) == 0) {
                System.out.print(".");
            }
        }

        // An Excel row is considered valid if no validation errors were
        // recorded for that row.
        SdssValidationSummary summary = new SdssValidationSummary(
            rowNumber,
            rowNumber - detail.size(),
            detail.size()
        );

        return new SdssValidationResponse(summary, detail);
    }

    /**
     * Validates SDSS records contained in a CSV file.
     * <p>
     * CSV rows are converted into {@link SdssStagingDim} instances using the
     * same marshalling metadata used elsewhere in the application. The common
     * {@link #validateRow(SdssStagingDim)} method then applies the same business
     * rules used for Excel validation.
     * </p>
     *
     * @param inputFile  path to the CSV file
     * @param skipHeader when {@code true}, the first physical CSV row is
     *                   treated as a header and is not validated
     * @return validation summary and detailed record-level validation errors
     * @throws Exception if the CSV cannot be read or a row cannot be converted
     *                   into an SDSS staging object
     */
    public SdssValidationResponse validateCsv(
            Path inputFile,
            boolean skipHeader
    ) throws Exception {

        List<SdssValidationRecordError> detail = new ArrayList<>();

        // rowNumber tracks the physical CSV row so that validation errors can
        // be associated with the row number seen by the user.
        int rowNumber = 0;

        // dataRows excludes the optional CSV header and therefore represents
        // the actual number of SDSS data records evaluated.
        int dataRows = 0;

        try (CSVReader csvReader = new CSVReader(new FileReader(inputFile.toFile()))) {
            String[] values;

            while ((values = csvReader.readNext()) != null) {
                rowNumber++;

                // Preserve the physical row count, but do not include the
                // header in the number of validated data records.
                if (skipHeader && rowNumber == 1) {
                    continue;
                }

                dataRows++;

                // Build the SDSS staging bean from the CSV column values.
                SdssStagingDim row =
                    (SdssStagingDim) DataMarshaller.buildObjectFromList(
                        SdssStagingDim.class,
                        values
                    );

                List<SdssAttributeError> errors = validateRow(row);

                if (!errors.isEmpty()) {
                    detail.add(new SdssValidationRecordError(rowNumber, errors));
                }
            }
        }

        SdssValidationSummary summary = new SdssValidationSummary(
            dataRows,
            dataRows - detail.size(),
            detail.size()
        );

        return new SdssValidationResponse(summary, detail);
    }

    /**
     * Applies all SDSS validation rules to one staging record.
     * <p>
     * Validation is performed in four broad groups:
     * </p>
     * <ol>
     *   <li>Required-value and maximum-length checks.</li>
     *   <li>JRS taxonomy branch validation.</li>
     *   <li>Allowed-value validation for controlled SDSS attributes.</li>
     *   <li>Sourcing Strategy validation against current reference records.</li>
     * </ol>
     *
     * @param row SDSS staging record to validate
     * @return all attribute-level validation errors found for the record
     */
    private List<SdssAttributeError> validateRow(SdssStagingDim row) {
        List<SdssAttributeError> errors = new ArrayList<>();

        /*
         * ------------------------------------------------------------------
         * Required field and maximum-length validation
         * ------------------------------------------------------------------
         *
         * The boolean argument indicates whether the field is nullable.
         * Required fields pass false; optional fields pass true.
         */
        validateRequiredAndLength(
            errors,
            "Geography Type Name",
            row.getGeographyTypeNm(),
            false
        );
        validateRequiredAndLength(
            errors,
            "Geography Name",
            row.getGeographyNm(),
            false
        );
        validateRequiredAndLength(
            errors,
            "Market Name",
            row.getMarketNm(),
            false
        );
        validateRequiredAndLength(
            errors,
            "Market Region Name",
            row.getMarketRegionNm(),
            true
        );
        validateRequiredAndLength(
            errors,
            "Country Name",
            row.getCountryNm(),
            false
        );

        validateRequiredAndLength(
            errors,
            "Growth Platform Name",
            row.getGrowthPlatformNm(),
            false
        );
        validateRequiredAndLength(
            errors,
            "Service Line Name",
            row.getServiceLineNm(),
            false
        );
        validateRequiredAndLength(
            errors,
            "Practice Name",
            row.getPracticeNm(),
            false
        );
        validateRequiredAndLength(
            errors,
            "JRS Name",
            row.getJrsNm(),
            false
        );
        validateRequiredAndLength(
            errors,
            "JRS Id",
            row.getJrsId(),
            false
        );

        // Band Category is currently treated as nullable. The existing source
        // comment indicates that this was previously/non-finally intended to
        // be required, so the current behavior is deliberately preserved.
        validateRequiredAndLength(
            errors,
            "Band Category Name",
            row.getBandCatNm(),
            true
        ); // false - set to true right now

        validateRequiredAndLength(
            errors,
            "Market Scarcity Index Name",
            row.getMsiNm(),
            true
        );
        validateRequiredAndLength(
            errors,
            "Anticipated Demand for Skill Name",
            row.getAdsNm(),
            false
        );
        validateRequiredAndLength(
            errors,
            "Sourcing Strategy Name",
            row.getSourcingStrategyNm(),
            false
        );
        
        /*
         * ------------------------------------------------------------------
         * SDSS Geography taxonomy validation
         * ------------------------------------------------------------------
         *
         * SDSS stores the taxonomy values as descriptions. Build the branch
         * that should exist in the Geography taxonomy and validate it using
         * TaxonomyMatchMode.DESCRIPTION.
         *
         * The SDSS data being validated contains the following taxonomy
         * levels:
         *   0 - Geography Type
         *   1 - Geography
         *   2 - Market
         *   3 - Region
         *   4 - County
         *
         */
        TaxonomyBranchValidationResult geoResult = null;

        List<String> geoBranchValues = List.of(
            row.getGeographyTypeNm(),
            row.getGeographyNm(),
            row.getMarketNm(),
            row.getMarketRegionNm(),
            row.getCountryNm()
        );

        List<Integer> geoBranchLevels = List.of(
            0,
            1,
            2,
            3,
            4
        );

        /*
         * First locate the Geography Type root corresponding to the value
         * supplied by the SDSS row.
         *
         * Unlike the JRS taxonomy, the Geography taxonomy has multiple roots,
         * so validation should be performed only against the root identified
         * by Geography Type.
         */
        NaryTreeNode geographyType = referenceData.getGeographyTypes()
            .stream()
            .filter(geoType ->
                geoType.getDescription() != null &&
                geoType.getDescription().equalsIgnoreCase(row.getGeographyTypeNm())
            )
            .findFirst()
            .orElse(null);

        if (geographyType == null) {

            /*
             * The Geography Type itself is invalid. Since no matching taxonomy
             * root exists, there is no meaningful branch validation to perform.
             */
            errors.add(new SdssAttributeError(
                getAttributeName(0, TAXONOMY_SDSS_GEO),
                row.getGeographyTypeNm(),
                "Invalid Geography Type: " + row.getGeographyTypeNm(),
                null,
                null
            ));

        }
        else {

            geoResult = geographyType.validateBranchByLevels(
                geoBranchValues,
                geoBranchLevels,
                TaxonomyMatchMode.DESCRIPTION
            );

            /*
             * A null result is not expected, but handle it defensively so the
             * validation process returns a controlled error rather than throwing
             * a NullPointerException.
             */
            if (geoResult == null) {

                errors.add(new SdssAttributeError(
                    "Unknown",
                    null,
                    "Geography validation failed with an unknown error.",
                    null,
                    null
                ));

            }
            else if (!geoResult.valid()) {

                /*
                 * Preserve the taxonomy validation diagnostics in the SDSS error:
                 * - the attribute corresponding to the failed taxonomy level,
                 * - the value that failed,
                 * - the detailed taxonomy validation message,
                 * - a readable representation of a valid branch, and
                 * - the valid branch represented as code/description values for
                 *   downstream error reporting.
                 */
                errors.add(new SdssAttributeError(
                    getAttributeName(geoResult.failedLevel(), TAXONOMY_SDSS_GEO),
                    geoResult.failedCode(),
                    geoResult.message() != null
                        ? geoResult.message()
                        : "Geography validation failed with an unknown error.",
                    geoResult.validBranchAsText("->"),
                    geoResult.validBranch2CdDescArray(
                        geoResult.validBranchForLeaf()
                    )
                ));
            }
        }

        /*
         * ------------------------------------------------------------------
         * JRS taxonomy validation
         * ------------------------------------------------------------------
         *
         * SDSS stores the taxonomy values as descriptions. Build the branch
         * that should exist in the JRS taxonomy and validate it using
         * TaxonomyMatchMode.DESCRIPTION.
         *
         * The SDSS data being validated contains the following taxonomy
         * levels:
         *   0 - Brand
         *   1 - Growth Platform
         *   2 - Service Line
         *   3 - Practice
         *   5 - JRS
         *
         * Service Area (level 4) is intentionally omitted, so the branch is
         * validated by explicitly supplying the corresponding taxonomy levels.
         */
		Optional<NaryTreeNode> jrsNode = referenceData.getBrand().findByDescriptionAndLevel(row.getJrsNm(), 5);
		
		//if (jrsNode==null) {
		if (jrsNode.isEmpty()) {
            errors.add(new SdssAttributeError(
                "JRS",
                row.getJrsNm(),
                "Invalid JRS: " + row.getJrsNm(),
                null,
                null
            ));

        }
        else {

			TaxonomyBranchValidationResult result = null;
			
			List<String> branchCodes = new ArrayList<>();
			branchCodes.add("IBM Consulting");
			branchCodes.add(row.getGrowthPlatformNm());
			branchCodes.add(row.getServiceLineNm());
			branchCodes.add(row.getPracticeNm());
			branchCodes.add(row.getJrsNm());
			
			List<Integer> branchLevels = List.of(
			    0,
			    1,
			    2,
			    3,
			    5
			);
			
			result = referenceData.getBrand().validateBranchByLevels(
			    branchCodes,
			    branchLevels,
			    TaxonomyMatchMode.DESCRIPTION
			);
			
			// A null result is not expected, but it is handled defensively so that
			// validation can return a controlled error instead of failing with a
			// NullPointerException.
			if (result == null) {
			    errors.add(new SdssAttributeError(
			        "Unknown",
			        null,
			        "Validation failed with an unknown error.",
			        null,
			        null
			    ));
			}
			else if (!result.valid()) {
			    /*
			     * Preserve the taxonomy validation diagnostics in the SDSS error:
			     * - the attribute corresponding to the failed taxonomy level,
			     * - the value/code that failed,
			     * - the detailed taxonomy validation message,
			     * - a readable representation of a valid branch, and
			     * - the valid branch represented as code/description values for
			     *   downstream error reporting (for example, the validation
			     *   workbook returned to the user).
			     */
			    errors.add(new SdssAttributeError(
			        getAttributeName(result.failedLevel(), TAXONOMY_JRS),
			        result.failedCode(),
			        result.message() != null
			            ? result.message()
			            : "Validation failed with an unknown error.",
			        result.validBranchAsText("->"),
			        result.validBranch2CdDescArray(result.validBranchForLeaf())
			    ));
			}
        }

        /*
         * ------------------------------------------------------------------
         * Controlled-value validation
         * ------------------------------------------------------------------
         *
         * These fields must match the values loaded into SdssReferenceData.
         * Band Category and Market Scarcity Index are currently nullable;
         * Anticipated Demand is required.
         */
        validateAllowed(
            errors,
            "Band Category Name",
            row.getBandCatNm(),
            referenceData.bandCategories(),
            true
        );
        validateAllowed(
            errors,
            "Market Scarcity Index Name",
            row.getMsiNm(),
            referenceData.marketScarcityIndexes(),
            true
        );
        validateAllowed(
            errors,
            "Anticipated Demand for Skill Name",
            row.getAdsNm(),
            referenceData.anticipatedDemandValues()
        );

        // Sourcing Strategy is validated against the full collection of
        // SourcingStrategyDim records because the allowed value is represented
        // as an attribute on a reference bean rather than as a simple Set.
        validateAllowedSourcingStrategy(
            errors,
            row.getSourcingStrategyNm(),
            referenceData.getSourcingStrategyDims(),
            false
        );
        
        validateAllowedSdssExceptions(
    	        errors,
    	        row.getExceptionCd(),
    	        referenceData.getSdssExceptions(),
    	        true
    	);

        return errors;
    }

    /**
     * Converts a taxonomy level number into the user-facing SDSS attribute
     * name to report when branch validation fails.
     *
     * @param failedLevel taxonomy level returned by branch validation
     * @return corresponding attribute name, or an "Unknown" description when
     *         no recognized level is available
     */
    private String getAttributeName(Integer failedLevel, int taxonomy) {
        if (failedLevel == null) {
            return "Unknown";
        }

        failedLevel += taxonomy;
        return switch (failedLevel) {
            case 0 -> "Brand";
            case 1 -> "Growth Platform";
            case 2 -> "Service Line";
            case 3 -> "Practice";
            case 4 -> "Service Area";
            case 5 -> "JRS";
            case 6 -> "Geography Type";
            case 7 -> "Geography";
            case 8 -> "Market";
            case 9 -> "Market Region";
            case 10 -> "Country";
            default -> "Unknown level (" + failedLevel + ")";
        };
    }

    /**
     * Validates that an attribute is populated when required and does not
     * exceed the SDSS maximum string length.
     *
     * @param errors        collection to which validation failures are added
     * @param attributeName user-facing attribute name used in error reporting
     * @param value         attribute value to validate
     * @param nullable      {@code true} if a null/blank value is permitted
     */
    private void validateRequiredAndLength(
            List<SdssAttributeError> errors,
            String attributeName,
            String value,
            boolean nullable
    ) {
        // Required-value validation is performed first. Returning immediately
        // prevents an unnecessary length check after the required-field error.
        if (!nullable && isBlank(value)) {
            errors.add(new SdssAttributeError(
                attributeName,
                value,
                "Value is required"
            ));
            return;
        }

        if (value != null && value.length() > MAX_LEN) {
            errors.add(new SdssAttributeError(
                attributeName,
                value,
                "Value exceeds 128 characters"
            ));
        }
    }

    /**
     * Convenience overload for validating a required controlled-value field.
     *
     * @param errors        collection to which validation failures are added
     * @param attributeName user-facing attribute name used in error reporting
     * @param value         value to validate
     * @param allowed       normalized set of allowed values
     */
    private void validateAllowed(
            List<SdssAttributeError> errors,
            String attributeName,
            String value,
            Set<String> allowed
    ) {
        validateAllowed(errors, attributeName, value, allowed, false);
    }

    /**
     * Validates a value against a controlled set of allowed values.
     * <p>
     * Input is normalized before comparison so differences in surrounding
     * whitespace or letter case do not cause otherwise valid values to fail.
     * </p>
     *
     * @param errors        collection to which validation failures are added
     * @param attributeName user-facing attribute name used in error reporting
     * @param value         value to validate
     * @param allowed       normalized set of allowed values
     * @param nullable      {@code true} if null/blank values are permitted
     */
    private void validateAllowed(
            List<SdssAttributeError> errors,
            String attributeName,
            String value,
            Set<String> allowed,
            boolean nullable
    ) {
        // Optional controlled values do not need an allowed-list lookup when
        // the incoming value is null or blank.
        if (nullable && isBlank(value)) {
            return;
        }

        if (!allowed.contains(normalize(value))) {
            errors.add(new SdssAttributeError(
                attributeName,
                value,
                "Value is not in the allowed list"
            ));
        }
    }

    /**
     * Validates Sourcing Strategy against the current sourcing strategy
     * reference records.
     * <p>
     * Unlike the other controlled values, Sourcing Strategy is represented by
     * {@link SourcingStrategyDim} records, so validation searches the list for
     * an exact matching sourcing strategy name.
     * </p>
     *
     * @param errors               collection to which validation failures are
     *                             added
     * @param srcStrgyNm           sourcing strategy name to validate
     * @param sourcingStrategyDims current sourcing strategy reference records
     * @param nullable             {@code true} if null/blank values are allowed
     */
    private void validateAllowedSourcingStrategy(
            List<SdssAttributeError> errors,
            String srcStrgyNm,
            List<SourcingStrategyDim> sourcingStrategyDims,
            boolean nullable
    ) {
        if (nullable && isBlank(srcStrgyNm)) {
            return;
        }

        // Objects.equals safely handles null values while preserving the
        // existing exact/case-sensitive comparison behavior.
        boolean found = sourcingStrategyDims.stream()
            .anyMatch(dim ->
                Objects.equals(dim.getSrcStrgyNm(), srcStrgyNm));

        if (!found) {
            errors.add(new SdssAttributeError(
                "Sourcing Strategy",
                srcStrgyNm,
                "Value is not in the allowed list"
            ));
        }
    }
    
    /**
     * Validates Sourcing Strategy against the current sourcing strategy
     * reference records.
     * <p>
     * Unlike the other controlled values, Sourcing Strategy is represented by
     * {@link SourcingStrategyDim} records, so validation searches the list for
     * an exact matching sourcing strategy name.
     * </p>
     *
     * @param errors               collection to which validation failures are
     *                             added
     * @param srcStrgyNm           sourcing strategy name to validate
     * @param sourcingStrategyDims current sourcing strategy reference records
     * @param nullable             {@code true} if null/blank values are allowed
     */
	private void validateAllowedSdssExceptions(
	        List<SdssAttributeError> errors,
	        String sdssExceptionCd,
	        List<SdssExceptionDim> sdssExceptions,
	        boolean nullable
	) {
	    if (nullable && isBlank(sdssExceptionCd)) {
	        return;
	    }
	
	    // Objects.equals safely handles null values while preserving the
	    // existing exact/case-sensitive comparison behavior.
	    boolean found = sdssExceptions.stream()
	        .anyMatch(dim ->
	            Objects.equals(dim.getExceptionCd(), sdssExceptionCd));
	
	    if (!found) {
	        errors.add(new SdssAttributeError(
	            "SDSS Exception Code",
	            sdssExceptionCd,
	            "Value is not in the allowed list"
	        ));
	    }
	}    

    /**
     * Determines whether a String should be treated as having no value.
     *
     * @param value value to inspect
     * @return {@code true} when the value is null, empty, or only whitespace
     */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Normalizes a controlled-value String before allowed-list comparison.
     *
     * @param value value to normalize
     * @return trimmed upper-case representation, or an empty String for null
     */
    private static String normalize(String value) {
        return value == null
            ? ""
            : value.trim().toUpperCase(Locale.ROOT);
    }
}