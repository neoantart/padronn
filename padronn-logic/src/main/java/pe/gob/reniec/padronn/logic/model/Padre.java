package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.checks.EmptyOrLengthCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.NameCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DatosPersonalesPadre;

import javax.validation.constraints.Pattern;

/**
 * User: jronal at gmail dot com
 * Date: 18/10/13
 * Time: 09:08 AM
 * Datos basicos de padre
 */

@DatosPersonalesPadre
public class Padre {
    @NotEmpty(message = "Codigo de padron es requerido.")
    String coPadronNominal;

    @NotEmpty(message = "Vinculo del jefe de familia es requerido")
    String tiVinculoJefe;

    @EmptyOrLengthCheck(
            min = 8,
            max = 8,
            message = "Debe tener 8 caracteres, o estar vacío"
    )
    @Pattern(
            regexp = "^[0-9]*$",
            message = "Contiene caracteres no válidos"
    )
    String coDniJefeFam;

    @NotEmpty(message = "Primer apellido de la madre es requerido")
    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @EmptyOrLengthCheck(message = "Debe tener entre 2 a 40 caracteres", min = 2, max = 40)
    String apPrimerJefe;

    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @EmptyOrLengthCheck(message = "Debe tener entre 0 a 40 caracteres o estar vacio",min = 0, max = 40)
    String apSegundoJefe;

    @NameCheck(message = "Contiene caracteres o secuencias no válidas")
    @NotEmpty(message = "prenombres de la madre es requerido")
    @EmptyOrLengthCheck(message = "Debe de tener entre 2 a 60 caracteres", min=2, max=60)
    String prenomJefe;

    // extras
    String deVinculoJefe;

    @NotEmpty(message = "Detalle de la rectificación es requerido")
    @Length(max=500)
    String deRectificacion;

    public String getTiVinculoJefe() {
        return tiVinculoJefe;
    }

    public void setTiVinculoJefe(String tiVinculoJefe) {
        this.tiVinculoJefe = tiVinculoJefe;
    }

    public String getCoDniJefeFam() {
        return coDniJefeFam;
    }

    public void setCoDniJefeFam(String coDniJefeFam) {
        this.coDniJefeFam = coDniJefeFam;
    }

    public String getApPrimerJefe() {
        return apPrimerJefe;
    }

    public void setApPrimerJefe(String apPrimerJefe) {
        this.apPrimerJefe = apPrimerJefe;
    }

    public String getApSegundoJefe() {
        return apSegundoJefe;
    }

    public void setApSegundoJefe(String apSegundoJefe) {
        this.apSegundoJefe = apSegundoJefe;
    }

    public String getPrenomJefe() {
        return prenomJefe;
    }

    public void setPrenomJefe(String prenomJefe) {
        this.prenomJefe = prenomJefe;
    }

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getDeVinculoJefe() {
        return deVinculoJefe;
    }

    public void setDeVinculoJefe(String deVinculoJefe) {
        this.deVinculoJefe = deVinculoJefe;
    }

    public String getDeRectificacion() {
        return deRectificacion;
    }

    public void setDeRectificacion(String deRectificacion) {
        this.deRectificacion = deRectificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Padre padre = (Padre) o;

        if(padre.getApPrimerJefe() == null) padre.setApPrimerJefe("");
        if(padre.getApSegundoJefe() == null) padre.setApSegundoJefe("");
        if(padre.getPrenomJefe() == null) padre.setPrenomJefe("");
        if(padre.getTiVinculoJefe() == null) padre.setTiVinculoJefe("");
        if(padre.getCoDniJefeFam() == null) padre.setCoDniJefeFam("");

        if (apPrimerJefe != null ? !apPrimerJefe.equals(padre.apPrimerJefe) : padre.apPrimerJefe != null) return false;
        if (apSegundoJefe != null ? !apSegundoJefe.equals(padre.apSegundoJefe) : padre.apSegundoJefe != null)
            return false;
        if (coDniJefeFam != null ? !coDniJefeFam.equals(padre.coDniJefeFam) : padre.coDniJefeFam != null) return false;
        if (prenomJefe != null ? !prenomJefe.equals(padre.prenomJefe) : padre.prenomJefe != null) return false;
        if (tiVinculoJefe != null ? !tiVinculoJefe.equals(padre.tiVinculoJefe) : padre.tiVinculoJefe != null)
            return false;

        return true;
    }

}