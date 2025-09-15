package com.ibm.wfm.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("dif.datasource")
public class DifDatasourceProperties {
	private String url;
	
	public DifDatasourceProperties() {}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
