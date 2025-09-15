package com.ibm.wfm.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.TaxonomyEvaluationResponse;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.TaxonomyEvaluatorService;
import com.ibm.wfm.utils.Helpers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1")
public class TaxonomyEvaluatorContoller {

	@Autowired
	private FileStorageService fss;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private TaxonomyEvaluatorService te;

	@Operation(summary = "Evaluates the current IBM Consulting demographics and how each matches a branch within the JRS standard taxonomy, identifying discrepencies in nodes and/or arcs.")
	@PostMapping("/taxonomy-evaluator-jrs")
	public TaxonomyEvaluationResponse taxonomyEvaluatorJrs(
			@RequestParam(required = false, value = "keyStr", defaultValue = "0,2,16,18,20,15") @Parameter(description = "Offsets within the demographics CSV that map to the taxonomy key values. E.g., 0,2,16,18,20,15") String keyStr,
			@RequestParam(required = false, value = "ofn", defaultValue = "taxonomyEvaluationResults.csv") @Parameter(description = "Output file name.") String outputFileNm,
			@RequestParam(required = false, defaultValue = "true") @Parameter(description = "Include in output only rows for branches in error.") boolean outputErrorsOnly,
			@RequestParam(required = false, defaultValue = "none") @Parameter(description = "Offsets of the data file columns to be included in the output. E.g., 0:3,16:21,15,12,22:24,30,25:27,33,31. Additionally, \'all\' or \'none\' can also be specified.") String dataFileOffsetStr) {
		String delimiter = ",";
		boolean useFullkey = true;

		String[] keysString = keyStr.split(",");
		int[] keyOffsets = new int[keysString.length];
		for (int i = 0; i < keysString.length; i++) {
			keyOffsets[i] = Integer.parseInt(keysString[i]);
		}

		int[] dataFileOffsets = Helpers.parseIntegerList(dataFileOffsetStr);

		/*
		 * Retrieve IBM Consulting Demographics. Use ibm-consulting-demograpics Dynamic
		 * SQL query.
		 */

		/*
		 * Retrieve full JRS taxonomy. Use
		 * /api/v1/eds-ut-jrs-tax/jrss?includeParentage=true
		 */
		//NaryTreeNode rootNode = null;
		String jrsTaxonomyUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/eds-ut-jrs-tax/jrss/")
				.queryParam("includeParentage", true).toUriString();
		ResponseEntity <BrandDim[]> parametersResponseEntity = new RestTemplate().getForEntity(jrsTaxonomyUri, BrandDim[].class); //uriVariables); 
		List<BrandDim> brands = Arrays.asList(parametersResponseEntity.getBody());

		/*
		 * Enrich IBM Consulting Demographics to include codes for Growth Platform,
		 * Service Line, Practice, and Service Area using JRS taxonomy
		 */

		/*
		 * Store enriched IBM Consulting Demographics as CSV for evaluation
		 */
		// http://localhost:8080/api/v1/dyn/sql/rah-dev/ibm-consulting-demograpics/?to-csv=true
		String dataFileName = null;
		String iconDemographicsUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/dyn/sql/")
				.path("/rah-dev/ibm-consulting-demograpics/").queryParam("to-csv", true).toUriString();

		ResponseEntity<Resource> demographicsResponseEntity = new RestTemplate().getForEntity(iconDemographicsUri, Resource.class);
		
		Resource r = demographicsResponseEntity.getBody();
		InputStream is = null;
		
		int growthPlatformOffset = 2;
		int serviceLineOffset = 3;
		int practiceOffset=5;
		int serviceAreaOffset=6;
		
		try {
			is = r.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(is));        
	        String lineBuffer = null;
	         
	        String gpCd = null;
	        String slCd = null;
	        String practiceCd = null;
	        String serviceAreaCd = null;
	        
	        Map<String, Integer> missingGps = new HashMap<String, Integer>();
	        Map<String, Integer> missingSls = new HashMap<String, Integer>();
	        Map<String, Integer> missingPractices = new HashMap<String, Integer>();
	        Map<String, Integer> missingSas = new HashMap<String, Integer>();
	        
	        //BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	        //writer.write(str);
	        
	        //writer.close();
	        
	        
	        while ((lineBuffer = br.readLine()) != null) {
	            String[] columns = Helpers.parseLine(lineBuffer);
	            gpCd = getGpCd(brands, columns[growthPlatformOffset]);
	            if (gpCd==null) {
	            	Integer j = missingGps.get(columns[growthPlatformOffset]);
	            	missingGps.put(columns[growthPlatformOffset], (j == null) ? 1 : j + 1);
	            }
	            slCd = getSlCd(brands, columns[serviceLineOffset]);
	            if (slCd==null) {
	            	Integer j = missingSls.get(columns[serviceLineOffset]);
	            	missingSls.put(columns[serviceLineOffset], (j == null) ? 1 : j + 1);
	            }
	            practiceCd = getPracticeCd(brands, columns[practiceOffset]);
	            if (practiceCd==null) {
	            	Integer j = missingPractices.get(columns[practiceOffset]);
	            	missingPractices.put(columns[practiceOffset], (j == null) ? 1 : j + 1);
	            }
	            serviceAreaCd = getSaCd(brands, columns[serviceAreaOffset]);
	            if (serviceAreaCd==null) {
	            	Integer j = missingSas.get(columns[serviceAreaOffset]);
	            	missingSas.put(columns[serviceAreaOffset], (j == null) ? 1 : j + 1);
	            }
	        }
	        
	        System.out.println("Missing Growth Platforms:");
	        for (Map.Entry<String,Integer> entry : missingGps.entrySet())
	            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	        System.out.println("Missing Service Lines:");
	        for (Map.Entry<String,Integer> entry : missingSls.entrySet())
	            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	        System.out.println("Missing Practices:");
	        for (Map.Entry<String,Integer> entry : missingPractices.entrySet())
	            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	        System.out.println("Missing Service Areas:");
	        for (Map.Entry<String,Integer> entry : missingSas.entrySet())
	            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		/*
		 * Call the evaluation method
		 */
		return null;
		//return te.evaluateTaxonomy(brands.get(0), dataFileName, delimiter, useFullkey, keyOffsets, outputFileNm,
		//		outputErrorsOnly, dataFileOffsets);

	}
	
	private String getGpCd(List<BrandDim> brands, String gpName) {
		for (BrandDim brand: brands) {
			if (brand.getChildren()!=null) {
				for (NaryTreeNode growthPlatform: brand.getChildren()) {
					if (gpName.trim().equalsIgnoreCase(growthPlatform.getDescription().trim())) return growthPlatform.getCode();
				}
			}
		}
		return null;		
	}
	
	private String getSlCd(List<BrandDim> brands, String slName) {
		for (BrandDim brand: brands) {
			if (brand.getChildren()!=null) {
				for (NaryTreeNode growthPlatform: brand.getChildren()) {
					if (growthPlatform.getChildren()!=null) {
						for (NaryTreeNode serviceLine: growthPlatform.getChildren()) {
							if (slName.trim().equalsIgnoreCase(serviceLine.getDescription().trim())) return serviceLine.getCode();
						}
					}
				}
			}
		}
		return null;		
	}
	
	private String getPracticeCd(List<BrandDim> brands, String practiceName) {
		for (BrandDim brand: brands) {
			if (brand.getChildren()!=null) {
				for (NaryTreeNode growthPlatform: brand.getChildren()) {
					if (growthPlatform.getChildren()!=null) {
						for (NaryTreeNode serviceLine: growthPlatform.getChildren()) {
							for (NaryTreeNode practice: serviceLine.getChildren()) {
								if (practiceName.trim().equalsIgnoreCase(practice.getDescription().trim())) return practice.getCode();
							}
						}
					}
				}
			}
		}
		return null;		
	}
	
	private String getSaCd(List<BrandDim> brands, String saName) {
		for (BrandDim brand: brands) {
			if (brand.getChildren()!=null) {
				for (NaryTreeNode growthPlatform: brand.getChildren()) {
					if (growthPlatform.getChildren()!=null) {
						for (NaryTreeNode serviceLine: growthPlatform.getChildren()) {
							for (NaryTreeNode practice: serviceLine.getChildren()) {
								for (NaryTreeNode sa: practice.getChildren()) {
									if (saName.trim().equalsIgnoreCase(sa.getDescription().trim())) return sa.getCode();
								}
							}
						}
					}
				}
			}
		}
		return null;		
	}	

	@Operation(summary = "Evaluates if a branch within a data file matches a branch within a standard taxonomy, identifying discrepencies in nodes and/or arcs.")
	@PostMapping(value="/taxonomy-evaluator-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TaxonomyEvaluationResponse taxonomyEvaluatorUpload(
			@RequestParam(value = "tax", required = true) @Parameter(description = "CSV containing standard taxonomy.") MultipartFile taxFile,
			@RequestParam(value = "data", required = true) @Parameter(description = "CSV containing data to be evaluated agaist the taxonomy.") MultipartFile dataFile,
			@RequestParam(defaultValue = ",") @Parameter(description = "Data and taxonomy file delimiter.") String delimiter,
			@RequestParam("keyStr") @Parameter(description = "Offsets within the CSV that map to the taxonomy key values. E.g., 0,2,16,18,20,15") String keyStr,
			@RequestParam(required = false, value = "ofn", defaultValue = "taxonomyEvaluationResults.csv") @Parameter(description = "Output file name.") String ofn,
			@RequestParam(required = false, defaultValue = "true") @Parameter(description = "Include in output only rows for branches in error.") boolean outputErrorsOnly,
			@RequestParam(required = false, defaultValue = "none") @Parameter(description = "Offsets of the data file columns to be included in the output. E.g., 0:3,16:21,15,12,22:24,30,25:27,33,31. Additionally, \'all\' or \'none\' can also be specified.") String dataFileOffsetStr) {

		Date startTime = new Date();
		Date taxUploadStartTime;
		Date dataUploadStartTime;

		if (ofn == null)
			ofn = "taxonomyEvaluationResults.csv";

		String uploadDir = fileStorageProperties.getUploadDir();

		taxUploadStartTime = new Date();
		System.out.println("taxUploadStartTime: " + taxUploadStartTime);
		String uploadTaxonomyFile = fss.storeFile(taxFile);
		dataUploadStartTime = new Date();
		System.out.println("dataUploadStartTime: " + dataUploadStartTime);
		String uploadDataFile = fss.storeFile(dataFile);
		System.out.println("uploads completed at: " + new Date());
		String taxFileName = uploadDir + "/" + uploadTaxonomyFile; // "/Users/steve/$WFM/wf360/data/jrs_taxononomy.csv";
		String dataFileName = uploadDir + "/" + uploadDataFile; // "/Users/steve/$WFM/wf360/data/rah_people_data.csv";
		boolean useFullkey = true;

		String[] keysString = keyStr.split(",");
		int[] keyOffsets = new int[keysString.length];
		for (int i = 0; i < keysString.length; i++) {
			keyOffsets[i] = Integer.parseInt(keysString[i]);
		}

		int[] dataFileOffsets = Helpers.parseIntegerList(dataFileOffsetStr);

		TaxonomyEvaluationResponse ter = te.evaluateTaxonomy(taxFileName, dataFileName, delimiter, useFullkey,
				keyOffsets, ofn, outputErrorsOnly, dataFileOffsets);

		ter.setStartTime(startTime);
		ter.setDataUploadStartTime(dataUploadStartTime);
		ter.setTaxUploadStartTime(taxUploadStartTime);

		return ter;

	}

	@Operation(summary = "Evaluates if a branch within a data file matches a branch within a standard taxonomy, identifying discrepencies in nodes and/or arcs.")
	@PostMapping("/taxonomy-evaluator-file")
	public TaxonomyEvaluationResponse taxonomyEvaluatorFile(
			@RequestParam(value = "tax", required = true) @Parameter(description = "Name of CSV containing standard taxonomy.") String taxFile,
			@RequestParam(value = "data", required = true) @Parameter(description = "Name of CSV containing data to be evaluated agaist the taxonomy.") String dataFile,
			@RequestParam(defaultValue = ",") @Parameter(description = "Data and taxonomy file delimiter.") String delimiter,
			@RequestParam("keyStr") @Parameter(description = "Offsets within the CSV that map to the taxonomy key values. E.g., 0,2,16,18,20,15") String keyStr,
			@RequestParam(required = false, value = "ofn", defaultValue = "taxonomyEvaluationResults.csv") @Parameter(description = "Output file name.") String ofn,
			@RequestParam(required = false, defaultValue = "true") @Parameter(description = "Include in output only rows for branches in error.") boolean outputErrorsOnly,
			@RequestParam(required = false, defaultValue = "none") @Parameter(description = "Offsets of the data file columns to be included in the output. E.g., 0:3,16:21,15,12,22:24,30,25:27,33,31. Additionally, \'all\' or \'none\' can also be specified.") String dataFileOffsetStr) {

		Date startTime = new Date();
		Date taxUploadStartTime = new Date();
		;
		Date dataUploadStartTime = new Date();
		;

		if (ofn == null)
			ofn = "taxonomyEvaluationResults.csv";

		String uploadDir = fileStorageProperties.getUploadDir();

		String taxFileName = uploadDir + "/" + taxFile; // "/Users/steve/$WFM/wf360/data/jrs_taxononomy.csv";
		String dataFileName = uploadDir + "/" + dataFile; // "/Users/steve/$WFM/wf360/data/rah_people_data.csv";
		boolean useFullkey = true;

		String[] keysString = keyStr.split(",");
		int[] keyOffsets = new int[keysString.length];
		for (int i = 0; i < keysString.length; i++) {
			keyOffsets[i] = Integer.parseInt(keysString[i]);
		}

		int[] dataFileOffsets = Helpers.parseIntegerList(dataFileOffsetStr);

		TaxonomyEvaluationResponse ter = te.evaluateTaxonomy(taxFileName, dataFileName, delimiter, useFullkey,
				keyOffsets, ofn, outputErrorsOnly, dataFileOffsets);

		ter.setStartTime(startTime);
		ter.setDataUploadStartTime(dataUploadStartTime);
		ter.setTaxUploadStartTime(taxUploadStartTime);

		return ter;

	}

	@Operation(summary = "Evaluates if a branch within a data file matches a branch within a standard taxonomy, identifying discrepencies in nodes and/or arcs.")
	@PostMapping(value="/taxonomy-evaluator-api", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TaxonomyEvaluationResponse taxonomyEvaluatorApi(
			@RequestParam(value="tax-api", required=false, defaultValue="/api/v1/eds-ut-jrs-tax/jrss/") @Parameter(description = "API to retrieve standard taxonomy.") String taxApi,
			@RequestParam(value = "data", required = true) @Parameter(description = "CSV containing data to be evaluated agaist the taxonomy.") MultipartFile dataFile,
			@RequestParam("keyStr") @Parameter(description = "Offsets within API data that map to the taxonomy key values. E.g., 0,2,16,18,20,15") String keyStr,
			@RequestParam(required = false, value = "ofn", defaultValue = "taxonomyEvaluationResults.csv") @Parameter(description = "Output file name.") String ofn,
			@RequestParam(required = false, defaultValue = "true") @Parameter(description = "Include in output only rows for branches in error.") boolean outputErrorsOnly,
			@RequestParam(required = false, defaultValue = "none") @Parameter(description = "Offsets of the data within API data columns to be included in the output. E.g., 0:3,16:21,15,12,22:24,30,25:27,33,31. Additionally, \'all\' or \'none\' can also be specified.") String dataFileOffsetStr) {

		String delimiter = ",";
		boolean useFullkey = true;

		String[] keysString = keyStr.split(",");
		int[] keyOffsets = new int[keysString.length];
		for (int i = 0; i < keysString.length; i++) {
			keyOffsets[i] = Integer.parseInt(keysString[i]);
		}

		int[] dataFileOffsets = Helpers.parseIntegerList(dataFileOffsetStr);

		/*
		 * Retrieve full JRS taxonomy. Use
		 * /api/v1/eds-ut-jrs-tax/jrss?includeParentage=true
		 */
		System.out.println("Start /api/v1/eds-ut-jrs-tax/jrss: " + new Date());
		String jrsTaxonomyUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/eds-ut-jrs-tax/jrss/")
				.queryParam("includeParentage", true).toUriString();
		ResponseEntity <BrandDim[]> parametersResponseEntity = new RestTemplate().getForEntity(jrsTaxonomyUri, BrandDim[].class); //uriVariables); 
		List<BrandDim> brands = Arrays.asList(parametersResponseEntity.getBody());
		
		System.out.println("Completed /api/v1/eds-ut-jrs-tax/jrss: " + new Date());
		
		String uploadDir = fileStorageProperties.getUploadDir();
		
		System.out.println("Start data upload: " + new Date());
		String uploadDataFile = fss.storeFile(dataFile);
		System.out.println("Completed data upload: " + new Date());
		String dataFileName = uploadDir + "/" + uploadDataFile; // "/Users/steve/$WFM/wf360/data/rah_people_data.csv";
		
		System.out.println("Start te.evaluateTaxonomy: " + new Date());
		TaxonomyEvaluationResponse ter = te.evaluateTaxonomy(brands.get(0), dataFileName, delimiter, useFullkey,
				keyOffsets, ofn, outputErrorsOnly, dataFileOffsets);
		System.out.println("Completed te.evaluateTaxonomy: " + new Date());

		return ter;

	}

}
