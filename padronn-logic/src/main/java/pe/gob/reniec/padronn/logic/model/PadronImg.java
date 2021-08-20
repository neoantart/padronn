package pe.gob.reniec.padronn.logic.model;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Clase PadronImg.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 25/05/13 03:48 PM
 */
public class PadronImg	implements Serializable {

	String coPadroNominal;
	byte[] imgFotoMenor;
	String usuCreaAudi;
	String usuModiAudi;
	String extFotoMenor; //Extension: jpg
	String ctyFotoMenor; //ContentType: image/jpg

	@Override
	public String toString() {
		return getFieldsExcept("imgFotoMenor");
	}

    public String getFieldsExcept(final String...args) {
        return (
                new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE) {
                    protected boolean accept(Field f) {
                        return super.accept(f) && !ArrayUtils.contains(args, f.getName());
                    }
                }
        ).toString();
    }

	public boolean isEmpty() {
		return imgFotoMenor==null || imgFotoMenor.length==0;
	}

	public PadronImg() {
		this.imgFotoMenor=null;
	}

	public PadronImg(PadronNominal padronNominal) throws IOException {
		coPadroNominal = padronNominal.getCoPadronNominal();
		usuCreaAudi = padronNominal.getUsCreaRegistro();
		usuModiAudi = padronNominal.getUsModiRegistro();
		imgFotoMenor = padronNominal.getImgFotoMenor().getBytes();
		ctyFotoMenor = padronNominal.getImgFotoMenor().getContentType();
		extFotoMenor = FilenameUtils.getExtension(padronNominal.getImgFotoMenor().getOriginalFilename());
	}

	public String getCoPadroNominal() {
		return coPadroNominal;
	}

	public void setCoPadroNominal(String coPadroNominal) {
		this.coPadroNominal = coPadroNominal;
	}

	public byte[] getImgFotoMenor() {
		return imgFotoMenor;
	}

	public void setImgFotoMenor(byte[] imgFotoMenor) {
		this.imgFotoMenor = imgFotoMenor;
	}

	public String getUsuCreaAudi() {
		return usuCreaAudi;
	}

	public void setUsuCreaAudi(String usuCreaAudi) {
		this.usuCreaAudi = usuCreaAudi;
	}

	public String getUsuModiAudi() {
		return usuModiAudi;
	}

	public void setUsuModiAudi(String usuModiAudi) {
		this.usuModiAudi = usuModiAudi;
	}

	public String getExtFotoMenor() {
		return extFotoMenor;
	}

	public void setExtFotoMenor(String extFotoMenor) {
		this.extFotoMenor = extFotoMenor;
	}

	public String getCtyFotoMenor() {
		return ctyFotoMenor;
	}

	public void setCtyFotoMenor(String ctyFotoMenor) {
		this.ctyFotoMenor = ctyFotoMenor;
	}
}
