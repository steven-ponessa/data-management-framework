package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class KyndrylStrategicMarketDim {
	@DbColumn(columnName="STRATEGIC_MKT_ID",isId=true)
	private int        strategicMktId;
	@DbColumn(columnName="STRATEGIC_MKT_CD",keySeq=1)
	private String     strategicMktCd;
	@DbColumn(columnName="STRATEGIC_MKT_SHORT_NM")
	private String     strategicMktShortNm;
	@DbColumn(columnName="STRATEGIC_MKT_MED_NM")
	private String     strategicMktMedNm;
	@DbColumn(columnName="STRATEGIC_MKT_NM")
	private String     strategicMktNm;
	@DbColumn(columnName="ORG_MKT_CD")
	private String     orgMktCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public KyndrylStrategicMarketDim () {}
	
	// Define natural key constructor
	public KyndrylStrategicMarketDim (
      String     strategicMktCd
	) {
		this.strategicMktCd                 = strategicMktCd;
	}
	
	// Define base constructor
	public KyndrylStrategicMarketDim (
      String     strategicMktCd
    , String     strategicMktShortNm
    , String     strategicMktMedNm
    , String     strategicMktNm
    , String     orgMktCd
	) {
		this.strategicMktCd                 = strategicMktCd;
		this.strategicMktShortNm            = strategicMktShortNm;
		this.strategicMktMedNm              = strategicMktMedNm;
		this.strategicMktNm                 = strategicMktNm;
		this.orgMktCd                       = orgMktCd;
	}
    
	// Define full constructor
	public KyndrylStrategicMarketDim (
		  int        strategicMktId
		, String     strategicMktCd
		, String     strategicMktShortNm
		, String     strategicMktMedNm
		, String     strategicMktNm
		, String     orgMktCd
	) {
		this.strategicMktId                 = strategicMktId;
		this.strategicMktCd                 = strategicMktCd;
		this.strategicMktShortNm            = strategicMktShortNm;
		this.strategicMktMedNm              = strategicMktMedNm;
		this.strategicMktNm                 = strategicMktNm;
		this.orgMktCd                       = orgMktCd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		KyndrylStrategicMarketDim other = (KyndrylStrategicMarketDim) obj;
		if (
            this.strategicMktCd.equals(other.getStrategicMktCd())
         && this.strategicMktShortNm.equals(other.getStrategicMktShortNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.strategicMktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.strategicMktShortNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.strategicMktMedNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.strategicMktNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.orgMktCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("STRATEGIC_MKT_CD")
        + "," + Helpers.formatCsvField("STRATEGIC_MKT_SHORT_NM")
        + "," + Helpers.formatCsvField("STRATEGIC_MKT_MED_NM")
        + "," + Helpers.formatCsvField("STRATEGIC_MKT_NM")
        + "," + Helpers.formatCsvField("ORG_MKT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getStrategicMktId() {
		return strategicMktId;
	}
	public void setStrategicMktId(int strategicMktId) {
		this.strategicMktId = strategicMktId;
	}
	public String getStrategicMktCd() {
		return strategicMktCd;
	}
	public void setStrategicMktCd(String strategicMktCd) {
		this.strategicMktCd = strategicMktCd;
	}
	public String getStrategicMktShortNm() {
		return strategicMktShortNm;
	}
	public void setStrategicMktShortNm(String strategicMktShortNm) {
		this.strategicMktShortNm = strategicMktShortNm;
	}
	public String getStrategicMktMedNm() {
		return strategicMktMedNm;
	}
	public void setStrategicMktMedNm(String strategicMktMedNm) {
		this.strategicMktMedNm = strategicMktMedNm;
	}
	public String getStrategicMktNm() {
		return strategicMktNm;
	}
	public void setStrategicMktNm(String strategicMktNm) {
		this.strategicMktNm = strategicMktNm;
	}
	public String getOrgMktCd() {
		return orgMktCd;
	}
	public void setOrgMktCd(String orgMktCd) {
		this.orgMktCd = orgMktCd;
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