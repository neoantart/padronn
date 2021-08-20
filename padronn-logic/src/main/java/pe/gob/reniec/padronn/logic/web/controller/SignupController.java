package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.gob.reniec.padronn.logic.dao.GrupoDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.BuscarMenorDocumento;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;
import pe.gob.reniec.padronn.logic.model.form.SignupUpdateForm;
import pe.gob.reniec.padronn.logic.service.*;
import pe.gob.reniec.padronn.logic.util.ImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import pe.gob.reniec.padronn.logic.util.SimpleBase64;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 16/07/13
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class SignupController extends AbstractBaseController {

    private static final String REG_VALID_ENTIDAD = "[\\w\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-, \\.]+";
    private static final String REG_VALID_STR_NUMERIC = "\\d*";
    private static final String REG_VALID_CO_ENTIDAD = "[\\d_,-]*";
    private static final String MESA_AYUDA ="3";

    Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioExternoService usuarioExternoService;

	@Autowired
	PadronService padronService;

	@Autowired
	Usuario usuario;

	@Autowired
	PadronProperties padronProperties;

	@Autowired
	RegistroManualController registroManualController;

	@Autowired
	AniService aniService;

	@Autowired
	BusquedaDeMenorService busquedaDeMenorService;

	@Autowired
	DominioService dominioService;

    @Autowired
    GrupoService grupoService;

    @Autowired
    GrupoPermisoService grupoPermisoService;

	@Autowired
	private ImagenCiudadano imagenCiudadano;

	@RequestMapping("/signup/form.do")
	public String signup(@ModelAttribute SignupForm signupForm) {
		return "signup/form";
	}

	@RequestMapping("/signup/new.do")
	@ResponseBody
	public Map<String, Object> signup(@Valid SignupForm signupForm, BindingResult result) {
		Map<String, Object> res = new HashMap<String, Object>();
		if (result.hasErrors()) {
			Map<String, Object> errors = getErrores(result);
			res.put("success", false);
			res.put("error", errors);
			res.put("mensaje", "No se puede guardar el usuario");
			return res;
		}

		String coUsuario = signupForm.getCoUsuario();
		if (usuarioExternoService.getUsuarioExterno(coUsuario) != null) {
			result.rejectValue("coUsuario", "errors.signup.coUsuario", "Codigo de usuario esta en uso.");
			Map<String, Object> errors = getErrores(result);
			res.put("success", false);
			res.put("error", errors);
			res.put("mensaje", "No se puede guardar el usuario");
			return res;
		}
		String password = signupForm.getDePassword();
		String rePassword = signupForm.getRePassword();

//		log.info("password: " + password);
//		log.info("rePassword: " + rePassword);

		if (!password.equals(rePassword)) {
			result.rejectValue("dePassword", "errors.signup.dePassword", "Password no es igual .");
			result.rejectValue("rePassword", "errors.signup.rePassword", "Password no es igual .");
			Map<String, Object> errors = getErrores(result);
			res.put("success", false);
			res.put("error", errors);
			res.put("mensaje", "No se puede guardar el usuario");
			return res;
		}

		UsuarioExterno usuarioExterno = new UsuarioExterno();
		usuarioExterno.setCoUsuario(signupForm.getCoUsuario());
		usuarioExterno.setApPrimer(signupForm.getApPrimer());
		usuarioExterno.setApSegundo(signupForm.getApSegundo());
		usuarioExterno.setPrenombres(signupForm.getPrenombres());
		usuarioExterno.setDePassword(signupForm.getDePassword());
		usuarioExterno.setUsCreaAudi(usuario.getLogin());
		usuarioExterno.setUsModiAudi(usuario.getLogin());
		//usuarioExterno.setEsUsuario(signupForm.getEsUsuario());
		//usuarioExterno.setDeEmail(signupForm.getDeEmail());
		usuarioExterno.setCoTipoAccionPw("01");

		if (signupForm.getGrupos() != null)
			usuarioExterno.setGrupos(signupForm.getGrupos());
		if (signupForm.getEntidades() != null)
			usuarioExterno.setEntidades(signupForm.getEntidades());
//		log.info("OK: usuarioExterno: " + usuarioExterno.toString());
		usuarioExternoService.insert(usuarioExterno);
		res.put("success", true);
		res.put("mensaje", "Se agrego correctamente el usuario.");
		return res;
	}

	@RequestMapping("/signup/get_extras.do")
	@ResponseBody
	public Map<String, Object> getExtras() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("grupos", usuarioExternoService.getGrupos());
		data.put("entidades", usuarioExternoService.getEntidades());
		result.put("data", data);
		return result;
	}


	@RequestMapping("/signup/tipo_entidades.do")
	@ResponseBody
	public Map<String, Object> getTipoEntidades() {
		Map<String, Object> res = new HashMap<String, Object>();

		res.put("success", true);
		res.put("data", usuarioExternoService.getTipoEntidades());
		return res;
	}

	@RequestMapping("/signup/grupos.do")
	@ResponseBody
	public Map<String, Object> getGrupos() {

		Map<String, Object> res = new HashMap<String, Object>();
		res.put("success", true);
		res.put("data", usuarioExternoService.getGrupos());
		return res;
	}

/*
	@RequestMapping("/signup/get_grupos_usuario.do")
	@ResponseBody
	public Map<String,Object> get_grupos_usuario(@RequestParam(required = true, value = "coGrupos") String coGrupos){
		Map<String, Object> res = new HashMap<String, Object>();
		logger.info(coGrupos);
		*/
/*res.put("data", usuarioExternoService.getGrupos());*//*

		return res;
	}
*/

	@RequestMapping("/signup/get_grupo.do")
	@ResponseBody
	public Grupo getGrupo(@RequestParam(required = true, value = "coGrupo") String coGrupo) {
        if (!coGrupo.matches(REG_VALID_STR_NUMERIC)) {
            return new Grupo();
        }
        return usuarioExternoService.getGrupo(coGrupo);
    }

	@RequestMapping("/signup/get_entidad.do")
	@ResponseBody
	public List<Entidad> getEntidad(
            @RequestParam(required = true, value = "coEntidad") String coEntidad) {
        // validacion de coEntidad
        if (!coEntidad.matches(REG_VALID_CO_ENTIDAD)) {
            return new ArrayList<Entidad>();
        }
        String[] entidades = coEntidad.split(",");

        List<Entidad> result = new ArrayList<Entidad>();
        for (String entidad : entidades) {
            String[] split = entidad.split("_");
            result.add(usuarioExternoService.getEntidad(split[0], split[1]));
        }
        return result;
    }

	@RequestMapping("/signup/buscar_entidad.do")
	@ResponseBody
	public List<Entidad> buscarEntidad(@RequestParam(required = true, value = "parteDeEntidad") String parteDeEntidad,
									   @RequestParam(required = true, value = "tipoEntidades") String tipoEntidadesItem) {

		if (!parteDeEntidad.matches(REG_VALID_ENTIDAD)) {
			return new ArrayList<Entidad>();
		}

		String[] tipoEntidades = tipoEntidadesItem.split(",");

		List<String> result = new ArrayList<String>();
		for (String tipoEntidad : tipoEntidades) {
			result.add(tipoEntidad);
		}
		//tipoEntidades
		return usuarioExternoService.listarEntidad(parteDeEntidad, result);
	}

	@RequestMapping("/signup/lista_usuarios.do")
	public String listaUsuarios(@RequestParam(required = false) Integer nuPagina, Model model) {
		nuPagina = (nuPagina == null || nuPagina <= 0) ? 1 : nuPagina;
		int filas = usuarioExternoService.countAll();

		model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
		model.addAttribute("nuPagina", nuPagina);
		List<UsuarioExterno> lista = usuarioExternoService.getAll((nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);
		model.addAttribute("usuariosList", lista);
		//log.info("usuariosList: " + usuarioExternoService.getAll((nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS));
		//log.debug("usuariosList:"+lista);
		return "mantenimiento/usuarios";
	}

	@RequestMapping("/signup/get_usuario.do")
	public String getUsuario(@RequestParam(required = false, value = "coUsuario") String coUsuario,
                             @RequestParam(required = false, value = "nuPagina") String nuPagina,
                             @RequestParam(required = false, value = "buscar") String buscar,
                             @RequestParam(required = false, value = "resetear") String resetear,
                             Model model) {
		UsuarioExterno usuarioExterno = usuarioExternoService.getUsuarioExterno(coUsuario);
		if (usuarioExterno != null) {
			model.addAttribute("signupForm", usuarioExterno);
            model.addAttribute("detalle", "");
            model.addAttribute("buscar", buscar);
            model.addAttribute("resetear", resetear);
			if (coUsuario != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(coUsuario.getBytes())));
			}
			return "mantenimiento/usuario-detalle";
		}
		model.addAttribute("noExisteUsuario", true);
		return "mantenimiento/usuario-form-busqueda";
	}

    @RequestMapping("/signup/form_update.do")
    public String formUpdate(@RequestParam(required = false, value = "coUsuario") String coUsuario,
							 @RequestParam(required = false, value = "nuPagina") String nuPagina, /*@ModelAttribute SignupForm signupForm,*/
							 Model model){
        UsuarioExterno usuarioExterno = usuarioExternoService.getUsuarioExterno(coUsuario);
        //String tiposEntidad = usuarioExterno.getTiposEntidad().toString().replaceAll("[\\[|\\]]| ", "");
        if (usuarioExterno != null) {
			SignupForm signupForm = new SignupForm();

			signupForm.setCoUsuario(usuarioExterno.getCoUsuario());
			signupForm.setApPrimer(usuarioExterno.getApPrimer());
			signupForm.setApSegundo(usuarioExterno.getApSegundo());
			signupForm.setPrenombres(usuarioExterno.getPrenombres());
			signupForm.setNuTelefono(usuarioExterno.getNuTelefono());
			signupForm.setDeEmail(usuarioExterno.getDeEmail());
			signupForm.setEsUsuario(usuarioExterno.getEsUsuario());
			signupForm.setGrupos(usuarioExterno.getGrupos());
			signupForm.setTipoEntidades(usuarioExterno.getTipoEntidades());
			signupForm.setEntidades(usuarioExterno.getEntidades());
            //signupForm.setTiposEntidad(usuarioExterno.getTiposEntidad());
            model.addAttribute("signupForm", usuarioExterno);
			model.addAttribute("action", "update.do");
			model.addAttribute("coUsuario", coUsuario);
			model.addAttribute("titulo", "Actualizar usuario con DNI: " + coUsuario);
            model.addAttribute("usuarioExterno", usuarioExterno);
            //model.addAttribute("listaDeGrupos", usuarioExternoService.getGrupos());
			if (nuPagina == null || nuPagina.isEmpty())
				nuPagina = "1";
			model.addAttribute("nuPagina", nuPagina);

			if (coUsuario != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(coUsuario.getBytes())));
			}

			List<String> grupos = usuario.getGrupos();
			if(grupos.contains(MESA_AYUDA) && grupos.size()==1){//perfil de usuario solo con mesa de ayuda
				model.addAttribute("esSoporte", true);
			} else{
				model.addAttribute("esSoporte",false);
			}

			/*model.addAttribute("esSoporte", );*/
            model.addAttribute("esMinsa", usuario.getEsMinsa());
            return "mantenimiento/usuario-form";
        }
        model.addAttribute("noExisteUsuario", true);
        return "mantenimiento/usuario-form-busqueda";
    }

	@RequestMapping("/signup/update.do")
	public String update(@Valid @ModelAttribute("signupForm") SignupUpdateForm signupForm, BindingResult result, Model model) {
		model.addAttribute("action", "update.do");
		Map<String, Object> res = new HashMap<String, Object>();
		String coUsuario = signupForm.getCoUsuario();
		if (result.hasErrors()) {
            UsuarioExterno usuarioExterno = usuarioExternoService.getUsuarioExterno(coUsuario);
            model.addAttribute("coUsuario", usuarioExterno.getCoUsuario());
            model.addAttribute("usuarioExterno", usuarioExterno);
			res.put("mensajeError", "No se puede guardar el usuario");
			if (coUsuario != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(coUsuario.getBytes())));
			}

			List<String> grupos = usuario.getGrupos();
			if(grupos.contains(MESA_AYUDA) && grupos.size()==1){//perfil de usuario solo con mesa de ayuda
				model.addAttribute("esSoporte", true);
			}else{
				model.addAttribute("esSoporte",false);
			}

			model.addAttribute("esMinsa", usuario.getEsMinsa());

			//model.addAttribute(signupForm);
			return "mantenimiento/usuario-form";
		}
		String password = signupForm.getDePassword();
		String rePassword = signupForm.getRePassword();

        UsuarioExterno tmp = usuarioExternoService.getUsuarioExterno(signupForm.getCoUsuario());
		UsuarioExterno usuarioExterno = new UsuarioExterno();
		usuarioExterno.setCoUsuario(signupForm.getCoUsuario());
		usuarioExterno.setApPrimer(signupForm.getApPrimer());
		usuarioExterno.setApSegundo(signupForm.getApSegundo());
		usuarioExterno.setPrenombres(signupForm.getPrenombres());
		usuarioExterno.setDePassword(signupForm.getDePassword());
        usuarioExterno.setEntidades(signupForm.getEntidades());

		usuarioExterno.setUsCreaAudi(usuario.getLogin());
		usuarioExterno.setUsModiAudi(usuario.getLogin());
		usuarioExterno.setNuTelefono(signupForm.getNuTelefono());
		usuarioExterno.setEsUsuario(signupForm.getEsUsuario());
		usuarioExterno.setDeEmail(signupForm.getDeEmail());
        usuarioExterno.setFeLastLogin(tmp.getFeLastLogin());
        usuarioExterno.setFeLastLogin(tmp.getFeLastLogin());
        usuarioExterno.setDeObservacion(signupForm.getDeObservacion());
        usuarioExterno.setCoTipoAccionPw("03");

		if (signupForm.getGrupos() != null)
			usuarioExterno.setGrupos(signupForm.getGrupos());

		/*model.addAttribute("grupos", usuarioExternoService.getDeGrupos(usuarioExterno.getGrupos()));*/

		if (usuarioExternoService.update(usuarioExterno)) {
            usuarioExterno = usuarioExternoService.getUsuarioExterno(coUsuario);

            model.addAttribute("signupForm", usuarioExterno);
//            model.addAttribute("entidades", usuarioExterno.getDeEntidades());
			model.addAttribute("mensaje", "Se actualizo al usuario exitosamente.");
	         /*model.addAttribute("entidades", usuarioExternoService.getDeEntidades(usuarioExterno.getEntidades()));
            model.addAttribute("grupos", usuarioExternoService.getDeGrupos(usuarioExterno.getGrupos()));*/

            model.addAttribute("buscar", "1");
			if (coUsuario != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(coUsuario.getBytes())));
			}
			return "mantenimiento/usuario-detalle";
		} else {
			model.addAttribute("mensajeError", "No se pudo actualizar al usuario.");
			if (coUsuario != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(coUsuario.getBytes())));
			}
			return "mantenimiento/usuario-form";
		}
	}

    @RequestMapping("/signup/detalle_usuario.do")
    public String detalleUsuario(Model model, HttpServletRequest request) {
        String coUsuario = request.getParameter("coUsuario");
        UsuarioExterno usuarioExterno = usuarioExternoService.getUsuarioExterno(coUsuario);
        model.addAttribute("signupForm", usuarioExterno);
        model.addAttribute("buscar", "1");
		if (usuarioExterno!=null && usuarioExterno.getCoUsuario() != null) {
			model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(usuarioExterno.getCoUsuario().getBytes())));
		}
        return "mantenimiento/usuario-detalle";
    }

	@RequestMapping(value="/signup/buscar_usuario.do")
	public String buscarUsuario(Model model,
                                @RequestParam(value = "buscar", required = false) String buscar,
                                @RequestParam(value = "resetear", required = false) String resetear
                                ) {
        model.addAttribute("buscar", buscar);
        model.addAttribute("resetear", resetear);
		return "mantenimiento/usuario-form-busqueda";
	}

	@RequestMapping("/signup/nuevo_usuario.do")
	public String nuevoUsuario(@ModelAttribute SignupForm signupForm, Model model) {
		model.addAttribute("action", "insert.do");
		model.addAttribute("titulo", "Registro de Nuevo Usuario");
		/*model.addAttribute("tipoEntidades", dominioService.tipoEntidadesItems());*/
        signupForm.setEsUsuario("1");
		return "mantenimiento/usuario-form";
	}

	@RequestMapping("/signup/insert.do")
	public String insert(@Valid @ModelAttribute("signupForm") SignupForm signupForm, BindingResult result, Model model) {
		//Map<String, Object> res = new HashMap<String, Object>();
        model.addAttribute("titulo", "Registro de Nuevo Usuario");
		if (result.hasErrors()) {
			model.addAttribute("action", "insert.do");
			/*model.addAttribute("tipoEntidadItems", dominioService.tipoEntidadesItems());*/
			model.addAttribute("mensajeErrorInsert", "No se puede guardar el usuario");
			if (signupForm != null && signupForm.getCoUsuario()!=null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(signupForm.getCoUsuario().getBytes())));
			}
			return "mantenimiento/usuario-form";
		}

		String coUsuario = signupForm.getCoUsuario();

		String password = signupForm.getDePassword();
		String rePassword = signupForm.getRePassword();

		UsuarioExterno usuarioExterno = new UsuarioExterno();
		usuarioExterno.setCoUsuario(signupForm.getCoUsuario());
		usuarioExterno.setApPrimer(signupForm.getApPrimer());
		usuarioExterno.setApSegundo(signupForm.getApSegundo());
		usuarioExterno.setPrenombres(signupForm.getPrenombres());
		usuarioExterno.setDePassword(signupForm.getDePassword());
		usuarioExterno.setUsCreaAudi(usuario.getLogin());
		usuarioExterno.setUsModiAudi(usuario.getLogin());
		usuarioExterno.setNuTelefono(signupForm.getNuTelefono());
		usuarioExterno.setEsUsuario(signupForm.getEsUsuario());
		usuarioExterno.setDeEmail(signupForm.getDeEmail());
		usuarioExterno.setCoTipoAccionPw("01");

		if (signupForm.getGrupos() != null)
			usuarioExterno.setGrupos(signupForm.getGrupos());
		if (signupForm.getEntidades() != null) {
            usuarioExterno.setEntidades(signupForm.getEntidades());
        }

		model.addAttribute("grupos", usuarioExternoService.getDeGrupos(usuarioExterno.getGrupos()));

		if (usuarioExternoService.insert(usuarioExterno)) {
            UsuarioExterno usuarioExterno1 = usuarioExternoService.getUsuarioExterno(usuarioExterno.getCoUsuario());
            if (usuarioExterno1 != null) {
                model.addAttribute("signupForm", usuarioExterno1);
                model.addAttribute("mensaje", "Se agrego correctamente al usuario: " + usuarioExterno.getCoUsuario());
            }
            model.addAttribute("buscar", "1");
			if (coUsuario != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(coUsuario.getBytes())));
			}
            return "mantenimiento/usuario-detalle";
		} else {
			model.addAttribute("action", "insert.do");
			model.addAttribute("mensajeErrorInsert", "Ocurrio un error registrando al usuario.");
			if (coUsuario != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(coUsuario.getBytes())));
			}
			return "mantenimiento/usuario-form";
		}
	}

    @RequestMapping(value = "/signup/buscar_usuario.do", method= RequestMethod.POST)
    public String doBuscarUsuario(@RequestParam(value = "apPrimer") String apPrimer,
                                  @RequestParam(value = "apSegundo") String apSegundo,
                                  @RequestParam(value = "preNombres") String preNombres,
                                  @RequestParam(value="buscar") String buscar,
                                  @RequestParam(value="resetear") String resetear,
                                  Model model) {
        model.addAttribute("usuariosList", usuarioExternoService.buscarUsuarioExterno(apPrimer, apSegundo, preNombres));
        model.addAttribute("buscar", buscar);
        model.addAttribute("resetear", resetear);
        return "mantenimiento/resultado-buscar-usuario";
    }

    @RequestMapping("/signup/get_usuario_ani.do")
    @ResponseBody
    public Map<String, Object> doGetUsuarioAni(@RequestParam(value = "coUsuario") String coUsuario, Model model) {
        Map<String, Object> result = new HashMap<String, Object>();
        // validacion coUsuario
        if (!coUsuario.matches(REG_VALID_STR_NUMERIC)) {
            return result;
        }

        UsuarioExterno usuarioExterno = usuarioExternoService.getUsuarioAni(coUsuario);

        if (usuarioExterno.getCoUsuario() != null) {
			Objeto resultVerificacion = usuarioExternoService.verificarUsuario(coUsuario);

			if (resultVerificacion != null) {
				result.put("success", false);
				result.put("mensaje", resultVerificacion.getText());
				return result;
			}
			result.put("success", true);
            result.put("data", usuarioExterno);
            result.put( "mensaje", "DNI:  " + usuarioExterno.getCoUsuario() + " <br/> Nombres: " + usuarioExterno.getNombresCompletos() );
        }
        else{
            result.put("success", false);
            result.put("mensaje", "El DNI <strong>" + coUsuario + "</strong> no es válido o no pertenece a ninguna persona.");
        }
        return result;
    }

	@RequestMapping("/registromanual/padronnform.do")
	public String doPadronnForm(@ModelAttribute(value = "buscarMenorDocumento") BuscarMenorDocumento buscarMenorDocumento) {

		return "mantenimiento/padronn-form";
	}

	@RequestMapping("/registromanual/padronnbuscarmenor.do")
	public String doPadronnBuscarMenor(@ModelAttribute(value = "buscarMenorDocumento") BuscarMenorDocumento buscarMenorDocumento, Model model) {
		Persona persona = null;
		DatosCaducidadDni datosCaducidadDni = null;
		IntegerValidator integerValidator = IntegerValidator.getInstance();

		persona = busquedaDeMenorService.buscarMenor(buscarMenorDocumento);
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

		if(persona != null) {
			PadronNominalBaja padronNominalBaja = busquedaDeMenorService.getMotivoBaja(persona.getCodigoPadronNominal());
			padronNominalBaja.setCoPadronNominal(persona.getCodigoPadronNominal());
			model.addAttribute("coMotivoBajaItems", dominioService.getMotivoBajaList());
			model.addAttribute("coMotivoAltaItems", dominioService.getMotivoAltaList());
			model.addAttribute("padronNominalBaja", padronNominalBaja);
			if (persona.getCodigoUbigeo().equals(usuario.getCoUbigeoInei()) || persona.getCodigoUbigeo().equals("") || persona.getCodigoUbigeo() ==null) {
				model.addAttribute("ubigeoValido", "valido");
			}
		}

		if (persona != null && persona.getOrigenFoto() != null && persona.getOrigenFoto().equals(Persona.OrigenFoto.ANI)) {
			if (persona.getDni() != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(persona.getDni().getBytes())));
			}
		}
		return "mantenimiento/padronn-form-resultado";
	}

	@RequestMapping("/registromanual/padronnbaja.do")
	public String doPadronBaja(
			@Valid @ModelAttribute PadronNominalBaja padronNominalBaja, BindingResult bindingResult,
			Model model	) {

		if(bindingResult.hasErrors()) {
			model.addAttribute("coMotivoBajaItems", dominioService.getMotivoBajaList());
			return "mantenimiento/padronn-form-resultado-espadron-dardebaja";
		} else {
			boolean realizado = padronService.darBajaPadron(padronNominalBaja);
			model.addAttribute("baja", realizado);
			model.addAttribute("coPadronNominal", padronNominalBaja.getCoPadronNominal());
			model.addAttribute("padronNominalBaja", null);
			return "mantenimiento/padronn-form-resultado-espadron-dardebaja";
		}
	}

	@RequestMapping("/registromanual/padronnalta.do")
	public String doPadronAlta(
			@Valid @ModelAttribute PadronNominalBaja padronNominalBaja, BindingResult bindingResult,
			Model model) {

		//log.debug("padronNominalBaja="+padronNominalBaja);
		//log.debug("bindingResult="+bindingResult);
		if(bindingResult.hasErrors()) {
			model.addAttribute("coMotivoAltaItems", dominioService.getMotivoAltaList());
			return "mantenimiento/padronn-form-resultado-espadron-dardealta";

		} else {
			boolean realizado = padronService.darAltaPadron(padronNominalBaja);
			model.addAttribute("alta", realizado);
			model.addAttribute("coPadronNominal", padronNominalBaja.getCoPadronNominal());
			model.addAttribute("padronNominalBaja", null);
			return "mantenimiento/padronn-form-resultado-espadron-dardealta";
		}
	}


    @RequestMapping("/signup/form_grupo.do")
    public String formGrupos(@ModelAttribute Grupo grupo, Model model){
        model.addAttribute("action", "signup/inserta_grupo.do");

        return "mantenimiento/form-grupo";
    }

    @RequestMapping("/signup/inserta_grupo.do")
    public String insertaGrupo(@Valid @ModelAttribute Grupo grupo, BindingResult result, Model model){
        if(result.hasErrors()){
            return "mantenimiento/form-grupo";
        }
        else{
            if(grupoService.insert(grupo)) {
                model.addAttribute("success", true);
                model.addAttribute("mensaje", "Grupo Agregado correctamente.");
                return "redirect:/signup/lista_grupos.do";
            } else{
                model.addAttribute("success", false);
                model.addAttribute("mensaje", "Error agregando el grupo");
                return "mantenimiento/form-grupo";
            }
        }
    }

    @RequestMapping("/signup/update_grupo.do")
    public String updateGrupo(@Valid @ModelAttribute Grupo grupo, BindingResult result, Model model){
        if(result.hasErrors()){
            return "mantenimiento/form-grupo";
        }
        else{
            if(grupoService.update(grupo)) {
                model.addAttribute("success", true);
                model.addAttribute("mensaje", "Rol actualizado correctamente.");
                return "redirect:/signup/lista_grupos.do";
            } else{
                model.addAttribute("success", false);
                model.addAttribute("mensaje", "Error actualizando el grupo");
                return "mantenimiento/form-grupo";
            }
        }
    }

    @RequestMapping("/signup/lista_grupos.do")
    public String listaGrupos(Model model) {
        model.addAttribute("grupos", grupoService.getAll());
        return "mantenimiento/lista-grupos";
    }

    @RequestMapping("/signup/get_grupo_update.do")
    public String getGrupo(@RequestParam(value = "coGrupo") String coGrupo, Model model) {
        Grupo grupo = grupoService.getGrupo(coGrupo);
        model.addAttribute("grupo", grupo);
        model.addAttribute("coGrupo", grupo.getCoGrupo());
        model.addAttribute("action", "signup/update_grupo.do");
        return "mantenimiento/form-grupo";
    }

    @RequestMapping("/signup/lista_permisos.do")
    public String listaPermisos(Model model){
        model.addAttribute("grupoPermisos", grupoPermisoService.getAll());
        return "mantenimiento/lista-grupo-permisos";
    }

    @RequestMapping("/signup/form_grupo_permiso.do")
    public String addGrupoPermiso(@ModelAttribute GrupoPermiso grupoPermiso, Model model){
        model.addAttribute("action", "signup/insert_grupo_permiso.do");
        model.addAttribute("grupos", grupoPermisoService.getGrupos());
        return "mantenimiento/form-grupo-permiso";
    }

    @RequestMapping("/signup/insert_grupo_permiso.do")
    public String insertGrupoPermiso(@Valid @ModelAttribute GrupoPermiso grupoPermiso, BindingResult result, Model model){
        if(result.hasErrors()){
            return "mantenimiento/form-grupo-permiso";
        }
        else if(grupoPermisoService.insert(grupoPermiso)){
            model.addAttribute("mensaje", "Permiso agregado correctamente");
            model.addAttribute("success", "true");
            return "forward:/signup/lista_permisos.do";
        }
        else{
            model.addAttribute("mensaje", "Permiso no agregado");
            model.addAttribute("grupos", grupoPermisoService.getGrupos());
            return "mantenimiento/form-grupo-permiso";
        }
    }

    @RequestMapping("/signup/edit_grupo_permiso.do")
    public String editGrupoPermiso(
            @ModelAttribute GrupoPermiso grupoPermiso,
            @RequestParam(value = "coGrupo", required = true) String coGrupo,
            @RequestParam(value = "dePermiso", required = true) String dePermiso,
            Model model){

//        GrupoPermiso grupoPermiso1 = grupoPermisoService.getGrupoPermiso(coGrupo, dePermiso);
        model.addAttribute("action", "signup/update_grupo_permiso.do");
        model.addAttribute("grupoPermiso", grupoPermisoService.getGrupoPermiso(coGrupo, dePermiso));
        model.addAttribute("grupos", grupoPermisoService.getGrupos());
        return "mantenimiento/form-grupo-permiso";
    }

    @RequestMapping("/signup/update_grupo_permiso.do")
    public String updateGrupoPermiso(@Valid @ModelAttribute GrupoPermiso grupoPermiso, BindingResult result, Model model){
        if(result.hasErrors())
            return "mantenimiento/form-grupo-permiso";
        else if(grupoPermisoService.update(grupoPermiso)){
            model.addAttribute("mensaje", "Permiso actualizado correctamente");
            model.addAttribute("success", "true");
            return "forward:/signup/lista_permisos.do";
        }
        else{
            model.addAttribute("mensaje", "Permiso no se puede editar");
            model.addAttribute("grupos", grupoPermisoService.getGrupos());
            return "mantenimiento/form-grupo-permiso";
        }
    }

    @RequestMapping(value = "/signup/reset_password.do", method = RequestMethod.GET)
    public String resetPassword(
            @RequestParam(value = "nuDni", required = true) String nuDni,
            Model model){
        if(usuarioExternoService.resetPassword(nuDni, usuario.getLogin())) {
            model.addAttribute("mensaje", "Se reseteo la clave; <strong>la clave es igual que el DNI</strong>" );
            model.addAttribute("usuario", usuarioExternoService.getUsuarioExterno(nuDni));
            model.addAttribute("success", true);
        }
        else
            model.addAttribute("mensaje", "No se puede resetear la contraseña del usuario: " + nuDni);
        return "mantenimiento/resetPassword";
    }

    @RequestMapping(value = "/cambio_password/index.do")
    public String cambioPassword(@ModelAttribute CambioCredencial cambioCredencial, Model model){
        cambioCredencial.setFullName(usuario.getFullName());
        cambioCredencial.setCoUsuario(usuario.getLogin());
        return "mantenimiento/usuario-cambio-password";
    }

    @RequestMapping(value = "/cambio_password/set_password.do")
    public String setPassword(@Valid @ModelAttribute CambioCredencial cambioCredencial, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("");
            return "mantenimiento/usuario-cambio-password";
        }
        else {
        	cambioCredencial.setCoTipoAccionPw("03");
        	cambioCredencial.setUsModiPw(usuario.getLogin());
            usuarioExternoService.updateUsuarioPassword(cambioCredencial);
            model.addAttribute("success", true);
            model.addAttribute("loginUrl","logout.do");
            return "mantenimiento/usuario-cambio-password";
        }
    }

    @RequestMapping(value = "/signup/reporte_usuarios_xls.do", method = RequestMethod.GET)
    public String reporteUsuariosXls(Model model) {
        String filename = "Reporte_usuarios";
        filename = filename + ".xls";
        model.addAttribute("nombreArchivo", filename);

        List<UsuarioExterno> usuarios = usuarioExternoService.getAllDetails();

        logger.debug("usuarios::" + usuarios);
        model.addAttribute("usuarios", usuarios);
        return "reporteUsuarios.xls";
    }


}