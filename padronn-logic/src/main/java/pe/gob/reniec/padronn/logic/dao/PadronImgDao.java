package pe.gob.reniec.padronn.logic.dao;

import org.springframework.dao.DataAccessException;
import pe.gob.reniec.padronn.logic.model.PadronImg;

/**
 * Clase PadronImgDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 25/05/13 02:23 PM
 */
public interface PadronImgDao {

	byte[] obtenerFotoMenor(String coPadronNominal);
	void clearPadronImg(String coPadronNominal);
	void insertarPadronImg(PadronImg padronImg);
	boolean tieneImagen(String coPadronNominal);
	PadronImg obtenerPadronImg(String coPadronNominal);
}
