package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PadronVisitaMenorValidator implements ConstraintValidator<PadronVisitaMenor, PadronNominal> {

	@Override
	public void initialize(PadronVisitaMenor PadronVisitaMenor) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
		String feVisita=padronNominal.getFeVisita();
		String coMenorEncontrado=padronNominal.getCoMenorEncontrado();

		boolean existeVinculo=feVisita!=null&&!feVisita.isEmpty();
		boolean existeCoMenorEnc= coMenorEncontrado!=null&&!coMenorEncontrado.isEmpty();
		if(existeVinculo){
			return existeCoMenorEnc;
		}else{
			return !(existeCoMenorEnc);
		}
	}
}
