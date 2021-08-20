package pe.gob.reniec.padronn.logic.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.CentroEducativo;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class CentroEducativoDaoTest {

	@Autowired
	CentroEducativoDao centroEducativoDao;

	Gson gsonBeautiful=new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void testListarPorDepartamento() throws Exception {
		List<CentroEducativo> centroEducativoList=centroEducativoDao.listarPorDepartamento("21");
		System.out.println(gsonBeautiful.toJson(centroEducativoList));
	}

	@Test
	public void testObtenerPorCodigo() throws Exception {
		CentroEducativo centroEducativo=centroEducativoDao.obtenerPorCodigo("10");
		System.out.println(gsonBeautiful.toJson(centroEducativo));
	}

}
