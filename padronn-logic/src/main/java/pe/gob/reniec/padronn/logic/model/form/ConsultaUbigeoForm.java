package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.CoUbigeo;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;

import java.io.Serializable;

/**
 * Created by jfloresh on 01/12/2014.
 */
public class ConsultaUbigeoForm implements Serializable {

    @CoUbigeo
    private String coUbigeo;

    private String deUbigeo;
    private String tiDocMenor;
    private String coGeneroMenor;

    @DateFormat(message = "Fecha inicial invalida")
    @DateFuture(message = "Fecha inicial no debe ser futura a la fecha actual")
    private String feIni;

    @DateFormat(message = "Fecha final invalida")
    @DateFuture(message = "Fecha final no debe ser futura a la fecha actual")
    private String feFin;

    private String deEdad;
    private String hastaEdad;
    private String coProgramaSocial;
    private String coEstSalud;
    private String tiSeguro;

    private Integer esPadron;

    //extras
    private String urlVolver;
    private String urlVolverFicha;
    private String nuPagina;


    //
    private String apPrimerMenor;
    private String apSegundoMenor;
    private String prenombresMenor;

    private String apPrimer;
    private String apSegundo;
    private String prenombre;
    private String dni;


    private String codigoPadron;
    private String coPadronNominal;
    private String nuDoc;
    private String tiDoc;

    private String tiRegFecha;

    public ConsultaUbigeoForm() {
    }


    public Integer getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
    }

    public String getCoUbigeo() {
        return coUbigeo;
    }

    public void setCoUbigeo(String coUbigeo) {
        this.coUbigeo = coUbigeo;
    }

    public String getDeUbigeo() {
        return deUbigeo;
    }

    public void setDeUbigeo(String deUbigeo) {
        this.deUbigeo = deUbigeo;
    }

    public String getUrlVolver() {
        return urlVolver;
    }

    public void setUrlVolver(String urlVolver) {
        this.urlVolver = urlVolver;
    }

    public String getTiDocMenor() {
        return tiDocMenor;
    }

    public void setTiDocMenor(String tiDocMenor) {
        this.tiDocMenor = tiDocMenor;
    }

    public String getCoGeneroMenor() {
        return coGeneroMenor;
    }

    public void setCoGeneroMenor(String coGeneroMenor) {
        this.coGeneroMenor = coGeneroMenor;
    }

    public String getFeIni() {
        return feIni;
    }

    public void setFeIni(String feIni) {
        this.feIni = feIni;
    }

    public String getFeFin() {
        return feFin;
    }

    public void setFeFin(String feFin) {
        this.feFin = feFin;
    }

    public String getDeEdad() {
        return deEdad;
    }

    public void setDeEdad(String deEdad) {
        this.deEdad = deEdad;
    }

    public String getHastaEdad() {
        return hastaEdad;
    }

    public void setHastaEdad(String hastaEdad) {
        this.hastaEdad = hastaEdad;
    }

    public String getCoProgramaSocial() {
        return coProgramaSocial;
    }

    public void setCoProgramaSocial(String coProgramaSocial) {
        this.coProgramaSocial = coProgramaSocial;
    }

    public String getCoEstSalud() {
        return coEstSalud;
    }

    public void setCoEstSalud(String coEstSalud) {
        this.coEstSalud = coEstSalud;
    }

    public String getTiSeguro() {
        return tiSeguro;
    }

    public void setTiSeguro(String tiSeguro) {
        this.tiSeguro = tiSeguro;
    }

    public String getNuPagina() {
        return nuPagina;
    }

    public void setNuPagina(String nuPagina) {
        this.nuPagina = nuPagina;
    }

    public String getUrlVolverFicha() {
        return urlVolverFicha;
    }

    public void setUrlVolverFicha(String urlVolverFicha) {
        this.urlVolverFicha = urlVolverFicha;
    }

    public String getApPrimerMenor() {
        return apPrimerMenor;
    }

    public void setApPrimerMenor(String apPrimerMenor) {
        this.apPrimerMenor = apPrimerMenor;
    }

    public String getApSegundoMenor() {
        return apSegundoMenor;
    }

    public void setApSegundoMenor(String apSegundoMenor) {
        this.apSegundoMenor = apSegundoMenor;
    }

    public String getPrenombresMenor() {
        return prenombresMenor;
    }

    public void setPrenombresMenor(String prenombresMenor) {
        this.prenombresMenor = prenombresMenor;
    }

    public String getApPrimer() {
        return apPrimer;
    }

    public void setApPrimer(String apPrimer) {
        this.apPrimer = apPrimer;
    }

    public String getApSegundo() {
        return apSegundo;
    }

    public void setApSegundo(String apSegundo) {
        this.apSegundo = apSegundo;
    }

    public String getPrenombre() {
        return prenombre;
    }

    public void setPrenombre(String prenombre) {
        this.prenombre = prenombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCodigoPadron() {
        return codigoPadron;
    }

    public void setCodigoPadron(String codigoPadron) {
        this.codigoPadron = codigoPadron;
    }

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getNuDoc() {
        return nuDoc;
    }

    public void setNuDoc(String nuDoc) {
        this.nuDoc = nuDoc;
    }

    public String getTiDoc() {
        return tiDoc;
    }

    public void setTiDoc(String tiDoc) {
        this.tiDoc = tiDoc;
    }

    public String getTiRegFecha() {
        return tiRegFecha;
    }

    public void setTiRegFecha(String tiRegFecha) {
        this.tiRegFecha = tiRegFecha;
    }
}
