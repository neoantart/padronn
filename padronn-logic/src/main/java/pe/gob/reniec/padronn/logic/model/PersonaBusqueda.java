package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 22/05/13
 * Time: 05:51 PM
 */
public class PersonaBusqueda implements Serializable {

	String source;//0=padron, 1=ani

	String coPadronNominal;
	String nuDniMenor;

	String apPrimerMenor;
	String apSegundoMenor;
	String prenombreMenor;
	String feNacMenor;

	String coDniJefeFam;
	String apPrimerJefe;
	String apSegundoJefe;
	String prenomJefe;
	String tiVinculoJefe;

	String coDniMadre;
	String apPrimerMadre;
	String apSegundoMadre;
	String prenomMadre;
	String tiVinculoMadre;

	String deUbigeo;
	String deDireccion;
	String nuEdadActual;

	public String getCoPadronNominal() {
		return coPadronNominal;
	}

	public void setCoPadronNominal(String coPadronNominal) {
		this.coPadronNominal = coPadronNominal;
	}

	public String getNuDniMenor() {
		return nuDniMenor;
	}

	public void setNuDniMenor(String nuDniMenor) {
		this.nuDniMenor = nuDniMenor;
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

	public String getCoDniJefeFam() {
		return coDniJefeFam;
	}

	public void setCoDniJefeFam(String coDniJefeFam) {
		this.coDniJefeFam = coDniJefeFam;
	}

	public String getApPrimerJefe() {
		return apPrimerJefe;
	}

	public void setApPrimerJefe(String apPrimerJefe) {
		this.apPrimerJefe = apPrimerJefe;
	}

	public String getApSegundoJefe() {
		return apSegundoJefe;
	}

	public void setApSegundoJefe(String apSegundoJefe) {
		this.apSegundoJefe = apSegundoJefe;
	}

	public String getPrenomJefe() {
		return prenomJefe;
	}

	public void setPrenomJefe(String prenomJefe) {
		this.prenomJefe = prenomJefe;
	}

	public String getTiVinculoJefe() {
		return tiVinculoJefe;
	}

	public void setTiVinculoJefe(String tiVinculoJefe) {
		this.tiVinculoJefe = tiVinculoJefe;
	}

	public String getCoDniMadre() {
		return coDniMadre;
	}

	public void setCoDniMadre(String coDniMadre) {
		this.coDniMadre = coDniMadre;
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

	public String getTiVinculoMadre() {
		return tiVinculoMadre;
	}

	public void setTiVinculoMadre(String tiVinculoMadre) {
		this.tiVinculoMadre = tiVinculoMadre;
	}

	public String getDeUbigeo() {
		return deUbigeo;
	}

	public void setDeUbigeo(String deUbigeo) {
		this.deUbigeo = deUbigeo;
	}

	public String getDeDireccion() {
		return deDireccion;
	}

	public void setDeDireccion(String deDireccion) {
		this.deDireccion = deDireccion;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getNuEdadActual() {
		return nuEdadActual;
	}

	public void setNuEdadActual(String nuEdadActual) {
		this.nuEdadActual = nuEdadActual;
	}
}
