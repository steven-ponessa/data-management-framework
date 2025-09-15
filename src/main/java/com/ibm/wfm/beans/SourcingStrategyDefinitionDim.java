package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SourcingStrategyDefinitionDim",baseTableName="REFT.SOURCING_STRATEGY_DEFINITION")
public class SourcingStrategyDefinitionDim extends NaryTreeNode {
	@DbColumn(columnName="SRC_STRGY_CTRY_ID",isId=true)
	private int        srcStrgyCtryId;
	@DbColumn(columnName="JRS_CD",keySeq=1)
	private String     jrsCd;
	@DbColumn(columnName="GEO_CD",keySeq=2)
	private String     geoCd;
	@DbColumn(columnName="MRKT_CD",keySeq=3)
	private String     mrktCd;
	@DbColumn(columnName="RGN_CD",keySeq=4)
	private String     rgnCd;
	@DbColumn(columnName="CTRY_CD",keySeq=5)
	private String     ctryCd;
	@DbColumn(columnName="JRS_NM")
	private String     jrsNm;
	@DbColumn(columnName="GEO_NM")
	private String     geoNm;
	@DbColumn(columnName="MRKT_NM")
	private String     mrktNm;
	@DbColumn(columnName="RGN_NM")
	private String     rgnNm;
	@DbColumn(columnName="CTRY_NM")
	private String     ctryNm;
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
	public SourcingStrategyDefinitionDim () {
		
	}
	
	// Define natural key constructor
	public SourcingStrategyDefinitionDim (
      String     jrsCd
    , String     geoCd
    , String     mrktCd
    , String     rgnCd
    , String     ctryCd
	) {
		this.jrsCd                          = jrsCd;
		this.geoCd                          = geoCd;
		this.mrktCd                         = mrktCd;
		this.rgnCd                          = rgnCd;
		this.ctryCd                         = ctryCd;
		
	}
	
	// Define base constructor
	public SourcingStrategyDefinitionDim (
      String     jrsCd
    , String     geoCd
    , String     mrktCd
    , String     rgnCd
    , String     ctryCd
    , String     jrsNm
    , String     geoNm
    , String     mrktNm
    , String     rgnNm
    , String     ctryNm
    , String     srcStrgyCd
    , String     srcStrgyNm
    , String     srcTypNm
	) {
		this.jrsCd                          = jrsCd;
		this.geoCd                          = geoCd;
		this.mrktCd                         = mrktCd;
		this.rgnCd                          = rgnCd;
		this.ctryCd                         = ctryCd;
		this.jrsNm                          = jrsNm;
		this.geoNm                          = geoNm;
		this.mrktNm                         = mrktNm;
		this.rgnNm                          = rgnNm;
		this.ctryNm                         = ctryNm;
		this.srcStrgyCd                     = srcStrgyCd;
		this.srcStrgyNm                     = srcStrgyNm;
		this.srcTypNm                       = srcTypNm;
		
	}
    
	// Define full constructor
	public SourcingStrategyDefinitionDim (
		  int        srcStrgyCtryId
		, String     jrsCd
		, String     geoCd
		, String     mrktCd
		, String     rgnCd
		, String     ctryCd
		, String     jrsNm
		, String     geoNm
		, String     mrktNm
		, String     rgnNm
		, String     ctryNm
		, String     srcStrgyCd
		, String     srcStrgyNm
		, String     srcTypNm
	) {
		this.srcStrgyCtryId                 = srcStrgyCtryId;
		this.jrsCd                          = jrsCd;
		this.geoCd                          = geoCd;
		this.mrktCd                         = mrktCd;
		this.rgnCd                          = rgnCd;
		this.ctryCd                         = ctryCd;
		this.jrsNm                          = jrsNm;
		this.geoNm                          = geoNm;
		this.mrktNm                         = mrktNm;
		this.rgnNm                          = rgnNm;
		this.ctryNm                         = ctryNm;
		this.srcStrgyCd                     = srcStrgyCd;
		this.srcStrgyNm                     = srcStrgyNm;
		this.srcTypNm                       = srcTypNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.jrsCd
    +":"+ this.geoCd
    +":"+ this.mrktCd
    +":"+ this.rgnCd
    +":"+ this.ctryCd
		;
	}
	public String getDescription() { 
		return null; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SourcingStrategyDefinitionDim other = (SourcingStrategyDefinitionDim) obj;
		if (
            this.jrsCd.equals(other.getJrsCd())
         && this.geoCd.equals(other.getGeoCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.rgnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.rgnNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.ctryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcStrgyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcStrgyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcTypNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("GEO_CD")
        + "," + Helpers.formatCsvField("MRKT_CD")
        + "," + Helpers.formatCsvField("RGN_CD")
        + "," + Helpers.formatCsvField("CTRY_CD")
        + "," + Helpers.formatCsvField("JRS_NM")
        + "," + Helpers.formatCsvField("GEO_NM")
        + "," + Helpers.formatCsvField("MRKT_NM")
        + "," + Helpers.formatCsvField("RGN_NM")
        + "," + Helpers.formatCsvField("CTRY_NM")
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
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
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
	public String getRgnCd() {
		return rgnCd;
	}
	public void setRgnCd(String rgnCd) {
		this.rgnCd = rgnCd;
	}
	public String getCtryCd() {
		return ctryCd;
	}
	public void setCtryCd(String ctryCd) {
		this.ctryCd = ctryCd;
	}
	public String getJrsNm() {
		return jrsNm;
	}
	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
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
	public String getRgnNm() {
		return rgnNm;
	}
	public void setRgnNm(String rgnNm) {
		this.rgnNm = rgnNm;
	}
	public String getCtryNm() {
		return ctryNm;
	}
	public void setCtryNm(String ctryNm) {
		this.ctryNm = ctryNm;
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