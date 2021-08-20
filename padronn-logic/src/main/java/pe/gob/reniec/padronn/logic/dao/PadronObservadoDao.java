package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.PadronObservado;

import java.util.List;

/**
 * Created by jfloresh on 15/03/2016.
 */
public interface PadronObservadoDao {
    void insertPadronObservado(PadronObservado padronObservado);

    List<PadronObservado> obtenerPadronObservados(String coUbigeoInei);

    List<PadronObservado> obtenerPadronObservados(String coUbigeoInei, String filaIni, String filaFin);

    Integer contarPadronObservados(String coUbigeoInei);

    List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion);

    List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion, String filaIni, String filaFin);

    Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion);

    PadronObservado obtenerPadronObservado(String coPadronNominal, String coTipoObservacion);

    Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion, String nuCnv,
                                                       String coDniMadre, String apPrimer, String apSegundo, String prenombres);

    List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                                      String nuCnv, String coDniMadre, String apPrimer, String apSegundo, String prenombres,
                                                                      String filaIni, String filaFin);

    Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                       String nuDniMenor, String apPrimer, String apSegundo, String prenombres);

    List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                                      String nuDniMenor, String apPrimer, String apSegundo, String prenombres,
                                                                      String filaIni, String filaFin);

    void setEsObservado(String esObservado, String coPadronNominal, String coTipoObservacion);

    boolean existeObservacionActiva(String coPadronNominal);
}
