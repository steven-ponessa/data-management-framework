package com.ibm.wfm.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JobRoleDim",baseTableName="REFT.JRS")
public class JobRoleDim {
	@JsonIgnore
	@DbColumn(columnName="JOB_ROLE_ID",isId=true)
	private int        jobRoleId;
	@DbColumn(columnName="JOB_ROLE_CD",keySeq=1)
	private String     jobRoleCd;
	@DbColumn(columnName="JOB_ROLE_NM")
	private String     jobRoleNm;
	@DbColumn(columnName="CMPNSTN_GRD_LST")
	private String     cmpnstnGrdLst;
	@DbColumn(columnName="INCENTIVE_FLG")
	private String     incentiveFlg;
	

	// Define null constructor
	public JobRoleDim () {}
	
    
	// Define full constructor
	public JobRoleDim (
		  int        jobRoleId
		, String     jobRoleCd
		, String     jobRoleNm
	) {
		this.jobRoleId                      = jobRoleId;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JobRoleDim other = (JobRoleDim) obj;
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
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JOB_ROLE_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_NM")
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


	public String getCmpnstnGrdLst() {
		return cmpnstnGrdLst;
	}


	public void setCmpnstnGrdLst(String cmpnstnGrdLst) {
		this.cmpnstnGrdLst = cmpnstnGrdLst;
	}


	public String getIncentiveFlg() {
		return incentiveFlg;
	}
	
	public void setIncentiveFlg(String incentiveFlg) {
		this.incentiveFlg = incentiveFlg;
	}
}