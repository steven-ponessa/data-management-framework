package com.ibm.wfm.services;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.util.retry.Retry;

/**
 * Authenticates an API key using IBM IAM by exchanging it for an access token (JWT).
 */
@Service
public class IbmIamAuthService {

    private final WebClient webClient;

    public IbmIamAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://iam.cloud.ibm.com").build();
    }

    @Value("${ibm.iam.apiKeyHeaderName:X-API-KEY}")
    private String apiKeyHeader;

    /**
     * Exchanges an API key for an IAM access token.
     */
    public Optional<String> getAccessTokenFromApiKey(String apiKey) {
        try {
            String response = webClient.post()
                    .uri("/identity/token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .bodyValue("grant_type=urn:ibm:params:oauth:grant-type:apikey&apikey=" + apiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(3))
                    .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)))
                    .block();  // Blocking call to integrate into a sync filter

            // Very simple parse (assumes JSON: { "access_token": "..." })
            String token = extractAccessToken(response);
            return Optional.ofNullable(token);
        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Naively parses the access token from the response body.
     * (Should be replaced with a proper JSON parser in production)
     */
	private String extractAccessToken(String json) {
	    // Very naive JSON parsing. Use a real parser like Jackson in production.
	    int start = json.indexOf("\"access_token\":\"");
	    if (start == -1) return null;
	    start += "\"access_token\":\"".length();
	    int end = json.indexOf("\"", start);
	    return json.substring(start, end);
	}
}