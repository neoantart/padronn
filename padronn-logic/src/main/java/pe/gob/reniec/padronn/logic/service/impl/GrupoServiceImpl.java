package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.GrupoDao;
import pe.gob.reniec.padronn.logic.model.Grupo;
import pe.gob.reniec.padronn.logic.service.GrupoService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 06/09/13
 * Time: 05:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GrupoServiceImpl implements GrupoService {
    @Autowired
    GrupoDao grupoDao;

    @Override
    public boolean insert(Grupo grupo) {
        return grupoDao.insert(grupo);
    }

    @Override
    public boolean update(Grupo grupo) {
        return grupoDao.update(grupo);
    }

    @Override
    public List<Grupo> getAll() {
        return grupoDao.getAll();
    }

    @Override
    public Grupo getGrupo(String coGrupo) {
        return grupoDao.getGrupo(coGrupo);
    }
}
