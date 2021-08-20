package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TipoEstablecimientoEmptyValidator implements ConstraintValidator<TipoEstablecimientoEmpty, Integer> {

	@Override
	public void initialize(TipoEstablecimientoEmpty tipoEstablecimientoEmpty) {
	}

	@Override
	public boolean isValid(Integer tiEstablecimiento, ConstraintValidatorContext constraintValidatorContext) {
		/*if (tiEstablecimiento!=null){
			return true;
		}
		return false;*/
		return tiEstablecimiento!=null;
	}
}