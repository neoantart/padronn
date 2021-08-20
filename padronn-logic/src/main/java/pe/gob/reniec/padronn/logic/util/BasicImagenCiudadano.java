package pe.gob.reniec.padronn.logic.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL; //to open connection
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jfloresh on 30/06/2015.
 */

@Component
public class BasicImagenCiudadano {

    @Autowired
    ImageUtils imageUtils;

    private static final Logger logger = Logger.getLogger(BasicImagenCiudadano.class);

    public byte[] obtenerImagenCiudadanoWs(String urlWs, String nuDni, String usuario) throws Exception {
        nuDni = new String(Base64.decodeBase64(nuDni));
        String uri = new String(urlWs + nuDni);
        URL url = new URL(uri);
        logger.info("obteniendo imagen del ws con URI=  " + uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //can cause a IOException (i.e. server is down)
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "image/jpg");
        if ( conn.getResponseCode() != HttpURLConnection.HTTP_OK ) {
            logger.error("Error obteniendo foto: "  + conn.getResponseCode() + " de URL " + urlWs);
            throw new RuntimeException("Codigo Error : " + conn.getResponseCode());
        }
        InputStream is = conn.getInputStream();

        byte[] img = IOUtils.toByteArray(is);
        if (img != null) {
            try {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
                logger.info("entramando imagen");
                return imageUtils.entramarFotoAlt(img, usuario, "RENIEC", "PADRON NOMINAL", simpleDateFormat.format(cal.getTime()));
            } catch (Exception e) {
                logger.error("Error: ", e);
                return img;
            }
        }
        return img;
    }

}