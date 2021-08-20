package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.reniec.padronn.logic.dao.PadronSeguroDao;
import pe.gob.reniec.padronn.logic.model.*; //importing all models bean
import pe.gob.reniec.padronn.logic.model.form.BuscarMenorDocumento;
import pe.gob.reniec.padronn.logic.service.*; //class implementing aniService
import pe.gob.reniec.padronn.logic.util.ImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.SimpleBase64;

import javax.validation.*;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * espacio de nombes: regman (para elementos de vistas)
 * registrar de esta manera: controlador-carpetavistas-archivo-elemento[-id]
 * controlador = espacio de nombres = carpeta de vistas
 * elemento = elemento de interfaz grafica (tab, button, etc)
 * id : en caso de existir mas de uno, un nombre indentificatorio o numeracion
 */
@SessionAttributes({"padronNominalSesion", "padronNominalImgSesion"})
@Controller
@RequestMapping(value = "registromanual")
public class RegistroManualController {

	private static final int DNI_LENGTH = 8;
	private static final int CO_PADRON_LENGTH = 8;
	private static final int CNV_LENGTH = 10;
	private static final int CNV_LENGTH_CUI = 8;
	private static final String REG_ACTIVOS="1";
    private static final String REG_INACTIVOS="0";
	//inyeccion por propiedad
	@Autowired
	PadronService padronService;

	@Autowired
	DominioService dominioService;

	@Autowired
	AniService aniService;

	@Autowired
	RcService rcService;

	@Autowired
	BusquedaDeMenorService busquedaDeMenorService;

	@Autowired
	UbigeoService ubigeoService;

	@Autowired
	EstablecimientoSaludService establecimientoSaludService;

	@Autowired
	CentroEducativoService centroEducativoService;

	@Autowired
	ProgramaSocialService programaSocialService;

	@Autowired
	SeguroService seguroService;

	@Autowired
	NivelEducativoService nivelEducativoService;

	@Autowired
	Usuario usuario;

	@Autowired
    FuenteDatosService fuenteDatosService;

	@Autowired
	FrecuenciaAtencionService frecuenciaAtencionService;

	@Autowired
	PadronSeguroDao padronSeguroDao;

	Logger logger = Logger.getLogger(RegistroManualController.class);

	@Autowired
	private ImagenCiudadano imagenCiudadano;

	@RequestMapping(value = "buscarmenorform.do")
	public String buscarMenorForm(@ModelAttribute(value = "buscarMenorDocumento") BuscarMenorDocumento buscarMenorDocumento) {
		return "registroManual/buscarmenor-form";
	}

	@RequestMapping(value = "buscarmenor.do")
	public String buscarMenor(@ModelAttribute(value = "buscarMenorDocumento") BuscarMenorDocumento buscarMenorDocumento, Model model) {

		Persona persona;
		DatosCaducidadDni datosCaducidadDni = null;
		IntegerValidator integerValidator = IntegerValidator.getInstance();

		persona = busquedaDeMenorService.buscarMenor(buscarMenorDocumento);//numDoc,tiDoc(pasamos toda la responsabilidad al servicio)
		model.addAttribute("nuDoc", buscarMenorDocumento.getNuDoc());

		if (buscarMenorDocumento.getTiDoc()!=null && buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.DNI.getTiDoc())) {
			datosCaducidadDni = aniService.obtenerDatosCaducidadDni(buscarMenorDocumento.getNuDoc());
			boolean tieneFechaCaducidadDni = datosCaducidadDni != null && datosCaducidadDni.getFechaCaducidad() != null && !datosCaducidadDni.getFechaCaducidad().isEmpty();
			if(!tieneFechaCaducidadDni) {
				datosCaducidadDni = null;
			}
		}

		model.addAttribute("datosCaducidadDni", datosCaducidadDni);
		model.addAttribute("persona", persona);
		if(persona!=null && persona.getDni()!=null){
		   model.addAttribute("img", imagenCiudadano.obtenerImagen(SimpleBase64.encodeBase64(persona.getDni())));
		}

		return "registroManual/buscarmenor-resultados";
	}

	@RequestMapping(value = "formulario.do")
	public String formulario(
			@RequestParam(value = "coPadronNominal", required = false) String coPadronNominal,
			@RequestParam(value = "nuDniMenor", required = false) String nuDniMenor,
			@RequestParam(value = "cnv", required = false) String cnv,
			@RequestParam(value = "nuEnvio", required = false) String nuEnvio,
			@RequestParam(value = "nuPagina", required = false) String nuPagina,
			Model model) {
		model.addAttribute("nuEnvio", nuEnvio);
		model.addAttribute("nuPagina", nuPagina);
		model.addAttribute("fuenteDatosList", fuenteDatosService.obtenerFuenteDatos());
		model.addAttribute("frecuenciaAtencionList", frecuenciaAtencionService.listarFrecuenciaAtencion());
		model.addAttribute("tipoEntidad", usuario.getCoTipoEntidad());

		PadronNominal padronNominal = null;
		boolean existeDocumento = false;
		String documentoValidado = "";
		String numeroDocumento = "";

		if (nuDniMenor != null && !nuDniMenor.isEmpty() && nuDniMenor.length() == DNI_LENGTH) {
			existeDocumento = true;
			documentoValidado = "DNI";
			numeroDocumento = nuDniMenor;
			Persona persona = aniService.obtenerPorDniEnPersona (nuDniMenor, Persona.TipoPersona.MENOR);
			if (persona != null) {
				padronNominal = PadronNominal.fromPersona(persona);
				padronNominal.setDeUbigeoDomReniec(persona.getUbigeo());
				padronNominal.setDeDireccionDomReniec(persona.getDireccion());

//				setea valores de correo y numero de celular de la madre
				Persona personaMadre = aniService.obtenerPorDniEnPersona(persona.getMadreDni(), Persona.TipoPersona.MADRE);
				if (personaMadre != null){
					padronNominal.setNuCelMadre(personaMadre.getNuTelefono());
					padronNominal.setDiCorreoMadre(personaMadre.getDeEmail());
				}
				// setea el centro poblado capital
				CentroPoblado centroPobladoCapital = ubigeoService.obtenerCentroPoblado(padronNominal.getCoUbigeoInei());
				if (centroPobladoCapital != null && centroPobladoCapital.getCoCentroPoblado()!=null) {
					padronNominal.setCoCentroPoblado(centroPobladoCapital.getCoCentroPoblado());
					padronNominal.setDeAreaCcpp(centroPobladoCapital.getDeAreaCcpp());
					padronNominal.setCoAreaCcpp(centroPobladoCapital.getCoAreaCcpp());
				}
				model.addAttribute("baseDatosOrigen", Persona.BaseDatosOrigen.ANI);
				model.addAttribute("coRestri", persona.getCoRestri());
				model.addAttribute("deRestri", persona.getDeRestri());
			}
		} else if (coPadronNominal != null && !coPadronNominal.isEmpty() && coPadronNominal.length() == CO_PADRON_LENGTH) {
			existeDocumento = true;
			documentoValidado = "Código de Padrón Nominal";
			numeroDocumento = coPadronNominal;
			padronNominal = padronService.obtenerPorCodigoPadron(coPadronNominal);
//			padronNominal.setFeVisitaBefore(padronNominal.getFeVisita()); //setea feVisita para comparacion
//			padronNominal.setInMenorVisitadoBefore(padronNominal.getInMenorVisitado()); //setea inMenorVisitado para comparacion
			if (padronNominal != null) {
				if (padronNominal.getNuDniMenor() != null) {
					String nuDni = padronNominal.getNuDniMenor();
					Persona persona = aniService.obtenerPorDniEnPersona(nuDni, Persona.TipoPersona.MENOR);
					if (persona != null) {
						// para obtener la direccion original de RENIEC
						padronNominal.setDeUbigeoDomReniec(persona.getUbigeo());
						padronNominal.setDeDireccionDomReniec(persona.getDireccion());
					}
					// para obtener la direccion y ubigeo original
				} else if (padronNominal.getNuCui() != null) {
					Persona persona = aniService.obtenerPorDniEnPersona(padronNominal.getNuCui(), Persona.TipoPersona.MENOR);
					if (persona != null && persona.getUbigeo() != null && persona.getDireccion() != null) {
						padronNominal.setDeUbigeoDomReniec(persona.getUbigeo());
						padronNominal.setDeDireccionDomReniec(persona.getDireccion());
					}
					// para obtener la direccion y ubigeo original
				} else if (padronNominal.getNuCnv() != null) {
					Persona persona = rcService.obtenerPorCnv(padronNominal.getNuCnv());
					if (persona != null) {
						Ubigeo ubigeo = ubigeoService.obtenerPorCodigoUbigeoReniec(persona.getCodigoUbigeo());
						logger.info("ubigeo" + ubigeo);
						if(ubigeo!=null){
							padronNominal.setDeUbigeoDomCnv((ubigeo.getDeDepartamento() != null ? ubigeo.getDeDepartamento() : "") + ", " +
										(ubigeo.getDeProvincia() != null ? ubigeo.getDeProvincia() : "") + ", " +
										(ubigeo.getDeDistrito() != null?ubigeo.getDeDistrito():""));
						}
						padronNominal.setDeDireccionDomCnv(persona.getDireccion());
						padronNominal.setDeGraInstMadreCnv(persona.getMadreGradoInstruccionCnv());
					}
				}
				model.addAttribute("baseDatosOrigen", Persona.BaseDatosOrigen.PN);
			}
		}

		else if (cnv != null && !cnv.isEmpty() && (cnv.length() == CNV_LENGTH || cnv.length() == CNV_LENGTH_CUI)) {
			existeDocumento = true;
			documentoValidado = "CNV";
			Persona persona = rcService.obtenerPorCnv(cnv);
			Ubigeo ubigeo = ubigeoService.obtenerPorCodigoUbigeoReniec(persona.getCodigoUbigeo());
			logger.info("ubigeo" + ubigeo);
			if (ubigeo != null) {
				persona.setCodigoUbigeo(ubigeo.getCoUbigeoInei());
			} else {
				persona.setCodigoUbigeo("");
			}
			if (persona != null) {
				padronNominal = PadronNominal.fromPersona(persona);
				if (ubigeo != null) {
					padronNominal.setDeUbigeoDomCnv((ubigeo.getDeDepartamento() != null ? ubigeo.getDeDepartamento() : "") + ", " +
							(ubigeo.getDeProvincia() != null ? ubigeo.getDeProvincia() : "") + ", " +
							(ubigeo.getDeDistrito() != null?ubigeo.getDeDistrito():""));
					padronNominal.setDeDireccionDomCnv(persona.getDireccion());
				}

				if(persona.getMadrePrimerApellido()!=null && !persona.getMadrePrimerApellido().isEmpty())
					padronNominal.setApSegundoMenor(persona.getMadrePrimerApellido());
				model.addAttribute("baseDatosOrigen", Persona.BaseDatosOrigen.CNV);
			}
		}

		if (existeDocumento) {
			if (padronNominal == null) {
				model.addAttribute("mensaje", "No se encontró ningun menor con el " + documentoValidado + " número " + numeroDocumento);
				return "registroManual/buscarmenor-form";
			} else {
				if (documentoValidado.equals("CNV")) {
					model.addAttribute("existenDatos", true);
					model.addAttribute("existenDatosSoloCNV",true);
				}
				//CASO de registros con solo CNV
				else if(padronNominal.getNuCnv()!=null && padronNominal.getNuDniMenor()==null &&
						padronNominal.getApPrimerMenor()==null && padronNominal.getPrenombreMenor()==null && padronNominal.getNuCui()==null){
					 padronNominal.setDeUbigeoDomReniec(padronNominal.getDeUbigeoInei());
					 padronNominal.setDeDireccionDomReniec(padronNominal.getDeDireccion());
					 model.addAttribute("existenDatos",true);
					 model.addAttribute("existenDatosSoloCNV",true);
				}
				else {
					if(padronNominal!=null && padronNominal.getNuDniMenor()!=null){
					   model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getNuDniMenor().getBytes())));
					}
					model.addAttribute("existenDatos", true);
				}
				registrarDominioItems(model);
				model.addAttribute("padronNominal", padronNominal);
				String isPrecarga = padronService.isPrecarga(padronNominal.getCoPadronNominal())?"1":"0";
				String isSusalud = padronService.isSusalud(padronNominal.getCoPadronNominal())?"1":"0";
				String isTipoSeguro = padronService.isTipoSeguro(padronNominal.getCoPadronNominal())?"1":"0";
//				PadronNominal padronNominalBefore = setValuesBeforeSave(padronNominal);

				model.addAttribute("isPrecarga", isPrecarga);
				model.addAttribute("isSusalud", isSusalud);
				model.addAttribute("isTipoSeguro",isTipoSeguro);
				model.addAttribute("ubigeoMenor", ubigeoService.obtenerPorCodigoInei(padronNominal.getCoUbigeoInei()));
				model.addAttribute("ubigeoUsuario", usuario.getCoUbigeoInei().trim());
//				model.addAttribute("padronNominalBefore", padronNominalBefore);
				/*model.addAttribute("coPadronNominal",coPadronNominal);*/
				return "registroManual/formulario";
			}
		} else {
			registrarDominioItems(model);
			model.addAttribute("padronNominal", new PadronNominal());
			model.addAttribute("existenDatos", false);
			model.addAttribute("ubigeoUsuario", usuario.getCoUbigeoInei().trim());
//			model.addAttribute("padronNominalBefore", new PadronNominal());
			/*model.addAttribute("coPadronNominal",coPadronNominal);*/


			return "registroManual/formulario";
		}
	}

	/*-------------------Added 25-032018------------------------*/
/*	@RequestMapping("/getProgramasSociales.do")
	@ResponseBody
	public Map<String, Object> getGrupos() {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("success", true);
		res.put("data", usuarioExternoService.getGrupos());
		return res;
		//return usuarioExternoService.getGrupos();
	} */

	@InitBinder("padronNominal")
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(
				MultipartFile.class,
				"imgFotoMenor",
				new PropertyEditorSupport() {
					@Override
					public void setAsText(String text) {
						// sin esta clase Spring fails miserably cuando no se envía nada
					}
				}
		);
	}

	private Map<String, String> getErrors(Model model, BindingResult result) {
		Map<String, String> errors = new HashMap<String, String>();
		List<FieldError> fieldErrors = result.getFieldErrors();
		for(FieldError fieldError: fieldErrors){
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return errors;
	}

	private void registrarErroresPorTabs(Model model, BindingResult result) {

		List<FieldError> fieldErrorsList = result.getFieldErrors();
		ArrayList<String> fieldErrors = new ArrayList<String>();
		for (FieldError fieldError : fieldErrorsList) {
			fieldErrors.add(fieldError.getField());
		}
		String[] grupo1 = {"nuCui", "apPrimerMenor", "apSegundoMenor", "prenombreMenor", "feNacMenor", "coGeneroMenor", "coUbigeoInei", "deDireccion", "coCentroPoblado", "feVisita", "coMenorEncontrado"};
		String[] grupo2 = {"coEstSalud", "tiSeguroMenor", "tiProSocial", "coInstEducativa","coEstSaludNac","coEstSaludAds"};
		String[] grupo3 = {"tiVinculoMadre", "coDniMadre", "apPrimerMadre", "apSegundoMadre", "prenomMadre", "coGraInstMadre", "coLenMadre", "nuCelMadre"};
		for (String s : grupo1) {
			if (fieldErrors.contains(s)) {
				model.addAttribute("grupo1Errors", true);
				break;
			}
		}
		for (String s : grupo2) {
			if (fieldErrors.contains(s)) {
				model.addAttribute("grupo2Errors", true);
				break;
			}
		}
		for (String s : grupo3) {
			if (fieldErrors.contains(s)) {
				model.addAttribute("grupo3Errors", true);
				break;
			}
		}
		List<ObjectError> globalErrorList = result.getGlobalErrors();
		ArrayList<String> globlarErrors = new ArrayList<String>();
		for (ObjectError globalError : globalErrorList) {
			globlarErrors.add(globalError.getCode());
		}
		String[] grupoGlobal1 = {"UbigeoIneiJurisdiccion", "MenorVisitado"};
		String[] grupoGlobal2 = {"InformacionAdicionalVal"};
		String[] grupoGlobal3 = {"PadronMadreFamilia", "PadronApoderado", "PadronTitularFamilia"};
		for (String s : grupoGlobal1) {
			if (globlarErrors.contains(s)) {
				model.addAttribute("grupo1Errors", true);
				break;
			}
		}
		for (String s : grupoGlobal2) {
			if (globlarErrors.contains(s)) {
				model.addAttribute("grupo2Errors", true);
				break;
			}
		}
		for (String s : grupoGlobal3) {
			if (globlarErrors.contains(s)) {
				model.addAttribute("grupo3Errors", true);
				break;
			}
		}

/*		if(result.getGlobalErrors().size()>0){
			model.addAttribute("grupo3Errors", true);
		}*/
	}

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

	private PadronNominal setValuesInfAdiApod(PadronNominal padronNominal, PadronNominal padronNominalBefore){
		//Informacion adicional del niño
		padronNominal.setDeEstSaludAds(padronNominalBefore.getDeEstSaludAds());
		padronNominal.setCoEstSaludAds(padronNominalBefore.getCoEstSaludAds());
		padronNominal.setNuSecuenciaLocalAds(padronNominalBefore.getNuSecuenciaLocalAds());
		padronNominal.setDeEstSaludNac(padronNominalBefore.getDeEstSaludNac());
		padronNominal.setCoEstSaludNac(padronNominalBefore.getCoEstSaludNac());
		padronNominal.setNuSecuenciaLocalNac(padronNominalBefore.getNuSecuenciaLocalNac());
		padronNominal.setCoEstSalud(padronNominalBefore.getCoEstSalud());
		padronNominal.setDeEstSalud(padronNominalBefore.getDeEstSalud());
		padronNominal.setNuSecuenciaLocal(padronNominalBefore.getNuSecuenciaLocal());
		padronNominal.setCoInstEducativa(padronNominalBefore.getCoInstEducativa());
		padronNominal.setDeInstEducativa(padronNominalBefore.getDeInstEducativa());
		padronNominal.setTiSeguroMenor(padronNominalBefore.getTiSeguroMenor());
		padronNominal.setTiProSocial(padronNominalBefore.getTiProSocial());

		//Informacion del apoderado
		padronNominal.setTiVinculoJefe(padronNominalBefore.getTiVinculoJefe());
		padronNominal.setCoDniJefeFam(padronNominalBefore.getCoDniJefeFam());
		padronNominal.setApPrimerJefe(padronNominalBefore.getApPrimerJefe());
		padronNominal.setApSegundoJefe(padronNominalBefore.getApSegundoJefe());
		padronNominal.setPrenomJefe(padronNominalBefore.getPrenomJefe());
		padronNominal.setCoDniMadre(padronNominalBefore.getCoDniMadre());
		padronNominal.setApPrimerMadre(padronNominalBefore.getApPrimerMadre());
		padronNominal.setApSegundoMadre(padronNominalBefore.getApSegundoMadre());
		padronNominal.setPrenomMadre(padronNominalBefore.getPrenomMadre());
		padronNominal.setCoGraInstMadre(padronNominalBefore.getCoGraInstMadre());
		padronNominal.setDeGraInstMadre(padronNominalBefore.getDeGraInstMadre());
		padronNominal.setNuCelMadre(padronNominalBefore.getNuCelMadre());
		padronNominal.setDiCorreoMadre(padronNominalBefore.getDiCorreoMadre());
		padronNominal.setCoLenMadre(padronNominalBefore.getCoLenMadre());
		padronNominal.setDeLenMadre(padronNominalBefore.getDeLenMadre());
		return padronNominal;
	}
	private PadronNominal setValuesBeforeSave(PadronNominal padronNominal){
		padronNominal.setCoMenorEncontrado(padronNominal.getCoMenorEncontrado());
		padronNominal.setCoUbigeoInei(padronNominal.getCoUbigeoInei());
		if (padronNominal.getCoUbigeoInei() != null){
            Ubigeo ubigeo = ubigeoService.obtenerPorCodigoInei(padronNominal.getCoUbigeoInei());
			padronNominal.setDeUbigeoInei(ubigeo.getDeDepartamento() + ", " + ubigeo.getDeProvincia() + ", " + ubigeo.getDeDistrito());
        }
        if (padronNominal.getCoCentroPoblado() != null){
            CentroPoblado centroPoblado = ubigeoService.getCentroPoblado(padronNominal.getCoCentroPoblado());
			padronNominal.setDeCentroPoblado(centroPoblado.getNoCentroPoblado());
        }
		padronNominal.setDeDireccion(padronNominal.getDeDireccion());
		padronNominal.setDeRefDir(padronNominal.getDeRefDir());
		padronNominal.setDeAreaCcpp(padronNominal.getDeAreaCcpp());

		//Informacion adicional del niño
		if (padronNominal.getCoInstEducativa() != null && !padronNominal.getCoInstEducativa().equals("")){
			CentroEducativo centroEducativo = centroEducativoService.obtenerPorCodigo(padronNominal.getCoInstEducativa());
			padronNominal.setDeInstEducativa(centroEducativo.getNoCentroEducativo()+ " (CÓDIGO MODULAR: " + centroEducativo.getCoModular() + ", "+
					centroEducativo.getDeDepartamento() + ", "+ centroEducativo.getDeProvincia() + ", " + centroEducativo.getDeDistrito() +")");
		}
		padronNominal.setTiSeguroMenor(padronNominal.getTiSeguroMenor());
//		padronNominal.setTiProSocial(padronNominal.getTiProSocial());

		return padronNominal;
	}

	@RequestMapping(value = "guardar.do",  method = {RequestMethod.POST})
	public String guardar(@Valid @ModelAttribute("padronNominal") PadronNominal padronNominal, BindingResult result, Model model) {
		String nuDniMenor = padronNominal.getNuDniMenor();
		String coPadronNominal = padronNominal.getCoPadronNominal();
		String nuCnv = padronNominal.getNuCnv();
        model.addAttribute("fuenteDatosList", fuenteDatosService.obtenerFuenteDatos());
		model.addAttribute("frecuenciaAtencionList", frecuenciaAtencionService.listarFrecuenciaAtencion());

		// Filtro XSS
		Pattern scriptPattern = Pattern.compile("</([a-zA-Z]*)><script>(.*?)</script><([a-zA-Z]*)>", Pattern.CASE_INSENSITIVE);
		String apPrimerMenor = scriptPattern.matcher(padronNominal.getApPrimerMenor()).replaceAll("");
		padronNominal.setApPrimerMenor(apPrimerMenor);

		if (padronNominal.getApSegundoMenor() != null && !padronNominal.getApSegundoMenor().equals("")) {
			String apSegundoMenor = scriptPattern.matcher(padronNominal.getApSegundoMenor()).replaceAll("");
			padronNominal.setApSegundoMenor(apSegundoMenor);
		}

		if (padronNominal.getCoPadronNominal() != null && !padronNominal.getCoPadronNominal().equals("")) {
			String coPadronNominal_ = scriptPattern.matcher(padronNominal.getCoPadronNominal()).replaceAll("");
			padronNominal.setCoPadronNominal(coPadronNominal_);
		}

		if (padronNominal.getDeDireccion() != null && !padronNominal.getDeDireccion().equals("")) {
			String deDireccion = scriptPattern.matcher(padronNominal.getDeDireccion().replaceAll("\n ", " ")).replaceAll("");
			padronNominal.setDeDireccion(deDireccion);
		}

		if (padronNominal.getFeNacMenor() != null && !padronNominal.getFeNacMenor().equals("")) {
			String feNacMenor = scriptPattern.matcher(padronNominal.getFeNacMenor()).replaceAll("");
			padronNominal.setFeNacMenor(feNacMenor);
		}

		if (padronNominal.getNuCui() != null && !padronNominal.getNuCui().equals("")) {
			String nuCui = scriptPattern.matcher(padronNominal.getNuCui()).replaceAll("");
			padronNominal.setNuCui(nuCui);
		}

		if (padronNominal.getNuDniMenor() != null && !padronNominal.getNuDniMenor().equals("") ) {
			String nuDniMenor_ = scriptPattern.matcher(padronNominal.getNuDniMenor()).replaceAll("");
			padronNominal.setNuDniMenor(nuDniMenor_);
		}

		if (padronNominal.getPrenombreMenor() != null && !padronNominal.getPrenombreMenor().equals("")) {
			String prenombreMenor = scriptPattern.matcher(padronNominal.getPrenombreMenor()).replaceAll("");
			padronNominal.setPrenombreMenor(prenombreMenor);
		}

		// inyeccion SQL
		if(padronNominal.getApSegundoJefe()!=null && !padronNominal.getApSegundoJefe().equals(""))
			padronNominal.setApSegundoJefe(padronNominal.getApSegundoJefe().replaceAll("%", ""));

		if(padronNominal.getApSegundoMadre()!=null && !padronNominal.getApSegundoMadre().equals(""))
			padronNominal.setApSegundoMadre(padronNominal.getApSegundoMadre().replaceAll("%", ""));

		if(padronNominal.getPrenomJefe()!=null && !padronNominal.getPrenomJefe().equals(""))
			padronNominal.setPrenomJefe(padronNominal.getPrenomJefe().replaceAll("%", ""));

		if(padronNominal.getPrenomMadre()!=null  && !padronNominal.getPrenomMadre().equals(""))
			padronNominal.setPrenomMadre(padronNominal.getPrenomMadre().replaceAll("%", ""));

		if(padronNominal.getPrenombreMenor()!=null && !padronNominal.getPrenombreMenor().equals("") )
			padronNominal.setPrenombreMenor( padronNominal.getPrenombreMenor().replaceAll("%", ""));

		if(padronNominal.getNuCelMadre()!=null && !padronNominal.getNuCelMadre().equals("") )
			padronNominal.setNuCelMadre( padronNominal.getNuCelMadre().replaceAll("%", ""));

		if(padronNominal.getDiCorreoMadre()!=null && !padronNominal.getDiCorreoMadre().equals("") )
			padronNominal.setDiCorreoMadre( padronNominal.getDiCorreoMadre().replaceAll("%", ""));

		if(padronNominal.getCoInstEducativa()!=null && !padronNominal.getCoInstEducativa().equals("")){
			CentroEducativo centroEducativoDetalles = centroEducativoService.obtenerPorCodigo(padronNominal.getCoInstEducativa());
			padronNominal.setDeInstEducativa(centroEducativoDetalles.getNoCentroEducativo()+ " (CÓDIGO MODULAR: " + centroEducativoDetalles.getCoModular() + ", "+
					centroEducativoDetalles.getDeDepartamento() + ", "+ centroEducativoDetalles.getDeProvincia() + ", " + centroEducativoDetalles.getDeDistrito() +")");
		}

		if (padronNominal.getCoFuenteDatos()!= null && !padronNominal.getCoFuenteDatos().equals("")){
		    padronNominal.setDeFuenteDatos(fuenteDatosService.obtenerFuenteDatosPorCodigo(padronNominal.getCoFuenteDatos()).getDeFuenteDatos());
        }

		String isSusalud = padronService.isSusalud(padronNominal.getCoPadronNominal())?"1":"0";
		String isTipoSeguro = padronService.isTipoSeguro(padronNominal.getCoPadronNominal())?"1":"0";

		if (result.hasErrors()) {
			List<ObjectError> objectErrors = result.getGlobalErrors();
			for(ObjectError objectError:objectErrors) {
				model.addAttribute(objectError.getCode(), objectError);
				logger.info("objectError.getObjectName()"+objectError.getObjectName());
			}

			registrarDominioItems(model);
			registrarErroresPorTabs(model, result);
			if ((nuDniMenor != null && !nuDniMenor.isEmpty()) || (coPadronNominal != null && !coPadronNominal.isEmpty())) {
				if(nuCnv != null && (padronNominal.getApPrimerMenor()==null || padronNominal.getApPrimerMenor().isEmpty()) &&
				  (padronNominal.getPrenombreMenor()==null || padronNominal.getPrenombreMenor().isEmpty() ) &&
				  (padronNominal.getNuDniMenor()==null || padronNominal.getNuDniMenor().isEmpty()) &&
				  (padronNominal.getNuCui()==null || padronNominal.getNuCui().isEmpty() )) {
				   model.addAttribute("existenDatos", true);
				   model.addAttribute("existenDatosSoloCNV",true);
				}
				else{
				   model.addAttribute("existenDatos", true);
				}
				padronNominal.setDeVinculoMadre(dominioService.obtener(padronNominal.getTiVinculoMadre(), "TI_VINCULO_MENOR").getDeDom());
				padronNominal.setDeVinculoJefe(dominioService.obtener(padronNominal.getTiVinculoJefe(), "TI_VINCULO_MENOR").getDeDom());
				padronNominal.setDeGeneroMenor(dominioService.obtener(padronNominal.getCoGeneroMenor(), "CO_GENERO").getDeDom());

			} else if (nuCnv != null && !nuCnv.isEmpty()) {
				padronNominal.setDeGeneroMenor(dominioService.obtener(padronNominal.getCoGeneroMenor(), "CO_GENERO").getDeDom());
				/*model.addAttribute("existenDatos", false);
				model.addAttribute("baseDatosOrigen", Persona.BaseDatosOrigen.CNV);*/
				model.addAttribute("existenDatos", true);
				model.addAttribute("baseDatosOrigen", Persona.BaseDatosOrigen.CNV);
				model.addAttribute("existenDatosSoloCNV",true);
			} else {
				model.addAttribute("existenDatos", false);
			}
			model.addAttribute("errores", getErrors(model, result));

			if (padronNominal!=null && padronNominal.getCoEstSaludAds()!=null && !padronNominal.getCoEstSaludAds().isEmpty()) {
			    padronNominal.setDeEstSaludAds(padronService.obtenerDeEstSalud(padronNominal.getCoEstSaludAds(),padronNominal.getNuSecuenciaLocalAds()));
			}
			else{
				padronNominal.setDeEstSaludAds("");
			}


			if (coPadronNominal != null && !coPadronNominal.equals("")) {
				padronNominal = padronService.obtenerPorCodigoPadron(coPadronNominal);
//				padronNominal.setFeVisitaBefore(padronNominal.getFeVisita()); //para comparacion
//				padronNominal.setInMenorVisitadoBefore(padronNominal.getInMenorVisitado()); //para comparacion
				String precarga = padronNominal.getIsPrecarga()!="0"?"1":"0";
				model.addAttribute("isPrecarga", precarga);
			}
			if (padronNominal.getNuDniMenor()!=null && padronNominal.getNuDniMenor().length()==8) {
				padronNominal.setOrigenFoto(Persona.OrigenFoto.ANI);
				Persona persona = aniService.obtenerPorDniEnPersona(nuDniMenor, Persona.TipoPersona.MENOR);
				if (persona != null) {
					padronNominal = PadronNominal.fromPersona(persona);
					padronNominal.setDeUbigeoDomReniec(persona.getUbigeo());
					padronNominal.setDeDireccionDomReniec(persona.getDireccion());
				}
			}

			if (padronNominal!=null && padronNominal.getNuDniMenor() != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getNuDniMenor().getBytes())));
			}
			model.addAttribute("isSusalud", isSusalud);
			model.addAttribute("isTipoSeguro", isTipoSeguro);
			model.addAttribute("existeError", true);
			model.addAttribute("origenFoto", padronNominal.getOrigenFoto());
			model.addAttribute("tipoEntidad", usuario.getCoTipoEntidad());
//			model.addAttribute("padronNominalBefore", padronNominalBefore);
			model.addAttribute("coRestri", padronNominal.getCoRestri());
			model.addAttribute("deRestri", padronNominal.getDeRestri());
			return "registroManual/formulario";
		} else {
			//Mostramos la información para ser confirmada por el usuario
			model.addAttribute("padronNominalSesion", padronNominal);
			try {
				PadronImg padronImg;
				if (padronNominal.getImgFotoMenor() != null) {
					padronImg = new PadronImg(padronNominal);
				} else {
					padronImg = new PadronImg();
				}
				model.addAttribute("padronNominalImgSesion", padronImg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (isSusalud.equals("1") && isTipoSeguro.equals("1")){
				padronNominal.setTiSeguroMenor(padronSeguroDao.listarCoPadronSeguro(padronNominal.getCoPadronNominal()));
			}

			if (padronNominal.getInMenorVisitado().equals("1")) {
			    padronNominal.setCoFuenteDatos("");
			    padronNominal.setFeUltimaTomaDatos(padronNominal.getFeUltimaTomaDatosBefore());
			} else {
                padronNominal.setCoMenorEncontrado("");
                padronNominal.setFeVisita(padronNominal.getFeVisitaBefore());
            }
			if (padronNominal.getCoMenorEncontrado().equals("0")){
				if (padronNominal.getCoPadronNominal() != null && !padronNominal.getCoPadronNominal().equals("")){
					PadronNominal padronNominalBefore = padronService.obtenerPorCodigoPadronConDetalles(padronNominal.getCoPadronNominal());
					padronNominal = setValuesInfAdiApod(padronNominal, padronNominalBefore);
				} else {
					if (padronNominal.getNuDniMenor() != null && !padronNominal.getNuDniMenor().equals("")) {
						PadronNominal padronNominalBefore;
						Persona personaBefore = aniService.obtenerPorDniEnPersona (padronNominal.getNuDniMenor(), Persona.TipoPersona.MENOR);
						if (personaBefore != null) {
							padronNominalBefore = PadronNominal.fromPersona(personaBefore);

							//setea valores de correo y numero de celular de la madre
							Persona personaMadreBefore = aniService.obtenerPorDniEnPersona(personaBefore.getMadreDni(), Persona.TipoPersona.MADRE);
							if (personaMadreBefore != null){
								padronNominalBefore.setNuCelMadre(personaMadreBefore.getNuTelefono());
								padronNominalBefore.setDiCorreoMadre(personaMadreBefore.getDeEmail());
							}
							padronNominal = setValuesInfAdiApod(padronNominal, padronNominalBefore);
						}
					} else {  if (padronNominal.getNuCnv() != null && !padronNominal.getNuCnv().isEmpty() && (padronNominal.getNuCnv().length() == CNV_LENGTH || padronNominal.getNuCnv().length() == CNV_LENGTH_CUI)) {
						PadronNominal padronNominalBefore;
						Persona personaBefore = rcService.obtenerPorCnv(padronNominal.getNuCnv());
							if (personaBefore != null) {
								padronNominalBefore = PadronNominal.fromPersona(personaBefore);
								if(personaBefore.getMadrePrimerApellido()!=null && !personaBefore.getMadrePrimerApellido().isEmpty())
									padronNominalBefore.setApSegundoMenor(personaBefore.getMadrePrimerApellido());
								padronNominal = setValuesInfAdiApod(padronNominal, padronNominalBefore);
							}
						}

					}
				}
			}
			//
			padronNominal.setTiVinculoMadre("1");
			LlenarDescripcionesPadronNominal(padronNominal);
			List<FrecuenciaAtencion> listFrecAtencion = frecuenciaAtencionService.listarFrecuenciaAtencion();

			for ( int i = 0; i< listFrecAtencion.size(); i++){
				if (listFrecAtencion.get(i).getCoFrecuenciaAtencion().equals(padronNominal.getCoFrecAtencion())){
					padronNominal.setDeFrecAtencion(listFrecAtencion.get(i).getNoFrecuenciaAtencion());
					break;
				}
			}
			model.addAttribute("padronNominal", padronNominal);
			if ((nuDniMenor != null && !nuDniMenor.isEmpty()) || coPadronNominal != null && !coPadronNominal.isEmpty()) {
			    if (coPadronNominal != null && !coPadronNominal.isEmpty()) {model.addAttribute("tiRegistroPadron","actualizacion");}
				return "registroManual/fichaConfirmacion";
			} else {
				List<PadronNominal> registrosDuplicados = padronService.buscarDuplicados(padronNominal, REG_ACTIVOS);
				if (registrosDuplicados != null && registrosDuplicados.size() > 0) {
					model.addAttribute("registrosDuplicados", registrosDuplicados);
					return "registroManual/registrosDuplicados";
				}
				else {
                    List<PadronNominal> registrosDuplicadosInactivos = padronService.buscarDuplicados(padronNominal, REG_INACTIVOS);
                    List<PadronNominal> registrosDuplicadosInactivos2;

					if (registrosDuplicadosInactivos != null && registrosDuplicadosInactivos.size() > 0) {
						registrosDuplicadosInactivos2= addDetallesBajaPorPadron(registrosDuplicadosInactivos);

					    model.addAttribute("registrosDuplicadosInactivos", registrosDuplicadosInactivos2);
                        return "registroManual/registrosDuplicadosInactivos";
                    }
					else {
						List<Persona> personas = null;
						List<Persona> personasInactivas;
						List<Persona> personasInactivas2 = new ArrayList<Persona>();

						/*Inicio de busqueda de duplicados de menores por madre documentada e indocumentada*/
						if (padronNominal.getCoDniMadre() != null && !padronNominal.getCoDniMadre().isEmpty()) {
							personas = padronService.listarMenoresPorDniMadre(padronNominal);
							personasInactivas = padronService.listarMenoresPorDniMadreInactivos(padronNominal);

							if (personasInactivas != null && personasInactivas.size() > 0) {
								personasInactivas2 = addDetallesBajaPorPersona(personasInactivas);
							}
						}

						if ((personas != null && personas.size() > 0) || (personasInactivas2!=null && personasInactivas2.size()>0)) {
							model.addAttribute("personaListCoincidencias", personas);
							model.addAttribute("personaListCoincidenciasInactivos",personasInactivas2);
							model.addAttribute("padronNominal",padronNominal);
							return "registroManual/fichaEleccionHijosMadre";
						} else {
								//madres indocumentadas, solo presentan nombres y apellidos, buscamos madre con los mismos datos y con un hijo nacido en la misma fecha
								personas = padronService.listarMenoresPorDatosMadre(padronNominal, REG_ACTIVOS);
								personasInactivas = padronService.listarMenoresPorDatosMadre(padronNominal, REG_INACTIVOS);

								if(personasInactivas != null && personasInactivas.size() > 0) {
								   personasInactivas2 = addDetallesBajaPorPersona(personasInactivas);
								}

								if((personas != null && personas.size() > 0) || (personasInactivas != null && personasInactivas.size() > 0)) {
									model.addAttribute("personaListCoincidencias", personas);
									model.addAttribute("personaListCoincidenciasInactivo", personasInactivas2);
									model.addAttribute("padronNominal", padronNominal);
									return "registroManual/fichaEleccionMadres";
								} else {
									List<Persona> personaList = busquedaDeMenorService.buscarNombresSimilares(padronNominal.getApPrimerMenor(), padronNominal.getApSegundoMenor(), padronNominal.getPrenombreMenor());
									model.addAttribute("personaListCoincidencias", personaList);
									return "registroManual/fichaEleccion";
								}
						}
					}
				}
			}
		}
	}

	private List<PadronNominal> addDetallesBajaPorPadron(List<PadronNominal> registrosDuplicadosInactivos) {
		List<PadronNominal> registrosDuplicadosInactivos2 = new ArrayList<PadronNominal>();

		for (PadronNominal registrosDuplicadosInactivo : registrosDuplicadosInactivos) {
			registrosDuplicadosInactivo.setMotivoBaja(padronService.obtenerMotivoBaja(registrosDuplicadosInactivo.getCoPadronNominal()));
			registrosDuplicadosInactivo.setObservacionBaja(padronService.obtenerObservacionBaja(registrosDuplicadosInactivo.getCoPadronNominal()));
			registrosDuplicadosInactivos2.add(registrosDuplicadosInactivo);
		}
		return registrosDuplicadosInactivos2;
	}

	private List<Persona> addDetallesBajaPorPersona(List<Persona> registrosDuplicadosInactivos) {
		List<Persona> registrosDuplicadosInactivos2 = new ArrayList<Persona>();

		for (Persona registrosDuplicadosInactivo : registrosDuplicadosInactivos) {
			registrosDuplicadosInactivo.setMotivoBaja(padronService.obtenerMotivoBaja(registrosDuplicadosInactivo.getCodigoPadronNominal()));
			registrosDuplicadosInactivo.setObservacionBaja(padronService.obtenerObservacionBaja(registrosDuplicadosInactivo.getCodigoPadronNominal()));
			registrosDuplicadosInactivos2.add(registrosDuplicadosInactivo);
		}
		return registrosDuplicadosInactivos2;
	}

	private void LlenarDescripcionesPadronNominal(PadronNominal padronNominal) {
		padronNominal.setDeGeneroMenor(dominioService.obtener(padronNominal.getCoGeneroMenor(), "CO_GENERO").getDeDom());
		Ubigeo ubigeo = ubigeoService.obtenerPorCodigoInei(padronNominal.getCoUbigeoInei());
		padronNominal.setDeUbigeoInei(ubigeo.getDeDepartamento() + ", " + ubigeo.getDeProvincia() + ", " + ubigeo.getDeDistrito());

		if (padronNominal.getCoEstSalud() != null && padronNominal.getNuSecuenciaLocal() !=null) {
            EstablecimientoSalud establecimientoSalud = establecimientoSaludService.obtenerPorCodigoRenaes(padronNominal.getCoEstSalud() + "_" + padronNominal.getNuSecuenciaLocal());
		/*if(establecimientoSalud != null) {
			padronNominal.setDeEstSalud(establecimientoSalud.getDeEstSalud() + " (RENAES: " + establecimientoSalud.getCoEstSalud() + ", " + establecimientoSalud.getDeDepartamento() + ", " + establecimientoSalud.getDeProvincia() + ", " + establecimientoSalud.getDeDistrito() + ")");
		} */


            padronNominal.setDeEstSalud(establecimientoSalud.getDeEstSalud() + " (Código Renaes: " + establecimientoSalud.getCoEstSalud() + ")");
            /*" <br/><strong>Ubigeo:</strong> " + establecimientoSalud.getDeDepartamento() + ", " + establecimientoSalud.getDeProvincia() + ", " + establecimientoSalud.getDeDistrito());*/
        }

		if (padronNominal.getCoEstSaludNac() != null) {
			EstablecimientoSalud establecimientoSaludNac = establecimientoSaludService.obtenerPorCodigoRenaes(padronNominal.getCoEstSaludNac() + "_" + padronNominal.getNuSecuenciaLocalNac());
			if(establecimientoSaludNac!=null)
				padronNominal.setDeEstSaludNac(establecimientoSaludNac.getDeEstSalud() + " (Código Renaes: " + establecimientoSaludNac.getCoEstSalud() + ")" );
						/*" <br/><strong>Ubigeo:</strong> " + establecimientoSaludNac.getDeDepartamento() + ", " + establecimientoSaludNac.getDeProvincia() + ", " + establecimientoSaludNac.getDeDistrito());*/
		} else {
			padronNominal.setDeEstSaludNac("");
		}

        /*-------------------------------------------------------------------*/
		/* Establecimiento de salud de adscripcion (aquispej@reniec.gob.pe)*/
        if (padronNominal.getCoEstSaludAds() != null) {
            EstablecimientoSalud establecimientoSaludAds = establecimientoSaludService.obtenerPorCodigoRenaes(padronNominal.getCoEstSaludAds() + "_" + padronNominal.getNuSecuenciaLocalAds());
            if(establecimientoSaludAds!=null)
                padronNominal.setDeEstSaludAds(establecimientoSaludAds.getDeEstSalud() + " (Código Renaes: " + establecimientoSaludAds.getCoEstSalud() + ")" );
            /*" <br/><strong>Ubigeo:</strong> " + establecimientoSaludNac.getDeDepartamento() + ", " + establecimientoSaludNac.getDeProvincia() + ", " + establecimientoSaludNac.getDeDistrito());*/
        } else {
            padronNominal.setDeEstSaludAds("");
        }

        /*-------------------------------------------------------------------*/

		//padronNominal.setDeEstSalud(establecimientoSalud.getDeEstSalud());

/*		padronNominal.setDeSeguroMenor(dominioService.obtener(padronNominal.getTiSeguroMenor(), "TI_SEGURO_MENOR").getDeDomDetalle());
		CentroEducativo centroEducativo = centroEducativoService.obtenerPorCodigo(padronNominal.getCoInstEducativa());
		if (centroEducativo != null) {
			padronNominal.setDeInstEducativa(
					centroEducativo.getNoCentroEducativo() + " (Código Modular: " + centroEducativo.getCoModular() + ")");
                            *//*" <br/><strong>Ubigeo:</strong> " + centroEducativo.getDeUbigeo() + ", " + centroEducativo.getNoCentroPoblado() + " <br/><strong>Direccion:</strong> " + centroEducativo.getDiCentroEducativo()*//*
//            );
		}*/
		List<String> codigoListSeguros = padronNominal.getTiSeguroMenor();
		List<Dominio> tipoSeguros = new ArrayList<Dominio>();
		if (codigoListSeguros != null) {
			for (String codigo : codigoListSeguros) {
				tipoSeguros.add(seguroService.obtenerPorCodigoEnDominio(codigo));
			}
		}
		/*padronNominal.setPadronProgramaList(tipoSeguros);*/
		padronNominal.setPadronSeguroList(tipoSeguros);

		List<String> codigoList = padronNominal.getTiProSocial();
		List<Dominio> programaSocial = new ArrayList<Dominio>();
		if (codigoList != null) {
			for (String codigo : codigoList) {
				programaSocial.add(programaSocialService.obtenerPorCodigoEnDominio(codigo));
			}
		}
		padronNominal.setPadronProgramaList(programaSocial);

		if (padronNominal.getTiVinculoMadre() != null){
            padronNominal.setDeVinculoMadre(dominioService.obtener(padronNominal.getTiVinculoMadre(), "TI_VINCULO_MENOR").getDeDom());
        }
		if (padronNominal.getTiVinculoJefe() != null) {
            padronNominal.setDeVinculoJefe(dominioService.obtener(padronNominal.getTiVinculoJefe(), "TI_VINCULO_MENOR").getDeDom());
        }
		if (padronNominal.getCoLenMadre() != null) {
            padronNominal.setDeLenMadre(dominioService.obtener(padronNominal.getCoLenMadre(), "CO_LEN_MADRE").getDeDom());
        }
		if (padronNominal.getCoGraInstMadre() != null) {
            padronNominal.setDeGraInstMadre(dominioService.obtener(padronNominal.getCoGraInstMadre(), "NIVEL_EDUCATIVO").getDeDom());
        }
//		padronNominal.setDeGraInstMadre(nivelEducativoService.obtenerPorCodigo(padronNominal.getCoGraInstMadre()).getDeNivelEduca());


		if (padronNominal.getCoCentroPoblado()!=null && !padronNominal.getCoCentroPoblado().isEmpty()) {
			CentroPoblado centroPoblado = ubigeoService.getCentroPoblado(padronNominal.getCoCentroPoblado());
			padronNominal.setDeCentroPoblado(centroPoblado.getNoCentroPoblado());
		}

		if (padronNominal.getCoVia() != null && !padronNominal.getCoVia().isEmpty()) {
			EjeVial ejeVial = ubigeoService.obtenerEjeVial(padronNominal.getCoVia());
			padronNominal.setDeVia(ejeVial.getDeVia());
		}
	}

	@RequestMapping(value = "guardarconfirmado.do", method = {RequestMethod.POST})
	public String guardarConfirmado(
			@ModelAttribute("padronNominalSesion") PadronNominal padronNominal,
			@ModelAttribute("padronNominalImgSesion") PadronImg padronImg,
			SessionStatus sessionStatus,
			BindingResult result,
			Model model) {
		String coRestri = (padronNominal.getCoRestri()!=null)? padronNominal.getCoRestri():"";

		String[] split = usuario.getCoEntidad().split("_");
		String coEntidad = split[0];
		/*String coTipoEntidad = split[1];*/
		String coTipoEntidad = usuario.getCoTipoEntidad();

        model.addAttribute("fuenteDatosList", fuenteDatosService.obtenerFuenteDatos());
		model.addAttribute("frecuenciaAtencionList", frecuenciaAtencionService.listarFrecuenciaAtencion());

		List<FrecuenciaAtencion> listFrecAtencion = frecuenciaAtencionService.listarFrecuenciaAtencion();

		padronNominal.setUsCreaRegistro(usuario.getLogin());
		padronNominal.setUsModiRegistro(usuario.getLogin());
		padronNominal.setCoEntidad(coEntidad);
		padronNominal.setCoTipoEntidad(coTipoEntidad);

		padronImg.setUsuCreaAudi(usuario.getLogin());
		padronImg.setUsuModiAudi(usuario.getLogin());
		try {
			if (padronService.guardarPadronNominal(padronNominal, padronImg)) {
				padronService.actualizarEstadoPadron(padronNominal);
				padronNominal = padronService.obtenerPorCodigoPadronConDetalles(padronNominal.getCoPadronNominal());
//				padronNominal.setFeVisitaBefore(padronNominal.getFeVisita()); //para comparar
//				padronNominal.setInMenorVisitadoBefore(padronNominal.getInMenorVisitado());

				for ( int i = 0; i< listFrecAtencion.size(); i++){

					if (listFrecAtencion.get(i).getCoFrecuenciaAtencion().equals(padronNominal.getCoFrecAtencion())){
						padronNominal.setDeFrecAtencion(listFrecAtencion.get(i).getNoFrecuenciaAtencion());
						break;
					}
				}

				model.addAttribute("padronNominal", padronNominal);
				model.addAttribute("mensaje", "La información se guardó correctamente");
				if(coRestri.trim().equals("04") || coRestri.trim().equals("A")){
					model.addAttribute("msgRestriccion", "El menor se encuentra fallecido, su registro en el padrón queda observado");
				}
			} else {
				model.addAttribute("padronNominal", null);
				model.addAttribute("mensaje", "No se pudo encontrar la información del Padrón Nominal");
			}
		} catch (DataIntegrityViolationException e) {
			logger.error("Error:", e);
			if (padronNominal.getNuCui() != null) {
				if(padronService.existeNuCui(padronNominal.getNuCui())) {
					model.addAttribute("coPadronNominal", padronService.getCoPadronNominalByCui(padronNominal.getNuCui()));
					model.addAttribute("mensajeErrorCui", String.format("CUI: %s duplicado, ya existe en el PADRON! ", padronNominal.getNuCui()));
				}
			}
			registrarDominioItems(model);
			model.addAttribute("padronNominal", padronNominal);
			String isSusalud = padronService.isSusalud(padronNominal.getCoPadronNominal())?"1":"0";
			String isTipoSeguro = padronService.isTipoSeguro(padronNominal.getCoPadronNominal())?"1":"0";
			model.addAttribute("isSusalud", isSusalud);
			model.addAttribute("isTipoSeguro", isTipoSeguro);
			if (padronNominal!=null && padronNominal.getNuDniMenor() != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getNuDniMenor().getBytes())));
			}

			return "registroManual/formulario";
		}
		sessionStatus.setComplete();
		return "registroManual/detalles";
	}

	@RequestMapping(value = "corregirinformacion.do")
	public String corregirInformacion(@ModelAttribute("padronNominalSesion") PadronNominal padronNominal, Model model) {
		boolean existenDatos = false;
		String nuDniMenor = padronNominal.getNuDniMenor();
		String coPadronNominal = padronNominal.getCoPadronNominal();
		if ((nuDniMenor != null && !nuDniMenor.isEmpty()) || coPadronNominal != null && !coPadronNominal.isEmpty()) {
			existenDatos = true;
			if (nuDniMenor != null) {
				padronNominal.setOrigenFoto(Persona.OrigenFoto.ANI);
				if(padronNominal != null && padronNominal.getNuDniMenor() != null){
					model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getNuDniMenor().getBytes())));
				}

			} else {
				padronNominal.setOrigenFoto(Persona.OrigenFoto.PN);
			}
		}
		registrarDominioItems(model);
		String isSusalud = padronService.isSusalud(padronNominal.getCoPadronNominal())?"1":"0";
		String isTipoSeguro = padronService.isTipoSeguro(padronNominal.getCoPadronNominal())?"1":"0";
        model.addAttribute("fuenteDatosList", fuenteDatosService.obtenerFuenteDatos());
		model.addAttribute("frecuenciaAtencionList", frecuenciaAtencionService.listarFrecuenciaAtencion());
		model.addAttribute("isSusalud", isSusalud);
		model.addAttribute("isTipoSeguro", isTipoSeguro);
		model.addAttribute("padronNominal", padronNominal);
		model.addAttribute("existenDatos", existenDatos);
		return "registroManual/formulario";
	}

	@RequestMapping(value = "seleccionarnuevomenor.do")
	public String seleccionarNuevoMenor(@ModelAttribute("padronNominalSesion") PadronNominal padronNominalGuardado,
										@RequestParam(value = "codigoMenorCoincidente", required = false) String codigoMenorCoincidente,
										Model model) {
		if (codigoMenorCoincidente != null && !codigoMenorCoincidente.isEmpty()) {
			boolean existenDatos = false;
			String[] parsedString = codigoMenorCoincidente.split("=");
			PadronNominal padronNominal = null;
			String documentoValidado = null;
			String numeroDocumento = null;
			if (parsedString.length == 2) {
				if (parsedString[0].equals("dni")) {
					documentoValidado = "DNI";
					Persona persona = aniService.obtenerPorDniEnPersona(parsedString[1], Persona.TipoPersona.MENOR);
					if (persona != null) {
						padronNominal = PadronNominal.fromPersona(persona);
						model.addAttribute("baseDatosOrigen", Persona.BaseDatosOrigen.ANI);
						existenDatos = true;
					}
				} else if (parsedString[0].equals("pn")) {
					documentoValidado = "código de Padrón Nominal ";
					padronNominal = padronService.obtenerPorCodigoPadron(parsedString[1]);
//					padronNominal.setFeVisitaBefore(padronNominal.getFeVisita()); //para comparar
//					padronNominal.setInMenorVisitadoBefore(padronNominal.getInMenorVisitado()); //para comparar
					model.addAttribute("baseDatosOrigen", Persona.BaseDatosOrigen.PN);
				}
				numeroDocumento = parsedString[1];
			}
			if (padronNominal != null) {
				String nuDniMenor = padronNominalGuardado.getNuDniMenor();
				padronNominalGuardado.setNuDniMenor(padronNominal.getNuDniMenor());
				padronNominalGuardado.setCoPadronNominal(padronNominal.getCoPadronNominal());
				padronNominalGuardado.setApPrimerMenor(padronNominal.getApPrimerMenor());
				padronNominalGuardado.setApSegundoMenor(padronNominal.getApSegundoMenor());
				padronNominalGuardado.setPrenombreMenor(padronNominal.getPrenombreMenor());
				padronNominalGuardado.setFeNacMenor(padronNominal.getFeNacMenor());
				padronNominalGuardado.setCoGeneroMenor(padronNominal.getCoGeneroMenor());
				padronNominalGuardado.setEsPadron(padronNominal.getEsPadron());
				String coPadronNominal = padronNominalGuardado.getCoPadronNominal();

				if ((nuDniMenor != null && !nuDniMenor.isEmpty()) || coPadronNominal != null && !coPadronNominal.isEmpty()) {
					existenDatos = true;

					if(padronNominal != null && padronNominal.getOrigenFoto() != null &&padronNominal.getOrigenFoto().equals(Persona.OrigenFoto.ANI)){
						model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(padronNominal.getNuDniMenor().getBytes())));
					}
				}
				logger.debug("existenDatos:" + existenDatos);
				registrarDominioItems(model);
				model.addAttribute("padronNominal", padronNominalGuardado);
				model.addAttribute("ubigeoMenor", ubigeoService.obtenerPorCodigoInei(padronNominal.getCoUbigeoInei()));
				model.addAttribute("existenDatos", existenDatos);
                model.addAttribute("fuenteDatosList", fuenteDatosService.obtenerFuenteDatos());
				model.addAttribute("frecuenciaAtencionList", frecuenciaAtencionService.listarFrecuenciaAtencion());

				return "registroManual/formulario";
			} else {
				if (documentoValidado == null) {
					model.addAttribute("mensaje", "La información proporcionada no es válida.");
				} else {
					model.addAttribute("mensaje", "No se encontró ningun menor con el " + documentoValidado + " número " + numeroDocumento);
				}

				return "registroManual/buscarmenor-form";
			}
		} else {
			model.addAttribute("mensaje", "La información proporcionada no es válida.");
			return "registroManual/buscarmenor-form";
		}
	}

	@RequestMapping(value = "buscarJefeFamiliaPorDni.do")
	public String buscarJefeFamiliaPorDni(
			@RequestParam(value = "coDniJefeFam", required = false) String coDniJefeFam,
			@RequestParam(value = "tiVinculoJefe", required = false) String tiVinculoJefe, Model model) {

		if (coDniJefeFam != null && !coDniJefeFam.isEmpty()) {
			Persona persona = null;

			if (tiVinculoJefe.equals("2")) { //padre
				persona = aniService.obtenerPorDniEnPersona(coDniJefeFam, Persona.TipoPersona.PADRE);
			}
			else if(tiVinculoJefe.equals("1")) { // madre
				persona = aniService.obtenerPorDniEnPersona(coDniJefeFam, Persona.TipoPersona.MADRE);
			}
			else
				persona = aniService.obtenerPorDniEnPersona(coDniJefeFam, Persona.TipoPersona.TODOS);

			if (persona != null) {
				model.addAttribute("persona", persona);
				model.addAttribute("mensaje", "El DNI indicado corresponde a " + persona.getPrimerApellido() + " " + persona.getSegundoApellido() + ", " + persona.getNombres());
			} else {
				int tempIndex = coDniJefeFam.indexOf("_");
				String dni;
				if (tempIndex >= 0) {
					dni = coDniJefeFam.substring(0, tempIndex);
				} else {
					dni = coDniJefeFam;
				}
				//model.addAttribute("mensaje", "El DNI " + dni + " no es válido o no pertenece a ninguna persona");

				persona = aniService.obtenerPorDniEnPersona(coDniJefeFam, Persona.TipoPersona.MENOR);
				if (persona != null) {
					model.addAttribute("mensaje", "El DNI " + dni + " corresponde a un menor de edad");
				} else {
					String genero = aniService.obtenerGeneroPorDniPersona(coDniJefeFam);
					if(genero!="" && genero.equals(tiVinculoJefe)){
						model.addAttribute("mensaje", "El DNI " + dni + " pertenece a una persona de diferente género");
					}else{
						model.addAttribute("mensaje", "El DNI " + dni + " no es válido o no pertenece a ninguna persona");
					}
				}
			}
		} else {
			model.addAttribute("mensaje", "");
		}
		return "registroManual/resultadoBusquedaJefeFamiliaPorDni";
	}

	@RequestMapping(value = "buscarMenorPorDni.do")
	public String doBuscarMenorPorDni(@RequestParam(value = "nuDniMenor", required = false) String nuDniMenor, Model model) {
		if (nuDniMenor != null && !nuDniMenor.isEmpty()) {
			Persona persona = aniService.obtenerPorDniEnPersona(nuDniMenor, Persona.TipoPersona.MENOR);
			if (persona != null) {
				model.addAttribute("persona", persona);
				model.addAttribute("mensaje", "El DNI indicado corresponde a " + persona.getPrimerApellido() + " " + persona.getSegundoApellido() + ", " + persona.getNombres());

			} else {
				int tempIndex = nuDniMenor.indexOf("_");
				String dni;
				if (tempIndex >= 0) {
					dni = nuDniMenor.substring(0, tempIndex);
				} else {
					dni = nuDniMenor;
				}

				model.addAttribute("mensaje", "El DNI " + dni + " no es válido o no pertenece a ninguna persona");
			}
		} else {
			model.addAttribute("mensaje", "");
		}
		return "registroManual/resultadoBusquedaMenorPorDni";
	}

	/*Contabilizar Busquedas al ANI(por dni de la madre)*/
	@RequestMapping(value = "buscarMadreApoderadoPorDni.do")
	public String buscarMadreApoderadoPorDni(@RequestParam(value = "coDniMadre", required = false) String coDniMadre, Model model) {
		if (coDniMadre != null && !coDniMadre.isEmpty()) {
			Persona persona = aniService.obtenerPorDniEnPersona(coDniMadre, Persona.TipoPersona.MADRE);
			if (persona != null) {
				model.addAttribute("persona", persona);
				model.addAttribute("mensaje", "El DNI " + coDniMadre + " corresponde a " + persona.getPrimerApellido() + " " + persona.getSegundoApellido() + ", " + persona.getNombres());
			} else {
				int tempIndex = coDniMadre.indexOf("_");
				String dni;
				if (tempIndex >= 0) {
					dni = coDniMadre.substring(0, tempIndex);
				} else {
					dni = coDniMadre;
				}
				persona = aniService.obtenerPorDniEnPersona(coDniMadre, Persona.TipoPersona.MENOR);
				if (persona != null) {
					model.addAttribute("mensaje", "El DNI " + dni + " corresponde a un menor de edad");
				} else {
					persona = aniService.obtenerPorDniEnPersona(coDniMadre, Persona.TipoPersona.PADRE);
					if (persona != null){
						model.addAttribute("mensaje", "El DNI " + dni + " pertenece al género masculino");
					} else {
						model.addAttribute("mensaje", "El DNI " + dni + " no existe en la base de datos");
					}
				}
			}
		} else {
			model.addAttribute("mensaje", "");
		}
		return "registroManual/resultadoBusquedaMadreApoderadoPorDni";
	}

	/*
	solo para efectos de pruebas
	 */
	@RequestMapping(value = "detalles.do")
	public String detalles(@RequestParam(value = "codigo", required = false) String codigo, Model model) {
		PadronNominal padronNominal = padronService.obtenerPorCodigoPadronConDetalles(codigo);
		model.addAttribute("padronNominal", padronNominal);
		return "registroManual/detalles";
	}

	@RequestMapping(value = "detalle.do")
	public String detalle(@RequestParam(value = "codigo", required = false) String codigo, @RequestParam(value = "nuEnvio") String nuEnvio, @RequestParam(value = "nuPagina") String nuPagina, Model model) {
		PadronNominal padronNominal = padronService.obtenerPorCodigoPadronConDetalles(codigo);
		//nuEnvio=${nuEnvio}&amp;nuPagina=${nuPagina}
		model.addAttribute("padronNominal", padronNominal);
		model.addAttribute("nuEnvio", nuEnvio);
		model.addAttribute("nuPagina", nuPagina);
		return "registroManual/detalle";
	}

	@RequestMapping(value = "personasindocumento.do")
	public String personasindocumento(Model model) {
		model.addAttribute("nivelPobrezaItems", dominioService.nivelPobrezaItems());
		model.addAttribute("tipoSeguroItems", dominioService.tipoSeguroItems());
		model.addAttribute("tipoProgramaSocialItems", dominioService.tipoProgramaSocialItems());
		model.addAttribute("tipoGestionItems", dominioService.tipoGestionItems());
		model.addAttribute("vinculoFamiliarItems", dominioService.vinculoFamiliarItems());
		model.addAttribute("gradoInstruccionItems", dominioService.gradoInstruccionItems());
		model.addAttribute("lenguajeHabitualItems", dominioService.lenguajeHabitualItems());
		model.addAttribute("etniaItems", dominioService.etniaItems());
		PadronNominal padronNominal = new PadronNominal();
		padronNominal.setApPrimerMenor("FLORES");
		padronNominal.setApSegundoMenor("BELIZARIO");
		padronNominal.setPrenombreMenor("VICTOR DINO");
		padronNominal.setFeNacMenor("25/09/2010");
		padronNominal.setCoGeneroMenor("1");
		padronNominal.setCoUbigeoInei("140101");
		padronNominal.setDeDireccion("AV TEODOMIRO GUTIERRES 578");
		padronNominal.setCoNivelPobreza("1");
		padronNominal.setCoEstSalud("00003251");
		/*padronNominal.setTiSeguroMenor("1");*/
		padronNominal.setNuAfiliacion("123456");
		padronNominal.setTiProSocial(new ArrayList<String>() {{	add("1"); }});
		padronNominal.setTiSeguroMenor(new ArrayList<String>() {{ add("1"); }});
		padronNominal.setCoInstEducativa("89134");
		padronNominal.setTiVinculoJefe("2");
		padronNominal.setCoDniJefeFam("04326311");
		padronNominal.setApPrimerJefe("FLORES");
		padronNominal.setApSegundoJefe("PARIZACA");
		padronNominal.setPrenomJefe("VICTOR");
		padronNominal.setTiVinculoMadre("1");
		padronNominal.setCoDniMadre("07745874");
		padronNominal.setApPrimerMadre("BELIZARIO");
		padronNominal.setApSegundoMadre("PACOMPIA");
		padronNominal.setPrenomMadre("CRISTINA");
		padronNominal.setCoGraInstMadre("30");
		padronNominal.setCoLenMadre("1");
		model.addAttribute("padronNominal", padronNominal);
		model.addAttribute("existenDatos", false);
		return "registroManual/formulario";
	}

}


