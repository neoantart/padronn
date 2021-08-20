package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: jronal at gmail dot com
 * Date: 31/10/13
 * Time: 03:19 PM
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DatosPersonalesMadreValidator.class)
public @interface DatosPersonalesMadre {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
