package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Menor;
import pe.gob.reniec.padronn.logic.model.PadronMovimiento;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.Ubigeo;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUbigeoForm;

import java.util.List;

/**
 * Created by jfloresh on 02/12/2014.
 */
public interface MidisDao {
    List<PadronNominal> listarPadron(ConsultaUbigeoForm consultaUbigeoForm, String esPadron);

    List<PadronNominal> listarPadron(ConsultaUbigeoForm consultaUbigeoForm, int filaIni, int filaFin, String esPadron);

    Integer countRowsListarPadron(ConsultaUbigeoForm consultaUbigeoForm, String esPadron);

    List<Menor> buscarMenor(String apPrimerMenor, String apSegundoMenor, String prenombresMenor,String codigoPadron, int filaIni, int filaFin);

    int countBuscarMenor(String apPrimerMenor, String apSegundoMenor, String prenombresMenor,String codigoPadron);

    List<PadronMovimiento> listadoPadronMovimiento(String coUbigeo, String feIni, String feFin);

    List<PadronMovimiento> listadoPadronMovimiento(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin);

    int countPadronMovimientos(String coUbigeo, String feIni, String feFin);

    int countAllPadronMovimientos();

    List<Ubigeo> getCantidadUbigeoProSocialDepa();

    List<Ubigeo> getCantidadUbigeoProSocial();

    List<Ubigeo> getCantidadUbigeoEdadDepa();

    List<Ubigeo> getCantidadUbigeoEdad();

    List<Ubigeo> getCantidadUbigeoTiSeguroDepa();

    List<Ubigeo> getCantidadUbigeoTiSeguro();
}
