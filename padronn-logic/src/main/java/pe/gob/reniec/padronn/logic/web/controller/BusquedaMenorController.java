package pe.gob.reniec.padronn.logic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.reniec.padronn.logic.dao.ConfiguracionAplicacionDao;
import pe.gob.reniec.padronn.logic.dao.PadronDao;
import pe.gob.reniec.padronn.logic.model.Persona;
import pe.gob.reniec.padronn.logic.service.AniService;
import pe.gob.reniec.padronn.logic.service.BusquedaDeMenorService;
import pe.gob.reniec.padronn.logic.service.PadronService;
import pe.gob.reniec.padronn.logic.util.ImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import org.apache.xerces.impl.dv.util.Base64;
import java.util.List;


@Controller
@RequestMapping("busquedademenor")
public class BusquedaMenorController {

	private static final String REG_DNI_VALID = "\\d{8}";
	private static final String REG_AP_PRIMER = "[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ0123456789\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-\' \\.]{0,40}";
	private static final String REG_AP_SEGUNDO = "[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ0123456789\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-\' \\.]{0,40}";;
	private static final String REG_PRENOMBRE = "[ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ0123456789\\dÄ-ÜÁ-ÚÀ-ÙÂ-ÛÃÕÑä-üá-úà-ùâ-ûãõñ\\-\' \\.]{0,60}";;

	@Autowired
	PadronService padronService;

	@Autowired
	PadronDao padronDao;

	@Autowired
	BusquedaDeMenorService busquedaDeMenorService;

	@Autowired
	AniService aniService;
	Logger logger = Logger.getLogger(getClass());


	@Autowired
	PadronProperties padronProperties;

	@Autowired
	private ImagenCiudadano imagenCiudadano;
	/* Formularios de búsqueda */

	@RequestMapping(value = "padronnominalform.do")
	public String padronNominalForm(Model model){
		return "busquedaDeMenor/padronNominalForm";
	}

	@RequestMapping(value="avanzadoform.do")
	public String busquedaAvanzada(@RequestParam(value = "tipo", required = false) String tipo, Model model){
		if(tipo!=null&&tipo.equals("madre")){
			model.addAttribute("tabmadre", "active");
			model.addAttribute("tabmenor", "");
		}else{
			model.addAttribute("tabmadre", "");
			model.addAttribute("tabmenor", "active");
		}
		return "busquedaDeMenor/avanzadoForm";
	}

	private int obtenerPaginaActual(String paginaIndicada){
		int paginaActual;
		if(paginaIndicada==null||paginaIndicada.isEmpty()){
			paginaActual=1;
		}else{
			try{
				paginaActual=Integer.parseInt(paginaIndicada);
				if(paginaActual<1){
					paginaActual=1;
				}
			}catch (NumberFormatException ne){
				paginaActual=1;
			}
		}
		return paginaActual;
	}

	/* Búsqueda del menor en la base de datos de RENIEC y del Padrón Nominal */
	@RequestMapping("buscaravanzadamenor.do")
	public String buscarAvanzadaMenor(@RequestParam(value = "apPrimerMenor", required=false) String apPrimerMenor,
									  @RequestParam(value = "apSegundoMenor", required=false) String apSegundoMenor,
									  @RequestParam(value = "prenombreMenor", required=false) String prenombreMenor,
									  @RequestParam(value = "nuPagina", required=false) String pagina,
									  Model model) {
		apPrimerMenor = apPrimerMenor.trim();
		apSegundoMenor = apSegundoMenor.trim();
		prenombreMenor = prenombreMenor.trim();
		boolean minimoCampos = false;
		if(apPrimerMenor.length() > 0 && apSegundoMenor.length() > 0)
			minimoCampos = true;
		if(apPrimerMenor.length() > 0 && prenombreMenor.length() > 0)
			minimoCampos = true;
		if(apSegundoMenor.length() > 0 && prenombreMenor.length() > 0)
			minimoCampos = true;
		if(apSegundoMenor.length() > 0 && prenombreMenor.length() > 0 && prenombreMenor.length() > 0)
			minimoCampos = true;

		if(minimoCampos) {
			if (apPrimerMenor.matches(REG_AP_PRIMER) && apSegundoMenor.matches(REG_AP_SEGUNDO) && prenombreMenor.matches(REG_PRENOMBRE)) {
				Integer nuPagina;
				nuPagina = (pagina == null || Integer.parseInt(pagina) <= 0) ? 1 : Integer.parseInt(pagina);
				int filaInicio = ConfiguracionAplicacionDao.CantidadFilasTabla * (nuPagina - 1) + 1;
				int filaFin = ConfiguracionAplicacionDao.CantidadFilasTabla * nuPagina;
				// todo aun falta calcular la cantidad exacta de registro

				Integer filas = busquedaDeMenorService.countListarMenoresPorDatos(apPrimerMenor, apSegundoMenor, prenombreMenor);
				List<Persona> personaList = busquedaDeMenorService.listarMenoresPorDatos(apPrimerMenor, apSegundoMenor, prenombreMenor, (nuPagina - 1) * padronProperties.PAGINA_FILAS + 1, nuPagina * padronProperties.PAGINA_FILAS);

				filas = Math.abs(filas - (personaList.size() - padronProperties.PAGINA_FILAS));
				model.addAttribute("nuPagina", nuPagina);
				model.addAttribute("nuPaginas", Math.floor(filas / padronProperties.PAGINA_FILAS) + (filas % padronProperties.PAGINA_FILAS == 0 ? 0 : 1));
				model.addAttribute("apPrimerMenor", apPrimerMenor);
				model.addAttribute("apSegundoMenor", apSegundoMenor);
				model.addAttribute("prenombreMenor", prenombreMenor);
				model.addAttribute("personaList", personaList);
				model.addAttribute("cantidadFilas", ConfiguracionAplicacionDao.CantidadFilasTabla);
				model.addAttribute("cantidadResultados", filas);
				model.addAttribute("paginaActual", nuPagina);
			}
		} else {
			model.addAttribute("camposInsuficientes", "Campos insuficientes");
		}
		return "busquedaDeMenor/avanzadaListMenor";
	}

	@RequestMapping("informacionadicionalmenor.do")
	public String informacionAdicionalMenor(@RequestParam(value = "nuDniMenor", required=false) String nuDniMenor,
											@RequestParam(value = "coPadronNominal", required=false) String coPadronNominal,
											Model model) {
		if(coPadronNominal != null && !coPadronNominal.isEmpty()) { //busqueda por padron nominal
			/*Persona persona = padronService.obtenerPorDniEnPersona(coPadronNominal);*/
			Persona persona =  padronDao.obtenerPorCoPadronNominalEnPersona(coPadronNominal);

			if (persona != null && persona.getDni() != null) {
				model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(persona.getDni().getBytes())));
			}
			model.addAttribute("persona", persona);
		} else if(nuDniMenor!=null&&!nuDniMenor.isEmpty()) { //busqued en el ANI con numero de DNI
			model.addAttribute("img", imagenCiudadano.obtenerImagen(Base64.encode(nuDniMenor.getBytes())));
			model.addAttribute("persona", aniService.obtenerPorDniEnPersona(nuDniMenor, Persona.TipoPersona.MENOR));
		}
		return "busquedaDeMenor/informacionAdicionalMenor";
	}

	/* Búsqueda del menor a través de los datos de la madre */
	@RequestMapping("buscaravanzadamadre.do")
	public String buscarAvanzadaMadre(@RequestParam(value = "dni", required=false) String dni,
									  @RequestParam(value = "apPrimer", required=false) String apPrimer,
									  @RequestParam(value = "apSegundo", required=false) String apSegundo,
									  @RequestParam(value = "prenombre", required=false) String prenombre,
									  @RequestParam(value = "nuPagina", required=false) String pagina,
									  Model model) {
		// validacion
		boolean minimoCampos = false;
		if(apPrimer.length() > 0 && apSegundo.length() > 0)
			minimoCampos = true;
		if(apPrimer.length() > 0 && prenombre.length() > 0)
			minimoCampos = true;
		if(apSegundo.length() > 0 && prenombre.length() > 0)
			minimoCampos = true;
		if(apPrimer.length() > 0 && apSegundo.length() > 0 && prenombre.length() > 0)
			minimoCampos = true;
		if(dni.length() > 0)
			minimoCampos = true;

		if(minimoCampos) {
			int paginaActual = obtenerPaginaActual(pagina);
			int filaInicio	 = ConfiguracionAplicacionDao.CantidadFilasTabla*(paginaActual-1)+1;
			int filaFin		 = ConfiguracionAplicacionDao.CantidadFilasTabla*paginaActual;

			if(dni!=null && !dni.isEmpty()) { //si el dni no esta vacio (realiza la busqueda por DNI)
				Persona persona = null;
				Persona persona2 = null;
				if (dni.matches(REG_DNI_VALID)) {
					persona = aniService.obtenerPorDniEnPersona(dni, Persona.TipoPersona.MADRE);
					List<Persona> personaList = busquedaDeMenorService.listarMenoresPorDniMadre(dni);
					model.addAttribute("persona", persona);
					model.addAttribute("hijosRegistrados", personaList);

					if(persona == null) {
						persona2 = aniService.obtenerPorDniEnPersona(dni, Persona.TipoPersona.MENOR);
						if (persona2 != null) {
							model.addAttribute("mensajeMenor", "El DNI " + dni + " corresponde a un menor de edad");
						} else {
							model.addAttribute("mensaje", "El DNI " + dni + " no es válido o no pertenece a ninguna persona");
						}
					}
					return "busquedaDeMenor/avanzadaListMadrePorDni";
				} else {
					return "busquedaDeMenor/avanzadaListMadreVacio";
				}


			} else if( (apPrimer!=null&&!apPrimer.isEmpty()||  //realiza la busqueda por nombres y apellidos
					apSegundo!=null&&!apSegundo.isEmpty()||
					prenombre!=null&&!prenombre.isEmpty()) &&
					apPrimer.matches(REG_AP_PRIMER) && apSegundo.matches(REG_AP_SEGUNDO) && prenombre.matches(REG_PRENOMBRE)
					) {
				Integer filas = aniService.contarRegistrosPorDatos(apPrimer.trim(), apSegundo.trim(), prenombre.trim(), Persona.TipoPersona.MADRE);
				model.addAttribute("nuPagina", paginaActual);
				model.addAttribute("nuPaginas", Math.floor(filas / ConfiguracionAplicacionDao.CantidadFilasTabla) + (filas % ConfiguracionAplicacionDao.CantidadFilasTabla == 0 ? 0 : 1));
				model.addAttribute("apPrimer", apPrimer);
				model.addAttribute("apSegundo", apSegundo);
				model.addAttribute("prenombre", prenombre);

				List<Persona> personaList = aniService.listarPorDatosEnPersona(apPrimer, apSegundo, prenombre, Persona.TipoPersona.MADRE, filaInicio, filaFin);
				model.addAttribute("personaList", personaList);
				model.addAttribute("cantidadResultados", filas);
				return "busquedaDeMenor/avanzadaListMadre";
			} else {
				return "busquedaDeMenor/avanzadaListMadreVacio";
			}
		}
		else {
			return "busquedaDeMenor/avanzadaListMadreMinimoCampos";
		}
	}


	/*por verificar*/
	@RequestMapping ("informacionadicionalmadre.do")
	public String informacionAdicionalMadre(@RequestParam(value = "nuDni", required = false) String nuDni, Model model) {
		if(nuDni!=null && nuDni.length()==8) { //verificamos q el numero de DNI contenga 8 caracteres y que no sea nulo
			model.addAttribute("persona", aniService.obtenerPorDniEnPersona(nuDni, Persona.TipoPersona.MAYOR));
			model.addAttribute("hijosRegistrados", busquedaDeMenorService.listarMenoresPorDniMadre(nuDni));
		}else{
			model.addAttribute("personaAni", null);
		}
		return "busquedaDeMenor/informacionAdicionalMadre";
	}

}
