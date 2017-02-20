package dsergeyev.example.resources.httpresponse.info;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({ "timeStamp", "path", "status", "info", "message" })
public class StandartInfoHttpResponse extends StandardHttpResponse {

	private HttpStatus info;

	public HttpStatus getInfo() {
		return info;
	}

	public void setInfo(HttpStatus info) {
		this.info = info;
	}	
	
	public StandartInfoHttpResponse(HttpStatus info, String path) {
		super(info.value(), path);
		this.info = info;
	}
	
	public StandartInfoHttpResponse(HttpStatus info, String message, String path) {
		super(info.value(), message, path);
		this.info = info;
	}
}
