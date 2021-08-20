package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.ProgramaSocial;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 17/06/13
 * Time: 11:17 AM
 */
public interface ProgramaSocialDao {

	ProgramaSocial obtenerPorCodigo(String codigo);

	Dominio obtenerPorCodigoEnDominio(String codigo);

}
