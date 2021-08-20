package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.ActaArchivosDao;
import pe.gob.reniec.padronn.logic.model.Acta;
import pe.gob.reniec.padronn.logic.model.ActaArchivos;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 15/08/13
 * Time: 10:02 AM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class ActaArchivosDaoImpl extends AbstractBaseDao implements ActaArchivosDao {
    @Override
    public boolean insert(ActaArchivos actaArchivos) {
        String query = "" +
                "INSERT INTO PNTV_ACTA_ARCHIVOS " +
                "  (CO_ACTA, CO_ACTA_ARCHIVO, ES_ACTA_ARCHIVOS " +
                "  ) VALUES " +
                "  (?, ?, ? " +
                "  )";
        log.debug("DAO: " + query + " por ejecutar");
        try {
            int resultado = this.jdbcTemplate.update(query,
                    actaArchivos.getCoActa(),
                    actaArchivos.getCoActaArchivo(),
                    actaArchivos.getEsActaArchivos()
            );
            return resultado == 1;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ActaArchivos getActaArchivos(String coActa, String coActaArchivo) {
        String query = "SELECT CO_ACTA, CO_ACTA_ARCHIVO FROM PNTV_ACTA_ARCHIVOS WHERE co_acta=? AND CO_ACTA_ARCHIVO=?";
        log.info("DAO " + query + " por ejecutar con: " + coActaArchivo );
        try{
            ActaArchivos actaArchivos = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(ActaArchivos.class), coActa, coActaArchivo);
            return actaArchivos;
        }
        catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ActaArchivos> getActaArchivosByCoActa(String coActa) {
        String query = "SELECT CO_ACTA, CO_ACTA_ARCHIVO FROM PNTV_ACTA_ARCHIVOS WHERE co_acta=? ";
        try{
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(ActaArchivos.class), coActa);
        }
        catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return null;
        }
        catch (IncorrectResultSizeDataAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ActaArchivos> getActaArchivosByCoActaArchivo(String coActaArchivo) {
        String query = "SELECT CO_ACTA, CO_ACTA_ARCHIVO FROM PNTV_ACTA_ARCHIVOS WHERE co_acta_archivo=? ";
        try{
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(ActaArchivos.class), coActaArchivo);
        }
        catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return null;
        }
        catch (IncorrectResultSizeDataAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(String coActa) {
        String query = "DELETE FROM PNTV_ACTA_ARCHIVOS WHERE CO_ACTA=?";
        this.jdbcTemplate.update(query, coActa);
        return true;
    }


    @Override
    public void deleteActaArchivos(String coActa) {
        String query = "" +
                "update pntv_acta_archivos " +
                "   set es_acta_archivos = '0' " +
                " where co_acta = ?";
        try {
            jdbcTemplate.update(query, coActa);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
