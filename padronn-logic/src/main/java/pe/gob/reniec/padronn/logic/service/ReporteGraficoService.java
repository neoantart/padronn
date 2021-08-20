package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.Lugar;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrador
 * Date: 30/05/13
 * Time: 06:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReporteGraficoService {
    // numero de registros por departamentos
    List<Map<String, Object>> getNuRegistrosPN();

    // numero de registros por departamento
    List<Map<String, Object>> getNuRegistrosPN(String coDepartamento);

    // numero de registros por provincia
    List<Map<String, Object>> getNuRegistrosPN(String coDep, String coPrv);

    /**
     * Porcentaje
     * @return
     */
    List<Map<String, Object>> getPcRegistrosPN();
    List<Map<String, Object>> getPcRegistrosPN(String coDep);
    List<Map<String, Object>> getPcRegistrosPN(String coDep, String coPrv);

    /**
     * ubigeo
     * @return
     */
    List<Lugar> getDepartamentos();
    List<Lugar> getProvincias(String coDepartamento);
    List<Lugar> getDistritos(String coDepartamento, String coProvincias);
}
