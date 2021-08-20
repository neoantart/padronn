package pe.gob.reniec.padronn.logic.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.service.*;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import pe.gob.reniec.padronn.logic.web.validator.checks.MinimalPrecotejoRegistroPriorInsertChecks;
import pe.gob.reniec.padronn.logic.web.validator.checks.PrecotejoRegistroEditObsChecks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("precotejo")
public class PrecotejoObsController
		extends AbstractBaseController {

	@Autowired
	DominioService dominioService;

	@Autowired
	PrecotejoRegistroService precotejoRegistroService;

	@Autowired
	Usuario usuario;

	@Autowired
	UbigeoService ubigeoService;

	@Autowired
	EstablecimientoSaludService establecimientoSaludService;

	@Autowired
	CentroEducativoService centroEducativoService;

	@Autowired
	ProgramaSocialService programaSocialService;

	@Autowired
	NivelEducativoService nivelEducativoService;

	@Autowired
	PadronProperties padronProperties;


	private void registrarDominioItems(Model model) {
		model.addAttribute("nivelPobrezaItems", dominioService.nivelPobrezaItems());
		model.addAttribute("tipoSeguroItems", dominioService.tipoSeguroItems());
		model.addAttribute("tipoProgramaSocialItems", dominioService.tipoProgramaSocialItems());
		model.addAttribute("tipoGestionItems", dominioService.tipoGestionItems());
		model.addAttribute("vinculoFamiliarItems", dominioService.vinculoFamiliarItems());
		model.addAttribute("gradoInstruccionItems", dominioService.gradoInstruccionItems());
		model.addAttribute("lenguajeHabitualItems", dominioService.lenguajeHabitualItems());
		model.addAttribute("etniaItems", dominioService.etniaItems());
	}

	@RequestMapping("/precotejoregobs.do")
	public String doPrecotejoRegObs(
			@RequestParam String nuEnvio,
			@RequestParam String nuRegistro,
			@RequestParam(required = false) Integer nuPagina,
			Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

		PrecotejoRegistro precotejoRegistro;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];

        precotejoRegistro = precotejoRegistroService.getPrecotejoRegistroObs(coEntidad, nuEnvio, nuRegistro);
		precotejoRegistro.updateTiProSocialList(); //convertimos el string en lista para mostrarlo adecuadamente
		precotejoRegistro.setSuccessAction("precotejo/precotejoregobsedit.do#observacion-modulo-div");
		precotejoRegistro.setCancelAction(String.format("precotejo/allregs.do?nuEnvio=%s&nuPagina=%s#precarga-modulo-div", nuEnvio, nuPagina));
		precotejoRegistro.setPadronProperties(padronProperties);
		nuPagina = (nuPagina==null||nuPagina<=0) ? 1 : nuPagina;

		model.addAttribute("precotejoRegistro", precotejoRegistro);
//        log.info("nuEnvio:"+ nuEnvio);

		model.addAttribute("nuEnvio", nuEnvio);
		model.addAttribute("nuRegistro", nuRegistro);
		model.addAttribute("nuPaginaAnterior", nuPagina);

		//BindingResult bindingResult = null;
		//model.addAttribute(bindingResult);

		registrarDominioItems(model);
		return "precotejo/observacion-precotejo-registro";
	}



	@RequestMapping("/precotejoregobsedit.do")
	public String doPrecotejoRegObsEdit(
			@Validated({MinimalPrecotejoRegistroPriorInsertChecks.class}) @ModelAttribute PrecotejoRegistro precotejoRegistro, BindingResult bindingResult,
			Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

		// @validated en vez de @valid => http://stackoverflow.com/questions/6944975/spring-mvc-with-hibernate-validator-how-to-validate-property-by-group
//		log.debug(bindingResult);
		if (bindingResult.hasErrors()) {
			registrarDominioItems(model);
			return "precotejo/observacion-editform";
		}

		try {
			model.addAttribute("divToHide", "#observacion-detalle-modulo-div");
//            precotejoRegistro.setNuRegistro(Integer.parseInt(precotejoRegistro.getNuRegistro()) + 1);

//			if(precotejoRegistroService.updatePrecotejoRegistro(precotejoRegistro)) {
			if(precotejoRegistroService.insertPrecotejoRegistro(precotejoRegistro)) {
				completePrecotejoRegistroForView(precotejoRegistro);
				model.addAttribute("precotejoRegistro", precotejoRegistro);
				return "precotejo/observacion-editform-success";

			} else {
				model.addAttribute("precotejoRegistro", null);
				return "precotejo/observacion-editform-success";
			}

		} catch (DataAccessException dae) {
			httpServletResponse.setStatus(500);
			return null;
		}
	}



	@RequestMapping("/cotejoregobseditform.do")
	public String doCotejoRegObsEditForm(
			@RequestParam String nuEnvio,
			@RequestParam String nuRegistro,
			Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

		PrecotejoRegistro precotejoRegistro;
        String[] split = usuario.getCoEntidad().split("_");
        String coEntidad = split[0];
        precotejoRegistro = precotejoRegistroService.getPrecotejoRegistro(coEntidad, nuEnvio, nuRegistro);
		precotejoRegistro.updateTiProSocialList(); //convertimos el string en lista para mostrarlo adecuadamente
		precotejoRegistro.setSuccessAction("precotejo/cotejoregobsedit.do#observacion-modulo-div");
		precotejoRegistro.setCancelAction(String.format("precotejo/allregs.do?nuEnvio=%s&nuPagina=%s#precarga-modulo-div", nuEnvio, 1));
		model.addAttribute("precotejoRegistro", precotejoRegistro);

		registrarDominioItems(model);
		return "precotejo/observacion-editform";
	}

	@RequestMapping("/cotejoregobsedit.do")
	public String doCotejoRegObsEdit(
			//@Valid @ModelAttribute PrecotejoRegistro precotejoRegistro, BindingResult bindingResult,
			@Validated({MinimalPrecotejoRegistroPriorInsertChecks.class}) @ModelAttribute PrecotejoRegistro precotejoRegistro, BindingResult bindingResult,
			Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

		// @validated en vez de @valid => http://stackoverflow.com/questions/6944975/spring-mvc-with-hibernate-validator-how-to-validate-property-by-group
//		log.debug(bindingResult);
		if (bindingResult.hasErrors()) {
			registrarDominioItems(model);
			return "precotejo/observacion-editform";
		}

		try {
			model.addAttribute("divToHide", "#observacion-detalle-modulo-div");
			if(precotejoRegistroService.updatePrecotejoRegistro(precotejoRegistro)) {
				completePrecotejoRegistroForView(precotejoRegistro);
				model.addAttribute("precotejoRegistro", precotejoRegistro);
				return "precotejo/observacion-editform-success";

			} else {
				model.addAttribute("precotejoRegistro", null);
				return "precotejo/observacion-editform-success";
			}

		} catch (DataAccessException dae) {
			dae.printStackTrace();
			httpServletResponse.setStatus(500);
			return null;
		}
	}

	// desde RegistroManualController.LlenarDescripcionesPadronNominal
	private void completePrecotejoRegistroForView(PrecotejoRegistro precotejoRegistro) {
		precotejoRegistro.setDeVinculoJefe(dominioService.obtener(precotejoRegistro.getTiVinculoJefe(), "TI_VINCULO_MENOR").getDeDom());
		precotejoRegistro.setDeGeneroMenor(dominioService.obtener(precotejoRegistro.getCoGeneroMenor(), "CO_GENERO").getDeDom());
		Ubigeo ubigeo = ubigeoService.obtenerPorCodigoInei(precotejoRegistro.getCoUbigeoInei());
		precotejoRegistro.setDeUbigeoInei(ubigeo.getDeDepartamento() + ", " + ubigeo.getDeProvincia() + ", " + ubigeo.getDeDistrito());

        if (precotejoRegistro.getCoCentroPoblado()!=null) {
            CentroPoblado centroPoblado = ubigeoService.getCentroPoblado(precotejoRegistro.getCoCentroPoblado());
            if (centroPoblado != null) {
                precotejoRegistro.setDeCentroPoblado(centroPoblado.getNoCentroPoblado() + ", " +centroPoblado.getNoCategoria());
            }
        }

		EstablecimientoSalud establecimientoSalud = establecimientoSaludService.obtenerPorCodigoRenaes(precotejoRegistro.getCoEstSalud());
		if(establecimientoSalud != null) {
			precotejoRegistro.setDeEstSalud(establecimientoSalud.getDeEstSalud() + " (Código Renaes: " + establecimientoSalud.getCoEstSalud() +") "+
                    " <br/><strong>Ubigeo:</strong> "+ establecimientoSalud.getDeDepartamento() + ", " + establecimientoSalud.getDeProvincia() + ", " + establecimientoSalud.getDeDistrito() );
		}
		precotejoRegistro.setDeSeguroMenor(dominioService.obtener(precotejoRegistro.getTiSeguroMenor(), "TI_SEGURO_MENOR").getDeDom());
		CentroEducativo centroEducativo = centroEducativoService.obtenerPorCodigo(precotejoRegistro.getCoInstEducativa());
		if (centroEducativo != null) {
			precotejoRegistro.setDeInstEducativa(centroEducativo.getNoCentroEducativo() + " (Código Modular: " + centroEducativo.getCoCentroEducativo() + ")");
		}

		List<String> codigoList = precotejoRegistro.getTiProSocialList();
		List<String> programaSocial = new ArrayList<String>();
		if (codigoList != null) {
			for (String codigo : codigoList) {
				programaSocial.add(programaSocialService.obtenerPorCodigoEnDominio(codigo).getDeDom());
			}
		}
		precotejoRegistro.setTiProSocialList(programaSocial);

		//precotejoRegistro.setDeVinculoMadre(dominioService.obtener(precotejoRegistro.getTiVinculoMadre(), "TI_VINCULO_MENOR").getDeDom());
		//precotejoRegistro.setDeVinculoJefe(dominioService.obtener(precotejoRegistro.getTiVinculoJefe(), "TI_VINCULO_MENOR").getDeDom());
		precotejoRegistro.setDeLenMadre(dominioService.obtener(precotejoRegistro.getCoLenMadre(), "CO_LEN_MADRE").getDeDom());
		precotejoRegistro.setDeGraInstMadre(nivelEducativoService.obtenerPorCodigo(precotejoRegistro.getCoGraInstMadre()).getDeNivelEduca());
	}
}
