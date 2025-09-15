package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CountryMeasurementDim",baseTableName="ERDM.COUNTRY_MEASUREMENT",parentBeanName="RegionDim",parentBaseTableName="ERDM.REGION")
public class CountryMeasurementDim extends NaryTreeNode {
	@DbColumn(columnName="COUNTRY_MEASUREMENT_CD",keySeq=1)
	private String     countryMeasurementCd;
	@DbColumn(columnName="REGION_CD",foreignKeySeq=1)
	private String     regionCd;
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
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public CountryMeasurementDim () {
		this.level=10;
	}
	
	// Define natural key constructor
	public CountryMeasurementDim (
      String     countryMeasurementCd
	) {
		this.countryMeasurementCd           = countryMeasurementCd;
		this.level=10;
	}
    
	// Define full constructor
	public CountryMeasurementDim (
		  String     countryMeasurementCd
		, String     regionCd
		, int        levelnum
		, String     longdescription
		, String     mediumdescription
		, String     shortdescription
		, String     codeType
		, String     complianceCd
		, String     usageRule
	) {
		this.countryMeasurementCd           = countryMeasurementCd;
		this.regionCd                       = regionCd;
		this.levelnum                       = levelnum;
		this.longdescription                = longdescription;
		this.mediumdescription              = mediumdescription;
		this.shortdescription               = shortdescription;
		this.codeType                       = codeType;
		this.complianceCd                   = complianceCd;
		this.usageRule                      = usageRule;
		this.level=10;
	}
	
	@Override
	public String getCode() { 
		return this.countryMeasurementCd
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
                    
		CountryMeasurementDim other = (CountryMeasurementDim) obj;
		if (
            this.countryMeasurementCd.equals(other.getCountryMeasurementCd())
         && this.regionCd.equals(other.getRegionCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.countryMeasurementCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.regionCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelnum))
        + "," + Helpers.formatCsvField(String.valueOf(this.longdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.mediumdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.shortdescription))
        + "," + Helpers.formatCsvField(String.valueOf(this.codeType))
        + "," + Helpers.formatCsvField(String.valueOf(this.complianceCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.usageRule))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("COUNTRY_MEASUREMENT_CD")
        + "," + Helpers.formatCsvField("REGION_CD")
        + "," + Helpers.formatCsvField("LEVELNUM")
        + "," + Helpers.formatCsvField("LONGDESCRIPTION")
        + "," + Helpers.formatCsvField("MEDIUMDESCRIPTION")
        + "," + Helpers.formatCsvField("SHORTDESCRIPTION")
        + "," + Helpers.formatCsvField("CODE_TYPE")
        + "," + Helpers.formatCsvField("COMPLIANCE_CD")
        + "," + Helpers.formatCsvField("USAGE_RULE")
		;
	}
    
	// Define Getters and Setters
	public String getCountryMeasurementCd() {
		return countryMeasurementCd;
	}
	public void setCountryMeasurementCd(String countryMeasurementCd) {
		this.countryMeasurementCd = countryMeasurementCd;
	}
	public String getRegionCd() {
		return regionCd;
	}
	public void setRegionCd(String regionCd) {
		this.regionCd = regionCd;
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