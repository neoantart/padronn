package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.form.SignupUpdateForm;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jfloresh on 06/03/2015.
 */
@Component
public class SignupUpdateFormValidatorImpl implements ConstraintValidator<SignupUpdateFormValidator, SignupUpdateForm> {

    @Autowired
    MessageSource messageSource;

    @Autowired
    PadronProperties padronProperties;

    @Override
    public void initialize(SignupUpdateFormValidator constraintAnnotation) {   }

    @Override
    public boolean isValid(SignupUpdateForm signupUpdateForm, ConstraintValidatorContext context) {
        if (signupUpdateForm.getGrupos() != null && signupUpdateForm.getEntidades() != null) {
            List<String> entidades = signupUpdateForm.getEntidades();
            if (entidades.size() > 0) {
                List<String> coTipoEntidades = new ArrayList<String>();
                for (String entidad : entidades) {
                    String[] entidadSplit = entidad.split("_");
                    if(entidadSplit.length > 1 && entidadSplit[1]!=null)
                        coTipoEntidades.add(entidadSplit[1]);
                }

                if (signupUpdateForm.getGrupos().contains(padronProperties.REGISTRADOR_PERFIL)
                            && !(coTipoEntidades.contains(padronProperties.MUNICIPALIDAD_ENTIDAD) ||
                            coTipoEntidades.contains(padronProperties.ESTABLECIMIENTO_ENTIDAD) ) ) {

                    context
                            .buildConstraintViolationWithTemplate(
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
