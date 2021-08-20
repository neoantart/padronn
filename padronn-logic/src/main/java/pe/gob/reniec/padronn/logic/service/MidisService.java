package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.PadronMovimiento;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.Ubigeo;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUbigeoForm;
import pe.gob.reniec.padronn.logic.model.form.ListaMenores;

import java.util.List;

/**
 * Created by jfloresh on 02/12/2014.
 */
public interface MidisService {
    public List<PadronNominal> listarPadronActivos(ConsultaUbigeoForm consultaUbigeoForm);

    List<PadronNominal> listarPadronObservados(ConsultaUbigeoForm consultaUbigeoForm);

    List<PadronNominal> listarPadronTodos(ConsultaUbigeoForm consultaUbigeoForm);

    public List<PadronNominal> listarPadronActivos(ConsultaUbigeoForm consultaUbigeoForm, int filaIni, int filaFin);

    List<PadronNominal> listarPadronObservados(ConsultaUbigeoForm consultaUbigeoForm, int filaIni, int filaFin);

    List<PadronNominal> listarPadronTodos(ConsultaUbigeoForm consultaUbigeoForm, int filaIni, int filaFin);

    Integer countRowsListarPadronActivos(ConsultaUbigeoForm consultaUbigeoForm);

    Integer countRowsListarPadronObservados(ConsultaUbigeoForm consultaUbigeoForm);

    Integer countRowsListarPadronTodos(ConsultaUbigeoForm consultaUbigeoForm);

    int countAllPadronMovimientos();

    int countPadronMovimientos(String coUbigeo, String feIni, String feFin);

    List<PadronMovimiento> listadoPadronMovimiento(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin);

    List<PadronMovimiento> listadoPadronMovimiento(String coUbigeo, String feIni, String feFin);

    List<Ubigeo> getCantidadUbigeoProSocialDepa();

    List<Ubigeo> getCantidadUbigeoProSocial();

    List<Ubigeo> getCantidadUbigeoEdadDepa();

    List<Ubigeo> getCantidadUbigeoEdad();

    List<Ubigeo> getCantidadUbigeoTiSeguroDepa();

    List<Ubigeo> getCantidadUbigeoTiSeguro();
}
