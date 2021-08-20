package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.GriasDao;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.form.ListaMenores;
import pe.gob.reniec.padronn.logic.service.GriasService;

import java.util.List;

/**
 * Created by jfloresh on 26/05/2014.
 */
@Service
public class GriasServiceImpl implements GriasService {

    @Autowired
    GriasDao griasDao;

    @Override
    public List<PadronNominal> listarPadronActivos(ListaMenores listaMenores) {
        try {
            return griasDao.listarPadron(listaMenores, "1");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public List<PadronNominal> listarPadronObservados(ListaMenores listaMenores) {
        try {
            return griasDao.listarPadron(listaMenores, "2");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public List<PadronNominal> listarPadronTodos(ListaMenores listaMenores) {
        try {
            return griasDao.listarPadron(listaMenores, "3");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronActivos(ListaMenores listaMenores, int filaIni, int filaFin) {
        try {
            return griasDao.listarPadron(listaMenores, filaIni, filaFin, "1");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public List<PadronNominal> listarPadronObservados(ListaMenores listaMenores, int filaIni, int filaFin) {
        try {
            return griasDao.listarPadron(listaMenores, filaIni, filaFin, "2");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public List<PadronNominal> listarPadronTodos(ListaMenores listaMenores, int filaIni, int filaFin) {
        try {
            return griasDao.listarPadron(listaMenores, filaIni, filaFin, "3");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    @Override
    public Integer countRowsListarPadronActivos(ListaMenores listaMenores) {
        try {
            return griasDao.countRowsListarPadron(listaMenores, "1");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public Integer countRowsListarPadronObservados(ListaMenores listaMenores) {
        try {
            return griasDao.countRowsListarPadron(listaMenores, "2");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public Integer countRowsListarPadronTodos(ListaMenores listaMenores) {
        try {
            return griasDao.countRowsListarPadron(listaMenores, "3");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
