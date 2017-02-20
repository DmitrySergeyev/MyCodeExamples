package dsergeyev.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;


@SpringBootApplication
public class SimpleApiApplication {
	
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd::MM::yyyy");
	
	public static void main(String[] args) {
		SpringApplication.run(SimpleApiApplication.class, args);		
	}
	
	
}
