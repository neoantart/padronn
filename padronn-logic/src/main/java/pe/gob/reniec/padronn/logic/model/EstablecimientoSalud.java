package pe.gob.reniec.padronn.logic.model;


public class EstablecimientoSalud implements java.io.Serializable {

	String coEstSalud;
	String deEstSalud;
	String tiEstSalud;
	String deDepartamento;
	String deProvincia;
	String deDistrito;
	String deDireccion;
	String coUbigeoInei;
	String nuSecuenciaLocal;

    //extra
    String coUbigeoUs;

    //indicador si el establecimiento esta fuera del ubigeo del usuario
    String inFueraUbigeo;

	// campos para validar establecimiento de salud, para busqueda.
	String nombre;

	String codigo;

	public String getCoEstSalud() {
		return coEstSalud;
	}

	public void setCoEstSalud(String coEstSalud) {
		this.coEstSalud = coEstSalud;
	}

	public String getDeEstSalud() {
		return deEstSalud;
	}

	public void setDeEstSalud(String deEstSalud) {
		this.deEstSalud = deEstSalud;
	}

	public String getTiEstSalud() {
		return tiEstSalud;
	}

	public void setTiEstSalud(String tiEstSalud) {
		this.tiEstSalud = tiEstSalud;
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

	public String getDeDireccion() {
		return deDireccion;
	}

	public void setDeDireccion(String deDireccion) {
		this.deDireccion = deDireccion;
	}

	public String getCoUbigeoInei() {
		return coUbigeoInei;
	}

	public void setCoUbigeoInei(String coUbigeoInei) {
		this.coUbigeoInei = coUbigeoInei;
	}

    public String getCoUbigeoUs() {
        return coUbigeoUs;
    }

    public void setCoUbigeoUs(String coUbigeoUs) {
        this.coUbigeoUs = coUbigeoUs;
    }

    public String getInFueraUbigeo() {
        return inFueraUbigeo;
    }

    public void setInFueraUbigeo(String inFueraUbigeo) {
        this.inFueraUbigeo = inFueraUbigeo;
    }

	public String getNuSecuenciaLocal() {
		return nuSecuenciaLocal;
	}

	public void setNuSecuenciaLocal(String nuSecuenciaLocal) {
		this.nuSecuenciaLocal = nuSecuenciaLocal;
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
}
