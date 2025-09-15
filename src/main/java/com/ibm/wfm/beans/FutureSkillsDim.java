package com.ibm.wfm.beans;


import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class FutureSkillsDim {
	@DbColumn(columnName="FUTURE_SKILLS_ID",isId=true)
	private int        futureSkillsId;
	@DbColumn(columnName="CNUM",keySeq=1)
	private String     cnum;
	@DbColumn(columnName="TARGET_JRS",keySeq=2)
	private String     targetJrs;
	@DbColumn(columnName="TYPE_OF_SKILLING",keySeq=3)
	private String     typeOfSkilling;
	@DbColumn(columnName="SK_EMPLOYEE")
	private int        skEmployee;
	@DbColumn(columnName="CURRENT_BUSINESS_VALUE")
	private String     currentBusinessValue;
	@DbColumn(columnName="TARGET_BUSINESS_VALUE")
	private String     targetBusinessValue;
	@DbColumn(columnName="CURRENT_INDICATOR")
	private int        currentIndicator;
	@DbColumn(columnName="EFFECTIVE_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       effectiveDate;
	@DbColumn(columnName="SK_EFFECTIVE_DATE")
	private int        skEffectiveDate;
	@DbColumn(columnName="EXPIRATION_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       expirationDate;
	@DbColumn(columnName="SK_EXPIRATION_DATE")
	private int        skExpirationDate;
	@DbColumn(columnName="ETL_TIMESTAMP")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  etlTimestamp;
	@DbColumn(columnName="AUDIT_TIMESTAMP")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  auditTimestamp;
	@DbColumn(columnName="STATUS")
	private String     status;
	@DbColumn(columnName="COMPLETION_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       completionDate;
	@DbColumn(columnName="BATCH_NAME")
	private String     batchName;
	@DbColumn(columnName="BATCH_STATUS")
	private String     batchStatus;
	@DbColumn(columnName="COMPLETION_PERCENTAGE")
	private int        completionPercentage;
	@DbColumn(columnName="PLAN_NAME")
	private String     planName;
	@DbColumn(columnName="JRS_CHANGED")
	private String     jrsChanged;
	@DbColumn(columnName="REPORTING_YEAR")
	private int        reportingYear;
	@DbColumn(columnName="LEARNING_ADVISOR")
	private String     learningAdvisor;
	@DbColumn(columnName="ENROLLMENT_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       enrollmentDate;
	@DbColumn(columnName="SOURCE_JRS")
	private String     sourceJrs;
	@DbColumn(columnName="MANAGEMENT_ORGANIZATION")
	private String     managementOrganization;
	@DbColumn(columnName="CHANNEL")
	private String     channel;
	@DbColumn(columnName="START_MONTH")
	private String     startMonth;
	@DbColumn(columnName="ARCHIVED_STATUS")
	private String     archivedStatus;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public FutureSkillsDim () {}
	
	// Define base constructor
	public FutureSkillsDim (
      String     cnum
    , String     targetJrs
    , String     typeOfSkilling
    , int        skEmployee
    , String     currentBusinessValue
    , String     targetBusinessValue
    , int        currentIndicator
    , Date       effectiveDate
    , int        skEffectiveDate
    , Date       expirationDate
    , int        skExpirationDate
    , Timestamp  etlTimestamp
    , Timestamp  auditTimestamp
    , String     status
    , Date       completionDate
    , String     batchName
    , String     batchStatus
    , int        completionPercentage
    , String     planName
    , String     jrsChanged
    , int        reportingYear
    , String     learningAdvisor
    , Date       enrollmentDate
    , String     sourceJrs
    , String     managementOrganization
    , String     channel
    , String     startMonth
    , String     archivedStatus
	) {
		this.cnum                           = cnum;
		this.targetJrs                      = targetJrs;
		this.typeOfSkilling                 = typeOfSkilling;
		this.skEmployee                     = skEmployee;
		this.currentBusinessValue           = currentBusinessValue;
		this.targetBusinessValue            = targetBusinessValue;
		this.currentIndicator               = currentIndicator;
		this.effectiveDate                  = effectiveDate;
		this.skEffectiveDate                = skEffectiveDate;
		this.expirationDate                 = expirationDate;
		this.skExpirationDate               = skExpirationDate;
		this.etlTimestamp                   = etlTimestamp;
		this.auditTimestamp                 = auditTimestamp;
		this.status                         = status;
		this.completionDate                 = completionDate;
		this.batchName                      = batchName;
		this.batchStatus                    = batchStatus;
		this.completionPercentage           = completionPercentage;
		this.planName                       = planName;
		this.jrsChanged                     = jrsChanged;
		this.reportingYear                  = reportingYear;
		this.learningAdvisor                = learningAdvisor;
		this.enrollmentDate                 = enrollmentDate;
		this.sourceJrs                      = sourceJrs;
		this.managementOrganization         = managementOrganization;
		this.channel                        = channel;
		this.startMonth                     = startMonth;
		this.archivedStatus                 = archivedStatus;
	}
    
	// Define full constructor
	public FutureSkillsDim (
		  int        futureSkillsId
		, String     cnum
		, String     targetJrs
		, String     typeOfSkilling
		, int        skEmployee
		, String     currentBusinessValue
		, String     targetBusinessValue
		, int        currentIndicator
		, Date       effectiveDate
		, int        skEffectiveDate
		, Date       expirationDate
		, int        skExpirationDate
		, Timestamp  etlTimestamp
		, Timestamp  auditTimestamp
		, String     status
		, Date       completionDate
		, String     batchName
		, String     batchStatus
		, int        completionPercentage
		, String     planName
		, String     jrsChanged
		, int        reportingYear
		, String     learningAdvisor
		, Date       enrollmentDate
		, String     sourceJrs
		, String     managementOrganization
		, String     channel
		, String     startMonth
		, String     archivedStatus
	) {
		this.futureSkillsId                 = futureSkillsId;
		this.cnum                           = cnum;
		this.targetJrs                      = targetJrs;
		this.typeOfSkilling                 = typeOfSkilling;
		this.skEmployee                     = skEmployee;
		this.currentBusinessValue           = currentBusinessValue;
		this.targetBusinessValue            = targetBusinessValue;
		this.currentIndicator               = currentIndicator;
		this.effectiveDate                  = effectiveDate;
		this.skEffectiveDate                = skEffectiveDate;
		this.expirationDate                 = expirationDate;
		this.skExpirationDate               = skExpirationDate;
		this.etlTimestamp                   = etlTimestamp;
		this.auditTimestamp                 = auditTimestamp;
		this.status                         = status;
		this.completionDate                 = completionDate;
		this.batchName                      = batchName;
		this.batchStatus                    = batchStatus;
		this.completionPercentage           = completionPercentage;
		this.planName                       = planName;
		this.jrsChanged                     = jrsChanged;
		this.reportingYear                  = reportingYear;
		this.learningAdvisor                = learningAdvisor;
		this.enrollmentDate                 = enrollmentDate;
		this.sourceJrs                      = sourceJrs;
		this.managementOrganization         = managementOrganization;
		this.channel                        = channel;
		this.startMonth                     = startMonth;
		this.archivedStatus                 = archivedStatus;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		FutureSkillsDim other = (FutureSkillsDim) obj;
		if (
            this.cnum.equals(other.getCnum())
         && this.targetJrs.equals(other.getTargetJrs())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(this.cnum)
        + "," + Helpers.formatCsvField(this.targetJrs)
        + "," + Helpers.formatCsvField(this.typeOfSkilling)
        + "," + this.skEmployee
        + "," + Helpers.formatCsvField(this.currentBusinessValue)
        + "," + Helpers.formatCsvField(this.targetBusinessValue)
        + "," + this.currentIndicator
        + "," + Helpers.formatDate(this.effectiveDate)
        + "," + this.skEffectiveDate
        + "," + Helpers.formatDate(this.expirationDate)
        + "," + this.skExpirationDate
        + "," + Helpers.formatDate(this.etlTimestamp,"yyyy-MM-dd hh.ss.mm.tt")
        + "," + Helpers.formatDate(this.auditTimestamp,"yyyy-MM-dd hh.ss.mm.tt")
        + "," + Helpers.formatCsvField(this.status)
        + "," + Helpers.formatDate(this.completionDate)
        + "," + Helpers.formatCsvField(this.batchName)
        + "," + Helpers.formatCsvField(this.batchStatus)
        + "," + this.completionPercentage
        + "," + Helpers.formatCsvField(this.planName)
        + "," + Helpers.formatCsvField(this.jrsChanged)
        + "," + this.reportingYear
        + "," + Helpers.formatCsvField(this.learningAdvisor)
        + "," + Helpers.formatDate(this.enrollmentDate)
        + "," + Helpers.formatCsvField(this.sourceJrs)
        + "," + Helpers.formatCsvField(this.managementOrganization)
        + "," + Helpers.formatCsvField(this.channel)
        + "," + Helpers.formatCsvField(this.startMonth)
        + "," + Helpers.formatCsvField(this.archivedStatus)
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CNUM")
        + "," + Helpers.formatCsvField("TARGET_JRS")
        + "," + Helpers.formatCsvField("TYPE_OF_SKILLING")
        + "," + Helpers.formatCsvField("SK_EMPLOYEE")
        + "," + Helpers.formatCsvField("CURRENT_BUSINESS_VALUE")
        + "," + Helpers.formatCsvField("TARGET_BUSINESS_VALUE")
        + "," + Helpers.formatCsvField("CURRENT_INDICATOR")
        + "," + Helpers.formatCsvField("EFFECTIVE_DATE")
        + "," + Helpers.formatCsvField("SK_EFFECTIVE_DATE")
        + "," + Helpers.formatCsvField("EXPIRATION_DATE")
        + "," + Helpers.formatCsvField("SK_EXPIRATION_DATE")
        + "," + Helpers.formatCsvField("ETL_TIMESTAMP")
        + "," + Helpers.formatCsvField("AUDIT_TIMESTAMP")
        + "," + Helpers.formatCsvField("STATUS")
        + "," + Helpers.formatCsvField("COMPLETION_DATE")
        + "," + Helpers.formatCsvField("BATCH_NAME")
        + "," + Helpers.formatCsvField("BATCH_STATUS")
        + "," + Helpers.formatCsvField("COMPLETION_PERCENTAGE")
        + "," + Helpers.formatCsvField("PLAN_NAME")
        + "," + Helpers.formatCsvField("JRS_CHANGED")
        + "," + Helpers.formatCsvField("REPORTING_YEAR")
        + "," + Helpers.formatCsvField("LEARNING_ADVISOR")
        + "," + Helpers.formatCsvField("ENROLLMENT_DATE")
        + "," + Helpers.formatCsvField("SOURCE_JRS")
        + "," + Helpers.formatCsvField("MANAGEMENT_ORGANIZATION")
        + "," + Helpers.formatCsvField("CHANNEL")
        + "," + Helpers.formatCsvField("START_MONTH")
        + "," + Helpers.formatCsvField("ARCHIVED_STATUS")
		;
	}
    
	// Define Getters and Setters
	public int getFutureSkillsId() {
		return futureSkillsId;
	}
	public void setFutureSkillsId(int futureSkillsId) {
		this.futureSkillsId = futureSkillsId;
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
	public int getSkEmployee() {
		return skEmployee;
	}
	public void setSkEmployee(int skEmployee) {
		this.skEmployee = skEmployee;
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
	public int getCurrentIndicator() {
		return currentIndicator;
	}
	public void setCurrentIndicator(int currentIndicator) {
		this.currentIndicator = currentIndicator;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public int getSkEffectiveDate() {
		return skEffectiveDate;
	}
	public void setSkEffectiveDate(int skEffectiveDate) {
		this.skEffectiveDate = skEffectiveDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public int getSkExpirationDate() {
		return skExpirationDate;
	}
	public void setSkExpirationDate(int skExpirationDate) {
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
	public int getCompletionPercentage() {
		return completionPercentage;
	}
	public void setCompletionPercentage(int completionPercentage) {
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
	public int getReportingYear() {
		return reportingYear;
	}
	public void setReportingYear(int reportingYear) {
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
	public Timestamp getEffTms() {
		return effTms;
	}
	public void setEffTms(Timestamp effTms) {
		this.effTms = effTms;
	}
	public Timestamp getExpirTms() {
		return expirTms;
	}
	public void setExpirTms(Timestamp expirTms) {
		this.expirTms = expirTms;
	}
	public String getRowStatusCd() {
		return rowStatusCd;
	}
	public void setRowStatusCd(String rowStatusCd) {
		this.rowStatusCd = rowStatusCd;
	}
}