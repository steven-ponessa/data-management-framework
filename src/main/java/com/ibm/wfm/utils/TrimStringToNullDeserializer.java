package com.ibm.wfm.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static java.util.Objects.isNull;

/**
 * Deserializer that will trim all the leading and trailing spaces from a String value.
 * If the final value us empty string ("") it will be returned as null.
 */
public class TrimStringToNullDeserializer extends JsonDeserializer {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String value = jsonParser.getValueAsString();

        if (isNull(value)) {
            return null;
        }

        value = value.trim();
        if (value.length() == 0) {
            value = null;
        }

        return value;
    }
}