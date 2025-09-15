package com.ibm.wfm.controllers.tram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ibm.wfm.beans.TaxonomyEvaluationResponse;
import com.ibm.wfm.beans.tram.TramProjectFinancialsByQuarter;
import com.ibm.wfm.beans.tram.TramProjectFinancialsByQuarterDetailItem;
import com.ibm.wfm.controllers.AbstractDaoController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class TramApiController extends AbstractDaoController {
	
	@Operation(summary = "Evaluates the current IBM Consulting demographics and how each matches a branch within the JRS standard taxonomy, identifying discrepencies in nodes and/or arcs.")
	@GetMapping("/tram/project-fin-by-qtr")
	public ResponseEntity<TramProjectFinancialsByQuarter> projectFinByQtr(
			@RequestParam(required = true, value = "project-country-code") @Parameter(description = "Financial Country Code") String ctryCd,
			@RequestParam(required = true, value = "project-number") @Parameter(description = "Project number") String projectNum) {

		//?project-country-code=706&project-number=BTFAACX
		String projectFinByQtrUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/dyn/sql/")
				.path("/epm-datamart/project-fin-by-qtr")
				.queryParam("project-country-code", ctryCd)
				.queryParam("project-number", projectNum)
				.toUriString();

		ResponseEntity<Resource> projectResponseEntity = new RestTemplate().getForEntity(projectFinByQtrUri, Resource.class);
		
        TramProjectFinancialsByQuarter pfq = null;
		
		Resource r = projectResponseEntity.getBody();
		InputStream is = null;
		try {
			is = r.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(is));        
	        String lineBuffer = null;
	        StringBuffer jsonString = new StringBuffer();
	        while ((lineBuffer = br.readLine()) != null) {
	        	jsonString.append(lineBuffer);
	        }
            ObjectMapper mapper = new ObjectMapper();
            //JsonNode resultObject = mapper.readTree(jsonString.toString());

            ArrayNode results = (ArrayNode) mapper.readTree(jsonString.toString()).get("results");
            
            if(results.isArray()) {
               for(JsonNode result : results) {
            	   if (pfq==null) {
            		   pfq = new TramProjectFinancialsByQuarter(result.get("PROJECT_COUNTRY_CODE").textValue()
            				                                  	, result.get("PROJECT_NUMBER").textValue()
																, result.get("PROJECT_DESCRIPTION").textValue()
																, result.get("START_DATE").textValue()
																, result.get("END_DATE").textValue()
																, result.get("START_DATE").textValue()
																);
            	   }
            	   TramProjectFinancialsByQuarterDetailItem item = new TramProjectFinancialsByQuarterDetailItem(
            			   												 Integer.parseInt(result.get("YR").textValue())
            			   												, Integer.parseInt(result.get("QUARTER").textValue())
            			   												, result.get("MNEMONIC_QUARTER_BY_MONTH").textValue()
            			   												, result.get("MNEMONIC_YEAR_BY_MONTH").textValue()
            			   												, result.get("REVENUE_PLAN_AMOUNT").asDouble()
            			   												, result.get("COST_PLAN_AMOUNT").asDouble()
            			   												, result.get("GP_PLAN_AMOUNT").asDouble()
            			   												);
            	   pfq.addDetailItem(item);
            	   System.out.println(result);
               }
            }
            
            if (pfq==null) return ResponseEntity.notFound().build();
            
            ArrayList<TramProjectFinancialsByQuarterDetailItem> items = pfq.getItems();
            
            
            if (items!=null) {
            	int start=0;
                int end = items.size();
            	if (items.get(items.size()-1).getMnemonicQuarterByMonth().equalsIgnoreCase("CQ")) {
            		end--;
            	}
            	if (end-start>4) start = end-4;
            	double cost = 0.0;
            	double revenue = 0.0;
            	double gp = 0.0;
            	int cnt=0;
            	for (int i=start; i<end; i++) {
            		cnt++;
            		cost+= items.get(i).getCostPlanAmount();
            		revenue+= items.get(i).getRevenuePlanAmount();
            		gp+= items.get(i).getGpPlanAmount();
            	}
            	pfq.setAverageCostAmt(cost/cnt);
            	pfq.setAverageRevenueAmt(revenue/cnt);
            	pfq.setAverageGpAmt(gp/cnt);
            	pfq.setAverageGpMarginAmt((gp/revenue)/cnt);
            	pfq.setStatus(cnt);
            	
            }
            
            		
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().body(pfq);
	}
	

}
