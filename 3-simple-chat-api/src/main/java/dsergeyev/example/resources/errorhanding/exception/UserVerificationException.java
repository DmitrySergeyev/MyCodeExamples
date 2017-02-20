package dsergeyev.example.resources.errorhanding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserVerificationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UserVerificationException() {
	}

	public UserVerificationException(String message) {
		super(message);
	}

	public UserVerificationException(String message, Throwable cause) {
		super(message, cause);
	}
}
