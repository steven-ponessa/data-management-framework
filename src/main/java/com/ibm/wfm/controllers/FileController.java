package com.ibm.wfm.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.UploadFileResponse;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.services.FileSortService;
import com.ibm.wfm.services.FileStorageService;
import com.ibm.wfm.utils.FileHelpers;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private FileStorageProperties fileStorageProperties;
    
    private FileSortService fileSortService;
    
    @GetMapping("/pwd")
    public ResponseEntity<String> printWorkingDirectory() {
		return ResponseEntity.ok().body(fileStorageService.getCurrentDirectory());
    }
    
    @GetMapping("/file-exists")
    public boolean fileExists(@RequestParam String path) {
        File file = new File(path);
        return file.exists();
    }

    @GetMapping("/list-files")
    public String[] listFiles(@RequestParam String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            return directory.list();
        }
        return new String[] {"Not a directory or does not exist"};
    }
    
    @GetMapping("/list-upload-directory")
    public List<String> listUploadDirectory(@RequestParam(value="filePatterns", required=false, defaultValue="") @Parameter(description="File name or pattern. Multiple can be comma separated") String filePattern) {
		String[] filePatterns = filePattern.split(",");
		List<String> allFiles = new ArrayList<>();
		for (String fp: filePatterns) {
			List<String> files = FileHelpers.getFileList(fileStorageProperties.getUploadDir()+"/"+fp);
			allFiles.addAll(files);
		}
		return allFiles;
    }
    
    @DeleteMapping("/delete-from-upload-directory")
    public int deleteUploadDirectory(@RequestParam(value="filePatterns", required=false, defaultValue="") @Parameter(description="File name or pattern. Multiple can be comma separated") String filePattern) {
		String[] filePatterns = filePattern.split(",");
		int fileCnt=0;
		for (String fp: filePatterns) {
			List<String> files = FileHelpers.getFileList(fileStorageProperties.getUploadDir()+"/"+fp);
			fileCnt+=files.size();
			for (String file: files) {
				FileHelpers.delete(file);
			}
		}
		return fileCnt;
    }

    @PostMapping(value="/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    
    @PostMapping(value="/uploadFile/{directory}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse uploadFileToDirectory(@RequestParam("file") MultipartFile file
    										, @RequestParam("directory") String directory) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/downloadFile/"+directory+"/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
    
    @PostMapping("/uploadMultipleFiles/ddl")
    public List<UploadFileResponse> uploadMultipleFilesDDl(@RequestParam("files") MultipartFile[] files) {
		if (FileHelpers.isDirectory(fileStorageProperties.getUploadDir()+"/ddl")) {
			List<String> localFiles = FileHelpers.getFileList(fileStorageProperties.getUploadDir()+"/ddl/*.*");
			for (String localFile: localFiles) {
				FileHelpers.delete(localFile);
			}
		}
		else {
			FileHelpers.makeDirectory(fileStorageProperties.getUploadDir()+"/ddl");
		}
		
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @PostMapping("/sort")
    public UploadFileResponse sort(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/downloadFile/")
                .path(fileName)
                .toUriString();
        String sortedFileName = fileSortService.sort(fileName);
        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

}