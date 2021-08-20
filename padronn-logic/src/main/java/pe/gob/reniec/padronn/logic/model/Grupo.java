package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 19/07/13
 * Time: 08:57 AM
 */
public class Grupo implements java.io.Serializable{
    String coGrupo;

    @NotEmpty(message = "Nombre de Grupo requerido, longitud valida 3, 50")
    @Size(
            message = "Longitud invalida",
            min= 3,
            max= 50
    )
    String deGrupo;

    @NotEmpty(message = "Detalle de grupo requerido")
    @Size(
            message = "Longitud invalida, longitud valida 3, 200",
            min= 3,
            max= 200
    )
    String deDetalle;

    public Grupo() {
    }

    public String getCoGrupo() {
        return coGrupo;
    }

    public String getDeGrupo() {
        return deGrupo;
    }

    public void setCoGrupo(String coGrupo) {
        this.coGrupo = coGrupo;
    }

    public void setDeGrupo(String deGrupo) {
        this.deGrupo = deGrupo;
    }

    public String getDeDetalle() {
        return deDetalle;
    }

    public void setDeDetalle(String deDetalle) {
        this.deDetalle = deDetalle;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "coGrupo='" + coGrupo + '\'' +
                ", deGrupo='" + deGrupo + '\'' +
                ", deDetalle='" + deDetalle + '\'' +
                '}';
    }
}
