package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.model.Grupo;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteEntidadChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteRegistradorChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteRegistradorPrecargaChecks;

/**
 * Created by mfigueroa on 05/06/2014.
 */
public class ReporteUsuarios {
    @NotEmpty(message = "Ubigeo es requerido")
    private String coUbigeo;

    @NotEmpty(message = "Entidad/Municipalidad es requerido", groups = {ReporteEntidadChecks.class, ReporteRegistradorChecks.class, ReporteRegistradorPrecargaChecks.class})
    private String coEntidad;

    @NotEmpty(message = "Grupo es requerido")
    private String coGrupo;

    @NotEmpty(message = "Usuario es requerido")
    private String esUsuario;

    private String deEntidad;

    private String deGrupo;


    public ReporteUsuarios() {
    }

    public String getCoUbigeo() {
        return coUbigeo;
    }

    public void setCoUbigeo(String coUbigeo) {
        this.coUbigeo = coUbigeo;
    }

    public String getCoEntidad() {
        return coEntidad;
    }

    public void setCoEntidad(String coEntidad) {
        this.coEntidad = coEntidad;
    }

    public String getCoGrupo() {
        return coGrupo;
    }

    public void setCoGrupo(String coGrupo) {
        this.coGrupo = coGrupo;
    }

    public String getEsUsuario() {
        return esUsuario;
    }

    public void setEsUsuario(String esUsuario) {
        this.esUsuario = esUsuario;
    }

    public String getDeEntidad() {
        return deEntidad;
    }

    public void setDeEntidad(String deEntidad) {
        this.deEntidad = deEntidad;
    }

    public String getDeGrupo() {
        return deGrupo;
    }

    public void setDeGrupo(String deGrupo) {
        this.deGrupo = deGrupo;
    }

    /* HACER LAS VALIDACIONES DE REPORTE_USUARIO_MINSA */


    @Override
    public String toString() {
        return "ReporteUsuarios{" +
                " coeUbigeoInei='" + coUbigeo + '\'' +
                ", coEntidad='" + coEntidad + '\'' +
                ", coGrupo=" + coGrupo +
                ", esUsuario=" + esUsuario +
                ", deEntidad='" + deEntidad + '\'' +
                ", deGrupo='" + deGrupo + '\'' +
               '}';
    }


}
