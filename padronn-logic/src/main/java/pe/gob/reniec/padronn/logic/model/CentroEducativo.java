package pe.gob.reniec.padronn.logic.model;

import pe.gob.reniec.padronn.logic.web.validator.constraints.NameCharacters;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 30/05/13
 * Time: 02:03 PM
 */
public class CentroEducativo extends AbstractBean {

	String coCentroEducativo;
	String coModular;
	String noCentroEducativo;
	String coUbigeo;
    String deUbigeo;
	String deDepartamento;
	String deProvincia;
	String deDistrito;
	String coCentroPoblado;
	String noCentroPoblado;
	String diCentroEducativo;
	String deNivelEducativo;

    String inFueraUbigeo;

    String coUbigeoUs;

	// campos para la validacion
    @NameCharacters
	String nombre;

	String codigo;

	public String getCoCentroEducativo() {
		return coCentroEducativo;
	}

	public void setCoCentroEducativo(String coCentroEducativo) {
		this.coCentroEducativo = coCentroEducativo;
	}

	public String getCoModular() {
		return coModular;
	}

	public void setCoModular(String coModular) {
		this.coModular = coModular;
	}

	public String getNoCentroEducativo() {
		return noCentroEducativo;
	}

	public void setNoCentroEducativo(String noCentroEducativo) {
		this.noCentroEducativo = noCentroEducativo;
	}

	public String getCoUbigeo() {
		return coUbigeo;
	}

	public void setCoUbigeo(String coUbigeo) {
		this.coUbigeo = coUbigeo;
	}

	public String getDeDepartamento() {
		return deDepartamento;
	}

	public void setDeDepartamento(String deDepartamento) {
		this.deDepartamento = deDepartamento;
	}

	public String getDeProvincia() {
		return deProvincia;
	}

	public void setDeProvincia(String deProvincia) {
		this.deProvincia = deProvincia;
	}

	public String getDeDistrito() {
		return deDistrito;
	}

	public void setDeDistrito(String deDistrito) {
		this.deDistrito = deDistrito;
	}

	public String getCoCentroPoblado() {
		return coCentroPoblado;
	}

	public void setCoCentroPoblado(String coCentroPoblado) {
		this.coCentroPoblado = coCentroPoblado;
	}

	public String getNoCentroPoblado() {
		return noCentroPoblado;
	}

	public void setNoCentroPoblado(String noCentroPoblado) {
		this.noCentroPoblado = noCentroPoblado;
	}

	public String getDiCentroEducativo() {
		return diCentroEducativo;
	}

	public void setDiCentroEducativo(String diCentroEducativo) {
		this.diCentroEducativo = diCentroEducativo;
	}

	public String getDeNivelEducativo() {
		return deNivelEducativo;
	}

	public void setDeNivelEducativo(String deNivelEducativo) {
		this.deNivelEducativo = deNivelEducativo;
	}

    public String getDeUbigeo() {
        return deUbigeo;
    }

    public void setDeUbigeo(String deUbigeo) {
        this.deUbigeo = deUbigeo;
    }

    public String getInFueraUbigeo() {
        return inFueraUbigeo;
    }

    public void setInFueraUbigeo(String inFueraUbigeo) {
        this.inFueraUbigeo = inFueraUbigeo;
    }

    public String getCoUbigeoUs() {
        return coUbigeoUs;
    }

    public void setCoUbigeoUs(String coUbigeoUs) {
        this.coUbigeoUs = coUbigeoUs;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/*String coCentroEducativo;
	String noCentroEducativo;
	String tiCentroEducativo;
	String tiGestionCentro;
	String deDireccion;
	String usCreaAudi;
	String feCreaAudi;
	String usModiAudi;
	String feModiAudi;

	public String getCoCentroEducativo() {
		return coCentroEducativo;
	}

	public void setCoCentroEducativo(String coCentroEducativo) {
		this.coCentroEducativo = coCentroEducativo;
	}

	public String getNoCentroEducativo() {
		return noCentroEducativo;
	}

	public void setNoCentroEducativo(String noCentroEducativo) {
		this.noCentroEducativo = noCentroEducativo;
	}

	public String getTiCentroEducativo() {
		return tiCentroEducativo;
	}

	public void setTiCentroEducativo(String tiCentroEducativo) {
		this.tiCentroEducativo = tiCentroEducativo;
	}

	public String getTiGestionCentro() {
		return tiGestionCentro;
	}

	public void setTiGestionCentro(String tiGestionCentro) {
		this.tiGestionCentro = tiGestionCentro;
	}

	public String getDeDireccion() {
		return deDireccion;
	}

	public void setDeDireccion(String deDireccion) {
		this.deDireccion = deDireccion;
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
	}*/

}
