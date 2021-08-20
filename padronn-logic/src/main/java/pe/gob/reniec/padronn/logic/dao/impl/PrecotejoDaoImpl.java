package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.PadronProgramaDao;
import pe.gob.reniec.padronn.logic.dao.PrecotejoDao;
import pe.gob.reniec.padronn.logic.model.Precotejo;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistroDetalle;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Clase PrecotejoDaoImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 17/05/13 04:35 PM
 */
@Repository
public class PrecotejoDaoImpl
		extends AbstractBaseDao
		implements PrecotejoDao {

	@Autowired
	PadronProgramaDao padronProgramaDao;

	@Override
	@Transactional
	public void insert(Precotejo precotejo) {

		String query = "" +

				"INSERT INTO " +
				"PNTV_PRECOTEJO " +
				"(" +
				"CO_ENTIDAD, " +
				"NU_ENVIO, " +
				"US_CREA_AUDI, " +
				"FE_CREA_AUDI, " +
				"US_MODI_AUDI, " +
				"FE_MODI_AUDI, " +
				"NO_ARCHIVO_ORIGINAL, " +
				"ES_ENVIO, " +
				"ES_ENVIO_HK " +
				") " +

				"VALUES " +
				"( " +
				"?, " +
				"?, " +
				"?, " +
				"SYSDATE, " +
				"?, " +
				"SYSDATE, " +
				"?, " +
				"?," +
				"? " +
				") ";

//		log.info("DAO '" + query + "' con '" + precotejo + "' por ejecutar");
		jdbcTemplate.update(query,
				precotejo.getCoEntidad(),
				precotejo.getNuEnvio(),
				precotejo.getUsCreaAudi(),
				precotejo.getUsModiAudi(),
				precotejo.getNoArchivoOriginal(),
				precotejo.getEsEnvio(),
				precotejo.getEsEnvio());

		// histórico
		String queryHk = "" +

				"INSERT INTO " +
				"PNTH_PRECOTEJO " +
				"( " +
				"CO_ENTIDAD, " +
				"NU_ENVIO, " +
				"ES_ENVIO_INICIAL, " +
				"ES_ENVIO_FINAL, " +
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
				") ";

//		log.info("DAO '" + queryHk + "' con '" + precotejo + "' por ejecutar");
		jdbcTemplate.update(queryHk,
				precotejo.getCoEntidad(),
				precotejo.getNuEnvio(),
				"", //estado inicial <null>
				precotejo.getEsEnvio(),
				precotejo.getUsCreaAudi(),
				precotejo.getUsModiAudi());


		//throw new RuntimeException("TEST");
	}


	@Override
	public void insert(PrecotejoRegistro precotejoRegistro) {

		String query = "" +

				"INSERT INTO " +
				"PNTV_PRECOTEJO_REGISTRO " +
				"(" +
				"CO_ENTIDAD, " +
				"NU_ENVIO, " +
				"NU_DNI_MENOR, " +
				"AP_PRIMER_MENOR, " +
				"AP_SEGUNDO_MENOR, " +
				"PRENOMBRE_MENOR, " +
				"FE_NAC_MENOR, " +
				"CO_GENERO_MENOR, " +
				"CO_UBIGEO_INEI, " +
				"CO_CENTRO_POBLADO, " +
				"CO_EST_SALUD, " +
				"TI_SEGURO_MENOR, " +
				"CO_DNI_JEFE_FAM, " +
				"AP_PRIMER_JEFE, " +
				"AP_SEGUNDO_JEFE, " +
				"PRENOM_JEFE, " +
				"TI_VINCULO_JEFE, " +
				"CO_DNI_MADRE, " +
				"TI_DOC_IDENTIDAD, " +
				"DE_DIRECCION, " +
				"CO_INST_EDUCATIVA, " +
				"AP_PRIMER_MADRE, " +
				"AP_SEGUNDO_MADRE, " +
				"PRENOM_MADRE, " +
				"TI_VINCULO_MADRE, " +
				"CO_GRA_INST_MADRE, " +
				"CO_LEN_MADRE, " +
				"CO_NIVEL_POBREZA, " +
				"US_CREA_REGISTRO, " +
				"FE_CREA_REGISTRO, " +
				"US_MODI_REGISTRO, " +
				"FE_MODI_REGISTRO, " +
				"CO_ETNIA, " +
				"NU_AFILIACION, " +
				"ES_REGISTRO, " +
				"TI_PRO_SOCIAL," +
				"NU_REGISTRO  " +
				") " +

				"VALUES " +
				"( " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"?, " +
				"sysdate, " +
				"?, " +
				"sysdate, " +
				"?, " +
				"?, " +
				"?, " +
				"?," +
				"? " +
				")";

//		log.info("DAO '" + query + "' con '" + precotejoRegistro + "' por ejecutar");
//        log.debug("fe_nac_menor==" + precotejoRegistro.getFeNacMenor());
        try{
        jdbcTemplate.update(query,
                precotejoRegistro.getCoEntidad(),
                precotejoRegistro.getNuEnvio(),
                precotejoRegistro.getNuDniMenor(),
                precotejoRegistro.getApPrimerMenor(),
                precotejoRegistro.getApSegundoMenor(),
                precotejoRegistro.getPrenombreMenor(),
                precotejoRegistro.getFeNacMenor(),
                precotejoRegistro.getCoGeneroMenor(),
                precotejoRegistro.getCoUbigeoInei(),
                precotejoRegistro.getCoCentroPoblado(),
                precotejoRegistro.getCoEstSalud(),
                precotejoRegistro.getTiSeguroMenor(),
                precotejoRegistro.getCoDniJefeFam(),
                precotejoRegistro.getApPrimerJefe(),
                precotejoRegistro.getApSegundoJefe(),
                precotejoRegistro.getPrenomJefe(),
                precotejoRegistro.getTiVinculoJefe(),
                precotejoRegistro.getCoDniMadre(),
                precotejoRegistro.getTiDocIdentidad(),
                precotejoRegistro.getDeDireccion(),
                precotejoRegistro.getCoInstEducativa(),
                precotejoRegistro.getApPrimerMadre(),
                precotejoRegistro.getApSegundoMadre(),
                precotejoRegistro.getPrenomMadre(),
                precotejoRegistro.getTiVinculoMadre(),
                precotejoRegistro.getCoGraInstMadre(),
                precotejoRegistro.getCoLenMadre(),
                precotejoRegistro.getCoNivelPobreza(),
                precotejoRegistro.getUsCreaRegistro(),
                precotejoRegistro.getUsModiRegistro(),
                precotejoRegistro.getCoEtnia(),
                precotejoRegistro.getNuAfiliacion(),
                padronProperties.ESREGISTRO_REG_OK,
                precotejoRegistro.getTiProSocial(),
                precotejoRegistro.getNo());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


	@Override
	public void insertObs(PrecotejoRegistro precotejoRegistro) {
		String query = "" +

				"INSERT INTO " +
				"PNTV_PRECOTEJO_REGISTRO_OBS " +
				"(" +
				"CO_ENTIDAD, " +
				"NU_ENVIO, " +
				"NU_DNI_MENOR, " +
				"AP_PRIMER_MENOR, " +
				"AP_SEGUNDO_MENOR, " +
				"PRENOMBRE_MENOR, " +
				"FE_NAC_MENOR, " +
				"CO_GENERO_MENOR, " +
				"CO_UBIGEO_INEI, " +
				"CO_CENTRO_POBLADO, " +
				"CO_EST_SALUD, " +
				"TI_SEGURO_MENOR, " +
				"CO_DNI_JEFE_FAM, " +
				"AP_PRIMER_JEFE, " +
				"AP_SEGUNDO_JEFE, " +
				"PRENOM_JEFE, " +
				"TI_VINCULO_JEFE, " +
				"CO_DNI_MADRE, " +
				"TI_DOC_IDENTIDAD, " +
				"DE_DIRECCION, " +
				"CO_INST_EDUCATIVA, " +
				"AP_PRIMER_MADRE, " +
				"AP_SEGUNDO_MADRE, " +
				"PRENOM_MADRE, " +
				"TI_VINCULO_MADRE, " +
				"CO_GRA_INST_MADRE, " +
				"CO_LEN_MADRE, " +
				"CO_NIVEL_POBREZA, " +
				"US_CREA_REGISTRO, " +
				"FE_CREA_REGISTRO, " +
				"US_MODI_REGISTRO, " +
				"FE_MODI_REGISTRO, " +
				"CO_ETNIA, " +
				"NU_AFILIACION, " +
				"DE_OBSERVACION, " +
				"ES_REGISTRO, " +
				"TI_PRO_SOCIAL, " +
				"NU_REGISTRO " +
				") " +

				"VALUES " +
				"( " +
				"?, " +
				"?, " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,40), " +
				"SUBSTR(?,1,40), " +
				"SUBSTR(?,1,60), " +
				"SUBSTR(?,1,20), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,20), " +
				"SUBSTR(?,1,20), " +
				"SUBSTR(?,1,20), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,40), " +
				"SUBSTR(?,1,40), " +
				"SUBSTR(?,1,60), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,100), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,40), " +
				"SUBSTR(?,1,40), " +
				"SUBSTR(?,1,60), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,10), " +
				"?, " +
				"sysdate, " +
				"?, " +
				"sysdate, " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,10), " +
				"SUBSTR(?,1,500), " +
				"?, " + //12
				"SUBSTR(?,1,20), " +
				"? " +
				")";

		// limitamos toda entrada porque no sabemos qué se insertará!
//		log.info("DAO '" + query + "' con '" + precotejoRegistro + "' por ejecutar");
        try{
            jdbcTemplate.update(query,
                    precotejoRegistro.getCoEntidad(),
                    precotejoRegistro.getNuEnvio(),
                    precotejoRegistro.getNuDniMenor(),
                    precotejoRegistro.getApPrimerMenor(),
                    precotejoRegistro.getApSegundoMenor(),
                    precotejoRegistro.getPrenombreMenor(),
                    precotejoRegistro.getFeNacMenor(),
                    precotejoRegistro.getCoGeneroMenor(),
                    precotejoRegistro.getCoUbigeoInei(),
                    precotejoRegistro.getCoCentroPoblado(),
                    precotejoRegistro.getCoEstSalud(),
                    precotejoRegistro.getTiSeguroMenor(),
                    precotejoRegistro.getCoDniJefeFam(),
                    precotejoRegistro.getApPrimerJefe(),
                    precotejoRegistro.getApSegundoJefe(),
                    precotejoRegistro.getPrenomJefe(),
                    precotejoRegistro.getTiVinculoJefe(),
                    precotejoRegistro.getCoDniMadre(),
                    precotejoRegistro.getTiDocIdentidad(),
                    precotejoRegistro.getDeDireccion(),
                    precotejoRegistro.getCoInstEducativa(),
                    precotejoRegistro.getApPrimerMadre(),
                    precotejoRegistro.getApSegundoMadre(),
                    precotejoRegistro.getPrenomMadre(),
                    precotejoRegistro.getTiVinculoMadre(),
                    precotejoRegistro.getCoGraInstMadre(),
                    precotejoRegistro.getCoLenMadre(),
                    precotejoRegistro.getCoNivelPobreza(),
                    precotejoRegistro.getUsCreaRegistro(),
                    precotejoRegistro.getUsModiRegistro(),
                    precotejoRegistro.getCoEtnia(),
                    precotejoRegistro.getNuAfiliacion(),
                    precotejoRegistro.getDeObservacion(),
                    padronProperties.ESREGISTRO_REG_ERROR,
                    precotejoRegistro.getTiProSocial(),
                    precotejoRegistro.getNo());
        }
        catch (Exception e){
            e.printStackTrace();
        }
	}

	@Override
	public Number getNextNuEnvio(String coEntidad) {

		String query = "" +

				"SELECT " +
				"NVL(MAX(NU_ENVIO),0)+1 " +

				"FROM " +
				"PNTV_PRECOTEJO " +

				"WHERE " +
				"CO_ENTIDAD=? ";

//		log.info("DAO '" + query + "' con '" + coEntidad + "' por ejecutar");
		return jdbcTemplate.queryForLong(query, coEntidad);
	}

	@Override
	public List getAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin) {
		// ver http://stackoverflow.com/questions/595072/ora-01830-date-format-picture-ends-before-converting-entire-input-string
		String query = "" +
				"SELECT " +
				"CO_ENTIDAD as coEntidad, " +
				"NU_ENVIO as nuEnvio, " +
				"FE_CREA_AUDI as feCreaAudi, " +
				"US_CREA_AUDI as usCreaAudi, " +
				"NO_ARCHIVO_ORIGINAL as noArchivoOriginal, " +
				"ES_ENVIO as esEnvio, " +
				"FE_PROCESO_INI as feProcesoIni, " +
				"FE_PROCESO_FIN as feProcesoFin, " +
				"(SELECT count(1) FROM pntv_precotejo_registro_obs PO WHERE PO.nu_envio=PV.nu_envio AND PO.co_entidad=PV.co_entidad) AS nuRegistrosObs, " +
				"(SELECT count(1) FROM pntv_precotejo_registro PR WHERE PR.nu_envio=pv.nu_envio AND PR.co_entidad=pv.co_entidad) AS nuRegistros, " +
				"(SELECT count(1) FROM pntv_precotejo_registro PR WHERE PR.nu_envio=pv.nu_envio AND PR.co_entidad=pv.co_entidad AND PR.es_registro IN (" + padronProperties.ESREGISTRO_COT_ERROR + ") ) AS nuRegistrosCotObs " +

				"FROM " +
				"PNTV_PRECOTEJO PV " +

				"WHERE " +
				"(? IS NULL OR CO_ENTIDAD=?) " +
				"AND (? IS NULL OR ES_ENVIO=?) " +
				"AND (? IS NULL OR NU_ENVIO=?) " +
				"AND (? IS NULL OR FE_CREA_AUDI>=?) " +
				"AND (? IS NULL OR FE_CREA_AUDI<=?	) " +
				//"AND (? IS NULL OR FE_CREA_AUDI>=to_date(?,'dd/mm/yyyy HH24:MI:SS')) " +
				//"AND (? IS NULL OR FE_CREA_AUDI<=to_date(?,'dd/mm/yyyy HH24:MI:SS')) " +

				"ORDER BY " +
				"NU_ENVIO DESC ";

		Object[] params = new Object[]{coEntidad, coEntidad, esEnvio, esEnvio, nuEnvio, nuEnvio, feInicio, feInicio, feFin, feFin};
//		log.info("DAO '" + query + "' con '" + StringUtils.join(params, ",") + "' por ejecutar");
		return jdbcTemplate.queryForList(query, params);
	}

	@Override
	public List getAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin, int nuRegistroInicial, int nuRegistroFinal) {

		String query = "" +

				"SELECT " +
				"* " +

				"FROM " +
				"( " +
				"SELECT " +
				"t.*, rownum rn " +

				"FROM " +
				"( " +
				"SELECT " +
				"CO_ENTIDAD as coEntidad, " +
				"NU_ENVIO as nuEnvio, " +
				"FE_CREA_AUDI as feCreaAudi, " +
				"US_CREA_AUDI as usCreaAudi, " +
				"NO_ARCHIVO_ORIGINAL as noArchivoOriginal, " +
				"ES_ENVIO as esEnvio, " +
				"FE_PROCESO_INI as feProcesoIni, " +
				"FE_PROCESO_FIN as feProcesoFin, " +
				"NU_REGISTROS as nuRegistros, " +
				"NU_REGISTROS_OBS as nuRegistrosObs, " +
				"NU_REGISTROS_OK as nuRegistrosOk," +
				"NU_REGISTROS_COR as nuRegistrosCor " +
				//"(SELECT count(1) FROM pntv_precotejo_registro_obs PO WHERE PO.nu_envio=PV.nu_envio AND PO.co_entidad=PV.co_entidad) AS nuRegistrosObs, " +
				//"(SELECT count(1) FROM pntv_precotejo_registro PR WHERE PR.nu_envio=pv.nu_envio AND PR.co_entidad=pv.co_entidad) AS nuRegistros, " +
				//"(SELECT count(1) FROM pntv_precotejo_registro PR WHERE PR.nu_envio=pv.nu_envio AND PR.co_entidad=pv.co_entidad AND PR.es_registro IN (" + padronProperties.ESREGISTRO_COT_ERROR + ") ) AS nuRegistrosCotObs " +

				"FROM " +
				"PNTV_PRECOTEJO PV " +

				"WHERE " +
				"(? IS NULL OR CO_ENTIDAD=?) " +
				"AND (? IS NULL OR ES_ENVIO=?) " +
				"AND (? IS NULL OR NU_ENVIO=?) " +
				"AND (? IS NULL OR FE_CREA_AUDI>=?) " +
				"AND (? IS NULL OR FE_CREA_AUDI<=?) " +

				"ORDER BY " +
				"NU_ENVIO DESC " +
				") t " +
				") r " +

				"WHERE " +
				"r.rn BETWEEN ? AND ? ";

		Object[] params = new Object[]{coEntidad, coEntidad, esEnvio, esEnvio, nuEnvio, nuEnvio, feInicio, feInicio, feFin, feFin, nuRegistroInicial, nuRegistroFinal};
//		log.info("DAO '" + query + "' con '" + StringUtils.join(params, ",") + "' por ejecutar");
		return jdbcTemplate.queryForList(query, params);
	}

	@Override
	public int countAll(String coEntidad, String esEnvio, String nuEnvio, String feInicio, String feFin) {

		String query = "" +

				"SELECT " +
				"count(1) " +

				"FROM " +
				"PNTV_PRECOTEJO PV " +

				"WHERE " +
				"(? IS NULL OR CO_ENTIDAD=?) " +
				"AND (? IS NULL OR ES_ENVIO=?) " +
				"AND (? IS NULL OR NU_ENVIO=?) " +
				"AND (? IS NULL OR FE_CREA_AUDI>=?) " +
				"AND (? IS NULL OR FE_CREA_AUDI<=?) ";

		Object[] params = new Object[]{coEntidad, coEntidad, esEnvio, esEnvio, nuEnvio, nuEnvio, feInicio, feInicio, feFin, feFin};
//		log.info("DAO '" + query + "' con '" + StringUtils.join(params, ",") + "' por ejecutar");
		return jdbcTemplate.queryForInt(query, params);
	}

	@Override
	public List getAllFromPrecotejo(String coEntidad, String nuEnvio) {

		String query = "" +
				"SELECT " +
				"NU_REGISTRO as nuRegistro, " +
				"CO_ENTIDAD as coEntidad, " +
				"NU_ENVIO as nuEnvio, " +
				"NU_DNI_MENOR as nuDniMenor, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"TO_CHAR(FE_NAC_MENOR, 'dd/mm/yyyy') as feNacMenor " +
				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO " +
				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"AND ES_REGISTRO=? " + //11
				"ORDER BY " +
				"NU_REGISTRO ASC ";

		Object[] params = {coEntidad, nuEnvio, padronProperties.ESREGISTRO_REG_OK};
//		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		return jdbcTemplate.queryForList(query, params);
	}

	@Override
	public List getAllFromPrecotejoObs(String coEntidad, String nuEnvio) {

		String query = "" +

				"SELECT " +
				"NU_REGISTRO as nuRegistro, " +
				"DE_OBSERVACION as deObservacion, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"FE_NAC_MENOR as feNacMenor, " +
				"NU_DNI_MENOR as nuDniMenor, " +
				"DE_DIRECCION as deDireccion, " +
				"TI_PRO_SOCIAL as tiProSocial," +
				"CO_DNI_JEFE_FAM as coDniJefeFam, " +
				"AP_PRIMER_JEFE as apPrimerJefe, " +
				"AP_SEGUNDO_JEFE as apSegundoJefe, " +
				"PRENOM_JEFE as prenomJefe, " +
				"CO_DNI_MADRE as coDniMadre, " +
				"AP_PRIMER_MADRE as apPrimerMadre, " +
				"AP_SEGUNDO_MADRE as apSegundoMadre, " +
				"PRENOM_MADRE as prenomMadre," +
				"CO_GENERO_MENOR as coGeneroMenor, " +
				"TI_DOC_IDENTIDAD as tiDocIdentidad, " +
				"CO_UBIGEO_INEI as coUbigeoInei, " +
				"CO_EST_SALUD as coEstSalud, " +
				"TI_SEGURO_MENOR as tiSeguroMenor, " +
				"CO_INST_EDUCATIVA as coInstEducativa, " +
				"TI_VINCULO_JEFE as tiVinculoJefe, " +
				"TI_VINCULO_MADRE as tiVinculoMadre, " +
				"CO_GRA_INST_MADRE as coGraInstMadre, " +
				"CO_LEN_MADRE as coLenMadre " +

				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO_OBS " +

				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"AND ES_REGISTRO=? " +

				"ORDER BY " +
				"NU_REGISTRO ASC "; //12

		Object[] params = {coEntidad, nuEnvio, padronProperties.ESREGISTRO_REG_ERROR};
//		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		return jdbcTemplate.queryForList(query, params);
	}



	@Override
	public List getAllObs(String coEntidad, String nuEnvio) {
		String query = "" +
				"(" +
				"SELECT " +
				"NU_REGISTRO as nuRegistro, " +
				"DE_OBSERVACION as deObservacion, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"FE_NAC_MENOR as feNacMenor, " +
				"NU_DNI_MENOR as nuDniMenor, " +
				"DE_DIRECCION as deDireccion, " +
				"TI_PRO_SOCIAL as tiProSocial," +
				"CO_DNI_JEFE_FAM as coDniJefeFam, " +
				"AP_PRIMER_JEFE as apPrimerJefe, " +
				"AP_SEGUNDO_JEFE as apSegundoJefe, " +
				"PRENOM_JEFE as prenomJefe, " +
				"CO_DNI_MADRE as coDniMadre, " +
				"AP_PRIMER_MADRE as apPrimerMadre, " +
				"AP_SEGUNDO_MADRE as apSegundoMadre, " +
				"PRENOM_MADRE as prenomMadre," +
				"CO_GENERO_MENOR as coGeneroMenor, " +
				"TI_DOC_IDENTIDAD as tiDocIdentidad, " +
				"CO_UBIGEO_INEI as coUbigeoInei, " +
				"CO_EST_SALUD as coEstSalud, " +
				"TI_SEGURO_MENOR as tiSeguroMenor, " +
				"CO_INST_EDUCATIVA as coInstEducativa, " +
				"TI_VINCULO_JEFE as tiVinculoJefe, " +
				"TI_VINCULO_MADRE as tiVinculoMadre, " +
				"CO_GRA_INST_MADRE as coGraInstMadre, " +
				"CO_LEN_MADRE as coLenMadre " +
				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO_OBS " +
				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"AND ES_REGISTRO=? " +
				") " +
				"UNION " +
				"(" +
				"SELECT " +
				"NU_REGISTRO as nuRegistro, " +
				"DE_OBSERVACION as deObservacion, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"TO_CHAR(FE_NAC_MENOR,'dd/mm/yyyy') as feNacMenor, " +
				"NU_DNI_MENOR as nuDniMenor, " +
				"DE_DIRECCION as deDireccion, " +
				"TI_PRO_SOCIAL as tiProSocial," +
				"CO_DNI_JEFE_FAM as coDniJefeFam, " +
				"AP_PRIMER_JEFE as apPrimerJefe, " +
				"AP_SEGUNDO_JEFE as apSegundoJefe, " +
				"PRENOM_JEFE as prenomJefe, " +
				"CO_DNI_MADRE as coDniMadre, " +
				"AP_PRIMER_MADRE as apPrimerMadre, " +
				"AP_SEGUNDO_MADRE as apSegundoMadre, " +
				"PRENOM_MADRE as prenomMadre," +
				"CO_GENERO_MENOR as coGeneroMenor, " +
				"TI_DOC_IDENTIDAD as tiDocIdentidad, " +
				"CO_UBIGEO_INEI as coUbigeoInei, " +
				"CO_EST_SALUD as coEstSalud, " +
				"TI_SEGURO_MENOR as tiSeguroMenor, " +
				"CO_INST_EDUCATIVA as coInstEducativa, " +
				"TI_VINCULO_JEFE as tiVinculoJefe, " +
				"TI_VINCULO_MADRE as tiVinculoMadre, " +
				"CO_GRA_INST_MADRE as coGraInstMadre, " +
				"CO_LEN_MADRE as coLenMadre " +
				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO " +
				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"AND ES_REGISTRO IN ("+padronProperties.ESREGISTRO_COT_ERROR+") " +
//				"AND ES_REGISTRO IN ('40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55')
				") "; //12

		Object[] params = {coEntidad, nuEnvio, padronProperties.ESREGISTRO_REG_ERROR, coEntidad, nuEnvio};
		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		return jdbcTemplate.queryForList(query, params);
	}

	@Override
	public List getAllFromCotejo(String coEntidad, String nuEnvio) {

		String query = "" +

				"SELECT " +
				"NU_REGISTRO as nuRegistro, " +
				"CO_ENTIDAD as coEntidad, " +
				"NU_ENVIO as nuEnvio, " +
				"NVL(NU_DNI_MENOR, PNPK_COTEJO_MASIVO.FU_GET_CO_PADRON_NOMINAL(AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, FE_NAC_MENOR)) as nuDniMenor, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"TO_CHAR(FE_NAC_MENOR, 'dd/mm/yyyy') as feNacMenor " +

				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO " +

				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"AND (ES_REGISTRO IN (" + padronProperties.ESREGISTRO_COT_ACTUALIZADO + "," + padronProperties.ESREGISTRO_COT_INSERTADO + ")) "; //2x 3x

		Object[] params = {coEntidad, nuEnvio};
//		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		return jdbcTemplate.queryForList(query, params);
	}

	@Override
	public List getAllFromCotejoObs(String coEntidad, String nuEnvio) {

		String query = "" +

				"SELECT " +
				//"CO_ENTIDAD as coEntidad, " +
				//"NU_ENVIO as nuEnvio, " +
				"NU_DNI_MENOR as nuDniMenor, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"FE_NAC_MENOR as feNacMenor, " +
				"DE_OBSERVACION as deObservacion " +

				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO_OBS " +

				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"AND ES_REGISTRO IN (" + padronProperties.ESREGISTRO_COT_ERROR + ") "; //4x

		Object[] params = {coEntidad, nuEnvio};
//		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		return jdbcTemplate.queryForList(query, params);
	}

	@Override
	public List getAllFromPrecotejoCotejo(String coEntidad, String nuEnvio, int nuRegistroInicial, int nuRegistroFinal, String esRegistro, String esRegistroObs) {
		String query = "" +
				"SELECT " +
				"* " +
				"FROM " +
				"( " +
				"SELECT " +
				"r.*, rownum rn " +
				"FROM " +
				"( " +
				"SELECT " +
				"CO_ENTIDAD as coEntidad, " +
				"NU_ENVIO as nuEnvio, " +
				"NU_REGISTRO as nuRegistro, " +
				"NU_DNI_MENOR as nuDniMenor, " +
                "PNPK_COTEJO_MASIVO.FU_GET_CO_PADRON_NOMINAL(AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, FE_NAC_MENOR) as coPadronNominal, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"TO_CHAR(FE_NAC_MENOR, 'dd/mm/yyyy') as feNacMenor, " +
				"ES_REGISTRO as esRegistro, " +
				"DE_OBSERVACION as deObservacion " +
				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO " +
				"WHERE " +
				//"ES_REGISTRO IN ("+padronProperties.ESREGISTRO_REG_OK+","+padronProperties.ESREGISTRO_COT_ACTUALIZADO+","+padronProperties.ESREGISTRO_COT_INSERTADO+","+padronProperties.ESREGISTRO_COT_ERROR+") " +
				"ES_REGISTRO IN (" + esRegistro + ") " +
				"AND CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"UNION " +
				"SELECT " +
				"CO_ENTIDAD as coEntidad, " +
				"NU_ENVIO as nuEnvio, " +
				"NU_REGISTRO as nuRegistro, " +
				"NU_DNI_MENOR as nuDniMenor, " +
				//"PNPK_COTEJO_MASIVO.FU_GET_CO_PADRON_NOMINAL(AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, FE_NAC_MENOR) as coPadronNominal, " +
				"null as coPadronNominal, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"FE_NAC_MENOR as feNacMenor, " +
				"ES_REGISTRO as esRegistro, " +
				"DE_OBSERVACION as deObservacion " +
				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO_OBS " +
				"WHERE " +
				//"ES_REGISTRO IN ("+padronProperties.ESREGISTRO_REG_ERROR+") " +
				"ES_REGISTRO IN (" + esRegistroObs + ") " +
				"AND CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				") r " +
				"ORDER BY r.NUREGISTRO " +
				") " +
				"WHERE " +
				"(rn between ? and ?) ";
		Object[] params = {coEntidad, nuEnvio, coEntidad, nuEnvio, nuRegistroInicial, nuRegistroFinal};
		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
        List res = jdbcTemplate.queryForList(query, params);
        log.info(res);
		return toHtml(res);
	}

	/**
	 * @param list
	 * @return
	 */
	private List toHtml(List<Map<String, Object>> list) {
		if (list == null) {
			return list;
		}


		for (Map<String, Object> item : list) {

			Object deObservacionValue;
			if ((deObservacionValue = item.get("deObservacion")) != null) {

				Object esRegistroValue;
				if ((esRegistroValue = item.get("esRegistro")) != null) {

					// sólo traducimos en caso esRegistro == observacion de precotejo
					if (padronProperties.ESREGISTRO_REG_ERROR.contains((String) esRegistroValue)) {
						String deObservacion = (String) deObservacionValue;

						StringBuilder sb = new StringBuilder();
						sb.append("<ul>");
						for (StringTokenizer tokenizer = new StringTokenizer(deObservacion, padronProperties.MAPCOLS_SEPARADOR_LINEA); tokenizer.hasMoreTokens(); ) {
							String linea = tokenizer.nextToken();
							StringTokenizer lineaTokenizer = new StringTokenizer(linea, padronProperties.MAPCOLS_SEPARADOR_DEFINICION);
							int lineaTokens = lineaTokenizer.countTokens();

							sb.append("<li><small>");
							if (lineaTokens > 1) {
								sb.append("<strong>");
								sb.append(padronProperties.getReadableMappingColumn(lineaTokenizer.nextToken()));
								sb.append(". </strong>");
								sb.append(lineaTokenizer.nextToken());
							} else {
								sb.append(padronProperties.getReadableMappingColumn(lineaTokenizer.nextToken()));
							}
							sb.append("</small></li>");
						}
						sb.append("</ul>");

            /*
            String[] lineas = deObservacion.split(padronProperties.MAPCOLS_SEPARADOR_LINEA);
            StringBuilder sb = new StringBuilder();
            sb.append("<ul>");
            for (String linea : lineas) {
              String[] definiciones =  linea.split(padronProperties.MAPCOLS_SEPARADOR_DEFINICION);
              sb.append("<li><small>");
              if(definiciones.length>1) {
                sb.append("<strong>");
                sb.append(padronProperties.getReadableMappingColumn(definiciones[0]));
                sb.append(". </strong>");
                sb.append(definiciones[1]);
              } else if(definiciones.length == 1) {
                sb.append(padronProperties.getReadableMappingColumn(definiciones[0]));
              } else {
                sb.append(linea);
              }
              sb.append("</small></li>");
            }
            sb.append("</ul>");
            */

						item.put("deObservacion", sb.toString());
					}
				}
			}
		}

		return list;
	}

	@Override
	public int countAllFromRegistroObs(String coEntidad, String nuEnvio, String esRegistroObs) {
		String query = "" +
				"SELECT " +
				"count(1) " +
				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO_OBS " +
				"WHERE " +
				//"ES_REGISTRO IN ("+padronProperties.ESREGISTRO_REG_ERROR+") " +
				"ES_REGISTRO IN (" + esRegistroObs + ") " +
				"AND CO_ENTIDAD=? " +
				"AND NU_ENVIO=? ";

		Object[] params = {coEntidad, nuEnvio};
		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		return jdbcTemplate.queryForInt(query, params);
	}

	@Override
	public int countAllFromRegistro(String coEntidad, String nuEnvio, String esRegistro) {
		String query = "" +
				"SELECT " +
				"count(1) " +
				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO " +
				"WHERE " +
				//"ES_REGISTRO IN ("+padronProperties.ESREGISTRO_REG_OK+","+padronProperties.ESREGISTRO_COT_ACTUALIZADO+","+padronProperties.ESREGISTRO_COT_INSERTADO+","+padronProperties.ESREGISTRO_COT_ERROR+") " +
				"ES_REGISTRO IN (" + esRegistro + ") " +
				"AND CO_ENTIDAD=? " +
				"AND NU_ENVIO=? ";

		Object[] params = {coEntidad, nuEnvio};
		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		return jdbcTemplate.queryForInt(query, params);
	}

	@Override
	public Precotejo getPrecotejoDetails(String coEntidad, String nuEnvio) {

		String query = "" +

				"SELECT " +
				"CO_ENTIDAD, " +
				"NU_ENVIO, " +
				"US_CREA_AUDI, " +
				"FE_CREA_AUDI, " +
				"US_MODI_AUDI, " +
				"FE_MODI_AUDI, " +
				"NO_ARCHIVO_ORIGINAL, " +
				"ES_ENVIO, " +
				"ES_ENVIO_HK, " +
				"FE_PROCESO_INI, " +
				"FE_PROCESO_FIN " +

				"FROM  " +
				"PNTV_PRECOTEJO " +

				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND (? IS NULL OR NU_ENVIO=?) ";

		Object[] params = new Object[]{coEntidad, nuEnvio, nuEnvio};
//		log.info("DAO '" + query + "' con '" + StringUtils.join(params, ",") + "' por ejecutar");
		return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Precotejo.class), params);
	}

	@Override
	public List<PrecotejoRegistro> getAllObsDetails(String coEntidad, String nuEnvio, String esRegistro) {

		String query = "" +

				"SELECT " +
				"NU_REGISTRO as nuRegistro, " +
				"DE_OBSERVACION as deObservacion, " +
				"AP_PRIMER_MENOR as apPrimerMenor, " +
				"AP_SEGUNDO_MENOR as apSegundoMenor, " +
				"PRENOMBRE_MENOR as prenombreMenor, " +
				"FE_NAC_MENOR as feNacMenor, " +
				"NU_DNI_MENOR as nuDniMenor, " +
				"DE_DIRECCION as deDireccion, " +
				"TI_PRO_SOCIAL as tiProSocial," +
				"CO_DNI_JEFE_FAM as coDniJefeFam, " +
				"AP_PRIMER_JEFE as apPrimerJefe, " +
				"AP_SEGUNDO_JEFE as apSegundoJefe, " +
				"PRENOM_JEFE as prenomJefe, " +
				"CO_DNI_MADRE as coDniMadre, " +
				"AP_PRIMER_MADRE as apPrimerMadre, " +
				"AP_SEGUNDO_MADRE as apSegundoMadre, " +
				"PRENOM_MADRE as prenomMadre," +
				"CO_GENERO_MENOR as coGeneroMenor, " +
				"TI_DOC_IDENTIDAD as tiDocIdentidad, " +
				"CO_UBIGEO_INEI as coUbigeoInei, " +
				"CO_EST_SALUD as coEstSalud, " +
				"TI_SEGURO_MENOR as tiSeguroMenor, " +
				"CO_INST_EDUCATIVA as coInstEducativa, " +
				"TI_VINCULO_JEFE as tiVinculoJefe, " +
				"TI_VINCULO_MADRE as tiVinculoMadre, " +
				"CO_GRA_INST_MADRE as coGraInstMadre, " +
				"CO_LEN_MADRE as coLenMadre " +

				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO_OBS " +

				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND (? IS NULL OR NU_ENVIO=?) " +
				"AND (? IS NULL OR ES_REGISTRO IN (" + esRegistro + ")) " +

				"ORDER BY " +
				"NU_REGISTRO ASC ";

		Object[] params = {coEntidad, nuEnvio, nuEnvio, esRegistro};
//		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PrecotejoRegistro.class), params);
	}

	// Considerar que la actualización de un precotejo sólo es posible en el ESTADO, NUREGISTROS

	/**
	 * La siguiente consulta actualiza los contadores
	 * UPDATE
	 * pntv_precotejo p
	 * SET
	 * p.nu_registros=
	 * (SELECT (SELECT COUNT(1) FROM PNTV_PRECOTEJO_REGISTRO WHERE NU_ENVIO=p.NU_ENVIO)+(SELECT COUNT(1) FROM PNTV_PRECOTEJO_REGISTRO_OBS WHERE NU_ENVIO=p.NU_ENVIO) FROM DUAL),
	 * p.nu_registros_ok=
	 * (SELECT COUNT(1) FROM PNTV_PRECOTEJO_REGISTRO WHERE CO_ENTIDAD=p.CO_ENTIDAD AND NU_ENVIO=p.NU_ENVIO AND ES_REGISTRO IN (20,21,22,23,30,31))
	 * --WHERE
	 * --p.nu_envio>='90'
	 * ;
	 */
	@Override
	@Transactional
	public void update(Precotejo precotejo) {

		// histórico sólo de cambios de estado, antes de actualizar es_envio de tabla
		String queryHk = "" +

				"INSERT INTO " +
				"PNTH_PRECOTEJO " +
				"( " +
				"CO_ENTIDAD, " +
				"NU_ENVIO, " +
				"ES_ENVIO_INICIAL, " +
				"ES_ENVIO_FINAL, " +
				"US_CREA_AUDI, " +
				"FE_CREA_AUDI, " +
				"US_MODI_AUDI, " +
				"FE_MODI_AUDI " +
				") " +

				"SELECT " +
				"?, " +
				"?, " +
				"ES_ENVIO, " +
				"?, " +
				"?, " +
				"SYSDATE, " +
				"?, " +
				"SYSDATE " +
				"FROM " +
				"PNTV_PRECOTEJO " +
				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? ";

//		log.info("DAO '" + queryHk + "' con '" + precotejo + "' por ejecutar");
		jdbcTemplate.update(queryHk,
				precotejo.getCoEntidad(),
				precotejo.getNuEnvio(),
				precotejo.getEsEnvio(),
				precotejo.getUsCreaAudi(),
				precotejo.getUsModiAudi(),
				precotejo.getCoEntidad(),
				precotejo.getNuEnvio());

		String query = "" +
				"UPDATE " +
				"PNTV_PRECOTEJO " +
				"SET " +
				"ES_ENVIO=?, " +
				"ES_ENVIO_HK=ES_ENVIO_HK||','||?, " +
				"NU_REGISTROS=?, " +
				"NU_REGISTROS_OBS=?, " +
				"US_MODI_AUDI=?, " +
				"FE_MODI_AUDI=SYSDATE " +
				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? ";

		Object[] params = new Object[]{precotejo.getEsEnvio(), precotejo.getEsEnvio(), precotejo.getNuRegistros(), precotejo.getNuRegistrosObs(), precotejo.getUsModiAudi(), precotejo.getCoEntidad(), precotejo.getNuEnvio()};
//		log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
		jdbcTemplate.update(query, params);
	}

	@Override
	public PrecotejoRegistroDetalle getPrecotejoRegistroDetalle(String coEntidad, String nuEnvio, String nuRegistro) {
		String query = "SELECT P.NU_REGISTRO, P.NU_DNI_MENOR, P.TI_DOC_IDENTIDAD, \n" +
				"P.AP_PRIMER_MENOR, P.AP_SEGUNDO_MENOR, P.PRENOMBRE_MENOR, P.CO_GENERO_MENOR, D.DE_GENERO_MENOR AS DE_GENERO_MENOR, TO_CHAR(P.FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, \n" +
				"P.DE_DIRECCION, P.CO_UBIGEO_INEI, U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO AS DE_UBIGEO_INEI, P.CO_ETNIA, \n" +
				"P.CO_EST_SALUD, ES.DE_EST_SALUD||' (RENAES: '||ES.CO_EST_SALUD||', '||ES.DE_DEPARTAMENTO||', '||ES.DE_PROVINCIA||', '||ES.DE_DISTRITO||')' AS DE_EST_SALUD, \n" +
				"/*P.TI_SEGURO_MENOR, D4.de_seguro_menor AS DE_SEGURO_MENOR,*/ P.NU_AFILIACION, \n" +
				"DECODE(P.CO_INST_EDUCATIVA, NULL, '', IE.NO_CENTRO_EDUCATIVO||' (CÓDIGO MODULAR: '||IE.CO_MODULAR||', '||IE.DE_DEPARTAMENTO||', '||IE.DE_PROVINCIA||', '||IE.DE_DISTRITO||')') AS DE_INST_EDUCATIVA, \n" +
				"P.CO_DNI_JEFE_FAM, P.AP_PRIMER_JEFE, P.AP_SEGUNDO_JEFE, P.PRENOM_JEFE, P.TI_VINCULO_JEFE, D1.de_vinculo AS DE_VINCULO_JEFE, \n" +
				"P.CO_DNI_MADRE, P.AP_PRIMER_MADRE, P.AP_SEGUNDO_MADRE, P.PRENOM_MADRE, P.TI_VINCULO_MADRE, D2.de_vinculo AS DE_VINCULO_MADRE, \n" +
				"P.CO_GRA_INST_MADRE, NE.DE_NIVEL_EDUCA AS DE_GRA_INST_MADRE, P.CO_LEN_MADRE, D5.DE_LENGUA AS DE_LEN_MADRE, \n" +
				"P.CO_NIVEL_POBREZA, D3.DE_DOM AS DE_NIVEL_POBREZA, P.US_CREA_REGISTRO, P.FE_CREA_REGISTRO, P.US_MODI_REGISTRO, P.FE_MODI_REGISTRO, P.DE_OBSERVACION, P.NU_ENVIO, P.CO_ENTIDAD, P.DE_OBSERVACION_COTEJO\n" +
				"FROM PNTV_PRECOTEJO_REGISTRO P \n" +
				"LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR \n" +
				"LEFT JOIN PNTR_VINCULO_FAMILIAR D1 ON D1.co_vinculo=p.TI_VINCULO_JEFE \n" +
				"LEFT JOIN PNTR_VINCULO_FAMILIAR D2 ON D2.co_vinculo=p.TI_VINCULO_MADRE \n" +
				"LEFT JOIN PNTR_DOMINIOS D3 ON D3.NO_DOM='CO_NIVEL_POBREZA' AND D3.CO_DOMINIO=CO_NIVEL_POBREZA \n" +
				"/*LEFT JOIN PNTR_SEGURO_SALUD D4 ON D4.TI_SEGURO_MENOR=P.TI_SEGURO_MENOR */\n" +
				"LEFT JOIN PNTR_LENGUA D5 ON D5.CO_LENGUA=p.CO_LEN_MADRE \n" +
				"LEFT JOIN GETR_NIVEL_EDUCATIVO NE ON NE.CO_NIVEL_EDUCA=CO_GRA_INST_MADRE \n" +
				"LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI AND U.ES_UBIGEO='1' \n" +
				"LEFT JOIN PNTM_CENTRO_EDUCATIVO IE ON IE.CO_CENTRO_EDUCATIVO=P.CO_INST_EDUCATIVA \n" +
				"LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES ON ES.CO_EST_SALUD=P.CO_EST_SALUD \n" +
				"WHERE NU_REGISTRO=? AND CO_ENTIDAD=? AND NU_ENVIO=?";

		try {
			return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(PrecotejoRegistroDetalle.class), new Object[]{nuRegistro, coEntidad, nuEnvio,});
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void callCotejo() {
		// todo usar jdbccall
		this.jdbcTemplate.execute("call PNPK_COTEJO_MASIVO.SP_MAIN()");
	}
	
	
	// Agregado jfloresh: 23/02/2016
	@Override
	public void cotejarPrecarga(Number coEntidad, Number nuEnvio) {
		log.debug("Ejecutando PNPK_COTEJO_MASIVO.SP_CARGA_ENTIDAD");
		SimpleJdbcCall callCargaEntidad;
		callCargaEntidad = new SimpleJdbcCall(jdbcTemplate)
				.withCatalogName("PNPK_COTEJO_MASIVO")
				.withProcedureName("SP_CARGA_ENTIDAD")
				.withoutProcedureColumnMetaDataAccess()
				.declareParameters(
						new SqlParameter("PCO_ENTIDAD", Types.NUMERIC),
						new SqlParameter("pNU_ENVIO", Types.NUMERIC)
				);

		// establece parametros de entrada
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("PCO_ENTIDAD", coEntidad);
		params.put("pNU_ENVIO", nuEnvio);
		try {
			// ejecuta procedimiento
			callCargaEntidad.execute(params);
		} catch (Exception e) {
			logger.error("ERROR cotejarPrecarga:", e);
		}
	}


    @Override
    public void setEstadoPrecotejo(String nuEnvio, String coEntidad){
        String query = "" +
                "update pntv_precotejo set es_envio='21' where co_entidad=? and nu_envio=?";
        Object[] params = new Object[]{coEntidad, nuEnvio};
        jdbcTemplate.update(query, params);

    }
}
