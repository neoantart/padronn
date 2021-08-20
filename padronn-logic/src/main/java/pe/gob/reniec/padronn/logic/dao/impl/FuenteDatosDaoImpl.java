package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.FuenteDatosDao;
import pe.gob.reniec.padronn.logic.model.FuenteDatos;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FuenteDatosDaoImpl extends SimpleJdbcDaoBase implements FuenteDatosDao {
    @Override
    public List<FuenteDatos> obtenerFuenteDatos() {
        List<FuenteDatos> fuenteDatosList = new ArrayList<FuenteDatos>();

        String query= "select F.CO_FUENTE_DATOS, F.DE_FUENTE_DATOS, F.ES_FUENTE_DATOS, F.US_CREA_AUDI AS usCreaRegistro, \n"+
                    "TO_CHAR(F.FE_CREA_AUDI, 'dd/mm/yyyy') AS feCreaRegistro, F.US_MODI_AUDI AS usModiRegistro, \n"+
                    "        TO_CHAR(F.FE_MODI_AUDI, 'dd/mm/yyyy') AS feModiRegistro \n"+
                    "from PNTR_FUENTE_DATOS F \n"+
                    "WHERE F.ES_FUENTE_DATOS = '1'";
        try{
            fuenteDatosList = jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(FuenteDatos.class));
            return fuenteDatosList;
        }catch(EmptyResultDataAccessException e){
            logger.error("No se encontro ninguna fuente de datos " + e.getMessage());
            return fuenteDatosList;
        }catch(Exception e){
            logger.error("Error al obtener fuente de datos " + e.getMessage());
            e.printStackTrace();
            return fuenteDatosList;
        }
    }

    @Override
    public FuenteDatos obtenerFuenteDatosPorCodigo(String coFuenteDatos) {
        FuenteDatos fuenteDatos = new FuenteDatos();

        String query= "select F.CO_FUENTE_DATOS, F.DE_FUENTE_DATOS, F.ES_FUENTE_DATOS, F.US_CREA_AUDI AS usCreaRegistro, \n"+
                "TO_CHAR(F.FE_CREA_AUDI, 'dd/mm/yyyy') AS feCreaRegistro, F.US_MODI_AUDI AS usModiRegistro, \n"+
                "        TO_CHAR(F.FE_MODI_AUDI, 'dd/mm/yyyy') AS feModiRegistro \n"+
                "from PNTR_FUENTE_DATOS F \n"+
                "WHERE F.ES_FUENTE_DATOS = '1'" +
                "AND F.CO_FUENTE_DATOS=?";
        try{
            fuenteDatos = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(FuenteDatos.class), new Object[]{coFuenteDatos});
            return fuenteDatos;
        }catch(EmptyResultDataAccessException e){
            logger.error("No se encontro ninguna fuente de datos " + e.getMessage());
            return fuenteDatos;
        }catch(Exception e){
            logger.error("Error al obtener fuente de datos " + e.getMessage());
            e.printStackTrace();
            return fuenteDatos;
        }
    }
}
