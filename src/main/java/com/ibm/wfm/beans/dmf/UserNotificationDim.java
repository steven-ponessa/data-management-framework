package com.ibm.wfm.beans.dmf;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="UserNotificationDim",baseTableName="DMF.USER_NOTIFICATION")
public class UserNotificationDim {
	@DbColumn(columnName="NOTIFICATION_ID",isId=true)
	private int        notificationId;
	@DbColumn(columnName="USERID",keySeq=1)
	private String     userid;
	@DbColumn(columnName="MESSAGE_ID",keySeq=2)
	private int        messageId;
	@DbColumn(columnName="TITLE")
	private String     title;
	@DbColumn(columnName="MESSAGE")
	private String     message;
	@DbColumn(columnName="KIND")
	private String     kind;
	@DbColumn(columnName="TARGET_URL")
	private String     targetUrl;
	@DbColumn(columnName="READ_IND")
	private String     readInd;
	@DbColumn(columnName ="EFF_TMS",isScd=true) 
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp effTms;
	@DbColumn(columnName ="EXPIR_TMS",isScd=true)   
	@JsonFormat(pattern="yyyy-MM-dd-HH.mm.ss.SSS",timezone="GMT-04:00")
	private Timestamp expirTms;
	@DbColumn(columnName ="ROW_STATUS_CD",isScd=true)    
	private String rowStatusCd;

	// Define null constructor
	public UserNotificationDim () {
		
	}
	
	// Define natural key constructor
	public UserNotificationDim (
      String     userid
    , int        messageId
	) {
		this.userid                         = userid;
		this.messageId                      = messageId;
		
	}
    
	// Define full constructor
	public UserNotificationDim (
		  int        notificationId
		, String     userid
		, int        messageId
		, String     title
		, String     message
		, String     kind
		, String     targetUrl
		, String     readInd
	) {
		this.notificationId                 = notificationId;
		this.userid                         = userid;
		this.messageId                      = messageId;
		this.title                          = title;
		this.message                        = message;
		this.kind                           = kind;
		this.targetUrl                      = targetUrl;
		this.readInd                        = readInd;
		
	}
	
	public String toEtlString() {
		return 
                Helpers.formatCsvField(String.valueOf(this.notificationId))
        + "," + Helpers.formatCsvField(String.valueOf(this.userid))
        + "," + Helpers.formatCsvField(String.valueOf(this.messageId))
        + "," + Helpers.formatCsvField(String.valueOf(this.title))
        + "," + Helpers.formatCsvField(String.valueOf(this.message))
        + "," + Helpers.formatCsvField(String.valueOf(this.kind))
        + "," + Helpers.formatCsvField(String.valueOf(this.targetUrl))
        + "," + Helpers.formatCsvField(String.valueOf(this.readInd))
		;
	}
	
	public static String getEtlHeader() {
		return 
                Helpers.formatCsvField("NOTIFICATION_ID")
        + "," + Helpers.formatCsvField("USERID")
        + "," + Helpers.formatCsvField("MESSAGE_ID")
        + "," + Helpers.formatCsvField("TITLE")
        + "," + Helpers.formatCsvField("MESSAGE")
        + "," + Helpers.formatCsvField("KIND")
        + "," + Helpers.formatCsvField("TARGET_URL")
        + "," + Helpers.formatCsvField("READ_IND")
		;
	}
    
	// Define Getters and Setters
	public int getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public String getReadInd() {
		return readInd;
	}
	public void setReadInd(String readInd) {
		this.readInd = readInd;
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