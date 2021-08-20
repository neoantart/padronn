package pe.gob.reniec.padronn.logic.web.validator.checks;

import pe.gob.reniec.padronn.logic.web.validator.TiProSocialValidator;
import pe.gob.reniec.padronn.logic.web.validator.UbigeoUsuarioValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Clase UbigeoUsuarioCheck.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 25/06/13 06:09 PM
 */
@Target(value = {java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UbigeoUsuarioValidator.class)
@Documented
public @interface UbigeoUsuarioCheck {
	String message() default "{pe.gob.reniec.padronn.logic.web.validator.checks.UbigeoUsuarioCheck}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
