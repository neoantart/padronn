package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.CentroEducativoDao;
import pe.gob.reniec.padronn.logic.dao.FrecuenciaAtencionDao;
import pe.gob.reniec.padronn.logic.model.CentroEducativo;
import pe.gob.reniec.padronn.logic.model.FrecuenciaAtencion;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FrecuenciaAtencionDaoImpl extends SimpleJdbcDaoBase implements FrecuenciaAtencionDao {

    @Override
    public FrecuenciaAtencion obtenerFrecuenciaAtencion(String coFrecuenciaAtencion) {

        FrecuenciaAtencion frecuenciaAtencion = new FrecuenciaAtencion();

        String query = "select " +
                "co_frecuencia_atencion, no_frecuencia_atencion, es_frecuencia_atencion " +
                "from pntm_frecuencia_atencion " +
                "where co_frecuencia_atencion = ?"+
                "and es_frecuencia_atencion = '1' ";
        try{
            frecuenciaAtencion = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(FrecuenciaAtencion.class), new Object[]{coFrecuenciaAtencion});
            return frecuenciaAtencion;
        }catch(EmptyResultDataAccessException e){
            logger.error("No se encontro ninguna fuente de datos " + e.getMessage());
            return frecuenciaAtencion;
        }catch(Exception e){
            logger.error("Error al obtener fuente de datos " + e.getMessage());
            e.printStackTrace();
            return frecuenciaAtencion;
        }
    }

    @Override
    public List<FrecuenciaAtencion> listarFrecuenciaAtencion() {

        List<FrecuenciaAtencion> frecuenciaAtencionList = new ArrayList<FrecuenciaAtencion>();

        String query = "select " +
                "co_frecuencia_atencion, no_frecuencia_atencion, es_frecuencia_atencion " +
                "from pntd_frecuencia_atencion " +
                "where es_frecuencia_atencion = '1' ";

        try{
            frecuenciaAtencionList = jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(FrecuenciaAtencion.class));
            return frecuenciaAtencionList;
        }catch(EmptyResultDataAccessException e){
            logger.error("No se encontro ninguna fuente de datos " + e.getMessage());
            return frecuenciaAtencionList;
        }catch(Exception e){
            logger.error("Error al obtener fuente de datos " + e.getMessage());
            e.printStackTrace();
            return frecuenciaAtencionList;
        }


    }

}
