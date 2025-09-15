package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="FbsDivisionDim",baseTableName="TEST.FBS_DIVISION",parentBeanName="FbsConferenceDim",parentBaseTableName="TEST.FBS_CONFERENCE")
public class FbsDivisionDim extends NaryTreeNode {
	@DbColumn(columnName="DIV_ID",isId=true)
	private int        divId;
	@DbColumn(columnName="DIV_CD",keySeq=1)
	private String     divCd;
	@DbColumn(columnName="DIV_NM")
	private String     divNm;
	@DbColumn(columnName="CONF_CD",foreignKeySeq=1)
	private String     confCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public FbsDivisionDim () {
				this.level = 1;
	}
	
	// Define natural key constructor
	public FbsDivisionDim (
      String     divCd
	) {
		this.divCd                          = divCd;
		this.level                          = 1;
	}
	
	// Define base constructor
	public FbsDivisionDim (
      String     divCd
    , String     divNm
    , String     confCd
	) {
		this.divCd                          = divCd;
		this.divNm                          = divNm;
		this.confCd                         = confCd;
		this.level                          = 1;
	}
    
	// Define full constructor
	public FbsDivisionDim (
		  int        divId
		, String     divCd
		, String     divNm
		, String     confCd
	) {
		this.divId                          = divId;
		this.divCd                          = divCd;
		this.divNm                          = divNm;
		this.confCd                         = confCd;
		this.level                          = 1;
	}
	
	@Override
	public String getCode() { 
		return this.divCd
		;
	}
	public String getDescription() { 
		return this.divNm;  //manually entered
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		FbsDivisionDim other = (FbsDivisionDim) obj;
		if (
            this.divCd.equals(other.getDivCd())
         && this.divNm.equals(other.getDivNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.divCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.divNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.confCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("DIV_CD")
        + "," + Helpers.formatCsvField("DIV_NM")
        + "," + Helpers.formatCsvField("CONF_CD")
		;
	}
    
	// Define Getters and Setters
	public int getDivId() {
		return divId;
	}
	public void setDivId(int divId) {
		this.divId = divId;
	}
	public String getDivCd() {
		return divCd;
	}
	public void setDivCd(String divCd) {
		this.divCd = divCd;
	}
	public String getDivNm() {
		return divNm;
	}
	public void setDivNm(String divNm) {
		this.divNm = divNm;
	}
	public String getConfCd() {
		return confCd;
	}
	public void setConfCd(String confCd) {
		this.confCd = confCd;
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