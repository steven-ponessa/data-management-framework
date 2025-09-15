package com.ibm.wfm.beans.prom;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OpnsetPosCandStatTDim",baseTableName="BCSPMPC.OPNSET_POS_CAND_STAT_T",isDimension=false,parentBeanName="OpnsetPosCandTDim",parentBaseTableName="BCSPMPC.OPNSET_POS_CAND_T")
public class OpnsetPosCandStatTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="OPNSET_POS_CAND_STAT_ID",keySeq=1)
	private int        opnsetPosCandStatId;
	@DbColumn(columnName="OPNSET_POS_CAND_ID",foreignKeySeq=1)
	private int        opnsetPosCandId;
	@DbColumn(columnName="CAND_OPNSET_STAT_ID")
	private int        candOpnsetStatId;
	@DbColumn(columnName="CAND_OPNSET_STAT_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  candOpnsetStatT;
	@DbColumn(columnName="GR_OPNSET_POS_ID")
	private int        grOpnsetPosId;
	@DbColumn(columnName="ASGNMT_ID")
	private int        asgnmtId;
	@DbColumn(columnName="CRE_USR_ID")
	private int        creUsrId;
	@DbColumn(columnName="LST_MOD_SYS_EVNT_ID")
	private int        lstModSysEvntId;
	@DbColumn(columnName="GEO_OPNSET_POS_ID")
	private int        geoOpnsetPosId;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OpnsetPosCandStatTDim () {
		
	}
	
	// Define base constructor
	public OpnsetPosCandStatTDim (
      int        opnsetPosCandStatId
	) {
		this.opnsetPosCandStatId            = opnsetPosCandStatId;
		
	}
    
	// Define full constructor
	public OpnsetPosCandStatTDim (
		  int        recordId
		, int        opnsetPosCandStatId
		, int        opnsetPosCandId
		, int        candOpnsetStatId
		, Timestamp  candOpnsetStatT
		, int        grOpnsetPosId
		, int        asgnmtId
		, int        creUsrId
		, int        lstModSysEvntId
		, int        geoOpnsetPosId
	) {
		this.recordId                       = recordId;
		this.opnsetPosCandStatId            = opnsetPosCandStatId;
		this.opnsetPosCandId                = opnsetPosCandId;
		this.candOpnsetStatId               = candOpnsetStatId;
		this.candOpnsetStatT                = candOpnsetStatT;
		this.grOpnsetPosId                  = grOpnsetPosId;
		this.asgnmtId                       = asgnmtId;
		this.creUsrId                       = creUsrId;
		this.lstModSysEvntId                = lstModSysEvntId;
		this.geoOpnsetPosId                 = geoOpnsetPosId;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.opnsetPosCandStatId)
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
                    
		OpnsetPosCandStatTDim other = (OpnsetPosCandStatTDim) obj;
		if (
            this.opnsetPosCandStatId==other.getOpnsetPosCandStatId()
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.opnsetPosCandStatId))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OPNSET_POS_CAND_STAT_ID")
		;
	}
    
	// Define Getters and Setters
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getOpnsetPosCandStatId() {
		return opnsetPosCandStatId;
	}
	public void setOpnsetPosCandStatId(int opnsetPosCandStatId) {
		this.opnsetPosCandStatId = opnsetPosCandStatId;
	}
	public int getOpnsetPosCandId() {
		return opnsetPosCandId;
	}
	public void setOpnsetPosCandId(int opnsetPosCandId) {
		this.opnsetPosCandId = opnsetPosCandId;
	}
	public int getCandOpnsetStatId() {
		return candOpnsetStatId;
	}
	public void setCandOpnsetStatId(int candOpnsetStatId) {
		this.candOpnsetStatId = candOpnsetStatId;
	}
	public Timestamp getCandOpnsetStatT() {
		return candOpnsetStatT;
	}
	public void setCandOpnsetStatT(Timestamp candOpnsetStatT) {
		this.candOpnsetStatT = candOpnsetStatT;
	}
	public int getGrOpnsetPosId() {
		return grOpnsetPosId;
	}
	public void setGrOpnsetPosId(int grOpnsetPosId) {
		this.grOpnsetPosId = grOpnsetPosId;
	}
	public int getAsgnmtId() {
		return asgnmtId;
	}
	public void setAsgnmtId(int asgnmtId) {
		this.asgnmtId = asgnmtId;
	}
	public int getCreUsrId() {
		return creUsrId;
	}
	public void setCreUsrId(int creUsrId) {
		this.creUsrId = creUsrId;
	}
	public int getLstModSysEvntId() {
		return lstModSysEvntId;
	}
	public void setLstModSysEvntId(int lstModSysEvntId) {
		this.lstModSysEvntId = lstModSysEvntId;
	}
	public int getGeoOpnsetPosId() {
		return geoOpnsetPosId;
	}
	public void setGeoOpnsetPosId(int geoOpnsetPosId) {
		this.geoOpnsetPosId = geoOpnsetPosId;
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