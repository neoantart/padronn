package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.checks.EmptyOrLengthCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.NameCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DatosPersonalesMadre;

import javax.validation.constraints.Pattern;

/**
 * User: jronal at gmail dot com
 * Date: 18/10/13
 * Time: 09:10 AM
 * Datos basicos de la madre
 */
@DatosPersonalesMadre
public class Madre {
    @NotEmpty(message = "Codigo de padron es requerido.")
    String coPadronNominal;

    @EmptyOrLengthCheck(
            min = 8,
            max = 8,
            message = "Debe tener 8 caracteres, o estar vacío"
    )
    @Pattern(
            regexp = "^[0-9]*$",
            message = "Contiene caracteres no válidos"
    )
    @NotEmpty(message = "DNI de es requerido")
//    @Length(max=8, min = 8)
    String coDniMadre;

    @NotEmpty(message = "Primer apellido de la madre es requerido")
    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @EmptyOrLengthCheck(message = "Debe tener entre 2 a 40 caracteres", min = 2, max = 40)
    String apPrimerMadre;

    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @EmptyOrLengthCheck(message = "Debe tener entre 0 a 40 caracteres o estar vacio",min = 0, max = 40)
    String apSegundoMadre;

    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @NotEmpty(message = "prenombres de la madre es requerido")
    @EmptyOrLengthCheck(message = "Debe de tener entre 2 a 60 caracteres", min=2, max=60)
    String prenomMadre;

    @NotEmpty(message = "Detalle de la rectificación es requerido")
    @Length(max=500)
    String deRectificacion;

    String nuCelMadre;
    String diCorreoMadre;

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getCoDniMadre() {
        return coDniMadre;
    }

    public void setCoDniMadre(String coDniMadre) {
        this.coDniMadre = coDniMadre;
    }

    public String getApPrimerMadre() {
        return apPrimerMadre;
    }

    public void setApPrimerMadre(String apPrimerMadre) {
        this.apPrimerMadre = apPrimerMadre;
    }

    public String getApSegundoMadre() {
        return apSegundoMadre;
    }

    public void setApSegundoMadre(String apSegundoMadre) {
        this.apSegundoMadre = apSegundoMadre;
    }

    public String getPrenomMadre() {
        return prenomMadre;
    }

    public void setPrenomMadre(String prenomMadre) {
        this.prenomMadre = prenomMadre;
    }

    public String getDeRectificacion() {
        return deRectificacion;
    }

    public void setDeRectificacion(String deRectificacion) {
        this.deRectificacion = deRectificacion;
    }

    public String getNuCelMadre() {
        return nuCelMadre;
    }

    public void setNuCelMadre(String nuCelMadre) {
        this.nuCelMadre = nuCelMadre;
    }

    public String getDiCorreoMadre() {
        return diCorreoMadre;
    }

    public void setDiCorreoMadre(String diCorreoMadre) {
        this.diCorreoMadre = diCorreoMadre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Madre madre = (Madre) o;

        if(madre.getApPrimerMadre() == null) madre.setApPrimerMadre("");
        if(madre.getApSegundoMadre() == null) madre.setApSegundoMadre("");
        if(madre.getPrenomMadre() == null) madre.setPrenomMadre("");
        if(madre.getCoDniMadre() == null) madre.setCoDniMadre("");

        if (apPrimerMadre != null ? !apPrimerMadre.equals(madre.apPrimerMadre) : madre.apPrimerMadre != null)
            return false;
        if (apSegundoMadre != null ? !apSegundoMadre.equals(madre.apSegundoMadre) : madre.apSegundoMadre != null)
            return false;
        if (coDniMadre != null ? !coDniMadre.equals(madre.coDniMadre) : madre.coDniMadre != null) return false;
        if (prenomMadre != null ? !prenomMadre.equals(madre.prenomMadre) : madre.prenomMadre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}