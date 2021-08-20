package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by jfloresh on 14/09/2015.
 */
public class CoPadronNominalValidator implements ConstraintValidator<CoPadronNominal, String> {


    CoPadronNominal coPadronNominal;

    @Override
    public void initialize(CoPadronNominal coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    @Override
    public boolean isValid(String coPadronNominal, ConstraintValidatorContext constraintValidatorContext) {
        return coPadronNominal == null || coPadronNominal.isEmpty() || coPadronNominal.matches("\\d{8}");
    }

}