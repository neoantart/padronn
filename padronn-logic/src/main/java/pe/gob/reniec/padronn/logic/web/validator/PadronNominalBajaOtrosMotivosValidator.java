package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.PadronNominalBaja;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;
import pe.gob.reniec.padronn.logic.web.validator.checks.PadronNominalBajaOtrosMotivos;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
/*import java.util.Locale;*/

/**
 * Clase CoDominioValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 24/06/13 07:46 PM
 */
@Component
public class PadronNominalBajaOtrosMotivosValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<PadronNominalBajaOtrosMotivos, PadronNominalBaja> {

	private static final String CODIGO_OTROS_MOTIVOS = "3";
	private static final String CODIGO_REGISTRO_DUPLICADO = "8";
	private static final String CODIGO_REGISTRO_FALLECIMIENTO = "1";

	@Autowired
	UsuarioExternoService usuarioExternoService;

	@Override
	public void initialize(PadronNominalBajaOtrosMotivos constraintAnnotation) {
	}

	@Override
	public boolean isValid(PadronNominalBaja padronNominalBaja, ConstraintValidatorContext context) {
		if(padronNominalBaja == null)
			return true;

		// TODO "3" obtener de PadronProperties, "mensaje" obtener de MessageSource
		if(CODIGO_OTROS_MOTIVOS.equals(padronNominalBaja.getCoMotivoBaja()) || CODIGO_REGISTRO_DUPLICADO.equals(padronNominalBaja.getCoMotivoBaja())
				|| CODIGO_REGISTRO_FALLECIMIENTO.equals(padronNominalBaja.getCoMotivoBaja())) {
			if(padronNominalBaja.getDeObservacion() != null && padronNominalBaja.getDeObservacion().trim().isEmpty()) {
				context.
						buildConstraintViolationWithTemplate("no puede estar vacío")
						//buildConstraintViolationWithTemplate(messageSource.getMessage("NotEquals.padronNominal.dePasswordNuevoRepetido", null, Locale.getDefault()))
						.addNode("deObservacion")
						.addConstraintViolation();
				context.disableDefaultConstraintViolation();
				return false;
			}
		}
		return true;
	}
}