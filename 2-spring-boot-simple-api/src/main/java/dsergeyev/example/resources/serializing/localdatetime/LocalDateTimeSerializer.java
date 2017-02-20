package dsergeyev.example.resources.serializing.localdatetime;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import dsergeyev.example.SimpleApiApplicationConfig;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {    
	
	@Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.format(SimpleApiApplicationConfig.LOCAL_DATE_TIME_FORMATTER));
    }
}
