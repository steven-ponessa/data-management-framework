package com.ibm.wfm.beans;



import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="SecondaryJobCategoryDim",baseTableName="REFT.SECONDARY_JOB_CATEGORY",parentBeanName="PrimaryJobCategoryDim",parentBaseTableName="REFT.PRIMARY_JOB_CATEGORY")
public class SecondaryJobCategoryDim extends NaryTreeNode {
	@DbColumn(columnName="SEC_JOB_CAT_ID",isId=true)
	private int        secJobCatId;
	@DbColumn(columnName="SEC_JOB_CAT_CD",keySeq=1)
	private String     secJobCatCd;
	@DbColumn(columnName="SEC_JOB_CAT_NM")
	private String     secJobCatNm;
	@DbColumn(columnName="PRI_JOB_CAT_CD",foreignKeySeq=1)
	private String     priJobCatCd;

	// Define null constructor
	public SecondaryJobCategoryDim () {
		
	}
	
	// Define natural key constructor
	public SecondaryJobCategoryDim (
      String     secJobCatCd
	) {
		this.secJobCatCd                    = secJobCatCd;
		
	}
	
	// Define base constructor
	public SecondaryJobCategoryDim (
      String     secJobCatCd
    , String     secJobCatNm
    , String     priJobCatCd
	) {
		this.secJobCatCd                    = secJobCatCd;
		this.secJobCatNm                    = secJobCatNm;
		this.priJobCatCd                    = priJobCatCd;
		
	}
    
	// Define full constructor
	public SecondaryJobCategoryDim (
		  int        secJobCatId
		, String     secJobCatCd
		, String     secJobCatNm
		, String     priJobCatCd
	) {
		this.secJobCatId                    = secJobCatId;
		this.secJobCatCd                    = secJobCatCd;
		this.secJobCatNm                    = secJobCatNm;
		this.priJobCatCd                    = priJobCatCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.secJobCatCd
		;
	}
	public String getDescription() { 
		return this.secJobCatNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SecondaryJobCategoryDim other = (SecondaryJobCategoryDim) obj;
		if (
            this.secJobCatCd.equals(other.getSecJobCatCd())
         && this.secJobCatNm.equals(other.getSecJobCatNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.secJobCatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.secJobCatNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.priJobCatCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SEC_JOB_CAT_CD")
        + "," + Helpers.formatCsvField("SEC_JOB_CAT_NM")
        + "," + Helpers.formatCsvField("PRI_JOB_CAT_CD")
		;
	}
    
	// Define Getters and Setters
	public int getSecJobCatId() {
		return secJobCatId;
	}
	public void setSecJobCatId(int secJobCatId) {
		this.secJobCatId = secJobCatId;
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
	public String getPriJobCatCd() {
		return priJobCatCd;
	}
	public void setPriJobCatCd(String priJobCatCd) {
		this.priJobCatCd = priJobCatCd;
	}
}