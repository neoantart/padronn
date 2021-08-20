package pe.gob.reniec.padronn.logic.service;

import org.springframework.dao.DataIntegrityViolationException;
import pe.gob.reniec.padronn.logic.model.PadronImg;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.PadronNominalBaja;
import pe.gob.reniec.padronn.logic.model.Persona;

import java.sql.SQLException;
import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * victordino.fb@gmail.com
 * Date: 30/04/13
 * Time: 04:39 PM
 */
public interface PadronService {

	Persona obtenerPorDniEnPersona(String dni);

	PadronNominal obtenerPorCodigoPadron(String codigoPadronNominal);

	PadronNominal obtenerPorCodigoPadronConDetalles(String codigoPadronNominal);

	Persona obtenerPersonaPorCoPadron(String coPadronNominal);

	boolean isPrecarga(String padronNominal);

	boolean isPrecargaByDni(String nuDni);

	//List<PadronNominal> listarPadrones();

	//List<PadronNominal> listarPadrones(int filaInicio, int filaFinal, PadronNominal.Campo campo);

	//List<PadronNominal> buscarPadrones(String nuDniMenor, String apPrimerMenor, String apSegundoMenor, String prenombreMenor, int filaInicio, int filaFinal, PadronNominal.Campo campo);

	//public int numeroRegistros();

	public int numeroRegistrosPorBusqueda(String nuDniMenor, String apPrimerMenor, String apSegundoMenor, String prenombreMenor);

	boolean guardarPadronNominal(PadronNominal padronNominal, PadronImg padronImg) throws DataIntegrityViolationException;

	void actualizarEstadoPadron(PadronNominal padronNominal);

	boolean darBajaPadron(PadronNominalBaja padronNominalBaja);

	boolean darAltaPadron(PadronNominalBaja padronNominalBaja);

    Integer getCoPadronNominalByDni(String nuDniMenor);

    Integer getCoPadronNominalByCui(String nuCui);

    boolean existeNuDniMenor(String nuDniMenor);

    boolean existeNuCui(String nuCui);

    List<PadronNominal> buscarDuplicados(PadronNominal padronNominal, String esPadron);


	List<Persona> listarMenoresPorDniMadre(PadronNominal padronNominal);

	void insertarPadronHis(String coPadronNominal);

	//PadronNominal buscarPersonaPorDni(String dni);
	String obtenerDeEstSalud(String coEstSalud, String nuSecEstSalud);


	List<Persona> listarMenoresPorDatosMadre(PadronNominal padronNominal, String esPadron);

	boolean isSusalud(String coPadronNominal);

	String obtenerMotivoBaja(String coPadronNominal);

	String obtenerObservacionBaja(String coPadronNominal);

	List<Persona> listarMenoresPorDniMadreInactivos(PadronNominal padronNominal);

	boolean isTipoSeguro(String coPadronNominal);
}
