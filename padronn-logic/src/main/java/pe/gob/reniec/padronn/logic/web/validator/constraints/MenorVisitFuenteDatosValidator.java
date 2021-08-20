package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MenorVisitFuenteDatosValidator implements ConstraintValidator<MenorVisitFuenteDatos, PadronNominal> {
    @Override
    public void initialize(MenorVisitFuenteDatos MenorVisitFuenteDatos) {
    }

    @Override
    public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
        boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;
        String coFuenteDatos=padronNominal.getCoFuenteDatos();

        boolean existeCoFuenteDatos=coFuenteDatos!=null&&!coFuenteDatos.isEmpty();
        if(!isMenorVisitado){
            return existeCoFuenteDatos;
        }else{
            return true;
        }
    }
}
