package pe.gob.reniec.padronn.logic.web.validator.checks;

import pe.gob.reniec.padronn.logic.web.validator.CoDominioValidator;
import pe.gob.reniec.padronn.logic.web.validator.EmptyOrLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Clase EmptyOrLengthCheck.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 02/07/13 07:22 PM
 */
@Target(value = {java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmptyOrLengthValidator.class)
@Documented
public @interface EmptyOrLengthCheck {
	String message() default "{pe.gob.reniec.padronn.logic.web.validator.checks.EmptyOrLengthCheck}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int min();

	int max();
}
