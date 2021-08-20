package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 04/07/13
 * Time: 06:43 PM
 */
public class DateBirthValidator implements ConstraintValidator<DateBirth, String> {

	DateBirth dateBirth;

	@Override
	public void initialize(DateBirth dateBirth) {
		this.dateBirth=dateBirth;
	}

	@Override
	public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
		if(date!=null&&!date.isEmpty()){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateBirth.format());
			try {
				simpleDateFormat.setLenient(false);
				Date parsed = simpleDateFormat.parse(date);
				if(date.equals(simpleDateFormat.format(parsed))){
					Calendar now=Calendar.getInstance();
					return parsed.getTime()<=now.getTime().getTime();
				}
				return false;
			} catch (ParseException e) {
				return true;
			}
		}
		return true;
	}
}
