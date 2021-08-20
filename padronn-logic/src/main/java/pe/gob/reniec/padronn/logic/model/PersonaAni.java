package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ycarrillo
 * Date: 30/04/13
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class PersonaAni
        implements Serializable {

    private String nuDni;
    private String apPrimer;
    private String apSegundo;
    private String prenomInscrito;
    private String deDireccion;
    private String feNacimiento;
	private String deGenero;
    private String apPadrePrimer;
    private String apPadreSegundo;
    private String prenomPadre;
    private String nuDocPadre;
    private String apMadrePrimer;
    private String apMadreSegundo;
    private String prenomMadre;
    private String nuDocMadre;
	private String deUbigeo;

	public String getDeUbigeo() {
		return deUbigeo;
	}

	public void setDeUbigeo(String deUbigeo) {
		this.deUbigeo = deUbigeo;
	}

    public String getNuDni() {
        return nuDni;
    }

    public void setNuDni(String nuDni) {
        this.nuDni = nuDni;
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

    public String getPrenomInscrito() {
        return prenomInscrito;
    }

    public void setPrenomInscrito(String prenomInscrito) {
        this.prenomInscrito = prenomInscrito;
    }

    public String getDeDireccion() {
        return deDireccion;
    }

    public void setDeDireccion(String deDireccion) {
        this.deDireccion = deDireccion;
    }

    public String getFeNacimiento() {
        return feNacimiento;
    }

    public void setFeNacimiento(String feNacimiento) {
        this.feNacimiento = feNacimiento;
    }

	public String getDeGenero() {
		return deGenero;
	}

	public void setDeGenero(String deGenero) {
		this.deGenero = deGenero;
	}

	public String getApPadrePrimer() {
        return apPadrePrimer;
    }

    public void setApPadrePrimer(String apPadrePrimer) {
        this.apPadrePrimer = apPadrePrimer;
    }

    public String getApPadreSegundo() {
        return apPadreSegundo;
    }

    public void setApPadreSegundo(String apPadreSegundo) {
        this.apPadreSegundo = apPadreSegundo;
    }

    public String getPrenomPadre() {
        return prenomPadre;
    }

    public void setPrenomPadre(String prenomPadre) {
        this.prenomPadre = prenomPadre;
    }

    public String getNuDocPadre() {
        return nuDocPadre;
    }

    public void setNuDocPadre(String nuDocPadre) {
        this.nuDocPadre = nuDocPadre;
    }

    public String getApMadrePrimer() {
        return apMadrePrimer;
    }

    public void setApMadrePrimer(String apMadrePrimer) {
        this.apMadrePrimer = apMadrePrimer;
    }

    public String getApMadreSegundo() {
        return apMadreSegundo;
    }

    public void setApMadreSegundo(String apMadreSegundo) {
        this.apMadreSegundo = apMadreSegundo;
    }

    public String getPrenomMadre() {
        return prenomMadre;
    }

    public void setPrenomMadre(String prenomMadre) {
        this.prenomMadre = prenomMadre;
    }

    public String getNuDocMadre() {
        return nuDocMadre;
    }

    public void setNuDocMadre(String nuDocMadre) {
        this.nuDocMadre = nuDocMadre;
    }
}
