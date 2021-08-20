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
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 03/07/13
 * Time: 09:51 AM
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = LessThanAgeValidator.class)
@Documented
public @interface LessThanAge {

	int years() default 0;
	String dateFormat() default "dd/MM/yyyy";

	String message() default "Edad mayor o igual a la indicada";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
