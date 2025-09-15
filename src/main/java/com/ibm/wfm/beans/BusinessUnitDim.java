package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="BusinessUnitDim",baseTableName="REFT.BUSINESS_UNIT",parentBeanName="BusinessUnitTopDim",parentBaseTableName="REFT.BUSINESS_UNIT_TOP")
public class BusinessUnitDim extends NaryTreeNode {
	@DbColumn(columnName="BUS_UNIT_ID",isId=true)
	private int        busUnitId;
	@DbColumn(columnName="BUS_UNIT_CD",keySeq=1)
	private String     busUnitCd;
	@DbColumn(columnName="BUS_UNIT_NM")
	private String     busUnitNm;
	@DbColumn(columnName="BUS_UNIT_DESC")
	private String     busUnitDesc;
	@DbColumn(columnName="LEVEL_NUM")
	private int        levelNum;
	@DbColumn(columnName="BUS_UNIT_TOP_CD",foreignKeySeq=1)
	private String     busUnitTopCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BusinessUnitDim () {
		
	}
	
	// Define natural key constructor
	public BusinessUnitDim (
      String     busUnitCd
	) {
		this.busUnitCd                      = busUnitCd;
		
	}
	
	// Define base constructor
	public BusinessUnitDim (
      String     busUnitCd
    , String     busUnitNm
    , String     busUnitDesc
    , int        levelNum
    , String     busUnitTopCd
	) {
		this.busUnitCd                      = busUnitCd;
		this.busUnitNm                      = busUnitNm;
		this.busUnitDesc                    = busUnitDesc;
		this.levelNum                       = levelNum;
		this.busUnitTopCd                   = busUnitTopCd;
		
	}
    
	// Define full constructor
	public BusinessUnitDim (
		  int        busUnitId
		, String     busUnitCd
		, String     busUnitNm
		, String     busUnitDesc
		, int        levelNum
		, String     busUnitTopCd
	) {
		this.busUnitId                      = busUnitId;
		this.busUnitCd                      = busUnitCd;
		this.busUnitNm                      = busUnitNm;
		this.busUnitDesc                    = busUnitDesc;
		this.levelNum                       = levelNum;
		this.busUnitTopCd                   = busUnitTopCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.busUnitCd
		;
	}
	public String getDescription() { 
		return this.busUnitNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BusinessUnitDim other = (BusinessUnitDim) obj;
		if (
            this.busUnitCd.equals(other.getBusUnitCd())
         && this.busUnitNm.equals(other.getBusUnitNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.busUnitCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.busUnitNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.busUnitDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.busUnitTopCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("BUS_UNIT_CD")
        + "," + Helpers.formatCsvField("BUS_UNIT_NM")
        + "," + Helpers.formatCsvField("BUS_UNIT_DESC")
        + "," + Helpers.formatCsvField("LEVEL_NUM")
        + "," + Helpers.formatCsvField("BUS_UNIT_TOP_CD")
		;
	}
    
	// Define Getters and Setters
	public int getBusUnitId() {
		return busUnitId;
	}
	public void setBusUnitId(int busUnitId) {
		this.busUnitId = busUnitId;
	}
	public String getBusUnitCd() {
		return busUnitCd;
	}
	public void setBusUnitCd(String busUnitCd) {
		this.busUnitCd = busUnitCd;
	}
	public String getBusUnitNm() {
		return busUnitNm;
	}
	public void setBusUnitNm(String busUnitNm) {
		this.busUnitNm = busUnitNm;
	}
	public String getBusUnitDesc() {
		return busUnitDesc;
	}
	public void setBusUnitDesc(String busUnitDesc) {
		this.busUnitDesc = busUnitDesc;
	}
	public int getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}
	public String getBusUnitTopCd() {
		return busUnitTopCd;
	}
	public void setBusUnitTopCd(String busUnitTopCd) {
		this.busUnitTopCd = busUnitTopCd;
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