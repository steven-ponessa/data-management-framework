package com.ibm.wfm.beans.sdss;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="RegionDim",baseTableName="SDSS.REGION",parentBeanPackageName="com.ibm.wfm.beans.sdss", parentBeanName="MarketDim",parentBaseTableName="SDSS.MARKET")
public class RegionDim extends NaryTreeNode {
	@DbColumn(columnName="RGN_ID",isId=true)
	private int        rgnId;
	@DbColumn(columnName="RGN_CD",keySeq=1)
	private String     rgnCd;
	@DbColumn(columnName="RGN_NM")
	private String     rgnNm;
	@DbColumn(columnName="LGCY_RGN_CD")
	private String     lgcyRgnCd;
	@DbColumn(columnName="MRKT_CD",foreignKeySeq=1)
	private String     mrktCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public RegionDim () {
				this.level = 3;
	}
	
	// Define natural key constructor
	public RegionDim (
      String     rgnCd
	) {
		this.rgnCd                          = rgnCd;
		this.level                          = 3;
	}
    
	// Define full constructor
	public RegionDim (
		  int        rgnId
		, String     rgnCd
		, String     rgnNm
		, String     lgcyRgnCd
		, String     mrktCd
	) {
		this.rgnId                          = rgnId;
		this.rgnCd                          = rgnCd;
		this.rgnNm                          = rgnNm;
		this.lgcyRgnCd                      = lgcyRgnCd;
		this.mrktCd                         = mrktCd;
		this.level                          = 3;
	}
	
	@Override
	public String getCode() { 
		return this.rgnCd
		;
	}
	public String getDescription() { 
		return this.rgnNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		RegionDim other = (RegionDim) obj;
		if (
            this.rgnCd.equals(other.getRgnCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.rgnId))
        + "," + Helpers.formatCsvField(String.valueOf(this.rgnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.rgnNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lgcyRgnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("RGN_ID")
        + "," + Helpers.formatCsvField("RGN_CD")
        + "," + Helpers.formatCsvField("RGN_NM")
        + "," + Helpers.formatCsvField("LGCY_RGN_CD")
        + "," + Helpers.formatCsvField("MRKT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getRgnId() {
		return rgnId;
	}
	public void setRgnId(int rgnId) {
		this.rgnId = rgnId;
	}
	public String getRgnCd() {
		return rgnCd;
	}
	public void setRgnCd(String rgnCd) {
		this.rgnCd = rgnCd;
	}
	public String getRgnNm() {
		return rgnNm;
	}
	public void setRgnNm(String rgnNm) {
		this.rgnNm = rgnNm;
	}
	public String getLgcyRgnCd() {
		return lgcyRgnCd;
	}
	public void setLgcyRgnCd(String lgcyRgnCd) {
		this.lgcyRgnCd = lgcyRgnCd;
	}
	public String getMrktCd() {
		return mrktCd;
	}
	public void setMrktCd(String mrktCd) {
		this.mrktCd = mrktCd;
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