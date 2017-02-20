package dsergeyev.example.resources.responses.errors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentNotValidErrorDetail extends StandartErrorDetail {
	
	private Map<String, List<ValidationError>> errors = 
			new HashMap<String, List<ValidationError>>();

	public Map<String, List<ValidationError>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, List<ValidationError>> errors) {
		this.errors = errors;
	}
}
