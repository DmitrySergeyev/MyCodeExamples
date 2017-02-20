package dsergeyev.example.resources.errorhanding;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dsergeyev.example.resources.responses.errors.StandartErrorDetail;
import dsergeyev.example.resources.responses.errors.ArgumentNotValidErrorDetail;
import dsergeyev.example.resources.responses.errors.ValidationError;
import dsergeyev.example.resources.errorhanding.exceptions.ResourceNotFoundException;
import dsergeyev.example.resources.errorhanding.exceptions.EmailAddressInUseException;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {

		StandartErrorDetail errorDetail = new StandartErrorDetail();

		errorDetail.setTimeStamp(System.currentTimeMillis());
		errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
		errorDetail.setTitle("Resource Not Found");
		errorDetail.setDetail(ex.getMessage());
		errorDetail.setDeveloperMessage(ex.getClass().getName());
		errorDetail.setPath(request.getRequestURI());

		return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request) {

		ArgumentNotValidErrorDetail errorDetail = new ArgumentNotValidErrorDetail();

		errorDetail.setTimeStamp(System.currentTimeMillis());
		errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setTitle("Validation Failed");
		errorDetail.setDetail("Input validation failed");
		errorDetail.setDeveloperMessage(manve.getClass().getName());
		errorDetail.setPath(request.getRequestURI());

		List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
		
		for (FieldError fe : fieldErrors) {
			List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
			
			if (validationErrorList == null) {
				validationErrorList = new ArrayList<ValidationError>();
				errorDetail.getErrors().put(fe.getField(), validationErrorList);
			}
			
			ValidationError validationError = new ValidationError();
			validationError.setCode(fe.getCode());
			validationError.setMessage(fe.getDefaultMessage());
			validationErrorList.add(validationError);
		}
		
		return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(EmailAddressInUseException.class)
	public ResponseEntity<?> handleDataIntegrityViolation(EmailAddressInUseException eaiue, HttpServletRequest request) {
		
		StandartErrorDetail errorDetail = new StandartErrorDetail();

		errorDetail.setTimeStamp(System.currentTimeMillis());
		errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setTitle("Email in use");
		errorDetail.setDetail(eaiue.getMessage());
		errorDetail.setDeveloperMessage(eaiue.getClass().getName());
		errorDetail.setPath(request.getRequestURI());
				
		return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	}
}
