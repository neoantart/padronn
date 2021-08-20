package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.GrupoPermiso;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 06/09/13
 * Time: 05:40 PM
 * To change this template use File | Settings | File Templates.
 */

public interface GrupoPermisoService {
    boolean insert(GrupoPermiso grupoPermiso);
    boolean update(GrupoPermiso grupoPermiso);
    List<GrupoPermiso> getAll();

    GrupoPermiso getGrupoPermiso(String coGrupo, String dePermiso);

    Map<Integer, String> getGrupos();
}
