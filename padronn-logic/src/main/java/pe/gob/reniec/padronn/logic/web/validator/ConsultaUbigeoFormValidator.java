package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUbigeoForm;

/**
 * Created by jfloresh on 02/12/2014.
 */
public class ConsultaUbigeoFormValidator implements Validator {

    public ConsultaUbigeoFormValidator() {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ConsultaUbigeoForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ConsultaUbigeoForm consultaUbigeoForm = (ConsultaUbigeoForm) o;
        String coUbigeo = consultaUbigeoForm.getCoUbigeo();
        String feIni = consultaUbigeoForm.getFeIni();
        String feFin = consultaUbigeoForm.getFeFin();
        String tiRegFecha = consultaUbigeoForm.getTiRegFecha()==null?"":consultaUbigeoForm.getTiRegFecha();
        if (coUbigeo!=null && coUbigeo.isEmpty()) {
            errors.rejectValue("coUbigeo", "NotEmpty.consultaUsuarioDatos.coUbigeo");
        }
        if ((feIni!=null &&  feFin!=null) && feIni.isEmpty() && !feFin.isEmpty()) {
            errors.rejectValue("feIni", "NotEmpty.consultaUsuarioDatos.feIni");
        }

        if ((feIni!=null &&  feFin!=null) && !feIni.isEmpty() && feFin.isEmpty()) {
            errors.rejectValue("feFin", "NotEmpty.consultaUsuarioDatos.feFin");
        }

        if (tiRegFecha.isEmpty()){
            if ((feIni!=null && !feIni.isEmpty()) || (feFin!=null && !feFin.isEmpty())){
                errors.rejectValue("tiRegFecha", "NotEmpty.consultaUsuarioDatos.tiRegFecha");
            }
        } else {
            if ((feIni==null || feIni.isEmpty()) && (feFin==null || feFin.isEmpty())){
                errors.rejectValue("feIni", "NotEmpty.consultaUsuarioDatos.feIni");
                errors.rejectValue("feFin", "NotEmpty.consultaUsuarioDatos.feFin");
            }
        }
    }
}
