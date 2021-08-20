package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Clase Entidad.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 16/07/13 11:46 AM
 */
public class Entidad  implements java.io.Serializable {

    public enum TipoEntidad {
        MUNICIPIO("1"),
        MINSA("2"),
        MEF("3"),
        RENIEC("4"),
        DISA("5"),
        RED("6"),
        MICRORED("7"),
        ESTABLECIMIENTO_SALUD("8"),
        OTROS("9"),
        MIDIS("10");

        private String coTipoEntidad;

        private TipoEntidad(String coTipoEntidad) {
            this.coTipoEntidad = coTipoEntidad;
        }

        public String getCoTipoEntidad() {
            return coTipoEntidad;
        }
    };

    /*public Entidad(Entidad entidad, TipoEntidad tipoEntidad) {
    }*/

    String coEntidad;

    @NotEmpty(message = "Nombre entidad corta requerida.")
    String deEntidad;

    @NotEmpty(message = "Nombre entidad larga requerida.")
    String deEntidadLarga;

    @NotEmpty(message = "Tipo entidad requerida.")
    String coTipoEntidad;

    @NotEmpty(message = "Ubigeo requerido.")
    String coUbigeoInei;

    String coRenaes;

    String usuCreaAudi;
    String feCreaAudi;
    String usuModiAudi;
    String feModiAudi;

    String deUbigeoInei;
    String deTipoEntidad;

    String deDepartamento;
    String deProvincia;
    String deDistrito;

    // extras
    String id;
    String text;


  /*public static enum CoTipoEntidad {
      MUNICIPIO("1");
  }*/


    public String getCoRenaes() {
        return coRenaes;
    }

    public void setCoRenaes(String coRenaes) {
        this.coRenaes = coRenaes;
    }

    public String getCoEntidad() {
        return coEntidad;
    }

    public void setCoEntidad(String coEntidad) {
        this.coEntidad = coEntidad;
    }

    public String getDeEntidad() {
        return deEntidad;
    }

    public void setDeEntidad(String deEntidad) {
        this.deEntidad = deEntidad;
    }

    public String getUsuCreaAudi() {
        return usuCreaAudi;
    }

    public void setUsuCreaAudi(String usuCreaAudi) {
        this.usuCreaAudi = usuCreaAudi;
    }

    public String getFeCreaAudi() {
        return feCreaAudi;
    }

    public void setFeCreaAudi(String feCreaAudi) {
        this.feCreaAudi = feCreaAudi;
    }

    public String getUsuModiAudi() {
        return usuModiAudi;
    }

    public void setUsuModiAudi(String usuModiAudi) {
        this.usuModiAudi = usuModiAudi;
    }

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
    }

    public String getCoTipoEntidad() {
        return coTipoEntidad;
    }

    public void setCoTipoEntidad(String coTipoEntidad) {
        this.coTipoEntidad = coTipoEntidad;
    }

    public String getDeEntidadLarga() {
        return deEntidadLarga;
    }

    public void setDeEntidadLarga(String deEntidadLarga) {
        this.deEntidadLarga = deEntidadLarga;
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

    public String getDeTipoEntidad() {
        return deTipoEntidad;
    }

    public void setDeTipoEntidad(String deTipoEntidad) {
        this.deTipoEntidad = deTipoEntidad;
    }

    public String getDeDepartamento() {
        return deDepartamento;
    }

    public void setDeDepartamento(String deDepartamento) {
        this.deDepartamento = deDepartamento;
    }

    public String getDeProvincia() {
        return deProvincia;
    }

    public void setDeProvincia(String deProvincia) {
        this.deProvincia = deProvincia;
    }

    public String getDeDistrito() {
        return deDistrito;
    }

    public void setDeDistrito(String deDistrito) {
        this.deDistrito = deDistrito;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
