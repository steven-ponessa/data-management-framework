package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JrsSkillAssocDim",baseTableName="REFT.JRS_SKILL_ASSOC",parentBeanName="JrsDim",parentBaseTableName="REFT.JRS")
public class JrsSkillAssocDim extends NaryTreeNode {
	@DbColumn(columnName="JRS_SKILL_SK_ID",isId=true)
	private int        jrsSkillSkId;
	@DbColumn(columnName="JRS_CD",keySeq=1,foreignKeySeq=1,assocParentKey=1)
	private String     jrsCd;
	@DbColumn(columnName="SKILL_ID",keySeq=2,assocChildKey=1)
	private int        skillId;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public JrsSkillAssocDim () {
		
	}
	
	// Define natural key constructor
	public JrsSkillAssocDim (
      String     jrsCd
    , int        skillId
	) {
		this.jrsCd                          = jrsCd;
		this.skillId                        = skillId;
		
	}
    
	// Define full constructor
	public JrsSkillAssocDim (
		  int        jrsSkillSkId
		, String     jrsCd
		, int        skillId
	) {
		this.jrsSkillSkId                   = jrsSkillSkId;
		this.jrsCd                          = jrsCd;
		this.skillId                        = skillId;
		
	}
	
	@Override
	public String getCode() { 
		return this.jrsCd
    +":"+ this.skillId
		;
	}
	public String getDescription() { 
		return this.jrsCd
			    +":"+ this.skillId; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JrsSkillAssocDim other = (JrsSkillAssocDim) obj;
		if (
            this.jrsCd.equals(other.getJrsCd())
         && this.skillId == other.getSkillId()
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.skillId))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("SKILL_ID")
		;
	}
    
	// Define Getters and Setters
	public int getJrsSkillSkId() {
		return jrsSkillSkId;
	}
	public void setJrsSkillSkId(int jrsSkillSkId) {
		this.jrsSkillSkId = jrsSkillSkId;
	}
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
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