package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.GrupoDao;
import pe.gob.reniec.padronn.logic.dao.GrupoPermisoDao;
import pe.gob.reniec.padronn.logic.model.Grupo;
import pe.gob.reniec.padronn.logic.model.GrupoPermiso;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 06/09/13
 * Time: 04:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GrupoPermisoDaoImpl extends AbstractBaseDao implements GrupoPermisoDao {

    @Override
    public boolean insert(GrupoPermiso grupoPermiso) {
        String query = "" +
                "INSERT " +
                "INTO PNTM_GRUPO_PERMISO " +
                "  ( " +
                "    CO_GRUPO, " +
                "    DE_PERMISO, " +
                "    NO_MODULO, " +
                "    ES_PERMISO," +
                "    ES_GRUPO_PERMISO " +
                "  ) " +
                "  VALUES " +
                "  ( " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    '1' " +
                "  )";
        Object[] params = new Object[]{grupoPermiso.getCoGrupo(), grupoPermiso.getDePermiso(), grupoPermiso.getNoModulo(), grupoPermiso.getEsPermiso()};
        log.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        Integer result = jdbcTemplate.update(query, params);
        return result == 1;
    }

    @Override
    public boolean update(GrupoPermiso grupoPermiso) {
        String query = "" +
                "UPDATE PNTM_GRUPO_PERMISO " +
                "SET ES_PERMISO=?, " +
                "NO_MODULO=? " +
                "WHERE CO_GRUPO = ? " +
                " AND DE_PERMISO = ?";
        Object[] params = new Object[]{grupoPermiso.getEsPermiso(), grupoPermiso.getNoModulo(), grupoPermiso.getCoGrupo(), grupoPermiso.getDePermiso()};
        log.debug(String.format("DAO '%s' por ejecutar: con '%s'", query, ArrayUtils.toString(params)));
        int result = jdbcTemplate.update(query, params);
        return result >= 1;
    }

    @Override
    public List<GrupoPermiso> getAll() {
        String query = "" +
                "SELECT gp.CO_GRUPO, " +
                "  g.DE_GRUPO, " +
                "  gp.DE_PERMISO, " +
                "  gp.NO_MODULO, " +
                "  gp.ES_PERMISO " +
                "FROM PNTM_GRUPO_PERMISO gp " +
                "LEFT JOIN PNTM_GRUPO g ON gp.CO_GRUPO=g.CO_GRUPO AND g.es_grupo<>'9' AND gp.ES_GRUPO_PERMISO<>'9'" +
                "ORDER BY gp.CO_GRUPO, gp.DE_PERMISO";
        log.debug(String.format("DAO '%s' por ejecutar", query));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(GrupoPermiso.class));
    }

    @Override
    public GrupoPermiso getGrupoPermiso(String coGrupo, String dePermiso){
        String query = "" +
                "SELECT distinct gp.CO_GRUPO,  " +
                "  g.DE_GRUPO,  " +
                "  gp.DE_PERMISO,  " +
                "  gp.NO_MODULO,  " +
                "  gp.ES_PERMISO  " +
                "FROM PNTM_GRUPO_PERMISO gp  " +
                "LEFT JOIN PNTM_GRUPO g ON gp.CO_GRUPO=g.CO_GRUPO AND g.es_grupo<>'9' AND gp.ES_GRUPO_PERMISO<>'9' " +
                "WHERE gp.CO_GRUPO=? AND gp.DE_PERMISO=?";
        Object[] params = new Object[]{coGrupo, dePermiso};
//        log.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));

        return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(GrupoPermiso.class), params);
    }
}