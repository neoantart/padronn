package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MenorVisitadoEncontValidator implements ConstraintValidator<MenorVisitadoEncont, PadronNominal> {

	@Override
	public void initialize(MenorVisitadoEncont MenorVisitadoEncont) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
		boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;
		String coMenorEncontrado=padronNominal.getCoMenorEncontrado();

		boolean existeCoMenorEn=coMenorEncontrado!=null&&!coMenorEncontrado.isEmpty();
		if(isMenorVisitado){
			return existeCoMenorEn;
		}else{
			return true;
		}
	}
}
