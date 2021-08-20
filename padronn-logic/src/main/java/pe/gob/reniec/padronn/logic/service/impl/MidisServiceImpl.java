package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.MidisDao;
import pe.gob.reniec.padronn.logic.model.PadronMovimiento;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.Ubigeo;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUbigeoForm;
import pe.gob.reniec.padronn.logic.service.MidisService;

import java.util.List;

/**
 * Created by jfloresh on 02/12/2014.
 */
@Service
public class MidisServiceImpl implements MidisService {
    @Autowired
    MidisDao midisDao;

    @Override
    public List<PadronNominal> listarPadronActivos(ConsultaUbigeoForm consultaUbigeoForm) {
        return midisDao.listarPadron(consultaUbigeoForm,"1");
    }
    @Override
    public List<PadronNominal> listarPadronObservados(ConsultaUbigeoForm consultaUbigeoForm) {
        return midisDao.listarPadron(consultaUbigeoForm,"2");
    }
    @Override
    public List<PadronNominal> listarPadronTodos(ConsultaUbigeoForm consultaUbigeoForm) {
        return midisDao.listarPadron(consultaUbigeoForm,"3");
    }

    @Override
    public List<PadronNominal> listarPadronActivos(ConsultaUbigeoForm consultaUbigeoForm, int filaIni, int filaFin) {
        return midisDao.listarPadron(consultaUbigeoForm,filaIni, filaFin, "1");
    }
    @Override
    public List<PadronNominal> listarPadronObservados(ConsultaUbigeoForm consultaUbigeoForm, int filaIni, int filaFin) {
        return midisDao.listarPadron(consultaUbigeoForm,filaIni, filaFin, "2");
    }
    @Override
    public List<PadronNominal> listarPadronTodos(ConsultaUbigeoForm consultaUbigeoForm, int filaIni, int filaFin) {
        return midisDao.listarPadron(consultaUbigeoForm,filaIni, filaFin, "3");
    }
//--
    @Override
    public Integer countRowsListarPadronActivos(ConsultaUbigeoForm consultaUbigeoForm) {
        return midisDao.countRowsListarPadron(consultaUbigeoForm,"1");
    }

    @Override
    public Integer countRowsListarPadronObservados(ConsultaUbigeoForm consultaUbigeoForm) {
        return midisDao.countRowsListarPadron(consultaUbigeoForm,"2");
    }

    @Override
    public Integer countRowsListarPadronTodos(ConsultaUbigeoForm consultaUbigeoForm) {
        return midisDao.countRowsListarPadron(consultaUbigeoForm,"3");
    }
//--
    @Override
    public int countAllPadronMovimientos() {
        return midisDao.countAllPadronMovimientos();
    }

    @Override
    public int countPadronMovimientos(String coUbigeo, String feIni, String feFin) {
        return midisDao.countPadronMovimientos(coUbigeo, feIni, feFin);
    }

    @Override
    public List<PadronMovimiento> listadoPadronMovimiento(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin) {
        return midisDao.listadoPadronMovimiento(coUbigeo, feIni, feFin, filaIni, filaFin);
    }

    @Override
    public List<PadronMovimiento> listadoPadronMovimiento(String coUbigeo, String feIni, String feFin) {
        return midisDao.listadoPadronMovimiento(coUbigeo, feIni, feFin);
    }



    @Override
    public List<Ubigeo> getCantidadUbigeoProSocialDepa(){
        return midisDao.getCantidadUbigeoProSocialDepa();
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoProSocial() {
        return midisDao.getCantidadUbigeoProSocial();
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoEdadDepa() {
        return midisDao.getCantidadUbigeoEdadDepa();
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoEdad(){
        return midisDao.getCantidadUbigeoEdad();
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoTiSeguroDepa(){
        return midisDao.getCantidadUbigeoTiSeguroDepa();
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoTiSeguro(){
        return midisDao.getCantidadUbigeoTiSeguro();
    }
}
