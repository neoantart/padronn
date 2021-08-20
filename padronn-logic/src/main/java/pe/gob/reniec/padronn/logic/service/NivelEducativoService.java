package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.NivelEducativo;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 17/06/13
 * Time: 04:10 PM
 */
public interface NivelEducativoService {

	NivelEducativo obtenerPorCodigo(String codigo);

    NivelEducativo obtenerPorCodigoDominio(String codigo);
}
