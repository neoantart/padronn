package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class SignupFormNombresValidator implements ConstraintValidator<SignupFormNombres, SignupForm> {

    @Autowired
    UsuarioExternoService usuarioExternoService;

    @Override
    public void initialize(SignupFormNombres constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(SignupForm signupForm, ConstraintValidatorContext constraintValidatorContext) {

        if (!usuarioExternoService.verificarDniNombres(signupForm)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("El código de usuario(DNI) no corresponde a los nombres y apellidos.")
                    .addNode("coUsuario")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}