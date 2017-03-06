package dsergeyev.example.resources.httpresponse.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({"timeStamp", "path", "message", "exception"})
public class StandartErrorHttpResponse extends StandardHttpResponse {

	private String exception;

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public StandartErrorHttpResponse(String message, String path, String exception) {
		super(message, path);
		this.exception = exception;
	}
}
