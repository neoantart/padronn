package pe.gob.reniec.padronn.logic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.Persona;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class BusquedaDeMenorServiceTest {

	@Autowired
	BusquedaDeMenorService busquedaDeMenorService;

	Gson gson=new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void testName() throws Exception {
		Persona persona=busquedaDeMenorService.buscarMenorPorDni("63204356");
		System.out.println(gson.toJson(persona));
	}
}
