package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.Seguro;

public interface SeguroDao {
    Seguro obtenerPorCodigo(String codigo);
    Dominio obtenerPorCodigoEnDominio(String codigo);
}
