package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.reniec.padronn.logic.dao.ConfiguracionAplicacionDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.service.CentroEducativoService;
import pe.gob.reniec.padronn.logic.service.EstablecimientoSaludService;
import pe.gob.reniec.padronn.logic.service.FrecuenciaAtencionService;
import pe.gob.reniec.padronn.logic.service.UbigeoService;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("dominio")
public class DominioController {
    private static final String REG_VALID_STR_NUMERIC = "\\d*";
    private static final String REG_VALID_NOM_INST_EDU = "[\\w\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ012345678\\-, \\.]+";
    ;
    private static final String REG_VALID_NOM_RENAES = "[\\w\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ012345678\\-, \\.]+";
    ;;
    private static Logger logger = Logger.getLogger(DominioController.class);

    private static final String REG_VALID_NOM_UBIGEO = "[\\w\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-, \\.]+";

    private static final String REG_VALID_CO_UBIGEO = "[\\d_]*";

    @Autowired
    UbigeoService ubigeoService;

    @Autowired
    EstablecimientoSaludService establecimientoSaludService;

    @Autowired
    CentroEducativoService centroEducativoService;

    @Autowired
    FrecuenciaAtencionService frecuenciaAtencionService;


    @Autowired
    Usuario usuario;

    @Autowired
    PadronProperties padronProperties;

    @RequestMapping(value = "buscarEstablecimientosSalud.do", produces = {"application/json"})
    @ResponseBody
    public HashMap<String, List<EstablecimientoSalud>> buscarEstablecimientosSalud(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) {
        HashMap<String, List<EstablecimientoSalud>> map = new HashMap<String, List<EstablecimientoSalud>>();

        if (!nombre.matches(REG_VALID_NOM_RENAES)) {
            return map;
        }
        nombre = nombre.replaceAll("%", "");
        map.put("lista1", establecimientoSaludService.listarPorDatosEnDepartamento(nombre, usuario.getCoUbigeoInei(), ConfiguracionAplicacionDao.CantidadResultadosBusquedaLista));
        map.put("lista2", establecimientoSaludService.listarPorDatosFueraDeDepartamento(nombre, usuario.getCoDepartamento(), ConfiguracionAplicacionDao.CantidadResultadosBusquedaLista));
        map.put("lista3", establecimientoSaludService.listarPorRenaes(nombre));
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("X-XSS-Protection", "1");
        response.setHeader("X-Content-Type-Options", "nosniff");
        return map;
    }

    @RequestMapping(value = "buscarInstitucionesEducativas.do", produces = {"application/json"})
    @ResponseBody
    public HashMap<String, List<CentroEducativo>> buscarInstitucionesEducativas(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) {
        HashMap<String, List<CentroEducativo>> map = new HashMap<String, List<CentroEducativo>>();
        if (!nombre.matches(REG_VALID_NOM_INST_EDU)) {
            return map;
        }
        nombre = nombre.replaceAll("%", "");
        map.put("lista1", centroEducativoService.listarPorDatosEnDepartamento(nombre, usuario.getCoUbigeoInei(), ConfiguracionAplicacionDao.CantidadResultadosBusquedaLista));
        map.put("lista2", centroEducativoService.listarPorDatosFueraDeDepartamento(nombre, usuario.getCoDepartamento(), ConfiguracionAplicacionDao.CantidadResultadosBusquedaLista));
        map.put("lista3", centroEducativoService.listarPorCodigoModular(nombre));
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("X-XSS-Protection", "1");
        response.setHeader("X-Content-Type-Options", "nosniff");
        return map;
    }
/*
    @RequestMapping(value = "buscarFrecuenciasAtencion,do", produces = {"application/json"})
    @ResponseBody
    public HashMap<String, List<FrecuenciaAtencion>> buscarFrecuenciasAtencion(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) {
        HashMap<String, List<FrecuenciaAtencion>> map = new HashMap<String, List<FrecuenciaAtencion>>();
        nombre = nombre.replaceAll("%", "");
        map.put("lista1", frecuenciaAtencionService.listarFrecuenciaAtencion(nombre));




    }
*/

    @RequestMapping(value = "buscarUbigeos.do")
    @ResponseBody
    public HashMap<String, List<Ubigeo>> buscarUbigeos(@RequestParam(value = "nombre", required = false) String nombre) {
        HashMap<String, List<Ubigeo>> map = new HashMap<String, List<Ubigeo>>();
        if (!nombre.matches(REG_VALID_NOM_UBIGEO)) {
            return new HashMap<String, List<Ubigeo>>();
        }
        map.put("lista1", ubigeoService.buscarPorProvinciaDistritoEnDepartamento(nombre, usuario.getCoDepartamento(), ConfiguracionAplicacionDao.CantidadResultadosBusquedaLista));
        map.put("lista2", ubigeoService.buscarPorProvinciaDistritoFueraDeDepartamento(nombre, usuario.getCoDepartamento(), ConfiguracionAplicacionDao.CantidadResultadosBusquedaLista));
        return map;
    }

    @RequestMapping(value = "buscarCentroPoblado.do")
    @ResponseBody
    public List<CentroPoblado> buscarCentroPoblado(@RequestParam("nombre") String nombre, @RequestParam("coUbigeoInei") String coUbigeoInei) {
        if (!nombre.matches(REG_VALID_NOM_UBIGEO) || !coUbigeoInei.matches(REG_VALID_CO_UBIGEO)) {
            return new ArrayList<CentroPoblado>();
        }
        return ubigeoService.buscarCentroPoblado(coUbigeoInei, nombre);
    }

    @RequestMapping(value = "getCentroPoblado.do")
    @ResponseBody
    public CentroPoblado getCentroPoblado(@RequestParam() String coCentroPoblado) {
        if (!coCentroPoblado.matches(REG_VALID_STR_NUMERIC)) {
            return new CentroPoblado();
        }
        return ubigeoService.getCentroPoblado(coCentroPoblado);
    }

    @RequestMapping(value = "buscarEstablecimientoSalud.do")
    @ResponseBody
    public EstablecimientoSalud buscarEstablecimientoSalud(@RequestParam(value = "codigo", required = false) String codigo) {
        if (!codigo.matches(REG_VALID_CO_UBIGEO)) {
            return new EstablecimientoSalud();
        }
        return establecimientoSaludService.obtenerPorCodigoRenaes(codigo);
    }

    @RequestMapping(value = "buscarInstitucionEducativa.do")
    @ResponseBody
    public CentroEducativo buscarInstitucionEducativa(@RequestParam(value = "codigo", required = false) String codigo) {
        if (!codigo.matches(REG_VALID_STR_NUMERIC)) {
            return new CentroEducativo();
        }
        return centroEducativoService.obtenerPorCodigo(codigo);
    }

    @RequestMapping(value = "buscarUbigeo.do")
    @ResponseBody
    public Ubigeo buscarUbigeo(@RequestParam(value = "codigo", required = false) String codigo) {
        if (!codigo.matches(REG_VALID_STR_NUMERIC)) {
            return new Ubigeo();
        }
        return ubigeoService.obtenerPorCodigoInei(codigo);
    }

    @RequestMapping(value = "buscarEjeVial.do")
    @ResponseBody
    public Map<String, Object> buscarEjeVial(@RequestParam(value = "parteDeVia", required = true) String parteDeVia,
                                                  @RequestParam(value = "coCentroPoblado", required = false) String coCentroPoblado,
                                                  @RequestParam(value = "nuPagina", required = false) Integer nuPagina
                                                  ) {
        Map<String, Object> result = new HashMap<String, Object>();
        nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        int totalCount = 0;
        totalCount = ubigeoService.countBuscarEjeVial(parteDeVia, coCentroPoblado);
        List<EjeVial> items = ubigeoService.buscarEjeVial(parteDeVia, coCentroPoblado, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
        result.put("total_count", totalCount);
        result.put("items", items);
        return result;
    }

    @RequestMapping(value="obtenerEjeVial.do")
    @ResponseBody
    public EjeVial obtenerEjeVial(@RequestParam(value = "coVia", required = true) String coVia){
        return ubigeoService.obtenerEjeVial(coVia);
    }

}