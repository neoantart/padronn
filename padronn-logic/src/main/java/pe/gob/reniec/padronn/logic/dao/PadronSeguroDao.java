package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.PadronSeguro;

import java.util.List;

/**
 * Clase PadronSeguroDao.
 * @author aquispej
 * @date 20/12/18 09:54 AM
 */

public interface PadronSeguroDao {
    void guardarPadronSeguro(PadronSeguro padronSeguro);
    void guardarPadronSeguroHistorico(PadronSeguro padronSeguro);
    List<PadronSeguro> listarPadronSeguro(String coPadronNominal);
    List<PadronSeguro> listarPadronSeguroHistorico(String coPadronNominal, String nuSec);
    List<String> listarCoPadronSeguro(String coPadronNominal);
    void clearPadronSeguro(String coPadronNominal);
}
