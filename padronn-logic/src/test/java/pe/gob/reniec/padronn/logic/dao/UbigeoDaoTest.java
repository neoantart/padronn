package pe.gob.reniec.padronn.logic.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.Ubigeo;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})//configuracion spring
public class UbigeoDaoTest {

	@Autowired
	UbigeoDao ubigeoDao;

	Gson gson=new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void buscarPorProvinciaDistrito() throws Exception {
		List<Ubigeo> ubigeoList=ubigeoDao.buscarPorProvinciaDistritoEnDepartamento("jau", "10", 10);
		System.out.println(gson.toJson(ubigeoList));
	}
}
