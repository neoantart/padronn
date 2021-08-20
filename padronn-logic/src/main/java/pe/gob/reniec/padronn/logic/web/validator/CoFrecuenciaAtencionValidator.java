package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronResumenEESS;
import pe.gob.reniec.padronn.logic.web.validator.constraints.TipoEstablecimientoEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.CoFrecuenciaAtencion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Component
public class CoFrecuenciaAtencionValidator implements ConstraintValidator<CoFrecuenciaAtencion, ReportePadronResumenEESS> {

    @Override
    public void initialize(CoFrecuenciaAtencion constraintAnnotation) {
    }

    @Override
    public boolean isValid(ReportePadronResumenEESS reportePadronResumenEESS, ConstraintValidatorContext constraintValidatorContext) {
		if(reportePadronResumenEESS.getTiEstablecimiento()!=null && reportePadronResumenEESS.getTiEstablecimiento() == 1){
            if (reportePadronResumenEESS.getCoFrecAtencion().equals(""))
            {
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }
}