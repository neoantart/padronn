package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 03/07/13
 * Time: 10:06 AM
 */
public class LessThanAgeValidator implements ConstraintValidator<LessThanAge, String> {

	LessThanAge lessThanAge;

	@Override
	public void initialize(LessThanAge lessThanAge) {
		this.lessThanAge=lessThanAge;
	}

	@Override
	public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
		SimpleDateFormat dateOfBirth=new SimpleDateFormat(lessThanAge.dateFormat());
		dateOfBirth.setLenient(false);
		Calendar dob = Calendar.getInstance();
		try {
			dob.setTime(dateOfBirth.parse(date));
		} catch (ParseException e) {
			return true;
		}
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
			age--;
		} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
				&& today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
			age--;
		}
		return lessThanAge.years()>age;
	}
}
