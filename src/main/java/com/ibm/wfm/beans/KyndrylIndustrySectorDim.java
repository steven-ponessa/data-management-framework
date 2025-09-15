package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class KyndrylIndustrySectorDim {
	@DbColumn(columnName="IND_SCTR_ID",isId=true)
	private int        indSctrId;
	@DbColumn(columnName="IND_SCTR_CD",keySeq=1)
	private String     indSctrCd;
	@DbColumn(columnName="IND_SCTR_SHORT_NM")
	private String     indSctrShortNm;
	@DbColumn(columnName="IND_SCTR_MED_NM")
	private String     indSctrMedNm;
	@DbColumn(columnName="IND_SCTR_NM")
	private String     indSctrNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public KyndrylIndustrySectorDim () {}
	
	// Define natural key constructor
	public KyndrylIndustrySectorDim (
      String     indSctrCd
	) {
		this.indSctrCd                      = indSctrCd;
	}
	
	// Define base constructor
	public KyndrylIndustrySectorDim (
      String     indSctrCd
    , String     indSctrShortNm
    , String     indSctrMedNm
    , String     indSctrNm
	) {
		this.indSctrCd                      = indSctrCd;
		this.indSctrShortNm                 = indSctrShortNm;
		this.indSctrMedNm                   = indSctrMedNm;
		this.indSctrNm                      = indSctrNm;
	}
    
	// Define full constructor
	public KyndrylIndustrySectorDim (
		  int        indSctrId
		, String     indSctrCd
		, String     indSctrShortNm
		, String     indSctrMedNm
		, String     indSctrNm
	) {
		this.indSctrId                      = indSctrId;
		this.indSctrCd                      = indSctrCd;
		this.indSctrShortNm                 = indSctrShortNm;
		this.indSctrMedNm                   = indSctrMedNm;
		this.indSctrNm                      = indSctrNm;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		KyndrylIndustrySectorDim other = (KyndrylIndustrySectorDim) obj;
		if (
            this.indSctrCd.equals(other.getIndSctrCd())
         && this.indSctrShortNm.equals(other.getIndSctrShortNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.indSctrCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.indSctrShortNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.indSctrMedNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.indSctrNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("IND_SCTR_CD")
        + "," + Helpers.formatCsvField("IND_SCTR_SHORT_NM")
        + "," + Helpers.formatCsvField("IND_SCTR_MED_NM")
        + "," + Helpers.formatCsvField("IND_SCTR_NM")
		;
	}
    
	// Define Getters and Setters
	public int getIndSctrId() {
		return indSctrId;
	}
	public void setIndSctrId(int indSctrId) {
		this.indSctrId = indSctrId;
	}
	public String getIndSctrCd() {
		return indSctrCd;
	}
	public void setIndSctrCd(String indSctrCd) {
		this.indSctrCd = indSctrCd;
	}
	public String getIndSctrShortNm() {
		return indSctrShortNm;
	}
	public void setIndSctrShortNm(String indSctrShortNm) {
		this.indSctrShortNm = indSctrShortNm;
	}
	public String getIndSctrMedNm() {
		return indSctrMedNm;
	}
	public void setIndSctrMedNm(String indSctrMedNm) {
		this.indSctrMedNm = indSctrMedNm;
	}
	public String getIndSctrNm() {
		return indSctrNm;
	}
	public void setIndSctrNm(String indSctrNm) {
		this.indSctrNm = indSctrNm;
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