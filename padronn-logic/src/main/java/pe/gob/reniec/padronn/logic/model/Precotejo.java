package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase Precotejo.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 04:30 PM
 */
public class Precotejo
        implements Serializable {

	Number coEntidad;
	Number nuEnvio;
	String usCreaAudi;
	String usModiAudi;
	String noArchivoOriginal;
	String esEnvio;
	String nuRegistros;
	String nuRegistrosOk;
	String nuRegistrosObs; // todo eliminar nuRegistroObs
	//
	Date feCreaAudi;
	Date feModiAudi;
	String esEnvioHk;
	Date feProcesoIni;
	Date feProcesoFin;

	public Precotejo() {
	}

	public Precotejo(Precotejo precotejo) {
		// duplicamos registro considerar que Long, String son objetos inmutables, podríamos haber usado <CLONE>
		// http://stackoverflow.com/questions/10607990/how-should-i-copy-strings-in-java
		this.coEntidad = precotejo.coEntidad;
		this.nuEnvio = precotejo.nuEnvio;
		this.usCreaAudi = precotejo.usCreaAudi;
		this.usModiAudi = precotejo.usModiAudi;
		this.noArchivoOriginal = precotejo.getNoArchivoOriginal();
		this.esEnvio = precotejo.esEnvio;
		//
		if (precotejo.feCreaAudi != null)
			this.feCreaAudi = new Date(precotejo.feCreaAudi.getTime());
		if (precotejo.feModiAudi != null)
			this.feModiAudi = new Date(precotejo.feModiAudi.getTime());

		this.esEnvioHk = precotejo.esEnvioHk;
		if (precotejo.feProcesoIni != null)
			this.feProcesoIni = new Date(precotejo.feProcesoIni.getTime());
		if (precotejo.feProcesoFin != null)
			this.feProcesoFin = new Date(precotejo.feProcesoFin.getTime());
	}

	public Number getCoEntidad() {
		return coEntidad;
	}

	public void setCoEntidad(Number coEntidad) {
		this.coEntidad = coEntidad;
	}

	public Number getNuEnvio() {
		return nuEnvio;
	}

	public void setNuEnvio(Number nuEnvio) {
		this.nuEnvio = nuEnvio;
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

	public String getNoArchivoOriginal() {
		return noArchivoOriginal;
	}

	public void setNoArchivoOriginal(String noArchivoOriginal) {
		this.noArchivoOriginal = noArchivoOriginal;
	}

	public String getEsEnvio() {
		return esEnvio;
	}

	public void setEsEnvio(String esEnvio) {
		this.esEnvio = esEnvio;
	}

	public Date getFeCreaAudi() {
		return feCreaAudi;
	}

	public void setFeCreaAudi(Date feCreaAudi) {
		this.feCreaAudi = feCreaAudi;
	}

	public Date getFeModiAudi() {
		return feModiAudi;
	}

	public void setFeModiAudi(Date feModiAudi) {
		this.feModiAudi = feModiAudi;
	}

	public String getEsEnvioHk() {
		return esEnvioHk;
	}

	public void setEsEnvioHk(String esEnvioHk) {
		this.esEnvioHk = esEnvioHk;
	}

	public Date getFeProcesoIni() {
		return feProcesoIni;
	}

	public void setFeProcesoIni(Date feProcesoIni) {
		this.feProcesoIni = feProcesoIni;
	}

	public Date getFeProcesoFin() {
		return feProcesoFin;
	}

	public void setFeProcesoFin(Date feProcesoFin) {
		this.feProcesoFin = feProcesoFin;
	}

	public String getNuRegistrosObs() {
		return nuRegistrosObs;
	}

	public void setNuRegistrosObs(String nuRegistrosObs) {
		this.nuRegistrosObs = nuRegistrosObs;
	}

	public String getNuRegistros() {
		return nuRegistros;
	}

	public void setNuRegistros(String nuRegistros) {
		this.nuRegistros = nuRegistros;
	}

	public String getNuRegistrosOk() {
		return nuRegistrosOk;
	}

	public void setNuRegistrosOk(String nuRegistrosOk) {
		this.nuRegistrosOk = nuRegistrosOk;
	}
}
