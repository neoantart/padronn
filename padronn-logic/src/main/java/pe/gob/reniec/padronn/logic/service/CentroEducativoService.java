package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.CentroEducativo;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 09/06/13
 * Time: 04:28 PM
 */
public interface CentroEducativoService {

	public List<CentroEducativo> listarPorDepartamento(String codigoDepartamento);
	CentroEducativo obtenerPorCodigo(String codigo);
	//List<CentroEducativo> listarPorCodigoYDepartamento(String nombreParcial, String codigoDepartamento, int cantidadResultados);
	List<CentroEducativo> listarPorDatosEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);
	List<CentroEducativo> listarPorDatosFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);


    List<CentroEducativo> listarPorCodigoModular(String nombreParcial);
}
