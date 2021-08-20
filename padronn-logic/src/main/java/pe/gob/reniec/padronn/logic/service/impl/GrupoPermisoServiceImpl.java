package pe.gob.reniec.padronn.logic.service.impl;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.GrupoDao;
import pe.gob.reniec.padronn.logic.dao.GrupoPermisoDao;
import pe.gob.reniec.padronn.logic.model.Grupo;
import pe.gob.reniec.padronn.logic.model.GrupoPermiso;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.GrupoPermisoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 06/09/13
 * Time: 05:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GrupoPermisoServiceImpl extends AbstractBaseService implements GrupoPermisoService {
    @Autowired
    GrupoPermisoDao grupoPermisoDao;

    @Autowired
    GrupoDao grupoDao;

    @Override
    public boolean insert(GrupoPermiso grupoPermiso) {
        return grupoPermisoDao.insert(grupoPermiso);
    }

    @Override
    public boolean update(GrupoPermiso grupoPermiso) {
        return grupoPermisoDao.update(grupoPermiso);
    }

    @Override
    public List<GrupoPermiso> getAll() {
        return grupoPermisoDao.getAll();
    }

    @Override
    public GrupoPermiso getGrupoPermiso(String coGrupo, String dePermiso){
        return grupoPermisoDao.getGrupoPermiso(coGrupo, dePermiso);
    }

    @Override
    public Map<Integer, String> getGrupos() {
        List<Grupo> list = grupoDao.getAll();
        Map<Integer, String> result = new HashMap<Integer, String>();
        for(Grupo grupo: list){
            result.put(Integer.parseInt(grupo.getCoGrupo()), grupo.getDeGrupo());
        }
        // Sort by key
        Map<Integer, String> treeMap = new TreeMap<Integer, String>(result);
        return treeMap;
    }

}