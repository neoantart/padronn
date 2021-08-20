package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.FuenteDatos;

import java.util.List;

public interface FuenteDatosDao {
    List<FuenteDatos> obtenerFuenteDatos();
    FuenteDatos obtenerFuenteDatosPorCodigo(String coFuenteDatos);
}
