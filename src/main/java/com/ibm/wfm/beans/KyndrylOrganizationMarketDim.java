package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class KyndrylOrganizationMarketDim {
	@DbColumn(columnName="ORG_MKT_ID",isId=true)
	private int        orgMktId;
	@DbColumn(columnName="ORG_MKT_CD",keySeq=1)
	private String     orgMktCd;
	@DbColumn(columnName="ORG_MKT_SHORT_NM")
	private String     orgMktShortNm;
	@DbColumn(columnName="ORG_MKT_MED_NM")
	private String     orgMktMedNm;
	@DbColumn(columnName="ORG_MKT_NM")
	private String     orgMktNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public KyndrylOrganizationMarketDim () {}
	
	// Define natural key constructor
	public KyndrylOrganizationMarketDim (
      String     orgMktCd
	) {
		this.orgMktCd                       = orgMktCd;
	}
	
	// Define base constructor
	public KyndrylOrganizationMarketDim (
      String     orgMktCd
    , String     orgMktShortNm
    , String     orgMktMedNm
    , String     orgMktNm
	) {
		this.orgMktCd                       = orgMktCd;
		this.orgMktShortNm                  = orgMktShortNm;
		this.orgMktMedNm                    = orgMktMedNm;
		this.orgMktNm                       = orgMktNm;
	}
    
	// Define full constructor
	public KyndrylOrganizationMarketDim (
		  int        orgMktId
		, String     orgMktCd
		, String     orgMktShortNm
		, String     orgMktMedNm
		, String     orgMktNm
	) {
		this.orgMktId                       = orgMktId;
		this.orgMktCd                       = orgMktCd;
		this.orgMktShortNm                  = orgMktShortNm;
		this.orgMktMedNm                    = orgMktMedNm;
		this.orgMktNm                       = orgMktNm;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		KyndrylOrganizationMarketDim other = (KyndrylOrganizationMarketDim) obj;
		if (
            this.orgMktCd.equals(other.getOrgMktCd())
         && this.orgMktShortNm.equals(other.getOrgMktShortNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.orgMktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMktShortNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMktMedNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMktNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("ORG_MKT_CD")
        + "," + Helpers.formatCsvField("ORG_MKT_SHORT_NM")
        + "," + Helpers.formatCsvField("ORG_MKT_MED_NM")
        + "," + Helpers.formatCsvField("ORG_MKT_NM")
		;
	}
    
	// Define Getters and Setters
	public int getOrgMktId() {
		return orgMktId;
	}
	public void setOrgMktId(int orgMktId) {
		this.orgMktId = orgMktId;
	}
	public String getOrgMktCd() {
		return orgMktCd;
	}
	public void setOrgMktCd(String orgMktCd) {
		this.orgMktCd = orgMktCd;
	}
	public String getOrgMktShortNm() {
		return orgMktShortNm;
	}
	public void setOrgMktShortNm(String orgMktShortNm) {
		this.orgMktShortNm = orgMktShortNm;
	}
	public String getOrgMktMedNm() {
		return orgMktMedNm;
	}
	public void setOrgMktMedNm(String orgMktMedNm) {
		this.orgMktMedNm = orgMktMedNm;
	}
	public String getOrgMktNm() {
		return orgMktNm;
	}
	public void setOrgMktNm(String orgMktNm) {
		this.orgMktNm = orgMktNm;
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