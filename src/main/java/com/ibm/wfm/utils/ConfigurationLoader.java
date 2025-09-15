package com.ibm.wfm.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ConfigurationLoader {
    public static void main(String[] args) {
        String jsonConfig = "[\n" +
                "  {\n" +
                "    \"type\": \"header\",\n" +
                "    \"start_row\": 8,\n" +
                "    \"end_row\": 10,\n" +
                "    \"columns\": {\n" +
                "      \"Type\": \"type\",\n" +
                "      \"Location Number\": \"location_number\",\n" +
                "      \"Landed\": {\n" +
                "        \"type\": \"header\",\n" +
                "        \"start_row_offset\": 1,\n" +
                "        \"end_row_offset\": 0,\n" +
                "        \"subheaders\": {\n" +
                "          \"Scope\": \"scope\",\n" +
                "          \"Utilization\": \"utilization\",\n" +
                "          \"Default Zone\": \"default_zone\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"table\",\n" +
                "    \"start_row_offset\": 2,\n" +
                "    \"columns\": {\n" +
                "      \"Type\": \"type\",\n" +
                "      \"Location Number\": \"location_number\",\n" +
                "      \"Scope\": \"scope\",\n" +
                "      \"Utilization\": \"utilization\",\n" +
                "      \"Default Zone\": \"default_zone\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"header\",\n" +
                "    \"start_row_offset\": 3,\n" +
                "    \"end_row_offset\": 1,\n" +
                "    \"subheaders\": {\n" +
                "      \"Start Date\": \"start_date\",\n" +
                "      \"Duration\": \"duration\",\n" +
                "      \"Start Month #\": \"start_month\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"table\",\n" +
                "    \"start_row_offset\": 4,\n" +
                "    \"columns\": {\n" +
                "      \"Start Date\": \"start_date\",\n" +
                "      \"Duration\": \"duration\",\n" +
                "      \"Start Month #\": \"start_month\"\n" +
                "    }\n" +
                "  }\n" +
                "]";

        // Load JSON configuration into List<Map<String, Object>>
        List<Map<String, Object>> configList = getConfigurationFromJsonString(jsonConfig);

        // Print the loaded configuration for verification
        for (Map<String, Object> config : configList) {
            System.out.println(config);
        }
    }
    
    public static List<Map<String, Object>> getConfigurationFromJsonString(String jsonConfig) {
    	// Load JSON configuration into List<Map<String, Object>>
        Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
    	List<Map<String, Object>> configList = new Gson().fromJson(jsonConfig, listType);
    	return configList;
    	
    }
}
