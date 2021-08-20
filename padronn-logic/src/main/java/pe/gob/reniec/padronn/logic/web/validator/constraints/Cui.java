package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: jronal at gmail dot com
 * Date: 04/11/13
 * Time: 09:14 AM
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CuiValidator.class)
@Documented
public @interface Cui {
    String message() default "Formato de CUI no válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
