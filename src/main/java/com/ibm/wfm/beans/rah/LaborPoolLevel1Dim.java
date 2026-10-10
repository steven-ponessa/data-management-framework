package com.ibm.wfm.beans.rah;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="LaborPoolLevel1Dim",baseTableName="METRICS.LABOR_POOL_LEVEL_1_DIM",parentBeanPackageName="com.ibm.wfm.beans.rah",parentBeanName="LaborTypeDim",parentBaseTableName="METRICS.LABOR_TYPE_DIM",useTable=true)
public class LaborPoolLevel1Dim extends NaryTreeNode implements Comparable<LaborPoolLevel1Dim>{
	@DbColumn(columnName="LBR_POOL_LVL_1_KEY",keySeq=1)
	private int        lbrPoolLvl1Key;
	@DbColumn(columnName="LBR_POOL_LVL_1_NM")
	private String     lbrPoolLvl1Nm;
	@DbColumn(columnName="DSHB_LBR_POOL_NM")
	private String     dshbLbrPoolNm;
	@DbColumn(columnName="ACTV_FLG")
	private String     actvFlg;
	@DbColumn(columnName="DSHB_LBR_POOL_SHRT_NM")
	private String     dshbLbrPoolShrtNm;
	@DbColumn(columnName="LBR_TYP_KEY",foreignKeySeq=1)
	private int        lbrTypKey;
	@DbColumn(columnName="LBR_TYP_NM")
	private String     lbrTypNm;
	@DbColumn(columnName="COST_RANK_NUM")
	private Short      costRankNum;

	// Define null constructor
	public LaborPoolLevel1Dim () {
	}
	
	// Define natural key constructor
	public LaborPoolLevel1Dim (
      int        lbrPoolLvl1Key
	) {
		this.lbrPoolLvl1Key                 = lbrPoolLvl1Key;
	}
	
	@Override
	public int compareTo(LaborPoolLevel1Dim o) {
		return ((Integer)this.getLbrPoolLvl1Key()).compareTo((Integer)(o.getLbrPoolLvl1Key()));
	}
    
	// Define full constructor
	public LaborPoolLevel1Dim (
		  int        lbrPoolLvl1Key
		, String     lbrPoolLvl1Nm
		, String     dshbLbrPoolNm
		, String     actvFlg
		, String     dshbLbrPoolShrtNm
		, int        lbrTypKey
		, String     lbrTypNm
		, Short      costRankNum
	) {
		this.lbrPoolLvl1Key                 = lbrPoolLvl1Key;
		this.lbrPoolLvl1Nm                  = lbrPoolLvl1Nm;
		this.dshbLbrPoolNm                  = dshbLbrPoolNm;
		this.actvFlg                        = actvFlg;
		this.dshbLbrPoolShrtNm              = dshbLbrPoolShrtNm;
		this.lbrTypKey                      = lbrTypKey;
		this.lbrTypNm                       = lbrTypNm;
		this.costRankNum                    = costRankNum;
		this.level                          = 19;
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.lbrPoolLvl1Key)
		;
	}
	public String getDescription() { 
		return this.lbrPoolLvl1Nm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		LaborPoolLevel1Dim other = (LaborPoolLevel1Dim) obj;
		if (
            this.lbrPoolLvl1Key == other.getLbrPoolLvl1Key()
         && this.lbrPoolLvl1Nm.equals(other.getLbrPoolLvl1Nm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.lbrPoolLvl1Key))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrPoolLvl1Nm))
        + "," + Helpers.formatCsvField(String.valueOf(this.dshbLbrPoolNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.actvFlg))
        + "," + Helpers.formatCsvField(String.valueOf(this.dshbLbrPoolShrtNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrTypKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrTypNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.costRankNum))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("LBR_POOL_LVL_1_KEY")
        + "," + Helpers.formatCsvField("LBR_POOL_LVL_1_NM")
        + "," + Helpers.formatCsvField("DSHB_LBR_POOL_NM")
        + "," + Helpers.formatCsvField("ACTV_FLG")
        + "," + Helpers.formatCsvField("DSHB_LBR_POOL_SHRT_NM")
        + "," + Helpers.formatCsvField("LBR_TYP_KEY")
        + "," + Helpers.formatCsvField("LBR_TYP_NM")
        + "," + Helpers.formatCsvField("COST_RANK_NUM")
		;
	}
    
	// Define Getters and Setters
	public int getLbrPoolLvl1Key() {
		return lbrPoolLvl1Key;
	}
	public void setLbrPoolLvl1Key(int lbrPoolLvl1Key) {
		this.lbrPoolLvl1Key = lbrPoolLvl1Key;
	}
	public String getLbrPoolLvl1Nm() {
		return lbrPoolLvl1Nm;
	}
	public void setLbrPoolLvl1Nm(String lbrPoolLvl1Nm) {
		this.lbrPoolLvl1Nm = lbrPoolLvl1Nm;
	}
	public String getDshbLbrPoolNm() {
		return dshbLbrPoolNm;
	}
	public void setDshbLbrPoolNm(String dshbLbrPoolNm) {
		this.dshbLbrPoolNm = dshbLbrPoolNm;
	}
	public String getActvFlg() {
		return actvFlg;
	}
	public void setActvFlg(String actvFlg) {
		this.actvFlg = actvFlg;
	}
	public String getDshbLbrPoolShrtNm() {
		return dshbLbrPoolShrtNm;
	}
	public void setDshbLbrPoolShrtNm(String dshbLbrPoolShrtNm) {
		this.dshbLbrPoolShrtNm = dshbLbrPoolShrtNm;
	}
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
	public Short getCostRankNum() {
		return costRankNum;
	}
	public void setCostRankNum(Short costRankNum) {
		this.costRankNum = costRankNum;
	}
}