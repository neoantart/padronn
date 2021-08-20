package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 04/02/14
 * Time: 09:59 AM
 * To change this template use File | Settings | File Templates.
 */

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ReportePadronSinDocConstraintsValidator.class)
public @interface ReporteUsuarioMinsaConstraints {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
