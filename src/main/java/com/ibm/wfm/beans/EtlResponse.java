package com.ibm.wfm.beans;


public class EtlResponse {
	private int totalCnt=0;
	private int insertCnt=0;
	private int updateCnt=0;
	private int deleteCnt=0;
	private int insertUpdateAppliedCnt=0;
	private int deleteAppliedCnt=0;
	private String deltaFile;
	
	public EtlResponse() {}
	

	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public int getInsertCnt() {
		return insertCnt;
	}
	public void setInsertCnt(int insertCnt) {
		this.insertCnt = insertCnt;
	}
	public int getDeleteCnt() {
		return deleteCnt;
	}
	public void setDeleteCnt(int deleteCnt) {
		this.deleteCnt = deleteCnt;
	}

	public int getUpdateCnt() {
		return updateCnt;
	}

	public void setUpdateCnt(int updateCnt) {
		this.updateCnt = updateCnt;
	}

	public int getInsertUpdateAppliedCnt() {
		return insertUpdateAppliedCnt;
	}

	public void setInsertUpdateAppliedCnt(int insertUpdateAppliedCnt) {
		this.insertUpdateAppliedCnt = insertUpdateAppliedCnt;
	}

	public int getDeleteAppliedCnt() {
		return deleteAppliedCnt;
	}

	public void setDeleteAppliedCnt(int deleteAppliedCnt) {
		this.deleteAppliedCnt = deleteAppliedCnt;
	}


	public String getDeltaFile() {
		return deltaFile;
	}


	public void setDeltaFile(String deltaFile) {
		this.deltaFile = deltaFile;
	}
	
}
