package com.ibm.wfm.beans;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.annotations.DbColumn;

import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="EmployeeTypeDim",baseTableName="REFT.EMPLOYEE_TYPE")
public class EmployeeTypeDim extends NaryTreeNode {
	@DbColumn(columnName="EMPLOYEE_TYP_ID",isId=true)
	private int        employeeTypId;
	@DbColumn(columnName="EMPLOYEE_TYP_CODE",keySeq=1)
	private String     employeeTypCode;
	@DbColumn(columnName="EMPLOYEE_TYP_NM")
	private String     employeeTypNm;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public EmployeeTypeDim () {
		
	}
	
	// Define natural key constructor
	public EmployeeTypeDim (
      String     employeeTypCode
	) {
		this.employeeTypCode                = employeeTypCode;
		
	}
	
	// Define base constructor
	public EmployeeTypeDim (
      String     employeeTypCode
    , String     employeeTypNm
	) {
		this.employeeTypCode                = employeeTypCode;
		this.employeeTypNm                  = employeeTypNm;
		
	}
    
	// Define full constructor
	public EmployeeTypeDim (
		  int        employeeTypId
		, String     employeeTypCode
		, String     employeeTypNm
	) {
		this.employeeTypId                  = employeeTypId;
		this.employeeTypCode                = employeeTypCode;
		this.employeeTypNm                  = employeeTypNm;
		
	}
	
	@Override
	public String getCode() { 
		return this.employeeTypCode
		;
	}
	public String getDescription() { 
		return this.employeeTypNm; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
                    
		EmployeeTypeDim other = (EmployeeTypeDim) obj;
		if (
            this.employeeTypCode.equals(other.getEmployeeTypCode())
         && this.employeeTypNm.equals(other.getEmployeeTypNm())
		) return true;
		return false;
	}	
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.employeeTypCode))
        + "," + Helpers.formatCsvField(String.valueOf(this.employeeTypNm))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("EMPLOYEE_TYP_CODE")
        + "," + Helpers.formatCsvField("EMPLOYEE_TYP_NM")
		;
	}
    
	// Define Getters and Setters
	public int getEmployeeTypId() {
		return employeeTypId;
	}
	public void setEmployeeTypId(int employeeTypId) {
		this.employeeTypId = employeeTypId;
	}
	public String getEmployeeTypCode() {
		return employeeTypCode;
	}
	public void setEmployeeTypCode(String employeeTypCode) {
		this.employeeTypCode = employeeTypCode;
	}
	public String getEmployeeTypNm() {
		return employeeTypNm;
	}
	public void setEmployeeTypNm(String employeeTypNm) {
		this.employeeTypNm = employeeTypNm;
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