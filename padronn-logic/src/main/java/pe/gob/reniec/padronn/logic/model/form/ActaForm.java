package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.omg.CORBA.PRIVATE_MEMBER;
import pe.gob.reniec.padronn.logic.web.validator.checks.EmptyOrLengthCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.ActaFormFechas;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateCurrentYear;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 19/08/13
 * Time: 08:44 AM
 * To change this template use File | Settings | File Templates.
 */

@ActaFormFechas
public class ActaForm {
    @NotEmpty(message = "Fecha inicial es requerida")
    @DateFormat
    //@DateCurrentYear
    private String feIniActa;


    @NotEmpty(message = "Fecha final es requerida")
    @DateFormat
    //@DateCurrentYear
    private String feFinActa;

    @NotEmpty(message = "Descripcion es requerido")
    @EmptyOrLengthCheck(
            message = "La descripción insertada es muy larga",
            min = 0,
            max = 300)
    private String deActa;

    @NotEmpty(message = "Suba un archivo PDF")
    private List<String> files;

    private List<String> deActaArchivos;
    private Date feIniActaDate;

    private Date feFinActaDate;

    /*@NotEmpty(message = "Establecimiento salud es requerido")*/
    private String coEstSalud;

    // extras
    private String deEstSalud;

    public ActaForm() {
    }

    public String getFeIniActa() {
        return feIniActa;
    }

    public String getFeFinActa() {
        return feFinActa;
    }

    public void setFeIniActa(String feIniActa) {
        this.feIniActa = feIniActa;
    }

    public void setFeFinActa(String feFinActa) {
        this.feFinActa = feFinActa;
    }


    public List<String> getDeActaArchivos() {
        return deActaArchivos;
    }


    public void setDeActaArchivos(List<String> deActaArchivos) {
        this.deActaArchivos = deActaArchivos;
    }


    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public void setFeIniActaDate(Date feIniActaDate) {
        this.feIniActaDate = feIniActaDate;
    }

    public void setFeFinActaDate(Date feFinActaDate) {
        this.feFinActaDate = feFinActaDate;
    }

    public Date getFeIniActaDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(this.feIniActa);

    }

    public Date getFeFinActaDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.parse(this.feFinActa);
    }

    public String getDeActa() {
        return deActa;
    }

    public void setDeActa(String deActa) {
        this.deActa = deActa;
    }

    public String getCoEstSalud() {
        return coEstSalud;
    }

    public void setCoEstSalud(String coEstSalud) {
        this.coEstSalud = coEstSalud;
    }

    public String getDeEstSalud() {
        return deEstSalud;
    }

    public void setDeEstSalud(String deEstSalud) {
        this.deEstSalud = deEstSalud;
    }

    @Override
    public String toString() {
        return "ActaForm{" +
                "feIniActa='" + feIniActa + '\'' +
                ", feFinActa='" + feFinActa + '\'' +
                ", deActa='" + deActa + '\'' +
                ", files=" + files +
                ", deActaArchivos=" + deActaArchivos +
                ", feIniActaDate=" + feIniActaDate +
                ", feFinActaDate=" + feFinActaDate +
                ", coEstSalud='" + coEstSalud + '\'' +
                '}';
    }
}
