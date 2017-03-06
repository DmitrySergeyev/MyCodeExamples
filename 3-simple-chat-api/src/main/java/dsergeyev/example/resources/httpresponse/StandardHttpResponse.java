package dsergeyev.example.resources.httpresponse;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timeStamp", "path", "message" })
public class StandardHttpResponse {

	private ZonedDateTime timeStamp;
	private String message;
	private String path;

	public ZonedDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(ZonedDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public StandardHttpResponse(String path) {
		super();
		this.timeStamp = ZonedDateTime.now(ZoneId.of("GMT"));
		this.path = path;
	}

	public StandardHttpResponse(String message, String path) {
		super();
		this.timeStamp = ZonedDateTime.now(ZoneId.of("GMT"));
		this.message = message;
		this.path = path;
	}
}
