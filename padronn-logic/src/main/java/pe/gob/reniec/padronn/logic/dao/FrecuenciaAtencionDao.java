package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.CentroEducativo;
import pe.gob.reniec.padronn.logic.model.FrecuenciaAtencion;

import java.util.List;

public interface FrecuenciaAtencionDao {
    FrecuenciaAtencion obtenerFrecuenciaAtencion(String codigo);
    public List<FrecuenciaAtencion> listarFrecuenciaAtencion();

}
