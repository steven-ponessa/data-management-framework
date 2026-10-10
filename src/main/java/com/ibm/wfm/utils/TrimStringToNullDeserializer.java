package com.ibm.wfm.utils;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import static java.util.Objects.isNull;

/**
 * Deserializer that will trim all the leading and trailing spaces from a String value.
 * If the final value us empty string ("") it will be returned as null.
 */
public class TrimStringToNullDeserializer extends ValueDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
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