package pe.gob.reniec.padronn.logic.dao;

import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;

/**
 * Clase PrecotejoRegistroDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 03/08/13 04:55 PM
 */
public interface PrecotejoRegistroDao {
	PrecotejoRegistro getPrecotejoRegistro(String coEntidad, String nuEnvio, String nuRegistro);

	@Transactional
	void updatePrecotejoRegistro(PrecotejoRegistro precotejoRegistro);

	PrecotejoRegistro getPrecotejoRegistroObs(String coEntidad, String nuEnvio, String nuRegistro);

    void updatePrecotejoRegistroObs(PrecotejoRegistro precotejoRegistro);

    void updatePrecotejo(PrecotejoRegistro precotejoRegistro, boolean updateRegistrosObservados);

	void deletePrecotejoRegistroObs(PrecotejoRegistro precotejoRegistro);

	void insertPrecotejoRegistro(PrecotejoRegistro precotejoRegistro);
}
