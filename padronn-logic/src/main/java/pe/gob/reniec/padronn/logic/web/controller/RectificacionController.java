package pe.gob.reniec.padronn.logic.web.controller;

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
import pe.gob.reniec.padronn.logic.model.form.BuscarMenorDocumento;
import pe.gob.reniec.padronn.logic.service.DominioService;
import pe.gob.reniec.padronn.logic.service.RectificacionService;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.validation.Valid;
import java.util.List;

/**
 * User: jronal at gmail dot com
 * Date: 17/10/13
 * Time: 05:51 PM
 */
@Controller
public class RectificacionController extends AbstractBaseController {

    @Autowired
    DominioService dominioService;

    @Autowired
    RectificacionDao rectificacionDao;

    @Autowired
    PadronProperties padronProperties;

    @Autowired
    RectificacionService rectificacionService;

    @RequestMapping(value = "/rectificacion/index.do")
    public String index() {
        return "rectificacion/index";
    }

    @RequestMapping(value = "/rectificacion/con_dni.do")
    public String conDni(@ModelAttribute(value = "buscarMenorDocumento")BuscarMenorDocumento buscarMenorDocumento) {
        return "rectificacion/con-dni";
    }

    /**
     * Muestra datos del menor, jefe de familia y madre
     */
    @RequestMapping(value = "/rectificacion/datos_personales_padron.do")
    public String getDatosMenor(
            @ModelAttribute(value = "buscarMenorDocumento") BuscarMenorDocumento buscarMenorDocumento, Model model
    ) {
        if (buscarMenorDocumento.getTiDoc() == null ) {
            buscarMenorDocumento.setTiDoc(BuscarMenorDocumento.TipoDoc.CO_PADRON.getTiDoc());
            buscarMenorDocumento.setNuDoc(buscarMenorDocumento.getCoPadronNominal());
        }

        PadronNominal padronNominal = rectificacionService.getDatosMenor(buscarMenorDocumento);
        if (padronNominal == null)
            return "rectificacion/datos-personales-padron";

            Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(padronNominal.getCoPadronNominal());
            Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(padronNominal.getCoPadronNominal());
            Padre padrePadron = rectificacionDao.getDatosPersonalesPadre(padronNominal.getCoPadronNominal());

            String nuDniMenor = "";
            if (menorPadron != null && menorPadron.getNuDniMenor() != null) {
                nuDniMenor = menorPadron.getNuDniMenor() != null ? padronNominal.getNuDniMenor() : "";
            }

            String coDniMadre = padronNominal.getCoDniMadre() != null ? padronNominal.getCoDniMadre() : "";
            String coDniJefeFam = padronNominal.getCoDniJefeFam() != null ? padronNominal.getCoDniJefeFam() : "";

            Persona menorAni = rectificacionDao.getPersonaAni(nuDniMenor, Persona.TipoPersona.MENOR);
            Persona madreAni = rectificacionDao.getPersonaAni(coDniMadre, Persona.TipoPersona.MADRE);
            Persona padreAni = rectificacionDao.getPersonaAni(coDniJefeFam, Persona.TipoPersona.PADRE);

            List<Rectificacion> rectificacionesMenor = rectificacionDao.getRectificaciones(menorPadron.getCoPadronNominal(), Rectificacion.TipoPersona.MENOR.getTiPersona());
            List<Rectificacion> rectificacionesMadre = rectificacionDao.getRectificaciones(madrePadron.getCoPadronNominal(), Rectificacion.TipoPersona.MADRE.getTiPersona());
            List<Rectificacion> rectificacionesPadre = rectificacionDao.getRectificaciones(padrePadron.getCoPadronNominal(), Rectificacion.TipoPersona.PADRE.getTiPersona());

            model.addAttribute("menorPadron", menorPadron);
            model.addAttribute("madrePadron", madrePadron);
            model.addAttribute("padrePadron", padrePadron);
            model.addAttribute("menorAni", menorAni);
            model.addAttribute("madreAni", madreAni);
            model.addAttribute("padreAni", padreAni);
            model.addAttribute("rectificacionesMenor", rectificacionesMenor);
            model.addAttribute("rectificacionesMadre", rectificacionesMadre);
            model.addAttribute("rectificacionesPadre", rectificacionesPadre);
            return "rectificacion/datos-personales-padron";

    }

    @RequestMapping(value = "/rectificacion/rectificar_datos_menor.do", method = RequestMethod.GET)
    public String rectificarDatosMenor(@ModelAttribute("menor") Menor menor,
                                       @RequestParam(value = "coPadronNominal", required = true) String coPadronNominal,
                                       Model model) {
        Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(coPadronNominal);
        Persona menorAni = new Persona();
        if (menorPadron.getNuDniMenor() != null) {
            menorAni = rectificacionDao.getPersonaAni(menorPadron.getNuDniMenor(), Persona.TipoPersona.MENOR);
        }
        menor.setApPrimerMenor(menorPadron.getApPrimerMenor());
        menor.setApSegundoMenor(menorPadron.getApSegundoMenor());
        menor.setPrenombreMenor(menorPadron.getPrenombreMenor());
        menor.setCoGeneroMenor(menorPadron.getCoGeneroMenor());
        menor.setDeGeneroMenor(menorPadron.getDeGeneroMenor());
        menor.setFeNacMenor(menorPadron.getFeNacMenor());
        menor.setNuDniMenor(menorPadron.getNuDniMenor());
        menor.setNuCui(menorPadron.getNuCui());
        menor.setCoPadronNominal(menorPadron.getCoPadronNominal());
        model.addAttribute("coPadronNominal", menorPadron.getCoPadronNominal());
        model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
        model.addAttribute("menorAni", menorAni);
        return "rectificacion/rectificar-datos-menor";
    }

    @RequestMapping(value = "/rectificacion/set_datos_menor.do", method = RequestMethod.POST)
    public String setDatosMenorDni(@Valid @ModelAttribute("menor") Menor menor, BindingResult result, Model model) {
        Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(menor.getCoPadronNominal());
//        menor.set
        if (result.hasErrors()) {
            model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
            return "rectificacion/rectificar-datos-menor";
        }
        try {
            if (rectificacionDao.rectificarDatosMenor(menor)) {
                model.addAttribute("mensajeSuccess", "Se Rectifico correctamente!");
            } else {
                model.addAttribute("mensajeError", "Ocurrio un error rectificando");
            }
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("nuCui", "errors.menor.nuCui", "CUI esta en uso, el CUI debe ser unico!.");
        }
        if (result.hasErrors()) {
            model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
            return "rectificacion/rectificar-datos-menor";
        }

        Menor menorPadronActualizado = rectificacionDao.getDatosPersonalesMenor(menor.getCoPadronNominal());
        Persona menorAni = rectificacionDao.getPersonaAni(menorPadronActualizado.getNuDniMenor(), Persona.TipoPersona.MENOR);
        model.addAttribute("menorPadron", menorPadronActualizado);
        model.addAttribute("menorAni", menorAni);
        model.addAttribute("rectificacionesMenor", rectificacionDao.getRectificaciones(menorPadronActualizado.getCoPadronNominal(), Rectificacion.TipoPersona.MENOR.getTiPersona()));
        return "rectificacion/datos-padron-menor";
    }

    @RequestMapping(value = "/rectificacion/get_datos_menor.do", method = RequestMethod.GET)
    public String getDatosMenorPadron(@RequestParam(value = "coPadronNominal") String coPadronNominal, Model model) {
        Menor menorPadron = rectificacionDao.getDatosPersonalesMenor(coPadronNominal);
        Persona menorAni = rectificacionDao.getPersonaAni(menorPadron.getNuDniMenor(), Persona.TipoPersona.MENOR);
        model.addAttribute("menorPadron", menorPadron);
        model.addAttribute("menorAni", menorAni);
        model.addAttribute("nuDniMenor", menorPadron.getNuDniMenor());
        return "rectificacion/datos-padron-menor";
    }

    @RequestMapping(value = "/rectificacion/get_datos_menor_dni.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String getDatosMenorDni(@RequestParam(value = "dni") String dni, Model model) {
        Persona menorAni = rectificacionDao.getPersonaAni(dni, Persona.TipoPersona.MENOR);
        model.addAttribute("menorAni", menorAni);
        return "rectificacion/datos-ani-menor";
    }

    @RequestMapping(value = "/rectificacion/get_datos_madre_dni.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String getDatosMadreDni(@RequestParam(value = "dni") String dni, Model model) {
        Persona madreAni = rectificacionDao.getPersonaAni(dni, Persona.TipoPersona.MADRE);
        model.addAttribute("madreAni", madreAni);
        return "rectificacion/datos-ani-madre";
    }

    @RequestMapping(value = "/rectificacion/get_datos_padre_dni.do", method = RequestMethod.POST)
    public String getDatosPadreDni(@RequestParam(value = "dni") String dni, Model model) {
        Persona padreAni = rectificacionDao.getPersonaAni(dni, Persona.TipoPersona.PADRE);
        model.addAttribute("padreAni", padreAni);
        return "rectificacion/datos-ani-padre";
    }

    @RequestMapping(value = "/rectificacion/get_datos_madre.do", method = RequestMethod.GET)
    public String getDatosMadre(@RequestParam(value = "coPadronNominal") String coPadronNominal, Model model) {
        Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(coPadronNominal);
        Persona madreAni = null;
        if (madrePadron.getCoDniMadre() != null && !madrePadron.getCoDniMadre().isEmpty()) {
            madreAni = rectificacionDao.getPersonaAni(madrePadron.getCoDniMadre(), Persona.TipoPersona.MADRE);
        }
        model.addAttribute("madrePadron", madrePadron);
        model.addAttribute("madreAni", madreAni);
        return "rectificacion/datos-padron-madre";
    }

    @RequestMapping(value = "/rectificacion/rectificar_datos_madre.do", method = RequestMethod.GET)
    public String rectificarDatosMadre(@ModelAttribute("madre") Madre madre, @RequestParam(value = "coPadronNominal") String coPadronNominal, Model model) {
        Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(coPadronNominal);
        Persona madreAni = rectificacionDao.getPersonaAni(madrePadron.getCoDniMadre(), Persona.TipoPersona.MADRE);
        madre.setCoDniMadre(madrePadron.getCoDniMadre());
        madre.setApPrimerMadre(madrePadron.getApPrimerMadre());
        madre.setApSegundoMadre(madrePadron.getApSegundoMadre());
        madre.setPrenomMadre(madrePadron.getPrenomMadre());
        madre.setCoPadronNominal(madrePadron.getCoPadronNominal());
        model.addAttribute("coPadronNominal", madrePadron.getCoPadronNominal());
        model.addAttribute("coDniMadre", madrePadron.getCoDniMadre());
        model.addAttribute("madreAni", madreAni);
        return "rectificacion/rectificar-datos-madre";
    }

    @RequestMapping(value = "/rectificacion/set_datos_madre.do", method = RequestMethod.POST)
    public String setDatosMadre(@Valid @ModelAttribute("madre") Madre madre, BindingResult result, Model model) {
        Madre madrePadron = rectificacionDao.getDatosPersonalesMadre(madre.getCoPadronNominal());
        if (result.hasErrors()) {
            model.addAttribute("coDniMadre", madrePadron.getCoDniMadre());
            return "rectificacion/rectificar-datos-madre";
        }
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
        return "rectificacion/datos-padron-madre";
    }


    @RequestMapping(value = "/rectificacion/get_datos_padre.do", method = RequestMethod.GET)
    public String getDatosPadre(@RequestParam(value = "coPadronNominal") String coPadronNominal, Model model) {
        Padre padrePadron = rectificacionDao.getDatosPersonalesPadre(coPadronNominal);
        Persona padreAni = null;
        if (padrePadron.getCoDniJefeFam() != null && !padrePadron.getCoDniJefeFam().isEmpty()) {
            padreAni = rectificacionDao.getPersonaAni(padrePadron.getCoDniJefeFam(), Persona.TipoPersona.PADRE);
        }
        model.addAttribute("padrePadron", padrePadron);
        model.addAttribute("padreAni", padreAni);
        return "rectificacion/datos-padron-padre";
    }

    @RequestMapping(value = "/rectificacion/set_datos_padre.do", method = RequestMethod.POST)
    public String setDatosPadre(@Valid @ModelAttribute("padre") Padre padre, BindingResult result, Model model) {
        Padre padrePadron = rectificacionDao.getDatosPersonalesPadre(padre.getCoPadronNominal());
        if (result.hasErrors()) {
            model.addAttribute("coDniJefeFam", padrePadron.getCoDniJefeFam());
            model.addAttribute("vinculoFamiliarItems", dominioService.vinculoFamiliarItems());
            return "rectificacion/rectificar-datos-padre";
        }
        if (rectificacionDao.rectificarDatosPadre(padre)) {
            model.addAttribute("mensajeSuccess", "Se rectifico correctamente!");
        } else {
            model.addAttribute("mensajeError", "Ocurrio un error rectificando");
        }
        Padre padrePadronActualizado = rectificacionDao.getDatosPersonalesPadre(padre.getCoPadronNominal());
        Persona padreAni = rectificacionDao.getPersonaAni(padrePadronActualizado.getCoDniJefeFam(), Persona.TipoPersona.PADRE);
        model.addAttribute("padrePadron", padrePadronActualizado);
        model.addAttribute("padreAni", padreAni);
        model.addAttribute("rectificacionesPadre", rectificacionDao.getRectificaciones(padrePadronActualizado.getCoPadronNominal(), Rectificacion.TipoPersona.PADRE.getTiPersona()));
        return "rectificacion/datos-padron-padre";
    }

    @RequestMapping(value = "/rectificacion/rectificar_datos_padre.do", method = RequestMethod.GET)
    public String rectificarDatosPadre(@ModelAttribute("padre") Padre padre, @RequestParam(value = "coPadronNominal") String coPadronNominal, Model model) {
        Padre padrePadron = rectificacionDao.getDatosPersonalesPadre(coPadronNominal);
        Persona padreAni = rectificacionDao.getPersonaAni(padrePadron.getCoDniJefeFam(), Persona.TipoPersona.PADRE);
        padre.setCoDniJefeFam(padrePadron.getCoDniJefeFam());
        padre.setApPrimerJefe(padrePadron.getApPrimerJefe());
        padre.setApSegundoJefe(padrePadron.getApSegundoJefe());
        padre.setPrenomJefe(padrePadron.getPrenomJefe());
        padre.setCoPadronNominal(padrePadron.getCoPadronNominal());
        padre.setTiVinculoJefe(padrePadron.getTiVinculoJefe());
        model.addAttribute("vinculoFamiliarItems", dominioService.vinculoFamiliarItems());
        model.addAttribute("coPadronNominal", padrePadron.getCoPadronNominal());
        model.addAttribute("coDniJefeFam", padrePadron.getCoDniJefeFam());
        model.addAttribute("padreAni", padreAni);
        return "rectificacion/rectificar-datos-padre";
    }



    @RequestMapping(value = "/rectificacion/sin_dni.do")
    public String sinDni() {
        return "rectificacion/sin-dni";
    }

    @RequestMapping(value = "/rectificacion/buscar_menor.do", method = RequestMethod.GET)
    public String buscarMenor(
            @RequestParam(value = "apPrimerMenor", required = false) String apPrimerMenor,
            @RequestParam(value = "apSegundoMenor", required = false) String apSegundoMenor,
            @RequestParam(value = "prenombresMenor", required = false) String prenombresMenor,
            @RequestParam(value = "codigoPadron", required = false) String codigoPadron,
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            Model model) {

        List<Menor> menoresList = null;

        if (apPrimerMenor.isEmpty() && apSegundoMenor.isEmpty()) {
            //model.addAttribute("camposMinimos", true);
            return "rectificacion/result-buscar-menor";
        }
        if (apPrimerMenor.isEmpty() && prenombresMenor.isEmpty()) {
            //model.addAttribute("camposMinimos", true);
            return "rectificacion/result-buscar-menor";
        }
        if (apSegundoMenor.isEmpty() && prenombresMenor.isEmpty()) {
           // model.addAttribute("camposMinimos", true);
            return "rectificacion/result-buscar-menor";
        }

        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int filas = rectificacionDao.countBuscarMenor(apPrimerMenor, apSegundoMenor, prenombresMenor);
        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
        model.addAttribute("contador", 1 + padronProperties.PAGINA_FILAS*(nuPagina-1));
        model.addAttribute("nuPagina", nuPagina);
        menoresList = rectificacionDao.buscarMenor(apPrimerMenor, apSegundoMenor, prenombresMenor, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("apPrimerMenor", apPrimerMenor);
        model.addAttribute("apSegundoMenor", apSegundoMenor);
        model.addAttribute("prenombresMenor", prenombresMenor);
        model.addAttribute("menoresList", menoresList);
        return "rectificacion/result-buscar-menor";
    }





}