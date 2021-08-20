package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronActivos;

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
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */

public class ReportePadronActivosFechasValidator implements ConstraintValidator<ReportePadronActivosFechas, ReportePadronActivos> {

    @Override
    public void initialize(ReportePadronActivosFechas constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronActivos reportePadronActivos, ConstraintValidatorContext context) {
        boolean validaFechas;
        Calendar feIni = reportePadronActivos.getFeIniCalendar();
        Calendar feFin = reportePadronActivos.getFeFinCalendar();
        Calendar current = Calendar.getInstance();

        validaFechas = true;
        if ( feIni != null && feFin != null) {
            if (feIni != null && feFin != null) {
                validaFechas = feFin.after(feIni) || (feIni.get(Calendar.YEAR) == feFin.get(Calendar.YEAR) && feIni.get(Calendar.MONTH) == feFin.get(Calendar.MONTH) && feIni.get(Calendar.DAY_OF_MONTH) == feFin.get(Calendar.DAY_OF_MONTH));
                // todo: mensaje a properties
                if (!validaFechas) {
                    context
                            .buildConstraintViolationWithTemplate("Fecha inicial mayor que fecha final")
                            .addNode("feIni")
                            .addConstraintViolation();
                }
                /*validaFechaFutura = !feIni.after(current);
                if (!validaFechaFutura) {
                    context
                            .buildConstraintViolationWithTemplate("La fecha inicial no puede ser mayor que la Fecha actual")
                            .addNode("feIni")
                            .addConstraintViolation();
                }
                validaFechaFutura = !feFin.after(current);
                if (!validaFechaFutura) {
                    context
                            .buildConstraintViolationWithTemplate("La fecha Final no puede ser mayor que la Fecha actual")
                            .addNode("feFin")
                            .addConstraintViolation();
                }*/
            } /*else if (feIni == null && feFin != null) {
                context
                        .buildConstraintViolationWithTemplate("La Fecha Inicial es invalida")
                        .addNode("feIni")
                        .addConstraintViolation();
                validaFechas = false;
            } else if (feFin == null && feIni != null) {
                context
                        .buildConstraintViolationWithTemplate("La Fecha final es invalida")
                        .addNode("feFin")
                        .addConstraintViolation();
                validaFechas = false;
            }*/ else {
                validaFechas = true;
            }
        }
        return validaFechas ;
    }
}
