package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="CicCenterGroupDim",baseTableName="REFT.CIC_CENTER_GROUP")
public class CicCenterGroupDim extends NaryTreeNode implements SelfReferenceTaxonomyNodeInterface {
	@DbColumn(columnName="CIC_CENTER_GROUP_ID",isId=true)
	private int        cicCenterGroupId;
	@DbColumn(columnName="CIC_CENTER_GROUP_CODE",keySeq=1)
	private String     cicCenterGroupCode;
	@DbColumn(columnName="CIC_CENTER_GROUP_NM")
	private String     cicCenterGroupNm;
	@DbColumn(columnName="CIC_CENTER_GROUP_DESC")
	private String     cicCenterGroupDesc;
	@DbColumn(columnName="HEIGHT")
	private int        height;
	@DbColumn(columnName="DEPTH")
	private int        depth;
	@DbColumn(columnName="SOURCE_CODE")
	private String     sourceCode;
	@DbColumn(columnName="REL_TYPE_CD")
	private String     relTypeCd;
	@DbColumn(columnName="PARENT_CIC_CENTER_GROUP_CODE")
	private String     parentCicCenterGroupCode;
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
	public CicCenterGroupDim () {
		
	}
	
	// Define natural key constructor
	public CicCenterGroupDim (
      String     cicCenterGroupCode
	) {
		this.cicCenterGroupCode             = cicCenterGroupCode;
		
	}
	
	// Define base constructor
	public CicCenterGroupDim (
      String     cicCenterGroupCode
    , String     cicCenterGroupNm
    , String     cicCenterGroupDesc
    , int        height
    , int        depth
    , String     sourceCode
    , String     relTypeCd
    , String     parentCicCenterGroupCode
	) {
		this.cicCenterGroupCode             = cicCenterGroupCode;
		this.cicCenterGroupNm               = cicCenterGroupNm;
		this.cicCenterGroupDesc             = cicCenterGroupDesc;
		this.height                         = height;
		this.depth                          = depth;
		this.sourceCode                     = sourceCode;
		this.relTypeCd                      = relTypeCd;
		this.parentCicCenterGroupCode       = parentCicCenterGroupCode;
		
	}	
	
	// Define full constructor
	public CicCenterGroupDim (
		  int        cicCenterGroupId
		, String     cicCenterGroupCode
		, String     cicCenterGroupNm
		, String     cicCenterGroupDesc
		, int        height
		, int        depth
		, String     sourceCode
		, String     relTypeCd
		, String     parentCicCenterGroupCode
	) {
		this.cicCenterGroupId               = cicCenterGroupId;
		this.cicCenterGroupCode             = cicCenterGroupCode;
		this.cicCenterGroupNm               = cicCenterGroupNm;
		this.cicCenterGroupDesc             = cicCenterGroupDesc;
		this.height                         = height;
		this.depth                          = depth;
		this.sourceCode                     = sourceCode;
		this.relTypeCd                      = relTypeCd;
		this.parentCicCenterGroupCode       = parentCicCenterGroupCode;
		
	}
	
	@Override
	public String getCode() { 
		return this.cicCenterGroupCode
		;
	}
	public String getDescription() { 
		return this.getCicCenterGroupNm(); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		CicCenterGroupDim other = (CicCenterGroupDim) obj;
		if (
            this.cicCenterGroupCode.equals(other.getCicCenterGroupCode())
         && this.cicCenterGroupNm.equals(other.getCicCenterGroupNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.cicCenterGroupCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCenterGroupNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.cicCenterGroupDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.height))
        + "," + Helpers.formatCsvField(String.valueOf(this.depth))
        + "," + Helpers.formatCsvField(String.valueOf(this.sourceCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.relTypeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.parentCicCenterGroupCode))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("CIC_CENTER_GROUP_CODE")
        + "," + Helpers.formatCsvField("CIC_CENTER_GROUP_NM")
        + "," + Helpers.formatCsvField("CIC_CENTER_GROUP_DESC")
        + "," + Helpers.formatCsvField("HEIGHT")
        + "," + Helpers.formatCsvField("DEPTH")
        + "," + Helpers.formatCsvField("SOURCE_CODE")
        + "," + Helpers.formatCsvField("REL_TYPE_CD")
        + "," + Helpers.formatCsvField("PARENT_CIC_CENTER_GROUP_CODE")
		;
	}
    
	// Define Getters and Setters
	public int getCicCenterGroupId() {
		return cicCenterGroupId;
	}
	public void setCicCenterGroupId(int cicCenterGroupId) {
		this.cicCenterGroupId = cicCenterGroupId;
	}
	public String getCicCenterGroupCode() {
		return cicCenterGroupCode;
	}
	public void setCicCenterGroupCode(String cicCenterGroupCode) {
		this.cicCenterGroupCode = cicCenterGroupCode;
	}
	public String getCicCenterGroupNm() {
		return cicCenterGroupNm;
	}
	public void setCicCenterGroupNm(String cicCenterGroupNm) {
		this.cicCenterGroupNm = cicCenterGroupNm;
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
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getParentCicCenterGroupCode() {
		return parentCicCenterGroupCode;
	}
	public void setParentCicCenterGroupCode(String parentCicCenterGroupCode) {
		this.parentCicCenterGroupCode = parentCicCenterGroupCode;
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
		return this.parentCicCenterGroupCode;
	}

	public void setParentCd(String parentCd) {
		this.parentCicCenterGroupCode = parentCd;
	}

	public int getLevelNum() {
		return this.level;
	}

	public void setLevelNum(int levelNum) {
		this.level = levelNum;
	}
	@Override
	public String getName() {
		return this.cicCenterGroupNm;
	}

	@Override
	public void setName(String name) {
		this.cicCenterGroupNm = name;		
	}

	public String getCicCenterGroupDesc() {
		return cicCenterGroupDesc;
	}

	public void setCicCenterGroupDesc(String cicCenterGroupDesc) {
		this.cicCenterGroupDesc = cicCenterGroupDesc;
	}

	public String getRelTypeCd() {
		return relTypeCd;
	}

	public void setRelTypeCd(String relTypeCd) {
		this.relTypeCd = relTypeCd;
	}
}