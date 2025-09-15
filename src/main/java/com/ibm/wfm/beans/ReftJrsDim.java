package com.ibm.wfm.beans;


import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="ReftJrsDim",baseTableName="ERDMPROD.REFT_JRS")
public class ReftJrsDim extends NaryTreeNode {
	@DbColumn(columnName="REFT_JRS_DIM_KEY",isId=true)
	private int        reftJrsDimKey;
	@DbColumn(columnName="SUB_KEY",isId=true)
	private int        subKey;
	@DbColumn(columnName="JRS_CD",keySeq=1)
	private String     jrsCd;
	@DbColumn(columnName="BRAND_CD")
	private String     brandCd;
	@DbColumn(columnName="BRAND_NM")
	private String     brandNm;
	@DbColumn(columnName="GROWTH_PLATFORM_CD")
	private String     growthPlatformCd;
	@DbColumn(columnName="GROWTH_PLATFORM_NM")
	private String     growthPlatformNm;
	@DbColumn(columnName="SERVICE_LINE_CD")
	private String     serviceLineCd;
	@DbColumn(columnName="SERVICE_LINE_NM")
	private String     serviceLineNm;
	@DbColumn(columnName="PRACTICE_CD")
	private String     practiceCd;
	@DbColumn(columnName="PRACTICE_NM")
	private String     practiceNm;
	@DbColumn(columnName="SERVICE_AREA_CD")
	private String     serviceAreaCd;
	@DbColumn(columnName="SERVICE_AREA_NM")
	private String     serviceAreaNm;
	@DbColumn(columnName="JOB_ROLE_CD")
	private String     jobRoleCd;
	@DbColumn(columnName="JOB_ROLE_NM")
	private String     jobRoleNm;
	@DbColumn(columnName="SPECIALTY_CD")
	private String     specialtyCd;
	@DbColumn(columnName="SPECIALTY_NM")
	private String     specialtyNm;
	@DbColumn(columnName="STATUS")
	private String     status;
	@DbColumn(columnName="MAP_TO_CODE")
	private String     mapToCode;
	@DbColumn(columnName="ACTIVE_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       activeDate;
	@DbColumn(columnName="COMMENTS")
	private String     comments;
	@DbColumn(columnName="INACTIVE_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date       inactiveDate;
	@DbColumn(columnName="ROW_CREATE_TS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  rowCreateTs;
	@DbColumn(columnName="ROW_UPDT_TS")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  rowUpdtTs;

	// Define null constructor
	public ReftJrsDim () {
		
	}
	
	// Define natural key constructor
	public ReftJrsDim (
      String     jrsCd
	) {
		this.jrsCd                          = jrsCd;
		
	}
	
	// Define base constructor
	public ReftJrsDim (
      String     jrsCd
    , String     brandCd
    , String     brandNm
    , String     growthPlatformCd
    , String     growthPlatformNm
    , String     serviceLineCd
    , String     serviceLineNm
    , String     practiceCd
    , String     practiceNm
    , String     serviceAreaCd
    , String     serviceAreaNm
    , String     jobRoleCd
    , String     jobRoleNm
    , String     specialtyCd
    , String     specialtyNm
    , String     status
    , String     mapToCode
    , Date       activeDate
    , String     comments
    , Date       inactiveDate
    , Timestamp  rowCreateTs
    , Timestamp  rowUpdtTs
	) {
		this.jrsCd                          = jrsCd;
		this.brandCd                        = brandCd;
		this.brandNm                        = brandNm;
		this.growthPlatformCd               = growthPlatformCd;
		this.growthPlatformNm               = growthPlatformNm;
		this.serviceLineCd                  = serviceLineCd;
		this.serviceLineNm                  = serviceLineNm;
		this.practiceCd                     = practiceCd;
		this.practiceNm                     = practiceNm;
		this.serviceAreaCd                  = serviceAreaCd;
		this.serviceAreaNm                  = serviceAreaNm;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.status                         = status;
		this.mapToCode                      = mapToCode;
		this.activeDate                     = activeDate;
		this.comments                       = comments;
		this.inactiveDate                   = inactiveDate;
		this.rowCreateTs                    = rowCreateTs;
		this.rowUpdtTs                      = rowUpdtTs;
		
	}
    
	// Define full constructor
	public ReftJrsDim (
		  int        reftJrsDimKey
		, int        subKey
		, String     jrsCd
		, String     brandCd
		, String     brandNm
		, String     growthPlatformCd
		, String     growthPlatformNm
		, String     serviceLineCd
		, String     serviceLineNm
		, String     practiceCd
		, String     practiceNm
		, String     serviceAreaCd
		, String     serviceAreaNm
		, String     jobRoleCd
		, String     jobRoleNm
		, String     specialtyCd
		, String     specialtyNm
		, String     status
		, String     mapToCode
		, Date       activeDate
		, String     comments
		, Date       inactiveDate
		, Timestamp  rowCreateTs
		, Timestamp  rowUpdtTs
	) {
		this.reftJrsDimKey                  = reftJrsDimKey;
		this.subKey                         = subKey;
		this.jrsCd                          = jrsCd;
		this.brandCd                        = brandCd;
		this.brandNm                        = brandNm;
		this.growthPlatformCd               = growthPlatformCd;
		this.growthPlatformNm               = growthPlatformNm;
		this.serviceLineCd                  = serviceLineCd;
		this.serviceLineNm                  = serviceLineNm;
		this.practiceCd                     = practiceCd;
		this.practiceNm                     = practiceNm;
		this.serviceAreaCd                  = serviceAreaCd;
		this.serviceAreaNm                  = serviceAreaNm;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.status                         = status;
		this.mapToCode                      = mapToCode;
		this.activeDate                     = activeDate;
		this.comments                       = comments;
		this.inactiveDate                   = inactiveDate;
		this.rowCreateTs                    = rowCreateTs;
		this.rowUpdtTs                      = rowUpdtTs;
		
	}
	
	@Override
	public String getCode() { 
		return this.jrsCd
		;
	}
	public String getDescription() { 
		return jobRoleNm+"-"+specialtyNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		ReftJrsDim other = (ReftJrsDim) obj;
		if (
            this.jrsCd.equals(other.getJrsCd())
         && this.brandCd.equals(other.getBrandCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.brandCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.brandNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.growthPlatformCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.growthPlatformNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceLineCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceLineNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.practiceCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.practiceNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceAreaNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.status))
        + "," + Helpers.formatCsvField(String.valueOf(this.mapToCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.activeDate))
        + "," + Helpers.formatCsvField(String.valueOf(this.comments))
        + "," + Helpers.formatCsvField(String.valueOf(this.inactiveDate))
        + "," + Helpers.formatCsvField(String.valueOf(this.rowCreateTs))
        + "," + Helpers.formatCsvField(String.valueOf(this.rowUpdtTs))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("BRAND_CD")
        + "," + Helpers.formatCsvField("BRAND_NM")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_CD")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_NM")
        + "," + Helpers.formatCsvField("SERVICE_LINE_CD")
        + "," + Helpers.formatCsvField("SERVICE_LINE_NM")
        + "," + Helpers.formatCsvField("PRACTICE_CD")
        + "," + Helpers.formatCsvField("PRACTICE_NM")
        + "," + Helpers.formatCsvField("SERVICE_AREA_CD")
        + "," + Helpers.formatCsvField("SERVICE_AREA_NM")
        + "," + Helpers.formatCsvField("JOB_ROLE_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_NM")
        + "," + Helpers.formatCsvField("SPECIALTY_CD")
        + "," + Helpers.formatCsvField("SPECIALTY_NM")
        + "," + Helpers.formatCsvField("STATUS")
        + "," + Helpers.formatCsvField("MAP_TO_CODE")
        + "," + Helpers.formatCsvField("ACTIVE_DATE")
        + "," + Helpers.formatCsvField("COMMENTS")
        + "," + Helpers.formatCsvField("INACTIVE_DATE")
        + "," + Helpers.formatCsvField("ROW_CREATE_TS")
        + "," + Helpers.formatCsvField("ROW_UPDT_TS")
		;
	}
    
	// Define Getters and Setters
	public int getReftJrsDimKey() {
		return reftJrsDimKey;
	}
	public void setReftJrsDimKey(int reftJrsDimKey) {
		this.reftJrsDimKey = reftJrsDimKey;
	}
	public int getSubKey() {
		return subKey;
	}
	public void setSubKey(int subKey) {
		this.subKey = subKey;
	}
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
	public String getBrandCd() {
		return brandCd;
	}
	public void setBrandCd(String brandCd) {
		this.brandCd = brandCd;
	}
	public String getBrandNm() {
		return brandNm;
	}
	public void setBrandNm(String brandNm) {
		this.brandNm = brandNm;
	}
	public String getGrowthPlatformCd() {
		return growthPlatformCd;
	}
	public void setGrowthPlatformCd(String growthPlatformCd) {
		this.growthPlatformCd = growthPlatformCd;
	}
	public String getGrowthPlatformNm() {
		return growthPlatformNm;
	}
	public void setGrowthPlatformNm(String growthPlatformNm) {
		this.growthPlatformNm = growthPlatformNm;
	}
	public String getServiceLineCd() {
		return serviceLineCd;
	}
	public void setServiceLineCd(String serviceLineCd) {
		this.serviceLineCd = serviceLineCd;
	}
	public String getServiceLineNm() {
		return serviceLineNm;
	}
	public void setServiceLineNm(String serviceLineNm) {
		this.serviceLineNm = serviceLineNm;
	}
	public String getPracticeCd() {
		return practiceCd;
	}
	public void setPracticeCd(String practiceCd) {
		this.practiceCd = practiceCd;
	}
	public String getPracticeNm() {
		return practiceNm;
	}
	public void setPracticeNm(String practiceNm) {
		this.practiceNm = practiceNm;
	}
	public String getServiceAreaCd() {
		return serviceAreaCd;
	}
	public void setServiceAreaCd(String serviceAreaCd) {
		this.serviceAreaCd = serviceAreaCd;
	}
	public String getServiceAreaNm() {
		return serviceAreaNm;
	}
	public void setServiceAreaNm(String serviceAreaNm) {
		this.serviceAreaNm = serviceAreaNm;
	}
	public String getJobRoleCd() {
		return jobRoleCd;
	}
	public void setJobRoleCd(String jobRoleCd) {
		this.jobRoleCd = jobRoleCd;
	}
	public String getJobRoleNm() {
		return jobRoleNm;
	}
	public void setJobRoleNm(String jobRoleNm) {
		this.jobRoleNm = jobRoleNm;
	}
	public String getSpecialtyCd() {
		return specialtyCd;
	}
	public void setSpecialtyCd(String specialtyCd) {
		this.specialtyCd = specialtyCd;
	}
	public String getSpecialtyNm() {
		return specialtyNm;
	}
	public void setSpecialtyNm(String specialtyNm) {
		this.specialtyNm = specialtyNm;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMapToCode() {
		return mapToCode;
	}
	public void setMapToCode(String mapToCode) {
		this.mapToCode = mapToCode;
	}
	public Date getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getInactiveDate() {
		return inactiveDate;
	}
	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
	public Timestamp getRowCreateTs() {
		return rowCreateTs;
	}
	public void setRowCreateTs(Timestamp rowCreateTs) {
		this.rowCreateTs = rowCreateTs;
	}
	public Timestamp getRowUpdtTs() {
		return rowUpdtTs;
	}
	public void setRowUpdtTs(Timestamp rowUpdtTs) {
		this.rowUpdtTs = rowUpdtTs;
	}
}