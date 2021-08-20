package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.UbigeoDao;
import pe.gob.reniec.padronn.logic.model.CentroPoblado;
import pe.gob.reniec.padronn.logic.model.EjeVial;
import pe.gob.reniec.padronn.logic.model.Ubigeo;
import pe.gob.reniec.padronn.logic.service.UbigeoService;

import java.util.ArrayList;
import java.util.List;


@Service
public class UbigeoServiceImpl implements UbigeoService {

    @Autowired
    UbigeoDao ubigeoDao;

    @Override
    public List<Ubigeo> buscarPorProvinciaDistritoEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
        return ubigeoDao.buscarPorProvinciaDistritoEnDepartamento(nombreParcial.trim(), codigoUbigeoInei, cantidadResultados);
    }

    @Override
    public List<Ubigeo> buscarPorProvinciaDistritoFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
        return ubigeoDao.buscarPorProvinciaDistritoFueraDeDepartamento(nombreParcial.trim(), codigoUbigeoInei, cantidadResultados);
    }

    @Override
    public List<Ubigeo> listarPorDepartamento(String codigoDepartamento) {
        return ubigeoDao.listarPorDepartamento(codigoDepartamento);
    }

    @Override
    public Ubigeo obtenerPorCodigoUbigeoReniec(String codigoUbigeoReniec) {
        return ubigeoDao.obtenerPorCodigoUbigeoReniec(codigoUbigeoReniec);
    }

    @Override
    public Ubigeo obtenerPorCodigoInei(String codigoInei) {
        return ubigeoDao.obtenerPorCodigoInei(codigoInei);
    }

    @Override
    public String obtenerDeUbigeoInei(String coUbigeoInei) {
        return ubigeoDao.obtenerDeUbigeoInei(coUbigeoInei);
    }

    @Override
    public String obtenerDeUbigeoIneiCorto(String coUbigeoInei) {
        return ubigeoDao.obtenerDeUbigeoIneiCorto(coUbigeoInei);
    }

    @Override
    public List<CentroPoblado> buscarCentroPoblado(String coUbigeoInie, String parteNoCentroPoblado) {
        return ubigeoDao.buscarCentroPoblado(coUbigeoInie, parteNoCentroPoblado.trim());
    }

    @Override
    public CentroPoblado getCentroPoblado(String coCentroPoblado) {
        return ubigeoDao.getCentroPoblado(coCentroPoblado);
    }

    @Override
    public List<Ubigeo> getDistritosEuropan() {
        try {
            List<Ubigeo> ubigeoList = new ArrayList<Ubigeo>();
            Ubigeo ubigeo = new Ubigeo();
            ubigeo.setId("00");
            ubigeo.setText("TODOS LOS DISTRITOS DE EUROPAN");
            ubigeoList.add(ubigeo);
            ubigeoList.addAll(ubigeoDao.getDistritosEuropan());
            return ubigeoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CentroPoblado> listaCentrosPobladosPorDepartamento(String codigoDepartamento) {
        try {
            return ubigeoDao.listaCentrosPobladosPorDepartamento(codigoDepartamento);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CentroPoblado> buscarUbigeoInei(String parteDeUbigeo, int filaIni, int filaFin) {
        return ubigeoDao.buscarUbigeoInei(parteDeUbigeo, filaIni, filaFin);
    }

    @Override
    public Integer countBuscarUbigeoInei(String parteDeUbigeo) {
        return ubigeoDao.countBuscarUbigeoInei(parteDeUbigeo);
    }

    @Override
    public CentroPoblado getUbigeoInei(String coUbigeoInei) {
        return ubigeoDao.getUbigeoInei(coUbigeoInei);
    }


    /**
     * metodo que obtiene centro poblado de capital de un distrito
     *
     * @param coUbigeoInei
     * @return
     */
    @Override
    public CentroPoblado obtenerCentroPoblado(String coUbigeoInei) {
        return ubigeoDao.obtenerCentroPoblado(coUbigeoInei);
    }

    @Override
    public int countBuscarEjeVial(String parteDeVia, String coCentroPoblado) {
        return ubigeoDao.countBuscarEjeVial(parteDeVia, coCentroPoblado);
    }

    @Override
    public List<EjeVial> buscarEjeVial(String parteDeVia, String coCentroPoblado, int filaIni, int filaFin) {
        return ubigeoDao.buscarEjeVial(parteDeVia, coCentroPoblado, filaIni, filaFin);
    }

    @Override
    public EjeVial obtenerEjeVial(String coVia) {
        return ubigeoDao.obtenerEjeVial(coVia);
    }


}