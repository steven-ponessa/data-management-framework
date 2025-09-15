package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.ExcelSheet;

public class ExcelColumn {
	@ExcelSheet(columnName="Excel Position", columnNum=0)
	private int position;
	@ExcelSheet(columnName="Excel Column", columnNum=1)
	private String letter;
	@ExcelSheet(columnName="Excel Header", columnNum = 2)
	private String name;
	@ExcelSheet(columnName="Name", columnNum=3)
	private String dbName;
	
	public ExcelColumn() {}
	
	public ExcelColumn(int position, String letter, String name, String dbName) {
		super();
		this.position = position;
		this.letter = letter;
		this.name = name;
		this.dbName = dbName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}
