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
 * Date: 12/08/13
 * Time: 05:51 PM
 * To change this template use File | Settings | File Templates.
 */

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SignupDniValidator.class)
public @interface SignupDni {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
