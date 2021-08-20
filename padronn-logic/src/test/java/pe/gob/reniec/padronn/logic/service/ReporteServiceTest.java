package pe.gob.reniec.padronn.logic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class ReporteServiceTest {

    @Autowired
    ReporteService reporteService;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Before
    public void setup() {

    }

    @Test
    public void listarPadronesActivosTest() {
        System.out.println(gson.toJson(reporteService.listarPadronesActivos("100801", "01/01/2013", "23/08/2013", "")));
    }

    @Test
    public void countListaPadronesActivosTest() {
        System.out.println(String.format("Numero de registros: '%s'", reporteService.countListaPadronesActivos("100801", "01/01/2013", "23/08/2013","")));
    }


    @Test
    public void listarPadronesActivosFilasTest(){
        System.out.println(reporteService.listarPadronesActivos("100801", "01/01/2013", "23/08/2013", 1, 5, ""));
    }

    @Test
    public void getUbigeo() {
        System.out.println(gson.toJson(reporteService.getUbigeo("angaro")));
    }

}
