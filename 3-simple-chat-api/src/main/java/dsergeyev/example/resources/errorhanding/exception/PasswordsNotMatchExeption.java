package dsergeyev.example.resources.errorhanding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordsNotMatchExeption extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public PasswordsNotMatchExeption() {
	}

	public PasswordsNotMatchExeption(String message) {
		super(message);
	}

	public PasswordsNotMatchExeption(String message, Throwable cause) {
		super(message, cause);
	}
}