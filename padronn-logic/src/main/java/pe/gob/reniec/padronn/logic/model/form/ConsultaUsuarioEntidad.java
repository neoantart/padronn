package pe.gob.reniec.padronn.logic.model.form;

import java.io.Serializable;

/**
 * Created by jfloresh on 02/09/2014.
 */
public class ConsultaUsuarioEntidad implements Serializable {
    String coEntidad;
    String coUbigeoInei;
    String esUsuario;

    String nuPagina;

    String deUbigeo;

    public ConsultaUsuarioEntidad() {
    }

    public String getCoEntidad() {
        return coEntidad;
    }

    public void setCoEntidad(String coEntidad) {
        this.coEntidad = coEntidad;
    }

    public String getCoUbigeoInei() {
        return coUbigeoInei;
    }

    public void setCoUbigeoInei(String coUbigeoInei) {
        this.coUbigeoInei = coUbigeoInei;
    }

    public String getEsUsuario() {
        return esUsuario;
    }

    public void setEsUsuario(String esUsuario) {
        this.esUsuario = esUsuario;
    }

    public String getNuPagina() {
        return nuPagina;
    }

    public void setNuPagina(String nuPagina) {
        this.nuPagina = nuPagina;
    }

    public String getDeUbigeo() {
        return deUbigeo;
    }

    public void setDeUbigeo(String deUbigeo) {
        this.deUbigeo = deUbigeo;
    }



    @Override
    public String toString() {
        return "ConsultaUsuarios{" +
                "coEntidad='" + coEntidad + '\'' +
                ", coUbigeoInei='" + coUbigeoInei + '\'' +
                ", esUsuario='" + esUsuario + '\'' +
                '}';
    }
}
