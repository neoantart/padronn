package pe.gob.reniec.padronn.logic.util;

import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.Usuario;


@Component
public class ImagenCiudadano {
    private static final Logger logger = Logger.getLogger(ImagenCiudadano.class);

    @Autowired
    BasicImagenCiudadano basicImagenCiudadano;

    @Autowired
    PadronProperties padronProperties;

    @Autowired
    Usuario usuario;

    public String obtenerImagen(String nuDni) {
        try {
            return Base64.encode(basicImagenCiudadano.obtenerImagenCiudadanoWs(padronProperties.URL_WS_IMAGENES, nuDni, new String(org.apache.commons.codec.binary.Base64.decodeBase64(nuDni))));
        } catch (Exception e) {
            logger.error("Error en obtenerImagen", e);
            return "";
        }
    }

}