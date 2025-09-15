package com.ibm.wfm.beans.ihub;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SkillDim",baseTableName="REFT.SKILL",parentBeanName="JrsSkillAssocDim",parentBaseTableName="REFT.JRS_SKILL_ASSOC")
public class SkillDim extends NaryTreeNode {
	@DbColumn(columnName="SKILL_SK_ID",isId=true)
	private int        skillSkId;
	@DbColumn(columnName="SKILL_ID",keySeq=1)
	private int        skillId;
	@DbColumn(columnName="SKILL_NM")
	private String     skillNm;
	@DbColumn(columnName="SKILL_DESC")
	private String     skillDesc;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SkillDim () {
		
	}
	
	// Define natural key constructor
	public SkillDim (
      int        skillId
	) {
		this.skillId                        = skillId;
		
	}
	
	// Define base constructor
	public SkillDim (
      int        skillId
    , String     skillNm
    , String     skillDesc
	) {
		this.skillId                        = skillId;
		this.skillNm                        = skillNm;
		this.skillDesc                      = skillDesc;
		
	}
    
	// Define full constructor
	public SkillDim (
		  int        skillSkId
		, int        skillId
		, String     skillNm
		, String     skillDesc
	) {
		this.skillSkId                      = skillSkId;
		this.skillId                        = skillId;
		this.skillNm                        = skillNm;
		this.skillDesc                      = skillDesc;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.skillId)
		;
	}
	public String getDescription() { 
		return this.skillNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SkillDim other = (SkillDim) obj;
		if (
            this.skillId == other.getSkillId()
         && this.skillNm.equals(other.getSkillNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.skillId))
        + "," + Helpers.formatCsvField(String.valueOf(this.skillNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.skillDesc))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SKILL_ID")
        + "," + Helpers.formatCsvField("SKILL_NM")
        + "," + Helpers.formatCsvField("SKILL_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getSkillSkId() {
		return skillSkId;
	}
	public void setSkillSkId(int skillSkId) {
		this.skillSkId = skillSkId;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public String getSkillNm() {
		return skillNm;
	}
	public void setSkillNm(String skillNm) {
		this.skillNm = skillNm;
	}
	public String getSkillDesc() {
		return skillDesc;
	}
	public void setSkillDesc(String skillDesc) {
		this.skillDesc = skillDesc;
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