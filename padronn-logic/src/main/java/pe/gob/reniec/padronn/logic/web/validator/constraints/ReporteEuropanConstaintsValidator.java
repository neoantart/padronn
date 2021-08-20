package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReporteEuropan;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 01/02/14
 * Time: 05:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReporteEuropanConstaintsValidator implements ConstraintValidator<ReporteEuropanConstaints, ReporteEuropan> {


    @Override
    public void initialize(ReporteEuropanConstaints constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReporteEuropan reporteEuropan, ConstraintValidatorContext context) {
        boolean validaFechas;
        Calendar feIni = reporteEuropan.getFeIniCalendar();
        Calendar feFin = reporteEuropan.getFeFinCalendar();
        validaFechas = true;
        if ( feIni != null && feFin != null) {
            validaFechas = feFin.after(feIni) || (feIni.get(Calendar.YEAR) == feFin.get(Calendar.YEAR) && feIni.get(Calendar.MONTH) == feFin.get(Calendar.MONTH) && feIni.get(Calendar.DAY_OF_MONTH) == feFin.get(Calendar.DAY_OF_MONTH));
            if (!validaFechas) {
                context
                        .buildConstraintViolationWithTemplate("La fecha inicial no puede ser mayor que la Fecha Final")
                        .addNode("feIni")
                        .addConstraintViolation();
            }
        }
        return validaFechas ;
    }
}
