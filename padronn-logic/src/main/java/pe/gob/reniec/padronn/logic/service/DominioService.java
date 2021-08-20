package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.Dominio;

import java.util.List;
import java.util.Map;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 13/05/13
 * Time: 08:32 PM
 */
public interface DominioService {

	List<Dominio> nivelPobrezaItems();
	List<Dominio> tipoSeguroItems();
	List<Dominio> tipoProgramaSocialItems();
	List<Dominio> tipoGestionItems();
	List<Dominio> vinculoFamiliarItems();
	List<Dominio> gradoInstruccionItems();
	List<Dominio> lenguajeHabitualItems();
	List<Dominio> etniaItems();
	Dominio obtener(String codigoDominio, String nombreDominio);

  List<Dominio> tipoDocumentoItems();

  List<Dominio> generoItems();

	List<Dominio> getMotivoBajaList();

	List<Dominio> getMotivoAltaList();

	List<Dominio> getCoTipoSinNombre();

	List<String> getDeTipoSinNombre();

    List<Dominio> tipoEntidadesItems();
}
