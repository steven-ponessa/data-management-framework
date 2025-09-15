package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OfferingPortfolioDim",baseTableName="REFT.OFFERING_PORTFOLIO",parentBeanName="ServiceLineDim",parentBaseTableName="REFT.SERVICE_LINE")
public class OfferingPortfolioDim extends NaryTreeNode implements Comparable<OfferingPortfolioDim> {
	@DbColumn(columnName="OFFERING_PORTFOLIO_ID",isId=true)
	private int        offeringPortfolioId;
	@DbColumn(columnName="OFFERING_PORTFOLIO_CD",keySeq=1)
	private String     offeringPortfolioCd;
	@DbColumn(columnName="OFFERING_PORTFOLIO_NM")
	private String     offeringPortfolioNm;
	@DbColumn(columnName="OFFERING_PORTFOLIO_DESC")
	private String     offeringPortfolioDesc;
	@DbColumn(columnName="SERVICE_LINE_CD",foreignKeySeq=1)
	private String     serviceLineCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OfferingPortfolioDim () {
				this.level = 3;
	}
	
	// Define natural key constructor
	public OfferingPortfolioDim (
      String     offeringPortfolioCd
	) {
		this.offeringPortfolioCd            = offeringPortfolioCd;
		this.level                          = 3;
	}
	
	// Define base constructor
	public OfferingPortfolioDim (
      String     offeringPortfolioCd
    , String     offeringPortfolioNm
    , String     offeringPortfolioDesc
    , String     serviceLineCd
	) {
		this.offeringPortfolioCd            = offeringPortfolioCd;
		this.offeringPortfolioNm            = offeringPortfolioNm;
		this.offeringPortfolioDesc          = offeringPortfolioDesc;
		this.serviceLineCd                  = serviceLineCd;
		this.level                          = 3;
	}
    
	// Define full constructor
	public OfferingPortfolioDim (
		  int        offeringPortfolioId
		, String     offeringPortfolioCd
		, String     offeringPortfolioNm
		, String     offeringPortfolioDesc
		, String     serviceLineCd
	) {
		this.offeringPortfolioId            = offeringPortfolioId;
		this.offeringPortfolioCd            = offeringPortfolioCd;
		this.offeringPortfolioNm            = offeringPortfolioNm;
		this.offeringPortfolioDesc          = offeringPortfolioDesc;
		this.serviceLineCd                  = serviceLineCd;
		this.level                          = 3;
	}
	
	@Override
	public String getCode() { 
		return this.offeringPortfolioCd
		;
	}
	public String getDescription() { 
		return this.offeringPortfolioDesc; 
	}
	
	@Override
    public int compareTo(OfferingPortfolioDim o) {
        return this.getOfferingPortfolioCd().compareTo(o.getOfferingPortfolioCd());
    }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		OfferingPortfolioDim other = (OfferingPortfolioDim) obj;
		if (
            this.offeringPortfolioCd.equals(other.getOfferingPortfolioCd())
         && this.offeringPortfolioNm.equals(other.getOfferingPortfolioNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.offeringPortfolioCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringPortfolioNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.offeringPortfolioDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceLineCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OFFERING_PORTFOLIO_CD")
        + "," + Helpers.formatCsvField("OFFERING_PORTFOLIO_NM")
        + "," + Helpers.formatCsvField("OFFERING_PORTFOLIO_DESC")
        + "," + Helpers.formatCsvField("SERVICE_LINE_CD")
		;
	}
    
	// Define Getters and Setters
	public int getOfferingPortfolioId() {
		return offeringPortfolioId;
	}
	public void setOfferingPortfolioId(int offeringPortfolioId) {
		this.offeringPortfolioId = offeringPortfolioId;
	}
	public String getOfferingPortfolioCd() {
		return offeringPortfolioCd;
	}
	public void setOfferingPortfolioCd(String offeringPortfolioCd) {
		this.offeringPortfolioCd = offeringPortfolioCd;
	}
	public String getOfferingPortfolioNm() {
		return offeringPortfolioNm;
	}
	public void setOfferingPortfolioNm(String offeringPortfolioNm) {
		this.offeringPortfolioNm = offeringPortfolioNm;
	}
	public String getOfferingPortfolioDesc() {
		return offeringPortfolioDesc;
	}
	public void setOfferingPortfolioDesc(String offeringPortfolioDesc) {
		this.offeringPortfolioDesc = offeringPortfolioDesc;
	}
	public String getServiceLineCd() {
		return serviceLineCd;
	}
	public void setServiceLineCd(String serviceLineCd) {
		this.serviceLineCd = serviceLineCd;
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