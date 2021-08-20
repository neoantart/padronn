package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * Created by jfloresh on 15/03/2016.
 */
public class PadronObservado implements Serializable{
    private String coPadronNominal;
    private String coTipoObservacion;
    private String esObservado;
    private String deObservadoDetalle;
    private String usCreaAudi;
    private String feCreaAudi;
    private String usModiAudi;
    private String feModiAudi;
    private String coPadronNominalDupli;


    // adicionales para ser mostrados en el reporte
    private String nuDniMenor;
    private String nuCui;
    private String nuCnv;
    private String apPrimerMenor;
    private String apSegundoMenor;
    private String prenombreMenor;
    private String feNacMenor;
    private String deEntidad;
    private String deTipoObservacion;
    private String nuItem;
    private String apPrimerMadre;
    private String apSegundoMadre;
    private String prenomMadre;
    private String coDniMadre;

    public PadronObservado() {}

    public PadronObservado(PadronNominal padronNominal) {
        coPadronNominal = padronNominal.getCoPadronNominal();
        usCreaAudi = padronNominal.getUsCreaRegistro();
        usModiAudi = padronNominal.getUsModiRegistro();
    }

    public PadronObservado(PadronNominal padronNominal, Tipo tipo) {
        this(padronNominal);
        this.coTipoObservacion = tipo.getCoTipo();
    }

    public static enum Tipo {
        MADRE_INDOCUMENTADA("1"),
        SIN_DATOS_MADRE("2"),
        MENOR_SIN_NOMBRE("3"),
        REGISTRO_POSIBLE_DUPLICADO("4"),
        FALLECIDO("5"),
        MULTIPLE_INSCRIPCION_IDENTIDAD("6");

        private String coTipo;

        private Tipo(String coTipo) {
            this.coTipo = coTipo;
        }
        public String getCoTipo() {
            return coTipo;
        }
    }

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getCoTipoObservacion() {
        return coTipoObservacion;
    }

    public void setCoTipoObservacion(String coTipoObservacion) {
        this.coTipoObservacion = coTipoObservacion;
    }

    public String getEsObservado() {
        return esObservado;
    }

    public void setEsObservado(String esObservado) {
        this.esObservado = esObservado;
    }

    public String getDeObservadoDetalle() {
        return deObservadoDetalle;
    }

    public void setDeObservadoDetalle(String deObservadoDetalle) {
        this.deObservadoDetalle = deObservadoDetalle;
    }

    public String getUsCreaAudi() {
        return usCreaAudi;
    }

    public void setUsCreaAudi(String usCreaAudi) {
        this.usCreaAudi = usCreaAudi;
    }

    public String getFeCreaAudi() {
        return feCreaAudi;
    }

    public void setFeCreaAudi(String feCreaAudi) {
        this.feCreaAudi = feCreaAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
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

    public String getDeEntidad() {
        return deEntidad;
    }

    public void setDeEntidad(String deEntidad) {
        this.deEntidad = deEntidad;
    }

    public String getDeTipoObservacion() {
        return deTipoObservacion;
    }

    public void setDeTipoObservacion(String deTipoObservacion) {
        this.deTipoObservacion = deTipoObservacion;
    }

    public String getNuItem() {
        return nuItem;
    }

    public void setNuItem(String nuItem) {
        this.nuItem = nuItem;
    }

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

    public String getCoPadronNominalDupli() {
        return coPadronNominalDupli;
    }

    public void setCoPadronNominalDupli(String coPadronNominalDupli) {
        this.coPadronNominalDupli = coPadronNominalDupli;
    }

    public String getApPrimerMadre() {
        return apPrimerMadre;
    }

    public void setApPrimerMadre(String apPrimerMadre) {
        this.apPrimerMadre = apPrimerMadre;
    }

    public String getApSegundoMadre() {
        return apSegundoMadre;
    }

    public void setApSegundoMadre(String apSegundoMadre) {
        this.apSegundoMadre = apSegundoMadre;
    }

    public String getPrenomMadre() {
        return prenomMadre;
    }

    public void setPrenomMadre(String prenomMadre) {
        this.prenomMadre = prenomMadre;
    }

    public String getCoDniMadre() {
        return coDniMadre;
    }

    public void setCoDniMadre(String coDniMadre) {
        this.coDniMadre = coDniMadre;
    }
}