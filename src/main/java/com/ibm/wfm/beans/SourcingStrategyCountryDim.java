package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SourcingStrategyCountryDim",baseTableName="REFT.SOURCING_STRATEGY_COUNTRY")
public class SourcingStrategyCountryDim {
	@DbColumn(columnName="SRC_STRGY_CTRY_ID",isId=true)
	private int        srcStrgyCtryId;
	@DbColumn(columnName="GEO_TYP_CD",keySeq=1)
	private String     geoTypCd;
	@DbColumn(columnName="GEO_CD",keySeq=2)
	private String     geoCd;
	@DbColumn(columnName="MRKT_CD",keySeq=3)
	private String     mrktCd;
	@DbColumn(columnName="MRKT_RGN_CD",keySeq=4)
	private String     mrktRgnCd;
	@DbColumn(columnName="CTRY_ISO_CD",keySeq=5)
	private String     ctryIsoCd;
	@DbColumn(columnName="CTRY_CD")
	private String     ctryCd;
	@DbColumn(columnName="JRS_CD",keySeq=6)
	private String     jrsCd;
	@DbColumn(columnName="GEO_TYP_NM")
	private String     geoTypNm;
	@DbColumn(columnName="GEO_NM")
	private String     geoNm;
	@DbColumn(columnName="MRKT_NM")
	private String     mrktNm;
	@DbColumn(columnName="MRKT_RGN_NM")
	private String     mrktRgnNm;
	@DbColumn(columnName="CTRY_NM")
	private String     ctryNm;
	@DbColumn(columnName="JRS_NM")
	private String     jrsNm;
	@DbColumn(columnName="LONGEVITY_NM")
	private String     longevityNm;
	@DbColumn(columnName="SKILL_VALUE_NM")
	private String     skillValueNm;
	@DbColumn(columnName="SRC_STRGY_CD")
	private String     srcStrgyCd;
	@DbColumn(columnName="SRC_STRGY_NM")
	private String     srcStrgyNm;
	@DbColumn(columnName="SRC_TYP_NM")
	private String     srcTypNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SourcingStrategyCountryDim () {
		
	}
	
	// Define natural key constructor
	public SourcingStrategyCountryDim (
      String     geoTypCd
    , String     geoCd
    , String     mrktCd
    , String     mrktRgnCd
    , String     ctryIsoCd
    , String     jrsCd
	) {
		this.geoTypCd                       = geoTypCd;
		this.geoCd                          = geoCd;
		this.mrktCd                         = mrktCd;
		this.mrktRgnCd                      = mrktRgnCd;
		this.ctryIsoCd                      = ctryIsoCd;
		this.jrsCd                          = jrsCd;
		
	}
	
	// Define base constructor
	public SourcingStrategyCountryDim (
      String     geoTypCd
    , String     geoCd
    , String     mrktCd
    , String     mrktRgnCd
    , String     ctryIsoCd
    , String     ctryCd
    , String     jrsCd
    , String     geoTypNm
    , String     geoNm
    , String     mrktNm
    , String     mrktRgnNm
    , String     ctryNm
    , String     jrsNm
    , String     longevityNm
    , String     skillValueNm
    , String     srcStrgyCd
    , String     srcStrgyNm
    , String     srcTypNm
	) {
		this.geoTypCd                       = geoTypCd;
		this.geoCd                          = geoCd;
		this.mrktCd                         = mrktCd;
		this.mrktRgnCd                      = mrktRgnCd;
		this.ctryIsoCd                      = ctryIsoCd;
		this.ctryCd                         = ctryCd;
		this.jrsCd                          = jrsCd;
		this.geoTypNm                       = geoTypNm;
		this.geoNm                          = geoNm;
		this.mrktNm                         = mrktNm;
		this.mrktRgnNm                      = mrktRgnNm;
		this.ctryNm                         = ctryNm;
		this.jrsNm                          = jrsNm;
		this.longevityNm                    = longevityNm;
		this.skillValueNm                   = skillValueNm;
		this.srcStrgyCd                     = srcStrgyCd;
		this.srcStrgyNm                     = srcStrgyNm;
		this.srcTypNm                       = srcTypNm;
		
	}
    
	// Define full constructor
	public SourcingStrategyCountryDim (
		  int        srcStrgyCtryId
		, String     geoTypCd
		, String     geoCd
		, String     mrktCd
		, String     mrktRgnCd
		, String     ctryIsoCd
		, String     ctryCd
		, String     jrsCd
		, String     geoTypNm
		, String     geoNm
		, String     mrktNm
		, String     mrktRgnNm
		, String     ctryNm
		, String     jrsNm
		, String     longevityNm
		, String     skillValueNm
		, String     srcStrgyCd
		, String     srcStrgyNm
		, String     srcTypNm
	) {
		this.srcStrgyCtryId                 = srcStrgyCtryId;
		this.geoTypCd                       = geoTypCd;
		this.geoCd                          = geoCd;
		this.mrktCd                         = mrktCd;
		this.mrktRgnCd                      = mrktRgnCd;
		this.ctryIsoCd                      = ctryIsoCd;
		this.ctryCd                         = ctryCd;
		this.jrsCd                          = jrsCd;
		this.geoTypNm                       = geoTypNm;
		this.geoNm                          = geoNm;
		this.mrktNm                         = mrktNm;
		this.mrktRgnNm                      = mrktRgnNm;
		this.ctryNm                         = ctryNm;
		this.jrsNm                          = jrsNm;
		this.longevityNm                    = longevityNm;
		this.skillValueNm                   = skillValueNm;
		this.srcStrgyCd                     = srcStrgyCd;
		this.srcStrgyNm                     = srcStrgyNm;
		this.srcTypNm                       = srcTypNm;
		
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SourcingStrategyCountryDim other = (SourcingStrategyCountryDim) obj;
		if (
            this.geoTypCd.equals(other.getGeoTypCd())
         && this.geoCd.equals(other.getGeoCd())
         && this.mrktCd.equals(other.getMrktCd())
         && this.mrktRgnCd.equals(other.getMrktRgnCd())
         && this.ctryIsoCd.equals(other.getCtryIsoCd())
         && this.jrsCd.equals(other.getJrsCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.geoTypCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktRgnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryIsoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoTypNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktRgnNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.longevityNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.skillValueNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcStrgyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcStrgyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcTypNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GEO_TYP_CD")
        + "," + Helpers.formatCsvField("GEO_CD")
        + "," + Helpers.formatCsvField("MRKT_CD")
        + "," + Helpers.formatCsvField("MRKT_RGN_CD")
        + "," + Helpers.formatCsvField("CTRY_ISO_CD")
        + "," + Helpers.formatCsvField("CTRY_CD")
        + "," + Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("GEO_TYP_NM")
        + "," + Helpers.formatCsvField("GEO_NM")
        + "," + Helpers.formatCsvField("MRKT_NM")
        + "," + Helpers.formatCsvField("MRKT_RGN_NM")
        + "," + Helpers.formatCsvField("CTRY_NM")
        + "," + Helpers.formatCsvField("JRS_NM")
        + "," + Helpers.formatCsvField("LONGEVITY_NM")
        + "," + Helpers.formatCsvField("SKILL_VALUE_NM")
        + "," + Helpers.formatCsvField("SRC_STRGY_CD")
        + "," + Helpers.formatCsvField("SRC_STRGY_NM")
        + "," + Helpers.formatCsvField("SRC_TYP_NM")
		;
	}
    
	// Define Getters and Setters
	public int getSrcStrgyCtryId() {
		return srcStrgyCtryId;
	}
	public void setSrcStrgyCtryId(int srcStrgyCtryId) {
		this.srcStrgyCtryId = srcStrgyCtryId;
	}
	public String getGeoTypCd() {
		return geoTypCd;
	}
	public void setGeoTypCd(String geoTypCd) {
		this.geoTypCd = geoTypCd;
	}
	public String getGeoCd() {
		return geoCd;
	}
	public void setGeoCd(String geoCd) {
		this.geoCd = geoCd;
	}
	public String getMrktCd() {
		return mrktCd;
	}
	public void setMrktCd(String mrktCd) {
		this.mrktCd = mrktCd;
	}
	public String getMrktRgnCd() {
		return mrktRgnCd;
	}
	public void setMrktRgnCd(String mrktRgnCd) {
		this.mrktRgnCd = mrktRgnCd;
	}
	public String getCtryIsoCd() {
		return ctryIsoCd;
	}
	public void setCtryIsoCd(String ctryIsoCd) {
		this.ctryIsoCd = ctryIsoCd;
	}
	public String getCtryCd() {
		return ctryCd;
	}
	public void setCtryCd(String ctryCd) {
		this.ctryCd = ctryCd;
	}
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
	public String getGeoTypNm() {
		return geoTypNm;
	}
	public void setGeoTypNm(String geoTypNm) {
		this.geoTypNm = geoTypNm;
	}
	public String getGeoNm() {
		return geoNm;
	}
	public void setGeoNm(String geoNm) {
		this.geoNm = geoNm;
	}
	public String getMrktNm() {
		return mrktNm;
	}
	public void setMrktNm(String mrktNm) {
		this.mrktNm = mrktNm;
	}
	public String getMrktRgnNm() {
		return mrktRgnNm;
	}
	public void setMrktRgnNm(String mrktRgnNm) {
		this.mrktRgnNm = mrktRgnNm;
	}
	public String getCtryNm() {
		return ctryNm;
	}
	public void setCtryNm(String ctryNm) {
		this.ctryNm = ctryNm;
	}
	public String getJrsNm() {
		return jrsNm;
	}
	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
	}
	public String getLongevityNm() {
		return longevityNm;
	}
	public void setLongevityNm(String longevityNm) {
		this.longevityNm = longevityNm;
	}
	public String getSkillValueNm() {
		return skillValueNm;
	}
	public void setSkillValueNm(String skillValueNm) {
		this.skillValueNm = skillValueNm;
	}
	public String getSrcStrgyCd() {
		return srcStrgyCd;
	}
	public void setSrcStrgyCd(String srcStrgyCd) {
		this.srcStrgyCd = srcStrgyCd;
	}
	public String getSrcStrgyNm() {
		return srcStrgyNm;
	}
	public void setSrcStrgyNm(String srcStrgyNm) {
		this.srcStrgyNm = srcStrgyNm;
	}
	public String getSrcTypNm() {
		return srcTypNm;
	}
	public void setSrcTypNm(String srcTypNm) {
		this.srcTypNm = srcTypNm;
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