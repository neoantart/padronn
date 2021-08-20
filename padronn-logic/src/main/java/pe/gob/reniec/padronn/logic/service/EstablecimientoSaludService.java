package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.EstablecimientoSalud;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 09/06/13
 * Time: 04:29 PM
 */
public interface EstablecimientoSaludService {

	public List<EstablecimientoSalud> listarPorDepartamento(String codigoDepartamento);
	EstablecimientoSalud obtenerPorCodigoRenaes(String codigoRenaes);
	//public List<EstablecimientoSalud> listarPorCodigoYDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);

	public List<EstablecimientoSalud> listarPorDatosEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);
	public List<EstablecimientoSalud> listarPorDatosFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados);


    public List<EstablecimientoSalud> listarPorCodigoUbigeoInei(String coUbigeoInei);

    public List<EstablecimientoSalud> listarPorRenaes(String coEstaSalud);
}
