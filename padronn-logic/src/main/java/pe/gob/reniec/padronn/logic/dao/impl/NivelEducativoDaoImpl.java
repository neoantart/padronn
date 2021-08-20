package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.NivelEducativoDao;
import pe.gob.reniec.padronn.logic.model.NivelEducativo;

@Repository
public class NivelEducativoDaoImpl extends SimpleJdbcDaoBase implements NivelEducativoDao {

	@Override
	public NivelEducativo obtenerPorCodigo(String codigo) {
		String query="select co_nivel_educa, de_nivel_educa " +
				"from getr_nivel_educativo " +
				"where co_nivel_educa=? and es_nivel_educa=1";
		try{
			return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(NivelEducativo.class), codigo);
		}catch(EmptyResultDataAccessException e){
			return new NivelEducativo();
		}catch(IncorrectResultSizeDataAccessException e){
			e.printStackTrace();
			return new NivelEducativo();
		}
	}

    @Override
    public NivelEducativo obtenerPorCodigoDominio(String codigo) {
        String query = "select co_grado_instruccion as  co_nivel_educa, de_grado_instruccion as  de_nivel_educa from PNTR_GRADO_INSTRUCCION where co_grado_instruccion=?";
        try {
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(NivelEducativo.class), codigo);
        } catch (EmptyResultDataAccessException e) {
            return new NivelEducativo();
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return new NivelEducativo();
        }
    }
}
