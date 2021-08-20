package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.hibernate.validator.internal.engine.ConstraintValidatorContextImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.rmi.registry.LocateRegistry;
//import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 12/08/13
 * Time: 04:42 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class PadronApMenorMadreValidator implements ConstraintValidator<PadronApSegundoMenor, PadronNominal> {

    @Autowired
    MessageSource messageSource;

    private static final String TIPO_PARENTESCO_MADRE = "1";
    private static final String TIPO_PARENTESCO_PADRE = "2";

    @Override
    public void initialize(PadronApSegundoMenor constraintAnnotation) {  }

    @Override
    public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
        String apPrimerMenor = padronNominal.getApPrimerMenor();
        String apSegundoMenor = padronNominal.getApSegundoMenor();
        String apPrimerMadre = padronNominal.getApPrimerMadre();
        String apSegundoMadre = padronNominal.getApSegundoMadre();
        String apPrimerJefeFam = padronNominal.getApPrimerJefe();
        String coDniMadre = padronNominal.getCoDniMadre();

        boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;

        String coMenorEncontrado=padronNominal.getCoMenorEncontrado();
        boolean isNotMenorEncontrado=coMenorEncontrado.equalsIgnoreCase("0")&&padronNominal.getCoMenorEncontrado()!=null&&!padronNominal.getCoMenorEncontrado().isEmpty();

        if (!(isMenorVisitado && isNotMenorEncontrado)) {
            if (padronNominal.getTiVinculoJefe().equals(TIPO_PARENTESCO_MADRE)) {
                if (!apPrimerMadre.equals(apPrimerMenor)) {
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("No coincide con primer apellido de la madre")
                            .addNode("apPrimerMenor")
                            .addConstraintViolation();
                    return false;
                }
                if (!apSegundoMadre.equals(apSegundoMenor)) {
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("No coincide con segundo apellido de la madre")
                            .addNode("apSegundoMenor")
                            .addConstraintViolation();
                    return false;
                }

            }
            if (padronNominal.getTiVinculoJefe().equals(TIPO_PARENTESCO_PADRE)) {
                if (!apPrimerJefeFam.equals(apPrimerMenor)) {
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("No coincide con primer apellido del padre")
                            .addNode("apPrimerMenor")
                            .addConstraintViolation();
                    return false;
                }
                if (!apPrimerMadre.equals(apSegundoMenor)) {
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("No coincide con primer apellido de la madre")
                            .addNode("apSegundoMenor")
                            .addConstraintViolation();
                    return false;
                }
            }
            if (!padronNominal.getTiVinculoJefe().equals(TIPO_PARENTESCO_PADRE) &&
                    !padronNominal.getTiVinculoJefe().equals(TIPO_PARENTESCO_MADRE)) {
                if (!apPrimerMadre.equals(apSegundoMenor)) {
                    if((String.valueOf(coDniMadre).equals("null") || String.valueOf(coDniMadre).equals("")) && apPrimerMadre.equals(apPrimerMenor) && apSegundoMadre.equals(apSegundoMenor)){
                        return true;
                    }
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("No coincide con primer apellido de la madre")
                            .addNode("apSegundoMenor")
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        /*
        1.	Si tiene DNI
        2.	Si tiene Acta de Nacimiento (si tiene CUI o Numero de Acta de Nacimiento)
        3.	Para el caso de nuevo registro sin DNI y sin Acta de Nacimiento el segundo apellido debe ser obligatorio
        */
        String nuDniMenor = padronNominal.getNuDniMenor();
        String nuCui = padronNominal.getNuCui();

        if (!(nuDniMenor != null && nuDniMenor.length() == 8)) {
            if (apSegundoMenor != null && apSegundoMenor.length() == 0) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(
                                "Segundo Apellido Menor es obligatorio")
                        .addNode("apSegundoMenor")
                        .addConstraintViolation();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

}
