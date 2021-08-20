package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.RcDao;
import pe.gob.reniec.padronn.logic.model.Persona;
import pe.gob.reniec.padronn.logic.service.RcService;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 09/06/13
 * Time: 07:51 PM
 */
@Service
public class RcServiceImpl implements RcService {

	@Autowired
	RcDao rcDao;

	@Override
	public Persona obtenerPorCnv(String cnv) {
        try {
            return rcDao.obtenerPorCnv(cnv);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
