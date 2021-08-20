package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UbigeoIneiJurisdiccionValidator implements ConstraintValidator<UbigeoIneiJurisdiccion, PadronNominal> {

	@Override
	public void initialize(UbigeoIneiJurisdiccion UbigeoIneiJurisdiccion) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {
		String coUbigeoInei=padronNominal.getCoUbigeoInei();
		String coUbigeoPad=padronNominal.getCoUbigeoPad().trim();
		boolean equalsUbigeo = true;

		if (coUbigeoInei !=null && !coUbigeoInei.isEmpty()){
			coUbigeoInei = coUbigeoInei.substring(0, coUbigeoPad.length()<=6?coUbigeoPad.length():6);
			equalsUbigeo = coUbigeoInei.equals(coUbigeoPad);
		}
		//boolean existeVinculo=feVisita!=null&&!feVisita.isEmpty();
		//boolean existeCoMenorEnc= coMenorEncontrado!=null&&!coMenorEncontrado.isEmpty();
		return equalsUbigeo;
	}
}
