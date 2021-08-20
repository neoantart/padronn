package pe.gob.reniec.padronn.logic.model;

/**
 * Clase Observacion.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 15/07/13 10:23 AM
 */
public class Observacion implements java.io.Serializable{

  String coObservacion;
  String coObservacionTipo;
  String coEntidad;
  String coUsuario;
  String coPadronNominal;
  String deDetalleAdicional;
  String usCreaAudi;
  String feCreaAudi;
  String usModiAudi;
  String feModiAudi;

  // adicionales para ser mostrados en el reporte
  String apPrimerMenor;
  String apSegundoMenor;
  String prenombreMenor;
  String feNacMenor;
  String deEntidad;
  String deObservacion;

  public Observacion() {}

  public Observacion(PadronNominal padronNominal) {
    coEntidad = padronNominal.getCoEntidad();
    coUsuario = padronNominal.getUsCreaRegistro();
    coPadronNominal = padronNominal.getCoPadronNominal();
    usCreaAudi = padronNominal.getUsCreaRegistro();
    usModiAudi = padronNominal.getUsModiRegistro();
  }

  public Observacion(PadronNominal padronNominal, Tipo tipo) {
    this(padronNominal);
    this.coObservacionTipo = tipo.getCoTipo();
  }

  public String getCoObservacion() {
    return coObservacion;
  }

  public void setCoObservacion(String coObservacion) {
    this.coObservacion = coObservacion;
  }

  public String getCoObservacionTipo() {
    return coObservacionTipo;
  }

  public void setCoObservacionTipo(String coObservacionTipo) {
    this.coObservacionTipo = coObservacionTipo;
  }

  public String getCoEntidad() {
    return coEntidad;
  }

  public void setCoEntidad(String coEntidad) {
    this.coEntidad = coEntidad;
  }

  public String getCoUsuario() {
    return coUsuario;
  }

  public void setCoUsuario(String coUsuario) {
    this.coUsuario = coUsuario;
  }

  public String getCoPadronNominal() {
    return coPadronNominal;
  }

  public void setCoPadronNominal(String coPadronNominal) {
    this.coPadronNominal = coPadronNominal;
  }

  public String getDeDetalleAdicional() {
    return deDetalleAdicional;
  }

  public void setDeDetalleAdicional(String deDetalleAdicional) {
    this.deDetalleAdicional = deDetalleAdicional;
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
  }

  // TODO el tipo de observación debería obtenerse de la base de datos de alguna forma, ahora no se me ocurre ninguna mas que la presentada ac toninuación
  public static enum Tipo {

    MADRE_INDOCUMENTADA("1"),
    SIN_DATOS_MADRE("2");


    private String coTipo;

    private Tipo(String coTipo) {
      this.coTipo = coTipo;
    }

    public String getCoTipo() {
      return coTipo;
    }
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

    public String getDeEntidad() {
        return deEntidad;
    }

    public void setDeEntidad(String deEntidad) {
        this.deEntidad = deEntidad;
    }

    public String getDeObservacion() {
        return deObservacion;
    }

    public void setDeObservacion(String deObservacion) {
        this.deObservacion = deObservacion;
    }
}
