package dsergeyev.example.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ControllersHelper {

	public static HttpHeaders getHeadersWithJsonContextType() {
		HttpHeaders headers = new HttpHeaders();		
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		URI uri = null;
		try {
			uri = new URI("en-us");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		headers.setLocation(uri);
		return headers;
	}
	
	public static String getAppUrl(String serverName, int serverPort) {
		return "http://" + serverName + ":" + Integer.toString(serverPort);
	}
}
