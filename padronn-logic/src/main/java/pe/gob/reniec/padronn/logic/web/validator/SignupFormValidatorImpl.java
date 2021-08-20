package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jfloresh on 25/02/2015.
 */

@Component
public class SignupFormValidatorImpl implements ConstraintValidator<SignupFormValidator, SignupForm> {
    @Autowired
    MessageSource messageSource;

    @Autowired
    PadronProperties padronProperties;

    @Override
    public void initialize(SignupFormValidator constraintAnnotation) {

    }

    @Override
    public boolean isValid(SignupForm signupForm, ConstraintValidatorContext context) {
        if (signupForm.getGrupos() != null && signupForm.getEntidades() != null) {
            List<String> entidades = signupForm.getEntidades();
            if (entidades.size() > 0) {
                List<String> coTipoEntidades = new ArrayList<String>();
                for (String entidad : entidades) {
                    String[] entidadSplit = entidad.split("_");
                    if(entidadSplit.length > 1 && entidadSplit[1]!=null)
                        coTipoEntidades.add(entidadSplit[1]);
                }
                if (signupForm.getGrupos().contains(padronProperties.REGISTRADOR_PERFIL)
                    && !(coTipoEntidades.contains(padronProperties.MUNICIPALIDAD_ENTIDAD) ||
                         coTipoEntidades.contains(padronProperties.ESTABLECIMIENTO_ENTIDAD) ) ) {

                    context.buildConstraintViolationWithTemplate(
                                    messageSource.getMessage("Invalid.Signup.entidadPerfil", null, Locale.getDefault()))
                            .addNode("errorMessage")
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        return true;
    }

}