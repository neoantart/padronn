package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.RectificacionDao;
import pe.gob.reniec.padronn.logic.model.Madre;
import pe.gob.reniec.padronn.logic.model.Menor;
import pe.gob.reniec.padronn.logic.model.Padre;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.form.BuscarMenorDocumento;
import pe.gob.reniec.padronn.logic.service.RectificacionService;

/**
 * Created by jfloresh on 04/01/2017.
 */
@Service
public class RectificacionServiceImpl implements RectificacionService {

    @Autowired
    RectificacionDao rectificacionDao;

    @Override
    public PadronNominal getDatosMenor(BuscarMenorDocumento buscarMenorDocumento) {
        if (buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.DNI.getTiDoc())) {
            return rectificacionDao.getDatosMenorNuDni(buscarMenorDocumento.getNuDoc());
        } else if (buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.CUI.getTiDoc())) {
            return rectificacionDao.getDatosMenorNuCui(buscarMenorDocumento.getNuDoc());
        } else if (buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.CNV.getTiDoc())) {
            return rectificacionDao.getDatosMenorNuCnv(buscarMenorDocumento.getNuDoc());
        } else if (buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.CO_PADRON.getTiDoc())) {
            return rectificacionDao.getDatosMenorCoPadron(buscarMenorDocumento.getNuDoc());
        } else
            return null;
    }

}
