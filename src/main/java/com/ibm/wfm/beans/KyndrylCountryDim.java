package com.ibm.wfm.beans;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class KyndrylCountryDim {
	@DbColumn(columnName="CTRY_ID",isId=true)
	private int        ctryId;
	@DbColumn(columnName="CTRY_ISO_CD",keySeq=1)
	private String     ctryIsoCd;
	@DbColumn(columnName="CTRY_ALPHA3_CD")
	private String     ctryAlpha3Cd;
	@DbColumn(columnName="CTRY_NUM_CD")
	private String     ctryNumCd;
	@DbColumn(columnName="CTRY_NM")
	private String     ctryNm;
	@DbColumn(columnName="CTRY_ISO_EN_NM")
	private String     ctryIsoEnNm;
	@DbColumn(columnName="ACTIVE_IND")
	private String     activeInd;
	@DbColumn(columnName="ORG_MKT_CD")
	private String     orgMktCd;
	@DbColumn(columnName="STRATEGIC_MKT_CD")
	private String     strategicMktCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public KyndrylCountryDim () {}
	
	// Define natural key constructor
	public KyndrylCountryDim (
      String     ctryIsoCd
	) {
		this.ctryIsoCd                      = ctryIsoCd;
	}
	
	// Define base constructor
	public KyndrylCountryDim (
      String     ctryIsoCd
    , String     ctryAlpha3Cd
    , String     ctryNumCd
    , String     ctryNm
    , String     ctryIsoEnNm
    , String     activeInd
    , String     orgMktCd
    , String     strategicMktCd
	) {
		this.ctryIsoCd                      = ctryIsoCd;
		this.ctryAlpha3Cd                   = ctryAlpha3Cd;
		this.ctryNumCd                      = ctryNumCd;
		this.ctryNm                         = ctryNm;
		this.ctryIsoEnNm                    = ctryIsoEnNm;
		this.activeInd                      = activeInd;
		this.orgMktCd                       = orgMktCd;
		this.strategicMktCd                 = strategicMktCd;
	}
    
	// Define full constructor
	public KyndrylCountryDim (
		  int        ctryId
		, String     ctryIsoCd
		, String     ctryAlpha3Cd
		, String     ctryNumCd
		, String     ctryNm
		, String     ctryIsoEnNm
		, String     activeInd
		, String     orgMktCd
		, String     strategicMktCd
	) {
		this.ctryId                         = ctryId;
		this.ctryIsoCd                      = ctryIsoCd;
		this.ctryAlpha3Cd                   = ctryAlpha3Cd;
		this.ctryNumCd                      = ctryNumCd;
		this.ctryNm                         = ctryNm;
		this.ctryIsoEnNm                    = ctryIsoEnNm;
		this.activeInd                      = activeInd;
		this.orgMktCd                       = orgMktCd;
		this.strategicMktCd                 = strategicMktCd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		KyndrylCountryDim other = (KyndrylCountryDim) obj;
		if (
            this.ctryIsoCd.equals(other.getCtryIsoCd())
         && this.ctryAlpha3Cd.equals(other.getCtryAlpha3Cd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.ctryIsoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryAlpha3Cd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryNumCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryIsoEnNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.activeInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.strategicMktCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CTRY_ISO_CD")
        + "," + Helpers.formatCsvField("CTRY_ALPHA3_CD")
        + "," + Helpers.formatCsvField("CTRY_NUM_CD")
        + "," + Helpers.formatCsvField("CTRY_NM")
        + "," + Helpers.formatCsvField("CTRY_ISO_EN_NM")
        + "," + Helpers.formatCsvField("ACTIVE_IND")
        + "," + Helpers.formatCsvField("ORG_MKT_CD")
        + "," + Helpers.formatCsvField("STRATEGIC_MKT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getCtryId() {
		return ctryId;
	}
	public void setCtryId(int ctryId) {
		this.ctryId = ctryId;
	}
	public String getCtryIsoCd() {
		return ctryIsoCd;
	}
	public void setCtryIsoCd(String ctryIsoCd) {
		this.ctryIsoCd = ctryIsoCd;
	}
	public String getCtryAlpha3Cd() {
		return ctryAlpha3Cd;
	}
	public void setCtryAlpha3Cd(String ctryAlpha3Cd) {
		this.ctryAlpha3Cd = ctryAlpha3Cd;
	}
	public String getCtryNumCd() {
		return ctryNumCd;
	}
	public void setCtryNumCd(String ctryNumCd) {
		this.ctryNumCd = ctryNumCd;
	}
	public String getCtryNm() {
		return ctryNm;
	}
	public void setCtryNm(String ctryNm) {
		this.ctryNm = ctryNm;
	}
	public String getCtryIsoEnNm() {
		return ctryIsoEnNm;
	}
	public void setCtryIsoEnNm(String ctryIsoEnNm) {
		this.ctryIsoEnNm = ctryIsoEnNm;
	}
	public String getActiveInd() {
		return activeInd;
	}
	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}
	public String getOrgMktCd() {
		return orgMktCd;
	}
	public void setOrgMktCd(String orgMktCd) {
		this.orgMktCd = orgMktCd;
	}
	public String getStrategicMktCd() {
		return strategicMktCd;
	}
	public void setStrategicMktCd(String strategicMktCd) {
		this.strategicMktCd = strategicMktCd;
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