package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MenorVisitadoFechaValidator implements ConstraintValidator<MenorVisitadoFecha, PadronNominal> {

	@Override
	public void initialize(MenorVisitadoFecha MenorVisitadoFecha) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
		boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;
		String feVisita=padronNominal.getFeVisita();
		String feUltimaTomaDatos=padronNominal.getFeUltimaTomaDatos();

		boolean existeFeVisita=feVisita!=null&&!feVisita.isEmpty();
		boolean existeFeUltimaTomaDatos=feUltimaTomaDatos!=null&&!feUltimaTomaDatos.isEmpty();

		if(isMenorVisitado){
			return existeFeVisita;
		}else{
			return existeFeUltimaTomaDatos;
		}
	}
}
