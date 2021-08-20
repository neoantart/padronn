package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.GrupoDao;
import pe.gob.reniec.padronn.logic.model.Grupo;
import pe.gob.reniec.padronn.logic.model.UsuarioExterno;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 06/09/13
 * Time: 04:15 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class GrupoDaoImpl extends AbstractBaseDao implements GrupoDao {

    @Override
    public boolean insert(Grupo grupo) {
        String query = "INSERT INTO PNTM_GRUPO " +
                "  (CO_GRUPO, DE_GRUPO, DE_DETALLE, ES_GRUPO " +
                "  ) VALUES " +
                "  (?, ?, ?, '1') ";
        Object[] params = new Object[]{getMaxCoGrupo(), grupo.getDeGrupo(), grupo.getDeDetalle()};
        log.debug(String.format("Dao '%s' por ejecutar, con: '%s'", query, params));
        int result = jdbcTemplate.update(query, getMaxCoGrupo(), grupo.getDeGrupo(), grupo.getDeDetalle());
        return result == 1;
    }

    @Override
    public boolean update(Grupo grupo) {
        Object[] params = new Object[]{grupo.getDeGrupo(), grupo.getDeDetalle(), grupo.getCoGrupo()};
        String query  = "UPDATE PNTM_GRUPO " +
                "SET de_grupo = ?, " +
                "de_detalle =? " +
                "WHERE CO_GRUPO = ? ";
        log.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        int result = jdbcTemplate.update(query, params);
        return result == 1;
    }

    @Override
    public List<Grupo> getAll() {
        String query = "SELECT CO_GRUPO, DE_GRUPO, DE_DETALLE FROM PNTM_GRUPO WHERE ES_GRUPO<>'9' ORDER BY CO_GRUPO";
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Grupo.class));
    }

    @Override
    public Grupo getGrupo(String coGrupo) {
        String query = "SELECT CO_GRUPO, DE_GRUPO, DE_DETALLE FROM PNTM_GRUPO WHERE CO_GRUPO=? AND ES_GRUPO<>'9'";
        Object[] params = new Object[]{coGrupo};
        return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Grupo.class), params);
    }

    private Integer getMaxCoGrupo() {
        String query = "select NVL(MAX(co_grupo),0)+1 from pntm_grupo ";
        return jdbcTemplate.queryForInt(query);
    }

}
