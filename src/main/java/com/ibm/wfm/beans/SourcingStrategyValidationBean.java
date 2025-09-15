package com.ibm.wfm.beans;

import java.util.ArrayList;
import java.util.List;

public class SourcingStrategyValidationBean {
	private int status=200;
	private boolean valid;
	private String sourcingStrategyCd;
	private String msg;
	private List<String> validExpressions;
	private List<String> errors;
	
	public SourcingStrategyValidationBean() {}
	
	public SourcingStrategyValidationBean(String sourcingStrategyCd) {
		this.sourcingStrategyCd = sourcingStrategyCd;
	}
	
	public boolean isValidated() {
		return valid;
	}
	public void setValidated(boolean validated) {
		this.valid = validated;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<String> getValidExpressions() {
		return validExpressions;
	}
	public void setValidExpressions(List<String> validExpressions) {
		this.validExpressions = validExpressions;
	}
	public String getSourcingStrategyCd() {
		return sourcingStrategyCd;
	}
	public void setSourcingStrategyCd(String sourcingStrategyCd) {
		this.sourcingStrategyCd = sourcingStrategyCd;
	}
	
	public void addError(String msg) {
		if (errors==null) errors = new ArrayList<>();
		errors.add(msg);
	}
	
	public void addValidExpression(String msg) {
		if (validExpressions==null) validExpressions = new ArrayList<>();
		validExpressions.add(msg);
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
