package com.ibm.wfm.beans.taxonomies;

import java.util.List;

public record TaxonomyTestCase(
	    String testCaseId,
	    String testType,
	    String matchMode,
	    Boolean expectedValid,
	    String expectedStatus,
	    String nodeCode,
	    Integer nodeLevel,
	    Integer leafLevel,
	    List<String> branchCodes
	) {}