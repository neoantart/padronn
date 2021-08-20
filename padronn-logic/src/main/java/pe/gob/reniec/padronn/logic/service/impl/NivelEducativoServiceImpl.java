package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.NivelEducativoDao;
import pe.gob.reniec.padronn.logic.model.NivelEducativo;
import pe.gob.reniec.padronn.logic.service.NivelEducativoService;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 17/06/13
 * Time: 04:10 PM
 */
@Service
public class NivelEducativoServiceImpl implements NivelEducativoService {

	@Autowired
	NivelEducativoDao nivelEducativoDao;

	@Override
	public NivelEducativo obtenerPorCodigo(String codigo) {
		return nivelEducativoDao.obtenerPorCodigo(codigo);
	}

    @Override
    public NivelEducativo obtenerPorCodigoDominio(String codigo) {
        return nivelEducativoDao.obtenerPorCodigoDominio(codigo);
    }
}
