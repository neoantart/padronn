package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.KeysDao;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.KeysService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase KeysServiceImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 21/06/13 04:24 PM
 */
@Service
public class KeysServiceImpl
		extends AbstractBaseService
		implements KeysService {

	@Autowired
	KeysDao keysDao;

	@Override
	public List<String> getTiProSocialKeys() {
		List<Map<String, Object>> keys;
		try {
			keys = keysDao.getTiProSocialKeys();
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			//e.printStackTrace();
			throw new RuntimeException("", new Throwable("ERR.KEYSSERVICE.DAE.CORRUPTDATABASE", e.getCause())); //sino existen provocarían comportamiento anómalo!
		}
		return getValues(keys);
	}

	private List<String> getCoDominio(String noDominio) {
		List<Map<String, Object>> keys;
		try {
			keys = keysDao.getCoDominio(noDominio);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			//e.printStackTrace();
			throw new RuntimeException("", new Throwable("ERR.KEYSSERVICE.DAE.CORRUPTDATABASE", e.getCause())); //sino existen provocarían comportamiento anómalo!
		}
		return getValues(keys);
	}

	@Override
	public List<String> getCoGeneroMenorKeys() {
		List<Map<String, Object>> keys = keysDao.getCoGeneroMenor();
		return getValues(keys);
	}

	@Override
	public List<String> getTiDocMenorKeys() {
		List<Map<String, Object>> keys = keysDao.getTiDocMenor();
		return getValues(keys);
	}

	@Override
	public List<String> getTiSeguroMenorKeys() {
		List<Map<String, Object>> keys = keysDao.getTiSeguroMenor();
		return getValues(keys);
	}

	@Override
	public List<String> getTiVinculoJefeKeys() {
		List<Map<String, Object>> keys = keysDao.getTiVinculoMenor();
		return getValues(keys);
	}

	@Override
	public List<String> getTiVinculoMadreKeys() {
		List<Map<String, Object>> keys = keysDao.getTiVinculoMenor();
		return getValues(keys);
	}

	@Override
	public List<String> getCoGraInstMadreKeys() {
		List<Map<String, Object>> keys = keysDao.getCoGraInstMadreKeys();
		return getValues(keys);
	}

	@Override
	public List<String> getCoLenMadreKeys() {
		List<Map<String, Object>> keys = keysDao.getCoLenMadreKeys();
		return getValues(keys);
	}

	private List<String>getValues(List<Map<String, Object>> mapList) {
		if(mapList==null)
			return new ArrayList<String>();

		List<String> list = new ArrayList<String>();
		for (Map<String, Object> map : mapList) {
			for (Object o : map.values()) {
				list.add(o.toString());
			}
		}
		return list;
	}
}
