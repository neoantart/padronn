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
 * Date: 04/07/13
 * Time: 06:18 PM
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FileSizeValidator.class)
@Documented
public @interface FileSize {

	long max() default 0;

	long min() default 0;

	String message() default "El archivo no tiene el tamaño correcto";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};


}
