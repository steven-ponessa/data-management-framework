package com.ibm.wfm.beans;

import java.util.Date;

public class TaxonomyEvaluationResponse {
	private Date startTime = new Date();
	private Date taxUploadStartTime;
	private Date dataUploadStartTime;
	private Date evaluationStartTime;
	private Date statisticsStartTime;
	private Date completionTime;
	
	private int taxonomyLevels;
	private int totalRecords;
	private int validRecords;
	private double percentValidRecords;
	private int invalidBrachRecords;
	private double percentInvalidBrachRecords;
	private int invalidNodeRecords;
	private double percentInvalidNodeRecords;
	private boolean tieOut;
	private boolean outputErrorsOnly;
	private String outputDatasetUrl;
	
	public TaxonomyEvaluationResponse(Date startTime, Date taxUploadStartTime, Date dataUploadStartTime,
			Date evaluationStartTime, Date statisticsStartTime, Date completionTime, int taxonomyLevels,
			int totalRecords, int validRecords, double percentValidRecords, int invalidBrachRecords,
			double percentInvalidBrachRecords, int invalidNodeRecords, double percentInvalidNodeRecords, boolean tieOut,
			boolean outputErrorsOnly, String outputDatasetUrl) {
		super();
		this.startTime = startTime;
		this.taxUploadStartTime = taxUploadStartTime;
		this.dataUploadStartTime = dataUploadStartTime;
		this.evaluationStartTime = evaluationStartTime;
		this.statisticsStartTime = statisticsStartTime;
		this.completionTime = completionTime;
		this.taxonomyLevels = taxonomyLevels;
		this.totalRecords = totalRecords;
		this.validRecords = validRecords;
		this.percentValidRecords = percentValidRecords;
		this.invalidBrachRecords = invalidBrachRecords;
		this.percentInvalidBrachRecords = percentInvalidBrachRecords;
		this.invalidNodeRecords = invalidNodeRecords;
		this.percentInvalidNodeRecords = percentInvalidNodeRecords;
		this.tieOut = tieOut;
		this.outputErrorsOnly = outputErrorsOnly;
		this.outputDatasetUrl = outputDatasetUrl;
	}	
	
	public TaxonomyEvaluationResponse(int taxonomyLevels, int totalRecords, int validRecords,
			double percentValidRecords, int invalidBrachRecords, double percentInvalidBrachRecords,
			int invalidNodeRecords, double percentInvalidNodeRecords, boolean tieOut, String outputDatasetUrl) {
		super();
		this.taxonomyLevels = taxonomyLevels;
		this.totalRecords = totalRecords;
		this.validRecords = validRecords;
		this.percentValidRecords = percentValidRecords;
		this.invalidBrachRecords = invalidBrachRecords;
		this.percentInvalidBrachRecords = percentInvalidBrachRecords;
		this.invalidNodeRecords = invalidNodeRecords;
		this.percentInvalidNodeRecords = percentInvalidNodeRecords;
		this.tieOut = tieOut;
		this.outputDatasetUrl = outputDatasetUrl;
	}

	public int getTaxonomyLevels() {
		return taxonomyLevels;
	}

	public void setTaxonomyLevels(int taxonomyLevels) {
		this.taxonomyLevels = taxonomyLevels;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getValidRecords() {
		return validRecords;
	}

	public void setValidRecords(int validRecords) {
		this.validRecords = validRecords;
	}

	public double getPercentValidRecords() {
		return percentValidRecords;
	}

	public void setPercentValidRecords(double percentValidRecords) {
		this.percentValidRecords = percentValidRecords;
	}

	public int getInvalidBrachRecords() {
		return invalidBrachRecords;
	}

	public void setInvalidBrachRecords(int invalidBrachRecords) {
		this.invalidBrachRecords = invalidBrachRecords;
	}

	public double getPercentInvalidBrachRecords() {
		return percentInvalidBrachRecords;
	}

	public void setPercentInvalidBrachRecords(double percentInvalidBrachRecords) {
		this.percentInvalidBrachRecords = percentInvalidBrachRecords;
	}

	public int getInvalidNodeRecords() {
		return invalidNodeRecords;
	}

	public void setInvalidNodeRecords(int invalidNodeRecords) {
		this.invalidNodeRecords = invalidNodeRecords;
	}

	public double getPercentInvalidNodeRecords() {
		return percentInvalidNodeRecords;
	}

	public void setPercentInvalidNodeRecords(double percentInvalidNodeRecords) {
		this.percentInvalidNodeRecords = percentInvalidNodeRecords;
	}

	public boolean isTieOut() {
		return tieOut;
	}

	public void setTieOut(boolean tieOut) {
		this.tieOut = tieOut;
	}

	@Override
	public String toString() {
		return "TaxonomyEvaluationResponse [taxonomyLevels=" + taxonomyLevels + ", totalRecords=" + totalRecords
				+ ", validRecords=" + validRecords + ", percentValidRecords=" + percentValidRecords
				+ ", invalidBrachRecords=" + invalidBrachRecords + ", percentInvalidBrachRecords="
				+ percentInvalidBrachRecords + ", invalidNodeRecords=" + invalidNodeRecords
				+ ", percentInvalidNodeRecords=" + percentInvalidNodeRecords + ", tieOut=" + tieOut + "]";
	}

	public String getOutputDatasetUrl() {
		return outputDatasetUrl;
	}

	public void setOutputDatasetUrl(String outputDatasetUrl) {
		this.outputDatasetUrl = outputDatasetUrl;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getTaxUploadStartTime() {
		return taxUploadStartTime;
	}

	public void setTaxUploadStartTime(Date taxUploadStartTime) {
		this.taxUploadStartTime = taxUploadStartTime;
	}

	public Date getDataUploadStartTime() {
		return dataUploadStartTime;
	}

	public void setDataUploadStartTime(Date dataUploadStartTime) {
		this.dataUploadStartTime = dataUploadStartTime;
	}

	public Date getEvaluationStartTime() {
		return evaluationStartTime;
	}

	public void setEvaluationStartTime(Date evaluationStartTime) {
		this.evaluationStartTime = evaluationStartTime;
	}

	public Date getStatisticsStartTime() {
		return statisticsStartTime;
	}

	public void setStatisticsStartTime(Date statisticsStartTime) {
		this.statisticsStartTime = statisticsStartTime;
	}

	public Date getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

	public boolean isOutputErrorsOnly() {
		return outputErrorsOnly;
	}

	public void setOutputErrorsOnly(boolean outputErrorsOnly) {
		this.outputErrorsOnly = outputErrorsOnly;
	}
	
}
