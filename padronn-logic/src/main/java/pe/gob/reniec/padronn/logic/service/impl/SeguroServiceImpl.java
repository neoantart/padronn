package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.SeguroDao;
import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.Seguro;
import pe.gob.reniec.padronn.logic.service.SeguroService;

/**
 * Creado por: aquispej
 * Date: 19/12/18
 */
@Service
public class SeguroServiceImpl implements SeguroService {

    @Autowired
    SeguroDao seguroDao;

    @Override
    public Seguro obtenerPorCodigo(String codigo) {
        return seguroDao.obtenerPorCodigo(codigo);
    }

    @Override
    public Dominio obtenerPorCodigoEnDominio(String codigo) {
        return seguroDao.obtenerPorCodigoEnDominio(codigo);
    }
}
