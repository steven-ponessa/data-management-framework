package com.ibm.wfm.beans;


import java.sql.Date;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.ExcelSheet;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="EnhancedExpertiseTaxonomyDim",baseTableName="REFT.ENHANCED_EXPERTISE_TAXONOMY")
public class EnhancedExpertiseTaxonomyDim  {
	@DbColumn(columnName="JRS_ID",isId=true)
	private int        jrsId;
	private int        id;
	@DbColumn(columnName="JRS_CD",keySeq=1)
	@ExcelSheet(columnName="JRS ID",columnNum=1)	
	private String     jrsCd;
	@DbColumn(columnName="JRS_NM")
	@ExcelSheet(columnName="JR/S",columnNum=0)	
	private String     jrsNm;
	@DbColumn(columnName="JOB_ROLE_CD")
	@ExcelSheet(columnName="JR Id",columnNum=5)	
	private String     jobRoleCd;
	@DbColumn(columnName="JOB_ROLE_NM")
	@ExcelSheet(columnName="Job Role Name  ",columnNum=4)	
	private String     jobRoleNm;
	@DbColumn(columnName="SPECIALTY_CD")
	@ExcelSheet(columnName="SS ID",columnNum=7)	
	private String     specialtyCd;
	@DbColumn(columnName="SPECIALTY_NM")
	@ExcelSheet(columnName="Specialty Name",columnNum=6)	
	private String     specialtyNm;
	@DbColumn(columnName="SPECIALTY_DESC")
	@ExcelSheet(columnName="Description",columnNum=8)	
	private String     specialtyDesc;
	@DbColumn(columnName="GROWTH_PLATFORM_CD")
	private String     growthPlatformCd;
	@DbColumn(columnName="GROWTH_PLATFORM_NM")
	@ExcelSheet(columnName="JR/S Growth Platform",columnNum=9)	
	private String     growthPlatformNm;
	@DbColumn(columnName="SERVICE_LINE_CD")
	private String     serviceLineCd;
	@DbColumn(columnName="SERVICE_LINE_NM")
	@ExcelSheet(columnName="JR/S Service Line",columnNum=10)	
	private String     serviceLineNm;
	@DbColumn(columnName="PRACTICE_CD")
	private String     practiceCd;
	@DbColumn(columnName="PRACTICE_NM")
	@ExcelSheet(columnName="JR/S Practice",columnNum=11)	
	private String     practiceNm;
	@DbColumn(columnName="SERVICE_AREA_CD")
	private String     serviceAreaCd;
	@DbColumn(columnName="SERVICE_AREA_NM")
	@ExcelSheet(columnName="JR/S Service Area",columnNum=12)	
	private String     serviceAreaNm;
	@DbColumn(columnName="CAPACITY_GROUP_NM")
	@ExcelSheet(columnName="Capacity Group",columnNum=13)	
	private String     capacityGroupNm;
	@DbColumn(columnName="ISV_PACKAGE_NM")
	@ExcelSheet(columnName="ISV Package",columnNum=14)	
	private String     isvPackageNm;
	@DbColumn(columnName="MARKET_VALUE_NM")
	@ExcelSheet(columnName="Market Value",columnNum=15)	
	private String     marketValueNm;
	@DbColumn(columnName="PREMIUM_TYPE_NM")
	@ExcelSheet(columnName="Premium Type",columnNum=16)	
	private String     premiumTypeNm;
	@DbColumn(columnName="PRIMARY_SKILL_GROUPING_NM")
	@ExcelSheet(columnName="Primary Skill Grouping",columnNum=17)	
	private String     primarySkillGroupingNm;
	@DbColumn(columnName="PRIMARY_JRS_CNT")
	@ExcelSheet(columnName="Primary JR/S Count",columnNum=18)	
	private int        primaryJrsCnt;
	@DbColumn(columnName="SECONDARY_JRS_CNT")
	@ExcelSheet(columnName="Secondary JR/S Count",columnNum=19)	
	private int        secondaryJrsCnt;
	@DbColumn(columnName="JRS_PRIMARY_JOB_CATEGORY_NM")
	@ExcelSheet(columnName="JR/S Primary Job Category",columnNum=20)	
	private String     jrsPrimaryJobCategoryNm;
	@DbColumn(columnName="JRS_COMMUNITY_NM")
	@ExcelSheet(columnName="JR/S Community",columnNum=21)	
	private String     jrsCommunityNm;
	@DbColumn(columnName="JRS_CAMSS_NM")
	@ExcelSheet(columnName="JR/S CAMSS",columnNum=22)	
	private String     jrsCamssNm;
	@DbColumn(columnName="SVF_GROUP_NM")
	@ExcelSheet(columnName="SVF Group",columnNum=23)	
	private String     svfGroupNm;
	@DbColumn(columnName="CMPNSTN_GRD_LST")
	@ExcelSheet(columnName="Band range",columnNum=2)	
	private String     cmpnstnGrdLst;
	@DbColumn(columnName="INCENTIVE_FLG")
	@ExcelSheet(columnName="Incentive Eligible",columnNum=3)	
	private String     incentiveFlg;
	@DbColumn(columnName="CREATED_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	@ExcelSheet(columnName="Date Created",columnNum=24)	
	private Date       createdDt;
	@DbColumn(columnName="PJC_CD")
	@ExcelSheet(columnName="PJC Id",columnNum=25)	
	private String     pjcCd;
	@DbColumn(columnName="PJC_NM")
	@ExcelSheet(columnName="PJC Name",columnNum=26)	
	private String     pjcNm;
	@DbColumn(columnName="SJC_CD")
	@ExcelSheet(columnName="SJC Id",columnNum=27)	
	private String     sjcCd;
	@DbColumn(columnName="SJC_NM")
	@ExcelSheet(columnName="SJC Name",columnNum=28)	
	private String     sjcNm;
	@DbColumn(columnName="IBM_CONSLTNG_IND")
	private String     ibmConsltngInd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public EnhancedExpertiseTaxonomyDim () {
		
	}
	
	// Define natural key constructor
	public EnhancedExpertiseTaxonomyDim (
      String     jrsCd
	) {
		this.jrsCd                          = jrsCd;
		
	}
	
	// Define base constructor
	public EnhancedExpertiseTaxonomyDim (
      String     jrsNm
    , String     jobRoleCd
    , String     jobRoleNm
    , String     specialtyCd
    , String     specialtyNm
    , String     specialtyDesc
    , String     growthPlatformCd
    , String     growthPlatformNm
    , String     serviceLineCd
    , String     serviceLineNm
    , String     practiceCd
    , String     practiceNm
    , String     serviceAreaCd
    , String     serviceAreaNm
    , String     capacityGroupNm
    , String     isvPackageNm
    , String     marketValueNm
    , String     premiumTypeNm
    , String     primarySkillGroupingNm
    , int        primaryJrsCnt
    , int        secondaryJrsCnt
    , String     jrsPrimaryJobCategoryNm
    , String     jrsCommunityNm
    , String     jrsCamssNm
    , String     svfGroupNm
    , String     cmpnstnGrdLst
    , String     incentiveFlg
    , Date       createdDt
    , String     pjcCd
    , String     pjcNm
    , String     sjcCd
    , String     sjcNm
    , String     ibmConsltngInd
	) {
		this.jrsNm                          = jrsNm;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.specialtyDesc                  = specialtyDesc;
		this.growthPlatformCd               = growthPlatformCd;
		this.growthPlatformNm               = growthPlatformNm;
		this.serviceLineCd                  = serviceLineCd;
		this.serviceLineNm                  = serviceLineNm;
		this.practiceCd                     = practiceCd;
		this.practiceNm                     = practiceNm;
		this.serviceAreaCd                  = serviceAreaCd;
		this.serviceAreaNm                  = serviceAreaNm;
		this.capacityGroupNm                = capacityGroupNm;
		this.isvPackageNm                   = isvPackageNm;
		this.marketValueNm                  = marketValueNm;
		this.premiumTypeNm                  = premiumTypeNm;
		this.primarySkillGroupingNm         = primarySkillGroupingNm;
		this.primaryJrsCnt                  = primaryJrsCnt;
		this.secondaryJrsCnt                = secondaryJrsCnt;
		this.jrsPrimaryJobCategoryNm        = jrsPrimaryJobCategoryNm;
		this.jrsCommunityNm                 = jrsCommunityNm;
		this.jrsCamssNm                     = jrsCamssNm;
		this.svfGroupNm                     = svfGroupNm;
		this.cmpnstnGrdLst                  = cmpnstnGrdLst;
		this.incentiveFlg                   = incentiveFlg;
		this.createdDt                      = createdDt;
		this.pjcCd                          = pjcCd;
		this.pjcNm                          = pjcNm;
		this.sjcCd                          = sjcCd;
		this.sjcNm                          = sjcNm;
		this.ibmConsltngInd                 = ibmConsltngInd;
		
	}
    
	// Define full constructor
	public EnhancedExpertiseTaxonomyDim (
		  int        jrsId
		, String     jrsCd
		, String     jrsNm
		, String     jobRoleCd
		, String     jobRoleNm
		, String     specialtyCd
		, String     specialtyNm
		, String     specialtyDesc
		, String     growthPlatformCd
		, String     growthPlatformNm
		, String     serviceLineCd
		, String     serviceLineNm
		, String     practiceCd
		, String     practiceNm
		, String     serviceAreaCd
		, String     serviceAreaNm
		, String     capacityGroupNm
		, String     isvPackageNm
		, String     marketValueNm
		, String     premiumTypeNm
		, String     primarySkillGroupingNm
		, int        primaryJrsCnt
		, int        secondaryJrsCnt
		, String     jrsPrimaryJobCategoryNm
		, String     jrsCommunityNm
		, String     jrsCamssNm
		, String     svfGroupNm
		, String     cmpnstnGrdLst
		, String     incentiveFlg
		, Date       createdDt
		, String     pjcCd
		, String     pjcNm
		, String     sjcCd
		, String     sjcNm
		, String     ibmConsltngInd
	) {
		this.jrsId                          = jrsId;
		this.id								= jrsId;
		this.jrsCd                          = jrsCd;
		this.jrsNm                          = jrsNm;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.specialtyDesc                  = specialtyDesc;
		this.growthPlatformCd               = growthPlatformCd;
		this.growthPlatformNm               = growthPlatformNm;
		this.serviceLineCd                  = serviceLineCd;
		this.serviceLineNm                  = serviceLineNm;
		this.practiceCd                     = practiceCd;
		this.practiceNm                     = practiceNm;
		this.serviceAreaCd                  = serviceAreaCd;
		this.serviceAreaNm                  = serviceAreaNm;
		this.capacityGroupNm                = capacityGroupNm;
		this.isvPackageNm                   = isvPackageNm;
		this.marketValueNm                  = marketValueNm;
		this.premiumTypeNm                  = premiumTypeNm;
		this.primarySkillGroupingNm         = primarySkillGroupingNm;
		this.primaryJrsCnt                  = primaryJrsCnt;
		this.secondaryJrsCnt                = secondaryJrsCnt;
		this.jrsPrimaryJobCategoryNm        = jrsPrimaryJobCategoryNm;
		this.jrsCommunityNm                 = jrsCommunityNm;
		this.jrsCamssNm                     = jrsCamssNm;
		this.svfGroupNm                     = svfGroupNm;
		this.cmpnstnGrdLst                  = cmpnstnGrdLst;
		this.incentiveFlg                   = incentiveFlg;
		this.createdDt                      = createdDt;
		this.pjcCd                          = pjcCd;
		this.pjcNm                          = pjcNm;
		this.sjcCd                          = sjcCd;
		this.sjcNm                          = sjcNm;
		this.ibmConsltngInd                 = ibmConsltngInd;
		
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		EnhancedExpertiseTaxonomyDim other = (EnhancedExpertiseTaxonomyDim) obj;
		if (
            this.jrsNm.equals(other.getJrsNm())
         && this.jobRoleCd.equals(other.getJobRoleCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.jrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.growthPlatformCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.growthPlatformNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceLineCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceLineNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.practiceCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.practiceNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.serviceAreaNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.capacityGroupNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.isvPackageNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.marketValueNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.premiumTypeNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.primarySkillGroupingNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.primaryJrsCnt))
        + "," + Helpers.formatCsvField(String.valueOf(this.secondaryJrsCnt))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsPrimaryJobCategoryNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsCommunityNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsCamssNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.svfGroupNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.cmpnstnGrdLst))
        + "," + Helpers.formatCsvField(String.valueOf(this.incentiveFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.createdDt))
        + "," + Helpers.formatCsvField(String.valueOf(this.pjcCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.pjcNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.sjcCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.sjcNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.ibmConsltngInd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("JRS_NM")
        + "," + Helpers.formatCsvField("JOB_ROLE_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_NM")
        + "," + Helpers.formatCsvField("SPECIALTY_CD")
        + "," + Helpers.formatCsvField("SPECIALTY_NM")
        + "," + Helpers.formatCsvField("SPECIALTY_DESC")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_CD")
        + "," + Helpers.formatCsvField("GROWTH_PLATFORM_NM")
        + "," + Helpers.formatCsvField("SERVICE_LINE_CD")
        + "," + Helpers.formatCsvField("SERVICE_LINE_NM")
        + "," + Helpers.formatCsvField("PRACTICE_CD")
        + "," + Helpers.formatCsvField("PRACTICE_NM")
        + "," + Helpers.formatCsvField("SERVICE_AREA_CD")
        + "," + Helpers.formatCsvField("SERVICE_AREA_NM")
        + "," + Helpers.formatCsvField("CAPACITY_GROUP_NM")
        + "," + Helpers.formatCsvField("ISV_PACKAGE_NM")
        + "," + Helpers.formatCsvField("MARKET_VALUE_NM")
        + "," + Helpers.formatCsvField("PREMIUM_TYPE_NM")
        + "," + Helpers.formatCsvField("PRIMARY_SKILL_GROUPING_NM")
        + "," + Helpers.formatCsvField("PRIMARY_JRS_CNT")
        + "," + Helpers.formatCsvField("SECONDARY_JRS_CNT")
        + "," + Helpers.formatCsvField("JRS_PRIMARY_JOB_CATEGORY_NM")
        + "," + Helpers.formatCsvField("JRS_COMMUNITY_NM")
        + "," + Helpers.formatCsvField("JRS_CAMSS_NM")
        + "," + Helpers.formatCsvField("SVF_GROUP_NM")
        + "," + Helpers.formatCsvField("CMPNSTN_GRD_LST")
        + "," + Helpers.formatCsvField("INCENTIVE_FLG")
        + "," + Helpers.formatCsvField("CREATED_DT")
        + "," + Helpers.formatCsvField("PJC_CD")
        + "," + Helpers.formatCsvField("PJC_NM")
        + "," + Helpers.formatCsvField("SJC_CD")
        + "," + Helpers.formatCsvField("SJC_NM")
        + "," + Helpers.formatCsvField("IBM_CONSLTNG_IND")
		;
	}
    
	// Define Getters and Setters
	public int getJrsId() {
		return jrsId;
	}
	public int getId() {
		return jrsId;
	}
	public void setJrsId(int jrsId) {
		this.jrsId = jrsId;
		this.id = jrsId;
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
	public String getSpecialtyDesc() {
		return specialtyDesc;
	}
	public void setSpecialtyDesc(String specialtyDesc) {
		this.specialtyDesc = specialtyDesc;
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
	public String getServiceLineCd() {
		return serviceLineCd;
	}
	public void setServiceLineCd(String serviceLineCd) {
		this.serviceLineCd = serviceLineCd;
	}
	public String getServiceLineNm() {
		return serviceLineNm;
	}
	public void setServiceLineNm(String serviceLineNm) {
		this.serviceLineNm = serviceLineNm;
	}
	public String getPracticeCd() {
		return practiceCd;
	}
	public void setPracticeCd(String practiceCd) {
		this.practiceCd = practiceCd;
	}
	public String getPracticeNm() {
		return practiceNm;
	}
	public void setPracticeNm(String practiceNm) {
		this.practiceNm = practiceNm;
	}
	public String getServiceAreaCd() {
		return serviceAreaCd;
	}
	public void setServiceAreaCd(String serviceAreaCd) {
		this.serviceAreaCd = serviceAreaCd;
	}
	public String getServiceAreaNm() {
		return serviceAreaNm;
	}
	public void setServiceAreaNm(String serviceAreaNm) {
		this.serviceAreaNm = serviceAreaNm;
	}
	public String getCapacityGroupNm() {
		return capacityGroupNm;
	}
	public void setCapacityGroupNm(String capacityGroupNm) {
		this.capacityGroupNm = capacityGroupNm;
	}
	public String getIsvPackageNm() {
		return isvPackageNm;
	}
	public void setIsvPackageNm(String isvPackageNm) {
		this.isvPackageNm = isvPackageNm;
	}
	public String getMarketValueNm() {
		return marketValueNm;
	}
	public void setMarketValueNm(String marketValueNm) {
		this.marketValueNm = marketValueNm;
	}
	public String getPremiumTypeNm() {
		return premiumTypeNm;
	}
	public void setPremiumTypeNm(String premiumTypeNm) {
		this.premiumTypeNm = premiumTypeNm;
	}
	public String getPrimarySkillGroupingNm() {
		return primarySkillGroupingNm;
	}
	public void setPrimarySkillGroupingNm(String primarySkillGroupingNm) {
		this.primarySkillGroupingNm = primarySkillGroupingNm;
	}
	public int getPrimaryJrsCnt() {
		return primaryJrsCnt;
	}
	public void setPrimaryJrsCnt(int primaryJrsCnt) {
		this.primaryJrsCnt = primaryJrsCnt;
	}
	public int getSecondaryJrsCnt() {
		return secondaryJrsCnt;
	}
	public void setSecondaryJrsCnt(int secondaryJrsCnt) {
		this.secondaryJrsCnt = secondaryJrsCnt;
	}
	public String getJrsPrimaryJobCategoryNm() {
		return jrsPrimaryJobCategoryNm;
	}
	public void setJrsPrimaryJobCategoryNm(String jrsPrimaryJobCategoryNm) {
		this.jrsPrimaryJobCategoryNm = jrsPrimaryJobCategoryNm;
	}
	public String getJrsCommunityNm() {
		return jrsCommunityNm;
	}
	public void setJrsCommunityNm(String jrsCommunityNm) {
		this.jrsCommunityNm = jrsCommunityNm;
	}
	public String getJrsCamssNm() {
		return jrsCamssNm;
	}
	public void setJrsCamssNm(String jrsCamssNm) {
		this.jrsCamssNm = jrsCamssNm;
	}
	public String getSvfGroupNm() {
		return svfGroupNm;
	}
	public void setSvfGroupNm(String svfGroupNm) {
		this.svfGroupNm = svfGroupNm;
	}
	public String getCmpnstnGrdLst() {
		return cmpnstnGrdLst;
	}
	public void setCmpnstnGrdLst(String cmpnstnGrdLst) {
		this.cmpnstnGrdLst = cmpnstnGrdLst;
	}
	public String getIncentiveFlg() {
		return incentiveFlg;
	}
	public void setIncentiveFlg(String incentiveFlg) {
		this.incentiveFlg = incentiveFlg;
	}
	public Date getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}
	public String getPjcCd() {
		return pjcCd;
	}
	public void setPjcCd(String pjcCd) {
		this.pjcCd = pjcCd;
	}
	public String getPjcNm() {
		return pjcNm;
	}
	public void setPjcNm(String pjcNm) {
		this.pjcNm = pjcNm;
	}
	public String getSjcCd() {
		return sjcCd;
	}
	public void setSjcCd(String sjcCd) {
		this.sjcCd = sjcCd;
	}
	public String getSjcNm() {
		return sjcNm;
	}
	public void setSjcNm(String sjcNm) {
		this.sjcNm = sjcNm;
	}
	public String getIbmConsltngInd() {
		return ibmConsltngInd;
	}
	public void setIbmConsltngInd(String ibmConsltngInd) {
		this.ibmConsltngInd = ibmConsltngInd;
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