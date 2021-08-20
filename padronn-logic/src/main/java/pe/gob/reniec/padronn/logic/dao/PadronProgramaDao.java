package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.PadronPrograma;

import java.util.List;

/**
 * Clase PadronProgramaDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 24/05/13 09:54 AM
 */
public interface PadronProgramaDao {
	void guardarPadronPrograma(PadronPrograma padronPrograma);
	void guardarPadronProgramaHistorico(PadronPrograma padronPrograma);
	List<PadronPrograma> listarPadronPrograma(String coPadronNominal);
	List<PadronPrograma> listarPadronProgramaHistorico(String coPadronNominal, String nuSec);
	List<String> listarCoPadronPrograma(String coPadronNominal);
	void clearPadronPrograma(String coPadronNominal);
}
