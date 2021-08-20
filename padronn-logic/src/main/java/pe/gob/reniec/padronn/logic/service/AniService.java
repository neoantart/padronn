package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.DatosCaducidadDni;
import pe.gob.reniec.padronn.logic.model.Persona;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 14/05/13
 * Time: 12:19 PM
 */
public interface AniService {

	Persona obtenerPorDniEnPersona(String dni, Persona.TipoPersona tipoPersona);

	List<Persona> listarPorDatosEnPersona(String apPrimer, String apSegundo, String prenombre, Persona.TipoPersona tipoPersonaBusqueda, int filaInicio, int filaFinal);

	int contarRegistrosPorDatos(String apPrimer, String apSegundo, String prenombre, Persona.TipoPersona tipoPersona);

	public DatosCaducidadDni obtenerDatosCaducidadDni(String dni);

    String obtenerGeneroPorDniPersona(String coDniJefeFam);


    //PersonaAni buscarPersonaPorDni(String dni);

	//List<PersonaAni> buscarPersonaPorDatos(String dni, String paterno, String materno, String nombres, int filaInicio, int filaFinal);

	//List<PersonaAni> buscarMadrePorDatos(String dni, String paterno, String materno, String nombres, int filaInicio, int filaFinal);

}
