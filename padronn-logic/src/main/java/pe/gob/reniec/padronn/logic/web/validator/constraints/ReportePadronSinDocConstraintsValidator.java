package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronSinDoc;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 04/02/14
 * Time: 09:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportePadronSinDocConstraintsValidator  implements ConstraintValidator<ReportePadronSinDocConstraints, ReportePadronSinDoc> {
    @Override
    public void initialize(ReportePadronSinDocConstraints constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronSinDoc reportePadronSinDoc, ConstraintValidatorContext context) {
        boolean validaFechas;
        Calendar feIni = reportePadronSinDoc.getFeIniCalendar();
        Calendar feFin = reportePadronSinDoc.getFeFinCalendar();
        Calendar current = Calendar.getInstance();
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
/*                validaFechaFutura = !feIni.after(current);
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
