package com.ibm.wfm.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * OpenAPI documentation configuration. Sets up API key-based authentication scheme for Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiKeyOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("WFM Data Management Framework")
                .version("1.0")
                .description("<p>The Workforce Managment - Data Management Framework (WFM-DMF) fundamentally transforms how organizations handle and interact with data by allowing direct access to business and reference information at its source, bypassing the need for traditional ETL (Extract, Transform, Load) pipelines.</p>"
                		   + "<p>Please see the <a href=\"https://pages.github.ibm.com/ponessa/wfm-dmf-documentation/en/learning-framework/fw-overview\" target=\"_blank\">Data Management Framework’s documenation</a> for additional detail.</p>")
                .termsOfService("https://w3.ibm.com/#/info_terms_of_use")
                .contact(new Contact()
                    .name("Steve Ponessa")
                    .email("ponessa@us.ibm.com")
                    .url("https://ibm.enterprise.slack.com/user/@W52QZ3BJ8"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
            .components(new Components()
                .addSecuritySchemes("ApiKeyAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("X-API-Key")))
            .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"));
    }
}