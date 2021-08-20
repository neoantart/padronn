package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.PadronObservado;
import pe.gob.reniec.padronn.logic.model.TipoObservacion;

import java.util.List;

/**
 * Created by jfloresh on 25/04/2016.
 */
public interface PadronObservadoService {
    List<PadronObservado> obtenerPadronObservados(String coUbigeoInei);

    List<PadronObservado> obtenerPadronObservados(String coUbigeoInei, Integer filaIni, Integer filaFin);

    Integer contarPadronObservado(String coUbigeoInei);

    List<TipoObservacion> obtenerTipoObservacion();

    List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion, Integer filaIni, Integer filaFin);

    List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion);

    Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion);

    String obtenerDeTipoObservacion(String coTipoObservacion);

    PadronObservado obtenerPadronObservado(String coPadronNominal, String coTipoObservacion);

    void setEsObservado(String esObservacion, String coPadronNominal, String coTipoObservacion);

    List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                                      String nuCnv, String coDniMadre, String apPrimer, String apSegundo, String prenombres,
                                                                      Integer filaIni, Integer filaFin);

    Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                       String nuCnv, String coDniMadre, String apPrimer, String apSegundo, String prenombres);

    Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                       String nuDniMenor, String apPrimer, String apSegundo, String prenombres);

    List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                                      String nuDniMenor, String apPrimer, String apSegundo, String prenombres,
                                                                      Integer filaIni, Integer filaFin);
}