package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.ActaArchivos;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 15/08/13
 * Time: 09:59 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ActaArchivosDao {
    boolean insert(ActaArchivos actaArchivos);
    ActaArchivos getActaArchivos(String coActa, String coActaArchivo);
    List<ActaArchivos> getActaArchivosByCoActa(String coActa);
    List<ActaArchivos> getActaArchivosByCoActaArchivo(String coActaArchivo);
    boolean delete(String coActa);

    void deleteActaArchivos(String coActa);
}
