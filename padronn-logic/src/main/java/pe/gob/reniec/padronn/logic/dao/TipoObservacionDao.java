package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.TipoObservacion;

import java.util.List;

/**
 * Created by jfloresh on 28/04/2016.
 */
public interface TipoObservacionDao {
    List<TipoObservacion> obtenerTipoObservacion();

    String obtenerDeTipoObservacion(String coTipoObservacion);
}
