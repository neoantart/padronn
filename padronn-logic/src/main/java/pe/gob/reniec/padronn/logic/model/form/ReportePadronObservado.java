package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronObservadoFechas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 20/09/13
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */

@ReportePadronObservadoFechas
public class ReportePadronObservado {

    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFormat(message = "Fecha inicial invalida")
    @DateFuture(message = "Fecha inicial no puede ser futura")
    String feIni;

    @NotEmpty(message = "Fecha final es requerida")
    @DateFormat(message = "Fecha final invalida")
    @DateFuture(message = "Fecha final no puede ser futura")
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
}
