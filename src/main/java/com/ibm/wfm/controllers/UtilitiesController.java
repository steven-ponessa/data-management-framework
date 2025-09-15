package com.ibm.wfm.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.wfm.beans.LadderComparatorResponse;
import com.ibm.wfm.configurations.BridgeDatasourceProperties;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.DbmsConnectionServices;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.services.LadderComparatorService;
import com.ibm.wfm.utils.DataManagerType4;

@RestController
@RequestMapping("/api/v1")
public class UtilitiesController {
	@Autowired
	private FileStorageService fss;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private LadderComparatorService lcr;
	
	@Autowired
	private BridgeDatasourceProperties bridgeProp;
	
	@Autowired
	private DbmsConnectionServices dbmsConnectionServices;
	
	@PostMapping(value="/utils/ladder-comparator", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public LadderComparatorResponse LadderComparator(@RequestParam("oldFileNm") MultipartFile oldFile
			, @RequestParam("newFileNm") MultipartFile newFile
			, @RequestParam(defaultValue = ",") String delimiter
			, @RequestParam("keyStr") String keyStr) {
		LadderComparatorResponse lcr = null;
		return lcr;
	}
	
	@PostMapping("/utils/exec-sql-batch")
	public String executeSqlBatch(@RequestParam(value="pattern", defaultValue = "*.ddl,*.sql") String pattern
			, @RequestParam(value="targetDbms", defaultValue = "WFM-EDS") String targetDbms) throws SQLException, IOException {
		
		Connection conn = dbmsConnectionServices.getConnectionForDbms(targetDbms);
		DataManagerType4.executeSqlBatch(conn, fileStorageProperties.getUploadDir()+"/ddl", pattern);
		return "Worked .. maybe";
	}

}
