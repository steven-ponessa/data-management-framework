package com.ibm.wfm.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "DMF Stub", description = "Hierarchical JSON Stub API")
public class DmfStubController {
    @Value("${file.json.path}")
    private String jsonFilesPath;
    
    private final ObjectMapper objectMapper;

    public DmfStubController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @GetMapping(path={"/api-stub"}, produces = {"application/json", "application/xml"})
    public ResponseEntity<Object> getStub(@RequestParam(name="json-input", required=false) @Parameter(description = "Specify a JSON file name or a valid JSON string.") String jsonInput) {
        try {
            // Check if input is a valid JSON structure
            JsonNode jsonNode;
            if (isValidJson(jsonInput)) {
                jsonNode = objectMapper.readTree(jsonInput);
            } else {
                // Treat input as a filename and attempt to read the JSON file
                File jsonFile = new File(jsonFilesPath, jsonInput);
                if (!jsonFile.exists()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("File not found: " + jsonInput);
                }
                String fileContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
                jsonNode = objectMapper.readTree(fileContent);
            }

            return ResponseEntity.ok(jsonNode);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }
    
    @PostMapping("/echo-stub")
    public ResponseEntity<Object> getEchoStub(@RequestBody @Parameter(description = "Specify a JSON file name or a valid JSON string.") String requestBody) {
        try {
            JsonNode responseJson;

            // Determine if the request body is a JSON structure
            if (isValidJson(requestBody)) {
                responseJson = objectMapper.readTree(requestBody);
            } else {
                // Assume the request body is a filename
                File jsonFile = new File(jsonFilesPath, requestBody.trim());
                if (!jsonFile.exists()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("File not found: " + requestBody);
                }
                String fileContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
                responseJson = objectMapper.readTree(fileContent);
            }

            return ResponseEntity.ok(responseJson);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }
    
    //GET endpoint for hierarchical path navigation
    /*
     * what didn't work with OpenAPI documentation
     */ 
    //@GetMapping("/stub/**/{path:.+}")
    //public ResponseEntity<Object> getStubHierarchicalNavigation(
    //	        @Parameter(description = "Path to JSON file within the stub directory, e.g., `dir1/subdir/myfile`")
    //	        @PathVariable String path) {
    @Operation(summary = "Hierarchical navigation for JSON stubs. Will not work in OpenAPI documenation.")
    @GetMapping("/stub/**")
    public ResponseEntity<Object> getStubHierarchicalNavigation() {
        try {
            // Get the full path after `/stub/`
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request context");
            }
            
            String requestUri = attributes.getRequest().getRequestURI();
            String path = requestUri.substring(requestUri.indexOf("/stub/") + 6); // Length of "/stub/"

            // Split the path by '/' to separate directories from the filename
            String[] pathSegments = path.split("/");

            if (pathSegments.length == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid path: no file specified");
            }

            // Extract the filename (last path segment)
            String fileName = pathSegments[pathSegments.length - 1] + ".json";

            // Construct the directory path
            StringBuilder directoryPath = new StringBuilder(jsonFilesPath);
            for (int i = 0; i < pathSegments.length - 1; i++) {
                directoryPath.append(File.separator).append(pathSegments[i]);
            }

            // Create the full file path
            File jsonFile = new File(directoryPath.toString(), fileName);

            // Check if the file exists
            if (!jsonFile.exists() || !jsonFile.isFile()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found: " + jsonFile.getPath());
            }

            // Read and parse the JSON file
            String fileContent = new String(Files.readAllBytes(Paths.get(jsonFile.getPath())));
            JsonNode jsonNode = objectMapper.readTree(fileContent);

            //return ResponseEntity.ok(jsonNode);
            if (jsonNode.isArray()) {
                return ResponseEntity.ok(objectMapper.convertValue(jsonNode, List.class));
            } else {
                return ResponseEntity.ok(objectMapper.convertValue(jsonNode, Map.class));
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }  
    
    private String extractWildcardPath(HttpServletRequest request, String userId) {
        String requestURI = request.getRequestURI();
        int index = requestURI.indexOf(userId) + userId.length() + 1; // +1 for the slash
        return requestURI.substring(index);
    }    

    // Helper method to check if input is valid JSON
    private boolean isValidJson(String input) {
        try {
            objectMapper.readTree(input);
            return true;
        } catch (IOException e) {
            return false;
        }
    }    

}
