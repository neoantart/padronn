package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronActas;
//import sun.util.resources.CalendarData_th;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 04/09/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */

public class ReportePadronActasFechasValidator implements ConstraintValidator<ReportePadronActasFechas, ReportePadronActas> {

    @Override
    public void initialize(ReportePadronActasFechas constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronActas reportePadronActas, ConstraintValidatorContext constraintValidatorContext) {
        if( reportePadronActas.getFeIni().isEmpty() || reportePadronActas.getFeFin().isEmpty()  )
            return true;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = new Date();
            Date feIniDate = reportePadronActas.getFeIniDate();
            Date feFinDate = reportePadronActas.getFeFinDate();

            /*Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            Calendar calendar3 = Calendar.getInstance();
            calendar1.setTime(feIniDate);
            calendar2.setTime(feFinDate);


            // valida que este en el presente año
            if( calendar1.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR) || calendar1.get(Calendar.YEAR) != date.getYear()) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Fecha inicial no corresponde al año actual")
                        .addNode("feIniActa")
                        .addConstraintViolation();

                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Fecha final no corresponde al año actual")
                        .addNode("feFinActa")
                        .addConstraintViolation();
                return false;
            }  */


            //valida que la fecha final no sea menor que la fecha inicial
            if(feIniDate.after(feFinDate)) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Fecha inicial mayor que fecha final")
                        .addNode("feIni")
                        .addConstraintViolation();

                /*constraintValidatorContext
                        .buildConstraintViolationWithTemplate("")
                        .addNode("feFinActa")
                        .addConstraintViolation();*/
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        return true;
    }
}
