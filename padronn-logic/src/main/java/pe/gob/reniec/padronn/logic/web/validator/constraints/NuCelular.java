package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = NuCelularValidator.class)
public @interface NuCelular {
    String message() default "Numero de celular del Menor es requerido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
