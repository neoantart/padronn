package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.EntidadDao;
import pe.gob.reniec.padronn.logic.model.Entidad;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.EntidadService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 21/09/13
 * Time: 01:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EntidadServiceImpl extends AbstractBaseService implements EntidadService {

    @Autowired
    EntidadDao entidadDao;

    @Override
    public boolean insert(Entidad entidad) {
        return entidadDao.insert(entidad);
    }

    @Override
    public boolean update(Entidad entidad) {
        return entidadDao.update(entidad);
    }

    @Override
    public Entidad getEntidad(String coEntidad) {
        return entidadDao.getEntidad(coEntidad);
    }

    @Override
    public List<Entidad> getAll() {
        return entidadDao.getAll();
    }

    @Override
    public List<Entidad> getAll(int filaIni, int filaFin) {
        return entidadDao.getAll(filaIni, filaFin);
    }

    @Override
    public List<Entidad> buscarEntidad(String deEntidad){
        return entidadDao.buscarEntidad(deEntidad);
    }


    @Override
    public int countAll() {
        return entidadDao.countAll();
    }

    @Override
    public List<Entidad> getDistritosEuropan() {
        try {
            List<Entidad> entidadList = new ArrayList<Entidad>();
            Entidad entidad = new Entidad();
            entidad.setId("0");
            entidad.setText("TODOS LOS DISTRITOS DE EUROPAN");
            entidadList.add(entidad);
            entidadList.addAll(entidadDao.getDistritosEuropan());
            return entidadList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
