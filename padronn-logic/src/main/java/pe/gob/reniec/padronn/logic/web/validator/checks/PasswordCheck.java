package pe.gob.reniec.padronn.logic.web.validator.checks;

import pe.gob.reniec.padronn.logic.web.validator.CambioCredencialPasswordValidator;
import pe.gob.reniec.padronn.logic.web.validator.PasswordCheckValidator;

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
@Target(value = {ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordCheckValidator.class)
@Documented
public @interface PasswordCheck {
    String field() default "field";

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
