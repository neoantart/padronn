package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ActaFormFechas;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronActivosFechas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 23/08/13
 * Time: 03:15 PM
 * To change this template use File | Settings | File Templates.
 */

@ReportePadronActivosFechas
public class ReportePadronActivos {
    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFuture(message = "Fecha inicial no puede ser futura ")
    @DateFormat
    private String feIni;


    @NotEmpty(message = "Fecha final es requerida")
    @DateFuture(message = "Fecha final no puede ser futura")
    @DateFormat
    private String feFin;

    @NotEmpty(message = "Ubigeo es requerido")
    private String coUbigeo;

    private String deUbigeo;

    private Integer nuPagina;

    @NotEmpty(message = "Carga de regitros es requerido")
    private String tiRegistro;

    @NotEmpty(message = "Tipo de registro es requerido")
    private String tiRegFecha;

    private Integer esPadron;

    public String getFeIni() {
        return feIni;
    }

    public String getFeFin() {
        return feFin;
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

    public String getDeUbigeo() {
        return deUbigeo;
    }

    public void setFeIni(String feIni) {
        this.feIni = feIni;
    }

    public void setFeFin(String feFin) {
        this.feFin = feFin;
    }

    public void setCoUbigeo(String coUbigeo) {
        this.coUbigeo = coUbigeo;
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

    public String getTiRegistro() {
        return tiRegistro;
    }

    public void setTiRegistro(String tiRegistro) {
        this.tiRegistro = tiRegistro;
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


    /*
    public Integer getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
    }
*/

    public Date getFeIniDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(feIni);
    }

    public Date getFeFinDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(feFin);
    }

}
