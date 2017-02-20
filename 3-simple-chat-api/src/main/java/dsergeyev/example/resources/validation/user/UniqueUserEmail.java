package dsergeyev.example.resources.validation.user;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({TYPE, METHOD, ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueUserValidator.class)
public @interface UniqueUserEmail { 
    String message() default "User with ";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}