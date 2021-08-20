package pe.gob.reniec.padronn.logic.web.validator;

import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.RuleResultDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.CambioCredencial;
import pe.gob.reniec.padronn.logic.model.UsuarioExterno;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;
import pe.gob.reniec.padronn.logic.web.validator.checks.CambioCredencialPasswordCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
//import java.util.Locale;

/**
 * Clase CoDominioValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 24/06/13 07:46 PM
 */
@Component
public class CambioCredencialPasswordValidator extends AbstractBaseValidator
        implements ConstraintValidator<CambioCredencialPasswordCheck, CambioCredencial> {

    @Autowired
    UsuarioExternoService usuarioExternoService;

    @Override
    public void initialize(CambioCredencialPasswordCheck constraintAnnotation) { }

    @Override
    public boolean isValid(CambioCredencial cambioCredencial, ConstraintValidatorContext context) {
        if (cambioCredencial == null)
            return true;

        if (cambioCredencial.getDePasswordNuevo() != null && cambioCredencial.getDePassword() != null) {
            if (!cambioCredencial.getDePasswordNuevo().equals(cambioCredencial.getDePasswordNuevoRepetido())) {
                // son iguales?
                //context.
                //    buildConstraintViolationWithTemplate(
                //        messageSource.getMessage("NotEquals.padronNominal.dePasswordNuevo", null, Locale.getDefault()))
                //    .addNode("dePasswordNuevo")
                //    .addConstraintViolation();
                context.
                        buildConstraintViolationWithTemplate(
                                "No coincide con nueva contraseña")
                        .addNode("dePasswordNuevoRepetido")
                        .addConstraintViolation();

                context.disableDefaultConstraintViolation();
                return false;

            } else {
                if (usuarioExternoService.getUsuario(cambioCredencial.getCoUsuario(), cambioCredencial.getDePassword()) == null) {
                    // usuario/contraseña correctos ?
                    context.
                            buildConstraintViolationWithTemplate(
                                    "Contraseña incorrecta")
                            .addNode("dePassword")
                            .addConstraintViolation();

                    context.disableDefaultConstraintViolation();
                    return false;
                }
                else{

                    if (cambioCredencial.getCoUsuario().equals(cambioCredencial.getDePasswordNuevo())||
                         cambioCredencial.getCoUsuario().equals(cambioCredencial.getDePasswordNuevoRepetido())){

                        context.
                                buildConstraintViolationWithTemplate(
                                        "La nueva contraseña debe ser diferente a su DNI")
                                .addNode("dePasswordNuevo")
                                .addConstraintViolation();

                        context.
                                buildConstraintViolationWithTemplate(
                                        "La nueva contraseña debe ser diferente a su DNI")
                                .addNode("dePasswordNuevoRepetido")
                                .addConstraintViolation();

                        context.disableDefaultConstraintViolation();
                        return false;
                    }
                    else{
                        if(cambioCredencial.getDePassword().equals(cambioCredencial.getDePasswordNuevo()) &&
                                cambioCredencial.getDePassword().equals(cambioCredencial.getDePasswordNuevoRepetido()) ){
                            context.buildConstraintViolationWithTemplate("La nueva contraseña debe ser distinta a la anterior")
                                    .addNode("dePasswordNuevo")
                                    .addConstraintViolation();

                            context.buildConstraintViolationWithTemplate("La nueva contraseña debe ser distinta a la anterior")
                                    .addNode("dePasswordNuevoRepetido")
                                    .addConstraintViolation();

                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
