package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pe.gob.reniec.padronn.logic.dao.RectificacionDao;
import pe.gob.reniec.padronn.logic.model.Madre;
import pe.gob.reniec.padronn.logic.model.Padre;
import pe.gob.reniec.padronn.logic.model.Persona;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: jronal at gmail dot com
 * Date: 31/10/13
 * Time: 04:09 PM
 */
public class DatosPersonalesPadreValidator implements ConstraintValidator<DatosPersonalesPadre, Padre> {

    Logger logger = Logger.getLogger(getClass());

    @Autowired
    RectificacionDao rectificacionDao;

    @Override
    public void initialize(DatosPersonalesPadre constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(Padre padre, ConstraintValidatorContext context) {
        String coPadronNominal = padre.getCoPadronNominal();
        Padre padrePadron = rectificacionDao.getDatosPersonalesPadre(coPadronNominal);
        String coDniJefeFam = padre.getCoDniJefeFam();
        if (padre.equals(padrePadron)) {
            context
                    .buildConstraintViolationWithTemplate("Primer apellido sin cambios")
                    .addNode("apPrimerJefe")
                    .addConstraintViolation();

            context
                    .buildConstraintViolationWithTemplate("Segundo apellido sin cambios")
                    .addNode("apSegundoJefe")
                    .addConstraintViolation();

            context
                    .buildConstraintViolationWithTemplate("Prenombres sin cambios")
                    .addNode("prenomJefe")
                    .addConstraintViolation();

            context
                    .buildConstraintViolationWithTemplate("DNI sin cambios")
                    .addNode("coDniJefeFam")
                    .addConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Tipo de vinculo sin cambios")
                    .addNode("tiVinculoJefe")
                    .addConstraintViolation();

            return false;
        }

        if(coDniJefeFam == null)
            coDniJefeFam = padrePadron.getCoDniJefeFam();
        if (coDniJefeFam == null)
            return true;
        if(coDniJefeFam.trim().isEmpty())
            return true;
        Persona padreAni = rectificacionDao.getPersonaAni(coDniJefeFam, Persona.TipoPersona.PADRE);
        if(padreAni!=null){
            boolean bApPrimerPadre = padre.getApPrimerJefe().trim().equals(padreAni.getPrimerApellido().trim());
            boolean bApSegundoPadre = padre.getApSegundoJefe().trim().equals(padreAni.getSegundoApellido().trim());
            boolean bPrenomJefe = padre.getPrenomJefe().trim().equals(padreAni.getNombres().trim());
            if(!bApPrimerPadre) {
                context
                        .buildConstraintViolationWithTemplate("Primer apellido no es igual que el DNI")
                        .addNode("apPrimerJefe")
                        .addConstraintViolation();
            }
            if(!bApSegundoPadre) {
                context
                        .buildConstraintViolationWithTemplate("Segundo apellido no es igual que el DNI")
                        .addNode("apSegundoJefe")
                        .addConstraintViolation();
            }
            if (!bPrenomJefe) {
                context
                        .buildConstraintViolationWithTemplate("Prenombres no es igual que el DNI")
                        .addNode("prenomJefe")
                        .addConstraintViolation();
            }
            return bApPrimerPadre && bApSegundoPadre && bPrenomJefe;
        } else {
            context
                    .buildConstraintViolationWithTemplate("DNI no corresponde a la persona o no existe")
                    .addNode("coDniJefeFam")
                    .addConstraintViolation();
            return false;
        }
    }
}
