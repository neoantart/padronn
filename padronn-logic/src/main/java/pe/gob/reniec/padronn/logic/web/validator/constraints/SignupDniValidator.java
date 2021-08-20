package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
//import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 12/08/13
 * Time: 05:53 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class SignupDniValidator implements ConstraintValidator<SignupDni, SignupForm> {

    @Autowired
    MessageSource messageSource;

    @Autowired
    UsuarioExternoService usuarioExternoService;

    @Override
    public void initialize(SignupDni constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(SignupForm signupForm, ConstraintValidatorContext constraintValidatorContext) {
        String coUsuario = signupForm.getCoUsuario();
        if (usuarioExternoService.getUsuarioExterno(coUsuario) != null) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(
                            "Codigo de usuario(DNI) esta en uso.")
                    .addNode("coUsuario")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
