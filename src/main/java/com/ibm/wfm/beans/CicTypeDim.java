package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CicTypeDim",baseTableName="REFT.CIC_TYPE")
public class CicTypeDim extends NaryTreeNode {
	@DbColumn(columnName="CIC_TYPE_ID",isId=true)
	private int        cicTypeId;
	@DbColumn(columnName="CIC_TYPE_CODE",keySeq=1)
	private String     cicTypeCode;
	@DbColumn(columnName="CIC_TYPE_NM")
	private String     cicTypeNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public CicTypeDim () {
		
	}
	
	// Define natural key constructor
	public CicTypeDim (
      String     cicTypeCode
	) {
		this.cicTypeCode                    = cicTypeCode;
		
	}
	
	// Define base constructor
	public CicTypeDim (
      String     cicTypeCode
    , String     cicTypeNm
	) {
		this.cicTypeCode                    = cicTypeCode;
		this.cicTypeNm                      = cicTypeNm;
		
	}
    
	// Define full constructor
	public CicTypeDim (
		  int        cicTypeId
		, String     cicTypeCode
		, String     cicTypeNm
	) {
		this.cicTypeId                      = cicTypeId;
		this.cicTypeCode                    = cicTypeCode;
		this.cicTypeNm                      = cicTypeNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.cicTypeCode
		;
	}
	public String getDescription() { 
		return this.cicTypeNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		CicTypeDim other = (CicTypeDim) obj;
		if (
            this.cicTypeCode.equals(other.getCicTypeCode())
         && this.cicTypeNm.equals(other.getCicTypeNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.cicTypeCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicTypeNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CIC_TYPE_CODE")
        + "," + Helpers.formatCsvField("CIC_TYPE_NM")
		;
	}
    
	// Define Getters and Setters
	public int getCicTypeId() {
		return cicTypeId;
	}
	public void setCicTypeId(int cicTypeId) {
		this.cicTypeId = cicTypeId;
	}
	public String getCicTypeCode() {
		return cicTypeCode;
	}
	public void setCicTypeCode(String cicTypeCode) {
		this.cicTypeCode = cicTypeCode;
	}
	public String getCicTypeNm() {
		return cicTypeNm;
	}
	public void setCicTypeNm(String cicTypeNm) {
		this.cicTypeNm = cicTypeNm;
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