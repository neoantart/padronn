package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pe.gob.reniec.padronn.logic.dao.RectificacionDao;
import pe.gob.reniec.padronn.logic.model.Menor;
import pe.gob.reniec.padronn.logic.model.Persona;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: jronal at gmail dot com
 * Date: 25/10/13
 * Time: 03:56 PM
 */
public class DatosPersonalesMenorValidator implements ConstraintValidator<DatosPersonalesMenor, Menor> {
    Logger logger = Logger.getLogger(getClass());
    @Autowired
    RectificacionDao rectificacionDao;

    @Override
    public void initialize(DatosPersonalesMenor constraintAnnotation) {

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(Menor menor, ConstraintValidatorContext context) {
        String coPadronNominal = menor.getCoPadronNominal();
        Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(coPadronNominal);
        String nuDniMenor = menor.getNuDniMenor();
        // boolean equals = menorPadron.equals(menor);
        // si esta igual no mandar a guardar
        logger.debug("menor: " + menor);
        logger.debug("menorPadron: " + menorPadron);
        /*if (menor.equals(menorPadron)) {
            context
                    .buildConstraintViolationWithTemplate("Apellido paterno sin cambios")
                    .addNode("apPrimerMenor")
                    .addConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Apellido materno sin cambios")
                    .addNode("apSegundoMenor")
                    .addConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("nombres sin cambios")
                    .addNode("prenombreMenor")
                    .addConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Fecha de nacimiento sin cambios")
                    .addNode("feNacMenor")
                    .addConstraintViolation();

            context.buildConstraintViolationWithTemplate("Genero del menor sin cambios")
                    .addNode("coGeneroMenor")
                    .addConstraintViolation();

            context.buildConstraintViolationWithTemplate("CUI del menor sin cambios")
                    .addNode("nuCui")
                    .addConstraintViolation();

            context.buildConstraintViolationWithTemplate("DNI del menor sin cambios")
                    .addNode("nuDniMenor")
                    .addConstraintViolation();

            context.buildConstraintViolationWithTemplate("Fecha de nacimiento sin cambios")
                    .addNode("feNacMenor")
                    .addConstraintViolation();

            return false;
        }*/


//        logger.debug("nuDniMenor: " + nuDniMenor);
        if(nuDniMenor == null)
            nuDniMenor = menorPadron.getNuDniMenor();
        if (nuDniMenor == null) {
            //validacion con el padron nominal

            /*boolean bApPrimerMenorPadron = menor.getApSegundoMenor().equals(menorPadron.getApPrimerMenor());
            boolean bApSegundoMenorPadron = menor.getApSegundoMenor().equals(menorPadron.getApSegundoMenor());
            boolean bPrenombresMenorPadron = menor.getPrenombreMenor().equals(menorPadron.getPrenombreMenor());
            if(!bApPrimerMenorPadron) {
                context
                        .buildConstraintViolationWithTemplate("Primer apellido no es igual que el primer apellido del PADRON")
                        .addNode("apPrimerMenor")
                        .addConstraintViolation();
            }
            if(!bApSegundoMenorPadron) {
                context
                        .buildConstraintViolationWithTemplate("Segundo apellido no es igual que el segundo apellido del PADRON")
                        .addNode("apSegundoMenor")
                        .addConstraintViolation();
            }
            if(!bPrenombresMenorPadron) {
                context
                        .buildConstraintViolationWithTemplate("Prenombres no es igual que el prenombres del PADRON")
                        .addNode("prenombreMenor")
                        .addConstraintViolation();
            }*/
            return true;
        }

        if(nuDniMenor.trim().isEmpty())
            return true;
        Persona menorAni = rectificacionDao.getPersonaAni(nuDniMenor, Persona.TipoPersona.MENOR);
        if(menorAni != null) {
            boolean bApPrimerMenor = menor.getApPrimerMenor().trim().equals(menorAni.getPrimerApellido().trim());
            boolean bApSegundoMenor = menor.getApSegundoMenor().trim().equals(menorAni.getSegundoApellido().trim());
            boolean bPrenombreMenor = menor.getPrenombreMenor().trim().equals(menorAni.getNombres().trim());
            boolean bFeNacMenor = menor.getFeNacMenor().equals(menorAni.getFechaNacimiento());
            boolean bCoGeneroMenor = menor.getCoGeneroMenor().equals(menorAni.getCodigoGenero());
            /*boolean bNuCui = false;
            if (menor.getNuCui() == null || menor.getNuCui().isEmpty()) {
                bNuCui = true;
            }*/

            /*boolean bApPrimerMenorPadron = menor.getApSegundoMenor().equals(menorPadron.getApPrimerMenor());
            boolean bApSegundoMenorPadron = menor.getApSegundoMenor().equals(menorPadron.getApSegundoMenor());
            boolean bPrenombresMenorPadron = menor.getPrenombreMenor().equals(menorPadron.getPrenombreMenor());
            if(!bApPrimerMenorPadron) {
                context
                        .buildConstraintViolationWithTemplate("Primer apellido no es igual que el primer apellido del PADRON")
                        .addNode("apPrimerMenor")
                        .addConstraintViolation();
            }
            if(!bApSegundoMenorPadron) {
                context
                        .buildConstraintViolationWithTemplate("Segundo apellido no es igual que el segundo apellido del PADRON")
                        .addNode("apSegundoMenor")
                        .addConstraintViolation();
            }
            if(!bPrenombresMenorPadron) {
                context
                        .buildConstraintViolationWithTemplate("Prenombres no es igual que el prenombres del PADRON")
                        .addNode("prenombreMenor")
                        .addConstraintViolation();
            }*/

            /*if (!bNuCui) {
                context
                        .buildConstraintViolationWithTemplate("CUI del menor no es igual que el DNI")
                        .addNode("nuCui")
                        .addConstraintViolation();
            }*/
            if(!bApPrimerMenor) {
                context
                        .buildConstraintViolationWithTemplate("Apellido paterno no es igual que el primer apellido del DNI")
                        .addNode("apPrimerMenor")
                        .addConstraintViolation();
            }
            if(!bApSegundoMenor) {
                context
                        .buildConstraintViolationWithTemplate("Apellido materno no es igual que el segundo apellido del DNI")
                        .addNode("apSegundoMenor")
                        .addConstraintViolation();
            }
            if (!bPrenombreMenor) {
                context
                        .buildConstraintViolationWithTemplate("Nombres no es igual que los prenombres del DNI")
                        .addNode("prenombreMenor")
                        .addConstraintViolation();
            }
            if(!bFeNacMenor) {
                context
                        .buildConstraintViolationWithTemplate("Fecha de nacimiento no es igual que la fecha de nacimiento del DNI")
                        .addNode("feNacMenor")
                        .addConstraintViolation();
            }
            if(!bCoGeneroMenor) {
                context.buildConstraintViolationWithTemplate("Genero del menor no es igual que el DNI")
                        .addNode("coGeneroMenor")
                        .addConstraintViolation();
            }

            return /*bNuCui &&*/ bApPrimerMenor && bApSegundoMenor && bPrenombreMenor && bFeNacMenor && bCoGeneroMenor;
        } else {
            context
                    .buildConstraintViolationWithTemplate("DNI no corresponde a un menor o no existe")
                    .addNode("nuDniMenor")
                    .addConstraintViolation();
            return false;
        }
    }
}
