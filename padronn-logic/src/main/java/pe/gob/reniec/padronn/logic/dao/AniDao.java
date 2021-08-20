package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.DatosCaducidadDni;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.Persona;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 14/05/13
 * Time: 12:14 PM
 */
public interface AniDao {

	Persona obtenerPorDniEnPersona(String dni, Persona.TipoPersona tipoPersona);


	String registrarSessionRUIP(String nuDni, String nuDniUser, String appName);

	String obtenerDescripcionUbigeoReniec(String coUbigeoReniec);

	List<Persona> listarPorDatosEnPersona(String apPrimer, String apSegundo, String prenombre, Persona.TipoPersona tipoPersona, int filaInicio, int filaFinal);

	List<Persona> listarPorDniMadre(String dni, Persona.TipoPersona tipoPersona);

	List<Persona> listarPorDniMadre(PadronNominal padronNominal, Persona.TipoPersona tipoPersona);

	int contarRegistrosPorDatos(String apPrimer, String apSegundo, String prenombre, Persona.TipoPersona tipoPersona);

	DatosCaducidadDni obtenerDatosCaducidadDni(String dni);

	List<Persona> listarNombresSimilares(String paterno, String materno, String nombres);

    String obtenerDireccion(String dni);

    String obtenerGeneroPorDniPersona(String coDniJefeFam);
}
