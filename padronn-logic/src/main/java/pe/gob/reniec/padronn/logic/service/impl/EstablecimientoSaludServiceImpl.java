package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.EstablecimientoSaludDao;
import pe.gob.reniec.padronn.logic.model.EstablecimientoSalud;
import pe.gob.reniec.padronn.logic.service.EstablecimientoSaludService;

import java.util.ArrayList;
import java.util.List;


@Service
public class EstablecimientoSaludServiceImpl implements EstablecimientoSaludService {

	@Autowired
	EstablecimientoSaludDao establecimientoSaludDao;

	@Override
	public List<EstablecimientoSalud> listarPorDepartamento(String codigoDepartamento) {
		return establecimientoSaludDao.listarPorDepartamento(codigoDepartamento);
	}

	@Override
	public EstablecimientoSalud obtenerPorCodigoRenaes(String codigo) {
        String [] partes = codigo.split("_");
        if(partes.length>1)
		    return establecimientoSaludDao.obtenerPorCodigoRenaes(partes[0], partes[1]);
        return new EstablecimientoSalud();
	}

	/*@Override
	public List<EstablecimientoSalud> listarPorCodigoYDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
		return establecimientoSaludDao.listarPorCodigoYDepartamento(nombreParcial, codigoUbigeoInei, cantidadResultados);
	}*/

	@Override
	public List<EstablecimientoSalud> listarPorDatosEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
		List<EstablecimientoSalud> establecimientosDistrito = establecimientoSaludDao.listarPorDatosEnDepartamento(nombreParcial, codigoUbigeoInei, cantidadResultados);
        List<EstablecimientoSalud> establecimientosDepartamento = establecimientoSaludDao.listarPorDatosEnDepartamento(nombreParcial, codigoUbigeoInei.substring(0, 2), cantidadResultados);
        List<EstablecimientoSalud> result = new ArrayList<EstablecimientoSalud>();
        // merge priorizamos los registros del distrito...
        if (establecimientosDistrito != null && establecimientosDistrito.size() > 0) {
            for (EstablecimientoSalud establecimientoSalud : establecimientosDistrito) {
                for (int i = 0; i < establecimientosDepartamento.size(); i++) {
                    if (establecimientosDepartamento.get(i).getCoEstSalud().equals(establecimientoSalud.getCoEstSalud())) {
                        establecimientosDepartamento.remove(i);
                    }
                }
                result.add(establecimientoSalud);
            }
        }
        if (establecimientosDepartamento != null && establecimientosDepartamento.size() > 0) {
            for (EstablecimientoSalud establecimientoSalud : establecimientosDepartamento) {
                result.add(establecimientoSalud);
            }
        }
        return result;
	}

	@Override
	public List<EstablecimientoSalud> listarPorDatosFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
		return establecimientoSaludDao.listarPorDatosFueraDeDepartamento(nombreParcial, codigoUbigeoInei, cantidadResultados);
	}

    @Override
    public List<EstablecimientoSalud> listarPorCodigoUbigeoInei(String coUbigeoInei) {
        return establecimientoSaludDao.listarPorCodigoUbigeoInei(coUbigeoInei);
    }

    @Override
    public List<EstablecimientoSalud> listarPorRenaes(String coEstaSalud) {
        return establecimientoSaludDao.listarPorRenaes(coEstaSalud);
    }
}
