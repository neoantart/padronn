package pe.gob.reniec.padronn.logic.service;

import java.util.List;

/**
 * Clase KeysService.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 21/06/13 04:48 PM
 */
public interface KeysService {
	List<String> getTiProSocialKeys();

	List<String> getCoLenMadreKeys();

	List<String> getCoGraInstMadreKeys();

	List<String> getTiVinculoMadreKeys();

	List<String> getTiVinculoJefeKeys();

	List<String> getTiSeguroMenorKeys();

	List<String> getTiDocMenorKeys();

	List<String> getCoGeneroMenorKeys();
}
