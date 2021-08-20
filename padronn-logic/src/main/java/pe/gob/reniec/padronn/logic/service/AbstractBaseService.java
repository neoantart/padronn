package pe.gob.reniec.padronn.logic.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import pe.gob.reniec.padronn.logic.web.validator.BeanValidator;
import pe.gob.reniec.padronn.logic.web.validator.ValidatorUtils;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Properties;

/**
 * Clase AbstractBaseService.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 04:07 PM
 */
public abstract class AbstractBaseService {

	protected static Logger log = Logger.getLogger(AbstractBaseService.class);

	@Autowired
	protected BeanValidator beanValidator;
	//protected static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	//public static boolean	updateDatabase = true;

	@Autowired
	protected ValidatorUtils validatorUtils;

	@Autowired
	protected PadronProperties padronProperties;
}
