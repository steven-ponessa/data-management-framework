package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="BmsOfferingCategoryDim",baseTableName="TEST.BMS_OFFERING_CATEGORY")
public class BmsOfferingCategoryDim extends NaryTreeNode {
	@DbColumn(columnName="OFFERING_CAT_ID",isId=true)
	private int        offeringCatId;
	@DbColumn(columnName="OFFERING_CAT_CD",keySeq=1)
	private String     offeringCatCd;
	@DbColumn(columnName="OFFERING_CAT_NM")
	private String     offeringCatNm;
	@DbColumn(columnName="LOB_ID")
	private String     lobId;
	@DbColumn(columnName="BUS_MEAS_DIV_CD")
	private String     busMeasDivCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BmsOfferingCategoryDim () {
				this.level = 0;
	}
	
	// Define natural key constructor
	public BmsOfferingCategoryDim (
      String     offeringCatCd
	) {
		this.offeringCatCd                  = offeringCatCd;
		this.level                          = 0;
	}
	
	// Define base constructor
	public BmsOfferingCategoryDim (
      String     offeringCatCd
    , String     offeringCatNm
    , String     lobId
    , String     busMeasDivCd
	) {
		this.offeringCatCd                  = offeringCatCd;
		this.offeringCatNm                  = offeringCatNm;
		this.lobId                          = lobId;
		this.busMeasDivCd                   = busMeasDivCd;
		this.level                          = 0;
	}
    
	// Define full constructor
	public BmsOfferingCategoryDim (
		  int        offeringCatId
		, String     offeringCatCd
		, String     offeringCatNm
		, String     lobId
		, String     busMeasDivCd
	) {
		this.offeringCatId                  = offeringCatId;
		this.offeringCatCd                  = offeringCatCd;
		this.offeringCatNm                  = offeringCatNm;
		this.lobId                          = lobId;
		this.busMeasDivCd                   = busMeasDivCd;
		this.level                          = 0;
	}
	
	@Override
	public String getCode() { 
		return this.offeringCatCd
		;
	}
	public String getDescription() { 
		return this.offeringCatNm; 
	}
	
	public int getId() {
		return this.offeringCatId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BmsOfferingCategoryDim other = (BmsOfferingCategoryDim) obj;
		if (
            this.offeringCatCd.equals(other.getOfferingCatCd())
         && this.offeringCatNm.equals(other.getOfferingCatNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.offeringCatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringCatNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lobId))
        + "," + Helpers.formatCsvField(String.valueOf(this.busMeasDivCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OFFERING_CAT_CD")
        + "," + Helpers.formatCsvField("OFFERING_CAT_NM")
        + "," + Helpers.formatCsvField("LOB_ID")
        + "," + Helpers.formatCsvField("BUS_MEAS_DIV_CD")
		;
	}
    
	// Define Getters and Setters
	public int getOfferingCatId() {
		return offeringCatId;
	}
	public void setOfferingCatId(int offeringCatId) {
		this.offeringCatId = offeringCatId;
	}
	public String getOfferingCatCd() {
		return offeringCatCd;
	}
	public void setOfferingCatCd(String offeringCatCd) {
		this.offeringCatCd = offeringCatCd;
	}
	public String getOfferingCatNm() {
		return offeringCatNm;
	}
	public void setOfferingCatNm(String offeringCatNm) {
		this.offeringCatNm = offeringCatNm;
	}
	public String getLobId() {
		return lobId;
	}
	public void setLobId(String lobId) {
		this.lobId = lobId;
	}
	public String getBusMeasDivCd() {
		return busMeasDivCd;
	}
	public void setBusMeasDivCd(String busMeasDivCd) {
		this.busMeasDivCd = busMeasDivCd;
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