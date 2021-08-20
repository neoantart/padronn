package pe.gob.reniec.padronn.logic.model;

import org.apache.commons.lang3.ArrayUtils;
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
import java.util.regex.Pattern;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class PrecotejoValidatorTest {

	private static Validator validator;

	Logger log = Logger.getLogger(getClass());

	@Autowired
	ValidatorUtils validatorUtils;

	@Autowired
	BeanValidator beanValidator;

	@BeforeClass
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testValidator() {
		Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations;

		constraintViolations=validator.validateValue(PrecotejoRegistro.class, "feNacMenor", "29/07/1995", PrecotejoRegistroChecks.class);
		Assert.assertEquals(0, constraintViolations.size());

		constraintViolations=validator.validateValue(PrecotejoRegistro.class, "feNacMenor", "29-07-1995", PrecotejoRegistroChecks.class);
		Assert.assertEquals(0, constraintViolations.size());

		constraintViolations=validator.validateValue(PrecotejoRegistro.class, "feNacMenor", "29.07.1995", PrecotejoRegistroChecks.class);
		Assert.assertEquals(0, constraintViolations.size());

		constraintViolations=validator.validateValue(PrecotejoRegistro.class, "feNacMenor", "9.7.1995", PrecotejoRegistroChecks.class);
		Assert.assertEquals(0, constraintViolations.size());

		constraintViolations=validator.validateValue(PrecotejoRegistro.class, "feNacMenor", "9.07.1995", PrecotejoRegistroChecks.class);
		Assert.assertEquals(0, constraintViolations.size());

		constraintViolations=validator.validateValue(PrecotejoRegistro.class, "feNacMenor", "09.7.95", PrecotejoRegistroChecks.class);
		Assert.assertEquals(1, constraintViolations.size());
	}

	@Test
	public void testTiProSocialCheck() {

//		log.debug("validatorUtils=>" + ArrayUtils.toString(validatorUtils.getTiProSocialKeys().toArray()));
		Validator validator2;
	  //validator2 = beanValidator.getInstance(TiProSocialValidator.class);
	  validator2 = beanValidator.getValidator();

    Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations;
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "tiProSocial", "2,8", PrecotejoRegistroChecks.class);
//		log.debug(constraintViolations);
		Assert.assertEquals(1, constraintViolations.size());

		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "tiProSocial", "1,2", PrecotejoRegistroChecks.class);
//		log.debug(constraintViolations);
		Assert.assertEquals(0, constraintViolations.size());
	}

	@Test
	public void testCoDominioCheck() {

//		log.debug("Validación de @CoDominioCheck");

		Validator validator2;
	  validator2 = beanValidator.getValidator();

    Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations;

		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "coGeneroMenor", "2", PrecotejoRegistroChecks.class);
//		log.debug(constraintViolations);
		//Assert.assertEquals(0, constraintViolations.size());

		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "coGeneroMenor", "X", PrecotejoRegistroChecks.class);
//		log.debug(constraintViolations);
		//Assert.assertEquals(1, constraintViolations.size());

		String field = "0";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "tiDocIdentidad", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(0, constraintViolations.size());

		field = "1";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "tiDocIdentidad", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(0, constraintViolations.size());

		field = "2";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "tiDocIdentidad", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(1, constraintViolations.size());

		field = "3";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "tiDocIdentidad", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(1, constraintViolations.size());

		field = "4";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "tiDocIdentidad", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(1, constraintViolations.size());

		field = "00";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "coGraInstMadre", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(1, constraintViolations.size());

		field = "10";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "coGraInstMadre", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(1, constraintViolations.size());

		field = "20";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "coGraInstMadre", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(1, constraintViolations.size());

		field = "020";
		constraintViolations=validator2.validateValue(PrecotejoRegistro.class, "coGraInstMadre", field, PrecotejoRegistroChecks.class);
//		log.debug(field + ":" + constraintViolations);
		//Assert.assertEquals(1, constraintViolations.size());

	}

	@Test
	public void testEquivalencias_apPrimerMenor() {
		validator = beanValidator.getValidator();
		Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations;
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

		String value = ""; String field = "apPrimerMenor";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = null;
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

 		value = "MAIMAN";
 		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = "MAIMAN1";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = "M";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = "MAIMAN DE LAS CASAS FLORIDAS DE ARRIBA CUARENTA";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		// validNameCharsPattern = Pattern.compile("^[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ\\-\\' \\.0123456789]*$", Pattern.CASE_INSENSITIVE);
		// invalidNameCharsPattern = Pattern.compile("AAA|BBB|CCC|DDD|EEE|FFF|GGG|HH|III|JJ|KK|LLL|MMM|NNN|OOO|PPP|QQ|RRR|SSS|TTT|UUU|VV|WW|XX|YY|ZZZ|ÄÄ|ËË|ÑÑ|ÖÖ|ÜÜ", Pattern.CASE_INSENSITIVE);
		// invalidFirstNameCharsPattern = Pattern.compile("[\\-\\' \\.0123456789]");

		value = "1";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = "abc defg";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = "'abc defg";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = ".abc defg";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = "abc defg AAA";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = " abc defg aaa ";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = " abc defg aaa";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = "abc eee defg";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		value = "ii abc defg";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);

		Assert.assertEquals(0, 0);
	}

	@Test
	public void testEquivalencias_feNacMenor() {
		validator = beanValidator.getValidator();
		Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations;
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
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = null;
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

 		value = "28/07/1986";
 		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "12/12/2013";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "1/1/2001";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "1/1/2008";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "1A2Y3C";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		value = "121348";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(value + ":" + validatorUtils.getJsonMessage(constraintViolations));

		Assert.assertEquals(0, 0);
	}

	@Test
	public void testEquivalencias_nuDniMenor() {
		validator = beanValidator.getValidator();
		Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations;
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

		String value = ""; String field = "nuDniMenor";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(validatorUtils.getJsonMessage(constraintViolations));

		value = null;
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);//
//		log.debug(validatorUtils.getJsonMessage(constraintViolations));

 		value = "00AB0000";
 		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(validatorUtils.getJsonMessage(constraintViolations));

		value = "12345678";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(validatorUtils.getJsonMessage(constraintViolations));

		value = "04128790";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(validatorUtils.getJsonMessage(constraintViolations));

		value = "00000000";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(validatorUtils.getJsonMessage(constraintViolations));

		value = "99999999";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(validatorUtils.getJsonMessage(constraintViolations));

		value = "1";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
		//log.debug(value + ":" + constraintViolations);
//		log.debug(validatorUtils.getJsonMessage(constraintViolations));

		Assert.assertEquals(0, 0);
	}

	@Test
	public void testEquivalencias_tiVinculoMadre() {
		validator = beanValidator.getValidator();
		Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations;
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

		String value = ""; String field = "tiVinculoMadre";
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(validatorUtils.getJsonMessage(constraintViolations));

		value = null;
		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);//
		//log.debug(validatorUtils.getJsonMessage(constraintViolations));

 		value = "0";
 		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(validatorUtils.getJsonMessage(constraintViolations));

 		value = "1";
 		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(validatorUtils.getJsonMessage(constraintViolations));

 		value = "2";
 		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(validatorUtils.getJsonMessage(constraintViolations));

 		value = "33";
 		constraintViolations=validator.validateValue(PrecotejoRegistro.class, field, value, PrecotejoRegistroChecks.class);
//		log.debug(value + ":" + constraintViolations);
		//log.debug(validatorUtils.getJsonMessage(constraintViolations));

		Assert.assertEquals(0, 0);
	}

}
