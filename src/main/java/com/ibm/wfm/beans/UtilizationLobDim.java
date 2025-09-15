package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="UtilizationLobDim",baseTableName="REFT.UTILIZATION_LOB")
public class UtilizationLobDim extends NaryTreeNode {
	@DbColumn(columnName="UTIL_LOB_ID",isId=true)
	private int        utilLobId;
	@DbColumn(columnName="UTIL_LOB_CODE",keySeq=1)
	private String     utilLobCode;
	@DbColumn(columnName="UTIL_LOB_NM")
	private String     utilLobNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public UtilizationLobDim () {
		
	}
	
	// Define natural key constructor
	public UtilizationLobDim (
      String     utilLobCode
	) {
		this.utilLobCode                    = utilLobCode;
		
	}
	
	// Define base constructor
	public UtilizationLobDim (
      String     utilLobCode
    , String     utilLobNm
	) {
		this.utilLobCode                    = utilLobCode;
		this.utilLobNm                      = utilLobNm;
		
	}
    
	// Define full constructor
	public UtilizationLobDim (
		  int        utilLobId
		, String     utilLobCode
		, String     utilLobNm
	) {
		this.utilLobId                      = utilLobId;
		this.utilLobCode                    = utilLobCode;
		this.utilLobNm                      = utilLobNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.utilLobCode
		;
	}
	public String getDescription() { 
		return this.utilLobNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		UtilizationLobDim other = (UtilizationLobDim) obj;
		if (
            this.utilLobCode.equals(other.getUtilLobCode())
         && this.utilLobNm.equals(other.getUtilLobNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.utilLobCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.utilLobNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("UTIL_LOB_CODE")
        + "," + Helpers.formatCsvField("UTIL_LOB_NM")
		;
	}
    
	// Define Getters and Setters
	public int getUtilLobId() {
		return utilLobId;
	}
	public void setUtilLobId(int utilLobId) {
		this.utilLobId = utilLobId;
	}
	public String getUtilLobCode() {
		return utilLobCode;
	}
	public void setUtilLobCode(String utilLobCode) {
		this.utilLobCode = utilLobCode;
	}
	public String getUtilLobNm() {
		return utilLobNm;
	}
	public void setUtilLobNm(String utilLobNm) {
		this.utilLobNm = utilLobNm;
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