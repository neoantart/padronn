package pe.gob.reniec.padronn.logic.model.form;

import pe.gob.reniec.padronn.logic.model.AbstractBean;

/**
 * Clase AbstractForm.
 *
 * @author lmamani
 * @version 1.0.0
 * @since 16/05/13 04:07 PM
 */
public abstract class AbstractForm
		extends AbstractBean {

	String action;

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

}
