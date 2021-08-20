package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronEdad;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 14/02/14
 * Time: 03:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportePadronEdadConstraintValidator implements ConstraintValidator<ReportePadronEdadConstraint, ReportePadronEdad> {
    @Override
    public void initialize(ReportePadronEdadConstraint constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronEdad reportePadronEdad, ConstraintValidatorContext context) {
        boolean validaFechas;
        Calendar feIni = reportePadronEdad.getFeIniCalendar();
        Calendar feFin = reportePadronEdad.getFeFinCalendar();

        validaFechas = true;
        if (feIni != null && feFin != null) {
            if (feIni != null && feFin != null) {
                validaFechas = feFin.after(feIni) || (feIni.get(Calendar.YEAR) == feFin.get(Calendar.YEAR) && feIni.get(Calendar.MONTH) == feFin.get(Calendar.MONTH) && feIni.get(Calendar.DAY_OF_MONTH) == feFin.get(Calendar.DAY_OF_MONTH));
                if (!validaFechas) {
                    context
                            .buildConstraintViolationWithTemplate("Fecha inicial mayor que la fecha final")
                            .addNode("feIni")
                            .addConstraintViolation();
                }

            } else {
                validaFechas = true;
            }
        }
        return validaFechas;
    }
}

