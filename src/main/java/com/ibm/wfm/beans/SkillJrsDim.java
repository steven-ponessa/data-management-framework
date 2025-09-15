package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SkillJrsDim",baseTableName="REFT.SKILL_JRS",parentBeanName="JrsDim",parentBaseTableName="REFT.JRS")
public class SkillJrsDim extends NaryTreeNode {
	@DbColumn(columnName="SKILL_SK_ID",isId=true)
	private int        skillSkId;
	@DbColumn(columnName="SKILL_ID",keySeq=1)
	private int        skillId;
	@DbColumn(columnName="SKILL_NM")
	private String     skillNm;
	@DbColumn(columnName="SKILL_DESC")
	private String     skillDesc;
	@DbColumn(columnName="JRS_CD",foreignKeySeq=1)
	private String     jrsCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SkillJrsDim () {
		
	}
	
	// Define natural key constructor
	public SkillJrsDim (
      int        skillId
	) {
		this.skillId                        = skillId;
		
	}
    
	// Define full constructor
	public SkillJrsDim (
		  int        skillSkId
		, int        skillId
		, String     skillNm
		, String     skillDesc
		, String     jrsCd
	) {
		this.skillSkId                      = skillSkId;
		this.skillId                        = skillId;
		this.skillNm                        = skillNm;
		this.skillDesc                      = skillDesc;
		this.jrsCd                          = jrsCd;
		
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

	public String getJrsCd() {
		return jrsCd;
	}

	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
}