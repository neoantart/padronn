package pe.gob.reniec.padronn.logic.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.padronn.logic.dao.*;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.service.DominioService;
import pe.gob.reniec.padronn.logic.service.PadronObservadoService;
import pe.gob.reniec.padronn.logic.service.PadronService;

import java.util.*;

/**
 * Creado por: Victor Dino Flores Belizario
 * vflores@reniec.gob.pe
 * victordino.fb@gmail.com
 * Date: 30/04/13
 * Time: 04:39 PM
 */
@Service
public class PadronServiceImpl implements PadronService {

    private static final String ESTADO_REGISTRO_INACTIVO = "0";
    private static final String ESTADO_REGISTRO_ACTIVO = "1";
    private static final String MENOR_DESDE_CNV = "7";
    private static final String MADRE_INDOCUMENTADA = "1";
    private static final String SIN_DATOS_MADRE = "2";
    private static final String MENOR_SIN_NOMBRE = "3";
    private static final String ES_OBSERVACION_INACTIVO = "0";

    Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	PadronDao padronDao;

	@Autowired
    PadronObservadoDao padronObservadoDao;

    @Autowired
    DominioService dominioService;

	@Autowired
	PadronProgramaDao padronProgramaDao;

	@Autowired
    PadronObservadoService padronObservadoService;

	@Autowired
	PadronSeguroDao padronSeguroDao;

    @Autowired
	PadronImgDao padronImgDao;

	@Autowired
	Usuario usuario;

    @Autowired
    AniDao aniDao;

    @Autowired
    RcDao rcDao;

	@Override
	public Persona obtenerPorDniEnPersona(String dni) {
		return padronDao.obtenerPorDniEnPersona(dni);
	}

	@Override
	public PadronNominal obtenerPorCodigoPadron(String coPadronNominal) {
		return padronDao.obtenerPorCodigoPadron(coPadronNominal);
	}

    @Override
    public String obtenerDeEstSalud(String coEstSalud, String nuSecEstSalud){
	    return padronDao.obtenerDeEstSalud(coEstSalud, nuSecEstSalud);
    }

    @Override
    public List<Persona> listarMenoresPorDatosMadre(PadronNominal padronNominal, String esPadron) {
        List<Persona> hijosPN = padronDao.listarPorDatosMadre(padronNominal, esPadron);

        ArrayList<Persona> resultado = new ArrayList<Persona>();

        if (hijosPN != null && hijosPN.size() > 0) {
            for (Persona personaPN : hijosPN) {
                personaPN.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
                resultado.add(personaPN);
            }
        }

        return resultado;
    }

    @Override
    public boolean isSusalud(String coPadronNominal) {
        return padronDao.isSusalud(coPadronNominal);
    }

    @Override
    public String obtenerMotivoBaja(String coPadronNominal) {
	    return padronDao.obtenerMotivoBaja(coPadronNominal);
    }

    @Override
    public String obtenerObservacionBaja(String coPadronNominal) {
        return padronDao.obtenerObservacionBaja(coPadronNominal);
    }




    @Override
	public PadronNominal obtenerPorCodigoPadronConDetalles(String coPadronNominal) {
		return padronDao.obtenerPorCodigoPadronConDetalles(coPadronNominal);
	}

    @Override
    public Persona obtenerPersonaPorCoPadron(String coPadronNominal) {
        return padronDao.obtenerDatosPadronPorcoPadron(coPadronNominal);
    }

    @Override
    public boolean isPrecarga(String padronNominal){
        return padronDao.isPrecarga(padronNominal);
    }

    @Override
    public boolean isPrecargaByDni(String nuDni){
        return padronDao.isPrecarga(nuDni);
    }

	/* depurar */

	/*@Override
	public List<PadronNominal> listarPadrones() {
		return padronDao.listarPadrones();
	}*/

	/*@Override
	public List<PadronNominal> listarPadrones(int filaInicio, int filaFinal, PadronNominal.Campo campo) {
		return padronDao.listarPadrones(filaInicio, filaFinal, campo);
	}*/

	/*@Override
	public List<PadronNominal> buscarPadrones(String nuDniMenor, String apPrimerMenor, String apSegundoMenor, String prenombreMenor, int filaInicio, int filaFinal, PadronNominal.Campo campo) {
		return padronDao.buscarPadrones(nuDniMenor, apPrimerMenor, apSegundoMenor, prenombreMenor, filaInicio, filaFinal, campo);
	}*/

	/*@Override
	public int numeroRegistros() {
		return padronDao.numeroRegistros();
	}*/

	@Override
	public int numeroRegistrosPorBusqueda(String nuDniMenor, String apPrimerMenor, String apSegundoMenor, String prenombreMenor) {
		return padronDao.numeroRegistrosPorBusqueda(nuDniMenor, apPrimerMenor, apSegundoMenor, prenombreMenor);
	}

	@Override
	@Transactional
	public boolean guardarPadronNominal(PadronNominal padronNominal, PadronImg padronImg) throws DataIntegrityViolationException {
        String coPadronNominalTemp = padronNominal.getCoPadronNominal();

        boolean flag = false;
        try {
            flag = padronDao.guardarPadronNominal(padronNominal);
        } catch (Exception e) {
            logger.error("Error,", e);
            throw new DataIntegrityViolationException("DataIntegrityViolationException");
        }

        if (!padronImg.isEmpty()) {
            padronImgDao.clearPadronImg(padronNominal.getCoPadronNominal());
            padronImg.setCoPadroNominal(padronNominal.getCoPadronNominal());
            padronImgDao.insertarPadronImg(padronImg);
        }

        padronProgramaDao.clearPadronPrograma(padronNominal.getCoPadronNominal());
        if(padronNominal.getTiProSocial() ==null || padronNominal.getTiProSocial().size()==0){
            List<String> sinPrograma =  new ArrayList<String>();
            sinPrograma.add("0");
            padronNominal.setTiProSocial(sinPrograma);
        }
        if(padronNominal.getTiSeguroMenor()==null || padronNominal.getTiSeguroMenor().size()==0){
            List<String> sinSeguro =  new ArrayList<String>();
            sinSeguro.add("0");
            padronNominal.setTiSeguroMenor(sinSeguro);
        }

        if (padronNominal.getTiProSocial() != null && padronNominal.getTiProSocial().size() > 0) {
            PadronPrograma padronPrograma = new PadronPrograma();
            padronPrograma.setCoPadronNominal(padronNominal.getCoPadronNominal());
            padronPrograma.setUsuCreaAudi(padronNominal.getUsCreaRegistro());
            padronPrograma.setUsuModiAudi(padronNominal.getUsModiRegistro());
            padronPrograma.setNuSec(padronNominal.getNuSec());
            // programas sociales a los que pertenece el niño
            for (String coProgramaSocial : padronNominal.getTiProSocial()) {
                 padronPrograma.setCoProgramaSocial(coProgramaSocial);
                 padronProgramaDao.guardarPadronPrograma(padronPrograma);
                 padronProgramaDao.guardarPadronProgramaHistorico(padronPrograma);
            }
        }

        padronSeguroDao.clearPadronSeguro(padronNominal.getCoPadronNominal());
        if (padronNominal.getTiSeguroMenor() != null && padronNominal.getTiSeguroMenor().size() > 0) {
            PadronSeguro padronSeguro = new PadronSeguro();
            padronSeguro.setCoPadronNominal(padronNominal.getCoPadronNominal());
            padronSeguro.setUsuCreaAudi(padronNominal.getUsCreaRegistro());
            padronSeguro.setUsuModiAudi(padronNominal.getUsModiRegistro());
            padronSeguro.setNuSec(padronNominal.getNuSec());

            for (String coTipoSeguro : padronNominal.getTiSeguroMenor()) {
                 padronSeguro.setCoTipoSeguro(coTipoSeguro);
                 padronSeguroDao.guardarPadronSeguro(padronSeguro);
                 padronSeguroDao.guardarPadronSeguroHistorico(padronSeguro);
            }
        }

        if (flag==true && padronNominal!=null && !coPadronNominalTemp.equals("")) {
            if(( padronNominal.getNuCnv()!=null && !padronNominal.getNuCnv().isEmpty()) && !padronNominal.getApPrimerMenor().isEmpty() && !padronNominal.getPrenombreMenor().isEmpty()) {
                padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, padronNominal.getCoPadronNominal(), MENOR_DESDE_CNV);
            }
            if( padronNominal.getCoDniMadre()!=null && !padronNominal.getCoDniMadre().isEmpty() ) {
                padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, padronNominal.getCoPadronNominal(), MADRE_INDOCUMENTADA);
            }
            if( (padronNominal.getCoDniMadre()!=null && !padronNominal.getCoDniMadre().isEmpty()) && !padronNominal.getApPrimerMadre().isEmpty() &&
                    !padronNominal.getApSegundoMadre().isEmpty() && !padronNominal.getPrenombreMenor().isEmpty()) {
                padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, padronNominal.getCoPadronNominal(), SIN_DATOS_MADRE);//levantamos ya tienen nombre
            }
            if (padronNominal.getPrenombreMenor() != null && !padronNominal.getPrenombreMenor().isEmpty()) {
                List<String> sinPrenombreMenor = dominioService.getDeTipoSinNombre();
                if (sinPrenombreMenor!=null && !sinPrenombreMenor.contains(padronNominal.getPrenombreMenor()) && padronNominal.getPrenombreMenor().length() >= 3) {
                    padronObservadoService.setEsObservado(ES_OBSERVACION_INACTIVO, padronNominal.getCoPadronNominal(), MENOR_SIN_NOMBRE);//levantamos ya tienen nombre
                }
            }

        }
        return flag;
    }

    @Override
    public void actualizarEstadoPadron(PadronNominal padronNominal){
        if(!padronObservadoDao.existeObservacionActiva(padronNominal.getCoPadronNominal()))
            padronDao.setEsPadronInit(padronNominal.getCoPadronNominal(), "1");
    }

	@Override
	public boolean darBajaPadron(PadronNominalBaja padronNominalBaja) {
		padronNominalBaja.setUsCreaAudi(usuario.getLogin());
		padronNominalBaja.setUsModiAudi(usuario.getLogin());
		padronNominalBaja.setEsPadron(ESTADO_REGISTRO_INACTIVO);
		return setEsPadron(padronNominalBaja);
	}

	@Override
	public boolean darAltaPadron(PadronNominalBaja padronNominalBaja) {
		padronNominalBaja.setUsCreaAudi(usuario.getLogin());
		padronNominalBaja.setUsModiAudi(usuario.getLogin());
		padronNominalBaja.setEsPadron(ESTADO_REGISTRO_ACTIVO);
		return setEsPadron(padronNominalBaja);
	}

	@Transactional
    boolean setEsPadron(PadronNominalBaja padronNominalBaja) {
		try {
			padronDao.setEsPadron(padronNominalBaja.getCoPadronNominal(), padronNominalBaja.getEsPadron());
			padronDao.insertPadronNominalBaja(padronNominalBaja);

            if(padronDao.esObservado(ESTADO_REGISTRO_ACTIVO, padronNominalBaja.getCoPadronNominal())) {
               padronDao.setEsObservado(ESTADO_REGISTRO_INACTIVO, padronNominalBaja.getCoPadronNominal());
            }
			return true;
		} catch(DataAccessException dae) {
			logger.error(dae.getMessage());
		}
		return false;
	}

    @Override
    public Integer getCoPadronNominalByDni(String nuDniMenor) {
        return padronDao.getCoPadronNominalByDni(nuDniMenor);
    }

    @Override
    public Integer getCoPadronNominalByCui(String nuCui) {
        return padronDao.getCoPadronNominalByCui(nuCui);
    }

    @Override
    public boolean existeNuDniMenor(String nuDniMenor){
        return padronDao.existeNuDniMenor(nuDniMenor);
    }

    @Override
    public boolean existeNuCui(String nuCui){
        return padronDao.existeNuCui(nuCui);
    }

    @Override
    public List<PadronNominal> buscarDuplicados(PadronNominal padronNominal, String esPadron){
        return padronDao.buscarDuplicados(padronNominal, esPadron);
    }

    @Override
    public List<Persona> listarMenoresPorDniMadreInactivos(PadronNominal padronNominal) {
        List<Persona> hijosPN = padronDao.listarPorDniMadre(padronNominal, ESTADO_REGISTRO_INACTIVO);
        List<Persona> resultado = new ArrayList<Persona>();

        for(Persona personaPN: hijosPN) {
            personaPN.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
            resultado.add(personaPN);
        }
        return resultado;
    }

    @Override
    public boolean isTipoSeguro(String coPadronNominal) {
        return padronDao.isTipoSeguro(coPadronNominal);
    }

    @Override
    public List<Persona> listarMenoresPorDniMadre(PadronNominal padronNominal) {
        List<Persona> hijosPN=padronDao.listarPorDniMadre(padronNominal, ESTADO_REGISTRO_ACTIVO);//activos
        List<Persona> hijosANI=aniDao.listarPorDniMadre(padronNominal, Persona.TipoPersona.MENOR);//en ani
        List<Persona> hijosPNInactivos=padronDao.listarPorDniMadre(padronNominal, ESTADO_REGISTRO_INACTIVO);//inactivos

        ArrayList<Persona> resultado = new ArrayList<Persona>();

        //quitamos los menores inactivos, se mostrara en otra lista
        if (hijosPNInactivos!=null && hijosPNInactivos.size() > 0) {
            for (Persona hijosInactivoPN: hijosPNInactivos) {
                for (int j = 0; j < hijosANI.size(); j++) {
                    if (compararDatosPersonas(hijosInactivoPN, hijosANI.get(j))) {
                        hijosANI.remove(j);
                    }
                }
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

    @Override
    public void insertarPadronHis(String coPadronNominal) {
        padronDao.insertarPadronHis(coPadronNominal);
    }

}