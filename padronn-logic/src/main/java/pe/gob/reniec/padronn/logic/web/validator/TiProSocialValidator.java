package pe.gob.reniec.padronn.logic.web.validator;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import pe.gob.reniec.padronn.logic.model.Usuario;
import pe.gob.reniec.padronn.logic.service.KeysService;
import pe.gob.reniec.padronn.logic.service.impl.KeysServiceImpl;
import pe.gob.reniec.padronn.logic.web.validator.checks.TiProSocialCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.StringTokenizer;

/**
 * Clase TiProSocialValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 21/06/13 03:40 PM
 */
@Component
public class TiProSocialValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<TiProSocialCheck, String> {

	@Override
	public void initialize(TiProSocialCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || value.isEmpty()) return true;
		for (StringTokenizer tokenizer = new StringTokenizer(value, ","); tokenizer.hasMoreTokens(); ) {
			String token = tokenizer.nextToken();
			if(!validatorUtils.getTiProSocialKeys().contains(token))
				return false;
		}
		return true;
	}
}
