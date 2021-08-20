package pe.gob.reniec.padronn.logic.service;

import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;

/**
 * Clase PrecotejoRegistroService.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 03/08/13 05:12 PM
 */
public interface PrecotejoRegistroService {
	PrecotejoRegistro getPrecotejoRegistro(String coEntidad, String nuEnvio, String nuRegistro);

	boolean updatePrecotejoRegistro(PrecotejoRegistro precotejoRegistro);

	PrecotejoRegistro getPrecotejoRegistroObs(String coEntidad, String nuEnvio, String nuRegistro);

    @Transactional
    boolean updatePrecotejoRegistroObs(PrecotejoRegistro precotejoRegistro);

    @Transactional
	boolean insertPrecotejoRegistro(PrecotejoRegistro precotejoRegistro);
}
