package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronEstablecimientoSaludFechas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 26/08/13
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
@ReportePadronEstablecimientoSaludFechas
public class ReportePadronEstablecimientoSalud {

    @NotEmpty(message = "Establecimiento de salud es requerido")
    private String coEstSalud;
    private String nuSecuenciaLocal;

    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFormat(message = "Fecha inicial invalida")
    @DateFuture(message = "Fecha inicial no debe ser futura")
    private String feIni;

    @NotEmpty(message = "Fecha final es requerida")
    @DateFormat(message = "Fecha final invalida")
    @DateFuture(message = "Fecha final no debe ser futura")
    private String feFin;

    private String deEstSalud;

    private Integer nuPagina;

    private Integer esPadron;

    @NotEmpty(message = "Tipo de registro es requerido")
    private String tiRegFecha;

    public Integer getEsPadron() {
        return esPadron;
    }

    public String getFeIni() {
        return feIni;
    }

    public String getFeFin() {
        return feFin;
    }

    public String getCoEstSalud() {
        return coEstSalud;
    }

    public String getDeEntidad() {
        return deEstSalud;
    }

    public Integer getNuPagina() {
        return nuPagina;
    }

    public void setFeIni(String feIni) {
        this.feIni = feIni;
    }

    public void setFeFin(String feFin) {
        this.feFin = feFin;
    }

    public void setCoEstSalud(String coEstSalud) {
        this.coEstSalud = coEstSalud;
    }

    public void setDeEntidad(String deEstSalud) {
        this.deEstSalud = deEstSalud;
    }

    public void setNuPagina(Integer nuPagina) {
        this.nuPagina = nuPagina;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;

    }


    public String getNuSecuenciaLocal() {
        return nuSecuenciaLocal;
    }

    public void setNuSecuenciaLocal(String nuSecuenciaLocal) {
        this.nuSecuenciaLocal = nuSecuenciaLocal;
    }

    public String getDeEstSalud() {
        return deEstSalud;
    }

    public void setDeEstSalud(String deEstSalud) {
        this.deEstSalud = deEstSalud;
    }
    /*public Date getFeIniDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(feIni);
    }
    public Date getFeFinDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(feFin);
    }*/

    public String getTiRegFecha() {
        return tiRegFecha;
    }

    public void setTiRegFecha(String tiRegFecha) {
        this.tiRegFecha = tiRegFecha;
    }

    //----------------------------------------------------------------
    public Calendar getFeIniCalendar() {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            calendar.setTime(simpleDateFormat.parse(feIni));
        } catch (ParseException e) {
            calendar = null;
        } catch (NullPointerException e) {
            e.printStackTrace();
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
        } catch (NullPointerException e) {
            e.printStackTrace();
            calendar = null;
        }
        return calendar;
    }
    //----------------------------------------------------------------




    @Override
    public String toString() {
        return "ReportePadronEstablecimientoSalud{" +
                "feIni='" + feIni + '\'' +
                ", feFin='" + feFin + '\'' +
                ", coEstSalud='" + coEstSalud + '\'' +
                ", deEstSalud='" + deEstSalud + '\'' +
                ", nuPagina=" + nuPagina +
                ", esPadron=" + esPadron +
                '}';
    }

}
