package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="WdJobProfileDim",baseTableName="REFT.WD_JOB_PROFILE",parentBeanName="SecondaryJobCategoryDim",parentBaseTableName="REFT.SECONDARY_JOB_CATEGORY")
public class WdJobProfileDim extends NaryTreeNode {
	@DbColumn(columnName="WD_JOB_PRO_CAT_ID",isId=true)
	private int        wdJobProCatId;
	@DbColumn(columnName="WD_JOB_PRO_CD",keySeq=1)
	private String     wdJobProCd;
	@DbColumn(columnName="WD_JOB_PRO_NM")
	private String     wdJobProNm;
	@DbColumn(columnName="WD_JOB_PRO_DESC")
	private String     wdJobProDesc;
	@DbColumn(columnName="SEC_JOB_CAT_CD",foreignKeySeq=1)
	private String     secJobCatCd;

	// Define null constructor
	public WdJobProfileDim () {
		
	}
	
	// Define natural key constructor
	public WdJobProfileDim (
      String     wdJobProCd
	) {
		this.wdJobProCd                     = wdJobProCd;
		
	}
	
	// Define base constructor
	public WdJobProfileDim (
      String     wdJobProCd
    , String     wdJobProNm
    , String     wdJobProDesc
    , String     secJobCatCd
	) {
		this.wdJobProCd                     = wdJobProCd;
		this.wdJobProNm                     = wdJobProNm;
		this.wdJobProDesc                   = wdJobProDesc;
		this.secJobCatCd                    = secJobCatCd;
		
	}
    
	// Define full constructor
	public WdJobProfileDim (
		  int        wdJobProCatId
		, String     wdJobProCd
		, String     wdJobProNm
		, String     wdJobProDesc
		, String     secJobCatCd
	) {
		this.wdJobProCatId                  = wdJobProCatId;
		this.wdJobProCd                     = wdJobProCd;
		this.wdJobProNm                     = wdJobProNm;
		this.wdJobProDesc                   = wdJobProDesc;
		this.secJobCatCd                    = secJobCatCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.wdJobProCd
		;
	}
	public String getDescription() { 
		return this.wdJobProNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		WdJobProfileDim other = (WdJobProfileDim) obj;
		if (
            this.wdJobProCd.equals(other.getWdJobProCd())
         && this.wdJobProNm.equals(other.getWdJobProNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.wdJobProCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wdJobProNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.wdJobProDesc))
        + "," + Helpers.formatCsvField(String.valueOf(this.secJobCatCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("WD_JOB_PRO_CD")
        + "," + Helpers.formatCsvField("WD_JOB_PRO_NM")
        + "," + Helpers.formatCsvField("WD_JOB_PRO_DESC")
        + "," + Helpers.formatCsvField("SEC_JOB_CAT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getWdJobProCatId() {
		return wdJobProCatId;
	}
	public void setWdJobProCatId(int wdJobProCatId) {
		this.wdJobProCatId = wdJobProCatId;
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
	public String getWdJobProDesc() {
		return wdJobProDesc;
	}
	public void setWdJobProDesc(String wdJobProDesc) {
		this.wdJobProDesc = wdJobProDesc;
	}
	public String getSecJobCatCd() {
		return secJobCatCd;
	}
	public void setSecJobCatCd(String secJobCatCd) {
		this.secJobCatCd = secJobCatCd;
	}
}