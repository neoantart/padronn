package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.ProgramaSocialDao;
import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.ProgramaSocial;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 17/06/13
 * Time: 11:18 AM
 */
@Repository
public class ProgramaSocialDaoImpl extends SimpleJdbcDaoBase implements ProgramaSocialDao {

	@Override
	public ProgramaSocial obtenerPorCodigo(String codigo) {
		String query="select co_programa_social, de_programa, de_programa_larga " +
				"from pntm_programa_social " +
				"where co_programa_social=? and es_programa=1";
		try{
			return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(ProgramaSocial.class), new Object[]{codigo});
		}catch(EmptyResultDataAccessException e){
			return new ProgramaSocial();
		}catch(IncorrectResultSizeDataAccessException e){
			e.printStackTrace();
			return new ProgramaSocial();
		}
	}

	@Override
	public Dominio obtenerPorCodigoEnDominio(String codigo) {
		String query = "SELECT CO_PROGRAMA_SOCIAL as co_dominio, DE_PROGRAMA_LARGA||' ('||DE_PROGRAMA||')' as de_dom " +
				"FROM PNTM_PROGRAMA_SOCIAL " +
				"WHERE es_programa=1 and CO_PROGRAMA_SOCIAL=?";
		try{
			return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Dominio.class), codigo);
		}catch(EmptyResultDataAccessException e){
			return new Dominio();
		}catch(IncorrectResultSizeDataAccessException e){
			e.printStackTrace();
			return new Dominio();
		}
	}

}
