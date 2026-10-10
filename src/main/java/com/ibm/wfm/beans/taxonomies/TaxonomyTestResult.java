package com.ibm.wfm.beans.taxonomies;

public record TaxonomyTestResult(
	    String testCaseId,
	    String testType,
	    String matchMode,
	    Boolean expectedValid,
	    String expectedStatus,
	    Boolean actualValid,
	    String actualStatus,
	    Integer failedLevel,
	    String failedCode,
	    String message,
	    boolean passed
	) {}