package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="WdJobProfileJrsAssocDim",baseTableName="REFT.WD_JOB_PROFILE_JRS_ASSOC",parentBeanName="WdJobProfileDim",parentBaseTableName="REFT.WD_JOB_PROFILE")
public class WdJobProfileJrsAssocDim extends NaryTreeNode {
	@DbColumn(columnName="WD_JOB_PROFILE_JRS_ASSOC_ID",isId=true)
	private int        wdJobProfileJrsAssocId;
	@DbColumn(columnName="WD_JOB_PRO_CD",keySeq=1,foreignKeySeq=1)
	private String     wdJobProCd;
	@DbColumn(columnName="COMP_GRD_CD",keySeq=2)
	private String     compGrdCd;
	@DbColumn(columnName="JRS_CD",keySeq=3)
	private String     jrsCd;
	@DbColumn(columnName="JOB_FAMILY_CD")
	private String     jobFamilyCd;
	@DbColumn(columnName="INCENTIVE_FLG")
	private String     incentiveFlg;
	@DbColumn(columnName="OT_ELIGIBLE_DLG")
	private String     otEligibleDlg;
	@DbColumn(columnName="JOB_FAMILY_NM")
	private String     jobFamilyNm;
	@DbColumn(columnName="JRS_NM")
	private String     jrsNm;

	// Define null constructor
	public WdJobProfileJrsAssocDim () {
		
	}
	
	// Define natural key constructor
	public WdJobProfileJrsAssocDim (
      String     wdJobProCd
    , String     compGrdCd
    , String     jrsCd
	) {
		this.wdJobProCd                     = wdJobProCd;
		this.compGrdCd                      = compGrdCd;
		this.jrsCd                          = jrsCd;
		
	}
	
	// Define base constructor table
	public WdJobProfileJrsAssocDim (
      String     wdJobProCd
    , String     compGrdCd
    , String     jrsCd
    , String     jobFamilyCd
    , String     incentiveFlg
    , String     otEligibleDlg
	) {
		this.wdJobProCd                     = wdJobProCd;
		this.compGrdCd                      = compGrdCd;
		this.jrsCd                          = jrsCd;
		this.jobFamilyCd                    = jobFamilyCd;
		this.incentiveFlg                   = incentiveFlg;
		this.otEligibleDlg                  = otEligibleDlg;
		
	}
	
	// Define base constructor view
	public WdJobProfileJrsAssocDim (
      String     wdJobProCd
    , String     compGrdCd
    , String     jrsCd
    , String     jobFamilyCd
    , String     incentiveFlg
    , String     otEligibleDlg
	, String     jobFamilyNm
	, String     jrsNm
	) {
		this.wdJobProCd                     = wdJobProCd;
		this.compGrdCd                      = compGrdCd;
		this.jrsCd                          = jrsCd;
		this.jobFamilyCd                    = jobFamilyCd;
		this.incentiveFlg                   = incentiveFlg;
		this.otEligibleDlg                  = otEligibleDlg;
		this.jobFamilyNm                    = jobFamilyNm;
		this.jrsNm							= jrsNm;
		
	}
    
	// Define full constructor for table
	public WdJobProfileJrsAssocDim (
		  int        wdJobProfileJrsAssocId
		, String     wdJobProCd
		, String     compGrdCd
		, String     jrsCd
		, String     jobFamilyCd
		, String     incentiveFlg
		, String     otEligibleDlg
	) {
		this.wdJobProfileJrsAssocId         = wdJobProfileJrsAssocId;
		this.wdJobProCd                     = wdJobProCd;
		this.compGrdCd                      = compGrdCd;
		this.jrsCd                          = jrsCd;
		this.jobFamilyCd                    = jobFamilyCd;
		this.incentiveFlg                   = incentiveFlg;
		this.otEligibleDlg                  = otEligibleDlg;
		
	}
	
	// Define full constructor for view
	public WdJobProfileJrsAssocDim (
		  int        wdJobProfileJrsAssocId
		, String     wdJobProCd
		, String     compGrdCd
		, String     jrsCd
		, String     jobFamilyCd
		, String     incentiveFlg
		, String     otEligibleDlg
		, String     jobFamilyNm
		, String     jrsNm
	) {
		this.wdJobProfileJrsAssocId         = wdJobProfileJrsAssocId;
		this.wdJobProCd                     = wdJobProCd;
		this.compGrdCd                      = compGrdCd;
		this.jrsCd                          = jrsCd;
		this.jobFamilyCd                    = jobFamilyCd;
		this.incentiveFlg                   = incentiveFlg;
		this.otEligibleDlg                  = otEligibleDlg;
		this.jobFamilyNm                    = jobFamilyNm;
		this.jrsNm							= jrsNm;
	}
	
	@Override
	public String getCode() { 
		return this.wdJobProCd
    +":"+ this.compGrdCd
    +":"+ this.jrsCd
		;
	}
	public String getDescription() { 
		return this.wdJobProCd
			    +":"+ this.compGrdCd
			    +":"+ this.jrsCd
		;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		WdJobProfileJrsAssocDim other = (WdJobProfileJrsAssocDim) obj;
		if (
            this.wdJobProCd.equals(other.getWdJobProCd())
         && this.compGrdCd.equals(other.getCompGrdCd())
         && this.jrsCd.equals(other.getJrsCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.wdJobProCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.compGrdCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jrsCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.jobFamilyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.incentiveFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.otEligibleDlg))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("WD_JOB_PRO_CD")
        + "," + Helpers.formatCsvField("COMP_GRD_CD")
        + "," + Helpers.formatCsvField("JRS_CD")
        + "," + Helpers.formatCsvField("JOB_FAMILY_CD")
        + "," + Helpers.formatCsvField("INCENTIVE_FLG")
        + "," + Helpers.formatCsvField("OT_ELIGIBLE_DLG")
		;
	}
    
	// Define Getters and Setters
	public int getWdJobProfileJrsAssocId() {
		return wdJobProfileJrsAssocId;
	}
	public void setWdJobProfileJrsAssocId(int wdJobProfileJrsAssocId) {
		this.wdJobProfileJrsAssocId = wdJobProfileJrsAssocId;
	}
	public String getWdJobProCd() {
		return wdJobProCd;
	}
	public void setWdJobProCd(String wdJobProCd) {
		this.wdJobProCd = wdJobProCd;
	}
	public String getCompGrdCd() {
		return compGrdCd;
	}
	public void setCompGrdCd(String compGrdCd) {
		this.compGrdCd = compGrdCd;
	}
	public String getJrsCd() {
		return jrsCd;
	}
	public void setJrsCd(String jrsCd) {
		this.jrsCd = jrsCd;
	}
	public String getJobFamilyCd() {
		return jobFamilyCd;
	}
	public void setJobFamilyCd(String jobFamilyCd) {
		this.jobFamilyCd = jobFamilyCd;
	}
	public String getIncentiveFlg() {
		return incentiveFlg;
	}
	public void setIncentiveFlg(String incentiveFlg) {
		this.incentiveFlg = incentiveFlg;
	}
	public String getOtEligibleDlg() {
		return otEligibleDlg;
	}
	public void setOtEligibleDlg(String otEligibleDlg) {
		this.otEligibleDlg = otEligibleDlg;
	}

	public String getJobFamilyNm() {
		return jobFamilyNm;
	}

	public void setJobFamilyNm(String jobFamilyNm) {
		this.jobFamilyNm = jobFamilyNm;
	}

	public String getJrsNm() {
		return jrsNm;
	}

	public void setJrsNm(String jrsNm) {
		this.jrsNm = jrsNm;
	}
}