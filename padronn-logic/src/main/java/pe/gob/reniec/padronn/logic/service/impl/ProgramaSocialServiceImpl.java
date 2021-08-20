package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.ProgramaSocialDao;
import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.ProgramaSocial;
import pe.gob.reniec.padronn.logic.service.ProgramaSocialService;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 17/06/13
 * Time: 03:08 PM
 */
@Service
public class ProgramaSocialServiceImpl implements ProgramaSocialService {

	@Autowired
	ProgramaSocialDao programaSocialDao;

	@Override
	public ProgramaSocial obtenerPorCodigo(String codigo) {
		return programaSocialDao.obtenerPorCodigo(codigo);
	}

	@Override
	public Dominio obtenerPorCodigoEnDominio(String codigo) {
		return programaSocialDao.obtenerPorCodigoEnDominio(codigo);
	}
}
