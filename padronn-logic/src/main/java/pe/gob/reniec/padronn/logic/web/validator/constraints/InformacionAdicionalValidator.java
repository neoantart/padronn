package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InformacionAdicionalValidator implements ConstraintValidator<InformacionAdicionalVal, PadronNominal> {

	@Override
	public void initialize(InformacionAdicionalVal InformacionAdicionalVal) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {

		boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;

		String coMenorEncontrado=padronNominal.getCoMenorEncontrado();
		boolean isNotMenorEncontrado=coMenorEncontrado.equalsIgnoreCase("0")&&padronNominal.getCoMenorEncontrado()!=null&&!padronNominal.getCoMenorEncontrado().isEmpty();

		String coEstSalud=padronNominal.getCoEstSalud();
		boolean existeCoEstSalud=coEstSalud!=null&&!coEstSalud.isEmpty();

		if(isMenorVisitado && isNotMenorEncontrado){
			return true;
		}else{
			if (!existeCoEstSalud) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								"El establecimiento de salud es requerido")
						.addNode("coEstSalud")
						.addConstraintViolation();
			}
			return existeCoEstSalud;
		}
	}
}
