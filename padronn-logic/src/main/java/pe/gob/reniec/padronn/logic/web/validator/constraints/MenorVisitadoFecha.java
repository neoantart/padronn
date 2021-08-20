package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MenorVisitadoFechaValidator.class)
public @interface MenorVisitadoFecha {

	String message() default "La fecha es requerida";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
