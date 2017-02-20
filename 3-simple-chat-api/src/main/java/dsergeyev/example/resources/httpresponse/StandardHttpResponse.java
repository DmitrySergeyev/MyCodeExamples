package dsergeyev.example.resources.httpresponse;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public abstract class StandardHttpResponse {

	private ZonedDateTime timeStamp;
	private int status;

	private String message;
	private String path;

	public ZonedDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(ZonedDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	
	public StandardHttpResponse(int status, String path) {
		super();
		this.timeStamp = ZonedDateTime.now();
		this.status = status;
		this.path = path;
	}

	public StandardHttpResponse(int status, String message, String path) {
		super();
		this.timeStamp = ZonedDateTime.now(ZoneId.of("GMT"));
		this.status = status;
		this.message = message;
		this.path = path;
	}
}
