package com.ibm.wfm.beans.rah;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="LaborGroupDim",baseTableName="METRICS.LABOR_GROUP_DIM",useTable=true)
public class LaborGroupDim extends NaryTreeNode {
	@DbColumn(columnName="LBR_GRP_KEY",keySeq=1)
	private int        lbrGrpKey;
	@DbColumn(columnName="LBR_GRP_NM")
	private String     lbrGrpNm;
	@DbColumn(columnName="ACTV_FLG")
	private String     actvFlg;

	// Define null constructor
	public LaborGroupDim () {
		
	}
	
	// Define natural key constructor
	public LaborGroupDim (
      int        lbrGrpKey
	) {
		this.lbrGrpKey                      = lbrGrpKey;
		
	}
	
	// Define full constructor
	public LaborGroupDim (
		  int        lbrGrpKey
		, String     lbrGrpNm
		, String     actvFlg
	) {
		this.lbrGrpKey                      = lbrGrpKey;
		this.lbrGrpNm                       = lbrGrpNm;
		this.actvFlg                        = actvFlg;
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.lbrGrpKey)
		;
	}
	public String getDescription() { 
		return this.lbrGrpNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		LaborGroupDim other = (LaborGroupDim) obj;
		if (
            this.lbrGrpKey == other.getLbrGrpKey()
         && this.lbrGrpNm.equals(other.getLbrGrpNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.lbrGrpKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrGrpNm))
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
	public int getLbrGrpKey() {
		return lbrGrpKey;
	}
	public void setLbrGrpKey(int lbrGrpKey) {
		this.lbrGrpKey = lbrGrpKey;
	}
	public String getLbrGrpNm() {
		return lbrGrpNm;
	}
	public void setLbrGrpNm(String lbrGrpNm) {
		this.lbrGrpNm = lbrGrpNm;
	}
	public String getActvFlg() {
		return actvFlg;
	}
	public void setActvFlg(String actvFlg) {
		this.actvFlg = actvFlg;
	}
}