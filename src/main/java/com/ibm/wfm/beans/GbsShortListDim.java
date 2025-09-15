package com.ibm.wfm.beans;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.ExcelSheet;

@DbTable(beanName="GbsShortListDim",baseTableName="REFT.GBS_SHORT_LIST")
public class GbsShortListDim  {
	@DbColumn(columnName="JRS_ID",isId=true)
	private int        jrsId;
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
	//@ExcelSheet(columnName="Premium Type",columnNum=16)	
	@ExcelSheet(columnName="Recovery Adder (India)",columnNum=16)
	private String     recoveryAdderCicIndiaNm;
	@ExcelSheet(columnName="Recovery Adder (Other CICs Excl Domestic CICs)",columnNum=17)
	private String     recoveryAdderCicOtherNm;
	@DbColumn(columnName="PRIMARY_SKILL_GROUPING_NM")
	@ExcelSheet(columnName="Primary Skill Grouping",columnNum=18)	
	private String     primarySkillGroupingNm;
	@DbColumn(columnName="PRIMARY_JRS_CNT")
	@ExcelSheet(columnName="Primary JR/S Count",columnNum=19)	
	private int        primaryJrsCnt;
	@DbColumn(columnName="SECONDARY_JRS_CNT")
	@ExcelSheet(columnName="Secondary JR/S Count",columnNum=20)	
	private int        secondaryJrsCnt;
	@DbColumn(columnName="JRS_PRIMARY_JOB_CATEGORY_NM")
	@ExcelSheet(columnName="JR/S Primary Job Category",columnNum=21)	
	private String     jrsPrimaryJobCategoryNm;
	@DbColumn(columnName="JRS_COMMUNITY_NM")
	@ExcelSheet(columnName="JR/S Community",columnNum=22)	
	private String     jrsCommunityNm;
	@DbColumn(columnName="JRS_CAMSS_NM")
	@ExcelSheet(columnName="JR/S CAMSS",columnNum=23)	
	private String     jrsCamssNm;
	@DbColumn(columnName="SVF_GROUP_NM")
	@ExcelSheet(columnName="SVF Group",columnNum=24)	
	private String     svfGroupNm;
	@DbColumn(columnName="CALCULATED_VALUE")
	private int        calculatedValue;
	@DbColumn(columnName="CMPNSTN_GRD_LST")
	@ExcelSheet(columnName="Band range",columnNum=2)	
	private String     cmpnstnGrdLst;
	@DbColumn(columnName="INCENTIVE_FLG")
	@ExcelSheet(columnName="Incentive Eligible",columnNum=3)	
	private String     incentiveFlg;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;
	
	// Define null constructor
	public GbsShortListDim () {
		
	}
	
	
	// Define Getters and Setters
	public int getJrsId() {
		return jrsId;
	}
	public void setJrsId(int jrsId) {
		this.jrsId = jrsId;
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
	public int getCalculatedValue() {
		return calculatedValue;
	}
	public void setCalculatedValue(int calculatedValue) {
		this.calculatedValue = calculatedValue;
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
	public String getRecoveryAdderCicIndiaNm() {
		return recoveryAdderCicIndiaNm;
	}
	public void setRecoveryAdderCicIndiaNm(String recoveryAdderCicIndiaNm) {
		this.recoveryAdderCicIndiaNm = recoveryAdderCicIndiaNm;
	}
	public String getRecoveryAdderCicOtherNm() {
		return recoveryAdderCicOtherNm;
	}
	public void setRecoveryAdderCicOther(String recoveryAdderCicOtherNm) {
		this.recoveryAdderCicOtherNm = recoveryAdderCicOtherNm;
	}
}