package pe.gob.reniec.padronn.logic.dao;


import pe.gob.reniec.padronn.logic.model.Lugar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jronal
 * Date: 29/05/13
 * Time: 11:23 AM
 */
public interface ReporteGraficoDao {
    // numero de registros por departamentos
    List<Map<String, Object>> getNuRegistrosPN();

    // numero de registros por departamento
    List<Map<String, Object>> getNuRegistrosPN(String coDepartamento);

    // numero de registros por provincia
    List<Map<String, Object>> getNuRegistrosPN(String coDep, String coPrv);

    // get total de registros
    long getTotalRegistrosPN();

    /**
     * Porcentaje
     * @return
     */
    List<Map<String, Object>> getPcRegistrosPN();
    List<Map<String, Object>> getPcRegistrosPN(String coDep);
    List<Map<String, Object>> getPcRegistrosPN(String coDep, String coPrv);


}