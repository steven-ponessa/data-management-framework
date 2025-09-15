package com.ibm.wfm.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.GrowthPlatformDim;
import com.ibm.wfm.beans.PracticeDim;
import com.ibm.wfm.beans.ServiceLineDim;
import com.ibm.wfm.beans.UtNode;
import com.ibm.wfm.services.UtAccessService;
import com.ibm.wfm.utils.DataMarshaller;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class UtAccessController {

	@Autowired
	private UtAccessService utAccessService;

	@GetMapping("/ut-all")
	public List<UtNode> findAllUt(@RequestParam(defaultValue = "10J00") String utlevel10
			, @RequestParam(defaultValue = "O") String ocstatus) {
		return utAccessService.findAllEdsUt(utlevel10, ocstatus, "All");
	}

	@GetMapping(value="/ut-all/csv", produces="text/csv")
	public void toCsvAllUt(@RequestParam(defaultValue = "10J00") String utlevel10
			, @RequestParam(defaultValue = "O") String ocstatus
			, HttpServletResponse response) throws IOException {
		response.setContentType("text/csv; charset=utf-8");
		List<UtNode> utNodes = utAccessService.findAllEdsUt(utlevel10, ocstatus, "All");
		DataMarshaller.writeCsv2PrintWriter(UtNode.class, response.getWriter(), utNodes);
		return;
	}
	
	//@RequestParam(required=false, defaultValue="true") @Parameter(description="Include in output only rows for branches in error.")
	@GetMapping("/ut")
	public List<UtNode> findUt(@RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default) to get all data.") String size
			, @RequestParam(required=false, defaultValue = "O") @Parameter(description="UT Status. O = Open") String ocstatus
			, @RequestParam(required=false, defaultValue = "10J00") @Parameter(description="Brand Code. 10J00 = Global Business Services") String utlevel10
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Growth Platform Code") String utlevel15
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Service Line Code") String utlevel17
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="GBS Practice Code") String gbspracticecode
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Financial Offering Portfolio") String utlevel20
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Offering") String utlevel30
			, @RequestParam(required=false, defaultValue = "") @Parameter(description="Additional query string") String addlQueryString
			) {
		return utAccessService.findEdsUt(size, utlevel10, ocstatus, utlevel15, utlevel17, gbspracticecode, utlevel20, utlevel30, addlQueryString);
	}
	
	@GetMapping(value="/ut/brand",produces = { "application/json", "application/xml"})
	public List<BrandDim> findBrandsUt(@RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default) to get all data.") String size
			, @RequestParam(required=false, defaultValue = "O") @Parameter(description="UT Status. O = Open. Use ‘ALL’ to get all data.") String ocstatus
			, @RequestParam(required=false, defaultValue = "10J00") @Parameter(description="Brand Code. 10J00 = Global Business Services. Use ‘ALL’ to get all data.") String utlevel10
			)  {
		
		String utUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/api/v1/")
	            .path("ut")
                .queryParam("utlevel10", utlevel10)
                .queryParam("ocstatus", ocstatus)
	            .toUriString();
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<BrandDim> brands = new ArrayList<BrandDim>();
		
		for (UtNode utNode: utNodes) {
			BrandDim brand = new BrandDim(utNode.getUtlevel10(),utNode.getUtlevel10description(),utNode.getUtlevel10description());
			if (!(brands.contains(brand))) brands.add(brand);
		}
		return brands;
	}
	
	@GetMapping(value="/ut/growth-platform",produces = { "application/json", "application/xml"})
	public List<GrowthPlatformDim> findUtGrowthPlatforms(@RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default)to get all data.") String size
			, @RequestParam(required=false, defaultValue = "O") @Parameter(description="UT Status. O = Open") String ocstatus
			, @RequestParam(required=false, defaultValue = "10J00") @Parameter(description="Brand Code. 10J00 = Global Business Services") String utlevel10
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Growth Platform Code") String utlevel15
			, @RequestParam(required=false, defaultValue = "") @Parameter(description="Additional query string") String addlQueryString
			)  {
		
		UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/api/v1/")
	            .path("ut")
                .queryParam("utlevel10", utlevel10)
                .queryParam("ocstatus", ocstatus);
		
		if (!(utlevel15==null || utlevel15.trim().length()==0 || utlevel15.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel15", utlevel15);
		
		if (!(addlQueryString==null || addlQueryString.trim().length()==0 ))
			utUriBuilder.queryParam("addlQueryString", addlQueryString);
  
		String utUri = utUriBuilder.toUriString();
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<GrowthPlatformDim> growthPlatforms = new ArrayList<GrowthPlatformDim>();
		
		for (UtNode utNode: utNodes) {
			GrowthPlatformDim growthPlatform = new GrowthPlatformDim(utNode.getUtlevel15(),utNode.getUtlevel15description(),utNode.getUtlevel15description(),utNode.getUtlevel10());
			if (!(growthPlatforms.contains(growthPlatform))) growthPlatforms.add(growthPlatform);
		}
		return growthPlatforms;
	}
	
	@GetMapping(value="/ut/growth-platform/tax",produces = { "application/json", "application/xml"})
	public List<BrandDim> findUtGrowthPlatformsTax(@RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default)to get all data.") String size
			, @RequestParam(required=false, defaultValue = "O") @Parameter(description="UT Status. O = Open") String ocstatus
			, @RequestParam(required=false, defaultValue = "10J00") @Parameter(description="Brand Code. 10J00 = Global Business Services") String utlevel10
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Growth Platform Code") String utlevel15
			, @RequestParam(required=false, defaultValue = "") @Parameter(description="Additional query string") String addlQueryString
			)  {
		
		UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/api/v1/")
	            .path("ut")
                .queryParam("utlevel10", utlevel10)
                .queryParam("ocstatus", ocstatus);
		
		if (!(utlevel15==null || utlevel15.trim().length()==0 || utlevel15.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel15", utlevel15);
		
		if (!(addlQueryString==null || addlQueryString.trim().length()==0 ))
			utUriBuilder.queryParam("addlQueryString", addlQueryString);
  
		String utUri = utUriBuilder.toUriString();
		
		System.out.println("******"+utUri);
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		System.out.println(utNodes.length);
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<BrandDim> brands = new ArrayList<BrandDim>();
		List<GrowthPlatformDim> growthPlatforms = new ArrayList<GrowthPlatformDim>();
		
		for (UtNode utNode: utNodes) {
			BrandDim brand = new BrandDim(utNode.getUtlevel10(),utNode.getUtlevel10description(),utNode.getUtlevel10description());
			if (!(brands.contains(brand))) brands.add(brand);
			
			GrowthPlatformDim growthPlatform = new GrowthPlatformDim(utNode.getUtlevel15(),utNode.getUtlevel15description(),utNode.getUtlevel15description(),utNode.getUtlevel10());
			if (!(growthPlatforms.contains(growthPlatform))) growthPlatforms.add(growthPlatform);
		}
		
		//BrandDim brand = brands.get(0);
		for (BrandDim brand: brands) {
			for (GrowthPlatformDim growthPlatform: growthPlatforms) {
				if (growthPlatform.getBrandCd().equals(brand.getBrandCd()))
					brand.addChild(growthPlatform);
			}
		}
				
		return brands;
	}	
	
	@GetMapping(value="/ut/service-line",produces = { "application/json", "application/xml"})
	public List<ServiceLineDim> findUtServiceLines(@RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default)to get all data.") String size
			, @RequestParam(required=false, defaultValue = "O") @Parameter(description="UT Status. O = Open") String ocstatus
			, @RequestParam(required=false, defaultValue = "10J00") @Parameter(description="Brand Code. 10J00 = Global Business Services") String utlevel10
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Growth Platform Code") String utlevel15
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Service Line Code") String utlevel17
			, @RequestParam(required=false, defaultValue = "") @Parameter(description="Additional query string") String addlQueryString
			)  {
		
		UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/api/v1/")
	            .path("ut")
                .queryParam("utlevel10", utlevel10)
                .queryParam("ocstatus", ocstatus);
		
		if (!(utlevel15==null || utlevel15.trim().length()==0 || utlevel15.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel15", utlevel15);
		
		if (!(utlevel17==null || utlevel17.trim().length()==0 || utlevel17.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel17", utlevel17);
		
		if (!(addlQueryString==null || addlQueryString.trim().length()==0 ))
			utUriBuilder.queryParam("addlQueryString", addlQueryString);
  
		String utUri = utUriBuilder.toUriString();
		
		System.out.println("******"+utUri);
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		System.out.println(utNodes.length);
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<ServiceLineDim> serviceLines = new ArrayList<ServiceLineDim>();
		
		for (UtNode utNode: utNodes) {
			ServiceLineDim serviceLine = new ServiceLineDim(utNode.getUtlevel17(),utNode.getUtlevel17description(),utNode.getUtlevel17description(),utNode.getUtlevel15());
			if (!(serviceLines.contains(serviceLine))) serviceLines.add(serviceLine);
		}
		return serviceLines;
	}
	
	@GetMapping(value="/ut/service-line/tax",produces = { "application/json", "application/xml"})
	public List<BrandDim> findUtServiceLineTax(@RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default)to get all data.") String size
			, @RequestParam(required=false, defaultValue = "O") @Parameter(description="UT Status. O = Open") String ocstatus
			, @RequestParam(required=false, defaultValue = "10J00") @Parameter(description="Brand Code. 10J00 = Global Business Services") String utlevel10
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Growth Platform Code") String utlevel15
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Service Line Code") String utlevel17
			, @RequestParam(required=false, defaultValue = "") @Parameter(description="Additional query string") String addlQueryString
			)  {
		
		UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/api/v1/")
	            .path("ut")
                .queryParam("utlevel10", utlevel10)
                .queryParam("ocstatus", ocstatus);
		
		if (!(utlevel15==null || utlevel15.trim().length()==0 || utlevel15.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel15", utlevel15);
		
		if (!(utlevel17==null || utlevel17.trim().length()==0 || utlevel17.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel17", utlevel17);
		
		
		if (!(addlQueryString==null || addlQueryString.trim().length()==0 ))
			utUriBuilder.queryParam("addlQueryString", addlQueryString);
  
		String utUri = utUriBuilder.toUriString();
		
		System.out.println("******"+utUri);
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		System.out.println(utNodes.length);
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<BrandDim> brands = new ArrayList<BrandDim>();
		List<GrowthPlatformDim> growthPlatforms = new ArrayList<GrowthPlatformDim>();
		List<ServiceLineDim> serviceLines = new ArrayList<ServiceLineDim>();
		
		for (UtNode utNode: utNodes) {
			BrandDim brand = new BrandDim(utNode.getUtlevel10(),utNode.getUtlevel10description(),utNode.getUtlevel10description());
			if (!(brands.contains(brand))) brands.add(brand);
			
			GrowthPlatformDim growthPlatform = new GrowthPlatformDim(utNode.getUtlevel15(),utNode.getUtlevel15description(),utNode.getUtlevel15description(),utNode.getUtlevel10());
			if (!(growthPlatforms.contains(growthPlatform))) growthPlatforms.add(growthPlatform);
			
			ServiceLineDim serviceLine = new ServiceLineDim(utNode.getUtlevel17(),utNode.getUtlevel17description(),utNode.getUtlevel17description(),utNode.getUtlevel15());
			if (!(serviceLines.contains(serviceLine))) serviceLines.add(serviceLine);
		}
		
		//BrandDim brand = brands.get(0);
		for (BrandDim brand: brands) {
			for (GrowthPlatformDim growthPlatform: growthPlatforms) {
				if (growthPlatform.getBrandCd().equals(brand.getBrandCd()))
					brand.addChild(growthPlatform);
				for (ServiceLineDim serviceLine: serviceLines) {
					if (serviceLine.getGrowthPlatformCd().equals(growthPlatform.getGrowthPlatformCd()))
						growthPlatform.addChild(serviceLine);
				}
			}
		}
				
		return brands;
	}		
	
	@GetMapping(value="/ut/practice",produces = { "application/json", "application/xml"})
	public List<PracticeDim> findUtPracticess(@RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default)to get all data.") String size
			, @RequestParam(required=false, defaultValue = "O") @Parameter(description="UT Status. O = Open") String ocstatus
			, @RequestParam(required=false, defaultValue = "10J00") @Parameter(description="Brand Code. 10J00 = Global Business Services") String utlevel10
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Growth Platform Code") String utlevel15
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Service Line Code") String utlevel17
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="GBS Practice Code") String gbspracticecode
			, @RequestParam(required=false, defaultValue = "") @Parameter(description="Additional query string") String addlQueryString
			)  {
		
		UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/api/v1/")
	            .path("ut")
                .queryParam("utlevel10", utlevel10)
                .queryParam("ocstatus", ocstatus);
		
		if (!(utlevel15==null || utlevel15.trim().length()==0 || utlevel15.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel15", utlevel15);
		
		if (!(utlevel17==null || utlevel17.trim().length()==0 || utlevel17.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel17", utlevel17);
		
		if (!(gbspracticecode==null || gbspracticecode.trim().length()==0 || gbspracticecode.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("gbspracticecode", gbspracticecode);
		
		if (!(addlQueryString==null || addlQueryString.trim().length()==0 ))
			utUriBuilder.queryParam("addlQueryString", addlQueryString);
  
		String utUri = utUriBuilder.toUriString();
		
		System.out.println("******"+utUri);
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		System.out.println(utNodes.length);
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<PracticeDim> practices = new ArrayList<PracticeDim>();
		
		for (UtNode utNode: utNodes) {
			PracticeDim practice = new PracticeDim(utNode.getGbspracticecode(),utNode.getGbspracticedescription(),utNode.getGbspracticedescription(),utNode.getUtlevel17());
			if (!(practices.contains(practice))) practices.add(practice);
		}
		return practices;
	}
	
	@GetMapping(value="/ut/practice/tax",produces = { "application/json", "application/xml"})
	public List<BrandDim> findUtPracticeTax(@RequestParam(required=false, defaultValue = "ALL") @Parameter(description="Size of the results, use ‘ALL’ (default)to get all data.") String size
			, @RequestParam(required=false, defaultValue = "O") @Parameter(description="UT Status. O = Open") String ocstatus
			, @RequestParam(required=false, defaultValue = "10J00") @Parameter(description="Brand Code. 10J00 = Global Business Services") String utlevel10
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Growth Platform Code") String utlevel15
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="Service Line Code") String utlevel17
			, @RequestParam(required=false, defaultValue = "all") @Parameter(description="GBS Practice Code") String gbspracticecode
			, @RequestParam(required=false, defaultValue = "") @Parameter(description="Additional query string") String addlQueryString
			)  {
		
		UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/api/v1/")
	            .path("ut")
                .queryParam("utlevel10", utlevel10)
                .queryParam("ocstatus", ocstatus);
		
		if (!(utlevel15==null || utlevel15.trim().length()==0 || utlevel15.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel15", utlevel15);
		
		if (!(utlevel17==null || utlevel17.trim().length()==0 || utlevel17.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("utlevel17", utlevel17);
		
		if (!(gbspracticecode==null || gbspracticecode.trim().length()==0 || gbspracticecode.trim().equalsIgnoreCase("all")))
			utUriBuilder.queryParam("gbspracticecode", gbspracticecode);
		
		if (!(addlQueryString==null || addlQueryString.trim().length()==0 ))
			utUriBuilder.queryParam("addlQueryString", addlQueryString);
  
		String utUri = utUriBuilder.toUriString();
		
		System.out.println("******"+utUri);
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		System.out.println(utNodes.length);
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<BrandDim> brands = new ArrayList<BrandDim>();
		List<GrowthPlatformDim> growthPlatforms = new ArrayList<GrowthPlatformDim>();
		List<ServiceLineDim> serviceLines = new ArrayList<ServiceLineDim>();
		List<PracticeDim> practices = new ArrayList<PracticeDim>();
		
		for (UtNode utNode: utNodes) {
			BrandDim brand = new BrandDim(utNode.getUtlevel10(),utNode.getUtlevel10description(),utNode.getUtlevel10description());
			if (!(brands.contains(brand))) brands.add(brand);
			
			GrowthPlatformDim growthPlatform = new GrowthPlatformDim(utNode.getUtlevel15(),utNode.getUtlevel15description(),utNode.getUtlevel15description(),utNode.getUtlevel10());
			if (!(growthPlatforms.contains(growthPlatform))) growthPlatforms.add(growthPlatform);
			
			ServiceLineDim serviceLine = new ServiceLineDim(utNode.getUtlevel17(),utNode.getUtlevel17description(),utNode.getUtlevel17description(),utNode.getUtlevel15());
			if (!(serviceLines.contains(serviceLine))) serviceLines.add(serviceLine);
			
			PracticeDim practice = new PracticeDim(utNode.getGbspracticecode(),utNode.getGbspracticedescription(),utNode.getGbspracticedescription(),utNode.getUtlevel17());
			if (!(practices.contains(practice))) practices.add(practice);
		}
		
		//BrandDim brand = brands.get(0);
		for (BrandDim brand: brands) {
			for (GrowthPlatformDim growthPlatform: growthPlatforms) {
				if (growthPlatform.getBrandCd().equals(brand.getBrandCd()))
					brand.addChild(growthPlatform);
				for (ServiceLineDim serviceLine: serviceLines) {
					if (serviceLine.getGrowthPlatformCd().equals(growthPlatform.getGrowthPlatformCd())) {
						growthPlatform.addChild(serviceLine);
						for (PracticeDim practice: practices) {
							if (practice.getServiceLineCd().equals(serviceLine.getServiceLineCd()))
								serviceLine.addChild(practice);
						}
					}
				}
			}
		}
				
		return brands;
	}		

}
