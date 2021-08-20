package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronEdadConstraint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 17/09/13
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */

@ReportePadronEdadConstraint
public class ReportePadronEdad {
    @NotEmpty(message = "Rango de edad De: es requerido")
    String deEdad;

    @NotEmpty(message = "Rango de edad Hasta: es requerido")
    String hastaEdad;

    @NotEmpty(message = "Ubigeo es requerido")
    String coUbigeo;

    String deUbigeo;

    String edadMeses;

    Integer nuPagina;

    Integer esPadron;

    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFuture(message = "Fecha inicial no puede ser futura ")
    @DateFormat
    String feIni;

    @NotEmpty(message = "Fecha final es requerida")
    @DateFuture(message = "Fecha final no puede ser futura")
    @DateFormat
    String feFin;

    @NotEmpty(message = "Tipo de registro es requerido")
    String tiRegFecha;

    public String getDeEdad() {
        return deEdad;
    }

    public void setDeEdad(String deEdad) {
        this.deEdad = deEdad;
    }

    public Integer getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
    }

    public String getHastaEdad() {
        return hastaEdad;
    }

    public void setHastaEdad(String hastaEdad) {
        this.hastaEdad = hastaEdad;
    }

    public String getEdadMeses() {
        return edadMeses;
    }

    public void setEdadMeses(String edadMeses) {
        this.edadMeses = edadMeses;
    }


    public String getCoUbigeo() {
        return coUbigeo;
    }

    public void setCoUbigeo(String coUbigeo) {
        this.coUbigeo = coUbigeo;
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
}
