package pe.gob.reniec.padronn.logic.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.PadronObservadoDao;
import pe.gob.reniec.padronn.logic.dao.TipoObservacionDao;
import pe.gob.reniec.padronn.logic.model.PadronObservado;
import pe.gob.reniec.padronn.logic.model.TipoObservacion;
import pe.gob.reniec.padronn.logic.service.PadronObservadoService;

import java.util.List;

/**
 * Created by jfloresh on 25/04/2016.
 */
@Service
public class PadronObservadoServiceImpl implements PadronObservadoService {
    private static final String TODOS = "0";
    private static final String DE_OBSERVACION_TODOS = "TODOS";

    private static Logger LOG = Logger.getLogger(PadronObservadoServiceImpl.class);

    @Autowired
    PadronObservadoDao padronObservadoDao;

    @Autowired
    TipoObservacionDao tipoObservacionDao;

    @Override
    public List<PadronObservado> obtenerPadronObservados(String coUbigeoInei) {
        return padronObservadoDao.obtenerPadronObservados(coUbigeoInei);
    }

    @Override
    public List<PadronObservado> obtenerPadronObservados(String coUbigeoInei, Integer filaIni, Integer filaFin) {
        return padronObservadoDao.obtenerPadronObservados(coUbigeoInei, Integer.toString(filaIni), Integer.toString(filaFin));
    }

    @Override
    public Integer contarPadronObservado(String coUbigeoInei) {
        return padronObservadoDao.contarPadronObservados(coUbigeoInei);
    }

    @Override
    public List<TipoObservacion> obtenerTipoObservacion() {
        return tipoObservacionDao.obtenerTipoObservacion();
    }

    @Override
    public List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion, Integer filaIni, Integer filaFin) {
        int len = coTipoObservacion.length();
        if (len>0 && !coTipoObservacion.equals(TODOS)) {
            return padronObservadoDao.obtenerPadronObservadosPorCoTipoObservacion(coUbigeoInei, coTipoObservacion, Integer.toString(filaIni), Integer.toString(filaFin));
        } else {
            return padronObservadoDao.obtenerPadronObservados(coUbigeoInei, Integer.toString(filaIni), Integer.toString(filaFin));
        }
    }

    @Override
    public List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion) {
        return padronObservadoDao.obtenerPadronObservadosPorCoTipoObservacion(coUbigeoInei, coTipoObservacion);
    }

    @Override
    public Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion) {
        int len=coTipoObservacion==null?0:coTipoObservacion.length();
        if (len>0 && !coTipoObservacion.equals(TODOS)) {
            return padronObservadoDao.contarPadronObservadosPorCoTipoObservacion(coUbigeoInei, coTipoObservacion);
        } else {
            return padronObservadoDao.contarPadronObservados(coUbigeoInei);
        }
    }

    @Override
    public String obtenerDeTipoObservacion(String coTipoObservacion) {
        if(coTipoObservacion!=null && coTipoObservacion.equals(TODOS)) return DE_OBSERVACION_TODOS;
        return tipoObservacionDao.obtenerDeTipoObservacion(coTipoObservacion);
    }

    @Override
    public PadronObservado obtenerPadronObservado(String coPadronNominal, String coTipoObservacion) {
        return padronObservadoDao.obtenerPadronObservado(coPadronNominal, coTipoObservacion);
    }

    @Override
    public void setEsObservado(String esObservacion, String coPadronNominal, String coTipoObservacion) {
        padronObservadoDao.setEsObservado(esObservacion, coPadronNominal, coTipoObservacion);
    }

    @Override
    public List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                                             String nuCnv, String coDniMadre, String apPrimer, String apSegundo, String prenombres,
                                                                             Integer filaIni, Integer filaFin) {
        return padronObservadoDao.obtenerPadronObservadosPorCoTipoObservacion(coUbigeoInei, coTipoObservacion, nuCnv,
                coDniMadre, apPrimer, apSegundo, prenombres, Integer.toString(filaIni), Integer.toString(filaFin));
    }

    @Override
    public Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                              String nuCnv, String coDniMadre, String apPrimer,
                                                              String apSegundo, String prenombres) {
        return padronObservadoDao.contarPadronObservadosPorCoTipoObservacion(coUbigeoInei, coTipoObservacion, nuCnv,
                coDniMadre, apPrimer, apSegundo, prenombres);
    }


    @Override
    public Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                              String nuDniMenor, String apPrimer, String apSegundo, String prenombres) {
        if(coTipoObservacion==null)coTipoObservacion="";
        coTipoObservacion = (coTipoObservacion.equals(TODOS))?"":coTipoObservacion;
        return padronObservadoDao.contarPadronObservadosPorCoTipoObservacion(coUbigeoInei, coTipoObservacion,
                nuDniMenor, apPrimer, apSegundo, prenombres);
    }

    @Override
    public List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                                             String nuDniMenor, String apPrimer, String apSegundo, String prenombres,
                                                                             Integer filaIni, Integer filaFin) {
        if(coTipoObservacion==null)coTipoObservacion="";
        coTipoObservacion = (coTipoObservacion.equals(TODOS))?"":coTipoObservacion;
        return padronObservadoDao.obtenerPadronObservadosPorCoTipoObservacion(coUbigeoInei, coTipoObservacion, nuDniMenor, apPrimer, apSegundo, prenombres, Integer.toString(filaIni), Integer.toString(filaFin));
    }


}