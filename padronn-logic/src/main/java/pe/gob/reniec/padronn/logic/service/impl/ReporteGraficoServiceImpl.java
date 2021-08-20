package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.LugarDao;
import pe.gob.reniec.padronn.logic.model.Lugar;
import pe.gob.reniec.padronn.logic.service.ReporteGraficoService;
import pe.gob.reniec.padronn.logic.dao.ReporteGraficoDao;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jronal
 * Date: 30/05/13
 * Time: 06:24 PM
 */

@Service
public class  ReporteGraficoServiceImpl implements ReporteGraficoService {

    @Autowired
    ReporteGraficoDao reporteGraficoDao;

    @Autowired
    LugarDao lugarDao;

    @Override
    public List<Map<String, Object>> getNuRegistrosPN() {
        return reporteGraficoDao.getNuRegistrosPN();
    }

    @Override
    public List<Map<String, Object>> getNuRegistrosPN(String coDepartamento) {
        return reporteGraficoDao.getNuRegistrosPN(coDepartamento);
    }

    @Override
    public List<Map<String, Object>> getNuRegistrosPN(String coDepartamento, String coProvincia) {
        return reporteGraficoDao.getNuRegistrosPN(coDepartamento, coProvincia);
    }

    @Override
    public List<Map<String, Object>> getPcRegistrosPN() {
        return reporteGraficoDao.getPcRegistrosPN();
    }

    @Override
    public List<Map<String, Object>> getPcRegistrosPN(String coDepartamento) {
        return reporteGraficoDao.getPcRegistrosPN(coDepartamento);
    }

    @Override
    public List<Map<String, Object>> getPcRegistrosPN(String coDepartamento, String coProvincia) {
        return reporteGraficoDao.getPcRegistrosPN(coDepartamento, coProvincia);
    }

    @Override
    public List<Lugar> getDepartamentos(){
        return lugarDao.getDepartamentos();
    }

    @Override
    public List<Lugar> getProvincias(String coDepartamento) {
        return lugarDao.getProvincias(coDepartamento);
    }

    @Override
    public List<Lugar> getDistritos(String coDepartamento, String coProvincias) {
        return lugarDao.getDistritos(coDepartamento, coProvincias);
    }
}
