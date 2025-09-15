package com.ibm.wfm.beans;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInfo {

    @Value("${info.app.name}")
    private String name;
    
    @Value("${info.app.description}")
    private String description;
    
    @Value("${info.app.version}")
    private String version;

    @Value("${info.app.springBootVersion}")
    private String springBootVersion;

    @Value("${info.app.openApiVersion}")
    private String openApiVersion;
    
    @Value("${info.app.releaseNotes}")
    private String releaseNotes;
    
    @Value("${info.app.license}")
    private String license;
    
    @Value("${info.app.documentationLink}")
    private String documentationLink;
    
    public ApplicationInfo() {
    	
    }
    
    public String getVersion() {
        return version;
    }

    public String getSpringBootVersion() {
        return springBootVersion;
    }

    public String getOpenApiVersion() {
        return openApiVersion;
    }

	public String getReleaseNotes() {
		return releaseNotes;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setSpringBootVersion(String springBootVersion) {
		this.springBootVersion = springBootVersion;
	}

	public void setOpenApiVersion(String openApiVersion) {
		this.openApiVersion = openApiVersion;
	}
	
	public void setReleaseNotes(String releaseNotes) {
		this.releaseNotes = releaseNotes;
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

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getDocumentationLink() {
		return documentationLink;
	}

	public void setDocumentationLink(String documentationLink) {
		this.documentationLink = documentationLink;
	}
}