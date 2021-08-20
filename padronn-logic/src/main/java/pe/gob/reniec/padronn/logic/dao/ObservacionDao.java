package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Observacion;

/**
 * Clase ObservacionDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 15/07/13 10:22 AM
 */
public interface ObservacionDao {
  void addObservacion(Observacion observacion);

    String getNextCoObservacion();

    String getNextCoObservacionTipo();
}
