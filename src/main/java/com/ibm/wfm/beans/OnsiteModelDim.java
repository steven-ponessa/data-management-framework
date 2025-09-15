package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OnsiteModelDim",baseTableName="REFT.ONSITE_MODEL")
public class OnsiteModelDim extends NaryTreeNode {
	@DbColumn(columnName="ONSITE_MODEL_ID",isId=true)
	private int        onsiteModelId;
	@DbColumn(columnName="ONSITE_MODEL_CODE",keySeq=1)
	private String     onsiteModelCode;
	@DbColumn(columnName="ONSITE_MODEL_NM")
	private String     onsiteModelNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OnsiteModelDim () {
		
	}
	
	// Define natural key constructor
	public OnsiteModelDim (
      String     onsiteModelCode
	) {
		this.onsiteModelCode                = onsiteModelCode;
		
	}
	
	// Define base constructor
	public OnsiteModelDim (
      String     onsiteModelCode
    , String     onsiteModelNm
	) {
		this.onsiteModelCode                = onsiteModelCode;
		this.onsiteModelNm                  = onsiteModelNm;
		
	}
    
	// Define full constructor
	public OnsiteModelDim (
		  int        onsiteModelId
		, String     onsiteModelCode
		, String     onsiteModelNm
	) {
		this.onsiteModelId                  = onsiteModelId;
		this.onsiteModelCode                = onsiteModelCode;
		this.onsiteModelNm                  = onsiteModelNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.onsiteModelCode
		;
	}
	public String getDescription() { 
		return null; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		OnsiteModelDim other = (OnsiteModelDim) obj;
		if (
            this.onsiteModelCode.equals(other.getOnsiteModelCode())
         && this.onsiteModelNm.equals(other.getOnsiteModelNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.onsiteModelCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.onsiteModelNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("ONSITE_MODEL_CODE")
        + "," + Helpers.formatCsvField("ONSITE_MODEL_NM")
		;
	}
    
	// Define Getters and Setters
	public int getOnsiteModelId() {
		return onsiteModelId;
	}
	public void setOnsiteModelId(int onsiteModelId) {
		this.onsiteModelId = onsiteModelId;
	}
	public String getOnsiteModelCode() {
		return onsiteModelCode;
	}
	public void setOnsiteModelCode(String onsiteModelCode) {
		this.onsiteModelCode = onsiteModelCode;
	}
	public String getOnsiteModelNm() {
		return onsiteModelNm;
	}
	public void setOnsiteModelNm(String onsiteModelNm) {
		this.onsiteModelNm = onsiteModelNm;
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