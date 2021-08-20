package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.ReporteDao;
import pe.gob.reniec.padronn.logic.dao.UsuarioExternoDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.ReporteEuropan;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.ReporteService;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 23/08/13
 * Time: 09:06 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReporteServiceImpl extends AbstractBaseService implements ReporteService {

    @Autowired
    ReporteDao reporteDao;

    @Autowired
    UsuarioExternoDao usuarioExternoDao;

    @Override
    public List<PadronNominal> listarPadronesActivos(String coUbigeoInei, String feIni, String feFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadrones(coUbigeoInei, "1", feIni, feFin, tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesObservados(String coUbigeoInei, String feIni, String feFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadrones(coUbigeoInei, "2", feIni, feFin, tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListaPadronesActivos(String coUbigeoInei, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListaPadrones(coUbigeoInei, "1", feIni, feFin, tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesActivos(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadrones(coUbigeoInei, "1", feIni, feFin, filaIni, filaFin, tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesActivos(String coUbigeoInei, int filaIni, int filaFin) {
        try{
            return reporteDao.listarPadrones(coUbigeoInei, "1", null, null, filaIni, filaFin, "");
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListaPadronesActivos(String coUbigeoInei) {
        return reporteDao.countListaPadrones(coUbigeoInei, "1", null, null,"");
    }

    @Override
    public List<PadronNominal> listarPadronesActivos(String coUbigeoInei) {
        try{
            return reporteDao.listarPadrones(coUbigeoInei, "1", null, null, "");
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesInactivos(String coUbigeoInei, int filaIni, int filaFin) {
        try{
            return reporteDao.listarPadrones(coUbigeoInei, "0", null, null, filaIni, filaFin, "");
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListaPadronesInactivos(String coUbigeoInei) {
        return reporteDao.countListaPadrones(coUbigeoInei, "0", null, null, "");
    }

    @Override
    public List<PadronNominal> listarPadronesInactivos(String coUbigeoInei){
        try{
            return reporteDao.listarPadrones(coUbigeoInei, "0", null, null, "");
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadrones(coUbigeoInei, "0", feIni, feFin, tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }
    // Modificar para ver inactivos desarrollo pablo aguilar
    @Override
    public List<PadronNominal> listarPadronesInactivos(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadrones(coUbigeoInei, "0", feIni, feFin, filaIni, filaFin, tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesObservados(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadrones(coUbigeoInei, "2", feIni, feFin, filaIni, filaFin, tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListaPadronesInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListaPadrones(coUbigeoInei, "0", feIni, feFin, tiRegFecha);
    }

    @Override
    public int countListaPadronesObservados(String coUbigeoInei, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListaPadrones(coUbigeoInei, "2", feIni, feFin, tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidadInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try{
            return reporteDao.listarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "0",tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidadInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, int filaIni, int filaFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "0", filaIni, filaFin,tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidadTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "3", tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidadTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, int filaIni, int filaFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "3", filaIni, filaFin,tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }
    @Override
    public int countListarPadronesByEntidadInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        return reporteDao.countListarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "0", tiRegFecha);
    }

    @Override
    public int countListarPadronesByEntidadTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        return reporteDao.countListarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "3", tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByEstablecimientoSaludActivos(String coEstSalud, String feIni, String feFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "1", tiRegFecha);
        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByEstablecimientoSaludObservados(String coEstSalud, String feIni, String feFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "2", tiRegFecha);
        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }


    @Override
    public List<PadronNominal> listarPadronesByEstablecimientoSaludOBservados(String coEstSalud, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        return reporteDao.listarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "2", filaIni, filaFin, tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByEstablecimientoSaludActivos(String coEstSalud, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        return reporteDao.listarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "1", filaIni, filaFin, tiRegFecha);
    }


    @Override
    public int countListarPadronesByEstablecimientoSaludObservados(String coEstSalud, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "2", tiRegFecha);
    }
    @Override
    public int countListarPadronesByEstablecimientoSaludActivos(String coEstSalud, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "1", tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByEstablecimientoSaludInactivos(String coEstSalud, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.listarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "0", tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByEstablecimientoSaludInactivos(String coEstSalud, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        return reporteDao.listarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "0", filaIni, filaFin, tiRegFecha);
    }

    @Override
    public int countListarPadronesByEstablecimientoSaludInactivos(String coEstSalud, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListarPadronesByEstablecimientoSalud(coEstSalud, feIni, feFin, "0",tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByProgramaSocialActivos(String coProgramaSocial, String feIni, String feFin, String coEntidad, String tiRegFecha) {
        return reporteDao.listarPadronesByProgramaSocial(coProgramaSocial, feIni, feFin, coEntidad, "1", tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByProgramaSocialObservados(String coProgramaSocial, String feIni, String feFin, String coEntidad, String tiRegFecha) {
        return reporteDao.listarPadronesByProgramaSocial(coProgramaSocial, feIni, feFin, coEntidad, "2", tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByProgramaSocialActivos(String coProgramaSocial, String feIni, String feFin, String coEntidad, int filaIni, int filaFin, String tiRegFecha) {
        return reporteDao.listarPadronesByProgramaSocial(coProgramaSocial, feIni, feFin, coEntidad, "1", filaIni, filaFin, tiRegFecha);
    }

    @Override
    public int countListarPadronesByProgramaSocialActivos(String coProgramaSocial,String feIni, String feFin,String coEntidad, String tiRegFecha) {
        return reporteDao.countListarPadronesByProgramaSocial(coProgramaSocial, feIni, feFin, coEntidad, "1", tiRegFecha);
    }

    @Override
    public int countListarPadronesByProgramaSocialObservados(String coProgramaSocial,String feIni, String feFin,String coEntidad, String tiRegFecha) {
        return reporteDao.countListarPadronesByProgramaSocial(coProgramaSocial, feIni, feFin, coEntidad, "2", tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByProgramaSocialInactivos(String coProgramaSocial, String feIni, String feFin, String coEntidad, String tiRegFecha) {
        return reporteDao.listarPadronesByProgramaSocial(coProgramaSocial, feIni, feFin, coEntidad, "0", tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByProgramaSocialInactivos(String coProgramaSocial, String feIni, String feFin, String coEntidad, int filaIni, int filaFin, String tiRegFecha) {
        return reporteDao.listarPadronesByProgramaSocial(coProgramaSocial, feIni, feFin, coEntidad, "0", filaIni, filaFin, tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByProgramaSocialObservados(String coProgramaSocial, String feIni, String feFin, String coEntidad, int filaIni, int filaFin, String tiRegFecha) {
        return reporteDao.listarPadronesByProgramaSocial(coProgramaSocial, feIni, feFin, coEntidad, "2", filaIni, filaFin, tiRegFecha);
    }

    @Override
    public int countListarPadronesByProgramaSocialInactivos(String coEstSalud, String feIni, String feFin, String coEntidad, String tiRegFecha) {
        return reporteDao.countListarPadronesByProgramaSocial(coEstSalud, feIni, feFin, coEntidad, "0", tiRegFecha);
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidadObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try{
            return reporteDao.listarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "2", tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidadActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try{
            return reporteDao.listarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "1", tiRegFecha);
        }
        catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidadObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, int filaIni, int filaFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "2", filaIni, filaFin, tiRegFecha);
        }
        catch(DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidadActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, int filaIni, int filaFin, String tiRegFecha) {
        try{
            return reporteDao.listarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "1", filaIni, filaFin, tiRegFecha);
        }
        catch(DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListarPadronesByEntidadObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        return reporteDao.countListarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "2", tiRegFecha);
    }

    @Override
    public int countListarPadronesByEntidadActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        return reporteDao.countListarPadronesByEntidad(coUbigeoInei, feIni, feFin, tiRegistro, "1", tiRegFecha);
    }

    @Override
    public List<Lugar> getUbigeo(String deUbigeo){
        try{
            return reporteDao.getUbigeo(deUbigeo.trim());
        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public Lugar getLugar(String coUbigeo){
        try{
            return reporteDao.getLugar(coUbigeo);
        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Lugar> buscarEntidad(String deEntidad){
        try{
            return reporteDao.buscarEntidad(deEntidad.trim());
        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public Lugar getEntidad(String coEntidad){
        try{
            return reporteDao.getEntidad(coEntidad);
        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public Entidad getEntidadUsuarios(String coEntidad){
        String[] split = coEntidad.split("_");
        try {
            return usuarioExternoDao.getEntidad(split[0], split[1]);
        } catch (Exception e) {
            return new Entidad();
        }
    }

    @Override
    public List<EstablecimientoSalud> buscarEstablecimientoSalud(String deEstSalud){
        try{
            return reporteDao.buscarEstablecimientoSalud(deEstSalud);
        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EstablecimientoSalud> buscarEstablecimientoSaludRecienNacido(String deEstSalud, String coUbigeoInei) {
        try{
            return reporteDao.buscarEstablecimientoSaludRecienNacido(deEstSalud, coUbigeoInei);
        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public EstablecimientoSalud getEstablecimientoSalud(String coEstSalud) {

        try{
            String[] partes = coEstSalud.split("_");
            return reporteDao.getEstablecimientoSalud(partes[0], partes[1]);
        }
        catch(Exception dae){
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> getProgramasSociales(){
        List<ProgramaSocial> list = reporteDao.getProgramasSociales();
        //Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> result = new TreeMap<String, Object>();

        for(ProgramaSocial programaSocial: list) {
            if(programaSocial.getCoProgramaSocial().equals("0")) {
               result.put(programaSocial.getCoProgramaSocial(), programaSocial.getDePrograma());
            }
            else {
               result.put(programaSocial.getCoProgramaSocial(), programaSocial.getDeProgramaLarga() + "( " + programaSocial.getDePrograma() + " )");
            }
        }


        return result;
    }
    @Override
    public Map<String, Object> getPrograma(){
        List<ProgramaSocial> list = reporteDao.getProgramasSociales();
        Map<String, Object> result = new HashMap<String, Object>();
        for(ProgramaSocial programaSocial: list) {
            result.put(programaSocial.getDePrograma(), programaSocial.getDePrograma());
        }
        return result;
    }
    @Override
    public List<Lugar> getCantidadesDepartamento() {
        return reporteDao.getCantidadesDepartamento();
    }

    @Override
    public List<Lugar> getCantidadesProvincia(String coUbigeo){
        return reporteDao.getCantidadesProvincia(coUbigeo);
    }

    @Override
    public List<Lugar> getCantidadesDistrito(String coUbigeo){
        return reporteDao.getCantidadDistrito(coUbigeo);
    }

    @Override
    public List<PadronNominal> listarPadronEdadActivo(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.listarPadronEdad(coUbigeoInei, edadAnios, edadMeses, feIni, feFin, tiRegFecha, "1");
    }

    @Override
    public List<PadronNominal> listarPadronEdadObservado(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.listarPadronEdad(coUbigeoInei, edadAnios, edadMeses, feIni, feFin, tiRegFecha, "2");
    }
    @Override
    public List<PadronNominal> listarPadronEdadTodos(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.listarPadronEdad(coUbigeoInei, edadAnios, edadMeses, feIni, feFin, tiRegFecha, "3");
    }

    @Override
    public List<PadronNominal> listarPadronEdadActivo(String coUbigeoInei, String deEdad, String hastaEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.listarPadronEdad(coUbigeoInei, deEdad, hastaEdad, filaIni, filaFin, feIni, feFin, tiRegFecha,"1");
    }
    @Override
    public List<PadronNominal> listarPadronEdadObservado(String coUbigeoInei, String deEdad, String hastaEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.listarPadronEdad(coUbigeoInei, deEdad, hastaEdad, filaIni, filaFin, feIni, feFin, tiRegFecha,"2");
    }
    @Override
    public List<PadronNominal> listarPadronEdadTodos(String coUbigeoInei, String deEdad, String hastaEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.listarPadronEdad(coUbigeoInei, deEdad, hastaEdad, filaIni, filaFin, feIni, feFin, tiRegFecha,"3");
    }

//--
    @Override
    public Integer countListarPadronEdadActivo(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListarPadronEdad(coUbigeoInei, edadAnios, edadMeses, feIni, feFin, tiRegFecha, "1");
    }

    @Override
    public Integer countListarPadronEdadObservado(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListarPadronEdad(coUbigeoInei, edadAnios, edadMeses, feIni, feFin, tiRegFecha, "2");
    }

    @Override
    public Integer countListarPadronEdadTodos(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha) {
        return reporteDao.countListarPadronEdad(coUbigeoInei, edadAnios, edadMeses, feIni, feFin, tiRegFecha, "3");
    }
//--
    @Override
    public List<PadronMovimiento> listadoPadronMovimiento(String feIni, String feFin){
        return reporteDao.listadoPadronMovimiento(feIni, feFin);
    }

    @Override
    public List<PadronMovimiento> listadoPadronMovimiento(String feIni, String feFin, int filaIni, int filaFin) {
        try {
            return reporteDao.listadoPadronMovimiento(feIni, feFin, filaIni, filaFin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer countAllPadronMovimiento(){
        return reporteDao.countAllPadronMovimientos();
    }

    @Override
    public Integer countPadronMovimientos(String feIni, String feFin){
        return reporteDao.countPadronMovimientos(feIni,  feFin);
    }

    @Override
    public List<Ubigeo> listadoPadronesPorUbigeo() {
        return reporteDao.listadoPadronesPorUbigeo();
    }

    @Override
    public List<PadronObservado> listadoObservaciones(String feIni, String feFin){
        return reporteDao.listadoObservaciones(feIni, feFin);
    }

    @Override
    public List<PadronObservado> listadoObservaciones(String feIni, String feFin, int filaIni, int filaFin){
        return reporteDao.listadoObservaciones(feIni, feFin, filaIni, filaFin);
    }

    @Override
    public int countListadoObservaciones(String feIni, String feFin){
        return reporteDao.countListadoObservaciones(feIni, feFin);
    }

    @Override
    public List<PadronNominal> listadoPadronSinDoc(String coUbigeo, String feIni, String feFin, String tiRegFecha, String esPadron){
        try{
            return reporteDao.listadoPadronSinDoc(coUbigeo, feIni, feFin, tiRegFecha, esPadron);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listadoPadronSinDoc(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin, String  tiRegFecha, String esPadron) {
        return reporteDao.listadoPadronSinDoc(coUbigeo, feIni, feFin, filaIni, filaFin, tiRegFecha, esPadron);
    }

    @Override
    public int countListadoPadronSinDoc(String coUbigeo, String feIni, String feFin, String tiRegFecha, String esPadron){
        return reporteDao.countListadoPadronSinDoc(coUbigeo, feIni, feFin, tiRegFecha, esPadron);
    }

    @Override
    public List<Ubigeo> consolidadoEuropan(ReporteEuropan reporteEuropan) {
        try {
            return reporteDao.consolidadoEuropan(reporteEuropan);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> getPadronUbigeoEuropan(String coUbigeoInei, String feIni, String feFin, String deEdad, String hastaEdad, String  tiRegFecha) {
        try {
            return reporteDao.getPadronUbigeoEuropan(coUbigeoInei, feIni, feFin, deEdad, hastaEdad, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesUbigeoActivos(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesRangoFecha(coUbigeoInei, "1", feIni, feFin, filaIni, filaFin, tiRegFecha, tiRegistro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesUbigeoObservados(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesRangoFecha(coUbigeoInei, "2", feIni, feFin, filaIni, filaFin, tiRegFecha, tiRegistro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesUbigeoTodos(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesRangoFecha(coUbigeoInei, "3", feIni, feFin, filaIni, filaFin, tiRegFecha, tiRegistro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int countListarPadronesUbigeoActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.countListaPadronesRangoFecha(coUbigeoInei, "1", feIni, feFin, tiRegFecha, tiRegistro);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int countListarPadronesUbigeoTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.countListaPadronesRangoFecha(coUbigeoInei, "3", feIni, feFin, tiRegFecha, tiRegistro); //TODOS
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int countListarPadronesUbigeoObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.countListaPadronesRangoFecha(coUbigeoInei, "2", feIni, feFin, tiRegFecha, tiRegistro);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesUbigeoActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesRangoFecha(coUbigeoInei, "1", feIni, feFin, tiRegFecha, tiRegistro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesUbigeoObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesRangoFecha(coUbigeoInei, "2", feIni, feFin, tiRegFecha, tiRegistro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesUbigeoTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesRangoFecha(coUbigeoInei, "3", feIni, feFin, tiRegFecha, tiRegistro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesNacidosActivos(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesNacidos(coUbigeoInei,coEstSalud , "1", feNacIni, feNacFin, feIni, feFin, filaIni, filaFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesNacidosObservados(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesNacidos(coUbigeoInei,coEstSalud , "2", feNacIni, feNacFin, feIni, feFin, filaIni, filaFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesNacidosTodos(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesNacidos(coUbigeoInei,coEstSalud , "3", feNacIni, feNacFin, feIni, feFin, filaIni, filaFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesNacidosActivos(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesNacidos(coUbigeoInei, coEstSalud ,  "1", feNacIni, feNacFin, feIni, feFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesNacidosObservados(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesNacidos(coUbigeoInei, coEstSalud ,  "2", feNacIni, feNacFin, feIni, feFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesNacidosTodos(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha) {
        try {
            return reporteDao.listarPadronesNacidos(coUbigeoInei, coEstSalud ,  "3", feNacIni, feNacFin, feIni, feFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListarPadronesNacidosActivos(String coUbigeoInei,String coEstSalud,String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha) {
        try {
            return reporteDao.countListaPadronesNacidos(coUbigeoInei,coEstSalud , "1", feNacIni, feNacFin, feIni, feFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int countListarPadronesNacidosObservados(String coUbigeoInei,String coEstSalud,String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha) {
        try {
            return reporteDao.countListaPadronesNacidos(coUbigeoInei,coEstSalud , "2", feNacIni, feNacFin, feIni, feFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int countListarPadronesNacidosTodos(String coUbigeoInei,String coEstSalud,String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha) {
        try {
            return reporteDao.countListaPadronesNacidos(coUbigeoInei,coEstSalud , "3", feNacIni, feNacFin, feIni, feFin, tiRegFecha);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String getFechaGeneracionConsolidado() {
        return reporteDao.getFechaGeneracionConsolidado();
    }


    @Override
    public List<UsuarioExterno> buscarUsuarioPorDatos(String apPrimer, String apSegundo, String prenombres) {
        return reporteDao.buscarUsuarioPorDatos(apPrimer, apSegundo, prenombres, 1, padronProperties.PAGINA_FILAS);
    }

    @Override
    public List<UsuarioExterno> buscarUsuarioPorDni(String coUsuario) {
        return reporteDao.buscarUsuarioPorDni(coUsuario);
    }


    @Override
    public List<UsuarioExterno> buscaUsuarioPorEntidad(String coEntidad, String esUsuario) {
        return reporteDao.buscaUsuarioPorEntidad(coEntidad, esUsuario);
    }

    //Desde aqui se modifico por Jose Vidal Flores
    @Override
    public List<PadronEdadEESS> listarCantNiñosEdadEESS(String coUbigeoInei, int tiEstablecimiento, int filaIni, int filaFin, String esPadron) {
        try {
            List<PadronEdadEESS> listaPadronEdad = null;
            if (tiEstablecimiento != 1 && tiEstablecimiento != 2 && tiEstablecimiento != 3){listaPadronEdad = null;}
            else {listaPadronEdad = reporteDao.listarCantNiñosEdadEESS(coUbigeoInei, filaIni, filaFin, tiEstablecimiento, esPadron);}
            return listaPadronEdad;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronEdadEESS> listarCantNiñosEdadEESSxFrecAten(String coUbigeoInei, int tiEstablecimiento, int filaIni, int filaFin, String coFrecuenciaAtencion, String esPadron) {
        try {
            List<PadronEdadEESS> listaPadronEdad = null;
            if (tiEstablecimiento != 1 && tiEstablecimiento != 2 && tiEstablecimiento != 3){listaPadronEdad = null;}
            else {listaPadronEdad = reporteDao.listarCantNiñosEdadEESSxFrecAten(coUbigeoInei, filaIni, filaFin, coFrecuenciaAtencion, esPadron);}
            return listaPadronEdad;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListarCantNiñosEdadEESS(String coUbigeoInei, int tiEstablecimiento, String esPadron) {
        try {
            int cantRegistro = 0;
            if (tiEstablecimiento != 1 && tiEstablecimiento != 2 && tiEstablecimiento != 3){cantRegistro = 0;}
            else {cantRegistro = reporteDao.countListaCantNiñosEdadEESS(coUbigeoInei, tiEstablecimiento, esPadron);}
            return cantRegistro;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //nuevo:cambio para frecuencia de atencion
    @Override
    public int countListarCantNiñosEdadEESSxFrecAten(String coUbigeoInei, int tiEstablecimiento, String coFrecuenciaAtencion, String esPadron){
        try {
            int cantRegistro;
            if (tiEstablecimiento != 1 && tiEstablecimiento != 2 && tiEstablecimiento != 3){
                cantRegistro = 0;
            }
            else {
                cantRegistro = reporteDao.countListaCantNiñosEdadEESSxFrecAten(coUbigeoInei, coFrecuenciaAtencion, esPadron);
            }
            return cantRegistro;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<PadronNominal> listarNiñosEdadEESS(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, int filaIni, int filaFin, String feIni,String feFin, String tiRegFecha, String esPadron) {
        try {
            List<PadronNominal> listaNiñosEdad = null;
            if (tiEstablecimiento != 1 && tiEstablecimiento != 2 && tiEstablecimiento != 3){listaNiñosEdad = null;}
            else {
                listaNiñosEdad = reporteDao.listarNiñosEdadEESS(coUbigeoInei, coEstSalud, tiEstablecimiento, nuEdad, filaIni, filaFin, feIni, feFin, tiRegFecha, esPadron);
            }
            return listaNiñosEdad;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarNiñosEdadEESSxFrecAten(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, int filaIni, int filaFin, String coFrecuenciaAtencion, String feIni, String feFin,String tiRegFecha, String esPadron) {
        try {
            List<PadronNominal> listaNiñosEdad = null;
            if (tiEstablecimiento != 1 && tiEstablecimiento != 2 && tiEstablecimiento != 3){listaNiñosEdad = null;}
            else { listaNiñosEdad = reporteDao.listarNiñosEdadEESSxFrecAten(coUbigeoInei, coEstSalud, nuEdad, filaIni, filaFin, coFrecuenciaAtencion, feIni, feFin, tiRegFecha, esPadron);}
            return listaNiñosEdad;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public int countListarNiñosEdadEESS(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, String feIni, String feFin, String tiRegFecha, String esPadron) {
        try {
            int cantRegistro = 0;
            if (tiEstablecimiento != 1 && tiEstablecimiento != 2 && tiEstablecimiento != 3){cantRegistro = 0;}
            else {cantRegistro = reporteDao.countListaNiñosEdadEESS(coUbigeoInei, coEstSalud, tiEstablecimiento, nuEdad, feIni, feFin, tiRegFecha, esPadron);}
            return cantRegistro;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int countListarNiñosEdadEESSxFrecAten(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, String coFrecuenciaAtencion, String feIni, String feFin, String tiRegFecha, String esPadron) {
        try {
            int cantRegistro = 0;
            if (tiEstablecimiento != 1 && tiEstablecimiento != 2 && tiEstablecimiento != 3){cantRegistro = 0;}
            else {cantRegistro = reporteDao.countListaNiñosEdadEESSxFrecAten(coUbigeoInei, coEstSalud, coFrecuenciaAtencion, nuEdad, feIni, feFin, tiRegFecha, esPadron);}
            return cantRegistro;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}