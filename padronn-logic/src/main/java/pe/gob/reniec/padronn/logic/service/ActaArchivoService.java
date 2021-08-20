package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.ActaArchivo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 15/08/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ActaArchivoService {
    String insert(ActaArchivo actaArchivo);
    boolean update(ActaArchivo actaArchivo);
    ActaArchivo getActaArchivo(String coActaArchivo);
    List<ActaArchivo> getAll();
    List<ActaArchivo> getAll(int filaIni, int filaFin);

    byte[] getActaArchivoBytes(String coActaArchivo);
}
