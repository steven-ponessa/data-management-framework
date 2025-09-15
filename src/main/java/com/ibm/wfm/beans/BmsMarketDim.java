package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="MarketDim",baseTableName="REFT.MARKET",parentBeanName="BmsGeographyDim",parentBaseTableName="REFT.GEOGRAPHY")
public class BmsMarketDim extends NaryTreeNode {
	@DbColumn(columnName="MRKT_ID",isId=true)
	private int        mrktId;
	@DbColumn(columnName="MRKT_CD",keySeq=1)
	private String     mrktCd;
	@DbColumn(columnName="MRKT_NM")
	private String     mrktNm;
	@DbColumn(columnName="MRKT_DESC")
	private String     mrktDesc;
	@DbColumn(columnName="GEO_CD",foreignKeySeq=1)
	private String     geoCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BmsMarketDim () {
		this.level=1;
	}
	
	// Define base constructor
	public BmsMarketDim (
      String     mrktCd
	) {
		this.mrktCd                         = mrktCd;
		this.level                          = 1;
	}
	
	// Define base constructor
	public BmsMarketDim (
      String     mrktCd
    , String     mrktNm
    , String     mrktDesc
    , String     geoCd
	) {
		this.mrktCd                         = mrktCd;
		this.mrktNm                         = mrktNm;
		this.mrktDesc                       = mrktDesc;
		this.geoCd                          = geoCd;
		this.level                          = 1;
	}
    
	// Define full constructor
	public BmsMarketDim (
		  int        mrktId
		, String     mrktCd
		, String     mrktNm
		, String     mrktDesc
		, String     geoCd
	) {
		this.mrktId                         = mrktId;
		this.mrktCd                         = mrktCd;
		this.mrktNm                         = mrktNm;
		this.mrktDesc                       = mrktDesc;
		this.geoCd                          = geoCd;
		this.level                          = 1;
	}
	
	@Override
	public String getCode() { return this.mrktCd; }
	public String getDescription() { return this.mrktDesc; }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BmsMarketDim other = (BmsMarketDim) obj;
		if (
            this.mrktCd.equals(other.getMrktCd())
         && this.mrktNm.equals(other.getMrktNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.mrktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("MRKT_CD")
        + "," + Helpers.formatCsvField("MRKT_NM")
        + "," + Helpers.formatCsvField("MRKT_DESC")
        + "," + Helpers.formatCsvField("GEO_CD")
		;
	}
    
	// Define Getters and Setters
	public int getMrktId() {
		return mrktId;
	}
	public void setMrktId(int mrktId) {
		this.mrktId = mrktId;
	}
	public String getMrktCd() {
		return mrktCd;
	}
	public void setMrktCd(String mrktCd) {
		this.mrktCd = mrktCd;
	}
	public String getMrktNm() {
		return mrktNm;
	}
	public void setMrktNm(String mrktNm) {
		this.mrktNm = mrktNm;
	}
	public String getMrktDesc() {
		return mrktDesc;
	}
	public void setMrktDesc(String mrktDesc) {
		this.mrktDesc = mrktDesc;
	}
	public String getGeoCd() {
		return geoCd;
	}
	public void setGeoCd(String geoCd) {
		this.geoCd = geoCd;
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