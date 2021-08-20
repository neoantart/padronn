package pe.gob.reniec.padronn.logic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.dao.UbigeoDao;
import pe.gob.reniec.padronn.logic.model.Ubigeo;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration("classpath:/applicationContext-test.xml")//configuracion spring
public class UbigeoServiceTest {

	Gson gson= new GsonBuilder().setPrettyPrinting().create();

	@Autowired
	UbigeoService ubigeoService;

	@Test
	public void testName() throws Exception {
		List<Ubigeo> ubigeoList=ubigeoService.buscarPorProvinciaDistritoEnDepartamento("pun", "21", 10);
		System.out.println(gson.toJson(ubigeoList));
	}
}
