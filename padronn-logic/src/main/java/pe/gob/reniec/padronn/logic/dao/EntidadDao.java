package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Entidad;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 21/09/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EntidadDao {
    boolean insert(Entidad entidad);
    boolean update(Entidad entidad);
    List<Entidad> getAll();
    List<Entidad> getAll(int filaIni, int filaFin);
    Entidad getEntidad(String coEntidad);

    List<Entidad> buscarEntidad(String deEntidad);

    List<Entidad> buscarEntidadIguales(String deEntidad, String deEntidadLarga, String coUbigeoInei);

    int countAll();

    List<Entidad> getDistritosEuropan();
}
