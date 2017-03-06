package dsergeyev.example.resources.validation.password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dsergeyev.example.models.user.User;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordsMatch, Object> {

	@Override
	public void initialize(PasswordsMatch constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		User user = (User) obj;
		
		if(user.getPassword() == null || user.getConfirmationPassword() == null) {
	        context.disableDefaultConstraintViolation();
	        context
	            .buildConstraintViolationWithTemplate("User registration request should contain valid 'password' and 'confirmationPassword' fields")
	            .addConstraintViolation();
	        return false;
		}
		
		return user.getPassword().equals(user.getConfirmationPassword());
	}
}
