package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.MovimientoPasswordDao;
import pe.gob.reniec.padronn.logic.model.MovimientoPassword;

@Repository
public class MovimientoPasswordDaoImpl extends SimpleJdbcDaoBase implements MovimientoPasswordDao {

    @Override
    public void insertMovimientoPw(MovimientoPassword movimientoPassword) {
        String query = "" +
                "INSERT INTO PNTV_MOVIMIENTO_PW (CO_USUARIO, \n" +
                            "DE_PASSWORD, \n" +
                            "CO_TIPO_ACCION_PW, \n" +
                            "NU_SEC_PW, \n" +
                            "US_MODI_PW, \n" +
                            "FE_PASSWORD) \n" +
                    "VALUES(?, \n" +
                    "       ?, \n" +
                    "       ?, \n" +
                    "            (SELECT NVL(MAX(MP.NU_SEC_PW), 0) + 1 \n" +
                    "            FROM PNTV_MOVIMIENTO_PW MP \n" +
                    "            WHERE MP.CO_USUARIO = ?), \n" +
                    "       ?, \n" +
                    "            SYSDATE)";

        Object[] params = new Object[] {
                movimientoPassword.getCoUsuario(),
                movimientoPassword.getDePassword(),
                movimientoPassword.getCoTipoAccionPw(),
                movimientoPassword.getCoUsuario(),
                movimientoPassword.getUsModiPw()
        };

        logger.debug(String.format("insertMovimientoPw DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try{
            jdbcTemplate.update(query, params);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }
}
