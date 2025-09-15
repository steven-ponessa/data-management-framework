package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class KyndrylCtryIndSctrAssocDim {
	@DbColumn(columnName="CTRY_IND_SCTR_ASSOC_ID",isId=true)
	private int        ctryIndSctrAssocId;
	@DbColumn(columnName="CTRY_ISO_CD",keySeq=1)
	private String     ctryIsoCd;
	@DbColumn(columnName="IND_SCTR_CD",keySeq=2)
	private String     indSctrCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public KyndrylCtryIndSctrAssocDim () {}
	
	// Define natural key constructor
	public KyndrylCtryIndSctrAssocDim (
      String     ctryIsoCd
    , String     indSctrCd
	) {
		this.ctryIsoCd                      = ctryIsoCd;
		this.indSctrCd                      = indSctrCd;
	}
	
	// Define base constructor

    
	// Define full constructor
	public KyndrylCtryIndSctrAssocDim (
		  int        ctryIndSctrAssocId
		, String     ctryIsoCd
		, String     indSctrCd
	) {
		this.ctryIndSctrAssocId             = ctryIndSctrAssocId;
		this.ctryIsoCd                      = ctryIsoCd;
		this.indSctrCd                      = indSctrCd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		KyndrylCtryIndSctrAssocDim other = (KyndrylCtryIndSctrAssocDim) obj;
		if (
            this.ctryIsoCd.equals(other.getCtryIsoCd())
         && this.indSctrCd.equals(other.getIndSctrCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.ctryIsoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.indSctrCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CTRY_ISO_CD")
        + "," + Helpers.formatCsvField("IND_SCTR_CD")
		;
	}
    
	// Define Getters and Setters
	public int getCtryIndSctrAssocId() {
		return ctryIndSctrAssocId;
	}
	public void setCtryIndSctrAssocId(int ctryIndSctrAssocId) {
		this.ctryIndSctrAssocId = ctryIndSctrAssocId;
	}
	public String getCtryIsoCd() {
		return ctryIsoCd;
	}
	public void setCtryIsoCd(String ctryIsoCd) {
		this.ctryIsoCd = ctryIsoCd;
	}
	public String getIndSctrCd() {
		return indSctrCd;
	}
	public void setIndSctrCd(String indSctrCd) {
		this.indSctrCd = indSctrCd;
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