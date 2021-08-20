package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by jfloresh on 10/09/2015.
 */
public class CoUbigeoValidator implements ConstraintValidator<CoUbigeo, String> {

    CoUbigeo coUbigeo;

    @Override
    public void initialize(CoUbigeo coUbigeo) {
        this.coUbigeo = coUbigeo;
    }

    @Override
    public boolean isValid(String coUbigeo, ConstraintValidatorContext context) {
        return coUbigeo == null || coUbigeo.isEmpty() || coUbigeo.matches("\\d*");
    }

}
