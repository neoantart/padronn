package pe.gob.reniec.padronn.logic.web.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DniValidator implements ConstraintValidator<Dni, String>{

	Dni dni;

	@Override
	public void initialize(Dni dni) {
		this.dni=dni;
	}

	@Override
	public boolean isValid(String dni, ConstraintValidatorContext constraintValidatorContext) {
		return dni==null||dni.isEmpty()||dni.matches("\\d{8}")||dni.matches("\\d{2}");
	}
}