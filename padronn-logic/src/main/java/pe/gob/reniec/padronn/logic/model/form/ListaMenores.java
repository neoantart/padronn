package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteEntidadChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteRegistradorChecks;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ListaMenoresFeNac;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ListaMenoresPeriodo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jfloresh on 23/05/2014.
 */

@ListaMenoresFeNac
@ListaMenoresPeriodo
public class ListaMenores implements Serializable {
    @NotEmpty(message = "Ubigeo es requerido")
    private String coUbigeo;

    private String deUbigeo;

    @DateFormat(message = "Fecha inicial de nacimiento invalida")
    @DateFuture(message = "Fecha inicial de nacimiento no debe ser futura")
    private String feNacIni;

    @DateFormat(message = "Fecha final de nacimiento invalida")
    @DateFuture(message = "Fecha final de nacimiento no debe ser futura")
    private String feNacFin;

    private String coGeneroMenor;
    private String tiDocMenor;
    private String deEdad;
    private String hastaEdad;

    @DateFormat(message = "Fecha inicial de periodo invalida")
    @DateFuture(message = "Fecha inicial de periodo no debe ser futura")
    private String feIni;

    @DateFormat(message = "Fecha final de periodo invalida")
    @DateFuture(message = "Fecha final de periodo no debe ser futura")
    private String feFin;

    //campos extras
    private Integer nuPagina;
    private String urlVolver;
    private String urlVolverFicha;

    private String apPrimerMenor;
    private String apSegundoMenor;
    private String prenombresMenor;

    private String apPrimer;
    private String apSegundo;
    private String prenombre;
    private String dni;

    private Integer esPadron;
    private String codigoPadron;
    private String coPadronNominal;

    private String tiDoc;
    private String nuDoc;

    private String tiRegFecha;

    public ListaMenores() {
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

    public String getCoGeneroMenor() {
        return coGeneroMenor;
    }

    public void setCoGeneroMenor(String coGeneroMenor) {
        this.coGeneroMenor = coGeneroMenor;
    }

    public String getTiDocMenor() {
        return tiDocMenor;
    }

    public void setTiDocMenor(String tiDocMenor) {
        this.tiDocMenor = tiDocMenor;
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

    public String getFeNacIni() {
        return feNacIni;
    }

    public void setFeNacIni(String feNacIni) {
        this.feNacIni = feNacIni;
    }

    public String getFeNacFin() {
        return feNacFin;
    }

    public void setFeNacFin(String feNacFin) {
        this.feNacFin = feNacFin;
    }

    public Integer getNuPagina() {
        return nuPagina;
    }

    public void setNuPagina(Integer nuPagina) {
        this.nuPagina = nuPagina;
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


    public String getTiDoc() {
        return tiDoc;
    }

    public void setTiDoc(String tiDoc) {
        this.tiDoc = tiDoc;
    }

    public String getNuDoc() {
        return nuDoc;
    }

    public void setNuDoc(String nuDoc) {
        this.nuDoc = nuDoc;
    }

    public String getTiRegFecha() {
        return tiRegFecha;
    }

    public void setTiRegFecha(String tiRegFecha) {
        this.tiRegFecha = tiRegFecha;
    }

    public Calendar getFeIniCalendar() {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            calendar.setTime(simpleDateFormat.parse(feIni));
        } catch (ParseException e) {
            calendar = null;
        }
        return calendar;
    }

    public Calendar getFeFinCalendar() {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            calendar.setTime(simpleDateFormat.parse(feFin));
        } catch (ParseException e) {
            calendar = null;
        }
        return calendar;
    }

    public Calendar getFeNacIniCalendar() {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            calendar.setTime(simpleDateFormat.parse(feNacIni));
        } catch (ParseException e) {
            calendar = null;
        }
        return calendar;
    }

    public Calendar getFeNacFinCalendar() {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            calendar.setTime(simpleDateFormat.parse(feNacFin));
        } catch (ParseException e) {
            calendar = null;
        }
        return calendar;
    }



}
