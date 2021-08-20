package pe.gob.reniec.padronn.logic.model.form;

/**
 * Created by jfloresh on 04/01/2017.
 */
public class BuscarMenorDocumento {
    private String nuDni;
    private String nuCui;
    private String nuCnv;
    private String coPadronNominal;
    private String nuDoc;

    private String tiDoc;

    public static enum TipoDoc {
        DNI("0"),
        CUI("1"),
        CNV("2"),
        CO_PADRON("3");

        private String tiDoc;

        private TipoDoc(String tiDoc) {
            this.tiDoc = tiDoc;
        }

        public String getTiDoc(){
            return tiDoc;
        }
    }

    public BuscarMenorDocumento() {
    }

    public String getNuDni() {
        return nuDni;
    }

    public void setNuDni(String nuDni) {
        this.nuDni = nuDni;
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

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getTiDoc() {
        return tiDoc;
    }

    public void setTiDoc(String tiDoc) {
        this.tiDoc = tiDoc;
    }

    public String getNuDoc() {
        return nuDoc;
    }

    public void setNuDoc(String nuDoc) {
        this.nuDoc = nuDoc;
    }
}
