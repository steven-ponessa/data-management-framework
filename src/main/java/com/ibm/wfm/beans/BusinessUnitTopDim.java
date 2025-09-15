package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="BusinessUnitTopDim",baseTableName="REFT.BUSINESS_UNIT_TOP")
public class BusinessUnitTopDim extends NaryTreeNode {
	@DbColumn(columnName="BUS_UNT_TOP_ID",isId=true)
	private int        busUntTopId;
	@DbColumn(columnName="BUS_UNIT_TOP_CD",keySeq=1)
	private String     busUnitTopCd;
	@DbColumn(columnName="BUS_UNIT_TOP_NM")
	private String     busUnitTopNm;
	@DbColumn(columnName="BUS_UNIT_TOP_DESC")
	private String     busUnitTopDesc;
	@DbColumn(columnName="LEVEL_NUM")
	private int        levelNum;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BusinessUnitTopDim () {
		
	}
	
	// Define natural key constructor
	public BusinessUnitTopDim (
      String     busUnitTopCd
	) {
		this.busUnitTopCd                   = busUnitTopCd;
		
	}
	
	// Define base constructor
	public BusinessUnitTopDim (
      String     busUnitTopCd
    , String     busUnitTopNm
    , String     busUnitTopDesc
    , int        levelNum
	) {
		this.busUnitTopCd                   = busUnitTopCd;
		this.busUnitTopNm                   = busUnitTopNm;
		this.busUnitTopDesc                 = busUnitTopDesc;
		this.levelNum                       = levelNum;
		
	}
    
	// Define full constructor
	public BusinessUnitTopDim (
		  int        busUntTopId
		, String     busUnitTopCd
		, String     busUnitTopNm
		, String     busUnitTopDesc
		, int        levelNum
	) {
		this.busUntTopId                    = busUntTopId;
		this.busUnitTopCd                   = busUnitTopCd;
		this.busUnitTopNm                   = busUnitTopNm;
		this.busUnitTopDesc                 = busUnitTopDesc;
		this.levelNum                       = levelNum;
		
	}
	
	@Override
	public String getCode() { 
		return this.busUnitTopCd
		;
	}
	public String getDescription() { 
		return this.busUnitTopNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BusinessUnitTopDim other = (BusinessUnitTopDim) obj;
		if (
            this.busUnitTopCd.equals(other.getBusUnitTopCd())
         && this.busUnitTopNm.equals(other.getBusUnitTopNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.busUnitTopCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.busUnitTopNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.busUnitTopDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelNum))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("BUS_UNIT_TOP_CD")
        + "," + Helpers.formatCsvField("BUS_UNIT_TOP_NM")
        + "," + Helpers.formatCsvField("BUS_UNIT_TOP_DESC")
        + "," + Helpers.formatCsvField("LEVEL_NUM")
		;
	}
    
	// Define Getters and Setters
	public int getBusUntTopId() {
		return busUntTopId;
	}
	public void setBusUntTopId(int busUntTopId) {
		this.busUntTopId = busUntTopId;
	}
	public String getBusUnitTopCd() {
		return busUnitTopCd;
	}
	public void setBusUnitTopCd(String busUnitTopCd) {
		this.busUnitTopCd = busUnitTopCd;
	}
	public String getBusUnitTopNm() {
		return busUnitTopNm;
	}
	public void setBusUnitTopNm(String busUnitTopNm) {
		this.busUnitTopNm = busUnitTopNm;
	}
	public String getBusUnitTopDesc() {
		return busUnitTopDesc;
	}
	public void setBusUnitTopDesc(String busUnitTopDesc) {
		this.busUnitTopDesc = busUnitTopDesc;
	}
	public int getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
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