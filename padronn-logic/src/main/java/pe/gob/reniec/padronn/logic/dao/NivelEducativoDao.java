package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.NivelEducativo;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 17/06/13
 * Time: 03:59 PM
 */
public interface NivelEducativoDao {

	NivelEducativo obtenerPorCodigo(String codigo);

    NivelEducativo obtenerPorCodigoDominio(String codigo);
}
