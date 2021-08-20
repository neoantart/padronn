package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Valida que la fecha corresponde al año actual
 * User: jfloresh
 * Date: 22/08/13
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
//@Component
public class DateCurrentYearValidator implements ConstraintValidator<DateCurrentYear, String> {

    @Autowired
    MessageSource messageSource;

    @Override
    public void initialize(DateCurrentYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(String fecha, ConstraintValidatorContext context) {
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate = simpleDateFormat.parse(fecha);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaDate);
            return !( calendar.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR) );
        }
        catch (ParseException e){
            e.printStackTrace();
            return false;
        }
    }
}
