package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.SeguroDao;
import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.Seguro;
/**
 * Creado por: aquispej
 * Date: 19/12/18
 */
@Repository
public class SeguroDaoImpl extends SimpleJdbcDaoBase implements SeguroDao {
    @Override
    public Seguro obtenerPorCodigo(String codigo) {
        String query="SELECT co_tipo_seguro, de_seguro, de_seguro_larga " +
                "FROM PNTR_TIPO_SEGURO " +
                "WHERE co_tipo_seguro=? AND es_seguro='1'";
        try{
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Seguro.class), new Object[]{codigo});
        }catch(EmptyResultDataAccessException e){
            return new Seguro();
        }catch(IncorrectResultSizeDataAccessException e){
            e.printStackTrace();
            return new Seguro();
        }
    }

    @Override
    public Dominio obtenerPorCodigoEnDominio(String codigo) {
        /*String query = "SELECT co_tipo_seguro as co_dominio, de_seguro||' ('||de_seguro_larga||')' as de_dom " +*/
        String query = "SELECT co_tipo_seguro as co_dominio, de_seguro_larga||' ('|| de_seguro ||')' as de_dom " +
                "FROM PNTR_TIPO_SEGURO " +
                "WHERE es_seguro='1' and co_tipo_seguro=?";
        try{
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Dominio.class), codigo);
        }catch(EmptyResultDataAccessException er){
            return new Dominio();
        }catch(IncorrectResultSizeDataAccessException er){
            er.printStackTrace();
            return new Dominio();
        }
    }

}
