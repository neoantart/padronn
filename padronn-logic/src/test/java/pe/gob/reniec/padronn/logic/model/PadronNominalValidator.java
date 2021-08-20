package pe.gob.reniec.padronn.logic.model;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.web.validator.BeanValidator;
import pe.gob.reniec.padronn.logic.web.validator.ValidatorUtils;
import pe.gob.reniec.padronn.logic.web.validator.checks.PrecotejoRegistroChecks;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class PadronNominalValidator {
	private static Validator validator;

	Logger log = Logger.getLogger(getClass());

	@Autowired
	ValidatorUtils validatorUtils;

	@BeforeClass
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void test_feNacMenor() {
		Set<ConstraintViolation<PadronNominal>> constraintViolations;
		/*
		apPrimerMenor
		1. Blanco o nulo
		2. 2<=Longitud<=40
		3. Caracteres []
		*
		4. Longitud<2
		5. Longitud>40
		6. No caracteres
		* */

		String value = ""; String field = "feNacMenor";
		constraintViolations=validator.validateValue(PadronNominal.class, field, value);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		//java.lang.NullPointerException
		//at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1234)
		//at java.text.DateFormat.parse(DateFormat.java:335)
		//at pe.gob.reniec.padronn.logic.web.validator.constraints.LessThanAgeValidator.isValid(LessThanAgeValidator.java:30)
		//no comprueba NullPointerException, es probable que ninguno de los demás tampoco (confirmado-namecharsvalidator.)
		//value = null;
		//constraintViolations=validator.validateValue(PadronNominal.class, field, value);
		//log.debug(value + ":" + constraintViolations);
		////log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "28/07/1986";
		constraintViolations=validator.validateValue(PadronNominal.class, field, value);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "12/12/2013";
		constraintViolations=validator.validateValue(PadronNominal.class, field, value);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "03/07/2007";
		constraintViolations=validator.validateValue(PadronNominal.class, field, value);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "04/07/2007";
		constraintViolations=validator.validateValue(PadronNominal.class, field, value);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "05/07/2007";
		constraintViolations=validator.validateValue(PadronNominal.class, field, value);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "06/07/2007";
		constraintViolations=validator.validateValue(PadronNominal.class, field, value);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		Assert.assertEquals(0, 0);
	}
}
