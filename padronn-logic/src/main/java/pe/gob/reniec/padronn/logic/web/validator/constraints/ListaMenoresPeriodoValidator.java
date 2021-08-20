package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.jfree.chart.LegendItemSource;
import pe.gob.reniec.padronn.logic.model.form.ListaMenores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

/**
 * Created by jfloresh on 23/05/2014.
 */
public class ListaMenoresPeriodoValidator implements ConstraintValidator<ListaMenoresPeriodo, ListaMenores>{
    @Override
    public void initialize(ListaMenoresPeriodo constraintAnnotation) {

    }

    @Override
    public boolean isValid(ListaMenores listaMenores, ConstraintValidatorContext context) {
        boolean validaFechas;
        Calendar feIni = listaMenores.getFeIniCalendar();
        Calendar feFin = listaMenores.getFeFinCalendar();
        String tiRegFecha = listaMenores.getTiRegFecha()==null?"":listaMenores.getTiRegFecha();

        validaFechas = true;
        if ( feIni != null || feFin != null || !tiRegFecha.isEmpty()) {
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
