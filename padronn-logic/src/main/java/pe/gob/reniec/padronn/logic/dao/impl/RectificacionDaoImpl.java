package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.PadronObservadoDao;
import pe.gob.reniec.padronn.logic.dao.RectificacionDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.service.PadronObservadoService;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jronal at gmail dot com
 * Date: 17/10/13
 * Time: 06:50 PM
 */
@Repository
public class RectificacionDaoImpl extends AbstractBaseDao implements RectificacionDao{
    @Autowired
    Usuario usuario;

    @Autowired
    PadronObservadoDao padronObservadoDao;


    private static final String ESTADO_REGISTRO_INACTIVO = "0";
    private static final String ESTADO_REGISTRO_ACTIVO = "1";
    private static final String MENOR_DESDE_CNV = "7";
    private static final String MADRE_INDOCUMENTADA = "1";
    private static final String SIN_DATOS_MADRE = "2";
    private static final String ES_OBSERVACION_INACTIVO = "0";

    @Override
    public PadronNominal getDatosMenorCoPadron(String coPadronNominal) {
        String query ="" +
                "select p.co_padron_nominal, " +
                "       p.nu_dni_menor, " +
                "       p.nu_cui, " +
                "       p.ap_primer_menor, " +
                "       p.ap_segundo_menor, " +
                "       p.prenombre_menor, " +
                "       TO_CHAR(p.fe_nac_menor, 'dd/MM/yyyy') feNacMenor, " +
                "       p.co_genero_menor, " +
                "       nvl(ti_vinculo_jefe,'') ti_vinculo_jefe, " +
                "       co_dni_jefe_fam, " +
                "       ap_primer_jefe, " +
                "       ap_segundo_jefe, " +
                "       prenom_jefe, " +
                "       co_dni_madre, " +
                "       ap_primer_madre, " +
                "       ap_segundo_madre, " +
                "       prenom_madre, " +
                "       d1.DE_GENERO_MENOR deGeneroMenor, " +
                "       d2.DE_VINCULO deVinculoJefe, " +
                "       p.di_correo_madre, " +
                "       p.nu_cel_madre " +
                "  from pntv_padron_nominal p " +
                "  left join PNTR_GENERO_MENOR d1 ON d1.CO_GENERO_MENOR = p.co_genero_menor " +
                "  left join PNTR_VINCULO_FAMILIAR d2 on d2.CO_VINCULO = p.ti_vinculo_jefe " +
                " where (p.es_padron = '1' or p.es_padron='2' or p.es_padron='0') " +
                "   and (p.co_padron_nominal=?)";
        try{
            Object[] params = new Object[]{coPadronNominal};
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public PadronNominal getDatosMenorNuDni(String nuDni) {
        String query ="" +
                "select p.co_padron_nominal, " +
                "       p.nu_dni_menor, " +
                "       p.nu_cui, " +
                "       p.ap_primer_menor, " +
                "       p.ap_segundo_menor, " +
                "       p.prenombre_menor, " +
                "       TO_CHAR(p.fe_nac_menor, 'dd/MM/yyyy') feNacMenor, " +
                "       p.co_genero_menor, " +
                "       ti_vinculo_jefe, " +
                "       co_dni_jefe_fam, " +
                "       ap_primer_jefe, " +
                "       ap_segundo_jefe, " +
                "       prenom_jefe, " +
                "       co_dni_madre, " +
                "       ap_primer_madre, " +
                "       ap_segundo_madre, " +
                "       prenom_madre, " +
                "       d1.DE_GENERO_MENOR deGeneroMenor, " +
                "       d2.DE_VINCULO deVinculoJefe, " +
                "       p.di_correo_madre, " +
                "       p.nu_cel_madre " +
                "  from pntv_padron_nominal p " +
                "  left join PNTR_GENERO_MENOR d1 ON d1.CO_GENERO_MENOR = p.co_genero_menor " +
                "  left join PNTR_VINCULO_FAMILIAR d2 on d2.CO_VINCULO = p.ti_vinculo_jefe " +
                " where (p.es_padron = '1' or p.es_padron='2' or p.es_padron='0') " +
                "   and (p.nu_dni_menor=?)";
        try{
            Object[] params = new Object[]{nuDni};
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public PadronNominal getDatosMenorNuCnv(String nuCnv) {
        String query ="" +
                "select p.co_padron_nominal, " +
                "       p.nu_dni_menor, " +
                "       p.nu_cui, " +
                "       p.ap_primer_menor, " +
                "       p.ap_segundo_menor, " +
                "       p.prenombre_menor, " +
                "       TO_CHAR(p.fe_nac_menor, 'dd/MM/yyyy') feNacMenor, " +
                "       p.co_genero_menor, " +
                "       ti_vinculo_jefe, " +
                "       co_dni_jefe_fam, " +
                "       ap_primer_jefe, " +
                "       ap_segundo_jefe, " +
                "       prenom_jefe, " +
                "       co_dni_madre, " +
                "       ap_primer_madre, " +
                "       ap_segundo_madre, " +
                "       prenom_madre, " +
                "       d1.DE_GENERO_MENOR deGeneroMenor, " +
                "       d2.DE_VINCULO deVinculoJefe, " +
                "       p.di_correo_madre, " +
                "       p.nu_cel_madre " +
                "  from pntv_padron_nominal p " +
                "  left join PNTR_GENERO_MENOR d1 ON d1.CO_GENERO_MENOR = p.co_genero_menor " +
                "  left join PNTR_VINCULO_FAMILIAR d2 on d2.CO_VINCULO = p.ti_vinculo_jefe " +
                " where (p.es_padron = '1' or p.es_padron='2' or p.es_padron='0') " +
                "   and (p.nu_cnv=?)";
        try{
            Object[] params = new Object[]{nuCnv};
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public PadronNominal getDatosMenorNuCui(String nuCui) {
        String query ="" +
                "select p.co_padron_nominal, " +
                "       p.nu_dni_menor, " +
                "       p.nu_cui, " +
                "       p.ap_primer_menor, " +
                "       p.ap_segundo_menor, " +
                "       p.prenombre_menor, " +
                "       TO_CHAR(p.fe_nac_menor, 'dd/MM/yyyy') feNacMenor, " +
                "       p.co_genero_menor, " +
                "       ti_vinculo_jefe, " +
                "       co_dni_jefe_fam, " +
                "       ap_primer_jefe, " +
                "       ap_segundo_jefe, " +
                "       prenom_jefe, " +
                "       co_dni_madre, " +
                "       ap_primer_madre, " +
                "       ap_segundo_madre, " +
                "       prenom_madre, " +
                "       d1.DE_GENERO_MENOR deGeneroMenor, " +
                "       d2.DE_VINCULO deVinculoJefe, " +
                "       p.di_correo_madre, " +
                "       p.nu_cel_madre " +
                "  from pntv_padron_nominal p " +
                "  left join PNTR_GENERO_MENOR d1 ON d1.CO_GENERO_MENOR = p.co_genero_menor " +
                "  left join PNTR_VINCULO_FAMILIAR d2 on d2.CO_VINCULO = p.ti_vinculo_jefe " +
                " where (p.es_padron = '1' or p.es_padron='2' or p.es_padron='0') " +
                "   and (p.nu_cui=?)";
        try{
            Object[] params = new Object[]{nuCui};
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Persona getPersonaAni(String nuDni, Persona.TipoPersona tipoPersona) {
        String where = "";
        if (tipoPersona.equals(Persona.TipoPersona.MADRE)) {
            where = "and de_genero=2 ";
        } else if (tipoPersona.equals(Persona.TipoPersona.MAYOR)) {
            where = "and IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(ti_ficha_reg)='"+padronProperties.FICHA_MAYOR+"'";
        } else if (tipoPersona.equals(Persona.TipoPersona.MENOR)) {
            where = "and IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(ti_ficha_reg)='"+padronProperties.FICHA_MENOR+"'  ";
        } else if (tipoPersona.equals(Persona.TipoPersona.PADRE)) {
            where = "and IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(ti_ficha_reg)='"+padronProperties.FICHA_MAYOR+"'" ;
        }

        String query = "" +
                "select nu_dni dni, " +
                "       ap_primer primerApellido, " +
                "       ap_segundo segundoApellido, " +
                "       ap_casada casadaApellido, " +
                "       prenom_inscrito nombres, " +
                "       de_genero codigoGenero, " +
                "       TO_CHAR(fe_nacimiento,'dd/MM/yyyy') as fechaNacimiento, " +
                "       decode(de_genero, 1, 'MASCULINO', 2, 'FEMENINO') genero, " +
                "       nu_telefono," +
                "       de_email" +
                "  from getm_ani " +
                " where nu_dni = ? " + where;
        try{
            Object[] params = new Object[]{nuDni};
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            Persona persona = jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Persona.class), params);
            persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.ANI);
            return persona;
        } catch (EmptyResultDataAccessException ee) {
            log.error(ee.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Menor getDatosPersonalesMenor(String coPadronNominal) {
        String query = "" +
                "select p.co_padron_nominal, " +
                "       p.nu_dni_menor, " +
                "       p.nu_cui, " +
                "       p.nu_cnv, " +
                "       p.ap_primer_menor, " +
                "       p.ap_segundo_menor, " +
                "       p.prenombre_menor, " +
                "       TO_CHAR(p.fe_nac_menor, 'dd/MM/yyyy') feNacMenor, " +
                "       p.co_genero_menor, " +
                "       p.es_padron esPadron, " +
                "       d1.DE_GENERO_MENOR deGeneroMenor " +
                "  from pntv_padron_nominal p " +
                "  left join PNTR_GENERO_MENOR d1 on d1.CO_GENERO_MENOR = p.co_genero_menor " +
                "  left join PNTR_VINCULO_FAMILIAR d2 on d2.CO_VINCULO = p.ti_vinculo_jefe " +
                " where (p.es_padron = '1' or p.es_padron='2' or p.es_padron='0') " + // registro activo o observado
                "   and (p.co_padron_nominal = ? )";
        Object[] params = new Object[]{coPadronNominal};
        try{
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Menor.class), params);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     *  Obtiene datos del jefe de familia del padron nominal
     * @param coPadronNominal
     * @return
     */
    @Override
    public Padre getDatosPersonalesPadre(String coPadronNominal) {
        String query = "" +
                "select " +
                "       p.co_padron_nominal, " +
                "       p.nu_dni_menor, " +
                "       ti_vinculo_jefe, " +
                "       co_dni_jefe_fam, " +
                "       ap_primer_jefe, " +
                "       ap_segundo_jefe, " +
                "       prenom_jefe, " +
                "       d2.DE_VINCULO deVinculoJefe " +
                "  from pntv_padron_nominal p " +
                "  left join PNTR_GENERO_MENOR d1 on d1.CO_GENERO_MENOR = p.co_genero_menor " +
                "  left join PNTR_VINCULO_FAMILIAR d2 on d2.CO_VINCULO = p.ti_vinculo_jefe " +
                " where (p.es_padron = '1' or p.es_padron='2' or p.es_padron='0') " +
                "   and (p.co_padron_nominal = ?) ";
        Object[] params = new Object[]{coPadronNominal};
        try{
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Padre.class), params);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Madre getDatosPersonalesMadre(String coPadronNominal) {
        String query = "" +
                "select p.co_padron_nominal, " +
                "       p.nu_dni_menor, " +
                "       co_dni_madre, " +
                "       ap_primer_madre, " +
                "       ap_segundo_madre, " +
                "       prenom_madre, " +
                "       p.nu_cel_madre, " +
                "       p.di_correo_madre " +
                "  from pntv_padron_nominal p " +
                "  left join PNTR_GENERO_MENOR d1 on d1.CO_GENERO_MENOR = p.co_genero_menor " +
                "  left join PNTR_VINCULO_FAMILIAR d2 on d2.CO_VINCULO = p.ti_vinculo_jefe " +
                /*" where (p.es_padron = '1' or p.es_padron='2') " +*/
                " where (p.es_padron = '1' or p.es_padron='2' or p.es_padron='0') " +
                "   and (p.co_padron_nominal = ?) ";
        Object[] params = new Object[]{coPadronNominal};
        try {
        return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Madre.class), params);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean rectificarDatosMenor(Menor menorPadron) throws DataIntegrityViolationException {
        String nuDniMenor = menorPadron.getNuDniMenor() ;
        String nuCui = menorPadron.getNuCui();
        String updateDniMenor = "";
        List<Object> params = new ArrayList<Object>();
        if (!nuDniMenor.isEmpty()) {
            updateDniMenor = "nu_dni_menor=?, ";
            params.add(nuDniMenor);
            menorPadron.setNuCui("");
            menorPadron.setTiDocIdentidad("1");
        } else {
            if (!nuCui.isEmpty()) {
                menorPadron.setTiDocIdentidad("2");
            } else {
                menorPadron.setTiDocIdentidad("3");
            }
        }
        String query ="" +
                "update pntv_padron_nominal " +
                "set " +
                updateDniMenor +
                "nu_cui=?, " +
                "ap_primer_menor = ?, " +
                "ap_segundo_menor = ?, " +
                "prenombre_menor = ?, " +
                "fe_nac_menor=?, " +
                "co_genero_menor=?, " +
                "ti_doc_identidad=?, " +
                "us_modi_registro=?," +
                "fe_modi_registro=sysdate," +
                "ti_registro='RM' " +
                "where co_padron_nominal=? ";

        params.add(menorPadron.getNuCui());
        params.add(menorPadron.getApPrimerMenor().trim());
        params.add(menorPadron.getApSegundoMenor().trim());
        params.add(menorPadron.getPrenombreMenor().trim());
        params.add(menorPadron.getFeNacMenor());
        params.add(menorPadron.getCoGeneroMenor());
        params.add(menorPadron.getTiDocIdentidad());
        params.add(usuario.getLogin());
        params.add(menorPadron.getCoPadronNominal());

        log.debug(String.format("DAO '%s' con '%s'", query, params));
        int result;
        try {
            PadronNominal padronNominal = getPadronNominal(menorPadron.getCoPadronNominal());
            Rectificacion rectificacion = new Rectificacion();
            rectificacion.setCoPadronNominal(menorPadron.getCoPadronNominal());
            rectificacion.setDeRectificacion(menorPadron.getDeRectificacion());
            rectificacion.setCoEntidad(usuario.getCoEntidad());
            rectificacion.setUsCreaAudi(usuario.getLogin());
            rectificacion.setTiPersona(Rectificacion.TipoPersona.MENOR.getTiPersona());
            rectificacion.setCoDniAnt(padronNominal.getNuDniMenor());
            rectificacion.setNuCuiAnt(padronNominal.getNuCui());
            rectificacion.setApPrimerAnt(padronNominal.getApPrimerMenor());
            rectificacion.setApSegundoAnt(padronNominal.getApSegundoMenor());
            rectificacion.setPrenombresAnt(padronNominal.getPrenombreMenor());
            rectificacion.setFeNacMenorAnt(padronNominal.getFeNacMenor());
            rectificacion.setCoGeneroMenorAnt(padronNominal.getCoGeneroMenor());

            guardarHistorico(menorPadron.getCoPadronNominal());
            result = jdbcTemplate.update(query, params.toArray(new Object[params.size()]));

            if(result==1){
                insertRectificacion(rectificacion);
                padronObservadoDao.setEsObservado(ES_OBSERVACION_INACTIVO, menorPadron.getCoPadronNominal(), MENOR_DESDE_CNV);
                if(!padronObservadoDao.existeObservacionActiva(padronNominal.getCoPadronNominal()))
                    setEsPadron(menorPadron.getCoPadronNominal(), ESTADO_REGISTRO_ACTIVO);
            }
            return result == 1;
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage());
            logger.debug("codigo de cui/Dni ya existe!");
            throw new DataIntegrityViolationException("DataIntegrityViolationException");
        }
    }



    @Override
    public boolean rectificarDatosMadre(Madre madrePadron) {
        String coDniMadre = madrePadron.getCoDniMadre();
        List<Object> params = new ArrayList<Object>();

        String query = "" +
                "update pntv_padron_nominal " +
                "set " +
                "co_dni_madre=?, " +
                "ap_primer_madre = ?, " +
                "ap_segundo_madre = ?, " +
                "prenom_madre = ?," +
                "us_modi_registro=?," +
                "fe_modi_registro=sysdate," +
                "ti_registro='RM' " +
                "where co_padron_nominal=? ";

        params.add(coDniMadre);
        params.add(madrePadron.getApPrimerMadre().trim());
        params.add(madrePadron.getApSegundoMadre().trim());
        params.add(madrePadron.getPrenomMadre().trim());
        params.add(usuario.getLogin());
        params.add(madrePadron.getCoPadronNominal());

        PadronNominal padronNominal = getPadronNominal(madrePadron.getCoPadronNominal());
        Rectificacion rectificacion = new Rectificacion();
        rectificacion.setCoPadronNominal(madrePadron.getCoPadronNominal());
        rectificacion.setDeRectificacion(madrePadron.getDeRectificacion());
        rectificacion.setCoEntidad(usuario.getCoEntidad());
        rectificacion.setUsCreaAudi(usuario.getLogin());
        rectificacion.setTiPersona(Rectificacion.TipoPersona.MADRE.getTiPersona());
        rectificacion.setCoDniAnt(padronNominal.getCoDniMadre());
        rectificacion.setApPrimerAnt(padronNominal.getApPrimerMadre());
        rectificacion.setApSegundoAnt(padronNominal.getApSegundoMadre());
        rectificacion.setPrenombresAnt(padronNominal.getPrenomMadre());

        guardarHistorico(madrePadron.getCoPadronNominal());
        log.debug(String.format("DAO '%s' con '%s'", query, params));
        int result = jdbcTemplate.update(query, params.toArray(new Object[params.size()]));

        if(result==1){
            insertRectificacion(rectificacion);
            padronObservadoDao.setEsObservado(ES_OBSERVACION_INACTIVO, madrePadron.getCoPadronNominal(), MADRE_INDOCUMENTADA);
            if(!padronObservadoDao.existeObservacionActiva(padronNominal.getCoPadronNominal()))
                setEsPadron(madrePadron.getCoPadronNominal(), ESTADO_REGISTRO_ACTIVO);
        }

        return result == 1;
    }

    @Override
    public boolean rectificarDatosPadre(Padre padrePadron) {
        String coDniPadre = padrePadron.getCoDniJefeFam();
        String updateCoDniPadre = "";
        List<Object> params = new ArrayList<Object>();

        String query = "" +
                "update pntv_padron_nominal " +
                "set " +
                "co_dni_jefe_fam=?, " +
                "ap_primer_jefe=?, " +
                "ap_segundo_jefe=?, " +
                "prenom_jefe=?," +
                "ti_vinculo_jefe=?, " +
                "us_modi_registro=?," +
                "fe_modi_registro=sysdate " +
                "where co_padron_nominal=?";
        params.add(coDniPadre);
        params.add(padrePadron.getApPrimerJefe());
        params.add(padrePadron.getApSegundoJefe());
        params.add(padrePadron.getPrenomJefe());
        params.add(padrePadron.getTiVinculoJefe());
        params.add(usuario.getLogin());
        params.add(padrePadron.getCoPadronNominal());


        PadronNominal padronNominal = getPadronNominal(padrePadron.getCoPadronNominal());
        Rectificacion rectificacion = new Rectificacion();
        rectificacion.setCoPadronNominal(padrePadron.getCoPadronNominal());
        rectificacion.setDeRectificacion(padrePadron.getDeRectificacion());
        rectificacion.setCoEntidad(usuario.getCoEntidad());
        rectificacion.setUsCreaAudi(usuario.getLogin());
        rectificacion.setTiPersona(Rectificacion.TipoPersona.PADRE.getTiPersona());
        rectificacion.setCoDniAnt(padronNominal.getCoDniJefeFam());
        rectificacion.setApPrimerAnt(padronNominal.getApPrimerJefe());
        rectificacion.setApSegundoAnt(padronNominal.getApSegundoJefe());
        rectificacion.setPrenombresAnt(padronNominal.getPrenomJefe());

        guardarHistorico(padrePadron.getCoPadronNominal());
        insertRectificacion(rectificacion);
        log.debug(String.format("DAO '%s' con '%s'", query, params));
        int result = jdbcTemplate.update(query, params.toArray(new Object[params.size()]));
        return result == 1;
    }

    @Override
    public void insertRectificacion(Rectificacion rectificacion) {
        String coEntidad = rectificacion.getCoEntidad();
        if (!coEntidad.isEmpty()) {
            String[] split = coEntidad.split("_");
            coEntidad = split[0];
        }
        // registramos log de rectificacion
        int nuSec = getMaxNuSecRectificacion(rectificacion.getCoPadronNominal(), rectificacion.getTiPersona());
        String query = "" +
                "insert into pntl_rectificacion " +
                "  (nu_sec, " +
                "   co_padron_nominal, " +
                "   de_rectificacion, " +
                "   co_entidad, " +
                "   us_crea_audi, " +
                "   fe_crea_audi, " +
                "   ti_persona, " +
                "   co_dni_ant, " +
                "   nu_cui_ant, " +
                "   ap_primer_ant, " +
                "   ap_segundo_ant, " +
                "   prenombres_ant, " +
                "   fe_nac_menor_ant, " +
                "   co_genero_menor_ant " +
                ") " +
                "values " +
                "  (?, " +
                "   ?, " +
                "   ?, " +
                "   ?, " +
                "   ?, " +
                "   sysdate, " +
                "   ?, " +
                "   ?, " +
                "   ?, " +
                "   ?, " +
                "   ?, " +
                "   ?, " +
                "   ?, " +
                "   ? " +
                ")";

        Object[] params = new Object[] {
                nuSec,
                rectificacion.getCoPadronNominal(),
                rectificacion.getDeRectificacion(),
                coEntidad,
                rectificacion.getUsCreaAudi(),
                rectificacion.getTiPersona(),
                rectificacion.getCoDniAnt(),
                rectificacion.getNuCuiAnt(),
                rectificacion.getApPrimerAnt(),
                rectificacion.getApSegundoAnt(),
                rectificacion.getPrenombresAnt(),
                rectificacion.getFeNacMenorAnt(),
                rectificacion.getCoGeneroMenorAnt()
        };

        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try{
            jdbcTemplate.update(query, params);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    @Transactional
    public int getMaxNuSecRectificacion(String coPadronNominal, String tiPersona) {
        String query = "" +
                "select nvl(max(nu_sec)+1, 1) " +
                "  from pntl_rectificacion " +
                " where co_padron_nominal = ? and ti_persona=?";
        Object[] params = new Object[]{coPadronNominal, tiPersona};
        logger.debug(String.format("DAO '%s' con '%s", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params);
    }

    // todo: modificar aqui
    @Override
    public void insertPadronHistorico(PadronNominal padronNominal) {
        padronNominal.setNuSec(getMaxNuSecPadronHistorico(padronNominal.getCoPadronNominal()));
        String queryHistorico = "INSERT INTO "
                + "pnth_padron_nominal ( "
                + "NU_CUI, NU_SEC, CO_PADRON_NOMINAL, NU_DNI_MENOR, TI_DOC_IDENTIDAD, "
                + "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, FE_NAC_MENOR, CO_GENERO_MENOR, "
                + "CO_UBIGEO_INEI, DE_DIRECCION, "
                + "CO_NIVEL_POBREZA, CO_EST_SALUD, TI_SEGURO_MENOR, NU_AFILIACION, CO_INST_EDUCATIVA, "
                + "CO_DNI_JEFE_FAM, AP_PRIMER_JEFE, AP_SEGUNDO_JEFE, PRENOM_JEFE, TI_VINCULO_JEFE, "
                + "CO_DNI_MADRE, AP_PRIMER_MADRE, AP_SEGUNDO_MADRE, PRENOM_MADRE, TI_VINCULO_MADRE, CO_GRA_INST_MADRE, CO_LEN_MADRE, "
                + "US_CREA_REGISTRO, FE_CREA_REGISTRO, US_MODI_REGISTRO, FE_MODI_REGISTRO, CO_ESTADO, TI_REGISTRO, CO_ENTIDAD " +
                ", ES_PADRON  "
                + ") VALUES ( "
                + "?,?,?,?,?,"
//                + "upper(?),upper(?),upper(?),to_date(?, 'dd/MM/yyyy'),?,"
                + "upper(?),upper(?),upper(?),?,?,"
                + "?,upper(?),"
                + "?,?,?,?,?,"
                + "?,upper(?),upper(?),upper(?),?,"
                + "?,upper(?),upper(?),upper(?),?,?,?,"
                + "?,sysdate,?,sysdate,?, 'RM', ? " +
                ", ? " //insert de es_padron en historico siempre debe ser de objeto padronnominal
                + ")";
        //Insertamos un nuevo registro histórico
        Object[] params = new Object[]{padronNominal.getNuCui(), padronNominal.getNuSec(), padronNominal.getCoPadronNominal(), padronNominal.getNuDniMenor(), padronNominal.getTiDocIdentidad(),
                padronNominal.getApPrimerMenor(), padronNominal.getApSegundoMenor(), padronNominal.getPrenombreMenor(), padronNominal.getFeNacMenor(), padronNominal.getCoGeneroMenor(),
                padronNominal.getCoUbigeoInei(), padronNominal.getDeDireccion(),
                padronNominal.getCoNivelPobreza(), padronNominal.getCoEstSalud(), padronNominal.getTiSeguroMenor(), padronNominal.getNuAfiliacion(), padronNominal.getCoInstEducativa(),
                padronNominal.getCoDniJefeFam(), padronNominal.getApPrimerJefe(), padronNominal.getApSegundoJefe(), padronNominal.getPrenomJefe(), padronNominal.getTiVinculoJefe(),
                padronNominal.getCoDniMadre(), padronNominal.getApPrimerMadre(), padronNominal.getApSegundoMadre(), padronNominal.getPrenomMadre(), padronNominal.getTiVinculoMadre(), padronNominal.getCoGraInstMadre(), padronNominal.getCoLenMadre(),
                padronNominal.getUsCreaRegistro(), padronNominal.getUsModiRegistro(), padronNominal.getCoEstado(), padronNominal.getCoEntidad()
                , padronNominal.getEsPadron()
        };
        log.debug(String.format("DAO '%s' con '%s'", queryHistorico, params));
        jdbcTemplate.update(queryHistorico, params);
        /*try{
            jdbcTemplate.update(queryHistorico, params);
        } catch (RuntimeException e){
            log.error(e.getMessage());
        }*/
    }

    @Override
    @Transactional
    public int getMaxNuSecPadronHistorico(String coPadronNominal) {
        String query = "select nvl(max(nu_sec)+1,1) from pnth_padron_nominal where co_padron_nominal=?";
        Object[] params = new Object[]{coPadronNominal};
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params);
    }

    @Override
    public PadronNominal getPadronNominal(String coPadronNominal){
        String query = "" +
                "select co_padron_nominal, " +
                "       nu_dni_menor, " +
                "       ap_primer_menor, " +
                "       ap_segundo_menor, " +
                "       prenombre_menor, " +
                "       to_char(fe_nac_menor, 'dd/mm/yyyy') fe_nac_menor, " +
                "       co_ubigeo_inei, " +
                "       co_est_salud, " +
                "       co_genero_menor, " +
                "       ti_doc_identidad, " +
                "       de_direccion, " +
                "       ti_seguro_menor, " +
                "       ti_pro_social, " +
                "       co_inst_educativa, " +
                "       co_dni_jefe_fam, " +
                "       ap_primer_jefe, " +
                "       ap_segundo_jefe, " +
                "       prenom_jefe, " +
                "       ti_vinculo_jefe, " +
                "       co_dni_madre, " +
                "       ap_primer_madre, " +
                "       ap_segundo_madre, " +
                "       prenom_madre, " +
                "       ti_vinculo_madre, " +
                "       co_gra_inst_madre, " +
                "       co_len_madre, " +
                "       co_nivel_pobreza, " +
                "       us_crea_registro, " +
                "       fe_crea_registro, " +
                "       us_modi_registro, " +
                "       fe_modi_registro, " +
                "       co_entidad, " +
                "       co_etnia, " +
                "       nu_afiliacion, " +
                "       ti_registro, " +
                "       es_padron, " +
                "       nu_cui " +
                "  from pntv_padron_nominal " +
                "where co_padron_nominal=? and (es_padron='1' or es_padron='2')";
        log.debug(String.format("DAO '%s' con '%s'", query, coPadronNominal));
        try {
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), coPadronNominal);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Menor> buscarMenor(String apPrimerMenor, String apSegundoMenor, String prenombresMenor, int filaIni, int filaFin) {

        List<Object> params = new ArrayList<Object>();

        String where = "where es_padron<>'9' and";

        if (!apPrimerMenor.isEmpty()) {
            where += " ap_primer_menor like ? and";
            params.add("%" + apPrimerMenor + "%");
        }
        if (!apSegundoMenor.isEmpty()) {
            where += " ap_segundo_menor like ? and";
            params.add("%" + apSegundoMenor + "%");
        }
        if (!prenombresMenor.isEmpty()) {
            where += " prenombre_menor like ? and";
            params.add("%" + prenombresMenor + "%");
        }

        if (where.equals("where es_padron<>'9' and es_padron<>'0' and"))
            return null;
        if (where.endsWith("and")) {
            where = where.substring(0, where.length() - 3);
        }

        String query = "" +
                "select * " +
                "  from (select rownum as fila, t.* " +
                "          from (select co_padron_nominal, " +
                "                       nu_dni_menor, " +
                "                       ap_primer_menor, " +
                "                       ap_segundo_menor, " +
                "                       prenombre_menor, " +
                "                       to_char(fe_nac_menor, 'dd/MM/yyyy') as FeNacMenor," +
                "                       es_padron esPadron " +
                "                  from pntv_padron_nominal " +
                where +
                "                  ) t) r " +
                " where fila between ? and ? ";
        try{
            params.add(filaIni);
            params.add(filaFin);
            log.debug(String.format("DAO '%s' con '%s'", query, params));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Menor.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public int countBuscarMenor(String apPrimerMenor, String apSegundoMenor, String prenombresMenor) {
        List<Object> params = new ArrayList<Object>();

        String where = "where es_padron<>'9' and";

        if (!apPrimerMenor.isEmpty()) {
            where += " ap_primer_menor like ? and";
            params.add("%" + apPrimerMenor + "%");
        }
        if (!apSegundoMenor.isEmpty()) {
            where += " ap_segundo_menor like ? and";
            params.add("%" + apSegundoMenor + "%");
        }
        if (!prenombresMenor.isEmpty()) {
            where += " prenombre_menor like ? and";
            params.add("%" + prenombresMenor + "%");
        }

        if (where.equals("where es_padron<>'9' and"))
            return 0;
        if (where.endsWith("and")) {
            where = where.substring(0, where.length() - 3);
        }

        String query = "" +
                "select count(1) " +
                "  from pntv_padron_nominal " +
                where;
        log.debug(String.format("DAO '%s' con '%s'", query, params));
        return jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }

    @Override
    public boolean buscarCuiDuplicado(String nuCui) {
        String query =
                "select count(1) from pntv_padron_nominal where nu_cui=?";
        return false;
    }

    @Override
    public List<Rectificacion> getRectificaciones(String coPadronNominal, String tiPersona) {
        String query = "" +
                "select nu_sec, " +
                "       co_padron_nominal, " +
                "       de_rectificacion, " +
                "       co_entidad, " +
                "       us_crea_audi, " +
                "       fe_crea_audi, " +
                "       ti_persona, " +
                "       co_dni_ant, " +
                "       nu_cui_ant, " +
                "       ap_primer_ant, " +
                "       ap_segundo_ant, " +
                "       prenombres_ant, " +
                "       fe_nac_menor_ant, " +
                "       co_genero_menor_ant " +
                "  from pntl_rectificacion " +
                " where co_padron_nominal = ? " +
                "   and ti_persona = ? " +
                " order by nu_sec desc";
        Object[] params = new Object[]{coPadronNominal, tiPersona};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Rectificacion.class), params);
        } catch (EmptyResultDataAccessException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void guardarHistorico(String coPadronNominal){

        String queryHistory = "INSERT INTO PNTH_PADRON_NOMINAL\n " +
                " (CO_PADRON_NOMINAL,\n " +
                "  NU_DNI_MENOR,\n " +
                "  AP_PRIMER_MENOR,\n " +
                "  AP_SEGUNDO_MENOR,\n " +
                "  PRENOMBRE_MENOR,\n " +
                "  FE_NAC_MENOR,\n " +
                "  HO_NAC_MENOR,\n " +
                "  CO_UBIGEO_INEI,\n " +
                "  CO_EST_SALUD,\n " +
                "  CO_GENERO_MENOR,\n " +
                "  TI_DOC_IDENTIDAD,\n " +
                "  CO_VIA,\n" +
                "  DE_REF_DIR, \n" +
                "  DE_DIRECCION,\n " +
                "  TI_PRO_SOCIAL,\n " +
                "  CO_INST_EDUCATIVA,\n " +
                "  CO_DNI_JEFE_FAM,\n " +
                "  AP_PRIMER_JEFE,\n " +
                "  AP_SEGUNDO_JEFE,\n " +
                "  PRENOM_JEFE,\n " +
                "  TI_VINCULO_JEFE,\n " +
                "  CO_DNI_MADRE,\n " +
                "  AP_PRIMER_MADRE,\n " +
                "  AP_SEGUNDO_MADRE,\n " +
                "  PRENOM_MADRE,\n " +
                "  TI_VINCULO_MADRE,\n " +
                "  CO_GRA_INST_MADRE,\n " +
                "  CO_LEN_MADRE,\n " +
                "  CO_NIVEL_POBREZA,\n " +
                "  US_CREA_REGISTRO,\n " +
                "  FE_CREA_REGISTRO,\n " +
                "  US_MODI_REGISTRO,\n " +
                "  FE_MODI_REGISTRO,\n " +
                "  CO_ENTIDAD,\n " +
                "  CO_ETNIA,\n " +
                "  NU_AFILIACION,\n " +
                "  NU_SEC,\n " +
                "  TI_REGISTRO,\n " +
                "  ES_PADRON,\n " +
                "  NU_CUI, " +
                "  NU_SECUENCIA_LOCAL," +
                "  CO_CENTRO_POBLADO," +
                "  NU_CNV," +
                "  CO_EST_SALUD_NAC," +
                "  CO_EST_SALUD_ADS," +
                "  NU_SECUENCIA_LOCAL_NAC," +
                "  NU_SECUENCIA_LOCAL_ADS,"+
                "  NU_CEL_MADRE,"+
                "  DI_CORREO_MADRE,"+
                "  FE_VISITA,"+
                "  IN_MENOR_ENC,"+
                "  IN_MENOR_VISITADO,"+
                "  CO_FUENTE_DATOS,"+
                "  FE_ULTIMA_TOMA_DATOS," +
                "  FUENTE_PRECARGA," +
                "  CO_ENTIDAD_EESS, "+
                "  CO_FRECUENCIA_ATENCION "+
                ")\n " +
                " SELECT CO_PADRON_NOMINAL,\n " +
                "        NU_DNI_MENOR,\n " +
                "        AP_PRIMER_MENOR,\n " +
                "        AP_SEGUNDO_MENOR,\n " +
                "        PRENOMBRE_MENOR,\n " +
                "        FE_NAC_MENOR,\n " +
                "        HO_NAC_MENOR,\n " +
                "        CO_UBIGEO_INEI,\n " +
                "        CO_EST_SALUD,\n " +
                "        CO_GENERO_MENOR,\n " +
                "        TI_DOC_IDENTIDAD,\n " +
                "        CO_VIA,\n " +
                "        DE_REF_DIR, \n" +
                "        DE_DIRECCION,\n " +
                "        TI_PRO_SOCIAL,\n " +
                "        CO_INST_EDUCATIVA,\n " +
                "        CO_DNI_JEFE_FAM,\n " +
                "        AP_PRIMER_JEFE,\n " +
                "        AP_SEGUNDO_JEFE,\n " +
                "        PRENOM_JEFE,\n " +
                "        TI_VINCULO_JEFE,\n " +
                "        CO_DNI_MADRE,\n " +
                "        AP_PRIMER_MADRE,\n " +
                "        AP_SEGUNDO_MADRE,\n " +
                "        PRENOM_MADRE,\n " +
                "        TI_VINCULO_MADRE,\n " +
                "        CO_GRA_INST_MADRE,\n " +
                "        CO_LEN_MADRE,\n " +
                "        CO_NIVEL_POBREZA,\n " +
                "        US_CREA_REGISTRO,\n " +
                "        FE_CREA_REGISTRO,\n " +
                "        US_MODI_REGISTRO,\n " +
                "        FE_MODI_REGISTRO,\n " +
                "        CO_ENTIDAD,\n " +
                "        CO_ETNIA,\n " +
                "        NU_AFILIACION,\n " +
                "        (SELECT NVL(MAX(PH.NU_SEC), 0) + 1\n " +
                "           FROM PNTH_PADRON_NOMINAL PH\n " +
                "          WHERE PH.CO_PADRON_NOMINAL = ?),\n " +
                "        TI_REGISTRO,\n " +
                "        ES_PADRON,\n " +
                "        NU_CUI,\n " +
                "        NU_SECUENCIA_LOCAL,\n " +
                "        CO_CENTRO_POBLADO," +
                "        NU_CNV," +
                "        CO_EST_SALUD_NAC," +
                "        CO_EST_SALUD_ADS," +
                "        NU_SECUENCIA_LOCAL_NAC," +
                "        NU_SECUENCIA_LOCAL_ADS," +
                "        NU_CEL_MADRE," +
                "        DI_CORREO_MADRE," +
                "        FE_VISITA," +
                "        IN_MENOR_ENC," +
                "        IN_MENOR_VISITADO," +
                "        CO_FUENTE_DATOS," +
                "        FE_ULTIMA_TOMA_DATOS," +
                "        FUENTE_PRECARGA," +
                "        CO_ENTIDAD_EESS, " +
                "        CO_FRECUENCIA_ATENCION " +
                "  FROM PNTV_PADRON_NOMINAL PN \n " +
                "  WHERE PN.CO_PADRON_NOMINAL = ?";
        Object[] params = new Object[]{
                Integer.valueOf(coPadronNominal),
                Integer.valueOf(coPadronNominal)};
        log.debug("DAO query='"+queryHistory+"'");
        log.debug("DAO params='"+ ArrayUtils.toString(params)+"'");
        this.jdbcTemplate.update(queryHistory, params);
    }


    @Override
    public boolean setEsPadron(String coPadronNominal, String esPadron) {
        String query = "" +
                "UPDATE PNTV_PADRON_NOMINAL " +
                "SET ES_PADRON=? " +
                "WHERE CO_PADRON_NOMINAL=?";
        Object[] params = {esPadron, coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.update(query, params) == 1;
    }



}