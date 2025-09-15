package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="BmsOfferingDim",baseTableName="TEST.BMS_OFFERING",parentBeanName="BmsOfferingCategoryDim",parentBaseTableName="TEST.BMS_OFFERING_CATEGORY")
public class BmsOfferingDim extends NaryTreeNode {
	@DbColumn(columnName="OFFERING_ID",isId=true)
	private int        offeringId;
	@DbColumn(columnName="OFFERING_CD",keySeq=1)
	private String     offeringCd;
	@DbColumn(columnName="OFFERING_NM")
	private String     offeringNm;
	@DbColumn(columnName="OFFERING_CAT_CD",foreignKeySeq=1)
	private String     offeringCatCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BmsOfferingDim () {
				this.level = 1;
	}
	
	// Define natural key constructor
	public BmsOfferingDim (
      String     offeringCd
	) {
		this.offeringCd                     = offeringCd;
		this.level                          = 1;
	}
	
	// Define base constructor
	public BmsOfferingDim (
      String     offeringCd
    , String     offeringNm
    , String     offeringCatCd
	) {
		this.offeringCd                     = offeringCd;
		this.offeringNm                     = offeringNm;
		this.offeringCatCd                  = offeringCatCd;
		this.level                          = 1;
	}
    
	// Define full constructor
	public BmsOfferingDim (
		  int        offeringId
		, String     offeringCd
		, String     offeringNm
		, String     offeringCatCd
	) {
		this.offeringId                     = offeringId;
		this.offeringCd                     = offeringCd;
		this.offeringNm                     = offeringNm;
		this.offeringCatCd                  = offeringCatCd;
		this.level                          = 1;
	}
	
	public int getId() {
		return this.offeringId;
	}
	
	@Override
	public String getCode() { 
		return this.offeringCd
		;
	}
	public String getDescription() { 
		return this.offeringNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BmsOfferingDim other = (BmsOfferingDim) obj;
		if (
            this.offeringCd.equals(other.getOfferingCd())
         && this.offeringNm.equals(other.getOfferingNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.offeringCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringCatCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OFFERING_CD")
        + "," + Helpers.formatCsvField("OFFERING_NM")
        + "," + Helpers.formatCsvField("OFFERING_CAT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getOfferingId() {
		return offeringId;
	}
	public void setOfferingId(int offeringId) {
		this.offeringId = offeringId;
	}
	public String getOfferingCd() {
		return offeringCd;
	}
	public void setOfferingCd(String offeringCd) {
		this.offeringCd = offeringCd;
	}
	public String getOfferingNm() {
		return offeringNm;
	}
	public void setOfferingNm(String offeringNm) {
		this.offeringNm = offeringNm;
	}
	public String getOfferingCatCd() {
		return offeringCatCd;
	}
	public void setOfferingCatCd(String offeringCatCd) {
		this.offeringCatCd = offeringCatCd;
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