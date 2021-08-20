package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.http.HttpRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.gob.reniec.padronn.logic.dao.ConfiguracionAplicacionDao;
import pe.gob.reniec.padronn.logic.dao.GriasDao;
import pe.gob.reniec.padronn.logic.dao.PadronImgDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.FormBuscarMenor;
import pe.gob.reniec.padronn.logic.model.form.FormBuscarPersona;
import pe.gob.reniec.padronn.logic.model.form.ListaMenores;
import pe.gob.reniec.padronn.logic.service.*;
import pe.gob.reniec.padronn.logic.util.BasicImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.reniec.padronn.logic.util.SimpleBase64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
public class GriasController {

    private static final String REG_VALID_UBIGEO = "[\\w\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-, \\.]+";
    private static final String REG_VALID_STR_NUMERIC = "\\d*";

    private static final List<String> entidadLocal = Arrays.asList(Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad(), Entidad.TipoEntidad.DISA.getCoTipoEntidad(), Entidad.TipoEntidad.RED.getCoTipoEntidad(), Entidad.TipoEntidad.MICRORED.getCoTipoEntidad(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());

    @Autowired
    DominioService dominioService;

    @Autowired
    GriasDao GriasDao;


    @Autowired
    PadronService padronService;

    @Autowired
    Usuario usuario;

    @Autowired
    ReporteService reporteService;

    @Autowired
    GriasService griasService;

    @Autowired
    UbigeoService ubigeoService;

    @Autowired
    BusquedaDeMenorService busquedaDeMenorService;

    @Autowired
    AniService aniService;


    Logger logger = Logger.getLogger(getClass());

    @Autowired
    BasicImagenCiudadano basicImagenCiudadano;

    @Autowired
    PadronProperties padronProperties;

    @Autowired
    PadronImgDao padronImgDao;


    @RequestMapping(value = "/grias/fichaPadronPDF.pdf", method = RequestMethod.GET)
    public ModelAndView fichaPadronPDF(@RequestParam(value = "coPadronNominal") String coPadronNominal,
                                       @RequestParam(value = "download", required = false) String download,
                                       @RequestParam(value = "imprimir", required = false) String imprimir,
                                       HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        PadronNominal padronNominal = padronService.obtenerPorCodigoPadronConDetalles(coPadronNominal);
        byte[] img=null;

        try {
            String dniMenor = padronNominal.getNuDniMenor();

            if(dniMenor!="" && !dniMenor.isEmpty() && dniMenor!=null) {//registro sin DNI aun, buscamos por dni
//                img = basicImagenCiudadano.obtenerImagenCiudadanoWs(padronProperties.URL_WS_IMAGENES, SimpleBase64.encodeBase64(dniMenor), usuario.getLogin());
                img = basicImagenCiudadano.obtenerImagenCiudadanoWs(padronProperties.URL_WS_IMAGENES, SimpleBase64.encodeBase64(dniMenor), usuario.getLogin());
            }
        } catch (Exception e) {
            try {
                 if (coPadronNominal != null && !coPadronNominal.isEmpty() && coPadronNominal != "") {
                    if (padronImgDao.tieneImagen(coPadronNominal)) {//si tiene imagen en PN
                            img = padronImgDao.obtenerPadronImg(coPadronNominal).getImgFotoMenor();

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
        String vista = "fichaPadronView";
        if (padronNominal == null)
            vista = "fichaPadronEmptyView";
        /*if(download!=null && !download.isEmpty())
            vista = "fichaPadronDownloadView";*/
        return new ModelAndView(vista, model);
    }

    @RequestMapping(value = "/grias/fichaPadron.do")
    public String fichaPadron(@RequestParam("coPadronNominal") String coPadronNominal,
                              @ModelAttribute(value = "listaMenores") ListaMenores listaMenores,
                              Model model, BindingResult result) {
        model.addAttribute("nuPagina", listaMenores.getNuPagina());
        model.addAttribute("coUbigeo", listaMenores.getCoUbigeo());
        model.addAttribute("deEdad", listaMenores.getDeEdad());
        model.addAttribute("hastaEdad", listaMenores.getHastaEdad());
        model.addAttribute("feIni", listaMenores.getFeIni());
        model.addAttribute("feFin", listaMenores.getFeFin());
        model.addAttribute("feNacIni", listaMenores.getFeNacIni());
        model.addAttribute("feNacFin", listaMenores.getFeNacFin());
        model.addAttribute("tiDocMenor", listaMenores.getTiDocMenor());
        model.addAttribute("coGeneroMenor", listaMenores.getCoGeneroMenor());
        model.addAttribute("padronNominal", padronService.obtenerPorCodigoPadronConDetalles(coPadronNominal));
        model.addAttribute("coPadronNominal", coPadronNominal);

        model.addAttribute("urlVolver", listaMenores.getUrlVolver());
        model.addAttribute("urlVolverFicha", listaMenores.getUrlVolverFicha());

        //para el caso de volver de busqueda del menor

        model.addAttribute("apPrimerMenor", listaMenores.getApPrimerMenor());
        model.addAttribute("apSegundoMenor", listaMenores.getApSegundoMenor());
        model.addAttribute("prenombresMenor", listaMenores.getPrenombresMenor());
        model.addAttribute("tiDoc", listaMenores.getTiDoc());
        model.addAttribute("nuDoc", listaMenores.getNuDoc());
//        model.addAttribute("codigoPadron", listaMenores.getCodigoPadron());

        // para volver por busqueda de datos de la madre
        model.addAttribute("apPrimer", listaMenores.getApPrimer());
        model.addAttribute("apSegundo", listaMenores.getApSegundo());
        model.addAttribute("prenombre", listaMenores.getPrenombre());
        model.addAttribute("dni", listaMenores.getDni());
        model.addAttribute("esPadron", listaMenores.getEsPadron());

        logger.info("coPadronNominal: " + coPadronNominal);
        return "grias/ficha_padron";
    }

    @RequestMapping(value = "/grias/consolidadoGrias.do")
    public String consolidadoGrias(Model model, HttpServletRequest request) {
        model.addAttribute("depas", reporteService.getCantidadesDepartamento());
        model.addAttribute("fechaGeneracion", reporteService.getFechaGeneracionConsolidado());
        model.addAttribute("esPadron", "3");
        model.addAttribute("urlVolver", request.getRequestURI());
        return "grias/cantidades_departamento_total";
    }

    @RequestMapping(value = "/grias/cantidades_departamentos.do", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> cantidadDepartamentos() {
        Map retorno = new HashMap<String, Object>();
        retorno.put("success", true);
        retorno.put("data", reporteService.getCantidadesDepartamento());
        retorno.put("mensaje", "");
        return retorno;
    }

    @RequestMapping(value = "/grias/padron_total_distritos.do", method = RequestMethod.GET)
    public String padronTotalDistritos(Model model, HttpServletRequest request) {
        model.addAttribute("totalDistritos", reporteService.listadoPadronesPorUbigeo());
        return "grias/padron-total-distritos";
    }

    @RequestMapping(value = "/grias/padron_total_distritos_xls.do", method = RequestMethod.GET)
    public String padronTotalDistritosXls(Model model) {
        String filename = "Reporte_Padron_Total_Distritos";
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xls");
        List<Ubigeo> list = reporteService.listadoPadronesPorUbigeo();
        model.addAttribute("padronTotalDistritos", list);
        return "reportePadronTotalDistritos.xls";
    }

    @RequestMapping(value = "/grias/cantidades_provincia.do", method = RequestMethod.GET)
    public String cantidadesProvincia(@RequestParam(value = "coUbigeo") String coUbigeo, Model model, HttpServletRequest request) {
        model.addAttribute("provincias", reporteService.getCantidadesProvincia(coUbigeo));
        model.addAttribute("coUbigeo", coUbigeo.substring(0, 2));
        model.addAttribute("fechaConsolidado", reporteService.getFechaGeneracionConsolidado());
        model.addAttribute("deUbigeo",ubigeoService.obtenerDeUbigeoInei(coUbigeo));
        model.addAttribute("esPadron","3");
        model.addAttribute("urlVolver", request.getRequestURI());
        return "grias/cantidades-provincia";
    }

    @RequestMapping(value = "/grias/cantidades_distrito.do", method = RequestMethod.GET)
    public String cantidadesDistrito(@RequestParam(value = "coUbigeo") String coUbigeo, Model model, HttpServletRequest request) {
        model.addAttribute("distritos", reporteService.getCantidadesDistrito(coUbigeo));
        model.addAttribute("fechaConsolidado", reporteService.getFechaGeneracionConsolidado());
        model.addAttribute("coUbigeo", coUbigeo);
        model.addAttribute("deUbigeo",ubigeoService.obtenerDeUbigeoInei(coUbigeo));
        model.addAttribute("esPadron", "3");
        model.addAttribute("urlVolver", request.getRequestURI());
        return "grias/cantidades-distrito";
    }


    private void llenarCamposListarMenores(Model model) {
        int anios = 6, meses = 11;
        String[] edadAnios = new String[anios + 1];
        for (int anio = 0; anio <= anios; anio++)
            edadAnios[anio] = Integer.toString(anio);
        String[] edadMeses = new String[meses];
        for (int mes = 0; mes < meses; mes++)
            edadMeses[mes] = Integer.toString(mes + 1);
        model.addAttribute("edadAnios", edadAnios);
        model.addAttribute("edadMeses", edadMeses);
    }

    /**
     * Formulario para listar menores
     */
    @RequestMapping(value = "/grias/form_listar_menores.do")
    public String formListarMenores(Model model, @ModelAttribute(value = "listaMenores") ListaMenores listaMenores, HttpServletRequest request) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }
        llenarCamposListarMenores(model);
        listaMenores.setUrlVolver(request.getRequestURI());
        model.addAttribute("coUbigeo", usuario.getCoUbigeoInei());
        model.addAttribute("initLoad","1");
        return "grias/form_listar_menores";
    }

    /**
     * resultados de formulario para listar menores
     */
    @RequestMapping(value = "/grias/result_listar_menores.do")
    public String listarMenores(@Valid @ModelAttribute(value = "listaMenores") ListaMenores listaMenores,
                                BindingResult result, Model model, HttpServletRequest request) {
        if (entidadLocal.contains(usuario.getCoTipoEntidad()) && usuario.getCoUbigeoInei().length()==6){
            model.addAttribute("ubigeoReadOnly",true);
            model.addAttribute("deUbigeoReadOnly",reporteService.getLugar(usuario.getCoUbigeoInei()).getDeLugar());
            model.addAttribute("coUbigeoReadOnly", usuario.getCoUbigeoInei());
        }

        if (result.hasErrors()) {
            llenarCamposListarMenores(model);
            return "grias/form_listar_menores";
        }

        Integer nuPagina = listaMenores.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        List<PadronNominal> padronList=null;
        int filas=0;

        if(listaMenores.getEsPadron() == 1){//activos
            filas = griasService.countRowsListarPadronActivos(listaMenores);
            padronList = griasService.listarPadronActivos(listaMenores, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        } else if(listaMenores.getEsPadron() == 2){//activos-observados
            filas = griasService.countRowsListarPadronObservados(listaMenores);
            padronList = griasService.listarPadronObservados(listaMenores, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        } else if(listaMenores.getEsPadron() == 3){//todos
            filas = griasService.countRowsListarPadronTodos(listaMenores);
            padronList = griasService.listarPadronTodos(listaMenores, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        }

        model.addAttribute("padronList", padronList);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);

        llenarCamposListarMenores(model);
        model.addAttribute("filas", filas);
        model.addAttribute("coUbigeo", listaMenores.getCoUbigeo());
        model.addAttribute("deUbigeo", ubigeoService.obtenerDeUbigeoInei(listaMenores.getCoUbigeo()));
        model.addAttribute("deEdad", listaMenores.getDeEdad());
        model.addAttribute("hastaEdad", listaMenores.getHastaEdad());
        model.addAttribute("feIni", listaMenores.getFeIni());
        model.addAttribute("feFin", listaMenores.getFeFin());
        model.addAttribute("tiRegFecha", listaMenores.getTiRegFecha());
        model.addAttribute("feNacIni", listaMenores.getFeNacIni());
        model.addAttribute("feNacFin", listaMenores.getFeNacFin());
        model.addAttribute("tiDocMenor", listaMenores.getTiDocMenor());
        model.addAttribute("coGeneroMenor", listaMenores.getCoGeneroMenor());
        model.addAttribute("esPadron", listaMenores.getEsPadron());
        model.addAttribute("urlVolver", listaMenores.getUrlVolver());
        model.addAttribute("urlVolverFicha", request.getRequestURI());
        return "grias/form_listar_menores";
    }

    @RequestMapping(value = "/grias/listar_menores_ubigeo.do")
    public String listarMenoresUbigeo(@ModelAttribute(value = "listaMenores") ListaMenores listaMenores,
                                      Model model, HttpServletRequest request) {
        Integer nuPagina = listaMenores.getNuPagina();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = griasService.countRowsListarPadronActivos(listaMenores);

        model.addAttribute("padronList", griasService.listarPadronTodos(listaMenores, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS));
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("filas", filas);

        llenarCamposListarMenores(model);
        model.addAttribute("coUbigeo", listaMenores.getCoUbigeo());
        model.addAttribute("deUbigeo", ubigeoService.obtenerDeUbigeoInei(listaMenores.getCoUbigeo()));
        model.addAttribute("deEdad", listaMenores.getDeEdad());
        model.addAttribute("hastaEdad", listaMenores.getHastaEdad());
        model.addAttribute("feIni", listaMenores.getFeIni());
        model.addAttribute("feFin", listaMenores.getFeFin());
        model.addAttribute("tiRegFecha", listaMenores.getTiRegFecha());
        model.addAttribute("feNacIni", listaMenores.getFeNacIni());
        model.addAttribute("feNacFin", listaMenores.getFeNacFin());
        model.addAttribute("tiDocMenor", listaMenores.getTiDocMenor());
        model.addAttribute("coGeneroMenor", listaMenores.getCoGeneroMenor());
        model.addAttribute("urlVolver", listaMenores.getUrlVolver());
        model.addAttribute("urlVolverFicha", request.getRequestURI());
        model.addAttribute("esPadron", listaMenores.getEsPadron());
        return "grias/result_listar_menores";
    }


    @RequestMapping(value = "/grias/result_listar_menores_xlsx.do")
    public String resultListarMenores(@ModelAttribute(value = "listaMenores") ListaMenores listaMenores, Model model) {
        String filename = "lista_menores_grias_" + listaMenores.getDeUbigeo().replaceAll("\\s+", "_").replaceAll(",", "_");
        model.addAttribute("nombreArchivo", filename.toUpperCase() + ".xlsx");
        model.addAttribute("listaMenores", listaMenores);

        if(listaMenores.getEsPadron() == 1)
            model.addAttribute("padronList", griasService.listarPadronActivos(listaMenores));
        else if (listaMenores.getEsPadron() == 2)
            model.addAttribute("padronList", griasService.listarPadronObservados(listaMenores));
        else if (listaMenores.getEsPadron() == 3)
            model.addAttribute("padronList", griasService.listarPadronTodos(listaMenores));

        return "reportePadronEdadGrias.xlsx";
    }

    @RequestMapping(value = "/grias/buscar_ubigeo.do", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Lugar> buscarUbigeo(@RequestParam(value = "deUbigeo") String deUbigeo) {
        if (!deUbigeo.matches(REG_VALID_UBIGEO)) {
            return new ArrayList<Lugar>();
        }
        return reporteService.getUbigeo(deUbigeo);
    }

    @RequestMapping(value = "/grias/get_ubigeo.do", method = RequestMethod.GET)
    public
    @ResponseBody
    Lugar getUbigeo(@RequestParam(value = "coUbigeo") String coUbigeo) {
        if (!coUbigeo.matches(REG_VALID_STR_NUMERIC)) {
            return new Lugar();
        }
        return reporteService.getLugar(coUbigeo);
    }

    @RequestMapping(value = "/grias/form_buscar_menor_grias.do")
    public String buscarMenorGrias(Model model, @ModelAttribute("formBuscarMenor")FormBuscarMenor formBuscarMenor) {
        return "grias/form_buscar_menor_grias";
    }

    @RequestMapping(value = "/grias/buscar_menor_grias.do", method = RequestMethod.GET)
    public String buscarMenorGrias(
            @ModelAttribute("formBuscarMenor")FormBuscarMenor formBuscarMenor,
            @RequestParam(value = "apPrimerMenor", required = false) String apPrimerMenor,
            @RequestParam(value = "apSegundoMenor", required = false) String apSegundoMenor,
            @RequestParam(value = "prenombresMenor", required = false) String prenombresMenor,
//            @RequestParam(value = "codigoPadron", required = false) String codigoPadron,
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            @RequestParam(value = "tiDoc", required = false) String tiDoc,
            @RequestParam(value = "nuDoc", required = false) String nuDoc,
            Model model,
            HttpServletRequest request) {

        model.addAttribute("enviado", "true");
        List<Menor> menoresList = null;
        if (nuDoc.isEmpty()) {
            if (apPrimerMenor.isEmpty() && apSegundoMenor.isEmpty()) {
                model.addAttribute("camposMinimos", true);
                return "grias/form_buscar_menor_grias";
//                return "grias/result_buscar_menor";
            }

            if (apPrimerMenor.isEmpty() && prenombresMenor.isEmpty()) {
                model.addAttribute("camposMinimos", true);
                return "grias/form_buscar_menor_grias";
//                return "grias/result_buscar_menor";
            }

            if (apSegundoMenor.isEmpty() && prenombresMenor.isEmpty()) {
                model.addAttribute("camposMinimos", true);
                return "grias/form_buscar_menor_grias";
//                return "grias/result_buscar_menor";
            }

            model.addAttribute("apPrimerMenor", apPrimerMenor);
            model.addAttribute("apSegundoMenor", apSegundoMenor);
            model.addAttribute("prenombresMenor", prenombresMenor);

        }else{
            model.addAttribute("nuDoc", true);
              /*  model.addAttribute("camposMinimos",true);*/
            formBuscarMenor.setApPrimerMenor("");
            formBuscarMenor.setApSegundoMenor("");
            formBuscarMenor.setPrenombresMenor("");
            model.addAttribute("nuDoc", nuDoc);
            model.addAttribute("tiDoc", tiDoc);
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
//        return "grias/result_buscar_menor";
        return "grias/form_buscar_menor_grias";
    }

    /**
     * formulario para la busqueda de un menor
     */
    @RequestMapping(value = "/grias/form_buscar_menor.do")
    public String buscarMenor(Model model) {
        return "grias/form_buscar_menor";
    }

    /**
     * resultado de formulario para la busqueda de un menor
     */
    @RequestMapping(value = "/grias/result_buscar_menor.do")
    public String resultBuscarMenor() {
        return "grias/result_buscar_menor";
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

    @RequestMapping(value = "/grias/form_buscar_madre_grias.do")
    public String buscarAvanzadaMadre(@ModelAttribute("formBuscarPersona") FormBuscarPersona formBuscarPersona,Model model) {
        return "grias/form_buscar_madre_grias";
    }

    @RequestMapping(value = "/grias/buscar_madre_grias.do", method = RequestMethod.GET)
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
        boolean dniinvalido=false;
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
        if(dni.length()==8){
            minimoCampos=true;
        }


        //minimoCampos = true;
        model.addAttribute("urlVolverFicha", request.getRequestURI());
        if (minimoCampos) {
            int paginaActual = obtenerPaginaActual(pagina);
            int filaInicio = ConfiguracionAplicacionDao.CantidadFilasTabla * (paginaActual - 1) + 1;
            int filaFin = ConfiguracionAplicacionDao.CantidadFilasTabla * paginaActual;

            if (dni != null && !dni.isEmpty()) { //busqueda por dni
                Persona persona = aniService.obtenerPorDniEnPersona(dni, Persona.TipoPersona.MADRE);
                Persona persona2 = null;
                List<Persona> personaList = busquedaDeMenorService.listarMenoresPorDniMadre(dni);
                model.addAttribute("persona", persona);
                model.addAttribute("hijosRegistrados", personaList);
                model.addAttribute("dni", dni);

                if(persona == null) {
                    persona2 = aniService.obtenerPorDniEnPersona(dni, Persona.TipoPersona.MENOR);
                    if (persona2 != null) {
                        model.addAttribute("mensajeMenor", "El DNI " + dni + " corresponde a un menor de edad");

                    } else {
                        model.addAttribute("mensaje", "El DNI " + dni + " no es válido o no pertenece a ninguna persona");
                    }
                }
//                return "grias/form_buscar_madre_grias";
//                logger.info("DNI:" + formBuscarPersona.getDni());
                return "grias/buscarMadrePorDni";
            } else if (apPrimer != null && !apPrimer.isEmpty() || //busqueda por nombres datos
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
//                return "grias/form_buscar_madre_grias";
                return "grias/buscarMadrePorDatos";
            } else {
//                return "grias/form_buscar_madre_grias";
                return "grias/avanzadaListMadreVacio";
            }
        } else {
            //if(!minimoCampos)
            model.addAttribute("msg", "Debe ingresar <strong>al menos dos campos</strong>, para la busqueda del menor.");
            if (dni.length() > 0 && dni.length() < 8)
                model.addAttribute("msg", "DNI no validos,<strong> ingrese número de 8 dígitos<strong>.");
//            return "grias/form_buscar_madre_grias";
            return "grias/avanzadaListMadreMinimoCampos";
        }
    }

    @RequestMapping(value = "grias/informacionadicionalmadre.do")
    public String informacionAdicionalMadre(@RequestParam(value = "nuDni", required = false) String nuDni, Model model) {
        if (nuDni != null && nuDni.length() == 8) {
            model.addAttribute("persona", aniService.obtenerPorDniEnPersona(nuDni, Persona.TipoPersona.MAYOR));
            model.addAttribute("hijosRegistrados", busquedaDeMenorService.listarMenoresPorDniMadre(nuDni));
            model.addAttribute("urlVolver", "grias/buscar_madre_grias.do");
            model.addAttribute("urlVolverFicha", "grias/buscar_madre_grias.do");
            //model.addAttribute("nuDni",nuDni);
        } else {
            model.addAttribute("personaAni", null);
        }
        return "grias/informacionAdicionalMadre";
    }

    @RequestMapping(value="grias/home_grias.do")
    public String homeGrias(Model model) {
        return "grias/home_grias";
    }

}