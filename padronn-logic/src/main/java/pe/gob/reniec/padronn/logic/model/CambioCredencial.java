package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.omg.CosNaming.NamingContextPackage.NotEmptyHelper;
import pe.gob.reniec.padronn.logic.web.validator.CambioCredencialPasswordValidator;
import pe.gob.reniec.padronn.logic.web.validator.checks.CambioCredencialPasswordCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.PasswordCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.Dni;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Clase CambioCredencial.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 16/07/13 05:39 PM
 */
@CambioCredencialPasswordCheck
public class CambioCredencial implements java.io.Serializable{

  @NotEmpty
  String fullName;
  @NotEmpty @Dni
  String coUsuario;
  @Length(min=6,max=20)
  String dePassword;
  @Length(min=8,max=15) @PasswordCheck
  String dePasswordNuevo;
  @Length(min=8,max=15) @PasswordCheck
  String dePasswordNuevoRepetido;

  String coTipoAccionPw;
  String usModiPw;

  public CambioCredencial() {
    this.fullName = "";
    this.coUsuario = "";
    this.dePassword = "";
    this.dePasswordNuevo = "";
    this.dePasswordNuevoRepetido = "";
  }

  public CambioCredencial(String fullName, String coUsuario) {
    this.fullName = fullName;
    this.coUsuario = coUsuario;
    this.dePassword = "";
    this.dePasswordNuevo = "";
    this.dePasswordNuevoRepetido = "";
  }

  public String getCoUsuario() {
    return coUsuario;
  }

  public void setCoUsuario(String coUsuario) {
    this.coUsuario = coUsuario;
  }

  public String getDePassword() {
    return dePassword;
  }

  public void setDePassword(String dePassword) {
    this.dePassword = dePassword;
  }

  public String getDePasswordNuevo() {
    return dePasswordNuevo;
  }

  public void setDePasswordNuevo(String dePasswordNuevo) {
    this.dePasswordNuevo = dePasswordNuevo;
  }

  public String getDePasswordNuevoRepetido() {
    return dePasswordNuevoRepetido;
  }

  public void setDePasswordNuevoRepetido(String dePasswordNuevoRepetido) {
    this.dePasswordNuevoRepetido = dePasswordNuevoRepetido;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getCoTipoAccionPw() {
    return coTipoAccionPw;
  }

  public void setCoTipoAccionPw(String coTipoAccionPw) {
    this.coTipoAccionPw = coTipoAccionPw;
  }

  public String getUsModiPw() {
    return usModiPw;
  }

  public void setUsModiPw(String usModiPw) {
    this.usModiPw = usModiPw;
  }
}
