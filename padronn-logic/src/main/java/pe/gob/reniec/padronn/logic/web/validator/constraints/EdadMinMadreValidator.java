package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 29/08/13
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */

public class EdadMinMadreValidator implements ConstraintValidator<EdadMinMadre, String> {

    @Autowired
    UsuarioExternoService usuarioExternoService;

    @Override
    public void initialize(EdadMinMadre constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(String nuDni, ConstraintValidatorContext context) {
          if(nuDni == null || nuDni.isEmpty())
              return true;
          return usuarioExternoService.isDNIMadre(nuDni);
        //usuarioExternoService.
    }
}
