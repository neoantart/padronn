package pe.gob.reniec.padronn.logic.service;

import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.padronn.logic.model.Acta;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 15/08/13
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ActaService {
    String addActa(Acta acta) throws Exception;
    boolean update(Acta acta);
    Acta getActa(String coActa);
    List<Acta> getAll();
    List<Acta> getAll(int filaIni, int filaFin);

    int getNumActas();

    List<Acta> getAll(String coEntidad, String feIni, String feFin, int filaIni, int filaFin);

    int getNumActas(String coEntidad, String feIni, String feFin);

    List<Acta> getActasByEntidad(String coEntidad, String feIni, String feFin, int filaIni, int filaFin);


    List<Acta> getAll(String coEntidad, int filaIni, int filaFin);

    int getNumActas(String coEntidad);


    List<Acta> getAllActas(String coUbigeo, String feIni, String feFin);

    int getNumAllActas(String coUbigeo, String feIni, String feFin);

    List<Acta> getAllActas(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin);

    @Transactional
    boolean darAltaActa(String coActa, String deObservacion);

    @Transactional
    boolean darBajaActa(String coActa, String deObservacion);
}
