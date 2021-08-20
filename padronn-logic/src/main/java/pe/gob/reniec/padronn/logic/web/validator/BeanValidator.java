package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.*;
import java.util.Map;
import java.util.Set;

/**
 * Clase BeanValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 24/06/13 05:19 PM
 */
@Component
public class BeanValidator
		implements org.springframework.validation.Validator, InitializingBean, ApplicationContextAware, ConstraintValidatorFactory {

	private Validator validator;
	private ApplicationContext applicationContext;

	// ver http://blog.trifork.com/2009/08/04/bean-validation-integrating-jsr-303-with-spring/
	// http://stackoverflow.com/questions/7080684/spring-validator-having-both-annotation-and-validator-implementation
	// thanks!!!

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Validator getValidator() {
		return validator;
	}

	public void afterPropertiesSet() throws Exception {
		ValidatorFactory validatorFactory =
				Validation.byDefaultProvider().configure().constraintValidatorFactory(this).buildValidatorFactory();
		validator = validatorFactory.usingContext().getValidator();
	}

	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
		Map beansByNames = applicationContext.getBeansOfType(key);
		if (beansByNames.isEmpty()) {
			try {
				return key.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Could not instantiate constraint validator class '" + key.getName() + "'", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Could not instantiate constraint validator class '" + key.getName() + "'", e);
			}
		}

		if (beansByNames.size() > 1) {
			throw new RuntimeException("Only one bean of type '" + key.getName() + "' is allowed in the application context");
		}
		return (T) beansByNames.values().iterator().next();
	}

	public boolean supports(Class clazz) {
		return true;
	}

	public void validate(Object target, Errors errors) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			String propertyPath = constraintViolation.getPropertyPath().toString();
			String message = constraintViolation.getMessage();
			errors.rejectValue(propertyPath, "", message);
		}
	}

}
