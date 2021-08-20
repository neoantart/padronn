package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.Grupo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 06/09/13
 * Time: 05:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GrupoService {
    boolean insert(Grupo grupo);
    boolean update(Grupo grupo);
    List<Grupo> getAll();

    Grupo getGrupo(String coGrupo);
}
