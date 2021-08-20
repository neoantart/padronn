package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

/**
 * Clase PadronPrograma.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 24/05/13 10:02 AM
 */
public class PadronPrograma  implements Serializable {

	String coPadronNominal;
	String coProgramaSocial;
	Integer nuSec;
	String usuCreaAudi;
	String feCreaAudi;
	String usuModiAudi;
	String feModiAudi;

    public PadronPrograma() {
    }

    public PadronPrograma(String coPadronNominal, String coProgramaSocial) {
        this.coPadronNominal = coPadronNominal;
        this.coProgramaSocial = coProgramaSocial;
    }

    public Integer getNuSec() {
		return nuSec;
	}

	public void setNuSec(Integer nuSec) {
		this.nuSec = nuSec;
	}

	public String getCoPadronNominal() {
		return coPadronNominal;
	}

	public void setCoPadronNominal(String coPadronNominal) {
		this.coPadronNominal = coPadronNominal;
	}

	public String getCoProgramaSocial() {
		return coProgramaSocial;
	}

	public void setCoProgramaSocial(String coProgramaSocial) {
		this.coProgramaSocial = coProgramaSocial;
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
