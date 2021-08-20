package pe.gob.reniec.padronn.logic.web.validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pe.gob.reniec.padronn.logic.model.UsuarioExterno;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUsuarioDatos;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;


/**
 * Created by jfloresh on 02/09/2014.
 */
public class ConsultaUsuarioDatosValidator implements Validator {

    UsuarioExternoService usuarioExternoService;

    public ConsultaUsuarioDatosValidator(UsuarioExternoService usuarioExternoService) {
        this.usuarioExternoService = usuarioExternoService;
    }

    public ConsultaUsuarioDatosValidator() {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ConsultaUsuarioDatos.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ConsultaUsuarioDatos consultaUsuarioDatos = (ConsultaUsuarioDatos) target;

        String coUsuario = consultaUsuarioDatos.getCoUsuario();
        String apPrimer = consultaUsuarioDatos.getApPrimer();
        String apSegundo = consultaUsuarioDatos.getApSegundo();
        String prenombres = consultaUsuarioDatos.getPrenombres();

        if (coUsuario!=null && !coUsuario.isEmpty()) {
            if(!coUsuario.matches("\\d{8}")) {
                errors.rejectValue("coUsuario", "Invalid.consultaUsuarioDatos.coUsuario");
            }
            else {
                if (usuarioExternoService.getUsuarioExterno(coUsuario) == null) {
                    errors.rejectValue("coUsuario", "NotExists.consultaUsuarioDatos.coUsuario");
                }
            }
        }
        else {
            if(consultaUsuarioDatos.getApPrimer().isEmpty() && consultaUsuarioDatos.getApSegundo().isEmpty() && !consultaUsuarioDatos.getPrenombres().isEmpty()) {
                errors.rejectValue("apPrimer", "NotEmpty.consultaUsuarioDatos.apPrimer");
                errors.rejectValue("apSegundo", "NotEmpty.consultaUsuarioDatos.apSegundo");
            }
            if (consultaUsuarioDatos.getApPrimer().isEmpty() && consultaUsuarioDatos.getPrenombres().isEmpty() && !consultaUsuarioDatos.getApSegundo().isEmpty()) {
                errors.rejectValue("apPrimer", "NotEmpty.consultaUsuarioDatos.apPrimer");
                errors.rejectValue("prenombres", "NotEmpty.consultaUsuarioDatos.prenombres");
            }
            if (consultaUsuarioDatos.getApSegundo().isEmpty() && consultaUsuarioDatos.getPrenombres().isEmpty() && !consultaUsuarioDatos.getApPrimer().isEmpty()) {
                errors.rejectValue("apSegundo", "NotEmpty.consultaUsuarioDatos.apSegundo");
                errors.rejectValue("prenombres", "NotEmpty.consultaUsuarioDatos.prenombres");
            }
        }

        if (coUsuario.isEmpty() && apPrimer.isEmpty() && apSegundo.isEmpty() && prenombres.isEmpty()) {
            errors.rejectValue("coUsuario", "NotEmpty.consultaUsuarioDatos.coUsuario");
        }
    }

}