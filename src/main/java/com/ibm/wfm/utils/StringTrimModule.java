package com.ibm.wfm.utils;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class StringTrimModule extends SimpleModule {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StringTrimModule() {
        addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException,
                    JsonProcessingException {
                return jsonParser.getValueAsString().trim();
            }
        });
    }
}