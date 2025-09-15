package com.ibm.wfm.beans;

import com.ibm.wfm.annotations.DbColumn;
import com.ibm.wfm.annotations.DbTable;
import com.ibm.wfm.utils.Helpers;

@DbTable(beanName="DataSourceDim",baseTableName="DMF.DATA_SOURCE")
public class DataSourceDim {
	@DbColumn(columnName = "ID", isId = true)
	private int id;
	@DbColumn(columnName = "NAME", keySeq = 1)
	private String name;
	@DbColumn(columnName = "DESCRIPTION")
	private String description;
	@DbColumn(columnName = "URL")
	private String url;
	@DbColumn(columnName = "API_KEY_TOKEN_NM")
	private String apiKeyTokenNm;
	@DbColumn(columnName = "USERID_TOKEN_NM")
	private String useridTokenNm;
	@DbColumn(columnName = "PASSWORD_TOKEN_NM")
	private String passwordTokenNm;

	// Define null constructor
	public DataSourceDim() {
	}

	// Define natural key constructor
	public DataSourceDim(String name) {
		this.name = name;
	}

	// Define full constructor
	public DataSourceDim(int id, String name, String description, String url, String apiKeyTokenNm,
			String useridTokenNm, String passwordTokenNm) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.url = url;
		this.apiKeyTokenNm = apiKeyTokenNm;
		this.useridTokenNm = useridTokenNm;
		this.passwordTokenNm = passwordTokenNm;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		DataSourceDim other = (DataSourceDim) obj;
		if (this.name.equals(other.getName()) && this.description.equals(other.getDescription()))
			return true;
		return false;
	}

	public String toEtlString() {
		return Helpers.formatCsvField(String.valueOf(this.name)) + ","
				+ Helpers.formatCsvField(String.valueOf(this.description)) + ","
				+ Helpers.formatCsvField(String.valueOf(this.url)) + ","
				+ Helpers.formatCsvField(String.valueOf(this.apiKeyTokenNm)) + ","
				+ Helpers.formatCsvField(String.valueOf(this.useridTokenNm)) + ","
				+ Helpers.formatCsvField(String.valueOf(this.passwordTokenNm));
	}

	public static String getEtlHeader() {
		return Helpers.formatCsvField("NAME") + "," + Helpers.formatCsvField("DESCRIPTION") + ","
				+ Helpers.formatCsvField("URL") + "," + Helpers.formatCsvField("API_KEY_TOKEN_NM") + ","
				+ Helpers.formatCsvField("USERID_TOKEN_NM") + "," + Helpers.formatCsvField("PASSWORD_TOKEN_NM");
	}

	// Define Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApiKeyTokenNm() {
		return apiKeyTokenNm;
	}

	public void setApiKeyTokenNm(String apiKeyTokenNm) {
		this.apiKeyTokenNm = apiKeyTokenNm;
	}

	public String getUseridTokenNm() {
		return useridTokenNm;
	}

	public void setUseridTokenNm(String useridTokenNm) {
		this.useridTokenNm = useridTokenNm;
	}

	public String getPasswordTokenNm() {
		return passwordTokenNm;
	}

	public void setPasswordTokenNm(String passwordTokenNm) {
		this.passwordTokenNm = passwordTokenNm;
	}

	@Override
	public String toString() {
		return "DataSourceDim [id=" + id + ", name=" + name + ", description=" + description + ", url=" + url
				+ ", apiKeyTokenNm=" + apiKeyTokenNm + ", useridTokenNm=" + useridTokenNm + ", passwordTokenNm="
				+ passwordTokenNm + "]";
	}
}