package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: jronal at gmail dot com
 * Date: 25/10/13
 * Time: 03:54 PM
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DatosPersonalesMenorValidator.class)
public @interface DatosPersonalesMenor {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
