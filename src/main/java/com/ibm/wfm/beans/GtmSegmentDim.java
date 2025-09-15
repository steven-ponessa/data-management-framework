package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GtmSegmentDim",baseTableName="REFT.GTM_SEGMENT",parentBeanName="TotalIndustryDim",parentBaseTableName="REFT.TOTAL_INDUSTRY")
public class GtmSegmentDim extends NaryTreeNode {
	@DbColumn(columnName="GTM_SGMT_ID",isId=true)
	private int        gtmSgmtId;
	@DbColumn(columnName="GTM_SGMT_CD",keySeq=1)
	private String     gtmSgmtCd;
	@DbColumn(columnName="GTM_SGMT_DESC")
	private String     gtmSgmtDesc;
	@DbColumn(columnName="TOT_IND_CD",foreignKeySeq=1)
	private String     totIndCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public GtmSegmentDim () {
		
	}
	
	// Define natural key constructor
	public GtmSegmentDim (
      String     gtmSgmtCd
	) {
		this.gtmSgmtCd                      = gtmSgmtCd;
		
	}
	
	// Define base constructor
	public GtmSegmentDim (
      String     gtmSgmtCd
    , String     gtmSgmtDesc
    , String     totIndCd
	) {
		this.gtmSgmtCd                      = gtmSgmtCd;
		this.gtmSgmtDesc                    = gtmSgmtDesc;
		this.totIndCd                       = totIndCd;
		
	}
    
	// Define full constructor
	public GtmSegmentDim (
		  int        gtmSgmtId
		, String     gtmSgmtCd
		, String     gtmSgmtDesc
		, String     totIndCd
	) {
		this.gtmSgmtId                      = gtmSgmtId;
		this.gtmSgmtCd                      = gtmSgmtCd;
		this.gtmSgmtDesc                    = gtmSgmtDesc;
		this.totIndCd                       = totIndCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.gtmSgmtCd
		;
	}
	public String getDescription() { 
		return this.gtmSgmtDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		GtmSegmentDim other = (GtmSegmentDim) obj;
		if (
            this.gtmSgmtCd.equals(other.getGtmSgmtCd())
         && this.gtmSgmtDesc.equals(other.getGtmSgmtDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.gtmSgmtCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.gtmSgmtDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.totIndCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GTM_SGMT_CD")
        + "," + Helpers.formatCsvField("GTM_SGMT_DESC")
        + "," + Helpers.formatCsvField("TOT_IND_CD")
		;
	}
    
	// Define Getters and Setters
	public int getGtmSgmtId() {
		return gtmSgmtId;
	}
	public void setGtmSgmtId(int gtmSgmtId) {
		this.gtmSgmtId = gtmSgmtId;
	}
	public String getGtmSgmtCd() {
		return gtmSgmtCd;
	}
	public void setGtmSgmtCd(String gtmSgmtCd) {
		this.gtmSgmtCd = gtmSgmtCd;
	}
	public String getGtmSgmtDesc() {
		return gtmSgmtDesc;
	}
	public void setGtmSgmtDesc(String gtmSgmtDesc) {
		this.gtmSgmtDesc = gtmSgmtDesc;
	}
	public String getTotIndCd() {
		return totIndCd;
	}
	public void setTotIndCd(String totIndCd) {
		this.totIndCd = totIndCd;
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