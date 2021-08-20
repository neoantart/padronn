package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.FuenteDatosDao;
import pe.gob.reniec.padronn.logic.model.FuenteDatos;
import pe.gob.reniec.padronn.logic.service.FuenteDatosService;

import java.util.List;

@Service
public class FuenteDatosServiceImpl implements FuenteDatosService {

    @Autowired
    FuenteDatosDao fuenteDatosDao;

    @Override
    public List<FuenteDatos> obtenerFuenteDatos() {
        return fuenteDatosDao.obtenerFuenteDatos();
    }

    @Override
    public FuenteDatos obtenerFuenteDatosPorCodigo(String coFuenteDatos) {
        return fuenteDatosDao.obtenerFuenteDatosPorCodigo(coFuenteDatos);
    }
}
