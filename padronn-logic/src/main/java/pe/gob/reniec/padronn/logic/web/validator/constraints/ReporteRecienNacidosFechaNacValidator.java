package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronRecienNacidos;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
/**
 * Created by paguilar on 20/08/2014.
 */
public class ReporteRecienNacidosFechaNacValidator implements ConstraintValidator<ReporteRecienNacidosFechaNac, ReportePadronRecienNacidos> {

    @Override
    public void initialize(ReporteRecienNacidosFechaNac constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronRecienNacidos reportePadronRecienNacidos, ConstraintValidatorContext context) {
        /*boolean validaFechas;

        Calendar feIni = reportePadronRecienNacidos.getFeNacIniCalendar();
        Calendar feFin = reportePadronRecienNacidos.getFeNacFinCalendar();

        validaFechas = true;
        if (feIni != null && feFin != null) {
            validaFechas = feFin.after(feIni) || (feIni.get(Calendar.YEAR) == feFin.get(Calendar.YEAR) && feIni.get(Calendar.MONTH) == feFin.get(Calendar.MONTH) && feIni.get(Calendar.DAY_OF_MONTH) == feFin.get(Calendar.DAY_OF_MONTH));
            if (!validaFechas) {
                context
                        .buildConstraintViolationWithTemplate("Fecha inicial mayor que la fecha final")
                        .addNode("feNacIni")
                        .addConstraintViolation();
            }
        }
        else if (feIni != null && feFin == null) {
            context.buildConstraintViolationWithTemplate("Fecha final del nacimiento no debe ser vacia")
                    .addNode("feNacFin")
                    .addConstraintViolation();
            validaFechas = false;
        } else if (feIni == null && feFin != null) {
            context.buildConstraintViolationWithTemplate("Fecha inicial del nacimiento no debe ser vacia")
                    .addNode("feNacIni")
                    .addConstraintViolation();
            validaFechas = false;
        }
        return validaFechas;*/
        return true;
    }
}
