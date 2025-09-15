package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="GeographyTypeDim",baseTableName="REFT.GEOGRAPHY_TYPE")
public class GeographyTypeDim extends NaryTreeNode {
	@DbColumn(columnName="GEOGRAPHY_TYP_ID",isId=true)
	private int        geographyTypId;
	@DbColumn(columnName="GEOGRAPHY_TYP_CODE",keySeq=1)
	private String     geographyTypCode;
	@DbColumn(columnName="GEOGRAPHY_TYP_NM")
	private String     geographyTypNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public GeographyTypeDim () {
		
	}
	
	// Define natural key constructor
	public GeographyTypeDim (
      String     geographyTypCode
	) {
		this.geographyTypCode               = geographyTypCode;
		
	}
	
	// Define base constructor
	public GeographyTypeDim (
      String     geographyTypCode
    , String     geographyTypNm
	) {
		this.geographyTypCode               = geographyTypCode;
		this.geographyTypNm                 = geographyTypNm;
		
	}
    
	// Define full constructor
	public GeographyTypeDim (
		  int        geographyTypId
		, String     geographyTypCode
		, String     geographyTypNm
	) {
		this.geographyTypId                 = geographyTypId;
		this.geographyTypCode               = geographyTypCode;
		this.geographyTypNm                 = geographyTypNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.geographyTypCode
		;
	}
	public String getDescription() { 
		return this.getGeographyTypNm(); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		GeographyTypeDim other = (GeographyTypeDim) obj;
		if (
            this.geographyTypCode.equals(other.getGeographyTypCode())
         && this.geographyTypNm.equals(other.getGeographyTypNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.geographyTypCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.geographyTypNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("GEOGRAPHY_TYP_CODE")
        + "," + Helpers.formatCsvField("GEOGRAPHY_TYP_NM")
		;
	}
    
	// Define Getters and Setters
	public int getGeographyTypId() {
		return geographyTypId;
	}
	public void setGeographyTypId(int geographyTypId) {
		this.geographyTypId = geographyTypId;
	}
	public String getGeographyTypCode() {
		return geographyTypCode;
	}
	public void setGeographyTypCode(String geographyTypCode) {
		this.geographyTypCode = geographyTypCode;
	}
	public String getGeographyTypNm() {
		return geographyTypNm;
	}
	public void setGeographyTypNm(String geographyTypNm) {
		this.geographyTypNm = geographyTypNm;
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