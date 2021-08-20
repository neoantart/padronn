package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.*;
import pe.gob.reniec.padronn.logic.service.*;
import pe.gob.reniec.padronn.logic.util.ImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import pe.gob.reniec.padronn.logic.web.validator.ConsultaUsuarioDatosValidator;
import pe.gob.reniec.padronn.logic.web.validator.ConsultaUsuarioEntidadValidator;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteEntidadChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteMovimientosCheck;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteRegistradorChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteRegistradorPrecargaChecks;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;


/**
 * Clase ReporteController.
 *
 * @author jfloresh
 */
@Controller
public class ReporteController extends AbstractBaseController {

    private static final String REG_VALID_ENTIDAD = "[\\w\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-, \\.]+";
    private static final String REG_VALID_CO_UBIGEO = "\\d*";
    private static final String REG_VALID_EST_SALUD = "[\\w\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-, \\.]+";
    private static final String REG_VALID_STR_NUMERIC = "[\\d_]*";

    private final String REG_ACTIVOS="1";
    private final String REG_ACTIVOS_OBSERVADOS="2";
    private final String REG_INACTIVOS="0";
    private final String REG_TODOS="3";

    private static final List<String> entidadLocal = Arrays.asList(Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad(), Entidad.TipoEntidad.DISA.getCoTipoEntidad(), Entidad.TipoEntidad.RED.getCoTipoEntidad(), Entidad.TipoEntidad.MICRORED.getCoTipoEntidad(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());

    @Autowired
    PadronProperties padronProperties;

    @Autowired
    ReporteService reporteService;

    @Autowired
    Usuario usuario;

    @Autowired
    PadronService padronService;

    @Autowired
    ActaService actaService;

    @Autowired
    UbigeoService ubigeoService;

    @Autowired
    EntidadService entidadService;

    @Autowired
    UsuarioExternoService usuarioExternoService;

    @Autowired
    FrecuenciaAtencionService frecuenciaAtencionService;

    Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ImagenCiudadano imagenCiudadano;

    @RequestMapping("/reporte/padron_ubigeo.do")
    public String formPadronUbigeo(@ModelAttribute ReportePadronActivos reportePadronActivos, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly", reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        return "reporte/padron_ubigeo";
    }

    @RequestMapping(value = "/reporte/consulta_padron_ubigeo.do", method = RequestMethod.GET)
    public String padronUbigeo(@Valid @ModelAttribute ReportePadronActivos reportePadronActivos, BindingResult result, Model model) {
        if (result.hasErrors())
            return "reporte/padron_ubigeo";

        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6) {
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }

        Integer nuPagina = reportePadronActivos.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = 0;
        List<PadronNominal> list = null;
        if (reportePadronActivos.getEsPadron() == 1) {//activos
            filas = reporteService.countListarPadronesUbigeoActivos(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesUbigeoActivos(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
            }
            else {
                model.addAttribute("message", padronProperties.MESSAGE_MAX_ROW);
                model.addAttribute("feIni", reportePadronActivos.getFeIni());
                model.addAttribute("feFin", reportePadronActivos.getFeFin());
                model.addAttribute("coUbigeo", reportePadronActivos.getCoUbigeo());
                return "reporte/padron_ubigeo";
            }
        } else if(reportePadronActivos.getEsPadron() == 2) {//activos-observados
            filas = reporteService.countListarPadronesUbigeoObservados(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesUbigeoObservados(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
            }
            else {
                model.addAttribute("feIni", reportePadronActivos.getFeIni());
                model.addAttribute("feFin", reportePadronActivos.getFeFin());
                model.addAttribute("coUbigeo", reportePadronActivos.getCoUbigeo());
                model.addAttribute("message", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron_ubigeo";
            }
        } else if(reportePadronActivos.getEsPadron() == 3) { //Todos
            filas = reporteService.countListarPadronesUbigeoTodos(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
            list = reporteService.listarPadronesUbigeoTodos(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
        }


        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));

        model.addAttribute("padronActivos", list);
        //model.addAttribute("reportePadronActivos", reportePadronActivos);
        model.addAttribute("coUbigeo", reportePadronActivos.getCoUbigeo());
        model.addAttribute("deUbigeo", reportePadronActivos.getDeUbigeo());
        model.addAttribute("feIni", reportePadronActivos.getFeIni());
        model.addAttribute("feFin", reportePadronActivos.getFeFin());
        model.addAttribute("tiRegistro", reportePadronActivos.getTiRegistro());
        model.addAttribute("tiRegFecha", reportePadronActivos.getTiRegFecha());
        model.addAttribute("filas", filas);
        model.addAttribute("esPadron", reportePadronActivos.getEsPadron());
        return "reporte/padron_ubigeo";
    }

    @RequestMapping(value = "/reporte/reporte_ubigeo_xls.do", method = RequestMethod.GET)
    public String reportePadronXls(@ModelAttribute ReportePadronActivos reportePadronActivos, Model model) {
        String filename = "reporte_por_rango_fechas_" + reportePadronActivos.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_")+"_del_"+ reportePadronActivos.getFeIni()+"_al_"+ reportePadronActivos.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase()+".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronActivos);
        List<PadronNominal> list=null;
        if(reportePadronActivos.getEsPadron() == 1) {
            list = reporteService.listarPadronesUbigeoActivos(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
        } else if(reportePadronActivos.getEsPadron() == 2) {
            list = reporteService.listarPadronesUbigeoObservados(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
        } else if(reportePadronActivos.getEsPadron() == 3) {
            list = reporteService.listarPadronesUbigeoTodos(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin(), reportePadronActivos.getTiRegistro(), reportePadronActivos.getTiRegFecha());
        }

        model.addAttribute("padronList", list);
        return "reportePadronRangoFecha.xls";
    }

    @RequestMapping("/reporte/padron_altas_bajas.do")
    public String formAltasBajas(@ModelAttribute ReportePadronAltasBajas reportePadronAltasBajas, Model model) {
        reportePadronAltasBajas.setEsPadron(1);
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        return "reporte/padron-altas-bajas";
    }


    @RequestMapping(value = "/reporte/consulta_padron_altas_bajas.do")
    public String consultaPadronAltasBajas(@Valid @ModelAttribute ReportePadronAltasBajas reportePadronAltasBajas, BindingResult result, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly", reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }

        if (result.hasErrors())
            return "reporte/padron-altas-bajas";

        Integer nuPagina = reportePadronAltasBajas.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = 0;
        List<PadronNominal> list = null;

        if (reportePadronAltasBajas.getEsPadron() == 1) {
            filas = reporteService.countListaPadronesActivos(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), reportePadronAltasBajas.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesActivos(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronAltasBajas.getTiRegFecha());
            }
            else {
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                model.addAttribute("coUbigeo", reportePadronAltasBajas.getCoUbigeo());
                return "reporte/padron-altas-bajas";
            }

        } else if(reportePadronAltasBajas.getEsPadron() == 0) {
            filas = reporteService.countListaPadronesInactivos(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), reportePadronAltasBajas.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesInactivos(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronAltasBajas.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                model.addAttribute("coUbigeo", reportePadronAltasBajas.getCoUbigeo());
                return "reporte/padron-altas-bajas";
            }
        } else if(reportePadronAltasBajas.getEsPadron() == 2) {
            filas = reporteService.countListaPadronesObservados(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), reportePadronAltasBajas.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesObservados(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronAltasBajas.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                model.addAttribute("coUbigeo", reportePadronAltasBajas.getCoUbigeo());
                return "reporte/padron-altas-bajas";
            }

        }

        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));

        model.addAttribute("padronActivos", list);
        model.addAttribute("coUbigeo", reportePadronAltasBajas.getCoUbigeo());
        model.addAttribute("deUbigeo", reportePadronAltasBajas.getDeUbigeo());
        model.addAttribute("esPadron", reportePadronAltasBajas.getEsPadron());
        model.addAttribute("tiRegFecha", reportePadronAltasBajas.getTiRegFecha());
        model.addAttribute("feIni", reportePadronAltasBajas.getFeIni());
        model.addAttribute("feFin", reportePadronAltasBajas.getFeFin());
        model.addAttribute("filas", filas);

        return "reporte/padron-altas-bajas";
    }

    @RequestMapping(value = "/reporte/buscar_ubigeo.do", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Lugar> buscarUbigeo(@RequestParam(value = "deUbigeo") String deUbigeo) {
        if (!deUbigeo.matches(REG_VALID_ENTIDAD)) {
            return new ArrayList<Lugar>();
        }
        return reporteService.getUbigeo(deUbigeo);
    }

    @RequestMapping(value = "/reporte/get_ubigeo.do", method = RequestMethod.GET)
    public
    @ResponseBody
    Lugar getUbigeo(@RequestParam(value = "coUbigeo") String coUbigeo) {
        if (!coUbigeo.matches(REG_VALID_CO_UBIGEO)) {
            return new Lugar();
        }
        return reporteService.getLugar(coUbigeo);
    }

    @RequestMapping(value = "/reporte/reporte_padron_altas_bajas_xls.do", method = RequestMethod.GET)
    public String reportePadronAltasBajasXls(@ModelAttribute ReportePadronAltasBajas reportePadronAltasBajas, Model model) {
        String filename = "reporte_altas_bajas_de_"+ reportePadronAltasBajas.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_")+"_del_"+ reportePadronAltasBajas.getFeIni()+ "_" +"al"+"_"+reportePadronAltasBajas.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronAltasBajas);
        List<PadronNominal> list=null;

        if(reportePadronAltasBajas.getEsPadron() == 1){//ACTIVOS
            list = reporteService.listarPadronesActivos(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), reportePadronAltasBajas.getTiRegFecha());
        } else if(reportePadronAltasBajas.getEsPadron() == 2){//ACTIVOS-OBSERVADOS
            list = reporteService.listarPadronesObservados(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), reportePadronAltasBajas.getTiRegFecha());
        }

        model.addAttribute("padronList", list);
        return "reportePadron.xls";
    }

    @RequestMapping(value = "/reporte/reporte_padron_bajas_xls.do", method = RequestMethod.GET)
    public String reportePadronBajasXls(@ModelAttribute ReportePadronAltasBajas reportePadronAltasBajas, Model model) {
        String filename = "reporte_altas_bajas_de_"+ reportePadronAltasBajas.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_") +"_del_"+ reportePadronAltasBajas.getFeIni()+ "_" +"al"+"_"+reportePadronAltasBajas.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronAltasBajas);
        List<PadronNominal> list;
        list = reporteService.listarPadronesInactivos(reportePadronAltasBajas.getCoUbigeo(), reportePadronAltasBajas.getFeIni(), reportePadronAltasBajas.getFeFin(), reportePadronAltasBajas.getTiRegFecha());

        model.addAttribute("padronList", list);
        return "reportePadronBajas.xls";
    }

    @RequestMapping(value = "/reporte/detalle_padron.do", method = RequestMethod.GET)
    public String detallePadron(@RequestParam(value = "codigo") String coPadronNominal, Model model) {
        model.addAttribute("padronNominal", padronService.obtenerPorCodigoPadron(coPadronNominal));
        return "reporte/detalle_padron";
    }

    @RequestMapping(value = "/reporte/padron_entidad.do")
    public String padronEntidad(@ModelAttribute ReportePadronEntidad reportePadronEntidad, Model model) {
        reportePadronEntidad.setEsPadron(1);
        model.addAttribute("usuario",usuario);
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6 && "1".equals(usuario.getCoTipoEntidad())) {
            model.addAttribute("municipioReadOnly",true);
            model.addAttribute("deEntidadReadOnly",reporteService.getEntidad(usuario.getCoEntidad()).getDeLugar());
            model.addAttribute("coEntidadReadOnly", usuario.getCoEntidad());
        }
        return "reporte/padron-entidad";
    }

    @RequestMapping(value = "/reporte/buscar_entidad.do", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Lugar> buscarEntidad(@RequestParam(value = "deEntidad", required = true) String deEntidad) {
        if (!deEntidad.matches(REG_VALID_ENTIDAD)) {
            return new ArrayList<Lugar>();
        }
        return reporteService.buscarEntidad(deEntidad);
    }

    @RequestMapping(value = "/reporte/get_entidad.do", method = RequestMethod.GET)
    public
    @ResponseBody
    Lugar getEntidad(@RequestParam(value = "coEntidad", required = true) String coEntidad) {
        if (!coEntidad.matches(REG_VALID_STR_NUMERIC)) {
            return new Lugar();
        }
        return reporteService.getEntidad(coEntidad);
    }

    @RequestMapping(value = "/reporte/consulta_padron_entidad.do", method = RequestMethod.GET)
    public String consultaPadronEntidad(@Validated({ReporteEntidadChecks.class}) @ModelAttribute ReportePadronEntidad reportePadronEntidad, BindingResult result, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6 && "1".equals(usuario.getCoTipoEntidad())){
            model.addAttribute("municipioReadOnly",true);
            model.addAttribute("deEntidadReadOnly",reporteService.getEntidad(usuario.getCoEntidad()).getDeLugar());
            model.addAttribute("coEntidadReadOnly", usuario.getCoEntidad());
        }

        if (result.hasErrors())
            return "reporte/padron-entidad";

        Integer nuPagina = reportePadronEntidad.getNuPagina();

        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;

        Entidad entidad = entidadService.getEntidad(reportePadronEntidad.getCoEntidad());

        List<PadronNominal> list=null;
        int filas = 0;
        if (reportePadronEntidad.getEsPadron() == 1) { //activos
            filas = reporteService.countListarPadronesByEntidadActivos(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL",reportePadronEntidad.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByEntidadActivos(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS,reportePadronEntidad.getTiRegFecha());
            }else {
                model.addAttribute("message", padronProperties.MESSAGE_MAX_ROW);
                model.addAttribute("coEntidad", reportePadronEntidad.getCoEntidad());
                return "reporte/padron-entidad";
            }

        } else if(reportePadronEntidad.getEsPadron() == 2) {//activos-observados
            filas = reporteService.countListarPadronesByEntidadObservados(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL",reportePadronEntidad.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByEntidadObservados(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS,reportePadronEntidad.getTiRegFecha());
            } else{
                model.addAttribute("message", padronProperties.MESSAGE_MAX_ROW);
                model.addAttribute("coEntidad", reportePadronEntidad.getCoEntidad());
                return "reporte/padron-entidad";
            }

        } else if(reportePadronEntidad.getEsPadron() == 0) { //inactivos
            filas = reporteService.countListarPadronesByEntidadInactivos(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL",reportePadronEntidad.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByEntidadInactivos(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS,reportePadronEntidad.getTiRegFecha());
            } else {
                model.addAttribute("message", padronProperties.MESSAGE_MAX_ROW);
                model.addAttribute("coEntidad", reportePadronEntidad.getCoEntidad());
                return "reporte/padron-entidad";
            }

        }

        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("padronList", list);
        //model.addAttribute("reportePadronActivos", reportePadronActivos);
        model.addAttribute("coEntidad", reportePadronEntidad.getCoEntidad());
        model.addAttribute("deEntidad", reportePadronEntidad.getDeEntidad());
        model.addAttribute("feIni", reportePadronEntidad.getFeIni());
        model.addAttribute("feFin", reportePadronEntidad.getFeFin());
        model.addAttribute("esPadron", reportePadronEntidad.getEsPadron());
        model.addAttribute("tiRegFecha", reportePadronEntidad.getTiRegFecha());
        model.addAttribute("filas", filas);
        return "reporte/padron-entidad";
    }


    @RequestMapping(value = "/reporte/padron_entidad_xls.do", method = RequestMethod.GET)
    public String padronEntidadXls(@ModelAttribute ReportePadronEntidad reportePadronEntidad, Model model){
        String filename = "reporte_" + reportePadronEntidad.getDeEntidad().replaceAll("\\s+", "_").replaceAll(",", "_")+ "_del_"+ reportePadronEntidad.getFeIni()+"_"+"al"+"_"+ reportePadronEntidad.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronEntidad);
        List<PadronNominal> list;
        Entidad entidad = entidadService.getEntidad(reportePadronEntidad.getCoEntidad());

        if (reportePadronEntidad.getEsPadron() == 1) {
            list = reporteService.listarPadronesByEntidadActivos(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL",reportePadronEntidad.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadron.xls";
        } else if(reportePadronEntidad.getEsPadron() == 2){
            list = reporteService.listarPadronesByEntidadObservados(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL",reportePadronEntidad.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadron.xls";
        } else {
            list = reporteService.listarPadronesByEntidadInactivos(entidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "ALL",reportePadronEntidad.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadronBajas.xls";
        }
    }


    @RequestMapping(value = "/reporte/padron_establecimiento_salud.do", method = RequestMethod.GET)
    public String padronEstablecimientoSalud(@ModelAttribute ReportePadronEstablecimientoSalud reportePadronEstablecimientoSalud, Model model) {
        reportePadronEstablecimientoSalud.setEsPadron(1);
        return "reporte/padron-establecimiento-salud";
    }


    @RequestMapping(value = "/reporte/buscar_establecimiento_salud.do", method = RequestMethod.GET)
    public
    @ResponseBody
    List<EstablecimientoSalud> buscarEstablecimientoSalud(@RequestParam(value = "deEstSalud", required = true) String deEstSalud) {
        if (!deEstSalud.matches(REG_VALID_EST_SALUD)) {
            return new ArrayList<EstablecimientoSalud>();
        }
        return reporteService.buscarEstablecimientoSalud(deEstSalud);
    }

    @RequestMapping(value = "/reporte/buscar_establecimiento_salud_recien_nacidos.do", method = RequestMethod.GET)
    public
    @ResponseBody
    List<EstablecimientoSalud> buscarEstablecimientoSaludRecienNacidos(
            @RequestParam(value = "deEstSalud", required = true) String deEstSalud,
            @RequestParam(value = "coUbigeoInei", required = true) String coUbigeoInei) {

        if (!deEstSalud.matches(REG_VALID_EST_SALUD) || !coUbigeoInei.matches(REG_VALID_CO_UBIGEO)) {
            return new ArrayList<EstablecimientoSalud>();
        }
        return reporteService.buscarEstablecimientoSaludRecienNacido(deEstSalud, coUbigeoInei);
    }


    @RequestMapping(value = "/reporte/get_establecimiento_salud.do", method = RequestMethod.GET)
    public
    @ResponseBody
    EstablecimientoSalud getEstablecimientoSalud(@RequestParam(value = "coEstSalud", required = true) String coEstSalud) {
        if (!coEstSalud.matches(REG_VALID_STR_NUMERIC)) {
            return new EstablecimientoSalud();
        }
        return reporteService.getEstablecimientoSalud(coEstSalud);
    }

    @RequestMapping(value = "/reporte/consulta_padron_establecimiento_salud.do", method = RequestMethod.GET)
    public String consultaEstablecimientoSalud(@Valid @ModelAttribute ReportePadronEstablecimientoSalud reportePadronEstablecimientoSalud,
                                               BindingResult result,
                                               Model model) {

        if (result.hasErrors())
            return "reporte/padron-establecimiento-salud";

        Integer nuPagina = reportePadronEstablecimientoSalud.getNuPagina();

        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;

        List<PadronNominal> list;
        int filas = 0;

        model.addAttribute("coEstSalud", reportePadronEstablecimientoSalud.getCoEstSalud());
        if (reportePadronEstablecimientoSalud.getEsPadron() == 1) {
            filas = reporteService.countListarPadronesByEstablecimientoSaludActivos(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), reportePadronEstablecimientoSalud.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByEstablecimientoSaludActivos(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEstablecimientoSalud.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-establecimiento-salud";
            }
        } else if(reportePadronEstablecimientoSalud.getEsPadron() == 2){
            filas = reporteService.countListarPadronesByEstablecimientoSaludObservados(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), reportePadronEstablecimientoSalud.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByEstablecimientoSaludActivos(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEstablecimientoSalud.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-establecimiento-salud";
            }
        } else{
            filas = reporteService.countListarPadronesByEstablecimientoSaludInactivos(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), reportePadronEstablecimientoSalud.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByEstablecimientoSaludInactivos(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEstablecimientoSalud.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-establecimiento-salud";
            }
        }

        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("padronList", list);
        //model.addAttribute("reportePadronActivos", reportePadronActivos);
        model.addAttribute("deEstSalud", reportePadronEstablecimientoSalud.getDeEntidad());
        model.addAttribute("feIni", reportePadronEstablecimientoSalud.getFeIni());
        model.addAttribute("feFin", reportePadronEstablecimientoSalud.getFeFin());
        model.addAttribute("esPadron", reportePadronEstablecimientoSalud.getEsPadron());
        model.addAttribute("tiRegFecha", reportePadronEstablecimientoSalud.getTiRegFecha());
        model.addAttribute("nuSecuenciaLocal", reportePadronEstablecimientoSalud.getNuSecuenciaLocal());
        model.addAttribute("filas", filas);
        return "reporte/padron-establecimiento-salud";

    }

    @RequestMapping(value = "/reporte/padron_establecimiento_salud_xls.do", method = RequestMethod.GET)
    public String padronEstablecimientoSaludXls(@ModelAttribute ReportePadronEstablecimientoSalud reportePadronEstablecimientoSalud, Model model) {
        String filename = "reporte_establecimiento_de_salud_"+ reportePadronEstablecimientoSalud.getDeEstSalud().replaceAll("\\s+", "_").replaceAll(",", "_")+"_del_"+ reportePadronEstablecimientoSalud.getFeIni()+"_"+"al"+"_"+ reportePadronEstablecimientoSalud.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronEstablecimientoSalud);
        List<PadronNominal> list;
        if (reportePadronEstablecimientoSalud.getEsPadron() == 1) {
            //reporteService.listar
            list = reporteService.listarPadronesByEstablecimientoSaludActivos(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), reportePadronEstablecimientoSalud.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadron.xls";
        } else if (reportePadronEstablecimientoSalud.getEsPadron() == 2){
            list = reporteService.listarPadronesByEstablecimientoSaludObservados(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), reportePadronEstablecimientoSalud.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadron.xls";
        } else {
            list = reporteService.listarPadronesByEstablecimientoSaludInactivos(reportePadronEstablecimientoSalud.getCoEstSalud(), reportePadronEstablecimientoSalud.getFeIni(), reportePadronEstablecimientoSalud.getFeFin(), reportePadronEstablecimientoSalud.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadronBajas.xls";
        }
    }

    @RequestMapping(value = "/reporte/padron_programa_social.do", method = RequestMethod.GET)
    public String padronProgramaSocial(@ModelAttribute ReportePadronProgramaSocial reportePadronProgramaSocial, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6 && "1".equals(usuario.getCoTipoEntidad())){
            model.addAttribute("municipioReadOnly",true);
            model.addAttribute("deEntidadReadOnly",reporteService.getEntidad(usuario.getCoEntidad()).getDeLugar());
            model.addAttribute("coEntidadReadOnly", usuario.getCoEntidad());
        }
        reportePadronProgramaSocial.setEsPadron(1);
        model.addAttribute("programasSociales", reporteService.getProgramasSociales());
        return "reporte/padron-programa-social";
    }

    @RequestMapping(value = "/reporte/consulta_padron_programa_social.do", method = RequestMethod.GET)
    public String consultaPadronProgramaSocial(@Valid ReportePadronProgramaSocial reportePadronProgramaSocial, BindingResult result, Model model) {
        model.addAttribute("programasSociales", reporteService.getProgramasSociales());
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6 && "1".equals(usuario.getCoTipoEntidad())){
            model.addAttribute("municipioReadOnly",true);
            model.addAttribute("deEntidadReadOnly",reporteService.getEntidad(usuario.getCoEntidad()).getDeLugar());
            model.addAttribute("coEntidadReadOnly", usuario.getCoEntidad());
        }
        if(result.hasErrors())
            return "reporte/padron-programa-social";

        Integer nuPagina = reportePadronProgramaSocial.getNuPagina();

        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;

        List<PadronNominal> list=null;
        int filas = 0;
        model.addAttribute("coProgramaSocial", reportePadronProgramaSocial.getCoProgramaSocial());
        if (reportePadronProgramaSocial.getEsPadron() == 1) {
            filas = reporteService.countListarPadronesByProgramaSocialActivos(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), reportePadronProgramaSocial.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByProgramaSocialActivos(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronProgramaSocial.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-programa-social";
            }
        } else if(reportePadronProgramaSocial.getEsPadron() == 2) {
            filas = reporteService.countListarPadronesByProgramaSocialObservados(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), reportePadronProgramaSocial.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByProgramaSocialObservados(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronProgramaSocial.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-programa-social";
            }

        } else if(reportePadronProgramaSocial.getEsPadron() == 0){
            filas = reporteService.countListarPadronesByProgramaSocialInactivos(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), reportePadronProgramaSocial.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesByProgramaSocialInactivos(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronProgramaSocial.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-programa-social";
            }
        }

        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("padronList", list);
        //model.addAttribute("reportePadronActivos", reportePadronProgramaSocial);
        /*model.addAttribute("coProgramaSocial", reportePadronProgramaSocial.getCoProgramaSocial());*/
        model.addAttribute("deProgramaSocial", reportePadronProgramaSocial.getDeProgramaSocial());

        model.addAttribute("coEntidad", reportePadronProgramaSocial.getCoEntidad());
        model.addAttribute("deEntidad", reportePadronProgramaSocial.getDeEntidad());

        model.addAttribute("feIni", reportePadronProgramaSocial.getFeIni());
        model.addAttribute("feFin", reportePadronProgramaSocial.getFeFin());
        model.addAttribute("esPadron", reportePadronProgramaSocial.getEsPadron());
        model.addAttribute("tiRegFecha", reportePadronProgramaSocial.getTiRegFecha());
        model.addAttribute("filas", filas);
        return "reporte/padron-programa-social";
    }


    @RequestMapping(value = "/reporte/padron_programa_social_xls.do", method = RequestMethod.GET)
    public String padronProgramaSocialXls(@ModelAttribute ReportePadronProgramaSocial reportePadronProgramaSocial , Model model) {
        String filename = "reporte_" + reportePadronProgramaSocial.getDeProgramaSocial().replaceAll("\\s+", "_").replaceAll(",", "_")+"_" +"del"+ "_"+ reportePadronProgramaSocial.getFeIni()+"_" +"al_" +reportePadronProgramaSocial.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronProgramaSocial);
        List<PadronNominal> list;
        if (reportePadronProgramaSocial.getEsPadron() == 1) {
            list = reporteService.listarPadronesByProgramaSocialActivos(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), reportePadronProgramaSocial.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadron.xls";
        } if (reportePadronProgramaSocial.getEsPadron() == 2) {
            list = reporteService.listarPadronesByProgramaSocialObservados(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), reportePadronProgramaSocial.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadron.xls";
        } else {
            list = reporteService.listarPadronesByProgramaSocialInactivos(reportePadronProgramaSocial.getCoProgramaSocial(), reportePadronProgramaSocial.getFeIni(), reportePadronProgramaSocial.getFeFin(), reportePadronProgramaSocial.getCoEntidad(), reportePadronProgramaSocial.getTiRegFecha());
            model.addAttribute("padronList", list);
            return "reportePadronBajas.xls";
        }
    }


    @RequestMapping(value = "/minsa_reporte/padron_actas.do", method = RequestMethod.GET)
    public String padronActas(
            @ModelAttribute ReportePadronActas reportePadronActas,
            Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6 && "1".equals(usuario.getCoTipoEntidad())){
            model.addAttribute("municipioReadOnly",true);
            model.addAttribute("deEntidadReadOnly",reporteService.getEntidad(usuario.getCoEntidad()).getDeLugar());
            model.addAttribute("coEntidadReadOnly", usuario.getCoEntidad());
        }
        return "reporte/padron-actas";
    }


    @RequestMapping(value = "/reporte/consulta_padron_actas.do", method = RequestMethod.GET)
    public String listaMinsa(@Valid @ModelAttribute ReportePadronActas reportePadronActas, BindingResult result, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6 && "1".equals(usuario.getCoTipoEntidad())){
            model.addAttribute("municipioReadOnly",true);
            model.addAttribute("deEntidadReadOnly",reporteService.getEntidad(usuario.getCoEntidad()).getDeLugar());
            model.addAttribute("coEntidadReadOnly", usuario.getCoEntidad());
        }

        if (result.hasErrors())
            return "reporte/padron-actas";
        Integer nuPagina = (reportePadronActas.getNuPagina() == null || reportePadronActas.getNuPagina() <= 0) ? 1 : reportePadronActas.getNuPagina();
        int filas = actaService.getNumActas(reportePadronActas.getCoEntidad(), reportePadronActas.getFeIni(), reportePadronActas.getFeFin());
        model.addAttribute("feIni", reportePadronActas.getFeIni());
        model.addAttribute("feFin", reportePadronActas.getFeFin());
        model.addAttribute("coEntidad", reportePadronActas.getCoEntidad());
        model.addAttribute("deEntidad", reportePadronActas.getDeEntidad());
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        List<Acta> actas = actaService.getAll(reportePadronActas.getCoEntidad(), reportePadronActas.getFeIni(), reportePadronActas.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("actas", actas);
        model.addAttribute("filas", filas);
        return "reporte/padron-actas";
    }


    @RequestMapping(value = "/minsa_reporte/padron_actas_todos.do", method = RequestMethod.GET)
    public String padronActasTodos(
            @ModelAttribute ReportePadronActasTodos reportePadronActasTodos,
            Model model) {
        return "reporte/padron-actas-todos";
    }

    @RequestMapping(value = "/reporte/consulta_padron_actas_todos.do", method = RequestMethod.GET)
    public String consultaPadronActasTodos(
            @Valid @ModelAttribute ReportePadronActasTodos reportePadronActasTodos,
            BindingResult result,
            Model model) {
        if (result.hasErrors())
            return "reporte/padron-actas-todos";

        Integer nuPagina = (reportePadronActasTodos.getNuPagina() == null || reportePadronActasTodos.getNuPagina() <= 0) ? 1 : reportePadronActasTodos.getNuPagina();
        int filas = actaService.getNumAllActas(reportePadronActasTodos.getCoUbigeo(), reportePadronActasTodos.getFeIni(), reportePadronActasTodos.getFeFin());
        model.addAttribute("coUbigeo", reportePadronActasTodos.getCoUbigeo());
        model.addAttribute("feIni", reportePadronActasTodos.getFeIni());
        model.addAttribute("feFin", reportePadronActasTodos.getFeFin());
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        List<Acta> actas = actaService.getAllActas(reportePadronActasTodos.getCoUbigeo(), reportePadronActasTodos.getFeIni(), reportePadronActasTodos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("actas", actas);
        model.addAttribute("filas", filas);
        return "reporte/padron-actas-todos";
    }

    @RequestMapping(value = "/reporte/consula_padron_actas_todos_xls.do", method = RequestMethod.GET)
    public String consultaPadronActasTodosXls(@RequestParam(value = "coUbigeo") String coUbigeo, @RequestParam(value = "feIni") String feIni, @RequestParam(value = "feFin") String feFin, Model model ) {

        List<Acta> listActas = actaService.getAllActas(coUbigeo, feIni, feFin);
//        log.info(listActas);
        model.addAttribute("listActas", listActas);
        return "reportePadronActas.xls";
    }


    //consulta para municipios/entidad
    @RequestMapping(value = "/registromanual/padron_municipio.do", method = RequestMethod.GET)
    public String padronEntidadDo(@ModelAttribute ReportePadronEntidad reportePadronEntidad, Model model) {
        reportePadronEntidad.setCoEntidad(usuario.getCoEntidad());
        reportePadronEntidad.setDeEntidad(usuario.getDeEntidad());
        reportePadronEntidad.setEsPadron(1);
        model.addAttribute("coEntidad", usuario.getCoEntidad());
        model.addAttribute("deEntidad", usuario.getDeEntidad());
        reportePadronEntidad.setCoUbigeoInei(usuario.getCoUbigeoInei());
        return "reporte/padron-municipio";
    }

    @RequestMapping(value = "/registromanual/consulta_padron_municipio.do", method = RequestMethod.GET)
    public String consultaPadronMunicipio(@Validated({ReporteRegistradorChecks.class}) @ModelAttribute("reportePadronEntidad") ReportePadronEntidad reportePadronEntidad, BindingResult result, Model model) {
        String coEntidad = usuario.getCoEntidad();
        model.addAttribute("coEntidad", coEntidad);
        model.addAttribute("deEntidad", usuario.getDeEntidad());
        if (coEntidad.equals(reportePadronEntidad.getCoEntidad())) {
            if (result.hasErrors()) {
                reportePadronEntidad.setCoUbigeoInei(usuario.getCoUbigeoInei());
                return "reporte/padron-municipio";
            }
            Integer nuPagina = reportePadronEntidad.getNuPagina();
            nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
            List<PadronNominal> list=null;
            int filas = 0;

            model.addAttribute("esPadron", reportePadronEntidad.getEsPadron());
            if (reportePadronEntidad.getEsPadron() == 1) {
                filas = reporteService.countListarPadronesByEntidadActivos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());
                if(filas<padronProperties.MAXROWS_REPORTE) {
                    list = reporteService.listarPadronesByEntidadActivos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEntidad.getTiRegFecha());
                }else {
                    model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                    return "reporte/padron-municipio";
                }

            } else if (reportePadronEntidad.getEsPadron() == 2) {
                filas = reporteService.countListarPadronesByEntidadObservados(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());
                if(filas<padronProperties.MAXROWS_REPORTE) {
                    list = reporteService.listarPadronesByEntidadObservados(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEntidad.getTiRegFecha());
                }else {
                    model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                    return "reporte/padron-municipio";
                }
            } else if (reportePadronEntidad.getEsPadron() == 3) {
                filas = reporteService.countListarPadronesByEntidadTodos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());
                if(filas<padronProperties.MAXROWS_REPORTE) {
                    list = reporteService.listarPadronesByEntidadTodos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEntidad.getTiRegFecha());
                }else {
                    model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                    return "reporte/padron-municipio";
                }
            } else if (reportePadronEntidad.getEsPadron() == 0) {
                filas = reporteService.countListarPadronesByEntidadInactivos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());
                if(filas<padronProperties.MAXROWS_REPORTE) {
                    list = reporteService.listarPadronesByEntidadInactivos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEntidad.getTiRegFecha());
                }else {
                    model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                    return "reporte/padron-municipio";
                }
            }
            model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
            model.addAttribute("nuPagina", nuPagina);
            model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
            model.addAttribute("padronList", list);
            model.addAttribute("filas", filas);
            model.addAttribute("feIni", reportePadronEntidad.getFeIni());
            model.addAttribute("feFin", reportePadronEntidad.getFeFin());
            model.addAttribute("tiRegFecha", reportePadronEntidad.getTiRegFecha());
            model.addAttribute("coUbigeoInei", reportePadronEntidad.getCoUbigeoInei());
            return "reporte/padron-municipio";
        } else {
            reportePadronEntidad.setCoEntidad(usuario.getCoEntidad());
            reportePadronEntidad.setDeEntidad(usuario.getDeEntidad());
            model.addAttribute("coUbigeoInei", reportePadronEntidad.getCoUbigeoInei());
            //reportePadronEntidad.setEsPadron(1);
            return "reporte/padron-municipio";
        }
    }


    @RequestMapping(value = "/registromanual/padron_municipio_xls.do", method = RequestMethod.GET)
    public String padronMunicipioXls(@ModelAttribute ReportePadronEntidad reportePadronEntidad, Model model) {
        reportePadronEntidad.setCoEntidad(usuario.getCoEntidad());
        String filename = "reporte_registros_menores_" + reportePadronEntidad.getDeEntidad().replaceAll("\\s+", "_").replaceAll(",", "_")+"_del_"+reportePadronEntidad.getFeIni()+"_al_"+reportePadronEntidad.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronEntidad);
        List<PadronNominal> list=null;

        if (reportePadronEntidad.getEsPadron() == 1)
            list = reporteService.listarPadronesByEntidadActivos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());
        else if (reportePadronEntidad.getEsPadron() == 2)
            list = reporteService.listarPadronesByEntidadObservados(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());
        else if (reportePadronEntidad.getEsPadron() == 3)
            list = reporteService.listarPadronesByEntidadTodos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());
        else if (reportePadronEntidad.getEsPadron() == 0)
            list = reporteService.listarPadronesByEntidadInactivos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());

        model.addAttribute("padronList", list);
        return "reportePadron.xls";
    }


    @RequestMapping(value = "/registromanual/padron_baja_xls.do", method = RequestMethod.GET)
    public String padronBajaXls(@ModelAttribute ReportePadronEntidad reportePadronEntidad, Model model) {
        reportePadronEntidad.setCoEntidad(usuario.getCoEntidad());
        String filename = "padron_municipio_" + reportePadronEntidad.getDeEntidad().replaceAll("\\s+", "_").replaceAll(",", "_");
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronEntidad);
        List<PadronNominal> list;
        if (reportePadronEntidad.getEsPadron() == 1)
            list = reporteService.listarPadronesByEntidadActivos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM",reportePadronEntidad.getTiRegFecha());
        else if (reportePadronEntidad.getEsPadron() == 2)
            list = reporteService.listarPadronesByEntidadObservados(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM",reportePadronEntidad.getTiRegFecha());
        else if (reportePadronEntidad.getEsPadron() == 3)
            list = reporteService.listarPadronesByEntidadTodos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM",reportePadronEntidad.getTiRegFecha());
        else
            list = reporteService.listarPadronesByEntidadInactivos(reportePadronEntidad.getCoUbigeoInei(), reportePadronEntidad.getFeIni(), reportePadronEntidad.getFeFin(), "RM", reportePadronEntidad.getTiRegFecha());
        model.addAttribute("padronList", list);
        /*logger.info("lista completa....");*/
        return "reportePadronBajas.xls";
    }


    //consulta para municipios/entidad precarga
    @RequestMapping(value = "/registromanual/padron_municipio_precarga.do", method = RequestMethod.GET)
    public String padronEntidadPrecarga(@ModelAttribute ReportePadronEntidad reportePadronEntidad, Model model) {
        reportePadronEntidad.setCoEntidad(usuario.getCoEntidad());
        reportePadronEntidad.setDeEntidad(usuario.getDeEntidad());
        reportePadronEntidad.setEsPadron(1);
        model.addAttribute("coEntidad", usuario.getCoEntidad());
        model.addAttribute("deEntidad", usuario.getDeEntidad());
        model.addAttribute("deUbigeoInei", ubigeoService.obtenerDeUbigeoIneiCorto(usuario.getCoUbigeoInei()));
        reportePadronEntidad.setCoUbigeoInei(usuario.getCoUbigeoInei());
        return "reporte/padron-municipio-precarga";
    }

    @RequestMapping(value = "/registromanual/consulta_padron_municipio_precarga.do", method = RequestMethod.GET)
    public String consultaPadronMunicipioPrecarga(@Validated({ReporteRegistradorPrecargaChecks.class}) @ModelAttribute("reportePadronEntidad") ReportePadronEntidad reportePadronEntidad, BindingResult result, Model model) {
        String coEntidad = usuario.getCoEntidad();
        model.addAttribute("coEntidad", coEntidad);
        model.addAttribute("deEntidad", usuario.getDeEntidad());

        model.addAttribute("deUbigeoInei", ubigeoService.obtenerDeUbigeoIneiCorto(reportePadronEntidad.getCoUbigeoInei()));
        model.addAttribute("esPadron", reportePadronEntidad.getEsPadron());

        if (coEntidad.equals(reportePadronEntidad.getCoEntidad())) {
            if (result.hasErrors()) {
                reportePadronEntidad.setCoUbigeoInei(usuario.getCoUbigeoInei());
                return "reporte/padron-municipio-precarga";
            }
            Integer nuPagina = reportePadronEntidad.getNuPagina();
            nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
            List<PadronNominal> list=null;
            int filas = 0;

            if (reportePadronEntidad.getEsPadron() == 1) {
                filas = reporteService.countListarPadronesByEntidadActivos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM", "");
                if(filas<padronProperties.MAXROWS_REPORTE) {
                    list = reporteService.listarPadronesByEntidadActivos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, "");
                }else {
                    model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                    return "reporte/padron-municipio-precarga";
                }
            } else if (reportePadronEntidad.getEsPadron() == 2) {
                filas = reporteService.countListarPadronesByEntidadObservados(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM", "");
                if(filas<padronProperties.MAXROWS_REPORTE) {
                    list = reporteService.listarPadronesByEntidadObservados(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, "");
                }else {
                    model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                    return "reporte/padron-municipio-precarga";
                }
            } else if (reportePadronEntidad.getEsPadron() == 3) {
                filas = reporteService.countListarPadronesByEntidadTodos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM", "");
                if(filas<padronProperties.MAXROWS_REPORTE) {
                    list = reporteService.listarPadronesByEntidadTodos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, "");
                }else {
                    model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                    return "reporte/padron-municipio-precarga";
                }
            } else if(reportePadronEntidad.getEsPadron() == 0) {
                filas = reporteService.countListarPadronesByEntidadInactivos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM","");
                if(filas<padronProperties.MAXROWS_REPORTE) {
                    list = reporteService.listarPadronesByEntidadInactivos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM", (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, "");
                }else {
                    model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                    return "reporte/padron-municipio-precarga";
                }
            }
            model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
            model.addAttribute("nuPagina", nuPagina);
            model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
            model.addAttribute("padronList", list);
            model.addAttribute("filas", filas);
/*          model.addAttribute("feIni", reportePadronEntidad.getFeIni());
            model.addAttribute("feFin", reportePadronEntidad.getFeFin());*/
            model.addAttribute("esPadron", reportePadronEntidad.getEsPadron());
            model.addAttribute("coUbigeoInei", reportePadronEntidad.getCoUbigeoInei());
            return "reporte/padron-municipio-precarga";
        } else {
            reportePadronEntidad.setCoEntidad(usuario.getCoEntidad());
            reportePadronEntidad.setDeEntidad(usuario.getDeEntidad());
            model.addAttribute("coUbigeoInei", reportePadronEntidad.getCoUbigeoInei());
            //reportePadronEntidad.setEsPadron(1);
            return "reporte/padron-municipio-precarga";
        }
    }


    @RequestMapping(value = "/registromanual/padron_municipio_precarga_xls.do", method = RequestMethod.GET)
    public String padronMunicipioPrecargaXls(@ModelAttribute ReportePadronEntidad reportePadronEntidad, Model model) {
        reportePadronEntidad.setCoEntidad(usuario.getCoEntidad());
        String filename = "padron_municipio_precarga_" + reportePadronEntidad.getDeEntidad().replaceAll("\\s+", "_").replaceAll(",", "_");
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronEntidad);
        List<PadronNominal> list=null;

        if (reportePadronEntidad.getEsPadron() == 1) {
            list = reporteService.listarPadronesByEntidadActivos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM",""); // CM o RL
            model.addAttribute("padronList", list);
            return "reportePadron.xls";
        } else if (reportePadronEntidad.getEsPadron() == 2) {
            list = reporteService.listarPadronesByEntidadObservados(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM",""); // CM o RL
            model.addAttribute("padronList", list);
            return "reportePadron.xls";
        }  else if (reportePadronEntidad.getEsPadron() == 3) {
            list = reporteService.listarPadronesByEntidadTodos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM",""); // CM o RL
            model.addAttribute("padronList", list);
            return "reportePadronBajas.xls";
        }  else {
            list = reporteService.listarPadronesByEntidadInactivos(reportePadronEntidad.getCoUbigeoInei(), "", "", "CM",""); //CM o RL
            model.addAttribute("padronList", list);
            return "reportePadronBajas.xls";
        }
    }
//


    @RequestMapping(value = "/reporte/acta_detalle.do", method = RequestMethod.GET)
    public String detalleActa(
            @RequestParam(value = "coActa", required = true) String coActa,
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            @RequestParam(value = "coEntidad") String coEntidad,
            @RequestParam(value = "feIni", required = true) String feIni,
            @RequestParam(value = "feFin", required = true) String feFin,
            @RequestParam(value = "enlace", required = true) String enlace,
            Model model) {
        Acta acta = actaService.getActa(coActa);
        model.addAttribute("coEntidad", coEntidad);
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("feIni", feIni);
        model.addAttribute("feFin", feFin);
        model.addAttribute("enlace", enlace);
        //model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS*(nuPagina-1));
        model.addAttribute("acta", acta);
        return "reporte/acta-detalle";
    }

    @RequestMapping(value = "/reporte/cantidades_departamentos_total.do")
    public String cantidadDepartamentosTotal(Model model) {
        model.addAttribute("depas", reporteService.getCantidadesDepartamento());
        model.addAttribute("fechaGeneracion", reporteService.getFechaGeneracionConsolidado());
        return "reporte/cantidades_departamento_total";
    }

    @RequestMapping(value = "/reporte/cantidades_departamentos.do", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> cantidadDepartamentos() {
        Map retorno = new HashMap<String, Object>();
        retorno.put("success", true);
        retorno.put("data", reporteService.getCantidadesDepartamento());
        retorno.put("mensaje", "");
        return retorno;
    }


    @RequestMapping(value = "/reporte/cantidades_provincia.do", method = RequestMethod.GET)
    public String cantidadesProvincia(@RequestParam(value = "coUbigeo") String coUbigeo, Model model) {
        model.addAttribute("provincias", reporteService.getCantidadesProvincia(coUbigeo));
        model.addAttribute("coUbigeo", coUbigeo.substring(0, 2));
        model.addAttribute("fechaConsolidado", reporteService.getFechaGeneracionConsolidado());
        return "reporte/cantidades-provincia";
    }

    @RequestMapping(value = "/reporte/cantidades_distrito.do", method = RequestMethod.GET)
    public String cantidadesDistrito(@RequestParam(value = "coUbigeo") String coUbigeo, Model model) {
        model.addAttribute("distritos", reporteService.getCantidadesDistrito(coUbigeo));
        model.addAttribute("fechaConsolidado", reporteService.getFechaGeneracionConsolidado());
        model.addAttribute("coUbigeo", coUbigeo);
        return "reporte/cantidades-distrito";
    }


    @RequestMapping(value = "/reporte/form_listado_edades.do", method = RequestMethod.GET)
    public String listadoEdades(
            @ModelAttribute(value = "reportePadronEdad") ReportePadronEdad reportePadronEdad,
            Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        int anios = 5, meses = 11;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes + 1);
        model.addAttribute("edadAnios", edadAnios);
        model.addAttribute("edadMeses", edadMeses);
        return "reporte/listado-edades";
    }

    @RequestMapping(value = "/reporte/consulta_listado_edades.do", method = RequestMethod.GET)
    public String consultaListadoEdades(
            @Valid @ModelAttribute(value = "reportePadronEdad") ReportePadronEdad reportePadronEdad,
            BindingResult result,
            Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6) {
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        int anios = 5, meses = 11;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        /*String[] edadMeses = new String[meses];
        for(int mes=0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes+1);*/
        model.addAttribute("edadAnios", edadAnios);
        /*model.addAttribute("edadMeses", edadMeses);*/
        Integer nuPagina = reportePadronEdad.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = 0;
        List<PadronNominal> list=null;
        if (result.hasErrors())
            return "reporte/listado-edades";


        model.addAttribute("deEdad", reportePadronEdad.getDeEdad());
        model.addAttribute("hastaEdad", reportePadronEdad.getHastaEdad());
        model.addAttribute("coUbigeo", reportePadronEdad.getCoUbigeo());

        if(reportePadronEdad.getEsPadron() == 1) {
            filas = reporteService.countListarPadronEdadActivo(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronEdadActivo(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/listado-edades";
            }

        } else if(reportePadronEdad.getEsPadron() == 2) {
            filas = reporteService.countListarPadronEdadObservado(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronEdadObservado(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/listado-edades";
            }

        } else if(reportePadronEdad.getEsPadron() == 3) {
            filas = reporteService.countListarPadronEdadTodos(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronEdadTodos(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/listado-edades";
            }

        }

        //log.debug("listPadron:" + list.toString());
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        //log.debug("listaPadrones: "+ list.toString());
        model.addAttribute("padronList", list);
        /*model.addAttribute("coUbigeo", reportePadronEdad.getCoUbigeo());*/
        model.addAttribute("deUbigeo", reportePadronEdad.getDeUbigeo());
        /*model.addAttribute("deEdad", reportePadronEdad.getDeEdad());
        model.addAttribute("hastaEdad", reportePadronEdad.getHastaEdad());*/
        model.addAttribute("tiRegFecha", reportePadronEdad.getTiRegFecha());
        model.addAttribute("feIni", reportePadronEdad.getFeIni());
        model.addAttribute("feFin", reportePadronEdad.getFeFin());
        model.addAttribute("filas", filas);
        model.addAttribute("esPadron",reportePadronEdad.getEsPadron());
        return "reporte/listado-edades";
    }

    @RequestMapping(value = "/reporte/padron_edad_xls.do", method = RequestMethod.GET)
    public String padronEdadXls(@ModelAttribute ReportePadronEdad reportePadronEdad, Model model) {
        String filename = "Reporte_Por_Rango_Edades_de_"+ reportePadronEdad.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_") +"_del_"+ reportePadronEdad.getFeIni()+"_"+"al_"+reportePadronEdad.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronEdad);
        List<PadronNominal> list =null;

        if(reportePadronEdad.getEsPadron() == 1) {
            list = reporteService.listarPadronEdadActivo(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
        } else if(reportePadronEdad.getEsPadron() == 2) {
            list = reporteService.listarPadronEdadObservado(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
        } else if(reportePadronEdad.getEsPadron() == 3) {
            list = reporteService.listarPadronEdadTodos(reportePadronEdad.getCoUbigeo(), reportePadronEdad.getDeEdad(), reportePadronEdad.getHastaEdad(), reportePadronEdad.getFeIni(), reportePadronEdad.getFeFin(), reportePadronEdad.getTiRegFecha());
        }

        model.addAttribute("padronList", list);
        return "reportePadronEdad.xls";
    }


    @RequestMapping(value = "/minsa_reporte/padron_movimientos.do", method = RequestMethod.GET)
    public String padronMovimientos(
            @ModelAttribute ReportePadronMovimientos reportePadronMovimientos,
            Model model
    ) {
        return "reporte/padron-movimientos";
    }

    @RequestMapping(value = "/reporte/consulta_padron_movimiento.do", method = RequestMethod.GET)
    public String consultaPadronMovimiento(
            @Validated(value = {ReporteMovimientosCheck.class}) @ModelAttribute ReportePadronMovimientos reportePadronMovimientos,
            BindingResult result,
            Model model) {

        Integer nuPagina = (reportePadronMovimientos.getNuPagina() == null || Integer.parseInt(reportePadronMovimientos.getNuPagina()) <= 0) ? 1 : Integer.parseInt(reportePadronMovimientos.getNuPagina());
        if (result.hasErrors())
            return "reporte/padron-movimientos";
        Integer filas = reporteService.countPadronMovimientos(reportePadronMovimientos.getFeIni(), reportePadronMovimientos.getFeFin());
        List<PadronMovimiento> movimientos = reporteService.listadoPadronMovimiento(reportePadronMovimientos.getFeIni(), reportePadronMovimientos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("movimientos", movimientos);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("feIni", reportePadronMovimientos.getFeIni());
        model.addAttribute("feFin", reportePadronMovimientos.getFeFin());
        model.addAttribute("filas", filas);
        return "reporte/padron-movimientos";
    }

    @RequestMapping(value = "/reporte/padron_movimientos_xls.do", method = RequestMethod.GET)
    public String padronMovimientosXls(
            @RequestParam(value = "feIni", required = true) String feIni,
            @RequestParam(value = "feFin", required = true) String feFin,
            Model model) {

        String filename = "Reporte_Movimientos_Ubigeo_del_"+ feIni +"_al_"+ feFin;
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        List<PadronMovimiento> list = reporteService.listadoPadronMovimiento(feIni, feFin);
        model.addAttribute("padronMovimientoList", list);
        return "reportePadronMovimientos.xls";

    }

    @RequestMapping(value = "/reporte/padron_total_distritos.do", method = RequestMethod.GET)
    public String padronTotalDistritos(Model model) {
        model.addAttribute("totalDistritos", reporteService.listadoPadronesPorUbigeo());
        return "reporte/padron-total-distritos";
    }

    @RequestMapping(value = "/reporte/padron_total_distritos_xls.do", method = RequestMethod.GET)
    public String padronTotalDistritosXls(Model model) {
        String filename = "Reporte_Padron_Total_Distritos";
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xls");
        List<Ubigeo> list = reporteService.listadoPadronesPorUbigeo();
        model.addAttribute("padronTotalDistritos", list);
        return "reportePadronTotalDistritos.xls";
    }

    @RequestMapping(value = "/minsa_reporte/padron_observados.do", method = RequestMethod.GET)
    public String padronObservados(@ModelAttribute ReportePadronObservado reportePadronObservado, Model model) {
        return "reporte/padron-observados";
    }

    @RequestMapping(value = "/reporte/consulta_padron_observados.do", method = RequestMethod.GET)
    public String consultaPadronObservados(
            @Valid @ModelAttribute ReportePadronObservado reportePadronObservado,
            BindingResult result,
            Model model
    ) {
        Integer nuPagina = (reportePadronObservado.getNuPagina() == null || Integer.parseInt(reportePadronObservado.getNuPagina()) <= 0) ? 1 : Integer.parseInt(reportePadronObservado.getNuPagina());
        if (result.hasErrors())
            return "reporte/padron-observados";
        Integer filas = reporteService.countListadoObservaciones(reportePadronObservado.getFeIni(), reportePadronObservado.getFeFin());
        List<PadronObservado> observaciones = reporteService.listadoObservaciones(reportePadronObservado.getFeIni(), reportePadronObservado.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("observaciones", observaciones);
        model.addAttribute("feIni", reportePadronObservado.getFeIni());
        model.addAttribute("feFin", reportePadronObservado.getFeFin());
        model.addAttribute("filas",filas);
        return "reporte/padron-observados";
    }

    @RequestMapping(value = "/reporte/padron_observados_xls.do", method = RequestMethod.GET)
    public String padronObservadosXls(
            @RequestParam(value = "feIni", required = true) String feIni,
            @RequestParam(value = "feFin", required = true) String feFin,
            Model model
    ) {
        String filename = "Reporte_Registros_Observados_del_"+ feIni + "_al_" + feFin + ".xlsx";
        model.addAttribute("nombreArchivo", filename);
        List<PadronObservado> list = reporteService.listadoObservaciones(feIni, feFin);
        model.addAttribute("padronObservados", list);
        return "reporteObservados.xls";
    }

    @RequestMapping("/reporte/padron_sin_docu.do")
    public String padronSinDocu(@ModelAttribute ReportePadronSinDoc reportePadronSinDoc, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        return "reporte/padron-sin-docu";
    }

    @RequestMapping(value = "/reporte/consulta_padron_sin_docu.do", method = RequestMethod.GET)
    public String consultaPadronSinDocu(
            @Valid @ModelAttribute ReportePadronSinDoc reportePadronSinDoc,
            BindingResult result, Model model
    ) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }

        if (result.hasErrors())
            return "reporte/padron-sin-docu";

        Integer nuPagina = reportePadronSinDoc.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;

        int filas=0;
        List<PadronNominal> list=null;
        model.addAttribute("coUbigeo", reportePadronSinDoc.getCoUbigeo());
        if(reportePadronSinDoc.getEsPadron() == 1) {
            filas = reporteService.countListadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), reportePadronSinDoc.getTiRegFecha(), "1");//activos
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronSinDoc.getTiRegFecha(), "1");
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-sin-docu";
            }
        } else if(reportePadronSinDoc.getEsPadron() == 2) {
            filas = reporteService.countListadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), reportePadronSinDoc.getTiRegFecha(), "2"); //activos-observados
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronSinDoc.getTiRegFecha(), "2");
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-sin-docu";
            }
        } else if(reportePadronSinDoc.getEsPadron() == 3) {
            filas = reporteService.countListadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), reportePadronSinDoc.getTiRegFecha(), "3"); //todos
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronSinDoc.getTiRegFecha(), "3");
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/padron-sin-docu";
            }
        }

        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));

        model.addAttribute("padronSinDocu", list);
        //model.addAttribute("reportePadronActivos", reportePadronActivos);
        /*model.addAttribute("coUbigeo", reportePadronSinDoc.getCoUbigeo());*/
        model.addAttribute("deUbigeo", reportePadronSinDoc.getDeUbigeo());
        model.addAttribute("tiRegFecha", reportePadronSinDoc.getTiRegFecha());
        model.addAttribute("feIni", reportePadronSinDoc.getFeIni());
        model.addAttribute("feFin", reportePadronSinDoc.getFeFin());
        model.addAttribute("esPadron", reportePadronSinDoc.getEsPadron());
        model.addAttribute("filas", filas);
        return "reporte/padron-sin-docu";
    }

    @RequestMapping(value = "/reporte/reporte_padron_sin_docu_xls.do", method = RequestMethod.GET)
    public String padronSinDocuXls(@ModelAttribute ReportePadronSinDoc reportePadronSinDoc, Model model) {
        String filename = "Reporte_menores_sin_DNI_" + reportePadronSinDoc.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_")+"_del_"+reportePadronSinDoc.getFeIni()+"_al_"+reportePadronSinDoc.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("reportePadronActivos", reportePadronSinDoc);

        List<PadronNominal> list= null;
        if(reportePadronSinDoc.getEsPadron() == 1) {
             list = reporteService.listadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), reportePadronSinDoc.getTiRegFecha(),"1");
        } else if(reportePadronSinDoc.getEsPadron() == 2) {
             list = reporteService.listadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), reportePadronSinDoc.getTiRegFecha(),"2");
        } else if(reportePadronSinDoc.getEsPadron() == 3) {
             list = reporteService.listadoPadronSinDoc(reportePadronSinDoc.getCoUbigeo(), reportePadronSinDoc.getFeIni(), reportePadronSinDoc.getFeFin(), reportePadronSinDoc.getTiRegFecha(),"3");
        }

        model.addAttribute("padronList", list);
        return "reportePadron.xls";
    }

    @RequestMapping(value = "/reporte/reporte_panel.do")
    public String reportePanel(Model model) {
        model.addAttribute("usuario", usuario);
        return "reporte/reporte-panel";
    }


    @RequestMapping(value = "/reporte/reporte_europan.do")
    public String consultaEuropan(@ModelAttribute("reporteEuropan") ReporteEuropan reporteEuropan, Model model) {
        int anios = 6;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        model.addAttribute("edadAnios", edadAnios);
        return "reporte/consulta_europan";
    }

    @RequestMapping(value = "/reporte/consulta_reporte_europan.do", method = RequestMethod.GET)
    public String consultaReporteEuropan(@Valid @ModelAttribute("reporteEuropan") ReporteEuropan reporteEuropan,
                                         BindingResult result,
                                         Model model) {
        int anios = 6;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        model.addAttribute("edadAnios", edadAnios);
        if (result.hasErrors()) {
            return "reporte/consulta_europan";
        }
        // consulta aqui
        reporteEuropan.setsEntidades(StringUtils.join(reporteEuropan.getEntidades(), ","));
        List<Ubigeo> totalDistritos = reporteService.consolidadoEuropan(reporteEuropan);
        model.addAttribute("reporteEuropan", reporteEuropan);
        model.addAttribute("totalDistritos", totalDistritos);
        model.addAttribute("filas", totalDistritos.size());
        return "reporte/consulta_europan";
    }

    @RequestMapping(value = "/reporte/padron_europan_xls.do", method = RequestMethod.GET)
    public String padronEuropanXls(
            @RequestParam(value = "coUbigeoInei") String coUbigeoInei,
            @RequestParam(value = "feIni") String feIni,
            @RequestParam(value = "feFin") String feFin,
            @RequestParam(value = "deEdad") String deEdad,
            @RequestParam(value = "hastaEdad") String hastaEdad,
            @RequestParam(value = "tiRegFecha") String tiRegFecha,
            Model model) {
        String filename = "Reporte_Europan_" + ubigeoService.obtenerDeUbigeoInei(coUbigeoInei).replaceAll("\\s+", "_").replaceAll(",", "_")+"_Del_" + feIni +"_al_"+feFin;

        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        List<PadronNominal> list = reporteService.getPadronUbigeoEuropan(coUbigeoInei, feIni, feFin, deEdad, hastaEdad, tiRegFecha);
        model.addAttribute("padronList", list);
        return "reportePadronEdad.xls";
    }

    @RequestMapping(value = "/reporte/padron_total_distritos_europan_xls.do", method = RequestMethod.GET)
    public String padronTotalDistritosEuropanXls(@ModelAttribute("reporteEuropan") ReporteEuropan reporteEuropan, Model model) {

        String filename = "Reporte_Consolidado_Distritos_Europan_Del_"+reporteEuropan.getFeIni()+"_Al_"+reporteEuropan.getFeFin()+ ".xls";
        model.addAttribute("nombreArchivo", filename);
        List<Ubigeo> list = reporteService.consolidadoEuropan(reporteEuropan);
        model.addAttribute("padronTotalDistritos", list);
        return "reportePadronTotalDistritos.xls";
    }

    @RequestMapping(value = "/reporte/get_distritos_europan.do")
    public
    @ResponseBody
    List<Ubigeo> getDistritosEuropan() {
        return ubigeoService.getDistritosEuropan();
    }

    @RequestMapping(value = "/minsa/ftp_acceso.do")
    public String ftpAcceso(Model model) {
        return "reporte/ftp_acceso";
    }


    @RequestMapping(value = "/minsa_reporte/reporte_usuarios_xls.do", method = RequestMethod.GET)
    public String reporteUsuariosXls(Model model) {
        String filename = "Reporte_usuarios";
        filename = filename + ".xls";
        model.addAttribute("nombreArchivo", filename);

        List<UsuarioExterno> usuarios = usuarioExternoService.getAllDetails();
        logger.debug("usuarios::" + usuarios);
        model.addAttribute("usuarios", usuarios);
        return "reporteUsuarios.xls";
    }

    @RequestMapping(value = "/reporte/reporte-recien-nacidos.do", method = RequestMethod.GET)
    public String padronRecienNacidos(@ModelAttribute ReportePadronRecienNacidos reportePadronRecienNacidos, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6) {
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        int anios = 6, meses = 13;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes);
        model.addAttribute("edadAnios", edadAnios);
        model.addAttribute("edadMeses", edadMeses);
        return "reporte/reporte-recien-nacidos";
    }

    @RequestMapping(value = "/reporte/consulta_reporte_recien_nacidos.do", method = RequestMethod.GET)
    public String padronRecienNacidos(@Valid @ModelAttribute ReportePadronRecienNacidos reportePadronRecienNacidos, BindingResult result, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }

        if (result.hasErrors()) {
            int anios = 6, meses = 13;
            String[] edadAnios = new String[anios + 1];
            for (int anio = 0; anio <= anios; anio++)
                edadAnios[anio] = Integer.toString(anio);
            String[] edadMeses = new String[meses];
            for (int mes = 0; mes < meses; mes++)
                edadMeses[mes] = Integer.toString(mes);
            model.addAttribute("edadAnios", edadAnios);
            model.addAttribute("edadMeses", edadMeses);
            return "reporte/reporte-recien-nacidos";
        }

        int anios = 6, meses = 13;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes);
        model.addAttribute("edadAnios", edadAnios);
        model.addAttribute("edadMeses", edadMeses);

        Integer nuPagina = reportePadronRecienNacidos.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = 0;
        List<PadronNominal> list = null;

        model.addAttribute("coUbigeo", reportePadronRecienNacidos.getCoUbigeo());
        if(reportePadronRecienNacidos.getEsPadron() == 1) { //ACTIVOS
            filas = reporteService.countListarPadronesNacidosActivos(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), reportePadronRecienNacidos.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesNacidosActivos(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronRecienNacidos.getTiRegFecha());
            }else {
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/reporte-recien-nacidos";
            }
        } else if(reportePadronRecienNacidos.getEsPadron() == 2) { //ACTIVOS-OBSERVADOS
            filas = reporteService.countListarPadronesNacidosObservados(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), reportePadronRecienNacidos.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarPadronesNacidosObservados(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronRecienNacidos.getTiRegFecha());
            }else {
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/reporte-recien-nacidos";
            }
        } if(reportePadronRecienNacidos.getEsPadron() == 3) { //TODOS
            filas = reporteService.countListarPadronesNacidosTodos(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), reportePadronRecienNacidos.getTiRegFecha());
            if(filas<padronProperties.MAXROWS_REPORTE) {
            list = reporteService.listarPadronesNacidosTodos(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronRecienNacidos.getTiRegFecha());
            }else {
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/reporte-recien-nacidos";
            }
        }

        model.addAttribute("registros", filas);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));

        model.addAttribute("padronActivos", list);
        //model.addAttribute("reportePadronActivos", reportePadronActivos);
        /*model.addAttribute("coUbigeo", reportePadronRecienNacidos.getCoUbigeo());*/
        model.addAttribute("deUbigeo", reportePadronRecienNacidos.getDeUbigeo());
        model.addAttribute("coEstSalud", reportePadronRecienNacidos.getCoEstSalud());
        model.addAttribute("deEstSalud", reportePadronRecienNacidos.getDeEstSalud());
        model.addAttribute("tiRegFecha", reportePadronRecienNacidos.getTiRegFecha());
//        model.addAttribute("deEdad", reportePadronRecienNacidos.getDeEdad());
        /*model.addAttribute("feNacIni", reportePadronRecienNacidos.getFeNacIni());
        model.addAttribute("feNacFin", reportePadronRecienNacidos.getFeNacFin());*/
        model.addAttribute("feIni", reportePadronRecienNacidos.getFeIni());
        model.addAttribute("feFin", reportePadronRecienNacidos.getFeFin());
        model.addAttribute("filas",filas);
//        model.addAttribute("tiRegistro", reportePadronRecienNacidos.getTiRegistro());
      model.addAttribute("esPadron", reportePadronRecienNacidos.getEsPadron());
        return "reporte/reporte-recien-nacidos";
    }

    @RequestMapping(value = "/reporte/reporte_recien_nacidos_xls.do", method = RequestMethod.GET)
    public String reporteRecienNacidosXls(@ModelAttribute ReportePadronRecienNacidos reportePadronRecienNacidos, Model model) {
        String filename = "Reporte_menores_sin_nombre" + "_" + reportePadronRecienNacidos.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_")+"_del_" + reportePadronRecienNacidos.getFeIni()+"_al_"+ reportePadronRecienNacidos.getFeFin();
        model.addAttribute("nombreArchivo", filename.toUpperCase()+".xlsx");
        model.addAttribute("reportePadronRecienNacidos", reportePadronRecienNacidos);
        List<PadronNominal> list=null;
//        if(reportePadronActivos.getEsPadron() == 1)
        log.info("CO_UBIGEO" + reportePadronRecienNacidos.getCoUbigeo());
        log.info("CO_EST_SALUD" + reportePadronRecienNacidos.getCoEstSalud());

        if(reportePadronRecienNacidos.getEsPadron() == 1){
            list = reporteService.listarPadronesNacidosActivos(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), reportePadronRecienNacidos.getTiRegFecha());
        } else if(reportePadronRecienNacidos.getEsPadron() == 2){
            list = reporteService.listarPadronesNacidosObservados(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), reportePadronRecienNacidos.getTiRegFecha());
        } else if(reportePadronRecienNacidos.getEsPadron() == 3){
            list = reporteService.listarPadronesNacidosTodos(reportePadronRecienNacidos.getCoUbigeo(), reportePadronRecienNacidos.getCoEstSalud(), "", "", reportePadronRecienNacidos.getFeIni(), reportePadronRecienNacidos.getFeFin(), reportePadronRecienNacidos.getTiRegFecha());
        }



/*        else
            list = reporteService.listarPadronesInactivos(reportePadronActivos.getCoUbigeo(), reportePadronActivos.getFeIni(), reportePadronActivos.getFeFin());*/
        model.addAttribute("padronList", list);
        log.info(list);
        return "reportePadron.xls";
    }

    @RequestMapping(value = "/minsa_reporte/reporte_usuarios_minsa.do")
    public String reporteUsuariosMinsa(Model model) {
        return "reporte/reporte_usuarios_minsa";
    }

    /**
     * UI Consulta de usuarios
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/reporte/consulta_usuarios.do")
    public String consultaUsuarios(Model model,
                                   @ModelAttribute(value = "consultaUsuarioDatos") ConsultaUsuarioDatos consultaUsuarioDatos,
                                   @ModelAttribute(value = "consultaUsuarioEntidad") ConsultaUsuarioEntidad consultaUsuarioEntidad
    ) {
        return "reporte_usuarios/consulta_usuario";
    }

    @RequestMapping(value = "/reporte/get_consulta_usuario_datos.do")
    public String doGetConsultaUsuario(
            @ModelAttribute(value = "consultaUsuarioEntidad") ConsultaUsuarioEntidad consultaUsuarioEntidad,
            @ModelAttribute(value = "consultaUsuarioDatos") ConsultaUsuarioDatos consultaUsuarioDatos,
            BindingResult result,
            Model model,
            HttpServletRequest request) {
        ConsultaUsuarioDatosValidator validator = new ConsultaUsuarioDatosValidator(usuarioExternoService);
        validator.validate(consultaUsuarioDatos, result);
        if (result.hasErrors()) {
            model.addAttribute("mensajeErrorUsuarioDatos", "Complete los campos");
//            return "reporte_usuarios/consulta_usuario_datos";
            return "reporte_usuarios/consulta_usuario";
        }
        if (!consultaUsuarioDatos.getCoUsuario().isEmpty()) { // busqueda por DNI
            model.addAttribute("usuarioListDatos", reporteService.buscarUsuarioPorDni(consultaUsuarioDatos.getCoUsuario()));
        } else { // busqueda por apPrimer, apSegundo, prenombres
            model.addAttribute("usuarioListDatos", reporteService.buscarUsuarioPorDatos(consultaUsuarioDatos.getApPrimer(), consultaUsuarioDatos.getApSegundo(), consultaUsuarioDatos.getPrenombres()));
        }
        model.addAttribute("urlBack", request.getRequestURI());
        model.addAttribute("params", "&apPrimer=" + consultaUsuarioDatos.getApPrimer() + "&apSegundo=" + consultaUsuarioDatos.getApSegundo() + "&prenombres=" + consultaUsuarioDatos.getPrenombres());
        return "reporte_usuarios/consulta_usuario";
    }

    /**
     * Consulta por municipalidad/ubigeo
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/reporte/get_consulta_usuario_entidad.do")
    public String doGetConsultaUsuarioEntidad(
            @ModelAttribute("consultaUsuarioDatos") ConsultaUsuarioDatos consultaUsuarioDatos,
            @ModelAttribute("consultaUsuarioEntidad") ConsultaUsuarioEntidad consultaUsuarioEntidad,
            BindingResult result,
            Model model,
            HttpServletRequest request) {
        ConsultaUsuarioEntidadValidator consultaUsuarioEntidadValidator = new ConsultaUsuarioEntidadValidator();
        consultaUsuarioEntidadValidator.validate(consultaUsuarioEntidad, result);
        if (result.hasErrors()) {
            model.addAttribute("mensajeErrorUsuarioEntidad", "Complete los campos");
            return "reporte_usuarios/consulta_usuario";
//            return "reporte_usuarios/consulta_usuario_entidad";
        }
        List<UsuarioExterno> usuarioListEntidad = reporteService.buscaUsuarioPorEntidad(consultaUsuarioEntidad.getCoEntidad(), consultaUsuarioEntidad.getEsUsuario());
        model.addAttribute("coEntidad", consultaUsuarioEntidad.getCoEntidad());
        model.addAttribute("esUsuario", consultaUsuarioEntidad.getEsUsuario());
        model.addAttribute("usuarioListEntidad", usuarioListEntidad);
        model.addAttribute("filas", usuarioListEntidad.size());
        model.addAttribute("generaExcel", true);
        model.addAttribute("urlBack", request.getRequestURI());
        model.addAttribute("params", "coEntidad=" + consultaUsuarioEntidad.getCoEntidad() + "&esUsuario=" + consultaUsuarioEntidad.getEsUsuario());
        return "reporte_usuarios/consulta_usuario";
    }

    @RequestMapping(value = "/reporte/usuario_detalle.do")
    public String usuarioDetalle(@RequestParam("coUsuario") String coUsuario,
                                 @RequestParam(value = "urlBack", required = false) String urlBack,
                                 @ModelAttribute("consultaUsuarioEntidad") ConsultaUsuarioEntidad consultaUsuarioEntidad,
                                 @ModelAttribute("consultaUsuarioDatos") ConsultaUsuarioDatos consultaUsuarioDatos,
                                 Model model) {
        UsuarioExterno usuarioExterno = usuarioExternoService.getUsuarioExterno(coUsuario);
        model.addAttribute("usuario", usuarioExterno);
        model.addAttribute("coUsuario", consultaUsuarioDatos.getCoUsuario());
        model.addAttribute("apPrimer", consultaUsuarioDatos.getApPrimer());
        model.addAttribute("apSegundo", consultaUsuarioDatos.getApSegundo());
        model.addAttribute("prenombres", consultaUsuarioDatos.getPrenombres());
        model.addAttribute("coEntidad", consultaUsuarioEntidad.getCoEntidad());
        model.addAttribute("esUsuario", consultaUsuarioEntidad.getEsUsuario());
        model.addAttribute("urlBack", urlBack);

        if (usuarioExterno != null && usuarioExterno.getCoUsuario() != null) {
            model.addAttribute("img", imagenCiudadano.obtenerImagen(usuarioExterno.getCoUsuario()));
            model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(usuarioExterno.getCoUsuario().getBytes())));
        }
        return "reporte_usuarios/detalle_usuario";
    }

    @RequestMapping(value = "/reporte/consulta_usuario_entidad_xls.do", method = RequestMethod.GET)
    public String doGetConsultaUsuarioEntidadXls(@ModelAttribute("consultaUsuarioEntidad") ConsultaUsuarioEntidad consultaUsuarioEntidad, Model model) {
//        Entidad entidad = entidadService.getEntidad(consultaUsuarioEntidad.getCoEntidad());
        String filename = "consulta_usuario_" + ".xls";
        model.addAttribute("nombreArchivo", filename);

        model.addAttribute("usuarios", reporteService.buscaUsuarioPorEntidad(consultaUsuarioEntidad.getCoEntidad(), consultaUsuarioEntidad.getEsUsuario()));
        return "reporteUsuarios.xls";
    }


    @RequestMapping(value = "/reporte/buscar_entidad_usuarios.do", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Entidad> buscarEntidadUsuarios(@RequestParam(value = "deEntidad", required = true) String deEntidad) {
        if (!deEntidad.matches(REG_VALID_ENTIDAD)) {
            return new ArrayList<Entidad>();
        }
        return usuarioExternoService.buscarEntidad(deEntidad);
    }

    @RequestMapping(value = "/reporte/get_entidad_usuarios.do", method = RequestMethod.GET)
    public
    @ResponseBody
    Entidad getEntidadUsuarios(@RequestParam(value = "coEntidad", required = true) String coEntidad) {
        if (!coEntidad.matches(REG_VALID_STR_NUMERIC)) {
            return new Entidad();
        }
        return reporteService.getEntidadUsuarios(coEntidad);
    }

//    Desde aqui hecho por Jose Vidal Flores

    @RequestMapping(value = "/reporte/reporte_resumen_edad_eess.do", method = RequestMethod.GET)
    public String padronResumenEdad(@ModelAttribute ReportePadronResumenEESS reportePadronResumenEESS, Model model) {

        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        model.addAttribute("frecAtencionList", frecuenciaAtencionService.listarFrecuenciaAtencion());

        int anios = 6, meses = 13;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes);
        model.addAttribute("edadAnios", edadAnios);
        model.addAttribute("edadMeses", edadMeses);
        return "reporte/reporte_resumen_edad_eess";
    }

    @RequestMapping(value = "/reporte/consulta_resumen_edad_eess.do", method = RequestMethod.GET)
    public String padronResumenEdad(@Valid @ModelAttribute ReportePadronResumenEESS reportePadronResumenEdadEESS, BindingResult result, Model model) {

        String coFrecAten = reportePadronResumenEdadEESS.getCoFrecAtencion();

        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }

        model.addAttribute("frecAtencionList", frecuenciaAtencionService.listarFrecuenciaAtencion());

        if (result.hasErrors()) {
            int anios = 6, meses = 13;
            String[] edadAnios = new String[anios + 1];
            for (int anio = 0; anio <= anios; anio++)
                edadAnios[anio] = Integer.toString(anio);
            String[] edadMeses = new String[meses];
            for (int mes = 0; mes < meses; mes++)
                edadMeses[mes] = Integer.toString(mes);
            model.addAttribute("edadAnios", edadAnios);
            model.addAttribute("edadMeses", edadMeses);
            model.addAttribute("tiEstablecimiento",reportePadronResumenEdadEESS.getTiEstablecimiento());
            return "reporte/reporte_resumen_edad_eess";
        }

        int anios = 6, meses = 13;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes);
        model.addAttribute("edadAnios", edadAnios);
        model.addAttribute("edadMeses", edadMeses);

        Integer nuPagina = reportePadronResumenEdadEESS.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = 0;
        List<PadronEdadEESS> list;

        if(reportePadronResumenEdadEESS.getTiEstablecimiento() == 1 && coFrecAten != null && !coFrecAten.equals("") && !coFrecAten.equals("5") ) {
            filas = reporteService.countListarCantNiñosEdadEESSxFrecAten(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getTiEstablecimiento(), coFrecAten, Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            list = reporteService.listarCantNiñosEdadEESSxFrecAten(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getTiEstablecimiento(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, coFrecAten, Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
        } else {
            filas = reporteService.countListarCantNiñosEdadEESS(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getTiEstablecimiento(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            list = reporteService.listarCantNiñosEdadEESS(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getTiEstablecimiento(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
        }

        model.addAttribute("registros", filas);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));

        model.addAttribute("padronActivos", list);
        //model.addAttribute("reportePadronActivos", reportePadronActivos);
        model.addAttribute("tiEstablecimiento", reportePadronResumenEdadEESS.getTiEstablecimiento());
        model.addAttribute("coUbigeo", reportePadronResumenEdadEESS.getCoUbigeo());
        model.addAttribute("deUbigeo", reportePadronResumenEdadEESS.getDeUbigeo());
        model.addAttribute("coEstSalud", reportePadronResumenEdadEESS.getCoEstSalud());
        model.addAttribute("coFrecAtencion", reportePadronResumenEdadEESS.getCoFrecAtencion());
        model.addAttribute("esPadron", reportePadronResumenEdadEESS.getEsPadron());

        return "reporte/reporte_resumen_edad_eess";
    }

    @RequestMapping(value = "/reporte/reporte_resumen_edad_eess_xlsx.do", method = RequestMethod.GET)
    public String padronResumenEdadXls(@ModelAttribute ReportePadronResumenEdadEESS reportePadronResumenEdadEESS, Model model) {

        String coFrecAten = reportePadronResumenEdadEESS.getCoFrecAtencion();

        List<PadronEdadEESS> padronEdadEESS;
        String deTiEstablecimiento = null;
        if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 1){deTiEstablecimiento = "Atención";}
        if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 2){deTiEstablecimiento = "Nacimiento";}
        if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 3){deTiEstablecimiento = "Adscripción";}

        if(reportePadronResumenEdadEESS.getTiEstablecimiento() == 1 && coFrecAten != null && !coFrecAten.equals("") && !coFrecAten.equals("5") ) {
            int cantRegistros = reporteService.countListarCantNiñosEdadEESSxFrecAten(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getTiEstablecimiento(),coFrecAten, Integer.toString(reportePadronResumenEdadEESS.getEsPadron()) );
            padronEdadEESS = reporteService.listarCantNiñosEdadEESSxFrecAten(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getTiEstablecimiento(), 1,  cantRegistros, coFrecAten, Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));

            if (!padronEdadEESS.isEmpty()) {
                for (int i = 0; i < padronEdadEESS.size(); i++) {
                    padronEdadEESS.get(i).setCoFrecuenciaAtencion(coFrecAten);
                }
            }

            String filename = "RESUMEN_CANT_MENORES_EESS_" + deTiEstablecimiento + "_" + reportePadronResumenEdadEESS.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_");


            model.addAttribute("nombreArchivo", filename.toUpperCase()+".xlsx");
            model.addAttribute("reportePadronResumenEdadEESS", reportePadronResumenEdadEESS);
            model.addAttribute("deTiEstablecimiento", deTiEstablecimiento);
            model.addAttribute("padronEdadEESSList", padronEdadEESS);

            return "reporteResumenEdadEESSatencion.xlsx";

        }else{
            int cantRegistros = reporteService.countListarCantNiñosEdadEESS(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getTiEstablecimiento(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            padronEdadEESS = reporteService.listarCantNiñosEdadEESS(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getTiEstablecimiento(), 1,  cantRegistros, Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));

            String filename = "RESUMEN_CANT_MENORES_EESS_" + deTiEstablecimiento + "_" + reportePadronResumenEdadEESS.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_");


            model.addAttribute("nombreArchivo", filename.toUpperCase()+".xlsx");
            model.addAttribute("reportePadronResumenEdadEESS", reportePadronResumenEdadEESS);
            model.addAttribute("deTiEstablecimiento", deTiEstablecimiento);
            model.addAttribute("padronEdadEESSList", padronEdadEESS);

            return "reporteResumenEdadEESS.xlsx";
        }
/*
        String filename = "RESUMEN_CANT_MENORES_EESS_" + deTiEstablecimiento + "_" + reportePadronResumenEdadEESS.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_");


        model.addAttribute("nombreArchivo", filename.toUpperCase()+".xlsx");
        model.addAttribute("reportePadronResumenEdadEESS", reportePadronResumenEdadEESS);
        model.addAttribute("deTiEstablecimiento", deTiEstablecimiento);
        model.addAttribute("padronEdadEESSList", padronEdadEESS);

        return "reporteResumenEdadEESS.xlsx";*/
    }

    @RequestMapping(value = "/reporte/reporte_detalle_edad_eess.do", method = RequestMethod.GET)
    public String padronDetalleEdad(@ModelAttribute ReportePadronResumenEdadEESS reportePadronResumenEdadEESS, Model model) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }

        model.addAttribute("coFrecAtencionList",frecuenciaAtencionService.listarFrecuenciaAtencion());

        int anios = 6, meses = 13;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes);
        model.addAttribute("edadAnios", edadAnios);
        model.addAttribute("edadMeses", edadMeses);
        return "reporte/reporte_detalle_edad_eess";
    }

    @RequestMapping(value = "/reporte/consulta_detalle_edad_eess.do", method = RequestMethod.GET)
    public String padronDetalleEdad(@Valid @ModelAttribute ReportePadronResumenEdadEESS reportePadronResumenEdadEESS, BindingResult result, Model model) {

        String coFrecAten = reportePadronResumenEdadEESS.getCoFrecAtencion();

        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }

        model.addAttribute("coFrecAtencionList",frecuenciaAtencionService.listarFrecuenciaAtencion());

        if (result.hasErrors()) {
            int anios = 6, meses = 13;
            String[] edadAnios = new String[anios + 1];
            for (int anio = 0; anio <= anios; anio++)
                edadAnios[anio] = Integer.toString(anio);
            String[] edadMeses = new String[meses];
            for (int mes = 0; mes < meses; mes++)
                edadMeses[mes] = Integer.toString(mes);
            model.addAttribute("edadAnios", edadAnios);
            model.addAttribute("edadMeses", edadMeses);
            model.addAttribute("tiEstablecimiento",reportePadronResumenEdadEESS.getTiEstablecimiento());
            return "reporte/reporte_detalle_edad_eess";
        }

        int anios = 6, meses = 13;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes);
        model.addAttribute("edadAnios", edadAnios);
        model.addAttribute("edadMeses", edadMeses);

        Integer nuPagina = reportePadronResumenEdadEESS.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = 0;
        List<PadronNominal> list;


        model.addAttribute("tiEstablecimiento", reportePadronResumenEdadEESS.getTiEstablecimiento());
        model.addAttribute("coEstSalud", reportePadronResumenEdadEESS.getCoEstSalud());
        model.addAttribute("coUbigeo", reportePadronResumenEdadEESS.getCoUbigeo());
        model.addAttribute("nuEdad", reportePadronResumenEdadEESS.getNuEdad());

        if(reportePadronResumenEdadEESS.getTiEstablecimiento() == 1 && coFrecAten != null && !coFrecAten.equals("") && !coFrecAten.equals("5")){
            filas = reporteService.countListarNiñosEdadEESSxFrecAten(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getCoEstSalud(), reportePadronResumenEdadEESS.getTiEstablecimiento(), Integer.toString(reportePadronResumenEdadEESS.getNuEdad()), coFrecAten, reportePadronResumenEdadEESS.getFeIni(), reportePadronResumenEdadEESS.getFeFin(), reportePadronResumenEdadEESS.getTiRegFecha(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarNiñosEdadEESSxFrecAten(reportePadronResumenEdadEESS.getCoUbigeo(),reportePadronResumenEdadEESS.getCoEstSalud() , reportePadronResumenEdadEESS.getTiEstablecimiento(),Integer.toString(reportePadronResumenEdadEESS.getNuEdad()), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, coFrecAten, reportePadronResumenEdadEESS.getFeIni(),reportePadronResumenEdadEESS.getFeFin(), reportePadronResumenEdadEESS.getTiRegFecha(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            } else {
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/reporte_detalle_edad_eess";
            }
        } else {
            filas = reporteService.countListarNiñosEdadEESS(reportePadronResumenEdadEESS.getCoUbigeo(),reportePadronResumenEdadEESS.getCoEstSalud() , reportePadronResumenEdadEESS.getTiEstablecimiento(),Integer.toString(reportePadronResumenEdadEESS.getNuEdad()), reportePadronResumenEdadEESS.getFeIni(),reportePadronResumenEdadEESS.getFeFin(), reportePadronResumenEdadEESS.getTiRegFecha(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            if(filas<padronProperties.MAXROWS_REPORTE) {
                list = reporteService.listarNiñosEdadEESS(reportePadronResumenEdadEESS.getCoUbigeo(), reportePadronResumenEdadEESS.getCoEstSalud(), reportePadronResumenEdadEESS.getTiEstablecimiento(), Integer.toString(reportePadronResumenEdadEESS.getNuEdad()), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, reportePadronResumenEdadEESS.getFeIni(), reportePadronResumenEdadEESS.getFeFin(), reportePadronResumenEdadEESS.getTiRegFecha(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            }else{
                model.addAttribute("limitSize", padronProperties.MESSAGE_MAX_ROW);
                return "reporte/reporte_detalle_edad_eess";
            }
        }

        model.addAttribute("registros", filas);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));

        model.addAttribute("padronActivos", list);
        //model.addAttribute("reportePadronActivos", reportePadronActivos);

        model.addAttribute("nuEdad", reportePadronResumenEdadEESS.getNuEdad());
        model.addAttribute("deEstSalud", reportePadronResumenEdadEESS.getDeEstSalud());
        model.addAttribute("deUbigeo", reportePadronResumenEdadEESS.getDeUbigeo());
        model.addAttribute("feIni", reportePadronResumenEdadEESS.getFeIni());
        model.addAttribute("feFin", reportePadronResumenEdadEESS.getFeFin());
        model.addAttribute("tiRegFecha", reportePadronResumenEdadEESS.getTiRegFecha());
        model.addAttribute("esPadron", reportePadronResumenEdadEESS.getEsPadron());
        return "reporte/reporte_detalle_edad_eess";
    }

    @RequestMapping(value = "/reporte/reporte_detalle_edad_eess_xlsx.do", method = RequestMethod.GET)
    public String padronDetalleEdadXLSX(@ModelAttribute ReportePadronResumenEdadEESS reportePadronResumenEdadEESS, Model model) {

        String coFrecAten = reportePadronResumenEdadEESS.getCoFrecAtencion();

        List<PadronNominal> padronNominal;
        String deTiEstablecimiento = null;
        if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 1){deTiEstablecimiento = "Atención";}
        if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 2){deTiEstablecimiento = "Nacimiento";}
        if (reportePadronResumenEdadEESS.getTiEstablecimiento() == 3){deTiEstablecimiento = "Adscripción";}

        if(reportePadronResumenEdadEESS.getTiEstablecimiento() == 1 && coFrecAten != null && !coFrecAten.equals("") && !coFrecAten.equals("5") ){

            int cantRegistros = reporteService.countListarNiñosEdadEESSxFrecAten(reportePadronResumenEdadEESS.getCoUbigeo(),reportePadronResumenEdadEESS.getCoEstSalud() , reportePadronResumenEdadEESS.getTiEstablecimiento(),Integer.toString(reportePadronResumenEdadEESS.getNuEdad()),coFrecAten, reportePadronResumenEdadEESS.getFeIni(), reportePadronResumenEdadEESS.getFeFin(), reportePadronResumenEdadEESS.getTiRegFecha(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            padronNominal = reporteService.listarNiñosEdadEESSxFrecAten(reportePadronResumenEdadEESS.getCoUbigeo(),reportePadronResumenEdadEESS.getCoEstSalud() , reportePadronResumenEdadEESS.getTiEstablecimiento(),Integer.toString(reportePadronResumenEdadEESS.getNuEdad()),  1, cantRegistros,coFrecAten, reportePadronResumenEdadEESS.getFeIni(), reportePadronResumenEdadEESS.getFeFin(), reportePadronResumenEdadEESS.getTiRegFecha(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()) );
            if (!padronNominal.isEmpty()) {
                for (int i = 0; i < padronNominal.size(); i++) {
                    padronNominal.get(i).setCoFrecAtencion(coFrecAten);
                }
            }
            String filename = "DETALLE_MENORES_EESS_" + deTiEstablecimiento + "_" + reportePadronResumenEdadEESS.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_");

            model.addAttribute("nombreArchivo", filename.toUpperCase()+".xlsx");
            model.addAttribute("reportePadronResumenEdadEESS", reportePadronResumenEdadEESS);
            model.addAttribute("deTiEstablecimiento", deTiEstablecimiento);
            model.addAttribute("padronNominalList", padronNominal);

            return "reporteDetalleEdadEESSatencion.xlsx";

        } else {
            int cantRegistros = reporteService.countListarNiñosEdadEESS(reportePadronResumenEdadEESS.getCoUbigeo(),reportePadronResumenEdadEESS.getCoEstSalud() , reportePadronResumenEdadEESS.getTiEstablecimiento(),Integer.toString(reportePadronResumenEdadEESS.getNuEdad()), reportePadronResumenEdadEESS.getFeIni(), reportePadronResumenEdadEESS.getFeFin(), reportePadronResumenEdadEESS.getTiRegFecha(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            padronNominal = reporteService.listarNiñosEdadEESS(reportePadronResumenEdadEESS.getCoUbigeo(),reportePadronResumenEdadEESS.getCoEstSalud() , reportePadronResumenEdadEESS.getTiEstablecimiento(),Integer.toString(reportePadronResumenEdadEESS.getNuEdad()),  1, cantRegistros, reportePadronResumenEdadEESS.getFeIni(), reportePadronResumenEdadEESS.getFeFin(), reportePadronResumenEdadEESS.getTiRegFecha(), Integer.toString(reportePadronResumenEdadEESS.getEsPadron()));
            String filename = "DETALLE_MENORES_EESS_" + deTiEstablecimiento + "_" + reportePadronResumenEdadEESS.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_");

            model.addAttribute("nombreArchivo", filename.toUpperCase()+".xlsx");
            model.addAttribute("reportePadronResumenEdadEESS", reportePadronResumenEdadEESS);
            model.addAttribute("deTiEstablecimiento", deTiEstablecimiento);
            model.addAttribute("padronNominalList", padronNominal);

            return "reporteDetalleEdadEESS.xlsx";
        }


    }

}