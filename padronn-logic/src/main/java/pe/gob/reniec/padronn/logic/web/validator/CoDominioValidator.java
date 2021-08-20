package pe.gob.reniec.padronn.logic.web.validator;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestScope;
import pe.gob.reniec.padronn.logic.web.validator.checks.CoDominio;
import pe.gob.reniec.padronn.logic.web.validator.checks.CoDominioCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.StringTokenizer;

/**
 * Clase CoDominioValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 24/06/13 07:46 PM
 */
@Component
@Scope("prototype") //http://static.springsource.org/spring/docs/3.0.0.M3/reference/html/ch04s04.html
public class CoDominioValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<CoDominioCheck, String> {

	private CoDominio coDominio;

	@Override
	public void initialize(CoDominioCheck constraintAnnotation) {
		this.coDominio = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || value.isEmpty())
			return true;

		switch (coDominio) {
			case coGeneroMenor:
				return validatorUtils.coGeneroMenorKeys.contains(value);

			case coGraInstMadre:
				return validatorUtils.coGraInstMadreKeys.contains(value);

			case coLenMadre:
				return validatorUtils.coLenMadreKeys.contains(value);

			case tiDocIdentidad:
				return validatorUtils.tiDocIdentidadKeys.contains(value);

			case tiSeguroMenor:
				return validatorUtils.tiSeguroMenorKeys.contains(value);

			case tiVinculoJefe:
				return validatorUtils.tiVinculoJefeKeys.contains(value);

			case tiVinculoMadre:
				return validatorUtils.tiVinculoMadreKeys.contains(value);

			default:
				throw new RuntimeException("CoDominio no válido");
		}
	}
}
