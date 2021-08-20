package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronActasFechas;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReportePadronActivosFechas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 26/08/13
 * Time: 06:58 PM
 * To change this template use File | Settings | File Templates.
 */

//@ReportePadronActivosFechas
//@ReportePadronActivosFechas
@ReportePadronActasFechas
public class ReportePadronActas {
    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFormat
    @DateFuture(message = "Fecha inicial no debe ser futura")
    private String feIni;


    @NotEmpty(message = "Fecha final es requerida")
    @DateFormat
    @DateFuture(message = "Fecha final no debe ser futura")
    private String feFin;

    @NotEmpty(message = "Entidad/Municipalidad es requerido")
    private String coEntidad;

    private String deEntidad;

    private Integer nuPagina;

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

    public Date getFeIniDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(this.feIni);

    }

    public Date getFeFinDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(this.feFin);

    }


    @Override
    public String toString() {
        return "ReportePadronActas{" +
                "feIni='" + feIni + '\'' +
                ", feFin='" + feFin + '\'' +
                ", coEntidad='" + coEntidad + '\'' +
                ", deEntidad='" + deEntidad + '\'' +
                ", nuPagina=" + nuPagina +
                '}';
    }
}
