package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.CentroPoblado;
import pe.gob.reniec.padronn.logic.model.EjeVial;
import pe.gob.reniec.padronn.logic.model.Ubigeo;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 29/05/13
 * Time: 06:11 PM
 */
public interface UbigeoDao {



	List<Ubigeo> buscarPorProvinciaDistritoEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);

	List<Ubigeo> buscarPorProvinciaDistritoFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);

	List<Ubigeo> listarPorDepartamento(String codigoDepartamento);

	Ubigeo obtenerPorCodigoUbigeoReniec(String codigoUbigeoReniec);

	Ubigeo obtenerPorCodigoInei(String codigoInei);

    String obtenerDeUbigeoInei(String coUbigeoInei);

    String obtenerDeUbigeoIneiCorto(String coUbigeoInei);

    List<CentroPoblado> buscarCentroPoblado(String buscarCentroPoblado, String parteNoCentroPoblado);

    CentroPoblado getCentroPoblado(String coCentroPoblado);

    List<Ubigeo> getDistritosEuropan();

    List<CentroPoblado> listaCentrosPobladosPorDepartamento(String codigoDepartamento);

    List<CentroPoblado> buscarUbigeoInei(String parteDeUbigeo, int filaIni, int filaFin);

    Integer countBuscarUbigeoInei(String parteDeUbigeo);

    CentroPoblado getUbigeoInei(String coUbigeoInei);

    CentroPoblado obtenerCentroPoblado(String coUbigeoInei);

    int countBuscarEjeVial(String parteDeVia, String coCentroPoblado);

    List<EjeVial> buscarEjeVial(String parteDeVia, String coCentroPoblado, int filaIni, int filaFin);

    EjeVial obtenerEjeVial(String coVia);
}
