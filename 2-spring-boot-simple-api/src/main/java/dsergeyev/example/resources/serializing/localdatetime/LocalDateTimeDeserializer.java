package dsergeyev.example.resources.serializing.localdatetime;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import dsergeyev.example.SimpleApiApplicationConfig;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    
	@Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return LocalDateTime.parse(p.getValueAsString(), SimpleApiApplicationConfig.LOCAL_DATE_TIME_FORMATTER);
    }
}
