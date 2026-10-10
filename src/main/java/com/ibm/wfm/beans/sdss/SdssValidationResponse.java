package com.ibm.wfm.beans.sdss;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public record SdssValidationResponse(
	    SdssValidationSummary summary,
	    List<SdssValidationRecordError> detail
	) {

	public void writeExcel(Path outputFile) throws IOException {
	    SdssValidationExcelRenderer.write(this, outputFile);
	}
	
	public String toMarkdown() {
	
	    StringBuilder sb = new StringBuilder();
	
	    sb.append("# SDSS Validation Results\n\n");
	    sb.append("## Summary\n\n");
	    sb.append("| Metric | Value |\n");
	    sb.append("|---|---:|\n");
	    sb.append("| Total Records | ").append(summary.totalRecords()).append(" |\n");
	    sb.append("| Valid Records | ").append(summary.validRecords()).append(" |\n");
	    sb.append("| Invalid Records | ").append(summary.invalidRecords()).append(" |\n\n");
	
	    sb.append("## Detail\n\n");
	    sb.append("| Row | Attribute | Value | Reason | Valid Branch |\n");
	    sb.append("|---:|---|---|---|---|\n");
	
	    for (SdssValidationRecordError record : detail) {
	        for (SdssAttributeError error : record.invalidAttributes()) {
	            sb.append("| ")
	              .append(record.rowNumber())
	              .append(" | ")
	              .append(escape(error.attributeName()))
	              .append(" | ")
	              .append(escape(error.value()))
	              .append(" | ")
	              .append(escape(error.reason()))
	              .append(" |")
	              .append(escape(error.validBranchAsText()))
	              .append(" |\n");
	        }
	    }
	
	    return sb.toString();
	}
	
	private static String escape(String value) {
	    if (value == null) return "";
	    return value.replace("|", "\\|");
	}
	
	public boolean hasErrors() {
		//return !detail.isEmpty();
		return summary().invalidRecords()>0;
	}
	public int errorCount() {
		return detail.stream()
	            .mapToInt(r -> r.invalidAttributes().size())
	            .sum();
	}
}