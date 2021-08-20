package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;

public class MovimientoPassword implements Serializable {
    String coUsuario;
    String dePassword;
    String coTipoAccionPw;
    Integer nuSecPw;
    String usModiPw;
    String fePassword;

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

    public String getCoTipoAccionPw() {
        return coTipoAccionPw;
    }

    public void setCoTipoAccionPw(String coTipoAccionPw) {
        this.coTipoAccionPw = coTipoAccionPw;
    }

    public Integer getNuSecPw() {
        return nuSecPw;
    }

    public void setNuSecPw(Integer nuSecPw) {
        this.nuSecPw = nuSecPw;
    }

    public String getUsModiPw() {
        return usModiPw;
    }

    public void setUsModiPw(String usModiPw) {
        this.usModiPw = usModiPw;
    }

    public String getFePassword() {
        return fePassword;
    }

    public void setFePassword(String fePassword) {
        this.fePassword = fePassword;
    }

    public static MovimientoPassword fromUsuarioExterno(UsuarioExterno usuarioExterno) {
        MovimientoPassword movimientoPassword = new MovimientoPassword();
        movimientoPassword.setCoUsuario(usuarioExterno.getCoUsuario());
        if (usuarioExterno.getDePassword() != null && !usuarioExterno.getDePassword().isEmpty())
                movimientoPassword.setDePassword(usuarioExterno.getDePassword());
        if (usuarioExterno.getCoTipoAccionPw() != null && !usuarioExterno.getCoTipoAccionPw().isEmpty())
                movimientoPassword.setCoTipoAccionPw(usuarioExterno.getCoTipoAccionPw());
        if (usuarioExterno.getUsModiAudi() !=null && !usuarioExterno.getUsModiAudi().isEmpty())
                movimientoPassword.setUsModiPw(usuarioExterno.getUsModiAudi());
        return movimientoPassword;
    }

    public static MovimientoPassword fromCambioCredencial(CambioCredencial cambioCredencial) {
        MovimientoPassword movimientoPassword = new MovimientoPassword();
        movimientoPassword.setCoUsuario(cambioCredencial.getCoUsuario());
        if (cambioCredencial.getDePasswordNuevo() != null && !cambioCredencial.getDePasswordNuevo().isEmpty())
            movimientoPassword.setDePassword(cambioCredencial.getDePasswordNuevo());
        if (cambioCredencial.getCoTipoAccionPw() != null && !cambioCredencial.getCoTipoAccionPw().isEmpty())
            movimientoPassword.setCoTipoAccionPw(cambioCredencial.getCoTipoAccionPw());
        if (cambioCredencial.getUsModiPw() !=null && !cambioCredencial.getUsModiPw().isEmpty())
            movimientoPassword.setUsModiPw(cambioCredencial.getUsModiPw());
        return movimientoPassword;
    }

    public static MovimientoPassword toResetPassword(String nuDni, String usModiPw) {
        MovimientoPassword movimientoPassword = new MovimientoPassword();
        movimientoPassword.setCoUsuario(nuDni);
        movimientoPassword.setDePassword(nuDni);
        movimientoPassword.setCoTipoAccionPw("02");
        if (usModiPw !=null && !usModiPw.isEmpty())
            movimientoPassword.setUsModiPw(usModiPw);
        return movimientoPassword;
    }

    @Override
    public String toString() {
        return "MovimientoPassword{" +
                "coUsuario='" + coUsuario + '\'' +
                ", dePassword='" + dePassword + '\'' +
                ", coTipoAccionPw='" + coTipoAccionPw + '\'' +
                ", nuSecPw=" + nuSecPw +
                ", usModiPw='" + usModiPw + '\'' +
                ", fePassword='" + fePassword + '\'' +
                '}';
    }
}
