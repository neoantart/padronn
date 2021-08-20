package pe.gob.reniec.padronn.logic.model;

import pe.gob.reniec.padronn.logic.web.validator.checks.CentroPobladoCheckValidorCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.CentroPobladoValidator;
import pe.gob.reniec.padronn.logic.web.validator.constraints.CoUbigeo;
import pe.gob.reniec.padronn.logic.web.validator.constraints.NameCharacters;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 09/01/14
 * Time: 07:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CentroPoblado implements Serializable {
    String coCentroPoblado;

    String noCentroPoblado;

    String deUbigeoInei;

    String coCategoria;
    String noCategoria;

    String coUbigeoInei;

    String deDepartamento;
    String deProvincia;
    String deDistrito;

    String coAreaCcpp;
    String deAreaCcpp;
    String esCentroPoblado;
    String usCreaAudi;
    String feCreaAudi;
    String feModiAudi;
    String usModiAudi;
    String coCcPpInei;

    public CentroPoblado() {
    }

    public String getCoCentroPoblado() {
        return coCentroPoblado;
    }

    public void setCoCentroPoblado(String coCentroPoblado) {
        this.coCentroPoblado = coCentroPoblado;
    }

    public String getNoCentroPoblado() {
        return noCentroPoblado;
    }

    public void setNoCentroPoblado(String noCentroPoblado) {
        this.noCentroPoblado = noCentroPoblado;
    }

    public String getDeUbigeoInei() {
        return deUbigeoInei;
    }

    public void setDeUbigeoInei(String deUbigeoInei) {
        this.deUbigeoInei = deUbigeoInei;
    }

    public String getNoCategoria() {
        return noCategoria;
    }

    public void setNoCategoria(String noCategoria) {
        this.noCategoria = noCategoria;
    }

    public String getCoUbigeoInei() {
        return coUbigeoInei;
    }

    public void setCoUbigeoInei(String coUbigeoInei) {
        this.coUbigeoInei = coUbigeoInei;
    }

    public String getCoCategoria() {
        return coCategoria;
    }

    public void setCoCategoria(String coCategoria) {
        this.coCategoria = coCategoria;
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

    public String getEsCentroPoblado() {
        return esCentroPoblado;
    }

    public void setEsCentroPoblado(String esCentroPoblado) {
        this.esCentroPoblado = esCentroPoblado;
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

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public String getCoCcPpInei() {
        return coCcPpInei;
    }

    public void setCoCcPpInei(String coCcPpInei) {
        this.coCcPpInei = coCcPpInei;
    }
}

