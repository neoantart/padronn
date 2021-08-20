package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteEntidadChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteMunicipioChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteRegistradorChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteRegistradorPrecargaChecks;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronEntidadFechas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 26/08/13
 * Time: 09:32 AM
 * To change this template use File | Settings | File Templates.
 */
@ReportePadronEntidadFechas(groups = {ReporteEntidadChecks.class,  ReporteRegistradorChecks.class})
public class ReportePadronEntidad {

    @NotEmpty(message = "Fecha inicial es requerida", groups = {ReporteEntidadChecks.class, ReporteRegistradorChecks.class})
    @DateFormat(message = "Fecha inicial invalida", groups = {ReporteEntidadChecks.class,  ReporteRegistradorChecks.class})
    @DateFuture(message = "Fecha inicial no debe ser futura", groups = {ReporteEntidadChecks.class, ReporteRegistradorChecks.class})
    private String feIni;


    @NotEmpty(message = "Fecha final es requerida", groups = {ReporteEntidadChecks.class, ReporteRegistradorChecks.class})
    @DateFormat(message = "Fecha final invalida", groups = {ReporteEntidadChecks.class, ReporteRegistradorChecks.class})
    @DateFuture(message = "Fecha final no debe ser futura", groups = {ReporteEntidadChecks.class, ReporteRegistradorChecks.class})
    private String feFin;

    @NotEmpty(message = "Entidad/Municipalidad es requerido", groups = {ReporteEntidadChecks.class, ReporteRegistradorChecks.class, ReporteRegistradorPrecargaChecks.class})
    private String coEntidad;

    private String deEntidad;

    private Integer nuPagina;

    private Integer esPadron;

    private String coUbigeoInei;

    private String deUbigeoInei;

    @NotEmpty(message = "Tipo de registro es requerido", groups = {ReporteEntidadChecks.class, ReporteRegistradorChecks.class})
    private String tiRegFecha;

    public String getFeIni() {
        return feIni;
    }

    public String getFeFin() {
        return feFin;
    }

    public String getCoEntidad() {
        return coEntidad;
    }

    public String getDeEntidad() {
        return deEntidad;
    }

    public Integer getNuPagina() {
        return nuPagina;
    }

    public Integer getEsPadron() {
        return esPadron;
    }

    public void setFeIni(String feIni) {
        this.feIni = feIni;
    }

    public void setFeFin(String feFin) {
        this.feFin = feFin;
    }

    public void setCoEntidad(String coEntidad) {
        this.coEntidad = coEntidad;
    }

    public void setDeEntidad(String deEntidad) {
        this.deEntidad = deEntidad;
    }

    public void setNuPagina(Integer nuPagina) {
        this.nuPagina = nuPagina;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
    }

    public String getCoUbigeoInei() {
        return coUbigeoInei;
    }

    public void setCoUbigeoInei(String coUbigeoInei) {
        this.coUbigeoInei = coUbigeoInei;
    }

    public String getDeUbigeoInei() {
        return deUbigeoInei;
    }

    public void setDeUbigeoInei(String deUbigeoInei) {
        this.deUbigeoInei = deUbigeoInei;
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
            e.printStackTrace();
            calendar = null;
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
            calendar = null;
        }

        return calendar;
    }
    //----------------------------------------------------------------



    @Override
    public String toString() {
        return "ReportePadronEntidad{" +
                "coEntidad='" + coEntidad + '\'' +
                ", deEntidad='" + deEntidad + '\'' +
                ", nuPagina=" + nuPagina +
                ", esPadron=" + esPadron +
                ", coUbigeoInei='" + coUbigeoInei + '\'' +
                ", deUbigeoInei='" + deUbigeoInei + '\'' +
                '}';
    }
}
