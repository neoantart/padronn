package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.DominioDao;
import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.service.DominioService;

import java.util.List;

@Service
public class DominioServiceImpl implements DominioService {

    @Autowired
    DominioDao dominioDao;

    @Override
    public List<Dominio> nivelPobrezaItems() {
        return dominioDao.nivelPobrezaItems();
    }

    @Override
    public List<Dominio> tipoSeguroItems() {
        return dominioDao.tipoSeguroItems();
    }

    @Override
    public List<Dominio> tipoProgramaSocialItems() {
        return dominioDao.tipoProgramaSocialItems();
    }

    @Override
    public List<Dominio> tipoGestionItems() {
        return dominioDao.tipoGestionItems();
    }

    @Override
    public List<Dominio> vinculoFamiliarItems() {
        return dominioDao.vinculoFamiliarItems();
    }

    @Override
    public List<Dominio> gradoInstruccionItems() {
        return dominioDao.gradoInstruccionItems();
    }

    @Override
    public List<Dominio> lenguajeHabitualItems() {
        return dominioDao.lenguajeHabitualItems();
    }

    @Override
    public List<Dominio> etniaItems() {
        return dominioDao.etniaItems();
    }

    @Override
    public List<Dominio> tipoDocumentoItems() {
        return dominioDao.tipoDocumentoItems();
    }

    @Override
    public List<Dominio> generoItems() {
        return dominioDao.generoItems();
    }

    @Override
    public Dominio obtener(String codigoDominio, String nombreDominio) {
        return dominioDao.obtener(codigoDominio, nombreDominio);
    }


    @Override
    public List<Dominio> getMotivoBajaList() {
        return dominioDao.getMotivoBajaList();
    }

    @Override
    public List<Dominio> getMotivoAltaList() {
        return dominioDao.getMotivoAltaList();
    }


    @Override
    public List<Dominio> getCoTipoSinNombre() {
        return dominioDao.getCoTipoSinNombre();
    }

    @Override
    public List<String> getDeTipoSinNombre() {
        return dominioDao.getDeTipoSinNombre();
    }

    @Override
    public List<Dominio> tipoEntidadesItems() { return dominioDao.tipoEntidadesItems(); }
}
