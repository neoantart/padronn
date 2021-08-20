package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.GrupoPermiso;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 06/09/13
 * Time: 04:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GrupoPermisoDao {
    boolean insert(GrupoPermiso grupoPermiso);
    boolean update(GrupoPermiso grupoPermiso);
    List<GrupoPermiso> getAll();

    GrupoPermiso getGrupoPermiso(String coGrupo, String dePermiso);
}
