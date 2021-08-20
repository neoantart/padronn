package pe.gob.reniec.padronn.logic.web.validator.constraints;

import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.web.validator.checks.TiProSocial;
import pe.gob.reniec.padronn.logic.web.validator.checks.TiProSocialCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jfloresh on 22/09/2015.
 */

@Component
public class TiProSocialVal implements ConstraintValidator<TiProSocial, List<String>> {

    private static final int MAX_LENTH_STR_TI_PRO_SOCIAL = 50;

    @Override
    public void initialize(TiProSocial constraintAnnotation) {
       /* @Pattern(
                regexp = "[\\[\\] 0123456789,\\\"\\\\]*",
                message = "Contiene caracteres no válidos")
        @Length(max=MAX_LENTH_STR_TI_PRO_SOCIAL)*/
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if(value==null) return true;
        //return value.matches("[\\[\\] 0123456789,\\\"\\\\]*") && value.length() == MAX_LENTH_STR_TI_PRO_SOCIAL;

        String concat = "";
        for (String item : value) {
            concat += item;
        }
        return concat.length()<MAX_LENTH_STR_TI_PRO_SOCIAL;
    }

}
