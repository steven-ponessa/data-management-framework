package com.ibm.wfm.beans;

import java.util.List;

public class UtResponse {
	private String msg;
	private int code;
	private int size;
	private int total;
	private List<UtNode> source;
	
	public UtResponse() {}

	public UtResponse(String msg, int code, int size, int total, List<UtNode> source) {
		super();
		this.msg = msg;
		this.code = code;
		this.size = size;
		this.total = total;
		this.source = source;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<UtNode> getSource() {
		return source;
	}

	public void setSource(List<UtNode> source) {
		this.source = source;
	}
	
	

}
