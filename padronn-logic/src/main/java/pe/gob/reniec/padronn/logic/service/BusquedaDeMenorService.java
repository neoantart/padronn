package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.PadronNominalBaja;
import pe.gob.reniec.padronn.logic.model.Persona;
import pe.gob.reniec.padronn.logic.model.PersonaBusqueda;
import pe.gob.reniec.padronn.logic.model.form.BuscarMenorDocumento;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 22/05/13
 * Time: 07:18 PM
 */
public interface BusquedaDeMenorService {

	int countListarMenoresPorDatos(String paterno, String materno, String nombres);

	List<Persona> listarMenoresPorDatos(String paterno, String materno, String nombres, int filaInicio, int filaFinal);

	List<Persona> listarMenoresPorDniMadre(String dni);

	int contarMenoresPorDatos(String paterno, String materno, String nombres);

	Persona buscarMenorPorDni(String dni);

	Persona buscarMenor(BuscarMenorDocumento buscarMenorDocumento);

	List<Persona> buscarNombresSimilares(String paterno, String materno, String nombres);

	PadronNominalBaja getMotivoBaja(String codigoPadronNominal);
}
