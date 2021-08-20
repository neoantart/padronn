package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.EntidadDao;
import pe.gob.reniec.padronn.logic.dao.UbigeoDao;
import pe.gob.reniec.padronn.logic.model.Entidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 21/09/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class EntidadDaoImpl extends AbstractBaseDao implements EntidadDao {

    @Autowired
    UbigeoDao ubigeoDao;

    @Override
    public boolean insert(Entidad entidad) {
        // 1 Municipio
        // 2 Entidad Minsa
        if(buscarEntidadIguales(entidad.getDeEntidad(), entidad.getDeEntidadLarga(), entidad.getCoUbigeoInei()) == null ) {
            String query = "" +
                    "insert into pntm_entidad " +
                    "  (co_entidad, de_entidad, usu_crea_audi, fe_crea_audi, co_tipo_entidad, de_entidad_larga, co_ubigeo_inei) " +
                    "values " +
                    "  (?, ?, ?, sysdate, ?, ?, ?)";
            String coEntidad = getNextValCoEntidad();
            Object[] params = new Object[]{coEntidad, entidad.getDeEntidad(), entidad.getUsuCreaAudi(), entidad.getCoTipoEntidad(), entidad.getDeEntidadLarga(), entidad.getCoUbigeoInei()};
//            logger.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
            int result = jdbcTemplate.update(query, params);
            return result >= 1;
        }
        return false;
    }

    @Override
    public boolean update(Entidad entidad) {
        String query = "" +
                "update pntm_entidad " +
                "   set " +
                "       de_entidad = ?, " +
                "       usu_modi_audi = ?, " +
                "       fe_modi_audi = sysdate, " +
                "       co_tipo_entidad = ?, " +
                "       de_entidad_larga = ?, " +
                "       co_ubigeo_inei = ? " +
                " where co_entidad = ? ";
        Object[] params = new Object[]{entidad.getDeEntidad(), entidad.getUsuModiAudi(), entidad.getCoTipoEntidad(), entidad.getDeEntidadLarga(), entidad.getCoUbigeoInei(), entidad.getCoEntidad()};
//        logger.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
        int result = jdbcTemplate.update(query, params);
        return result >= 1;
    }

    @Override
    public List<Entidad> getAll() {
        String query = "" +
                "select co_entidad, " +
                "       de_entidad, " +
                "       usu_crea_audi, " +
                "       fe_crea_audi, " +
                "       usu_modi_audi, " +
                "       fe_modi_audi, " +
                "       co_tipo_entidad, " +
                "       de_entidad_larga, " +
                "       co_ubigeo_inei " +
                "  from pntm_entidad " +
                "where es_entidad='1' ";
        try{
//            logger.debug(String.format("DAO '%s'  por ejecutar", query));
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Entidad.class));
        } catch (EmptyResultDataAccessException ee){
            ee.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Entidad> getAll(int filaIni, int filaFin) {
        String query = "" +
                "select * from (select rownum as fila, t.* from  (select co_entidad, " +
                "       de_entidad, " +
                "       usu_crea_audi, " +
                "       fe_crea_audi, " +
                "       usu_modi_audi, " +
                "       fe_modi_audi, " +
                "       co_tipo_entidad, " +
                "       de_entidad_larga, " +
                "       co_ubigeo_inei " +
                "  from pntm_entidad where es_entidad='1' ) t) " +
                "  where fila between ? and ? " +
                "  order by co_ubigeo_inei";
        Object[] params = new Object[]{filaIni, filaFin};
        try{
//            logger.debug(String.format("DAO '%s' con '%s' por ejecutar", query, params));
            List<Entidad> list = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Entidad.class), params);
            List<Entidad> retorno = new ArrayList<Entidad>();
            for(Entidad entidad: list) {
                entidad.setDeUbigeoInei(ubigeoDao.obtenerDeUbigeoInei(entidad.getCoUbigeoInei()));
                retorno.add(entidad);
            }
            return retorno;
        } catch (EmptyResultDataAccessException ee){
            ee.printStackTrace();
            return null;
        }
    }

    @Override
    public Entidad getEntidad(String coEntidad) {
        String query = "" +
                "select co_entidad, " +
                "       de_entidad, " +
                "       usu_crea_audi, " +
                "       fe_crea_audi, " +
                "       usu_modi_audi, " +
                "       fe_modi_audi, " +
                "       co_tipo_entidad, " +
                "       de_entidad_larga, " +
                "       co_ubigeo_inei " +
                "  from pntm_entidad " +
                " where co_entidad = ? and es_entidad='1' ";
        try{
            Object[] params = new Object[]{coEntidad};
           logger.debug(String.format("DAO '%s' con '%s' por ejecutar entidad", query, ArrayUtils.toString(params)));
            Entidad entidad = jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Entidad.class), params);
            entidad.setDeUbigeoInei(ubigeoDao.obtenerDeUbigeoInei(entidad.getCoUbigeoInei()));
            return entidad;
        } catch (EmptyResultDataAccessException ee){
            ee.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException ie){
            ie.printStackTrace();
            return null;
        }
    }

    private String getNextValCoEntidad() {
        String query = "select nvl(max(co_entidad),1)+1 from pntm_entidad";
//        logger.debug(String.format("DAO '%s' por ejecutar", query));
        return Integer.toString(jdbcTemplate.queryForInt(query));
    }

    //@Override


    @Override
    public List<Entidad> buscarEntidad(String deEntidad) {
        deEntidad = "%" + deEntidad + "%";
        String query = "" +
                "select co_entidad, " +
                "       de_entidad, " +
                "       usu_crea_audi, " +
                "       fe_crea_audi, " +
                "       usu_modi_audi, " +
                "       fe_modi_audi, " +
                "       co_tipo_entidad, " +
                "       de_entidad_larga, " +
                "       co_ubigeo_inei " +
                "  from pntm_entidad " +
                " where de_entidad_larga like ? " +
                "or de_entidad like ? and es_entidad='1' ";
        try{
            Object[] params = new Object[]{deEntidad, deEntidad};
//            logger.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
            List<Entidad> result = jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Entidad.class), params);
            if(result.size() == 0) return null;
            List<Entidad> retorno = new ArrayList<Entidad>();
            for(Entidad entidad: result) {
                entidad.setDeUbigeoInei(ubigeoDao.obtenerDeUbigeoInei(entidad.getCoUbigeoInei()));
                retorno.add(entidad);
            }
            return retorno;
        } catch (EmptyResultDataAccessException ee){
            ee.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException ie){
            ie.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Entidad> buscarEntidadIguales(String deEntidad, String deEntidadLarga, String coUbigeoInei){
/*        deEntidad = deEntidad + "%";
        deEntidadLarga = "%" + deEntidadLarga + "%";*/
        String query = "" +
                "select co_entidad, " +
                "       de_entidad, " +
                "       usu_crea_audi, " +
                "       fe_crea_audi, " +
                "       usu_modi_audi, " +
                "       fe_modi_audi, " +
                "       co_tipo_entidad, " +
                "       de_entidad_larga, " +
                "       co_ubigeo_inei " +
                "  from pntm_entidad " +
                " where ((upper(de_entidad) = upper(trim(?)) " +
                "and co_ubigeo_inei=?) or (upper(de_entidad_larga) = upper(trim(?)) and co_ubigeo_inei=?)) and es_entidad='1' ";

        Object[] params = new Object[]{deEntidad, coUbigeoInei, deEntidadLarga, coUbigeoInei};
//        log.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
        try{
            List<Entidad> list = jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Entidad.class), params);
            if(list.size() == 0)
                return null;
            return list;
        } catch (EmptyResultDataAccessException ee){
            ee.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException ie){
            ie.printStackTrace();
            return null;
        }
    }

    @Override
    public int countAll() {
        String query = "" +
                "select count(1) from pntm_entidad where es_entidad='1' ";
        return jdbcTemplate.queryForInt(query);
    }


    @Override
    public List<Entidad> getDistritosEuropan() {
        String query = "" +
                "select co_entidad, " +
                "       de_entidad, " +
                "       usu_crea_audi, " +
                "       fe_crea_audi, " +
                "       usu_modi_audi, " +
                "       fe_modi_audi, " +
                "       co_tipo_entidad, " +
                "       de_entidad_larga, " +
                "       co_ubigeo_inei, " +
                "       co_entidad as id," +
                "       de_entidad_larga as text" +
                "  from pntm_entidad " +
                " where co_ubigeo_inei in (" + StringUtils.join(padronProperties.DISTRITOS_EUROPAN, ',') + ") and es_entidad='1' ";

        logger.debug(String.format("DAO '%s'", query));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Entidad.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
