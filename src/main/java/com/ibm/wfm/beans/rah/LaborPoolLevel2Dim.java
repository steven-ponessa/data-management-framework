package com.ibm.wfm.beans.rah;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.JoinType;
import com.ibm.wfm.annotations.RelationshipType;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbRelationship;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(
		beanName="LaborPoolLevel2Dim",
		baseTableName="METRICS.LABOR_POOL_LEVEL_2_DIM",
		parentBeanPackageName="com.ibm.wfm.beans.rah",
		parentBeanName="LaborPoolLevel1Dim",
		parentBaseTableName="METRICS.LABOR_POOL_LEVEL_1_DIM",
		useTable=true
		)
@DbRelationship(
	    name = "laborPool1",
	    relationshipType = RelationshipType.PARENT, 
	    targetBeanPackageName = "com.ibm.wfm.beans.rah",
	    targetBeanName = "laborPool1Dim",
	    targetTableName = "METRICS.LABOR_POOL_LEVEL_1_DIM",
	    joinType = JoinType.INNER,
	    joinColumns = {
	        "METRICS.LABOR_POOL_LEVEL_2_DIM.GEOGRAPHY_ID = METRICS.LABOR_POOL_LEVEL_1_DIM.GEOGRAPHY_ID"
	    },
	    joinChildKey = "LBR_POOL_LVL_1_KEY",
	    joinParentKey = "LBR_POOL_LVL_1_KEY",
	    defaultPath = true
	)
@DbRelationship(
	    name = "laborGroup",
	    relationshipType = RelationshipType.PARENT,
	    targetBeanPackageName = "com.ibm.wfm.beans.rah",
	    targetBeanName = "LaborGroupDim",
	    targetTableName = "METRICS.LABOR_GROUP_DIM",
	    joinType = JoinType.INNER,
	    joinColumns = {
	        "METRICS.LABOR_POOL_LEVEL_2_DIM.LBR_GRP_KEY = METRICS.LBR_GRP_KEY.LBR_GRP_KEY"
	    },
	    joinChildKey = "LBR_GRP_KEY",
	    joinParentKey = "LBR_GRP_KEY"    
	)
public class LaborPoolLevel2Dim extends NaryTreeNode implements Comparable<LaborPoolLevel2Dim> {
	@DbColumn(columnName="LBR_POOL_LVL_2_KEY",keySeq=1)
	private int        lbrPoolLvl2Key;
	@DbColumn(columnName="LBR_POOL_LVL_2_NM")
	private String     lbrPoolLvl2Nm;
	@DbColumn(columnName="LBR_POOL_LVL_1_KEY",foreignKeySeq=1)
	private int        lbrPoolLvl1Key;
	@DbColumn(columnName="LBR_POOL_LVL_1_NM")
	private String     lbrPoolLvl1Nm;
	@DbColumn(columnName="LBR_GRP_KEY")
	private int        lbrGrpKey;
	@DbColumn(columnName="LBR_GRP_NM")
	private String     lbrGrpNm;
	@DbColumn(columnName="ACTV_FLG")
	private String     actvFlg;

	// Define null constructor
	public LaborPoolLevel2Dim () {
	}
	
	// Define natural key constructor
	public LaborPoolLevel2Dim (
      int        lbrPoolLvl2Key
	) {
		this.lbrPoolLvl2Key                 = lbrPoolLvl2Key;
	}
	
    
	// Define full constructor
	public LaborPoolLevel2Dim (
		  int        lbrPoolLvl2Key
		, String     lbrPoolLvl2Nm
		, int        lbrPoolLvl1Key
		, String     lbrPoolLvl1Nm
		, int        lbrGrpKey
		, String     lbrGrpNm
		, String     actvFlg
	) {
		this.lbrPoolLvl2Key                 = lbrPoolLvl2Key;
		this.lbrPoolLvl2Nm                  = lbrPoolLvl2Nm;
		this.lbrPoolLvl1Key                 = lbrPoolLvl1Key;
		this.lbrPoolLvl1Nm                  = lbrPoolLvl1Nm;
		this.lbrGrpKey                      = lbrGrpKey;
		this.lbrGrpNm                       = lbrGrpNm;
		this.actvFlg                        = actvFlg;
		this.level                          = 29;
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.lbrPoolLvl2Key)
		;
	}
	public String getDescription() { 
		return this.lbrPoolLvl2Nm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		LaborPoolLevel2Dim other = (LaborPoolLevel2Dim) obj;
		if (
            this.lbrPoolLvl2Key == other.getLbrPoolLvl2Key()
         && this.lbrPoolLvl2Nm.equals(other.getLbrPoolLvl2Nm())
		) return true;
		return false;
	}	
	
	@Override
	public int compareTo(LaborPoolLevel2Dim o) {
		return ((Integer)this.getLbrPoolLvl2Key()).compareTo((Integer)(o.getLbrPoolLvl2Key()));
	}
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.lbrPoolLvl2Key))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrPoolLvl2Nm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrPoolLvl1Key))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrPoolLvl1Nm))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrGrpKey))
        + "," + Helpers.formatCsvField(String.valueOf(this.lbrGrpNm))
        + "," + Helpers.formatCsvField(String.valueOf(this.actvFlg))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("LBR_POOL_LVL_2_KEY")
        + "," + Helpers.formatCsvField("LBR_POOL_LVL_2_NM")
        + "," + Helpers.formatCsvField("LBR_POOL_LVL_1_KEY")
        + "," + Helpers.formatCsvField("LBR_POOL_LVL_1_NM")
        + "," + Helpers.formatCsvField("LBR_GRP_KEY")
        + "," + Helpers.formatCsvField("LBR_GRP_NM")
        + "," + Helpers.formatCsvField("ACTV_FLG")
		;
	}
    
	// Define Getters and Setters
	public int getLbrPoolLvl2Key() {
		return lbrPoolLvl2Key;
	}
	public void setLbrPoolLvl2Key(int lbrPoolLvl2Key) {
		this.lbrPoolLvl2Key = lbrPoolLvl2Key;
	}
	public String getLbrPoolLvl2Nm() {
		return lbrPoolLvl2Nm;
	}
	public void setLbrPoolLvl2Nm(String lbrPoolLvl2Nm) {
		this.lbrPoolLvl2Nm = lbrPoolLvl2Nm;
	}
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