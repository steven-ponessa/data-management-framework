package com.ibm.wfm.beans;

import com.ibm.wfm.utils.Helpers;

public class JrsMapNode {
	private String growthPlatformNm;
	private String serviceLineNm;
	private String practiceNm;
	private String serviceAreaNm;
	private String capacityGroupNm;
	private String jrsNm;
	private String newGrowthPlatformNm;
	private String newServiceLineNm;
	private String newPracticeNm;
	private String economicModelNm;
	private String newServiceAreaNm;
	private String newJrsNm;
	private String practiceTypeNm;
	private String jrsActionNm;
	private String comments;
	
	public JrsMapNode(
			  String growthPlatformNm
			, String serviceLineNm
			, String practiceNm
			, String serviceAreaNm
			, String capacityGroupNm
			, String jrsNm
			, String newGrowthPlatformNm
			, String newServiceLineNm
			, String newPracticeNm
			, String economicModelNm
			, String newServiceAreaNm
			, String newJrsNm
			, String practiceTypeNm
			, String jrsActionNm
			, String comments
			) {
		super();
		this.growthPlatformNm = growthPlatformNm;
		this.serviceLineNm = serviceLineNm;
		this.practiceNm = practiceNm;
		this.serviceAreaNm = serviceAreaNm;
		this.capacityGroupNm = capacityGroupNm;
		this.jrsNm = jrsNm;
		this.newGrowthPlatformNm = newGrowthPlatformNm;
		this.newServiceLineNm = newServiceLineNm;
		this.newPracticeNm = newPracticeNm;
		this.newServiceAreaNm = newServiceAreaNm;
		this.economicModelNm = economicModelNm;
		this.newJrsNm = newJrsNm;
		this.practiceTypeNm = practiceTypeNm;
		this.jrsActionNm = jrsActionNm;
		this.comments = comments;
	}
	
	public static String getHeader(boolean debug) {
		//return "GROWTH_PLATFORM_NM,SVC_LINE_NM,PRACTICE_NM,SVC_AREA_NM,CAPACITY_GROUP_NM,JRS_NM,NEW_GROWTH_PLATFORM_NM,NEW_SVC_LINE_NM,NEW_PRACTICE_NM,ECONOMIC_MODEL_NM,NEW_SVC_AREA_NM,NEW_JRS_NM,PRACTICE_TYPE_NM,JRS_ACTION";
		return "NEW_GROWTH_PLATFORM_NM,NEW_SVC_LINE_NM,NEW_PRACTICE_NM,NEW_ECONOMIC_MODEL_NM,NEW_SVC_AREA_NM,NEW_JRS_NM"+(debug?",JRS_ACTION":"");
	}
	
	public String toString() {
		return toString(null, false);
	}
	
	public String toString(String useJrsNm, boolean debug) {
		return 
				//Helpers.formatCsvField(growthPlatformNm)
				//+","+Helpers.formatCsvField(serviceLineNm)
				//+","+Helpers.formatCsvField(practiceNm)
				//+","+Helpers.formatCsvField(serviceAreaNm)
				//+","+Helpers.formatCsvField(capacityGroupNm)
				//+","+Helpers.formatCsvField(jrsNm)
				//+","+
				Helpers.formatCsvField(newGrowthPlatformNm)
				+","+Helpers.formatCsvField(newServiceLineNm)
				+","+Helpers.formatCsvField(newPracticeNm)
				+","+Helpers.formatCsvField(economicModelNm)
				+","+Helpers.formatCsvField(newServiceAreaNm)
				+","+(useJrsNm==null?Helpers.formatCsvField(newJrsNm):Helpers.formatCsvField(useJrsNm))
				//+","+Helpers.formatCsvField(practiceTypeNm)
				+(debug?","+Helpers.formatCsvField(jrsActionNm):"")
		+","+Helpers.formatCsvField(practiceTypeNm)
		;
				//+","+Helpers.formatCsvField(comments);
	}
	
	public String echo() {
		return Helpers.formatCsvField(growthPlatformNm)
				+","+Helpers.formatCsvField(serviceLineNm)
				+","+Helpers.formatCsvField(practiceNm)
				+","+Helpers.formatCsvField(serviceAreaNm)
				+","+Helpers.formatCsvField(capacityGroupNm)
				+","+Helpers.formatCsvField(jrsNm)
				+","+Helpers.formatCsvField(newServiceAreaNm)
				+","+Helpers.formatCsvField(newPracticeNm)
				+","+Helpers.formatCsvField(newServiceLineNm)
				+","+Helpers.formatCsvField(economicModelNm)
				+","+Helpers.formatCsvField(newGrowthPlatformNm)
				+","+Helpers.formatCsvField(practiceTypeNm)
				+","+Helpers.formatCsvField(jrsActionNm)
				+","+Helpers.formatCsvField(newJrsNm)
		        +","+Helpers.formatCsvField(comments);
	}
	/*
	JR/S Growth Platform
	JR/S Service Line
	JR/S Practice
	JR/S Service Area
	Capacity Group
	JR/S
	New Service Area
	New Practice
	New Service Line
	New Economic Model
	New Growth Platform
	
	Practice Type
	JR/S Action
	New JR/S
	Details/Comments*/
	
	public String getFullKey() {
		return this.practiceNm.trim()+":"+this.serviceAreaNm.trim()+":"+this.jrsNm.trim()+":";
	}
	
	public String getSaJrs() {
		return this.serviceAreaNm.trim()+":"+this.jrsNm.trim()+":";
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
	public String getCapacityGroupNm() {
		return capacityGroupNm;
	}
	public void setCapacityGroupNm(String capacityGroupNm) {
		this.capacityGroupNm = capacityGroupNm;
	}
	public String getJrsNm() {
		return jrsNm;
	}
	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
	}
	public String getNewGrowthPlatformNm() {
		return newGrowthPlatformNm;
	}
	public void setNewGrowthPlatformNm(String newGrowthPlatformNm) {
		this.newGrowthPlatformNm = newGrowthPlatformNm;
	}
	public String getNewServiceLineNm() {
		return newServiceLineNm;
	}
	public void setNewServiceLineNm(String newServiceLineNm) {
		this.newServiceLineNm = newServiceLineNm;
	}
	public String getNewPracticeNm() {
		return newPracticeNm;
	}
	public void setNewPracticeNm(String newPracticeNm) {
		this.newPracticeNm = newPracticeNm;
	}
	public String getNewServiceAreaNm() {
		return newServiceAreaNm;
	}
	public void setNewServiceAreaNm(String newServiceAreaNm) {
		this.newServiceAreaNm = newServiceAreaNm;
	}
	public String getEconomicModelNm() {
		return economicModelNm;
	}
	public void setEconomicModelNm(String economicModelNm) {
		this.economicModelNm = economicModelNm;
	}
	public String getNewJrsNm() {
		return newJrsNm;
	}
	public void setNewJrsNm(String newJrsNm) {
		this.newJrsNm = newJrsNm;
	}
	public String getPracticeTypeNm() {
		return practiceTypeNm;
	}
	public void setPracticeTypeNm(String practiceTypeNm) {
		this.practiceTypeNm = practiceTypeNm;
	}
	public String getJrsActionNm() {
		return jrsActionNm;
	}
	public void setJrsActionNm(String jrsActionNm) {
		this.jrsActionNm = jrsActionNm;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
