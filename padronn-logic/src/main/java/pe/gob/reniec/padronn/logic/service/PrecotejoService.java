package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.Precotejo;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistroDetalle;

import java.util.HashMap;
import java.util.List;

/**
 * Archivo PrecotejoService.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 03:57 PM
 */
public interface PrecotejoService {
	List getAll();
	List getAll(String coEntidad, String esEnvio);
	List getAll(String nuEnvio, String feInicio, String feFin);
	List getAll(String coEntidad, String nuEnvio, String feInicio, String feFin);
	List getAllFromPrecotejo(String coEntidad, String nuEnvio);
	List<HashMap<String, Object>> getAllFromPrecotejoObs(String coEntidad, String nuEnvio);

	//void insert(Precotejo precotejo, List<PrecotejoRegistro> precotejoRegistroList);

	void waitTask();

	List getAllFromCotejoObs(String coEntidad, String nuEnvio);

	List getAllFromCotejo(String coEntidad, String nuEnvio);

	//List getAllFromPrecotejoCotejo(String coEntidad, String nuEnvio, int numeroRegistroInicial, int numeroRegistroFinal);
	List getAllFromPrecotejoCotejo(String coEntidad, String nuEnvio, int numeroRegistroInicial, int numeroRegistroFinal, PrecotejoFiltroTipoRegistro filtroTipoRegistro);

	int countAllFromPrecotejoCotejo(String coEntidad, String nuEnvio, PrecotejoFiltroTipoRegistro filtroTipoRegistro);

	List<PrecotejoRegistro> getAllFromCotejoObsDetails(String coEntidad, String nuEnvio);

	List<PrecotejoRegistro> getAllFromPrecotejoObsDetails(String coEntidad, String nuEnvio);

	Precotejo getPrecotejoDetails(String coEntidad, String nuEnvio);

    PrecotejoRegistroDetalle getPrecotejoRegistroDetalle(String coEntidad, String nuEnvio, String nuRegistro);

	/*
	int countAllFromCotejo(String coEntidad, String nuEnvio);

	int countAllFromPrecotejo(String coEntidad, String nuEnvio);
	*/

	List getAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin, int nuRegistroInicial, int nuRegistroFinal);

	int countAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin);

  // Este método no debe ser transaccional, porque el nuenvio debe quedar registrado antes de que se registren los registros, inclusive.
  void insert(Precotejo precotejo, List<PrecotejoRegistro> precotejoRegistroList, boolean minimalConstraintsCheck);

	List getAllObs(String coEntidad, String nuEnvio);

    void setEstadoPrecotejo(String esEnvio, String nuEnvio);
}
