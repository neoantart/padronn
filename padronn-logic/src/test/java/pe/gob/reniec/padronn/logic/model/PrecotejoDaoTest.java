package pe.gob.reniec.padronn.logic.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.dao.PrecotejoDao;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class PrecotejoDaoTest {

	@Autowired
	PrecotejoDao precotejoDao;

	@Test
	public void testUpdatePrecotejo() {
		Precotejo precotejo = new Precotejo();
		precotejo.setEsEnvio("3");
		precotejo.setCoEntidad(1);
		precotejo.setNuEnvio(4);
		precotejo.setUsCreaAudi("TEST");
		precotejo.setUsModiAudi("TEST");
		precotejoDao.update(precotejo);
	}

	@Test
	public void testInsertPrecotejo() {
		Precotejo precotejo = new Precotejo();
		precotejo.setEsEnvio("3");
		precotejo.setCoEntidad(1);
		precotejo.setNuEnvio(77);
		precotejo.setUsCreaAudi("TEST");
		precotejo.setUsModiAudi("TEST");
		precotejo.setNoArchivoOriginal("TEST");
		precotejoDao.insert(precotejo);
	}
}
