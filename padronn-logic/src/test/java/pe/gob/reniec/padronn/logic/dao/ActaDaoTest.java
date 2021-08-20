package pe.gob.reniec.padronn.logic.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.Persona;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 19/08/13
 * Time: 03:27 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class ActaDaoTest {
    Gson gsonBeautiful=new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    ActaDao actaDao;

    @Test
    public void testObtenerPorDniEnPersona() throws Exception {
        System.out.println(gsonBeautiful.toJson(actaDao.getAll(1,10)));
    }
}
