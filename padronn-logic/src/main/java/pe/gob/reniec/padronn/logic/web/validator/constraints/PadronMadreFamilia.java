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
 * Time: 10:43 AM
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PadronMadreFamiliaValidator.class)
public @interface PadronMadreFamilia {

	String message() default "La información de la madre es requerida";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
