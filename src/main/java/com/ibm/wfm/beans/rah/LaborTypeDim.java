package com.ibm.wfm.beans.rah;

import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="LaborTypeDim",baseTableName="METRICS.LABOR_TYPE_DIM",useTable=true)
public class LaborTypeDim extends NaryTreeNode implements Comparable<LaborTypeDim>{
	@DbColumn(columnName="LBR_TYP_KEY",keySeq=1)
	private int        lbrTypKey;
	@DbColumn(columnName="LBR_TYP_NM")
	private String     lbrTypNm;
	@DbColumn(columnName="ACTV_FLG")
	private String     actvFlg;

	// Define null constructor
	public LaborTypeDim () {
		
	}
	
	// Define natural key constructor
	public LaborTypeDim (
      int        lbrTypKey
	) {
		this.lbrTypKey                      = lbrTypKey;
		
	}
	
	// Define full constructor
	public LaborTypeDim (
		  int        lbrTypKey
		, String     lbrTypNm
		, String     actvFlg
	) {
		this.lbrTypKey                      = lbrTypKey;
		this.lbrTypNm                       = lbrTypNm;
		this.actvFlg                        = actvFlg;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.lbrTypKey)
		;
	}
	public String getDescription() { 
		return this.lbrTypNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		LaborTypeDim other = (LaborTypeDim) obj;
		if (
            this.lbrTypKey == other.getLbrTypKey()
         && this.lbrTypNm.equals(other.getLbrTypNm())
		) return true;
		return false;
	}	
	
	@Override
	public int compareTo(LaborTypeDim o) {
		return ((Integer)this.getLbrTypKey()).compareTo((Integer)(o.getLbrTypKey()));
	}
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.lbrTypKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrTypNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.actvFlg))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("LBR_TYP_KEY")
        + "," + Helpers.formatCsvField("LBR_TYP_NM")
        + "," + Helpers.formatCsvField("ACTV_FLG")
		;
	}
    
	// Define Getters and Setters
	public int getLbrTypKey() {
		return lbrTypKey;
	}
	public void setLbrTypKey(int lbrTypKey) {
		this.lbrTypKey = lbrTypKey;
	}
	public String getLbrTypNm() {
		return lbrTypNm;
	}
	public void setLbrTypNm(String lbrTypNm) {
		this.lbrTypNm = lbrTypNm;
	}
	public String getActvFlg() {
		return actvFlg;
	}
	public void setActvFlg(String actvFlg) {
		this.actvFlg = actvFlg;
	}
}