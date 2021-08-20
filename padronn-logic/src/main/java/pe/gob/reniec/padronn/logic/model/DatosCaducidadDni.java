package pe.gob.reniec.padronn.logic.model;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 10/06/13
 * Time: 06:59 PM
 */
public class DatosCaducidadDni implements java.io.Serializable{

	String dni;
	String fechaCaducidad;
	int mesesParaCaducidad;
	int diasParaCaducidad;

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(String fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public int getMesesParaCaducidad() {
		return mesesParaCaducidad;
	}

	public void setMesesParaCaducidad(int mesesParaCaducidad) {
		this.mesesParaCaducidad = mesesParaCaducidad;
	}

	public int getDiasParaCaducidad() {
		return diasParaCaducidad;
	}

	public void setDiasParaCaducidad(int diasParaCaducidad) {
		this.diasParaCaducidad = diasParaCaducidad;
	}
}
