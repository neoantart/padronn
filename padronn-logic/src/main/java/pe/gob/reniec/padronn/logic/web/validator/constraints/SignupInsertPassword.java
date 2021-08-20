package pe.gob.reniec.padronn.logic.web.validator.constraints;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 13/08/13
 * Time: 05:55 PM
 * To change this template use File | Settings | File Templates.
 */

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
 * Time: 06:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SignupInsertPasswordValidator.class)
public @interface SignupInsertPassword {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
