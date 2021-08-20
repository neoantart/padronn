package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.KeysDao;
import pe.gob.reniec.padronn.logic.model.Objeto;

import java.util.List;
import java.util.Map;

/**
 * Clase KeysDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 21/06/13 04:09 PM
 */
@Repository
public class KeysDaoImpl
		extends AbstractBaseDao
		implements KeysDao {

	@Override
	public List<Map<String, Object>> getTiProSocialKeys() {
		String query = "" +
				"SELECT " +
				"CO_PROGRAMA_SOCIAL " +
				"FROM " +
				"PNTM_PROGRAMA_SOCIAL ";
		log.debug("DAO query='"+query+"' params=''");
		return jdbcTemplate.queryForList(query);
	}

    @Override
    public List<Map<String, Object>> getCoLenMadreKeys() {
        String query = "" +
                "SELECT CO_LENGUA \n" +
                "FROM   PNTR_LENGUA \n" +
                "WHERE ES_LENGUA='1'";
        log.debug(String.format("DAO '%s'", query));
        return jdbcTemplate.queryForList(query);
    }

    @Override
    public List<Map<String, Object>> getCoGraInstMadreKeys() {
        String query = "" +
                "select CO_GRADO_INSTRUCCION \n" +
                "from PNTR_GRADO_INSTRUCCION \n" +
                "where ES_GRADO_INSTRUCCION='1'";
        log.debug(String.format("DAO '%s'", query));
        return jdbcTemplate.queryForList(query);
    }

    @Override
    public List<Map<String, Object>> getTiVinculoMenor() {
        String query = "" +
                "SELECT CO_VINCULO \n" +
                "FROM PNTR_VINCULO_FAMILIAR \n" +
                "WHERE ES_VINCULO='1' ";
        log.debug(String.format("DAO '%s'", query));
        return jdbcTemplate.queryForList(query);
    }

    @Override
    public List<Map<String, Object>> getTiSeguroMenor() {
        String query = "" +
                "SELECT co_tipo_seguro \n" +
                "FROM pntr_tipo_seguro \n" +
                "WHERE es_seguro='1'";
        log.debug(String.format("DAO '%s'", query));
        return jdbcTemplate.queryForList(query);
    }

    @Override
    public List<Map<String, Object>> getTiDocMenor() {
        String query = "" +
                "select TI_DOC_IDENTIDAD \n" +
                "from PNTR_DOC_IDENTIDAD";
        log.debug(String.format("DAO '%s'", query));
        return jdbcTemplate.queryForList(query);
    }

    @Override
    public List<Map<String, Object>> getCoGeneroMenor() {
        String query = "" +
                "  SELECT CO_GENERO_MENOR \n" +
                "  FROM PNTR_GENERO_MENOR";
        log.debug(String.format("DAO '%s'", query));
        return jdbcTemplate.queryForList(query);
    }

    @Override
	public List<Map<String, Object>> getCoDominio(String noDominio) {
		String query = "" +
				"SELECT " +
				"CO_DOMINIO " +
				"FROM " +
				"PNTR_DOMINIOS " +
				"WHERE " +
				"no_dom=? ";
		Object[] params = {noDominio};
		log.debug("DAO query='"+query+"' params='"+ ArrayUtils.toString(params)+"'");
		return jdbcTemplate.queryForList(query, params);
	}

}
