package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;
import pe.gob.reniec.padronn.logic.model.form.SignupUpdateForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
//import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 13/08/13
 * Time: 05:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SignupInsertPasswordValidator implements ConstraintValidator<SignupInsertPassword, SignupForm> {

    @Autowired
    MessageSource messageSource;

    @Override
    public void initialize(SignupInsertPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(SignupForm signupForm, ConstraintValidatorContext constraintValidatorContext) {
        if(!signupForm.getDePassword().equals(signupForm.getRePassword())) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(
                            "No coinciden")
                    .addNode("dePassword")
                    .addConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(
                            "No coincide con nueva contraseña")
                    .addNode("rePassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
