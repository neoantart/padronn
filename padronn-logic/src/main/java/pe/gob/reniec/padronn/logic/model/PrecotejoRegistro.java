package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import pe.gob.reniec.padronn.logic.web.validator.checks.*;
import pe.gob.reniec.padronn.logic.web.validator.constraints.CentroPobladoConstraint;

import javax.validation.constraints.Pattern;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Clase PrecotejoRegistro.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 04:09 PM
 */

@CentroPobladoConstraint(groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
public class PrecotejoRegistro
        implements Serializable {

	// *** Datos internos de app ***
	String coEntidad;
	Number nuEnvio;
	String deObservacion = "";
	String usCreaRegistro = "";
	String usModiRegistro = "";
	String coNivelPobreza = "";
	String coEtnia = "";
	String nuAfiliacion = "";
	String deObservacionCotejo = "";
	String cancelAction = "";
	String successAction = "";

	// *** Datos enviados en el registro ***
	String no = "";

	@NameCheck(
			message = "Contiene caracteres o secuencias no válidas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres, o estar vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String apPrimerMenor;


	@NameCheck(
			message = "Contiene caracteres o secuencias no válidas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres, o estar vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String apSegundoMenor;


	@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@NameCheck(
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String prenombreMenor;


	@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@CoDominioCheck(
			value = CoDominio.coGeneroMenor,
			message = "Debe tener un código válido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String coGeneroMenor = "";
	String deGeneroMenor;


	@NotEmpty(
			message = "Requerido",
			groups = {MinimalPrecotejoRegistroPriorInsertChecks.class})
	@Pattern(
			regexp = "^[^\\<\\>]*$",
			message = "Contiene caracteres no válidos",
			groups = {MinimalPrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 2,
			max = 100,
			message = "Debe tener entre 2 y 100 caracteres",
			groups = {MinimalPrecotejoRegistroPriorInsertChecks.class})
	String deDireccion;


	// formato d/m/yy 29/07/95
	//@Pattern(regexp="^(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)?\\d\\d)$") //fecha dd/mm/yy ó dd/mm/yyyy
	@NotEmpty(
			message = "Es requerida",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@DateCheck(
			message = "Debe ser una fecha válida y tener formato dd/mm/aaaa",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
/*	@AgeCheck(
			min = 0,
			max = 6,
			message = "Sólo de 0 a 6 años",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})*/
	@Pattern(
        regexp = "^(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)$",
        message = "Debe tener formato dd/mm/aaaa",
        groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class}
    )
    String feNacMenor;


	@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@CoDominioCheck(
			value = CoDominio.tiDocIdentidad,
			message = "Debe tener un código válido -verifique formulario xls-",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String tiDocIdentidad;


	@EmptyOrLengthCheck(
			min = 8,
			max = 8,
			message = "Debe tener 8 caracteres, o estar vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@Pattern(
			regexp = "^[0-9]*$",
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String nuDniMenor;


	@EmptyOrLengthCheck(
			min = 8,
			max = 8,
			message = "Debe tener 8 caracteres, o estar vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@Pattern(
			regexp = "^[0-9]*$",
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String coDniJefeFam;

	//@Pattern(
	//		regexp = "^[A-ZÁÉÍÓÚÑa-záéíóúñ ]*$",
	//		message = "Contiene caracteres no válidos",
	//		groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@NameCheck(
			message = "Contiene caracteres o secuencias no válidas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	/*@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres, o estar vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})*/
	String apPrimerJefe;


	//@Pattern(
	//		regexp = "^[A-ZÁÉÍÓÚÑa-záéíóúñ ]*$",
	//		message = "Contiene caracteres no válidos",
	//		groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@NameCheck(
			message = "Contiene caracteres o secuencias no válidas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	/*@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres, o estar vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})*/
	String apSegundoJefe;


	//@Pattern(
	//		regexp = "^[A-ZÁÉÍÓÚÑa-záéíóúñ ]*$",
	//		message = "Contiene caracteres no válidos",
	//		groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@NameCheck(
			message = "Contiene caracteres o secuencias no válidas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	/*@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres, o estar vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})*/
	String prenomJefe;


	@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@CoDominioCheck(
			value = CoDominio.tiSeguroMenor,
			message = "Debe tener un código válido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String tiSeguroMenor;

	String deSeguroMenor;


	@Pattern(
			regexp = "^[0-9]{0,7}$",
			message = "Debe tener un código válido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String coInstEducativa;

	String deInstEducativa;


	@CoDominioCheck(
			value = CoDominio.tiVinculoJefe,
			message = "Debe tener un código válido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String tiVinculoJefe;
	String deVinculoJefe;


	@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@CoDominioCheck(
			value = CoDominio.tiVinculoMadre,
			message = "Debe tener un código válido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String tiVinculoMadre;


	/*@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})*/
	@CoDominioCheck(
			value = CoDominio.coGraInstMadre,
			message = "Debe tener un código válido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String coGraInstMadre;
	String deGraInstMadre;


	/*@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@CoDominioCheck(
			value = CoDominio.coLenMadre,
			message = "Debe tener un código válido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})*/
	String coLenMadre;
	String deLenMadre;


	/*@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})*/
	@EmptyOrLengthCheck(
			min = 8,
			max = 8,
			message = "Debe tener 8 caracteres",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@Pattern(
			regexp = "^[0-9]*$",
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String coDniMadre;


	@Pattern(
			regexp = "^[A-ZÁÉÍÓÚÑa-záéíóúñ ]*$",
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@NameCheck(
			message = "Contiene caracteres o secuencias no válidas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres, o estar vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String apPrimerMadre;


	@Pattern(
			regexp = "^[A-ZÁÉÍÓÚÑa-záéíóúñ ]*$",
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@NameCheck(
			message = "Contiene caracteres o secuencias no válidas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres, o ser vacío",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String apSegundoMadre;


	@Pattern(
			regexp = "^[A-ZÁÉÍÓÚÑa-záéíóúñ ]*$",
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@NameCheck(
			message = "Contiene caracteres o secuencias no válidas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 2,
			max = 40,
			message = "Debe tener entre 2 y 40 caracteres",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String prenomMadre;


	//@UbigeoUsuarioCheck(
	//		message = "coUbigeoInei",
	//		groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 6,
			max = 6,
			message = "Debe tener 6 caracteres",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@Pattern(
			regexp = "^[0-9]*$",
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String coUbigeoInei;
	String deUbigeoInei;

    @EmptyOrLengthCheck(
            min = 0,
            max = 10,
            message = "Debe tener 10 caracteres",
            groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
    @Pattern(
            regexp = "^[0-9]*$",
            message = "Contiene caracteres no válidos",
            groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
    String coCentroPoblado;
    String deCentroPoblado;

	@NotEmpty(
			message = "Requerido",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class})
	@EmptyOrLengthCheck(
			min = 8,
			max = 8,
			message = "Debe tener 8 caracteres",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	@Pattern(
			regexp = "^[0-9]*$",
			message = "Contiene caracteres no válidos",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String coEstSalud;
	String deEstSalud;


	@TiProSocialCheck(
			message = "Debe tener códigos válidos separados por comas",
			groups = {PrecotejoRegistroChecks.class, PrecotejoRegistroPriorInsertChecks.class, MinimalPrecotejoRegistroPriorInsertChecks.class})
	String tiProSocial;
	List<String> tiProSocialList;

	//@NotEmpty(message = "coEntidad")
	String nuRegistro;

	public void setNuRegistro(String nuRegistro) {
		this.nuRegistro = nuRegistro;
	}

	public String getNuRegistro() {
		return nuRegistro;
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

	public String getUsModiRegistro() {
		return usModiRegistro;
	}

	public void setUsModiRegistro(String usModiRegistro) {
		this.usModiRegistro = usModiRegistro;
	}

	public String getCoEtnia() {
		return coEtnia;
	}

	public void setCoEtnia(String coEtnia) {
		this.coEtnia = coEtnia;
	}

	public String getNuAfiliacion() {
		return nuAfiliacion;
	}

	public void setNuAfiliacion(String nuAfiliacion) {
		this.nuAfiliacion = nuAfiliacion;
	}

	public String getCoUbigeoInei() {
		return coUbigeoInei;
	}

	public void setCoUbigeoInei(String coUbigeoInei) {
		this.coUbigeoInei = coUbigeoInei;
	}

	public String getCoEstSalud() {
		return coEstSalud;
	}

	public void setCoEstSalud(String coEstSalud) {
		this.coEstSalud = coEstSalud;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
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

	public String getTiSeguroMenor() {
		return tiSeguroMenor;
	}

	public void setTiSeguroMenor(String tiSeguroMenor) {
		this.tiSeguroMenor = tiSeguroMenor;
	}

	public String getCoInstEducativa() {
		return coInstEducativa;
	}

	public void setCoInstEducativa(String coInstEducativa) {
		this.coInstEducativa = coInstEducativa;
	}

	public String getTiVinculoJefe() {
		return tiVinculoJefe;
	}

	public void setTiVinculoJefe(String tiVinculoJefe) {
		this.tiVinculoJefe = tiVinculoJefe;
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

	public String getCoGeneroMenor() {
		return coGeneroMenor;
	}

	public void setCoGeneroMenor(String coGeneroMenor) {
		this.coGeneroMenor = coGeneroMenor;
	}

	public String getDeDireccion() {
		return deDireccion;
	}

	public void setDeDireccion(String deDireccion) {
		this.deDireccion = deDireccion;
	}

	public String getTiDocIdentidad() {
		return tiDocIdentidad;
	}

	public void setTiDocIdentidad(String tiDocIdentidad) {
		this.tiDocIdentidad = tiDocIdentidad;
	}

	public String getCoDniJefeFam() {
		return coDniJefeFam;
	}

	public void setCoDniJefeFam(String coDniJefeFam) {
		this.coDniJefeFam = coDniJefeFam;
	}

	public String getCoDniMadre() {
		return coDniMadre;
	}

	public void setCoDniMadre(String coDniMadre) {
		this.coDniMadre = coDniMadre;
	}

	public String getCoEntidad() {
		return coEntidad;
	}

	public void setCoEntidad(String coEntidad) {
		this.coEntidad = coEntidad;
	}

	public Number getNuEnvio() {
		return nuEnvio;
	}

	public void setNuEnvio(Number nuEnvio) {
		this.nuEnvio = nuEnvio;
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

	public String getDeObservacion() {
		return deObservacion;
	}

	@Autowired
	PadronProperties padronProperties;
	public void setPadronProperties(PadronProperties padronProperties) {
		this.padronProperties = padronProperties;
	}

	public String getDeObservacionHtml() {
		if(deObservacion != null) {

			StringBuilder deObservacionHtmlSb = new StringBuilder();
			deObservacionHtmlSb.append("<ul>");
			for (StringTokenizer stringTokenizer = new StringTokenizer(deObservacion, padronProperties.MAPCOLS_SEPARADOR_LINEA); stringTokenizer.hasMoreTokens(); ) {
				String linea = stringTokenizer.nextToken();
				for (StringTokenizer tokenizer = new StringTokenizer(linea, padronProperties.MAPCOLS_SEPARADOR_DEFINICION); tokenizer.hasMoreTokens(); ) {
					String lineai = tokenizer.nextToken();
					deObservacionHtmlSb.append("<li>");

					deObservacionHtmlSb.append("<strong>");
					deObservacionHtmlSb.append(padronProperties.getReadableMappingColumn(lineai));
					deObservacionHtmlSb.append("</strong>");

					if(tokenizer.hasMoreTokens()) {
						lineai = tokenizer.nextToken();
						deObservacionHtmlSb.append(": ");
						deObservacionHtmlSb.append(lineai);
					}

					deObservacionHtmlSb.append("</li>");
				}
			}
			deObservacionHtmlSb.append("</ul>");
			return deObservacionHtmlSb.toString();

			/*
			String[] observaciones = deObservacion.split(padronProperties.MAPCOLS_SEPARADOR_LINEA);
			deObservacionHtmlSb.append("<ul>");
			for(String observacion : observaciones) {
				String[] linea = observacion.split(padronProperties.MAPCOLS_SEPARADOR_DEFINICION);

				deObservacionHtmlSb.append("<li>");

				deObservacionHtmlSb.append("<strong>");
				deObservacionHtmlSb.append(padronProperties.getReadableMappingColumn(linea[0]));
				deObservacionHtmlSb.append("</strong>");

				if(linea.length>=2) {
					deObservacionHtmlSb.append(": ");
					deObservacionHtmlSb.append(linea[1]);
				}

				deObservacionHtmlSb.append("</li>");
			}
			deObservacionHtmlSb.append("</ul>");
			return deObservacionHtmlSb.toString();
			*/
		}
		return null;
	}

	public void setDeObservacion(String deObservacion) {
		this.deObservacion = deObservacion;
	}

	public String getTiProSocial() {
		return tiProSocial;
	}

	public void setTiProSocial(String tiProSocial) {
		this.tiProSocial = tiProSocial;
	}

	public List<String> getTiProSocialList() {
		return tiProSocialList;
	}

	public void setTiProSocialList(List<String> tiProSocialList) {
		this.tiProSocialList = tiProSocialList;
	}

	public void updateTiProSocialList() {
		setTiProSocialListFrom(this.tiProSocial);
	}

	public void setTiProSocialListFrom(String tiProSocial) {
		this.tiProSocialList = new ArrayList<String>();
		if (tiProSocial != null) {
			String[] tiProSocialArray = tiProSocial.split(",");
			for (String tiProSocialItem : tiProSocialArray) {
				this.tiProSocialList.add(tiProSocialItem);
			}
		}
	}

	public void updateTiProSocial() {
		setTiProSocialFrom(this.tiProSocialList);
	}

	public void setTiProSocialFrom(List<String> tiProSocialList) {
		if (tiProSocialList != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < tiProSocialList.size(); i++) {
				sb.append(tiProSocialList.get(i));
				if (i < tiProSocialList.size() - 1) {
					sb.append(",");
				}
			}
			this.tiProSocial = sb.toString();
		}
	}

	public String getDeObservacionCotejo() {
		return deObservacionCotejo;
	}

	public void setDeObservacionCotejo(String deObservacionCotejo) {
		this.deObservacionCotejo = deObservacionCotejo;
	}

	public String getDeGeneroMenor() {
		return deGeneroMenor;
	}

	public void setDeGeneroMenor(String deGeneroMenor) {
		this.deGeneroMenor = deGeneroMenor;
	}

	public String getDeUbigeoInei() {
		return deUbigeoInei;
	}

	public void setDeUbigeoInei(String deUbigeoInei) {
		this.deUbigeoInei = deUbigeoInei;
	}

	public String getDeSeguroMenor() {
		return deSeguroMenor;
	}

	public void setDeSeguroMenor(String deSeguroMenor) {
		this.deSeguroMenor = deSeguroMenor;
	}

	public String getDeInstEducativa() {
		return deInstEducativa;
	}

	public void setDeInstEducativa(String deInstEducativa) {
		this.deInstEducativa = deInstEducativa;
	}

	public String getDeVinculoJefe() {
		return deVinculoJefe;
	}

	public void setDeVinculoJefe(String deVinculoJefe) {
		this.deVinculoJefe = deVinculoJefe;
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

	public String getDeEstSalud() {
		return deEstSalud;
	}

	public void setDeEstSalud(String deEstSalud) {
		this.deEstSalud = deEstSalud;
	}

	public String getCancelAction() {
		return cancelAction;
	}

	public void setCancelAction(String cancelAction) {
		this.cancelAction = cancelAction;
	}

	public String getSuccessAction() {
		return successAction;
	}

	public void setSuccessAction(String successAction) {
		this.successAction = successAction;
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

    public boolean isEmpty() {
		// http://stackoverflow.com/questions/3333974/how-to-loop-over-a-class-attributes-in-java
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(getClass());
			for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
				//String propertyName = propertyDesc.getName();
				Object value = propertyDesc.getReadMethod().invoke(this);
				if (value != null && !value.toString().isEmpty()) {
					return false;
				}
			}
		} catch (IntrospectionException ie) {
			return false;
		} catch (InvocationTargetException ite) {
			return false;
		} catch (IllegalAccessException iae) {
			return false;
		}
		return true;
	}

	public static java.util.regex.Pattern pattern;

	static {
		// * regex posesivos http://stackoverflow.com/questions/5319840/greedy-vs-reluctant-vs-possessive-quantifiers
		pattern = java.util.regex.Pattern.compile("\\s++");
	}

	private String clean(String value) {
		return pattern.matcher(value.trim()).replaceAll(" ");
	}



	public void cleanBeforeInsert() {
		// menor
		if (prenombreMenor != null && !prenombreMenor.isEmpty()) {
			prenombreMenor = clean(prenombreMenor).toUpperCase();
		}
		if (apPrimerMenor != null && !apPrimerMenor.isEmpty()) {
			apPrimerMenor = clean(apPrimerMenor).toUpperCase();
		}
		if (apSegundoMenor != null && apSegundoMenor.isEmpty()) {
			apSegundoMenor = clean(apSegundoMenor).toUpperCase();
		}

		// padre
		if (prenomJefe != null && !prenomJefe.isEmpty()) {
			prenomJefe = clean(prenomJefe).toUpperCase();
		}
		if (apPrimerJefe != null && !apPrimerJefe.isEmpty()) {
			apPrimerJefe = clean(apPrimerJefe).toUpperCase();
		}
		if (apSegundoJefe != null && apSegundoJefe.isEmpty()) {
			apSegundoJefe = clean(apSegundoJefe).toUpperCase();
		}

		// madre
		if (prenomMadre != null && !prenomMadre.isEmpty()) {
			prenomMadre = clean(prenomMadre).toUpperCase();
		}
		if (apPrimerMadre != null && !apPrimerMadre.isEmpty()) {
			apPrimerMadre = clean(apPrimerMadre).toUpperCase();
		}
		if (apSegundoMadre != null && apSegundoMadre.isEmpty()) {
			apSegundoMadre = clean(apSegundoMadre).toUpperCase();
		}

		// direccion
		if (deDireccion != null && !deDireccion.isEmpty()) {
			deDireccion = clean(deDireccion).toUpperCase();
		}
	}

}