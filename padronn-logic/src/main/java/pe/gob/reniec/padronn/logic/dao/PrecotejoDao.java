package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Precotejo;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistroDetalle;

import java.util.HashMap;
import java.util.List;

/**
 * Archivo PrecotejoDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 04:34 PM
 */
public interface PrecotejoDao {
	void insert(Precotejo precotejo);
	void insert(PrecotejoRegistro precotejoRegistro);
	void insertObs(PrecotejoRegistro precotejoRegistro);
	Number getNextNuEnvio(String coEntidad);


	List getAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin);
	void update(Precotejo precotejo);

	List getAllFromPrecotejo(String coEntidad, String nuEnvio);
	List getAllFromPrecotejoObs(String coEntidad, String nuEnvio);

	List getAllFromCotejoObs(String coEntidad, String nuEnvio);
	List getAllFromCotejo(String coEntidad, String nuEnvio);

	//List getAllFromPrecotejoCotejo(String coEntidad, String nuEnvio, int numeroRegistroInicial, int numeroRegistroFinal);
	List getAllFromPrecotejoCotejo(String coEntidad, String nuEnvio, int nuRegistroInicial, int nuRegistroFinal, String esRegistro, String esRegistroObs);

	List<PrecotejoRegistro> getAllObsDetails(String coEntidad, String nuEnvio, String esRegistro);

	Precotejo getPrecotejoDetails(String coEntidad, String nuEnvio);

    PrecotejoRegistroDetalle getPrecotejoRegistroDetalle(String coEntidad, String nuEnvio, String nuRegistro);

	void callCotejo();

	int countAllFromRegistroObs(String coEntidad, String nuEnvio, String esRegistroObs);

	int countAllFromRegistro(String coEntidad, String nuEnvio, String esRegistro);

	List getAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin, int nuRegistroInicial, int nuRegistroFinal);

	int countAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin);

	List getAllObs(String coEntidad, String nuEnvio);

	// Agregado jfloresh: 23/02/2016
	void cotejarPrecarga(Number coEntidad, Number nuEnvio);

	void setEstadoPrecotejo(String esEnvio, String nuEnvio);
}
