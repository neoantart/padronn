package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 10/01/14
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */

public class UbigeoCentroPobladoValidator implements ConstraintValidator<UbigeoCentroPoblado, PadronNominal> {

    @Override
    public void initialize(UbigeoCentroPoblado constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
        String coUbigeoInei = padronNominal.getCoUbigeoInei();
        String coCentroPoblado = padronNominal.getCoCentroPoblado();

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
