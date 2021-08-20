package pe.gob.reniec.padronn.logic.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.SessionScope;
import pe.gob.reniec.padronn.logic.model.Persona;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class AniDaoTest {

	@Autowired
	AniDao aniDao;

	Gson gsonBeautiful=new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void testObtenerPorDniEnPersona() throws Exception {
		Persona persona=aniDao.obtenerPorDniEnPersona("43942930", Persona.TipoPersona.MAYOR);
		System.out.println(gsonBeautiful.toJson(persona));
	}

	@Test
	public void testListarPorDatosEnPersona() throws Exception {
		List<Persona> personaList=aniDao.listarPorDatosEnPersona("FERNANDEZ", "", "", Persona.TipoPersona.MADRE, 1, 10);
		System.out.println(gsonBeautiful.toJson(personaList));
	}

	/*@Test
	public void testBuscarPersonaPorDatos() throws Exception {
		List<PersonaAni> personaAniList=aniDao.buscarPersonaPorDatos("", "castillo", "", "", 1, 10);
		System.out.println(gsonBeautiful.toJson(personaAniList));
	}*/
}
