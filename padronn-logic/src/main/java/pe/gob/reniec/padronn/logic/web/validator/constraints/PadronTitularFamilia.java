package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 03/07/13
 * Time: 04:33 PM
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PadronTitularFamiliaValidator.class)
public @interface PadronTitularFamilia {

	String message() default "La información del jefe de familia o titular no esta completa";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
