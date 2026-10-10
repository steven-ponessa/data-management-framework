package com.ibm.wfm.configurations;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

@Component
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation( ContentNegotiationConfigurer configurer){
        configurer.defaultContentType( MediaType.APPLICATION_JSON );
    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        var urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
    
	@Override
	public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/api/**")
	        //.allowedOrigins("http://localhost:3000"
	        .allowedOriginPatterns("http://localhost:3000"
	        		, "https://wfm-table-editor-test-dev.prom-greenstar-integration.dal.dev.cirrus.ibm.com"
	        		, "https://*.dal.dev.cirrus.ibm.com"
	        		, "https://*.dal.cirrus.ibm.com"
	        		, "https://dl.watson-orchestrate.ibm.com/"
	        		, "https://localhost:3000"
	        		, "https://localhost:5000"
	        		, "https://*.intranet.ibm.com"
	        		, "https://pages.github.ibm.com"
	        		, "http://127.0.0.1:4000")
	        .allowedMethods("GET", "POST", "PUT", "DELETE")
	        .allowedHeaders("*")
	        .allowCredentials(true)
	        .maxAge(3600);
	}
}