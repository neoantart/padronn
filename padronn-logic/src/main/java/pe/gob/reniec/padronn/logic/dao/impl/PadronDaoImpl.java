
package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.*;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.service.DominioService;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Repository
public class PadronDaoImpl extends SimpleJdbcDaoBase implements PadronDao {

    // Descripcion de Restricciones
    private static final String DE_RESTRICCION_FALLECIMIENTO = "FALLECIMIENTO";
    private static final String DE_RESTRICCION_MULTIPLE_INSCRIPCION = "MULTIPLE INSCRIPCION/IDENTIDAD";
    private static final String ES_PADRON_OBSERVADO = "2";
    private static final int ES_POSTERIOR = 1;
    private static final int ES_ANTERIOR = 0;


    @Autowired
    public PadronProperties padronProperties;

    @Autowired
    PadronImgDao padronImgDao;

    @Autowired
    PadronObservadoDao padronObservadoDao;

    @Autowired
    DominioService dominioService;

    @Override
    public Persona obtenerPorDniEnPersona(String nuDni) {
        String query = "" +
                "select \n" +
                "p.nu_cui nuCUI, p.co_padron_nominal codigoPadronNominal, p.nu_dni_menor dni, p.ti_doc_identidad tipoDocumento, \n" +
                "trunc((months_between(sysdate, p.fe_nac_menor))/12) edad, \n" +
                "trunc((months_between(sysdate, p.fe_nac_menor))/12)||' año(s), '||trunc(mod((months_between(sysdate, p.fe_nac_menor)),12))||' mes(es)' edadEscrita, \n" +
                "p.ap_primer_menor primerApellido, p.ap_segundo_menor segundoApellido, p.prenombre_menor nombres, TO_CHAR(p.fe_nac_menor,'dd/MM/yyyy') fechaNacimiento, decode(p.co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero, \n" +
                "p.de_direccion direccion, \n" +
                "tv.de_cat_via_det || v.de_via as de_via,\n" +
                "cp.de_area_ccpp as de_area_ccpp,\n" +
                "p.de_ref_dir as de_ref_dir,\n" +
                "p.co_ubigeo_inei as codigoUbigeo,\n" +
                "u.de_departamento||', '||u.de_provincia||', '||u.de_distrito as ubigeo, \n" +
                "p.co_centro_poblado as codigoCentroPoblado, decode(p.co_centro_poblado, null, null, '', null, cp.no_centro_poblado || ', ' || cp.no_categoria ) as deCentroPoblado, \n" +
                "p.co_dni_jefe_fam as padreDni, p.ap_primer_jefe as padrePrimerApellido, p.ap_segundo_jefe as padreSegundoApellido, p.prenom_jefe as padreNombres, dj.de_vinculo as padreTipoVinculo, \n" +
                "p.co_dni_madre as madreDni, p.ap_primer_madre as madrePrimerApellido, p.ap_segundo_madre as madreSegundoApellido, p.prenom_madre as madreNombres, dm.de_vinculo as madreTipoVinculo, \n" +
                "p.es_padron as esPadron, \n" +
                "p.co_est_salud_nac, \n" +
                "es_nac.de_est_salud de_est_salud_nac, \n" +
                "p.nu_cnv as cnv \n" +
                "from pntv_padron_nominal p \n" +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' \n" +
                "left join PNTR_VINCULO_FAMILIAR dj on dj.co_vinculo=p.ti_vinculo_jefe \n" +
                "left join PNTR_VINCULO_FAMILIAR dm on dm.co_vinculo=p.ti_vinculo_madre \n" +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' \n" +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac \n" +
                "left join pntm_via v on v.co_via=p.co_via\n" +
                "left join pntr_tipo_via tv on tv.co_cat_via=v.co_cat_via\n" +
                "where nu_dni_menor=? and p.es_padron<>'9'\n";
        try {
            Object[] params = new Object[]{nuDni};
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            Persona persona = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
            persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
            nuDni = persona.getDni();
            if (nuDni != null && !nuDni.isEmpty()) {
                persona.setOrigenFoto(Persona.OrigenFoto.ANI);
            } else if (padronImgDao.tieneImagen(persona.getCodigoPadronNominal())) {
                persona.setOrigenFoto(Persona.OrigenFoto.PN);
            } else {
                persona.setOrigenFoto(Persona.OrigenFoto.EMPTY);
            }
            return persona;
        } catch (Exception e) {
            logger.error("Error:", e);
            return null;
        }
    }


    @Override
    public Persona obtenerPorCoPadronNominalEnPersona(String coPadronNominal) {
        String query = "" +
                "select \n" +
                "p.nu_cui nuCUI, p.co_padron_nominal codigoPadronNominal, p.nu_dni_menor dni, p.ti_doc_identidad tipoDocumento, \n" +
                "trunc((months_between(sysdate, p.fe_nac_menor))/12) edad, \n" +
                "trunc((months_between(sysdate, p.fe_nac_menor))/12)||' año(s), '||trunc(mod((months_between(sysdate, p.fe_nac_menor)),12))||' mes(es)' edadEscrita, \n" +
                "p.ap_primer_menor primerApellido, p.ap_segundo_menor segundoApellido, p.prenombre_menor nombres, TO_CHAR(p.fe_nac_menor,'dd/MM/yyyy') fechaNacimiento, decode(p.co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero, \n" +
                "p.de_direccion direccion, \n" +
                "tv.de_cat_via_det || v.de_via as de_via,\n" +
                "cp.de_area_ccpp as de_area_ccpp,\n" +
                "p.de_ref_dir as de_ref_dir,\n" +
                "p.co_ubigeo_inei as codigoUbigeo,\n" +
                "u.de_departamento||', '||u.de_provincia||', '||u.de_distrito as ubigeo, \n" +
                "p.co_centro_poblado as codigoCentroPoblado, decode(p.co_centro_poblado, null, null, '', null, cp.no_centro_poblado || ', ' || cp.no_categoria ) as deCentroPoblado, \n" +
                "p.co_dni_jefe_fam as padreDni, p.ap_primer_jefe as padrePrimerApellido, p.ap_segundo_jefe as padreSegundoApellido, p.prenom_jefe as padreNombres, dj.de_vinculo as padreTipoVinculo, \n" +
                "p.co_dni_madre as madreDni, p.ap_primer_madre as madrePrimerApellido, p.ap_segundo_madre as madreSegundoApellido, p.prenom_madre as madreNombres, dm.de_vinculo as madreTipoVinculo, \n" +
                "p.es_padron as esPadron, \n" +
                "p.co_est_salud_nac, \n" +
                "es_nac.de_est_salud de_est_salud_nac, nu_cnv as cnv \n" +
                "from pntv_padron_nominal p \n" +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' \n" +
                "left join PNTR_VINCULO_FAMILIAR dj on dj.co_vinculo=p.ti_vinculo_jefe \n" +
                "left join PNTR_VINCULO_FAMILIAR dm on dm.co_vinculo=p.ti_vinculo_madre \n" +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' \n" +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac \n" +
                "left join pntm_via v on v.co_via=p.co_via\n" +
                "left join pntr_tipo_via tv on tv.co_cat_via=v.co_cat_via\n" +
                "where co_padron_nominal=? and p.es_padron<>'9'\n";
        try {
            Object[] params = new Object[]{coPadronNominal};
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            Persona persona = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
            persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
            String nuDni = persona.getDni();
            if (nuDni != null && !nuDni.isEmpty()) {
                persona.setOrigenFoto(Persona.OrigenFoto.ANI);
            } else if (padronImgDao.tieneImagen(persona.getCodigoPadronNominal())) {
                persona.setOrigenFoto(Persona.OrigenFoto.PN);
            } else {
                persona.setOrigenFoto(Persona.OrigenFoto.EMPTY);
            }
            return persona;
        } catch (Exception e) {
            logger.error("Error:", e);
            return null;
        }
    }

    @Override
    public Persona obtenerPorCuiEnPersona(String nuCui) {
        String query = "" +
                "select \n" +
                "p.nu_cui nuCUI, p.co_padron_nominal codigoPadronNominal, p.nu_dni_menor dni, p.ti_doc_identidad tipoDocumento, \n" +
                "trunc((months_between(sysdate, p.fe_nac_menor))/12) edad, \n" +
                "trunc((months_between(sysdate, p.fe_nac_menor))/12)||' año(s), '||trunc(mod((months_between(sysdate, p.fe_nac_menor)),12))||' mes(es)' edadEscrita, \n" +
                "p.ap_primer_menor primerApellido, p.ap_segundo_menor segundoApellido, p.prenombre_menor nombres, TO_CHAR(p.fe_nac_menor,'dd/MM/yyyy') fechaNacimiento, decode(p.co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero, \n" +
                "p.de_direccion direccion, \n" +
                "tv.de_cat_via_det || v.de_via as de_via,\n" +
                "cp.de_area_ccpp as de_area_ccpp,\n" +
                "p.de_ref_dir as de_ref_dir,\n" +
                "p.co_ubigeo_inei as codigoUbigeo,\n" +
                "u.de_departamento||', '||u.de_provincia||', '||u.de_distrito as ubigeo, \n" +
                "p.co_centro_poblado as codigoCentroPoblado, decode(p.co_centro_poblado, null, null, '', null, cp.no_centro_poblado || ', ' || cp.no_categoria ) as deCentroPoblado, \n" +
                "p.co_dni_jefe_fam as padreDni, p.ap_primer_jefe as padrePrimerApellido, p.ap_segundo_jefe as padreSegundoApellido, p.prenom_jefe as padreNombres, dj.de_vinculo as padreTipoVinculo, \n" +
                "p.co_dni_madre as madreDni, p.ap_primer_madre as madrePrimerApellido, p.ap_segundo_madre as madreSegundoApellido, p.prenom_madre as madreNombres, dm.de_vinculo as madreTipoVinculo, \n" +
                "p.es_padron as esPadron, \n" +
                "p.co_est_salud_nac, \n" +
                "es_nac.de_est_salud de_est_salud_nac, nu_cnv as cnv \n" +
                "from pntv_padron_nominal p \n" +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' \n" +
                "left join PNTR_VINCULO_FAMILIAR dj on dj.co_vinculo=p.ti_vinculo_jefe \n" +
                "left join PNTR_VINCULO_FAMILIAR dm on dm.co_vinculo=p.ti_vinculo_madre \n" +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' \n" +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac \n" +
                "left join pntm_via v on v.co_via=p.co_via\n" +
                "left join pntr_tipo_via tv on tv.co_cat_via=v.co_cat_via\n" +
                "where nu_cui=? and p.es_padron<>'9'\n";
        try {
            Object[] params = new Object[]{nuCui};
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            Persona persona = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
            persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
            String nuDni = persona.getDni();
            if (nuDni != null && !nuDni.isEmpty()) {
                persona.setOrigenFoto(Persona.OrigenFoto.ANI);
            } else if (padronImgDao.tieneImagen(persona.getCodigoPadronNominal())) {
                persona.setOrigenFoto(Persona.OrigenFoto.PN);
            } else {
                persona.setOrigenFoto(Persona.OrigenFoto.EMPTY);
            }
            return persona;
        } catch (Exception e) {
            logger.error("Error:", e);
            return null;
        }
    }

    @Override
    public Persona obtenerPorCnvEnPersona(String nuCnv) {
        String query = "" +
                "select \n" +
                "p.nu_cui nuCUI, p.co_padron_nominal codigoPadronNominal, p.nu_dni_menor dni, p.ti_doc_identidad tipoDocumento, \n" +
                "trunc((months_between(sysdate, p.fe_nac_menor))/12) edad, \n" +
                "trunc((months_between(sysdate, p.fe_nac_menor))/12)||' año(s), '||trunc(mod((months_between(sysdate, p.fe_nac_menor)),12))||' mes(es)' edadEscrita, \n" +
                "p.ap_primer_menor primerApellido, p.ap_segundo_menor segundoApellido, p.prenombre_menor nombres, TO_CHAR(p.fe_nac_menor,'dd/MM/yyyy') fechaNacimiento, decode(p.co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero, \n" +
                "p.de_direccion direccion, \n" +
                "tv.de_cat_via_det || v.de_via as de_via,\n" +
                "cp.de_area_ccpp as de_area_ccpp,\n" +
                "p.de_ref_dir as de_ref_dir,\n" +
                "p.co_ubigeo_inei as codigoUbigeo,\n" +
                "u.de_departamento||', '||u.de_provincia||', '||u.de_distrito as ubigeo, \n" +
                "p.co_centro_poblado as codigoCentroPoblado, decode(p.co_centro_poblado, null, null, '', null, cp.no_centro_poblado || ', ' || cp.no_categoria ) as deCentroPoblado, \n" +
                "p.co_dni_jefe_fam as padreDni, p.ap_primer_jefe as padrePrimerApellido, p.ap_segundo_jefe as padreSegundoApellido, p.prenom_jefe as padreNombres, dj.de_vinculo as padreTipoVinculo, \n" +
                "p.co_dni_madre as madreDni, p.ap_primer_madre as madrePrimerApellido, p.ap_segundo_madre as madreSegundoApellido, p.prenom_madre as madreNombres, dm.de_vinculo as madreTipoVinculo, \n" +
                "p.es_padron as esPadron, \n" +
                "p.co_est_salud_nac, \n" +
                "es_nac.de_est_salud de_est_salud_nac," +
                "p.nu_cnv as cnv \n" +
                "from pntv_padron_nominal p \n" +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' \n" +
                "left join PNTR_VINCULO_FAMILIAR dj on dj.co_vinculo=p.ti_vinculo_jefe \n" +
                "left join PNTR_VINCULO_FAMILIAR dm on dm.co_vinculo=p.ti_vinculo_madre \n" +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' \n" +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac \n" +
                "left join pntm_via v on v.co_via=p.co_via\n" +
                "left join pntr_tipo_via tv on tv.co_cat_via=v.co_cat_via\n" +
                "where nu_cnv=? and p.es_padron<>'9'\n";
        try {
            Object[] params = new Object[]{nuCnv};
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            Persona persona = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
            persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.PN);
            String nuDni = persona.getDni();
            if (nuDni != null && !nuDni.isEmpty()) {
                persona.setOrigenFoto(Persona.OrigenFoto.ANI);
            } else if (padronImgDao.tieneImagen(persona.getCodigoPadronNominal())) {
                persona.setOrigenFoto(Persona.OrigenFoto.PN);
            } else {
                persona.setOrigenFoto(Persona.OrigenFoto.EMPTY);
            }
            return persona;
        } catch (Exception e) {
            logger.error("Error:", e);
            return null;
        }
    }

    @Override
    public List<Persona> listarPorDatosEnPersona(String apPrimer, String apSegundo, String prenombre, int filaInicio, int filaFinal) {
        String where = "";
        List<Object> parameters = new ArrayList<Object>();
        if (apPrimer != null && !apPrimer.isEmpty()) {
            where += "ap_primer_menor like ? and ";
            parameters.add(apPrimer.toUpperCase() + "%");
        }
        if (apSegundo != null && !apSegundo.isEmpty()) {
            where += "ap_segundo_menor like ? and ";
            parameters.add(apSegundo.toUpperCase() + "%");
        }
        if (prenombre != null && !prenombre.isEmpty()) {
            where += "prenombre_menor like ? and ";
            parameters.add("%"+prenombre.toUpperCase() + "%");
        }
        parameters.add(filaFinal);
        parameters.add(filaInicio);
//		logger.info("where=" + where);
        if (where.isEmpty()) {
            return new ArrayList<Persona>();
        } else {
            String query =
                    "select *  from  " +
                    "(select rownum fila, t.* " +
                    "from  ( " +
                    "select " +
                    "co_padron_nominal as codigoPadronNominal, nu_dni_menor as dni, nu_cui as nuCui, ti_doc_identidad tipoDocumento, " +
                    "trunc(months_between(sysdate, p.fe_nac_menor)/12) edad, " +
                    "(trunc(months_between(sysdate, p.fe_nac_menor)/12))||' año(s), '||nu_edad_meses||' mes(es)' edadEscrita, " +
                    "decode(co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero, " +
                    "ap_primer_menor as primerApellido, ap_segundo_menor as segundoApellido, prenombre_menor as nombres, " +
                    "TO_CHAR(fe_nac_Menor,'dd/MM/yyyy') as fechaNacimiento, " +
                    "co_dni_jefe_fam as padreDni, ap_primer_jefe as padrePrimerApellido, ap_segundo_jefe as padreSegundoApellido, prenom_jefe as padreNombres, dj.de_vinculo as padreTipoVinculo, " +
                    "co_dni_madre as madreDni, ap_primer_madre as madrePrimerApellido, ap_segundo_madre as madreSegundoApellido, prenom_madre as madreNombres, dm.de_vinculo as madreTipoVinculo " +
                    ", p.es_padron as esPadron " +
                    "from pntv_padron_nominal p " +
                    "left join PNTR_VINCULO_FAMILIAR dj on dj.co_vinculo=p.ti_vinculo_jefe  " +
                    "left join PNTR_VINCULO_FAMILIAR dm on dm.co_vinculo=p.ti_vinculo_madre " +
                    "where p.es_padron<>'9' and " + where.substring(0, where.length() - 4) +
                    " order by ap_primer_menor, ap_segundo_menor, prenombre_menor " +
                    ") t " +
                    "where rownum<=?) " +
                    "where fila>=?";

            logger.debug(String.format("DAO '%s' con '%s'", query, parameters));
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), parameters.toArray(new Object[parameters.size()]));
        }
    }

    @Override
    public List<Persona> buscarMenorePorNombres(String apPrimer, String apSegundo, String prenombre, int filaInicio, int filaFinal) {
        String where = "";
        String whereAni = "";
        List<Object> parameters = new ArrayList<Object>();

        if (apPrimer != null && !apPrimer.isEmpty()) {
            where += "P.ap_primer_menor like ? and ";
            whereAni += "ap_primer like ? and ";
            parameters.add(apPrimer.toUpperCase() + "%");
        }
        if (apSegundo != null && !apSegundo.isEmpty()) {
            where += "P.ap_segundo_menor like ? and ";
            whereAni += "ap_segundo like ? and ";
            parameters.add(apSegundo.toUpperCase() + "%");
        }
        if (prenombre != null && !prenombre.isEmpty()) {
            where += "P.prenombre_menor like ? and ";
            whereAni += "prenom_inscrito like ? and ";
            parameters.add("%"+prenombre.toUpperCase() + "%");
        }

        if (apPrimer != null && !apPrimer.isEmpty()) {
            parameters.add(apPrimer.toUpperCase() + "%");
        }
        if (apSegundo != null && !apSegundo.isEmpty()) {
            parameters.add(apSegundo.toUpperCase() + "%");
        }
        if (prenombre != null && !prenombre.isEmpty()) {
            parameters.add("%"+prenombre.toUpperCase() + "%");
        }

        parameters.add(filaFinal);
        parameters.add(filaInicio);
//		logger.info("where=" + where);
        if (where.isEmpty()) {
            return new ArrayList<Persona>();
        } else {
            String query = "" +
                    "select *  from  " +
                    "(select rownum fila, t.* " +
                    "from  ( " +
                    "select t1.co_padron_nominal as codigoPadronNominal,\n" +
                    "       t1.nu_cui as nuCui,\n" +
                    "       nvl2(t1.co_padron_nominal, 'PN', 'ANI') as tipo_fuente,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.nu_dni_menor, t2.nu_dni) as dni,\n" +
                    "       decode(nvl2(t1.co_padron_nominal, t1.co_genero_menor, t2.de_genero),\n" +
                    "              1,\n" +
                    "              'Masculino',\n" +
                    "              2,\n" +
                    "              'Femenino') as genero,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.ap_primer_menor, t2.ap_primer) as primerApellido,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.ap_segundo_menor, t2.ap_segundo) as segundoApellido,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.prenombre_menor, t2.prenom_inscrito) as nombres,\n" +
                    "       TO_CHAR(nvl2(t1.co_padron_nominal, t1.fe_nac_menor, t2.fe_nacimiento),\n" +
                    "               'dd/MM/yyyy') as fechaNacimiento,\n" +
                    "       trunc((months_between(sysdate,\n" +
                    "                             nvl2(t1.co_padron_nominal,\n" +
                    "                                  t1.fe_nac_menor,\n" +
                    "                                  t2.fe_nacimiento))) / 12) edad,\n" +
                    "       trunc((months_between(sysdate,\n" +
                    "                             nvl2(t1.co_padron_nominal,\n" +
                    "                                  t1.fe_nac_menor,\n" +
                    "                                  t2.fe_nacimiento))) / 12) || ' año(s), ' ||\n" +
                    "       trunc(mod((months_between(sysdate,\n" +
                    "                                 nvl2(t1.co_padron_nominal,\n" +
                    "                                      t1.fe_nac_menor,\n" +
                    "                                      t2.fe_nacimiento))),\n" +
                    "                 12)) || ' mes(es)' edadEscrita,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.co_dni_jefe_fam, t2.nu_doc_padre) as padreDni,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.ap_primer_jefe, t2.ap_padre_primer) as padrePrimerApellido,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.ap_segundo_jefe, t2.ap_padre_segundo) as padreSegundoApellido,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.prenom_jefe, t2.prenom_padre) as padreNombres,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.co_dni_madre, t2.nu_doc_madre) as madreDni,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.ap_primer_madre, t2.ap_madre_primer) as madrePrimerApellido,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.ap_segundo_madre, t2.ap_madre_segundo) as madreSegundoApellido,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.prenom_madre, t2.prenom_madre) as madreNombres,\n" +
                    "       t1.es_padron,\n" +
                    "       nvl2(t1.co_padron_nominal, t1.co_restri, t2.co_restri) as coRestri,\n" +
                    "       t2.de_restri as deRestri \n" +
                    "  from (SELECT P.co_padron_nominal,\n" +
                    "   P.nu_dni_menor,\n" +
                    "   P.nu_cui,\n" +
                    "   P.ti_doc_identidad,\n" +
                    "   P.co_genero_menor,\n" +
                    "   P.ap_primer_menor,\n" +
                    "   P.ap_segundo_menor,\n" +
                    "   P.prenombre_menor,\n" +
                    "   P.fe_nac_menor,\n" +
                    "   P.co_dni_jefe_fam,\n" +
                    "   P.ap_primer_jefe,\n" +
                    "   P.ap_segundo_jefe,\n" +
                    "   P.prenom_jefe,\n" +
                    "   P.co_dni_madre,\n" +
                    "   P.ap_primer_madre,\n" +
                    "   P.ap_segundo_madre,\n" +
                    "   P.prenom_madre,\n" +
                    "   P.es_padron,\n" +
                    "   B.CO_MOTIVO_BAJA co_restri \n" +
                    "FROM PNTV_PADRON_NOMINAL P \n" +
                    "LEFT JOIN PNTH_PADRON_NOMINAL_BAJA B ON P.CO_PADRON_NOMINAL=B.CO_PADRON_NOMINAL AND B.ES_PADRON='0' AND B.CO_MOTIVO_BAJA='1' AND B.US_CREA_AUDI='SPDEFDNI' \n" +
                    "WHERE P.es_padron <> '9' \n" +
                    "           and " + where.substring(0, where.length() - 4) +
                    ") t1\n" +
                    "  full outer JOIN (select nu_dni,\n" +
                    "                          ap_primer,\n" +
                    "                          ap_segundo,\n" +
                    "                          prenom_inscrito,\n" +
                    "                          de_genero,\n" +
                    "                          nu_doc_padre,\n" +
                    "                          ap_padre_primer,\n" +
                    "                          ap_padre_segundo,\n" +
                    "                          prenom_padre,\n" +
                    "                          nu_doc_madre,\n" +
                    "                          ap_madre_primer,\n" +
                    "                          ap_madre_segundo,\n" +
                    "                          prenom_madre,\n" +
                    "                          fe_nacimiento,\n" +
                    "                          trim(r.co_restri) as co_restri,\n" +
                    "                          trim(r.de_restri) as de_restri\n" +
                    "                     from GETM_ANI g\n" +
                    "                     left join getm_restriccion r\n" +
                    "                       on r.co_restri = g.co_restri\n" +
                    "                      and (r.co_restri <> ' ' or r.co_restri <> '  ')\n" +
                    "                    where IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(g.ti_ficha_reg)='"+padronProperties.FICHA_MENOR+"' \n" +
                    "                      AND " + whereAni.substring(0, whereAni.length() - 4) +
                    " ) t2\n" +
                    "    ON t1.NU_DNI_MENOR = t2.nu_dni\n" +
                    "   AND t1.AP_PRIMER_MENOR = t2.AP_PRIMER\n" +
                    "   AND t1.AP_SEGUNDO_MENOR = t2.AP_SEGUNDO\n" +
                    "   AND t1.PRENOMBRE_MENOR = t2.PRENOM_INSCRITO\n" +
                    "   AND TO_CHAR(t1.FE_NAC_MENOR, 'YYYYMMDD') =\n" +
                    "       TO_CHAR(t2.FE_NACIMIENTO, 'YYYYMMDD') " +
                    " order by nvl2(t1.co_padron_nominal, t1.ap_primer_menor, t2.ap_primer),\n" +
                    "          nvl2(t1.co_padron_nominal, t1.ap_segundo_menor, t2.ap_segundo),\n" +
                    "          nvl2(t1.co_padron_nominal, t1.prenombre_menor, t2.prenom_inscrito)\n" +
                    ") t " +
                    "where rownum<=?) " +
                    "where fila>=?";

            logger.debug(String.format("DAO '%s' con '%s'", query, parameters));
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), parameters.toArray(new Object[parameters.size()]));
        }
    }

    @Override
    public int countBuscarMenorePorNombres(String apPrimer, String apSegundo, String prenombre) {
        String where = "";
        String whereAni = "";
        List<Object> parameters = new ArrayList<Object>();
       /* if (apPrimer != null && !apPrimer.isEmpty()) {
            whereAni += "ap_primer like ? and ";
            parameters.add("%"+apPrimer.toUpperCase() + "%");
        }
        if (apSegundo != null && !apSegundo.isEmpty()) {
            whereAni += "ap_segundo like ? and ";
            parameters.add("%"+apSegundo.toUpperCase() + "%");
        }
        if (prenombre != null && !prenombre.isEmpty()) {
            whereAni += "prenom_inscrito like ? and ";
            parameters.add("%"+prenombre.toUpperCase() + "%");
        }*/

        if (apPrimer != null && !apPrimer.isEmpty()) {
            where += "ap_primer_menor like ? and ";
            whereAni += "ap_primer like ? and ";
            parameters.add(apPrimer.toUpperCase() + "%");
        }
        if (apSegundo != null && !apSegundo.isEmpty()) {
            where += "ap_segundo_menor like ? and ";
            whereAni += "ap_segundo like ? and ";
            parameters.add(apSegundo.toUpperCase() + "%");
        }
        if (prenombre != null && !prenombre.isEmpty()) {
            where += "prenombre_menor like ? and ";
            whereAni += "prenom_inscrito like ? and ";
            parameters.add("%" + prenombre.toUpperCase() + "%");
        }

        if (apPrimer != null && !apPrimer.isEmpty()) {
            parameters.add(apPrimer.toUpperCase() + "%");
        }
        if (apSegundo != null && !apSegundo.isEmpty()) {
            parameters.add(apSegundo.toUpperCase() + "%");
        }
        if (prenombre != null && !prenombre.isEmpty()) {
            parameters.add("%"+prenombre.toUpperCase() + "%");
        }


        if (where.isEmpty() || whereAni.isEmpty()) {
            return 0;
        } else {
            String query = "" +
                    "select count(1)\n" +
                    "  from (select co_padron_nominal,\n" +
                    "               nu_dni_menor,\n" +
                    "               nu_cui,\n" +
                    "               ti_doc_identidad,\n" +
                    "               co_genero_menor,\n" +
                    "               ap_primer_menor,\n" +
                    "               ap_segundo_menor,\n" +
                    "               prenombre_menor,\n" +
                    "               fe_nac_menor,\n" +
                    "               co_dni_jefe_fam,\n" +
                    "               ap_primer_jefe,\n" +
                    "               ap_segundo_jefe,\n" +
                    "               prenom_jefe,\n" +
                    "               co_dni_madre,\n" +
                    "               ap_primer_madre,\n" +
                    "               ap_segundo_madre,\n" +
                    "               prenom_madre,\n" +
                    "               es_padron\n" +
                    "          from PNTV_PADRON_NOMINAL\n" +
                    "         WHERE es_padron <> '9'\n" +
                    "           and " + where.substring(0, where.length() - 4) +
                    ") t1\n" +
                    "  full outer JOIN (select nu_dni,\n" +
                    "                          ap_primer,\n" +
                    "                          ap_segundo,\n" +
                    "                          prenom_inscrito,\n" +
                    "                          de_genero,\n" +
                    "                          nu_doc_padre,\n" +
                    "                          ap_padre_primer,\n" +
                    "                          ap_padre_segundo,\n" +
                    "                          prenom_padre,\n" +
                    "                          nu_doc_madre,\n" +
                    "                          ap_madre_primer,\n" +
                    "                          ap_madre_segundo,\n" +
                    "                          prenom_madre,\n" +
                    "                          fe_nacimiento,\n" +
                    "                          trim(r.co_restri) as co_restri,\n" +
                    "                          trim(r.de_restri) as de_restri\n" +
                    "                     from GETM_ANI g\n" +
                    "                     left join getm_restriccion r\n" +
                    "                       on r.co_restri = g.co_restri\n" +
                    "                      and (r.co_restri <> ' ' or r.co_restri <> '  ')\n" +
                    "                    where IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(g.ti_ficha_reg)='"+padronProperties.FICHA_MENOR+"' \n" +
                    "                      AND " + whereAni.substring(0, whereAni.length() - 4) +
                    " ) t2\n" +
                    "    ON t1.NU_DNI_MENOR = t2.nu_dni\n" +
                    "   AND t1.AP_PRIMER_MENOR = t2.AP_PRIMER\n" +
                    "   AND t1.AP_SEGUNDO_MENOR = t2.AP_SEGUNDO\n" +
                    "   AND t1.PRENOMBRE_MENOR = t2.PRENOM_INSCRITO\n" +
                    "   AND TO_CHAR(t1.FE_NAC_MENOR, 'YYYYMMDD') =\n" +
                    "       TO_CHAR(t2.FE_NACIMIENTO, 'YYYYMMDD')";

            logger.debug(String.format("DAO: '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(parameters)));
            return  jdbcTemplate.queryForInt(query, parameters.toArray(new Object[parameters.size()]));
        }
    }

    @Override
    public List<Persona> listarPorDniMadre(String dni, String esPadron) {
        String query = "SELECT co_padron_nominal AS codigoPadronNominal, nu_dni_menor AS dni, ti_doc_identidad tipoDocumento, nu_cui nuCui, " +
                           "TRUNC(MONTHS_BETWEEN(sysdate, p.fe_nac_menor)/12) edad, " +
                           "(TRUNC(MONTHS_BETWEEN(sysdate, p.fe_nac_menor)/12))||' año(s), '||nu_edad_meses||' mes(es)' edadEscrita, " +
                           "ap_primer_menor AS primerApellido, ap_segundo_menor AS segundoApellido, prenombre_menor AS nombres, " +
                           "DECODE(co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero, " +
                           "TO_CHAR(fe_nac_Menor,'dd/MM/yyyy') AS fechaNacimiento " +
                           ", es_padron AS esPadron " +
                       "FROM pntv_padron_nominal p " +
                       "WHERE co_dni_madre=? AND p.es_padron=? " +
                       "ORDER BY p.fe_crea_registro DESC";
                //"order by ap_primer_menor, ap_segundo_menor, prenombre_menor";
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(dni)));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), new Object[]{dni, esPadron});
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Persona> listarPorDniMadreDef(String dni) {
        String query = "SELECT p.co_padron_nominal AS codigoPadronNominal, p.nu_dni_menor AS dni, p.ti_doc_identidad tipoDocumento, p.nu_cui nuCui, TRUNC(MONTHS_BETWEEN(sysdate, p.fe_nac_menor)/12) edad, \n" +
                       "       (TRUNC(MONTHS_BETWEEN(sysdate, p.fe_nac_menor)/12))||' año(s), '||nu_edad_meses||' mes(es)' edadEscrita, p.ap_primer_menor AS primerApellido, p.ap_segundo_menor AS segundoApellido, \n" +
                       "       p.prenombre_menor AS nombres, DECODE(p.co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero, TO_CHAR(p.fe_nac_Menor,'dd/MM/yyyy') AS fechaNacimiento , p.es_padron AS esPadron\n" +
                       "FROM pntv_padron_nominal p \n" +
                       "JOIN pnth_padron_nominal_baja b ON p.co_padron_nominal=b.co_padron_nominal AND b.co_motivo_baja='1' AND b.es_padron='0' AND b.US_CREA_AUDI='SPDEFDNI'\n" +
                       "WHERE co_dni_madre=? AND p.es_padron='0'  ORDER BY p.fe_crea_registro DESC";
        //"order by ap_primer_menor, ap_segundo_menor, prenombre_menor";
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(dni)));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), new Object[]{dni});
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String getRangoFecha(String feNacMenor, int isDate){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        int months;
        String strDate;
        int range=Integer.parseInt(padronProperties.RANGE_MONTHS_SEARCH);

        try {
            Date dateToCheck = format.parse(feNacMenor);
            months = (isDate==ES_ANTERIOR)?(-1*range):range;
            Calendar c = Calendar.getInstance();
            c.setTime(dateToCheck);
            c.add(Calendar.MONTH, months);

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            strDate = dateFormat.format(c.getTime());

            return strDate;
        }
        catch(ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isTipoSeguro(String coPadronNominal) {
        String sql = "SELECT COUNT(1) FROM PNTV_PADRON_SEGURO WHERE CO_PADRON_NOMINAL=? AND CO_TIPO_SEGURO<>'0'";
        Object[] params = {coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForInt(sql, params) > 0;
        }
        catch(Exception e){
            logger.info("Error"+e.toString());
            return false;
        }
    }

    @Override
    public PadronNominalBaja getMotivoBaja(String codigoPadronNominal) {
        String query = "SELECT CO_MOTIVO_BAJA, US_CREA_AUDI FROM PNTH_PADRON_NOMINAL_BAJA WHERE CO_PADRON_NOMINAL=? AND ES_PADRON='0'";
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(codigoPadronNominal)));
        return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(PadronNominalBaja.class), new Object[]{codigoPadronNominal});
    }

    @Override
    public List<Persona> listarPorDniMadre(PadronNominal padronNominal, String esPadron) {

        String feAnterior = getRangoFecha(padronNominal.getFeNacMenor(), ES_ANTERIOR); //yyyyMMdd
        String fePosterior = getRangoFecha(padronNominal.getFeNacMenor(), ES_POSTERIOR);//yyyyMMdd

        String query="SELECT co_padron_nominal AS codigoPadronNominal, nu_dni_menor AS dni, ti_doc_identidad tipoDocumento, nu_cui nuCui, \n" +
                       "(TRUNC(MONTHS_BETWEEN(sysdate, p.fe_nac_menor)/12)) edad, \n" +
                       "(TRUNC(MONTHS_BETWEEN(sysdate, p.fe_nac_menor)/12))||' año(s), '||nu_edad_meses||' mes(es)' edadEscrita, \n" +
                       "ap_primer_menor AS primerApellido, ap_segundo_menor AS segundoApellido, prenombre_menor AS nombres, \n" +
                       "DECODE(co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero, TO_CHAR(fe_nac_Menor,'dd/MM/yyyy') AS fechaNacimiento , " +
                       "nu_cnv AS cnv, \n" +
                       "es_padron AS esPadron \n" +
                     "FROM pntv_padron_nominal p \n" +
                     "WHERE p.co_dni_madre=? \n" +
                        "AND p.es_padron=? \n" +
                        "AND TRANSLATE(TRIM(p.ap_primer_menor),'ÁÉÍÓÚÜ', 'AEIOUU') like ?  \n" +
                        "AND p.fe_nac_menor BETWEEN to_date(?,'YYYYMMDD') and to_date(?,'YYYYMMDD') " +
                     "ORDER BY p.fe_crea_registro DESC";

        Object[] params = new Object[]{padronNominal.getCoDniMadre(),esPadron,padronNominal.getApPrimerMenor(), feAnterior, fePosterior};
        logger.debug(String.format("DAO '%s' con '%s'", query, params));

        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public int contarRegistrosPorDatos(String apPrimer, String apSegundo, String prenombre) {
        String where = "";
        String whereAni = "";
        List<Object> parameters = new ArrayList<Object>();
       /* if (apPrimer != null && !apPrimer.isEmpty()) {
            whereAni += "ap_primer like ? and ";
            parameters.add("%"+apPrimer.toUpperCase() + "%");
        }
        if (apSegundo != null && !apSegundo.isEmpty()) {
            whereAni += "ap_segundo like ? and ";
            parameters.add("%"+apSegundo.toUpperCase() + "%");
        }
        if (prenombre != null && !prenombre.isEmpty()) {
            whereAni += "prenom_inscrito like ? and ";
            parameters.add("%"+prenombre.toUpperCase() + "%");
        }*/

        if (apPrimer != null && !apPrimer.isEmpty()) {
            where += "ap_primer_menor like ? and ";
            parameters.add(apPrimer.toUpperCase() + "%");
        }
        if (apSegundo != null && !apSegundo.isEmpty()) {
            where += "ap_segundo_menor like ? and ";
            parameters.add(apSegundo.toUpperCase() + "%");
        }
        if (prenombre != null && !prenombre.isEmpty()) {
            where += "prenombre_menor like ? and ";
            parameters.add("%"+prenombre.toUpperCase() + "%");
        }

        if (where.isEmpty() /*|| whereAni.isEmpty()*/) {
            return 0;
        } else {
            String query = "" +
                    "SELECT count(1) " +
                    "  FROM pntv_padron_nominal " +
                    "  where es_padron<>'9' and " + where.substring(0, where.length() - 4);
            logger.debug(String.format("DAO: '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(parameters)));
            return  jdbcTemplate.queryForInt(query, parameters.toArray(new Object[parameters.size()]));
        }
    }

    @Override
    public String obtenerDeEstSalud(String coEstSalud, String nuSecEstSalud){
        String sql = "SELECT DE_RENAES deEstSaludAds FROM PNTM_RENAES WHERE CO_RENAES = ? AND NU_SECUENCIA_LOCAL = ?";
        Object[] params = { coEstSalud , nuSecEstSalud};
        logger.debug(String.format("DAO '%s' con '%s'", new Object[] { sql, ArrayUtils.toString(params) }));

        return (String) this.jdbcTemplate.queryForObject(sql, String.class, params);

    }

    @Override
    public List<Persona> listarPorDatosMadre(PadronNominal padronNominal, String esPadron) {

        String feAnterior = getRangoFecha(padronNominal.getFeNacMenor(),ES_ANTERIOR);
        String fePosterior = getRangoFecha(padronNominal.getFeNacMenor(),ES_POSTERIOR);

        String apPrimerMadreParts[] = padronNominal.getApPrimerMadre().trim().split(" ");
        String apSegundoMadreParts[] = padronNominal.getApSegundoMadre().trim().split(" ");
        String prenombreMadreParts[] = padronNominal.getPrenomMadre().trim().split(" ");

        String apPrimerMenorParts[] = padronNominal.getApPrimerMenor().trim().split(" ");
        String prenombreMenorParts[] = padronNominal.getPrenombreMenor().trim().split(" ");

        String apPrimerMadrePart = "";
        String apSegundoMadrePart = "";
        String prenombreMadrePart = "";

        String apPrimerMenorPart = "";
        String prenombreMenorPart = "";

        if (apPrimerMadreParts != null && apSegundoMadreParts != null && prenombreMadreParts != null) {
            if (apPrimerMadreParts[0] != null && apSegundoMadreParts[0] != null && prenombreMadreParts[0] != null &&
                    !apPrimerMadreParts[0].isEmpty() && !apSegundoMadreParts[0].isEmpty() && !prenombreMadreParts[0].isEmpty()) {
                apPrimerMadrePart = "%"+apPrimerMadreParts[0]+"%";
                apSegundoMadrePart = "%"+apSegundoMadreParts[0]+"%";
                prenombreMadrePart = "%"+prenombreMadreParts[0]+"%";
            }
        }

        if (apPrimerMenorParts != null && prenombreMenorParts != null) {
            if (apPrimerMenorParts[0] != null && prenombreMenorParts[0] != null &&
                    !apPrimerMenorParts[0].isEmpty() && !prenombreMenorParts[0].isEmpty()) {
                apPrimerMenorPart = "%"+apPrimerMenorParts[0]+"%";
                prenombreMenorPart = "%"+prenombreMenorParts[0]+"%";
            }
        }

        String query =  "SELECT p.co_padron_nominal codigoPadronNominal, \n" +
                        "      p.ap_primer_menor primerApellido, \n" +
                        "      p.ap_segundo_menor segundoApellido, \n" +
                        "      p.prenombre_menor nombres, \n" +
                        "      TO_CHAR(p.fe_nac_menor, 'DD/MM/YYYY') fechaNacimiento, \n" +
                        "      p.ap_primer_madre madrePrimerApellido, \n" +
                        "      p.ap_segundo_madre madreSegundoApellido, \n" +
                        "      p.prenom_madre madreNombres, \n" +
                        "      u.de_departamento||' / '||u.de_provincia||' / '||u.de_distrito as descUbigeo, \n" +
                        "      p.es_padron esPadron," +
                        "      DECODE(p.co_genero_menor, 1, 'Masculino', 2, 'Femenino') genero," +
                        "      p.nu_cnv cnv \n" +
                        "FROM PNTV_PADRON_NOMINAL p \n" +
                        "LEFT JOIN PNTM_UBIGEO_INEI u \n" +
                        "ON p.co_ubigeo_inei = u.co_ubigeo_inei \n" +
                        "WHERE TRANSLATE(TRIM(p.ap_primer_madre), 'ÁÉÍÓÚÜ', 'AEIOUU') like ?  \n" +
                        "      AND TRANSLATE(TRIM(p.ap_segundo_madre), 'ÁÉÍÓÚÜ', 'AEIOUU') like ?  \n" +
                        "      AND TRANSLATE(TRIM(p.prenom_madre), 'ÁÉÍÓÚÜ', 'AEIOUU') like ?  \n" +
                        "      AND TRANSLATE(TRIM(p.ap_primer_menor),'ÁÉÍÓÚÜ', 'AEIOUU') like ?  \n" +
                        "      AND p.fe_nac_menor BETWEEN TO_DATE(?,'yyyyMMdd') and TO_DATE(?,'yyyyMMdd')" +
                        "      AND p.es_padron= ? ";

        Object[] params = new Object[]{ apPrimerMadrePart,
                                        apSegundoMadrePart,
                                        prenombreMadrePart,
                                        apPrimerMenorPart,
                                        feAnterior,
                                        fePosterior,
                                        esPadron };
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));

        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Persona.class), params);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Persona>();
        }
    }

    @Override
    public boolean isSusalud(String coPadronNominal) {
        String sql = "SELECT COUNT(1) FROM PNTV_PADRON_NOMINAL WHERE CO_PADRON_NOMINAL=? AND FUENTE_PRECARGA='1'";
        Object[] params = {coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForInt(sql, params) > 0;
        }
        catch(Exception e){
            logger.info("Error"+e.toString());
            return false;
        }
    }

    @Override
    public String obtenerMotivoBaja(String coPadronNominal) {
        logger.info("Iniciando metodo obtenerMotivoBaja...");

        String sql = "SELECT pd.de_dom FROM pntr_dominios pd \n" +
                     "WHERE no_dom='CO_MOTIVO_BAJA' AND co_dominio=( \n" +
                     "SELECT pb.co_motivo_baja FROM(SELECT co_motivo_baja FROM pnth_padron_nominal_baja WHERE co_padron_nominal=? ORDER BY fe_crea_audi DESC) pb \n" +
                     "WHERE rownum=1)";

        Object[] params = {coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", new Object[] { sql, ArrayUtils.toString(params) }));

        try {
            return this.jdbcTemplate.queryForObject(sql, String.class, params);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("Error al obtener el motivo de baja");
            return null;
        }
    }

    @Override
    public String obtenerObservacionBaja(String coPadronNominal) {
        logger.info("Iniciando metodo obtenerObservacionBaja...");

        String sql = "SELECT de_observacion FROM (SELECT de_observacion " +
                     "FROM pnth_padron_nominal_baja " +
                     "WHERE co_padron_nominal=? ORDER BY fe_crea_audi DESC) pb \n" +
                     "WHERE rownum=1";
        Object[] params = { coPadronNominal };
        logger.debug(String.format("DAO '%s' con '%s'", new Object[] { sql, ArrayUtils.toString(params) }));

        try {
            return this.jdbcTemplate.queryForObject(sql, String.class, params);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Error al obtener la observacion de la baja");
            return null;
        }
    }


    @Override
    public PadronNominal obtenerPorCodigoPadron(String coPadronNominal) {
        String query = "" +
                " select p.co_padron_nominal, p.nu_dni_menor, p.ti_doc_identidad, " +
                " p.ap_primer_menor, p.ap_segundo_menor, p.prenombre_menor, p.co_genero_menor, TO_CHAR(p.fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                " decode(p.co_genero_menor, 1, 'Masculino', 2, 'Femenino') deGeneroMenor, " +
                " trunc((months_between(sysdate, p.fe_nac_menor))/12) edad, " +
                " trunc((months_between(sysdate, p.fe_nac_menor))/12)||' año(s), '||trunc(mod((months_between(sysdate, p.fe_nac_menor)),12))||' mes(es)' edadEscrita, " +
                " p.de_direccion, p.co_ubigeo_inei, p.co_centro_poblado, decode(p.co_centro_poblado, null, null, '', null,  cp.no_centro_poblado || ', ' || cp.no_categoria) as de_centro_poblado, p.co_etnia, " +
                " p.co_est_salud, p.nu_secuencia_local, p.ti_seguro_menor, p.nu_afiliacion, p.co_inst_educativa, " +
                " p.co_dni_jefe_fam, p.ap_primer_jefe, p.ap_segundo_jefe, p.prenom_jefe, p.ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe, " +
                " p.co_dni_madre, p.ap_primer_madre, p.ap_segundo_madre, p.prenom_madre, p.ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre, " +
                " p.co_gra_inst_madre, p.co_len_madre, " +
                " p.co_nivel_pobreza, p.us_crea_registro, to_char(p.fe_crea_registro, 'dd/mm/yyyy') fe_crea_registro, p.us_modi_registro, to_char(p.fe_modi_registro, 'dd/mm/yyyy') fe_modi_registro " +
                " , p.es_padron, p.nu_cui, " +
                " (u.de_departamento || ', ' || u.de_provincia || ', ' || u.de_distrito) de_ubigeo_inei, p.nu_cnv, " +
                " trim(p.co_est_salud_nac) as coEstSaludNac, p.nu_secuencia_local_nac, " +
                " es_nac.de_est_salud as deEstSaludNac, co_via, de_ref_dir, " +
                " p.co_est_salud_ads, es_ads.de_est_salud as deEstSaludAds, p.nu_secuencia_local_ads, " +
                " p.nu_cel_madre, p.di_correo_madre, " +
                " to_char(p.fe_visita, 'dd/mm/yyyy') as feVisita, p.in_menor_enc as coMenorEncontrado, p.in_menor_visitado, p.co_fuente_datos, FDM.de_fuente_datos, to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy') as feUltimaTomaDatos," +
                " to_char(p.fe_visita, 'dd/mm/yyyy') as feVisitaBefore, p.in_menor_visitado as inMenorVisitadoBefore, to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy') as feUltimaTomaDatosBefore, " + //para comparacion en registroManualController
                " tv.DE_CAT_VIA_DET || ' '||v.de_via as de_via, " +
                " cp.co_area_ccpp, cp.de_area_ccpp, nvl(p.fuente_precarga,'0') isPrecarga," +
                " p.us_crea_registro usCreaRegistro, p.co_frecuencia_atencion coFrecAtencion, fa.no_frecuencia_atencion deFrecAtencion" +
                " from pntv_padron_nominal p " +
                " left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe " +
                " left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre " +
                " left join PNTR_FUENTE_DATOS FDM on FDM.co_fuente_datos=p.co_fuente_datos " +
                " left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                " left join pnvm_establecimiento_salud es_nac on p.co_est_salud_nac=es_nac.co_est_salud and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                " left join pnvm_establecimiento_salud es_ads on p.co_est_salud_ads=es_ads.co_est_salud and es_ads.nu_secuencia_local=p.nu_secuencia_local_ads " +
                " left join pntm_via v on v.co_via=p.co_via " +
                " left join PNTR_TIPO_VIA tv on tv.CO_CAT_VIA=v.CO_CAT_VIA " +
                " left join pntd_frecuencia_atencion fa on p.co_frecuencia_atencion=fa.co_frecuencia_atencion" +
                " where co_padron_nominal=? and p.es_padron<>'9' ";
        try {
            Object[] params = new Object[]{coPadronNominal};
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            PadronNominal padronNominal = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(PadronNominal.class), params);
            padronNominal.setTiProSocial(padronProgramaDao.listarCoPadronPrograma(padronNominal.getCoPadronNominal()));
            padronNominal.setTiSeguroMenor(padronSeguroDao.listarCoPadronSeguro(padronNominal.getCoPadronNominal()));

            String nuDni = padronNominal.getNuDniMenor();
            if (nuDni != null && !nuDni.isEmpty()) {
                padronNominal.setOrigenFoto(Persona.OrigenFoto.ANI);
            } else if (padronImgDao.tieneImagen(padronNominal.getCoPadronNominal())) {
                padronNominal.setOrigenFoto(Persona.OrigenFoto.PN);
            } else {
                padronNominal.setOrigenFoto(Persona.OrigenFoto.EMPTY);
            }
            return padronNominal;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PadronNominal obtenerPorCodigoPadronConDetalles(String coPadronNominal) {
        String query = "SELECT P.NU_CNV AS nuCnv, P.CO_PADRON_NOMINAL, P.NU_DNI_MENOR, P.TI_DOC_IDENTIDAD, \n" +
                "P.AP_PRIMER_MENOR, P.AP_SEGUNDO_MENOR, P.PRENOMBRE_MENOR, P.CO_GENERO_MENOR, D.DE_GENERO_MENOR AS DE_GENERO_MENOR, TO_CHAR(FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, \n" +
                "P.DE_DIRECCION, P.CO_UBIGEO_INEI, U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO AS DE_UBIGEO_INEI, P.CO_CENTRO_POBLADO, DECODE(CP.NO_CENTRO_POBLADO, NULL, NULL, '', NULL,  CP.NO_CENTRO_POBLADO || ', ' || CP.NO_CATEGORIA) AS DE_CENTRO_POBLADO, CO_ETNIA, \n" +
                "P.CO_EST_SALUD, P.CO_EST_SALUD_NAC, P.NU_SECUENCIA_LOCAL, ES.DE_EST_SALUD||' (RENAES: '||ES.CO_EST_SALUD||')' AS DE_EST_SALUD, \n" +
                "ES.DE_EST_SALUD NO_EST_SALUD, \n" +
                "P.TI_SEGURO_MENOR, D4.DE_SEGURO AS DE_SEGURO_MENOR, NU_AFILIACION, \n" +
                "IE.CO_MODULAR CO_INST_EDUCATIVA, \n" +
                "P.CO_FRECUENCIA_ATENCION AS CO_FREC_ATENCION, \n" +
                "IE.CO_MODULAR CO_MODULAR, \n" +
                "IE.NO_CENTRO_EDUCATIVO NOINSTEDUCATIVA, \n" +
                "DECODE(P.CO_INST_EDUCATIVA, NULL, '', IE.NO_CENTRO_EDUCATIVO||' (CÓDIGO MODULAR: '||IE.CO_MODULAR||', '||IE.DE_DEPARTAMENTO||', '||IE.DE_PROVINCIA||', '||IE.DE_DISTRITO||')') AS DE_INST_EDUCATIVA, \n" +
                "CO_DNI_JEFE_FAM, AP_PRIMER_JEFE, AP_SEGUNDO_JEFE, PRENOM_JEFE, TI_VINCULO_JEFE, D1.de_vinculo AS DE_VINCULO_JEFE, \n" +
                "CO_DNI_MADRE, AP_PRIMER_MADRE, AP_SEGUNDO_MADRE, PRENOM_MADRE, TI_VINCULO_MADRE, D2.de_vinculo AS DE_VINCULO_MADRE, \n" +
                "NU_CEL_MADRE nuCelMadre, DI_CORREO_MADRE diCorreoMadre, \n " +
                "CO_GRA_INST_MADRE, D6.DE_GRADO_INSTRUCCION DE_GRA_INST_MADRE, CO_LEN_MADRE, D5.de_lengua AS DE_LEN_MADRE, \n" +
                "CO_NIVEL_POBREZA, D3.DE_DOM AS DE_NIVEL_POBREZA, US_CREA_REGISTRO, TO_CHAR(FE_CREA_REGISTRO, 'DD/MM/YYYY') feCreaRegistro, US_MODI_REGISTRO, TO_CHAR(FE_MODI_REGISTRO, 'DD/MM/YYYY') FE_MODI_REGISTRO, NU_CUI, U.DE_DEPARTAMENTO, U.DE_PROVINCIA, U.DE_DISTRITO, \n" +
                "TRUNC((MONTHS_BETWEEN(SYSDATE, FE_NAC_MENOR))/12)||' AÑO(S), '||TRUNC(MOD((MONTHS_BETWEEN(SYSDATE, FE_NAC_MENOR)),12))||' MES(ES)' EDADESCRITA, \n" +
                "P.CO_EST_SALUD_NAC, P.NU_SECUENCIA_LOCAL_NAC, \n" +
                "P.CO_EST_SALUD_ADS, P.NU_SECUENCIA_LOCAL_ADS, \n" +
                /*"NVL2(TRIM(ES_NAC.CO_EST_SALUD), ES_NAC.DE_EST_SALUD||' (RENAES: '||ES_NAC.CO_EST_SALUD||')', ' ')  AS DE_EST_SALUD_NAC,\n" +*/
                "NVL2(TRIM(ES_NAC.CO_EST_SALUD), ES_NAC.DE_EST_SALUD, ' ')  AS DE_EST_SALUD_NAC,\n" +
                "NVL2(TRIM(ES_ADS.CO_EST_SALUD), ES_ADS.DE_EST_SALUD||' (RENAES: '||ES_ADS.CO_EST_SALUD||')', ' ')  AS DE_EST_SALUD_ADS,\n" +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, P.DE_REF_DIR, \n" +
                "V.CO_VIA, \n" +
                "P.NU_CEL_MADRE, P.DI_CORREO_MADRE, \n" +
                "to_char(p.fe_visita, 'dd/mm/yyyy') as feVisita, p.in_menor_enc as coMenorEncontrado, p.in_menor_visitado, p.co_fuente_datos, FDM.de_fuente_datos, to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy') as feUltimaTomaDatos, \n" +
                "to_char(p.fe_visita, 'dd/mm/yyyy') as feVisitaBefore, p.in_menor_visitado as inMenorVisitadoBefore, to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy') as feUltimaTomaDatosBefore \n" + //para comparacion en registroManualController
                "FROM PNTV_PADRON_NOMINAL P \n" +
                "LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR \n" +
                "LEFT JOIN PNTR_VINCULO_FAMILIAR D1 ON D1.co_vinculo=p.TI_VINCULO_JEFE \n" +
                "LEFT JOIN PNTR_VINCULO_FAMILIAR D2 ON D2.co_vinculo=p.TI_VINCULO_MADRE \n" +
                "LEFT JOIN PNTR_DOMINIOS D3 ON D3.NO_DOM='CO_NIVEL_POBREZA' AND D3.CO_DOMINIO=CO_NIVEL_POBREZA \n" +
                "LEFT JOIN PNTR_TIPO_SEGURO D4 ON D4.CO_TIPO_SEGURO=P.TI_SEGURO_MENOR \n" +
                "LEFT JOIN PNTR_LENGUA D5 ON D5.co_lengua=p.CO_LEN_MADRE \n" +
                "LEFT JOIN PNTR_FUENTE_DATOS FDM ON FDM.co_fuente_datos=p.co_fuente_datos\n" +
                "LEFT JOIN PNTR_GRADO_INSTRUCCION D6 ON D6.co_grado_instruccion=P.CO_GRA_INST_MADRE \n" +
                "LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI AND U.ES_UBIGEO='1' \n" +
                "LEFT JOIN PNTM_CENTRO_POBLADO CP ON CP.CO_CENTRO_POBLADO=P.CO_CENTRO_POBLADO AND CP.ES_CENTRO_POBLADO='1' \n" +
                "LEFT JOIN PNTM_CENTRO_EDUCATIVO IE ON IE.CO_CENTRO_EDUCATIVO=P.CO_INST_EDUCATIVA \n" +
                "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES ON ES.CO_EST_SALUD=P.CO_EST_SALUD AND ES.NU_SECUENCIA_LOCAL=P.NU_SECUENCIA_LOCAL \n" +
                "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES_NAC ON ES_NAC.CO_EST_SALUD=P.CO_EST_SALUD_NAC AND ES_NAC.NU_SECUENCIA_LOCAL=P.NU_SECUENCIA_LOCAL_NAC \n" +
                "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES_ADS ON ES_ADS.CO_EST_SALUD=P.CO_EST_SALUD_ADS AND ES_ADS.NU_SECUENCIA_LOCAL=P.NU_SECUENCIA_LOCAL_ADS \n" +
                "LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                "LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                "WHERE P.CO_PADRON_NOMINAL=? AND P.ES_PADRON<>'9' ";
        try {
            Object params = new Object[]{coPadronNominal};
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            PadronNominal padronNominal = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(PadronNominal.class), new Object[]{coPadronNominal});
            padronNominal.setTiProSocial(padronProgramaDao.listarCoPadronPrograma(padronNominal.getCoPadronNominal()));
            padronNominal.setPadronProgramaList(listarProgramas(padronNominal.getCoPadronNominal()));

            padronNominal.setTiSeguroMenor(padronSeguroDao.listarCoPadronSeguro(padronNominal.getCoPadronNominal()));
            padronNominal.setPadronSeguroList(listarSeguros(padronNominal.getCoPadronNominal()));

            return padronNominal;
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Persona> listarNombresSimilares(String paterno, String materno, String nombres) {
        try {
            Map<String, Object> sonidos = jdbcTemplate.queryForMap("select PNPK_SOUNDEXESP.FU_SOUNDESP(trim(?)) paterno, PNPK_SOUNDEXESP.FU_SOUNDESP(trim(?)) materno, PNPK_SOUNDEXESP.FU_SOUNDESP(trim(?)) nombres from dual", paterno, materno, nombres);
            String paternoSnd = (String) sonidos.get("paterno");
            String maternoSnd = (String) sonidos.get("materno");
            String nombresSnd = (String) sonidos.get("nombres");
            if (paternoSnd != null && !paternoSnd.isEmpty() && maternoSnd != null && !maternoSnd.isEmpty() && nombres != null && !nombresSnd.isEmpty()) {
                String query = "select s.co_padron_nominal codigoPadronNominal, p.ap_primer_menor primerApellido, p.ap_segundo_menor segundoApellido, p.prenombre_menor nombres, TO_CHAR(p.FE_NAC_MENOR,'dd/mm/yyyy') fechaNacimiento " +
                        "from pntx_padron_nominal_sonido s " +
                        "join pntv_padron_nominal p on p.co_padron_nominal=s.co_padron_nominal " +
                        "where s.ap_primer_menor=?  " +
                        "and s.ap_segundo_menor=? " +
                        "and s.prenombre_menor=? and p.es_padron='1'";
                Object[] params = new Object[]{paternoSnd, maternoSnd, nombresSnd};
                logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
                return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), paternoSnd, maternoSnd, nombresSnd);
            }
            return new ArrayList<Persona>();
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Persona>();
        } catch (IncorrectResultSizeDataAccessException e) {
            return new ArrayList<Persona>();
        }
    }

	/* depurar */
	/*@Override
	public List<PadronNominal> listarPadrones() {
		String query = "SELECT co_padron_nominal as coPadronNominal, DECODE(nu_dni_menor, null ,'SIN DOCUMENTO',nu_dni_menor) as nuDniMenor, ap_primer_menor as apPrimerMenor, ap_segundo_menor as apSegundoMenor,"
				+ " prenombre_menor as prenombreMenor, TO_CHAR(fe_nac_Menor,'dd/MM/yyyy') as feNacMenor, TO_CHAR(fe_crea_registro,'dd/MM/yyyy') as feCreaRegistro, DECODE(co_estado, 1, 'ACTIVO', 9,'DESACTIVADO') as coEstado "
				+ " FROM pnth_padron_nominal "
				+ " ORDER BY co_padron_nominal asc";
		return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(PadronNominal.class));
	}*/

	/*@Override
	public List<PadronNominal> listarPadrones(int filaInicio, int filaFinal, PadronNominal.listarProgramas campo) {
		String query = "select * from (" +
				"SELECT rownum as fila, co_padron_nominal as coPadronNominal, DECODE(nu_dni_menor, null ,'SIN DOCUMENTO',nu_dni_menor) as nuDniMenor, ap_primer_menor as apPrimerMenor, ap_segundo_menor as apSegundoMenor,"
				+ " prenombre_menor as prenombreMenor, TO_CHAR(fe_nac_Menor,'dd/MM/yyyy') as feNacMenor, TO_CHAR(fe_crea_registro,'dd/MM/yyyy') as feCreaRegistro, DECODE(co_estado, 1, 'ACTIVO', 9,'DESACTIVADO') as coEstado "
				+ " FROM pnth_padron_nominal "
				+ " ORDER BY ? asc)" +
				"where fila between ? and ?";
		return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(PadronNominal.class), new Object[]{campo.toString(), filaInicio, filaFinal});
	}*/

	/*@Override
	public List<PadronNominal> buscarPadrones(String nuDniMenor, String apPrimerMenor, String apSegundoMenor, String prenombreMenor, int filaInicio, int filaFinal, PadronNominal.Campo campo) {
		String where = "";
		List<Object> parameters = new ArrayList<Object>();
		if (nuDniMenor != null && !nuDniMenor.isEmpty()) {
			where += "nu_dni_menor like ? and ";
			parameters.add(nuDniMenor + "%");
		}
		if (apPrimerMenor != null && !apPrimerMenor.isEmpty()) {
			where += "ap_primer_menor like ? and ";
			parameters.add(apPrimerMenor.toUpperCase() + "%");
		}
		if (apSegundoMenor != null && !apSegundoMenor.isEmpty()) {
			where += "ap_segundo_menor like ? and ";
			parameters.add(apSegundoMenor.toUpperCase() + "%");
		}
		if (prenombreMenor != null && !prenombreMenor.isEmpty()) {
			where += "prenombre_menor like ? and ";
			parameters.add(prenombreMenor.toUpperCase() + "%");
		}
		parameters.add(campo.toString());
		parameters.add(filaInicio);
		parameters.add(filaFinal);
		if (where.isEmpty()) {
			return new ArrayList<PadronNominal>();
		} else {
			String query = "select * from (" +
					"select rownum as fila, co_padron_nominal as coPadronNominal, DECODE(nu_dni_menor, null ,'SIN DOCUMENTO',nu_dni_menor) as nuDniMenor, "
					+ " ap_primer_menor as apPrimerMenor, ap_segundo_menor as apSegundoMenor, prenombre_menor as prenombreMenor, "
					+ " co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, dj.de_dom_detalle ti_vinculo_jefe, "
					+ " co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, dm.de_dom_detalle ti_vinculo_madre,"
					+ " TO_CHAR(fe_nac_Menor,'dd/MM/yyyy') as feNacMenor, u.de_departamento||', '||u.de_provincia||', '||u.de_distrito as coUbigeoInei, "
					+ " de_direccion"
					+ " from pntv_padron_nominal p "
					+ " join pntr_dominios dj on dj.co_dominio=p.ti_vinculo_jefe and dj.no_dom='TI_VINCULO_MENOR' "
					+ " join pntr_dominios dm on dm.co_dominio=p.ti_vinculo_madre and dm.no_dom='TI_VINCULO_MENOR' "
					+ " join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei "
					+ " where " + where.substring(0, where.length() - 4)
					+ " order by ? asc)" +
					"where fila between ? and ?";
			return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(PadronNominal.class), parameters.toArray(new Object[parameters.size()]));
		}
	}*/

    @Override
    public int numeroRegistrosPorBusqueda(String nuDniMenor, String apPrimerMenor, String apSegundoMenor, String prenombreMenor) {
        String where = "";
        List<Object> parameters = new ArrayList<Object>();
        if (nuDniMenor != null && !nuDniMenor.isEmpty()) {
            where += "nu_dni_menor like ? and ";
            parameters.add(nuDniMenor + "%");
        }
        if (apPrimerMenor != null && !apPrimerMenor.isEmpty()) {
            where += "ap_primer_menor like ? and ";
            parameters.add(apPrimerMenor.toUpperCase() + "%");
        }
        if (apSegundoMenor != null && !apSegundoMenor.isEmpty()) {
            where += "ap_segundo_menor like ? and ";
            parameters.add(apSegundoMenor.toUpperCase() + "%");
        }
        if (prenombreMenor != null && !prenombreMenor.isEmpty()) {
            where += "prenombre_menor like ? and ";
            parameters.add(prenombreMenor.toUpperCase() + "%");
        }
        if (where.isEmpty()) {
            return 0;
        } else {
            String query = "select count(*) "
                    + " from pnth_padron_nominal"
                    + " where " + where.substring(0, where.length() - 4);
            return jdbcTemplate.queryForInt(query, parameters.toArray(new Object[parameters.size()]));
        }
    }

	/*@Override
	public int numeroRegistros() {
		String query = "select count(*) from pnth_padron_nominal";
		return jdbcTemplate.queryForInt(query);
	}*/

    private String generarNumeroPadronNominal() {
        /*String query = "select max(co_padron_nominal) from pntv_padron_nominal where co_padron_nominal>='90000000'";
        int numeroPadron = jdbcTemplate.queryForInt(query);
        if (numeroPadron == 0) {
            return "90000000";
        }
        return String.valueOf(numeroPadron + 1);*/

        // Se modifica para agregar secuencia inicial con 1,2,3,4
        String query = "select nvl(max(co_padron_nominal), 0) from pntv_padron_nominal where co_padron_nominal<='39999999'";
        logger.debug("DAO query");

        String coPadronNominal;
        int numeroPadron = jdbcTemplate.queryForInt(query);
        if (numeroPadron == 0) {
            coPadronNominal = "10000000";
        } else {
            coPadronNominal = String.valueOf(numeroPadron + 1);
        }

        if(!existeCoPadronNominal(coPadronNominal))
            return coPadronNominal;
        return generarNumeroPadronNominal();
    }

    @Override
    public String getEsPadron(String coPadronNominal) {
        String query = "SELECT ES_PADRON FROM PNTV_PADRON_NOMINAL WHERE CO_PADRON_NOMINAL=?";
        Object[] params = {coPadronNominal};
        try {
//			logger.debug(String.format("DAO query='%s'", query));
//			logger.debug(String.format("DAO params='%s'", ArrayUtils.toString(params)));
            return jdbcTemplate.queryForObject(query, params, String.class);
        } catch (EmptyResultDataAccessException erdae) {
            log.error(erdae.getMessage());
            return null;
        }
    }

    @Autowired
    Usuario usuario;

    @Override
    public boolean setEsPadron(String coPadronNominal, String esPadron) {
        String query = "" +
                "UPDATE PNTV_PADRON_NOMINAL " +
                "SET ES_PADRON=?, " +
                "FE_MODI_REGISTRO=SYSDATE, " +
                "US_MODI_REGISTRO=?, " +
                "TI_REGISTRO='RM' " +
                "WHERE CO_PADRON_NOMINAL=?";
        Object[] params = {esPadron, usuario.getLogin(), coPadronNominal};
		logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.update(query, params) == 1;
    }

    @Override
    public boolean setEsPadronInit(String coPadronNominal, String esPadron) {
        String query = "" +
                "UPDATE PNTV_PADRON_NOMINAL " +
                "SET ES_PADRON=? " +
                "WHERE CO_PADRON_NOMINAL=?";
        Object[] params = {esPadron, coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.update(query, params) == 1;
    }


    @Override
    public void insertPadronNominalBaja(PadronNominalBaja padronNominalBaja) {
        String query = "" +

                "INSERT " +
                "INTO PNTH_PADRON_NOMINAL_BAJA " +
                "( " +
                "CO_PADRON_NOMINAL, " +
                "ES_PADRON, " +
                "CO_MOTIVO_BAJA, " +
                "DE_OBSERVACION, " +
                "US_CREA_AUDI, " +
                "FE_CREA_AUDI, " +
                "US_MODI_AUDI, " +
                "FE_MODI_AUDI " +
                ") " +
                "VALUES " +
                "( " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "SYSDATE, " +
                "?, " +
                "SYSDATE " +
                ")";

        Object[] params = {
                padronNominalBaja.getCoPadronNominal(),
                padronNominalBaja.getEsPadron(),
                padronNominalBaja.getCoMotivoBaja(),
                padronNominalBaja.getDeObservacion(),
                padronNominalBaja.getUsCreaAudi(),
                padronNominalBaja.getUsModiAudi()
        };
		logger.debug(String.format("DAO query='%s'", query));
		logger.debug(String.format("DAO params='%s'", ArrayUtils.toString(params)));
        jdbcTemplate.update(query, params);

    }

    @Override
    @Transactional
    public boolean guardarPadronNominal(PadronNominal padronNominal) throws DataIntegrityViolationException {
        String coPadronNominal = padronNominal.getCoPadronNominal();
        String nuDniMenor = padronNominal.getNuDniMenor();
        String nuCui = padronNominal.getNuCui();

        if (coPadronNominal != null && !coPadronNominal.isEmpty()) {
            //verificamos que no se esté modificando es_padron y que es_padron esté activo
            String esPadron = getEsPadron(coPadronNominal);
            if (esPadron != null) {
                boolean sonIguales = esPadron.equalsIgnoreCase(padronNominal.getEsPadron());
                boolean esPadronActivo = (esPadron.equalsIgnoreCase("1") || esPadron.equalsIgnoreCase("2")); // si no está activo no puede ser modificable
                if (!sonIguales || !esPadronActivo) {
                    return false;
                }
            }
        }

		/*
		Casos:
		- ningun válido: Registro nuevo de menor sin DNI (insert generando número de padron)
		- solo nuDniMenor válido: Se saco la información del ani, registrar nuevo padron (insert con número de padron igual a coPadronNominal)
		- solo codigoPadronNominal válido: Se saco la información del padrón Nominal, pero el registrado no tiene DNI (update padrón Nominal)
		- codigoPadronNominal y nuDniMenor validos: Se saco la información del padrón Nominal y el registrado tiene DNI (update padrón Nominal)
		 */

        String queryHistorico = "" +
                " INSERT INTO "
                + "pnth_padron_nominal ( "
                + "nu_cui, NU_SEC, CO_PADRON_NOMINAL, NU_DNI_MENOR, TI_DOC_IDENTIDAD, "
                + "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, FE_NAC_MENOR, CO_GENERO_MENOR, "
                + "CO_UBIGEO_INEI, DE_DIRECCION, CO_CENTRO_POBLADO,"
                + "CO_NIVEL_POBREZA, CO_EST_SALUD, TI_SEGURO_MENOR, NU_AFILIACION, CO_INST_EDUCATIVA, "
                + "CO_DNI_JEFE_FAM, AP_PRIMER_JEFE, AP_SEGUNDO_JEFE, PRENOM_JEFE, TI_VINCULO_JEFE, "
                + "CO_DNI_MADRE, AP_PRIMER_MADRE, AP_SEGUNDO_MADRE, PRENOM_MADRE, TI_VINCULO_MADRE, CO_GRA_INST_MADRE, CO_LEN_MADRE, "
                + "US_CREA_REGISTRO, FE_CREA_REGISTRO, US_MODI_REGISTRO, FE_MODI_REGISTRO, CO_ESTADO, TI_REGISTRO, CO_ENTIDAD " +
                ", ES_PADRON, NU_CNV, CO_EST_SALUD_NAC, NU_SECUENCIA_LOCAL_NAC, NU_SECUENCIA_LOCAL, CO_VIA, DE_REF_DIR "
                + ") VALUES ( "
                + "?,?,?,?,?,"
                + "trim(upper(?)),trim(upper(?)),trim(upper(?)),to_date(?, 'dd/MM/yyyy'),?,"
                + "?,upper(?), ?, "
                + "?,?,?,?,trim(?),"
                + "?,trim(upper(?)),trim(upper(?)),trim(upper(?)),?,"
                + "?,trim(upper(?)),trim(upper(?)),trim(upper(?)),?,?,?,"
                + "?,sysdate,?,sysdate,?, 'RM', ? " +
                ", ?, " //insert de es_padron en historico siempre debe ser de objeto padronnominal
                + "?, ?, ?, ?,?,upper(?))";

        if (nuDniMenor == null || nuDniMenor.isEmpty()) {
            if (nuCui == null || nuCui.isEmpty()) {
                padronNominal.setTiDocIdentidad("3");
            } else {
                padronNominal.setTiDocIdentidad("2");
            }
        } else {
            padronNominal.setTiDocIdentidad("1");
        }

        if (coPadronNominal != null && !coPadronNominal.isEmpty()) {/*ACTUALIZACION .---------------------------------*/

            padronNominal.setCoEstado("1");
            padronNominal.setNuSec(generarNumeroSecPadronHistorico(padronNominal.getCoPadronNominal()));

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
                    "  FROM PNTV_PADRON_NOMINAL PN\n " +
                    "  WHERE PN.CO_PADRON_NOMINAL = ?";
            Object[] paramsHistory = new Object[]{
                    Integer.valueOf(padronNominal.getCoPadronNominal()),
                    Integer.valueOf(padronNominal.getCoPadronNominal())};
            log.debug("DAO query='"+queryHistory+"'");
            log.debug("DAO params='"+ ArrayUtils.toString(paramsHistory)+"'");
            int resultadoHistorico = this.jdbcTemplate.update(queryHistory, paramsHistory);

            String coEntidad=null;
            String coEntidadEESS=null;
            if(padronNominal.getCoTipoEntidad().equals("8")){
                coEntidadEESS = padronNominal.getCoEntidad();
            }
            else {
                coEntidad = padronNominal.getCoEntidad();
            }

            insertarPadronMovimiento(padronNominal);


        /*--------------------------------------Actualizacion de Tabla Maestra----------------------------------*/
            //Actualizamos la información de la tabla principal
            String queryUpdatePrincipal = "" +
                    "update pntv_padron_nominal set "
                    + " NU_CUI=?,NU_DNI_MENOR=trim(?), TI_DOC_IDENTIDAD=?, "
                    + " AP_PRIMER_MENOR=trim(upper(?)), AP_SEGUNDO_MENOR=trim(upper(?)), PRENOMBRE_MENOR=trim(upper(?)), FE_NAC_MENOR=to_date(?, 'dd/MM/yyyy'), "
                    + " CO_GENERO_MENOR=?, CO_UBIGEO_INEI=trim(?), DE_DIRECCION=upper(?), CO_CENTRO_POBLADO=trim(?), "
                    + " CO_NIVEL_POBREZA=?, CO_EST_SALUD=trim(?), NU_AFILIACION=?, CO_INST_EDUCATIVA=trim(?), "
                    + " CO_DNI_JEFE_FAM=?, AP_PRIMER_JEFE=trim(upper(?)), AP_SEGUNDO_JEFE=trim(upper(?)), PRENOM_JEFE=trim(upper(?)), TI_VINCULO_JEFE=?, "
                    + " CO_DNI_MADRE=?, AP_PRIMER_MADRE=trim(upper(?)), AP_SEGUNDO_MADRE=trim(upper(?)), PRENOM_MADRE=trim(upper(?)), TI_VINCULO_MADRE=?, CO_GRA_INST_MADRE=trim(?), CO_LEN_MADRE=trim(?), "
                    + " US_MODI_REGISTRO=?, FE_MODI_REGISTRO=sysdate, CO_ENTIDAD=trim(?), TI_REGISTRO='RM',"
                    + " CO_EST_SALUD_NAC=trim(?), NU_EDAD=TRUNC(MONTHS_BETWEEN(SYSDATE, FE_NAC_MENOR)/12), NU_EDAD_MESES=TRUNC(MOD((MONTHS_BETWEEN(SYSDATE, FE_NAC_MENOR)),12)), "
                    + " NU_SECUENCIA_LOCAL=?, NU_SECUENCIA_LOCAL_NAC=?, CO_VIA=?, DE_REF_DIR=upper(?), "
                    + " CO_EST_SALUD_ADS=?, NU_SECUENCIA_LOCAL_ADS=?, "
                    + " NU_CEL_MADRE=?, DI_CORREO_MADRE= trim(upper(?)), "
                    + " IN_MENOR_VISITADO=?, CO_FUENTE_DATOS=?, FE_VISITA=to_date(?, 'dd/mm/yyyy'), IN_MENOR_ENC= ?, FE_ULTIMA_TOMA_DATOS=to_date(?, 'dd/mm/yyyy'), CO_ENTIDAD_EESS=?, CO_FRECUENCIA_ATENCION=? \n" +
                    "where CO_PADRON_NOMINAL=?";

            Object[] params = new Object[] {
                    padronNominal.getNuCui(), padronNominal.getNuDniMenor(), padronNominal.getTiDocIdentidad(),
                    padronNominal.getApPrimerMenor(), padronNominal.getApSegundoMenor(), padronNominal.getPrenombreMenor(), padronNominal.getFeNacMenor(),
                    padronNominal.getCoGeneroMenor(), padronNominal.getCoUbigeoInei(), padronNominal.getDeDireccion(), padronNominal.getCoCentroPoblado(),
                    padronNominal.getCoNivelPobreza(), padronNominal.getCoEstSalud(), padronNominal.getNuAfiliacion(), padronNominal.getCoInstEducativa(),
                    padronNominal.getCoDniJefeFam(), padronNominal.getApPrimerJefe(), padronNominal.getApSegundoJefe(), padronNominal.getPrenomJefe(),
                    padronNominal.getTiVinculoJefe(), padronNominal.getCoDniMadre(), padronNominal.getApPrimerMadre(), padronNominal.getApSegundoMadre(),
                    padronNominal.getPrenomMadre(), padronNominal.getTiVinculoMadre(), padronNominal.getCoGraInstMadre(), padronNominal.getCoLenMadre(),
                    padronNominal.getUsModiRegistro(), coEntidad, padronNominal.getCoEstSaludNac(), padronNominal.getNuSecuenciaLocal(),
                    padronNominal.getNuSecuenciaLocalNac(), padronNominal.getCoVia(), padronNominal.getDeRefDir(), padronNominal.getCoEstSaludAds(),
                    padronNominal.getNuSecuenciaLocalAds(), padronNominal.getNuCelMadre(), padronNominal.getDiCorreoMadre(), padronNominal.getInMenorVisitado(),
                    padronNominal.getCoFuenteDatos(), padronNominal.getFeVisita(), padronNominal.getCoMenorEncontrado(), padronNominal.getFeUltimaTomaDatos(),
                    coEntidadEESS, padronNominal.getCoFrecAtencion(), padronNominal.getCoPadronNominal()
            };
			log.debug("DAO query='"+queryUpdatePrincipal+"'");
			log.debug("DAO params='"+ ArrayUtils.toString(params)+"'");

            int resultadoPrincipal;
            try {
                logger.debug(String.format("DAO '%s' con '%s'", queryUpdatePrincipal, ArrayUtils.toString(params)));
                resultadoPrincipal = this.jdbcTemplate.update(queryUpdatePrincipal, params);
            } catch (DataIntegrityViolationException e) {
                log.error(e.getMessage(), e);
                log.error("DNI/CUI duplicado");
                throw new DataIntegrityViolationException("DataIntegrityViolationException");
            }
       /*--------------------------------------FIN Actualizacion de Tabla Maestra----------------------------------*/


            return resultadoPrincipal == 1 && resultadoHistorico == 1;

        } else {/*-------------INSERCION -- NUEVO REGISTRO------------------------*/
            //Insertar la información del padron
            if (nuDniMenor == null || nuDniMenor.isEmpty()) {
                //Generar un nuevo número de padron
                padronNominal.setCoPadronNominal(generarNumeroPadronNominal());
                if (nuCui == null || nuCui.isEmpty()) {
                    padronNominal.setTiDocIdentidad("3");
                } else {
                    padronNominal.setTiDocIdentidad("2");
                }
            } else {
                // Se modifica para que el codigo padron sea una secuencia que inicia en 1,2,3,4
//                padronNominal.setCoPadronNominal(padronNominal.getNuDniMenor());
                padronNominal.setCoPadronNominal(generarNumeroPadronNominal());
                padronNominal.setTiDocIdentidad("1");
//                padronNominal.setNuCui(padronNominal.getNuDniMenor());
            }

            String coEntidad=null;
            String coEntidadEESS=null;
            if(padronNominal.getCoTipoEntidad().equals("8")){
                coEntidadEESS = padronNominal.getCoEntidad();
            }
            else {
                coEntidad = padronNominal.getCoEntidad();
            }

            padronNominal.setCoEstado("1");
            //Insertamos la información en la tabla principal
            String queryPrincipal = "" +
                    "INSERT INTO "
                    + "PNTV_PADRON_NOMINAL ( "
                    + "NU_CUI,CO_PADRON_NOMINAL, NU_DNI_MENOR, TI_DOC_IDENTIDAD, "
                    + "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, FE_NAC_MENOR, CO_GENERO_MENOR, "
                    + "CO_UBIGEO_INEI, DE_DIRECCION, CO_CENTRO_POBLADO, "
                    + "CO_NIVEL_POBREZA, CO_EST_SALUD, /*TI_SEGURO_MENOR,*/ NU_AFILIACION, CO_INST_EDUCATIVA, "
                    + "CO_DNI_JEFE_FAM, AP_PRIMER_JEFE, AP_SEGUNDO_JEFE, PRENOM_JEFE, TI_VINCULO_JEFE, "
                    + "CO_DNI_MADRE, AP_PRIMER_MADRE, AP_SEGUNDO_MADRE, PRENOM_MADRE, TI_VINCULO_MADRE, CO_GRA_INST_MADRE, CO_LEN_MADRE, "
                    + "US_CREA_REGISTRO, FE_CREA_REGISTRO, /*US_MODI_REGISTRO, FE_MODI_REGISTRO,*/ TI_REGISTRO, CO_ENTIDAD, "
                    + " ES_PADRON, NU_CNV, NU_EDAD, NU_EDAD_MESES, CO_EST_SALUD_NAC, "
                    + " NU_SECUENCIA_LOCAL, NU_SECUENCIA_LOCAL_NAC, CO_VIA, DE_REF_DIR, CO_EST_SALUD_ADS, NU_SECUENCIA_LOCAL_ADS  "
                    + ", NU_CEL_MADRE, DI_CORREO_MADRE, FE_VISITA, IN_MENOR_ENC, IN_MENOR_VISITADO, CO_FUENTE_DATOS, FE_ULTIMA_TOMA_DATOS, CO_ENTIDAD_EESS, CO_FRECUENCIA_ATENCION "
                    + ") VALUES ( "
                    + "trim(?),?,?,?,"
                    + "trim(upper(?)),trim(upper(?)),trim(upper(?)),to_date(?, 'dd/MM/yyyy'),?,"
                    + "trim(?),trim(upper(?)),trim(?),"
                    + "trim(?),trim(?),/*trim(?),*/trim(?),trim(?),"
                    + "?,trim(upper(?)),trim(upper(?)),trim(upper(?)),?,"
                    + "?,trim(upper(?)),trim(upper(?)),trim(upper(?)),?,?,?,"
                    + "?,sysdate,/*?,sysdate,*/ 'RM', ?, " +
                    "'1', " +
                    "trim(?), trunc(months_between(sysdate, ?)/12), trunc(mod((months_between(sysdate, ?)),12)), trim(?), " +
                    " ?, ?,?,upper(?),?,?, " +
                    " ?, trim(upper(?)),to_date(?, 'dd/MM/yyyy'),?,?,?, to_date(?, 'dd/MM/yyyy'),?,? " +
                    ")";
            Object[] parametrosPrincipal = new Object[]{ padronNominal.getNuCui(), padronNominal.getCoPadronNominal(), padronNominal.getNuDniMenor(), padronNominal.getTiDocIdentidad(),
                    padronNominal.getApPrimerMenor(), padronNominal.getApSegundoMenor(), padronNominal.getPrenombreMenor(), padronNominal.getFeNacMenor(), padronNominal.getCoGeneroMenor(),
                    padronNominal.getCoUbigeoInei(), padronNominal.getDeDireccion(), padronNominal.getCoCentroPoblado(), padronNominal.getCoNivelPobreza(),
                    padronNominal.getCoEstSalud(), /*padronNominal.getTiSeguroMenor(),*/ padronNominal.getNuAfiliacion(), padronNominal.getCoInstEducativa(),
                    padronNominal.getCoDniJefeFam(), padronNominal.getApPrimerJefe(), padronNominal.getApSegundoJefe(), padronNominal.getPrenomJefe(), padronNominal.getTiVinculoJefe(),
                    padronNominal.getCoDniMadre(), padronNominal.getApPrimerMadre(), padronNominal.getApSegundoMadre(), padronNominal.getPrenomMadre(), padronNominal.getTiVinculoMadre(),
                    padronNominal.getCoGraInstMadre(), padronNominal.getCoLenMadre(), padronNominal.getUsCreaRegistro(), /*padronNominal.getUsModiRegistro(),*/ coEntidad,
                    padronNominal.getNuCnv(), padronNominal.getFeNacMenor(), padronNominal.getFeNacMenor(), padronNominal.getCoEstSaludNac(), padronNominal.getNuSecuenciaLocal(),
                    padronNominal.getNuSecuenciaLocalNac(), padronNominal.getCoVia(), padronNominal.getDeRefDir(), padronNominal.getCoEstSaludAds(),padronNominal.getNuSecuenciaLocalAds(),
                    padronNominal.getNuCelMadre(), padronNominal.getDiCorreoMadre(),  padronNominal.getFeVisita(), padronNominal.getCoMenorEncontrado(), padronNominal.getInMenorVisitado(),
                    padronNominal.getCoFuenteDatos(), padronNominal.getFeUltimaTomaDatos(), coEntidadEESS, padronNominal.getCoFrecAtencion()
                };
			log.debug("DAO query='"+queryPrincipal+"'");
			log.debug("DAO params='"+ ArrayUtils.toString(parametrosPrincipal)+"'") ;
//            DataIntegrityViolationException
            int resultadoPrincipal;
            try {
                resultadoPrincipal = this.jdbcTemplate.update(queryPrincipal, parametrosPrincipal);
                padronNominal.setNuSec(0);//insercion por primera vez
            } catch (DataIntegrityViolationException e) {
                log.error(e.getMessage());
                log.error("DNI/CUI duplicado");
                throw new DataIntegrityViolationException("DataIntegrityViolationException");
            }

            //<< insertar log de observaciones de usuario
            insertarObservacion(padronNominal);
            //>>

            // Generamos número histórico secuencial
    /* !!!!     padronNominal.setNuSec(generarNumeroSecPadronHistorico(padronNominal.getCoPadronNominal()));   */

            //Insertamos un nuevo registro histórico
    /* !!!!        Object[] params = new Object[]{padronNominal.getNuCui(), padronNominal.getNuSec(), padronNominal.getCoPadronNominal(), padronNominal.getNuDniMenor(), padronNominal.getTiDocIdentidad(),
                    padronNominal.getApPrimerMenor(), padronNominal.getApSegundoMenor(), padronNominal.getPrenombreMenor(), padronNominal.getFeNacMenor(), padronNominal.getCoGeneroMenor(),
                    padronNominal.getCoUbigeoInei(), padronNominal.getDeDireccion(), padronNominal.getCoCentroPoblado(),
                    padronNominal.getCoNivelPobreza(), padronNominal.getCoEstSalud(), padronNominal.getTiSeguroMenor(), padronNominal.getNuAfiliacion(), padronNominal.getCoInstEducativa(),
                    padronNominal.getCoDniJefeFam(), padronNominal.getApPrimerJefe(), padronNominal.getApSegundoJefe(), padronNominal.getPrenomJefe(), padronNominal.getTiVinculoJefe(),
                    padronNominal.getCoDniMadre(), padronNominal.getApPrimerMadre(), padronNominal.getApSegundoMadre(), padronNominal.getPrenomMadre(), padronNominal.getTiVinculoMadre(), padronNominal.getCoGraInstMadre(), padronNominal.getCoLenMadre(),
                    padronNominal.getUsCreaRegistro(), padronNominal.getUsModiRegistro(), padronNominal.getCoEstado(), padronNominal.getCoEntidad(),
                    padronNominal.getEsPadron(), padronNominal.getNuCnv(), padronNominal.getCoEstSaludNac(),
                    padronNominal.getNuSecuenciaLocalNac(), padronNominal.getNuSecuenciaLocal(),padronNominal.getCoVia(), padronNominal.getDeRefDir(),
            };*/
	/* !!!!		log.debug("DAO query='"+queryHistorico+"'");
			log.debug("DAO params='"+ ArrayUtils.toString(params)+"'");
            int resultadoHistorico = this.jdbcTemplate.update(queryHistorico, params);  */

            String querySoundex = "" +
                    "    INSERT INTO PNTX_PADRON_NOMINAL_SONIDO " +
                    "      (" +
                    "        CO_PADRON_NOMINAL, " +
                    "        AP_PRIMER_MENOR, " +
                    "        AP_SEGUNDO_MENOR, " +
                    "        PRENOMBRE_MENOR " +
                    "      ) " +
                    "      VALUES " +
                    "      ( " +
                    "        ?, " +
                    "        PNPK_SOUNDEXESP.FU_SOUNDESP(?), " +
                    "        PNPK_SOUNDEXESP.FU_SOUNDESP(?), " +
                    "        PNPK_SOUNDEXESP.FU_SOUNDESP(?) " +
                    "      )";

            Object[] paramsSoundex = new Object[] {
                    padronNominal.getCoPadronNominal(),
                    padronNominal.getApPrimerMenor(),
                    padronNominal.getApSegundoMenor(),
                    padronNominal.getPrenombreMenor()
            };
            log.debug("DAO query='"+querySoundex+"'");
            log.debug("DAO params='"+ ArrayUtils.toString(paramsSoundex)+"'");
            try {
                int resultadoSoundex = this.jdbcTemplate.update(querySoundex, paramsSoundex);
               // return resultadoPrincipal == 1 && resultadoHistorico == 1 && resultadoSoundex == 1;
                return resultadoPrincipal == 1  && resultadoSoundex == 1;
            } catch (Exception e) {
                logger.error(e.getMessage());
                //return resultadoPrincipal == 1 && resultadoHistorico == 1;
                return resultadoPrincipal == 1;
            }
//            return resultadoPrincipal == 1 && resultadoHistorico == 1 /*&& resultadoSoundex == 1*/;
        }
    }

    private void insertarObservacion(PadronNominal padronNominal) {
        /*<< insertamos observación: 3 casos :
        1) madre con coPadronNominal
        2) madre sin coPadronNominal (indocumentada)
        3) sin madre
        4) Menor con restriccion de fallecido del DNI
        5) Menor con multiple inscripcion
        6) Menor sin nombre Recien nacidos
        7) Menor con posible duplicado...
        */

        if( ( padronNominal.getCoDniMadre() == null || padronNominal.getCoDniMadre().isEmpty()) &&
            (!padronNominal.getApPrimerMadre().isEmpty() && !padronNominal.getApSegundoMadre().isEmpty() && !padronNominal.getPrenomMadre().isEmpty()) ){

            PadronObservado padronObservado = new PadronObservado(padronNominal, PadronObservado.Tipo.MADRE_INDOCUMENTADA);
            padronObservado.setEsObservado("1");
            padronObservado.setDeObservadoDetalle("MADRE INDOCUMENTADA");
            padronObservadoDao.insertPadronObservado(padronObservado);
            setEsPadronInit(padronNominal.getCoPadronNominal(), ES_PADRON_OBSERVADO);
        } else if (padronNominal.getPrenomMadre().isEmpty() && padronNominal.getApPrimerMadre().isEmpty() &&
                   padronNominal.getApSegundoMadre().isEmpty() && padronNominal.getCoDniMadre().isEmpty()) {
            // caso 2
            PadronObservado padronObservado = new PadronObservado(padronNominal, PadronObservado.Tipo.SIN_DATOS_MADRE);
            padronObservado.setEsObservado("1");
            padronObservado.setDeObservadoDetalle("MADRE SIN DATOS");
            padronObservadoDao.insertPadronObservado(padronObservado);
            setEsPadronInit(padronNominal.getCoPadronNominal(), ES_PADRON_OBSERVADO);
        }

        if (padronNominal.getCoRestri() != null && padronNominal.getDeRestri() != null ) {
            String deRestri = padronNominal.getDeRestri().trim();
            // caso 4
            if (deRestri.equals(DE_RESTRICCION_FALLECIMIENTO)) {
                PadronObservado padronObservado = new PadronObservado(padronNominal, PadronObservado.Tipo.FALLECIDO);
                padronObservado.setEsObservado("1");
                padronObservado.setDeObservadoDetalle("FALLECIDO");
                padronObservadoDao.insertPadronObservado(padronObservado);
                setEsPadronInit(padronNominal.getCoPadronNominal(), ES_PADRON_OBSERVADO);
            } else if (deRestri.equals(DE_RESTRICCION_MULTIPLE_INSCRIPCION)) { // caso 5
                PadronObservado padronObservado = new PadronObservado(padronNominal, PadronObservado.Tipo.MULTIPLE_INSCRIPCION_IDENTIDAD);
                padronObservado.setEsObservado("1");
                padronObservado.setDeObservadoDetalle("MULTIPLE_INSCRIPCION_IDENTIDAD");
                padronObservadoDao.insertPadronObservado(padronObservado);
                setEsPadronInit(padronNominal.getCoPadronNominal(), ES_PADRON_OBSERVADO);
            }
        }

        // caso 6
        if (padronNominal.getPrenombreMenor() != null && !padronNominal.getPrenombreMenor().isEmpty()) {
            String prenombreMenor = padronNominal.getPrenombreMenor();
            String nuDniMenor = padronNominal.getNuDniMenor();
            if (nuDniMenor.trim().isEmpty()) {

                List<String> sinPrenombreMenor = dominioService.getDeTipoSinNombre();
                if (sinPrenombreMenor!=null && sinPrenombreMenor.contains(prenombreMenor) && prenombreMenor.length() < 3 && padronNominal.getNuDniMenor()!=null) {
                    PadronObservado padronObservado = new PadronObservado(padronNominal, PadronObservado.Tipo.MENOR_SIN_NOMBRE);
                    padronObservado.setEsObservado("1");
                    padronObservado.setDeObservadoDetalle("MENOR SIN NOMBRE");
                    padronObservadoDao.insertPadronObservado(padronObservado);
                    setEsPadronInit(padronNominal.getCoPadronNominal(), ES_PADRON_OBSERVADO);
                }
            }
        }

        // caso 7
        /*if (existePosibleDuplicado(padronNominal)) {
            PadronObservado padronObservado = new PadronObservado(padronNominal, PadronObservado.Tipo.REGISTRO_POSIBLE_DUPLICADO);
            padronObservado.setEsObservado("1");
            padronObservado.setDeObservadoDetalle("POSIBLE DUPLICADO");
            padronObservadoDao.insertPadronObservado(padronObservado);
            setEsPadron(padronNominal.getCoPadronNominal(), ES_PADRON_OBSERVADO);
        }*/
    }

    @Override
    public Boolean existePosibleDuplicado(PadronNominal padronNominal) {
        String sql = "" +
                "SELECT COUNT(1)\n" +
                "  FROM PNTV_PADRON_NOMINAL\n" +
                " WHERE FE_NAC_MENOR = ?\n" +
                "   AND CO_UBIGEO_INEI = ?\n" +
                "   AND CO_GENERO_MENOR = ?\n" +
                "   AND CO_DNI_MADRE = ?\n" +
                "   AND AP_PRIMER_MADRE = ?\n" +
                "   AND AP_SEGUNDO_MADRE = ?\n" +
                "   AND PRENOM_MADRE = ?";

        Object[] params = new Object[]{
                padronNominal.getFeNacMenor(),
                padronNominal.getCoUbigeoInei(),
                padronNominal.getCoGeneroMenor(),
                padronNominal.getCoDniMadre(),
                padronNominal.getApPrimerMadre(),
                padronNominal.getApSegundoMadre(),
                padronNominal.getPrenomMadre(),
        };

        logger.debug(String.format("DAO '%s', con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForObject(sql, params, Integer.class) > 1;
        } catch (Exception e) {
            logger.error("Error:", e);
            return false;
        }
    }

    @Autowired
    PadronProgramaDao padronProgramaDao;

    @Autowired
    PadronSeguroDao padronSeguroDao;

    //@Autowired
    private List<Dominio> listarProgramas(String coPadronNominal) {
        String query = "SELECT pr.CO_PROGRAMA_SOCIAL as co_dominio, pr.DE_PROGRAMA as de_dom " +
                "FROM PNTV_PADRON_PROGRAMA p " +
                "JOIN PNTM_PROGRAMA_SOCIAL pr on pr.co_programa_social=p.CO_PROGRAMA_SOCIAL " +
                "WHERE CO_PADRON_NOMINAL=? ";
        return this.jdbcTemplate.query(query, new Object[]{coPadronNominal}, BeanPropertyRowMapper.newInstance(Dominio.class));
    }

    private List<Dominio> listarSeguros(String coPadronNominal) {
        String query = "SELECT pr.co_tipo_seguro as co_dominio, pr.de_seguro as de_dom \n" +
                "FROM pntv_padron_seguro p \n" +
                "JOIN pntr_tipo_seguro pr on pr.co_tipo_seguro=p.co_tipo_seguro \n" +
                "WHERE p.co_padron_nominal=? ";
        return this.jdbcTemplate.query(query, new Object[]{coPadronNominal}, BeanPropertyRowMapper.newInstance(Dominio.class));
    }

    Logger log = Logger.getLogger(this.getClass());

    @Override
    public int generarNumeroSecPadronHistorico(String coPadronNominal) {
        String query =
                "SELECT NVL(MAX(NU_SEC),0)+1 \n" +
                        "FROM PNTH_PADRON_NOMINAL \n" +
                        "WHERE CO_PADRON_NOMINAL=? ";

		log.info("DAO '" + query + "' con '" + coPadronNominal + "' por ejecutar");
        return jdbcTemplate.queryForInt(query, coPadronNominal);
    }

    public boolean insertarPadronMovimiento(PadronNominal padronNominal) {
        String coPadronNominal = padronNominal.getCoPadronNominal();
        String query = "" +
                "SELECT * FROM ( \n"+
                    "SELECT co_ubigeo_inei AS coUbigeoInei, co_entidad AS coEntidad, co_entidad_eess coEntidadEESS , NU_SEC AS nuSec, CO_CENTRO_POBLADO AS coCentroPoblado \n" +
                    "FROM PNTH_PADRON_NOMINAL H \n" +
                    "WHERE CO_PADRON_NOMINAL = ? \n" +
                    "ORDER BY NVL(H.NU_SEC,0) DESC, NVL(H.FE_MODI_REGISTRO, H.FE_CREA_REGISTRO) DESC) \n" +
                "WHERE ROWNUM = 1";
        PadronNominal padronNominalAnt;
        try {
            logger.debug(String.format("DAO '%s' con '%s'", query, coPadronNominal));
            padronNominalAnt = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(PadronNominal.class), new Object[]{coPadronNominal});
            /*logger.info("padronNominalAnt::" + padronNominalAnt);*/
        } catch (EmptyResultDataAccessException erdae) {
            erdae.printStackTrace();
            padronNominalAnt = null;
        } catch (IncorrectResultSizeDataAccessException irsdae) {
            irsdae.printStackTrace();
            padronNominalAnt = null;
        }


        if (padronNominalAnt != null && padronNominalAnt.getCoUbigeoInei() != null && padronNominalAnt.getCoCentroPoblado() != null &&
                (!padronNominalAnt.getCoUbigeoInei().equals(padronNominal.getCoUbigeoInei())
                        || !padronNominalAnt.getCoCentroPoblado().equals(padronNominal.getCoCentroPoblado())) ) {

            String queryPadronMovimiento =
                    "        INSERT INTO PNTV_PADRON_MOVIMIENTO ( " +
                            "        CO_PADRON_NOMINAL, " +
                            "        CO_UBIGEO_INEI_ANT, " +
                            "        CO_ENTIDAD_ANT, " +
                            "        CO_ENTIDAD_ANT_EESS, " +
                            "        NU_SEC_ANT, " +
                            "        CO_UBIGEO_INEI_ACT, " +
                            "        CO_ENTIDAD_ACT, " +
                            "        CO_ENTIDAD_ACT_EESS, " +
                            "        NU_SEC_ACT, " +
                            "        FE_CREA_AUDI, " +
                            "        US_CREA_AUDI, " +
                            "        FE_MODI_AUDI, " +
                            "        US_MODI_AUDI, " +
                            "        CO_CENTRO_POBLADO_ANT, " +
                            "        CO_CENTRO_POBLADO_ACT) " +
                            "        VALUES ( " +
                            "          ?, " +
                            "          ?, " +
                            "          ?, " +
                            "          ?, " +
                            "          ?, " +
                            "          ?, " +
                            "          ?, " +
                            "          ?, " +
                            "          ?, " +
                            "          sysdate, " +
                            "          ?, " +
                            "          sysdate, " +
                            "          ?, " +
                            "          ?, " +
                            "          ? ) ";

            Object[] params = new Object[]{
                    coPadronNominal,
                    padronNominalAnt.getCoUbigeoInei(),
                    padronNominalAnt.getCoEntidad(),
                    padronNominalAnt.getCoEntidadEESS(),
                    padronNominalAnt.getNuSec(),
                    padronNominal.getCoUbigeoInei(),
                    padronNominal.getCoEntidad(),
                    padronNominal.getCoEntidadEESS(),
                    padronNominal.getNuSec(),
                    padronNominal.getUsModiRegistro(),
                    padronNominal.getUsModiRegistro(),
                    padronNominalAnt.getCoCentroPoblado(),
                    padronNominal.getCoCentroPoblado(),
            };
            logger.debug(String.format("DAO '%s' con '%s'", queryPadronMovimiento, ArrayUtils.toString(params)));
            int resultadoPadronMovimiento = this.jdbcTemplate.update(queryPadronMovimiento, params);
            return resultadoPadronMovimiento == 1;
        }
        return false;
    }

    @Override
    public Integer getCoPadronNominalByDni(String nuDniMenor) {
        String query = "select co_padron_nominal from pntv_padron_nominal where nu_dni_menor=?";
        try {
            log.debug(String.format("DAO '%s' con '%s'", query, nuDniMenor));
            Integer coPadronNominal = jdbcTemplate.queryForInt(query, nuDniMenor);
            log.info("coPadronNominal: " + coPadronNominal);
            return coPadronNominal;
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Integer getCoPadronNominalByCui(String nuCui) {
        String query = "select co_padron_nominal from pntv_padron_nominal where nu_cui=?";
        try {
            log.debug(String.format("DAO '%s' con '%s'", query, nuCui));
            return jdbcTemplate.queryForInt(query, nuCui);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean existeNuCnv(String nuCnv) {
        String query = "select count(1) from pntv_padron_nominal where nu_cnv=? and es_padron='1'";
        Object[] params = new Object[]{nuCnv};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params) > 0;
    }

    @Override
    public boolean existeNuDniMenor(String nuDniMenor) {
        String query = "select count(1) from pntv_padron_nominal where nu_dni_menor=?";
        Object[] params = new Object[]{nuDniMenor};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params) > 0;
    }

    @Override
    public boolean existeNuCui(String nuCui) {
        String query = "select count(1) from pntv_padron_nominal where nu_cui=?";
        Object[] params = new Object[]{nuCui};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params) > 0;
    }

    @Override
    public boolean existeCoPadronNominal(String coPadronNominal) {
        String query = "select count(1) from pntv_padron_nominal where co_padron_nominal=?";
        Object[] params = new Object[]{coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params) > 0;
    }


    /**
     * Buscar duplicados por nombres, apellidos, fecha de nacimiento y genero
     * @param padronNominal
     * @return
     */
    @Override
    public List<PadronNominal> buscarDuplicados(PadronNominal padronNominal, String esPadron) {
        String feNacimiento = padronNominal.getFeNacMenor();
        feNacimiento = feNacimiento.split("/")[2] + feNacimiento.split("/")[1] + feNacimiento.split("/")[0];
        String query = "" +
                "SELECT p.co_padron_nominal, p.nu_dni_menor, p.ap_primer_menor, " +
                "p.ap_segundo_menor, p.prenombre_menor, to_char(p.fe_nac_menor, 'DD/MM/YYYY') fe_nac_menor, p.co_ubigeo_inei, " +
                "u.de_departamento, u.de_provincia, u.de_distrito " +
                "FROM pntv_padron_nominal p " +
                "  LEFT join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                " WHERE translate(trim(p.ap_primer_menor), 'ÁÉÍÓÚÜ', 'AEIOUU') = ? " +
                "   AND translate(trim(p.ap_segundo_menor), 'ÁÉÍÓÚÜ', 'AEIOUU') = ? " +
                "   AND translate(trim(p.prenombre_menor), 'ÁÉÍÓÚÜ', 'AEIOUU') = ?" +
                "   AND to_char(p.fe_nac_menor, 'YYYYMMDD') = ? " +
                "   AND p.co_genero_menor= ?" +
                "   AND p.es_padron= ? ";
        Object[] params = new Object[]{padronNominal.getApPrimerMenor().trim(), padronNominal.getApSegundoMenor().trim(), padronNominal.getPrenombreMenor().trim(), feNacimiento, padronNominal.getCoGeneroMenor(),esPadron};
        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  Buscar coencidencias tokenizando apellidos y nombres; comparando las primeras ocurrencias
     * @param apPrimerMenor
     * @param apSegundoMenor
     * @param prenombreMenor
     * @return
     */
    @Override
    public List<Persona> listarNombresSimilaresPrimerasOcurrencias(String apPrimerMenor, String apSegundoMenor, String prenombreMenor) {
        apPrimerMenor = apPrimerMenor.trim();
        apSegundoMenor = apSegundoMenor.trim();
        prenombreMenor = prenombreMenor.trim();

        String apPrimerMenorParts[] = apPrimerMenor.split(" ");
        String apSegundoMenorParts[] = apSegundoMenor.split(" ");
        String prenombreMenorParts[] = prenombreMenor.split(" ");


        String apPrimerMenorPart = "";
        String apSegundoMenorPart = "";
        String prenombreMenorPart = "";

        if (apPrimerMenorParts != null && apSegundoMenorParts != null && prenombreMenorParts != null) {
            if (apPrimerMenorParts[0] != null && apSegundoMenorParts[0] != null && prenombreMenorParts[0] != null &&
                    !apPrimerMenorParts[0].isEmpty() && !apSegundoMenorParts[0].isEmpty() && !prenombreMenorParts[0].isEmpty()) {
                apPrimerMenorPart = apPrimerMenorParts[0];
                apSegundoMenorPart = apSegundoMenorParts[0];
                prenombreMenorPart = prenombreMenorParts[0];
            }
        }

        String query = "" +
                "select co_padron_nominal codigoPadronNominal, " +
                "       ap_primer_menor primerApellido, " +
                "       ap_segundo_menor segundoApellido, " +
                "       prenombre_menor nombres, " +
                "       TO_CHAR(FE_NAC_MENOR, 'dd/mm/yyyy') fechaNacimiento, " +
                "       nu_dni_menor dni " +
                "  from pntv_padron_nominal " +
                " where ap_primer_menor = ? " +
                "   and ap_segundo_menor = ? " +
                "   and prenombre_menor = ? " +
                "   and es_padron = '1'";
        Object[] params = new Object[]{apPrimerMenorPart, apSegundoMenorPart, prenombreMenorPart};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Persona.class), params);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Persona>();
        }
    }

    @Override
    public void insertarPadronHis(String coPadronNominal) {
        String query = "INSERT INTO PNTH_PADRON_NOMINAL\n " +
                " (CO_PADRON_NOMINAL,\n " +
                "  NU_DNI_MENOR,\n " +
                "  AP_PRIMER_MENOR,\n " +
                "  AP_SEGUNDO_MENOR,\n " +
                "  PRENOMBRE_MENOR,\n " +
                "  FE_NAC_MENOR,\n " +
                "  CO_UBIGEO_INEI,\n " +
                "  CO_EST_SALUD,\n " +
                "  CO_GENERO_MENOR,\n " +
                "  TI_DOC_IDENTIDAD,\n " +
                "  DE_DIRECCION,\n " +
                "  TI_SEGURO_MENOR,\n " +
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
                "  NU_SECUENCIA_LOCAL_NAC," +
                "  NU_SECUENCIA_LOCAL_ADS,"+
                "  NU_CEL_MADRE,"+
                "  DI_CORREO_MADRE,"+
                "  FE_VISITA,"+
                "  IN_MENOR_ENC,"+
                "  IN_MENOR_VISITADO,"+
                "  CO_FUENTE_DATOS,"+
                "  FE_ULTIMA_TOMA_DATOS"+
                ")\n " +
                " SELECT CO_PADRON_NOMINAL,\n " +
                "        NU_DNI_MENOR,\n " +
                "        AP_PRIMER_MENOR,\n " +
                "        AP_SEGUNDO_MENOR,\n " +
                "        PRENOMBRE_MENOR,\n " +
                "        FE_NAC_MENOR,\n " +
                "        CO_UBIGEO_INEI,\n " +
                "        CO_EST_SALUD,\n " +
                "        CO_GENERO_MENOR,\n " +
                "        TI_DOC_IDENTIDAD,\n " +
                "        DE_DIRECCION,\n " +
                "        TI_SEGURO_MENOR,\n " +
                "        TI_PRO_SOCIAL,\n " +
                "        CO_INST_EDUCATIVA,\n " +
                "        CO_DNI_JEFE_FAM,\n " +
                "        AP_PRIMER_JEFE,\n " +
                "        AP_SEGUNDO_JEFE,\n " +
                "        PRENOM_JEFE,\n " +
                "        TI_VINCULO_JEFE,\n " +
                "        CO_DNI_MADRE,\n " +
                "        AP_PRIMER_MADRE,\n " +
                "         AP_SEGUNDO_MADRE,\n " +
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
                "        NU_SECUENCIA_LOCAL_NAC, " +
                "        NU_SECUENCIA_LOCAL_ADS," +
                "        NU_CEL_MADRE," +
                "        DI_CORREO_MADRE," +
                "        FE_VISITA," +
                "        IN_MENOR_ENC," +
                "        IN_MENOR_VISITADO," +
                "        CO_FUENTE_DATOS," +
                "        FE_ULTIMA_TOMA_DATOS" +
                "  FROM PNTV_PADRON_NOMINAL PN\n " +
                "  WHERE PN.CO_PADRON_NOMINAL = ?";

        Object[] params = new Object[]{coPadronNominal, coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        this.jdbcTemplate.update(query, params);
    }
    @Override
    public boolean isPrecarga(String coPadronNominal){
        String sql = "SELECT COUNT(1) FROM PNTV_PADRON_NOMINAL WHERE CO_PADRON_NOMINAL=? AND FUENTE_PRECARGA IS NOT NULL";
        Object[] params = {coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForInt(sql, params) > 0;
        }
        catch(Exception e){
            logger.info("Error"+e.toString());
            return false;
        }
    }

    @Override
    public boolean isPrecargaByDni(String nuDni){
        String sql = "SELECT COUNT(1) FROM PNTV_PADRON_NOMINAL WHERE NU_DNI_MENOR=? AND FUENTE_PRECARGA IS NOT NULL";
        Object[] params = {nuDni};
        logger.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForInt(sql, params) > 0;
        }
        catch(Exception e){
            logger.info("Error"+e.toString());
            return false;
        }
    }

    @Override
    public Persona obtenerDatosPadronPorcoPadron(String coPadron){
        String query = "select p.co_padron_nominal codigoPadronNominal, p.nu_dni_menor dni, p.nu_cui nuCui \n" +
                "from pntv_padron_nominal p \n" +
                "where p.co_padron_nominal=?";

        Object[] params = new Object[]{coPadron};
        //  logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
        } catch (EmptyResultDataAccessException e) {
            logger.info("error getDataMenor"+e.toString());
            return null;
        } catch (Exception e) {
            logger.info("error getDataMenor"+e.toString());
            return null;
        }
    }

    @Override
    public Persona obtenerDatosPadronPorDni(String cnv){
        String query = "select p.co_padron_nominal codigoPadronNominal, p.nu_dni_menor dni, p.nu_cui nuCui \n" +
                "from pntv_padron_nominal p \n" +
                "where p.nu_cnv=?";

        Object[] params = new Object[]{cnv};
      //  logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
        } catch (EmptyResultDataAccessException e) {
            logger.info("error getDataMenor"+e.toString());
            return null;
        } catch (Exception e) {
            logger.info("error getDataMenor"+e.toString());
            return null;
        }
    }
  //  @Transactional
    @Override
    public void actualizarPadronNominal(Persona dataMenorRC){
        String query;
        Object[] params;

        String apPaternoMenor   = (dataMenorRC.getPrimerApellido() != null) ? dataMenorRC.getPrimerApellido().toUpperCase(): " ";
        String apMaternoMenor   = (dataMenorRC.getSegundoApellido() != null) ? dataMenorRC.getSegundoApellido().toUpperCase(): " ";
        String nombresMenor     = (dataMenorRC.getNombres() != null) ? dataMenorRC.getNombres().toUpperCase(): " ";
        String apPaternoPadre   = (dataMenorRC.getPadrePrimerApellido() != null) ? dataMenorRC.getPadrePrimerApellido().toUpperCase(): " ";
        String apMaternoPadre   = (dataMenorRC.getPadreSegundoApellido() != null) ? dataMenorRC.getPadreSegundoApellido().toUpperCase(): " ";
        String nombresPadre     = (dataMenorRC.getPadreNombres() != null) ? dataMenorRC.getPadreNombres().toUpperCase():" ";
        String dniPadre         = (dataMenorRC.getPadreDni() != null) ? dataMenorRC.getPadreDni(): " ";

        if(dataMenorRC.getCnv()!="" && dataMenorRC.getCnv()!= null && !dataMenorRC.getCnv().equals("0")){
             query  =   "update pntv_padron_nominal \n" +
                        "set nu_dni_menor=?,\n" +
                        "    nu_cui=?,\n" +
                        "    ap_primer_menor=?,\n" +
                        "    ap_segundo_menor=?,\n" +
                        "    prenombre_menor=?,\n" +
                        "    ap_primer_jefe=?,\n" +
                        "    ap_segundo_jefe=?,\n" +
                        "    prenom_jefe=?, \n "+
                        "    co_dni_jefe_fam=?,\n" +
                        "    ti_vinculo_jefe=? \n" +
                        "where nu_cnv=? ";


            params = new Object[]{
                    dataMenorRC.getNuCui(),
                    dataMenorRC.getNuCui(),
                    apPaternoMenor,
                    apMaternoMenor,
                    nombresMenor,
                    apPaternoPadre,
                    apMaternoPadre,
                    nombresPadre,
                    dniPadre,
                    dataMenorRC.getPadreCodigoTipoVinculo(),
                    dataMenorRC.getCnv()
            };
        }
        else{
            query = "update pntv_padron_nominal \n" +
                    "set nu_dni_menor=?,\n" +
                    "    nu_cui=?,\n" +
                    "    ap_primer_menor=?,\n" +
                    "    ap_segundo_menor=?,\n" +
                    "    prenombre_menor=?,\n" +
                    "    ap_primer_jefe=?,\n" +
                    "    ap_segundo_jefe=?,\n" +
                    "    prenom_jefe=?, \n "+
                    "    co_dni_jefe_fam=?,\n" +
                    "    ti_vinculo_jefe=? \n" +
                    " where co_dni_madre=? \n" +
                    " and fe_nac_menor=to_date(?,'yyyymmdd') \n" +
                    " and ho_nac_menor=? \n" +
                    " and co_genero_menor=? \n";

            params = new Object[]{
                    dataMenorRC.getNuCui(),
                    dataMenorRC.getNuCui(),
                    apPaternoMenor,
                    apMaternoMenor,
                    nombresMenor,
                    apPaternoPadre,
                    apMaternoPadre,
                    nombresPadre,
                    dniPadre,
                    dataMenorRC.getPadreCodigoTipoVinculo(),
                    dataMenorRC.getMadreDni(),
                    dataMenorRC.getFechaNacimiento(),
                    dataMenorRC.getHoraNacimiento(),
                    dataMenorRC.getGenero()
            };
        }

        try {
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            jdbcTemplate.update(query, params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("ERROR al actualizar informacion de RC en PN");
        }
    }

    public void setEsObservado(String esObservado, String coPadronNominal){
        String sql = "UPDATE PNTM_PADRON_OBSERVADO \nSET ES_OBSERVADO=? \nWHERE CO_PADRON_NOMINAL=?";
        Object[] params = { esObservado, coPadronNominal};
        logger.info(String.format("DAO '%s' con '%s'", new Object[] { sql, ArrayUtils.toString(params) }));
        this.jdbcTemplate.update(sql, params);
    }

    public boolean esObservado(String esObservado , String coPadronNominal){
        String sql = "SELECT COUNT(1) FROM PNTM_PADRON_OBSERVADO WHERE ES_OBSERVADO=? AND CO_PADRON_NOMINAL=?";
        Object[] params = { esObservado, coPadronNominal};
        logger.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(sql, params) > 0;
    }

    public String obtenerCoPadronNominal(String nuCnv)
    {
        String sql = "SELECT NVL(CO_PADRON_NOMINAL,0) FROM PNTV_PADRON_NOMINAL\nWHERE NU_CNV=? AND (ES_PADRON='1' OR ES_PADRON='2') ";
        Object[] params = { nuCnv };
        logger.debug(String.format("DAO '%s' con '%s'", new Object[] { sql, ArrayUtils.toString(params) }));
        return (String)this.jdbcTemplate.queryForObject(sql, String.class, params);
    }

    public boolean verificaExisteCnv(String cnv)
    {
        logger.info("Inicia metodo verificarExisteCNV");
        boolean existe = false;
        String query = "SELECT NVL(PN.CO_PADRON_NOMINAL,'0') \n FROM PNTV_PADRON_NOMINAL PN \n WHERE PN.NU_CNV = ?";
        String nuCui = "0";
        try
        {
            nuCui = (String)this.jdbcTemplate.queryForObject(query, String.class, new Object[] {cnv});
        }
        catch (Exception e)
        {
            logger.error("Error en verificarExisteCNV", e);
        }
        logger.info("nuCui:" + nuCui);
        if (!nuCui.equals("0")) {
            existe = true;
        }
        return existe;
    }

    public String obtenerCodigoPadronPorHoraYFecha(Persona personaRc){
        logger.info("Inicia metodo obtenerCodigoPadron DAO");
        String sql = "select co_padron_nominal " +
                "from pntv_padron_nominal " +
                "where co_dni_madre=? " +
                "and fe_nac_menor=to_date(?,'yyyymmdd') " +
                "and co_genero_menor=? " +
                "and ho_nac_menor=?";
        Object[] params = {personaRc.getMadreDni(),
                personaRc.getFechaNacimiento(),
                personaRc.getGenero(),
                personaRc.getHoraNacimiento() };

        try {
            logger.debug(String.format("DAO '%s'", new Object[]{sql}));
            return this.jdbcTemplate.queryForObject(sql, String.class, params);
        } catch (EmptyResultDataAccessException e) {
            logger.error("No existe registro con fecha, hora, genero y dniMadre especificada:");
            return "";
        } catch (Exception e) {
            logger.error("Error:", e);
            return "";
        }
    }

}