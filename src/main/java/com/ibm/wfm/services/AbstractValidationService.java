package com.ibm.wfm.services;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.ibm.wfm.beans.sdss.SdssAttributeError;

public abstract class AbstractValidationService {
	
    /**
     * Maximum supported length for validated SDSS string attributes.
     */
    private static final int MAX_LEN = 128;
	
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
