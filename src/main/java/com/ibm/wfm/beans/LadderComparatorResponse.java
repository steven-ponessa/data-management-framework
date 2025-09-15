package com.ibm.wfm.beans;

import java.util.Date;

public class LadderComparatorResponse {
	private Date startTime = new Date();
	private Date oldFileUploadStartTime;
	private Date newFileUploadStartTime;
	private Date compareStartTime;
	private Date completionTime;
	
	private int totalOldRecords;
	private int totalNewRecords;
	private int insertCnt;
	private int updateCnt;
	private int deleteCnt;
	private int noActionCnt;
	private String outputDatasetUrl;
	
	public LadderComparatorResponse() {}
	
	public LadderComparatorResponse(Date startTime, Date oldFileUploadStartTime, Date newFileUploadStartTime,
			Date compareStartTime, Date completionTime, int totalOldRecords, int totalNewRecords, int insertCnt,
			int updateCnt, int deleteCnt, int noActionCnt, String outputDatasetUrl) {
		super();
		this.startTime = startTime;
		this.oldFileUploadStartTime = oldFileUploadStartTime;
		this.newFileUploadStartTime = newFileUploadStartTime;
		this.compareStartTime = compareStartTime;
		this.completionTime = completionTime;
		this.totalOldRecords = totalOldRecords;
		this.totalNewRecords = totalNewRecords;
		this.insertCnt = insertCnt;
		this.updateCnt = updateCnt;
		this.deleteCnt = deleteCnt;
		this.noActionCnt = noActionCnt;
		this.outputDatasetUrl = outputDatasetUrl;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getOldFileUploadStartTime() {
		return oldFileUploadStartTime;
	}

	public void setOldFileUploadStartTime(Date oldFileUploadStartTime) {
		this.oldFileUploadStartTime = oldFileUploadStartTime;
	}

	public Date getNewFileUploadStartTime() {
		return newFileUploadStartTime;
	}

	public void setNewFileUploadStartTime(Date newFileUploadStartTime) {
		this.newFileUploadStartTime = newFileUploadStartTime;
	}

	public Date getCompareStartTime() {
		return compareStartTime;
	}

	public void setCompareStartTime(Date compareStartTime) {
		this.compareStartTime = compareStartTime;
	}

	public Date getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

	public int getTotalOldRecords() {
		return totalOldRecords;
	}

	public void setTotalOldRecords(int totalOldRecords) {
		this.totalOldRecords = totalOldRecords;
	}

	public int getTotalNewRecords() {
		return totalNewRecords;
	}

	public void setTotalNewRecords(int totalNewRecords) {
		this.totalNewRecords = totalNewRecords;
	}

	public int getInsertCnt() {
		return insertCnt;
	}

	public void setInsertCnt(int insertCnt) {
		this.insertCnt = insertCnt;
	}

	public int getUpdateCnt() {
		return updateCnt;
	}

	public void setUpdateCnt(int updateCnt) {
		this.updateCnt = updateCnt;
	}

	public int getDeleteCnt() {
		return deleteCnt;
	}

	public void setDeleteCnt(int deleteCnt) {
		this.deleteCnt = deleteCnt;
	}

	public int getNoActionCnt() {
		return noActionCnt;
	}

	public void setNoActionCnt(int noActionCnt) {
		this.noActionCnt = noActionCnt;
	}

	public String getOutputDatasetUrl() {
		return outputDatasetUrl;
	}

	public void setOutputDatasetUrl(String outputDatasetUrl) {
		this.outputDatasetUrl = outputDatasetUrl;
	}

}
