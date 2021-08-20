package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.web.validator.checks.EmptyOrLengthCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Clase EmptyOrLengthValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 02/07/13 07:24 PM
 */
@Component
public class EmptyOrLengthValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<EmptyOrLengthCheck, String> {

	private int minLength;
	private int maxLength;

	@Override
	public void initialize(EmptyOrLengthCheck constraintAnnotation) {
		this.minLength = constraintAnnotation.min();
		this.maxLength = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || value.trim().isEmpty())
			return true;
		if(value.length()>=minLength && value.length()<=maxLength)
			return true;
		return false;
	}

}
