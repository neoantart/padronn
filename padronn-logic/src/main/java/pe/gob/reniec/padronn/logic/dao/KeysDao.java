package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Objeto;

import java.util.List;
import java.util.Map;

/**
 * Clase KeysDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 21/06/13 04:21 PM
 */
public interface KeysDao {
	List<Map<String,Object>> getTiProSocialKeys();

	List<Map<String, Object>> getCoLenMadreKeys();

	List<Map<String, Object>> getCoGraInstMadreKeys();

	List<Map<String, Object>> getTiVinculoMenor();

	List<Map<String, Object>> getTiSeguroMenor();

	List<Map<String, Object>> getTiDocMenor();

	List<Map<String, Object>> getCoGeneroMenor();

	List<Map<String, Object>> getCoDominio(String noDominio);
}
