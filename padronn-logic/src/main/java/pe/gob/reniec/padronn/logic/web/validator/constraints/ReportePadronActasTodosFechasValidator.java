package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.form.ReportePadronActas;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronActasTodos;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 04/09/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */

public class ReportePadronActasTodosFechasValidator implements ConstraintValidator<ReportePadronActasTodosFechas, ReportePadronActasTodos> {

    @Override
    public void initialize(ReportePadronActasTodosFechas constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ReportePadronActasTodos reportePadronActas, ConstraintValidatorContext constraintValidatorContext) {
        if( reportePadronActas.getFeIni().isEmpty() || reportePadronActas.getFeFin().isEmpty()  )
            return true;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = new Date();
            Date feIniDate = reportePadronActas.getFeIniDate();
            Date feFinDate = reportePadronActas.getFeFinDate();

            //valida que la fecha final no sea menor que la fecha inicial
            if(feIniDate.after(feFinDate)) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Fecha inicial mayor que fecha final")
                        .addNode("feIni")
                        .addConstraintViolation();

                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        return true;
    }
}
