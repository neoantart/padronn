package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.reniec.padronn.logic.service.ReporteGraficoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrador
 * Date: 30/05/13
 * Time: 06:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "reportegrafico")
public class ReporteGraficoController {
    @Autowired
    ReporteGraficoService reporteGraficoService;

    Logger logger = Logger.getLogger(RegistroManualController.class);

    @RequestMapping(value="listadepartamentos.do")
    @ResponseBody
    public Map<String,Object>listaDepartamentos(){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", "true");
        result.put("data", reporteGraficoService.getNuRegistrosPN());
        return result;
    }

    @RequestMapping(value="listaprovincias.do")
    @ResponseBody
    public List<Map<String, Object>> listaProvincias(@RequestParam(value = "coDepartamento", required=true) String coDepartamento){
        return reporteGraficoService.getNuRegistrosPN(coDepartamento);
    }

    @RequestMapping(value="listadistritos.do")
    @ResponseBody
    public List<Map<String, Object>> listaDistritos(
            @RequestParam(value = "coDepartamento", required=true) String coDepartamento,
            @RequestParam(value = "coProvincia", required=true) String coProvincia,
            Model model){
        return reporteGraficoService.getNuRegistrosPN(coDepartamento, coProvincia);
    }

    @RequestMapping(value="listadepartamentosporcentaje.do")
    @ResponseBody
    public List<Map<String,Object>> listaDepartamentosPorcentaje(){
        return reporteGraficoService.getPcRegistrosPN();
    }

    @RequestMapping(value="listaprovinciasporcentaje.do")
    @ResponseBody
    public List<Map<String, Object>> listaProvinciasPorcentaje(@RequestParam(value = "coDepartamento", required=true) String coDepartamento){
        return reporteGraficoService.getPcRegistrosPN(coDepartamento);
    }

    @RequestMapping(value="listadistritosporcentaje.do")
    @ResponseBody
    public List<Map<String, Object>> listaDistritos(
            @RequestParam(value = "coDepartamento", required=true) String coDepartamento,
            @RequestParam(value = "coProvincia", required=true) String coProvincia){
        return reporteGraficoService.getPcRegistrosPN(coDepartamento, coProvincia);
    }

    @RequestMapping(value="departamentos.do")
    public String departamentos(Model model){
        return "reporteGrafico/departamentos";
    }

    @RequestMapping(value="pronvicias.do")
    public String provincias(){
        return "reporteGrafico/provincias";
    }

    @RequestMapping(value="distritos.do")
    public String distritos(){
        return "reporteGrafico/distritos";
    }

    @RequestMapping(value="show.do")
    public String show(){
        return "reporteGrafico/show";
    }

    @RequestMapping(value="get_data_reporte.do")
    @ResponseBody
    public Map<String, Object> getDataReporte() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("departamentos", reporteGraficoService.getDepartamentos());
        data.put("provincias", reporteGraficoService.getProvincias("01"));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("data", data);
        return result;
    }
}
