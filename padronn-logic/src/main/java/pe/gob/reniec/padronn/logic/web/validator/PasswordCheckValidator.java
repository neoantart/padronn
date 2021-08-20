package pe.gob.reniec.padronn.logic.web.validator;

import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.RuleResultDetail;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.CambioCredencial;
import pe.gob.reniec.padronn.logic.web.validator.checks.CambioCredencialPasswordCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.PasswordCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Locale;
/*import java.util.Locale;*/

/**
 * Clase CoDominioValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 24/06/13 07:46 PM
 */
@Component
@Scope("prototype")
public class PasswordCheckValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<PasswordCheck, String> {

    private PasswordCheck passwordCheckConstraint;

	@Override
	public void initialize(PasswordCheck constraintAnnotation) {
        this.passwordCheckConstraint = constraintAnnotation;
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password==null || password.isEmpty())
                return true; // debe existir otra constraint específica @NotEmpty

        PasswordData passwordData = new PasswordData();
        passwordData.setPassword(new Password(password));
        //passwordData.setUsername(this.usuario.getLogin()); //username => parte de sesión (no disponible aquí porque no hay sesión)
        RuleResult result = validatorUtils.passwordValidator.validate(passwordData);
        if(!result.isValid()) {
          ArrayList<String> errorCodes = new ArrayList<String>();
          ArrayList<Object[]> values = new ArrayList<Object[]>();
          // sólo tomamos un mensaje por tipo (passwordvalidator puede devolver más)
          for (RuleResultDetail detail : result.getDetails()) {
                String errorCode = detail.getErrorCode();
                /*if(!errorCodes.contains(errorCode) && !errorCode.equalsIgnoreCase("INSUFFICIENT_CHARACTERS")) {*/
                if(!errorCodes.contains(errorCode)) {
                  errorCodes.add(errorCode);
                  values.add(detail.getValues());
                }
          }

          for (int i = 0; i < errorCodes.size(); i++) {
            String errorCode = errorCodes.get(i);
            Object[] value = values.get(i);
            context.buildConstraintViolationWithTemplate(
                messageSource.getMessage(errorCode+".passwordCheck", value, Locale.getDefault()))
//                messageSource.getMessage(errorCode+".passwordCheck", value, Locale.US))
                .addConstraintViolation();
          }

          context.disableDefaultConstraintViolation();
          return false;
        }

        return true;
	}
}
