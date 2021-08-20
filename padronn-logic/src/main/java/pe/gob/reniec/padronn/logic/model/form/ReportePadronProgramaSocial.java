package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronProgramaSocialFechas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 26/08/13
 * Time: 02:32 PM
 * To change this template use File | Settings | File Templates.
 */

@ReportePadronProgramaSocialFechas
public class ReportePadronProgramaSocial {

    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFormat(message = "Fecha inicial invalida")
    @DateFuture(message = "Fecha inicial no debe ser futura")
    private String feIni;

    @NotEmpty(message = "Fecha final es requerida")
    @DateFormat(message = "Fecha final invalida")
    @DateFuture(message = "Fecha final no debe ser futura")
    private String feFin;

    @NotEmpty(message = "Programa social es requerido de salud es requerido")
    private String coProgramaSocial;
    private String dePrograma;
    private String deProgramaSocial;


    @NotEmpty(message = "Municipio/entidad es requerido")
    private String coEntidad;

    private String deEntidad;

    private Integer nuPagina;

    private Integer esPadron;

    private String coUbigeoInei;

    @NotEmpty(message = "Tipo de registro es requerido")
    private String tiRegFecha;

    /*public String getFeIni() {
        return feIni;
    }

    public String getFeFin() {
        return feFin;
    }*/

    public String getCoProgramaSocial() {
        return coProgramaSocial;
    }

    public String getDeProgramaSocial() {
        return deProgramaSocial;
    }

    public Integer getNuPagina() {
        return nuPagina;
    }

    public Integer getEsPadron() {
        return esPadron;
    }

    /*public void setFeIni(String feIni) {
        this.feIni = feIni;
    }

    public void setFeFin(String feFin) {
        this.feFin = feFin;
    }*/

    public void setCoProgramaSocial(String coProgramaSocial) {
        this.coProgramaSocial = coProgramaSocial;
    }

    public void setDeProgramaSocial(String deProgramaSocial) {
        this.deProgramaSocial = deProgramaSocial;
    }

    public void setNuPagina(Integer nuPagina) {
        this.nuPagina = nuPagina;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
    }

    public String getCoEntidad() {
        return coEntidad;
    }

    public String getDeEntidad() {
        return deEntidad;
    }

    public void setCoEntidad(String coEntidad) {
        this.coEntidad = coEntidad;
    }

    public void setDeEntidad(String deEntidad) {
        this.deEntidad = deEntidad;
    }

    public String getCoUbigeoInei() {
        return coUbigeoInei;
    }

    public void setCoUbigeoInei(String coUbigeoInei) {
        this.coUbigeoInei = coUbigeoInei;
    }

    public String getDePrograma() {
        return dePrograma;
    }

    public void setDePrograma(String dePrograma) {
        this.dePrograma = dePrograma;
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



    /*    public Date getFeIniDate() throws ParseException {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                return simpleDateFormat.parse(feIni);
            }

            public Date getFeFinDate() throws ParseException {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                return simpleDateFormat.parse(feFin);
            }*/
    @Override
    public String toString() {
        return "ReportePadronProgramaSocial{" +
                /*"feIni='" + feIni + '\'' +
                ", feFin='" + feFin + '\'' +*/
                ", coProgramaSocial='" + coProgramaSocial + '\'' +
                ", deProgramaSocial='" + deProgramaSocial + '\'' +
                ", coEntidad='" + coEntidad + '\'' +
                ", deEntidad='" + deEntidad + '\'' +
                ", nuPagina=" + nuPagina +
                ", esPadron=" + esPadron +
                '}';
    }
}
