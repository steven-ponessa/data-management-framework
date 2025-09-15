package com.ibm.wfm.beans;



import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="PrimaryJobCategoryDim",baseTableName="REFT.PRIMARY_JOB_CATEGORY")
public class PrimaryJobCategoryDim extends NaryTreeNode {
	@DbColumn(columnName="PRI_JOB_CAT_ID",isId=true)
	private int        priJobCatId;
	@DbColumn(columnName="PRI_JOB_CAT_CD",keySeq=1)
	private String     priJobCatCd;
	@DbColumn(columnName="PRI_JOB_CAT_NM")
	private String     priJobCatNm;

	// Define null constructor
	public PrimaryJobCategoryDim () {
		
	}
	
	// Define natural key constructor
	public PrimaryJobCategoryDim (
      String     priJobCatCd
	) {
		this.priJobCatCd                    = priJobCatCd;
		
	}
	
	// Define base constructor
	public PrimaryJobCategoryDim (
      String     priJobCatCd
    , String     priJobCatNm
	) {
		this.priJobCatCd                    = priJobCatCd;
		this.priJobCatNm                    = priJobCatNm;
		
	}
    
	// Define full constructor
	public PrimaryJobCategoryDim (
		  int        priJobCatId
		, String     priJobCatCd
		, String     priJobCatNm
	) {
		this.priJobCatId                    = priJobCatId;
		this.priJobCatCd                    = priJobCatCd;
		this.priJobCatNm                    = priJobCatNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.priJobCatCd
		;
	}
	public String getDescription() { 
		return this.priJobCatNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		PrimaryJobCategoryDim other = (PrimaryJobCategoryDim) obj;
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
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("PRI_JOB_CAT_CD")
        + "," + Helpers.formatCsvField("PRI_JOB_CAT_NM")
		;
	}
    
	// Define Getters and Setters
	public int getPriJobCatId() {
		return priJobCatId;
	}
	public void setPriJobCatId(int priJobCatId) {
		this.priJobCatId = priJobCatId;
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
}