package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.*;

@FrecuenciaAtencion
public class ReportePadronResumenEdadEESS {

    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFuture(message = "Fecha inicial no puede ser futura ")
    @DateFormat
    private String feIni;

    @NotEmpty(message = "Fecha final es requerida")
    @DateFuture(message = "Fecha final no puede ser futura")
    @DateFormat
    private String feFin;

    @NotEmpty(message = "Tipo de registro es requerido")
    private String tiRegFecha;

    @NotEmpty(message = "Ubigeo es requerido")
    private String coUbigeo;

    private String deUbigeo;

    @NotEmpty(message = "Ingresar el nombre del establecimiento de salud")
    private String coEstSalud;

    private String nuSecuenciaLocal;

    private String deEstSalud;

    private Integer esPadron;

    private String coFrecAtencion;
/*
    @DateFormat
    @NotEmpty(message = "Ingresar el nombre del establecimiento de salud")
    private String feRegistroIni;

    @DateFormat
    @NotEmpty(message = "Ingresar el nombre del establecimiento de salud")
    private String feRegistroFin;*/

//    @NotEmpty(message = "Tipo de Establecimiento de Salud es requerido")
    @TipoEstablecimientoEmpty
    private Integer tiEstablecimiento;

    private Integer nuPagina;

    private Integer nuEdad;

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

    public String getCoEstSalud() {
        return coEstSalud;
    }

    public void setCoEstSalud(String coEstSalud) {
        this.coEstSalud = coEstSalud;
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

    public Integer getTiEstablecimiento() {
        return tiEstablecimiento;
    }

    public void setTiEstablecimiento(Integer tiEstablecimiento) {
        this.tiEstablecimiento = tiEstablecimiento;
    }

    public Integer getNuPagina() {
        return nuPagina;
    }

    public Integer getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(Integer esPadron) {
        this.esPadron = esPadron;
    }

    public void setNuPagina(Integer nuPagina) {
        this.nuPagina = nuPagina;
    }

    public Integer getNuEdad() {
        return nuEdad;
    }

    public void setNuEdad(Integer nuEdad) {
        this.nuEdad = nuEdad;
    }

    public String getCoFrecAtencion() {
        return coFrecAtencion;
    }

    public void setCoFrecAtencion(String coFrecAtencion) {
        this.coFrecAtencion = coFrecAtencion;
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

    /*
    public String getFeRegistroIni() {
        return feRegistroIni;
    }

    public void setFeRegistroIni(String feRegistroIni) {
        this.feRegistroIni = feRegistroIni;
    }

    public String getFeRegistroFin() {
        return feRegistroFin;
    }

    public void setFeRegistroFin(String feRegistroFin) {
        this.feRegistroFin = feRegistroFin;
    }*/
}
