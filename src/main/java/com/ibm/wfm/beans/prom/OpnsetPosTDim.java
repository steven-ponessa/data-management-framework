package com.ibm.wfm.beans.prom;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OpnsetPosTDim",baseTableName="BCSPMPC.OPNSET_POS_T",isDimension=false,parentBeanPackageName="com.ibm.wfm.beans.prom",parentBeanName="OpnsetTDim",parentBaseTableName="BCSPMPC.OPNSET_T")
public class OpnsetPosTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="OPNSET_POS_ID",keySeq=1)
	private Long     opnsetPosId;
	@DbColumn(columnName="OPNSET_ID",foreignKeySeq=1)
	private Long     opnsetId;
	@DbColumn(columnName="POS_INDX_NUM")
	private Short   posIndxNum;
	@DbColumn(columnName="POS_STAT_CD")
	private String     posStatCd;
	@DbColumn(columnName="STAT_RESN_CD")
	private String     statResnCd;
	@DbColumn(columnName="WTHDRW_FIL_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  wthdrwFilT;
	@DbColumn(columnName="WTHDRW_FIL_USR_ID")
	private Long     wthdrwFilUsrId;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OpnsetPosTDim () {
		
	}
	
	// Define natural key constructor
	public OpnsetPosTDim (
      Long     opnsetPosId
	) {
		this.opnsetPosId                    = opnsetPosId;
		
	}
	
	// Define base constructor
	public OpnsetPosTDim (
      Long     opnsetPosId
    , Long     opnsetId
    , Short   posIndxNum
    , String     posStatCd
    , String     statResnCd
    , Timestamp  wthdrwFilT
    , Long     wthdrwFilUsrId
	) {
		this.opnsetPosId                    = opnsetPosId;
		this.opnsetId                       = opnsetId;
		this.posIndxNum                     = posIndxNum;
		this.posStatCd                      = posStatCd;
		this.statResnCd                     = statResnCd;
		this.wthdrwFilT                     = wthdrwFilT;
		this.wthdrwFilUsrId                 = wthdrwFilUsrId;
		
	}
    
	// Define full constructor
	public OpnsetPosTDim (
		  int        recordId
		, Long     opnsetPosId
		, Long     opnsetId
		, Short   posIndxNum
		, String     posStatCd
		, String     statResnCd
		, Timestamp  wthdrwFilT
		, Long     wthdrwFilUsrId
	) {
		this.recordId                       = recordId;
		this.opnsetPosId                    = opnsetPosId;
		this.opnsetId                       = opnsetId;
		this.posIndxNum                     = posIndxNum;
		this.posStatCd                      = posStatCd;
		this.statResnCd                     = statResnCd;
		this.wthdrwFilT                     = wthdrwFilT;
		this.wthdrwFilUsrId                 = wthdrwFilUsrId;
		
	}
	
	@Override
	public String getCode() { 
		return this.opnsetPosId
    +":"+ this.opnsetId
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
                    
		OpnsetPosTDim other = (OpnsetPosTDim) obj;
		if (
            this.opnsetPosId.equals(other.getOpnsetPosId())
         && this.opnsetId.equals(other.getOpnsetId())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.opnsetPosId))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnsetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.posIndxNum))
        + "," + Helpers.formatCsvField(String.valueOf(this.posStatCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.statResnCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.wthdrwFilT))
        + "," + Helpers.formatCsvField(String.valueOf(this.wthdrwFilUsrId))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OPNSET_POS_ID")
        + "," + Helpers.formatCsvField("OPNSET_ID")
        + "," + Helpers.formatCsvField("POS_INDX_NUM")
        + "," + Helpers.formatCsvField("POS_STAT_CD")
        + "," + Helpers.formatCsvField("STAT_RESN_CD")
        + "," + Helpers.formatCsvField("WTHDRW_FIL_T")
        + "," + Helpers.formatCsvField("WTHDRW_FIL_USR_ID")
		;
	}
    
	// Define Getters and Setters
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public Long getOpnsetPosId() {
		return opnsetPosId;
	}
	public void setOpnsetPosId(Long opnsetPosId) {
		this.opnsetPosId = opnsetPosId;
	}
	public Long getOpnsetId() {
		return opnsetId;
	}
	public void setOpnsetId(Long opnsetId) {
		this.opnsetId = opnsetId;
	}
	public Short getPosIndxNum() {
		return posIndxNum;
	}
	public void setPosIndxNum(Short posIndxNum) {
		this.posIndxNum = posIndxNum;
	}
	public String getPosStatCd() {
		return posStatCd;
	}
	public void setPosStatCd(String posStatCd) {
		this.posStatCd = posStatCd;
	}
	public String getStatResnCd() {
		return statResnCd;
	}
	public void setStatResnCd(String statResnCd) {
		this.statResnCd = statResnCd;
	}
	public Timestamp getWthdrwFilT() {
		return wthdrwFilT;
	}
	public void setWthdrwFilT(Timestamp wthdrwFilT) {
		this.wthdrwFilT = wthdrwFilT;
	}
	public Long getWthdrwFilUsrId() {
		return wthdrwFilUsrId;
	}
	public void setWthdrwFilUsrId(Long wthdrwFilUsrId) {
		this.wthdrwFilUsrId = wthdrwFilUsrId;
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