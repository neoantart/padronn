package pe.gob.reniec.padronn.logic.web.validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.Validator;
import pe.gob.reniec.padronn.logic.model.Usuario;

/**
 * Clase AbstractBaseValidator.
 *
 * @author lmamani
 * @version 1.0.0
 * @since 16/05/13 03:53 PM
 */
public abstract class AbstractBaseValidator	{

  protected static Logger log = Logger.getLogger(AbstractBaseValidator.class);

  @Autowired
  Usuario usuario;

  @Autowired
  ValidatorUtils validatorUtils;

  @Autowired
  MessageSource messageSource;

}
