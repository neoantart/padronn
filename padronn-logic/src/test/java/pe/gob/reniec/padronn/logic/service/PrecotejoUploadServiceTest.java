package pe.gob.reniec.padronn.logic.service;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.web.validator.checks.PrecotejoRegistroChecks;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration("classpath:/applicationContext-test.xml")//configuracion spring
//@ContextConfiguration("/applicationContext-test.xml")//configuracion spring
public class PrecotejoUploadServiceTest {

	@Autowired
	PrecotejoUploadService precotejoUploadService;

	private static Validator validator;

	@BeforeClass
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testGetResourceAsStreamAndValidation() {
		System.out.println("Consulta de Personas: ");
		List<PrecotejoRegistro> registroList;
		//registroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoISO.8859.1.csv")); //exportado desde oracle
		// TODO error leendo archivo ISO-8859-1 - configurar de acuerdo a codificación
		registroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoUTF8-1-1.csv")); //exportado desde oracle
		// TODO codificación UTF8 en Excel CSV
		//registroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoERRfechas.csv")); //guardado como csv desde Excel
		//registroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevosincom.csv")); //exportado desde Oracle con "
		//registroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevo-concolsnoexist.csv")); //
		//registroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevo-colsMayusc.csv")); //
		// ojo el csv lee tal como esta, excel lo interpreta, caso coInstEducativa = 000313, en excel=313
		int i = 0;
		for(PrecotejoRegistro registro:registroList) {
			System.out.println("#" +String.format("%02d", ++i)+":"+ registro);
			Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations = validator.validate(registro, PrecotejoRegistroChecks.class);
			System.out.println(constraintViolations);
			//System.out.println("#  >" +constraintViolations.iterator().next().getMessage());
		}
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testReadFromExcelStream() {
		System.out.println("ReadFromExcelStream: ");
		List<PrecotejoRegistro> registroList;
		registroList = precotejoUploadService.readFromExcelStream(getClass().getResourceAsStream("/xls/Formato1.xls"));
		int i = 0;
		for(PrecotejoRegistro registro:registroList) {
			System.out.println("#" +String.format("%02d", ++i)+":"+ registro);
			//Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations = validator.validate(registro, PrecotejoRegistroChecks.class);
			//System.out.println(constraintViolations);
			//System.out.println("#  >" +constraintViolations.iterator().next().getMessage());
		}
		Assert.assertEquals(1, 1);
	}
}
