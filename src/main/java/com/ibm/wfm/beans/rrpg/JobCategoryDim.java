package com.ibm.wfm.beans.rrpg;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JobCategoryDim",baseTableName="RRPG.JOB_CATEGORY")
public class JobCategoryDim extends NaryTreeNode {
	@DbColumn(columnName="JOB_CAT_ID",isId=true)
	private int        jobCatId;
	@DbColumn(columnName="JOB_CAT_CD",keySeq=1)
	private String     jobCatCd;
	@DbColumn(columnName="JOB_CAT_NM")
	private String     jobCatNm;
	@DbColumn(columnName="JOB_CAT_DESC")
	private String     jobCatDesc;
	@DbColumn(columnName="SRC_CD")
	private String     srcCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public JobCategoryDim () {
		
	}
	
	// Define natural key constructor
	public JobCategoryDim (
      String     jobCatCd
	) {
		this.jobCatCd                       = jobCatCd;
		
	}
	
	// Define base constructor
	public JobCategoryDim (
      String     jobCatCd
    , String     jobCatNm
    , String     jobCatDesc
    , String     srcCd
	) {
		this.jobCatCd                       = jobCatCd;
		this.jobCatNm                       = jobCatNm;
		this.jobCatDesc                     = jobCatDesc;
		this.srcCd                          = srcCd;
		
	}
    
	// Define full constructor
	public JobCategoryDim (
		  int        jobCatId
		, String     jobCatCd
		, String     jobCatNm
		, String     jobCatDesc
		, String     srcCd
	) {
		this.jobCatId                       = jobCatId;
		this.jobCatCd                       = jobCatCd;
		this.jobCatNm                       = jobCatNm;
		this.jobCatDesc                     = jobCatDesc;
		this.srcCd                          = srcCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.jobCatCd
		;
	}
	public String getDescription() { 
		return this.jobCatNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JobCategoryDim other = (JobCategoryDim) obj;
		if (
            this.jobCatCd.equals(other.getJobCatCd())
         && this.jobCatNm.equals(other.getJobCatNm())
         && this.jobCatDesc.equals(other.getJobCatDesc())
         && this.srcCd.equals(other.getSrcCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jobCatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobCatNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobCatDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JOB_CAT_CD")
        + "," + Helpers.formatCsvField("JOB_CAT_NM")
        + "," + Helpers.formatCsvField("JOB_CAT_DESC")
        + "," + Helpers.formatCsvField("SRC_CD")
		;
	}
    
	// Define Getters and Setters
	public int getJobCatId() {
		return jobCatId;
	}
	public void setJobCatId(int jobCatId) {
		this.jobCatId = jobCatId;
	}
	public String getJobCatCd() {
		return jobCatCd;
	}
	public void setJobCatCd(String jobCatCd) {
		this.jobCatCd = jobCatCd;
	}
	public String getJobCatNm() {
		return jobCatNm;
	}
	public void setJobCatNm(String jobCatNm) {
		this.jobCatNm = jobCatNm;
	}
	public String getJobCatDesc() {
		return jobCatDesc;
	}
	public void setJobCatDesc(String jobCatDesc) {
		this.jobCatDesc = jobCatDesc;
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

	public String getSrcCd() {
		return srcCd;
	}

	public void setSrcCd(String srcCd) {
		this.srcCd = srcCd;
	}
}