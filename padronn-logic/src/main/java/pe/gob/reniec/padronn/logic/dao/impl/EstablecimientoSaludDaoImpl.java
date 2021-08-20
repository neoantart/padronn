package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.EstablecimientoSaludDao;
import pe.gob.reniec.padronn.logic.model.EstablecimientoSalud;
import pe.gob.reniec.padronn.logic.model.Objeto;
import pe.gob.reniec.padronn.logic.model.Usuario;

import javax.naming.event.ObjectChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class EstablecimientoSaludDaoImpl extends SimpleJdbcDaoBase implements EstablecimientoSaludDao {

    private static Logger logger = Logger.getLogger(EstablecimientoSaludDaoImpl.class.getName());

    @Autowired
    Usuario usuario;

	@Override
	public List<EstablecimientoSalud> listarPorDepartamento(String codigoDepartamento) {
		String query = "select co_est_salud, nu_secuencia_local, de_est_salud, ti_est_salud, de_departamento, de_provincia, de_distrito, de_direccion, co_ubigeo_inei " +
				"from pnvm_establecimiento_salud " +
				"where co_ubigeo_inei like ? " +
				"order by de_est_salud";
		Object[] params = new Object[]{codigoDepartamento+"%"};
//		logger.info("DAO '" + query + "' con '" + Arrays.toString(params)+ "' por ejecutar");
		return jdbcTemplate.query(query, params, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class));
	}

	@Override
	public EstablecimientoSalud obtenerPorCodigoRenaes(String codigoRenaes, String nuSecuencia) {
		String query = "" +
                "select distinct co_est_salud, nu_secuencia_local, de_est_salud, ' ' as ti_est_salud, de_departamento, de_provincia, de_distrito, de_direccion, co_ubigeo_inei " +
				"from pnvm_establecimiento_salud " +
				"where co_est_salud = ? and nu_secuencia_local=?";
        Object[] params = new Object[]{codigoRenaes, nuSecuencia};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
		try {
			return jdbcTemplate.queryForObject(query, params, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class));
		} catch (Exception e) {
            logger.error("Error al consultar RENAES:", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<EstablecimientoSalud> listarPorDatosEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
		nombreParcial=nombreParcial.toUpperCase()+"%";
		String query = "" +
                "select co_est_salud, nu_secuencia_local, de_est_salud, ti_est_salud, de_departamento, de_provincia, de_distrito, de_direccion, co_ubigeo_inei, '" + usuario.getCoUbigeoInei() + "' as coUbigeoUs "+
				"from pnvm_establecimiento_salud " +
				"where co_ubigeo_inei like ? and (de_est_salud like ? or de_departamento like ? or de_provincia like ? or de_distrito like ?) and ROWNUM <= ? " +
				"order by de_est_salud";
        Object[] params = new Object[]{codigoUbigeoInei+"%", "%"+nombreParcial, nombreParcial, nombreParcial, nombreParcial, cantidadResultados};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
		return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class), params);
	}

	@Override
	public List<EstablecimientoSalud> listarPorDatosFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
		nombreParcial=nombreParcial.toUpperCase()+"%";
		String query = "" +
                "select co_est_salud, nu_secuencia_local, de_est_salud, ti_est_salud, de_departamento, de_provincia, de_distrito, de_direccion, co_ubigeo_inei, '"  + usuario.getCoUbigeoInei() + "' as coUbigeoUs, '1' in_fuera_ubigeo "+
				"from pnvm_establecimiento_salud " +
				"where co_ubigeo_inei not like ? and (de_est_salud like ? or de_departamento like ? or de_provincia like ? or de_distrito like ?) and ROWNUM <= ? " +
				"order by de_est_salud";
        Object[] params = new Object[]{codigoUbigeoInei+"%", "%"+nombreParcial, nombreParcial, nombreParcial, nombreParcial, cantidadResultados};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
		return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class),params );
	}

    @Override
    public List<EstablecimientoSalud> listarPorCodigoUbigeoInei(String coUbigeoInei) {
        String query = "" +
                "select co_est_salud, " +
                "       nu_secuencia_local, " +
                "       de_est_salud, " +
                "       ti_est_salud, " +
                "       de_departamento, " +
                "       de_provincia, " +
                "       de_distrito, " +
                "       de_direccion, " +
                "       us_crea_audi, " +
                "       fe_crea_audi, " +
                "       us_modi_audi, " +
                "       fe_modi_audi, " +
                "       co_ubigeo_inei, " +
                "       co_disa, " +
                "       co_red, " +
                "       co_microred, " +
                "       de_disa, " +
                "       de_red, " +
                "       de_microred " +
                "  from pnvm_establecimiento_salud " +
                " where co_ubigeo_inei=?";
        Object[] params = new Object[]{coUbigeoInei};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(coUbigeoInei)));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class), params);
    }

    @Override
    public List<EstablecimientoSalud> listarPorRenaes(String coEstSalud) {
        coEstSalud = coEstSalud.trim();
        if (!StringUtils.isNumeric(coEstSalud))
            return new ArrayList<EstablecimientoSalud>();
        /*coEstSalud = coEstSalud.replaceFirst("^0+(?!$)", "");*/
        try {
            coEstSalud = String.format("%08d", Integer.parseInt(coEstSalud));
        } catch (NumberFormatException e) {
            return new ArrayList<EstablecimientoSalud>();
        }

        /*coEstSalud = "%" + coEstSalud;*/
        String query = "" +
                "select co_est_salud," +
                " de_est_salud," +
                " nu_secuencia_local," +
                " ti_est_salud," +
                " de_departamento," +
                " de_provincia," +
                " de_distrito," +
                " de_direccion," +
                " co_ubigeo_inei, '" + usuario.getCoUbigeoInei() + "' as coUbigeoUs "+
                "from pnvm_establecimiento_salud  " +
                "where co_est_salud = ?";
        Object[] params = new Object[]{coEstSalud};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(coEstSalud)));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class), params);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<EstablecimientoSalud>();
        } catch (Exception e) {
            return new ArrayList<EstablecimientoSalud>();
        }
    }
}



