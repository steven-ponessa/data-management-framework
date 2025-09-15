package com.ibm.wfm.beans.prom;


//import java.sql.Date;
import java.time.LocalDate;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="KafkaAvailDtTDim",baseTableName="CERTINIA.KAFKA_AVAIL_DT_T",isDimension=false,useTable=true)
public class KafkaAvailDtTDim  {
	@DbColumn(columnName="RECORD_ID",isId=true)
	private int        recordId;
	@DbColumn(columnName="TALENT_ID",keySeq=1)
	private String     talentId;
	@DbColumn(columnName="AVAIL_DT")
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate       availDt;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public KafkaAvailDtTDim () {
		
	}
	
	// Define natural key constructor
	public KafkaAvailDtTDim (
      String     talentId
	) {
		this.talentId                       = talentId;
		
	}
	
    
	// Define full constructor
	public KafkaAvailDtTDim (
		  int        recordId
		, String     talentId
		, LocalDate  availDt
	) {
		this.recordId                       = recordId;
		this.talentId                       = talentId;
		this.availDt                        = availDt;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		KafkaAvailDtTDim other = (KafkaAvailDtTDim) obj;
		if (
            this.talentId.equals(other.getTalentId())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.talentId))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("TALENT_ID")
		;
	}
    
	// Define Getters and Setters
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getTalentId() {
		return talentId;
	}
	public void setTalentId(String talentId) {
		this.talentId = talentId;
	}
	public LocalDate getAvailDt() {
		return availDt;
	}
	public void setAvailDt(LocalDate availDt) {
		this.availDt = availDt;
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