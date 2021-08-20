package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 17/06/13
 * Time: 11:14 AM
 */
public class ProgramaSocial implements Serializable {

	String coProgramaSocial;
	String dePrograma;
	String deProgramaLarga;

	public String getCoProgramaSocial() {
		return coProgramaSocial;
	}

	public void setCoProgramaSocial(String coProgramaSocial) {
		this.coProgramaSocial = coProgramaSocial;
	}

	public String getDePrograma() {
		return dePrograma;
	}

	public void setDePrograma(String dePrograma) {
		this.dePrograma = dePrograma;
	}

	public String getDeProgramaLarga() {
		return deProgramaLarga;
	}

	public void setDeProgramaLarga(String deProgramaLarga) {
		this.deProgramaLarga = deProgramaLarga;
	}
}
