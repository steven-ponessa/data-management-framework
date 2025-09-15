package com.ibm.wfm.beans;


import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

@Repository
@DbTable(beanName="SourcingStrategyDim",baseTableName="REFT.SOURCING_STRATEGY")
public class SourcingStrategyDim {
	@DbColumn(columnName="SRC_STRGY_ID",isId=true)
	private int        srcStrgyId;
	@DbColumn(columnName="SRC_STRGY_CD",keySeq=1)
	private String     srcStrgyCd;
	@DbColumn(columnName="SRC_STRGY_NM")
	private String     srcStrgyNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public SourcingStrategyDim () {}
	
	// Define base constructor
	public SourcingStrategyDim (
      String     srcStrgyCd
	) {
		this.srcStrgyCd                     = srcStrgyCd;
	}
	
	// Define base constructor
	public SourcingStrategyDim (
      String     srcStrgyCd
    , String     srcStrgyNm
	) {
		this.srcStrgyCd                     = srcStrgyCd;
		this.srcStrgyNm                     = srcStrgyNm;
	}
    
	// Define full constructor
	public SourcingStrategyDim (
		  int        srcStrgyId
		, String     srcStrgyCd
		, String     srcStrgyNm
	) {
		this.srcStrgyId                     = srcStrgyId;
		this.srcStrgyCd                     = srcStrgyCd;
		this.srcStrgyNm                     = srcStrgyNm;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		SourcingStrategyDim other = (SourcingStrategyDim) obj;
		if (
            this.srcStrgyCd.equals(other.getSrcStrgyCd())
         && this.srcStrgyNm.equals(other.getSrcStrgyNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.srcStrgyCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.srcStrgyNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("SRC_STRGY_CD")
        + "," + Helpers.formatCsvField("SRC_STRGY_NM")
		;
	}
    
	// Define Getters and Setters
	public int getSrcStrgyId() {
		return srcStrgyId;
	}
	public void setSrcStrgyId(int srcStrgyId) {
		this.srcStrgyId = srcStrgyId;
	}
	public String getSrcStrgyCd() {
		return srcStrgyCd;
	}
	public void setSrcStrgyCd(String srcStrgyCd) {
		this.srcStrgyCd = srcStrgyCd;
	}
	public String getSrcStrgyNm() {
		return srcStrgyNm;
	}
	public void setSrcStrgyNm(String srcStrgyNm) {
		this.srcStrgyNm = srcStrgyNm;
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