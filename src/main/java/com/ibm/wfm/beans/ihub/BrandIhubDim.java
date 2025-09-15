package com.ibm.wfm.beans.ihub;


import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.utils.Helpers;

@Repository
@DbTable(beanName="BrandIhubDim",baseTableName="REFT.BRAND")
public class BrandIhubDim extends NaryTreeNode implements Comparable<BrandIhubDim> {

	@DbColumn(columnName="BRAND_ID",isId=true)
	private int        brandId;
	@DbColumn(columnName="BRAND_CD",keySeq=1)
	private String     brandCd;
	@DbColumn(columnName="BRAND_NM")
	private String     brandNm;
	@DbColumn(columnName="BRAND_DESC")
	private String     brandDesc;
	@DbColumn(columnName="LGCY_BRAND_CD",isExtension=true)
	private String     lgcyBrandCd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public BrandIhubDim () {
		this.level = 0;
	}
	
	// Define base constructor
	public BrandIhubDim (
      String     brandCd
	) {
		this.setLevel(0);
		this.brandCd                        = brandCd;
	}
	
	// Define base constructor
	public BrandIhubDim (
      String     brandCd
    , String     brandNm
    , String     brandDesc
	) {
		super(brandCd, brandNm);
		this.setLevel(0);
		this.brandCd                        = brandCd;
		this.brandNm                        = brandNm;
		this.brandDesc                      = brandDesc;
	}
    
	// Define full constructor
	public BrandIhubDim (
		  int        brandId
		, String     brandCd
		, String     brandNm
		, String     brandDesc
		, String     lgcyBrandCd
	) {
		super(brandCd, brandNm);
		this.setLevel(0);
		this.brandId                        = brandId;
		this.brandCd                        = brandCd;
		this.brandNm                        = brandNm;
		this.brandDesc                      = brandDesc;
		this.lgcyBrandCd                    = lgcyBrandCd;
	}
	
	@Override
	public String getCode() { return this.brandCd; }
	public String getDescription() { return this.brandNm; }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		BrandIhubDim other = (BrandIhubDim) obj;
		if (
            this.brandCd.equals(other.getBrandCd())
         && this.brandNm.equals(other.getBrandNm())
		) return true;
		return false;
	}	
	
    @Override
    public int compareTo(BrandIhubDim o) {
        return this.getBrandCd().compareTo(o.getBrandCd());
    }
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(this.brandCd)
        + "," + Helpers.formatCsvField(this.brandNm)
        + "," + Helpers.formatCsvField(this.brandDesc)
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("BRAND_CD")
        + "," + Helpers.formatCsvField("BRAND_NM")
        + "," + Helpers.formatCsvField("BRAND_DESC")
		;
	}
    
	// Define Getters and Setters
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public String getBrandCd() {
		return brandCd;
	}
	public void setBrandCd(String brandCd) {
		this.brandCd = brandCd;
	}
	public String getBrandNm() {
		return brandNm;
	}
	public void setBrandNm(String brandNm) {
		this.brandNm = brandNm;
	}
	public String getBrandDesc() {
		return brandDesc;
	}
	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
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

	public String getLgcyBrandCd() {
		return lgcyBrandCd;
	}

	public void setLgcyBrandCd(String lgcyBrandCd) {
		this.lgcyBrandCd = lgcyBrandCd;
	}
	@Override
	public String toString() {
		return "BrandDim [brandId=" + brandId + ", brandCd=" + brandCd + ", brandNm=" + brandNm + ", brandDesc="
				+ brandDesc + ", lgcyBrandCd=" + lgcyBrandCd + ", effTms=" + effTms + ", expirTms=" + expirTms
				+ ", rowStatusCd=" + rowStatusCd + "]";
	}
}