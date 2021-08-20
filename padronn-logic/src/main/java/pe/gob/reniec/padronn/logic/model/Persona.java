package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

public class Persona implements Serializable{

	public enum BaseDatosOrigen{ ANI, PN, CNV }
	public enum OrigenFoto{ ANI, PN, EMPTY }
	public enum TipoPersona{ MADRE,	MAYOR, MENOR, TODOS, PADRE }

	String codigoPadronNominal;
	String dni;
	String cnv;
    String nuCui;

	String tipoDocumento;
	String primerApellido;
	String segundoApellido;
    String casadaApellido;
	String nombres;
	String genero;
	String codigoGenero;
	String fechaNacimiento;

	String horaNacimiento;
	String ubigeo;
	String codigoUbigeo;
	String codigoUbigeoReniec;
	String direccion;
    String codigoCentroPoblado;
    String deCentroPoblado;

	String codigoGradoInstruccion;

	String padreTipoVinculo;
	String padreCodigoTipoVinculo;
	String padreDni;
	String padrePrimerApellido;
	String padreSegundoApellido;
	String padreNombres;

	String madreTipoVinculo;
	String madreCodigoTipoVinculo;
	String madreDni;
    String madreTiDoc;
	String madrePrimerApellido;
	String madreSegundoApellido;
	String madreNombres;
	String madreGradoInstruccion;
	String madreCodigoGradoInstruccion;

	String coEstSaludNac;
	String nuSecuenciaLocalNac;
	String deEstSaludNac;

	String madreGradoInstruccionCnv;
	String madreGradoInstruccionReniec;

	//Datos compuestos, calculados e informativos
	BaseDatosOrigen baseDatosOrigen;
	String edad;
	String edadEscrita;
	OrigenFoto origenFoto;

	String esPadron;

    // restricciones
    String coRestri;
    String deRestri;

	String descUbigeo;

	// Nucleo Urbano
	String deVia;
	String deRefDir;

	//Unidad Censal
	String deAreaCcpp;

    String fila;

	String tipoFuente;

//	Datos de contacto
	String nuTelefono;
	String deEmail;

	String coFuentePrecarga;
	String deFuentePrecarga;
	String motivoBaja;
	String observacionBaja;

	String deCargaRegistro; //solo para reporte de rango de fechas

	String coTipoEntidad;

	String coEntidadEESS;

	public String getCoEntidadEESS() {
		return coEntidadEESS;
	}

	public void setCoEntidadEESS(String coEntidadEESS) {
		this.coEntidadEESS = coEntidadEESS;
	}

	public String getCoTipoEntidad() {
		return coTipoEntidad;
	}

	public void setCoTipoEntidad(String coTipoEntidad) {
		this.coTipoEntidad = coTipoEntidad;
	}

	public String getDeCargaRegistro() {
        return deCargaRegistro;
    }

    public void setDeCargaRegistro(String deCargaRegistro) {
        this.deCargaRegistro = deCargaRegistro;
    }

    public String getCoFuentePrecarga() {
		return coFuentePrecarga;
	}

	public void setCoFuentePrecarga(String coFuentePrecarga) {
		this.coFuentePrecarga = coFuentePrecarga;
	}

	public String getDeFuentePrecarga() {
		return deFuentePrecarga;
	}

	public void setDeFuentePrecarga(String deFuentePrecarga) {
		this.deFuentePrecarga = deFuentePrecarga;
	}

	public String getMotivoBaja() {
		return motivoBaja;
	}

	public void setMotivoBaja(String motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public String getObservacionBaja() {
		return observacionBaja;
	}

	public void setObservacionBaja(String observacionBaja) {
		this.observacionBaja = observacionBaja;
	}

	public String getCodigoPadronNominal() {
		return codigoPadronNominal;
	}

	public void setCodigoPadronNominal(String codigoPadronNominal) {
		this.codigoPadronNominal = codigoPadronNominal;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getCnv() {
		return cnv;
	}

	public void setCnv(String cnv) {
		this.cnv = cnv;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getCodigoGenero() {
		return codigoGenero;
	}

	public void setCodigoGenero(String codigoGenero) {
		this.codigoGenero = codigoGenero;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

	public String getHoraNacimiento() {	return horaNacimiento;	}

	public void setHoraNacimiento(String horaNacimiento) {this.horaNacimiento = horaNacimiento;	}

	public String getUbigeo() {
		return ubigeo;
	}

	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}

	public String getCodigoUbigeo() {
		return codigoUbigeo;
	}

	public void setCodigoUbigeo(String codigoUbigeo) {
		this.codigoUbigeo = codigoUbigeo;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getDescUbigeo() {
		return descUbigeo;
	}

	public void setDescUbigeo(String descUbigeo) {
		this.descUbigeo = descUbigeo;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCodigoGradoInstruccion() {
		return codigoGradoInstruccion;
	}

	public void setCodigoGradoInstruccion(String codigoGradoInstruccion) {
		this.codigoGradoInstruccion = codigoGradoInstruccion;
	}

	public String getPadreTipoVinculo() {
		return padreTipoVinculo;
	}

	public void setPadreTipoVinculo(String padreTipoVinculo) {
		this.padreTipoVinculo = padreTipoVinculo;
	}

	public String getPadreCodigoTipoVinculo() {
		return padreCodigoTipoVinculo;
	}

	public void setPadreCodigoTipoVinculo(String padreCodigoTipoVinculo) {
		this.padreCodigoTipoVinculo = padreCodigoTipoVinculo;
	}

	public String getPadreDni() {
		return padreDni;
	}

	public void setPadreDni(String padreDni) {
		this.padreDni = padreDni;
	}

	public String getPadrePrimerApellido() {
		return padrePrimerApellido;
	}

	public void setPadrePrimerApellido(String padrePrimerApellido) {
		this.padrePrimerApellido = padrePrimerApellido;
	}

	public String getPadreSegundoApellido() {
		return padreSegundoApellido;
	}

	public void setPadreSegundoApellido(String padreSegundoApellido) {
		this.padreSegundoApellido = padreSegundoApellido;
	}

	public String getPadreNombres() {
		return padreNombres;
	}

	public void setPadreNombres(String padreNombres) {
		this.padreNombres = padreNombres;
	}

	public String getMadreTipoVinculo() {
		return madreTipoVinculo;
	}

	public void setMadreTipoVinculo(String madreTipoVinculo) {
		this.madreTipoVinculo = madreTipoVinculo;
	}

	public String getMadreCodigoTipoVinculo() {
		return madreCodigoTipoVinculo;
	}

	public void setMadreCodigoTipoVinculo(String madreCodigoTipoVinculo) {
		this.madreCodigoTipoVinculo = madreCodigoTipoVinculo;
	}

	public String getMadreDni() {
		return madreDni;
	}

	public void setMadreDni(String madreDni) {
		this.madreDni = madreDni;
	}

	public String getMadrePrimerApellido() {
		return madrePrimerApellido;
	}

	public void setMadrePrimerApellido(String madrePrimerApellido) {
		this.madrePrimerApellido = madrePrimerApellido;
	}

	public String getMadreSegundoApellido() {
		return madreSegundoApellido;
	}

	public void setMadreSegundoApellido(String madreSegundoApellido) {
		this.madreSegundoApellido = madreSegundoApellido;
	}

	public String getMadreNombres() {
		return madreNombres;
	}

	public void setMadreNombres(String madreNombres) {
		this.madreNombres = madreNombres;
	}

	public String getMadreGradoInstruccion() {
		return madreGradoInstruccion;
	}

	public void setMadreGradoInstruccion(String madreGradoInstruccion) {
		this.madreGradoInstruccion = madreGradoInstruccion;
	}

	public String getMadreCodigoGradoInstruccion() {
		return madreCodigoGradoInstruccion;
	}

	public void setMadreCodigoGradoInstruccion(String madreCodigoGradoInstruccion) {
		this.madreCodigoGradoInstruccion = madreCodigoGradoInstruccion;
	}

	public BaseDatosOrigen getBaseDatosOrigen() {
		return baseDatosOrigen;
	}

	public void setBaseDatosOrigen(BaseDatosOrigen baseDatosOrigen) {
		this.baseDatosOrigen = baseDatosOrigen;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getEdadEscrita() {
		return edadEscrita;
	}

	public void setEdadEscrita(String edadEscrita) {
		this.edadEscrita = edadEscrita;
	}

	public OrigenFoto getOrigenFoto() {
		return origenFoto;
	}

	public void setOrigenFoto(OrigenFoto origenFoto) {
		this.origenFoto = origenFoto;
	}

	public String getEsPadron() {
		return esPadron;
	}

	public void setEsPadron(String esPadron) {
		this.esPadron = esPadron;
	}

    public String getCoRestri() {
        return coRestri;
    }

    public String getDeRestri() {
        return deRestri;
    }

    public void setCoRestri(String coRestri) {
        this.coRestri = coRestri;
    }

    public void setDeRestri(String deRestri) {
        this.deRestri = deRestri;
    }

    public String getCasadaApellido() {
        return casadaApellido;
    }

    public void setCasadaApellido(String casadaApellido) {
        this.casadaApellido = casadaApellido;
    }

    public String getNuCui(){
        return nuCui;
    }

    public void setNuCui(String nuCui) {
        this.nuCui = nuCui;
    }

    public String getCodigoCentroPoblado() {
        return codigoCentroPoblado;
    }

    public void setCodigoCentroPoblado(String codigoCentroPoblado) {
        this.codigoCentroPoblado = codigoCentroPoblado;
    }

    public String getDeCentroPoblado() {
        return deCentroPoblado;
    }

    public void setDeCentroPoblado(String deCentroPoblado) {
        this.deCentroPoblado = deCentroPoblado;
    }

    public String getMadreTiDoc() {
        return madreTiDoc;
    }

    public void setMadreTiDoc(String madreTiDoc) {
        this.madreTiDoc = madreTiDoc;
    }


    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

	public String getMadreGradoInstruccionCnv() {
		return madreGradoInstruccionCnv;
	}

	public void setMadreGradoInstruccionCnv(String madreGradoInstruccionCnv) {
		this.madreGradoInstruccionCnv = madreGradoInstruccionCnv;
	}


    public String getMadreGradoInstruccionReniec() {
        return madreGradoInstruccionReniec;
    }

    public void setMadreGradoInstruccionReniec(String madreGradoInstruccionReniec) {
        this.madreGradoInstruccionReniec = madreGradoInstruccionReniec;
    }

	public String getCoEstSaludNac() {
		return coEstSaludNac;
	}

	public void setCoEstSaludNac(String coEstSaludNac) {
		this.coEstSaludNac = coEstSaludNac;
	}

	public String getDeEstSaludNac() {
		return deEstSaludNac;
	}

	public void setDeEstSaludNac(String deEstSaludNac) {
		this.deEstSaludNac = deEstSaludNac;
	}


	public String getCodigoUbigeoReniec() {
		return codigoUbigeoReniec;
	}

	public void setCodigoUbigeoReniec(String codigoUbigeoReniec) {
		this.codigoUbigeoReniec = codigoUbigeoReniec;
	}

	public String getNuSecuenciaLocalNac() {
		return nuSecuenciaLocalNac;
	}

	public void setNuSecuenciaLocalNac(String nuSecuenciaLocalNac) {
		this.nuSecuenciaLocalNac = nuSecuenciaLocalNac;
	}


	public String getDeVia() {
		return deVia;
	}

	public void setDeVia(String deVia) {
		this.deVia = deVia;
	}

	public String getDeRefDir() {
		return deRefDir;
	}

	public void setDeRefDir(String deRefDir) {
		this.deRefDir = deRefDir;
	}

	public String getDeAreaCcpp() {
		return deAreaCcpp;
	}

	public void setDeAreaCcpp(String deAreaCcpp) {
		this.deAreaCcpp = deAreaCcpp;
	}


	public String getTipoFuente() {
		return tipoFuente;
	}

	public void setTipoFuente(String tipoFuente) {
		this.tipoFuente = tipoFuente;
	}

	public String getNuTelefono() {
		return nuTelefono;
	}

	public void setNuTelefono(String nuTelefono) {
		this.nuTelefono = nuTelefono;
	}

	public String getDeEmail() {
		return deEmail;
	}

	public void setDeEmail(String deEmail) {
		this.deEmail = deEmail;
	}

	@Override
    public String toString() {
        return "Persona{" +
                "primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", nombres='" + nombres + '\'' +
                ", codigoGenero='" + codigoGenero + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", genero='" + genero + '\'' +
                ", baseDatosOrigen=" + baseDatosOrigen +
				", madreGradoInstruccionCnv=" + madreGradoInstruccionCnv +
				", nuTelefono=" + nuTelefono +
				", deEmail=" + deEmail +
				", coFuentePrecarga=" + coFuentePrecarga +
				", deFuentePrecarga=" + deFuentePrecarga +
                ", deCargaRegistro=" + deCargaRegistro +
				", coTipoEntidad=" + coTipoEntidad +
				", coEntidadEESS=" + coEntidadEESS +
                '}';
    }
}
