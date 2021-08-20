package pe.gob.reniec.padronn.logic.web.validator.checks;

import pe.gob.reniec.padronn.logic.web.validator.EmptyOrLengthValidator;
import pe.gob.reniec.padronn.logic.web.validator.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Clase NameCheck.
 * A partir de NameCharacters
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 05/07/13 02:58 PM
 */
@Target(value = {java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NameValidator.class)
@Documented
public @interface NameCheck {
	String message() default "{pe.gob.reniec.padronn.logic.web.validator.checks.NameCheck}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
