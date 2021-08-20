package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 04/07/13
 * Time: 11:01 AM
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PadronApoderadoValidator.class)
public @interface PadronApoderado {

	//String message() default "La información del titular y de la madre no coinciden";
	String message() default "Se requiere al menos un apoderado (madre o titular)";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
