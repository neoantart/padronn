package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.reniec.padronn.logic.dao.RectificacionDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.service.DominioService;
import pe.gob.reniec.padronn.logic.service.PadronObservadoService;
import pe.gob.reniec.padronn.logic.service.PadronService;
import pe.gob.reniec.padronn.logic.service.UbigeoService;
import pe.gob.reniec.padronn.logic.util.ImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jfloresh on 25/04/2016.
 */
@Controller
public class BandejaObservadoController {

    private static final String TODOS = "0";
    private static final String MADRE_INDOCUMENTADA = "1";
    private static final String SIN_DATOS_MADRE = "2";
    private static final String MENOR_SIN_NOMBRE = "3";
    private static final String DUPLICADO = "4";
    private static final String FALLECIMIENTO = "5";
    private static final String MULTIPLE_INSCRIPCION = "6";
    private static final String MENOR_DESDE_CNV = "7";

    private static final String CO_MOTIVO_BAJA_DUPLICADO = "8";
    private static final String DE_OBSERVACION_DUPLICADO = "REGISTRO DUPLICADO";
    private static final String ES_OBSERVACION_INACTIVO = "0";
    private static final String CO_MOTIVO_BAJA_MULTIPLE_INSCRIPCION = "9";
    private static final String DE_OBSERVACION_MULTIPLE_INSCRIPCION = "MULTIPLE INSCRIPCION/IDENTIDAD";
    private static final String ES_PADRON_ACTIVO = "1";

    private static final String CO_MOTIVO_BAJA_SIN_NOMBRE = "3";// otros motivos
    private static final String DE_OBSERVACION_MENOR_SIN_NOMBRE = "ALTA RECTIFICACION DE NOMBRE";

    private static final String CO_MOTIVO_BAJA_FALLECIMIENTO = "1";
    private static final String DE_OBSERVACION_FALLECIMIENTO = "FALLECIMIENTO DEL MENOR";
    private static final int DNI_LENGTH = 8;
    private static final int CNV_LENGTH = 10;
    private static final int CNV_LENGTH_CUI = 8;

    @Autowired
    Usuario usuario;

    @Autowired
    PadronObservadoService padronObservadoService;

    @Autowired
    PadronProperties padronProperties;

    @Autowired
    UbigeoService ubigeoService;

    @Autowired
    PadronService padronService;

    @Autowired
    DominioService dominioService;

    @Autowired
    RectificacionDao rectificacionDao;

    private static Logger LOG = Logger.getLogger(BandejaObservadoController.class);
    @Autowired
    private ImagenCiudadano imagenCiudadano;

    @RequestMapping(value = "/bandeja_observado/bandeja_observados.do")
    public String bandejaObservados(
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            @RequestParam(value = "coTipoObservacion", required = false) String coTipoObservacion,
            Model model) {
        Integer filas = padronObservadoService.contarPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), coTipoObservacion);
        Double nuPaginas = Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1);
        if(nuPagina!=null && nuPagina>nuPaginas) nuPagina = 1;
        nuPagina = ((nuPagina == null || nuPagina <= 0) ? 1 : nuPagina);

        coTipoObservacion = (coTipoObservacion == null) ? TODOS : coTipoObservacion;

        List<PadronObservado> padronObservados = padronObservadoService.obtenerPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), coTipoObservacion, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("nuPaginas", nuPaginas);
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("usuario", usuario);
        model.addAttribute("ubigeo", ubigeoService.obtenerPorCodigoInei(usuario.getCoUbigeoInei()));
        model.addAttribute("padronObservados", padronObservados);
        model.addAttribute("tiposObservacion", padronObservadoService.obtenerTipoObservacion());
        model.addAttribute("filas", filas);
        model.addAttribute("deTipoObservacion", padronObservadoService.obtenerDeTipoObservacion(TODOS));
        model.addAttribute("coTipoObservacion", coTipoObservacion);
        return "bandeja_observado/bandeja_observados";
    }

    @RequestMapping(value = "/bandeja_observado/listado_observados.do")
    public String listadoObservados(@RequestParam(value = "nuPagina", required = false) Integer nuPagina,
                                    @RequestParam(value = "coTipoObservacion", required = false) String coTipoObservacion,
                                    Model model) {
        LOG.info("Inicia metodo listadoObservados");
        nuPagina = ((nuPagina == null || nuPagina <= 0) ? 1 : nuPagina);
        Integer filas = padronObservadoService.contarPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), coTipoObservacion);
        List<PadronObservado> padronObservados = padronObservadoService.obtenerPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), coTipoObservacion, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);

        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("nuPagina", nuPagina);
        model.addAttribute("usuario", usuario);
        model.addAttribute("ubigeo", ubigeoService.obtenerPorCodigoInei(usuario.getCoUbigeoInei()));
        model.addAttribute("padronObservados", padronObservados);
        model.addAttribute("tiposObservacion", padronObservadoService.obtenerTipoObservacion());
        model.addAttribute("coTipoObservacion", coTipoObservacion);
        model.addAttribute("filas", filas);
        model.addAttribute("deTipoObservacion", padronObservadoService.obtenerDeTipoObservacion(coTipoObservacion));
        if (coTipoObservacion!=null && coTipoObservacion.equals(MENOR_DESDE_CNV)) {
            return "bandeja_observado/listado_observados_cnv";
        } else {
            return "bandeja_observado/listado_observados";
        }
    }

    @RequestMapping(value="/bandeja_observado/listado_observados_filtro_por_datos.do")
    public String listadoObservadosFitroPorDatos(
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            @RequestParam(value = "nuDniMenor", required = false) String nuDniMenor,
            @RequestParam(value = "apPrimer", required = false) String apPrimer,
            @RequestParam(value = "apSegundo", required = false) String apSegundo,
            @RequestParam(value = "prenombres", required = false) String prenombres,
            @RequestParam(value = "coTipoObservacion", required = false) String coTipoObservacion,
            Model model) {
        boolean minimoCampos = false;
        if (apPrimer.length() > 0 && apSegundo.length() > 0) {
            minimoCampos = true;
        }
        if (apPrimer.length() > 0 && prenombres.length() > 0) {
            minimoCampos = true;
        }

        if (apSegundo.length() > 0 && prenombres.length() > 0) {
            minimoCampos = true;
        }

        if (apPrimer.length() > 0 && apSegundo.length() > 0 && prenombres.length() > 0) {
            minimoCampos = true;
        }
        if(nuDniMenor.length()== DNI_LENGTH){
            minimoCampos=true;
        }
        if (minimoCampos) {
            nuPagina = ((nuPagina == null || nuPagina <= 0) ? 1 : nuPagina);

            Integer filas = padronObservadoService.contarPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), coTipoObservacion, nuDniMenor, apPrimer, apSegundo, prenombres);
            List<PadronObservado> padronObservados = padronObservadoService.obtenerPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), coTipoObservacion, nuDniMenor, apPrimer, apSegundo, prenombres, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);

            model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
            model.addAttribute("nuPagina", nuPagina);
            model.addAttribute("usuario", usuario);
            model.addAttribute("ubigeo", ubigeoService.obtenerPorCodigoInei(usuario.getCoUbigeoInei()));
            model.addAttribute("padronObservados", padronObservados);
            model.addAttribute("tiposObservacion", padronObservadoService.obtenerTipoObservacion());
            model.addAttribute("coTipoObservacion", coTipoObservacion);
            model.addAttribute("filas", filas);
            model.addAttribute("deTipoObservacion", padronObservadoService.obtenerDeTipoObservacion(coTipoObservacion));

            model.addAttribute("nuDniMenor", nuDniMenor);
            model.addAttribute("apPrimer", apPrimer);
            model.addAttribute("apSegundo", apSegundo);
            model.addAttribute("prenombres", prenombres);
            return "bandeja_observado/listado_observados_filtro_por_datos";
        } else {
            model.addAttribute("msg", "Debe ingresar <strong>al menos dos campos</strong>, para la busqueda del menor.");
            if (nuDniMenor.length() > 0 && nuDniMenor.length() < DNI_LENGTH)
                model.addAttribute("msg", "DNI no valido,<strong> ingrese número de 8 dígitos<strong>.");
            return "bandeja_observado/campos_minimos_filtro_por_datos";
        }
    }

    @RequestMapping(value = "/bandeja_observado/descargar_bandeja_observados.do")
    public String descargarBandejaObservados(
            @RequestParam(value = "coTipoObservacion", required = false) String coTipoObservacion,
            Model model) {
        List<PadronObservado> padronObservados;
        String deTipoObservacion;
        if (coTipoObservacion != null && !coTipoObservacion.equals(TODOS)) {
            deTipoObservacion = padronObservadoService.obtenerDeTipoObservacion(coTipoObservacion);
            padronObservados = padronObservadoService.obtenerPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), coTipoObservacion);
        } else {
            deTipoObservacion = "TODOS";
            padronObservados = padronObservadoService.obtenerPadronObservados(usuario.getCoUbigeoInei());
        }
        model.addAttribute("deTipoObservacion", deTipoObservacion);
        model.addAttribute("padronObservados", padronObservados);
        model.addAttribute("nombreArchivo", "BANDEJA_OBSERVADOS_"+deTipoObservacion.replaceAll(" ", "_")+".xlsx");
        model.addAttribute("usuario", usuario);
        model.addAttribute("ubigeo", ubigeoService.obtenerPorCodigoInei(usuario.getCoUbigeoInei()));
        model.addAttribute("titulo", "titulo de reporte");
        return "bandejaObservados.xlsx";
    }

    @RequestMapping(value = "/bandeja_observado/mostrar_detalle.do")
    public String mostrarDetalle(
            @RequestParam(value = "coPadronNominal", required = true) String coPadronNominal,
            @RequestParam(value = "coTipoObservacion", required = true) String coTipoObservacion,
            @RequestParam(value = "nuPagina", required = true) String nuPagina,
            Model model) {
        String view = "";
        model.addAttribute("nuPagina", nuPagina);
        PadronObservado padronObservado = padronObservadoService.obtenerPadronObservado(coPadronNominal, coTipoObservacion);
        if (coTipoObservacion.equals(FALLECIMIENTO)) {
            PadronNominal padronNominal = padronService.obtenerPorCodigoPadron(padronObservado.getCoPadronNominal());
            model.addAttribute("padronNominal", padronNominal);
            if (padronNominal != null && padronNominal.getNuDniMenor()!=null) {
//                model.addAttribute("img", imagenCiudadano.obtenerImagen(padronNominal.getNuDniMenor()));
                model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getNuDniMenor().getBytes())));
            }
            view = "bandeja_observado/fallecimiento";
        } else if (coTipoObservacion.equals(DUPLICADO)) {
            PadronNominal padronNominal1 = padronService.obtenerPorCodigoPadron(padronObservado.getCoPadronNominal());
            PadronNominal padronNominal2 = padronService.obtenerPorCodigoPadron(padronObservado.getCoPadronNominalDupli());
            model.addAttribute("padronNominal1", padronNominal1);
            model.addAttribute("padronNominal2", padronNominal2);
            if (padronNominal1 != null && padronNominal1.getNuDniMenor() != null) {
                //model.addAttribute("img", imagenCiudadano.obtenerImagen(padronNominal1.getNuDniMenor()));
                model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal1.getNuDniMenor().getBytes())));
            }
            view = "bandeja_observado/duplicado";
        } else if (coTipoObservacion.equals(MENOR_SIN_NOMBRE)) {
            Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(coPadronNominal);
            Persona menorAni = new Persona();
            if (menorPadron != null && menorPadron.getNuDniMenor() != null) {
                menorAni = rectificacionDao.getPersonaAni(menorPadron.getNuDniMenor(), Persona.TipoPersona.MENOR);
            }
            Menor menor = new Menor();
            menor.setApPrimerMenor(menorPadron.getApPrimerMenor());
            menor.setApSegundoMenor(menorPadron.getApSegundoMenor());
            menor.setPrenombreMenor(menorPadron.getPrenombreMenor());
            menor.setCoGeneroMenor(menorPadron.getCoGeneroMenor());
            menor.setDeGeneroMenor(menorPadron.getDeGeneroMenor());
            menor.setFeNacMenor(menorPadron.getFeNacMenor());
            menor.setNuDniMenor(menorPadron.getNuDniMenor());
            menor.setNuCui(menorPadron.getNuCui());
            menor.setCoPadronNominal(menorPadron.getCoPadronNominal());
            model.addAttribute("menor", menor);
            model.addAttribute("coPadronNominal", menorPadron.getCoPadronNominal());
            model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
            model.addAttribute("menorAni", menorAni);
            view = "bandeja_observado/menor_sin_nombre";
        } else if (coTipoObservacion.equals(SIN_DATOS_MADRE)) {
            Madre madre = new Madre();
            Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(coPadronNominal);
            Persona madreAni = rectificacionDao.getPersonaAni(madrePadron.getCoDniMadre(), Persona.TipoPersona.MADRE);
            madre.setCoDniMadre(madrePadron.getCoDniMadre());
            madre.setApPrimerMadre(madrePadron.getApPrimerMadre());
            madre.setApSegundoMadre(madrePadron.getApSegundoMadre());
            madre.setPrenomMadre(madrePadron.getPrenomMadre());
            madre.setCoPadronNominal(madrePadron.getCoPadronNominal());
            model.addAttribute("madre", madre);
            model.addAttribute("coPadronNominal", coPadronNominal);
            model.addAttribute("coDniMadre", madrePadron.getCoDniMadre());
            model.addAttribute("madreAni", madreAni);
            model.addAttribute("padronNominal", padronService.obtenerPorCodigoPadron(coPadronNominal));
            view = "bandeja_observado/sin_datos_madre";
        } else if (coTipoObservacion.equals(MADRE_INDOCUMENTADA)) {
            Madre madre = new Madre();
            Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(coPadronNominal);
            Persona madreAni = rectificacionDao.getPersonaAni(madrePadron.getCoDniMadre(), Persona.TipoPersona.MADRE);
            madre.setCoDniMadre(madrePadron.getCoDniMadre());
            madre.setApPrimerMadre(madrePadron.getApPrimerMadre());
            madre.setApSegundoMadre(madrePadron.getApSegundoMadre());
            madre.setPrenomMadre(madrePadron.getPrenomMadre());
            madre.setCoPadronNominal(madrePadron.getCoPadronNominal());
            model.addAttribute("madre", madre);
            model.addAttribute("coPadronNominal", coPadronNominal);
            model.addAttribute("coDniMadre", madrePadron.getCoDniMadre());
            model.addAttribute("madreAni", madreAni);
            model.addAttribute("padronNominal", padronService.obtenerPorCodigoPadron(coPadronNominal));
            view = "bandeja_observado/madre_sin_doc";
        } else if (coTipoObservacion.equals(MULTIPLE_INSCRIPCION)) {
            PadronNominal padronNominal = padronService.obtenerPorCodigoPadron(coPadronNominal);
            model.addAttribute("padronNominal", padronNominal);
            if (padronNominal != null && padronNominal.getNuDniMenor() != null) {
                //model.addAttribute("img", imagenCiudadano.obtenerImagen(padronNominal.getNuDniMenor()));
                model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getNuDniMenor().getBytes())));
            }
            view = "bandeja_observado/multiple_inscripcion";
        } else if (coTipoObservacion.equals(MENOR_DESDE_CNV)) {
            Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(coPadronNominal);
            Persona menorAni = new Persona();
            if (menorPadron.getNuDniMenor() != null) {
                menorAni = rectificacionDao.getPersonaAni(menorPadron.getNuDniMenor(), Persona.TipoPersona.MENOR);
            }
            Menor menor = new Menor();
            menor.setApPrimerMenor(menorPadron.getApPrimerMenor());
            menor.setApSegundoMenor(menorPadron.getApSegundoMenor());
            menor.setPrenombreMenor(menorPadron.getPrenombreMenor());
            menor.setCoGeneroMenor(menorPadron.getCoGeneroMenor());
            menor.setDeGeneroMenor(menorPadron.getDeGeneroMenor());
            menor.setFeNacMenor(menorPadron.getFeNacMenor());
            menor.setNuDniMenor(menorPadron.getNuDniMenor());
            menor.setNuCui(menorPadron.getNuCui());
            menor.setNuCnv(menorPadron.getNuCnv());
            menor.setCoPadronNominal(menorPadron.getCoPadronNominal());
            model.addAttribute("menor", menor);
            model.addAttribute("coPadronNominal", menorPadron.getCoPadronNominal());
            model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
            model.addAttribute("menorAni", menorAni);

            PadronNominal padronNominal = padronService.obtenerPorCodigoPadron(coPadronNominal);
            model.addAttribute("padronNominal", padronNominal);
            if (padronNominal != null && padronNominal.getCoDniMadre() != null) {
                model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getCoDniMadre().getBytes())));
            }

            view = "bandeja_observado/menor_cnv";
        }
        return view;
    }

    @RequestMapping(value = "/bandeja_observado/duplicado_baja.do")
    public String duplicadoBaja(
            @RequestParam(value = "coPadronNominal", required = true) String coPadronNominal,
            @RequestParam(value = "coTipoObservacion", required = true) String coTipoObservacion,
            @RequestParam(value = "nuPagina", required = true) String nuPagina,
            @ModelAttribute(value = "padronNominalBaja") PadronNominalBaja padronNominalBaja,
            Model model) {
        model.addAttribute("coPadronNominal", coPadronNominal);
        model.addAttribute("coTipoObservacion", coTipoObservacion);
        model.addAttribute("motivoBajaList", dominioService.getMotivoBajaList());
        model.addAttribute("nuPagina", nuPagina);

        padronNominalBaja.setCoMotivoBaja(CO_MOTIVO_BAJA_DUPLICADO);
        padronNominalBaja.setDeObservacion(DE_OBSERVACION_DUPLICADO);
        return "bandeja_observado/duplicado_baja";
    }

    @RequestMapping(value = "/bandeja_observado/send_duplicado_baja.do")
    public String sendDuplicadoBaja(@Valid @ModelAttribute(value = "padronNominalBaja") PadronNominalBaja padronNominalBaja,
                                    BindingResult bindingResult, Model model) {
        model.addAttribute("coPadronNominal", padronNominalBaja.getCoPadronNominal());
        model.addAttribute("coTipoObservacion", DUPLICADO);
        model.addAttribute("motivoBajaList", dominioService.getMotivoBajaList());
        model.addAttribute("nuPagina", padronNominalBaja.getNuPagina());
        padronNominalBaja.setCoMotivoBaja(CO_MOTIVO_BAJA_DUPLICADO);
        if (bindingResult.hasErrors()) {
            return "bandeja_observado/duplicado_baja";
        }
        PadronObservado padronObservado = padronObservadoService.obtenerPadronObservado(padronNominalBaja.getCoPadronNominal(), DUPLICADO);

        // Levantar la observacion
        padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, padronObservado.getCoPadronNominal(), DUPLICADO);

        // Dar de baja el registro duplicado
        padronNominalBaja.setCoPadronNominal(padronObservado.getCoPadronNominalDupli());
        padronService.insertarPadronHis(padronObservado.getCoPadronNominalDupli());
        padronService.darBajaPadron(padronNominalBaja);


        // Dar de alta el registro con DNI
        padronNominalBaja.setCoPadronNominal(padronObservado.getCoPadronNominal());
        padronService.insertarPadronHis(padronObservado.getCoPadronNominal());
        padronService.darAltaPadron(padronNominalBaja);

        // muestra infomacion registro dado de baja
        PadronNominal padronNominal2 = padronService.obtenerPorCodigoPadron(padronObservado.getCoPadronNominalDupli());
        model.addAttribute("padronNominal2", padronNominal2);

        return "bandeja_observado/send_duplicado_baja";
    }

    @RequestMapping(value = "/bandeja_observado/fallecimiento_baja.do")
    public String fallecimientoBaja(
            @RequestParam(value = "coPadronNominal", required = true) String coPadronNominal,
            @RequestParam(value = "coTipoObservacion", required = true) String coTipoObservacion,
            @RequestParam(value = "nuPagina", required = true) String nuPagina,
            @ModelAttribute(value = "padronNominalBaja") PadronNominalBaja padronNominalBaja,
            Model model) {

            model.addAttribute("coPadronNominal", coPadronNominal);
            model.addAttribute("coTipoObservacion", coTipoObservacion);
            model.addAttribute("motivoBajaList", dominioService.getMotivoBajaList());
            model.addAttribute("nuPagina", nuPagina);

            padronNominalBaja.setCoMotivoBaja(CO_MOTIVO_BAJA_FALLECIMIENTO);
            padronNominalBaja.setDeObservacion(DE_OBSERVACION_FALLECIMIENTO);
            return "bandeja_observado/fallecimiento_baja";
    }

    @RequestMapping(value = "/bandeja_observado/send_fallecimiento_baja.do")
    public String sendFallecimeintoBaja(@Valid @ModelAttribute(value = "padronNominalBaja") PadronNominalBaja padronNominalBaja,
                                    BindingResult bindingResult, Model model) {
        model.addAttribute("coPadronNominal", padronNominalBaja.getCoPadronNominal());
        model.addAttribute("coTipoObservacion", FALLECIMIENTO);
        model.addAttribute("motivoBajaList", dominioService.getMotivoBajaList());
        model.addAttribute("nuPagina", padronNominalBaja.getNuPagina());
        padronNominalBaja.setCoMotivoBaja(CO_MOTIVO_BAJA_DUPLICADO);
        if (bindingResult.hasErrors()) {
            return "bandeja_observado/fallecimiento_baja";
        }
        PadronObservado padronObservado = padronObservadoService.obtenerPadronObservado(padronNominalBaja.getCoPadronNominal(), FALLECIMIENTO);

        // Levantar la observacion
        padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, padronObservado.getCoPadronNominal(), FALLECIMIENTO);

        padronService.insertarPadronHis(padronObservado.getCoPadronNominal());
        padronNominalBaja.setCoPadronNominal(padronObservado.getCoPadronNominal());
        padronService.darBajaPadron(padronNominalBaja);

        PadronNominal padronNominal = padronService.obtenerPorCodigoPadron(padronObservado.getCoPadronNominal());
        model.addAttribute("padronNominal", padronNominal);

        return "bandeja_observado/send_fallecimiento_baja";
    }

    @RequestMapping(value = "/bandeja_observado/multiple_inscripcion_baja.do")
    public String multipleInscripcionBaja(
            @RequestParam(value = "coPadronNominal", required = true) String coPadronNominal,
            @RequestParam(value = "coTipoObservacion", required = true) String coTipoObservacion,
            @RequestParam(value = "nuPagina", required = true) String nuPagina,
            @ModelAttribute(value = "padronNominalBaja") PadronNominalBaja padronNominalBaja,
            Model model) {

        model.addAttribute("coPadronNominal", coPadronNominal);
        model.addAttribute("coTipoObservacion", coTipoObservacion);
        model.addAttribute("motivoBajaList", dominioService.getMotivoBajaList());
        model.addAttribute("nuPagina", nuPagina);

        padronNominalBaja.setCoMotivoBaja(CO_MOTIVO_BAJA_MULTIPLE_INSCRIPCION);
        padronNominalBaja.setDeObservacion(DE_OBSERVACION_MULTIPLE_INSCRIPCION);
        return "bandeja_observado/multiple_inscripcion_baja";
    }

    @RequestMapping(value = "/bandeja_observado/send_multiple_inscripcion_baja.do")
    public String sendMultipleInscripcionBaja(@Valid @ModelAttribute(value = "padronNominalBaja") PadronNominalBaja padronNominalBaja,
                                        BindingResult bindingResult, Model model) {
        model.addAttribute("coPadronNominal", padronNominalBaja.getCoPadronNominal());
        model.addAttribute("coTipoObservacion", FALLECIMIENTO);
        model.addAttribute("motivoBajaList", dominioService.getMotivoBajaList());
        model.addAttribute("nuPagina", padronNominalBaja.getNuPagina());
        padronNominalBaja.setCoMotivoBaja(CO_MOTIVO_BAJA_DUPLICADO);
        if (bindingResult.hasErrors()) {
            return "bandeja_observado/multiple_inscripcion_baja";
        }
        PadronObservado padronObservado = padronObservadoService.obtenerPadronObservado(padronNominalBaja.getCoPadronNominal(), MULTIPLE_INSCRIPCION);

        // Levantar la observacion
        padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, padronObservado.getCoPadronNominal(), MULTIPLE_INSCRIPCION);

        // guardar historico
        padronService.insertarPadronHis(padronObservado.getCoPadronNominal());

        padronNominalBaja.setCoPadronNominal(padronObservado.getCoPadronNominal());
        padronService.darBajaPadron(padronNominalBaja);

        PadronNominal padronNominal = padronService.obtenerPorCodigoPadron(padronObservado.getCoPadronNominal());
        model.addAttribute("padronNominal", padronNominal);

        return "bandeja_observado/send_multiple_inscripcion_baja";
    }

    @RequestMapping(value="/bandeja_observado/send_menor_sin_nombre.do")
    public String sendMenorSinNombre(@Valid @ModelAttribute("menor") Menor menor,BindingResult result, Model model) {
        Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(menor.getCoPadronNominal());
        if (result.hasErrors()) {
            model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
            return "bandeja_observado/form_menor_sin_nombre";
        }
        try {
            // guardar movimiento
            padronService.insertarPadronHis(menor.getCoPadronNominal());
            if (rectificacionDao.rectificarDatosMenor(menor)) {
                model.addAttribute("mensajeSuccess", "Se Rectifico correctamente!");
            } else {
                model.addAttribute("mensajeError", "Ocurrio un error rectificando");
            }
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("nuDniMenor", "errors.menor.nuDniMenor", "DNI esta en uso, verificar menor con DNI="+menor.getNuDniMenor());
            result.rejectValue("nuCui", "errors.menor.nuCui", "CUI esta en uso, el CUI debe ser unico!");
        }
        if (result.hasErrors()) {
            model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
            return "bandeja_observado/form_menor_sin_nombre";
        }

        // Levantar la observacion
        padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, menor.getCoPadronNominal(), MENOR_SIN_NOMBRE);

        PadronNominalBaja padronNominalBaja = new PadronNominalBaja();
        padronNominalBaja.setCoPadronNominal(menor.getCoPadronNominal());
        padronNominalBaja.setCoMotivoBaja(CO_MOTIVO_BAJA_SIN_NOMBRE);
        padronNominalBaja.setDeObservacion(DE_OBSERVACION_MENOR_SIN_NOMBRE);

        padronService.darAltaPadron(padronNominalBaja);

        Menor menorPadronActualizado = rectificacionDao.getDatosPersonalesMenor(menor.getCoPadronNominal());
        Persona menorAni = rectificacionDao.getPersonaAni(menorPadronActualizado.getNuDniMenor(), Persona.TipoPersona.MENOR);
        model.addAttribute("menorPadron", menorPadronActualizado);
        model.addAttribute("menorAni", menorAni);
        model.addAttribute("rectificacionesMenor", rectificacionDao.getRectificaciones(menorPadronActualizado.getCoPadronNominal(), Rectificacion.TipoPersona.MENOR.getTiPersona()));
        PadronNominal padronNominal = padronService.obtenerPorCodigoPadron(menor.getCoPadronNominal());
        model.addAttribute("padronNominal", padronNominal);
        return "bandeja_observado/send_menor_sin_nombre";
    }

    @RequestMapping(value="/bandeja_observado/send_menor_cnv.do")
    public String sendMenorCnv(@Valid @ModelAttribute("menor") Menor menor,BindingResult result, Model model) {
        Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(menor.getCoPadronNominal());
        PadronNominal padronNominal = padronService.obtenerPorCodigoPadron(menor.getCoPadronNominal());
        model.addAttribute("padronNominal", padronNominal);

        if (result.hasErrors()) {
            model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
            if (padronNominal.getCoDniMadre() != null) {
                model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getCoDniMadre().getBytes())));
            }
            return "bandeja_observado/form_menor_cnv";
        }
        try {
            padronService.insertarPadronHis(menor.getCoPadronNominal());
            if (rectificacionDao.rectificarDatosMenor(menor)) {
                model.addAttribute("mensajeSuccess", "Se Rectifico correctamente!");
            } else {
                model.addAttribute("mensajeError", "Ocurrio un error rectificando");
            }
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("nuCui", "errors.menor.nuCui", "CUI esta en uso, el CUI debe ser unico!");
        }
        if (result.hasErrors()) {
            model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
            if (padronNominal.getCoDniMadre() != null) {
                model.addAttribute("img", imagenCiudadano.obtenerImagen(padronNominal.getCoDniMadre()));
            }
            return "bandeja_observado/form_menor_cnv";
        }

        // Levantar la observacion
        padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, menor.getCoPadronNominal(), MENOR_DESDE_CNV);

        PadronNominalBaja padronNominalBaja = new PadronNominalBaja();
        padronNominalBaja.setCoPadronNominal(menor.getCoPadronNominal());
        padronNominalBaja.setCoMotivoBaja(CO_MOTIVO_BAJA_SIN_NOMBRE);
        padronNominalBaja.setDeObservacion(DE_OBSERVACION_MENOR_SIN_NOMBRE);

        padronService.darAltaPadron(padronNominalBaja);

        Menor menorPadronActualizado = rectificacionDao.getDatosPersonalesMenor(menor.getCoPadronNominal());
        Persona menorAni = rectificacionDao.getPersonaAni(menorPadronActualizado.getNuDniMenor(), Persona.TipoPersona.MENOR);
        model.addAttribute("menorPadron", menorPadronActualizado);
        model.addAttribute("menorAni", menorAni);
        model.addAttribute("rectificacionesMenor", rectificacionDao.getRectificaciones(menorPadronActualizado.getCoPadronNominal(), Rectificacion.TipoPersona.MENOR.getTiPersona()));
        model.addAttribute("padronNominal", padronService.obtenerPorCodigoPadron(menor.getCoPadronNominal()));
        return "bandeja_observado/send_menor_cnv";
    }

    @RequestMapping(value = "bandeja_observado/send_madre_sin_doc.do")
    public String sendMadreSinDoc(@Valid @ModelAttribute("madre") Madre madre, BindingResult result, Model model) {
        Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(madre.getCoPadronNominal());
        if (result.hasErrors()) {
            model.addAttribute("coDniMadre", madrePadron.getCoDniMadre());
            return "bandeja_observado/form_madre_sin_doc";
        }
        padronService.insertarPadronHis(madre.getCoPadronNominal());
        if (rectificacionDao.rectificarDatosMadre(madre)) {
            model.addAttribute("mensajeSuccess", "Se rectifico correctamente!");
        } else {
            model.addAttribute("mensajeError", "Ocurrio un error rectificando");
        }
        Madre madrePadronActualizado = rectificacionDao.getDatosPersonalesMadre(madre.getCoPadronNominal());
        Persona madreAni = rectificacionDao.getPersonaAni(madrePadronActualizado.getCoDniMadre(), Persona.TipoPersona.MADRE);
        model.addAttribute("madrePadron", madrePadronActualizado);
        model.addAttribute("madreAni", madreAni);
        model.addAttribute("rectificacionesMadre", rectificacionDao.getRectificaciones(madrePadronActualizado.getCoPadronNominal(), Rectificacion.TipoPersona.MADRE.getTiPersona()));
        model.addAttribute("padronNominal", padronService.obtenerPorCodigoPadron(madre.getCoPadronNominal()));

        // Levantar la observacion
        padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, madre.getCoPadronNominal(), MADRE_INDOCUMENTADA);
        return "bandeja_observado/send_madre_sin_doc";
    }

    @RequestMapping(value = "bandeja_observado/send_sin_datos_madre.do")
    public String sendSinDatosMadre(@Valid @ModelAttribute(value = "madre") Madre madre, BindingResult result, Model model) {
        Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(madre.getCoPadronNominal());
        if (result.hasErrors()) {
            model.addAttribute("coDniMadre", madrePadron.getCoDniMadre());
            return "bandeja_observado/form_sin_datos_madre";
        }
        padronService.insertarPadronHis(madre.getCoPadronNominal());
        if (rectificacionDao.rectificarDatosMadre(madre)) {
            model.addAttribute("mensajeSuccess", "Se rectifico correctamente!");
        } else {
            model.addAttribute("mensajeError", "Ocurrio un error rectificando");
        }
        Madre madrePadronActualizado = rectificacionDao.getDatosPersonalesMadre(madre.getCoPadronNominal());
        Persona madreAni = rectificacionDao.getPersonaAni(madrePadronActualizado.getCoDniMadre(), Persona.TipoPersona.MADRE);
        model.addAttribute("madrePadron", madrePadronActualizado);
        model.addAttribute("madreAni", madreAni);
        model.addAttribute("rectificacionesMadre", rectificacionDao.getRectificaciones(madrePadronActualizado.getCoPadronNominal(), Rectificacion.TipoPersona.MADRE.getTiPersona()));
        model.addAttribute("padronNominal", padronService.obtenerPorCodigoPadron(madre.getCoPadronNominal()));
        // Levantar la observacion
        padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, madre.getCoPadronNominal(), SIN_DATOS_MADRE);
        return "bandeja_observado/send_sin_datos_madre";
    }

    @RequestMapping(value = "/bandeja_observado/listado_observados_cnv.do", method = RequestMethod.GET)
    public String listadoObservadosCnv(
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            @RequestParam(value = "nuCnv" , required = false) String nuCnv,
            @RequestParam(value = "coDniMadre", required = false) String coDniMadre,
            @RequestParam(value = "apPrimer", required = false) String apPrimer,
            @RequestParam(value = "apSegundo", required = false) String apSegundo,
            @RequestParam(value = "prenombres", required = false) String prenombres,
            Model model) {
        boolean minimoCampos = false;
        if (apPrimer.length() > 0 && apSegundo.length() > 0) {
            minimoCampos = true;
        }
        if (apPrimer.length() > 0 && prenombres.length() > 0) {
            minimoCampos = true;
        }

        if (apSegundo.length() > 0 && prenombres.length() > 0) {
            minimoCampos = true;
        }

        if (apPrimer.length() > 0 && apSegundo.length() > 0 && prenombres.length() > 0) {
            minimoCampos = true;
        }
        if(coDniMadre.length()== DNI_LENGTH){
            minimoCampos=true;
        }
        if (nuCnv.length() == CNV_LENGTH || nuCnv.length() == CNV_LENGTH_CUI) {
            minimoCampos=true;
        }
        if (minimoCampos) {
            nuPagina = ((nuPagina == null || nuPagina <= 0) ? 1 : nuPagina);
            Integer filas = padronObservadoService.contarPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), MENOR_DESDE_CNV, nuCnv, coDniMadre,
                    apPrimer, apSegundo, prenombres);
            List<PadronObservado> padronObservados = padronObservadoService.obtenerPadronObservadosPorCoTipoObservacion(usuario.getCoUbigeoInei(), MENOR_DESDE_CNV, nuCnv,
                    coDniMadre, apPrimer, apSegundo, prenombres,
                    (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);

            model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
            model.addAttribute("nuPagina", nuPagina);
            model.addAttribute("usuario", usuario);
            model.addAttribute("ubigeo", ubigeoService.obtenerPorCodigoInei(usuario.getCoUbigeoInei()));
            model.addAttribute("padronObservados", padronObservados);
            model.addAttribute("tiposObservacion", padronObservadoService.obtenerTipoObservacion());
            model.addAttribute("coTipoObservacion", MENOR_DESDE_CNV);
            model.addAttribute("filas", filas);
            model.addAttribute("deTipoObservacion", padronObservadoService.obtenerDeTipoObservacion(MENOR_DESDE_CNV));

            model.addAttribute("nuCnv", nuCnv);
            model.addAttribute("coDniMadre", coDniMadre);
            model.addAttribute("apPrimer", apPrimer);
            model.addAttribute("apSegundo", apSegundo);
            model.addAttribute("prenombres", prenombres);
            return "bandeja_observado/listado_observados_cnv_filtro";
        } else {
            model.addAttribute("msg", "Debe ingresar <strong>al menos dos campos</strong>, para la busqueda del menor.");
            if (coDniMadre.length() > 0 && coDniMadre.length() < DNI_LENGTH)
                model.addAttribute("msg", "DNI no validos,<strong> ingrese número de 8 dígitos<strong>.");
            if(nuCnv.length() > 0 && nuCnv.length() < CNV_LENGTH && nuCnv.length() != CNV_LENGTH_CUI)
                model.addAttribute("msg", "CNV no validos,<strong> ingrese número de 8 o 10 dígitos<strong>.");
            return "bandeja_observado/campos_minimos_cnv";
        }
    }

    @RequestMapping(value = "/bandeja_observado/obtener_datos_menor_dni.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String obtenerDatosMenorDni(@RequestParam(value="dni") String nuDniMenor, Model model) {
        Persona menorAni = rectificacionDao.getPersonaAni(nuDniMenor, Persona.TipoPersona.MENOR);
        model.addAttribute("menorAni", menorAni);
        return "bandeja_observado/obtener_datos_menor_dni";
    }

}