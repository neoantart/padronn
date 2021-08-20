package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteMidisCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteMovimientosCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReporteMovimientoFechas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 19/09/13
 * Time: 07:02 PM
 * To change this template use File | Settings | File Templates.
 */

@ReporteMovimientoFechas
public class ReportePadronMovimientos {
    @NotEmpty(message = "Ubigeo es requerido", groups = {ReporteMidisCheck.class})
    String coUbigeo;

    @NotEmpty(message = "Fecha inicial requerido", groups = {ReporteMidisCheck.class, ReporteMovimientosCheck.class})
    @DateFormat(message = "Fecha inicial invalida", groups = {ReporteMidisCheck.class, ReporteMovimientosCheck.class})
    @DateFuture(message = "Fecha inicial no debe ser futura", groups = {ReporteMidisCheck.class, ReporteMovimientosCheck.class})
    String feIni;

    @NotEmpty(message = "Fecha final requerido", groups = {ReporteMidisCheck.class, ReporteMovimientosCheck.class})
    @DateFormat(message = "Fecha final invalida", groups = {ReporteMidisCheck.class, ReporteMovimientosCheck.class})
    @DateFuture(message = "Fecha final no debe ser futura", groups = {ReporteMidisCheck.class, ReporteMovimientosCheck.class})
    String feFin;

    String nuPagina;

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

    public String getNuPagina() {
        return nuPagina;
    }

    public void setNuPagina(String nuPagina) {
        this.nuPagina = nuPagina;
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

    public String getCoUbigeo() {
        return coUbigeo;
    }

    public void setCoUbigeo(String coUbigeo) {
        this.coUbigeo = coUbigeo;
    }
}
