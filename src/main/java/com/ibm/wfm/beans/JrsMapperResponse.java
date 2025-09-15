package com.ibm.wfm.beans;

import java.util.Date;

public class JrsMapperResponse {
	private Date startTime;
	private Date mapUploadStartTime;
	private Date dataUploadStartTime;
	private Date evaluationStartTime;
	private Date completionTime;
	private int statusCd = -99;
	private String message = "Unknown error has occurred.";
	private int totalCnt;
	private int fullKeyCnt;
	private int saJrsCnt;
	private int pSaCnt;
	private int jrsMatchCnt;
	private int serviceAreaMatchCnt;
	private int practiceMatchCnt;
	private int matchCnt;
	private int notMatchedCnt;
	private boolean tieOut;
	private boolean matchTieOut = false;
	private String outputDatasetUrl;
	
	public JrsMapperResponse() {
		startTime = new Date();
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getMapUploadStartTime() {
		return mapUploadStartTime;
	}
	public void setMapUploadStartTime(Date mapUploadStartTime) {
		this.mapUploadStartTime = mapUploadStartTime;
	}
	public Date getDataUploadStartTime() {
		return dataUploadStartTime;
	}
	public void setDataUploadStartTime(Date dataUploadStartTime) {
		this.dataUploadStartTime = dataUploadStartTime;
	}
	public Date getEvaluationStartTime() {
		return evaluationStartTime;
	}
	public void setEvaluationStartTime(Date evaluationStartTime) {
		this.evaluationStartTime = evaluationStartTime;
	}
	public Date getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}
	public int getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(int statusCd) {
		this.statusCd = statusCd;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public int getJrsMatchCnt() {
		return jrsMatchCnt;
	}
	public void setJrsMatchCnt(int jrsMatchCnt) {
		this.jrsMatchCnt = jrsMatchCnt;
	}
	public int getServiceAreaMatchCnt() {
		return serviceAreaMatchCnt;
	}
	public void setServiceAreaMatchCnt(int serviceAreaMatchCnt) {
		this.serviceAreaMatchCnt = serviceAreaMatchCnt;
	}
	public int getPracticeMatchCnt() {
		return practiceMatchCnt;
	}
	public void setPracticeMatchCnt(int practiceMatchCnt) {
		this.practiceMatchCnt = practiceMatchCnt;
	}
	public int getMatchCnt() {
		return matchCnt;
	}
	public void setMatchCnt(int matchCnt) {
		this.matchCnt = matchCnt;
	}
	public int getNotMatchedCnt() {
		return notMatchedCnt;
	}
	public void setNotMatchedCnt(int notMatchedCnt) {
		this.notMatchedCnt = notMatchedCnt;
	}
	public boolean isTieOut() {
		return tieOut;
	}
	public void setTieOut(boolean tieOut) {
		this.tieOut = tieOut;
	}
	public String getOutputDatasetUrl() {
		return outputDatasetUrl;
	}
	public void setOutputDatasetUrl(String outputDatasetUrl) {
		this.outputDatasetUrl = outputDatasetUrl;
	}

	public int getFullKeyCnt() {
		return fullKeyCnt;
	}

	public void setFullKeyCnt(int fullKeyCnt) {
		this.fullKeyCnt = fullKeyCnt;
	}

	public int getSaJrsCnt() {
		return saJrsCnt;
	}

	public void setSaJrsCnt(int saJrsCnt) {
		this.saJrsCnt = saJrsCnt;
	}

	public int getPSaCnt() {
		return pSaCnt;
	}

	public void setPSaCnt(int pSaCnt) {
		this.pSaCnt = pSaCnt;
	}

	public int getpSaCnt() {
		return pSaCnt;
	}

	public void setpSaCnt(int pSaCnt) {
		this.pSaCnt = pSaCnt;
	}

	public boolean isMatchTieOut() {
		return matchTieOut;
	}

	public void setMatchTieOut(boolean matchTieOut) {
		this.matchTieOut = matchTieOut;
	}


}
