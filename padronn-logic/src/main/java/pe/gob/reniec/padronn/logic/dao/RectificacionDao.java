package pe.gob.reniec.padronn.logic.dao;

import org.springframework.dao.DataIntegrityViolationException;
import pe.gob.reniec.padronn.logic.model.*;

import java.util.List;

/**
 * User: jronal at gmail dot com
 * Date: 17/10/13
 * Time: 06:42 PM
 */

public interface RectificacionDao {
    PadronNominal getDatosMenorCoPadron(String nuDniMenor);

    PadronNominal getDatosMenorNuDni(String nuDni);

    PadronNominal getDatosMenorNuCnv(String nuCnv);

    PadronNominal getDatosMenorNuCui(String nuCui);

    Persona getPersonaAni(String nuDni, Persona.TipoPersona tipoPersona);

    Menor getDatosPersonalesMenor(String coPadronNominal);

    Padre getDatosPersonalesPadre(String coPadronNominal);

    Madre getDatosPersonalesMadre(String coPadronNominal);

    boolean rectificarDatosMenor(Menor menorPadron) throws DataIntegrityViolationException;

    boolean rectificarDatosMadre(Madre madrePadron);

    boolean rectificarDatosPadre(Padre padrePadron);

    /**
     * inserta log de rectificacion de datos del padron
     * @param rectificacion
     */
    void insertRectificacion(Rectificacion rectificacion);

    int getMaxNuSecRectificacion(String coPadronNominal, String tiPersona);

    void insertPadronHistorico(PadronNominal padronNominal);

    int getMaxNuSecPadronHistorico(String coPadronNominal);

    PadronNominal getPadronNominal(String coPadronNominal);

    List<Menor> buscarMenor(String apPrimerMenor, String apSegundoMenor, String prenombresMenor, int filaIni, int filaFin);

    int countBuscarMenor(String apPrimerMenor, String apSegundoMenor, String prenombresMenor);

    boolean buscarCuiDuplicado(String cui);

    List<Rectificacion> getRectificaciones(String coPadronNominal, String tiPersona);

//    boolean isUniqueCui(String nuCui);
    void guardarHistorico(String coPadronNominal);

    boolean setEsPadron(String coPadronNominal, String esPadron);


}