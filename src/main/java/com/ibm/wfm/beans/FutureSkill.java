package com.ibm.wfm.beans;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;

public class FutureSkill {
	
	//@DbColumn(columnName ="FUTURE_SKILL_ID",isId=true)    
	//private int  futureSkillId;
	@DbColumn(columnName ="CNUM",keySeq=1) 
	private String cnum;
	@DbColumn(columnName ="TARGET_JRS",keySeq=2) 
	private String targetJrs;
	@DbColumn(columnName ="TYPE_OF_SKILLING",keySeq=3) 
	private String typeOfSkilling;
	@DbColumn(columnName ="CURRENT_BUSINESS_VALUE")
	private String currentBusinessValue;
	@DbColumn(columnName ="TARGET_BUSINESS_VALUE")
	private String targetBusinessValue;
	@DbColumn(columnName ="SK_EMPLOYEE")
	private Integer skEmployee;
	@DbColumn(columnName ="STATUS")
	private String status;
	@DbColumn(columnName ="COMPLETION_DATE")
	private Date completionDate;
	//@DbColumn(columnName ="SK_COMPLETION_DATE")
	//private Integer skCompletionDate;
	@DbColumn(columnName ="BATCH_NAME")
	private String batchName;
	@DbColumn(columnName ="BATCH_STATUS")
	private String batchStatus;
	@DbColumn(columnName ="COMPLETION_PERCENTAGE")
	private Integer completionPercentage;
	@DbColumn(columnName ="PLAN_NAME")
	private String planName;
	@DbColumn(columnName ="JRS_CHANGED")
	private String jrsChanged;
	@DbColumn(columnName ="REPORTING_YEAR")
	private Integer reportingYear;
	@DbColumn(columnName ="LEARNING_ADVISOR")
	private String learningAdvisor;
	@DbColumn(columnName ="ENROLLMENT_DATE")
	private Date enrollmentDate;
	//@DbColumn(columnName ="SK_ENROLLMENT_DATE")
	//private Integer skEnrollmentDate;
	@DbColumn(columnName ="SOURCE_JRS")
	private String sourceJrs;
	@DbColumn(columnName ="MANAGEMENT_ORGANIZATION")
	private String managementOrganization;
	@DbColumn(columnName ="CHANNEL")
	private String channel;
	@DbColumn(columnName ="START_MONTH")
	private String startMonth;
	@DbColumn(columnName ="ARCHIVED_STATUS")
	private String archivedStatus;
	@DbColumn(columnName ="CURRENT_INDICATOR")
	private Integer currentIndicator;
	@DbColumn(columnName ="EFFECTIVE_DATE")
	private Date effectiveDate;
	@DbColumn(columnName ="SK_EFFECTIVE_DATE")
	private Integer skEffectiveDate;
	@DbColumn(columnName ="EXPIRATION_DATE")
	private Date expirationDate;
	@DbColumn(columnName ="SK_EXPIRATION_DATE")
	private Integer skExpirationDate;
	@DbColumn(columnName ="ETL_TIMESTAMP")
	//@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT-04:00")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp etlTimestamp;
	@DbColumn(columnName ="AUDIT_TIMESTAMP")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp auditTimestamp;
	//private Timestamp auditTms;
	@DbColumn(columnName ="EFF_TMS",isScd=true)    
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)    
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;
	
	public FutureSkill() {}

	public FutureSkill(String cnum, String targetJrs, String typeOfSkilling, String currentBusinessValue,
			String targetBusinessValue, Integer skEmployee, String status, Date completionDate,
			String batchName, String batchStatus, Integer completionPercentage,
			String planName, String jrsChanged, Integer reportingYear, String learningAdvisor, Date enrollmentDate,
			String sourceJrs, String managementOrganization, String channel,
			String startMonth, String archivedStatus, Integer currentIndicator, Date effectiveDate,
			Integer skEffectiveDate, Date expirationDate, Integer skExpirationDate, Timestamp etlTimestamp,
			Timestamp auditTimestamp) {
		super();
		this.cnum = cnum;
		this.targetJrs = targetJrs;
		this.typeOfSkilling = typeOfSkilling;
		this.currentBusinessValue = currentBusinessValue;
		this.targetBusinessValue = targetBusinessValue;
		this.skEmployee = skEmployee;
		this.status = status;
		this.completionDate = completionDate;
		//this.skCompletionDate = skCompletionDate;
		this.batchName = batchName;
		this.batchStatus = batchStatus;
		this.completionPercentage = completionPercentage;
		this.planName = planName;
		this.jrsChanged = jrsChanged;
		this.reportingYear = reportingYear;
		this.learningAdvisor = learningAdvisor;
		this.enrollmentDate = enrollmentDate;
		//this.skEnrollmentDate = skEnrollmentDate;
		this.sourceJrs = sourceJrs;
		this.managementOrganization = managementOrganization;
		this.channel = channel;
		this.startMonth = startMonth;
		this.archivedStatus = archivedStatus;
		this.currentIndicator = currentIndicator;
		this.effectiveDate = effectiveDate;
		this.skEffectiveDate = skEffectiveDate;
		this.expirationDate = expirationDate;
		this.skExpirationDate = skExpirationDate;
		this.etlTimestamp = etlTimestamp;
		this.auditTimestamp = auditTimestamp;
	}



	public String getCnum() {
		return cnum;
	}

	public void setCnum(String cnum) {
		this.cnum = cnum;
	}

	public String getTargetJrs() {
		return targetJrs;
	}

	public void setTargetJrs(String targetJrs) {
		this.targetJrs = targetJrs;
	}

	public String getTypeOfSkilling() {
		return typeOfSkilling;
	}

	public void setTypeOfSkilling(String typeOfSkilling) {
		this.typeOfSkilling = typeOfSkilling;
	}

	public String getCurrentBusinessValue() {
		return currentBusinessValue;
	}

	public void setCurrentBusinessValue(String currentBusinessValue) {
		this.currentBusinessValue = currentBusinessValue;
	}

	public String getTargetBusinessValue() {
		return targetBusinessValue;
	}

	public void setTargetBusinessValue(String targetBusinessValue) {
		this.targetBusinessValue = targetBusinessValue;
	}

	public Integer getSkEmployee() {
		return skEmployee;
	}

	public void setSkEmployee(Integer skEmployee) {
		this.skEmployee = skEmployee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getBatchStatus() {
		return batchStatus;
	}

	public void setBatchStatus(String batchStatus) {
		this.batchStatus = batchStatus;
	}

	public Integer getCompletionPercentage() {
		return completionPercentage;
	}

	public void setCompletionPercentage(Integer completionPercentage) {
		this.completionPercentage = completionPercentage;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getJrsChanged() {
		return jrsChanged;
	}

	public void setJrsChanged(String jrsChanged) {
		this.jrsChanged = jrsChanged;
	}

	public Integer getReportingYear() {
		return reportingYear;
	}

	public void setReportingYear(Integer reportingYear) {
		this.reportingYear = reportingYear;
	}

	public String getLearningAdvisor() {
		return learningAdvisor;
	}

	public void setLearningAdvisor(String learningAdvisor) {
		this.learningAdvisor = learningAdvisor;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getSourceJrs() {
		return sourceJrs;
	}

	public void setSourceJrs(String sourceJrs) {
		this.sourceJrs = sourceJrs;
	}

	public String getManagementOrganization() {
		return managementOrganization;
	}

	public void setManagementOrganization(String managementOrganization) {
		this.managementOrganization = managementOrganization;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public String getArchivedStatus() {
		return archivedStatus;
	}

	public void setArchivedStatus(String archivedStatus) {
		this.archivedStatus = archivedStatus;
	}

	public Integer getCurrentIndicator() {
		return currentIndicator;
	}

	public void setCurrentIndicator(Integer currentIndicator) {
		this.currentIndicator = currentIndicator;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getSkEffectiveDate() {
		return skEffectiveDate;
	}

	public void setSkEffectiveDate(Integer skEffectiveDate) {
		this.skEffectiveDate = skEffectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getSkExpirationDate() {
		return skExpirationDate;
	}

	public void setSkExpirationDate(Integer skExpirationDate) {
		this.skExpirationDate = skExpirationDate;
	}

	public Timestamp getEtlTimestamp() {
		return etlTimestamp;
	}

	public void setEtlTimestamp(Timestamp etlTimestamp) {
		this.etlTimestamp = etlTimestamp;
	}

	public Timestamp getAuditTimestamp() {
		return auditTimestamp;
	}

	public void setAuditTimestamp(Timestamp auditTimestamp) {
		this.auditTimestamp = auditTimestamp;
	}

	@Override
	public String toString() {
		return "FutureSkill [cnum=" + cnum + ", targetJrs=" + targetJrs + ", typeOfSkilling=" + typeOfSkilling
				+ ", currentBusinessValue=" + currentBusinessValue + ", targetBusinessValue=" + targetBusinessValue
				+ ", skEmployee=" + skEmployee + ", status=" + status + ", completionDate=" + completionDate
			    + ", batchName=" + batchName + ", batchStatus=" + batchStatus
				+ ", completionPercentage=" + completionPercentage + ", planName=" + planName + ", jrsChanged="
				+ jrsChanged + ", reportingYear=" + reportingYear + ", learningAdvisor=" + learningAdvisor
				+ ", enrollmentDate=" + enrollmentDate  + ", sourceJrs="
				+ sourceJrs + ", managementOrganization=" + managementOrganization + ", channel=" + channel
				+ ", startMonth=" + startMonth + ", archivedStatus=" + archivedStatus + ", currentIndicator="
				+ currentIndicator + ", effectiveDate=" + effectiveDate + ", skEffectiveDate=" + skEffectiveDate
				+ ", expirationDate=" + expirationDate + ", skExpirationDate=" + skExpirationDate + ", etlTimestamp="
				+ etlTimestamp + ", auditTimestamp=" + auditTimestamp + "]";
	}

}
