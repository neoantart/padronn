package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Menor;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.form.ListaMenores;

import java.util.List;

/**
 * Created by jfloresh on 23/05/2014.
 */
public interface GriasDao {
    List<PadronNominal> listarPadron(ListaMenores listaMenores, String esPadron);

    List<PadronNominal> listarPadron(ListaMenores listaMenores, int filaIni, int filaFin, String esPadron);

    Integer countRowsListarPadron(ListaMenores listaMenores, String esPadron);

    List<Menor> buscarMenorGrias(String apPrimerMenor, String apSegundoMenor, String prenombresMenor, String nuDoc, String tiDoc, int filaIni, int filaFin);

    int countBuscarMenorGrias(String apPrimerMenor, String apSegundoMenor, String prenombresMenor, String nuDoc, String tiDoc);
}
