package pe.gob.reniec.padronn.logic.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jfloresh on 30/06/2015.
 */

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring*/
public class TestFoto {

    @Autowired
    BasicImagenCiudadano basicImagenCiudadano;

    private static final Logger logger = Logger.getLogger(TestFoto.class);

    @Test
    public void testImagen() {
        /*try {
            byte[] foto = basicImagenCiudadano.obtenerImagenCiudadanoWs("http://minsawlogic.reniec.gob.pe:7001/WebServicesImagenes/server/entrega/dni/", "45760858", "41691600");
            logger.info(Base64.encodeBase64String(foto));
        } catch (Exception e) {
            logger.error("error:", e);
        }*/
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher("2937492749823849248923843274");
        boolean b = m.matches();
        System.out.println(b);

    }
}