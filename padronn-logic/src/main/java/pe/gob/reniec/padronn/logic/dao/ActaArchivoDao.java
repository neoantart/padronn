package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.AbstractBean;
import pe.gob.reniec.padronn.logic.model.ActaArchivo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 14/08/13
 * Time: 06:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ActaArchivoDao{
    String insert(ActaArchivo actaArchivo);
    boolean update(ActaArchivo actaArchivo);
    ActaArchivo getActaArchivo(String coActaArchivo);
    List<ActaArchivo> getAll();
    List<ActaArchivo> getAll(int filaIni, int filaFin);

    byte[] getActaArchivoBytes(String coActaArchivo);

    //String getCoArchivo();
}
