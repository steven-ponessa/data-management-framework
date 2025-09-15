package com.ibm.wfm.beans.prom;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OpnsetPosCandTDim",baseTableName="BCSPMPC.OPNSET_POS_CAND_T",isDimension=false, parentBeanName="OpnsetTDim",parentBaseTableName="BCSPMPC.OPNSET_T")
public class OpnsetPosCandTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="OPNSET_POS_CAND_ID",keySeq=1)
	private Long       opnsetPosCandId;
	@DbColumn(columnName="OPNSET_ID",foreignKeySeq=1)
	private Long       opnsetId;
	@DbColumn(columnName="CAND_ID")
	private Long       candId;
	@DbColumn(columnName="OPNSET_OWNR_CMT_TXT")
	private String     opnsetOwnrCmtTxt;
	@DbColumn(columnName="CAND_CMT_TXT")
	private String     candCmtTxt;
	@DbColumn(columnName="RSA_CMT_TXT")
	private String     rsaCmtTxt;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OpnsetPosCandTDim () {
		
	}
	
	// Define natural key constructor
	public OpnsetPosCandTDim (
      Long       opnsetPosCandId
	) {
		this.opnsetPosCandId                = opnsetPosCandId;
		
	}
	
	// Define base constructor
	public OpnsetPosCandTDim (
      Long       opnsetPosCandId
    , Long       opnsetId
    , Long       candId
    , String     opnsetOwnrCmtTxt
    , String     candCmtTxt
    , String     rsaCmtTxt
	) {
		this.opnsetPosCandId                = opnsetPosCandId;
		this.opnsetId                       = opnsetId;
		this.candId                         = candId;
		this.opnsetOwnrCmtTxt               = opnsetOwnrCmtTxt;
		this.candCmtTxt                     = candCmtTxt;
		this.rsaCmtTxt                      = rsaCmtTxt;
		
	}
    
	// Define full constructor
	public OpnsetPosCandTDim (
		  int        recordId
		, Long       opnsetPosCandId
		, Long       opnsetId
		, Long       candId
		, String     opnsetOwnrCmtTxt
		, String     candCmtTxt
		, String     rsaCmtTxt
	) {
		this.recordId                       = recordId;
		this.opnsetPosCandId                = opnsetPosCandId;
		this.opnsetId                       = opnsetId;
		this.candId                         = candId;
		this.opnsetOwnrCmtTxt               = opnsetOwnrCmtTxt;
		this.candCmtTxt                     = candCmtTxt;
		this.rsaCmtTxt                      = rsaCmtTxt;
		
	}
	
	@Override
	public String getCode() { 
		return String.valueOf(this.opnsetPosCandId)
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
                    
		OpnsetPosCandTDim other = (OpnsetPosCandTDim) obj;
		if (
            this.opnsetPosCandId.equals(other.getOpnsetPosCandId())
         && this.opnsetId.equals(other.getOpnsetId())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.opnsetPosCandId))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnsetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.candId))
        + "," + Helpers.formatCsvField(String.valueOf(this.opnsetOwnrCmtTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.candCmtTxt))
        + "," + Helpers.formatCsvField(String.valueOf(this.rsaCmtTxt))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OPNSET_POS_CAND_ID")
        + "," + Helpers.formatCsvField("OPNSET_ID")
        + "," + Helpers.formatCsvField("CAND_ID")
        + "," + Helpers.formatCsvField("OPNSET_OWNR_CMT_TXT")
        + "," + Helpers.formatCsvField("CAND_CMT_TXT")
        + "," + Helpers.formatCsvField("RSA_CMT_TXT")
		;
	}
    
	// Define Getters and Setters
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public Long getOpnsetPosCandId() {
		return opnsetPosCandId;
	}
	public void setOpnsetPosCandId(Long opnsetPosCandId) {
		this.opnsetPosCandId = opnsetPosCandId;
	}
	public Long getOpnsetId() {
		return opnsetId;
	}
	public void setOpnsetId(Long opnsetId) {
		this.opnsetId = opnsetId;
	}
	public Long getCandId() {
		return candId;
	}
	public void setCandId(Long candId) {
		this.candId = candId;
	}
	public String getOpnsetOwnrCmtTxt() {
		return opnsetOwnrCmtTxt;
	}
	public void setOpnsetOwnrCmtTxt(String opnsetOwnrCmtTxt) {
		this.opnsetOwnrCmtTxt = opnsetOwnrCmtTxt;
	}
	public String getCandCmtTxt() {
		return candCmtTxt;
	}
	public void setCandCmtTxt(String candCmtTxt) {
		this.candCmtTxt = candCmtTxt;
	}
	public String getRsaCmtTxt() {
		return rsaCmtTxt;
	}
	public void setRsaCmtTxt(String rsaCmtTxt) {
		this.rsaCmtTxt = rsaCmtTxt;
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