package pe.gob.reniec.padronn.logic.model;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 18/07/13
 * Time: 10:50 AM
 */
public class GrupoPermiso implements java.io.Serializable{
    String coGrupo;
    String dePermiso;
    String noModulo;
    String esPermiso;

    //vals
    String deGrupo;

    public GrupoPermiso() {
    }

    public String getCoGrupo() {
        return coGrupo;
    }

    public String getDePermiso() {
        return dePermiso;
    }

    public String getNoModulo() {
        return noModulo;
    }

    public void setCoGrupo(String coGrupo) {
        this.coGrupo = coGrupo;
    }

    public void setDePermiso(String dePermiso) {
        this.dePermiso = dePermiso;
    }

    public void setNoModulo(String noModulo) {
        this.noModulo = noModulo;
    }

    public String getEsPermiso() {
        return esPermiso;
    }

    public void setEsPermiso(String esPermiso) {
        this.esPermiso = esPermiso;
    }

    public String getDeGrupo() {
        return deGrupo;
    }

    public void setDeGrupo(String deGrupo) {
        this.deGrupo = deGrupo;
    }

    @Override
    public String toString() {
        return "GrupoPermiso{" +
                "coGrupo='" + coGrupo + '\'' +
                ", dePermiso='" + dePermiso + '\'' +
                ", noModulo='" + noModulo + '\'' +
                ", esPermiso='" + esPermiso + '\'' +
                ", deGrupo='" + deGrupo + '\'' +
                '}';
    }
}