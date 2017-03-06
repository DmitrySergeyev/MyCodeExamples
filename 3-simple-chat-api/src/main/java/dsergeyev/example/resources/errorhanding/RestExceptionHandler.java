package dsergeyev.example.resources.errorhanding;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dsergeyev.example.resources.httpresponse.error.ArgumentNotValidErrorHttpResponse;
import dsergeyev.example.resources.httpresponse.error.StandartErrorHttpResponse;
import dsergeyev.example.resources.httpresponse.error.ValidationError;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationError(ValidationException ex, HttpServletRequest request) {

		StandartErrorHttpResponse errorDetail = new StandartErrorHttpResponse(ex.getClass().getName(), ex.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleArgumentValidationError(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		ArgumentNotValidErrorHttpResponse errorDetail = new ArgumentNotValidErrorHttpResponse(ex.getClass().getName(),
				"Validation of some fields failed. For more information see 'entityErrors' and 'fieldErrors' sections",
				request.getRequestURI());

		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

		for (FieldError fe : fieldErrors) {
			List<ValidationError> validationErrorList = errorDetail.getFieldErrors().get(fe.getField());

			if (validationErrorList == null) {
				validationErrorList = new ArrayList<ValidationError>();
				errorDetail.getFieldErrors().put(fe.getField(), validationErrorList);
			}

			ValidationError validationError = new ValidationError();
			validationError.setCode(fe.getCode());
			validationError.setMessage(fe.getDefaultMessage());
			validationErrorList.add(validationError);
		}

		List<ObjectError> globalError = ex.getBindingResult().getGlobalErrors();

		for (ObjectError oe : globalError) {
			errorDetail.getEntityErrors().add(oe.getDefaultMessage());
		}

		return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	}
}
