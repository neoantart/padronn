package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.web.validator.CoFrecuenciaAtencionValidator;
import pe.gob.reniec.padronn.logic.web.validator.FrecuenciaAtencionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FrecuenciaAtencionValidator.class)
public @interface FrecuenciaAtencion {

    String message() default "La frecuencia de atencion es requerida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}