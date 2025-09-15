package com.ibm.wfm.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Looks up Bluegroups for a user using IBM Bluepages based on email.
 */
@Service
public class IbmBluegroupService {
	//https://bluepages.ibm.com/BpHttpApisv3/slaphapi?ibmperson/(mail=lorna_gibson@nl.ibm.com).list/byldif?ibm-allgroups
	
    private final WebClient webClient;

    public IbmBluegroupService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://bluepages.ibm.com").build();
    }
    
    /**
     * Fetches WFM-DMF Bluegroups from Bluepages LDIF response.
     */
    public List<String> getWfmDmfGroups(String emailAddress) {
        String path = String.format("/BpHttpApisv3/slaphapi?ibmperson/(mail=%s).list/byldif?ibm-allgroups", emailAddress);

        String response = webClient.get()
            .uri(path)
            .retrieve()
            .bodyToMono(String.class)
            .block(); // block to get String result

        if (response == null || response.isEmpty()) {
            return List.of(); // return empty list if no response
        }

        return Arrays.stream(response.split("\n"))
            .filter(line -> line.startsWith("ibm-allgroups: cn=wfm-dmf"))
            .map(this::extractGroupName)
            .collect(Collectors.toList());
    }

    private String extractGroupName(String line) {
        int start = line.indexOf("cn=") + 3;
        int end = line.indexOf(',', start);
        return line.substring(start, end);
    }

}