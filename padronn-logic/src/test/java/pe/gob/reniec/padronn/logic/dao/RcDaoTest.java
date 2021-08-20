package pe.gob.reniec.padronn.logic.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.Persona;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-test.xml"})
public class RcDaoTest {

	@Autowired
	RcDao rcDao;

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void testName() throws Exception {
		List<Persona> personaList = rcDao.listarDatosCNVPorDniMadre("00000015");
		System.out.println(gson.toJson(personaList));
	}
}
