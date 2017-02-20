package dsergeyev.example.resources.errorhanding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArgumentNotValidException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ArgumentNotValidException() {
	}

	public ArgumentNotValidException(String message) {
		super(message);
	}

	public ArgumentNotValidException(String message, Throwable cause) {
		super(message, cause);
	}
}
