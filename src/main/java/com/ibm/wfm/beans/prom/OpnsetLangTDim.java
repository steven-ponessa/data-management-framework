package com.ibm.wfm.beans.prom;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="OpnsetLangTDim",baseTableName="BCSPMPC.OPNSET_LANG_T",isDimension=false,parentBeanPackageName="com.ibm.wfm.beans.prom",parentBeanName="OpnsetTDim",parentBaseTableName="BCSPMPC.OPNSET_T")
public class OpnsetLangTDim extends NaryTreeNode {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="OPNSET_ID",keySeq=1,foreignKeySeq=1)
	private Long     opnsetId;
	@DbColumn(columnName="LANG_CD",keySeq=2)
	private String     langCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public OpnsetLangTDim () {
		
	}
	
	// Define natural key constructor
	public OpnsetLangTDim (
      Long     opnsetId
    , String     langCd
	) {
		this.opnsetId                       = opnsetId;
		this.langCd                         = langCd;
		
	}
    
	// Define full constructor
	public OpnsetLangTDim (
		  int        recordId
		, Long     opnsetId
		, String     langCd
	) {
		this.recordId                       = recordId;
		this.opnsetId                       = opnsetId;
		this.langCd                         = langCd;
		
	}
	
	@Override
	public String getCode() { 
		return this.opnsetId
    +":"+ this.langCd
		;
	}
	public String getDescription() { 
		return this.opnsetId
			    +":"+ this.langCd; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		OpnsetLangTDim other = (OpnsetLangTDim) obj;
		if (
            this.opnsetId.equals(other.getOpnsetId())
         && this.langCd.equals(other.getLangCd())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.opnsetId))
        + "," + Helpers.formatCsvField(String.valueOf(this.langCd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("OPNSET_ID")
        + "," + Helpers.formatCsvField("LANG_CD")
		;
	}
    
	// Define Getters and Setters
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public Long getOpnsetId() {
		return opnsetId;
	}
	public void setOpnsetId(Long opnsetId) {
		this.opnsetId = opnsetId;
	}
	public String getLangCd() {
		return langCd;
	}
	public void setLangCd(String langCd) {
		this.langCd = langCd;
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