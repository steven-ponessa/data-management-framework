package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JobRoleDescriptionDim",baseTableName="REFT.JOB_ROLE_DESCRIPTION")
public class JobRoleDescriptionDim  {
	@DbColumn(columnName="JOB_ROLE_DESC_ID",isId=true)
	private int        jobRoleDescId;
	@DbColumn(columnName="JOB_ROLE_CD",keySeq=1)
	private String     jobRoleCd;
	@DbColumn(columnName="JOB_ROLE_DESC")
	private String     jobRoleDesc;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public JobRoleDescriptionDim () {
		
	}
	
	// Define natural key constructor
	public JobRoleDescriptionDim (
      String     jobRoleCd
	) {
		this.jobRoleCd                      = jobRoleCd;
		
	}
	
	// Define base constructor
	public JobRoleDescriptionDim (
      String     jobRoleCd
    , String     jobRoleDesc
	) {
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleDesc                    = jobRoleDesc;
		
	}
    
	// Define full constructor
	public JobRoleDescriptionDim (
		  int        jobRoleDescId
		, String     jobRoleCd
		, String     jobRoleDesc
	) {
		this.jobRoleDescId                  = jobRoleDescId;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleDesc                    = jobRoleDesc;
		
	}
	/*
	public String getCode() { 
		return this.jobRoleCd; 
	}
	
	public String getDescription() { 
		return this.jobRoleDesc; 
	}
	*/
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JobRoleDescriptionDim other = (JobRoleDescriptionDim) obj;
		if (
            this.jobRoleCd.equals(other.getJobRoleCd())
         && this.jobRoleDesc.equals(other.getJobRoleDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jobRoleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleDesc))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JOB_ROLE_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getJobRoleDescId() {
		return jobRoleDescId;
	}
	public void setJobRoleDescId(int jobRoleDescId) {
		this.jobRoleDescId = jobRoleDescId;
	}
	public String getJobRoleCd() {
		return jobRoleCd;
	}
	public void setJobRoleCd(String jobRoleCd) {
		this.jobRoleCd = jobRoleCd;
	}
	public String getJobRoleDesc() {
		return jobRoleDesc;
	}
	public void setJobRoleDesc(String jobRoleDesc) {
		this.jobRoleDesc = jobRoleDesc;
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