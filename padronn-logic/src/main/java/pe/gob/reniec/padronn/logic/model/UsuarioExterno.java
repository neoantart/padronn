package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class UsuarioExterno
        implements Serializable {

    String coUsuario;
    int coEntidad;
    String apPrimer;
    String apSegundo;
    String apCasada;
    String prenombres;
    String dePassword;
    String usCreaAudi;
    String feCreaAudi;
    String usModiAudi;
    String feModiAudi;
    String esUsuario;
    String deEmail;
    String nuTelefono;
    String feLastLogin;
    String horaAcceso;
    String coUbigeoInei;
    String deDepartamento;
    String deProvincia;
    String deDistrito;
    int nuSec;

    List<String> entidades;
    List<String> tipoEntidades;
    List<String> grupos;


    String apPrimerCrea;
    String apSegundoCrea;
    String prenombresCrea;
    String coUsuarioCrea;
    String apPrimerModi;
    String apSegundoModi;
    String prenombresModi;
    String coUsuarioModi;
    String deEntidad;
    String deEntidadLarga;
    String deGrupo;
    String dniAlcalde;
    String deAlcalde;
    String deEntidades;
    String deTipoEntidades;
    String deGrupos;
    String deObservacion;
    String errorMessage;
    String coTipoAccionPw;
    String coTipoEntidad;
    String deTipoEntidad;

    public String getDeTipoEntidad() {
        return deTipoEntidad;
    }

    public void setDeTipoEntidad(String deTipoEntidad) {
        this.deTipoEntidad = deTipoEntidad;
    }

    public String getCoTipoEntidad() {
        return coTipoEntidad;
    }

    public void setCoTipoEntidad(String coTipoEntidad) {
        this.coTipoEntidad = coTipoEntidad;
    }

    public String getDeTipoEntidades() {
        return deTipoEntidades;
    }

    public void setDeTipoEntidades(String deTipoEntidades) {
        this.deTipoEntidades = deTipoEntidades;
    }

    public List<String> getTipoEntidades() {
        return tipoEntidades;
    }

    public void setTipoEntidades(List<String> tipoEntidades) {
        this.tipoEntidades = tipoEntidades;
    }

    public UsuarioExterno() {  }

    public String getCoUsuario() {
        return coUsuario;
    }

    public int getCoEntidad() {
        return coEntidad;
    }

    public String getApPrimer() {
        return apPrimer;
    }

    public String getApSegundo() {
        return apSegundo;
    }

    public String getApCasada() {
        return apCasada;
    }

    public String getPrenombres() {
        return prenombres;
    }

    public String getDePassword() {
        return dePassword;
    }

    public String getUsCreaAudi() {
        return usCreaAudi;
    }

    public String getFeCreaAudi() {
        return feCreaAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public String getEsUsuario() {
        return esUsuario;
    }

    public String getDeEmail() {
        return deEmail;
    }

    public List<String> getEntidades() {
        return entidades;
    }

    public void setCoUsuario(String coUsuario) {
        this.coUsuario = coUsuario;
    }

    public void setCoEntidad(int coEntidad) {
        this.coEntidad = coEntidad;
    }

    public void setApPrimer(String apPrimer) {
        this.apPrimer = apPrimer;
    }

    public void setApSegundo(String apSegundo) {
        this.apSegundo = apSegundo;
    }

    public void setApCasada(String apCasada) {
        this.apCasada = apCasada;
    }

    public void setPrenombres(String prenombres) {
        this.prenombres = prenombres;
    }

    public void setDePassword(String dePassword) {
        this.dePassword = dePassword;
    }

    public void setUsCreaAudi(String usCreaAudi) {
        this.usCreaAudi = usCreaAudi;
    }

    public void setFeCreaAudi(String feCreaAudi) {
        this.feCreaAudi = feCreaAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
    }

    public void setEsUsuario(String esUsuario) {
        this.esUsuario = esUsuario;
    }

    public void setDeEmail(String deEmail) {
        this.deEmail = deEmail;
    }

    public void setEntidades(List<String> entidades) {
        this.entidades = entidades;
    }

    public List<String> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<String> grupos) {
        this.grupos = grupos;
    }

    public String getNuTelefono() {
        return nuTelefono;
    }

    public void setNuTelefono(String nuTelefono) {
        this.nuTelefono = nuTelefono;
    }

    public String getFeLastLogin() {
        return feLastLogin;
    }

    public void setFeLastLogin(String feLastLogin) {
        this.feLastLogin = feLastLogin;
    }

    public int getNuSec() {
        return nuSec;
    }

    public void setNuSec(int nuSec) {
        this.nuSec = nuSec;
    }

    public String getNombresCompletos() {
        return this.apPrimer + " " + this.apSegundo + " " + this.prenombres;
    }

    public String getApPrimerCrea() {
        return apPrimerCrea;
    }

    public void setApPrimerCrea(String apPrimerCrea) {
        this.apPrimerCrea = apPrimerCrea;
    }

    public String getApSegundoCrea() {
        return apSegundoCrea;
    }

    public void setApSegundoCrea(String apSegundoCrea) {
        this.apSegundoCrea = apSegundoCrea;
    }

    public String getPrenombresCrea() {
        return prenombresCrea;
    }

    public void setPrenombresCrea(String prenombresCrea) {
        this.prenombresCrea = prenombresCrea;
    }

    public String getCoUsuarioCrea() {
        return coUsuarioCrea;
    }

    public void setCoUsuarioCrea(String coUsuarioCrea) {
        this.coUsuarioCrea = coUsuarioCrea;
    }

    public String getApPrimerModi() {
        return apPrimerModi;
    }

    public void setApPrimerModi(String apPrimerModi) {
        this.apPrimerModi = apPrimerModi;
    }

    public String getApSegundoModi() {
        return apSegundoModi;
    }

    public void setApSegundoModi(String apSegundoModi) {
        this.apSegundoModi = apSegundoModi;
    }

    public String getPrenombresModi() {
        return prenombresModi;
    }

    public void setPrenombresModi(String prenombresModi) {
        this.prenombresModi = prenombresModi;
    }

    public String getCoUsuarioModi() {
        return coUsuarioModi;
    }

    public void setCoUsuarioModi(String coUsuarioModi) {
        this.coUsuarioModi = coUsuarioModi;
    }

    public String getDeEntidad(String deEntidades) {
        return deEntidad;
    }

    public void setDeEntidad(String deEntidad) {
        this.deEntidad = deEntidad;
    }

    public String getDeEntidadLarga() {
        return deEntidadLarga;
    }

    public void setDeEntidadLarga(String deEntidadLarga) {
        this.deEntidadLarga = deEntidadLarga;
    }

    public String getDeGrupo() {
        return deGrupo;
    }

    public void setDeGrupo(String deGrupo) {
        this.deGrupo = deGrupo;
    }

    public String getDniAlcalde() {
        return dniAlcalde;
    }

    public void setDniAlcalde(String dniAlcalde) {
        this.dniAlcalde = dniAlcalde;
    }

    public String getDeAlcalde() {
        return deAlcalde;
    }

    public void setDeAlcalde(String deAlcalde) {
        this.deAlcalde = deAlcalde;
    }

    public String getHoraAcceso() {
        return horaAcceso;
    }

    public void setHoraAcceso(String horaAcceso) {
        this.horaAcceso = horaAcceso;
    }

    public String getCoUbigeoInei() {
        return coUbigeoInei;
    }

    public void setCoUbigeoInei(String coUbigeoInei) {
        this.coUbigeoInei = coUbigeoInei;
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

    public String getDeEntidad() {
        return deEntidad;
    }

    public String getDeEntidades() {
        return deEntidades;
    }

    public void setDeEntidades(String deEntidades) {
        this.deEntidades = deEntidades;
    }

    public String getDeObservacion() {
        return deObservacion;
    }

    public void setDeObservacion(String deObservacion) {
        this.deObservacion = deObservacion;
    }

    public String getDeGrupos() {
        return deGrupos;
    }

    public void setDeGrupos(String deGrupos) {
        this.deGrupos = deGrupos;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCoTipoAccionPw() {
        return coTipoAccionPw;
    }

    public void setCoTipoAccionPw(String coTipoAccionPw) {
        this.coTipoAccionPw = coTipoAccionPw;
    }

    /*public List<String> getTiposEntidad() {
        return tiposEntidad;
    }

    public void setTiposEntidad(List<String> tiposEntidad) {
        this.tiposEntidad = tiposEntidad;
    }*/



}
