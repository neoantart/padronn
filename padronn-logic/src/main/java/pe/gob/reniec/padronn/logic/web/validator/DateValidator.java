package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.web.validator.checks.DateCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;

/**
 * Clase DateValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 02/07/13 08:28 PM
 */
@Component
public class DateValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<DateCheck, String> {

	@Override
	public void initialize(DateCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || value.isEmpty())
			return true;
		try {
			//if(validatorUtils.simpleDateFormat.parse(value).getTime()< System.currentTimeMillis())
			//	return true;
			validatorUtils.simpleDateFormat.parse(value);
			return true;
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return false;
	}

}
