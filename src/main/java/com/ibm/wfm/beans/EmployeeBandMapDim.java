package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="EmployeeBandMapDim",baseTableName="REFT.EMPLOYEE_BAND_MAP")
public class EmployeeBandMapDim extends NaryTreeNode implements SelfReferenceTaxonomyNodeInterface {
	@DbColumn(columnName="EMP_BAND_MAP_ID",isId=true)
	private int        empBandMapId;
	@DbColumn(columnName="EMP_BAND_MAP_CODE",keySeq=1)
	private String     empBandMapCode;
	@DbColumn(columnName="EMP_BAND_NM")
	private String     empBandNm;
	@DbColumn(columnName="EMP_BAND_CODE")
	private String     empBandCode;
	@DbColumn(columnName="SOURCE_CODE")
	private String     sourceCode;
	@DbColumn(columnName="HEIGHT")
	private int        height;
	@DbColumn(columnName="DEPTH")
	private int        depth;
	@DbColumn(columnName="RELATIONSHIP_TYPE_CODE")
	private String     relationshipTypeCode;
	@DbColumn(columnName="PARENT_EMP_BAND_MAP_CODE",foreignKeySeq=1)
	private String     parentEmpBandMapCode;
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
	public EmployeeBandMapDim () {
		
	}
	
	// Define natural key constructor
	public EmployeeBandMapDim (
      String     empBandMapCode
	) {
		this.empBandMapCode                 = empBandMapCode;
		
	}
	
	// Define base constructor
	public EmployeeBandMapDim (
      String     empBandMapCode
    , String     empBandNm
    , String     empBandCode
    , String     sourceCode
    , int        height
    , int        depth
    , String     relationshipTypeCode
    , String     parentEmpBandMapCode
	) {
		this.empBandMapCode                 = empBandMapCode;
		this.empBandCode                    = empBandCode;
		this.empBandNm                      = empBandNm;
		this.height                         = height;
		this.depth                          = depth;
		this.sourceCode                     = sourceCode;
		this.relationshipTypeCode           = relationshipTypeCode;
		this.parentEmpBandMapCode           = parentEmpBandMapCode;
		
	}
    
	// Define full constructor
	public EmployeeBandMapDim (
		  int        empBandMapId
		, String     empBandMapCode
		, String     empBandNm
		, String     empBandCode
		, String     sourceCode
		, int        height
		, int        depth
		, String     relationshipTypeCode
		, String     parentEmpBandMapCode
	) {
		this.empBandMapId                   = empBandMapId;
		this.empBandMapCode                 = empBandMapCode;
		this.empBandCode                    = empBandCode;
		this.empBandNm                      = empBandNm;
		this.height                         = height;
		this.depth                          = depth;
		this.sourceCode                     = sourceCode;
		this.relationshipTypeCode           = relationshipTypeCode;
		this.parentEmpBandMapCode           = parentEmpBandMapCode;
		
	}
	
	@Override
	public String getCode() { 
		return this.empBandMapCode
		;
	}
	public String getDescription() { 
		return this.empBandNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		EmployeeBandMapDim other = (EmployeeBandMapDim) obj;
		if (
            this.empBandMapCode.equals(other.getEmpBandMapCode())
         && this.empBandCode.equals(other.getEmpBandCode())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.empBandMapCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.empBandNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.empBandCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.sourceCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.height))
        + "," + Helpers.formatCsvField(String.valueOf(this.depth))
        + "," + Helpers.formatCsvField(String.valueOf(this.relationshipTypeCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.parentEmpBandMapCode))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("EMP_BAND_MAP_CODE")
        + "," + Helpers.formatCsvField("EMP_BAND_NM")
        + "," + Helpers.formatCsvField("EMP_BAND_CODE")
        + "," + Helpers.formatCsvField("SOURCE_CODE")
        + "," + Helpers.formatCsvField("HEIGHT")
        + "," + Helpers.formatCsvField("DEPTH")
        + "," + Helpers.formatCsvField("RELATIONSHIP_TYPE_CODE")
        + "," + Helpers.formatCsvField("PARENT_EMP_BAND_MAP_CODE")
		;
	}
    
	// Define Getters and Setters
	public int getEmpBandMapId() {
		return empBandMapId;
	}
	public void setEmpBandMapId(int empBandMapId) {
		this.empBandMapId = empBandMapId;
	}
	public String getEmpBandMapCode() {
		return empBandMapCode;
	}
	public void setEmpBandMapCode(String empBandMapCode) {
		this.empBandMapCode = empBandMapCode;
	}
	public String getEmpBandCode() {
		return empBandCode;
	}
	public void setEmpBandCode(String empBandCode) {
		this.empBandCode = empBandCode;
	}
	public String getEmpBandNm() {
		return empBandNm;
	}
	public void setEmpBandNm(String empBandNm) {
		this.empBandNm = empBandNm;
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
	public String getParentEmpBandMapCode() {
		return parentEmpBandMapCode;
	}
	public void setParentEmpBandMapCode(String parentEmpBandMapCode) {
		this.parentEmpBandMapCode = parentEmpBandMapCode;
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
		return this.parentEmpBandMapCode;
	}

	public void setParentCd(String parentCd) {
		this.parentEmpBandMapCode = parentCd;
	}

	public int getLevelNum() {
		return this.level;
	}

	public void setLevelNum(int levelNum) {
		this.level = levelNum;
	}

	@Override
	public String getName() {
		return this.empBandNm;
	}

	@Override
	public void setName(String name) {
		this.empBandNm = name;
	}

	public String getRelationshipTypeCode() {
		return relationshipTypeCode;
	}

	public void setRelationshipTypeCode(String relationshipTypeCode) {
		this.relationshipTypeCode = relationshipTypeCode;
	}
}