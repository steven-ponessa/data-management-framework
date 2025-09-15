package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="BmsOfferingComponentDim",baseTableName="TEST.BMS_OFFERING_COMPONENT",parentBeanName="BmsOfferingDim",parentBaseTableName="TEST.BMS_OFFERING")
public class BmsOfferingComponentDim extends NaryTreeNode {
	@DbColumn(columnName="OFFERING_COMP_ID",isId=true)
	private int        offeringCompId;
	@DbColumn(columnName="OFFERING_COMP_CD",keySeq=1)
	private String     offeringCompCd;
	@DbColumn(columnName="OFFERING_COMP_NM")
	private String     offeringCompNm;
	@DbColumn(columnName="OFFERING_AREA_CD")
	private String     offeringAreaCd;
	@DbColumn(columnName="FCR_ELIGIBLE_IND")
	private String     fcrEligibleInd;
	@DbColumn(columnName="OFFERING_CAT_CD")
	private String     offeringCatCd;
	@DbColumn(columnName="OFFERING_CD",foreignKeySeq=1)
	private String     offeringCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BmsOfferingComponentDim () {
				this.level = 2;
	}
	
	// Define natural key constructor
	public BmsOfferingComponentDim (
      String     offeringCompCd
	) {
		this.offeringCompCd                 = offeringCompCd;
		this.level                          = 2;
	}
	
	// Define base constructor
	public BmsOfferingComponentDim (
      String     offeringCompCd
    , String     offeringCompNm
    , String     offeringAreaCd
    , String     fcrEligibleInd
    , String     offeringCatCd
    , String     offeringCd
	) {
		this.offeringCompCd                 = offeringCompCd;
		this.offeringCompNm                 = offeringCompNm;
		this.offeringAreaCd                 = offeringAreaCd;
		this.fcrEligibleInd                 = fcrEligibleInd;
		this.offeringCatCd                  = offeringCatCd;
		this.offeringCd                     = offeringCd;
		this.level                          = 2;
	}
    
	// Define full constructor
	public BmsOfferingComponentDim (
		  int        offeringCompId
		, String     offeringCompCd
		, String     offeringCompNm
		, String     offeringAreaCd
		, String     fcrEligibleInd
		, String     offeringCatCd
		, String     offeringCd
	) {
		this.offeringCompId                 = offeringCompId;
		this.offeringCompCd                 = offeringCompCd;
		this.offeringCompNm                 = offeringCompNm;
		this.offeringAreaCd                 = offeringAreaCd;
		this.fcrEligibleInd                 = fcrEligibleInd;
		this.offeringCatCd                  = offeringCatCd;
		this.offeringCd                     = offeringCd;
		this.level                          = 2;
	}
	
	@Override
	public String getCode() { 
		return this.offeringCompCd
		;
	}
	public String getDescription() { 
		return this.offeringCompNm; 
	}
	
	public int getId() {
		return this.offeringCompId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BmsOfferingComponentDim other = (BmsOfferingComponentDim) obj;
		if (
            this.offeringCompCd.equals(other.getOfferingCompCd())
         && this.offeringCompNm.equals(other.getOfferingCompNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.offeringCompCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringCompNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.fcrEligibleInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringCatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OFFERING_COMP_CD")
        + "," + Helpers.formatCsvField("OFFERING_COMP_NM")
        + "," + Helpers.formatCsvField("OFFERING_AREA_CD")
        + "," + Helpers.formatCsvField("FCR_ELIGIBLE_IND")
        + "," + Helpers.formatCsvField("OFFERING_CAT_CD")
        + "," + Helpers.formatCsvField("OFFERING_CD")
		;
	}
    
	// Define Getters and Setters
	public int getOfferingCompId() {
		return offeringCompId;
	}
	public void setOfferingCompId(int offeringCompId) {
		this.offeringCompId = offeringCompId;
	}
	public String getOfferingCompCd() {
		return offeringCompCd;
	}
	public void setOfferingCompCd(String offeringCompCd) {
		this.offeringCompCd = offeringCompCd;
	}
	public String getOfferingCompNm() {
		return offeringCompNm;
	}
	public void setOfferingCompNm(String offeringCompNm) {
		this.offeringCompNm = offeringCompNm;
	}
	public String getOfferingAreaCd() {
		return offeringAreaCd;
	}
	public void setOfferingAreaCd(String offeringAreaCd) {
		this.offeringAreaCd = offeringAreaCd;
	}
	public String getFcrEligibleInd() {
		return fcrEligibleInd;
	}
	public void setFcrEligibleInd(String fcrEligibleInd) {
		this.fcrEligibleInd = fcrEligibleInd;
	}
	public String getOfferingCatCd() {
		return offeringCatCd;
	}
	public void setOfferingCatCd(String offeringCatCd) {
		this.offeringCatCd = offeringCatCd;
	}
	public String getOfferingCd() {
		return offeringCd;
	}
	public void setOfferingCd(String offeringCd) {
		this.offeringCd = offeringCd;
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