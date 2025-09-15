package com.ibm.wfm.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("wf360-prod.datasource")
public class Wf360DatasourceProperties {
	private String url;
	private String futureSkillSql;
	private String certificationSql;
	
	public Wf360DatasourceProperties() {}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFutureSkillSql() {
		return futureSkillSql;
	}

	public void setFutureSkillSql(String futureSkillSql) {
		this.futureSkillSql = futureSkillSql;
	}

	public String getCertificationSql() {
		return certificationSql;
	}

	public void setCertificationSql(String certificationSql) {
		this.certificationSql = certificationSql;
	}

}
