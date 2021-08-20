package pe.gob.reniec.padronn.logic.model;

/**
 * Created with IntelliJ IDEA.
 * User: jronal
 * Date: 29/05/13
 * Time: 03:48 PM
 */
public class Lugar implements java.io.Serializable{
    String coLugar;
    String deLugar;
    String coUbigeo;
    String deUbigeo;

    //extras
    String nuSinDni;
    String nuConDni;
    String nuTotal;
    String nuCui;

    public Lugar() {
    }

    public Lugar(String coLugar, String deLugar) {
        this.coLugar = coLugar;
        this.deLugar = deLugar;
    }

    public enum BaseDatosOrigen{
        ANI,
        PN,
        CNV
    }

    public void setCoLugar(String coLugar) {
        this.coLugar = coLugar;
    }

    public void setDeLugar(String deLugar) {
        this.deLugar = deLugar;
    }

    public String getCoLugar() {
        return coLugar;
    }

    public String getDeLugar() {
        return deLugar;
    }


    public String getCoUbigeo() {
        return coUbigeo;
    }

    public String getDeUbigeo() {
        return deUbigeo;
    }

    public void setCoUbigeo(String coUbigeo) {
        this.coUbigeo = coUbigeo;
    }

    public void setDeUbigeo(String deUbigeo) {
        this.deUbigeo = deUbigeo;
    }

    public String getNuSinDni() {
        return nuSinDni;
    }

    public void setNuSinDni(String nuSinDni) {
        this.nuSinDni = nuSinDni;
    }

    public String getNuConDni() {
        return nuConDni;
    }

    public void setNuConDni(String nuConDni) {
        this.nuConDni = nuConDni;
    }

    public String getNuTotal() {
        return nuTotal;
    }

    public void setNuTotal(String nuTotal) {
        this.nuTotal = nuTotal;
    }

    public String getNuCui() {
        return nuCui;
    }

    public void setNuCui(String nuCui) {
        this.nuCui = nuCui;
    }
}
