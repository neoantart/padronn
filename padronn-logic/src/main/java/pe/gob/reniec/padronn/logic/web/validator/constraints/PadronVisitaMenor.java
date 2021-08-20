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
@Constraint(validatedBy = PadronVisitaMenorValidator.class)
public @interface PadronVisitaMenor {

	String message() default "La información del menor encontrado es requerida para una fecha de visita seleccionada";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
