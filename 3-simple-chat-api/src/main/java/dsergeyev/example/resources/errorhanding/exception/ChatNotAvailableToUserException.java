package dsergeyev.example.resources.errorhanding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ChatNotAvailableToUserException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ChatNotAvailableToUserException() {
	}

	public ChatNotAvailableToUserException(String message) {
		super(message);
	}

	public ChatNotAvailableToUserException(String message, Throwable cause) {
		super(message, cause);
	}
}

