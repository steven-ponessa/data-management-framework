package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="FbsConferenceDim",baseTableName="TEST.FBS_CONFERENCE")
public class FbsConferenceDim extends NaryTreeNode {
	@DbColumn(columnName="CONF_ID",isId=true)
	private int        confId;
	@DbColumn(columnName="CONF_CD",keySeq=1)
	private String     confCd;
	@DbColumn(columnName="CONF_NM")
	private String     confNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public FbsConferenceDim () {
				this.level = 0;
	}
	
	// Define natural key constructor
	public FbsConferenceDim (
      String     confCd
	) {
		this.confCd                         = confCd;
		this.level                          = 0;
	}
	
	// Define base constructor
	public FbsConferenceDim (
      String     confCd
    , String     confNm
	) {
		this.confCd                         = confCd;
		this.confNm                         = confNm;
		this.level                          = 0;
	}
    
	// Define full constructor
	public FbsConferenceDim (
		  int        confId
		, String     confCd
		, String     confNm
	) {
		this.confId                         = confId;
		this.confCd                         = confCd;
		this.confNm                         = confNm;
		this.level                          = 0;
	}
	
	@Override
	public String getCode() { 
		return this.confCd
		;
	}
	public String getDescription() { 
		return this.confNm ; //SP Manually added (for now)
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		FbsConferenceDim other = (FbsConferenceDim) obj;
		if (
            this.confCd.equals(other.getConfCd())
         && this.confNm.equals(other.getConfNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.confCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.confNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CONF_CD")
        + "," + Helpers.formatCsvField("CONF_NM")
		;
	}
    
	// Define Getters and Setters
	public int getConfId() {
		return confId;
	}
	public void setConfId(int confId) {
		this.confId = confId;
	}
	public String getConfCd() {
		return confCd;
	}
	public void setConfCd(String confCd) {
		this.confCd = confCd;
	}
	public String getConfNm() {
		return confNm;
	}
	public void setConfNm(String confNm) {
		this.confNm = confNm;
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