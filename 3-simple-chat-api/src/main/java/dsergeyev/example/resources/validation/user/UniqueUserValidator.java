package dsergeyev.example.resources.validation.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dsergeyev.example.ChatApplication;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;

public class UniqueUserValidator implements ConstraintValidator<UniqueUserEmail, String> {
	
	@Autowired
	UserService userService;
	
	@Override
	public void initialize(UniqueUserEmail constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
        
		if(email == null) {
	        context.disableDefaultConstraintViolation();
	        context
	            .buildConstraintViolationWithTemplate("User registration request should contain valid 'email' field")
	            .addConstraintViolation();
	        return false;
		}

		boolean	result = this.userService.checkIsEmailAvalible(email);		
		
		if(!result) {
			context.disableDefaultConstraintViolation();
	        context
	            .buildConstraintViolationWithTemplate("Email address '" + email + "' is already in use")
	            .addConstraintViolation();
		}
			
		return result;
	}
}