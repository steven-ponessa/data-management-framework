package com.ibm.wfm.beans.rrpg;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JobRoleRrpgDim",baseTableName="RRPG.JOB_ROLE",parentBeanPackageName="com.ibm.wfm.beans.rrpg",parentBeanName="JobCategoryDim",parentBaseTableName="RRPG.JOB_CATEGORY")
public class JobRoleRrpgDim extends NaryTreeNode implements Comparable<JobRoleRrpgDim> {
	@DbColumn(columnName="JOB_ROLE_ID",isId=true)
	private int        jobRoleId;
	@DbColumn(columnName="JOB_ROLE_CD",keySeq=1)
	private String     jobRoleCd;
	@DbColumn(columnName="JOB_ROLE_NM")
	private String     jobRoleNm;
	@DbColumn(columnName="JOB_ROLE_DESC")
	private String     jobRoleDesc;
	@DbColumn(columnName="JOB_CAT_CD",foreignKeySeq=1)
	private String     jobCatCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public JobRoleRrpgDim () {
		
	}
	
	// Define natural key constructor
	public JobRoleRrpgDim (
      String     jobRoleCd
	) {
		this.jobRoleCd                      = jobRoleCd;
		
	}
	
	// Define base constructor
	public JobRoleRrpgDim (
      String     jobRoleCd
    , String     jobRoleNm
    , String     jobRoleDesc
    , String     jobCatCd
	) {
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.jobRoleDesc                    = jobRoleDesc;
		this.jobCatCd                       = jobCatCd;
		
	}
    
	// Define full constructor
	public JobRoleRrpgDim (
		  int        jobRoleId
		, String     jobRoleCd
		, String     jobRoleNm
		, String     jobRoleDesc
		, String     jobCatCd
	) {
		this.jobRoleId                      = jobRoleId;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.jobRoleDesc                    = jobRoleDesc;
		this.jobCatCd                       = jobCatCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.jobRoleCd
		;
	}
	public String getDescription() { 
		return this.jobRoleNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JobRoleRrpgDim other = (JobRoleRrpgDim) obj;
		if (
            this.jobRoleCd.equals(other.getJobRoleCd())
         && this.jobRoleNm.equals(other.getJobRoleNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jobRoleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobCatCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JOB_ROLE_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_NM")
        + "," + Helpers.formatCsvField("JOB_ROLE_DESC")
        + "," + Helpers.formatCsvField("JOB_CAT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getJobRoleId() {
		return jobRoleId;
	}
	public void setJobRoleId(int jobRoleId) {
		this.jobRoleId = jobRoleId;
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
	public String getJobRoleDesc() {
		return jobRoleDesc;
	}
	public void setJobRoleDesc(String jobRoleDesc) {
		this.jobRoleDesc = jobRoleDesc;
	}
	public String getJobCatCd() {
		return jobCatCd;
	}
	public void setJobCatCd(String jobCatCd) {
		this.jobCatCd = jobCatCd;
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

	@Override
	public int compareTo(JobRoleRrpgDim o) {
		return this.getJobRoleCd().compareTo(o.getJobRoleCd());
	}
}