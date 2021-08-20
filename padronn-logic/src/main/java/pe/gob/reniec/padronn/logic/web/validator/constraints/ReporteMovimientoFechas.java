package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by JFLORESH on 20/03/2014.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ReporteMovimientoFechasValidator.class)
public @interface ReporteMovimientoFechas {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}