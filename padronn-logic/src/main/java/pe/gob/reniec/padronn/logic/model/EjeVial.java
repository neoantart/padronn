package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * Created by jfloresh on 09/11/2016.
 */
public class EjeVial implements Serializable {
    private String coVia;
    private String deVia;
    private String deViaAlt;
    private String coCentroPoblado;
    private String coCatVia;
    private String usCreaAudi;
    private String usModiAudi;
    private String feCreaAudi;
    private String feModiAudi;
    private String esVia;
    private String deCatViaDet;
    private String deCatVia;

    public EjeVial() {
    }

    public String getCoVia() {
        return coVia;
    }

    public void setCoVia(String coVia) {
        this.coVia = coVia;
    }

    public String getDeVia() {
        return deVia;
    }

    public void setDeVia(String deVia) {
        this.deVia = deVia;
    }

    public String getDeViaAlt() {
        return deViaAlt;
    }

    public void setDeViaAlt(String deViaAlt) {
        this.deViaAlt = deViaAlt;
    }

    public String getCoCentroPoblado() {
        return coCentroPoblado;
    }

    public void setCoCentroPoblado(String coCentroPoblado) {
        this.coCentroPoblado = coCentroPoblado;
    }

    public String getCoCatVia() {
        return coCatVia;
    }

    public void setCoCatVia(String coCatVia) {
        this.coCatVia = coCatVia;
    }

    public String getUsCreaAudi() {
        return usCreaAudi;
    }

    public void setUsCreaAudi(String usCreaAudi) {
        this.usCreaAudi = usCreaAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public String getFeCreaAudi() {
        return feCreaAudi;
    }

    public void setFeCreaAudi(String feCreaAudi) {
        this.feCreaAudi = feCreaAudi;
    }

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
    }

    public String getEsVia() {
        return esVia;
    }

    public void setEsVia(String esVia) {
        this.esVia = esVia;
    }

    public String getDeCatViaDet() {
        return deCatViaDet;
    }

    public void setDeCatViaDet(String deCatViaDet) {
        this.deCatViaDet = deCatViaDet;
    }

    public String getDeCatVia() {
        return deCatVia;
    }

    public void setDeCatVia(String deCatVia) {
        this.deCatVia = deCatVia;
    }
}