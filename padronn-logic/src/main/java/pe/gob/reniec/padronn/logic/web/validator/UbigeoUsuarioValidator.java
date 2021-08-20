package pe.gob.reniec.padronn.logic.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.Usuario;
import pe.gob.reniec.padronn.logic.web.validator.checks.TiProSocialCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.UbigeoUsuarioCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.StringTokenizer;

/**
 * Clase UbigeoUsuarioValidator.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 25/06/13 06:11 PM
 */
@Component
@Scope("session") //el thread que lo está llamadno no está en sesión!!
public class UbigeoUsuarioValidator
		extends AbstractBaseValidator
		implements ConstraintValidator<UbigeoUsuarioCheck, String> {

	@Autowired
	Usuario usuario;

	@Override
	public void initialize(UbigeoUsuarioCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || value.length()<2) {
			return true;
		}
		//Usuario usuario = new AnnotationConfigApplicationContext(getClass()).getBeanNamesForType(Usuario);
		if(usuario.getCoUbigeoInei()==null || usuario.getCoUbigeoInei().length()<2) {
			// todo este caso es el caso de root
			return true;
		}
		if(usuario.getCoUbigeoInei().startsWith(value.substring(0, 2))) {
			return true;
		}
		return false;
	}

}
