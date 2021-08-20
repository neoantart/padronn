package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronEntidad;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 04/09/13
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportePadronEntidadFechasValidator implements ConstraintValidator<ReportePadronEntidadFechas, ReportePadronEntidad> {

    @Override
    public void initialize(ReportePadronEntidadFechas constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronEntidad reportePadronEntidad, ConstraintValidatorContext context) {
        boolean validaFechas;
        Calendar feIni = reportePadronEntidad.getFeIniCalendar();
        Calendar feFin = reportePadronEntidad.getFeFinCalendar();
        validaFechas = true;
        if ( feIni != null && feFin != null) {
            if (feIni != null && feFin != null) {
                validaFechas = feFin.after(feIni) || (feIni.get(Calendar.YEAR) == feFin.get(Calendar.YEAR) && feIni.get(Calendar.MONTH) == feFin.get(Calendar.MONTH) && feIni.get(Calendar.DAY_OF_MONTH) == feFin.get(Calendar.DAY_OF_MONTH));
                if (!validaFechas) {
                    context
                            .buildConstraintViolationWithTemplate("La fecha inicial no puede ser mayor que la Fecha Final")
                            .addNode("feIni")
                            .addConstraintViolation();
                }
            } else {
                validaFechas = true;
            }
        }
        return validaFechas ;
    }

}
