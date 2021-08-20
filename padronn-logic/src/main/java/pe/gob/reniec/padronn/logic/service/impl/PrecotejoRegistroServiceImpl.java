package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.padronn.logic.dao.PrecotejoRegistroDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.DominioService;
import pe.gob.reniec.padronn.logic.service.PrecotejoRegistroService;

/**
 * Clase PrecotejoRegistroServiceImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 03/08/13 05:08 PM
 */
@Service
public class PrecotejoRegistroServiceImpl
		extends AbstractBaseService
		implements PrecotejoRegistroService {

	@Autowired
	PrecotejoRegistroDao precotejoRegistroDao;

	@Autowired
	DominioService dominioService;

	@Autowired
	Usuario usuario;


	@Override
	public PrecotejoRegistro getPrecotejoRegistro(String coEntidad, String nuEnvio, String nuRegistro) {
		try {
			return precotejoRegistroDao.getPrecotejoRegistro(coEntidad, nuEnvio, nuRegistro);

		} catch(EmptyResultDataAccessException ersdae) {
			log.error(ersdae.getMessage());
			return new PrecotejoRegistro();

		} catch(IncorrectResultSizeDataAccessException irsdae) {
			log.error(irsdae.getMessage());
			irsdae.printStackTrace();
			return new PrecotejoRegistro();

		} catch(DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			return new PrecotejoRegistro();
		}
	}



	@Override
	public PrecotejoRegistro getPrecotejoRegistroObs(String coEntidad, String nuEnvio, String nuRegistro) {
		try {
			return precotejoRegistroDao.getPrecotejoRegistroObs(coEntidad, nuEnvio, nuRegistro);

		} catch(EmptyResultDataAccessException ersdae) {
			log.error(ersdae.getMessage());
			return new PrecotejoRegistro();

		} catch(IncorrectResultSizeDataAccessException irsdae) {
			log.error(irsdae.getMessage());
			irsdae.printStackTrace();
			return new PrecotejoRegistro();

		} catch(DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			return new PrecotejoRegistro();
		}
	}



	@Override
	@Transactional
	public boolean updatePrecotejoRegistro(PrecotejoRegistro precotejoRegistro) {

		beforeInsertOrUpdatePrecotejoRegistro(precotejoRegistro);
		try {
			precotejoRegistroDao.updatePrecotejoRegistro(precotejoRegistro);
			precotejoRegistroDao.updatePrecotejo(precotejoRegistro, false);
			return true;

		} catch(DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
		}
		return false;
	}

    @Override
    @Transactional
    public boolean updatePrecotejoRegistroObs(PrecotejoRegistro precotejoRegistro){
        beforeInsertOrUpdatePrecotejoRegistro(precotejoRegistro);
        try {
            precotejoRegistroDao.updatePrecotejoRegistro(precotejoRegistro);
            precotejoRegistroDao.updatePrecotejo(precotejoRegistro, false);
            return true;

        } catch(DataAccessException dae) {
            log.error(dae.getMessage());
            dae.printStackTrace();
        }
        return false;
    }



	@Override
	@Transactional
	public boolean insertPrecotejoRegistro(PrecotejoRegistro precotejoRegistro) {

		beforeInsertOrUpdatePrecotejoRegistro(precotejoRegistro);
		try {
			precotejoRegistroDao.insertPrecotejoRegistro(precotejoRegistro);
			precotejoRegistroDao.updatePrecotejo(precotejoRegistro, true);
			precotejoRegistroDao.deletePrecotejoRegistroObs(precotejoRegistro);
			return true;

		} catch(DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
		}
		return false;
	}



	private void beforeInsertOrUpdatePrecotejoRegistro(PrecotejoRegistro precotejoRegistro) {
		precotejoRegistro.updateTiProSocial();
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
		precotejoRegistro.setCoEntidad(coEntidad);
		precotejoRegistro.setUsCreaRegistro(usuario.getLogin());
		precotejoRegistro.setUsModiRegistro(usuario.getLogin());
		boolean existeDniMenor = precotejoRegistro.getNuDniMenor() != null && !precotejoRegistro.getNuDniMenor().isEmpty();
		precotejoRegistro.setTiDocIdentidad((existeDniMenor ? "1" : "3"));
		precotejoRegistro.cleanBeforeInsert();
	}
}
