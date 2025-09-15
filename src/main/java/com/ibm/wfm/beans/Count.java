package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.DbColumn;

public class Count {
	@DbColumn(columnName="CNT")
	private int cnt = -1;
	
	public Count() {
		super();
	}

	public Count(int cnt) {
		super();
		this.cnt = cnt;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	

}
