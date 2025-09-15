package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="UtilizationCategoryDim",baseTableName="REFT.UTILIZATION_CATEGORY")
public class UtilizationCategoryDim extends NaryTreeNode {
	@DbColumn(columnName="UTIL_CAT_ID",isId=true)
	private int        utilCatId;
	@DbColumn(columnName="UTIL_CAT_CODE",keySeq=1)
	private String     utilCatCode;
	@DbColumn(columnName="UTIL_CAT_NM")
	private String     utilCatNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public UtilizationCategoryDim () {
		
	}
	
	// Define natural key constructor
	public UtilizationCategoryDim (
      String     utilCatCode
	) {
		this.utilCatCode                    = utilCatCode;
		
	}
	
	// Define base constructor
	public UtilizationCategoryDim (
      String     utilCatCode
    , String     utilCatNm
	) {
		this.utilCatCode                    = utilCatCode;
		this.utilCatNm                      = utilCatNm;
		
	}
    
	// Define full constructor
	public UtilizationCategoryDim (
		  int        utilCatId
		, String     utilCatCode
		, String     utilCatNm
	) {
		this.utilCatId                      = utilCatId;
		this.utilCatCode                    = utilCatCode;
		this.utilCatNm                      = utilCatNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.utilCatCode
		;
	}
	public String getDescription() { 
		return this.utilCatNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		UtilizationCategoryDim other = (UtilizationCategoryDim) obj;
		if (
            this.utilCatCode.equals(other.getUtilCatCode())
         && this.utilCatNm.equals(other.getUtilCatNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.utilCatCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.utilCatNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("UTIL_CAT_CODE")
        + "," + Helpers.formatCsvField("UTIL_CAT_NM")
		;
	}
    
	// Define Getters and Setters
	public int getUtilCatId() {
		return utilCatId;
	}
	public void setUtilCatId(int utilCatId) {
		this.utilCatId = utilCatId;
	}
	public String getUtilCatCode() {
		return utilCatCode;
	}
	public void setUtilCatCode(String utilCatCode) {
		this.utilCatCode = utilCatCode;
	}
	public String getUtilCatNm() {
		return utilCatNm;
	}
	public void setUtilCatNm(String utilCatNm) {
		this.utilCatNm = utilCatNm;
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