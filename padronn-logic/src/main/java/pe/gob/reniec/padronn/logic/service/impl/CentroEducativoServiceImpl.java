package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.CentroEducativoDao;
import pe.gob.reniec.padronn.logic.model.CentroEducativo;
import pe.gob.reniec.padronn.logic.service.CentroEducativoService;

import java.util.ArrayList;
import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 09/06/13
 * Time: 04:31 PM
 */
@Service
public class CentroEducativoServiceImpl implements CentroEducativoService {

	@Autowired
	CentroEducativoDao centroEducativoDao;

	@Override
	public List<CentroEducativo> listarPorDepartamento(String codigoDepartamento) {
		return centroEducativoDao.listarPorDepartamento(codigoDepartamento);
	}

	@Override
	public CentroEducativo obtenerPorCodigo(String codigo) {
		return centroEducativoDao.obtenerPorCodigo(codigo);
	}

	@Override
	public List<CentroEducativo> listarPorDatosEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
        List<CentroEducativo> centroEducativosDepartamento = centroEducativoDao.listarPorDatosEnDepartamento(nombreParcial, codigoUbigeoInei.substring(0, 2), cantidadResultados);
        List<CentroEducativo> centroEducativosDistrito = centroEducativoDao.listarPorDatosEnDepartamento(nombreParcial, codigoUbigeoInei, cantidadResultados);
        List<CentroEducativo> result = new ArrayList<CentroEducativo>();
        // merge priorizamos los registros del distrito...
        if (centroEducativosDistrito != null && centroEducativosDistrito.size() > 0) {
            for (CentroEducativo centroEducativo : centroEducativosDistrito) {
                for (int i = 0; i < centroEducativosDepartamento.size(); i++) {
                    if (centroEducativosDepartamento.get(i).getCoCentroEducativo().equals(centroEducativo.getCoCentroEducativo())) {
                        centroEducativosDepartamento.remove(i);
                    }
                }
                result.add(centroEducativo);
            }
        }
        if (centroEducativosDepartamento != null && centroEducativosDepartamento.size() > 0) {
            for (CentroEducativo centroEducativo : centroEducativosDepartamento) {
                result.add(centroEducativo);
            }
        }
		return result;
	}

	@Override
	public List<CentroEducativo> listarPorDatosFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
		return centroEducativoDao.listarPorDatosFueraDeDepartamento(nombreParcial, codigoUbigeoInei, cantidadResultados);
	}

    @Override
    public List<CentroEducativo> listarPorCodigoModular(String nombreParcial) {
        return centroEducativoDao.listarPorCodigoModular(nombreParcial);
    }


	/*@Override
	public List<CentroEducativo> listarPorCodigoYDepartamento(String nombreParcial, String codigoDepartamento, int cantidadResultados) {
		return centroEducativoDao.listarPorCodigoYDepartamento(nombreParcial, codigoDepartamento, cantidadResultados);
	}*/
}
