package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: jronal at gmail dot com
 * Date: 04/11/13
 * Time: 09:14 AM
 */
public class CuiValidator implements ConstraintValidator<Cui, String> {

    Cui cui;

    @Override
    public void initialize(Cui cui) {
        this.cui = cui;
    }

    /**.
     * CUI valido que inicien con 6 u 8
     * @param cui
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String cui, ConstraintValidatorContext context) {
//        return cui == null || cui.isEmpty() || cui.matches("/6\\d{7}|8\\d{7}");
        return cui == null || cui.isEmpty() || cui.matches("\\d{8}");
    }

}
