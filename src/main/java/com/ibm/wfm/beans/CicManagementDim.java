package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CicManagementDim",baseTableName="REFT.CIC_MANAGEMENT")
public class CicManagementDim extends NaryTreeNode {
	@DbColumn(columnName="CIC_MANAGEMENT_ID",isId=true)
	private int        cicManagementId;
	@DbColumn(columnName="CIC_MANAGEMENT_CODE",keySeq=1)
	private String     cicManagementCode;
	@DbColumn(columnName="CIC_MANAGEMENT_NM")
	private String     cicManagementNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public CicManagementDim () {
		
	}
	
	// Define natural key constructor
	public CicManagementDim (
      String     cicManagementCode
	) {
		this.cicManagementCode              = cicManagementCode;
		
	}
	
	// Define base constructor
	public CicManagementDim (
      String     cicManagementCode
    , String     cicManagementNm
	) {
		this.cicManagementCode              = cicManagementCode;
		this.cicManagementNm                = cicManagementNm;
		
	}
    
	// Define full constructor
	public CicManagementDim (
		  int        cicManagementId
		, String     cicManagementCode
		, String     cicManagementNm
	) {
		this.cicManagementId                = cicManagementId;
		this.cicManagementCode              = cicManagementCode;
		this.cicManagementNm                = cicManagementNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.cicManagementCode
		;
	}
	public String getDescription() { 
		return this.cicManagementNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		CicManagementDim other = (CicManagementDim) obj;
		if (
            this.cicManagementCode.equals(other.getCicManagementCode())
         && this.cicManagementNm.equals(other.getCicManagementNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.cicManagementCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicManagementNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CIC_MANAGEMENT_CODE")
        + "," + Helpers.formatCsvField("CIC_MANAGEMENT_NM")
		;
	}
    
	// Define Getters and Setters
	public int getCicManagementId() {
		return cicManagementId;
	}
	public void setCicManagementId(int cicManagementId) {
		this.cicManagementId = cicManagementId;
	}
	public String getCicManagementCode() {
		return cicManagementCode;
	}
	public void setCicManagementCode(String cicManagementCode) {
		this.cicManagementCode = cicManagementCode;
	}
	public String getCicManagementNm() {
		return cicManagementNm;
	}
	public void setCicManagementNm(String cicManagementNm) {
		this.cicManagementNm = cicManagementNm;
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