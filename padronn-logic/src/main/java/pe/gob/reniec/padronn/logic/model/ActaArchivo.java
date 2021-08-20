package pe.gob.reniec.padronn.logic.model;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 14/08/13
 * Time: 06:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActaArchivo implements java.io.Serializable {
    private String coActaArchivo;
    private byte[] archivo;
    private String noActaArchivo;
    private String sizeActaArchivo;
    private String sizeActaArchivoFormat;
    private String extArchivo;

    private String contentType;
    private String feCreaAudi;
    private String usCreaAudi;
    private String feModiAudi;
    private String usModiAudi;

    public String getCoActaArchivo() {
        return coActaArchivo;
    }

    public String getNoActaArchivo() {
        return noActaArchivo;
    }

    // http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
    public String getSizeActaArchivo() {
        return sizeActaArchivo;
        /*long size = Long.parseLong(sizeActaArchivo);
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];*/
    }

    public String getFeCreaAudi() {
        return feCreaAudi;
    }

    public String getUsCreaAudi() {
        return usCreaAudi;
    }

    public String getFeModiAudi() {
        return feModiAudi;
    }

    public String getUsModiAudi() {
        return usModiAudi;
    }

    public void setCoActaArchivo(String coActaArchivo) {
        this.coActaArchivo = coActaArchivo;
    }

    public void setNoActaArchivo(String noActaArchivo) {
        this.noActaArchivo = noActaArchivo;
    }

    public void setSizeActaArchivo(String sizeActaArchivo) {
        this.sizeActaArchivo = sizeActaArchivo;
    }

    public void setFeCreaAudi(String feCreaAudi) {
        this.feCreaAudi = feCreaAudi;
    }

    public void setUsCreaAudi(String usCreaAudi) {
        this.usCreaAudi = usCreaAudi;
    }

    public void setFeModiAudi(String feModiAudi) {
        this.feModiAudi = feModiAudi;
    }

    public void setUsModiAudi(String usModiAudi) {
        this.usModiAudi = usModiAudi;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public String getExtArchivo() {
        return extArchivo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setExtArchivo(String extArchivo) {
        this.extArchivo = extArchivo;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSizeActaArchivoFormat() {
        return sizeActaArchivoFormat;
    }

    public void setSizeActaArchivoFormat(String sizeActaArchivoFormat) {
        this.sizeActaArchivoFormat = sizeActaArchivoFormat;
    }

    @Override
    public String toString() {
        return "ActaArchivo{" +
                "coActaArchivo='" + coActaArchivo + '\'' +
                ", archivo=" + archivo +
                ", noActaArchivo='" + noActaArchivo + '\'' +
                ", sizeActaArchivo='" + sizeActaArchivo + '\'' +
                ", sizeActaArchivoFormat='" + sizeActaArchivoFormat + '\'' +
                ", extArchivo='" + extArchivo + '\'' +
                ", contentType='" + contentType + '\'' +
                ", feCreaAudi='" + feCreaAudi + '\'' +
                ", usCreaAudi='" + usCreaAudi + '\'' +
                ", feModiAudi='" + feModiAudi + '\'' +
                ", usModiAudi='" + usModiAudi + '\'' +
                '}';
    }
}