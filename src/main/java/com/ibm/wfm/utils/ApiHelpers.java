package com.ibm.wfm.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ApiHelpers {
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	public ApiHelpers() {}

	public static void main(String[] args) {
		
		boolean validParams = true;
		String url=null;
		String token = null;
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-u") || args[optind].equalsIgnoreCase("-url")) {
					url = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-t") || args[optind].equalsIgnoreCase("-token")) {
					token = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("-help")) {
					validParams = false;
				} else {
					// logger.info("E0001: Unknown parameter specified: " + args[optind]);
					System.err.println("E0001: Unknown parameter specified: " + args[optind]);
					validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			// logger.error(msgs.getText("error.e0001", e.getMessage()));
			e.printStackTrace();
			validParams = false;
		}
		if (url == null)
			validParams = false;

		if (!validParams) {
			// logger.info(msgs.getText("info.usage"));
			System.out.println("Put usage here"); // msgs.getText("Seat2PldbEntitlementEtl.usage"));
			System.exit(-99);
		}
		
		try {
			System.out.println("Started at "+new java.util.Date());
			System.out.println("URL: "+url);
			
			//ResponseEntity<Object> myObject = ApiHelpers.getApi(url);
			ApiHelpers.getBluepagesInfo();
			
			System.out.println("Completed at "+new java.util.Date());
		}
		catch (Exception e) {
			System.out.println("Failed at "+new java.util.Date());
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void getBluepagesInfo()  {
		ArrayList<String> members = DataMarshaller.buildStringListFromExcel("/Users/steve/$GBS-RBD/GBSDataGovernance/GBS Data Governance Council 2022-v.xlsx", "Member List", 2);
		ArrayList<String> output = new ArrayList<String>();
		
		for (String member: members) {
			String url = "https://w3.api.ibm.com/common/run/bluepages/slaphapi/ibmperson/mail="
					+ member + ".list,printable/byjson/cn&jobResponsibilities&co?client_id=5c429871-57e9-4ed7-b383-735357e1aace";
			ResponseEntity<Object> responseObject = ApiHelpers.getApi(url);
			//JsonObject jsonObject = new JsonParser().parse((String)myObject.getBody()).getAsJsonObject();
			
			//create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();

			try {
				//read JSON like DOM Parser
				//String jsonStr = (String)myObject.getBody().toString(); //Api does not return valid JSON
				//JsonNode rootNode = objectMapper.readTree(jsonStr);
				//JsonNode entity = rootNode.path("entry");
				LinkedHashMap lhm = (LinkedHashMap)responseObject.getBody();
				LinkedHashMap search = (LinkedHashMap)lhm.get("search");
				ArrayList entries = (ArrayList)search.get("entry");
				LinkedHashMap x = (LinkedHashMap)entries.get(0);
				ArrayList attributes = (ArrayList)x.get("attribute");
				
				String cn = null;
				String jobResponsilities = null;
				String co = null;
				
				for (Object attributeObject: attributes) {
					LinkedHashMap attribute = (LinkedHashMap)attributeObject;
					
					Set<String> keys = attribute.keySet();

					String keyName = attribute.get("name").toString();
					ArrayList<Object> keyValues = (ArrayList)attribute.get("value");

					if (keyName.equalsIgnoreCase("cn"))
						cn = keyValues.get(0)==null?"":keyValues.get(0).toString();
					if (keyName.equalsIgnoreCase("jobResponsibilities"))
						jobResponsilities = keyValues.get(0)==null?"":keyValues.get(0).toString();
					if (keyName.equalsIgnoreCase("co"))
						co = keyValues.get(0)==null?"":keyValues.get(0).toString();
						
				}
				output.add(member+",\""+cn+"\",\""+jobResponsilities+"\",\""+co+"\"")	;
				
				//LinkedHashMap attributes = (LinkedHashMap)((ArrayList)entries.get(0)).get(1);
				System.out.println("Debug here");
			}
			catch (Exception e) { //JsonProcessingException e) {
				System.out.println("Error ("+member+", "+e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("Inet Id, Name, Job Responsibilities, Country");
		for (String out: output) {
			System.out.println(out);
		}
	}
	
	public static ResponseEntity<Object> getApi(String url) {
	    //Add a ClientHttpRequestInterceptor to the RestTemplate
	    restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor(){
	        @Override
	        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
	            request.getHeaders().set("Accept", "text/html");//Set the header for each request
	            return execution.execute(request, body);
	        }
	    });
		
		
		ResponseEntity<Object> responseEntity =
				restTemplate.getForEntity(url, Object.class);
		return responseEntity;
	}
	


}
