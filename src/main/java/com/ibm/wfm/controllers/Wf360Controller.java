package com.ibm.wfm.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.wfm.beans.Certification;
import com.ibm.wfm.beans.FutureSkill;
import com.ibm.wfm.beans.FutureSkillsDim;
import com.ibm.wfm.services.CertificationDaoService;
import com.ibm.wfm.services.Wf360FutureSkillDaoService;
import com.ibm.wfm.utils.DataMarshaller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class Wf360Controller {
	
	@Autowired
	private Wf360FutureSkillDaoService futureSkillDaoService;
	
	@Autowired
	private CertificationDaoService certificationDaoService; 
	
	@GetMapping("/wf360/future-skills")
	public List<FutureSkillsDim> retrieveAllFutureSkills() {
		int resultSetSize=0; //return all
		//                           findAll(Map<String, Object> pathVarMap, String filters, boolean includePit, String pit, int size, String orderByCols, boolean edsOnly)   
		return futureSkillDaoService.findAll(null                          ,""             ,false              ,null       ,-1, resultSetSize,null           , false);
	}
	
	@Operation(summary = "Get all Future Skills as CSV", responses = {
		    @ApiResponse(content = @Content(mediaType = "text/csv"))
		})
	@GetMapping(value="/wf360/future-skills/csv",produces="text/csv")
	public void toCsvAllFutureSkills(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv; charset=utf-8");
		List<FutureSkill> futureSkills = futureSkillDaoService.findAll();
		DataMarshaller.writeCsv2PrintWriter(FutureSkill.class, response.getWriter(), futureSkills);
		return;
	}
	
	/*
	
	@GetMapping("/wf360/etl/future-skills")
	public int etlFutureSkills() {
		List<FutureSkill> futureSkills = futureSkillDaoService.findAll();
		return futureSkillDaoService.insertAll(futureSkills);
		//return null;
	}
	*/
	
	@GetMapping("/wf360/certifications")
	public List<Certification> retrieveAllCertifications() throws IOException {
		return certificationDaoService.findAll();
	}
	
	/*
	@GetMapping("/wf360/idl/certifications")
	public int idlCertifications() {
		List<Certification> certifications = certificationDaoService.findAll();
		return certificationDaoService.insertAll(certifications);
	}
	*/
	
	@DeleteMapping("/wf360/certifications")
	public int deleteCertifications() {
		return certificationDaoService.deleteAll();
	}

}