package pe.gob.reniec.padronn.logic.service.impl;

import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;
//import org.apache.xpath.operations.String;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.*;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.BuscarMenorDocumento;
import pe.gob.reniec.padronn.logic.service.BusquedaDeMenorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.String;


@Service
public class BusquedaDeMenorServiceImpl implements BusquedaDeMenorService {

    @Autowired
    PadronDao padronDao;

    @Autowired
    AniDao aniDao;

    @Autowired
    PadronImgDao padronImgDao;

    @Autowired
    RcDao rcDao;

    //@Autowired
    //HvDao hvDao;

    Logger logger = Logger.getLogger(getClass());

    @Override
    public int countListarMenoresPorDatos(String paterno, String materno, String nombres) {
        return padronDao.countBuscarMenorePorNombres(paterno, materno, nombres);
    }

    @Override
    public List<Persona> listarMenoresPorDatos(String paterno, String materno, String nombres, int filaInicio, int filaFinal) {

        List<Persona> padronNominalList = padronDao.buscarMenorePorNombres(paterno, materno, nombres, filaInicio, filaFinal);

        List<Persona> resultado = new ArrayList<Persona>();

        for (Persona persona : padronNominalList) {
            if (persona.getDni() != null && persona.getDni().length() > 0 && persona.getTipoFuente().equals("ANI")) {
                persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.ANI);
                resultado.add(persona);
            } else if (persona.getCodigoPadronNominal() != null && persona.getCodigoPadronNominal().length() > 0 && persona.getTipoFuente().equals("PN")) {
                persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
                resultado.add(persona);
            }
        }
        return resultado;
    }

    /**
     * helper
     * @param personaPN
     * @param personaAni
     * @return
     */
    private boolean compararDatosPersonas(Persona personaPN, Persona personaAni) {
        // si el menor tiene dni en el padron
        if (personaPN.getDni() != null && personaAni.getDni() != null) {
            if(personaPN.getDni().equals(personaAni.getDni())) return true;
        }
        if (personaPN.getPrimerApellido() != null && personaPN.getSegundoApellido() != null
                && personaPN.getNombres() != null && personaPN.getFechaNacimiento() != null
                /*&& personaPN.getCodigoGenero() != null */&& personaAni.getPrimerApellido() != null
                && personaAni.getSegundoApellido() != null && personaAni.getNombres() != null
                && personaAni.getFechaNacimiento() != null /*&& personaAni.getCodigoGenero() != null*/) {
            return personaPN.getPrimerApellido().equals(personaAni.getPrimerApellido())
                    && personaPN.getSegundoApellido().equals(personaAni.getSegundoApellido())
                    && personaPN.getNombres().equals(personaAni.getNombres())
                    && personaPN.getFechaNacimiento().equals(personaAni.getFechaNacimiento())
                    /*&& personaPN.getCodigoGenero().equals(personaAni.getCodigoGenero())*/;
        }
        return false;
    }

    /**
     * helper
     * @param personaPn
     * @param personaAni
     * @return
     */
    private boolean compararDatosMadre(Persona personaPn, Persona personaAni) {
        if (personaPn.getDni() != null && personaAni.getDni() != null) {
            if(personaPn.getDni().equals(personaAni.getDni())) return true;
        }
        if (personaPn.getPrimerApellido() != null && personaPn.getSegundoApellido() != null
                && personaPn.getNombres() != null && personaPn.getFechaNacimiento() != null
                && personaAni.getPrimerApellido() != null
                && personaAni.getSegundoApellido() != null && personaAni.getNombres() != null
                && personaAni.getFechaNacimiento() != null ) {
            return personaPn.getPrimerApellido().equals(personaAni.getPrimerApellido())
                    && personaPn.getSegundoApellido().equals(personaAni.getSegundoApellido())
                    && personaPn.getNombres().equals(personaAni.getNombres())
                    /*&& personaPN.getCodigoGenero().equals(personaAni.getCodigoGenero())*/;
        }
        return false;
    }

    @Override
    public List<Persona> listarMenoresPorDniMadre(String dni) {
        List<Persona> hijosPN=padronDao.listarPorDniMadre(dni,"1");
        List<Persona> hijosPnDef=padronDao.listarPorDniMadreDef(dni);

        List<Persona> hijosANI=aniDao.listarPorDniMadre(dni, Persona.TipoPersona.MENOR);

        List<Persona> personaCNVList = rcDao.listarDatosCNVPorDniMadre(dni);

        ArrayList<Persona> resultado = new ArrayList<Persona>();

        /*for(Persona persona:personaCNVList) {
            if (!padronDao.existeNuCnv(persona.getCnv())) {
                persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.CNV);
                resultado.add(persona);
            }
        }

        //priorisamos resultados del padron y no mostramos los menores(ani) que ya esten en el padron
        if (hijosPN!=null && hijosPN.size() > 0) {
            for (Persona personaPN: hijosPN) {
                for (int j = 0; j < hijosANI.size(); j++) {
                    if (compararDatosPersonas(personaPN, hijosANI.get(j))) {
                        //eliminamos ANI, porque el menor ya esta en el padron
                        hijosANI.remove(j);
                    }
                }
                personaPN.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
                resultado.add(personaPN);
            }
        }
        // agregamos los que quedaron del ani
        if (hijosANI != null && hijosANI.size() > 0) {
            for (Persona personaAni : hijosANI) {
                personaAni.setBaseDatosOrigen(Persona.BaseDatosOrigen.ANI);
                resultado.add(personaAni);
            }
        }*/
        if (hijosPnDef!=null && hijosPnDef.size() > 0) {
            for (Persona personaPN: hijosPnDef) {
                for (int j = 0; j < hijosANI.size(); j++) {
                    if (compararDatosPersonas(personaPN, hijosANI.get(j))) {
                        //eliminamos ANI, porque el menor ya esta en el padron
                        hijosANI.remove(j);
                    }
                }
                personaPN.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
                resultado.add(personaPN);
            }
        }

        HashMap<String, String> claves = new HashMap<String, String>();
        HashMap<String, String> clavesDni = new HashMap<String, String>();
//        ArrayList<Persona> resultado = new ArrayList<Persona>();
        for(Persona persona:personaCNVList) {
            if (!padronDao.existeNuCnv(persona.getCnv())) {
                persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.CNV);
                resultado.add(persona);
            }
        }
        for(Persona persona:hijosPN){
            persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
            resultado.add(persona);
            //claves.put(persona.getCodigoPadronNominal(), "");
            clavesDni.put(persona.getDni(), "");
        }
        for(Persona persona:hijosANI){
            if(!clavesDni.containsKey(persona.getDni())){
                persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.ANI);
                resultado.add(persona);
            }
        }

        return resultado;
    }

    @Override
    public int contarMenoresPorDatos(String paterno, String materno, String nombres) {
        return padronDao.contarRegistrosPorDatos(paterno, materno, nombres) + aniDao.contarRegistrosPorDatos(paterno, materno, nombres, Persona.TipoPersona.MENOR);
    }

    @Override
    public Persona buscarMenorPorDni(String dni) {
        Persona persona = padronDao.obtenerPorDniEnPersona(dni);

        Persona tmp = aniDao.obtenerPorDniEnPersona(dni, Persona.TipoPersona.MENOR);
        if (persona == null) {
            persona = aniDao.obtenerPorDniEnPersona(dni, Persona.TipoPersona.MENOR);
        }
        if(tmp != null) {
            persona.setCoRestri(tmp.getCoRestri());
            persona.setDeRestri(tmp.getDeRestri());
        }

        return persona;
    }


    @Override
    public Persona buscarMenor(BuscarMenorDocumento buscarMenorDocumento) { //pasando tiDoc y nuDoc
        if (buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.DNI.getTiDoc())) {//caso tiDoc = 0 DNI
            Persona persona=null;
            String cnvRC;

            Persona personaPn = padronDao.obtenerPorDniEnPersona(buscarMenorDocumento.getNuDoc());//busqueda en el padron nominal--> PARA EL CASO DE ACTUALIZAR

            if (personaPn == null) {//si no encuentra a la persona en Padron busca en rrcc
                Persona personaRc = rcDao.obtenerMenorDeRRCC(buscarMenorDocumento.getNuDoc());//recuperamos los datos de RRCC(dni=cui)

                if(personaRc!=null) {//caso encuentre en RC

                    if(personaRc.getCnv()==null || personaRc.getCnv().trim() =="" || personaRc.getCnv().trim().equals("0")) {/*si no hay cnv en RC, entonces recuperamos de ANI*/
                        if(personaRc.getHoraNacimiento()!=null && personaRc.getHoraNacimiento()!="" && !personaRc.getHoraNacimiento().isEmpty())
                        {
                            String coPadronnominal = padronDao.obtenerCodigoPadronPorHoraYFecha(personaRc);
                            if(coPadronnominal != "") {
                                padronDao.actualizarPadronNominal(personaRc);//actualiza
                                padronDao.setEsObservado("0", coPadronnominal);
                                return  padronDao.obtenerPorDniEnPersona(buscarMenorDocumento.getNuDoc());//busqueda en el padron nominal--> PARA EL CASO DE ACTUALIZAR
                            }
                            else {
                                return aniDao.obtenerPorDniEnPersona(buscarMenorDocumento.getNuDoc(), Persona.TipoPersona.MENOR);
                            }
                        }
                        return aniDao.obtenerPorDniEnPersona(buscarMenorDocumento.getNuDoc(), Persona.TipoPersona.MENOR);
                    }
                    else if(padronDao.verificaExisteCnv(personaRc.getCnv().trim())){//verificamos si cnv existe en padron
                          padronDao.actualizarPadronNominal(personaRc);//actualiza
                          padronDao.setEsObservado("0",padronDao.obtenerCoPadronNominal(personaRc.getCnv()));//levantar de la bandeja de observados
                          return padronDao.obtenerPorDniEnPersona(buscarMenorDocumento.getNuDoc());//busqueda en el padron nominal--> PARA EL CASO DE ACTUALIZAR
                    }
                    else{
                        return aniDao.obtenerPorDniEnPersona(buscarMenorDocumento.getNuDoc(), Persona.TipoPersona.MENOR);
                    }
                }
                else {//si no encuentra en RC busca en ANI
                    return aniDao.obtenerPorDniEnPersona(buscarMenorDocumento.getNuDoc(), Persona.TipoPersona.MENOR);
                }
            }
            return personaPn;
        }
        if (buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.CUI.getTiDoc())) {//caso tiDoc = 1 CUI
            return padronDao.obtenerPorCuiEnPersona(buscarMenorDocumento.getNuDoc());
        }
        if (buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.CNV.getTiDoc())) {//caso tiDoc = 2 CNV
            return padronDao.obtenerPorCnvEnPersona(buscarMenorDocumento.getNuDoc());
        }
        if (buscarMenorDocumento.getTiDoc().equals(BuscarMenorDocumento.TipoDoc.CO_PADRON.getTiDoc())) { //caso tiDoc = 3 CO_PADRON
            return padronDao.obtenerPorCoPadronNominalEnPersona(buscarMenorDocumento.getNuDoc());
        } else {
            return null;
        }
    }



    @Override
    public List<Persona> buscarNombresSimilares(String paterno, String materno, String nombres) {
        List<Persona> personaNombresSimilares = padronDao.listarNombresSimilares(paterno, materno, nombres);
        List<Persona> personaCoencidenciasPrimerosNombres = padronDao.listarNombresSimilaresPrimerasOcurrencias(paterno, materno, nombres);

        List<Persona> personasNombresSimilares = ListUtils.union(personaNombresSimilares, personaCoencidenciasPrimerosNombres);
        ArrayList<Persona> resultado = new ArrayList<Persona>();
        for (Persona personaTemp : personasNombresSimilares) {
            personaTemp.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
            resultado.add(personaTemp);
        }
        return resultado;
    }

    @Override
    public PadronNominalBaja getMotivoBaja(String codigoPadronNominal) {
        try{
            return padronDao.getMotivoBaja(codigoPadronNominal);
        }catch (Exception e){
            return new PadronNominalBaja();
        }

    }

}