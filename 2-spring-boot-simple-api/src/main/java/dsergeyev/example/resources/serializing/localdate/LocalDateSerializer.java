package dsergeyev.example.resources.serializing.localdate;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import dsergeyev.example.SimpleApiApplicationConfig;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {    
	
	@Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.format(SimpleApiApplicationConfig.LOCAL_DATE_FORMATTER));
    }
}
