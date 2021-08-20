package pe.gob.reniec.padronn.logic.model;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 19/09/13
 * Time: 09:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class PadronMovimiento extends AbstractBean {
    String coPadronNominal;
    String apPrimerMenor;
    String apSegundoMenor;
    String prenombreMenor;
    String feNacMenor;
    String coUbigeoIneiAnt;
    String coEntidadAnt;
    String coUbigeoIneiAct;
    String coEntidadAct;
    String coEntidadAntEess;
    String coEntidadActEess;
    //
    String deDepartamentoAnt;
    String deProvinciaAnt;
    String deDistritoAnt;
    String deEntidadAnt;

    String deDepartamentoAct;
    String deProvinciaAct;
    String deDistritoAct;
    String deEntidadAct;

    String usCreaAudi;
    String usModiAudi;
    String feCreaAudi;
    String feModiAudi;

    String coCentroPobladoAnt;
    String coCentroPobladoAct;

    String deCentroPobladoAnt;
    String deCentroPobladoAct;

    private String nuDniMenor;
    private String nuCui;
    private String nuCnv;

    public String getNuDniMenor() {
        return nuDniMenor;
    }

    public void setNuDniMenor(String nuDniMenor) {
        this.nuDniMenor = nuDniMenor;
    }

    public String getNuCui() {
        return nuCui;
    }

    public void setNuCui(String nuCui) {
        this.nuCui = nuCui;
    }

    public String getNuCnv() {
        return nuCnv;
    }

    public void setNuCnv(String nuCnv) {
        this.nuCnv = nuCnv;
    }

    public String getCoEntidadAntEess() {
        return coEntidadAntEess;
    }

    public void setCoEntidadAntEess(String coEntidadAntEess) {
        this.coEntidadAntEess = coEntidadAntEess;
    }

    public String getCoEntidadActEess() {
        return coEntidadActEess;
    }

    public void setCoEntidadActEess(String coEntidadActEess) {
        this.coEntidadActEess = coEntidadActEess;
    }

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getApPrimerMenor() {
        return apPrimerMenor;
    }

    public void setApPrimerMenor(String apPrimerMenor) {
        this.apPrimerMenor = apPrimerMenor;
    }

    public String getApSegundoMenor() {
        return apSegundoMenor;
    }

    public void setApSegundoMenor(String apSegundoMenor) {
        this.apSegundoMenor = apSegundoMenor;
    }

    public String getPrenombreMenor() {
        return prenombreMenor;
    }

    public void setPrenombreMenor(String prenombreMenor) {
        this.prenombreMenor = prenombreMenor;
    }

    public String getFeNacMenor() {
        return feNacMenor;
    }

    public void setFeNacMenor(String feNacMenor) {
        this.feNacMenor = feNacMenor;
    }

    public String getCoUbigeoIneiAnt() {
        return coUbigeoIneiAnt;
    }

    public void setCoUbigeoIneiAnt(String coUbigeoIneiAnt) {
        this.coUbigeoIneiAnt = coUbigeoIneiAnt;
    }

    public String getCoEntidadAnt() {
        return coEntidadAnt;
    }

    public void setCoEntidadAnt(String coEntidadAnt) {
        this.coEntidadAnt = coEntidadAnt;
    }

    public String getCoUbigeoIneiAct() {
        return coUbigeoIneiAct;
    }

    public void setCoUbigeoIneiAct(String coUbigeoIneiAct) {
        this.coUbigeoIneiAct = coUbigeoIneiAct;
    }

    public String getCoEntidadAct() {
        return coEntidadAct;
    }

    public void setCoEntidadAct(String coEntidadAct) {
        this.coEntidadAct = coEntidadAct;
    }

    public String getDeDepartamentoAnt() {
        return deDepartamentoAnt;
    }

    public void setDeDepartamentoAnt(String deDepartamentoAnt) {
        this.deDepartamentoAnt = deDepartamentoAnt;
    }

    public String getDeProvinciaAnt() {
        return deProvinciaAnt;
    }

    public void setDeProvinciaAnt(String deProvinciaAnt) {
        this.deProvinciaAnt = deProvinciaAnt;
    }

    public String getDeDistritoAnt() {
        return deDistritoAnt;
    }

    public void setDeDistritoAnt(String deDistritoAnt) {
        this.deDistritoAnt = deDistritoAnt;
    }

    public String getDeEntidadAnt() {
        return deEntidadAnt;
    }

    public void setDeEntidadAnt(String deEntidadAnt) {
        this.deEntidadAnt = deEntidadAnt;
    }

    public String getDeDepartamentoAct() {
        return deDepartamentoAct;
    }

    public void setDeDepartamentoAct(String deDepartamentoAct) {
        this.deDepartamentoAct = deDepartamentoAct;
    }

    public String getDeProvinciaAct() {
        return deProvinciaAct;
    }

    public void setDeProvinciaAct(String deProvinciaAct) {
        this.deProvinciaAct = deProvinciaAct;
    }

    public String getDeDistritoAct() {
        return deDistritoAct;
    }

    public void setDeDistritoAct(String deDistritoAct) {
        this.deDistritoAct = deDistritoAct;
    }

    public String getDeEntidadAct() {
        return deEntidadAct;
    }

    public void setDeEntidadAct(String deEntidadAct) {
        this.deEntidadAct = deEntidadAct;
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

    public String getCoCentroPobladoAnt() {
        return coCentroPobladoAnt;
    }

    public void setCoCentroPobladoAnt(String coCentroPobladoAnt) {
        this.coCentroPobladoAnt = coCentroPobladoAnt;
    }

    public String getCoCentroPobladoAct() {
        return coCentroPobladoAct;
    }

    public void setCoCentroPobladoAct(String coCentroPobladoAct) {
        this.coCentroPobladoAct = coCentroPobladoAct;
    }

    public String getDeCentroPobladoAnt() {
        return deCentroPobladoAnt;
    }

    public void setDeCentroPobladoAnt(String deCentroPobladoAnt) {
        this.deCentroPobladoAnt = deCentroPobladoAnt;
    }

    public String getDeCentroPobladoAct() {
        return deCentroPobladoAct;
    }

    public void setDeCentroPobladoAct(String deCentroPobladoAct) {
        this.deCentroPobladoAct = deCentroPobladoAct;
    }

    @Override
    public String toString() {
        return "PadronMovimiento{" +
                "coPadronNominal='" + coPadronNominal + '\'' +
                ", apPrimerMenor='" + apPrimerMenor + '\'' +
                ", apSegundoMenor='" + apSegundoMenor + '\'' +
                ", prenombreMenor='" + prenombreMenor + '\'' +
                ", feNacMenor='" + feNacMenor + '\'' +
                ", coUbigeoIneiAnt='" + coUbigeoIneiAnt + '\'' +
                ", coEntidadAnt='" + coEntidadAnt + '\'' +
                ", coUbigeoIneiAct='" + coUbigeoIneiAct + '\'' +
                ", coEntidadAct='" + coEntidadAct + '\'' +
                ", deDepartamentoAnt='" + deDepartamentoAnt + '\'' +
                ", deProvinciaAnt='" + deProvinciaAnt + '\'' +
                ", deDistritoAnt='" + deDistritoAnt + '\'' +
                ", deEntidadAnt='" + deEntidadAnt + '\'' +
                ", deDepartamentoAct='" + deDepartamentoAct + '\'' +
                ", deProvinciaAct='" + deProvinciaAct + '\'' +
                ", deDistritoAct='" + deDistritoAct + '\'' +
                ", deEntidadAct='" + deEntidadAct + '\'' +
                ", usCreaAudi='" + usCreaAudi + '\'' +
                ", usModiAudi='" + usModiAudi + '\'' +
                ", feCreaAudi='" + feCreaAudi + '\'' +
                ", feModiAudi='" + feModiAudi + '\'' +
                ", coCentroPobladoAnt='" + coCentroPobladoAnt + '\'' +
                ", coCentroPobladoAct='" + coCentroPobladoAct + '\'' +
                ", deCentroPobladoAnt='" + deCentroPobladoAnt + '\'' +
                ", deCentroPobladoAct='" + deCentroPobladoAct + '\'' +
                '}';
    }
}
