package pe.gob.reniec.padronn.logic.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class PadronEdadEESS implements Serializable{
    private static final int CO_EST_SALUD_LENGTH = 8;

    private String fila;

    private String coRenaes;

    private String coFrecuenciaAtencion;

    @Length(max=CO_EST_SALUD_LENGTH, message = "Codigo de establecimiento de salud inválido ")
    @Pattern(
            regexp = "\\d{0,8}",
            message = "Contiene caracteres no válidos")
    private String coEstSalud;

    @Length(max=CO_EST_SALUD_LENGTH, message = "Codigo de establecimiento de salud inválido ")
    @Pattern(
            regexp = "\\d{0,8}",
            message = "Contiene caracteres no válidos")
    private String coEstSaludNac;

    @Length(max=CO_EST_SALUD_LENGTH, message = "Codigo de establecimiento inválido ")
    @Pattern(
            regexp = "\\d{0,8}",
            message = "Contiene caracteres no válidos")
    private String coEstSaludAds;

    private String deEstSalud;

    private String deEstSaludNac;

    private String deEstSaludAds;

    private String deRenaesDireccion;

    private Integer anno0;

    private Integer anno1;

    private Integer anno2;

    private Integer anno3;

    private Integer anno4;

    private Integer anno5;

    private Integer total;

    public static int getCoEstSaludLength() {
        return CO_EST_SALUD_LENGTH;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getCoRenaes() {
        return coRenaes;
    }

    public void setCoRenaes(String coRenaes) {
        this.coRenaes = coRenaes;
    }

    public String getDeEstSalud() {
        return deEstSalud;
    }

    public void setDeEstSalud(String deEstSalud) {
        this.deEstSalud = deEstSalud;
    }

    public String getCoEstSalud() {
        return coEstSalud;
    }

    public void setCoEstSalud(String coEstSalud) {
        this.coEstSalud = coEstSalud;
    }

    public Integer getAnno0() {
        return anno0;
    }

    public void setAnno0(Integer anno0) {
        this.anno0 = anno0;
    }

    public Integer getAnno1() {
        return anno1;
    }

    public void setAnno1(Integer anno1) {
        this.anno1 = anno1;
    }

    public Integer getAnno2() {
        return anno2;
    }

    public void setAnno2(Integer anno2) {
        this.anno2 = anno2;
    }

    public Integer getAnno3() {
        return anno3;
    }

    public void setAnno3(Integer anno3) {
        this.anno3 = anno3;
    }

    public Integer getAnno4() {
        return anno4;
    }

    public void setAnno4(Integer anno4) {
        this.anno4 = anno4;
    }

    public Integer getAnno5() {
        return anno5;
    }

    public void setAnno5(Integer anno5) {
        this.anno5 = anno5;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getCoEstSaludNac() {
        return coEstSaludNac;
    }

    public void setCoEstSaludNac(String coEstSaludNac) {
        this.coEstSaludNac = coEstSaludNac;
    }

    public String getCoEstSaludAds() {
        return coEstSaludAds;
    }

    public void setCoEstSaludAds(String coEstSaludAds) {
        this.coEstSaludAds = coEstSaludAds;
    }

    public String getDeEstSaludNac() {
        return deEstSaludNac;
    }

    public void setDeEstSaludNac(String deEstSaludNac) {
        this.deEstSaludNac = deEstSaludNac;
    }

    public String getDeEstSaludAds() {
        return deEstSaludAds;
    }

    public String getCoFrecuenciaAtencion() {
        return coFrecuenciaAtencion;
    }

    public void setCoFrecuenciaAtencion(String coFrecuenciaAtencion) {
        this.coFrecuenciaAtencion = coFrecuenciaAtencion;
    }

    public void setDeEstSaludAds(String deEstSaludAds) {
        this.deEstSaludAds = deEstSaludAds;
    }

    public String getDeRenaesDireccion() {
        return deRenaesDireccion;
    }

    public void setDeRenaesDireccion(String deRenaesDireccion) {
        this.deRenaesDireccion = deRenaesDireccion;
    }

    @Override
    public String toString() {
        return "PadronEdadEESS{" +
                "fila='" + fila + '\'' +
                ", coFrecuenciaAtencion='" + coFrecuenciaAtencion + '\'' +
                ", coEstSalud='" + coEstSalud + '\'' +
                ", coEstSaludNac='" + coEstSaludNac + '\'' +
                ", coEstSaludAds='" + coEstSaludAds + '\'' +
                ", deEstSalud='" + deEstSalud + '\'' +
                ", deEstSaludNac='" + deEstSaludNac + '\'' +
                ", deEstSaludAds='" + deEstSaludAds + '\'' +
                ", deRenaesDireccion='" + deRenaesDireccion + '\'' +
                ", anno0=" + anno0 +
                ", anno1=" + anno1 +
                ", anno2=" + anno2 +
                ", anno3=" + anno3 +
                ", anno4=" + anno4 +
                ", anno5=" + anno5 +
                ", total=" + total +
                '}';
    }
}
