package pe.gob.reniec.padronn.logic.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
//import pe.gob.reniec.jr.ftp.archivos.main.FtpArchivo;
//import pe.gob.reniec.jr.ftp.archivos.main.FtpArchivo;
import pe.gob.reniec.padronn.logic.dao.ActaArchivoDao;
import pe.gob.reniec.padronn.logic.dao.ActaArchivosDao;
import pe.gob.reniec.padronn.logic.dao.ActaDao;
import pe.gob.reniec.padronn.logic.model.Acta;
import pe.gob.reniec.padronn.logic.model.ActaArchivo;
import pe.gob.reniec.padronn.logic.model.ActaArchivos;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.ActaService;

import java.io.FileInputStream;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 15/08/13
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ActaServiceImpl extends AbstractBaseService implements ActaService {

    @Autowired
    ActaDao actaDao;

    @Autowired
    ActaArchivosDao actaArchivosDao;

    @Autowired
    ActaArchivoDao actaArchivoDao;

    Logger logger = Logger.getLogger(getClass());

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public String addActa(Acta acta) throws Exception {
        String coActa = actaDao.insert(acta);
        acta.setCoActa(coActa);
        List<String> archivos = acta.getCoActaArchivos();
        for(String archivo: archivos) {
            ActaArchivos actaArchivos = new ActaArchivos();
            actaArchivos.setCoActa(acta.getCoActa());
            actaArchivos.setCoActaArchivo(archivo);
            actaArchivos.setEsActaArchivos("1");
            ActaArchivo actaArchivo = actaArchivoDao.getActaArchivo(archivo);

            byte[] byFile = actaArchivoDao.getActaArchivoBytes(archivo);
            actaArchivo.getArchivo();
//            FtpArchivo ftpArchivo = new FtpArchivo();
            boolean bResult = false;
            String fileName = actaArchivo.getCoActaArchivo() + "_" + actaArchivo.getNoActaArchivo();

            //            bResult = ftpArchivo.agrega_archivo(fileName, byFile, "lima", "lima", "los olivos", "2014", "04");
            /*logger.info("=======================================");
            logger.info("coActa:" + coActa);
            logger.info("=======================================");*/

//            Acta actaDirectory = actaDao.getDirectory(coActa);

            /*logger.info("=======================================");
            logger.info("actaDirectory:"+actaDirectory);
            logger.info("=======================================");*/

            /*if (actaDirectory != null)
            {
                bResult = ftpArchivo.agrega_archivo(fileName, byFile, actaDirectory.getDeDepartamento(), actaDirectory.getDeProvincia(), actaDirectory.getDeDistrito(), actaDirectory.getAnio(), actaDirectory.getMes());
            }
            if (!bResult) {
                logger.error("error guardando archivo en ftp");
                throw new Exception("Error guardando archivo en FTP...");
            }*/
            actaArchivosDao.insert(actaArchivos);
        }
        return coActa;
    }

    @Override
    public boolean update(Acta acta) {
        String coActa = acta.getCoActa();
        if(actaDao.update(acta)) {
            actaArchivosDao.delete(acta.getCoActa());
            for(String archivo: acta.getCoActaArchivos()) {
                ActaArchivos actaArchivos = new ActaArchivos();
                actaArchivos.setCoActa(coActa);
                actaArchivos.setCoActaArchivo(archivo);
                actaArchivosDao.insert(actaArchivos);
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public Acta getActa(String coActa) {
        try{
            return actaDao.getActa(coActa);
        }
        catch (DataAccessException dae){
            log.error(dae.getMessage());
            return null;
        }
    }

    @Override
    public List<Acta> getAll() {
        try{
            return actaDao.getAll();
        }
        catch (DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Acta> getAll(int filaIni, int filaFin) {
        try{
            return actaDao.getAll(filaIni, filaFin);
        }
        catch (DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Acta> getAll(String coEntidad, String feIni, String feFin, int filaIni, int filaFin){
        try{
            return actaDao.getAll(coEntidad, feIni, feFin, filaIni, filaFin);
        }
        catch (DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public int getNumActas() {
        return actaDao.getNumActas();
    }

    @Override
    public int getNumActas(String coEntidad, String feIni, String feFin) {
        return actaDao.getNumActas(coEntidad, feIni, feFin);
    }

    @Override
    public List<Acta> getActasByEntidad(String coEntidad, String feIni, String feFin, int filaIni, int filaFin) {
        return actaDao.getAll(coEntidad, feIni, feFin, filaIni, filaFin);
    }

    @Override
    public List<Acta> getAll(String coEntidad, int filaIni, int filaFin) {
        return actaDao.getAll(coEntidad, filaIni, filaFin);
    }

    @Override
    public int getNumActas(String coEntidad) {
        return actaDao.getNumActas(coEntidad);
    }

    @Override
    public int getNumAllActas(String coUbigeo, String feIni, String feFin) {
        return actaDao.getNumAllActas(coUbigeo, feIni, feFin);
    }

    @Override
    public List<Acta> getAllActas(String coUbigeo, String feIni, String feFin) {
        return actaDao.getAllActas(coUbigeo, feIni, feFin);
    }

    @Override
    public List<Acta> getAllActas(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin) {
        return actaDao.getAllActas(coUbigeo, feIni, feFin, filaIni, filaFin);
    }

    @Override
    @Transactional
    public boolean darAltaActa(String coActa, String deObservacion) {
        return actaDao.updateEsActa(coActa, deObservacion, "1");
    }

    @Override
    @Transactional
    public boolean darBajaActa(String coActa, String deObservacion) {
        try {
//            FtpArchivo ftpArchivo = new FtpArchivo();
//            boolean bResult = false;
//            List<ActaArchivo> actaArchivos = actaDao.getActaArchivos(coActa);
//            ftpArchivo.existe_archivo("", "")
/*bResult = ftpArchivo.elimina_archivo("201401155-archivo.xls","ancash","lima","los olivos","2014","02");*/

            logger.debug("coActa:" + coActa);
            actaArchivosDao.deleteActaArchivos(coActa);
            return actaDao.updateEsActa(coActa, deObservacion, "0");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
