package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.Madre;
import pe.gob.reniec.padronn.logic.model.Menor;
import pe.gob.reniec.padronn.logic.model.Padre;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.form.BuscarMenorDocumento;

/**
 * Created by jfloresh on 04/01/2017.
 */
public interface RectificacionService {
    PadronNominal getDatosMenor(BuscarMenorDocumento buscarMenorDocumento);

}
