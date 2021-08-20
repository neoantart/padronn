package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.form.ListaMenores;

import java.util.List;

/**
 * Created by jfloresh on 26/05/2014.
 */

public interface GriasService {
    public List<PadronNominal> listarPadronActivos(ListaMenores listaMenores);

    List<PadronNominal> listarPadronObservados(ListaMenores listaMenores);

    List<PadronNominal> listarPadronTodos(ListaMenores listaMenores);

    public List<PadronNominal> listarPadronActivos(ListaMenores listaMenores, int filaIni, int filaFin);

    List<PadronNominal> listarPadronObservados(ListaMenores listaMenores, int filaIni, int filaFin);

    List<PadronNominal> listarPadronTodos(ListaMenores listaMenores, int filaIni, int filaFin);

    Integer countRowsListarPadronActivos(ListaMenores listaMenores);

    Integer countRowsListarPadronObservados(ListaMenores listaMenores);

    Integer countRowsListarPadronTodos(ListaMenores listaMenores);
}
