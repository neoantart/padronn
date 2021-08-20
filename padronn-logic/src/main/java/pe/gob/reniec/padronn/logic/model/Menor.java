package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.checks.EmptyOrLengthCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.NameCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.PrenombreCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.*;

import javax.validation.constraints.Pattern;

/**
 * User: jronal at gmail dot com
 * Date: 18/10/13
 * Time: 09:05 AM
 * Datos basicos del menor
 */

@DatosPersonalesMenor
public class Menor {
    @NotEmpty(message = "Codigo de padron es requerido.")
    String coPadronNominal;

    @NotEmpty(message = "Primer apellido del menor es requerido")
    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @EmptyOrLengthCheck(message = "Debe tener entre 2 a 40 caracteres", min = 2, max = 40)
    String apPrimerMenor;

    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @EmptyOrLengthCheck(message = "Debe tener entre 2 a 40 caracteres o estar vacio",min = 0, max = 40)
    String apSegundoMenor;

    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @NotEmpty(message = "Nombres del menor es requerido")
    @EmptyOrLengthCheck(message = "Debe de tener entre 2 a 60 caracteres", min=2, max=60)
    @PrenombreCheck(message = "Nombre invalido, no debe contener secuencias como NN, SN, etc.")
    String prenombreMenor;

    @EmptyOrLengthCheck(
            min = 8,
            max = 8,
            message = "DNI Debe tener 8 caracteres, o estar vacío"
            )
    @Pattern(
            regexp = "^[0-9]*$",
            message = "DNI Contiene caracteres no válidos"
            )
    String nuDniMenor;

    /*@EmptyOrLengthCheck(
            min = 8,
            max = 8,
            message = "CUI Debe tener 8 caracteres, o estar vacío"
    )
    @Pattern(
            regexp = "^[0-9]*$",
            message = "CUI Contiene caracteres no válidos"
    )*/
    @Cui
    String nuCui;

    String nuCnv;

    @NotEmpty(message = "Fecha de nacimiento del menor es requerida")
    @DateFormat
    @LessThanAge(years = 6)
    @DateBirth

    String feNacMenor;

    String coUbigeoInei;
    String deUbigeoInei;


    @NotEmpty(message = "Genero del menor es requerida")
    String coGeneroMenor;

    //extras
    String deGeneroMenor;

    @NotEmpty(message = "Detalle de la rectificación es requerido")
    @Length(max=500)
    String deRectificacion;

    String tiDocIdentidad;

    String esPadron;
    String fila;



    public String getApPrimerMenor() {
        return apPrimerMenor;
    }

    public void setApPrimerMenor(String apPrimerMenor) {
        this.apPrimerMenor = apPrimerMenor;
    }

    public String getApSegundoMenor() {
        return apSegundoMenor;
    }

    public void setApSegundoMenor(String apSegundoMenor) {
        this.apSegundoMenor = apSegundoMenor;
    }

    public String getPrenombreMenor() {
        return prenombreMenor;
    }

    public void setPrenombreMenor(String prenombreMenor) {
        this.prenombreMenor = prenombreMenor;
    }

    public String getFeNacMenor() {
        return feNacMenor;
    }

    public void setFeNacMenor(String feNacMenor) {
        this.feNacMenor = feNacMenor;
    }

    public String getCoGeneroMenor() {
        return coGeneroMenor;
    }

    public void setCoGeneroMenor(String coGeneroMenor) {
        this.coGeneroMenor = coGeneroMenor;
    }

    public String getDeGeneroMenor() {
        return deGeneroMenor;
    }

    public void setDeGeneroMenor(String deGeneroMenor) {
        this.deGeneroMenor = deGeneroMenor;
    }

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getNuDniMenor() {
        return nuDniMenor;
    }

    public void setNuDniMenor(String nuDniMenor) {
        this.nuDniMenor = nuDniMenor;
    }

    public String getNuCui() {
        return nuCui;
    }

    public void setNuCui(String nuCui) {
        this.nuCui = nuCui;
    }

    public String getDeRectificacion() {
        return deRectificacion;
    }

    public void setDeRectificacion(String deRectificacion) {
        this.deRectificacion = deRectificacion;
    }

    public String getTiDocIdentidad() {
        return tiDocIdentidad;
    }

    public void setTiDocIdentidad(String tiDocIdentidad) {
        this.tiDocIdentidad = tiDocIdentidad;
    }

    public String getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(String esPadron) {
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

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }


    public String getNuCnv() {
        return nuCnv;
    }

    public void setNuCnv(String nuCnv) {
        this.nuCnv = nuCnv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menor menor = (Menor) o;
        if(menor.getApPrimerMenor() == null) menor.setApPrimerMenor("");
        if(menor.getApSegundoMenor() == null) menor.setApSegundoMenor("");
        if(menor.getPrenombreMenor() == null) menor.setPrenombreMenor("");
        if(menor.getCoGeneroMenor() == null) menor.setCoGeneroMenor("");
        if(menor.getFeNacMenor() == null) menor.setFeNacMenor("");
        if(menor.getNuCui() == null) menor.setNuCui("");
        if(menor.getNuDniMenor() == null) menor.setNuDniMenor("");

        if (apPrimerMenor != null ? !apPrimerMenor.equals(menor.apPrimerMenor) : menor.apPrimerMenor != null)
            return false;
        if (apSegundoMenor != null ? !apSegundoMenor.equals(menor.apSegundoMenor) : menor.apSegundoMenor != null)
            return false;
        if (coGeneroMenor != null ? !coGeneroMenor.equals(menor.coGeneroMenor) : menor.coGeneroMenor != null)
            return false;
        if (feNacMenor != null ? !feNacMenor.equals(menor.feNacMenor) : menor.feNacMenor != null) return false;
        if (nuCui != null ? !nuCui.equals(menor.nuCui) : menor.nuCui != null) return false;
        if (nuDniMenor != null ? !nuDniMenor.equals(menor.nuDniMenor) : menor.nuDniMenor != null) return false;
        if (prenombreMenor != null ? !prenombreMenor.equals(menor.prenombreMenor) : menor.prenombreMenor != null)
            return false;

        return true;
    }


    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "Menor{" +
                "coPadronNominal='" + coPadronNominal + '\'' +
                ", apPrimerMenor='" + apPrimerMenor + '\'' +
                ", apSegundoMenor='" + apSegundoMenor + '\'' +
                ", prenombreMenor='" + prenombreMenor + '\'' +
                ", nuDniMenor='" + nuDniMenor + '\'' +
                ", nuCui='" + nuCui + '\'' +
                ", feNacMenor='" + feNacMenor + '\'' +
                ", coGeneroMenor='" + coGeneroMenor + '\'' +
                ", deGeneroMenor='" + deGeneroMenor + '\'' +
                ", deRectificacion='" + deRectificacion + '\'' +
                ", tiDocIdentidad='" + tiDocIdentidad + '\'' +
                ", coUbigeoInei='" + coUbigeoInei + '\'' +
                ", deUbigeoInei='" + deUbigeoInei + '\'' +
                ", esPadron='" + esPadron + '\'' +
                '}';
    }
}
