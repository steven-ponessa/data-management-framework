package com.ibm.wfm.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.BmsCountryDim;
import com.ibm.wfm.beans.SourcingStrategyValidationBean;
import com.ibm.wfm.utils.ExpressionEvaluator;
import com.ibm.wfm.utils.Helpers;

/*
 * This class will utilize the **Singleton** creation pattern.
 * The singleton pattern insures a class can only be instantiated once, and is a global point of access to that instance is provided.
 * 
 * We do this since the evaluation requires us to retrieve the Geography Taxonomy and Lists of CIC Domestic and Global Delivery Centers. 
 * This lists are gathered using APIs, however we don't want to re-run these APIs for each validation. These lists will be cached and
 * refreshed ...
 * 
 * In the Java programming language, there are a few standard implementations of the singleton pattern. We'll implement the pattern by 
 * having the constructor of the SingletonClass class made private, so that there is no other way to instantiate the class. We'll also 
 * have a method that creates a new instance of the class if one does not exist or is older than .... If an instance has already been created
 * and was created within the specified time perion, the constructor simply returns a reference to that object. 
 * 
 * This Singleton class will implemeent a "lazy initialization" – which means that it does not create the instance until it is requested for the first time.
 * 
 * To insure that we'll only have a single instance in a multi-threaded environment (i.e.,  if two threads call the getInstance() method, it is possible that 
 * two individual objects of the same class get created, due to different times of accessing the (instance==null) check. To make it thread-safe we are 
 * specifyint that the getInstance() method be synchronized.  This will make it impossible for one thread to access the method if another thread hasn’t 
 * finished with its execution.
 * 
 */

public class SourcingStrategyValidationService {
	
	private volatile static SourcingStrategyValidationService instance = null;
	private static LocalDateTime cacheUpdateTime = null;
	private List<BmsCountryDim> countries;
	private List<BmsCountryDim> cicDomCntrs;
	private List<BmsCountryDim> cicGblCntrs;
	
	private static final String SS01_AFFILIATE = "SS01";
	private static final String SS02_CIC_DOMESTIC_CENTER = "SS02";
	private static final String SS03_CIC_DOMESTIC_CENTER_CONTRACTOR = "SS03";
	private static final String SS04_CIC_DOMESTIC_CENTER_EPH = "SS04";
	private static final String SS05_CIC_GLOBAL = "SS05";
	private static final String SS06_CIC_GLOBAL_AFFILIATE_ON_CLIENT_SITE = "SS06";
	private static final String SS07_CIC_GLOBAL_OR_AFFILIATE = "SS07";
	private static final String SS08_CIC_GLOBAL_OR_CIC_DOMESTIC_CENTER = "SS08";
	private static final String SS09_CIC_GLOBAL_OR_CONTRACTOR_DOMESTIC = "SS09";
	private static final String SS10_CIC_GLOBAL_OR_GEO_REGULARS_DOMESTIC = "SS10";
	private static final String SS11_CIC_GLOBAL_OR_GEO_REGULARS_DOMESTIC_B8P_AFFILIATE_LE_B7 = "SS11";
	private static final String SS12_CIC_GLOBAL_OR_GEO_REGULARS_DOMESTIC_B9P_AFFILIATE_LE_B8 = "SS12";
	private static final String SS13_CONTRACTOR__DOMESTIC = "SS13";
	private static final String SS14_GEO_REGULARS__DOMESTIC = "SS14";
	private static final String SS15_GEO_REGULARS_DOMESTIC_EPH = "SS15";
	private static final String SS16_GEO_REGULARS_DOMESTIC_B8_CIC_DOMESTIC_CENTER_LE_B7 = "SS16";
	private static final String SS17_GEO_REGULARS_DOMESTI_B8P_CIC_GLOBAL_LE_B7 = "SS17";
	private static final String SS18_INDIRECT = "SS18";
	private static final String SS19_OTHER = "SS19";
	private static final String SS20_CIC_GLOBAL_DOMESTIC_AFFILIATE = "SS20";
	//CIC Global OR Geo Regulars (Domestic) B8+ / CIC Domestic Center <= B7
	private static final String SS21_CIC_GLOBAL_DOMESTIC_B8P_DOM_CENTER = "SS21";
	private static final String SS22_CIC_GLOBAL_DOMESTIC_B8P_DOM_CENTER = "SS22";
	/* SS90 - Other – CIC Domestic Center */
	private static final String SS90_OTHER_CIC_DOMESTIC_CENTER = "SS90";
	/* SS91 - Other – Geo Regulars (Domestic) */
	private static final String SS91_OTHER_GEO_REGULARS_DOMESTIC = "SS91";
	
	//@Autowired
	//private static Configuration configuration;
	
	private SourcingStrategyValidationService() {
		
		String countriesUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		        .path("/api/v1")
		        .path("/eds-country-tax/countries")
		        //.queryParam("includeParentage", true)
		        .toUriString();
		ResponseEntity<BmsCountryDim[]> countriesResponseEntity = new RestTemplate().getForEntity(countriesUri, BmsCountryDim[].class);
		this.countries = Arrays.asList(countriesResponseEntity.getBody());
		
		String cicDomUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		        .path("/api/v1")
		        .path("/eds-country-tax/countries")
		        .queryParam("filters", "CIC_DOM_DLVRY_CNTR_IND='Y'")
		        .toUriString();
		ResponseEntity<BmsCountryDim[]> cicDomResponseEntity = new RestTemplate().getForEntity(cicDomUri, BmsCountryDim[].class);
		this.cicDomCntrs = Arrays.asList(cicDomResponseEntity.getBody());
		
		String cicGblUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		        .path("/api/v1")
		        .path("/eds-country-tax/countries")
		        .queryParam("filters", "CIC_GBL_DLVRY_CNTR_IND='Y'")
		        .toUriString();
		ResponseEntity<BmsCountryDim[]> cicGblResponseEntity = new RestTemplate().getForEntity(cicGblUri, BmsCountryDim[].class);
		this.cicGblCntrs = Arrays.asList(cicGblResponseEntity.getBody());
		
		cacheUpdateTime = LocalDateTime.now();
	}
	
	public static SourcingStrategyValidationService getInstance() {
		//if (instance==null || cacheUpdateTime==null || cacheUpdateTime.plusSeconds(configuration.getCacheExpireSecs()).isAfter(LocalDateTime.now())) instance = new SourcingStrategyValidationService();
		if (instance==null || cacheUpdateTime==null || cacheUpdateTime.plusSeconds(86400).isBefore(LocalDateTime.now())) {
			synchronized (SourcingStrategyValidationService.class ) {
				if (instance==null || cacheUpdateTime==null || cacheUpdateTime.plusSeconds(86400).isBefore(LocalDateTime.now())) {
					instance = new SourcingStrategyValidationService();
				}
			}
		}
		return instance;
	}
	
	public SourcingStrategyValidationBean validateTest(String sourcingStrategyCd, Map<String, Object> parameterMap, Map<String, String> expressionMap) {
		SourcingStrategyValidationBean validation = new SourcingStrategyValidationBean(sourcingStrategyCd);
		
		ExpressionEvaluator parser = new ExpressionEvaluator();
		
		/*
		 * 1st validation: expression is equality check (trim() eq 'Geo')
		 */
		String expression = expressionMap.get("role-seat-type-cd");
		Object object = parameterMap.get("role-seat-type-cd");
		if (object==null) {
			validation.addError("role-seat-type-cd is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(object+" "+expression+" true");
			else validation.addError(object+" "+expression+" false");
		}
		
		/*
		 * 2nd validation: expression is equality check (trim() eq 'Affiliate')
		 */
		expression = expressionMap.get("pref-resource-channel");
		object = parameterMap.get("pref-resource-channel");
		if (object==null) {
			validation.addError("pref-resource-channel is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(object+" "+expression+" true");
			else validation.addError(object+" "+expression+" false");
		}
		
		/*
		 * 3rd validation: Is not an expression. Checks if "gic-provider-country" is in cicDomCntrs.
		 * Uses Helpers.findByProperty() which returns an object within a list when the passed in property matches a property from the list.
		 * 
		 * Notes:
		 * expression = "teamCd.trim() == 'FLA'";
		 * if (parser.evaluateContext(secTeams.get(0),expression)) 
		 * 
		 * expression = "contains('FLA')";
		 * if (parser.evaluateContext(secTeamCds,expression))
		 */
		object = parameterMap.get("gic-provider-country");
		if (object==null) {
			validation.addError("pref-resource-channel is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			/*
			//CountryDim cicDomProvider = Helpers.findByProperty(cicDomCntrs, cicDomCntr -> parameterMap.get("gic-provider-country").equals(cicDomCntr.getCtryCd().trim()));
			CountryDim cicDomProvider = cicDomCntrs
					.stream()
					//.filter(cicDomCntr -> object.equals(cicDomCntr.getCtryCd().trim()))
					.filter(cicDomCntr -> parameterMap.get("gic-provider-country").equals(cicDomCntr.getCtryCd().trim()))
					.findFirst()
					.orElse(null);
			*/
			BmsCountryDim cicDomProvider = (BmsCountryDim)parser.findInListByAttribute(cicDomCntrs, ((Object)parameterMap.get("gic-provider-country")), "ctryCd");
			if (cicDomProvider==null) validation.addError(parameterMap.get("gic-provider-country") + " not found in cicDomCntrs");
			else validation.addValidExpression(parameterMap.get("gic-provider-country") + " found in cicDomCntrs");
		}
		
		/*
		 * 4th validatation
		 */
		BmsCountryDim cicDomProvider = (BmsCountryDim) parser.findInListByAttribute(cicDomCntrs, (Object)parameterMap.get("gic-provider-country"), "ctryCd");
				//Helpers.findByProperty(cicDomCntrs, cicDomCntr -> parameterMap.get("gic-provider-country").equals(cicDomCntr.getCtryCd().trim()));
		if (cicDomProvider==null) {
			validation.addError("gic-provider-country provided ("+parameterMap.get("gic-provider-country")+") is not a valid domestic provider.");
		}
		else {
			String cicDomProviderCtryCd = (String) parser.getAttribute(cicDomProvider, "ctryCd");
			object = parameterMap.get("contract-owning-country");
			expression = "trim() eq '"+cicDomProviderCtryCd+"'";
			System.out.println("expression: "+expression);
			if (object==null) {
				validation.addError("contract-owning-country is required but not provided");
				validation.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation.addValidExpression(object+" "+expression+" true");
				}
				else {
					BmsCountryDim contractOwningCountry = Helpers.findByProperty(countries, country -> parameterMap.get("contract-owning-country").equals(country.getCtryCd().trim()));
					if (cicDomProvider.getMrktCd().equals(contractOwningCountry.getMrktCd())) {
						validation.addValidExpression("cicDomProvider country ("+cicDomProvider.getCtryCd()+") and contractOwningCountry ("+contractOwningCountry.getCtryCd()+") are in the same market ("+contractOwningCountry.getMrktCd()+")");
					}
					else
						validation.addError("cicDomProvider country ("+cicDomProvider.getCtryCd()+") and contractOwningCountry ("+contractOwningCountry.getCtryCd()+") are not in the same market ("+cicDomProvider.getMrktCd()+" / "+contractOwningCountry.getMrktCd()+")");
				}
			}
		}
		
		
		/*
		 * 5th validation (nust a test): Is not an expression. Checks if "gic-provider-country" is in cicGblCntrs.
		 */
		BmsCountryDim cicGblProvider = Helpers.findByProperty(cicGblCntrs, cicGblCntr -> parameterMap.get("gic-provider-country").equals(cicGblCntr.getCtryCd().trim()));
		if (cicGblProvider==null) {
			 System.out.println(parameterMap.get("gic-provider-country") + " not found in cicGblCntrs");
		}
		else System.out.println(parameterMap.get("gic-provider-country") + " found in cicGblCntrs");
		
		/*
		 * Wrap up. If no errors, validate as true, otherwise false
		 */
		if (validation.getErrors()==null) {
			validation.setValidated(true);
			if (validation.getMsg()==null) validation.setMsg("Sourcing Strategy: "+sourcingStrategyCd+ " is valid.");
		}
		else {
			validation.setValidated(false);
			if (validation.getMsg()==null) validation.setMsg("Sourcing Strategy: "+sourcingStrategyCd+ " is not valid.");
		}
		
		return validation;
	}
	
	public SourcingStrategyValidationBean validate(String sourcingStrategyCd, Map<String, Object> parameterMap, Map<String, String> expressionMap) {
		
		SourcingStrategyValidationBean validation = null;
		if (sourcingStrategyCd==null || sourcingStrategyCd.trim().length()==0) {
			validation = new SourcingStrategyValidationBean();
			validation.addError("Sourcing Strategy Code required but not specified.");
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS01_AFFILIATE)) {
			validation = affiliateRule(sourcingStrategyCd, parameterMap);
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS02_CIC_DOMESTIC_CENTER)) {
			validation = cicDomesticCenterRule(sourcingStrategyCd, parameterMap);
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS03_CIC_DOMESTIC_CENTER_CONTRACTOR)) { 
			validation = cicDomesticCenterRuleContractor(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS04_CIC_DOMESTIC_CENTER_EPH)) { 
			validation =  cicDomesticCenterRuleEph(sourcingStrategyCd, parameterMap);
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS05_CIC_GLOBAL)) { 
			validation = cicGlobalRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS06_CIC_GLOBAL_AFFILIATE_ON_CLIENT_SITE)) { 
			//validation = cicGlobalAffiliateOnClientSiteRule(sourcingStrategyCd, parameterMap); 
			validation = cicDomesticAffiliateOnClientSiteRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS07_CIC_GLOBAL_OR_AFFILIATE)) { 
			validation = cicGlobalAffiliateRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS08_CIC_GLOBAL_OR_CIC_DOMESTIC_CENTER)) { 
			validation = cicGlobalOrCiCDomesticRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS09_CIC_GLOBAL_OR_CONTRACTOR_DOMESTIC)) { 
			validation = cicGlobalOrContractorDomesticRule(sourcingStrategyCd, parameterMap);
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS10_CIC_GLOBAL_OR_GEO_REGULARS_DOMESTIC)) { 
			validation = cicGlobalOrGeoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS11_CIC_GLOBAL_OR_GEO_REGULARS_DOMESTIC_B8P_AFFILIATE_LE_B7)) { 
			validation = cicGlobalOrGeoRegularDomesticB8PlusOrAffiliateLtB7Rule(sourcingStrategyCd, parameterMap);
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS12_CIC_GLOBAL_OR_GEO_REGULARS_DOMESTIC_B9P_AFFILIATE_LE_B8)) { 
			validation = cicGlobalOrGeoRegularDomesticB9PlusOrAffiliateLtB8Rule(sourcingStrategyCd, parameterMap);
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS13_CONTRACTOR__DOMESTIC)) { 
			validation = contractorDomesticRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS14_GEO_REGULARS__DOMESTIC)) { 
			validation = geoRegularDomesticRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS15_GEO_REGULARS_DOMESTIC_EPH)) { 
			validation = geoRegularDomesticEphRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS16_GEO_REGULARS_DOMESTIC_B8_CIC_DOMESTIC_CENTER_LE_B7)) { 
			validation = geoRegularDomesticB8PlusOrCicDomesticLtB7Rule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS17_GEO_REGULARS_DOMESTI_B8P_CIC_GLOBAL_LE_B7)) { 
			validation = geoRegularDomesticB8PlusOrCicGlobalLtB7Rule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS20_CIC_GLOBAL_DOMESTIC_AFFILIATE)) { 
			validation = cicGlobalOrDomesticAffiliateOnClientSiteRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS21_CIC_GLOBAL_DOMESTIC_B8P_DOM_CENTER)) { 
			validation = cicGlobalOrDomesticB8pDomCenterRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS22_CIC_GLOBAL_DOMESTIC_B8P_DOM_CENTER)) { 
			validation = cicGlobalOrDomesticB9pDomCenterRule(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS90_OTHER_CIC_DOMESTIC_CENTER)) { 
			/* validation 2 - CIC Global Center */
			SourcingStrategyValidationBean validation2 = cicGlobalRule(sourcingStrategyCd, parameterMap);
			validation = otherCicGlobalOrCicDomesticCenter(sourcingStrategyCd, parameterMap); 
		}
		//
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS91_OTHER_GEO_REGULARS_DOMESTIC)) { 
			validation = otherCicGlobalOrGeoRegularsDomestic(sourcingStrategyCd, parameterMap); 
		}
		else if (sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS18_INDIRECT)
			 ||  sourcingStrategyCd.equalsIgnoreCase(SourcingStrategyValidationService.SS19_OTHER)) { 
			validation = new SourcingStrategyValidationBean();
			validation.setSourcingStrategyCd(sourcingStrategyCd);
			validation.setMsg("Unable to determine.");
			validation.addError("Unable to determine.");
			validation.setValid(false);
			validation.setMsg("Sourcing Strategy: "+sourcingStrategyCd+ " is not valid.");
		}
		else {
			validation = new SourcingStrategyValidationBean();
			validation.setSourcingStrategyCd(sourcingStrategyCd);
			validation.addError("Sourcing Strategy Code not recognized.");
			validation.setValid(false);
			validation.setMsg("Sourcing Strategy: "+sourcingStrategyCd+ " is not valid.");
		}
		
		return validation;
	}
	
	public SourcingStrategyValidationBean setValidity(String sourcingStrategyCd,SourcingStrategyValidationBean validation) {
		/*
		 * Wrap up. If no errors, validate as true, otherwise false
		 */
		if (validation.getErrors()==null) {
			validation.setValid(true);
			if (validation.getMsg()==null) validation.setMsg("Sourcing Strategy: "+sourcingStrategyCd+ " is valid.");
		}
		else {
			validation.setValid(false);
			if (validation.getMsg()==null) validation.setMsg("Sourcing Strategy: "+sourcingStrategyCd+ " is not valid.");
		}
		return validation;
	}
	
	/* SS01 - Affiliate */
	public SourcingStrategyValidationBean affiliateRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		SourcingStrategyValidationBean validation = new SourcingStrategyValidationBean(sourcingStrategyCd);
		
		String parameter = "role-seat-type-cd";
		Object object = parameterMap.get("role-seat-type-cd");
		String expression = "toLowerCase().trim() eq 'geo'";
		ExpressionEvaluator parser = new ExpressionEvaluator();
		
		/*
		 * 1st validation: expression is equality check (trim() eq 'Geo')
		 */
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		/*
		 * 2nd validation: expression is equality check (trim() eq 'Affiliate')
		 */
		parameter = "pref-resource-channel";
		expression = "toLowerCase().trim() eq 'affiliate'";
		object = parameterMap.get(parameter);
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		return setValidity(sourcingStrategyCd, validation);
	}
	
	/* SS02 - CIC Domestic Center */
	public SourcingStrategyValidationBean cicDomesticCenterRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		SourcingStrategyValidationBean validation = new SourcingStrategyValidationBean(sourcingStrategyCd);
		ExpressionEvaluator parser = new ExpressionEvaluator();
		
		/* validation 1 - Role Type = GIC */
		String parameter = "role-seat-type-cd";
		Object object = parameterMap.get(parameter);
		String expression = "toLowerCase().trim() eq 'gic'";
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		
		/* validation 2 - GIC Provider Country IN CIC Domestic Delivery Center */
		parameter = "gic-provider-country";
		String attribute = "ctryCd";
		object = parameterMap.get(parameter);
		BmsCountryDim cicDomProviderCtry = null;
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			cicDomProviderCtry = (BmsCountryDim)parser.findInListByAttribute(cicDomCntrs, ((Object)parameterMap.get(parameter)), attribute);
			if (cicDomProviderCtry==null) validation.addError(parameter+" "+parameterMap.get(parameter) + " not found in cicDomCntrs");
			else validation.addValidExpression(parameter+" "+parameterMap.get(parameter) + " found in cicDomCntrs");
		}
		
		/* validation 3 - GIC Provider Country & Contractt Owning Country in same market */
		parameter = "contract-owning-country";
		object = parameterMap.get(parameter);
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			BmsCountryDim contractOwningCtry = (BmsCountryDim)parser.findInListByAttribute(countries, ((Object)parameterMap.get(parameter)), attribute);
			if (contractOwningCtry==null) validation.addError(parameter+" "+parameterMap.get(parameter) + " not found in countries");
			else {
				if (cicDomProviderCtry!=null && cicDomProviderCtry.getMrktCd().trim().equals(contractOwningCtry.getMrktCd().trim())) {
					validation.addValidExpression("gic-provider-country ("+cicDomProviderCtry.getCtryCd()
					+") in same market ("+cicDomProviderCtry.getMrktCd().trim()+") as contractOwningCtry ("+contractOwningCtry.getCtryCd()+")");
					
				}
				else {
					String temp="";
					temp += "gic-provider-country (";
					temp += (cicDomProviderCtry==null?"null":cicDomProviderCtry.getCtryCd());
					temp +=") market (";
					temp += (cicDomProviderCtry==null?"null":cicDomProviderCtry.getMrktCd().trim());
					temp +=") not same market (";
					temp += contractOwningCtry.getMrktCd().trim()+") as contractOwningCtry ("+contractOwningCtry.getCtryCd()+")";
					validation.addError(temp);
				}
			}
		}
		return setValidity(sourcingStrategyCd, validation);
	}
	
	/* SS03 - CIC Domestic Center - Contractor */
	public SourcingStrategyValidationBean cicDomesticCenterRuleContractor(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		
		/* validation 1 - CIC Domestic Center Rule */
		SourcingStrategyValidationBean validation = cicDomesticCenterRule(sourcingStrategyCd, parameterMap);
		
		/* validation 2 - Request contractor / Accept Global Contractor (GIC) = Yes */
		String parameter = "req-cntrct-accept-gbl-delivery";
		Object object = parameterMap.get(parameter);
		String expression = "toLowerCase().trim() eq 'yes'";
		ExpressionEvaluator parser = new ExpressionEvaluator();
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		return setValidity(sourcingStrategyCd, validation);
	}
	
	/* SS04 - CIC Domestic Center - EPH */
	public SourcingStrategyValidationBean cicDomesticCenterRuleEph(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		
		/* validation 1 - CIC Domestic Center Rule */
		SourcingStrategyValidationBean validation = cicDomesticCenterRule(sourcingStrategyCd, parameterMap);
		
		/* validation 2 - **Requested Band High: <= 6 And ** Requested Band Low: => 6G */
		ExpressionEvaluator parser = new ExpressionEvaluator();
		
		String parameter = "band-low";
	    Object object = parameterMap.get(parameter);
	    String expression = "toLowerCase().trim() ge '6'";

		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		
		parameter = "band-high";
		object = parameterMap.get("band-high");
		expression = "toLowerCase().trim() lt '7'";

		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		return setValidity(sourcingStrategyCd, validation);
	}
	
	/* SS05 - CIC Global  */
	public SourcingStrategyValidationBean cicGlobalRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		SourcingStrategyValidationBean validation = new SourcingStrategyValidationBean(sourcingStrategyCd);
		ExpressionEvaluator parser = new ExpressionEvaluator();
		
		/* validation 1 - Role Type = GIC */
		String parameter = "role-seat-type-cd";
		Object object = parameterMap.get(parameter);
		String expression = "toLowerCase().trim() eq 'gic'";
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		
		/* validation 2 - GIC Provider Country IN CIC Global Delivery Center */
		parameter = "gic-provider-country";
		String attribute = "ctryCd";
		object = parameterMap.get(parameter);
		BmsCountryDim cicDomProviderCtry = null;
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			cicDomProviderCtry = (BmsCountryDim)parser.findInListByAttribute(cicGblCntrs, ((Object)parameterMap.get(parameter)), attribute);
			if (cicDomProviderCtry==null) validation.addError(parameter+" "+parameterMap.get(parameter) + " not found in cicGblCntrs");
			else validation.addValidExpression(parameter+" "+parameterMap.get(parameter) + " found in cicGblCntrs");
		}
		return setValidity(sourcingStrategyCd, validation);
	}
	
	/* SS06 - CIC Global / Affiliate-on-client-site  */
	//public SourcingStrategyValidationBean cicGlobalAffiliateOnClientSiteRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
	//	/* validation 1 - CIC Global */
	//	SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
//
//		if (validation1.isValid()) {
//			validation1.setSourcingStrategyCd(sourcingStrategyCd);
//			return setValidity(sourcingStrategyCd, validation1);
//		}
//		/* validation 2 - CIC Global */
//		SourcingStrategyValidationBean validation2 = affiliateRule(sourcingStrategyCd, parameterMap);
//		if (validation2.isValid()) {
//			validation2.setSourcingStrategyCd(sourcingStrategyCd);
//			return setValidity(sourcingStrategyCd, validation2);
//		}
//		/*
//		 * Both validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
//		 */
//		validation1.setSourcingStrategyCd(sourcingStrategyCd);
//
//		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
//			validation1.addValidExpression(validExpression);
//		}
//
//		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
//			validation1.addError(error);
//		}
//
//		return setValidity(sourcingStrategyCd, validation1);
//	}
	
	/* SS06 - CIC Domestic / Affiliate-on-client-site  */
	public SourcingStrategyValidationBean cicDomesticAffiliateOnClientSiteRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = this.cicDomesticCenterRule(sourcingStrategyCd, parameterMap);

		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - CIC Global */
		SourcingStrategyValidationBean validation2 = affiliateRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			validation2.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation2);
		}
		/*
		 * Both validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);

		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}

		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}

		return setValidity(sourcingStrategyCd, validation1);
	}
	
	/* SS07 - CIC Global or Affiliate  */
	public SourcingStrategyValidationBean cicGlobalAffiliateRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - Affiliate */
		SourcingStrategyValidationBean validation2 = affiliateRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			validation2.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation2);
		}
		/*
		 * Both validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}	
	
	/* SS08 - CIC Global OR CIC Domestic Center  */
	public SourcingStrategyValidationBean cicGlobalOrCiCDomesticRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - CIC Global */
		SourcingStrategyValidationBean validation2 = cicDomesticCenterRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			validation2.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation2);
		}
		/*
		 * Both validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}	
	
	/* SS09 - CIC Global OR Contractor (Domestic)  */
	public SourcingStrategyValidationBean cicGlobalOrContractorDomesticRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - Contractor Domestic */
		SourcingStrategyValidationBean validation2 = contractorDomesticRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			validation2.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation2);
		}
		/*
		 * Both validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}		

	/* SS10 - CIC Global OR Geo Regulars (Domestic)  */
	public SourcingStrategyValidationBean cicGlobalOrGeoRegularDomesticRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - Geo Regular Domestic */
		SourcingStrategyValidationBean validation2 = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			validation2.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation2);
		}
		/*
		 * Both validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}	
	
	/* SS11 - CIC Global OR Geo Regulars (Domestic) B8+ / Affiliate <= B7)  */
	public SourcingStrategyValidationBean cicGlobalOrGeoRegularDomesticB8PlusOrAffiliateLtB7Rule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - Geo Regular Domestic */
		SourcingStrategyValidationBean validation2 = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			/* validation 2a - Requested Band High: >=8 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() ge '8'";

			if (object==null) {
				validation2.addError(parameter+" is required but not provided");
				validation2.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation2.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation2.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation2);
				}
				else validation2.addError(parameter+" "+object+" "+expression+" false");
			}
		}
		/* validation 3 - Affiliatee */
		SourcingStrategyValidationBean validation3 = affiliateRule(sourcingStrategyCd, parameterMap);
		if (validation3.isValid()) {
			/* validation 3a - Requested Low High: <=7 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-low";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() le '7'";

			if (object==null) {
				validation3.addError(parameter+" is required but not provided");
				validation3.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation3.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation3.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation3);
				}
				else validation3.addError(parameter+" "+object+" "+expression+" false");
			}
		}		
		/*
		 * Validation 1, 2, and 3 have failed. Add validation 2 & 3's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		for (String validExpression: ListUtils.emptyIfNull(validation3.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation3.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}
	
	/* SS12 - CIC Global OR Geo Regulars (Domestic) B9+ / Affiliate <= B8)  */
	public SourcingStrategyValidationBean cicGlobalOrGeoRegularDomesticB9PlusOrAffiliateLtB8Rule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - Geo Regular Domestic */
		SourcingStrategyValidationBean validation2 = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			/* validation 2a - Requested Band High: >=8 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() ge '9'";

			if (object==null) {
				validation2.addError(parameter+" is required but not provided");
				validation2.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation2.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation2.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation2);
				}
				else validation2.addError(parameter+" "+object+" "+expression+" false");
			}
		}
		/* validation 3 - Affiliatee */
		SourcingStrategyValidationBean validation3 = affiliateRule(sourcingStrategyCd, parameterMap);
		if (validation3.isValid()) {
			/* validation 3a - Requested Low High: <=7 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() le '8'";

			if (object==null) {
				validation3.addError(parameter+" is required but not provided");
				validation3.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation3.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation3.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation3);
				}
				else validation3.addError(parameter+" "+object+" "+expression+" false");
			}
		}		
		/*
		 * Validation 1, 2, and 3 have failed. Add validation 2 & 3's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		for (String validExpression: ListUtils.emptyIfNull(validation3.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation3.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}	
	
	/* SS13 - Contractor (Domestic) */
	public SourcingStrategyValidationBean contractorDomesticRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		SourcingStrategyValidationBean validation = new SourcingStrategyValidationBean(sourcingStrategyCd);
		
		/* validation 1 - Role Type = Geo */
		String parameter = "role-seat-type-cd";
		Object object = parameterMap.get("role-seat-type-cd");
		String expression = "toLowerCase().trim() eq 'geo'";
		ExpressionEvaluator parser = new ExpressionEvaluator();
		
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		/* validation 2 - *Preferred Resource Channel: = Contractor */
		parameter = "pref-resource-channel";
		expression = "toLowerCase().trim() eq 'contractor'";
		object = parameterMap.get(parameter);
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		
		/* validation 3 - Request contractor / Accept Global Contractor (GIC) = Yes */
		parameter = "req-cntrct-accept-gbl-delivery";
		object = parameterMap.get(parameter);
		//expression = "toLowerCase().trim() eq 'yes'";
		expression = "toLowerCase().trim() ne 'no'";
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		
		return setValidity(sourcingStrategyCd, validation);
	}	
	
	/* SS14 - Geo Regulars (Domestic) */
	public SourcingStrategyValidationBean geoRegularDomesticRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		SourcingStrategyValidationBean validation = new SourcingStrategyValidationBean(sourcingStrategyCd);
		
		/* validation 1 - Role Type = Geo */
		String parameter = "role-seat-type-cd";
		Object object = parameterMap.get("role-seat-type-cd");
		String expression = "toLowerCase().trim() eq 'geo'";
		ExpressionEvaluator parser = new ExpressionEvaluator();
		
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		/* validation 2 - *Preferred Resource Channel: = Contractor */
		parameter = "pref-resource-channel";
		expression = "toLowerCase().trim() eq 'regular'";
		object = parameterMap.get(parameter);
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		
		/* validation 3 - Request contractor / Accept Global Contractor (GIC) = no */
		parameter = "req-cntrct-accept-gbl-delivery";
		object = parameterMap.get(parameter);
		expression = "toLowerCase().trim() eq 'no'";
		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		
		return setValidity(sourcingStrategyCd, validation);
	}
	
	/* SS15 - Geo Regulars (Domestic) - EPH */
	public SourcingStrategyValidationBean geoRegularDomesticEphRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		
		/* validation 1 - Geo Reegular (Domestic) Rule */
		SourcingStrategyValidationBean validation = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		
		/* validation 2 - **Requested Band High: <= 6 And ** Requested Band Low: => 6G */
		ExpressionEvaluator parser = new ExpressionEvaluator();
		
		String parameter = "band-low";
	    Object object = parameterMap.get(parameter);
	    String expression = "toLowerCase().trim() ge '6'";

		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		
		parameter = "band-high";
		object = parameterMap.get("band-high");
		expression = "toLowerCase().trim() lt '7'";

		if (object==null) {
			validation.addError(parameter+" is required but not provided");
			validation.setMsg("Unable to determine.");
		}
		else {
			if (parser.evaluateContext(object,expression)) validation.addValidExpression(parameter+" "+object+" "+expression+" true");
			else validation.addError(parameter+" "+object+" "+expression+" false");
		}
		return setValidity(sourcingStrategyCd, validation);
	}
	
	/* SS16 - Geo Regulars (Domestic) B8+ / CIC Domestic Center <= B7  */
	public SourcingStrategyValidationBean geoRegularDomesticB8PlusOrCicDomesticLtB7Rule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - Geo Regular Domestic */
		SourcingStrategyValidationBean validation1 = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			/* validation 1a - Requested Band High: >=8 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() ge '8'";

			if (object==null) {
				validation1.addError(parameter+" is required but not provided");
				validation1.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation1.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation1.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation1);
				}
				else validation1.addError(parameter+" "+object+" "+expression+" false");
			}
		}
		/* validation 2 - CIC Domestic Center */
		SourcingStrategyValidationBean validation2 = cicDomesticCenterRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			/* validation 2a - Requested Low High: <=7 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() le '7'";

			if (object==null) {
				validation2.addError(parameter+" is required but not provided");
				validation2.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation2.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation2.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation1);
				}
				else validation2.addError(parameter+" "+object+" "+expression+" false");
			}
		}		
		/*
		 * Validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}	
	
	/* SS17 - Geo Regulars (Domestic) B8+ / CIC Global <= B7  */
	public SourcingStrategyValidationBean geoRegularDomesticB8PlusOrCicGlobalLtB7Rule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - Geo Regular Domestic */
		SourcingStrategyValidationBean validation1 = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			/* validation 1a - Requested Band High: >=8 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() ge '8'";

			if (object==null) {
				validation1.addError(parameter+" is required but not provided");
				validation1.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation1.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation1.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation1);
				}
				else validation1.addError(parameter+" "+object+" "+expression+" false");
			}
		}
		/* validation 2 - CIC Global Center */
		SourcingStrategyValidationBean validation2 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			/* validation 2a - Requested Low High: <=7 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() le '7'";

			if (object==null) {
				validation2.addError(parameter+" is required but not provided");
				validation2.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation2.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation2.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation2);
				}
				else validation2.addError(parameter+" "+object+" "+expression+" false");
			}
		}		
		/*
		 * Validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}
	
	/* SS20 - CIC Global OR CIC Domestic Center / Affiliate-on-client-site  */
	public SourcingStrategyValidationBean cicGlobalOrDomesticAffiliateOnClientSiteRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 0 - CIC Global */
		//
		SourcingStrategyValidationBean validation0 = this.cicGlobalRule(sourcingStrategyCd, parameterMap);

		if (validation0.isValid()) {
			validation0.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation0);
		}
		
		/* validation 1 - CIC Domestic Center */
		SourcingStrategyValidationBean validation1 = this.cicDomesticCenterRule(sourcingStrategyCd, parameterMap);

		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		
		/* validation 2 - CIC Global */
		SourcingStrategyValidationBean validation2 = affiliateRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			validation2.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation2);
		}
		/*
		 * Both validation 1 and 2 have failed. Add validation 2's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);

		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}

		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}

		return setValidity(sourcingStrategyCd, validation1);
	}	
	/* SS21 - CIC Global OR Geo Regulars (Domestic) B8+ / CIC Domestic Center <= B7 */
	public SourcingStrategyValidationBean cicGlobalOrDomesticB8pDomCenterRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - Geo Regular Domestic */
		SourcingStrategyValidationBean validation2 = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			/* validation 2a - Requested Band High: >=8 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() ge '8'";

			if (object==null) {
				validation2.addError(parameter+" is required but not provided");
				validation2.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation2.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation2.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation2);
				}
				else validation2.addError(parameter+" "+object+" "+expression+" false");
			}
		}
		/* validation 3 - CIC Domestic Center */
		SourcingStrategyValidationBean validation3 = this.cicDomesticCenterRule(sourcingStrategyCd, parameterMap);
		if (validation3.isValid()) {
			/* validation 3a - Requested Low High: <=7 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() le '7'";

			if (object==null) {
				validation3.addError(parameter+" is required but not provided");
				validation3.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation3.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation3.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation3);
				}
				else validation3.addError(parameter+" "+object+" "+expression+" false");
			}
		}		
		/*
		 * Validation 1, 2, and 3 have failed. Add validation 2 & 3's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		for (String validExpression: ListUtils.emptyIfNull(validation3.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation3.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}	
	
	/* SS90 - Other – CIC Domestic Center */
	public SourcingStrategyValidationBean otherCicGlobalOrCicDomesticCenter(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		
		SourcingStrategyValidationBean validation = cicDomesticCenterRule(sourcingStrategyCd, parameterMap);
		return setValidity(sourcingStrategyCd, validation);
	}
	/* SS91 - Other – Geo Regulars (Domestic) */
	public SourcingStrategyValidationBean otherCicGlobalOrGeoRegularsDomestic(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		SourcingStrategyValidationBean validation = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		return setValidity(sourcingStrategyCd, validation);
	}
	
	/* SS21 - CIC Global OR Geo Regulars (Domestic) B8+ / CIC Domestic Center <= B7 
	 * SS22 - CIC Global OR Geo Regulars (Domestic) B9+ / CIC Domestic Center <= B8 */
	public SourcingStrategyValidationBean cicGlobalOrDomesticB9pDomCenterRule(String sourcingStrategyCd, Map<String, Object> parameterMap) {
		/* validation 1 - CIC Global */
		SourcingStrategyValidationBean validation1 = cicGlobalRule(sourcingStrategyCd, parameterMap);
		if (validation1.isValid()) {
			validation1.setSourcingStrategyCd(sourcingStrategyCd);
			return setValidity(sourcingStrategyCd, validation1);
		}
		/* validation 2 - Geo Regular Domestic */
		SourcingStrategyValidationBean validation2 = geoRegularDomesticRule(sourcingStrategyCd, parameterMap);
		if (validation2.isValid()) {
			/* validation 2a - Requested Band High: >=9 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() ge '9'";

			if (object==null) {
				validation2.addError(parameter+" is required but not provided");
				validation2.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation2.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation2.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation2);
				}
				else validation2.addError(parameter+" "+object+" "+expression+" false");
			}
		}
		/* validation 3 - CIC Domestic Center */
		SourcingStrategyValidationBean validation3 = this.cicDomesticCenterRule(sourcingStrategyCd, parameterMap);
		if (validation3.isValid()) {
			/* validation 3a - Requested Low High: <=8 */
			ExpressionEvaluator parser = new ExpressionEvaluator();
			
			String parameter = "band-high";
		    Object object = parameterMap.get(parameter);
		    String expression = "toLowerCase().trim() le '8'";

			if (object==null) {
				validation3.addError(parameter+" is required but not provided");
				validation3.setMsg("Unable to determine.");
			}
			else {
				if (parser.evaluateContext(object,expression)) {
					validation3.addValidExpression(parameter+" "+object+" "+expression+" true");
					validation3.setSourcingStrategyCd(sourcingStrategyCd);
					return setValidity(sourcingStrategyCd, validation3);
				}
				else validation3.addError(parameter+" "+object+" "+expression+" false");
			}
		}		
		/*
		 * Validation 1, 2, and 3 have failed. Add validation 2 & 3's valid expressions and erros to validation 1 and return
		 */
		validation1.setSourcingStrategyCd(sourcingStrategyCd);
		for (String validExpression: ListUtils.emptyIfNull(validation2.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation2.getErrors())) {
			validation1.addError(error);
		}
		for (String validExpression: ListUtils.emptyIfNull(validation3.getValidExpressions())) {
			validation1.addValidExpression(validExpression);
		}
		
		for (String error: ListUtils.emptyIfNull(validation3.getErrors())) {
			validation1.addError(error);
		}
		return setValidity(sourcingStrategyCd, validation1);
	}	

}
