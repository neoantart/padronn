package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronRecienNacidos;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

/**
 * Created by paguilar on 20/08/2014.
 */
public class ReporteRecienNacidosFechaInsValidator implements ConstraintValidator<ReporteRecienNacidosFechaIns, ReportePadronRecienNacidos> {

    @Override
    public void initialize(ReporteRecienNacidosFechaIns constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronRecienNacidos reportePadronRecienNacidos, ConstraintValidatorContext context) {
        boolean validaFechas;
        Calendar feIni = reportePadronRecienNacidos.getFeIniCalendar();
        Calendar feFin = reportePadronRecienNacidos.getFeFinCalendar();
        String tiRegFecha = reportePadronRecienNacidos.getTiRegFecha()==null?"":reportePadronRecienNacidos.getTiRegFecha();

        validaFechas = true;
        if (feIni != null || feFin != null || !tiRegFecha.isEmpty() ){
            if (feIni != null && feFin != null) {
                validaFechas = feFin.after(feIni) || (feIni.get(Calendar.YEAR) == feFin.get(Calendar.YEAR) && feIni.get(Calendar.MONTH) == feFin.get(Calendar.MONTH) && feIni.get(Calendar.DAY_OF_MONTH) == feFin.get(Calendar.DAY_OF_MONTH));
                if (!validaFechas) {
                    context
                            .buildConstraintViolationWithTemplate("Fecha inicial del periodo mayor que la fecha final")
                            .addNode("feIni")
                            .addConstraintViolation();
                }
            } else if (feIni != null && feFin == null) {
                context.buildConstraintViolationWithTemplate("Fecha final del periodo no debe ser vacia")
                        .addNode("feFin")
                        .addConstraintViolation();
                validaFechas = false;
            } else if (feIni == null && feFin != null) {
                context.buildConstraintViolationWithTemplate("Fecha inicial del periodo no debe ser vacia")
                        .addNode("feIni")
                        .addConstraintViolation();
                validaFechas = false;
            } else {
                validaFechas = true;
            }

            if (tiRegFecha.isEmpty()){
                context
                        .buildConstraintViolationWithTemplate("Seleccione el tipo de registro")
                        .addNode("tiRegFecha")
                        .addConstraintViolation();
                validaFechas = false;
            } else if (feIni== null && feFin== null){
                context
                        .buildConstraintViolationWithTemplate("Ingresar fecha inicial")
                        .addNode("feIni")
                        .addConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate("Ingresar fecha final")
                        .addNode("feFin")
                        .addConstraintViolation();
                validaFechas = false;
            }
        }


        return validaFechas;
    }
}
