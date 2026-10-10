package com.ibm.wfm.utils;

import static java.util.Objects.isNull;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * This class shows, how Jackson can be used to trim all the string values in a Java object.
 */
public class TrimStringToNullConfiguration {

    private ObjectMapper objectMapper;

    //public Client trimToNull(Client inputClient) throws JacksonException {
    //    return getObjectMapper().readValue(getObjectMapper().writeValueAsString(inputClient), Client.class);
    //}

    private ObjectMapper getObjectMapper() {
        if (isNull(objectMapper)) {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(String.class, new TrimStringToNullDeserializer());
            objectMapper = JsonMapper.builder()
                .addModule(module)
                .build();
        }
        return objectMapper;
    }
}