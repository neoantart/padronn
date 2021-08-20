package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.AniDao;
import pe.gob.reniec.padronn.logic.model.DatosCaducidadDni;
import pe.gob.reniec.padronn.logic.model.Persona;
import pe.gob.reniec.padronn.logic.service.AniService;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 14/05/13
 * Time: 12:20 PM
 */
@Service
public class AniServiceImpl implements AniService {

	@Autowired
	AniDao aniDao;

	@Override
	public Persona obtenerPorDniEnPersona(String dni, Persona.TipoPersona tipoPersona) {
		return aniDao.obtenerPorDniEnPersona(dni, tipoPersona);
	}

	@Override
	public List<Persona> listarPorDatosEnPersona(String apPrimer, String apSegundo, String prenombre, Persona.TipoPersona tipoPersonaBusqueda, int filaInicio, int filaFinal) {
		return aniDao.listarPorDatosEnPersona(apPrimer, apSegundo, prenombre, tipoPersonaBusqueda, filaInicio, filaFinal);
	}

	@Override
	public int contarRegistrosPorDatos(String apPrimer, String apSegundo, String prenombre, Persona.TipoPersona tipoPersona) {
		return aniDao.contarRegistrosPorDatos(apPrimer, apSegundo, prenombre, tipoPersona);
	}

	@Override
	public DatosCaducidadDni obtenerDatosCaducidadDni(String dni) {
		return aniDao.obtenerDatosCaducidadDni(dni);
	}

	@Override
	public String obtenerGeneroPorDniPersona(String coDniJefeFam) {
		return aniDao.obtenerGeneroPorDniPersona(coDniJefeFam);
	}


	/*@Override
	public PersonaAni buscarPersonaPorDni(String dni) {
		return aniDao.buscarPersonaPorDni(dni);
	}*/

	/*@Override
	public List<PersonaAni> buscarPersonaPorDatos(String dni, String paterno, String materno, String nombres, int filaInicio, int filaFinal) {
		return aniDao.buscarPersonaPorDatos(dni, paterno, materno, nombres, filaInicio, filaFinal);
	}*/

	/*@Override
	public List<PersonaAni> buscarMadrePorDatos(String dni, String paterno, String materno, String nombres, int filaInicio, int filaFinal) {
		return aniDao.buscarMadrePorDatos(dni, paterno, materno, nombres, filaInicio, filaFinal);
	}*/

}
