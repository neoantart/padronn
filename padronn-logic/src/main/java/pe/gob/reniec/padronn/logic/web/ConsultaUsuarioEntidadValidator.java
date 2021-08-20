package pe.gob.reniec.padronn.logic.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUsuarioDatos;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUsuarioEntidad;

/**
 * Created by jfloresh on 02/09/2014.
 */
public class ConsultaUsuarioEntidadValidator implements Validator  {

    public ConsultaUsuarioEntidadValidator() {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ConsultaUsuarioEntidad.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
