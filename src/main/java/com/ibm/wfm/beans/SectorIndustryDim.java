package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SectorIndustryDim",baseTableName="REFT.SECTOR_INDUSTRY")
public class SectorIndustryDim extends NaryTreeNode implements SelfReferenceTaxonomyNodeInterface {
	@DbColumn(columnName="SECTOR_INDUSTRY_ID",isId=true)
	private int        sectorIndustryId;
	@DbColumn(columnName="SECTOR_INDUSTRY_CODE",keySeq=1)
	private String     sectorIndustryCode;
	@DbColumn(columnName="SECTOR_INDUSTRY_NM")
	private String     sectorIndustryNm;
	@DbColumn(columnName="HEIGHT")
	private int        height;
	@DbColumn(columnName="DEPTH")
	private int        depth;
	@DbColumn(columnName="PARENT_SECTOR_INDUSTRY_CODE")
	private String     parentSectorIndustryCode;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	//@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	@JsonIgnore
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	//@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	@JsonIgnore
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true) 
	@JsonIgnore
	private String rowStatusCd;

	// Define null constructor
	public SectorIndustryDim () {
		
	}
	
	// Define natural key constructor
	public SectorIndustryDim (
      String     sectorIndustryCode
	) {
		this.sectorIndustryCode             = sectorIndustryCode;
		
	}
	
	// Define base constructor
	public SectorIndustryDim (
      String     sectorIndustryCode
    , String     sectorIndustryNm
    , int        height
    , int        depth
    , String     parentSectorIndustryCode
	) {
		this.sectorIndustryCode             = sectorIndustryCode;
		this.sectorIndustryNm               = sectorIndustryNm;
		this.height                         = height;
		this.depth                          = depth;
		this.parentSectorIndustryCode       = parentSectorIndustryCode;
		
	}
    
	// Define full constructor
	public SectorIndustryDim (
		  int        sectorIndustryId
		, String     sectorIndustryCode
		, String     sectorIndustryNm
		, int        height
		, int        depth
		, String     parentSectorIndustryCode
	) {
		this.sectorIndustryId               = sectorIndustryId;
		this.sectorIndustryCode             = sectorIndustryCode;
		this.sectorIndustryNm               = sectorIndustryNm;
		this.height                         = height;
		this.depth                          = depth;
		this.parentSectorIndustryCode       = parentSectorIndustryCode;
		
	}
	
	@Override
	public String getCode() { 
		return this.sectorIndustryCode
		;
	}
	public String getDescription() { 
		return this.sectorIndustryNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SectorIndustryDim other = (SectorIndustryDim) obj;
		if (
            this.sectorIndustryCode.equals(other.getSectorIndustryCode())
         && this.sectorIndustryNm.equals(other.getSectorIndustryNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.sectorIndustryCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.sectorIndustryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.height))
        + "," + Helpers.formatCsvField(String.valueOf(this.depth))
        + "," + Helpers.formatCsvField(String.valueOf(this.parentSectorIndustryCode))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SECTOR_INDUSTRY_CODE")
        + "," + Helpers.formatCsvField("SECTOR_INDUSTRY_NM")
        + "," + Helpers.formatCsvField("HEIGHT")
        + "," + Helpers.formatCsvField("DEPTH")
        + "," + Helpers.formatCsvField("PARENT_SECTOR_INDUSTRY_CODE")
		;
	}
    
	// Define Getters and Setters
	public int getSectorIndustryId() {
		return sectorIndustryId;
	}
	public void setSectorIndustryId(int sectorIndustryId) {
		this.sectorIndustryId = sectorIndustryId;
	}
	public String getSectorIndustryCode() {
		return sectorIndustryCode;
	}
	public void setSectorIndustryCode(String sectorIndustryCode) {
		this.sectorIndustryCode = sectorIndustryCode;
	}
	public String getSectorIndustryNm() {
		return sectorIndustryNm;
	}
	public void setSectorIndustryNm(String sectorIndustryNm) {
		this.sectorIndustryNm = sectorIndustryNm;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getParentSectorIndustryCode() {
		return parentSectorIndustryCode;
	}
	public void setParentSectorIndustryCode(String parentSectorIndustryCode) {
		this.parentSectorIndustryCode = parentSectorIndustryCode;
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

	public String getParentCd() {
		return this.parentSectorIndustryCode;
	}

	public void setParentCd(String parentCd) {
		this.parentSectorIndustryCode = parentCd;
	}

	public int getLevelNum() {
		return this.level;
	}

	public void setLevelNum(int levelNum) {
		this.level = levelNum;
	}
	
	public void setDescription(String description) {
		this.sectorIndustryNm = description;
		
	}

	@Override
	public String getName() {
		return this.sectorIndustryNm;
	}

	@Override
	public void setName(String name) {
		this.sectorIndustryNm = name;	
	}
}