package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronSinDocConstraints;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 20/09/13
 * Time: 04:40 PM
 * To change this template use File | Settings | File Templates.
 */
@ReportePadronSinDocConstraints
public class ReportePadronSinDoc {

    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFormat(message = "Fecha inicial invalida")
    @DateFuture(message = "Fecha inicial no debe ser futura")
    private String feIni;


    @NotEmpty(message = "Fecha final es requerida")
    @DateFormat(message = "Fecha final invalida")
    @DateFuture(message = "Fecha final no debe ser futura")
    private String feFin;

    @NotEmpty(message = "Ubigeo es requerido")
    private String coUbigeo;

    private String deUbigeo;

    private Integer esPadron;

    private Integer nuPagina;

    @NotEmpty(message = "Tipo de registro es requerido")
    private String tiRegFecha;

    public String getFeIni() {
        return feIni;
    }

    public void setFeIni(String feIni) {
        this.feIni = feIni;
    }


    public Integer getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
    }

    public String getFeFin() {
        return feFin;
    }

    public void setFeFin(String feFin) {
        this.feFin = feFin;
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

    //----------------------------------------------------------------
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
    //----------------------------------------------------------------

    @Override
    public String toString() {
        return "ReportePadronSinDoc{" +
                /*"feIni='" + feIni + '\'' +
                ", feFin='" + feFin + '\'' +*/
                ", coUbigeo='" + coUbigeo + '\'' +
                ", deUbigeo='" + deUbigeo + '\'' +
                ", nuPagina=" + nuPagina +
                '}';
    }
}
