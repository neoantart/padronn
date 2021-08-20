package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class PrecotejoRegistroDetalle implements Serializable {

    private String nuRegistro; //codigo de registro padrón Nominal

    private String nuEnvio;

    private String deObservacion;

    private Integer nuSec;

    private String coEntidad;

    private String nuDniMenor; //numero de dni del menor

    private String nuCnv;

    @NotEmpty
    private String apPrimerMenor; //primer apellido del menor

    @NotEmpty
    private String apSegundoMenor; //ap_segundo_menor

    @NotEmpty
    private String prenombreMenor; //prenombres del menor

    @NotEmpty
    private String feNacMenor; //fecha de nacimiento del menor

    @NotEmpty
    private String deDireccion; //direccion del menor

    @NotEmpty
    private String coGeneroMenor; //genero del menor

    private String coNivelPobreza; //nivel de pobreza del menor

    private String coEtnia;

    @NotEmpty
    private String coEstSalud; //codigo de establecimiento de salud

    @NotEmpty
    private String tiSeguroMenor; //tipo de seguro del menor

    private String nuAfiliacion; //numero de afiliacion al seguro

    private List<String> tiProSocial; //tipo de programa social afiliado

    private List<Dominio> padronProgramaList; //tipo de programa social afiliado

    private String coInstEducativa; //codigo de institucion educativa

    private String tiVinculoJefe; //tipo de vinculo del jefe de familia

    private String coDniJefeFam; //dni del jefe de familia

    private String apPrimerJefe; //primer apellido del jefe de familia

    private String apSegundoJefe; //segundo apellido del jefe de familia

    private String prenomJefe; //prenombre del jefe de familia

    //@NotEmpty
    private String tiVinculoMadre; //tipo de vinculo de la madre o apoderado

    @NotEmpty
    private String coDniMadre; //dni de la madre o apoderado

    @NotEmpty
    private String apPrimerMadre; //primer apellido de la madre o apoderado

    @NotEmpty
    private String apSegundoMadre; //segundo apellido de la madre o apoderado

    @NotEmpty
    private String prenomMadre; //prenombre de la madre o apoderado

    @NotEmpty
    private String coGraInstMadre; //grado de instruccion de la madre o apoderado

    @NotEmpty
    private String coLenMadre; //lengua habitual de la madre o apoderado

    @NotEmpty
    private String coUbigeoInei;

    /*
    Campos complementarios
     */
    private String deGeneroMenor;
    private String deVinculoJefe;
    private String deVinculoMadre;
    private String edad;
    private String edadEscrita;
    private String deUbigeoInei;

    private String deNivelPobreza;
    private String deEstSalud;
    private String deSeguroMenor;
    private String deInstEducativa;
    private String deGraInstMadre;
    private String deLenMadre;

    private HashMap<String, Object> camposErrores;

    private Persona.OrigenFoto origenFoto;

	/*
	Campos de auditoria
	 */

    private String usCreaRegistro; //usuario crea registro
    private String feCreaRegistro; //fecha de creacion registro
    private String usModiRegistro; //usuario modifica registro
    private String feModiRegistro; //fecha modifica registro
    private String coEstado;

	/* Funciones auxiliares */

    String deObservacionCotejo;


	/*
	 Campos sin depurar
	 */

    private MultipartFile imgFotoMenor;
    private String tiDocIdentidad; //tipo de documento de identidad del menor

    public MultipartFile getImgFotoMenor() {
        return imgFotoMenor;
    }

    public void setImgFotoMenor(MultipartFile imgFotoMenor) {
        this.imgFotoMenor = imgFotoMenor;
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

    public String getTiSeguroMenor() {
        return tiSeguroMenor;
    }

    public void setTiSeguroMenor(String tiSeguroMenor) {
        this.tiSeguroMenor = tiSeguroMenor;
    }

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
        //this.coProgramaSocial = tiProSocial.toArray(new String[0]); //NO!
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

    public String getDeSeguroMenor() {
        return deSeguroMenor;
    }

    public HashMap<String, Object> getCamposErrores() {
        return camposErrores;
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

    public String getNuRegistro() {
        return nuRegistro;
    }

    public String getNuEnvio() {
        return nuEnvio;
    }

    public void setNuRegistro(String nuRegistro) {
        this.nuRegistro = nuRegistro;
    }

    public void setNuEnvio(String nuEnvio) {
        this.nuEnvio = nuEnvio;
    }

    public String getDeObservacion() {
        return deObservacion;
    }

    public void setDeObservacion(String deObservacion) {
        this.deObservacion = deObservacion;
    }

    public String getDeObservacionCotejo() {
        return deObservacionCotejo;
    }

    public void setDeObservacionCotejo(String deObservacionCotejo) {
        this.deObservacionCotejo = deObservacionCotejo;
    }

    public void setCamposErrores(HashMap<String, Object> camposErrores) {
        this.camposErrores = camposErrores;
    }
}
