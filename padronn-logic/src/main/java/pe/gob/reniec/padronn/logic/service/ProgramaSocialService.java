package pe.gob.reniec.padronn.logic.service;

import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.ProgramaSocial;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 17/06/13
 * Time: 03:08 PM
 */
public interface ProgramaSocialService {

	ProgramaSocial obtenerPorCodigo(String codigo);

	Dominio obtenerPorCodigoEnDominio(String codigo);

}
