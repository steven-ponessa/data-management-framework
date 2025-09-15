package com.ibm.wfm.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("rah.datasource")
public class RahEtlDatasourceProperties {
	private String url;
	
	public RahEtlDatasourceProperties() {}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}