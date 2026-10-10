package com.ibm.wfm.beans.sdss;

public record SdssValidationSummary(
	    int totalRecords,
	    int validRecords,
	    int invalidRecords
	) {}