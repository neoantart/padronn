package pe.gob.reniec.padronn.logic.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;

@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
//@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})//configuracion spring
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class PrecotejoRegistroDaoTest {

	@Autowired
	PrecotejoRegistroDao precotejoRegistroDao;

	@Test
	public void testGuardarProgramaSocial() {
		PrecotejoRegistro precotejoRegistro;
		precotejoRegistro = precotejoRegistroDao.getPrecotejoRegistro("5", "118", "1");
		System.out.println(precotejoRegistro);
	}

}
