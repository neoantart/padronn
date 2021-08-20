package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import pe.gob.reniec.padronn.logic.model.PadronNominal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
//import java.util.Locale;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 04/07/13
 * Time: 10:45 AM
 */
public class PadronMadreFamiliaValidator implements ConstraintValidator<PadronMadreFamilia, PadronNominal> {

	@Autowired
	MessageSource messageSource;

	@Override
	public void initialize(PadronMadreFamilia padronMadreFamilia) {
	}

	@Override
	public boolean isValid(PadronNominal padronNominal, ConstraintValidatorContext constraintValidatorContext) {

		String dni = padronNominal.getCoDniMadre();
		boolean existeDni = dni != null && !dni.isEmpty();

		String paterno = padronNominal.getApPrimerMadre();
		boolean existePaterno = paterno != null && !paterno.isEmpty();

		String materno = padronNominal.getApSegundoMadre();
//		boolean existeMaterno = (materno != null && !materno.isEmpty());
        boolean existeMaterno = true;// se permiten registrar vacio tambien

		String nombres = padronNominal.getPrenomMadre();
		boolean existeNombres = nombres != null && !nombres.isEmpty();

		String gradoInstruccion = padronNominal.getCoGraInstMadre();
		boolean existeGradoInstruccion = gradoInstruccion != null && !gradoInstruccion.isEmpty();

		String codigoLenguaje = padronNominal.getCoLenMadre();
		boolean existeCodigoLenguaje = codigoLenguaje != null && !codigoLenguaje.isEmpty();

		boolean isMenorVisitado=padronNominal.getInMenorVisitado().equalsIgnoreCase("1")&&padronNominal.getInMenorVisitado()!=null;

		String coMenorEncontrado=padronNominal.getCoMenorEncontrado();
		boolean isNotMenorEncontrado=coMenorEncontrado.equalsIgnoreCase("0")&&padronNominal.getCoMenorEncontrado()!=null&&!padronNominal.getCoMenorEncontrado().isEmpty();

		if (!(isMenorVisitado && isNotMenorEncontrado)){
			if(existeDni || existePaterno || existeMaterno || existeNombres || existeGradoInstruccion || existeCodigoLenguaje) {
				// caso 1: madre tiene dni; y caso 2: madre no tiene dni --> dni no es requerido
				//Locale.getDefault()
				if (!existePaterno) {

					constraintValidatorContext
							.buildConstraintViolationWithTemplate(
									"El primer apellido de la madre es necesario")
							.addNode("apPrimerMadre")
							.addConstraintViolation();
				}
				if (!existeMaterno) {
					constraintValidatorContext
							.buildConstraintViolationWithTemplate(
									"El segundo apellido de la madre es necesario")
							.addNode("apSegundoMadre")
							.addConstraintViolation();
				}
				if (!existeNombres) {
					constraintValidatorContext
							.buildConstraintViolationWithTemplate(
									"El nombre de la madre es necesario")
							.addNode("prenomMadre")
							.addConstraintViolation();
				}
				if (!existeGradoInstruccion) {
					constraintValidatorContext
							.buildConstraintViolationWithTemplate(
									"El grado de instruccion de la madre es necesario")
							.addNode("coGraInstMadre")
							.addConstraintViolation();
				}
				if (!existeCodigoLenguaje) {
					constraintValidatorContext
							.buildConstraintViolationWithTemplate(
									"El lenguage de la madre es necesario")
							.addNode("coLenMadre")
							.addConstraintViolation();
				}
				return existePaterno && existeMaterno && existeNombres && existeGradoInstruccion && existeCodigoLenguaje;

			} else {
				//caso 3: sin madre
				return true;
			}
		}
		return true;

		/*
		if (padronNominal.getCoDniMadre() != null && !padronNominal.getCoDniMadre().isEmpty()) {


			String dni = padronNominal.getCoDniMadre();
			String paterno = padronNominal.getApPrimerMadre();
			String materno = padronNominal.getApSegundoMadre();
			String nombres = padronNominal.getPrenomMadre();
			String gradoInstruccion = padronNominal.getCoGraInstMadre();
			String codigoLenguaje = padronNominal.getCoLenMadre();

			boolean existeDni = dni != null && !dni.isEmpty();
			//boolean existeNombre=paterno!=null&&!paterno.isEmpty()&&materno!=null&&!materno.isEmpty()&&nombres!=null&&!nombres.isEmpty();
			boolean existePaterno = paterno != null && !paterno.isEmpty();
			boolean existeMaterno = materno != null && !materno.isEmpty();
			boolean existeNombre = nombres != null && !nombres.isEmpty();
			boolean existeGradoInstruccion = gradoInstruccion != null && !gradoInstruccion.isEmpty();
			boolean existeCodigoLenguaje = codigoLenguaje != null && !codigoLenguaje.isEmpty();

			// solución: construir constraintviolation ver: http://goo.gl/oCnF7 http://goo.gl/DZ31M (más factible)
			// otra solución podría ser usar: http://goo.gl/JAjfC
			if (!existePaterno) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								messageSource.getMessage("NotEmpty.padronNominal.apPrimerMadre", null, null))
						.addNode("apPrimerMadre")
						.addConstraintViolation();
			}
			if (!existeMaterno) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								messageSource.getMessage("NotEmpty.padronNominal.apSegundoMadre", null, null))
						.addNode("apSegundoMadre")
						.addConstraintViolation();
			}
			if (!existeNombre) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								messageSource.getMessage("NotEmpty.padronNominal.prenomMadre", null, null))
						.addNode("prenomMadre")
						.addConstraintViolation();
			}
			if (!existeGradoInstruccion) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								messageSource.getMessage("NotEmpty.padronNominal.coGraInstMadre", null, null))
						.addNode("coGraInstMadre")
						.addConstraintViolation();
			}
			if (!existeCodigoLenguaje) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								messageSource.getMessage("NotEmpty.padronNominal.coLenMadre", null, null))
						.addNode("coLenMadre")
						.addConstraintViolation();
			}
			return existeDni && existePaterno && existeMaterno && existeNombre && existeGradoInstruccion && existeCodigoLenguaje;

		} else if (padronNominal.getPrenomMadre() != null && !padronNominal.getPrenomMadre().isEmpty()) {

			// caso 2: madre no tiene dni
			//String paterno=padronNominal.getApPrimerMadre();
			//String materno=padronNominal.getApSegundoMadre();
			String nombres = padronNominal.getPrenomMadre();
			String gradoInstruccion = padronNominal.getCoGraInstMadre();
			String codigoLenguaje = padronNominal.getCoLenMadre();

			//boolean existeNombre=paterno!=null&&!paterno.isEmpty()&&materno!=null&&!materno.isEmpty()&&nombres!=null&&!nombres.isEmpty();
			boolean existeNombre = nombres != null && !nombres.isEmpty();
			boolean existeGradoInstruccion = gradoInstruccion != null && !gradoInstruccion.isEmpty();
			boolean existeCodigoLenguaje = codigoLenguaje != null && !codigoLenguaje.isEmpty();

			// solución: construir constraintviolation ver: http://goo.gl/oCnF7 http://goo.gl/DZ31M (más factible)
			// otra solución podría ser usar: http://goo.gl/JAjfC
			if (!existeGradoInstruccion) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								messageSource.getMessage("NotEmpty.padronNominal.coGraInstMadre", null, null))
						.addNode("coGraInstMadre")
						.addConstraintViolation();
			}
			if (!existeCodigoLenguaje) {
				constraintValidatorContext
						.buildConstraintViolationWithTemplate(
								messageSource.getMessage("NotEmpty.padronNominal.coLenMadre", null, null))
						.addNode("coLenMadre")
						.addConstraintViolation();
			}
			return existeNombre && existeGradoInstruccion && existeCodigoLenguaje;

		} else {
			// caso 3: no hay madre (borramos t o d o)
			padronNominal.setCoDniMadre("");
			padronNominal.setApPrimerMadre("");
			padronNominal.setApSegundoMadre("");
			padronNominal.setPrenomMadre("");
			padronNominal.setCoGraInstMadre("");
			padronNominal.setCoLenMadre("");
			return true;
		}
		*/
	}
}
