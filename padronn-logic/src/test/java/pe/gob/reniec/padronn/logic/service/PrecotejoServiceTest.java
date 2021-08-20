package pe.gob.reniec.padronn.logic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.dao.PrecotejoDao;
import pe.gob.reniec.padronn.logic.model.Precotejo;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistroDetalle;
import pe.gob.reniec.padronn.logic.service.impl.PrecotejoServiceImpl;
import pe.gob.reniec.padronn.logic.web.validator.ValidatorUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class PrecotejoServiceTest {

	@Autowired
	PrecotejoUploadService precotejoUploadService;
	@Autowired
	PrecotejoService precotejoService;
	@Autowired
	ValidatorUtils validatorUtils;

	private static Validator validator;
    Gson gson=new GsonBuilder().setPrettyPrinting().create();

	@BeforeClass
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testInsertList() {
		System.out.println("Validación de registros de padrón de precotejo CON TRANSACCIONALIDAD: ");
		List<PrecotejoRegistro> precotejoRegistroList;
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1trans.csv"));

		Precotejo precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		precotejo.setEsEnvio("1");
		precotejo.setUsCreaAudi("TEST");
		precotejo.setUsModiAudi("TEST");
		precotejo.setNoArchivoOriginal("TEST");
		//precotejoService.updateDatabase = false;
		precotejoService.insert(precotejo, precotejoRegistroList, false);

		Assert.assertEquals(1, 1);
	}

	/*
	@Test
	public void testGetPadron() {
		System.out.println("Validación de registros de padrón de precotejo: ");
		List<PrecotejoRegistro> precotejoRegistroList;
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1.csv"));

		Precotejo precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		precotejo.setEsEnvio("1");
		precotejo.setUsCreaAudi("43873223");
		precotejo.setUsModiAudi("43873223");
		//precotejo.setNoArchivoOriginal("padron1.csv");
		///precotejoService.updateDatabase = false;
		precotejoService.insert(precotejo, precotejoRegistroList);

		Assert.assertEquals(1, 1);
	}
	*/

	@Test
	public void testGetPadronOk() {
		System.out.println("Validación de registros de padrón de precotejo: ");
		List<PrecotejoRegistro> precotejoRegistroList;
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoUTF8-1-1.csv"));
		/*int i = 0;
		for(PrecotejoRegistro registro:precotejoRegistroList) {
			System.out.println("#" +String.format("%02d", ++i)+":"+ registro);
			Set<ConstraintViolation<PrecotejoRegistro>> constraintViolations = validator.validate(registro, PrecotejoRegistroChecks.class);
			System.out.println(constraintViolations);
			//System.out.println("#  >" +constraintViolations.iterator().next().getMessage());
		} */
		//System.out.println(precotejoRegistroList);

		Precotejo precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		//precotejo.setEsEnvio("10");
		precotejo.setUsCreaAudi("JUTEST");
		precotejo.setUsModiAudi("JUTEST");
		precotejo.setNoArchivoOriginal("JUTEST");
		//precotejo.setNoArchivoOriginal("padron1ok.csv");
		//precotejoService.updateDatabase = false;
		precotejoService.insert(precotejo, precotejoRegistroList, false);
		precotejoService.waitTask(); //sino el junit lo para en prima!

		Assert.assertEquals(1, 1);
	}

	@Test
	public void testGetPadronThreads() {
		System.out.println("Validación de registros de padrón de precotejo: ");
		// archivo 1
		List<PrecotejoRegistro> precotejoRegistroList;
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoUTF8-1-1.P1.csv"));
		Precotejo precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		precotejo.setUsCreaAudi("JUTEST");
		precotejo.setUsModiAudi("JUTEST");
		precotejo.setNoArchivoOriginal("JUTEST1");
		precotejoService.insert(precotejo, precotejoRegistroList, false);
		// archivo 2
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoUTF8-1-1.P2.csv"));
		precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		precotejo.setUsCreaAudi("JUTEST");
		precotejo.setUsModiAudi("JUTEST");
		precotejo.setNoArchivoOriginal("JUTEST2");
		precotejoService.insert(precotejo, precotejoRegistroList, false);
		// archivo 3
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoUTF8-1-1.P3.csv"));
		precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		precotejo.setUsCreaAudi("JUTEST");
		precotejo.setUsModiAudi("JUTEST");
		precotejo.setNoArchivoOriginal("JUTEST3");
		precotejoService.insert(precotejo, precotejoRegistroList, false);
		// archivo 4
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoUTF8-1-1.P4.csv"));
		precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		precotejo.setUsCreaAudi("JUTEST");
		precotejo.setUsModiAudi("JUTEST");
		precotejo.setNoArchivoOriginal("JUTEST4");
		precotejoService.insert(precotejo, precotejoRegistroList, false);
		// archivo 5
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoUTF8-1-1.P5.csv"));
		precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		precotejo.setUsCreaAudi("JUTEST");
		precotejo.setUsModiAudi("JUTEST");
		precotejo.setNoArchivoOriginal("JUTEST5");
		precotejoService.insert(precotejo, precotejoRegistroList, false);
		// archivo 6
		precotejoRegistroList = precotejoUploadService.readFromStream(getClass().getResourceAsStream("/csv/padron1nuevoUTF8-1-1.P6.csv"));
		precotejo = new Precotejo();
		precotejo.setCoEntidad(1);
		precotejo.setUsCreaAudi("JUTEST");
		precotejo.setUsModiAudi("JUTEST");
		precotejo.setNoArchivoOriginal("JUTEST6");
		precotejoService.insert(precotejo, precotejoRegistroList, false);
		//
		precotejoService.waitTask(); //sino el junit lo para en prima!

		Assert.assertEquals(1, 1);
	}

	@Test
	public void testWritePadron() {
		System.out.println("Volcar observaciones de padrón a CSV: ");

		/*
		List list = precotejoService.getAllFromPrecotejoObs("1", "1");
		ByteArrayOutputStream stream=(ByteArrayOutputStream)precotejoUploadService.writeToStream(list);
		System.out.println(stream);
		*/

		List<PrecotejoRegistro> list = precotejoService.getAllFromCotejoObsDetails("5","2");
		System.out.println(new String(precotejoUploadService.writeToCsvBytesFromPrecoteRegistroList(list)));

		Assert.assertEquals(1, 1);
	}

    @Test
    public void testPrecotejoRegistroDetalle(){
        PrecotejoRegistroDetalle precotejoRegistroDetalle = precotejoService.getPrecotejoRegistroDetalle("5","1","1");
        System.out.println(precotejoRegistroDetalle);
    }

}
