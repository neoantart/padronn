package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.PrecotejoRegistroDao;
import pe.gob.reniec.padronn.logic.model.PrecotejoRegistro;

/**
 * Clase PrecotejoRegistroDaoImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 03/08/13 04:55 PM
 */
@Repository
public class PrecotejoRegistroDaoImpl
	extends AbstractBaseDao
	implements PrecotejoRegistroDao {

	@Override
	public PrecotejoRegistro getPrecotejoRegistro(String coEntidad, String nuEnvio, String nuRegistro) {

		String query = "" +

				"SELECT " +
				"CO_ENTIDAD, " +
				"NU_ENVIO, " +
				"NU_DNI_MENOR, " +
				"AP_PRIMER_MENOR, " +
				"AP_SEGUNDO_MENOR, " +
				"PRENOMBRE_MENOR, " +
				"to_char(FE_NAC_MENOR, 'dd/mm/yyyy') as FE_NAC_MENOR, " +
				"CO_GENERO_MENOR, " +
				"CO_UBIGEO_INEI, " +
				"CO_CENTRO_POBLADO, " +
				"CO_EST_SALUD, " +
				"TI_SEGURO_MENOR, " +
				"TI_PRO_SOCIAL, " +
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
				"NU_REGISTRO, " +
				"DE_OBSERVACION, " +
				"DE_PATH, " +
				"DE_OBSERVACION_COTEJO " +
				"FROM  " +
				"PNTV_PRECOTEJO_REGISTRO  " +
				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"AND NU_REGISTRO=? ";

		Object[] params = new Object[]{coEntidad, nuEnvio, nuRegistro};
//		log.info("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
		return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(PrecotejoRegistro.class), params);
	}



	@Override
	public PrecotejoRegistro getPrecotejoRegistroObs(String coEntidad, String nuEnvio, String nuRegistro) {

		String query = "" +

				"SELECT " +
				"CO_ENTIDAD, " +
				"NU_ENVIO, " +
				"NU_DNI_MENOR, " +
				"AP_PRIMER_MENOR, " +
				"AP_SEGUNDO_MENOR, " +
				"PRENOMBRE_MENOR, " +
				//"to_char(to_date(FE_NAC_MENOR, 'dd/mm/yyyy'), 'dd/mm/yyyy') as FE_NAC_MENOR, " +
				"FE_NAC_MENOR, " +
				"CO_GENERO_MENOR, " +
				"CO_UBIGEO_INEI, " +
				"CO_CENTRO_POBLADO, " +
				"CO_EST_SALUD, " +
				"TI_SEGURO_MENOR, " +
				"TI_PRO_SOCIAL, " +
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
				"NU_REGISTRO, " +
				"DE_OBSERVACION " +
				"FROM  " +
				"PNTV_PRECOTEJO_REGISTRO_OBS  " +
				"WHERE " +
				"CO_ENTIDAD=? " +
				"AND NU_ENVIO=? " +
				"AND NU_REGISTRO=? ";

		Object[] params = new Object[]{coEntidad, nuEnvio, nuRegistro};
//		log.info("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
		return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(PrecotejoRegistro.class), params);
	}



	@Override
	public void insertPrecotejoRegistro(PrecotejoRegistro precotejoRegistro) {

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
				"TO_DATE(?, 'dd/mm/yyyy'), " +
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
				"'1', " +
				"?, " +
				"?, " +
				"'', " +
				"?, " +
				"sysdate, " +
				"?, " +
				"sysdate, " +
				"'', " +
				"'', " +
				"?, " +
				"?," +
				"? " +
				")";

		Object[] params = new Object[]{
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
				//
				//
				precotejoRegistro.getCoGraInstMadre(),
				precotejoRegistro.getCoLenMadre(),
				//
				precotejoRegistro.getUsCreaRegistro(),
				//
				precotejoRegistro.getUsModiRegistro(),
				//
				//
				//
//				padronProperties.ESREGISTRO_REG_OK,
                "11",
				precotejoRegistro.getTiProSocial(),
				precotejoRegistro.getNuRegistro()
		};
		log.info("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
		jdbcTemplate.update(query, params);
	}



	@Override
	public void deletePrecotejoRegistroObs(PrecotejoRegistro precotejoRegistro) {
		String query = "" +

				"DELETE " +
				"FROM " +
				"PNTV_PRECOTEJO_REGISTRO_OBS " +
				"WHERE " +
				"CO_ENTIDAD = ? " +
				"AND NU_ENVIO = ? " +
				"AND NU_REGISTRO = ? ";

		Object params = new Object[]{
				""+precotejoRegistro.getCoEntidad(),
				""+precotejoRegistro.getNuEnvio().intValue(),
				""+precotejoRegistro.getNuRegistro()
		};
//		log.info("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
		jdbcTemplate.update(query,
				""+precotejoRegistro.getCoEntidad(),
				""+precotejoRegistro.getNuEnvio().intValue(),
				""+precotejoRegistro.getNuRegistro());
	}



	@Override
	public void updatePrecotejoRegistro(PrecotejoRegistro precotejoRegistro) {

		String query = "" +

				"UPDATE " +
				"PNTV_PRECOTEJO_REGISTRO " +
				"SET " +
				"NU_DNI_MENOR = ?, " +
				"AP_PRIMER_MENOR = ?, " +
				"AP_SEGUNDO_MENOR = ?, " +
				"PRENOMBRE_MENOR = ?, " +
				"FE_NAC_MENOR = ?, " +
				"CO_GENERO_MENOR = ?, " +
				"CO_UBIGEO_INEI = ?, " +
				"CO_CENTRO_POBLADO = ?, " +
				"DE_DIRECCION = ?, " +
				"CO_EST_SALUD = ?, " +
				"TI_SEGURO_MENOR = ?, " +
				"TI_PRO_SOCIAL = ?, " +
				"CO_INST_EDUCATIVA = ?, " +
				"TI_VINCULO_JEFE = ?, " +
				"CO_DNI_JEFE_FAM = ?, " +
				"AP_PRIMER_JEFE = ?, " +
				"AP_SEGUNDO_JEFE = ?, " +
				"PRENOM_JEFE = ?, " +
				"CO_DNI_MADRE = ?, " +
				//"TI_DOC_IDENTIDAD = ?, " +
				"AP_PRIMER_MADRE = ?, " +
				"AP_SEGUNDO_MADRE = ?, " +
				"PRENOM_MADRE = ?, " +
				//"TI_VINCULO_MADRE = ?, " +
				"CO_GRA_INST_MADRE = ?, " +
				"CO_LEN_MADRE = ?, " +
				//"CO_NIVEL_POBREZA = ?, " +
				//"US_CREA_REGISTRO = ?, " +
				//"FE_CREA_REGISTRO = ?, " +
				"US_MODI_REGISTRO = ?, " +
				"FE_MODI_REGISTRO = SYSDATE, " +
				//"CO_ETNIA = ?, " +
				//"NU_AFILIACION = ?, " +
				"ES_REGISTRO = ?, " +
				"DE_OBSERVACION = '', " +
				"DE_PATH = '', " +
				"DE_OBSERVACION_COTEJO = '' " +
				"WHERE  " +
				"CO_ENTIDAD = ? " +
				"AND NU_ENVIO = ? " +
				"AND NU_REGISTRO = ? ";

		Object[] params = new Object[]{
				precotejoRegistro.getNuDniMenor(),
				precotejoRegistro.getApPrimerMenor(),
				precotejoRegistro.getApSegundoMenor(),
				precotejoRegistro.getPrenombreMenor(),
				precotejoRegistro.getFeNacMenor(),
				precotejoRegistro.getCoGeneroMenor(),
				precotejoRegistro.getCoUbigeoInei(),
				precotejoRegistro.getCoCentroPoblado(),
				precotejoRegistro.getDeDireccion(),
				precotejoRegistro.getCoEstSalud(),
				precotejoRegistro.getTiSeguroMenor(),
				precotejoRegistro.getTiProSocial(),
				precotejoRegistro.getCoInstEducativa(),
				precotejoRegistro.getTiVinculoJefe(),
				precotejoRegistro.getCoDniJefeFam(),
				precotejoRegistro.getApPrimerJefe(),
				precotejoRegistro.getApSegundoJefe(),
				precotejoRegistro.getPrenomJefe(),
				precotejoRegistro.getCoDniMadre(),
				precotejoRegistro.getApPrimerMadre(),
				precotejoRegistro.getApSegundoMadre(),
				precotejoRegistro.getPrenomMadre(),
				precotejoRegistro.getCoGraInstMadre(),
				precotejoRegistro.getCoLenMadre(),
				precotejoRegistro.getUsModiRegistro(),
//				padronProperties.ESREGISTRO_REG_OK,
                "11",
				precotejoRegistro.getCoEntidad(),
				precotejoRegistro.getNuEnvio(),
				precotejoRegistro.getNuRegistro()
		};
		log.info("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
		jdbcTemplate.update(query, params);
	}

    @Override
    public void updatePrecotejoRegistroObs(PrecotejoRegistro precotejoRegistro) {
        String query = "" +

                "UPDATE " +
                "PNTV_PRECOTEJO_REGISTRO " +
                "SET " +
                "NU_DNI_MENOR = ?, " +
                "AP_PRIMER_MENOR = ?, " +
                "AP_SEGUNDO_MENOR = ?, " +
                "PRENOMBRE_MENOR = ?, " +
                "FE_NAC_MENOR = ?, " +
                "CO_GENERO_MENOR = ?, " +
                "CO_UBIGEO_INEI = ?, " +
                "CO_CENTRO_POBLADO = ?, " +
                "DE_DIRECCION = ?, " +
                "CO_EST_SALUD = ?, " +
                "TI_SEGURO_MENOR = ?, " +
                "TI_PRO_SOCIAL = ?, " +
                "CO_INST_EDUCATIVA = ?, " +
                "TI_VINCULO_JEFE = ?, " +
                "CO_DNI_JEFE_FAM = ?, " +
                "AP_PRIMER_JEFE = ?, " +
                "AP_SEGUNDO_JEFE = ?, " +
                "PRENOM_JEFE = ?, " +
                "CO_DNI_MADRE = ?, " +
                //"TI_DOC_IDENTIDAD = ?, " +
                "AP_PRIMER_MADRE = ?, " +
                "AP_SEGUNDO_MADRE = ?, " +
                "PRENOM_MADRE = ?, " +
                //"TI_VINCULO_MADRE = ?, " +
                "CO_GRA_INST_MADRE = ?, " +
                "CO_LEN_MADRE = ?, " +
                //"CO_NIVEL_POBREZA = ?, " +
                //"US_CREA_REGISTRO = ?, " +
                //"FE_CREA_REGISTRO = ?, " +
                "US_MODI_REGISTRO = ?, " +
                "FE_MODI_REGISTRO = SYSDATE, " +
                //"CO_ETNIA = ?, " +
                //"NU_AFILIACION = ?, " +
                "ES_REGISTRO = ?, " +
                "DE_OBSERVACION = '', " +
                "DE_PATH = '', " +
                "DE_OBSERVACION_COTEJO = '' " +
                "WHERE  " +
                "CO_ENTIDAD = ? " +
                "AND NU_ENVIO = ? " +
                "AND NU_REGISTRO = ? ";

        Object[] params = new Object[]{
                precotejoRegistro.getNuDniMenor(),
                precotejoRegistro.getApPrimerMenor(),
                precotejoRegistro.getApSegundoMenor(),
                precotejoRegistro.getPrenombreMenor(),
                precotejoRegistro.getFeNacMenor(),
                precotejoRegistro.getCoGeneroMenor(),
                precotejoRegistro.getCoUbigeoInei(),
                precotejoRegistro.getCoCentroPoblado(),
                precotejoRegistro.getDeDireccion(),
                precotejoRegistro.getCoEstSalud(),
                precotejoRegistro.getTiSeguroMenor(),
                precotejoRegistro.getTiProSocial(),
                precotejoRegistro.getCoInstEducativa(),
                precotejoRegistro.getTiVinculoJefe(),
                precotejoRegistro.getCoDniJefeFam(),
                precotejoRegistro.getApPrimerJefe(),
                precotejoRegistro.getApSegundoJefe(),
                precotejoRegistro.getPrenomJefe(),
                precotejoRegistro.getCoDniMadre(),
                precotejoRegistro.getApPrimerMadre(),
                precotejoRegistro.getApSegundoMadre(),
                precotejoRegistro.getPrenomMadre(),
                precotejoRegistro.getCoGraInstMadre(),
                precotejoRegistro.getCoLenMadre(),
                precotejoRegistro.getUsModiRegistro(),
//				padronProperties.ESREGISTRO_REG_OK,
                "11",
                precotejoRegistro.getCoEntidad(),
                precotejoRegistro.getNuEnvio(),
                precotejoRegistro.getNuRegistro()
        };
        log.info("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
        jdbcTemplate.update(query, params);
    }
	@Override
	public void updatePrecotejo(PrecotejoRegistro precotejoRegistro, boolean updateRegistrosObservados) {
		//  modificamos estado en pntv_precotejo para que sea leído nuevamente por cotejo
		String query = "" +

				"UPDATE " +
				"PNTV_PRECOTEJO " +
				"SET " +
				//"US_CREA_AUDI = ?, " +
				//"FE_CREA_AUDI = ?, " +
				"US_MODI_AUDI = ?, " +
				"FE_MODI_AUDI = SYSDATE, " +
				//"NO_ARCHIVO_ORIGINAL = ?, " +
				"ES_ENVIO = ?, " +
				//"ES_ENVIO_HK = ?, " + TODO registrar histórico
				"FE_PROCESO_INI = '', " +
				"FE_PROCESO_FIN = '', " +
				"IN_FLAG = '', " +
				"IN_EMAIL = '', " +
				"NU_REGISTROS_COR = (NVL(NU_REGISTROS_COR, 0)+1), " +
				//"NU_REGISTROS = ?, " +
				//"NU_REGISTROS_OK = ?, " +
				"NU_REGISTROS_OBS = (NVL(NU_REGISTROS_OBS, 0)-?) " +
				"WHERE " +
				"CO_ENTIDAD = ? " +
				"AND NU_ENVIO = ? ";

		Object params = new Object[]{
				""+precotejoRegistro.getUsModiRegistro(),
//				padronProperties.ESENVIO_REG_CORREGIDO,
                "30",// corregido
				(updateRegistrosObservados ? 1 : 0),
				precotejoRegistro.getCoEntidad(),
				""+precotejoRegistro.getNuEnvio().intValue()
		};
//		log.info("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
		jdbcTemplate.update(query,
				precotejoRegistro.getUsModiRegistro(),
//				padronProperties.ESENVIO_REG_CORREGIDO,
                "30",
				(updateRegistrosObservados ? 1 : 0),
				precotejoRegistro.getCoEntidad(),
				precotejoRegistro.getNuEnvio());
	}
}
