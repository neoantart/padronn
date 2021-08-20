package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUsuarioEntidad;

/**
 * Created by jfloresh on 03/09/2014.
 */
public class ConsultaUsuarioEntidadValidator implements Validator {

    public ConsultaUsuarioEntidadValidator() {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ConsultaUsuarioEntidad.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ConsultaUsuarioEntidad consultaUsuarioEntidad = (ConsultaUsuarioEntidad) target;
        String coEntidad = consultaUsuarioEntidad.getCoEntidad();
        //if (coEntidad.isEmpty()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "coEntidad", "NotEmpty.consultaUsuarioEntidad.coEntidad");
        //}
    }



}
