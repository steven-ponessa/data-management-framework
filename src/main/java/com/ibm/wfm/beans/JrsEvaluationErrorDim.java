package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="JrsEvaluationErrorDim",baseTableName="DIF.JRS_EVALUATION_ERROR")
public class JrsEvaluationErrorDim extends NaryTreeNode {
	@DbColumn(columnName="JRS_EVAL_ID",isId=true)
	private int        jrsEvalId;
	@DbColumn(columnName="INET_EMAIL_ID",keySeq=1)
	private String     inetEmailId;
	@DbColumn(columnName="MGR_INET_EMAIL_ID")
	private String     mgrInetEmailId;
	@DbColumn(columnName="BRANCH_KEY_CD")
	private String     branchKeyCd;
	@DbColumn(columnName="FOUND_IND")
	private String     foundInd;
	@DbColumn(columnName="LEAF_KEY_CD")
	private String     leafKeyCd;
	@DbColumn(columnName="LVLEAF_KEY_CD")
	private String     lvleafKeyCd;
	@DbColumn(columnName="NODE_ERROR_IND")
	private String     nodeErrorInd;
	@DbColumn(columnName="NI_BYTES")
	private String     niBytes;
	@DbColumn(columnName="ARC_ERROR_IND")
	private String     arcErrorInd;
	@DbColumn(columnName="AI_BYTES")
	private String     aiBytes;
	@DbColumn(columnName="INVALID_NODE_LVL_TXT")
	private String     invalidNodeLvlTxt;
	@DbColumn(columnName="INVALID_ARC_LVL_TXT")
	private String     invalidArcLvlTxt;
	@DbColumn(columnName="BRAND_CD")
	private String     brandCd;
	@DbColumn(columnName="BRAND_NM")
	private String     brandNm;
	@DbColumn(columnName="GROWTH_PLATFORM_CD")
	private String     growthPlatformCd;
	@DbColumn(columnName="GROWTH_PLATFORM_NM")
	private String     growthPlatformNm;
	@DbColumn(columnName="JRS_SVC_LINE_CD")
	private String     jrsSvcLineCd;
	@DbColumn(columnName="JRS_SVC_LINE_NM")
	private String     jrsSvcLineNm;
	@DbColumn(columnName="JRS_PRACTICE_CD")
	private String     jrsPracticeCd;
	@DbColumn(columnName="JRS_PRACTICE_NM")
	private String     jrsPracticeNm;
	@DbColumn(columnName="JRS_SVC_AREA_CD")
	private String     jrsSvcAreaCd;
	@DbColumn(columnName="JRS_SVC_AREA_NM")
	private String     jrsSvcAreaNm;
	@DbColumn(columnName="JOB_ROLE_CD")
	private String     jobRoleCd;
	@DbColumn(columnName="JOB_ROLE_NM")
	private String     jobRoleNm;
	@DbColumn(columnName="SPECIALTY_CD")
	private String     specialtyCd;
	@DbColumn(columnName="SPECIALTY_NM")
	private String     specialtyNm;
	@DbColumn(columnName="JRS_CD")
	private String     jrsCd;
	@DbColumn(columnName="JRS_NM")
	private String     jrsNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public JrsEvaluationErrorDim () {
		
	}
	
	// Define natural key constructor
	public JrsEvaluationErrorDim (
      String     inetEmailId
	) {
		this.inetEmailId                    = inetEmailId;
		
	}
	
	// Define base constructor
	public JrsEvaluationErrorDim (
      String     inetEmailId
    , String     mgrInetEmailId
    , String     branchKeyCd
    , String     foundInd
    , String     leafKeyCd
    , String     lvleafKeyCd
    , String     nodeErrorInd
    , String     niBytes
    , String     arcErrorInd
    , String     aiBytes
    , String     invalidNodeLvlTxt
    , String     invalidArcLvlTxt
    , String     brandCd
    , String     brandNm
    , String     growthPlatformCd
    , String     growthPlatformNm
    , String     jrsSvcLineCd
    , String     jrsSvcLineNm
    , String     jrsPracticeCd
    , String     jrsPracticeNm
    , String     jrsSvcAreaCd
    , String     jrsSvcAreaNm
    , String     jobRoleCd
    , String     jobRoleNm
    , String     specialtyCd
    , String     specialtyNm
    , String     jrsCd
    , String     jrsNm
	) {
		this.inetEmailId                    = inetEmailId;
		this.mgrInetEmailId                 = mgrInetEmailId;
		this.branchKeyCd                    = branchKeyCd;
		this.foundInd                       = foundInd;
		this.leafKeyCd                      = leafKeyCd;
		this.lvleafKeyCd                    = lvleafKeyCd;
		this.nodeErrorInd                   = nodeErrorInd;
		this.niBytes                        = niBytes;
		this.arcErrorInd                    = arcErrorInd;
		this.aiBytes                        = aiBytes;
		this.invalidNodeLvlTxt              = invalidNodeLvlTxt;
		this.invalidArcLvlTxt               = invalidArcLvlTxt;
		this.brandCd                        = brandCd;
		this.brandNm                        = brandNm;
		this.growthPlatformCd               = growthPlatformCd;
		this.growthPlatformNm               = growthPlatformNm;
		this.jrsSvcLineCd                   = jrsSvcLineCd;
		this.jrsSvcLineNm                   = jrsSvcLineNm;
		this.jrsPracticeCd                  = jrsPracticeCd;
		this.jrsPracticeNm                  = jrsPracticeNm;
		this.jrsSvcAreaCd                   = jrsSvcAreaCd;
		this.jrsSvcAreaNm                   = jrsSvcAreaNm;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.jrsCd                          = jrsCd;
		this.jrsNm                          = jrsNm;
		
	}
    
	// Define full constructor
	public JrsEvaluationErrorDim (
		  int        jrsEvalId
		, String     inetEmailId
		, String     mgrInetEmailId
		, String     branchKeyCd
		, String     foundInd
		, String     leafKeyCd
		, String     lvleafKeyCd
		, String     nodeErrorInd
		, String     niBytes
		, String     arcErrorInd
		, String     aiBytes
		, String     invalidNodeLvlTxt
		, String     invalidArcLvlTxt
		, String     brandCd
		, String     brandNm
		, String     growthPlatformCd
		, String     growthPlatformNm
		, String     jrsSvcLineCd
		, String     jrsSvcLineNm
		, String     jrsPracticeCd
		, String     jrsPracticeNm
		, String     jrsSvcAreaCd
		, String     jrsSvcAreaNm
		, String     jobRoleCd
		, String     jobRoleNm
		, String     specialtyCd
		, String     specialtyNm
		, String     jrsCd
		, String     jrsNm
	) {
		this.jrsEvalId                      = jrsEvalId;
		this.inetEmailId                    = inetEmailId;
		this.mgrInetEmailId                 = mgrInetEmailId;
		this.branchKeyCd                    = branchKeyCd;
		this.foundInd                       = foundInd;
		this.leafKeyCd                      = leafKeyCd;
		this.lvleafKeyCd                    = lvleafKeyCd;
		this.nodeErrorInd                   = nodeErrorInd;
		this.niBytes                        = niBytes;
		this.arcErrorInd                    = arcErrorInd;
		this.aiBytes                        = aiBytes;
		this.invalidNodeLvlTxt              = invalidNodeLvlTxt;
		this.invalidArcLvlTxt               = invalidArcLvlTxt;
		this.brandCd                        = brandCd;
		this.brandNm                        = brandNm;
		this.growthPlatformCd               = growthPlatformCd;
		this.growthPlatformNm               = growthPlatformNm;
		this.jrsSvcLineCd                   = jrsSvcLineCd;
		this.jrsSvcLineNm                   = jrsSvcLineNm;
		this.jrsPracticeCd                  = jrsPracticeCd;
		this.jrsPracticeNm                  = jrsPracticeNm;
		this.jrsSvcAreaCd                   = jrsSvcAreaCd;
		this.jrsSvcAreaNm                   = jrsSvcAreaNm;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.jrsCd                          = jrsCd;
		this.jrsNm                          = jrsNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.inetEmailId
		;
	}
	public String getDescription() { 
		return null; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		JrsEvaluationErrorDim other = (JrsEvaluationErrorDim) obj;
		if (
            this.inetEmailId.equals(other.getInetEmailId())
         && this.mgrInetEmailId.equals(other.getMgrInetEmailId())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.inetEmailId))
        + "," + Helpers.formatCsvField(String.valueOf(this.mgrInetEmailId))
        + "," + Helpers.formatCsvField(String.valueOf(this.branchKeyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.foundInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.leafKeyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.lvleafKeyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.nodeErrorInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.niBytes))
        + "," + Helpers.formatCsvField(String.valueOf(this.arcErrorInd))
        + "," + Helpers.formatCsvField(String.valueOf(this.aiBytes))
        + "," + Helpers.formatCsvField(String.valueOf(this.invalidNodeLvlTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.invalidArcLvlTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.brandCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.brandNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.growthPlatformCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.growthPlatformNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsSvcLineCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsSvcLineNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsPracticeCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsPracticeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsSvcAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsSvcAreaNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("INET_EMAIL_ID")
        + "," + Helpers.formatCsvField("MGR_INET_EMAIL_ID")
        + "," + Helpers.formatCsvField("BRANCH_KEY_CD")
        + "," + Helpers.formatCsvField("FOUND_IND")
        + "," + Helpers.formatCsvField("LEAF_KEY_CD")
        + "," + Helpers.formatCsvField("LVLEAF_KEY_CD")
        + "," + Helpers.formatCsvField("NODE_ERROR_IND")
        + "," + Helpers.formatCsvField("NI_BYTES")
        + "," + Helpers.formatCsvField("ARC_ERROR_IND")
        + "," + Helpers.formatCsvField("AI_BYTES")
        + "," + Helpers.formatCsvField("INVALID_NODE_LVL_TXT")
        + "," + Helpers.formatCsvField("INVALID_ARC_LVL_TXT")
        + "," + Helpers.formatCsvField("BRAND_CD")
        + "," + Helpers.formatCsvField("BRAND_NM")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_CD")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_NM")
        + "," + Helpers.formatCsvField("JRS_SVC_LINE_CD")
        + "," + Helpers.formatCsvField("JRS_SVC_LINE_NM")
        + "," + Helpers.formatCsvField("JRS_PRACTICE_CD")
        + "," + Helpers.formatCsvField("JRS_PRACTICE_NM")
        + "," + Helpers.formatCsvField("JRS_SVC_AREA_CD")
        + "," + Helpers.formatCsvField("JRS_SVC_AREA_NM")
        + "," + Helpers.formatCsvField("JOB_ROLE_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_NM")
        + "," + Helpers.formatCsvField("SPECIALTY_CD")
        + "," + Helpers.formatCsvField("SPECIALTY_NM")
        + "," + Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("JRS_NM")
		;
	}
    
	// Define Getters and Setters
	public int getJrsEvalId() {
		return jrsEvalId;
	}
	public void setJrsEvalId(int jrsEvalId) {
		this.jrsEvalId = jrsEvalId;
	}
	public String getInetEmailId() {
		return inetEmailId;
	}
	public void setInetEmailId(String inetEmailId) {
		this.inetEmailId = inetEmailId;
	}
	public String getMgrInetEmailId() {
		return mgrInetEmailId;
	}
	public void setMgrInetEmailId(String mgrInetEmailId) {
		this.mgrInetEmailId = mgrInetEmailId;
	}
	public String getBranchKeyCd() {
		return branchKeyCd;
	}
	public void setBranchKeyCd(String branchKeyCd) {
		this.branchKeyCd = branchKeyCd;
	}
	public String getFoundInd() {
		return foundInd;
	}
	public void setFoundInd(String foundInd) {
		this.foundInd = foundInd;
	}
	public String getLeafKeyCd() {
		return leafKeyCd;
	}
	public void setLeafKeyCd(String leafKeyCd) {
		this.leafKeyCd = leafKeyCd;
	}
	public String getLvleafKeyCd() {
		return lvleafKeyCd;
	}
	public void setLvleafKeyCd(String lvleafKeyCd) {
		this.lvleafKeyCd = lvleafKeyCd;
	}
	public String getNodeErrorInd() {
		return nodeErrorInd;
	}
	public void setNodeErrorInd(String nodeErrorInd) {
		this.nodeErrorInd = nodeErrorInd;
	}
	public String getNiBytes() {
		return niBytes;
	}
	public void setNiBytes(String niBytes) {
		this.niBytes = niBytes;
	}
	public String getArcErrorInd() {
		return arcErrorInd;
	}
	public void setArcErrorInd(String arcErrorInd) {
		this.arcErrorInd = arcErrorInd;
	}
	public String getAiBytes() {
		return aiBytes;
	}
	public void setAiBytes(String aiBytes) {
		this.aiBytes = aiBytes;
	}
	public String getInvalidNodeLvlTxt() {
		return invalidNodeLvlTxt;
	}
	public void setInvalidNodeLvlTxt(String invalidNodeLvlTxt) {
		this.invalidNodeLvlTxt = invalidNodeLvlTxt;
	}
	public String getInvalidArcLvlTxt() {
		return invalidArcLvlTxt;
	}
	public void setInvalidArcLvlTxt(String invalidArcLvlTxt) {
		this.invalidArcLvlTxt = invalidArcLvlTxt;
	}
	public String getBrandCd() {
		return brandCd;
	}
	public void setBrandCd(String brandCd) {
		this.brandCd = brandCd;
	}
	public String getBrandNm() {
		return brandNm;
	}
	public void setBrandNm(String brandNm) {
		this.brandNm = brandNm;
	}
	public String getGrowthPlatformCd() {
		return growthPlatformCd;
	}
	public void setGrowthPlatformCd(String growthPlatformCd) {
		this.growthPlatformCd = growthPlatformCd;
	}
	public String getGrowthPlatformNm() {
		return growthPlatformNm;
	}
	public void setGrowthPlatformNm(String growthPlatformNm) {
		this.growthPlatformNm = growthPlatformNm;
	}
	public String getJrsSvcLineCd() {
		return jrsSvcLineCd;
	}
	public void setJrsSvcLineCd(String jrsSvcLineCd) {
		this.jrsSvcLineCd = jrsSvcLineCd;
	}
	public String getJrsSvcLineNm() {
		return jrsSvcLineNm;
	}
	public void setJrsSvcLineNm(String jrsSvcLineNm) {
		this.jrsSvcLineNm = jrsSvcLineNm;
	}
	public String getJrsPracticeCd() {
		return jrsPracticeCd;
	}
	public void setJrsPracticeCd(String jrsPracticeCd) {
		this.jrsPracticeCd = jrsPracticeCd;
	}
	public String getJrsPracticeNm() {
		return jrsPracticeNm;
	}
	public void setJrsPracticeNm(String jrsPracticeNm) {
		this.jrsPracticeNm = jrsPracticeNm;
	}
	public String getJrsSvcAreaCd() {
		return jrsSvcAreaCd;
	}
	public void setJrsSvcAreaCd(String jrsSvcAreaCd) {
		this.jrsSvcAreaCd = jrsSvcAreaCd;
	}
	public String getJrsSvcAreaNm() {
		return jrsSvcAreaNm;
	}
	public void setJrsSvcAreaNm(String jrsSvcAreaNm) {
		this.jrsSvcAreaNm = jrsSvcAreaNm;
	}
	public String getJobRoleCd() {
		return jobRoleCd;
	}
	public void setJobRoleCd(String jobRoleCd) {
		this.jobRoleCd = jobRoleCd;
	}
	public String getJobRoleNm() {
		return jobRoleNm;
	}
	public void setJobRoleNm(String jobRoleNm) {
		this.jobRoleNm = jobRoleNm;
	}
	public String getSpecialtyCd() {
		return specialtyCd;
	}
	public void setSpecialtyCd(String specialtyCd) {
		this.specialtyCd = specialtyCd;
	}
	public String getSpecialtyNm() {
		return specialtyNm;
	}
	public void setSpecialtyNm(String specialtyNm) {
		this.specialtyNm = specialtyNm;
	}
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
	public String getJrsNm() {
		return jrsNm;
	}
	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
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