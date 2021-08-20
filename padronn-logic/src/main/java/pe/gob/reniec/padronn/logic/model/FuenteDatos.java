package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

public class FuenteDatos implements Serializable {
    String coFuenteDatos;
    String deFuenteDatos;
    String esFuenteDatos;
    String usCreaRegistro;
    String feCreaRegistro;
    String usModiRegistro;
    String feModiRegistro;

    public String getCoFuenteDatos() {
        return coFuenteDatos;
    }

    public void setCoFuenteDatos(String coFuenteDatos) {
        this.coFuenteDatos = coFuenteDatos;
    }

    public String getDeFuenteDatos() {
        return deFuenteDatos;
    }

    public void setDeFuenteDatos(String deFuenteDatos) {
        this.deFuenteDatos = deFuenteDatos;
    }

    public String getEsFuenteDatos() {
        return esFuenteDatos;
    }

    public void setEsFuenteDatos(String esFuenteDatos) {
        this.esFuenteDatos = esFuenteDatos;
    }

    public String getUsCreaRegistro() {
        return usCreaRegistro;
    }

    public void setUsCreaRegistro(String usCreaRegistro) {
        this.usCreaRegistro = usCreaRegistro;
    }

    public String getFeCreaRegistro() {
        return feCreaRegistro;
    }

    public void setFeCreaRegistro(String feCreaRegistro) {
        this.feCreaRegistro = feCreaRegistro;
    }

    public String getUsModiRegistro() {
        return usModiRegistro;
    }

    public void setUsModiRegistro(String usModiRegistro) {
        this.usModiRegistro = usModiRegistro;
    }

    public String getFeModiRegistro() {
        return feModiRegistro;
    }

    public void setFeModiRegistro(String feModiRegistro) {
        this.feModiRegistro = feModiRegistro;
    }

    @Override
    public String toString() {
        return "FuenteDatos{" +
                "coFuenteDatos='" + coFuenteDatos + '\'' +
                ", deFuenteDatos='" + deFuenteDatos + '\'' +
                ", esFuenteDatos='" + esFuenteDatos + '\'' +
                ", usCreaRegistro='" + usCreaRegistro + '\'' +
                ", feCreaRegistro='" + feCreaRegistro + '\'' +
                ", usModiRegistro='" + usModiRegistro + '\'' +
                ", feModiRegistro='" + feModiRegistro + '\'' +
                '}';
    }
}
