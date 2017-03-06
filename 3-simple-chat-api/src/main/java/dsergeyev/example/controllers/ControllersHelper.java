package dsergeyev.example.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ControllersHelper {

	public static HttpHeaders getHeadersWithJsonContextType() {
		HttpHeaders headers = new HttpHeaders();		
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
