package com.ibm.wfm.utils;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * This class shows, how Jackson can be used to trim all the string values in a Java object.
 */
public class TrimStringToNullConfiguration {

    private ObjectMapper objectMapper;

    //public Client trimToNull(Client inputClient) throws JsonProcessingException {
    //    return getObjectMapper().readValue(getObjectMapper().writeValueAsString(inputClient), Client.class);
    //}

    private ObjectMapper getObjectMapper() {
        if (isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(String.class, new TrimStringToNullDeserializer());
            objectMapper.registerModule(module);
        }
        return objectMapper;
    }
}