package pe.gob.reniec.padronn.logic.model.form;

/**
 * Created by jfloresh on 04/06/2014.
 */
public class FormBuscarPersona {
    private String apPrimer;
    private String apSegundo;
    private String prenombre;
    private String dni;

    public FormBuscarPersona() {
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

    public String getPrenombre() {
        return prenombre;
    }

    public void setPrenombre(String prenombre) {
        this.prenombre = prenombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
