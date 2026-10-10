package com.ibm.wfm.utils;

import java.io.IOException;

import org.springframework.stereotype.Component;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdScalarDeserializer;
import tools.jackson.databind.module.SimpleModule;

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
            public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws JacksonException {
                return jsonParser.getValueAsString().trim();
            }
        });
    }
}