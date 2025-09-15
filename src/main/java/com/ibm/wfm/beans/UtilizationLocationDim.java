package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="UtilizationLocationDim",baseTableName="REFT.UTILIZATION_LOCATION")
public class UtilizationLocationDim extends NaryTreeNode {
	@DbColumn(columnName="UTIL_LOCATION_ID",isId=true)
	private int        utilLocationId;
	@DbColumn(columnName="UTIL_LOCATION_CODE",keySeq=1)
	private String     utilLocationCode;
	@DbColumn(columnName="UTIL_LOCATION_NM")
	private String     utilLocationNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public UtilizationLocationDim () {
		
	}
	
	// Define natural key constructor
	public UtilizationLocationDim (
      String     utilLocationCode
	) {
		this.utilLocationCode               = utilLocationCode;
		
	}
	
	// Define base constructor
	public UtilizationLocationDim (
      String     utilLocationCode
    , String     utilLocationNm
	) {
		this.utilLocationCode               = utilLocationCode;
		this.utilLocationNm                 = utilLocationNm;
		
	}
    
	// Define full constructor
	public UtilizationLocationDim (
		  int        utilLocationId
		, String     utilLocationCode
		, String     utilLocationNm
	) {
		this.utilLocationId                 = utilLocationId;
		this.utilLocationCode               = utilLocationCode;
		this.utilLocationNm                 = utilLocationNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.utilLocationCode
		;
	}
	public String getDescription() { 
		return this.getUtilLocationNm(); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		UtilizationLocationDim other = (UtilizationLocationDim) obj;
		if (
            this.utilLocationCode.equals(other.getUtilLocationCode())
         && this.utilLocationNm.equals(other.getUtilLocationNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.utilLocationCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.utilLocationNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("UTIL_LOCATION_CODE")
        + "," + Helpers.formatCsvField("UTIL_LOCATION_NM")
		;
	}
    
	// Define Getters and Setters
	public int getUtilLocationId() {
		return utilLocationId;
	}
	public void setUtilLocationId(int utilLocationId) {
		this.utilLocationId = utilLocationId;
	}
	public String getUtilLocationCode() {
		return utilLocationCode;
	}
	public void setUtilLocationCode(String utilLocationCode) {
		this.utilLocationCode = utilLocationCode;
	}
	public String getUtilLocationNm() {
		return utilLocationNm;
	}
	public void setUtilLocationNm(String utilLocationNm) {
		this.utilLocationNm = utilLocationNm;
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