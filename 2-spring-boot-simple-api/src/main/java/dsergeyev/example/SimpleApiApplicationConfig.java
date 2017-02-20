package dsergeyev.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dsergeyev.example.resources.serializing.localdate.LocalDateSerializer;
import dsergeyev.example.resources.serializing.localdate.LocalDateDeserializer;
import dsergeyev.example.resources.serializing.localdatetime.LocalDateTimeSerializer;
import dsergeyev.example.resources.serializing.localdatetime.LocalDateTimeDeserializer;

@Configuration
public class SimpleApiApplicationConfig {
	
	public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
	
	@Bean
	@Primary
	public ObjectMapper serializingObjectMapper() {
	    ObjectMapper objectMapper = new ObjectMapper();
	    JavaTimeModule javaTimeModule = new JavaTimeModule();
	    
	    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
	    javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
	    
	    javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
	    javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
	    		
	    objectMapper.registerModule(javaTimeModule);
	    return objectMapper;
	}
}
