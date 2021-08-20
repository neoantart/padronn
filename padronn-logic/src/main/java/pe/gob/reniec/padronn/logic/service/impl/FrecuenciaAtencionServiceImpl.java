package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.CentroEducativoDao;
import pe.gob.reniec.padronn.logic.dao.FrecuenciaAtencionDao;
import pe.gob.reniec.padronn.logic.model.CentroEducativo;
import pe.gob.reniec.padronn.logic.model.FrecuenciaAtencion;
import pe.gob.reniec.padronn.logic.service.FrecuenciaAtencionService;

import java.util.List;

@Service
public class FrecuenciaAtencionServiceImpl implements FrecuenciaAtencionService {

    @Autowired
    FrecuenciaAtencionDao frecuenciaAtencionDao;

    @Override
    public FrecuenciaAtencion obtenerFrecuenciaAtencion(String codigo) {
        return frecuenciaAtencionDao.obtenerFrecuenciaAtencion(codigo);
    }

    @Override
    public List<FrecuenciaAtencion> listarFrecuenciaAtencion() {
        return frecuenciaAtencionDao.listarFrecuenciaAtencion();
    }

}
