package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class NuCelularValidator implements ConstraintValidator<NuCelular, PadronNominal> {

    @Override
    public void initialize(NuCelular constraintAnnotation) {  }

    @Override
    public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
        String coFuenteDatos = padronNominal.getCoFuenteDatos();
        String nuCelularMadre = padronNominal.getNuCelMadre();

        if(coFuenteDatos.equals("12") && nuCelularMadre.isEmpty()){ //Telemonitoreo.
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Numero de celular es obligatorio")
                    .addNode("nuCelMadre")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
