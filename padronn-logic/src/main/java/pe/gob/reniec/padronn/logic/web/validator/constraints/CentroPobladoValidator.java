package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 11/02/14
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */

public class CentroPobladoValidator implements ConstraintValidator<CentroPobladoConstraint, PrecotejoRegistro> {

    @Override
    public void initialize(CentroPobladoConstraint constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(PrecotejoRegistro precotejoRegistro, ConstraintValidatorContext constraintValidatorContext) {
        String coUbigeoInei = precotejoRegistro.getCoUbigeoInei();
        String coCentroPoblado = precotejoRegistro.getCoCentroPoblado();

        if ( !coCentroPoblado.isEmpty() && !coUbigeoInei.isEmpty() &&!coCentroPoblado.substring(0, 6).equals(coUbigeoInei)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("El centro poblado no pertenece al ubigeo indicado.")
                    .addNode("coCentroPoblado")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
