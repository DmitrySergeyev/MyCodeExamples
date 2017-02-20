package dsergeyev.example.resources.validation.date;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Target({FIELD, METHOD, ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = MyPastDateValidator.class)
public @interface PastDateOrNull {   
    String message() default "{javax.validation.constraints.Past.message}";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
