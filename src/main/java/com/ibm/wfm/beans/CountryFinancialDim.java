package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CountryFinancialDim",baseTableName="ERDM.COUNTRY_FINANCIAL",parentBeanName="RegionDim",parentBaseTableName="ERDM.REGION")
public class CountryFinancialDim extends NaryTreeNode {
	@DbColumn(columnName="COUNTRY_FINANCIAL_CD",keySeq=1)
	private String     countryFinancialCd;
	@DbColumn(columnName="COUNTRY_MEASUREMENT_CD")
	private String     countryMeasurementCd;
	@DbColumn(columnName="LEVELNUM")
	private int        levelnum;
	@DbColumn(columnName="LONGDESCRIPTION")
	private String     longdescription;
	@DbColumn(columnName="MEDIUMDESCRIPTION")
	private String     mediumdescription;
	@DbColumn(columnName="SHORTDESCRIPTION")
	private String     shortdescription;
	@DbColumn(columnName="CODE_TYPE")
	private String     codeType;
	@DbColumn(columnName="COMPLIANCE_CD")
	private String     complianceCd;
	@DbColumn(columnName="USAGE_RULE")
	private String     usageRule;
	@DbColumn(columnName="REGION_CD",foreignKeySeq=1)
	private String     regionCd;
	@DbColumn(columnName="MARKET_CD")
	private String     marketCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public CountryFinancialDim () {
		this.level=0;
	}
	
	// Define natural key constructor
	public CountryFinancialDim (
      String     countryFinancialCd
	) {
		this.countryFinancialCd             = countryFinancialCd;
		this.level=0;
	}
    
	// Define full constructor
	public CountryFinancialDim (
		  String     countryFinancialCd
		, String     countryMeasurementCd
		, int        levelnum
		, String     longdescription
		, String     mediumdescription
		, String     shortdescription
		, String     codeType
		, String     complianceCd
		, String     usageRule
		, String     regionCd
		, String     marketCd
	) {
		this.countryFinancialCd             = countryFinancialCd;
		this.countryMeasurementCd           = countryMeasurementCd;
		this.levelnum                       = levelnum;
		this.longdescription                = longdescription;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.codeType                       = codeType;
		this.complianceCd                   = complianceCd;
		this.usageRule                      = usageRule;
		this.regionCd                       = regionCd;
		this.marketCd                       = marketCd;
		this.level=0;
	}
	
	@Override
	public String getCode() { 
		return this.countryFinancialCd
		;
	}
	public String getDescription() { 
		return this.longdescription; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		CountryFinancialDim other = (CountryFinancialDim) obj;
		if (
            this.countryFinancialCd.equals(other.getCountryFinancialCd())
         && this.countryMeasurementCd.equals(other.getCountryMeasurementCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.countryFinancialCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.countryMeasurementCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelnum))
        + "," + Helpers.formatCsvField(String.valueOf(this.longdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.mediumdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.shortdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.codeType))
        + "," + Helpers.formatCsvField(String.valueOf(this.complianceCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRule))
        + "," + Helpers.formatCsvField(String.valueOf(this.regionCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("COUNTRY_FINANCIAL_CD")
        + "," + Helpers.formatCsvField("COUNTRY_MEASUREMENT_CD")
        + "," + Helpers.formatCsvField("LEVELNUM")
        + "," + Helpers.formatCsvField("LONGDESCRIPTION")
        + "," + Helpers.formatCsvField("MEDIUMDESCRIPTION")
        + "," + Helpers.formatCsvField("SHORTDESCRIPTION")
        + "," + Helpers.formatCsvField("CODE_TYPE")
        + "," + Helpers.formatCsvField("COMPLIANCE_CD")
        + "," + Helpers.formatCsvField("USAGE_RULE")
        + "," + Helpers.formatCsvField("REGION_CD")
		;
	}
    
	// Define Getters and Setters
	public String getCountryFinancialCd() {
		return countryFinancialCd;
	}
	public void setCountryFinancialCd(String countryFinancialCd) {
		this.countryFinancialCd = countryFinancialCd;
	}
	public String getCountryMeasurementCd() {
		return countryMeasurementCd;
	}
	public void setCountryMeasurementCd(String countryMeasurementCd) {
		this.countryMeasurementCd = countryMeasurementCd;
	}
	public int getLevelnum() {
		return levelnum;
	}
	public void setLevelnum(int levelnum) {
		this.levelnum = levelnum;
	}
	public String getLongdescription() {
		return longdescription;
	}
	public void setLongdescription(String longdescription) {
		this.longdescription = longdescription;
	}
	public String getMediumdescription() {
		return mediumdescription;
	}
	public void setMediumdescription(String mediumdescription) {
		this.mediumdescription = mediumdescription;
	}
	public String getShortdescription() {
		return shortdescription;
	}
	public void setShortdescription(String shortdescription) {
		this.shortdescription = shortdescription;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getComplianceCd() {
		return complianceCd;
	}
	public void setComplianceCd(String complianceCd) {
		this.complianceCd = complianceCd;
	}
	public String getUsageRule() {
		return usageRule;
	}
	public void setUsageRule(String usageRule) {
		this.usageRule = usageRule;
	}
	public String getRegionCd() {
		return regionCd;
	}
	public void setRegionCd(String regionCd) {
		this.regionCd = regionCd;
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

	public String getMarketCd() {
		return marketCd;
	}

	public void setMarketCd(String marketCd) {
		this.marketCd = marketCd;
	}
}