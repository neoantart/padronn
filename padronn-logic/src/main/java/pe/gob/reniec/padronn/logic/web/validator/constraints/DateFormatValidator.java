package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 03/07/13
 * Time: 12:27 PM
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

	DateFormat dateFormat;

	@Override
	public void initialize(DateFormat dateFormat) {
		this.dateFormat=dateFormat;
	}

	@Override
	public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
		if(date!=null&&!date.isEmpty()){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.format());
			try {
				simpleDateFormat.setLenient(false);
				Date parsed = simpleDateFormat.parse(date);
				if(date.equals(simpleDateFormat.format(parsed))){
					return true;
				}
				return false;
			} catch (ParseException e) {
				return false;
			}
		}
		return true;
	}
}
