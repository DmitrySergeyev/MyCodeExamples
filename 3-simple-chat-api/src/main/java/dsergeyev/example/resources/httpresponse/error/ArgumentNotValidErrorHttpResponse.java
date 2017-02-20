package dsergeyev.example.resources.httpresponse.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({ "timeStamp", "path", "status", "error", "message", "entityErrors", "fieldErrors", "exception"})
public class ArgumentNotValidErrorHttpResponse extends StandartErrorHttpResponse {

	private List<String> entityErrors = new ArrayList<>();
	private Map<String, List<ValidationError>> fieldErrors = new HashMap<String, List<ValidationError>>();

	public List<String> getEntityErrors() {
		return entityErrors;
	}

	public void setEntityErrors(List<String> entityErrors) {
		this.entityErrors = entityErrors;
	}

	public Map<String, List<ValidationError>> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, List<ValidationError>> errors) {
		this.fieldErrors = errors;
	}

	public ArgumentNotValidErrorHttpResponse(HttpStatus error, String exception, String message, String path) {
		super(error, exception, message, path);
	}	
}
