package com.ibm.wfm.services;

import java.util.ArrayList;
import java.util.List;

import com.ibm.wfm.beans.sdss.SdssAttributeError;
import com.ibm.wfm.beans.sdss.SdssChangeRequestPendingDim;
import com.ibm.wfm.beans.sdss.SdssValidationRecordError;
import com.ibm.wfm.beans.sdss.SdssValidationResponse;
import com.ibm.wfm.beans.sdss.SdssValidationSummary;
import com.ibm.wfm.utils.DataMarshaller;

public class SdssCrValidationService extends AbstractValidationService {
	
	public SdssCrValidationService() {
		
	}
	
	
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
        List<SdssChangeRequestPendingDim> sdssChangeRequests =
            DataMarshaller.getObjectListFromExcel(
            	SdssChangeRequestPendingDim.class,
                excelFilePathName,
                excelTabName
            );
        
        for (SdssChangeRequestPendingDim sdssChangeRequestPending : sdssChangeRequests) {
            rowNumber++;

            // Apply all validation rules to the current SDSS record.
            List<SdssAttributeError> errors = validateRow(sdssChangeRequestPending);

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
    
    private List<SdssAttributeError> validateRow(SdssChangeRequestPendingDim row) {
        List<SdssAttributeError> errors = new ArrayList<>();
        System.out.println(row);
        return errors;
    }
        
}
