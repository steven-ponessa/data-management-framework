package com.ibm.wfm.beans;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="WorkdayJobCatalogDim",baseTableName="REFT.WORKDAY_JOB_CATALOG")
public class WorkdayJobCatalogDim extends NaryTreeNode {
	@DbColumn(columnName="JOB_CAT_ID",isId=true)
	private int        jobCatId;
	@DbColumn(columnName="PRI_JOB_CAT_CD",keySeq=1)
	private String     priJobCatCd;
	@DbColumn(columnName="PRI_JOB_CAT_NM")
	private String     priJobCatNm;
	@DbColumn(columnName="SEC_JOB_CAT_CD",keySeq=2)
	private String     secJobCatCd;
	@DbColumn(columnName="SEC_JOB_CAT_NM")
	private String     secJobCatNm;
	@DbColumn(columnName="JOB_FAMILY_CD")
	private String     jobFamilyCd;
	@DbColumn(columnName="JOB_FAMILY_NM")
	private String     jobFamilyNm;
	@DbColumn(columnName="INCENTIVE_FLG")
	private String     incentiveFlg;
	@DbColumn(columnName="COMP_GRD_CD",keySeq=3)
	private String     compGrdCd;
	@DbColumn(columnName="JOB_ROLE_CD")
	private String     jobRoleCd;
	@DbColumn(columnName="JOB_ROLE_NM")
	private String     jobRoleNm;
	@DbColumn(columnName="WD_JOB_PRO_CD")
	private String     wdJobProCd;
	@DbColumn(columnName="WD_JOB_PRO_NM")
	private String     wdJobProNm;
	@DbColumn(columnName="JOB_PRO_DESC")
	private String     jobProDesc;
	@DbColumn(columnName="WD_JOB_CD_AVAIL_NM")
	private String     wdJobCdAvailNm;
	@DbColumn(columnName="OT_ELIGIBLE_DLG")
	private String     otEligibleDlg;
	@DbColumn(columnName="JRS_CD",keySeq=4)
	private String     jrsCd;
	@DbColumn(columnName="JRS_NM")
	private String     jrsNm;
	@DbColumn(columnName="SPECIALTY_CD")
	private String     specialtyCd;
	@DbColumn(columnName="SPECIALTY_NM")
	private String     specialtyNm;
	@DbColumn(columnName="JRS_DESC")
	private String     jrsDesc;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public WorkdayJobCatalogDim () {
		
	}
	
	// Define natural key constructor
	public WorkdayJobCatalogDim (
      String     priJobCatCd
    , String     secJobCatCd
    , String     compGrdCd
    , String     jrsCd
	) {
		this.priJobCatCd                    = priJobCatCd;
		this.secJobCatCd                    = secJobCatCd;
		this.compGrdCd                      = compGrdCd;
		this.jrsCd                          = jrsCd;
		
	}
	
	// Define base constructor
	public WorkdayJobCatalogDim (
      String     priJobCatCd
    , String     priJobCatNm
    , String     secJobCatCd
    , String     secJobCatNm
    , String     jobFamilyCd
    , String     jobFamilyNm
    , String     incentiveFlg
    , String     compGrdCd
    , String     jobRoleCd
    , String     jobRoleNm
    , String     wdJobProCd
    , String     wdJobProNm
    , String     jobProDesc
    , String     wdJobCdAvailNm
    , String     otEligibleDlg
    , String     jrsCd
    , String     jrsNm
    , String     specialtyCd
    , String     specialtyNm
    , String     jrsDesc
	) {
		this.priJobCatCd                    = priJobCatCd;
		this.priJobCatNm                    = priJobCatNm;
		this.secJobCatCd                    = secJobCatCd;
		this.secJobCatNm                    = secJobCatNm;
		this.jobFamilyCd                    = jobFamilyCd;
		this.jobFamilyNm                    = jobFamilyNm;
		this.incentiveFlg                   = incentiveFlg;
		this.compGrdCd                      = compGrdCd;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.wdJobProCd                     = wdJobProCd;
		this.wdJobProNm                     = wdJobProNm;
		this.jobProDesc                     = jobProDesc;
		this.wdJobCdAvailNm                 = wdJobCdAvailNm;
		this.otEligibleDlg                  = otEligibleDlg;
		this.jrsCd                          = jrsCd;
		this.jrsNm                          = jrsNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.jrsDesc                        = jrsDesc;
		
	}
    
	// Define full constructor
	public WorkdayJobCatalogDim (
		  int        jobCatId
		, String     priJobCatCd
		, String     priJobCatNm
		, String     secJobCatCd
		, String     secJobCatNm
		, String     jobFamilyCd
		, String     jobFamilyNm
		, String     incentiveFlg
		, String     compGrdCd
		, String     jobRoleCd
		, String     jobRoleNm
		, String     wdJobProCd
		, String     wdJobProNm
		, String     jobProDesc
		, String     wdJobCdAvailNm
		, String     otEligibleDlg
		, String     jrsCd
		, String     jrsNm
		, String     specialtyCd
		, String     specialtyNm
		, String     jrsDesc
	) {
		this.jobCatId                       = jobCatId;
		this.priJobCatCd                    = priJobCatCd;
		this.priJobCatNm                    = priJobCatNm;
		this.secJobCatCd                    = secJobCatCd;
		this.secJobCatNm                    = secJobCatNm;
		this.jobFamilyCd                    = jobFamilyCd;
		this.jobFamilyNm                    = jobFamilyNm;
		this.incentiveFlg                   = incentiveFlg;
		this.compGrdCd                      = compGrdCd;
		this.jobRoleCd                      = jobRoleCd;
		this.jobRoleNm                      = jobRoleNm;
		this.wdJobProCd                     = wdJobProCd;
		this.wdJobProNm                     = wdJobProNm;
		this.jobProDesc                     = jobProDesc;
		this.wdJobCdAvailNm                 = wdJobCdAvailNm;
		this.otEligibleDlg                  = otEligibleDlg;
		this.jrsCd                          = jrsCd;
		this.jrsNm                          = jrsNm;
		this.specialtyCd                    = specialtyCd;
		this.specialtyNm                    = specialtyNm;
		this.jrsDesc                        = jrsDesc;
		
	}
	
	@Override
	public String getCode() { 
		return this.priJobCatCd
    +":"+ this.secJobCatCd
    +":"+ this.compGrdCd
    +":"+ this.jrsCd
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
                    
		WorkdayJobCatalogDim other = (WorkdayJobCatalogDim) obj;
		if (
            this.priJobCatCd.equals(other.getPriJobCatCd())
         && this.priJobCatNm.equals(other.getPriJobCatNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.priJobCatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.priJobCatNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.secJobCatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.secJobCatNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobFamilyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobFamilyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.incentiveFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.compGrdCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobRoleNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.wdJobProCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wdJobProNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobProDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.wdJobCdAvailNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.otEligibleDlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.specialtyNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsDesc))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("PRI_JOB_CAT_CD")
        + "," + Helpers.formatCsvField("PRI_JOB_CAT_NM")
        + "," + Helpers.formatCsvField("SEC_JOB_CAT_CD")
        + "," + Helpers.formatCsvField("SEC_JOB_CAT_NM")
        + "," + Helpers.formatCsvField("JOB_FAMILY_CD")
        + "," + Helpers.formatCsvField("JOB_FAMILY_NM")
        + "," + Helpers.formatCsvField("INCENTIVE_FLG")
        + "," + Helpers.formatCsvField("COMP_GRD_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_CD")
        + "," + Helpers.formatCsvField("JOB_ROLE_NM")
        + "," + Helpers.formatCsvField("WD_JOB_PRO_CD")
        + "," + Helpers.formatCsvField("WD_JOB_PRO_NM")
        + "," + Helpers.formatCsvField("JOB_PRO_DESC")
        + "," + Helpers.formatCsvField("WD_JOB_CD_AVAIL_NM")
        + "," + Helpers.formatCsvField("OT_ELIGIBLE_DLG")
        + "," + Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("JRS_NM")
        + "," + Helpers.formatCsvField("SPECIALTY_CD")
        + "," + Helpers.formatCsvField("SPECIALTY_NM")
        + "," + Helpers.formatCsvField("JRS_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getJobCatId() {
		return jobCatId;
	}
	public void setJobCatId(int jobCatId) {
		this.jobCatId = jobCatId;
	}
	public String getPriJobCatCd() {
		return priJobCatCd;
	}
	public void setPriJobCatCd(String priJobCatCd) {
		this.priJobCatCd = priJobCatCd;
	}
	public String getPriJobCatNm() {
		return priJobCatNm;
	}
	public void setPriJobCatNm(String priJobCatNm) {
		this.priJobCatNm = priJobCatNm;
	}
	public String getSecJobCatCd() {
		return secJobCatCd;
	}
	public void setSecJobCatCd(String secJobCatCd) {
		this.secJobCatCd = secJobCatCd;
	}
	public String getSecJobCatNm() {
		return secJobCatNm;
	}
	public void setSecJobCatNm(String secJobCatNm) {
		this.secJobCatNm = secJobCatNm;
	}
	public String getJobFamilyCd() {
		return jobFamilyCd;
	}
	public void setJobFamilyCd(String jobFamilyCd) {
		this.jobFamilyCd = jobFamilyCd;
	}
	public String getJobFamilyNm() {
		return jobFamilyNm;
	}
	public void setJobFamilyNm(String jobFamilyNm) {
		this.jobFamilyNm = jobFamilyNm;
	}
	public String getIncentiveFlg() {
		return incentiveFlg;
	}
	public void setIncentiveFlg(String incentiveFlg) {
		this.incentiveFlg = incentiveFlg;
	}
	public String getCompGrdCd() {
		return compGrdCd;
	}
	public void setCompGrdCd(String compGrdCd) {
		this.compGrdCd = compGrdCd;
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
	public String getWdJobProCd() {
		return wdJobProCd;
	}
	public void setWdJobProCd(String wdJobProCd) {
		this.wdJobProCd = wdJobProCd;
	}
	public String getWdJobProNm() {
		return wdJobProNm;
	}
	public void setWdJobProNm(String wdJobProNm) {
		this.wdJobProNm = wdJobProNm;
	}
	public String getJobProDesc() {
		return jobProDesc;
	}
	public void setJobProDesc(String jobProDesc) {
		this.jobProDesc = jobProDesc;
	}
	public String getWdJobCdAvailNm() {
		return wdJobCdAvailNm;
	}
	public void setWdJobCdAvailNm(String wdJobCdAvailNm) {
		this.wdJobCdAvailNm = wdJobCdAvailNm;
	}
	public String getOtEligibleDlg() {
		return otEligibleDlg;
	}
	public void setOtEligibleDlg(String otEligibleDlg) {
		this.otEligibleDlg = otEligibleDlg;
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
	public String getJrsDesc() {
		return jrsDesc;
	}
	public void setJrsDesc(String jrsDesc) {
		this.jrsDesc = jrsDesc;
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