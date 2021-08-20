package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFuture;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ReporteEuropanConstaints;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 01/02/14
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
@ReporteEuropanConstaints
public class ReporteEuropan implements Serializable {

    @NotEmpty(message = "Distritos EUROPAN es requerido")
    private List<String> entidades;
    private String sEntidades;

    @NotEmpty(message = "Fecha inicial requerida")
    @DateFormat(message = "Fecha inicial invalida")
    @DateFuture(message = "Fecha inicial no debe ser futura")
    private String feIni;

    @NotEmpty(message = "Fecha final requerida")
    @DateFormat(message = "Fecha final invalida")
    @DateFuture(message = "Fecha final no debe ser futura")
    private String feFin;

    @NotEmpty(message = "Rango de edad es requerido")
    String deEdad;

    @NotEmpty(message = "Rango de edad es requerido")
    String hastaEdad;

    @NotEmpty(message = "Tipo de registro es requerido")
    String tiRegFecha;

    public List<String> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<String> entidades) {
        this.entidades = entidades;
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

    public String getDeEdad() {
        return deEdad;
    }

    public void setDeEdad(String deEdad) {
        this.deEdad = deEdad;
    }

    public String getHastaEdad() {
        return hastaEdad;
    }

    public void setHastaEdad(String hastaEdad) {
        this.hastaEdad = hastaEdad;
    }

    public String getsEntidades() {
        return sEntidades;
    }

    public void setsEntidades(String sEntidades) {
        this.sEntidades = sEntidades;
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
        return "ReporteEuropan{" +
                "entidades=" + entidades +
                ", feIni='" + feIni + '\'' +
                ", feFin='" + feFin + '\'' +
                ", deEdad='" + deEdad + '\'' +
                ", hastaEdad='" + hastaEdad + '\'' +
                '}';
    }
}
