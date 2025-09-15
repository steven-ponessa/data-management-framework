package com.ibm.wfm.beans;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

public class LaborModelDim extends NaryTreeNode implements SelfReferenceTaxonomyNodeInterface {
	
	@DbColumn(columnName="LBR_MODEL_ID",isId=true)
	private int        lbrModelId;
	@DbColumn(columnName="LBR_MODEL_CD",keySeq=1)
	private String     lbrModelCd;
	@DbColumn(columnName="LBR_MODEL_NM")
	private String     lbrModelNm;
	@DbColumn(columnName="LBR_MODEL_SHORT_NM")
	private String     lbrModelShortNm;
	@DbColumn(columnName="LBR_MODEL_PARENT_CD")
	private String     lbrModelParentCd;
	
	private int levelNum;
	
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
	public LaborModelDim () {}
	
	// Define natural key constructor
	public LaborModelDim (
      String     lbrModelCd
	) {
		this.lbrModelCd                     = lbrModelCd;
	}
	
	// Define base constructor
	public LaborModelDim (
      String     lbrModelCd
    , String     lbrModelNm
    , String     lbrModelShortNm
    , String     lbrModelParentCd
	) {
		this.lbrModelCd                     = lbrModelCd;
		this.lbrModelNm                     = lbrModelNm;
		this.lbrModelShortNm                = lbrModelShortNm;
		this.lbrModelParentCd               = lbrModelParentCd;
	}
    
	// Define full constructor
	public LaborModelDim (
		  int        lbrModelId
		, String     lbrModelCd
		, String     lbrModelNm
		, String     lbrModelShortNm
		, String     lbrModelParentCd
	) {
		this.lbrModelId                     = lbrModelId;
		this.lbrModelCd                     = lbrModelCd;
		this.lbrModelNm                     = lbrModelNm;
		this.lbrModelShortNm                = lbrModelShortNm;
		this.lbrModelParentCd               = lbrModelParentCd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		LaborModelDim other = (LaborModelDim) obj;
		if (
            this.lbrModelCd.equals(other.getLbrModelCd())
         && this.lbrModelNm.equals(other.getLbrModelNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.lbrModelCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrModelNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrModelShortNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrModelParentCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("LBR_MODEL_CD")
        + "," + Helpers.formatCsvField("LBR_MODEL_NM")
        + "," + Helpers.formatCsvField("LBR_MODEL_SHORT_NM")
        + "," + Helpers.formatCsvField("LBR_MODEL_PARENT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getLbrModelId() {
		return lbrModelId;
	}
	public void setLbrModelId(int lbrModelId) {
		this.lbrModelId = lbrModelId;
	}
	public String getLbrModelCd() {
		return lbrModelCd;
	}
	public void setLbrModelCd(String lbrModelCd) {
		this.lbrModelCd = lbrModelCd;
	}
	public String getLbrModelNm() {
		return lbrModelNm;
	}
	public void setLbrModelNm(String lbrModelNm) {
		this.lbrModelNm = lbrModelNm;
	}
	public String getLbrModelShortNm() {
		return lbrModelShortNm;
	}
	public void setLbrModelShortNm(String lbrModelShortNm) {
		this.lbrModelShortNm = lbrModelShortNm;
	}
	public String getLbrModelParentCd() {
		return lbrModelParentCd;
	}
	public void setLbrModelParentCd(String lbrModelParentCd) {
		this.lbrModelParentCd = lbrModelParentCd;
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
		return lbrModelParentCd;
	}

	public void setParentCd(String parentCd) {
		this.lbrModelParentCd = parentCd;
		
	}

	public String getCode() {
		return this.lbrModelCd;
	}

	public void setCode(String code) {
		this.lbrModelCd = code;
	}

	
	public int getLevelNum() {
		return this.level;
	}

	public void setLevelNum(int levelNum) {
		this.level = levelNum;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return this.lbrModelNm;
	}

	public void setDescription(String description) {
		this.lbrModelNm = description;
	}

	@Override
	public String getName() {
		return this.lbrModelShortNm;
	}

	@Override
	public void setName(String name) {
		this.lbrModelShortNm = name;
		
	}
}