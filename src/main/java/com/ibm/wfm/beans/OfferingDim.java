package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OfferingDim",baseTableName="REFT.OFFERING",parentBeanName="OfferingPortfolioDim",parentBaseTableName="REFT.OFFERING_PORTFOLIO")
public class OfferingDim extends NaryTreeNode implements Comparable<OfferingDim> {
	@DbColumn(columnName="OFFERING_ID",isId=true)
	private int        offeringId;
	@DbColumn(columnName="OFFERING_CD",keySeq=1)
	private String     offeringCd;
	@DbColumn(columnName="OFFERING_NM")
	private String     offeringNm;
	@DbColumn(columnName="OFFERING_DESC")
	private String     offeringDesc;
	@DbColumn(columnName="LEAD_PRACTICE_CD")
	private String     leadPracticeCd;
	@DbColumn(columnName="LEAD_PRACTICE_NM")
	private String     leadPracticeNm;
	@DbColumn(columnName="FINANCIAL_OFFERING_ATTRIBUTE_NM")
	private String     financialOfferingAttributeNm;
	@DbColumn(columnName="OFFERING_PORTFOLIO_CD",foreignKeySeq=1)
	private String     offeringPortfolioCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OfferingDim () {
				this.level = 4;
	}
	
	// Define natural key constructor
	public OfferingDim (
      String     offeringCd
	) {
		this.offeringCd                     = offeringCd;
		this.level                          = 4;
	}
	
	// Define base constructor
	public OfferingDim (
      String     offeringCd
    , String     offeringNm
    , String     offeringDesc
    , String     leadPracticeCd
    , String     leadPracticeNm
    , String     financialOfferingAttributeNm
    , String     offeringPortfolioCd
	) {
		this.offeringCd                     = offeringCd;
		this.offeringNm                     = offeringNm;
		this.offeringDesc                   = offeringDesc;
		this.leadPracticeCd                 = leadPracticeCd;
		this.leadPracticeNm                 = leadPracticeNm;
		this.financialOfferingAttributeNm   = financialOfferingAttributeNm;
		this.offeringPortfolioCd            = offeringPortfolioCd;
		this.level                          = 4;
	}
    
	// Define full constructor
	public OfferingDim (
		  int        offeringId
		, String     offeringCd
		, String     offeringNm
		, String     offeringDesc
		, String     leadPracticeCd
		, String     leadPracticeNm
		, String     financialOfferingAttributeNm
		, String     offeringPortfolioCd
	) {
		this.offeringId                     = offeringId;
		this.offeringCd                     = offeringCd;
		this.offeringNm                     = offeringNm;
		this.offeringDesc                   = offeringDesc;
		this.leadPracticeCd                 = leadPracticeCd;
		this.leadPracticeNm                 = leadPracticeNm;
		this.financialOfferingAttributeNm   = financialOfferingAttributeNm;
		this.offeringPortfolioCd            = offeringPortfolioCd;
		this.level                          = 4;
	}
	
	@Override
	public String getCode() { 
		return this.offeringCd
		;
	}
	public String getDescription() { 
		return this.offeringDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		OfferingDim other = (OfferingDim) obj;
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
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.leadPracticeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.leadPracticeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.financialOfferingAttributeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringPortfolioCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OFFERING_CD")
        + "," + Helpers.formatCsvField("OFFERING_NM")
        + "," + Helpers.formatCsvField("OFFERING_DESC")
        + "," + Helpers.formatCsvField("LEAD_PRACTICE_CD")
        + "," + Helpers.formatCsvField("LEAD_PRACTICE_NM")
        + "," + Helpers.formatCsvField("FINANCIAL_OFFERING_ATTRIBUTE_NM")
        + "," + Helpers.formatCsvField("OFFERING_PORTFOLIO_CD")
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
	public String getOfferingDesc() {
		return offeringDesc;
	}
	public void setOfferingDesc(String offeringDesc) {
		this.offeringDesc = offeringDesc;
	}
	public String getLeadPracticeCd() {
		return leadPracticeCd;
	}
	public void setLeadPracticeCd(String leadPracticeCd) {
		this.leadPracticeCd = leadPracticeCd;
	}
	public String getLeadPracticeNm() {
		return leadPracticeNm;
	}
	public void setLeadPracticeNm(String leadPracticeNm) {
		this.leadPracticeNm = leadPracticeNm;
	}
	public String getFinancialOfferingAttributeNm() {
		return financialOfferingAttributeNm;
	}
	public void setFinancialOfferingAttributeNm(String financialOfferingAttributeNm) {
		this.financialOfferingAttributeNm = financialOfferingAttributeNm;
	}
	public String getOfferingPortfolioCd() {
		return offeringPortfolioCd;
	}
	public void setOfferingPortfolioCd(String offeringPortfolioCd) {
		this.offeringPortfolioCd = offeringPortfolioCd;
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
	@Override
    public int compareTo(OfferingDim o) {
        return this.getOfferingCd().compareTo(o.getOfferingCd());
    }
}