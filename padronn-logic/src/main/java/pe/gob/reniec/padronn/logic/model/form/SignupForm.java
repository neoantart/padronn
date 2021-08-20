package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.model.AbstractBean;
import pe.gob.reniec.padronn.logic.web.validator.SignupFormValidator;
import pe.gob.reniec.padronn.logic.web.validator.checks.EmptyOrLengthCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.*;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 16/07/13
 * Time: 12:03 PM
 */

//@SignupInsertPassword
@SignupFormValidator
@SignupDni
@SignupFormNombres
public class SignupForm extends AbstractBean {

    @NotEmpty(message = "DNI requerido")
    @Dni
    //@DniMayorEdad
    String coUsuario;

    @NotEmpty(message="Primer apellido requerido")
    @EmptyOrLengthCheck(
            min = 2,
            max = 40,
            message = "Primer apellido debe tener entre 2 y 40 caracteres"
    )
    private String apPrimer;


   // @NotEmpty(message="Segundo apellido requerido")
    @EmptyOrLengthCheck(
            min = 2,
            max = 40,
            message = "Segundo apellido debe tener entre 2 y 40 caracteres"
    )
    private String apSegundo;

    @NotEmpty(message="Nombres de usuario requerido")
    @EmptyOrLengthCheck(
            min = 2,
            max = 40,
            message = "Nombres de usuario debe tener entre 2 y 40 caracteres"
    )
    private String prenombres;

    private String dePassword;

    private String rePassword;

    private String esUsuario;

    @Pattern(regexp = "^$|(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "Email invalido")
    private String deEmail;

    @NotEmpty(message = "Entidades no debe ser vacio")
    private List<String> entidades;

    @NotEmpty(message = "Perfil de usuario no debe ser vacio")
    private List<String> grupos;

    @NotEmpty(message = "Tipo de entidades no debe estar vacio")
    private List<String> tipoEntidades;

    private String nuTelefono;

    private String feLastLogin;

    private String deObservacion;

    private String deEntidades;

    private String deGrupos;

    private String usModiAudi;

    private String feModiAudi;

    private String errorMessage;


    public List<String> getTipoEntidades() {
        return tipoEntidades;
    }

    public void setTipoEntidades(List<String> tipoEntidades) {
        this.tipoEntidades = tipoEntidades;
    }

    public void setCoUsuario(String coUsuario) {
        this.coUsuario = coUsuario;
    }

    public void setApPrimer(String apPrimer) {
        this.apPrimer = apPrimer;
    }

    public void setApSegundo(String apSegundo) {
        this.apSegundo = apSegundo;
    }

    public void setPrenombres(String prenombres) {
        this.prenombres = prenombres ;
    }

    public void setDePassword(String dePassword) {
        this.dePassword = dePassword;
    }

    /*public void setDePasswordRepeat(String dePasswordRepeat) {
        this.dePasswordRepeat = dePasswordRepeat;
    } */

    public void setEsUsuario(String esUsuario) {
        this.esUsuario = esUsuario;
    }

    public void setDeEmail(String deEmail) {
        this.deEmail = deEmail;
    }

    public void setEntidades(List<String> entidades) {
        this.entidades = entidades;
    }

    public void setGrupos(List<String> grupos) {
        this.grupos = grupos;
    }

    public String getCoUsuario() {
        return coUsuario;
    }

    public String getApPrimer() {
        return apPrimer;
    }

    public String getApSegundo() {
        return apSegundo;
    }

    public String getPrenombres() {
        return prenombres;
    }

    public String getDePassword() {
        return dePassword;
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

    public List<String> getGrupos() {
        return grupos;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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

    public String getDeObservacion() {
        return deObservacion;
    }

    public void setDeObservacion(String deObservacion) {
        this.deObservacion = deObservacion;
    }

    public String getDeEntidades() {
        return deEntidades;
    }

    public void setDeEntidades(String deEntidades) {
        this.deEntidades = deEntidades;
    }

    public String getDeGrupos() {
        return deGrupos;
    }

    public void setDeGrupos(String deGrupos) {
        this.deGrupos = deGrupos;
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
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
