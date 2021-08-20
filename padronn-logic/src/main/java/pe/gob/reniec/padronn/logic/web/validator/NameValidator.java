package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.web.validator.checks.DateCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.NameCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;

/**
 * Clase NameValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 05/07/13 03:02 PM
 */
@Component
public class NameValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<NameCheck, String> {

	@Override
	public void initialize(NameCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		if(name==null || name.isEmpty()) {
			return true;
		}

		if(validatorUtils.getInvalidFirstNameCharsPattern().matcher(name.substring(0,1)).matches()) {
			return false;
		}

		if(validatorUtils.getInvalidNameCharsPattern().matcher(name).find()) {
			return false;
		}

		return validatorUtils.getValidNameCharsPattern().matcher(name).matches();
	}


}
