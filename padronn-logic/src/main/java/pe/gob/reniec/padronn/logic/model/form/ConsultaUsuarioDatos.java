package pe.gob.reniec.padronn.logic.model.form;

import java.io.Serializable;

/**
 * Created by jfloresh on 02/09/2014.
 */
public class ConsultaUsuarioDatos implements Serializable {
    String coUsuario;
    String apPrimer;
    String apSegundo;
    String prenombres;

    String nuPagina;
    String deUbigeo;

    public ConsultaUsuarioDatos() {
    }

    public String getCoUsuario() {
        return coUsuario;
    }

    public void setCoUsuario(String coUsuario) {
        this.coUsuario = coUsuario;
    }

    public String getApPrimer() {
        return apPrimer;
    }

    public void setApPrimer(String apPrimer) {
        this.apPrimer = apPrimer;
    }

    public String getApSegundo() {
        return apSegundo;
    }

    public void setApSegundo(String apSegundo) {
        this.apSegundo = apSegundo;
    }

    public String getPrenombres() {
        return prenombres;
    }

    public void setPrenombres(String prenombres) {
        this.prenombres = prenombres;
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
        return "CosultaUsuario{" +
                "coUsuario='" + coUsuario + '\'' +
                ", apPrimer='" + apPrimer + '\'' +
                ", apSegundo='" + apSegundo + '\'' +
                ", prenombres='" + prenombres + '\'' +
                '}';
    }
}
