package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * Created by jfloresh on 28/04/2016.
 */
public class TipoObservacion implements Serializable{
    String coTipoObservacion;
    String deTipoObservacion;
    String esTipoObservacion;
    String usCreaAudi;
    String feCreaAudi;
    String usModiAudi;
    String feModiAudi;
    String nuOrden;

    public TipoObservacion() {
    }

    public String getCoTipoObservacion() {
        return coTipoObservacion;
    }

    public void setCoTipoObservacion(String coTipoObservacion) {
        this.coTipoObservacion = coTipoObservacion;
    }

    public String getDeTipoObservacion() {
        return deTipoObservacion;
    }

    public void setDeTipoObservacion(String deTipoObservacion) {
        this.deTipoObservacion = deTipoObservacion;
    }

    public String getEsTipoObservacion() {
        return esTipoObservacion;
    }

    public void setEsTipoObservacion(String esTipoObservacion) {
        this.esTipoObservacion = esTipoObservacion;
    }

    public String getUsCreaAudi() {
        return usCreaAudi;
    }

    public void setUsCreaAudi(String usCreaAudi) {
        this.usCreaAudi = usCreaAudi;
    }

    public String getFeCreaAudi() {
        return feCreaAudi;
    }

    public void setFeCreaAudi(String feCreaAudi) {
        this.feCreaAudi = feCreaAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
    }

    public String getNuOrden() {
        return nuOrden;
    }

    public void setNuOrden(String nuOrden) {
        this.nuOrden = nuOrden;
    }

    @Override
    public String toString() {
        return "TipoObservacion{" +
                "coTipoObservacion='" + coTipoObservacion + '\'' +
                ", deTipoObservacion='" + deTipoObservacion + '\'' +
                ", esTipoObservacion='" + esTipoObservacion + '\'' +
                ", usCreaAudi='" + usCreaAudi + '\'' +
                ", feCreaAudi='" + feCreaAudi + '\'' +
                ", usModiAudi='" + usModiAudi + '\'' +
                ", feModiAudi='" + feModiAudi + '\'' +
                ", nuOrden='" + nuOrden + '\'' +
                '}';
    }
}
