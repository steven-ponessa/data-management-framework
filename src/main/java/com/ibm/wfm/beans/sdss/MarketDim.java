package com.ibm.wfm.beans.sdss;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="MarketDim",baseTableName="SDSS.MARKET",parentBeanPackageName="com.ibm.wfm.beans.sdss",parentBeanName="GeographyDim",parentBaseTableName="SDSS.GEOGRAPHY")
public class MarketDim extends NaryTreeNode {
	@DbColumn(columnName="MRKT_ID",isId=true)
	private int        mrktId;
	@DbColumn(columnName="MRKT_CD",keySeq=1)
	private String     mrktCd;
	@DbColumn(columnName="MRKT_NM")
	private String     mrktNm;
	@DbColumn(columnName="LGCY_MRKT_CD")
	private String     lgcyMrktCd;
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
	public MarketDim () {
				this.level = 2;
	}
	
	// Define natural key constructor
	public MarketDim (
      String     mrktCd
	) {
		this.mrktCd                         = mrktCd;
		this.level                          = 2;
	}
    
	// Define full constructor
	public MarketDim (
		  int        mrktId
		, String     mrktCd
		, String     mrktNm
		, String     lgcyMrktCd
		, String     geoCd
	) {
		this.mrktId                         = mrktId;
		this.mrktCd                         = mrktCd;
		this.mrktNm                         = mrktNm;
		this.lgcyMrktCd                     = lgcyMrktCd;
		this.geoCd                          = geoCd;
		this.level                          = 2;
	}
	
	@Override
	public String getCode() { 
		return this.mrktCd
		;
	}
	public String getDescription() { 
		return this.mrktNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		MarketDim other = (MarketDim) obj;
		if (
            this.mrktCd.equals(other.getMrktCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.mrktId))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lgcyMrktCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.geoCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("MRKT_ID")
        + "," + Helpers.formatCsvField("MRKT_CD")
        + "," + Helpers.formatCsvField("MRKT_NM")
        + "," + Helpers.formatCsvField("LGCY_MRKT_CD")
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
	public String getLgcyMrktCd() {
		return lgcyMrktCd;
	}
	public void setLgcyMrktCd(String lgcyMrktCd) {
		this.lgcyMrktCd = lgcyMrktCd;
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