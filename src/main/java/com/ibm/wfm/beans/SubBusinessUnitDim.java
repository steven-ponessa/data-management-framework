package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SubBusinessUnitDim",baseTableName="REFT.SUB_BUSINESS_UNIT",parentBeanName="BusinessUnitDim",parentBaseTableName="REFT.BUSINESS_UNIT")
public class SubBusinessUnitDim extends NaryTreeNode {
	@DbColumn(columnName="SUB_BUS_UNIT_ID",isId=true)
	private int        subBusUnitId;
	@DbColumn(columnName="SUB_BUS_UNIT_CD",keySeq=1)
	private String     subBusUnitCd;
	@DbColumn(columnName="SUB_BUS_UNIT_NM")
	private String     subBusUnitNm;
	@DbColumn(columnName="SUB_BUS_UNIT_DESC")
	private String     subBusUnitDesc;
	@DbColumn(columnName="LEVEL_NUM")
	private int        levelNum;
	@DbColumn(columnName="BUS_UNIT_CD",foreignKeySeq=1)
	private String     busUnitCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SubBusinessUnitDim () {
		
	}
	
	// Define natural key constructor
	public SubBusinessUnitDim (
      String     subBusUnitCd
	) {
		this.subBusUnitCd                   = subBusUnitCd;
		
	}
	
	// Define base constructor
	public SubBusinessUnitDim (
      String     subBusUnitCd
    , String     subBusUnitNm
    , String     subBusUnitDesc
    , int        levelNum
    , String     busUnitCd
	) {
		this.subBusUnitCd                   = subBusUnitCd;
		this.subBusUnitNm                   = subBusUnitNm;
		this.subBusUnitDesc                 = subBusUnitDesc;
		this.levelNum                       = levelNum;
		this.busUnitCd                      = busUnitCd;
		
	}
    
	// Define full constructor
	public SubBusinessUnitDim (
		  int        subBusUnitId
		, String     subBusUnitCd
		, String     subBusUnitNm
		, String     subBusUnitDesc
		, int        levelNum
		, String     busUnitCd
	) {
		this.subBusUnitId                   = subBusUnitId;
		this.subBusUnitCd                   = subBusUnitCd;
		this.subBusUnitNm                   = subBusUnitNm;
		this.subBusUnitDesc                 = subBusUnitDesc;
		this.levelNum                       = levelNum;
		this.busUnitCd                      = busUnitCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.subBusUnitCd
		;
	}
	public String getDescription() { 
		return this.subBusUnitNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SubBusinessUnitDim other = (SubBusinessUnitDim) obj;
		if (
            this.subBusUnitCd.equals(other.getSubBusUnitCd())
         && this.subBusUnitNm.equals(other.getSubBusUnitNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.subBusUnitCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.subBusUnitNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.subBusUnitDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.levelNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.busUnitCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SUB_BUS_UNIT_CD")
        + "," + Helpers.formatCsvField("SUB_BUS_UNIT_NM")
        + "," + Helpers.formatCsvField("SUB_BUS_UNIT_DESC")
        + "," + Helpers.formatCsvField("LEVEL_NUM")
        + "," + Helpers.formatCsvField("BUS_UNIT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getSubBusUnitId() {
		return subBusUnitId;
	}
	public void setSubBusUnitId(int subBusUnitId) {
		this.subBusUnitId = subBusUnitId;
	}
	public String getSubBusUnitCd() {
		return subBusUnitCd;
	}
	public void setSubBusUnitCd(String subBusUnitCd) {
		this.subBusUnitCd = subBusUnitCd;
	}
	public String getSubBusUnitNm() {
		return subBusUnitNm;
	}
	public void setSubBusUnitNm(String subBusUnitNm) {
		this.subBusUnitNm = subBusUnitNm;
	}
	public String getSubBusUnitDesc() {
		return subBusUnitDesc;
	}
	public void setSubBusUnitDesc(String subBusUnitDesc) {
		this.subBusUnitDesc = subBusUnitDesc;
	}
	public int getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}
	public String getBusUnitCd() {
		return busUnitCd;
	}
	public void setBusUnitCd(String busUnitCd) {
		this.busUnitCd = busUnitCd;
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