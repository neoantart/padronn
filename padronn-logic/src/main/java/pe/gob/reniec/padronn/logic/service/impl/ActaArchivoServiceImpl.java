package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.ActaArchivoDao;
import pe.gob.reniec.padronn.logic.model.ActaArchivo;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.ActaArchivoService;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 15/08/13
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ActaArchivoServiceImpl extends AbstractBaseService implements ActaArchivoService {

    @Autowired
    ActaArchivoDao actaArchivoDao;

    @Override
    public String insert(ActaArchivo actaArchivo) {
        try{
            return actaArchivoDao.insert(actaArchivo);
        } catch (DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(ActaArchivo actaArchivo) {
        try{
            return actaArchivoDao.update(actaArchivo);
        }
        catch(DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return false;
        }
    }

    @Override
    public ActaArchivo getActaArchivo(String coActaArchivo) {
        try{
            return actaArchivoDao.getActaArchivo(coActaArchivo);
        }
        catch (DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ActaArchivo> getAll() {
        try{
            return actaArchivoDao.getAll();
        }
        catch (DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ActaArchivo> getAll(int filaIni, int filaFin) {
        try{
            return actaArchivoDao.getAll(filaIni, filaFin);
        }
        catch (DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }


    @Override
    public byte[] getActaArchivoBytes(String coActaArchivo) {
        try{
            return actaArchivoDao.getActaArchivoBytes(coActaArchivo);
        }
        catch (DataAccessException dae ){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }



}
