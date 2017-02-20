package dsergeyev.example.resources.errorhanding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserBlockedExeption extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public UserBlockedExeption(String message) {
		super(message);
	}

	public UserBlockedExeption(String message, Throwable cause) {
		super(message, cause);
	}
}
