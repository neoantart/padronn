package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.CentroEducativoDao;
import pe.gob.reniec.padronn.logic.model.CentroEducativo;
import pe.gob.reniec.padronn.logic.model.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Repository
public class CentroEducativoDaoImpl extends SimpleJdbcDaoBase implements CentroEducativoDao {

    @Autowired
    Usuario usuario;

	@Override
	public List<CentroEducativo> listarPorDepartamento(String codigoDepartamento) {
		String query = "select " +
				"co_centro_educativo, co_modular, no_centro_educativo, " +
				"co_ubigeo, de_departamento, de_provincia, de_distrito," +
				"co_centro_poblado, no_centro_poblado, " +
				"di_centro_educativo, de_nivel_educativo " +
				"from pntm_centro_educativo " +
				"where co_ubigeo like ? " +
				"AND ES_CENTRO_EDUCATIVO='1' ";
		Object[] params = new Object[]{codigoDepartamento+"%"};
		return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CentroEducativo.class), params);
	}

	@Override
	public CentroEducativo obtenerPorCodigo(String codigo) {
		String query = "select " +
				"co_centro_educativo, co_modular, no_centro_educativo, " +
				"co_ubigeo, de_ubigeo, de_departamento, de_provincia, de_distrito, " +
				"co_centro_poblado, no_centro_poblado, " +
				"di_centro_educativo, de_nivel_educativo " +
				"from pntm_centro_educativo " +
				"where co_centro_educativo = ? ";
		try{
			return jdbcTemplate.queryForObject(query, new Object[]{codigo}, BeanPropertyRowMapper.newInstance(CentroEducativo.class));
		}catch(EmptyResultDataAccessException e){
			return null;
		}catch(IncorrectResultSizeDataAccessException e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CentroEducativo> listarPorDatosEnDepartamento(String nombreParcial, String codigoDepartamento, int cantidadResultados) {
		nombreParcial=nombreParcial.toUpperCase()+"%";
		String query = "" +
                "select " +
				"co_centro_educativo, co_modular, no_centro_educativo, co_ubigeo, de_departamento, de_provincia, de_distrito," +
				"co_centro_poblado, no_centro_poblado, di_centro_educativo, de_nivel_educativo " +
				"from pntm_centro_educativo " +
				"where co_ubigeo like ? and " +
				"(no_centro_educativo like ? or de_departamento like ? or de_provincia like ? or de_distrito like ?) and ROWNUM <= ? " +
				"order by no_centro_educativo";

        Object[] params = new Object[]{codigoDepartamento+"%", "%"+nombreParcial, nombreParcial, nombreParcial, nombreParcial, cantidadResultados};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
		return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CentroEducativo.class), params);
	}

	@Override
	public List<CentroEducativo> listarPorDatosFueraDeDepartamento(String nombreParcial, String codigoDepartamento, int cantidadResultados) {

		nombreParcial=nombreParcial.toUpperCase()+"%";
		String query = "select " +
				"co_centro_educativo, co_modular, no_centro_educativo, co_ubigeo, de_departamento, de_provincia, de_distrito," +
				"co_centro_poblado, no_centro_poblado, di_centro_educativo, de_nivel_educativo, '1' in_fuera_ubigeo,  " + usuario.getCoUbigeoInei() + " as co_ubigeo_us " +
				"from pntm_centro_educativo " +
				"where co_ubigeo not like ? and (no_centro_educativo like ? or de_departamento like ? or de_provincia like ? or de_distrito like ?) and ROWNUM <= ? " +
				"order by no_centro_educativo";
        Object[] params = new Object[]{codigoDepartamento+"%", "%"+nombreParcial, nombreParcial, nombreParcial, nombreParcial, cantidadResultados};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CentroEducativo.class), params);
	}

    @Override
    public List<CentroEducativo> listarPorCodigoModular(String coModular) {
        coModular = coModular.trim();
        if (!org.apache.commons.lang3.StringUtils.isNumeric(coModular)) {
            return new ArrayList<CentroEducativo>();
        }
        coModular = String.format("%07d", Integer.parseInt(coModular));
        String query = "" +
                "select co_centro_educativo, " +
                "       co_modular, " +
                "       no_centro_educativo, " +
                "       co_ubigeo, " +
                "       de_departamento, " +
                "       de_provincia, " +
                "       de_distrito, " +
                "       co_centro_poblado, " +
                "       no_centro_poblado, " +
                "       di_centro_educativo, " +
                "       de_nivel_educativo " +
                "  from pntm_centro_educativo " +
                " where co_modular = ? ";

        Object[] params = new Object[]{coModular};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CentroEducativo.class), params);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<CentroEducativo>();
        } catch (Exception e) {
            return new ArrayList<CentroEducativo>();
        }
    }

}
