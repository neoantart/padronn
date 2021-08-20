package pe.gob.reniec.padronn.logic.web.validator.checks;

import pe.gob.reniec.padronn.logic.web.validator.CambioCredencialPasswordValidator;
import pe.gob.reniec.padronn.logic.web.validator.PadronNominalBajaOtrosMotivosValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Clase CoDominioCheck.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 24/06/13 07:42 PM
 */
@Target(value = {ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PadronNominalBajaOtrosMotivosValidator.class)
@Documented
public @interface PadronNominalBajaOtrosMotivos {
	String message() default "{pe.gob.reniec.padronn.logic.web.validator.checks.PadronNominalBajaOtrosMotivos}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
