package pe.gob.reniec.padronn.logic.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 17/12/13
 * Time: 02:37 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class DownloadActasTest {
    /*@Autowired
    ActaDao actaDao;*/

    @Test
    public void testdDownload() throws Exception {
        /*System.out.println(actaDao.getAll(1,10));*/
        System.out.println("test");
    }
}
