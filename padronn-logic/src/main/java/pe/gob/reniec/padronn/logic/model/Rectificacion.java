package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * User: jronal at gmail dot com
 * Date: 31/10/13
 * Time: 07:02 PM
 */
public class Rectificacion implements Serializable {
    String nuSec;
    String coPadronNominal;
    String deRectificacion;
    String coEntidad;
    String usCreaAudi;
    String feCreaAudi;
    String tiPersona;
    String coDniAnt;
    String nuCuiAnt;
    String apPrimerAnt;
    String apSegundoAnt;
    String prenombresAnt;
    String feNacMenorAnt;
    String coGeneroMenorAnt;


    // TODO: mejorar...
    public enum TipoPersona{
        MENOR("1"),
        MADRE("2"),
        PADRE("3");
        private String tiPersona;

        private TipoPersona(String tiPersona) {
            this.tiPersona = tiPersona;
        }

        public String getTiPersona() {
            return tiPersona;
        }
    }

    public Rectificacion() {
    }
/*public Rectificacion(String coPadronNominal, String deRectificacion, String coEntidad, String usCreaAudi) {
        this.coPadronNominal = coPadronNominal;
        this.deRectificacion = deRectificacion;
        this.coEntidad = coEntidad;
        this.usCreaAudi = usCreaAudi;
    }*/

    // rectificacion del menor


/*    public Rectificacion(String nuSec, String coPadronNominal, String deRectificacion, String coEntidad, String usCreaAudi, String feCreaAudi, String tiPersona, String coDniAnt, String nuCuiAnt, String apPrimerAnt, String apSegundoAnt, String prenombresAnt, String feNacMenorAnt, String coGeneroMenorAnt) {
        this.nuSec = nuSec;
        this.coPadronNominal = coPadronNominal;
        this.deRectificacion = deRectificacion;
        this.coEntidad = coEntidad;
        this.usCreaAudi = usCreaAudi;
        this.feCreaAudi = feCreaAudi;
        this.tiPersona = tiPersona;
        this.coDniAnt = coDniAnt;
        this.nuCuiAnt = nuCuiAnt;
        this.apPrimerAnt = apPrimerAnt;
        this.apSegundoAnt = apSegundoAnt;
        this.prenombresAnt = prenombresAnt;
        this.feNacMenorAnt = feNacMenorAnt;
        this.coGeneroMenorAnt = coGeneroMenorAnt;
    }*/

    /*public Rectificacion(String coPadronNominal, String deRectificacion, String coEntidad, String usCreaAudi, String tiPersona, String coDniAnt, String nuCuiAnt, String apPrimerAnt, String apSegundoAnt, String prenombresAnt, String feNacMenorAnt, String coGeneroMenorAnt) {
        this.coPadronNominal = coPadronNominal;
        this.deRectificacion = deRectificacion;
        this.coEntidad = coEntidad;
        this.usCreaAudi = usCreaAudi;
        this.tiPersona = tiPersona;
        this.coDniAnt = coDniAnt;
        this.nuCuiAnt = nuCuiAnt;
        this.apPrimerAnt = apPrimerAnt;
        this.apSegundoAnt = apSegundoAnt;
        this.prenombresAnt = prenombresAnt;
        this.feNacMenorAnt = feNacMenorAnt;
        this.coGeneroMenorAnt = coGeneroMenorAnt;
    }


    // rectificacion padre y madre
    public Rectificacion(String coPadronNominal, String deRectificacion, String coEntidad, String usCreaAudi, String tiPersona, String coDniAnt, String apPrimerAnt, String apSegundoAnt, String prenombresAnt) {
        this.coPadronNominal = coPadronNominal;
        this.deRectificacion = deRectificacion;
        this.coEntidad = coEntidad;
        this.usCreaAudi = usCreaAudi;
        this.tiPersona = tiPersona;
        this.coDniAnt = coDniAnt;
        this.apPrimerAnt = apPrimerAnt;
        this.apSegundoAnt = apSegundoAnt;
        this.prenombresAnt = prenombresAnt;
    }*/

    public Rectificacion(String coPadronNominal, String deRectificacion, String coEntidad) {
        this.coPadronNominal = coPadronNominal;
        this.deRectificacion = deRectificacion;
        this.coEntidad = coEntidad;
    }

    public String getNuSec() {
        return nuSec;
    }

    public void setNuSec(String nuSec) {
        this.nuSec = nuSec;
    }

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getDeRectificacion() {
        return deRectificacion;
    }

    public void setDeRectificacion(String deRectificacion) {
        this.deRectificacion = deRectificacion;
    }

    public String getCoEntidad() {
        return coEntidad;
    }

    public void setCoEntidad(String coEntidad) {
        this.coEntidad = coEntidad;
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

    public String getTiPersona() {
        return tiPersona;
    }

    public void setTiPersona(String tiPersona) {
        this.tiPersona = tiPersona;
    }

    public String getCoDniAnt() {
        return coDniAnt;
    }

    public void setCoDniAnt(String coDniAnt) {
        this.coDniAnt = coDniAnt;
    }

    public String getNuCuiAnt() {
        return nuCuiAnt;
    }

    public void setNuCuiAnt(String nuCuiAnt) {
        this.nuCuiAnt = nuCuiAnt;
    }

    public String getApPrimerAnt() {
        return apPrimerAnt;
    }

    public void setApPrimerAnt(String apPrimerAnt) {
        this.apPrimerAnt = apPrimerAnt;
    }

    public String getApSegundoAnt() {
        return apSegundoAnt;
    }

    public void setApSegundoAnt(String apSegundoAnt) {
        this.apSegundoAnt = apSegundoAnt;
    }

    public String getPrenombresAnt() {
        return prenombresAnt;
    }

    public void setPrenombresAnt(String prenombresAnt) {
        this.prenombresAnt = prenombresAnt;
    }

    public String getFeNacMenorAnt() {
        return feNacMenorAnt;
    }

    public void setFeNacMenorAnt(String feNacMenorAnt) {
        this.feNacMenorAnt = feNacMenorAnt;
    }

    public String getCoGeneroMenorAnt() {
        return coGeneroMenorAnt;
    }

    public void setCoGeneroMenorAnt(String coGeneroMenorAnt) {
        this.coGeneroMenorAnt = coGeneroMenorAnt;
    }

}
