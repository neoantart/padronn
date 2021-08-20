package pe.gob.reniec.padronn.logic.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.Lugar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jronal
 * Date: 29/05/13
 * Time: 06:18 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class ReporteGraficoDaoTest {

    @Autowired
    ReporteGraficoDao reporteGraficoDao;

    Gson gson=new Gson();
    Gson gsonBeautiful=new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void getNuRegistrosPadron() {
        System.out.println(gsonBeautiful.toJson(reporteGraficoDao.getNuRegistrosPN()));
    }

    @Test
    public void getTotalRegistrosPN(){
        System.out.println(gsonBeautiful.toJson(reporteGraficoDao.getTotalRegistrosPN()));
    }

    @Test
    public void getPcRegistrosPN() {
        System.out.println(gsonBeautiful.toJson(reporteGraficoDao.getPcRegistrosPN("01")));
    }
}
