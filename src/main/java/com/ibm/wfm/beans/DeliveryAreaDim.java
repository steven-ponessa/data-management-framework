package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="DeliveryAreaDim",baseTableName="REFT.DELIVERY_AREA")
public class DeliveryAreaDim extends NaryTreeNode {
	@DbColumn(columnName="DLVRY_AREA_ID",isId=true)
	private int        dlvryAreaId;
	@DbColumn(columnName="DLVRY_AREA_CD",keySeq=1)
	private String     dlvryAreaCd;
	@DbColumn(columnName="DLVRY_AREA_NM")
	private String     dlvryAreaNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public DeliveryAreaDim () {
				this.level = 0;
	}
	
	// Define natural key constructor
	public DeliveryAreaDim (
      String     dlvryAreaCd
	) {
		this.dlvryAreaCd                    = dlvryAreaCd;
		this.level                          = 0;
	}
	
	// Define base constructor
	public DeliveryAreaDim (
      String     dlvryAreaCd
    , String     dlvryAreaNm
	) {
		this.dlvryAreaCd                    = dlvryAreaCd;
		this.dlvryAreaNm                    = dlvryAreaNm;
		this.level                          = 0;
	}
    
	// Define full constructor
	public DeliveryAreaDim (
		  int        dlvryAreaId
		, String     dlvryAreaCd
		, String     dlvryAreaNm
	) {
		this.dlvryAreaId                    = dlvryAreaId;
		this.dlvryAreaCd                    = dlvryAreaCd;
		this.dlvryAreaNm                    = dlvryAreaNm;
		this.level                          = 0;
	}
	
	@Override
	public String getCode() { 
		return this.dlvryAreaCd
		;
	}
	public String getDescription() { 
		return dlvryAreaNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		DeliveryAreaDim other = (DeliveryAreaDim) obj;
		if (
            this.dlvryAreaCd.equals(other.getDlvryAreaCd())
         && this.dlvryAreaNm.equals(other.getDlvryAreaNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.dlvryAreaCd))
        + "," + Helpers.formatCsvField(String.valueOf(this.dlvryAreaNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("DLVRY_AREA_CD")
        + "," + Helpers.formatCsvField("DLVRY_AREA_NM")
		;
	}
    
	// Define Getters and Setters
	public int getDlvryAreaId() {
		return dlvryAreaId;
	}
	public void setDlvryAreaId(int dlvryAreaId) {
		this.dlvryAreaId = dlvryAreaId;
	}
	public String getDlvryAreaCd() {
		return dlvryAreaCd;
	}
	public void setDlvryAreaCd(String dlvryAreaCd) {
		this.dlvryAreaCd = dlvryAreaCd;
	}
	public String getDlvryAreaNm() {
		return dlvryAreaNm;
	}
	public void setDlvryAreaNm(String dlvryAreaNm) {
		this.dlvryAreaNm = dlvryAreaNm;
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