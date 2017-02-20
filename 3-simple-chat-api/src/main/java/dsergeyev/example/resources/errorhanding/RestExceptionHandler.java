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

import dsergeyev.example.resources.errorhanding.exception.UserBlockedExeption;
import dsergeyev.example.resources.httpresponse.error.ArgumentNotValidErrorHttpResponse;
import dsergeyev.example.resources.httpresponse.error.StandartErrorHttpResponse;
import dsergeyev.example.resources.httpresponse.error.ValidationError;

@ControllerAdvice
public class RestExceptionHandler {

	// @ExceptionHandler(ResourceNotFoundException.class)
	// public ResponseEntity<?>
	// handleResourceNotFoundException(ResourceNotFoundException ex,
	// HttpServletRequest request) {
	//
	// StandartErrorDetail errorDetail = new StandartErrorDetail();
	//
	// errorDetail.setTimeStamp(System.currentTimeMillis());
	// errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
	// errorDetail.setTitle("Resource Not Found");
	// errorDetail.setDetail(ex.getMessage());
	// errorDetail.setDeveloperMessage(ex.getClass().getName());
	// errorDetail.setPath(request.getRequestURI());
	//
	// return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
	// }

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationError(ValidationException ex, HttpServletRequest request) {

		StandartErrorHttpResponse errorDetail = new StandartErrorHttpResponse(HttpStatus.BAD_REQUEST,
				ex.getClass().getName(), ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleArgumentValidationError(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		ArgumentNotValidErrorHttpResponse errorDetail = new ArgumentNotValidErrorHttpResponse(HttpStatus.BAD_REQUEST,
				ex.getClass().getName(),
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

//	@ExceptionHandler(UserBlockedExeption.class)
//	public ResponseEntity<?> handleUserBlockedError(ValidationException ex, HttpServletRequest request) {
//
//		StandartErrorHttpResponse errorDetail = new StandartErrorHttpResponse(HttpStatus.FORBIDDEN,
//				ex.getClass().getName(), ex.getMessage(), request.getRequestURI());
//		return new ResponseEntity<>(errorDetail, null, HttpStatus.FORBIDDEN);
//	}
	
	// @ExceptionHandler(EmailAddressInUseException.class)
	// public ResponseEntity<?>
	// handleEmailAddressInUse(EmailAddressInUseException eaiue,
	// HttpServletRequest request) {
	//
	// StandartErrorDetail errorDetail = new StandartErrorDetail();
	//
	// errorDetail.setTimeStamp(System.currentTimeMillis());
	// errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
	// errorDetail.setTitle("Create user error");
	// errorDetail.setDetail(eaiue.getMessage());
	// errorDetail.setDeveloperMessage(eaiue.getClass().getName());
	// errorDetail.setPath(request.getRequestURI());
	//
	// return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	// }

	// @ExceptionHandler(InvalidPasswordError.class)
	// public ResponseEntity<?> handleChangePasswor(InvalidPasswordError eaiue,
	// HttpServletRequest request) {
	//
	// StandartErrorDetail errorDetail = new StandartErrorDetail();
	//
	// errorDetail.setTimeStamp(System.currentTimeMillis());
	// errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
	// errorDetail.setTitle("Change user password error");
	// errorDetail.setDetail(eaiue.getMessage());
	// errorDetail.setDeveloperMessage(eaiue.getClass().getName());
	// errorDetail.setPath(request.getRequestURI());
	//
	// return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
	// }
}
