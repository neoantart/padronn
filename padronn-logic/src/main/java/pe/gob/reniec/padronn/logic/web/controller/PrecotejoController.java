package pe.gob.reniec.padronn.logic.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.PrecotejoUploadForm;
import pe.gob.reniec.padronn.logic.service.*;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

/**
 * Clase PrecotejoController.
 *
 * @author lmamani
 * @version 1.0.0
 * @since 16/05/13 03:01 PM
 */
@Controller
@RequestMapping("precotejo")
public class PrecotejoController
		extends AbstractBaseController {

	@Autowired
	PadronService padronService;

	@Autowired
	PrecotejoService precotejoService;

	@Autowired
	Usuario usuario;

	@Autowired
	UbigeoService ubigeoService;

	@Autowired
	CentroEducativoService centroEducativoService;

	@Autowired
	EstablecimientoSaludService establecimientoSaludService;

	@Autowired
	DominioService dominioService;

	@Autowired
	PadronProperties padronProperties;

	@RequestMapping(value = "precotejoform.do", method = RequestMethod.GET)
	public String doPrecotejoForm(@RequestParam(required = false) Integer nuPagina, Model model) {
		nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];

        int filas = precotejoService.countAll(coEntidad, null, null, null, null);

		PrecotejoUploadForm uploadForm = new PrecotejoUploadForm();
		model.addAttribute("uploadForm", uploadForm);
		model.addAttribute("uploadModulo", true);
		model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
		model.addAttribute("nuPagina", nuPagina);
		model.addAttribute("precotejoList",
				precotejoService.getAll(
						coEntidad, null, null, null, null, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS
				)
		);
		return "precotejo/precotejoform";
	}

	@RequestMapping(value = "consultaform.do", method = RequestMethod.GET)
	public String doPrecotejoConsulta(@RequestParam(required = false) Integer nuPagina, Model model) {
		nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
		int filas = precotejoService.countAll(coEntidad, null, null, null, null);

		model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
		model.addAttribute("nuPagina", nuPagina);
		model.addAttribute("precotejoList",
				precotejoService.getAll(
						coEntidad, null, null, null, null, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS
				)
		);
		return "precotejo/consultaform";
	}

	@RequestMapping(value = "consulta.do", method = RequestMethod.GET)
	public String doConsulta(@RequestParam String nuEnvio, @RequestParam String feInicio, @RequestParam String feFin, @RequestParam(required = false) Integer nuPagina, Model model) {
		nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
		int filas = precotejoService.countAll(coEntidad, null, nuEnvio, feInicio, feFin);

		model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
		model.addAttribute("nuPagina", nuPagina);
		model.addAttribute("nuEnvio", nuEnvio);
		model.addAttribute("feInicio", feInicio);
		model.addAttribute("feFin", feFin);
		model.addAttribute("precotejoList",
				precotejoService.getAll(
						coEntidad, null, nuEnvio, feInicio, feFin, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS
				)
		);
		return "precotejo/consultaform-registros";
	}

	@RequestMapping(value = "precotejoregs.do", method = RequestMethod.GET)
	public String doPrecotejoDetails(@RequestParam String nuEnvio, Model model) {
		model.addAttribute("nuEnvio", nuEnvio);
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
        model.addAttribute("precotejoRegistrosList", precotejoService.getAllFromPrecotejo(coEntidad, nuEnvio));
		return "precotejo/precotejoform-upload-details";
	}

	@RequestMapping(value = "allregs.do", method = RequestMethod.GET)
	public String doAllRegs(
			@RequestParam String nuEnvio,
			@RequestParam(required = false) Integer nuPaginaAnterior,
			@RequestParam(required = false) Integer nuPagina,
			@RequestParam(required = false) Integer tipoRegistro,
			Model model) {

		tipoRegistro = (tipoRegistro == null) ? 0 : tipoRegistro;
		PrecotejoFiltroTipoRegistro filtroTipoRegistro;
		switch (tipoRegistro) {
			case 3:
				filtroTipoRegistro = PrecotejoFiltroTipoRegistro.SOLO_ERRORES;
				break;
			case 2:
				filtroTipoRegistro = PrecotejoFiltroTipoRegistro.SOLO_OBSERVACIONES;
				break;
			case 1:
				filtroTipoRegistro = PrecotejoFiltroTipoRegistro.SOLO_OK;
				break;
			case 0:
			default:
				filtroTipoRegistro = PrecotejoFiltroTipoRegistro.TODO;
		}

		nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
		int filas = precotejoService.countAllFromPrecotejoCotejo(coEntidad, nuEnvio, filtroTipoRegistro);

		model.addAttribute("nuEnvio", nuEnvio);
		model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
		model.addAttribute("nuPagina", nuPagina);
		model.addAttribute("nuPaginaAnterior", nuPaginaAnterior);
		model.addAttribute("tipoRegistro", tipoRegistro);
		model.addAttribute("precotejoCotejoRegistrosList",
				precotejoService.getAllFromPrecotejoCotejo(
						coEntidad, nuEnvio, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS, filtroTipoRegistro
				)
		);
		return "precotejo/precotejoform-upload-allregs";
	}

	@RequestMapping(value = "cotejoregs.do", method = RequestMethod.GET)
	public String doCotejoDetails(@RequestParam String nuEnvio, Model model) {
		model.addAttribute("nuEnvio", nuEnvio);
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
		model.addAttribute("cotejoRegistrosList", precotejoService.getAllFromCotejo(coEntidad, nuEnvio));
		return "precotejo/cotejo-details";
	}

	@RequestMapping(value = "observations.do", method = RequestMethod.GET)
	public String doObservations(@RequestParam String nuEnvio, Model model) {
		model.addAttribute("nuEnvio", nuEnvio);
		// TODO cambiar nombre de método para asemejar tablas
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
		model.addAttribute("precotejoRegistrosList", precotejoService.getAllFromPrecotejoObs(coEntidad, nuEnvio));
		return "precotejo/precotejoform-upload-observations";
	}

	@RequestMapping(value = "registros.do", method = RequestMethod.GET)
	public String doRegistros(@RequestParam(required = false) Integer nuPagina, Model model) {
		nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];

        int filas = precotejoService.countAll(coEntidad, null, null, null, null);

		model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
		model.addAttribute("nuPagina", nuPagina);
		model.addAttribute("precotejoList",
				precotejoService.getAll(
						coEntidad, null, null, null, null, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS
				)
		);
		return "precotejo/precotejoform-upload-registros";
	}

	@RequestMapping(value = "precotejoformato.do", method = RequestMethod.GET)
	public String doPrecotejoFormato(@RequestParam(required = false) Integer db, Model model) {
		// si 1 se generará de la base de datos
		db = (db == null || db != 1) ? 0 : 1;

		String filename = padronProperties.FORM_NOMBRE + "-" + usuario.getDeEntidad();
		filename = filename.replaceAll("[^a-zA-Z0-9\\.-]", "_") + ".xls";
		model.addAttribute("nombreArchivo", filename);

		model.addAttribute("db", db);
		model.addAttribute("ubigeoList", ubigeoService.listarPorDepartamento(usuario.getCoDepartamento()));
        model.addAttribute("centroPobladoList", ubigeoService.listaCentrosPobladosPorDepartamento(usuario.getCoDepartamento()));
		model.addAttribute("centroEducativoList", centroEducativoService.listarPorDepartamento(usuario.getCoDepartamento()));
		model.addAttribute("establecimientoSaludList", establecimientoSaludService.listarPorDepartamento(usuario.getCoDepartamento()));
		model.addAttribute("gradoInstruccionList", dominioService.gradoInstruccionItems());
		model.addAttribute("tipoSeguroMenorList", dominioService.tipoSeguroItems());
		model.addAttribute("tipoDocumentoMenorList", dominioService.tipoDocumentoItems());
		model.addAttribute("lenguajeMadreList", dominioService.lenguajeHabitualItems());
		model.addAttribute("generoList", dominioService.generoItems());
		model.addAttribute("tipoVinculoList", dominioService.vinculoFamiliarItems());
		model.addAttribute("programaSocialList", dominioService.tipoProgramaSocialItems());
		return "precotejoformato.xls";
	}

	@RequestMapping(value = "detallespadron.do")
	public String detalles(@RequestParam(value = "codigo", required = false) String codigo, Model model) {
		PadronNominal padronNominal = padronService.obtenerPorCodigoPadronConDetalles(codigo);
		model.addAttribute("padronNominal", padronNominal);
		return "precotejo/detalles-padron";
	}

	@RequestMapping(value = "cotejoregobs.do")
	public String doObservacionPrecotejoRegistro(@RequestParam String nuEnvio, @RequestParam Integer nuPagina, @RequestParam String nuRegistro, Model model) {
		nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
		PrecotejoRegistroDetalle precotejoRegistroDetalle = precotejoService.getPrecotejoRegistroDetalle(coEntidad, nuEnvio, nuRegistro);
		model.addAttribute("precotejoRegistroDetalle", precotejoRegistroDetalle);
		model.addAttribute("nuEnvio", nuEnvio);
		model.addAttribute("nuPagina", nuPagina);
		model.addAttribute("nuRegistro", nuRegistro);
		return "precotejo/observacion-cotejo-registro";
	}


    @RequestMapping(value = "setestado.do")
    @ResponseBody
    public String setEstadoPrecotejo(
            @RequestParam(value = "nuEnvio", required = true) String nuEnvio,
            @RequestParam(value = "coEntidad", required = true) String coEntidad
    ) {
        precotejoService.setEstadoPrecotejo(nuEnvio, coEntidad);
        return "OK!";
    }

}
