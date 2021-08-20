package pe.gob.reniec.padronn.logic.model;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Clase AbstractBean.
 *
 * @author lmamani
 * @version 1.0.0
 * @since 16/05/13 04:02 PM
 */
public abstract class AbstractBean implements Serializable {

	Logger log = Logger.getLogger(getClass());

//	@Override
//	public String toString() {
//		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
//	}

	public String getFields(final String...args) {
		return (
				new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE) {
					protected boolean accept(Field f) {
						return super.accept(f) && ArrayUtils.contains(args, f.getName());
					}
				}
		).toString();
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

}
