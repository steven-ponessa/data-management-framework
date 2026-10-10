package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.ExcelSheet;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SkillsValueFrameworkDim",baseTableName="REFT.SKILLS_VALUE_FRAMEWORK")
public class SkillsValueFrameworkDim extends NaryTreeNode {
	@DbColumn(columnName="ESVF_ID",isId=true,keySeq=1)
	private String        esvfId;
	@DbColumn(columnName="GEOGRAPHY_TYPE_NM")
	@ExcelSheet(columnName="Geography Type",columnNum=0)	
	private String     geographyTypeNm;
	@DbColumn(columnName="GEOGRAPHY_NM")
	@ExcelSheet(columnName="Geography",columnNum=1)	
	private String     geographyNm;
	@DbColumn(columnName="MARKET_NM")
	@ExcelSheet(columnName="Market",columnNum=2)	
	private String     marketNm;
	@DbColumn(columnName="MARKET_REGION_NM")
	@ExcelSheet(columnName="Market Region",columnNum=3)	
	private String     marketRegionNm;
	@DbColumn(columnName="COUNTRY_NM")
	@ExcelSheet(columnName="Country",columnNum=4)	
	private String     countryNm;
	@DbColumn(columnName="GROWTH_PLATFORM_NM")
	@ExcelSheet(columnName="JR/S Growth Platform",columnNum=5)	
	private String     growthPlatformNm;
	@DbColumn(columnName="SERVICE_LINE_NM")
	@ExcelSheet(columnName="JR/S Service Line",columnNum=6)	
	private String     serviceLineNm;
	@DbColumn(columnName="PRACTICE_NM")
	@ExcelSheet(columnName="JR/S Practice",columnNum=7)	
	private String     practiceNm;
	@DbColumn(columnName="JRS_NM")
	@ExcelSheet(columnName="JR/S",columnNum=8)	
	private String     jrsNm;
	@DbColumn(columnName="JRS_ID")
	@ExcelSheet(columnName="JR/S ID",columnNum=9)	
	private String     jrsId;
	
	@DbColumn(columnName="MSI_NM")
	@ExcelSheet(columnName="MSI",columnNum=11)	
	private String     msiNm;
	@DbColumn(columnName="BAND_CAT_NM")
	@ExcelSheet(columnName="Band Category",columnNum=12)	
	private String     bandCatNm;
	@DbColumn(columnName="ADS_NM")
	@ExcelSheet(columnName="Anticipated Demand for Skills",columnNum=13)	
	private String     adsNm;	

	@DbColumn(columnName="SOURCING_STRATEGY_NM")
	@ExcelSheet(columnName="Sourcing Strategy",columnNum=14)	
	private String     sourcingStrategyNm;
	
	@DbColumn(columnName="APRVL_CD")
	@ExcelSheet(columnName="Approval Code",columnNum=15)	
	private String     aprvlCd;
	
	@DbColumn(columnName="INTRANET_ID")
	@ExcelSheet(columnName="Intranet Id",columnNum=16)	
	private String     intranetId;
	
	@DbColumn(columnName="EXCEPT_CD")
	@ExcelSheet(columnName="Exception Code",columnNum=16)	
	private int     exceptCd;	
	
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SkillsValueFrameworkDim () {
		
	}
	
	// Define natural key constructor
	public SkillsValueFrameworkDim (
      String        esvfId
	) {
		this.esvfId                         = esvfId;
		
	}
	
	// Define base constructor
	public SkillsValueFrameworkDim (
      String     geographyTypeNm
    , String     geographyNm
    , String     marketNm
    , String     marketRegionNm
    , String     countryNm
    , String     growthPlatformNm
    , String     serviceLineNm
    , String     practiceNm
    , String     jrsNm
    , String     jrsId
    , String     msiNm
    , String     bandCatNm
    , String     adsNm
    , String     sourcingStrategyNm
	) {
		this.geographyTypeNm                = geographyTypeNm;
		this.geographyNm                    = geographyNm;
		this.marketNm                       = marketNm;
		this.marketRegionNm                 = marketRegionNm;
		this.countryNm                      = countryNm;
		this.growthPlatformNm               = growthPlatformNm;
		this.serviceLineNm                  = serviceLineNm;
		this.practiceNm                     = practiceNm;
		this.jrsNm                          = jrsNm;
		this.jrsId                          = jrsId;
		this.bandCatNm                      = bandCatNm;
		this.msiNm                          = msiNm;
		this.adsNm                          = adsNm;
		this.sourcingStrategyNm             = sourcingStrategyNm;
	}
    
	// Define full constructor
	public SkillsValueFrameworkDim (
		  String     esvfId
		, String     geographyTypeNm
		, String     geographyNm
		, String     marketNm
		, String     marketRegionNm
		, String     countryNm
		, String     growthPlatformNm
		, String     serviceLineNm
		, String     practiceNm
		, String     jrsNm
		, String     jrsId
		, String     msiNm
		, String     bandCatNm
		, String     adsNm
		, String     sourcingStrategyNm
		, String     aprvlCd
		, String     intranetId
		, int        exceptCd
	) {
		this.esvfId                         = esvfId;
		this.geographyTypeNm                = geographyTypeNm;
		this.geographyNm                    = geographyNm;
		this.marketNm                       = marketNm;
		this.marketRegionNm                 = marketRegionNm;
		this.countryNm                      = countryNm;
		this.growthPlatformNm               = growthPlatformNm;
		this.serviceLineNm                  = serviceLineNm;
		this.practiceNm                     = practiceNm;
		this.jrsNm                          = jrsNm;
		this.jrsId                          = jrsId;
		this.bandCatNm                      = bandCatNm;
		this.msiNm                          = msiNm;
		this.adsNm                          = adsNm;
		this.sourcingStrategyNm             = sourcingStrategyNm;
		this.aprvlCd                        = aprvlCd;
		this.intranetId                     = intranetId;
		this.exceptCd                       = exceptCd;
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.esvfId)
		;
	}
	public String getDescription() { 
		return this.countryNm+"-"+this.jrsId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SkillsValueFrameworkDim other = (SkillsValueFrameworkDim) obj;
		if (
            this.geographyTypeNm.equals(other.getGeographyTypeNm())
         && this.geographyNm.equals(other.getGeographyNm())
         && this.marketNm.equals(other.getMarketNm())
         && this.marketRegionNm==null?other.getMarketRegionNm()==null:this.marketRegionNm.equals(other.getMarketRegionNm())
         && this.countryNm.equals(other.getCountryNm())
         && this.bandCatNm.equals(other.getBandCatNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.geographyTypeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.geographyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.marketNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.marketRegionNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.countryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.growthPlatformNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceLineNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.practiceNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsId)) 
        + "," + Helpers.formatCsvField(String.valueOf(this.msiNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.bandCatNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.adsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.sourcingStrategyNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GEOGRAPHY_TYPE_NM")
        + "," + Helpers.formatCsvField("GEOGRAPHY_NM")
        + "," + Helpers.formatCsvField("MARKET_NM")
        + "," + Helpers.formatCsvField("MARKET_REGION_NM")
        + "," + Helpers.formatCsvField("COUNTRY_NM")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_NM")
        + "," + Helpers.formatCsvField("SERVICE_LINE_NM")
        + "," + Helpers.formatCsvField("PRACTICE_NM")
        + "," + Helpers.formatCsvField("JRS_NM")
        + "," + Helpers.formatCsvField("JRS_ID")
        + "," + Helpers.formatCsvField("MSI_NM")
        + "," + Helpers.formatCsvField("BAND_CAT_NM")
        + "," + Helpers.formatCsvField("ADS_NM")
        + "," + Helpers.formatCsvField("SVF_NM")
        + "," + Helpers.formatCsvField("SOURCING_STRATEGY_NM")
		;
	}
    
	// Define Getters and Setters
	public String getEsvfId() {
		return esvfId;
	}
	public String getId() {
		return esvfId;
	}
	public void setEsvfId(String esvfId) {
		this.esvfId = esvfId;
	}
	public String getGeographyTypeNm() {
		return geographyTypeNm;
	}
	public void setGeographyTypeNm(String geographyTypeNm) {
		this.geographyTypeNm = geographyTypeNm;
	}
	public String getGeographyNm() {
		return geographyNm;
	}
	public void setGeographyNm(String geographyNm) {
		this.geographyNm = geographyNm;
	}
	public String getMarketNm() {
		return marketNm;
	}
	public void setMarketNm(String marketNm) {
		this.marketNm = marketNm;
	}
	public String getMarketRegionNm() {
		return marketRegionNm;
	}
	public void setMarketRegionNm(String marketRegionNm) {
		this.marketRegionNm = marketRegionNm;
	}
	public String getCountryNm() {
		return countryNm;
	}
	public void setCountryNm(String countryNm) {
		this.countryNm = countryNm;
	}
	public String getGrowthPlatformNm() {
		return growthPlatformNm;
	}
	public void setGrowthPlatformNm(String growthPlatformNm) {
		this.growthPlatformNm = growthPlatformNm;
	}
	public String getServiceLineNm() {
		return serviceLineNm;
	}
	public void setServiceLineNm(String serviceLineNm) {
		this.serviceLineNm = serviceLineNm;
	}
	public String getPracticeNm() {
		return practiceNm;
	}
	public void setPracticeNm(String practiceNm) {
		this.practiceNm = practiceNm;
	}
	public String getJrsNm() {
		return jrsNm;
	}
	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
	}
	public String getJrsId() {
		return jrsId;
	}
	public void setJrsId(String jrsId) {
		this.jrsId = jrsId;
	}
	public String getMsiNm() {
		return msiNm;
	}
	public void setMsiNm(String msiNm) {
		this.msiNm = msiNm;
	}
	public String getAdsNm() {
		return adsNm;
	}
	public void setAdsNm(String adsNm) {
		this.adsNm = adsNm;
	}
	public String getSourcingStrategyNm() {
		return sourcingStrategyNm;
	}
	public void setSourcingStrategyNm(String sourcingStrategyNm) {
		this.sourcingStrategyNm = sourcingStrategyNm;
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
	public String getBandCatNm() {
		return bandCatNm;
	}
	public void setBandCatNm(String bandCatNm) {
		this.bandCatNm = bandCatNm;
	}

	public String getAprvlCd() {
		return aprvlCd;
	}

	public void setAprvlCd(String aprvlCd) {
		this.aprvlCd = aprvlCd;
	}

	public String getIntranetId() {
		return intranetId;
	}

	public void setIntranetId(String intranetId) {
		this.intranetId = intranetId;
	}

	public int getExceptCd() {
		return exceptCd;
	}

	public void setExceptCd(int exceptCd) {
		this.exceptCd = exceptCd;
	}
}