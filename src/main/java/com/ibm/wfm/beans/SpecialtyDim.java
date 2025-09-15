package com.ibm.wfm.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class SpecialtyDim {
	@JsonIgnore
	@DbColumn(columnName="SPECIALTY_ID",isId=true)
	private int        specialtyId;
	@DbColumn(columnName="SPECIALTY_CD",keySeq=1)
	private String     specialtyCd;
	@DbColumn(columnName="SPECIALTY_NM")
	private String     specialtyNm;
	@DbColumn(columnName="JOB_ROLE_CD")
	private String     jobRoleCd;
	@DbColumn(columnName="CMPNSTN_GRD_LST")
	private String     cmpnstnGrdLst;
	@DbColumn(columnName="INCENTIVE_FLG")
	private String     incentiveFlg;

	// Define null constructor
	public SpecialtyDim () {}
	
    
	// Define full constructor
	public SpecialtyDim (
		  int        specialtyId
		, String     specialtyCd
		, String     specialtyNm
		, String     jobRoleCd
		, String     cmpnstnGrdLst
		, String     incentiveFlg
	) {
		this.specialtyId                    = specialtyId;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.jobRoleCd                      = jobRoleCd;
		this.cmpnstnGrdLst                  = cmpnstnGrdLst;
		this.incentiveFlg                   = incentiveFlg;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SpecialtyDim other = (SpecialtyDim) obj;
		if (
            this.specialtyCd.equals(other.getSpecialtyCd())
         && this.specialtyNm.equals(other.getSpecialtyNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.specialtyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SPECIALTY_CD")
        + "," + Helpers.formatCsvField("SPECIALTY_NM")
        + "," + Helpers.formatCsvField("JOB_ROLE_CD")
		;
	}
    
	// Define Getters and Setters
	public int getSpecialtyId() {
		return specialtyId;
	}
	public void setSpecialtyId(int specialtyId) {
		this.specialtyId = specialtyId;
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
	public String getJobRoleCd() {
		return jobRoleCd;
	}
	public void setJobRoleCd(String jobRoleCd) {
		this.jobRoleCd = jobRoleCd;
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