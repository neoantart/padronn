package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.CoUbigeo;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronAltasBajasConstraints;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * User: jronal at gmail dot com
 * Date: 15/10/13
 * Time: 06:50 PM
 */
@ReportePadronAltasBajasConstraints
public class ReportePadronAltasBajas {
    @NotEmpty(message = "Ubigeo es requerido")
    @CoUbigeo
    String coUbigeo;

    String deUbigeo;
    private Integer nuPagina;
    private Integer esPadron;

    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFormat(message = "Fecha inicial invalida")
    @DateFuture(message = "Fecha inicial no debe ser futura")
    private String feIni;

    @NotEmpty(message = "Fecha final es requerida")
    @DateFormat(message = "Fecha final invalida")
    @DateFuture(message = "Fecha final no debe ser futura")
    private String feFin;

    @NotEmpty(message = "Tipo de registro es requerido")
    private String tiRegFecha;

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

    public Integer getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
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
        return "ReportePadronAltasBajas{" +
                "coUbigeo='" + coUbigeo + '\'' +
                ", deUbigeo='" + deUbigeo + '\'' +
                ", nuPagina=" + nuPagina +
                ", esPadron=" + esPadron +
                ", feIni='" + feIni + '\'' +
                ", feFin='" + feFin + '\'' +
                ", tiRegFecha='" + tiRegFecha + '\'' +
                '}';
    }
}
