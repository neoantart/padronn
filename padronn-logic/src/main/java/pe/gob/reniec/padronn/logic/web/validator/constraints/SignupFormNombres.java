package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: aquispej
 * Date: 31/05/19
 * Time: 08:51 AM
 * To change this template use File | Settings | File Templates.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SignupFormNombresValidator.class)
public @interface SignupFormNombres {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
