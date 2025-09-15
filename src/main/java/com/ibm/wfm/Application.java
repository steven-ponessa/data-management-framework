package com.ibm.wfm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ibm.wfm.configurations.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class Application extends SpringBootServletInitializer {
	
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
	
	/*
	 * Enables CORS requests from any origin to any endpoint in the application.
	 * To lock this down a bit more, the `registry.addMapping()` method returns a` CorsRegistration` object, which we can use for additional configuration. 
	 * There’s also an `allowedOrigins()` method that lets us specify an array of allowed origins. This can be useful if we need to load this array from an 
	 * external source at runtime.
	 * 
	 * Additionally, there are also `allowedMethods`, `allowedHeaders`, `exposedHeaders`, `maxAge` and `allowCredentials` that we can use to set the response 
	 * headers and customization options.
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
			
		    @Override
		    public void configureContentNegotiation( ContentNegotiationConfigurer configurer){
		        configurer.defaultContentType( MediaType.APPLICATION_JSON );
		    }
		};
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
	    return new RestTemplate();
	}
    
}
