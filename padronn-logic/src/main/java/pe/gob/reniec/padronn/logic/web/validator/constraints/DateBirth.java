package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 04/07/13
 * Time: 06:18 PM
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DateBirthValidator.class)
@Documented
public @interface DateBirth {

	String format() default "dd/MM/yyyy";

	String message() default "La fecha de nacimiento no puede ser una fecha futura";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};


}
