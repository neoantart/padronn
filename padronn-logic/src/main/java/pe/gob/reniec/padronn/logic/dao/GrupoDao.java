package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Grupo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 06/09/13
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */

public interface GrupoDao {
    boolean insert(Grupo grupo);
    boolean update(Grupo grupo);
    List<Grupo> getAll();

    Grupo getGrupo(String coGrupo);
}
