package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by JFLORESH on 20/03/2014.
 */

public class DateFutureValidator implements ConstraintValidator<DateFuture, String> {

    DateFuture dateFuture;

    @Override
    public void initialize(DateFuture dateFuture) {
        this.dateFuture = dateFuture;
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        Calendar current = Calendar.getInstance();
        Calendar dateCalendar = getDateCalendar(date);
        if (dateCalendar == null) {
            return true;
        }
        return !dateCalendar.after(current);
    }


    public Calendar getDateCalendar(String date) {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            calendar = null;
        }
        return calendar;
    }

}
