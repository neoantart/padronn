package pe.gob.reniec.padronn.logic.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

/**
 * Clase AbstractBaseDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 04:40 PM
 */
public abstract class AbstractBaseDao	extends SimpleJdbcDaoBase {

	protected Logger log = Logger.getLogger(AbstractBaseDao.class);

	@Autowired
	public PadronProperties padronProperties;
}
