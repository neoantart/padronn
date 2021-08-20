package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.form.ActaForm;
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
 * Date: 22/08/13
 * Time: 09:36 AM
 * To change this template use File | Settings | File Templates.
 */
//@Component
public class ActaFormFechasValidator  implements ConstraintValidator<ActaFormFechas, ActaForm> {

    @Autowired
    MessageSource messageSource;

    @Override
    public void initialize(ActaFormFechas constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(ActaForm actaForm, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("actaForm:" + actaForm);
        if( actaForm.getFeIniActa().isEmpty() || actaForm.getFeFinActa().isEmpty()  )
            return true;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = new Date();
            Date feIniDate = actaForm.getFeIniActaDate();
            Date feFinDate = actaForm.getFeFinActaDate();

            /*Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            Calendar calendar3 = Calendar.getInstance();
            calendar1.setTime(feIniDate);
            calendar2.setTime(feFinDate);


            // valida que este en el presente año
            if( calendar1.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR) || calendar1.get(Calendar.YEAR) != date.getYear()) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Fecha inicial no corresponde al año actual")
                        .addNode("feIniActa")
                        .addConstraintViolation();

                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Fecha final no corresponde al año actual")
                        .addNode("feFinActa")
                        .addConstraintViolation();
                return false;
            }  */


            //valida que la fecha final no sea menor que la fecha inicial
            if(feIniDate.after(feFinDate)){
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("La Fecha Inicial no puede ser menor que la Fecha Final")
                        .addNode("feIniActa")
                        .addConstraintViolation();

                /*constraintValidatorContext
                        .buildConstraintViolationWithTemplate("")
                        .addNode("feFinActa")
                        .addConstraintViolation();*/
                return false;
            }
            //return false;
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }

        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date feIniActaDate = null;
        Date feFinActaDate = null;
        Date curDate = null;
        //DateFormat dateFormat= (DateFormat) new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {
            feIniActaDate = simpleDateFormat.parse(actaForm.getFeIniActa());
            feFinActaDate = simpleDateFormat.parse(actaForm.getFeFinActa());
            //curDate = simpleDateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert feIniActaDate != null;
        assert feFinActaDate != null;

        if( actaForm.getFeFinActa() == null || actaForm.getFeFinActa().isEmpty() ){
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(
                            messageSource.getMessage("Acta.empty.feIni", null, Locale.getDefault()))
                    .addNode("feIniActa")
                    .addConstraintViolation();
            return false;
        }
        if( actaForm.getFeFinActa() == null || actaForm.getFeFinActa().isEmpty() ){
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(
                            messageSource.getMessage("Acta.empty.feFin", null, Locale.getDefault()))
                    .addNode("feFinActa")
                    .addConstraintViolation();
            return false;
        }
        if(feIniActaDate.after(feFinActaDate)){
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(
                            messageSource.getMessage("Acta.fechasError", null, Locale.getDefault()))
                    .addNode("feIniActa")
                    .addConstraintViolation();

            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(
                            messageSource.getMessage("Acta.fechasError", null, Locale.getDefault()))
                    .addNode("feFinActa")
                    .addConstraintViolation();
            return false;
        }         */
        return true;
    }
}
