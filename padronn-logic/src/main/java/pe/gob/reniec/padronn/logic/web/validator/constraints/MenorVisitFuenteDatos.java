package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MenorVisitFuenteDatosValidator.class)
public @interface MenorVisitFuenteDatos {

    String message() default "Seleccione una opción";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
