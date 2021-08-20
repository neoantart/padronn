package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.reniec.padronn.logic.web.validator.constraints.DateFormat;
import pe.gob.reniec.padronn.logic.web.validator.checks.TiProSocial;
import pe.gob.reniec.padronn.logic.web.validator.constraints.Cui;
import pe.gob.reniec.padronn.logic.web.validator.constraints.*;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@InformacionAdicionalVal
@MenorVisitado
@UbigeoIneiJurisdiccion
@PadronTitularFamilia
@PadronMadreFamilia
@PadronApoderado
@UbigeoCentroPoblado
@PadronApSegundoMenor
@NuCelular
public class PadronNominal implements Serializable{

	private static final int CO_EST_SALUD_LENGTH = 8;
	private static final int NU_SECUENCIA_LOCAL_LENGTH = 3;
	private static final int MAX_LENTH_STR_TI_PRO_SOCIAL = 40;

	@CoPadronNominal
	private String coPadronNominal; //codigo de registro padrón Nominal

	private Integer nuSec;

	private String coRenaes;

	private String coEntidad;

	private String deEntidad;

	private String coTipoEntidad;

	@Dni
	private String nuDniMenor; //numero de dni del menor

	@Cui
	String nuCui;

	private String nuCnv;

	private String isPrecarga;

	@NotEmpty
	@Length(max=40)
	@NameCharacters
	private String apPrimerMenor; //primer apellido del menor

	/*@NotEmpty*/
	@Length(min=0, max=40)
	@NameCharacters
	private String apSegundoMenor; //ap_segundo_menor

	@NotEmpty
	@Length(max=60)
	@NameCharacters
	private String prenombreMenor; //prenombres del menor

	@NotEmpty
	@DateFormat
	@LessThanAge(years = 6)
	@DateBirth
	private String feNacMenor; //fecha de nacimiento del menor

	@NotEmpty
	@Length(max=100)
	private String deDireccion; //direccion del menor

	@NotEmpty
	private String coGeneroMenor; //genero del menor

	private String coNivelPobreza; //nivel de pobreza del menor

	private String coEtnia;

	@Length(max=NU_SECUENCIA_LOCAL_LENGTH)
	@Pattern(
			regexp = "\\d{0,3}",
			message = "Contiene caracteres no válidos")
	private String nuSecuenciaLocal;

	private String nuSecuenciaLocalNac;

	private String nuSecuenciaLocalAds;
//	@NotEmpty
	@Length(max=CO_EST_SALUD_LENGTH, message = "Codigo de establecimiento de salud invalido ")
	@Pattern(
			regexp = "\\d{0,8}",
			message = "Contiene caracteres no válidos")
	private String coEstSalud; //codigo de establecimiento de salud

	/*@NotEmpty*/
	@Length(max=CO_EST_SALUD_LENGTH, message = "Codigo de establecimiento de salud invalido ")
	@Pattern(
			regexp = "\\d{0,8}",
			message = "Contiene caracteres no válidos")
	private String coEstSaludNac;

	/*@NotEmpty*/
	@Length(max=CO_EST_SALUD_LENGTH, message = "Codigo de establecimiento de salud invalido ")
	@Pattern(
			regexp = "\\d{0,8}",
			message = "Contiene caracteres no válidos")
	private String coEstSaludAds;

/*
	@NotEmpty
	private String tiSeguroMenor; //tipo de seguro del menor
*/

	private String nuAfiliacion; //numero de afiliacion al seguro

	/*
	@Pattern(
			regexp = "[\\[\\] 0123456789,\\\"\\\\]*",
			message = "Contiene caracteres no válidos")
	@Length(max=MAX_LENTH_STR_TI_PRO_SOCIAL)
	*/

	/*@NotNull(message = "El tipo de seguro es requerido")*/
	private List<String> tiSeguroMenor;

	private List<Dominio> padronSeguroList;

	@TiProSocial
	private List<String> tiProSocial; //tipo de programa social afiliado

	private List<Dominio> padronProgramaList; //tipo de programa social afiliado

	private String coInstEducativa; //codigo de institucion educativa

	private String coFrecAtencion; //codigo de institucion educativa

	private String deFrecAtencion;

	private String tiVinculoJefe; //tipo de vinculo del jefe de familia

	@Dni
	private String coDniJefeFam; //dni del jefe de familia

	@Length(max=40)
	@NameCharacters
	private String apPrimerJefe; //primer apellido del jefe de familia

	@Length(min=0, max=40)
	@NameCharacters
	private String apSegundoJefe; //segundo apellido del jefe de familia

	@Length(max=60)
	@NameCharacters
	private String prenomJefe; //prenombre del jefe de familia

	//@NotEmpty
	private String tiVinculoMadre; //tipo de vinculo de la madre o apoderado

	//@NotEmpty
	@Dni
	//@EdadMinMadre
	private String coDniMadre; //dni de la madre o apoderado

	/*@NotEmpty*/
	@Length(max=40)
	@NameCharacters
	private String apPrimerMadre; //primer apellido de la madre o apoderado

	/*@NotEmpty*/
	@Length(max=40)
	@NameCharacters
	private String apSegundoMadre; //segundo apellido de la madre o apoderado

	/*@NotEmpty*/
	@Length(max=60)
	@NameCharacters
	private String prenomMadre; //prenombre de la madre o apoderado

	/*@NotEmpty*/
	private String coGraInstMadre; //grado de instruccion de la madre o apoderado

	/*@NotEmpty*/
	private String coLenMadre; //lengua habitual de la madre o apoderado

	public String getNuCelMadre() {
		return nuCelMadre;
	}

	public void setNuCelMadre(String nuCelMadre) {
		this.nuCelMadre = nuCelMadre;
	}

	public String getDiCorreoMadre() {
		return diCorreoMadre;
	}

	public void setDiCorreoMadre(String diCorreoMadre) {
		this.diCorreoMadre = diCorreoMadre;
	}

	private String nuCelMadre;

	private String diCorreoMadre;

	@NotEmpty
	private String coUbigeoInei;

	@NotEmpty
	private String coCentroPoblado;

	private String deCentroPoblado;

	private String fechaBaja;

	private String deMotivoBaja;

	private String deObservacionBaja;

	private String deUsuarioBaja;

	private String coMotivoBaja;

	private String esPadron;

	private String motivoBaja;

	private String observacionBaja;

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

    //	@DateFuture(message = "Fecha de visita no debe ser futura")
//	@DateFormat(message = "Fecha inicial inválida")
//	@NotEmpty
	private String feVisita;
//	@NotEmpty
	private String coMenorEncontrado;
	private String deMenorEncontrado;
	private String feVisitaBefore; 	//Para validacion
	private String inMenorVisitado;
	private String deMenorVisitado;
	private String inMenorVisitadoBefore; //Para validacion
	private String coFuenteDatos;
	private String deFuenteDatos;
	private String feUltimaTomaDatos;
	private String feUltimaTomaDatosBefore; //Para validacion

	//para consulta por tipos de EESS y edad modificado por Jose Vidal Flores
	private String coUbigeoPad;
	private String deUbigeoPad;
	/*
	Campos complementarios
	 */
	private String deGeneroMenor;
	private String deVinculoJefe;
	private String deVinculoMadre;
	private String edad;
	private String nuEdad;
	private String nuEdadMeses;
	private String edadEscrita;
	//private String deUbigeoInei;

	private String deNivelPobreza;
	private String deEstSalud;

	private String noEstSalud;

	/*private String deSeguroMenor;*/
	private String deInstEducativa;
	private String noInstEducativa;
	private String deGraInstMadre;
	private String deLenMadre;

	private String deUbigeoInei;

	private Persona.OrigenFoto origenFoto;

	/*
	Campos de auditoria
	 */

	private String usCreaRegistro; //usuario crea registro
	private String feCreaRegistro; //fecha de creacion registro
	private String usModiRegistro; //usuario modifica registro
	private String feModiRegistro; //fecha modifica registro
	private String coEstado;

	private String usCreaRegistroMidis; //usuario crea registro sin modificaciones para consulta MIDIS
	private String usModiRegistroMidis; //usuario modifica registro sin modificaciones para consulta MIDIS

	/*
        Restricciones : Fallecimiento, DOBLE NACIONALIDAD, ....
    */
	private String coRestri;
	private String deRestri;

	private String deDepartamento;
	private String deProvincia;
	private String deDistrito;

	private String coProgramasSociales;

	private String coTipoSeguros;
	// para los reportes
	private String coDocumentoIdentidad;
	private String coModular;

	private String ninguno;
	private String pvl;
	private String cuna;
	private String juntos;
	private String qaliwarma;
	private String cunamasscd;
	private String cunamassaf;


	public enum BaseDatosOrigen{
		ANI,
		PN,
		CNV
	}

	BaseDatosOrigen baseDatosOrigen;

	/* Funciones auxiliares */

	private String fila;

	private String deDireccionDomReniec;

	private String deUbigeoDomReniec;

	private String deDireccionDomCnv;

	private String deUbigeoDomCnv;

	private byte[] imagen;

	private String deGraInstMadreCnv;

	private String deGraInstMadreReniec;

	public String getDeEstSaludAds() {
		return deEstSaludAds;
	}

	public void setDeEstSaludAds(String deEstSaludAds) {
		this.deEstSaludAds = deEstSaludAds;
	}

	private String deEstSaludAds;

	private String deEstSaludNac;

	public String getIsPrecarga() {
		return isPrecarga;
	}

	public void setIsPrecarga(String isPrecarga) {
		this.isPrecarga = isPrecarga;
	}

	private String deTipoRegistro;

	private String coVia;

	private String deRefDir;

	private String deVia;

	private String coAreaCcpp;

	private String deAreaCcpp;

	private String coFuentePrecarga;

	private String deFuentePrecarga;

	private String deCargaRegistro;//solo para el reporte de rango de fechas
	// ANI, PN, campo para realizar las busqueda
	private String tipoFuente;

	private String coEntidadEESS;

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

	public static PadronNominal fromPersona(Persona persona) {
		PadronNominal padronNominal=new PadronNominal();
		padronNominal.setNuDniMenor(persona.getDni());
		padronNominal.setCoPadronNominal(persona.getCodigoPadronNominal());
		padronNominal.setNuCnv(persona.getCnv());

		padronNominal.setApPrimerMenor(persona.getPrimerApellido());
		padronNominal.setApSegundoMenor(persona.getSegundoApellido());
		padronNominal.setPrenombreMenor(persona.getNombres());
		padronNominal.setCoGeneroMenor(persona.getCodigoGenero());

		padronNominal.setFeNacMenor(persona.getFechaNacimiento());
		padronNominal.setDeDireccion(persona.getDireccion());

		padronNominal.setTiVinculoJefe(persona.getPadreCodigoTipoVinculo());
		padronNominal.setDeVinculoJefe(persona.getPadreTipoVinculo());
		padronNominal.setCoDniJefeFam(persona.getPadreDni());
		padronNominal.setApPrimerJefe(persona.getPadrePrimerApellido());
		padronNominal.setApSegundoJefe(persona.getPadreSegundoApellido());
		padronNominal.setPrenomJefe(persona.getPadreNombres());

		padronNominal.setDeVinculoMadre(persona.getMadreTipoVinculo());
		padronNominal.setTiVinculoMadre(persona.getMadreCodigoTipoVinculo());
		padronNominal.setCoDniMadre(persona.getMadreDni());
		padronNominal.setApPrimerMadre(persona.getMadrePrimerApellido());
		padronNominal.setApSegundoMadre(persona.getMadreSegundoApellido());
		padronNominal.setPrenomMadre(persona.getMadreNombres());
		padronNominal.setCoGraInstMadre(persona.getMadreCodigoGradoInstruccion());
		padronNominal.setDeGraInstMadre(persona.getMadreGradoInstruccion());

		/* campos complementarios */
		padronNominal.setEdad(persona.getEdad());
		padronNominal.setEdadEscrita(persona.getEdadEscrita());
		padronNominal.setDeGeneroMenor(persona.getGenero());
		padronNominal.setCoUbigeoInei(persona.getCodigoUbigeo());

		padronNominal.setOrigenFoto(persona.getOrigenFoto());

		padronNominal.setDeFuentePrecarga(persona.getDeFuentePrecarga());

		padronNominal.setCoFuentePrecarga(persona.getCoFuentePrecarga());

		padronNominal.setDeCargaRegistro(persona.getDeCargaRegistro());

		padronNominal.setCoTipoEntidad(persona.getCoTipoEntidad());

		padronNominal.setCoEntidadEESS(persona.getCoEntidadEESS());

		if (persona.getUbigeo() != null) {
			padronNominal.setDeUbigeoDomReniec(persona.getUbigeo());
		}

		if (persona.getDireccion() != null) {
			padronNominal.setDeDireccionDomReniec(persona.getDireccion());
		}

		if (persona.getCnv()!=null) {
			if (persona.getDireccion() != null) {
				padronNominal.setDeDireccionDomCnv(persona.getDireccion());
			}
		}

		if (persona.getMadreGradoInstruccionCnv() != null) {
			padronNominal.setDeGraInstMadreCnv(persona.getMadreGradoInstruccionCnv());
		}

		if (persona.getMadreGradoInstruccionReniec() != null) {
			padronNominal.setDeGraInstMadreReniec(persona.getMadreGradoInstruccionReniec());
		}

//        padronNominal.setBaseDatosOrigen(PadronNominal.BaseDatosOrigen.ANI);

		/* campos de restriccion*/
		if(persona != null || !"".equals(persona.getCoRestri().trim())) {
			padronNominal.setCoRestri(persona.getCoRestri());
			padronNominal.setDeRestri(persona.getDeRestri());
		}
		// codigo de establecimiento de salud de nacimiento
		if (persona.getCoEstSaludNac() != null) {
			padronNominal.setCoEstSaludNac(persona.getCoEstSaludNac());
			if(persona.getDeEstSaludNac()!=null)
				padronNominal.setDeEstSaludNac(persona.getDeEstSaludNac());

			if (persona.getNuSecuenciaLocalNac() != null) {
				padronNominal.setNuSecuenciaLocalNac(persona.getNuSecuenciaLocalNac());
			} else {
				padronNominal.setNuSecuenciaLocalNac(persona.getNuSecuenciaLocalNac());
			}
		}


		return padronNominal;
	}


	@FileType(types={"image/jpeg", "image/gif", "image/png"})
	@FileSize(max=1024*1024)
	private MultipartFile imgFotoMenor;

	private String tiDocIdentidad; //tipo de documento de identidad del menor

	public MultipartFile getImgFotoMenor() {
		return imgFotoMenor;
	}

	public void setImgFotoMenor(MultipartFile imgFotoMenor) {
		this.imgFotoMenor = imgFotoMenor;
	}

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

	public String getNuCnv() {
		return nuCnv;
	}

	public void setNuCnv(String nuCnv) {
		this.nuCnv = nuCnv;
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

	public String getCoEstSalud() {
		return coEstSalud;
	}

	public void setCoEstSalud(String coEstSalud) {
		this.coEstSalud = coEstSalud;
	}

	public String getCoGeneroMenor() {
		return coGeneroMenor;
	}

	public void setCoGeneroMenor(String coGeneroMenor) {
		this.coGeneroMenor = coGeneroMenor;
	}

	public String getTiDocIdentidad() {
		return tiDocIdentidad;
	}

	public void setTiDocIdentidad(String tiDocIdentidad) {
		this.tiDocIdentidad = tiDocIdentidad;
	}

	public String getDeDireccion() {
		return deDireccion;
	}


	public void setDeDireccion(String deDireccion) {
		this.deDireccion = deDireccion;
	}

/*	public String getTiSeguroMenor() {
		return tiSeguroMenor;
	}

	public void setTiSeguroMenor(String tiSeguroMenor) {
		this.tiSeguroMenor = tiSeguroMenor;
	}*/

	public String getNuAfiliacion() {
		return nuAfiliacion;
	}

	public void setNuAfiliacion(String nuAfiliacion) {
		this.nuAfiliacion = nuAfiliacion;
	}

	public List<String> getTiProSocial() {
		return tiProSocial;
	}

	public void setTiProSocial(List<String> tiProSocial) {
		this.tiProSocial = tiProSocial;
	}

	public String getCoInstEducativa() {
		return coInstEducativa;
	}

	public void setCoInstEducativa(String coInstEducativa) {
		this.coInstEducativa = coInstEducativa;
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

	public String getCoGraInstMadre() {
		return coGraInstMadre;
	}

	public void setCoGraInstMadre(String coGraInstMadre) {
		this.coGraInstMadre = coGraInstMadre;
	}

	public String getCoLenMadre() {
		return coLenMadre;
	}

	public void setCoLenMadre(String coLenMadre) {
		this.coLenMadre = coLenMadre;
	}

	public String getCoNivelPobreza() {
		return coNivelPobreza;
	}

	public void setCoNivelPobreza(String coNivelPobreza) {
		this.coNivelPobreza = coNivelPobreza;
	}

	public String getUsCreaRegistro() {
		return usCreaRegistro;
	}

	public void setUsCreaRegistro(String usCreaRegistro) {
		this.usCreaRegistro = usCreaRegistro;
	}

	public String getFeCreaRegistro() {
		return feCreaRegistro;
	}

	public void setFeCreaRegistro(String feCreaRegistro) {
		this.feCreaRegistro = feCreaRegistro;
	}

	public String getUsModiRegistro() {
		return usModiRegistro;
	}

	public void setUsModiRegistro(String usModiRegistro) {
		this.usModiRegistro = usModiRegistro;
	}

	public String getFeModiRegistro() {
		return feModiRegistro;
	}

	public void setFeModiRegistro(String feModiRegistro) {
		this.feModiRegistro = feModiRegistro;
	}

	public String getUsCreaRegistroMidis() {
		return usCreaRegistroMidis;
	}

	public void setUsCreaRegistroMidis(String usCreaRegistroMidis) {
		this.usCreaRegistroMidis = usCreaRegistroMidis;
	}

	public String getUsModiRegistroMidis() {
		return usModiRegistroMidis;
	}

	public void setUsModiRegistroMidis(String usModiRegistroMidis) {
		this.usModiRegistroMidis = usModiRegistroMidis;
	}

	public Integer getNuSec() {
		return nuSec;
	}

	public void setNuSec(Integer nuSec) {
		this.nuSec = nuSec;
	}

	public String getCoEstado() {
		return coEstado;
	}

	public void setCoEstado(String coEstado) {
		this.coEstado = coEstado;
	}

	public String getCoEntidad() {
		return coEntidad;
	}

	public void setCoEntidad(String coEntidad) {
		this.coEntidad = coEntidad;
	}

	public String getCoEtnia() {
		return coEtnia;
	}

	public void setCoEtnia(String coEtnia) {
		this.coEtnia = coEtnia;
	}

	public String getCoUbigeoInei() {
		return coUbigeoInei;
	}

	public void setCoUbigeoInei(String coUbigeoInei) {
		this.coUbigeoInei = coUbigeoInei;
	}

	public String getDeGeneroMenor() {
		return deGeneroMenor;
	}

	public void setDeGeneroMenor(String deGeneroMenor) {
		this.deGeneroMenor = deGeneroMenor;
	}

	public String getDeVinculoJefe() {
		return deVinculoJefe;
	}

	public void setDeVinculoJefe(String deVinculoJefe) {
		this.deVinculoJefe = deVinculoJefe;
	}

	public String getDeVinculoMadre() {
		return deVinculoMadre;
	}

	public void setDeVinculoMadre(String deVinculoMadre) {
		this.deVinculoMadre = deVinculoMadre;
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

	public String getDeUbigeoInei() {
		return deUbigeoInei;
	}

	public void setDeUbigeoInei(String deUbigeoInei) {
		this.deUbigeoInei = deUbigeoInei;
	}

	public String getDeNivelPobreza() {
		return deNivelPobreza;
	}

	public void setDeNivelPobreza(String deNivelPobreza) {
		this.deNivelPobreza = deNivelPobreza;
	}

	public String getDeEstSalud() {
		return deEstSalud;
	}

	public void setDeEstSalud(String deEstSalud) {
		this.deEstSalud = deEstSalud;
	}

	public String getCoFrecAtencion() {
		return coFrecAtencion;
	}

	public void setCoFrecAtencion(String coFrecAtencion) {
		this.coFrecAtencion = coFrecAtencion;
	}

    public String getDeFrecAtencion() {
        return deFrecAtencion;
    }

    public void setDeFrecAtencion(String deFrecAtencion) {
        this.deFrecAtencion = deFrecAtencion;
    }

	/*public String getDeSeguroMenor() {
		return deSeguroMenor;
	}

	public void setDeSeguroMenor(String deSeguroMenor) {
		this.deSeguroMenor = deSeguroMenor;
	}*/

	public String getDeInstEducativa() {
		return deInstEducativa;
	}

	public void setDeInstEducativa(String deInstEducativa) {
		this.deInstEducativa = deInstEducativa;
	}

	public String getDeGraInstMadre() {
		return deGraInstMadre;
	}

	public void setDeGraInstMadre(String deGraInstMadre) {
		this.deGraInstMadre = deGraInstMadre;
	}

	public String getDeLenMadre() {
		return deLenMadre;
	}

	public void setDeLenMadre(String deLenMadre) {
		this.deLenMadre = deLenMadre;
	}

	public Persona.OrigenFoto getOrigenFoto() {
		return origenFoto;
	}

	public void setOrigenFoto(Persona.OrigenFoto origenFoto) {
		this.origenFoto = origenFoto;
	}

	public List<Dominio> getPadronProgramaList() {
		return padronProgramaList;
	}

	public void setPadronProgramaList(List<Dominio> padronProgramaList) {
		this.padronProgramaList = padronProgramaList;
	}

	public List<String> getTiSeguroMenor() {
		return tiSeguroMenor;
	}

	public void setTiSeguroMenor(List<String> tiSeguroMenor) {
		this.tiSeguroMenor = tiSeguroMenor;
	}

	public List<Dominio> getPadronSeguroList() {
		return padronSeguroList;
	}

	public void setPadronSeguroList(List<Dominio> padronSeguroList) {
		this.padronSeguroList = padronSeguroList;
	}

	public String getCoTipoSeguros() {
		return coTipoSeguros;
	}

	public void setCoTipoSeguros(String coTipoSeguros) {
		this.coTipoSeguros = coTipoSeguros;
	}

	public String getEsPadron() {
		return esPadron;
	}

	public void setEsPadron(String esPadron) {
		this.esPadron = esPadron;
	}

	public String getDeEntidad() {
		return deEntidad;
	}

	public void setDeEntidad(String deEntidad) {
		this.deEntidad = deEntidad;
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

	public String getNuCui() {
		return nuCui;
	}

	public void setNuCui(String nuCui) {
		this.nuCui = nuCui;
	}

	public String getCoCentroPoblado() {
		return coCentroPoblado;
	}

	public void setCoCentroPoblado(String coCentroPoblado) {
		this.coCentroPoblado = coCentroPoblado;
	}

	public String getDeCentroPoblado() {
		return deCentroPoblado;
	}

	public void setDeCentroPoblado(String deCentroPoblado) {
		this.deCentroPoblado = deCentroPoblado;
	}

	public String getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getDeMotivoBaja() {
		return deMotivoBaja;
	}

	public void setDeMotivoBaja(String deMotivoBaja) {
		this.deMotivoBaja = deMotivoBaja;
	}

	public String getDeObservacionBaja() {
		return deObservacionBaja;
	}

	public void setDeObservacionBaja(String deObservacionBaja) {
		this.deObservacionBaja = deObservacionBaja;
	}

	public String getDeUsuarioBaja() {
		return deUsuarioBaja;
	}

	public void setDeUsuarioBaja(String deUsuarioBaja) {
		this.deUsuarioBaja = deUsuarioBaja;
	}

	public String getCoMotivoBaja() {
		return coMotivoBaja;
	}

	public void setCoMotivoBaja(String coMotivoBaja) {
		this.coMotivoBaja = coMotivoBaja;
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

	public String getCoProgramasSociales() {
		return coProgramasSociales;
	}

	public void setCoProgramasSociales(String coProgramasSociales) {
		this.coProgramasSociales = coProgramasSociales;
	}

	public String getCoDocumentoIdentidad() {
		return coDocumentoIdentidad;
	}

	public void setCoDocumentoIdentidad(String coDocumentoIdentidad) { this.coDocumentoIdentidad = coDocumentoIdentidad; }

	public String getNoEstSalud() {
		return noEstSalud;
	}

	public void setNoEstSalud(String noEstSalud) {
		this.noEstSalud = noEstSalud;
	}

	public String getCoModular() {
		return coModular;
	}

	public void setCoModular(String coModular) {
		this.coModular = coModular;
	}

	public String getNoInstEducativa() {
		return noInstEducativa;
	}

	public void setNoInstEducativa(String noInstEducativa) {
		this.noInstEducativa = noInstEducativa;
	}

	public String getFila() {
		return fila;
	}

	public void setFila(String fila) {
		this.fila = fila;
	}

	public String getNuEdad() {
		return nuEdad;
	}

	public void setNuEdad(String nuEdad) {
		this.nuEdad = nuEdad;
	}

	public String getNuEdadMeses() {
		return nuEdadMeses;
	}

	public void setNuEdadMeses(String nuEdadMeses) {
		this.nuEdadMeses = nuEdadMeses;
	}

	public String getPvl() {
		return pvl;
	}

	public void setPvl(String pvl) {
		this.pvl = pvl;
	}

	public String getCuna() {
		return cuna;
	}

	public void setCuna(String cuna) {
		this.cuna = cuna;
	}

	public String getJuntos() {
		return juntos;
	}

	public void setJuntos(String juntos) {
		this.juntos = juntos;
	}

	public String getQaliwarma() {
		return qaliwarma;
	}

	public void setQaliwarma(String qaliwarma) {
		this.qaliwarma = qaliwarma;
	}

	public String getNinguno() {
		return ninguno;
	}

	public void setNinguno(String ninguno) {
		this.ninguno = ninguno;
	}

	public String getCunamasscd() {
		return cunamasscd;
	}

	public void setCunamasscd(String cunamasscd) {
		this.cunamasscd = cunamasscd;
	}

	public String getCunamassaf() {
		return cunamassaf;
	}

	public void setCunamassaf(String cunamassaf) {
		this.cunamassaf = cunamassaf;
	}

	public BaseDatosOrigen getBaseDatosOrigen() {
		return baseDatosOrigen;
	}

	public void setBaseDatosOrigen(BaseDatosOrigen baseDatosOrigen) {
		this.baseDatosOrigen = baseDatosOrigen;
	}

	public String getDeDireccionDomReniec() {
		return deDireccionDomReniec;
	}

	public void setDeDireccionDomReniec(String deDireccionDomReniec) { this.deDireccionDomReniec = deDireccionDomReniec; }

	public String getDeUbigeoDomReniec() {
		return deUbigeoDomReniec;
	}

	public void setDeUbigeoDomReniec(String deUbigeoDomReniec) {
		this.deUbigeoDomReniec = deUbigeoDomReniec;
	}

	public String getDeDireccionDomCnv() {
		return deDireccionDomCnv;
	}

	public void setDeDireccionDomCnv(String deDireccionDomCnv) {
		this.deDireccionDomCnv = deDireccionDomCnv;
	}

	public String getDeUbigeoDomCnv() {
		return deUbigeoDomCnv;
	}

	public void setDeUbigeoDomCnv(String deUbigeoDomCnv) {
		this.deUbigeoDomCnv = deUbigeoDomCnv;
	}

	public byte[] getImagen() { return imagen; }

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getDeGraInstMadreCnv() {
		return deGraInstMadreCnv;
	}

	public void setDeGraInstMadreCnv(String deGraInstMadreCnv) {
		this.deGraInstMadreCnv = deGraInstMadreCnv;
	}

	public String getDeGraInstMadreReniec() {
		return deGraInstMadreReniec;
	}

	public void setDeGraInstMadreReniec(String deGraInstMadreReniec) { this.deGraInstMadreReniec = deGraInstMadreReniec; }

	public String getNuSecuenciaLocal() {
		return nuSecuenciaLocal;
	}

	public void setNuSecuenciaLocal(String nuSecuenciaLocal) {
		this.nuSecuenciaLocal = nuSecuenciaLocal;
	}

	public String getCoEstSaludNac() {
		return coEstSaludNac;
	}

	public String getCoEstSaludAds() {
		return coEstSaludAds;
	}

	public void setCoEstSaludAds(String coEstSaludAds) {
		this.coEstSaludAds = coEstSaludAds;
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

	public String getNuSecuenciaLocalNac() {
		return nuSecuenciaLocalNac;
	}

	public void setNuSecuenciaLocalNac(String nuSecuenciaLocalNac) {
		this.nuSecuenciaLocalNac = nuSecuenciaLocalNac;
	}

	public String getNuSecuenciaLocalAds() {
		return nuSecuenciaLocalAds;
	}

	public void setNuSecuenciaLocalAds(String nuSecuenciaLocalAds) {
		this.nuSecuenciaLocalAds = nuSecuenciaLocalAds;
	}

	public String getDeTipoRegistro() {
		return deTipoRegistro;
	}

	public void setDeTipoRegistro(String deTipoRegistro) {
		this.deTipoRegistro = deTipoRegistro;
	}

	public String getCoVia() {
		return coVia;
	}

	public void setCoVia(String coVia) {
		this.coVia = coVia;
	}

	public String getDeRefDir() {
		return deRefDir;
	}

	public void setDeRefDir(String deRefDir) {
		this.deRefDir = deRefDir;
	}

	public String getDeVia() {
		return deVia;
	}

	public void setDeVia(String deVia) {
		this.deVia = deVia;
	}

	public String getCoAreaCcpp() {
		return coAreaCcpp;
	}

	public void setCoAreaCcpp(String coAreaCcpp) {
		this.coAreaCcpp = coAreaCcpp;
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

	//Agregado por Jose Vidal Flores
	public String getCoUbigeoPad() {
		return coUbigeoPad;
	}

	public void setCoUbigeoPad(String coUbigeoPad) {
		this.coUbigeoPad = coUbigeoPad;
	}

	public String getDeUbigeoPad() {
		return deUbigeoPad;
	}

	public void setDeUbigeoPad(String deUbigeoPad) {
		this.deUbigeoPad = deUbigeoPad;
	}

	public String getFeVisita() {
		return feVisita;
	}

	public void setFeVisita(String feVisita) {
		this.feVisita = feVisita;
	}

	public String getCoMenorEncontrado() {
		return coMenorEncontrado;
	}

	public void setCoMenorEncontrado(String coMenorEncontrado) {
		this.coMenorEncontrado = coMenorEncontrado;
	}

	public String getDeMenorEncontrado() {
		return deMenorEncontrado;
	}

	public void setDeMenorEncontrado(String deMenorEncontrado) {
		this.deMenorEncontrado = deMenorEncontrado;
	}

	public String getFeVisitaBefore() {
		return feVisitaBefore;
	}

	public void setFeVisitaBefore(String feVisitaBefore) {
		this.feVisitaBefore = feVisitaBefore;
	}

	public String getInMenorVisitado() {
		return inMenorVisitado;
	}

	public void setInMenorVisitado(String inMenorVisitado) {
		this.inMenorVisitado = inMenorVisitado;
	}

	public String getDeMenorVisitado() {
		return deMenorVisitado;
	}

	public void setDeMenorVisitado(String deMenorVisitado) {
		this.deMenorVisitado = deMenorVisitado;
	}

	public String getInMenorVisitadoBefore() {
		return inMenorVisitadoBefore;
	}

	public void setInMenorVisitadoBefore(String inMenorVisitadoBefore) {
		this.inMenorVisitadoBefore = inMenorVisitadoBefore;
	}

	public String getCoFuenteDatos() {
		return coFuenteDatos;
	}

	public void setCoFuenteDatos(String coFuenteDatos) {
		this.coFuenteDatos = coFuenteDatos;
	}

	public String getDeFuenteDatos() {
		return deFuenteDatos;
	}

	public void setDeFuenteDatos(String deFuenteDatos) {
		this.deFuenteDatos = deFuenteDatos;
	}

	public String getFeUltimaTomaDatos() {
		return feUltimaTomaDatos;
	}

	public void setFeUltimaTomaDatos(String feUltimaTomaDatos) {
		this.feUltimaTomaDatos = feUltimaTomaDatos;
	}

	public String getFeUltimaTomaDatosBefore() {
		return feUltimaTomaDatosBefore;
	}

	public void setFeUltimaTomaDatosBefore(String feUltimaTomaDatosBefore) {
		this.feUltimaTomaDatosBefore = feUltimaTomaDatosBefore;
	}

	public String getDeFuentePrecarga() {
		return deFuentePrecarga;
	}

	public void setDeFuentePrecarga(String deFuentePrecarga) {
		this.deFuentePrecarga = deFuentePrecarga;
	}

	public String getCoFuentePrecarga() {
		return coFuentePrecarga;
	}

	public void setCoFuentePrecarga(String coFuentePrecarga) {
		this.coFuentePrecarga = coFuentePrecarga;
	}


	public String getDeCargaRegistro() {
		return deCargaRegistro;
	}

	public void setDeCargaRegistro(String deCargaRegistro) {
		this.deCargaRegistro = deCargaRegistro;
	}

	public String getCoRenaes() {
		return coRenaes;
	}

	public void setCoRenaes(String coRenaes) {
		this.coRenaes = coRenaes;
	}

	@Override
	public String toString() {
		return "PadronNominal{" +
				"coPadronNominal='" + coPadronNominal + '\'' +
				", nuSec=" + nuSec +
				", coEntidad='" + coEntidad + '\'' +
				", deEntidad='" + deEntidad + '\'' +
				", nuDniMenor='" + nuDniMenor + '\'' +
				", nuCui='" + nuCui + '\'' +
				", nuCnv='" + nuCnv + '\'' +
				", apPrimerMenor='" + apPrimerMenor + '\'' +
				", apSegundoMenor='" + apSegundoMenor + '\'' +
				", prenombreMenor='" + prenombreMenor + '\'' +
				", feNacMenor='" + feNacMenor + '\'' +
				", deDireccion='" + deDireccion + '\'' +
				", coGeneroMenor='" + coGeneroMenor + '\'' +
				", coNivelPobreza='" + coNivelPobreza + '\'' +
				", coEtnia='" + coEtnia + '\'' +
				", coEstSalud='" + coEstSalud + '\'' +
				", tiSeguroMenor='" + tiSeguroMenor + '\'' +
				", nuAfiliacion='" + nuAfiliacion + '\'' +
				", tiProSocial=" + tiProSocial +
				", padronProgramaList=" + padronProgramaList +
				", coInstEducativa='" + coInstEducativa + '\'' +
				", tiVinculoJefe='" + tiVinculoJefe + '\'' +
				", coDniJefeFam='" + coDniJefeFam + '\'' +
				", apPrimerJefe='" + apPrimerJefe + '\'' +
				", apSegundoJefe='" + apSegundoJefe + '\'' +
				", prenomJefe='" + prenomJefe + '\'' +
				", tiVinculoMadre='" + tiVinculoMadre + '\'' +
				", coDniMadre='" + coDniMadre + '\'' +
				", apPrimerMadre='" + apPrimerMadre + '\'' +
				", apSegundoMadre='" + apSegundoMadre + '\'' +
				", prenomMadre='" + prenomMadre + '\'' +
				", coGraInstMadre='" + coGraInstMadre + '\'' +
				", coLenMadre='" + coLenMadre + '\'' +
				", nuCelMadre='" + nuCelMadre + '\'' +
				", diCorreoMadre='" + diCorreoMadre + '\'' +
				", coUbigeoInei='" + coUbigeoInei + '\'' +
				", coCentroPoblado='" + coCentroPoblado + '\'' +
				", deCentroPoblado='" + deCentroPoblado + '\'' +
				", fechaBaja='" + fechaBaja + '\'' +
				", deMotivoBaja='" + deMotivoBaja + '\'' +
				", deObservacionBaja='" + deObservacionBaja + '\'' +
				", deUsuarioBaja='" + deUsuarioBaja + '\'' +
				", coMotivoBaja='" + coMotivoBaja + '\'' +
				", esPadron='" + esPadron + '\'' +
				", feVisita='" + feVisita + '\'' +
				", coMenorEncontrado='" + coMenorEncontrado + '\'' +
				", deMenorEncontrado='" + deMenorEncontrado + '\'' +
				", feVisitaBefore='" + feVisitaBefore + '\'' +
				", inMenorVisitado='" + inMenorVisitado + '\'' +
				", deMenorVisitado='" + deMenorVisitado + '\'' +
				", inMenorVisitadoBefore='" + inMenorVisitadoBefore + '\'' +
				", coFuenteDatos='" + coFuenteDatos + '\'' +
				", deFuenteDatos='" + deFuenteDatos + '\'' +
				", coUbigeoPad='" + coUbigeoPad + '\'' +
				", deUbigeoPad='" + deUbigeoPad + '\'' +
				", deGeneroMenor='" + deGeneroMenor + '\'' +
				", deVinculoJefe='" + deVinculoJefe + '\'' +
				", deVinculoMadre='" + deVinculoMadre + '\'' +
				", edad='" + edad + '\'' +
				", edadEscrita='" + edadEscrita + '\'' +
				", deNivelPobreza='" + deNivelPobreza + '\'' +
				", coFuentePrecarga='" + coFuentePrecarga + '\'' +
				", deFuentePrecarga='" + deFuentePrecarga + '\'' +
				", deEstSalud='" + deEstSalud + '\'' +
		/*		", deSeguroMenor='" + deSeguroMenor + '\'' +*/
				", deInstEducativa='" + deInstEducativa + '\'' +
				", deGraInstMadre='" + deGraInstMadre + '\'' +
				", deLenMadre='" + deLenMadre + '\'' +
				", deUbigeoInei='" + deUbigeoInei + '\'' +
				", origenFoto=" + origenFoto +
				", usCreaRegistro='" + usCreaRegistro + '\'' +
				", feCreaRegistro='" + feCreaRegistro + '\'' +
				", usModiRegistro='" + usModiRegistro + '\'' +
				", feModiRegistro='" + feModiRegistro + '\'' +
				", coEstado='" + coEstado + '\'' +
				", coRestri='" + coRestri + '\'' +
				", deRestri='" + deRestri + '\'' +
				", imgFotoMenor=" + imgFotoMenor +
				", tiDocIdentidad='" + tiDocIdentidad + '\'' +
				", deCargaRegistro='" + deCargaRegistro + '\'' +
				", coTipoEntidad='" + coTipoEntidad + '\'' +
				", coEntidadEESS='" + coEntidadEESS + '\'' +
				'}';
	}

}
