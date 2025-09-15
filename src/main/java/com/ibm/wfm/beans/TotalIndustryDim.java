package com.ibm.wfm.beans;



import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="TotalIndustryDim",baseTableName="REFT.TOTAL_INDUSTRY")
public class TotalIndustryDim extends NaryTreeNode {
	@DbColumn(columnName="TOT_IND_ID",isId=true)
	private int        totIndId;
	@DbColumn(columnName="TOT_IND_CD",keySeq=1)
	private String     totIndCd;
	@DbColumn(columnName="TOT_IND_DESC")
	private String     totIndDesc;

	// Define null constructor
	public TotalIndustryDim () {
		
	}
	
	// Define natural key constructor
	public TotalIndustryDim (
      String     totIndCd
	) {
		this.totIndCd                       = totIndCd;
		
	}
	
    
	// Define full constructor
	public TotalIndustryDim (
		  int        totIndId
		, String     totIndCd
		, String     totIndDesc
	) {
		this.totIndId                       = totIndId;
		this.totIndCd                       = totIndCd;
		this.totIndDesc                     = totIndDesc;
		
	}
	
	@Override
	public String getCode() { 
		return this.totIndCd
		;
	}
	public String getDescription() { 
		return this.totIndDesc; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		TotalIndustryDim other = (TotalIndustryDim) obj;
		if (
            this.totIndCd.equals(other.getTotIndCd())
         && this.totIndDesc.equals(other.getTotIndDesc())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.totIndCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.totIndDesc))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("TOT_IND_CD")
        + "," + Helpers.formatCsvField("TOT_IND_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getTotIndId() {
		return totIndId;
	}
	public void setTotIndId(int totIndId) {
		this.totIndId = totIndId;
	}
	public String getTotIndCd() {
		return totIndCd;
	}
	public void setTotIndCd(String totIndCd) {
		this.totIndCd = totIndCd;
	}
	public String getTotIndDesc() {
		return totIndDesc;
	}
	public void setTotIndDesc(String totIndDesc) {
		this.totIndDesc = totIndDesc;
	}
}