package com.ibm.wfm.beans;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="AccountTypeDim",baseTableName="REFT.ACCOUNT_TYPE")
public class AccountTypeDim extends NaryTreeNode {
	@DbColumn(columnName="ACCOUNT_TYP_ID",isId=true)
	private int        accountTypId;
	@DbColumn(columnName="ACCOUNT_TYP_CODE",keySeq=1)
	private String     accountTypCode;
	@DbColumn(columnName="ACCOUNT_TYP_NM")
	private String     accountTypNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public AccountTypeDim () {
		
	}
	
	// Define natural key constructor
	public AccountTypeDim (
      String     accountTypCode
	) {
		this.accountTypCode                 = accountTypCode;
		
	}
	
	// Define base constructor
	public AccountTypeDim (
      String     accountTypCode
    , String     accountTypNm
	) {
		this.accountTypCode                 = accountTypCode;
		this.accountTypNm                   = accountTypNm;
		
	}
    
	// Define full constructor
	public AccountTypeDim (
		  int        accountTypId
		, String     accountTypCode
		, String     accountTypNm
	) {
		this.accountTypId                   = accountTypId;
		this.accountTypCode                 = accountTypCode;
		this.accountTypNm                   = accountTypNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.accountTypCode
		;
	}
	public String getDescription() { 
		return this.accountTypNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		AccountTypeDim other = (AccountTypeDim) obj;
		if (
            this.accountTypCode.equals(other.getAccountTypCode())
         && this.accountTypNm.equals(other.getAccountTypNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.accountTypCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.accountTypNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("ACCOUNT_TYP_CODE")
        + "," + Helpers.formatCsvField("ACCOUNT_TYP_NM")
		;
	}
    
	// Define Getters and Setters
	public int getAccountTypId() {
		return accountTypId;
	}
	public void setAccountTypId(int accountTypId) {
		this.accountTypId = accountTypId;
	}
	public String getAccountTypCode() {
		return accountTypCode;
	}
	public void setAccountTypCode(String accountTypCode) {
		this.accountTypCode = accountTypCode;
	}
	public String getAccountTypNm() {
		return accountTypNm;
	}
	public void setAccountTypNm(String accountTypNm) {
		this.accountTypNm = accountTypNm;
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