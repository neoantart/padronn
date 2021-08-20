package pe.gob.reniec.padronn.logic.web.validator.constraints;
import org.apache.log4j.Logger;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronEdad;
import pe.gob.reniec.padronn.logic.model.form.ReporteUsuarios;
import pe.gob.reniec.padronn.logic.web.view.ReporteUsuariosView;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
/**
 * Created by mfigueroa on 20/06/2014.
 */
public class ReporteUsuarioMinsaConstraintsValidator implements ConstraintValidator<ReporteUsuarioMinsaConstraints, ReporteUsuarios>{

    Logger logger = Logger.getLogger(getClass());

    @Override
    public void initialize(ReporteUsuarioMinsaConstraints constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public boolean isValid(ReporteUsuarios reporteUsuarios, ConstraintValidatorContext context) {
        String coEntidad = reporteUsuarios.getCoEntidad();
        String coUbigeoInei = reporteUsuarios.getCoUbigeo();
        String esPadron =reporteUsuarios.getEsUsuario() ;
        String coGrupo = reporteUsuarios.getCoGrupo();

        if ( !coGrupo.isEmpty() && !esPadron.isEmpty() && !coUbigeoInei.isEmpty() && !coEntidad.isEmpty() && !coGrupo.substring(0, 6).equals(coEntidad) && !esPadron.substring(0, 6).equals(coUbigeoInei) && !coUbigeoInei.substring(0, 6).equals(coEntidad) && !coEntidad.substring(0, 6).equals(coUbigeoInei)) {
            context
                    .buildConstraintViolationWithTemplate("El centro poblado no pertenece al ubigeo indicado.")
                    .addNode("coUbigeo")
                    .addConstraintViolation();
            return false;
            }
        return true;
    }



}


