package dsergeyev.example.resources.httpresponse.error;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({"timeStamp", "path", "status", "error", "message", "exception"})
public class StandartErrorHttpResponse extends StandardHttpResponse {

	private HttpStatus error;
	private String exception;

	public HttpStatus getError() {
		return error;
	}

	public void setError(HttpStatus error) {
		this.error = error;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public StandartErrorHttpResponse(HttpStatus error, String exception, String message, String path) {
		super(error.value(), message, path);
		this.error = error;
		this.exception = exception;
	}
}
