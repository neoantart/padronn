package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.web.validator.checks.AgeCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.EmptyOrLengthCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Clase AgeValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 03/07/13 04:42 PM
 */
@Component
public class AgeValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<AgeCheck, String> {

	private int minAge;
	private int maxAge;

	@Override
	public void initialize(AgeCheck constraintAnnotation) {
		this.minAge = constraintAnnotation.min();
		this.maxAge = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || value.isEmpty())
			return true;

		try {
			Date bornDate = validatorUtils.simpleDateFormat.parse(value);
			Calendar minAgeCal = Calendar.getInstance();
			minAgeCal.set(Calendar.HOUR_OF_DAY, 0);
			minAgeCal.set(Calendar.MINUTE, 0);
			minAgeCal.set(Calendar.SECOND, 0);
			minAgeCal.set(Calendar.MILLISECOND, 0);
			minAgeCal.add(Calendar.YEAR, -minAge);
			// todo considerar: 5 años, 11 meses y 29 días
			Calendar maxAgeCal = Calendar.getInstance();
			maxAgeCal.set(Calendar.HOUR_OF_DAY, 0);
			maxAgeCal.set(Calendar.MINUTE, 0);
			maxAgeCal.set(Calendar.SECOND, 0);
			maxAgeCal.set(Calendar.MILLISECOND, 0);
			maxAgeCal.add(Calendar.YEAR, -maxAge);
			if( bornDate.getTime() <= minAgeCal.getTimeInMillis()
				 &&	bornDate.getTime() >= maxAgeCal.getTimeInMillis()) {
				return true;
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return false;
	}


}
