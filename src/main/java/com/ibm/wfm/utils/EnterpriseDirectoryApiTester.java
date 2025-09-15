package com.ibm.wfm.utils;


import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class EnterpriseDirectoryApiTester {

	public static void main(String[] args) {
		/*
		 * ISSUE: 
		 *  POSTMAN is returning a body of:
```
dn: uid=008121897,c=us,ou=bluepages,o=ibm.com
preferredIdentity: ponessa@us.ibm.com

# rc=0, count=1, message=Success
```
		RestTemplate returning a body of:
```

# rc=0, count=1, message=Success
```

		// Attempt 1
		String apiUrl = "http://bluepages.ibm.com/BpHttpApisv3/slaphapi?ibmperson/(cn=Steven%20J%20Ponessa).list/bytext?preferredIdentity";
		RestTemplate restTemplate = new RestTemplate();
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
		
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
		String responseBody = response.getBody();
		
		System.out.println("API Response: " + responseBody);

		// Attempt 2
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://bluepages.ibm.com/BpHttpApisv3/slaphapi?ibmperson/(cn=Steven%20J%20Ponessa).list/bytext?preferredIdentity";
		
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        String responseBody = response.getBody();

        // Trimming leading and trailing whitespace characters, including newlines
        responseBody = responseBody.trim();

        // Splitting the response body into lines using the appropriate newline character
        String[] lines = responseBody.split(System.lineSeparator());
        String dn = "";
        String preferredIdentity = "";

        for (String line : lines) {
            if (line.startsWith("dn:")) {
                dn = line.substring(4).trim();
            } else if (line.startsWith("preferredIdentity:")) {
                preferredIdentity = line.substring(18).trim();
            }
        }

        System.out.println("dn: " + dn);
        System.out.println("preferredIdentity: " + preferredIdentity);
        */
		
		//Attempt 3, replace RestTemplate with HttpClient
		String[] members = { "Alonso Andres Mena Macias"
				            ,"Frank Meyer"
		                    ,"Anita Wilhelm"
		                    ,"Jonathan M Turner"
		                    ,"Amir M Rahimi"
		                    ,"Mitra A. Nafissi"
		                    ,"Mani Shahidsales"
		                    ,"Jeffrey Bruty"
		                    ,"Julie Miller"
		                    ,"Alain Ruperto"
		                    ,"Tom Cubit"
		                    ,"Steve Bayline"
		                    ,"Chris Zack"
		                    ,"Sanjay Acharya"
		                    ,"Kelly N Brown"
		                    ,"Michelle T Zappavigna"
		                    ,"Jim Episale"
		                    ,"Mary G Knight"
		                    ,"Martin Upcraft"
		                    ,"Kelly N Brown"
		                    ,"Stephen B Piper"
		                    ,"Steve Rodriguez"
		                    ,"Luis M Cobera Fernandez"
		                    ,"PIETRO MAZZOLENI"
		                    ,"ANNE NETTO"
		                    ,"AXEL LOUCHIE"
		                    ,"Bernard Freund"
		                    ,"Boris Gottschalk"
		                    ,"Everton Tsuyoshi Kumazawa"
		                    ,"GIAN LUIGI CATTANEO"
		                    ,"Julio Ortega"
		                    ,"LAKSHMI SOUMYA"
		                    ,"Laurie J Schaefer"
		                    ,"Liz Meiley"
		                    ,"Mike Kromhout"
		                    ,"Paulette Mendes"
		                    ,"Pete Bauman"
		                    ,"Ron Daubel"
		                    ,"Sara Pale"
		                    ,"Srikanth Krishnappa"
		                    ,"Steven Zamora Alvarado"
		                    ,"Tim Redding"
		                    ,"VERONICA VARGAS LUPO"
		                    ,"Vish Persaud"
		                    ,"Laurence Ball"
		                    };
		
        HttpClient httpClient = HttpClient.newHttpClient();
        
        for (String member : members) {
        	
        	try {
		        //String apiUrl = "http://bluepages.ibm.com/BpHttpApisv3/slaphapi?ibmperson/(cn=Steven%20J%20Ponessa).list/bytext?mail";
		        String apiUrl = "http://bluepages.ibm.com/BpHttpApisv3/slaphapi?ibmperson/(cn="
		        		+ URLEncoder.encode(member, StandardCharsets.UTF_8.toString())
		        		//+ ").list/bytext?mail"
		                //+ ").list/bytext?co"
		                + ").list/bytext?jobresponsibilities"
		        		;
		
		        HttpRequest request = HttpRequest.newBuilder()
		                .uri(URI.create(apiUrl))
		                .build();
		
	
		            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		            String responseBody = response.body();
		
		            // Trimming leading and trailing whitespace characters, including newlines
		            responseBody = responseBody.trim();
		
		            // Splitting the response body into lines using the appropriate newline character
		            String[] lines = responseBody.split(System.lineSeparator());
		            String uid = null;
		            String mail = null;
		            String co = null;
		            String jobresponsibilities = null;
		
		            for (String line : lines) {
		                if (line.startsWith("dn:")) {
		                    uid = line.substring(8,17).trim();
		                } else if (line.startsWith("mail:") && mail==null) {
		                    mail = line.substring(6).trim();
		                } else if (line.startsWith("co:") && co==null) {
		                    co = line.substring(4).trim();
		                } else if (line.startsWith("jobresponsibilities:") && jobresponsibilities==null) {
		                	jobresponsibilities = line.substring(20).trim();
		                }
		            }
		
		            //System.out.println(member + "," + (uid==null?"not found":uid) + ","+ (mail==null?"not found":mail));
		            //System.out.println(co==null?"not found":co);
		            System.out.println(jobresponsibilities==null?"not found":jobresponsibilities);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
        }

	}

}
