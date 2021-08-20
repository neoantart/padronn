package pe.gob.reniec.padronn.logic.model;

public class Dominio implements java.io.Serializable {

    String coDominio;
    String noDom;
    String deDom;
    String deDomDetalle;
    String esDom;
    String usCreaAudi;
    String usModiAudi;
    String feCreaAudi;
    String feModiAudi;


    public String getCoDominio() {
        return coDominio;
    }

    public void setCoDominio(String coDominio) {
        this.coDominio = coDominio;
    }

    public String getNoDom() {
        return noDom;
    }

    public void setNoDom(String noDom) {
        this.noDom = noDom;
    }

    public String getDeDom() {
        return deDom;
    }

    public void setDeDom(String deDom) {
        this.deDom = deDom;
    }

    public String getDeDomDetalle() {
        return deDomDetalle;
    }

    public void setDeDomDetalle(String deDomDetalle) {
        this.deDomDetalle = deDomDetalle;
    }

    public String getEsDom() {
        return esDom;
    }

    public void setEsDom(String esDom) {
        this.esDom = esDom;
    }

    public String getUsCreaAudi() {
        return usCreaAudi;
    }

    public void setUsCreaAudi(String usCreaAudi) {
        this.usCreaAudi = usCreaAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public String getFeCreaAudi() {
        return feCreaAudi;
    }

    public void setFeCreaAudi(String feCreaAudi) {
        this.feCreaAudi = feCreaAudi;
    }

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
    }

    @Override
    public String toString() {
        return "Dominio{" +
                "coDominio='" + coDominio + '\'' +
                ", noDom='" + noDom + '\'' +
                ", deDom='" + deDom + '\'' +
                ", deDomDetalle='" + deDomDetalle + '\'' +
                ", esDom='" + esDom + '\'' +
                ", usCreaAudi='" + usCreaAudi + '\'' +
                ", usModiAudi='" + usModiAudi + '\'' +
                ", feCreaAudi='" + feCreaAudi + '\'' +
                ", feModiAudi='" + feModiAudi + '\'' +
                '}';
    }
}
