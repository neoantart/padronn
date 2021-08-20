package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 14/08/13
 * Time: 06:26 PM
 * To change this template use File | Settings | File Templates.
 */

public class Acta implements Serializable {
    private String coActa;
    private String deActa;
    private String coEntidad;
    private String deEntidad;
    private String coEstSalud;
    private String feIni;
    private String feFin;
    private String feCreaAudi;
    private String usCreaAudi;
    private String feModiAudi;
    private String usModiAudi;
    private String esActa;

    //datos del usuario que registro
    private String apPrimer;
    private String apSegundo;
    private String prenombres;

    private String nuEnvio;
    private List<String> coActaArchivos;


    /*private Date feIniDate;
    private Date feFinDate;
    private Date feCreaAudiDate;*/

    /* Campos complementarios*/
    private List<String> deArchivos;

    private List<ActaArchivo> actaArchivos;

    //extras
    private String deDepartamento;
    private String deProvincia;
    private String deDistrito;
    private String nuArchivos;
    private String deEstSalud;
    private String anio;
    private String mes;

    public String getCoActa() {
        return coActa;
    }

    public String getFeIni() {
        return feIni;
    }

    public String getFeFin() {
        return feFin;
    }

    public String getFeCreaAudi() {
        return feCreaAudi;
    }

    public String getUsCreaAudi() {
        return usCreaAudi;
    }

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public void setCoActa(String coActa) {
        this.coActa = coActa;
    }

    public void setFeIni(String feIni) {
        this.feIni = feIni;
    }

    public void setFeFin(String feFin) {
        this.feFin = feFin;
    }

    public void setFeCreaAudi(String feCreaAudi) {
        this.feCreaAudi = feCreaAudi;
    }

    public void setUsCreaAudi(String usCreaAudi) {
        this.usCreaAudi = usCreaAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public List<String> getCoActaArchivos() {
        return coActaArchivos;
    }

    public List<String> getDeArchivos() {
        return deArchivos;
    }

    public void setCoActaArchivos(List<String> coActaArchivos) {
        this.coActaArchivos = coActaArchivos;
    }

    public void setDeArchivos(List<String> deArchivos) {
        this.deArchivos = deArchivos;
    }

    public List<ActaArchivo> getActaArchivos() {
        return actaArchivos;
    }

    public void setActaArchivos(List<ActaArchivo> actaArchivos) {
        this.actaArchivos = actaArchivos;
    }

    public String getCoEntidad() {
        return coEntidad;
    }

    public void setCoEntidad(String coEntidad) {
        this.coEntidad = coEntidad;
    }

    public String getDeEntidad() {
        return deEntidad;
    }

    public void setDeEntidad(String deEntidad) {
        this.deEntidad = deEntidad;
    }

    public String getDeActa() {
        return deActa;
    }

    public void setDeActa(String deActa) {
        this.deActa = deActa;
    }

    public String getApPrimer() {
        return apPrimer;
    }

    public String getApSegundo() {
        return apSegundo;
    }

    public String getPrenombres() {
        return prenombres;
    }

    public void setApPrimer(String apPrimer) {
        this.apPrimer = apPrimer;
    }

    public void setApSegundo(String apSegundo) {
        this.apSegundo = apSegundo;
    }

    public void setPrenombres(String prenombres) {
        this.prenombres = prenombres;
    }

    public String getNuEnvio() {
        return nuEnvio;
    }

    public void setNuEnvio(String nuEnvio) {
        this.nuEnvio = nuEnvio;
    }

    public String getDeDepartamento() {
        return deDepartamento;
    }

    public void setDeDepartamento(String deDepartamento) {
        this.deDepartamento = deDepartamento;
    }

    public String getDeProvincia() {
        return deProvincia;
    }

    public void setDeProvincia(String deProvincia) {
        this.deProvincia = deProvincia;
    }

    public String getDeDistrito() {
        return deDistrito;
    }

    public void setDeDistrito(String deDistrito) {
        this.deDistrito = deDistrito;
    }

    public String getNuArchivos() {
        return nuArchivos;
    }

    public void setNuArchivos(String nuArchivos) {
        this.nuArchivos = nuArchivos;
    }

    public String getCoEstSalud() {
        return coEstSalud;
    }

    public void setCoEstSalud(String coEstSalud) {
        this.coEstSalud = coEstSalud;
    }

    public String getDeEstSalud() {
        return deEstSalud;
    }

    public void setDeEstSalud(String deEstSalud) {
        this.deEstSalud = deEstSalud;
    }

    public String getEsActa() {
        return esActa;
    }

    public void setEsActa(String esActa) {
        this.esActa = esActa;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    @Override
    public String toString() {
        return "Acta{" +
                "coActa='" + coActa + '\'' +
                ", deActa='" + deActa + '\'' +
                ", coEntidad='" + coEntidad + '\'' +
                ", deEntidad='" + deEntidad + '\'' +
                ", coEstSalud='" + coEstSalud + '\'' +
                ", feIni='" + feIni + '\'' +
                ", feFin='" + feFin + '\'' +
                ", feCreaAudi='" + feCreaAudi + '\'' +
                ", usCreaAudi='" + usCreaAudi + '\'' +
                ", feModiAudi='" + feModiAudi + '\'' +
                ", usModiAudi='" + usModiAudi + '\'' +
                ", esActa='" + esActa + '\'' +
                ", apPrimer='" + apPrimer + '\'' +
                ", apSegundo='" + apSegundo + '\'' +
                ", prenombres='" + prenombres + '\'' +
                ", nuEnvio='" + nuEnvio + '\'' +
                ", coActaArchivos=" + coActaArchivos +
                ", deArchivos=" + deArchivos +
                ", actaArchivos=" + actaArchivos +
                ", deDepartamento='" + deDepartamento + '\'' +
                ", deProvincia='" + deProvincia + '\'' +
                ", deDistrito='" + deDistrito + '\'' +
                ", nuArchivos='" + nuArchivos + '\'' +
                ", deEstSalud='" + deEstSalud + '\'' +
                ", anio='" + anio + '\'' +
                ", mes='" + mes + '\'' +
                '}';
    }
}
