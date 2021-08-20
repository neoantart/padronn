package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pe.gob.reniec.padronn.logic.dao.RectificacionDao;
import pe.gob.reniec.padronn.logic.model.Madre;
import pe.gob.reniec.padronn.logic.model.Menor;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.Persona;
import pe.gob.reniec.padronn.logic.service.PadronService;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: jronal at gmail dot com
 * Date: 31/10/13
 * Time: 03:20 PM
 */
public class DatosPersonalesMadreValidator implements ConstraintValidator<DatosPersonalesMadre, Madre> {

    private static Logger LOG = Logger.getLogger(DatosPersonalesMadreValidator.class);

    @Autowired
    RectificacionDao rectificacionDao;

    @Autowired
    PadronService padronService;

    @Override
    public void initialize(DatosPersonalesMadre constraintAnnotation) {

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(Madre madre, ConstraintValidatorContext context) {
        String coPadronNominal = madre.getCoPadronNominal();
        Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(coPadronNominal);
        String coDniMadre = madre.getCoDniMadre();

        /*if (madre.equals(madrePadron)) {
            context
                    .buildConstraintViolationWithTemplate("Primer apellido sin cambios")
                    .addNode("apPrimerMadre")
                    .addConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Segundo apellido sin cambios")
                    .addNode("apSegundoMadre")
                    .addConstraintViolation();

            context
                    .buildConstraintViolationWithTemplate("Prenombres sin cambios")
                    .addNode("prenomMadre")
                    .addConstraintViolation();

            context
                    .buildConstraintViolationWithTemplate("DNI sin cambios")
                    .addNode("coDniMadre")
                    .addConstraintViolation();

            return false;
        }*/

        PadronNominal padronNominal = padronService.obtenerPorCodigoPadron(coPadronNominal);

        if (madre.getApPrimerMadre() != null && padronNominal.getApSegundoMenor()!=null) {
            LOG.info("apPrimerMadre=" + madre.getApPrimerMadre());
            LOG.info("apSegundoMenor=" + padronNominal.getApSegundoMenor());
            if (!madre.getApPrimerMadre().trim().equals(padronNominal.getApSegundoMenor().trim()) && padronNominal.getTiVinculoJefe()!=null && !padronNominal.getTiVinculoJefe().equals("1")) {
                context
                        .buildConstraintViolationWithTemplate("Primer apellido de la madre no es igual que el Segundo apellido del Menor")
                        .addNode("apPrimerMadre")
                        .addConstraintViolation();
                return false;
            }
        }
        if(coDniMadre == null)
            coDniMadre = madrePadron.getCoDniMadre();
        if (coDniMadre == null)
            return true;
        if(coDniMadre.trim().isEmpty())
            return true;
        Persona madreAni = rectificacionDao.getPersonaAni(coDniMadre, Persona.TipoPersona.MADRE);
        if(madreAni!=null){
            boolean bApPrimerMadre = madre.getApPrimerMadre().trim().equals(madreAni.getPrimerApellido().trim());
            boolean bApSegundoMadre = madre.getApSegundoMadre().trim().equals(madreAni.getSegundoApellido().trim());
            boolean bPrenomMadre = madre.getPrenomMadre().trim().equals(madreAni.getNombres().trim());

            if(!bApPrimerMadre) {
                context
                        .buildConstraintViolationWithTemplate("Primer apellido no es igual que el DNI")
                        .addNode("apPrimerMadre")
                        .addConstraintViolation();
            }
            if(!bApSegundoMadre) {
                context
                        .buildConstraintViolationWithTemplate("Segundo apellido no es igual que el DNI")
                        .addNode("apSegundoMadre")
                        .addConstraintViolation();
            }
            if (!bPrenomMadre) {
                context
                        .buildConstraintViolationWithTemplate("Prenombres no es igual que el DNI")
                        .addNode("prenomMadre")
                        .addConstraintViolation();
            }

            return bApPrimerMadre && bApSegundoMadre && bPrenomMadre;
        } else {
            context
                    .buildConstraintViolationWithTemplate("DNI no corresponde a la persona o no existe")
                    .addNode("coDniMadre")
                    .addConstraintViolation();
            return false;
        }
    }
}