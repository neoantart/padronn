package pe.gob.reniec.padronn.logic.web.validator.checks;

import pe.gob.reniec.padronn.logic.web.validator.TiProSocialValidator;
import pe.gob.reniec.padronn.logic.web.validator.constraints.TiProSocialVal;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by jfloresh on 22/09/2015.
 */

@Target(value = {java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TiProSocialVal.class)
@Documented
public @interface TiProSocial {
    String message() default "Tipo de programas sociales invalidos";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
