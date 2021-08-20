package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.*;

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
@ReportePadronActasTodosFechas
public class ReportePadronActasTodos {
    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFuture(message = "Fecha inicial no debe ser futura")
    @DateFormat
    private String feIni;

    @NotEmpty(message = "Fecha final es requerida")
    @DateFuture(message = "Fecha final no debe ser futura")
    @DateFormat
    private String feFin;

    private String coUbigeo;

    private Integer nuPagina;

    public String getFeIni() {
        return feIni;
    }

    public String getFeFin() {
        return feFin;
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

    public String getCoUbigeo() {
        return coUbigeo;
    }

    public void setCoUbigeo(String coUbigeo) {
        this.coUbigeo = coUbigeo;
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

}
