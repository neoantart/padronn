package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MenorVisitadoValidator implements ConstraintValidator<MenorVisitado, PadronNominal> {

	@Override
	public void initialize(MenorVisitado MenorVisitado) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {

		boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;

		String feVisita=padronNominal.getFeVisita();
		boolean existeFeVisita=feVisita!=null&&!feVisita.isEmpty();

		String feUltimaTomaDatos=padronNominal.getFeUltimaTomaDatos();
		boolean existeFeUltimaTomaDatos=feUltimaTomaDatos!=null&&!feUltimaTomaDatos.isEmpty();

		String coMenorEncontrado=padronNominal.getCoMenorEncontrado();
		boolean existeCoMenorEncontrado=coMenorEncontrado!=null&&!coMenorEncontrado.isEmpty();

		String coFuenteDatos=padronNominal.getCoFuenteDatos();
		boolean existeCoFuenteDatos=coFuenteDatos!=null&&!coFuenteDatos.isEmpty();

		if(isMenorVisitado){
			if (!existeFeVisita) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								"La fecha es requerida")
						.addNode("feVisita")
						.addConstraintViolation();
			}
			if (!existeCoMenorEncontrado) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								"Seleccione una opción")
						.addNode("coMenorEncontrado")
						.addConstraintViolation();
			}
			return existeFeVisita && existeCoMenorEncontrado;
		}else{
			if (!existeCoFuenteDatos) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								"Seleccione una opción")
						.addNode("coFuenteDatos")
						.addConstraintViolation();
			}
			if (!existeFeUltimaTomaDatos) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								"La fecha es requerida")
						.addNode("feUltimaTomaDatos")
						.addConstraintViolation();
			}
			return existeCoFuenteDatos && existeFeUltimaTomaDatos;
		}
	}
}
