package pe.gob.reniec.padronn.logic.model;
import java.io.Serializable;

/**
 * Class PadronSeguro.
 * @author aquispej
 * @date 19/12/2018
 */
public class PadronSeguro implements Serializable{

    String coPadronNominal;
    String coTipoSeguro;
    Integer nuSec;
    String usuCreaAudi;
    String feCreaAudi;
    String usuModiAudi;
    String feModiAudi;

    public PadronSeguro() {
    }
    public PadronSeguro(String coPadronNominal, String coTipoSeguro) {
        this.coPadronNominal = coPadronNominal;
        this.coTipoSeguro = coTipoSeguro;
    }

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getCoTipoSeguro() {
        return coTipoSeguro;
    }

    public void setCoTipoSeguro(String coTipoSeguro) {
        this.coTipoSeguro = coTipoSeguro;
    }

    public Integer getNuSec() {
        return nuSec;
    }

    public void setNuSec(Integer nuSec) {
        this.nuSec = nuSec;
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
}
