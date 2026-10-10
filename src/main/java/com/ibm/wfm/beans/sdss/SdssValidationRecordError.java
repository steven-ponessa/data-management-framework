package com.ibm.wfm.beans.sdss;

import java.util.List;

public record SdssValidationRecordError(
	    int rowNumber,
	    List<SdssAttributeError> invalidAttributes
		) {

}