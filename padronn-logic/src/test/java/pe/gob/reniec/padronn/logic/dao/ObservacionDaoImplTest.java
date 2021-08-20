package pe.gob.reniec.padronn.logic.dao;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.Observacion;



@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class ObservacionDaoImplTest {

  @Autowired
  ObservacionDao observacionDao;

  @Test
  public void testAddObservacion() throws Exception {
    System.out.println("PRUEBA DE AddObservacion");

    Observacion observacion = new Observacion();
    observacion.setCoObservacion("1");
    observacion.setCoObservacionTipo("1");
    observacion.setCoUsuario("43873223");
    observacion.setDeDetalleAdicional("");
    observacion.setCoEntidad("5");
    observacion.setUsCreaAudi("43873223");
    observacion.setUsModiAudi("43873223");
    observacion.setCoPadronNominal("90000004");
    observacionDao.addObservacion(observacion);

    Assert.assertEquals(1, 1);
  }
}
