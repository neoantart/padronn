package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.reniec.padronn.logic.dao.ConfiguracionAplicacionDao;
import pe.gob.reniec.padronn.logic.dao.GriasDao;
import pe.gob.reniec.padronn.logic.dao.PadronImgDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUbigeoForm;
import pe.gob.reniec.padronn.logic.model.form.FormBuscarMenor;
import pe.gob.reniec.padronn.logic.model.form.FormBuscarPersona;
import pe.gob.reniec.padronn.logic.model.form.ReportePadronMovimientos;
import pe.gob.reniec.padronn.logic.service.*;
import pe.gob.reniec.padronn.logic.util.BasicImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import pe.gob.reniec.padronn.logic.util.SimpleBase64;
import pe.gob.reniec.padronn.logic.web.validator.ConsultaUbigeoFormValidator;
import pe.gob.reniec.padronn.logic.web.validator.checks.ReporteMidisCheck;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class ReporteMidisController {
    private static final String REG_VALID_UBIGEO = "[\\w\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-, \\.]+";
    private static final String REG_VALID_STR_NUMERIC = "\\d*";

    private static final List<String> entidadLocal = Arrays.asList(Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad(), Entidad.TipoEntidad.DISA.getCoTipoEntidad(), Entidad.TipoEntidad.RED.getCoTipoEntidad(), Entidad.TipoEntidad.MICRORED.getCoTipoEntidad(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());

    @Autowired
    ReporteService reporteService;

    @Autowired
    UbigeoService ubigeoService;

    @Autowired
    DominioService dominioService;

    @Autowired
    MidisService midisService;

    @Autowired
    PadronService padronService;

    @Autowired
    Usuario usuario;

    @Autowired
    GriasDao GriasDao;

    @Autowired
    BusquedaDeMenorService busquedaDeMenorService;

    @Autowired
    AniService aniService;


    @Autowired
    PadronProperties padronProperties;

    @Autowired
    BasicImagenCiudadano basicImagenCiudadano;

    @Autowired
    PadronImgDao padronImgDao;

    Logger logger = Logger.getLogger(ReporteMidisController.class);

    @RequestMapping(value = {"/reporte_midis/consolidado.do"})
    public ModelAndView showConsolidado(HttpServletRequest request) {
        String vista = "reporte_midis/consolidado";
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("depas", reporteService.getCantidadesDepartamento());
        model.put("fechaGeneracion", reporteService.getFechaGeneracionConsolidado());

        model.put("urlVolver", request.getRequestURI());
        return new ModelAndView(vista, model);
    }

    @RequestMapping(value = {"/reporte_midis/consulta_ubigeo_form.do"})
    public ModelAndView formListarMenores(@ModelAttribute("consultaUbigeoForm") ConsultaUbigeoForm consultaUbigeoForm) {
        String vista = "reporte_midis/consulta_ubigeo_form";
        Map<String, Object> model = new HashMap<String, Object>();
        llenarCamposListarMenores(model);
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.put("ubigeoReadOnly",true);
            model.put("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.put("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        return new ModelAndView(vista, model);
    }

    @RequestMapping(value = {"/reporte_midis/sendFormListarMenores.do", "/reporte_midis/listar_menores_ubigeo.do"})
    public ModelAndView sendFormListarMenores(@Valid @ModelAttribute("consultaUbigeoForm") ConsultaUbigeoForm consultaUbigeoForm,
                                              BindingResult result, HttpServletRequest request) {

        ConsultaUbigeoFormValidator consultaUbigeoFormValidator = new ConsultaUbigeoFormValidator();
        consultaUbigeoFormValidator.validate(consultaUbigeoForm, result);
        Map<String, Object> model = new HashMap<String, Object>();
        llenarCamposListarMenores(model);
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.put("ubigeoReadOnly", true);
            model.put("deUbigeoReadOnly", reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.put("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        if (result.hasErrors()) {
            return new ModelAndView("reporte_midis/consulta_ubigeo_form", model);
        }
        Integer nuPagina = consultaUbigeoForm.getNuPagina() != null ? Integer.valueOf(consultaUbigeoForm.getNuPagina()) : null;
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;

        int filas=0;
        List<PadronNominal> list = null;
        if(consultaUbigeoForm.getEsPadron() == 1){//Activos
            filas = midisService.countRowsListarPadronActivos(consultaUbigeoForm);
            list = midisService.listarPadronActivos(consultaUbigeoForm, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        } else if(consultaUbigeoForm.getEsPadron() == 2){//activos-observados
            filas = midisService.countRowsListarPadronObservados(consultaUbigeoForm);
            list = midisService.listarPadronObservados(consultaUbigeoForm, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        } else if(consultaUbigeoForm.getEsPadron() == 3){//Todos
            filas = midisService.countRowsListarPadronTodos(consultaUbigeoForm);
            list = midisService.listarPadronTodos(consultaUbigeoForm, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        }

        model.put("padronList", list);
        model.put("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.put("nuPagina", nuPagina);
        model.put("filas", filas);
        model.put("coUbigeo", consultaUbigeoForm.getCoUbigeo());
        model.put("tiDocMenor", consultaUbigeoForm.getTiDocMenor());
        model.put("deUbigeo", ubigeoService.obtenerDeUbigeoInei(consultaUbigeoForm.getCoUbigeo()));
        model.put("deEdad", consultaUbigeoForm.getDeEdad());
        model.put("hastaEdad", consultaUbigeoForm.getHastaEdad());
        model.put("feIni", consultaUbigeoForm.getFeIni());
        model.put("feFin", consultaUbigeoForm.getFeFin());
        model.put("coGeneroMenor", consultaUbigeoForm.getCoGeneroMenor());
        model.put("coProgramaSocial", consultaUbigeoForm.getCoProgramaSocial());
        model.put("tiSeguro", consultaUbigeoForm.getTiSeguro());
        model.put("tiRegFecha", consultaUbigeoForm.getTiRegFecha());
        model.put("esPadron", consultaUbigeoForm.getEsPadron());
        model.put("urlVolver", consultaUbigeoForm.getUrlVolver());
        model.put("urlVolverFicha", request.getRequestURI());
        model.put("initLoad","");

        String vista = "";
        if ("/reporte_midis/sendFormListarMenores.do".equals(request.getServletPath())) {
            vista = "reporte_midis/consulta_ubigeo_form";
        } else if ("/reporte_midis/listar_menores_ubigeo.do".equals(request.getServletPath())) {
            vista = "reporte_midis/result_listar_menores";
        }
        return new ModelAndView(vista, model);
    }

    @RequestMapping(value = "/reporte_midis/result_listar_menores_xlsx.do")
    public ModelAndView resultListarMenoresXlsx(@ModelAttribute("consultaUbigeoForm") ConsultaUbigeoForm consultaUbigeoForm) {
        Map<String, Object> model = new HashMap<String, Object>();
        String filename = "padron_nominal_" + consultaUbigeoForm.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_");
        model.put("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.put("listaMenores", consultaUbigeoForm);
        if(consultaUbigeoForm.getEsPadron() == 1) {
            model.put("padronList", midisService.listarPadronActivos(consultaUbigeoForm));
        } else if(consultaUbigeoForm.getEsPadron() == 2) {
            model.put("padronList", midisService.listarPadronObservados(consultaUbigeoForm));
        } else if(consultaUbigeoForm.getEsPadron() == 3) {
            model.put("padronList", midisService.listarPadronTodos(consultaUbigeoForm));
        }

        return new ModelAndView("reportePadronEdadMidis.xlsx", model);
    }

    @RequestMapping(value = {"/reporte_midis/form_buscar_menor.do"})
    public ModelAndView formBuscarMenor(@ModelAttribute("formBuscarMenor") FormBuscarMenor formBuscarMenor) {
        String vista = "reporte_midis/form_buscar_menor";
        Map<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView(vista, model);
    }

    @RequestMapping(value = "/reporte_midis/buscar_menor_midis.do", method = RequestMethod.GET)
    public String buscarMenorMidis(
            @ModelAttribute("formBuscarMenor") FormBuscarMenor formBuscarMenor,
            @RequestParam(value = "apPrimerMenor", required = false) String apPrimerMenor,
            @RequestParam(value = "apSegundoMenor", required = false) String apSegundoMenor,
            @RequestParam(value = "prenombresMenor", required = false) String prenombresMenor,
            @RequestParam(value = "nuDoc", required = false) String nuDoc,
            @RequestParam(value = "tiDoc", required = false) String tiDoc,
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            Model model,
            HttpServletRequest request) {

        model.addAttribute("enviado", "true");
        List<Menor> menoresList = null;
        if (nuDoc.isEmpty()) {
            if (apPrimerMenor.isEmpty() && apSegundoMenor.isEmpty()) {
                model.addAttribute("camposMinimos", true);
                return "reporte_midis/form_buscar_menor";
//                return "grias/result_buscar_menor";
            }

            if (apPrimerMenor.isEmpty() && prenombresMenor.isEmpty()) {
                model.addAttribute("camposMinimos", true);
                return "reporte_midis/form_buscar_menor";
//                return "grias/result_buscar_menor";
            }

            if (apSegundoMenor.isEmpty() && prenombresMenor.isEmpty()) {
                model.addAttribute("camposMinimos", true);
                return "reporte_midis/form_buscar_menor";
//                return "grias/result_buscar_menor";
            }

            model.addAttribute("apPrimerMenor", apPrimerMenor);
            model.addAttribute("apSegundoMenor", apSegundoMenor);
            model.addAttribute("prenombresMenor", prenombresMenor);

        } else {
            if (nuDoc.length() > 0 && nuDoc.length() < 8) {
                model.addAttribute("codigoPadron", true);
              /*  model.addAttribute("camposMinimos",true);*/
                formBuscarMenor.setApPrimerMenor("");
                formBuscarMenor.setApSegundoMenor("");
                formBuscarMenor.setPrenombresMenor("");
                model.addAttribute("nuDoc", nuDoc);
                model.addAttribute("tiDoc", tiDoc);
            }
        }
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = GriasDao.countBuscarMenorGrias(apPrimerMenor, apSegundoMenor, prenombresMenor, nuDoc, tiDoc);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("nuPagina", nuPagina);
        menoresList = GriasDao.buscarMenorGrias(apPrimerMenor, apSegundoMenor, prenombresMenor, nuDoc, tiDoc, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);

        model.addAttribute("menoresList", menoresList);
        model.addAttribute("urlVolverFicha", request.getRequestURI());
        model.addAttribute("filas", filas);
        model.addAttribute("nuDoc", nuDoc);
        return "reporte_midis/form_buscar_menor";
    }

    @RequestMapping(value = {"/reporte_midis/form_buscar_madre.do"})
    public ModelAndView formBuscarMadre(@ModelAttribute(value = "formBuscarPersona") FormBuscarPersona formBuscarPersona) {
        String vista = "reporte_midis/form_buscar_madre";
        Map<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView(vista, model);
    }


    /*
    *
    *
    * */
    @RequestMapping(value = "/reporte_midis/buscar_madre_midis.do", method = RequestMethod.GET)
    public String buscarAvanzadaMadre(
                                        @ModelAttribute("formBuscarPersona") FormBuscarPersona formBuscarPersona,
                                        @RequestParam(value = "dni", required = false) String dni,
                                        @RequestParam(value = "apPrimer", required = false) String apPrimer,
                                        @RequestParam(value = "apSegundo", required = false) String apSegundo,
                                        @RequestParam(value = "prenombre", required = false) String prenombre,
                                        @RequestParam(value = "nuPagina", required = false) String pagina,
                                        Model model,
                                        HttpServletRequest request) {
        // validacion
        boolean minimoCampos = false;
        boolean dniinvalido = false;
        if (apPrimer.length() > 0 && apSegundo.length() > 0) {
            minimoCampos = true;
        }
        if (apPrimer.length() > 0 && prenombre.length() > 0) {
            minimoCampos = true;
        }
        if (apSegundo.length() > 0 && prenombre.length() > 0) {
            minimoCampos = true;
        }
        if (apPrimer.length() > 0 && apSegundo.length() > 0 && prenombre.length() > 0) {
            minimoCampos = true;
        }
        if (dni.length() == 8) {
            minimoCampos = true;
        }


        //minimoCampos = true;
        model.addAttribute("urlVolverFicha", request.getRequestURI());
        if (minimoCampos) {
            int paginaActual = obtenerPaginaActual(pagina);
            int filaInicio = ConfiguracionAplicacionDao.CantidadFilasTabla * (paginaActual - 1) + 1;
            int filaFin = ConfiguracionAplicacionDao.CantidadFilasTabla * paginaActual;

            if (dni != null && !dni.isEmpty()) { //BUSQUEDA POR DNI
                Persona persona = aniService.obtenerPorDniEnPersona(dni, Persona.TipoPersona.MADRE);
                List<Persona> personaList = busquedaDeMenorService.listarMenoresPorDniMadre(dni);
                model.addAttribute("persona", persona);
                model.addAttribute("hijosRegistrados", personaList);
                model.addAttribute("dni", dni);
//                return "grias/form_buscar_madre_grias";
                return "reporte_midis/buscarMadrePorDni";
            } else if (apPrimer != null && !apPrimer.isEmpty() ||   //BUSQUEDA POR NOMBRES Y APELLIDOS
                    apSegundo != null && !apSegundo.isEmpty() ||
                    prenombre != null && !prenombre.isEmpty()
                    ) {
                Integer filas = aniService.contarRegistrosPorDatos(apPrimer, apSegundo, prenombre, Persona.TipoPersona.MADRE);
                model.addAttribute("nuPagina", paginaActual);
                model.addAttribute("nuPaginas", Math.floor(filas / ConfiguracionAplicacionDao.CantidadFilasTabla) + (filas % ConfiguracionAplicacionDao.CantidadFilasTabla == 0 ? 0 : 1));
                model.addAttribute("apPrimer", apPrimer);
                model.addAttribute("apSegundo", apSegundo);
                model.addAttribute("prenombre", prenombre);

                List<Persona> personaList = aniService.listarPorDatosEnPersona(apPrimer, apSegundo, prenombre, Persona.TipoPersona.MADRE, filaInicio, filaFin);
                model.addAttribute("personaList", personaList);
                model.addAttribute("cantidadResultados", filas);
                return "reporte_midis/buscarMadrePorDatos";
            } else {
                return "reporte_midis/avanzadaListMadreVacio";
            }
        } else {
            model.addAttribute("msg", "Debe ingresar <strong>al menos dos campos</strong>, para la busqueda del menor.");
            if (dni.length() > 0 && dni.length() < 8)
                model.addAttribute("msg", "DNI no validos,<strong> ingrese número de 8 dígitos<strong>.");
            return "reporte_midis/avanzadaListMadreMinimoCampos";
        }
    }


    @RequestMapping(value = "/reporte_midis/informacionadicionalmadre.do")
    public String informacionAdicionalMadre(@RequestParam(value = "nuDni", required = false) String nuDni, Model model) {
        if (nuDni != null && nuDni.length() == 8) {
            model.addAttribute("persona", aniService.obtenerPorDniEnPersona(nuDni, Persona.TipoPersona.MAYOR));
            model.addAttribute("hijosRegistrados", busquedaDeMenorService.listarMenoresPorDniMadre(nuDni));
            model.addAttribute("urlVolver", "reporte_midis/buscar_madre_midis.do");
            model.addAttribute("urlVolverFicha", "reporte_midis/buscar_madre_midis.do");
            //model.addAttribute("urlVolver", "reporte_midis/form_buscar_madre.do");
            //model.addAttribute("urlVolverFicha", "reporte_midis/form_buscar_madre.do");
        } else {
            model.addAttribute("personaAni", null);
        }
        return "reporte_midis/informacionAdicionalMadre";
    }


    @RequestMapping(value = {"/reporte_midis/consulta_europan.do"})
    public ModelAndView consultaEuropan() {
        String vista = "reporte_midis/consulta_europan";
        Map<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView(vista, model);
    }

    @RequestMapping(value = "/reporte_midis/cantidades_departamentos.do", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> cantidadDepartamentos() {
        Map retorno = new HashMap<String, Object>();
        retorno.put("success", true);
        retorno.put("data", reporteService.getCantidadesDepartamento());
        retorno.put("mensaje", "");
        return retorno;
    }

    @RequestMapping(value = "/reporte_midis/padron_total_distritos.do", method = RequestMethod.GET)
    public String padronTotalDistritos(Model model, HttpServletRequest request) {
        model.addAttribute("totalDistritos", reporteService.listadoPadronesPorUbigeo());
        return "reporte_midis/padron_total_distritos";
    }


    @RequestMapping(value = "/reporte_midis/padron_total_distritos_xls.do", method = RequestMethod.GET)
    public String padronTotalDistritosXls(Model model) {
        String filename = "Reporte_Padron_Total_Distritos";
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xls");
        List<Ubigeo> list = reporteService.listadoPadronesPorUbigeo();
        model.addAttribute("padronTotalDistritos", list);
        return "reportePadronTotalDistritos.xls";
    }

    private void/*Map<String, Object>*/ llenarCamposListarMenores(Map<String, Object> model) {
        //Map<String, Object> result = model;
        int anios = 6, meses = 11;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes + 1);
        model.put("tipoDocs", dominioService.tipoDocumentoItems());
        model.put("programasSociales", reporteService.getProgramasSociales());
        model.put("generos", dominioService.generoItems());
        model.put("tiposSeguro", dominioService.tipoSeguroItems());
        model.put("edadAnios", edadAnios);
        model.put("edadMeses", edadMeses);
        model.put("coUbigeo", usuario.getCoUbigeoInei());
        model.put("initLoad","1");
        /*return result;*/
    }

    @RequestMapping(value = "/reporte_midis/buscar_ubigeo.do", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Lugar> buscarUbigeo(@RequestParam(value = "deUbigeo") String deUbigeo) {
        if (!deUbigeo.matches(REG_VALID_UBIGEO)) {
            return new ArrayList<Lugar>();
        }
        return reporteService.getUbigeo(deUbigeo);
    }

    @RequestMapping(value = "/reporte_midis/get_ubigeo.do", method = RequestMethod.GET)
    public
    @ResponseBody
    Lugar getUbigeo(@RequestParam(value = "coUbigeo") String coUbigeo) {
        if (!coUbigeo.matches(REG_VALID_STR_NUMERIC)) {
            return new Lugar();
        }
        return reporteService.getLugar(coUbigeo);
    }


    @RequestMapping(value = "/reporte_midis/cantidades_provincia.do", method = RequestMethod.GET)
    public String cantidadesProvincia(@RequestParam(value = "coUbigeo") String coUbigeo, Model model, HttpServletRequest request) {
        model.addAttribute("provincias", reporteService.getCantidadesProvincia(coUbigeo));
        model.addAttribute("coUbigeo", coUbigeo.substring(0, 2));
        model.addAttribute("fechaConsolidado", reporteService.getFechaGeneracionConsolidado());
        model.addAttribute("deUbigeo", ubigeoService.obtenerDeUbigeoInei(coUbigeo));
        model.addAttribute("urlVolver", request.getRequestURI());
        model.addAttribute("esPadron","3");
        return "reporte_midis/cantidades-provincia";
    }

    @RequestMapping(value = "/reporte_midis/cantidades_distrito.do", method = RequestMethod.GET)
    public String cantidadesDistrito(@RequestParam(value = "coUbigeo") String coUbigeo, Model model, HttpServletRequest request) {
        model.addAttribute("distritos", reporteService.getCantidadesDistrito(coUbigeo));
        model.addAttribute("fechaConsolidado", reporteService.getFechaGeneracionConsolidado());
        model.addAttribute("coUbigeo", coUbigeo);
        model.addAttribute("deUbigeo", ubigeoService.obtenerDeUbigeoInei(coUbigeo));
        model.addAttribute("urlVolver", request.getRequestURI());
        model.addAttribute("esPadron","3");
        return "reporte_midis/cantidades-distrito";
    }

    @RequestMapping(value = "/reporte_midis/home_reporte_midis.do")
    public String homeReporteMidis() {
        return "reporte_midis/home_reporte_midis";
    }

    @RequestMapping(value = "/reporte_midis/fichaPadron.do")
    public String fichaPadron(@RequestParam("coPadronNominal") String coPadronNominal,
                              @ModelAttribute(value = "consultaUbigeoForm") ConsultaUbigeoForm consultaUbigeoForm,
                              Model model, BindingResult result) {
        model.addAttribute("nuPagina", consultaUbigeoForm.getNuPagina());
        model.addAttribute("coUbigeo", consultaUbigeoForm.getCoUbigeo());
        model.addAttribute("deEdad", consultaUbigeoForm.getDeEdad());
        model.addAttribute("hastaEdad", consultaUbigeoForm.getHastaEdad());
        model.addAttribute("feIni", consultaUbigeoForm.getFeIni());
        model.addAttribute("feFin", consultaUbigeoForm.getFeFin());
        model.addAttribute("coProgramaSocial", consultaUbigeoForm.getCoProgramaSocial());
        model.addAttribute("tiSeguro", consultaUbigeoForm.getTiSeguro());
        model.addAttribute("tiDocMenor", consultaUbigeoForm.getTiDocMenor());
        model.addAttribute("coGeneroMenor", consultaUbigeoForm.getCoGeneroMenor());
        model.addAttribute("padronNominal", padronService.obtenerPorCodigoPadronConDetalles(coPadronNominal));
        model.addAttribute("coPadronNominal", coPadronNominal);

        model.addAttribute("urlVolver", consultaUbigeoForm.getUrlVolver());
        model.addAttribute("urlVolverFicha", consultaUbigeoForm.getUrlVolverFicha());

        //para el caso de volver de busqueda del menor

        model.addAttribute("apPrimerMenor", consultaUbigeoForm.getApPrimerMenor());
        model.addAttribute("apSegundoMenor", consultaUbigeoForm.getApSegundoMenor());
        model.addAttribute("prenombresMenor", consultaUbigeoForm.getPrenombresMenor());
        model.addAttribute("nuDoc", consultaUbigeoForm.getNuDoc());
        model.addAttribute("tiDoc", consultaUbigeoForm.getTiDoc());

        // para volver por busqueda de datos de la madre
        model.addAttribute("apPrimer", consultaUbigeoForm.getApPrimer());
        model.addAttribute("apSegundo", consultaUbigeoForm.getApSegundo());
        model.addAttribute("prenombre", consultaUbigeoForm.getPrenombre());
        model.addAttribute("dni", consultaUbigeoForm.getDni());
        model.addAttribute("esPadron", consultaUbigeoForm.getEsPadron());

        logger.info("coPadronNominal: " + coPadronNominal);
        return "reporte_midis/ficha_padron";
    }

    @RequestMapping(value = "/reporte_midis/fichaPadronPDF.pdf", method = RequestMethod.GET)
    public ModelAndView fichaPadronPDF(@RequestParam(value = "coPadronNominal") String coPadronNominal,
                                       @RequestParam(value = "download", required = false) String download,
                                       @RequestParam(value = "imprimir", required = false) String imprimir) {
        Map<String, Object> model = new HashMap<String, Object>();

        PadronNominal padronNominal = padronService.obtenerPorCodigoPadronConDetalles(coPadronNominal);
        byte[] img = null;

        try {
            String dniMenor = padronNominal.getNuDniMenor();

            if(dniMenor!="" && !dniMenor.isEmpty() && dniMenor!=null) {//registro sin DNI aun, buscamos por dni
               img = basicImagenCiudadano.obtenerImagenCiudadanoWs(padronProperties.URL_WS_IMAGENES, SimpleBase64.encodeBase64(dniMenor), usuario.getLogin());
              //  img = basicImagenCiudadano.obtenerImagenCiudadanoWs(padronProperties.URL_WS_IMAGENES, SimpleBase64.encodeBase64(dniMenor), usuario.getLogin());
            }


        } catch (Exception e) {
            try {
                if (coPadronNominal != null && !coPadronNominal.isEmpty() && coPadronNominal != "") {
                    if (padronImgDao.tieneImagen(coPadronNominal)) {//si tiene imagen en PN
                        PadronImg padronImg = padronImgDao.obtenerPadronImg(coPadronNominal);
                        img = padronImg.getImgFotoMenor();
                    } else {
                        img = null;
                    }
                } else {
                    img = null;
                }

            }
            catch (Exception ex){
                img = null;
                e.printStackTrace();
            }
        }
        padronNominal.setImagen(img);

        model.put("padronNominal", padronNominal);
        model.put("coPadronNominal", coPadronNominal);
        model.put("coUsuario", usuario.getLogin());
        model.put("imprimir", imprimir);
        model.put("download", download);
        String vista = "fichaPadronMidisView";
        if (padronNominal == null)
            vista = "fichaPadronEmptyView";
        /*if(download!=null && !download.isEmpty())
            vista = "fichaPadronDownloadView";*/
        return new ModelAndView(vista, model);
    }

    private int obtenerPaginaActual(String paginaIndicada) {
        int paginaActual;
        if (paginaIndicada == null || paginaIndicada.isEmpty()) {
            paginaActual = 1;
        } else {
            try {
                paginaActual = Integer.parseInt(paginaIndicada);
                if (paginaActual < 1) {
                    paginaActual = 1;
                }
            } catch (NumberFormatException ne) {
                paginaActual = 1;
            }
        }
        return paginaActual;
    }

    @RequestMapping(value = "/reporte_midis/padron_movimientos.do", method = RequestMethod.GET)
    public String padronMovimientos(@ModelAttribute ReportePadronMovimientos reportePadronMovimientos, Model model) {
        return "reporte_midis/padron-movimientos";
    }

    @RequestMapping(value = "/reporte_midis/consulta_padron_movimiento.do", method = RequestMethod.GET)
    public String consultaPadronMovimiento(
            @Validated(value = {ReporteMidisCheck.class}) @ModelAttribute ReportePadronMovimientos reportePadronMovimientos,
            BindingResult result,
            Model model) {
        Integer nuPagina = (reportePadronMovimientos.getNuPagina() == null || Integer.parseInt(reportePadronMovimientos.getNuPagina()) <= 0) ? 1 : Integer.parseInt(reportePadronMovimientos.getNuPagina());
        if (result.hasErrors())
            return "reporte_midis/padron-movimientos";
        Integer filas = midisService.countPadronMovimientos(reportePadronMovimientos.getCoUbigeo(), reportePadronMovimientos.getFeIni(), reportePadronMovimientos.getFeFin());
        List<PadronMovimiento> movimientos = midisService.listadoPadronMovimiento(reportePadronMovimientos.getCoUbigeo(), reportePadronMovimientos.getFeIni(), reportePadronMovimientos.getFeFin(), (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("filas", filas);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("movimientos", movimientos);
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS * (nuPagina - 1));
        model.addAttribute("feIni", reportePadronMovimientos.getFeIni());
        model.addAttribute("feFin", reportePadronMovimientos.getFeFin());
        model.addAttribute("coUbigeo", reportePadronMovimientos.getCoUbigeo());
        return "reporte_midis/padron-movimientos";
    }

    @RequestMapping(value = "/reporte_midis/padron_movimientos_xls.do", method = RequestMethod.GET)
    public String padronMovimientosXls(
            @RequestParam(value = "coUbigeo", required = true) String coUbigeo,
            @RequestParam(value = "feIni", required = true) String feIni,
            @RequestParam(value = "feFin", required = true) String feFin,
            Model model) {

        String filename = "Reporte_Movimientos_Ubigeo";
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        List<PadronMovimiento> list = midisService.listadoPadronMovimiento(coUbigeo, feIni, feFin);
        model.addAttribute("padronMovimientoList", list);
        return "reportePadronMovimientos.xls";

    }

    @RequestMapping(value = "/reporte_midis/get_cantidad_ubigeo_pro_social_depa.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Ubigeo> getCantidadUbigeoProSocialDepa() {
        return midisService.getCantidadUbigeoProSocialDepa();
    }


    @RequestMapping(value = "/reporte_midis/get_cantidad_ubigeo_edad_depa.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Ubigeo> getCantidadUbigeoEdadDepa() {
        return midisService.getCantidadUbigeoEdadDepa();
    }

    @RequestMapping(value = "/reporte_midis/get_cantidad_ubigeo_ti_seguro_depa.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Ubigeo> getCantidadUbigeoTiSeguroDepa() {
        return midisService.getCantidadUbigeoTiSeguroDepa();
    }

    @RequestMapping(value = "/reporte_midis/cantidad_ubigeo_pro_social_xls.do")
    public String getCantidadUbigeoProSocialXls(Model model) {
        String filename = "Reporte_Padron_Total_Pro_Soc";
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("list", midisService.getCantidadUbigeoProSocial());
        return "reportePadronProcSocMidisView.xlsx";
    }

    @RequestMapping(value = "/reporte_midis/cantidad_ubigeo_edad_xls.do")
    public String getCantidadUbigeoEdadXls(Model model) {
        String filename = "Reporte_Padron_Edades";
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("list", midisService.getCantidadUbigeoEdad());
        return "reportePadronEdadesMidisView.xlsx";
    }

    @RequestMapping(value = "/reporte_midis/cantidad_ubigeo_ti_seguro_xls.do")
    public String getCantidadUbigeoTiSeguroXls(Model model) {
        String filename = "Reporte_Padron_Tipo_Seguro";
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("list", midisService.getCantidadUbigeoTiSeguro());
        return "reportePadronTiSeguroMidisView.xlsx";
    }

}

