package dsergeyev.example.resources.errorhanding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAddressInUseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EmailAddressInUseException() {
	}

	public EmailAddressInUseException(String message) {
		super(message);
	}

	public EmailAddressInUseException(String message, Throwable cause) {
		super(message, cause);
	}
}
