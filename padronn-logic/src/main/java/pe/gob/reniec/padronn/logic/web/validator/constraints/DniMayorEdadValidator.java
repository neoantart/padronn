package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 22/08/13
 * Time: 04:34 PM
 * To change this template use File | Settings | File Templates.
 */


public class DniMayorEdadValidator implements ConstraintValidator<DniMayorEdad, String> {


    @Autowired
    UsuarioExternoService usuarioExternoService;
    @Override
    public void initialize(DniMayorEdad constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(String nuDni, ConstraintValidatorContext context) {
        return usuarioExternoService.isDniMayor(nuDni);
    }
}
