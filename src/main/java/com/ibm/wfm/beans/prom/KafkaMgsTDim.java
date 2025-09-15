package com.ibm.wfm.beans.prom;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="KafkaMgsTDim",baseTableName="CERTINIA.KAFKA_MSG_T",isDimension=false,useTable=true)
public class KafkaMgsTDim {
	@DbColumn(columnName="REFERENCE_TX_ID")
	private String     referenceTxId;
	@DbColumn(columnName="TX_TYPE_CD")
	private String     txTypeCd;
	@DbColumn(columnName="CRE_OBJ_ID")
	private Long       creObjId;
	@DbColumn(columnName="CRE_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  creT;
	@DbColumn(columnName="LST_UPDT_T")
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp  lstUpdtT;
    
	// Null Constructor
	public KafkaMgsTDim() {
	}
	
	// Define full constructor
	public KafkaMgsTDim (
		  String     referenceTxId
		, String     txTypeCd
		, Long       creObjId
		, Timestamp  creT
		, Timestamp  lstUpdtT
	) {
		this.referenceTxId                  = referenceTxId;
		this.txTypeCd                       = txTypeCd;
		this.creObjId                       = creObjId;
		this.creT                           = creT;
		this.lstUpdtT                       = lstUpdtT;
		
	}

	// Define Getters and Setters
	public String getReferenceTxId() {
		return referenceTxId;
	}
	public void setReferenceTxId(String referenceTxId) {
		this.referenceTxId = referenceTxId;
	}
	public String getTxTypeCd() {
		return txTypeCd;
	}
	public void setTxTypeCd(String txTypeCd) {
		this.txTypeCd = txTypeCd;
	}
	public Long getCreObjId() {
		return creObjId;
	}
	public void setCreObjId(Long creObjId) {
		this.creObjId = creObjId;
	}
	public Timestamp getCreT() {
		return creT;
	}
	public void setCreT(Timestamp creT) {
		this.creT = creT;
	}
	public Timestamp getLstUpdtT() {
		return lstUpdtT;
	}
	public void setLstUpdtT(Timestamp lstUpdtT) {
		this.lstUpdtT = lstUpdtT;
	}
}