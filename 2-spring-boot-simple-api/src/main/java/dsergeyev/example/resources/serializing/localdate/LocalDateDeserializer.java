package dsergeyev.example.resources.serializing.localdate;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import dsergeyev.example.SimpleApiApplicationConfig;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    
	@Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return LocalDate.parse(p.getValueAsString(), SimpleApiApplicationConfig.LOCAL_DATE_FORMATTER);
    }
}