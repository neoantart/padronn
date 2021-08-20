package pe.gob.reniec.padronn.logic.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.gob.reniec.padronn.logic.model.Entidad;
import pe.gob.reniec.padronn.logic.model.Ubigeo;
import pe.gob.reniec.padronn.logic.model.Usuario;
import pe.gob.reniec.padronn.logic.service.EntidadService;
import pe.gob.reniec.padronn.logic.service.ReporteService;
import pe.gob.reniec.padronn.logic.service.UbigeoService;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 21/09/13
 * Time: 01:35 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class MaestrosController extends AbstractBaseController{

    @Autowired
    EntidadService entidadService;

    @Autowired
    ReporteService reporteService;

    @Autowired
    UbigeoService ubigeoService;

    @Autowired
    Usuario usuario;

    @Autowired
    PadronProperties padronProperties;

    @RequestMapping(value = "/maestros/form_add_entidad.do")
    public String formAddEntidad(@ModelAttribute(value="entidad") Entidad entidad, Model model) {
        // hardeado
        /*Map<String, String> coTipoEntidades = new HashMap<String, String>();
        coTipoEntidades.put("1", "Municipio");
        coTipoEntidades.put("2", "Entidad del MINSA");*/
        model.addAttribute("titulo", "Agregar Entidad");
        model.addAttribute("action", "maestros/add_entidad.do");
        return "maestros/entidad-form";
    }

    @RequestMapping(value = "/maestros/add_entidad.do", method = RequestMethod.POST)
    public String addEntidad(
            @Valid @ModelAttribute(value="entidad") Entidad entidad,
            BindingResult result,
            Model model){
        if(result.hasErrors()){
            model.addAttribute("titulo", "Agregar Entidad");
            model.addAttribute("action", "maestros/add_entidad.do");
            return "maestros/entidad-form";
        }
        entidad.setUsuCreaAudi(usuario.getLogin());
        entidad.setDeUbigeoInei(ubigeoService.obtenerDeUbigeoInei(entidad.getCoUbigeoInei()));
        if(entidadService.insert(entidad)){
            model.addAttribute("mensaje", "Se agrego correctamente la entidad");
            model.addAttribute("entidad", entidadService.getEntidad(entidad.getCoEntidad()));
            return "maestros/entidad-detalle";
        }
        else{
            model.addAttribute("titulo", "Agregar Entidad");
            model.addAttribute("action", "maestros/add_entidad.do");
            model.addAttribute("mensajeError", "Nose agrego la entidad!");
            return "maestros/entidad-form";
        }
    }

    @RequestMapping(value = "/maestros/index.do")
    public String index() {
        return "maestros/index";
    }

    @RequestMapping(value = "/maestros/form_buscar_entidad.do")
    public String buscarEntida(Model model) {
        return "maestros/form-buscar-entidad";
    }

    @RequestMapping(value = "/maestros/buscar_entidad.do", method = RequestMethod.POST)
    public String buscarEntidad(
            @RequestParam(value = "deEntidad") String deEntidad,
            Model model) {
        if(deEntidad.isEmpty()) {
            model.addAttribute("mensaje", "Debe ingresar una cadena no vacia.");
            return "maestros/result-buscar-entidad";
        }
        List<Entidad> list = entidadService.buscarEntidad(deEntidad);
        model.addAttribute("list", list);
        //model.addAttribute("mensaje", "Resultados de la busqueda.");
        return "maestros/result-buscar-entidad";
    }

    @RequestMapping(value = "/maestros/form_actualizar_entidad.do", method = RequestMethod.GET)
    public String formActualizarEntidad(
        @RequestParam(value = "coEntidad") String coEntidad,
                Model model){
        Entidad entidad = entidadService.getEntidad(coEntidad);
        if(entidad == null)
            return "redirect:/maestros/form_add_entidad.do";
        model.addAttribute("entidad", entidad);
        model.addAttribute("coEntidad", coEntidad);
        model.addAttribute("titulo", "Actualizar entidad");
        model.addAttribute("action", "maestros/actualizar_entidad.do");
        return "maestros/entidad-form";
    }

    @RequestMapping(value = "/maestros/actualizar_entidad.do", method = RequestMethod.POST)
    public String actualizarEntidad(
            @Valid @ModelAttribute(value = "entidad") Entidad entidad,
            BindingResult result,
            Model model){
        if(result.hasErrors()) {
            model.addAttribute("titulo", "Actualizar entidad");
            model.addAttribute("action", "maestros/actualizar_entidad.do");
            return "maestros/entidad-form";
        }

        if(entidadService.update(entidad)){
            //model.addAttribute("entidad")
            //entidad
            model.addAttribute("mensaje", "Se actualizo correctamente");
            model.addAttribute("entidad", entidadService.getEntidad(entidad.getCoEntidad()));
            model.addAttribute("coEntidad", entidad.getCoEntidad());
            return "maestros/entidad-detalle";
        }
        else{
            model.addAttribute("titulo", "Actualizar entidad");
            model.addAttribute("action", "maestros/actualizar_entidad.do");
            model.addAttribute("coEntidad", entidad.getCoEntidad());
            model.addAttribute("mensaje", "No se puede guardar entidad");
            return "maestros/entidad-form";
        }
    }

    @RequestMapping(value = "/maestros/listar_entidades.do")
    public String listarEntidades(
            @RequestParam(value = "nuPagina", required = false) Integer nuPagina,
            Model model) {
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;

        int filas  = entidadService.countAll();

        model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1) );
        model.addAttribute("nuPagina", nuPagina);

        List<Entidad> entidades = entidadService.getAll((nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        model.addAttribute("entidades", entidades);
        return "maestros/listar-entidades";
    }
}
