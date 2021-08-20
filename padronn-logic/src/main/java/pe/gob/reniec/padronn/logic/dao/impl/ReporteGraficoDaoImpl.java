package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.LugarDao;
import pe.gob.reniec.padronn.logic.dao.ReporteGraficoDao;
import pe.gob.reniec.padronn.logic.model.Lugar;

import javax.management.monitor.StringMonitor;
import java.util.List;
import java.util.Map;

/**
 * User: jronal
 * Date: 29/05/13
 * Time: 11:57 AM
 */
@Repository
public class ReporteGraficoDaoImpl extends SimpleJdbcDaoBase implements ReporteGraficoDao {
    @Autowired
    LugarDao lugarDao;

    @Override
    public List<Map<String, Object>> getNuRegistrosPN() {
        String query =
                "(SELECT DISTINCT SUBSTR(CO_UBIGEO_INEI, 1, 2)                                AS co_departamento, " +
                "  f_get_des_dpto_ubig(SUBSTR(CO_UBIGEO_INEI, 1, 2)) AS de_departamento, " +
                "  COUNT(*)                                                                   AS cantidad " +
                "FROM pntv_padron_nominal " +
                "GROUP BY SUBSTR(CO_UBIGEO_INEI, 1, 2) " +
                ") " +
                "UNION " +
                "  (SELECT DISTINCT SUBSTR(CO_UBIGEO_INEI, 1, 2)                                AS co_departamento, " +
                "    f_get_des_dpto_ubig(SUBSTR(CO_UBIGEO_INEI, 1, 2)) AS de_departamento, " +
                "    0 " +
                "  FROM pntm_ubigeo_inei " +
                "  WHERE SUBSTR(CO_UBIGEO_INEI, 1, 2) NOT IN " +
                "    (SELECT DISTINCT SUBSTR(CO_UBIGEO_INEI, 1, 2) " +
                "    FROM pntv_padron_nominal " +
                "    GROUP BY SUBSTR(CO_UBIGEO_INEI, 1, 2) " +
                "    ) " +
                "  )";
        return jdbcTemplate.queryForList(query);
    }

    @Override
    public List<Map<String, Object>> getNuRegistrosPN(String coDepartamento) {
        String query =
                "(SELECT SUBSTR(CO_UBIGEO_INEI, 3, 2)                                                                 AS co_provincia, " +
                        "  F_GET_DES_PROV_UBIG(SUBSTR(CO_UBIGEO_INEI,1,2),SUBSTR(CO_UBIGEO_INEI,3,2)) AS de_provincia, " +
                        "  COUNT(*)                                                                                           AS cantidad " +
                        "FROM pntv_padron_nominal " +
                        "WHERE SUBSTR(CO_UBIGEO_INEI, 1, 2)=? " +
                        "GROUP BY SUBSTR(CO_UBIGEO_INEI, 1, 2), " +
                        "  SUBSTR(CO_UBIGEO_INEI, 3, 2) " +
                        ") " +
                        "UNION " +
                        "  (SELECT DISTINCT SUBSTR(CO_UBIGEO_INEI, 3, 2) AS co_provincia, " +
                        "    de_provincia , " +
                        "    0 " +
                        "  FROM pntm_ubigeo_inei " +
                        "  WHERE SUBSTR(CO_UBIGEO_INEI, 1, 2)    = ? " +
                        "  AND SUBSTR(CO_UBIGEO_INEI, 3, 2) NOT IN  " +
                        "    (SELECT SUBSTR(CO_UBIGEO_INEI, 3, 2) AS co_provincia " +
                        "    FROM pntv_padron_nominal " +
                        "    WHERE SUBSTR(CO_UBIGEO_INEI, 1, 2)=? " +
                        "    GROUP BY SUBSTR(CO_UBIGEO_INEI, 1, 2), " +
                        "      SUBSTR(CO_UBIGEO_INEI, 3, 2) " +
                        "    ) " +
                        "  )";
//        logger.info("Numero de registros por departamento coDepartamento="+coDepartamento);
        return jdbcTemplate.queryForList(query, new Object[]{coDepartamento, coDepartamento, coDepartamento});
    }

    @Override
    public List<Map<String, Object>> getNuRegistrosPN(String coDepartamento, String coProvincia) {
        String query =
                "(SELECT SUBSTR(CO_UBIGEO_INEI, 5, 2)                                                                                             AS co_distrito, " +
                "  F_GET_DES_DIST_UBIG(SUBSTR(CO_UBIGEO_INEI,1,2),SUBSTR(CO_UBIGEO_INEI,3,2), SUBSTR(CO_UBIGEO_INEI,5,2)) AS de_distrito, " +
                "  COUNT(*)                                                                                                                       AS cantidad " +
                "FROM pntv_padron_nominal " +
                "WHERE SUBSTR(CO_UBIGEO_INEI, 1, 2)=? " +
                "AND SUBSTR(CO_UBIGEO_INEI,3,2)    =? " +
                "GROUP BY SUBSTR(CO_UBIGEO_INEI, 1, 2), " +
                "  SUBSTR(CO_UBIGEO_INEI, 3, 2), " +
                "  SUBSTR(CO_UBIGEO_INEI, 5,2) " +
                ") " +
                "UNION " +
                "  (SELECT DISTINCT SUBSTR(CO_UBIGEO_INEI, 5, 2) AS co_distrito, " +
                "    de_distrito , " +
                "    0 " +
                "  FROM pntm_ubigeo_inei " +
                "  WHERE SUBSTR(CO_UBIGEO_INEI, 1, 2)    = ? " +
                "  AND SUBSTR(CO_UBIGEO_INEI,3,2)        =? " +
                "  AND SUBSTR(CO_UBIGEO_INEI, 5, 2) NOT IN " +
                "    (SELECT SUBSTR(CO_UBIGEO_INEI, 5, 2) AS co_distrito " +
                "    FROM pntv_padron_nominal " +
                "    WHERE SUBSTR(CO_UBIGEO_INEI, 1, 2)=? " +
                "    AND SUBSTR(CO_UBIGEO_INEI,3,2)    =? " +
                "    GROUP BY SUBSTR(CO_UBIGEO_INEI, 1, 2), " +
                "      SUBSTR(CO_UBIGEO_INEI, 3, 2), " +
                "      SUBSTR(CO_UBIGEO_INEI, 5,2) " +
                "    ) " +
                "  )";
//        logger.info("Numero de registros por departamento coDepartamento="+coDepartamento + " coProvincia="+coProvincia);
        return jdbcTemplate.queryForList(query, new Object[]{coDepartamento, coProvincia,coDepartamento, coProvincia,coDepartamento, coProvincia});
    }

    @Override
    public long getTotalRegistrosPN() {
        String query = "SELECT count(*) FROM PNTV_PADRON_NOMINAL";
        long result = jdbcTemplate.queryForLong(query);
//        logger.info("Total de registros PN: " + result);
        return result;
    }

    @Override
    public List<Map<String, Object>> getPcRegistrosPN() {
        List<Map<String, Object>> list = getNuRegistrosPN();
        return calculaPorcentajes(list);
    }

    @Override
    public List<Map<String, Object>> getPcRegistrosPN(String coDep) {
        List<Map<String, Object>> list = getNuRegistrosPN(coDep);
        return calculaPorcentajes(list);
    }

    @Override
    public List<Map<String, Object>> getPcRegistrosPN(String coDep, String coPrv) {
        List<Map<String, Object>> list = getNuRegistrosPN(coDep, coPrv);
        return calculaPorcentajes(list);
    }

    public List<Map<String, Object>> calculaPorcentajes(List<Map<String, Object>> list){
        double totalRegistros = (double) getTotalRegistrosPN();
        for(Map<String, Object> row: list){
            for(Map.Entry<String, Object> entry: row.entrySet()){
                if( "CANTIDAD".equals(entry.getKey()) ){
                    //entry.getValue().toString()
                    double val = Double.parseDouble(entry.getValue().toString());
                    double porcentaje = val / totalRegistros;
                    porcentaje = (double) Math.rint(porcentaje * 100) /100;
                    entry.setValue(porcentaje);
                }
                //System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }
        return list;
    }


}
