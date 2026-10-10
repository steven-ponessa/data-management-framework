package com.ibm.wfm.beans.sdss;

public record SdssAttributeError(
	    String attributeName,
	    String value,
	    String reason,
	    String validBranchAsText,
	    String[] validBranchAsNodeArray
	) {
    public SdssAttributeError(
            String attributeName,
            String value,
            String reason
        ) {
            this(attributeName, value, reason, null, null);
    }
}