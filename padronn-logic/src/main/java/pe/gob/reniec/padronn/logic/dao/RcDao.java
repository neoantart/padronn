package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Persona;

import java.util.List;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * Date: 07/06/13
 * Time: 12:20 PM
 */
public interface RcDao {

    List<Persona> listarDatosCNVPorDniMadre(String dni);

	Persona obtenerPorCnv(String cnv);
    String getMadrePorDniMenor(String dni);
    Persona obtenerMenorDeRRCC(String coDni);
}
