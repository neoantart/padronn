package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.FuenteDatos;

import java.util.List;

public interface FuenteDatosService {
    List<FuenteDatos> obtenerFuenteDatos();
    FuenteDatos obtenerFuenteDatosPorCodigo(String coFuenteDatos);
}
