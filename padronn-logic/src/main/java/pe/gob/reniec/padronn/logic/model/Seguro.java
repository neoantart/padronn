package pe.gob.reniec.padronn.logic.model;
import java.io.Serializable;

public class Seguro implements Serializable{
    String coTipoSeguro;
    String deSeguro;
    String deSeguroLarga;

    public String getCoTipoSeguro() {
        return coTipoSeguro;
    }

    public void setCoTipoSeguro(String coTipoSeguro) {
        this.coTipoSeguro = coTipoSeguro;
    }

    public String getDeSeguro() {
        return deSeguro;
    }

    public void setDeSeguro(String deSeguro) {
        this.deSeguro = deSeguro;
    }

    public String getDeSeguroLarga() {
        return deSeguroLarga;
    }

    public void setDeSeguroLarga(String deSeguroLarga) {
        this.deSeguroLarga = deSeguroLarga;
    }
}
