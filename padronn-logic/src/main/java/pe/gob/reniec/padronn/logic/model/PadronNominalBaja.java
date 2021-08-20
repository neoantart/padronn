package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.checks.PadronNominalBajaOtrosMotivos;

import java.io.Serializable;

/**
 * Clase PadronNominalBaja.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 10/08/13 10:43 AM
 */
@PadronNominalBajaOtrosMotivos
public class PadronNominalBaja
        implements Serializable {

    @NotEmpty(message = "Codigo de Padron Nominal no puede estar vacio")
    private String coPadronNominal;
    private String esPadron;
    @NotEmpty(message = "Motivo de baja de registro es obligatorio")
    private String coMotivoBaja;

    @Length(max = 100, message = "Detalle del motivo es obligatorio")
    @NotEmpty(message = "Detalle es obligatorio")
    private String deObservacion;

    private String usCreaAudi;
    private String usModiAudi;

    private String nuPagina;

    public String getCoPadronNominal() {
        return coPadronNominal;
    }

    public void setCoPadronNominal(String coPadronNominal) {
        this.coPadronNominal = coPadronNominal;
    }

    public String getEsPadron() {
        return esPadron;
    }

    public void setEsPadron(String esPadron) {
        this.esPadron = esPadron;
    }

    public String getCoMotivoBaja() {
        return coMotivoBaja;
    }

    public void setCoMotivoBaja(String coMotivoBaja) {
        this.coMotivoBaja = coMotivoBaja;
    }

    public String getDeObservacion() {
        return deObservacion;
    }

    public void setDeObservacion(String deObservacion) {
        this.deObservacion = deObservacion;
    }

    public String getUsCreaAudi() {
        return usCreaAudi;
    }

    public void setUsCreaAudi(String usCreaAudi) {
        this.usCreaAudi = usCreaAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public String getNuPagina() {
        return nuPagina;
    }

    public void setNuPagina(String nuPagina) {
        this.nuPagina = nuPagina;
    }
}
