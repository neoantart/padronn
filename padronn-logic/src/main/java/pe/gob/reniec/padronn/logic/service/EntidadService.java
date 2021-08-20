package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.Entidad;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 21/09/13
 * Time: 01:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EntidadService {
    boolean insert(Entidad entidad);
    boolean update(Entidad entidad);
    Entidad getEntidad(String coEntidad);
    List<Entidad> getAll();
    List<Entidad> getAll(int filaIni, int filaFin);
    List<Entidad> buscarEntidad(String deEntidad);
    int countAll();
    List<Entidad> getDistritosEuropan();
}
