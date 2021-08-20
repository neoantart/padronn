package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 10/01/14
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UbigeoCentroPobladoValidator.class)
public @interface UbigeoCentroPoblado {
    //String message() default "La información del titular y de la madre no coinciden";
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
