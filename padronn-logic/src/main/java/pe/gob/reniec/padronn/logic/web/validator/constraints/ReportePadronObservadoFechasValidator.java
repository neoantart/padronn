package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronObservado;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

/**
 * Created by JFLORESH on 20/03/2014.
 */
public class ReportePadronObservadoFechasValidator   implements ConstraintValidator<ReportePadronObservadoFechas, ReportePadronObservado> {
    @Override
    public void initialize(ReportePadronObservadoFechas constraintAnnotation) {
    }

    @Override
    public boolean isValid(ReportePadronObservado reportePadronObservado, ConstraintValidatorContext constraintValidatorContext) {

        boolean validaFechas;
        Calendar feIni = reportePadronObservado.getFeIniCalendar();
        Calendar feFin = reportePadronObservado.getFeFinCalendar();

        validaFechas = true;
        if ( feIni != null && feFin != null) {
            if (feIni != null && feFin != null) {
                validaFechas = feFin.after(feIni) || (feIni.get(Calendar.YEAR) == feFin.get(Calendar.YEAR) && feIni.get(Calendar.MONTH) == feFin.get(Calendar.MONTH) && feIni.get(Calendar.DAY_OF_MONTH) == feFin.get(Calendar.DAY_OF_MONTH));
                // todo: mensaje a properties
                if (!validaFechas) {
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("Fecha inicial mayor que fecha final")
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
