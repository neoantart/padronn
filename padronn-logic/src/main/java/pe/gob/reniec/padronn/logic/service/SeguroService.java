package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.ProgramaSocial;
import pe.gob.reniec.padronn.logic.model.Seguro;

/**
 * Creado por: aquispej
 * Date: 19/12/18
 */
public interface SeguroService {

    Seguro obtenerPorCodigo(String codigo);

    Dominio obtenerPorCodigoEnDominio(String codigo);

}
