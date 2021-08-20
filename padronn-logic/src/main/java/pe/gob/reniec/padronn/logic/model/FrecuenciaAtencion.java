package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

public class FrecuenciaAtencion implements Serializable {

    String coFrecuenciaAtencion;
    String noFrecuenciaAtencion;
    String esFrecuenciaAtencion;

    public String getCoFrecuenciaAtencion() {
        return coFrecuenciaAtencion;
    }

    public void setCoFrecuenciaAtencion(String coFrecuenciaAtencion) {
        this.coFrecuenciaAtencion = coFrecuenciaAtencion;
    }

    public String getNoFrecuenciaAtencion() {
        return noFrecuenciaAtencion;
    }

    public void setNoFrecuenciaAtencion(String noFrecuenciaAtencion) {
        this.noFrecuenciaAtencion = noFrecuenciaAtencion;
    }

    public String getEsFrecuenciaAtencion() {
        return esFrecuenciaAtencion;
    }

    public void setEsFrecuenciaAtencion(String esFrecuenciaAtencion) {
        this.esFrecuenciaAtencion = esFrecuenciaAtencion;
    }
}
