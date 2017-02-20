package dsergeyev.example.resources.validation.date;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyPastDateValidator implements ConstraintValidator<PastDateOrNull, LocalDate> {

	@Override
	public void initialize(PastDateOrNull constraintAnnotation) {
	}

	@Override
	public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
		if (date == null) {
			return true;
		} else {
			return date.isBefore(LocalDate.now());
		}
	}
}
