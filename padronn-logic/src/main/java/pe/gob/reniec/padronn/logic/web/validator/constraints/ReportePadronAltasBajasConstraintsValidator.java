package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.apache.log4j.Logger;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronAltasBajas;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronEntidad;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 04/02/14
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportePadronAltasBajasConstraintsValidator implements ConstraintValidator<ReportePadronAltasBajasConstraints, ReportePadronAltasBajas> {

    Logger logger = Logger.getLogger(getClass());
    @Override
    public void initialize(ReportePadronAltasBajasConstraints constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronAltasBajas reportePadronAltasBajas, ConstraintValidatorContext context) {
        boolean validaFechas;
        Calendar feIni = reportePadronAltasBajas.getFeIniCalendar();
        Calendar feFin = reportePadronAltasBajas.getFeFinCalendar();
        String tiRegFecha = reportePadronAltasBajas.getTiRegFecha()==null?"":reportePadronAltasBajas.getTiRegFecha();
        validaFechas = true;
        if ( feIni != null || feFin != null || !tiRegFecha.isEmpty()) {
            if (feIni != null && feFin != null) {
                validaFechas = feFin.after(feIni) || (feIni.get(Calendar.YEAR) == feFin.get(Calendar.YEAR) && feIni.get(Calendar.MONTH) == feFin.get(Calendar.MONTH) && feIni.get(Calendar.DAY_OF_MONTH) == feFin.get(Calendar.DAY_OF_MONTH));
                if (!validaFechas) {
                    context
                            .buildConstraintViolationWithTemplate("Fecha inicial mayor que la fecha final")
                            .addNode("feIni")
                            .addConstraintViolation();
                }
            } else if (feIni == null && feFin != null) {
                context
                        .buildConstraintViolationWithTemplate("La Fecha Inicial es invalida para el periodo")
                        .addNode("feIni")
                        .addConstraintViolation();
                validaFechas = false;
            } else if (feFin == null && feIni != null) {
                context
                        .buildConstraintViolationWithTemplate("La Fecha final invalida para el periodo")
                        .addNode("feFin")
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
        return validaFechas ;
    }
}
