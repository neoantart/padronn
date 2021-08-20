package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.Acta;
import pe.gob.reniec.padronn.logic.model.ActaArchivo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 14/08/13
 * Time: 06:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ActaDao {
    String insert(Acta acta);
    boolean update(Acta acta);
    Acta getActa(String coActa);
    List<Acta> getAll();
    List<Acta> getAll(int filaIni, int filaFin);
    List<String> getDeArchivos(String coActa);

    int getNumActas();

    List<ActaArchivo> getActaArchivos(String coActa);

    List<Acta> getAll(String coEntidad, String feIni, String feFin, int filaIni, int filaFin);

    int getNumActas(String coEntidad, String feIni, String feFin);

    List<Acta> getActasByEntidad(String coEntidad, int filaIni, int filaFin);

    //para descargar excel, con mas detalle
    List<Acta> getAll(String coEntidad, String feIni, String feFin);

    List<Acta> getAll(String coEntidad, int filaIni, int filaFin);

    int getNumActas(String coEntidad);

    List<Acta> getAllActas(String coUbigeo, String feIni, String feFin);

    int getNumAllActas(String coUbigeo, String feIni, String feFin);

    List<Acta> getAllActas(String coUbigeo,String feIni, String feFin, int filaIni, int filaFin);

    boolean updateEsActa(String coActa, String deObservacion, String esActa);

    Acta getDirectory(String coActa);
}
