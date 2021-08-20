package pe.gob.reniec.padronn.logic.web.validator.constraints;

import pe.gob.reniec.padronn.logic.web.validator.CoFrecuenciaAtencionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CoFrecuenciaAtencionValidator.class)
public @interface CoFrecuenciaAtencion {

    String message() default "La frecuencia de atencion es requerida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}