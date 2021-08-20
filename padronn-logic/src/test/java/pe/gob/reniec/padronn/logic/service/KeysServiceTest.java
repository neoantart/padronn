package pe.gob.reniec.padronn.logic.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class KeysServiceTest {

	@Autowired
	KeysService keysService;

	Logger log = Logger.getLogger(getClass());

	@Test
	public void testGetTiProSocialKeys() {
		List list = keysService.getTiProSocialKeys();
		for (Object o : list) {
//			log.debug(o);
		}
	}
}
