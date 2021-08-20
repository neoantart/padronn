package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.EstablecimientoSalud;
import pe.gob.reniec.padronn.logic.model.Ubigeo;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 09/06/13
 * Time: 04:23 PM
 */
public interface EstablecimientoSaludDao {

	List<EstablecimientoSalud> listarPorDepartamento(String codigoDepartamento);
	//public List<EstablecimientoSalud> listarPorCodigoYDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);

	EstablecimientoSalud obtenerPorCodigoRenaes(String codigoRenaes, String nuSecuencia);

	public List<EstablecimientoSalud> listarPorDatosEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);
	public List<EstablecimientoSalud> listarPorDatosFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);


    List<EstablecimientoSalud> listarPorCodigoUbigeoInei(String coUbigeoInei);

    List<EstablecimientoSalud> listarPorRenaes(String coEstSalud);
}
