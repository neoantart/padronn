package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 15/08/13
 * Time: 09:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActaArchivos  implements Serializable {
    private String coActa;
    private String coActaArchivo;
    private String esActaArchivos;


    public String getCoActa() {
        return coActa;
    }

    public String getCoActaArchivo() {
        return coActaArchivo;
    }

    public void setCoActa(String coActa) {
        this.coActa = coActa;
    }

    public void setCoActaArchivo(String coActaArchivo) {
        this.coActaArchivo = coActaArchivo;
    }

    public String getEsActaArchivos() {
        return esActaArchivos;
    }

    public void setEsActaArchivos(String esActaArchivos) {
        this.esActaArchivos = esActaArchivos;
    }

    @Override
    public String toString() {
        return "ActaArchivos{" +
                "coActa='" + coActa + '\'' +
                ", coActaArchivo='" + coActaArchivo + '\'' +
                ", esActaArchivos='" + esActaArchivos + '\'' +
                '}';
    }
}
