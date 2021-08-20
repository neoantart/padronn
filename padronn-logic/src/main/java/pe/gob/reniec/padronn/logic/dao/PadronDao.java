package pe.gob.reniec.padronn.logic.dao;

import org.springframework.dao.DataIntegrityViolationException;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.PadronNominalBaja;
import pe.gob.reniec.padronn.logic.model.Persona;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 13/05/13
 * Time: 07:14 PM
 */
public interface PadronDao {

	Persona obtenerPorDniEnPersona(String dni);

	Persona obtenerPorCoPadronNominalEnPersona(String coPadronNominal);

	Persona obtenerPorCuiEnPersona(String nuCui);

	Persona obtenerPorCnvEnPersona(String nuCnv);

	List<Persona> listarPorDatosEnPersona(String apPrimer, String apSegundo, String prenombre, int filaInicio, int filaFinal);

	List<Persona> buscarMenorePorNombres(String apPrimer, String apSegundo, String prenombre, int filaInicio, int filaFinal);


	int countBuscarMenorePorNombres(String apPrimer, String apSegundo, String prenombre);

	List<Persona> listarPorDniMadre(String dni, String esPadron);

	List<Persona> listarPorDniMadre(PadronNominal padronNominal, String esPadron);

	int contarRegistrosPorDatos(String apPrimer, String apSegundo, String prenombre);

	PadronNominal obtenerPorCodigoPadron(String codigoPadronNominal);

	PadronNominal obtenerPorCodigoPadronConDetalles(String codigoPadronNominal);

	List<Persona> listarNombresSimilares(String paterno, String materno, String nombres);

	Persona obtenerDatosPadronPorcoPadron(String coPadron);

	boolean isPrecarga(String coPadronNominal);

	boolean isPrecargaByDni(String nuDni);
	/* depurar */
	//List<PadronNominal> listarPadrones();

	//List<PadronNominal> listarPadrones(int filaInicio, int filaFinal, PadronNominal.Campo campo);

	//List<PadronNominal> buscarPadrones(String nuDniMenor, String apPrimerMenor, String apSegundoMenor, String prenombreMenor, int filaInicio, int filaFinal, PadronNominal.Campo campo);

	int numeroRegistrosPorBusqueda(String nuDniMenor, String apPrimerMenor, String apSegundoMenor, String prenombreMenor);

	//int numeroRegistros();

	Boolean existePosibleDuplicado(PadronNominal padronNominal);

	int generarNumeroSecPadronHistorico(String coPadronHistorico);

	boolean guardarPadronNominal(PadronNominal padronNominal) throws DataIntegrityViolationException;

	String getEsPadron(String coPadronNominal);

	boolean setEsPadron(String coPadronNominal, String esPadron);

	boolean setEsPadronInit(String coPadronNominal, String esPadron);

	void insertPadronNominalBaja(PadronNominalBaja padronNominalBaja);

    Integer getCoPadronNominalByDni(String nuDniMenor);

    Integer getCoPadronNominalByCui(String nuCui);

    boolean existeNuCnv(String nuCnv);

    boolean existeNuDniMenor(String nuDniMenor);

    boolean existeNuCui(String nuCui);

	boolean existeCoPadronNominal(String coPadronNominal);

	List<PadronNominal> buscarDuplicados(PadronNominal padronNominal, String esPadron);

    List<Persona> listarNombresSimilaresPrimerasOcurrencias(String apPrimerMenor, String apSegundoMenor, String prenombreMenor);

	void insertarPadronHis(String coPadronNominal);

	Persona obtenerDatosPadronPorDni(String cnv);
	void actualizarPadronNominal(Persona dataMenorRC);
	//List<Dominio> listarProgramas(String coPadronNominal);
	void setEsObservado(String esObservado, String coPadronNominal);
	String obtenerCoPadronNominal(String nuCnv);
	boolean verificaExisteCnv(String cnv);
	String obtenerCodigoPadronPorHoraYFecha(Persona personaRc);
	boolean esObservado(String esObservado , String coPadronNominal);
	String obtenerDeEstSalud(String coEstSalud, String nuSecEstSalud);

    List<Persona> listarPorDatosMadre(PadronNominal padronNominal, String esPadron);

    boolean isSusalud(String coPadronNominal);

	String obtenerMotivoBaja(String coPadronNominal);

	String obtenerObservacionBaja(String coPadronNominal);

	List<Persona> listarPorDniMadreDef(String dni);

	String getRangoFecha(String feNacMenor, int isDate);

    boolean isTipoSeguro(String coPadronNominal);

    PadronNominalBaja getMotivoBaja(String codigoPadronNominal);
}
