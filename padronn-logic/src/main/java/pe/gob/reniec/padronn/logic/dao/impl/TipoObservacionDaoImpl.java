package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.TipoObservacionDao;
import pe.gob.reniec.padronn.logic.model.TipoObservacion;

import java.util.List;

/**
 * Created by jfloresh on 28/04/2016.
 */
@Repository
public class TipoObservacionDaoImpl extends SimpleJdbcDaoBase implements TipoObservacionDao {

    private static final String OBTENER_TIPO_OBSERVACION = "" +
            "SELECT CO_TIPO_OBSERVACION,\n" +
            "       DE_TIPO_OBSERVACION,\n" +
            "       ES_TIPO_OBSERVACION,\n" +
            "       US_CREA_AUDI,\n" +
            "       FE_CREA_AUDI,\n" +
            "       US_MODI_AUDI,\n" +
            "       FE_MODI_AUDI,\n" +
            "       NU_ORDEN\n" +
            "  FROM PNTR_TIPO_OBSERVACION\n" +
            "WHERE ES_TIPO_OBSERVACION='1'  \n" +
            "ORDER BY NU_ORDEN  \n";

    private static final String OBTENER_DE_TIPO_OBSERVACION = "" +
            "SELECT DE_TIPO_OBSERVACION FROM PNTR_TIPO_OBSERVACION\n" +
            "WHERE ES_TIPO_OBSERVACION='1' AND CO_TIPO_OBSERVACION=?";

    private static Logger LOG = Logger.getLogger(TipoObservacionDaoImpl.class);

    @Override
    public List<TipoObservacion> obtenerTipoObservacion() {
        LOG.info("Inicia metodo obtenerTipoObservacion");
        String sql = OBTENER_TIPO_OBSERVACION;
        try {
            LOG.debug(String.format("DAO '%s'", sql));
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TipoObservacion.class));
        } catch (Exception e) {
            LOG.error("Error", e);
            return null;
        }
    }

    @Override
    public String obtenerDeTipoObservacion(String coTipoObservacion) {
        String sql = OBTENER_DE_TIPO_OBSERVACION;
        try {
            Object[] params = new Object[]{coTipoObservacion};
            LOG.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(coTipoObservacion)));
            return jdbcTemplate.queryForObject(sql, String.class, params);
        } catch (Exception e) {
            //LOG.error("Error:", e);
            return "";
        }
    }

}