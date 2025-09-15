package com.ibm.wfm.beans.rrpg;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="RegionRrpgDim",baseTableName="RRPG.REGION",parentBeanPackageName="com.ibm.wfm.beans.rrpg",parentBeanName="MarketRrpgDim",parentBaseTableName="RRPG.MARKET")
public class RegionRrpgDim extends NaryTreeNode  implements Comparable <RegionRrpgDim> {
	@DbColumn(columnName="RGN_ID",isId=true)
	private int        rgnId;
	@DbColumn(columnName="RGN_CD",keySeq=1)
	private String     rgnCd;
	@DbColumn(columnName="RGN_NM")
	private String     rgnNm;
	@DbColumn(columnName="RGN_DESC")
	private String     rgnDesc;
	@DbColumn(columnName="EDS_RGN_CD")
	private String     edsRgnCd;
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
	public RegionRrpgDim () {
		
	}
	
	// Define natural key constructor
	public RegionRrpgDim (
      String     rgnCd
	) {
		this.rgnCd                          = rgnCd;
		
	}
	
	// Define base constructor
	public RegionRrpgDim (
      String     rgnCd
    , String     rgnNm
    , String     rgnDesc
    , String     edsRgnCd
    , String     mrktCd
	) {
		this.rgnCd                          = rgnCd;
		this.rgnNm                          = rgnNm;
		this.rgnDesc                        = rgnDesc;
		this.edsRgnCd                       = edsRgnCd;
		this.mrktCd                         = mrktCd;
		
	}
    
	// Define full constructor
	public RegionRrpgDim (
		  int        rgnId
		, String     rgnCd
		, String     rgnNm
		, String     rgnDesc
		, String     edsRgnCd
		, String     mrktCd
	) {
		this.rgnId                          = rgnId;
		this.rgnCd                          = rgnCd;
		this.rgnNm                          = rgnNm;
		this.rgnDesc                        = rgnDesc;
		this.edsRgnCd                       = edsRgnCd;
		this.mrktCd                         = mrktCd;
		
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
                    
		RegionRrpgDim other = (RegionRrpgDim) obj;
		if (
            this.rgnCd.equals(other.getRgnCd())
         && this.rgnNm.equals(other.getRgnNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.rgnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.rgnNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.rgnDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.edsRgnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.mrktCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("RGN_CD")
        + "," + Helpers.formatCsvField("RGN_NM")
        + "," + Helpers.formatCsvField("RGN_DESC")
        + "," + Helpers.formatCsvField("EDS_RGN_CD")
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
	public String getRgnDesc() {
		return rgnDesc;
	}
	public void setRgnDesc(String rgnDesc) {
		this.rgnDesc = rgnDesc;
	}
	public String getEdsRgnCd() {
		return edsRgnCd;
	}
	public void setEdsRgnCd(String edsRgnCd) {
		this.edsRgnCd = edsRgnCd;
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

	public int compareTo(RegionRrpgDim o) {
		return this.getRgnCd().compareTo(o.getRgnCd());
	}
}