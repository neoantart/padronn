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
 * Created by jfloresh on 14/09/2015.
 */

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CoPadronNominalValidator.class)
@Documented
public @interface CoPadronNominal {
    String message() default "Formato de Codigo de Padron Nominal no válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
