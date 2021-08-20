package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.reniec.padronn.logic.model.Usuario;
import pe.gob.reniec.padronn.logic.util.BasicImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.ImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jfloresh on 30/06/2015.
 */
/*desde ani*/
@Controller
public class ImagenCiudadanoController {
    private static final Logger logger = Logger.getLogger(ImagenCiudadanoController.class);

    @Autowired
    BasicImagenCiudadano basicImagenCiudadano;

    @Autowired
    PadronProperties padronProperties;

    @Autowired
    Usuario usuario;

    @Autowired
    ImagenCiudadano imagenCiudadano;

/*
    @RequestMapping(value = "/imagen_ciudadano/img_menor.do")
    public String mostrarImagen(@RequestParam(value="nu_dni") String nuDni, HttpServletResponse response) {
            return imagenCiudadano.obtenerImagen(nuDni);

   try {
            byte[] img = basicImagenCiudadano.obtenerImagenCiudadanoWs(padronProperties.URL_WS_IMAGENES, nuDni, usuario.getLogin());
            response.setContentType("image/jpg");
            //response.setHeader("Content-Disposition","attachment;filename="+nuDni+".jpg");
            response.setDateHeader("Expires", 0);
            response.setContentLength(img.length);
            try {
                ServletOutputStream out = response.getOutputStream();
                out.write(img, 0, img.length);
                out.flush();
                out.close();
                return null;
            } catch(IOException ioe) {
                logger.error("Error: ", ioe);
                response.setStatus(500);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error:", e);
            response.setContentType("image/jpg");
            response.setHeader("Content-Disposition", "attachment;filename=empty.jpg");
            response.setDateHeader("Expires", 0);
            response.setContentLength(0);
            return null;
        }
    }
*/
}