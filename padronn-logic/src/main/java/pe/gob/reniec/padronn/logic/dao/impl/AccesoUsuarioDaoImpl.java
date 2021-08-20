package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.AccesoUsuarioDao;
import pe.gob.reniec.padronn.logic.model.Usuario;

/**
 * Created by jfloresh on 26/02/2015.
 */



@Repository
public class AccesoUsuarioDaoImpl extends SimpleJdbcDaoBase implements AccesoUsuarioDao {

    @Override
    public Boolean insert(Usuario usuario) {
        String query = "" +
                "insert into pntl_acceso_usuario " +
                "  (ip_maquina, fe_acceso, co_tipo_acceso, " +
                "   co_usuario, co_entidad, " +
                "   co_ubigeo_inei, co_tipo_entidad, us_crea_audi, " +
                "   fe_crea_audi) " +
                "values " +
                "  (?, sysdate, ?, " +
                "   ?, ?, " +
                "   ?, ?, 'RENIEC', " +
                " sysdate)";


        Object[] params = new Object[]{
                usuario.getIpMaquina(), usuario.getCoTipoAcceso(),
                usuario.getLogin(), usuario.getCoEntidad(),
                usuario.getCoUbigeoInei(), usuario.getCoTipoEntidad(),
        };
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.update(query, params) == 1;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}