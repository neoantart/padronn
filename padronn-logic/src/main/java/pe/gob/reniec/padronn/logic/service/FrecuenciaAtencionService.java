package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.FrecuenciaAtencion;

import java.util.List;

public interface FrecuenciaAtencionService {

    FrecuenciaAtencion obtenerFrecuenciaAtencion(String codigo);
    List<FrecuenciaAtencion> listarFrecuenciaAtencion();

}
