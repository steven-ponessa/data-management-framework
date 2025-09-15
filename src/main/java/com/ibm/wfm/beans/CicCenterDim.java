package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CicCenterDim",baseTableName="REFT.CIC_CENTER")
public class CicCenterDim extends NaryTreeNode implements SelfReferenceTaxonomyNodeInterface {
	@DbColumn(columnName="CIC_CENTER_ID",isId=true)
	private int        cicCenterId;
	@DbColumn(columnName="CIC_CENTER_CD",keySeq=1)
	private String     cicCenterCd;
	@DbColumn(columnName="CIC_CENTER_NM")
	private String     cicCenterNm;
	@DbColumn(columnName="HEIGHT")
	private int        height;
	@DbColumn(columnName="DEPTH")
	private int        depth;
	@DbColumn(columnName="CIC_CENTER_SHORT_NM")
	private String     cicCenterShortNm;
	@DbColumn(columnName="CIC_CENTER_MEDIUM_NM")
	private String     cicCenterMediumNm;
	@DbColumn(columnName="CIC_CENTER_TYPE_CODE")
	private String     cicCenterTypeCode;
	@DbColumn(columnName="GIC_TYPE_CODE")
	private String     gicTypeCode;
	@DbColumn(columnName="PARENT_CIC_CENTER_CD")
	private String     parentCicCenterCd;
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
	public CicCenterDim () {
		
	}
	
	// Define natural key constructor
	public CicCenterDim (
      String     cicCenterCd
	) {
		this.cicCenterCd                    = cicCenterCd;
		
	}
	
	// Define base constructor
	public CicCenterDim (
      String     cicCenterCd
    , String     cicCenterNm
    , int        height
    , int        depth
    , String     cicCenterShortNm
    , String     cicCenterMediumNm
    , String     cicCenterTypeCode
    , String     gicTypeCode
    , String     parentCicCenterCd
	) {
		this.cicCenterCd                    = cicCenterCd;
		this.cicCenterNm                    = cicCenterNm;
		this.height                         = height;
		this.depth                          = depth;
		this.cicCenterShortNm               = cicCenterShortNm;
		this.cicCenterMediumNm              = cicCenterMediumNm;
		this.cicCenterTypeCode              = cicCenterTypeCode;
		this.gicTypeCode                    = gicTypeCode;
		this.parentCicCenterCd              = parentCicCenterCd;
		
	}
    
	// Define full constructor
	public CicCenterDim (
		  int        cicCenterId
		, String     cicCenterCd
		, String     cicCenterNm
		, int        height
		, int        depth
		, String     cicCenterShortNm
		, String     cicCenterMediumNm
		, String     cicCenterTypeCode
		, String     gicTypeCode
		, String     parentCicCenterCd
	) {
		this.cicCenterId                    = cicCenterId;
		this.cicCenterCd                    = cicCenterCd;
		this.cicCenterNm                    = cicCenterNm;
		this.height                         = height;
		this.depth                          = depth;
		this.cicCenterShortNm               = cicCenterShortNm;
		this.cicCenterMediumNm              = cicCenterMediumNm;
		this.cicCenterTypeCode              = cicCenterTypeCode;
		this.gicTypeCode                    = gicTypeCode;
		this.parentCicCenterCd              = parentCicCenterCd;
		
	}
	
	public int getId() {
		return this.cicCenterId;
	}
	
	@Override
	public String getCode() { 
		return this.cicCenterCd
		;
	}
	public String getDescription() { 
		return this.cicCenterNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		CicCenterDim other = (CicCenterDim) obj;
		if (
            this.cicCenterCd.equals(other.getCicCenterCd())
         && this.cicCenterNm.equals(other.getCicCenterNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.cicCenterCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCenterNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.height))
        + "," + Helpers.formatCsvField(String.valueOf(this.depth))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCenterShortNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCenterMediumNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCenterTypeCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.gicTypeCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.parentCicCenterCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CIC_CENTER_CD")
        + "," + Helpers.formatCsvField("CIC_CENTER_NM")
        + "," + Helpers.formatCsvField("HEIGHT")
        + "," + Helpers.formatCsvField("DEPTH")
        + "," + Helpers.formatCsvField("CIC_CENTER_SHORT_NM")
        + "," + Helpers.formatCsvField("CIC_CENTER_MEDIUM_NM")
        + "," + Helpers.formatCsvField("CIC_CENTER_TYPE_CODE")
        + "," + Helpers.formatCsvField("GIC_TYPE_CODE")
        + "," + Helpers.formatCsvField("PARENT_CIC_CENTER_CD")
		;
	}
    
	// Define Getters and Setters
	public int getCicCenterId() {
		return cicCenterId;
	}
	public void setCicCenterId(int cicCenterId) {
		this.cicCenterId = cicCenterId;
	}
	public String getCicCenterCd() {
		return cicCenterCd;
	}
	public void setCicCenterCd(String cicCenterCd) {
		this.cicCenterCd = cicCenterCd;
	}
	public String getCicCenterNm() {
		return cicCenterNm;
	}
	public void setCicCenterNm(String cicCenterNm) {
		this.cicCenterNm = cicCenterNm;
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
	public String getCicCenterShortNm() {
		return cicCenterShortNm;
	}
	public void setCicCenterShortNm(String cicCenterShortNm) {
		this.cicCenterShortNm = cicCenterShortNm;
	}
	public String getCicCenterMediumNm() {
		return cicCenterMediumNm;
	}
	public void setCicCenterMediumNm(String cicCenterMediumNm) {
		this.cicCenterMediumNm = cicCenterMediumNm;
	}
	public String getCicCenterTypeCode() {
		return cicCenterTypeCode;
	}
	public void setCicCenterTypeCode(String cicCenterTypeCode) {
		this.cicCenterTypeCode = cicCenterTypeCode;
	}
	public String getGicTypeCode() {
		return gicTypeCode;
	}
	public void setGicTypeCode(String gicTypeCode) {
		this.gicTypeCode = gicTypeCode;
	}
	public String getParentCicCenterCd() {
		return parentCicCenterCd;
	}
	public void setParentCicCenterCd(String parentCicCenterCd) {
		this.parentCicCenterCd = parentCicCenterCd;
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
		return this.parentCicCenterCd;
	}

	public void setParentCd(String parentCd) {
		 this.parentCicCenterCd = parentCd;		
	}

	public void setCode(String code) {
		this.cicCenterCd = code;
	}

	public int getLevelNum() {
		return this.level;
	}

	public void setLevelNum(int levelNum) {
		this.level = levelNum;
	}

	public void setDescription(String description) {
		this.cicCenterNm = description;
		
	}

	public String getName() {
		return this.cicCenterShortNm;
	}


	public void setName(String name) {
		this.cicCenterShortNm = name;		
	}
}