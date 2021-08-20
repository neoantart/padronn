package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by paguilar on 20/08/2014.
 */
//@ReporteRecienNacidosFechaNac
@ReporteRecienNacidosFechaIns
public class ReportePadronRecienNacidos {

    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFuture(message = "Fecha inicial no puede ser futura ")
    @DateFormat
    private String feIni;

    @NotEmpty(message = "Fecha final es requerida")
    @DateFuture(message = "Fecha final no puede ser futura")
    @DateFormat
    private String feFin;

    /*@DateFuture(message = "Fecha Nacimiento inicial no puede ser futura ")
    @DateFormat
    private String feNacIni;

    @DateFuture(message = "Fecha Nacimiento final no puede ser futura")
    @DateFormat
    private String feNacFin;*/

    @NotEmpty(message = "Ubigeo es requerido")
    private String coUbigeo;

    private String deUbigeo;

    private String coEstSalud;

    private String nuSecuenciaLocal;


    private String deEstSalud;

    //@NotEmpty(message = "Rango de edad es requerido")
//    String deEdad;

    //@NotEmpty(message = "Rango de edad es requerido")
    //String hastaEdad;

//    String edadMeses;

    private Integer nuPagina;

    @NotEmpty(message = "Tipo de registro es requerido")
    private String tiRegFecha;

    /*@NotEmpty(message = "Tipo de registro es requerido")
    private String tiRegistro;*/

    private Integer esPadron;


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

/*    public String getFeNacIni() {
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
    }*/

    public String getCoUbigeo() {
        return coUbigeo;
    }

    public void setCoUbigeo(String coUbigeo) {
        this.coUbigeo = coUbigeo;
    }

    public String getDeUbigeo() {
        return deUbigeo;
    }

    public Integer getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
    }

    public void setDeUbigeo(String deUbigeo) {
        this.deUbigeo = deUbigeo;
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

    public String getNuSecuenciaLocal() {
        return nuSecuenciaLocal;
    }

    public void setNuSecuenciaLocal(String nuSecuenciaLocal) {
        this.nuSecuenciaLocal = nuSecuenciaLocal;
    }

    /*    public String getDeEdad() {
        return deEdad;
    }

    public void setDeEdad(String deEdad) {
        this.deEdad = deEdad;
    }

    public String getEdadMeses() {
        return edadMeses;
    }

    public void setEdadMeses(String edadMeses) {
        this.edadMeses = edadMeses;
    }*/

    public Integer getNuPagina() {
        return nuPagina;
    }

    public void setNuPagina(Integer nuPagina) {
        this.nuPagina = nuPagina;
    }

    public String getTiRegFecha() {
        return tiRegFecha;
    }

    public void setTiRegFecha(String tiRegFecha) {
        this.tiRegFecha = tiRegFecha;
    }

    /*    public String getTiRegistro() {
        return tiRegistro;
    }

    public void setTiRegistro(String tiRegistro) {
        this.tiRegistro = tiRegistro;
    }*/

    /*public Calendar getFeNacIniCalendar() {
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
    }*/

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

    public Date getFeIniDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(feIni);
    }

    public Date getFeFinDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(feFin);
    }

 /*   public Date getFeNacIniDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(feNacIni);
    }

    public Date getFeNacFinDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(feNacFin);
    }*/

}
