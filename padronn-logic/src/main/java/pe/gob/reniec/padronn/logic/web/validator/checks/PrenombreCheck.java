package pe.gob.reniec.padronn.logic.web.validator.checks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.service.DominioService;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Created by jfloresh on 10/05/2016.
 */
@Target(value = {java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrenombreValidator.class)
@Documented
public @interface PrenombreCheck {
    String message() default "{pe.gob.reniec.padronn.logic.web.validator.checks.PrenombreCheck}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Component
class PrenombreValidator implements ConstraintValidator<PrenombreCheck, String> {

    @Autowired
    DominioService dominioService;

    @Override
    public void initialize(PrenombreCheck constraintAnnotation) {

    }

    @Override
    public boolean isValid(String prenombreMenor, ConstraintValidatorContext context) {
        List<String> sinPrenombreMenor = dominioService.getDeTipoSinNombre();
        return sinPrenombreMenor != null && !dominioService.getDeTipoSinNombre().contains(prenombreMenor);
    }

}