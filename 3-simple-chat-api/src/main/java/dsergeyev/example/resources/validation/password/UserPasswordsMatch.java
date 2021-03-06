package dsergeyev.example.resources.validation.password;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({TYPE, ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface UserPasswordsMatch { 
    String message() default "Password and confirmation of password do not match";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
