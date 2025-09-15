package com.ibm.wfm.beans;

import com.ibm.wfm.utils.Helpers;

public class ServiceAreaMapNode {
	private String growthPlatformNm;
	private String serviceLineNm;
	private String practiceNm;
	private String serviceAreaNm;
	private String newServiceAreaNm;
	private String newPracticeNm;
	private String newServiceLineNm;
	private String newEconomicModelNm;
	private String newGrowthPlatformNm;
	private String practiceTypeNm;
	private String practiceConfirmedInd;
	private String comments;


	public ServiceAreaMapNode(
						  String growthPlatformNm
						, String serviceLineNm
						, String practiceNm
						, String serviceAreaNm
						, String newServiceAreaNm
						, String newPracticeNm
						, String newServiceLineNm
						, String newEconomicModelNm
						, String newGrowthPlatformNm
						, String practiceTypeNm
						, String practiceConfirmedInd
						, String comments) {
		super();
		this.growthPlatformNm = growthPlatformNm;
		this.serviceLineNm = serviceLineNm;
		this.practiceNm = practiceNm;
		this.serviceAreaNm = serviceAreaNm;
		this.newServiceAreaNm = newServiceAreaNm;
		this.newPracticeNm = newPracticeNm;
		this.newServiceLineNm = newServiceLineNm;
		this.newEconomicModelNm = newEconomicModelNm;
		this.newGrowthPlatformNm = newGrowthPlatformNm;
		this.practiceTypeNm = practiceTypeNm;
		this.practiceConfirmedInd = practiceConfirmedInd;
		this.comments = comments;
	}

	public String toString(String useJrsNm) {
		return Helpers.formatCsvField(growthPlatformNm)
				+","+Helpers.formatCsvField(serviceLineNm)
				+","+Helpers.formatCsvField(practiceNm)
				+","+Helpers.formatCsvField(serviceAreaNm)
				+","+Helpers.formatCsvField(newGrowthPlatformNm)
				+","+Helpers.formatCsvField(newServiceLineNm)
				+","+Helpers.formatCsvField(newEconomicModelNm)
				+","+Helpers.formatCsvField(newPracticeNm)
				+","+Helpers.formatCsvField(newServiceAreaNm)
				+","+Helpers.formatCsvField(practiceTypeNm)
				+","+Helpers.formatCsvField(practiceConfirmedInd);
				//+","+Helpers.formatCsvField(comments);
	}
	
	public String echo() {
		return Helpers.formatCsvField(growthPlatformNm)
				+","+Helpers.formatCsvField(serviceLineNm)
				+","+Helpers.formatCsvField(practiceNm)
				+","+Helpers.formatCsvField(serviceAreaNm)
				+","+Helpers.formatCsvField(newServiceAreaNm)
				+","+Helpers.formatCsvField(newPracticeNm)
				+","+Helpers.formatCsvField(newServiceLineNm)
				+","+Helpers.formatCsvField(newEconomicModelNm)
				+","+Helpers.formatCsvField(newGrowthPlatformNm)
				+","+Helpers.formatCsvField(practiceTypeNm)
				+","+Helpers.formatCsvField(practiceConfirmedInd)
		        +","+Helpers.formatCsvField(comments);
	}
	
	public String getPSa() {
		return this.practiceNm.trim()+":"+this.serviceAreaNm.trim()+":";
	}

	public String getGrowthPlatformNm() {
		return growthPlatformNm;
	}

	public void setGrowthPlatformNm(String growthPlatformNm) {
		this.growthPlatformNm = growthPlatformNm;
	}

	public String getServiceLineNm() {
		return serviceLineNm;
	}

	public void setServiceLineNm(String serviceLineNm) {
		this.serviceLineNm = serviceLineNm;
	}

	public String getPracticeNm() {
		return practiceNm;
	}

	public void setPracticeNm(String practiceNm) {
		this.practiceNm = practiceNm;
	}

	public String getServiceAreaNm() {
		return serviceAreaNm;
	}

	public void setServiceAreaNm(String serviceAreaNm) {
		this.serviceAreaNm = serviceAreaNm;
	}

	public String getNewServiceAreaNm() {
		return newServiceAreaNm;
	}

	public void setNewServiceAreaNm(String newServiceAreaNm) {
		this.newServiceAreaNm = newServiceAreaNm;
	}

	public String getNewPracticeNm() {
		return newPracticeNm;
	}

	public void setNewPracticeNm(String newPracticeNm) {
		this.newPracticeNm = newPracticeNm;
	}

	public String getNewServiceLineNm() {
		return newServiceLineNm;
	}

	public void setNewServiceLineNm(String newServiceLineNm) {
		this.newServiceLineNm = newServiceLineNm;
	}

	public String getNewEconomicModelNm() {
		return newEconomicModelNm;
	}

	public void setNewEconomicModelNm(String newEconomicModeNm) {
		this.newEconomicModelNm = newEconomicModeNm;
	}

	public String getNewGrowthPlatformNm() {
		return newGrowthPlatformNm;
	}

	public void setNewGrowthPlatformNm(String newGrowthPlatformNm) {
		this.newGrowthPlatformNm = newGrowthPlatformNm;
	}

	public String getPracticeTypeNm() {
		return practiceTypeNm;
	}

	public void setPracticeTypeNm(String practiceTypeNm) {
		this.practiceTypeNm = practiceTypeNm;
	}

	public String getPracticeConfirmedInd() {
		return practiceConfirmedInd;
	}

	public void setPracticeConfirmedInd(String practiceConfirmedInd) {
		this.practiceConfirmedInd = practiceConfirmedInd;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
