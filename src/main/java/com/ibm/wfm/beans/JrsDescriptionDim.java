package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JrsDescriptionDim",baseTableName="REFT.JRS_DESCRIPTION")
public class JrsDescriptionDim  {
	@DbColumn(columnName="JRS_DESC_ID",isId=true)
	private int        jrsDescId;
	@DbColumn(columnName="JRS_CD",keySeq=1)
	private String     jrsCd;
	@DbColumn(columnName="JRS_NM")
	private String     jrsNm;
	@DbColumn(columnName="JRS_DESC")
	private String     jrsDesc;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public JrsDescriptionDim () {
		
	}
	
	// Define natural key constructor
	public JrsDescriptionDim (
      String     jrsCd
	) {
		this.jrsCd                          = jrsCd;
		
	}
	
	// Define base constructor
	public JrsDescriptionDim (
      String     jrsCd
    , String     jrsNm
    , String     jrsDesc
	) {
		this.jrsCd                          = jrsCd;
		this.jrsNm							= jrsNm;
		this.jrsDesc                        = jrsDesc;
		
	}
    
	// Define full constructor
	public JrsDescriptionDim (
		  int        jrsDescId
		, String     jrsCd
		, String     jrsNm
		, String     jrsDesc
	) {
		this.jrsDescId                      = jrsDescId;
		this.jrsCd                          = jrsCd;
		this.jrsNm							= jrsNm;
		this.jrsDesc                        = jrsDesc;
		
	}
	
	/*
	@Override
	public String getCode() { 
		return this.jrsCd
		;
	}
	public String getDescription() { 
		return null; 
	}
	*/
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JrsDescriptionDim other = (JrsDescriptionDim) obj;
		if (
            this.jrsCd.equals(other.getJrsCd())
         && this.jrsNm.equals(other.getJrsNm())
         && this.jrsDesc.equals(other.getJrsDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsDesc))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("JRS_NM")
        + "," + Helpers.formatCsvField("JRS_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getJrsDescId() {
		return jrsDescId;
	}
	public void setJrsDescId(int jrsDescId) {
		this.jrsDescId = jrsDescId;
	}
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
	public String getJrsDesc() {
		return jrsDesc;
	}
	public void setJrsDesc(String jrsDesc) {
		this.jrsDesc = jrsDesc;
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

	public String getJrsNm() {
		return jrsNm;
	}

	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
	}
}