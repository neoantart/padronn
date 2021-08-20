package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronResumenEESS;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronResumenEdadEESS;
import pe.gob.reniec.padronn.logic.web.validator.constraints.CoFrecuenciaAtencion;
import pe.gob.reniec.padronn.logic.web.validator.constraints.FrecuenciaAtencion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class FrecuenciaAtencionValidator implements ConstraintValidator<FrecuenciaAtencion, ReportePadronResumenEdadEESS> {

    @Override
    public void initialize(FrecuenciaAtencion constraintAnnotation) {
    }

    @Override
    public boolean isValid(ReportePadronResumenEdadEESS reportePadronResumenEdadEESS, ConstraintValidatorContext constraintValidatorContext) {
        if(reportePadronResumenEdadEESS.getTiEstablecimiento()!=null && reportePadronResumenEdadEESS.getTiEstablecimiento() == 1){
            if (reportePadronResumenEdadEESS.getCoFrecAtencion().equals(""))
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
