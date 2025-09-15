package com.ibm.wfm.services;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.ibm.wfm.beans.AttributeStatistic;
import com.ibm.wfm.beans.UtNode;
import com.ibm.wfm.beans.UtResponse;
import com.ibm.wfm.utils.Helpers;

@Component
public class UtAccessService {

	@Autowired
	private RestTemplate restTemplate; // = new RestTemplate();
	
	private static String FILTERS = "";
			/*
			"accountassignmentgroup"
			+ ",announcementtype"
			+ ",availabilitystatus"
			+ ",availabilitytype"
			+ ",bhaccountassignmentgroup"
			+ ",commonnameofavail"
			+ ",digitalsales"
			+ ",extprofilekeyindicator"
			+ ",fcreligibleindicator"
			+ ",gbsofferingattribute"
			+ ",gbspracticecode"
			+ ",gbspracticedescription"
			+ ",gbtbrandcode"
			+ ",gbtlevel10"
			+ ",gbtlevel10description"
			+ ",gbtlevel17"
			+ ",gbtlevel20"
			+ ",gbtlevel20description"
			+ ",gbtlevel30"
			+ ",gbtlevel30description"
			+ ",generalareaselection"
			+ ",idtype"
			+ ",idvalue"
			+ ",ierpproducthierarchycode"
			+ ",ierpprofitcentercode"
			+ ",lastupdated"
			+ ",leadpracticeattribute"
			+ ",leads"
			+ ",longname"
			+ ",machinemodel"
			+ ",machinetype"
			+ ",marketingname"
			+ ",marketingreportingcode"
			+ ",modelcategory"
			+ ",modelsubcategory"
			+ ",modelsubgroup"
			+ ",occname"
			+ ",ocstatus"
			+ ",octype"
			+ ",offeringcatcd"
			+ ",offeringcatcddesc"
			+ ",offeringcode"
			+ ",offeringcodedescription"
			+ ",offeringconfigurationtype"
			+ ",offeringindicator"
			+ ",offeringinvestmentstatus"
			+ ",ombrandcode"
			+ ",omproductfamilycode"
			+ ",percentageofcompletionindicator"
			+ ",piddescription"
			+ ",pidname"
			+ ",pocflag"
			+ ",pricefilename"
			+ ",productidentifier"
			+ ",producttype"
			+ ",producttypecode"
			+ ",publicdate"
			+ ",revenuedivision"
			+ ",revenuedivisioncode"
			+ ",shortname"
			+ ",sltlevel10"
			+ ",sltlevel15"
			+ ",sltlevel17"
			+ ",sltlevel20"
			+ ",sltlevel30"
			+ ",soprelevant"
			+ ",soptasktype"
			+ ",svclineareacode"
			+ ",svclineareadescription"
			+ ",taxclassification"
			+ ",taxcode"
			+ ",type"
			+ ",unitofmeasuresi"
			+ ",utcode"
			+ ",utlevel10"
			+ ",utlevel10description"
			+ ",utlevel15"
			+ ",utlevel15description"
			+ ",utlevel17"
			+ ",utlevel17description"
			+ ",utlevel20"
			+ ",utlevel20description"
			+ ",utlevel30"
			+ ",utlevel30description"
			+ ",utlevel35"
			+ ",utlevel35description";
			*/
	
	static {
		Class type = UtNode.class;
		for (Field field : type.getDeclaredFields()) {
			FILTERS+=(FILTERS.length()>0?",":"")+(field.getName());
		}
	}

	public UtAccessService() {
	}

	public static void main(String[] args) {
		boolean verbose = false;
		boolean validParams = true;
		String utlevel10 = "10J00";
		String ocstatus = "O";
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-utl10") || args[optind].equalsIgnoreCase("--utlevel10")) {
					utlevel10 = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-s") || args[optind].equalsIgnoreCase("--ocstatus")) {
					ocstatus = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("-help")) {
					validParams = false;
				} else {
					// logger.info("E0001: Unknown parameter specified: " + args[optind]);
					System.out.println("E0001: Unknown parameter specified: " + args[optind]);
					validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)
		} // end try
		catch (Exception e) {
			// logger.error(msgs.getText("error.e0001", e.getMessage()));
			e.printStackTrace();
			validParams = false;
		}

		UtAccessService utAccessService = new UtAccessService();

		List<UtNode> utNodes = utAccessService.findAllEdsUt(utlevel10, ocstatus, "All");

		for (UtNode utNode : utNodes) {
			System.out.println(utNode.toCsv());
		}

	}

	public List<UtNode> findAllEdsUt(String utlevel10, String ocstatus, String size) {
		
		//UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromPath("https://fedcat-api.us1a.cirrus.ibm.com")
		UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromPath("https://fedcat-api.fedcat-p360.wdc.app.cirrus.ibm.com")
	            .path("/api/v0.1/")
	            .path("offerings/");
		
		if(!(size==null||size.trim().length()==0||size.trim().equalsIgnoreCase("all"))) size="ALL";
		utUriBuilder.queryParam("size",size);
		
		String queryString="";
		if (!(utlevel10==null||utlevel10.trim().length()==0||utlevel10.trim().equalsIgnoreCase("all"))) queryString+=(queryString.length()>0?";":"")+("utlevel10:"+utlevel10.toUpperCase());
		if (!(ocstatus==null ||ocstatus.trim().length()==0 ||ocstatus.trim().equalsIgnoreCase("all")))  queryString+=(queryString.length()>0?";":"")+("ocstatus:"+ocstatus.toUpperCase());
		if (queryString.length()>0) utUriBuilder.queryParam("q",queryString);
		
		utUriBuilder.queryParam("filters",UtAccessService.FILTERS);
		
		
		String url = utUriBuilder.toUriString();
		url=url.replace("https:/", "https://").replace("size=All","size=ALL");
		
		//String url = "https://prodfedcat001.w3-969.ibm.com:3000/api/v0.1/offerings/?size=ALL&q=utlevel10:10J00;ocstatus:O&filters="+UtAccessService.FILTERS;
				//"utlevel10,utlevel10description,utlevel15,utlevel15description,utlevel17,utlevel17description,gbspracticecode,gbspracticedescription,utlevel30,utlevel30description";

		System.out.println(url);
		ResponseEntity<UtResponse> response = restTemplate.getForEntity(url, UtResponse.class);
		UtResponse resp = response.getBody();
		
		/*
		List<UtNode> utNodeList = resp.getSource();
		
		
		List<AttributeStatistic> attributeStatistics;
		try {
			attributeStatistics = Helpers.getStatsForList(utNodeList, UtNode.class);
			for (AttributeStatistic as: attributeStatistics) {
				System.out.println(as.toCsv());
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		return resp.getSource();
		//return utNodeList;
	}

	public List<UtNode> findEdsUt(String size
								, String utlevel10, String ocstatus
			                    , String utlevel15, String utlevel17, String gbspracticecode
			                    , String utlevel20, String utlevel30, String addtlQueryString) {

		//String url = "https://prodfedcat001.w3-969.ibm.com:3000/api/v0.1/offerings/?size=ALL&q=utlevel10:10J00;ocstatus:O&filters=utlevel10,utlevel10description,utlevel15,utlevel15description,utlevel17,utlevel17description,gbspracticecode,gbspracticedescription,utlevel30,utlevel30description";

		//UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromPath("https://fedcat-api.us1a.cirrus.ibm.com")
		UriComponentsBuilder utUriBuilder = ServletUriComponentsBuilder.fromPath("https://fedcat-api.fedcat-p360.wdc.app.cirrus.ibm.com")
	            .path("/api/v0.1/")
	            .path("offerings/");
		
		String queryString="";
		
		if(!(size==null||size.trim().length()==0||size.trim().equalsIgnoreCase("all"))) size="ALL";
		utUriBuilder.queryParam("size",size);
		
		if (!(utlevel10==null||utlevel10.trim().length()==0||utlevel10.trim().equalsIgnoreCase("all"))) queryString+=(queryString.length()>0?";":"")+("utlevel10:"+utlevel10.toUpperCase());
		if (!(ocstatus==null ||ocstatus.trim().length()==0 ||ocstatus.trim().equalsIgnoreCase("all")))  queryString+=(queryString.length()>0?";":"")+("ocstatus:"+ocstatus.toUpperCase());
		if (!(utlevel15==null||utlevel15.trim().length()==0||utlevel15.trim().equalsIgnoreCase("all"))) queryString+=(queryString.length()>0?";":"")+("utlevel15:"+utlevel15.toUpperCase());
		if (!(utlevel17==null||utlevel17.trim().length()==0||utlevel17.trim().equalsIgnoreCase("all"))) queryString+=(queryString.length()>0?";":"")+("utlevel17:"+utlevel17.toUpperCase());
		if (!(utlevel20==null||utlevel20.trim().length()==0||utlevel20.trim().equalsIgnoreCase("all"))) queryString+=(queryString.length()>0?";":"")+("utlevel20:"+utlevel20.toUpperCase());
		if (!(utlevel30==null||utlevel30.trim().length()==0||utlevel30.trim().equalsIgnoreCase("all"))) queryString+=(queryString.length()>0?";":"")+("utlevel30:"+utlevel30.toUpperCase());
		if (!(gbspracticecode==null||gbspracticecode.trim().length()==0||gbspracticecode.trim().equalsIgnoreCase("all"))) queryString+=(queryString.length()>0?";":"")+("gbspracticecode"+gbspracticecode.toUpperCase());
		if (!(addtlQueryString==null||addtlQueryString.trim().length()==0)) queryString+=(queryString.length()>0?";":"")+addtlQueryString;
		if (queryString.length()>0) utUriBuilder.queryParam("q",queryString);
		
		//String utColumns = "utlevel10,utlevel10description,utlevel15,utlevel15description,utlevel17,utlevel17description"
		//		+",utlevel20,utlevel20description,utlevel30,utlevel30description,gbspracticecode,gbspracticedescription,offeringcode,occname,octype,gbsofferingattribute,leadpracticeattribute,revenuedivisioncode,ierpprofitcentercode,publicdate,announcementdate"
		//		;

		utUriBuilder.queryParam("filters",UtAccessService.FILTERS);
		
		
		String utUri = utUriBuilder.toUriString();
		
		utUri=utUri.replace("https:/", "https://").replace("size=All","size=ALL");
		
		System.out.println("UT API: "+utUri);
		
		ResponseEntity<UtResponse> response = restTemplate.getForEntity(utUri, UtResponse.class);
		UtResponse resp = response.getBody();
		return resp.getSource();
	}


}
