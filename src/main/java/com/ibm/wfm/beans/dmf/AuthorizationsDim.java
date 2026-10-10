package com.ibm.wfm.beans.dmf;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="AuthorizationsDim",baseTableName="DMF.AUTHORIZATIONS")
public class AuthorizationsDim {
	@DbColumn(columnName="AUTH_ID",isId=true)
	private int        authId;
	@DbColumn(columnName="USERID",keySeq=1)
	private String     userid;
	@DbColumn(columnName="AUTH_JSON")
	private String     authJson;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public AuthorizationsDim () {
		
	}
	
	// Define natural key constructor
	public AuthorizationsDim (
      String     userid
	) {
		this.userid                         = userid;
		
	}
    
	// Define full constructor
	public AuthorizationsDim (
		  int        authId
		, String     userid
		, String     authJson
	) {
		this.authId                         = authId;
		this.userid                         = userid;
		this.authJson                       = authJson;
		
	}
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.authId))
        + "," + Helpers.formatCsvField(String.valueOf(this.userid))
        + "," + Helpers.formatCsvField(String.valueOf(this.authJson))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("AUTH_ID")
        + "," + Helpers.formatCsvField("USERID")
        + "," + Helpers.formatCsvField("AUTH_JSON")
		;
	}
    
	// Define Getters and Setters
	public int getAuthId() {
		return authId;
	}
	public void setAuthId(int authId) {
		this.authId = authId;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getAuthJson() {
		return authJson;
	}
	public void setAuthJson(String authJson) {
		this.authJson = authJson;
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