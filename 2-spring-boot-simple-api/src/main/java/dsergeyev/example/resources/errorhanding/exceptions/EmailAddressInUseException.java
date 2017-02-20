package dsergeyev.example.resources.errorhanding.exceptions;

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
