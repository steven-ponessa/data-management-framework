package com.ibm.wfm.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.EtlResponse;
import com.ibm.wfm.beans.GbsShortListDim;
import com.ibm.wfm.beans.GrowthPlatformDim;
import com.ibm.wfm.beans.JrsDim;
import com.ibm.wfm.beans.OfferingComponentDim;
import com.ibm.wfm.beans.OfferingDim;
import com.ibm.wfm.beans.OfferingPortfolioDim;
import com.ibm.wfm.beans.PracticeDim;
import com.ibm.wfm.beans.RahServiceAreaDim;
import com.ibm.wfm.beans.ServiceAreaDim;
import com.ibm.wfm.beans.ServiceLineDim;
import com.ibm.wfm.beans.UtNode;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.ShortListDaoService;
import com.ibm.wfm.utils.DataMarshaller;
import com.ibm.wfm.utils.FileHelpers;
import com.ibm.wfm.utils.Helpers;

@RestController
@RequestMapping("/api/v1")
public class JrsTaxonomyEtlController extends AbstractDaoController {
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private ShortListDaoService shortListDaoService;
	
	@PostMapping(path="/jrs-taxonomy/idl-sa-jrs",produces = { "application/json", "application/xml"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public List<EtlResponse> jrsTaxonomyIdlServiceAreasJrs(@RequestParam("shortListFileName") MultipartFile shortListFileName
			, @RequestParam(value="tabName",defaultValue="Consulting Short List",required=false) String tabName) throws IOException, NoSuchMethodException, SecurityException {

		String excelFileName = fileStorageService.storeFile(shortListFileName);

		shortListDaoService.setT(GbsShortListDim.class);
		shortListDaoService.setTableNm("REFT.GBS_SHORT_LIST_DIM_V");
		shortListDaoService.setScdTableNm("REFT.GBS_SHORT_LIST_SCD_V");

		//deprecated
		//return shortListDaoService.getShortListFromExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
		List<GbsShortListDim> shortLists = shortListDaoService.getObjectListFromExcel(fileStorageProperties.getUploadDir()+"/"+excelFileName, tabName);
		
		List<EtlResponse> etlResponses = new ArrayList<>();
		
		/*
		 * Perform ETL for up to paractice
		 */
		/*
		 * Assign practice codes to Short List practices. Note that these practice include GBS extensions and overrides
		 */
		
		List<PracticeDim> practices = new ArrayList<PracticeDim>();
		String practiceslUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		        .path("/api/v1/")
		        .path("eds-ut-jrs-tax/practices")
		        .toUriString();
		ResponseEntity<PracticeDim[]> practiceResponseEntity = new RestTemplate().getForEntity(practiceslUri, PracticeDim[].class); //uriVariables); 
		PracticeDim[] practiceArray = practiceResponseEntity.getBody();
		practices = Arrays.asList(practiceArray);
		
		int i=0;
		for (GbsShortListDim shortList: shortLists) {
			String practiceCd = null;
			for (PracticeDim practice: practices) {
				if (practice.getPracticeNm().trim().equalsIgnoreCase(shortList.getPracticeNm().trim())) {
					if (shortList.getPracticeNm().trim().equals("N/A - Service Area not allocated")) {
						/*
						if (shortList.getServiceLineNm().equalsIgnoreCase("Hybrid Cloud Management")) practiceCd = "GBS210";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Hybrid Cloud Transformation")) practiceCd = "GBS211";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Data & Technology Transformation")) practiceCd = "GBS212";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Finance & Supply Chain Transformation")) practiceCd = "GBS213";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Cognitive Process Services")) practiceCd = "GBS214";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Enterprise Strategy")) practiceCd = "GBS215";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Industry")) practiceCd = "GBS216";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Next Gen EA")) practiceCd = "GBS217";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("HR & Talent Transformationn")) practiceCd = "GBS218";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Customer Transformation")) practiceCd = "GBS219";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Cybersecurity")) practiceCd = "GBS220";
						*/
						if (shortList.getServiceLineNm().equalsIgnoreCase("Application Operations")) practiceCd = "GBS210";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Strategy & Transformation")) practiceCd = "GBS211";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Hybrid Cloud & Data")) practiceCd = "GBS212";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Cybersecurity")) practiceCd = "GBS213";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Business Applications")) practiceCd = "GBS214";
						else if (shortList.getServiceLineNm().equalsIgnoreCase("Business Operations")) practiceCd = "GBS215";
						else {
							System.out.println(i + ". ******* Practice not found: "+shortList.getPracticeNm()+", Service Line: "+shortList.getServiceLineNm());
							practiceCd = "GBS999";
						}
					}
					else {
						practiceCd = practice.getCode();
					}
					break;	
				}
			}
			i++;
			if (practiceCd==null) System.out.println(i+". ******* Practice not found: "+shortList.getPracticeNm());
			else {
				shortList.setPracticeCd(practiceCd);
			}
		}
		
		/*
		 * Temporarily commented out and RAH Service Area Dimension data read from a static file
		 *
		List<RahServiceAreaDim> rahServiceAreas = new ArrayList<>();
		String rahServiceAreaUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		        .path("/api/v1/")
		        .path("rah/service-areas")
		        .queryParam("filters", "ACTV_FLG='Y'")
		        .toUriString();
		ResponseEntity<RahServiceAreaDim[]> rahServiceAreaResponseEntity = new RestTemplate().getForEntity(rahServiceAreaUri, RahServiceAreaDim[].class); //uriVariables); 
		RahServiceAreaDim[] rahServiceAreaArray = rahServiceAreaResponseEntity.getBody();
		rahServiceAreas = Arrays.asList(rahServiceAreaArray);
		*/
		/* Temporary code to read RAH Service Area Dimension data read from a static file
		 * 
		 */
		String className = "com.ibm.wfm.beans.RahServiceAreaDim";
		String rahExcelFileName = "/Users/steve/$WFM/ReferenceData/2025-startup/rah-service-area-dim-2025-08-31.xlsx";
		String rahExcelTabName = "rah-service-area-dim-2025-08-31"; //"rah-service-area-dim-2025-08-31";
		List<Object> rahServiceAreasObj = DataMarshaller.buildObjectListFromExcel(className, rahExcelFileName,  rahExcelTabName);
		
		// Casting List<Object> to List<AnotherObject>
        List<RahServiceAreaDim> rahServiceAreas = Helpers.castList(rahServiceAreasObj, RahServiceAreaDim.class);
		
		//rahServiceAreas.forEach(item -> System.out.println(item.toEtlString()));
        //* End Temporary code
        
		
		/*
		 * Build Service Area and JRS* lists from the Short List.
		 * Note: that the service area code is the last two characters of the service area description.
		 */
		List<ServiceAreaDim> serviceAreas = new ArrayList<ServiceAreaDim>();
		List<JrsDim> jrss = new ArrayList<JrsDim>();
		i=0;
		for (GbsShortListDim shortList: shortLists) {
			String serviceAreaCd = null;
			for (RahServiceAreaDim rahServiceArea: rahServiceAreas) {
				if (shortList.getServiceAreaNm().trim().equals(rahServiceArea.getSvcAreaNm())) {
					serviceAreaCd = rahServiceArea.getSvcAreaCd();
					break;
				}
			}
			i++;
			if (serviceAreaCd==null) {
				System.out.println(i+". ******* Service Area not found: "+shortList.getServiceAreaNm());
				if (shortList.getServiceAreaNm().equalsIgnoreCase("Global Facilitation Center")) {
					serviceAreaCd = "ZZ";
					shortList.setServiceAreaCd(serviceAreaCd);
				}
			}
			else {
				shortList.setServiceAreaCd(serviceAreaCd);
			}
			//shortList.setServiceAreaCd
			ServiceAreaDim serviceArea = new ServiceAreaDim(shortList.getServiceAreaCd()==null?"ZZ":shortList.getServiceAreaCd().trim()
                                                          , shortList.getServiceAreaNm().trim()
                                                          , shortList.getServiceAreaNm().trim()
                                                          , shortList.getPracticeCd()
                                                          );
			if (!(serviceAreas.contains(serviceArea))) serviceAreas.add(serviceArea);

			JrsDim jrs = new JrsDim(-1
					, shortList.getJobRoleCd().trim()+"-"+shortList.getSpecialtyCd().trim()
					, shortList.getJrsNm()
					, shortList.getJrsNm()
					, shortList.getJobRoleCd()
					, shortList.getJobRoleNm()
					, shortList.getSpecialtyCd()
					, shortList.getSpecialtyNm()
					, shortList.getServiceAreaCd()
					, shortList.getCmpnstnGrdLst()
					, shortList.getIncentiveFlg()
					, shortList.getRecoveryAdderCicIndiaNm()
					, shortList.getRecoveryAdderCicOtherNm()
					, shortList.getPrimaryJrsCnt()
					, shortList.getSecondaryJrsCnt()
					, shortList.getJrsPrimaryJobCategoryNm()
					, shortList.getJrsCamssNm()
					, shortList.getSvfGroupNm()
					);
			if (!(jrss.contains(jrs))) jrss.add(jrs);	
		}
		
		Collections.sort(serviceAreas); 
		Collections.sort(jrss); 
		//Reverse order
		//Collections.sort(serviceAreas, Collections.reverseOrder()); 
		
		/*
		 * Write the lists for service areas and JRSs into files for ETL ladder processing.
		 */
		String rootFileName = "service-areas";
		BufferedWriter serviceAreaWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/"+rootFileName+"-new.csv"));
		boolean header = true;
		for (ServiceAreaDim serviceArea: serviceAreas) {
			if (header) {
				serviceAreaWriter.write(serviceArea.getEtlHeader());
				header = false;
			}
			serviceAreaWriter.write(System.lineSeparator()+serviceArea.toEtlString());
		}
		serviceAreaWriter.flush();
		serviceAreaWriter.close();
				
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(this.processIdl("eds-ut-jrs-tax", rootFileName));

		
		/*
		 * Write the lists for JRSs into files for ETL ladder processing.
		 */
		rootFileName = "jrss";
		BufferedWriter jrsWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/"+rootFileName+"-new.csv"));
		header = true;
		for (JrsDim jrs: jrss) {
			if (header) {
				jrsWriter.write(jrs.getEtlHeader());
				header = false;
			}
			jrsWriter.write(System.lineSeparator()+jrs.toEtlString());
		}
		jrsWriter.flush();
		jrsWriter.close();		
				
		/*
		 * Calling the Service Area dimension's ETL processing /api/v1/eds/service-area/etl api 
		 */
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(this.processIdl("eds-ut-jrs-tax", rootFileName));

				
		
		return etlResponses;
	}
	
	@PostMapping(path="/jrs-taxonomy/idl-brand-2-offering-component",produces = { "application/json", "application/xml"})
	public List<EtlResponse> jrsTaxonomyIdl() throws IOException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		
		List<EtlResponse> etlResponses = new ArrayList<>();
		
		/*
		 * Retrieve the UT by calling the /api/v1/ut api using the default parameters of 
		 *   ocstatus: O (open offerings)
		 *   utlevel10: 10J00 (GBS)
		 */
		String utUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("ut-all")
                .toUriString();
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<BrandDim> brands = new ArrayList<BrandDim>();
		List<GrowthPlatformDim> growthPlatforms = new ArrayList<GrowthPlatformDim>();
		List<ServiceLineDim> serviceLines = new ArrayList<ServiceLineDim>();
		List<PracticeDim> practices = new ArrayList<PracticeDim>();
		List<OfferingPortfolioDim> offeringPortfolios = new ArrayList<>();
		List<OfferingDim> offerings = new ArrayList<>();
		List<OfferingComponentDim> occs = new ArrayList<>();
		
		for (UtNode utNode: utNodes) {
			BrandDim brand = new BrandDim(utNode.getUtlevel10().trim(),utNode.getUtlevel10description(),utNode.getUtlevel10description());
			if (!(brands.contains(brand))) brands.add(brand);
			
			GrowthPlatformDim growthPlatform = new GrowthPlatformDim(utNode.getUtlevel15().trim(),utNode.getUtlevel15description(),utNode.getUtlevel15description(),utNode.getUtlevel10().trim());
			if (!(growthPlatforms.contains(growthPlatform))) growthPlatforms.add(growthPlatform);
			
			ServiceLineDim serviceLine = new ServiceLineDim(utNode.getUtlevel17().trim(),utNode.getUtlevel17description(),utNode.getUtlevel17description(),utNode.getUtlevel15().trim());
			if (!(serviceLines.contains(serviceLine))) serviceLines.add(serviceLine);
			
			PracticeDim practice = new PracticeDim(utNode.getGbspracticecode()==null?"GBS000":utNode.getGbspracticecode().trim()
					                              ,utNode.getGbspracticedescription()==null?"Null Practice":utNode.getGbspracticedescription()
					                              ,utNode.getGbspracticedescription(),utNode.getUtlevel17().trim());
			if (!(practices.contains(practice))) practices.add(practice);
			
			OfferingPortfolioDim offeringPortfolio = new OfferingPortfolioDim(utNode.getUtlevel20().trim(),utNode.getUtlevel20description(),utNode.getUtlevel20description(),utNode.getUtlevel17().trim());
			if (!(offeringPortfolios.contains(offeringPortfolio))) offeringPortfolios.add(offeringPortfolio);
			
			OfferingDim offering = new OfferingDim(utNode.getUtlevel30().trim(),utNode.getUtlevel30description(),utNode.getUtlevel30description()
					, utNode.getGbspracticecode()  			//leadPracticeCd
					, utNode.getGbspracticedescription()	//leadPracticeNm
					, utNode.getGbsofferingattribute()		//financialOfferingAttributeNm
					,utNode.getUtlevel20().trim());
			if (!(offerings.contains(offering))) offerings.add(offering);
			
		}
		
		//copy into new List<POJO> via map
		File jsonFile = new File(fileStorageProperties.getMapDir()+"/ut-node-occ.json");
		HashMap<String, String> map = new ObjectMapper().readValue(jsonFile, HashMap.class);
		//Populate the map.
		List<OfferingComponentDim> offeringComponents = DataMarshaller.mapList(Arrays.asList(utNodes), UtNode.class, OfferingComponentDim.class, map);
		
		Collections.sort(brands); 
		Collections.sort(growthPlatforms); 
		Collections.sort(serviceLines); 
		Collections.sort(practices); 
		Collections.sort(offeringPortfolios); 
		Collections.sort(offerings); 
		Collections.sort(offeringComponents); 
		
		/*
		 * Write the lists for Brand, Growth Platform, Service Line, and Practice into files for ETL ladder processing.
		 */
		BufferedWriter brandWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/brandDim-new.csv"));
		boolean header = true;
		for (BrandDim brand: brands) {
			if (header) {
				brandWriter.write(BrandDim.getEtlHeader());
				header = false;
			}
			brandWriter.write(System.lineSeparator()+brand.toEtlString());
		}
		brandWriter.flush();
		brandWriter.close();
		
		BufferedWriter growthPlatformWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/growthPlatformDim-new.csv"));
		header = true;
		for (GrowthPlatformDim growthPlatform: growthPlatforms) {
			if (header) {
				growthPlatformWriter.write(growthPlatform.getEtlHeader());
				header = false;
			}
			growthPlatformWriter.write(System.lineSeparator()+growthPlatform.toEtlString());
		}
		growthPlatformWriter.flush();
		growthPlatformWriter.close();
		
		BufferedWriter serviceLineWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/serviceLineDim-new.csv"));
		header = true;
		for (ServiceLineDim serviceLine: serviceLines) {
			if (header) {
				serviceLineWriter.write(serviceLine.getEtlHeader());
				header = false;
			}
			serviceLineWriter.write(System.lineSeparator()+serviceLine.toEtlString());
		}
		serviceLineWriter.flush();
		serviceLineWriter.close();
		

		BufferedWriter practiceWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/practiceDim-ut.csv"));
		header = true;
		for (PracticeDim practice: practices) {
			if (header) {
				practiceWriter.write(practice.getEtlHeader());
				header = false;
			}
			practiceWriter.write(System.lineSeparator()+practice.toEtlString());
		}
		practiceWriter.flush();
		practiceWriter.close();
		
		BufferedWriter offeringPortfolioWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/offeringPortfolioDim-new.csv"));
		header = true;
		for (OfferingPortfolioDim offeringPortfolio: offeringPortfolios) {
			if (header) {
				offeringPortfolioWriter.write(offeringPortfolio.getEtlHeader());
				header = false;
			}
			offeringPortfolioWriter.write(System.lineSeparator()+offeringPortfolio.toEtlString());
		}
		offeringPortfolioWriter.flush();
		offeringPortfolioWriter.close();
		
		BufferedWriter offeringWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/offeringDim-new.csv"));
		header = true;
		for (OfferingDim offering: offerings) {
			if (header) {
				offeringWriter.write(offering.getEtlHeader());
				header = false;
			}
			offeringWriter.write(System.lineSeparator()+offering.toEtlString());
		}
		offeringWriter.flush();
		offeringWriter.close();
		
		BufferedWriter offeringComponentWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/offeringComponentDim-new.csv"));
		header = true;
		for (OfferingComponentDim offeringComponent: offeringComponents) {
			if (header) {
				offeringComponentWriter.write(offeringComponent.getEtlHeader());
				header = false;
			}
			offeringComponentWriter.write(System.lineSeparator()+offeringComponent.toEtlString());
		}
		offeringComponentWriter.flush();
		offeringComponentWriter.close();
		/////////////////////////////////////////////////
		/*
		 * Calling the dimensions IDL processing /api/v1/eds/{dimension}/idl api 
		 */
		String[] dimensions = {"brand","growthPlatform","serviceLine","offeringPortfolio","offering","offeringComponent"};
		//String[] dimensions = {"brand"};
		for (String dimension: dimensions) {
			String dataFileName = dimension+"Dim-new.csv";
			String urlName = Helpers.fromCamelCase(dimension, "-")+"s"; 
			String rootFileName = dimension;
			// Build URI for the dimension IDL Service
			String dimensionIdllUri = ServletUriComponentsBuilder.fromCurrentContextPath()
			        .path("/api/v1/")
			        .path("eds-ut-jrs-tax/"+urlName+"/idl")
			        .toUriString();
		
			// Set tye URI Parameter values
			String newFileName = fileStorageProperties.getUploadDir()+"/"+dataFileName;
			String outputFileName = "/" + rootFileName + "Dim-idl.csv";
			
			FileSystemResource newFileResource = new FileSystemResource(new File(newFileName));
		
			// adding headers to the api
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			//headers.set("x-key", API_KEY);
		
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("new-file", newFileResource);
			body.add("output-file-name", outputFileName);
		
			HttpEntity<MultiValueMap<String, Object>> requestEntity= new HttpEntity<>(body, headers);
		
			// Call the fine grained service using RestTemplate
			ResponseEntity<EtlResponse> dimensionIdlResponseEntity = new RestTemplate().postForEntity(dimensionIdllUri, requestEntity, EtlResponse.class);
		
			// Add the returned EtlResponse object to the list of responses
			etlResponses.add(dimensionIdlResponseEntity.getBody());	
		}
		
		
		return etlResponses;
		
	}
	
	@PostMapping(path="/jrs-taxonomy/etl-brand-2-offering-component",produces = { "application/json", "application/xml"})
	public List<EtlResponse> jrsTaxonomyEtl() throws IOException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		
		List<EtlResponse> etlResponses = new ArrayList<>();
		
		/*
		 * Retrieve the UT by calling the /api/v1/ut api using the default parameters of 
		 *   ocstatus: O (open offerings)
		 *   utlevel10: 10J00 (GBS)
		 */
		String utUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("ut-all")
                .toUriString();
		
		ResponseEntity<UtNode[]> responseEntity = new RestTemplate().getForEntity(utUri, UtNode[].class); //uriVariables); 
		UtNode[] utNodes = responseEntity.getBody();
		
		/*
		 * Parse the returned UT into Brand, Growth Platform, Service Line, and Practice lists
		 */
		List<BrandDim> brands = new ArrayList<BrandDim>();
		List<GrowthPlatformDim> growthPlatforms = new ArrayList<GrowthPlatformDim>();
		List<ServiceLineDim> serviceLines = new ArrayList<ServiceLineDim>();
		List<PracticeDim> practices = new ArrayList<PracticeDim>();
		List<OfferingPortfolioDim> offeringPortfolios = new ArrayList<>();
		List<OfferingDim> offerings = new ArrayList<>();
		List<OfferingComponentDim> occs = new ArrayList<>();
		
		for (UtNode utNode: utNodes) {
			BrandDim brand = new BrandDim(utNode.getUtlevel10().trim(),utNode.getUtlevel10description(),utNode.getUtlevel10description());
			if (!(brands.contains(brand))) brands.add(brand);
			
			GrowthPlatformDim growthPlatform = new GrowthPlatformDim(utNode.getUtlevel15().trim(),utNode.getUtlevel15description(),utNode.getUtlevel15description(),utNode.getUtlevel10().trim());
			if (!(growthPlatforms.contains(growthPlatform))) growthPlatforms.add(growthPlatform);
			
			ServiceLineDim serviceLine = new ServiceLineDim(utNode.getUtlevel17().trim(),utNode.getUtlevel17description(),utNode.getUtlevel17description(),utNode.getUtlevel15().trim());
			if (!(serviceLines.contains(serviceLine))) serviceLines.add(serviceLine);
			
			PracticeDim practice = new PracticeDim(utNode.getGbspracticecode()==null?"GBS000":utNode.getGbspracticecode().trim()
					                              ,utNode.getGbspracticedescription()==null?"Null Practice":utNode.getGbspracticedescription()
					                              ,utNode.getGbspracticedescription(),utNode.getUtlevel17().trim());
			if (!(practices.contains(practice))) practices.add(practice);
			
			OfferingPortfolioDim offeringPortfolio = new OfferingPortfolioDim(utNode.getUtlevel20().trim(),utNode.getUtlevel20description(),utNode.getUtlevel20description(),utNode.getUtlevel17().trim());
			if (!(offeringPortfolios.contains(offeringPortfolio))) offeringPortfolios.add(offeringPortfolio);
			
			OfferingDim offering = new OfferingDim(utNode.getUtlevel30().trim(),utNode.getUtlevel30description(),utNode.getUtlevel30description()
					, utNode.getGbspracticecode()  			//leadPracticeCd
					, utNode.getGbspracticedescription()	//leadPracticeNm
					, utNode.getGbsofferingattribute()		//financialOfferingAttributeNm
					,utNode.getUtlevel20().trim());
			if (!(offerings.contains(offering))) offerings.add(offering);
			
		}
		
		//copy into new List<POJO> via map
		HashMap<String, String> map = new ObjectMapper().readValue(new File(fileStorageProperties.getMapDir()+"/ut-node-occ.json"), HashMap.class);
		//Populate the map.
		List<OfferingComponentDim> offeringComponents = DataMarshaller.mapList(Arrays.asList(utNodes), UtNode.class, OfferingComponentDim.class, map);
		
		Collections.sort(brands); 
		Collections.sort(growthPlatforms); 
		Collections.sort(serviceLines); 
		Collections.sort(practices); 
		Collections.sort(offeringPortfolios); 
		Collections.sort(offerings); 
		Collections.sort(offeringComponents); 
		
		/*
		 * Write the lists for Brand, Growth Platform, Service Line, and Practice into files for ETL ladder processing.
		 */
		BufferedWriter brandWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/brandDim-new.csv"));
		boolean header = true;
		for (BrandDim brand: brands) {
			if (header) {
				brandWriter.write(BrandDim.getEtlHeader());
				header = false;
			}
			brandWriter.write(System.lineSeparator()+brand.toEtlString());
		}
		brandWriter.flush();
		brandWriter.close();
		
		BufferedWriter growthPlatformWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/growthPlatformDim-new.csv"));
		header = true;
		for (GrowthPlatformDim growthPlatform: growthPlatforms) {
			if (header) {
				growthPlatformWriter.write(growthPlatform.getEtlHeader());
				header = false;
			}
			growthPlatformWriter.write(System.lineSeparator()+growthPlatform.toEtlString());
		}
		growthPlatformWriter.flush();
		growthPlatformWriter.close();
		
		BufferedWriter serviceLineWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/serviceLineDim-new.csv"));
		header = true;
		for (ServiceLineDim serviceLine: serviceLines) {
			if (header) {
				serviceLineWriter.write(serviceLine.getEtlHeader());
				header = false;
			}
			serviceLineWriter.write(System.lineSeparator()+serviceLine.toEtlString());
		}
		serviceLineWriter.flush();
		serviceLineWriter.close();
		
		BufferedWriter offeringPortfolioWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/offeringPortfolioDim-new.csv"));
		header = true;
		for (OfferingPortfolioDim offeringPortfolio: offeringPortfolios) {
			if (header) {
				offeringPortfolioWriter.write(offeringPortfolio.getEtlHeader());
				header = false;
			}
			offeringPortfolioWriter.write(System.lineSeparator()+offeringPortfolio.toEtlString());
		}
		offeringPortfolioWriter.flush();
		offeringPortfolioWriter.close();
		
		BufferedWriter offeringWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/offeringDim-new.csv"));
		header = true;
		for (OfferingDim offering: offerings) {
			if (header) {
				offeringWriter.write(offering.getEtlHeader());
				header = false;
			}
			offeringWriter.write(System.lineSeparator()+offering.toEtlString());
		}
		offeringWriter.flush();
		offeringWriter.close();
		
		BufferedWriter offeringComponentWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/offeringComponentDim-new.csv"));
		header = true;
		for (OfferingComponentDim offeringComponent: offeringComponents) {
			if (header) {
				offeringComponentWriter.write(offeringComponent.getEtlHeader());
				header = false;
			}
			offeringComponentWriter.write(System.lineSeparator()+offeringComponent.toEtlString());
		}
		offeringComponentWriter.flush();
		offeringComponentWriter.close();
		
		/*
		BufferedWriter practiceWriter = new BufferedWriter(new FileWriter(fileStorageProperties.getUploadDir()+"/practiceDim-ut.csv"));
		header = true;
		for (PracticeDim practice: practices) {
			if (header) {
				practiceWriter.write(practice.getEtlHeader());
				header = false;
			}
			practiceWriter.write(System.lineSeparator()+practice.toEtlString());
		}
		practiceWriter.flush();
		practiceWriter.close();
		*/
		
		/*
		 * Calling the Brand dimension's ETL processing /api/v1/eds/brand/etl api 
		 */
		String rootFileName = "brand";
		// Build URI for the Brand ETL Service
		String brandEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		        .path("/api/v1/")
		        .path("eds-ut-jrs-tax/brands/etl")
		        .toUriString();
		
		// Set tye URI Parameter values
		String oldFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv";
		String newFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv";
		String outputFileName = "/brandDim-etl.csv";
		
		// getting the file from disk
		FileSystemResource oldFileResource = null;
		if (FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv")) 
			oldFileResource = new FileSystemResource(new File(oldFileName));
		FileSystemResource newFileResource = new FileSystemResource(new File(newFileName));
		
		// adding headers to the api
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		//headers.set("x-key", API_KEY);
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("old-file", oldFileResource);
		body.add("new-file", newFileResource);
		body.add("output-file-name", outputFileName);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity= new HttpEntity<>(body, headers);
		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> brandEtlResponseEntity = new RestTemplate().postForEntity(brandEtlUri, requestEntity, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(brandEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");
	
		/*
		 * Calling the Growth Platform dimension's ETL processing /api/v1/eds/growth-platform/etl api 
		 */
		rootFileName = "growthPlatform";
		// Build URI for the Brand ETL Service
		String growthPlatformEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds-ut-jrs-tax/growth-platforms/etl")
                .toUriString();
		
		// Set tye URI Parameter values
		oldFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv";
		newFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv";
		outputFileName = rootFileName+"Dim-etl.csv";
		
		// getting the file from disk
		oldFileResource=null;
		if (FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv")) 
			oldFileResource = new FileSystemResource(new File(oldFileName));
		newFileResource = new FileSystemResource(new File(newFileName));
		
		// adding headers to the api
		headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		//headers.set("x-key", API_KEY);
		
		//MultiValueMap<String, Object> 
		body = new LinkedMultiValueMap<>();
		body.add("old-file", oldFileResource);
		body.add("new-file", newFileResource);
		body.add("output-file-name", outputFileName);
		
		//HttpEntity<MultiValueMap<String, Object>> 
		requestEntity= new HttpEntity<>(body, headers);		

		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> growthPlatformEtlResponseEntity = new RestTemplate().postForEntity(growthPlatformEtlUri, requestEntity, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(growthPlatformEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");
		
		/*
		 * Calling the Service Line dimension's ETL processing /api/v1/eds/service-line/etl api 
		 */
		rootFileName = "serviceLine";
		// Build URI for the Brand ETL Service
		String serviceLineEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds-ut-jrs-tax/service-lines/etl")
                .toUriString();

		// Set tye URI Parameter values
		oldFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv";
		newFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv";
		outputFileName = rootFileName+"Dim-etl.csv";
		
		// getting the file from disk
		oldFileResource=null;
		if (FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv")) 
			oldFileResource = new FileSystemResource(new File(oldFileName));
		newFileResource = new FileSystemResource(new File(newFileName));
		
		// adding headers to the api
		headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		//headers.set("x-key", API_KEY);
		
		//MultiValueMap<String, Object> 
		body = new LinkedMultiValueMap<>();
		body.add("old-file", oldFileResource);
		body.add("new-file", newFileResource);
		body.add("output-file-name", outputFileName);
		
		//HttpEntity<MultiValueMap<String, Object>> 
		requestEntity= new HttpEntity<>(body, headers);			
		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> serviceLineEtlResponseEntity = new RestTemplate().postForEntity(serviceLineEtlUri, requestEntity, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(serviceLineEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");		
		
		/*
		 * Calling the Practice dimension's ETL processing /api/v1/eds/practice/etl api 
		
		rootFileName = "practice";
		// Build URI for the Brand ETL Service
		String practiceEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds-ut-jrs-tax/practices/etl")
                .toUriString();
		
		// Set tye URI Parameter values
		oldFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv";
		newFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv";
		outputFileName = rootFileName+"Dim-etl.csv";
		
		// getting the file from disk
		oldFileResource=null;
		if (FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv")) 
			oldFileResource = new FileSystemResource(new File(oldFileName));
		newFileResource = new FileSystemResource(new File(newFileName));
		
		// adding headers to the api
		headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		//headers.set("x-key", API_KEY);
		
		//MultiValueMap<String, Object> 
		body = new LinkedMultiValueMap<>();
		body.add("old-file", oldFileResource);
		body.add("new-file", newFileResource);
		body.add("output-file-name", outputFileName);
		
		//HttpEntity<MultiValueMap<String, Object>> 
		requestEntity= new HttpEntity<>(body, headers);	

		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> practiceEtlResponseEntity = new RestTemplate().postForEntity(practiceEtlUri, requestEntity, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(practiceEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");		
		 */
		
		/*
		 * Calling the Service Line dimension's ETL processing /api/v1/eds/offering-portfolio/etl api 
		 */
		rootFileName = "offeringPortfolio";

		// Set tye URI Parameter values
		oldFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv";
		newFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv";
		outputFileName = rootFileName+"Dim-etl.csv";
		
		// getting the file from disk
		oldFileResource=null;
		String type = "idl";
		if (FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv")) {
			oldFileResource = new FileSystemResource(new File(oldFileName));
			type = "etl";
		}
		newFileResource = new FileSystemResource(new File(newFileName));
		
		// Build URI for the Offering Portfolio ETL Service
		String offeringPortfolioEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds-ut-jrs-tax/offering-portfolios/"+type)
                .toUriString();
		
		// adding headers to the api
		headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		//headers.set("x-key", API_KEY);
		
		//MultiValueMap<String, Object> 
		body = new LinkedMultiValueMap<>();
		if (oldFileResource!=null) body.add("old-file", oldFileResource);
		body.add("new-file", newFileResource);
		body.add("output-file-name", outputFileName);
		
		//HttpEntity<MultiValueMap<String, Object>> 
		requestEntity= new HttpEntity<>(body, headers);			
		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> offeringPortfolioEtlResponseEntity = new RestTemplate().postForEntity(offeringPortfolioEtlUri, requestEntity, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(offeringPortfolioEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");	
		
		/*
		 * Calling the Service Line dimension's ETL processing /api/v1/eds/offering/etl api 
		 */
		rootFileName = "offering";
		// Set tye URI Parameter values
		oldFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv";
		newFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv";
		outputFileName = rootFileName+"Dim-etl.csv";
		
		// getting the file from disk
		oldFileResource=null;
		type = "idl";
		if (FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv")) {
			oldFileResource = new FileSystemResource(new File(oldFileName));
			type = "etl";
		}
		newFileResource = new FileSystemResource(new File(newFileName));
		
		// Build URI for the Offering Portfolio ETL Service
		String offeringEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds-ut-jrs-tax/offerings/"+type)
                .toUriString();
		
		// adding headers to the api
		headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		//headers.set("x-key", API_KEY);
		
		//MultiValueMap<String, Object> 
		body = new LinkedMultiValueMap<>();
		if (oldFileResource!=null) body.add("old-file", oldFileResource);
		body.add("new-file", newFileResource);
		body.add("output-file-name", outputFileName);
		
		//HttpEntity<MultiValueMap<String, Object>> 
		requestEntity= new HttpEntity<>(body, headers);			
		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> offeringEtlResponseEntity = new RestTemplate().postForEntity(offeringEtlUri, requestEntity, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(offeringEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");			
				
		/*
		 * Calling the Service Line dimension's ETL processing /api/v1/eds/offering-component/etl api 
		 */
		rootFileName = "offeringComponent";
		// Set tye URI Parameter values
		oldFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv";
		newFileName = fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv";
		outputFileName = rootFileName+"Dim-etl.csv";
		
		// getting the file from disk
		oldFileResource=null;
		type = "idl";
		if (FileHelpers.existsBoolean(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv")) {
			oldFileResource = new FileSystemResource(new File(oldFileName));
			type = "etl";
		}
		newFileResource = new FileSystemResource(new File(newFileName));
		
		// Build URI for the Offering Portfolio ETL Service
		String offeringComponentEtlUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/")
                .path("eds-ut-jrs-tax/offering-components/"+type)
                .toUriString();
		
		// adding headers to the api
		headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		//headers.set("x-key", API_KEY);
		
		//MultiValueMap<String, Object> 
		body = new LinkedMultiValueMap<>();
		if (oldFileResource!=null) body.add("old-file", oldFileResource);
		body.add("new-file", newFileResource);
		body.add("output-file-name", outputFileName);
		
		//HttpEntity<MultiValueMap<String, Object>> 
		requestEntity= new HttpEntity<>(body, headers);			
		
		// Call the fine grained service using RestTemplate
		ResponseEntity<EtlResponse> offeringComponentEtlResponseEntity = new RestTemplate().postForEntity(offeringComponentEtlUri, requestEntity, EtlResponse.class);
		
		// Add the returned EtlResponse object to the list of responses
		etlResponses.add(offeringEtlResponseEntity.getBody());
		
		// copy the current extract file (*-new.csv) to the prior file (*-old.csv)
		FileHelpers.copy(fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-new.csv", fileStorageProperties.getUploadDir()+"/"+rootFileName+"Dim-old.csv");				
		
		
		return etlResponses;
	}

}