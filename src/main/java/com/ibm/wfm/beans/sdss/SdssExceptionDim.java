package com.ibm.wfm.beans.sdss;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SdssExceptionDim",baseTableName="SDSS.SDSS_EXCEPTION")
public class SdssExceptionDim {
	@DbColumn(columnName="EXCEPTION_ID",isId=true)
	private int        exceptionId;
	@DbColumn(columnName="EXCEPTION_CD",keySeq=1)
	private String     exceptionCd;
	@DbColumn(columnName="EXCEPTION_NM")
	private String     exceptionNm;
	@DbColumn(columnName="ORDER_NUM")
	private int        orderNum;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SdssExceptionDim () {
		
	}
	
	// Define natural key constructor
	public SdssExceptionDim (
      String     exceptionCd
	) {
		this.exceptionCd                    = exceptionCd;
		
	}
	
	// Define base constructor
	public SdssExceptionDim (
      int        exceptionId
    , String     exceptionCd
    , String     exceptionNm
	) {
		this.exceptionId                    = exceptionId;
		this.exceptionCd                    = exceptionCd;
		this.exceptionNm                    = exceptionNm;
		
	}
    
	// Define full constructor
	public SdssExceptionDim (
		  int        exceptionId
		, String     exceptionCd
		, String     exceptionNm
		, int      orderNum
	) {
		this.exceptionId                    = exceptionId;
		this.exceptionCd                    = exceptionCd;
		this.exceptionNm                    = exceptionNm;
		this.orderNum                       = orderNum;
		
	}
	
	public String getCode() { 
		return this.exceptionCd
		;
	}
	public String getDescription() { 
		return this.exceptionNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SdssExceptionDim other = (SdssExceptionDim) obj;
		if (
            this.exceptionCd.equals(other.getExceptionCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.exceptionId))
        + "," + Helpers.formatCsvField(String.valueOf(this.exceptionCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.exceptionNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("EXCEPTION_ID")
        + "," + Helpers.formatCsvField("EXCEPTION_CD")
        + "," + Helpers.formatCsvField("EXCEPTION_NM")
		;
	}
    
	// Define Getters and Setters
	public int getExceptionId() {
		return exceptionId;
	}
	public void setExceptionId(int exceptionId) {
		this.exceptionId = exceptionId;
	}
	public String getExceptionCd() {
		return exceptionCd;
	}
	public void setExceptionCd(String exceptionCd) {
		this.exceptionCd = exceptionCd;
	}
	public String getExceptionNm() {
		return exceptionNm;
	}
	public void setExceptionNm(String exceptionNm) {
		this.exceptionNm = exceptionNm;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
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