package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.AniDao;
import pe.gob.reniec.padronn.logic.dao.DominioDao;
import pe.gob.reniec.padronn.logic.dao.PadronDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class AniDaoImpl extends SimpleJdbcDaoBase implements AniDao {

	private static Logger LOG = Logger.getLogger(AniDaoImpl.class);

    private static final int ES_POSTERIOR = 1;
    private static final int ES_ANTERIOR = 0;

	@Autowired
	public PadronProperties padronProperties;

	@Autowired
	DominioDao dominioDao;

    @Autowired
    PadronDao padronDao;

	@Autowired
	Usuario usuario;

	@Override
	public Persona obtenerPorDniEnPersona(String dni, Persona.TipoPersona tipoPersona) {
		String where = "";
		if (tipoPersona.equals(Persona.TipoPersona.MADRE)) {
			where = "and g.de_genero=2  and trunc((months_between(sysdate, g.fe_nacimiento))/12) >= " + padronProperties.EDAD_MIN_MADRE+" ";
		} else if (tipoPersona.equals(Persona.TipoPersona.MAYOR)) {
			where = "and IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(g.ti_ficha_reg)='"+padronProperties.FICHA_MAYOR+"' and trunc((months_between(sysdate, g.fe_nacimiento))/12) >="+padronProperties.EDAD_MIN_MADRE+" "; //para madres mayores de 10 años
		} else if (tipoPersona.equals(Persona.TipoPersona.MENOR)) { //E=menor
			//where = "and g.ti_ficha_reg='E' ";// and trunc((months_between(sysdate, fe_nacimiento))/12)<6
			where = "and IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(g.ti_ficha_reg)='"+padronProperties.FICHA_MENOR+"' and trunc((months_between(sysdate, g.fe_nacimiento))/12) <="+padronProperties.EDAD_MIN_MADRE+" ";
		} else if (tipoPersona.equals(Persona.TipoPersona.PADRE)) {
			where = "and g.de_genero=1 ";
		}

		/*Query para recuperar datos de la persona(niño, adulto, mujer, varon) desde el ANI*/
		String query = "SELECT g.nu_dni as dni, '1' as tipoDocumento, " +
				"g.ap_primer as primerApellido, g.ap_segundo as segundoApellido, g.prenom_inscrito as nombres, " +
				"decode(g.de_genero, 1, 'Masculino', 2, 'Femenino') genero, g.de_genero codigoGenero, " +
				"TO_CHAR(g.fe_nacimiento,'dd/MM/yyyy') as fechaNacimiento, " +
				"trunc((months_between(sysdate, g.fe_nacimiento))/12) edad, " +
				"trunc((months_between(sysdate, g.fe_nacimiento))/12)||' año(s), '||trunc(mod((months_between(sysdate, g.fe_nacimiento)),12))||' mes(es)' edadEscrita, " +
				"g.co_nivel_educa codigoGradoInstruccion, " +
				"g.de_direccion as direccion, " +
				"u.co_ubigeo_inei as codigoUbigeo, " +
				"u.co_ubigeo_reniec as codigoUbigeoReniec, " +
				"g.ap_padre_primer as padrePrimerApellido, g.ap_padre_segundo as padreSegundoApellido, g.prenom_padre as padreNombres, " +
				"DECODE(LENGTH(NVL2(LENGTH(TRIM(TRANSLATE(g.nu_doc_padre, '0123456789',' '))), '', g.nu_doc_padre)), 8, NVL2(LENGTH(TRIM(TRANSLATE(g.nu_doc_padre, '0123456789',' '))), '', g.nu_doc_padre), '' ) as padreDni, " +
				"'PADRE' as padreTipoVinculo, '2' as padreCodigoTipoVinculo, " +
				"g.ap_madre_primer as madrePrimerApellido, g.ap_madre_segundo as madreSegundoApellido, g.prenom_madre as madreNombres, " +
				"DECODE(LENGTH(NVL2(LENGTH(TRIM(TRANSLATE(g.nu_doc_madre, '0123456789',' '))), '', g.nu_doc_madre)), 8, NVL2(LENGTH(TRIM(TRANSLATE(g.nu_doc_madre, '0123456789',' '))), '', g.nu_doc_madre), '' ) as madreDni, " +
				"'MADRE' as madreTipoVinculo, '1' as madreCodigoTipoVinculo," +
				"mg.co_nivel_educa as madreCodigoGradoInstruccion, " +
				"trim(r.co_restri) as coRestri, " +
				"trim(r.de_restri) as deRestri, " +
				"g.nu_telefono, g.de_email " +
				"FROM getm_ani g " +
				"LEFT JOIN pntm_ubigeo_inei u on u.co_ubigeo_reniec=co_departamento_domicilio||co_provincia_domicilio||co_distrito_domicilio and u.es_ubigeo='1' " +
				"LEFT JOIN getm_ani mg on mg.nu_dni=g.nu_doc_madre " +
				"LEFT JOIN getm_restriccion r on r.co_restri=g.co_restri and (r.co_restri<>' ' or r.co_restri<>'  ') " +
				"WHERE g.nu_dni=? " + where;
		try {
				Object[] params = new Object[]{dni};
				logger.info(String.format("DAO '%s'", dni));
				logger.debug("DAO query='" + query + "' params='" + ArrayUtils.toString(params) + "'");
				Persona persona = this.jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);

				persona.setBaseDatosOrigen(Persona.BaseDatosOrigen.ANI);
				persona.setOrigenFoto(Persona.OrigenFoto.ANI);

				//persona.setDireccion(obtenerDireccion(persona.getDni()));
                try {
                    if (!obtenerDireccion(persona.getDni()).isEmpty()) {
                        persona.setDireccion(obtenerDireccion(persona.getDni()));
                    }
                }catch(Exception e){
                   persona.setDireccion(obtenerDireccion(persona.getMadreDni()));
                }

				if (persona.getCodigoUbigeoReniec() != null) {
					persona.setUbigeo(obtenerDescripcionUbigeoReniec(persona.getCodigoUbigeoReniec()));
				} else {
					persona.setUbigeo("");
				}

				Dominio dominio = dominioDao.obtener(persona.getMadreCodigoGradoInstruccion(), "NIVEL_EDUCATIVO");
				if (dominio.getDeDom() != null) {
					persona.setMadreGradoInstruccionReniec(dominio.getDeDom());
				}

				// registro de log
				if(persona!=null && persona.getDni()!=null)
					registrarSessionRUIP(dni, usuario.getLogin(), "PADRON-NOMINAL");

				return persona;
		}
		catch (EmptyResultDataAccessException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				return null;
		} catch (IncorrectResultSizeDataAccessException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				return null;
		} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				return null;
		}
	}

	@Override
	public String registrarSessionRUIP(String nuDni, String nuDniUser, String appName) {
		SimpleJdbcCall callDatosSession;
		callDatosSession = new SimpleJdbcCall(jdbcTemplate)
				.withCatalogName("idoauditor")
				.withProcedureName("sp_datos_sesion_bd")
				.withoutProcedureColumnMetaDataAccess()
				.declareParameters(
						new SqlParameter("p_dni", Types.CHAR),
						new SqlParameter("p_apluser", Types.VARCHAR),
						new SqlParameter("p_program", Types.VARCHAR),
						new SqlOutParameter("p_vo", Types.VARCHAR)
				);
		logger.debug("p_dni:" + nuDni);
		logger.debug("p_apluser:" + nuDniUser);
		logger.debug("p_program:" + appName);

		// establece parametros de entrada
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_dni", nuDni);
		params.put("p_apluser", nuDniUser);
		params.put("p_program", appName);
		try {
			// ejecuta procedimiento
			Map<?, ?> resultado = callDatosSession.execute(params);
			return (String) resultado.get("p_vo");
		} catch (Exception e) {
			logger.error("ERROR registrarSessionRUIP:", e);
			return null;
		}
	}

	@Override
	public String obtenerDescripcionUbigeoReniec(String coUbigeoReniec) {
		String query = "" +
				"select de_departamento || ', '|| de_provincia || ', ' || de_distrito \n" +
				"from pntm_ubigeo_inei\n" +
				"where co_ubigeo_reniec=? and es_ubigeo='1'";
		Object[] params = new Object[]{coUbigeoReniec};
		logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
		try {
			return this.jdbcTemplate.queryForObject(query, String.class, params);
		} catch (EmptyResultDataAccessException e) {
			return "";
		} catch (Exception e) {
			return "";
		}
	}


	/**
	 * Variantes:
	 * - Buscar menores
	 * - Buscar madres
	 *
	 * @param apPrimer
	 * @param apSegundo
	 * @param prenombre
	 * @param filaInicio
	 * @param filaFinal
	 * @return
	 */
	@Override
	public List<Persona> listarPorDatosEnPersona(String apPrimer, String apSegundo, String prenombre, Persona.TipoPersona tipoPersona, int filaInicio, int filaFinal) {
		StringBuffer where = new StringBuffer();
		String and = "";
		boolean datosSuficientes = false;
		List<Object> parameters = new ArrayList<Object>();
		if (apPrimer != null && !apPrimer.isEmpty()) {
			where.append("ap_primer like ?");
			and = " and ";
			datosSuficientes = true;
			parameters.add(apPrimer.toUpperCase() + "%");
		}
		if (apSegundo != null && !apSegundo.isEmpty()) {
			where.append(and + "ap_segundo like ?");
			and = " and ";
			datosSuficientes = true;
			parameters.add(apSegundo.toUpperCase() + "%");
		}
		if (prenombre != null && !prenombre.isEmpty()) {
			where.append(and + "prenom_inscrito like ?");
			and = " and ";
			datosSuficientes = true;
			parameters.add("%" + prenombre.toUpperCase() + "%");
		}
		if (tipoPersona.equals(Persona.TipoPersona.MADRE)) {
			where.append(and + "de_genero=2 and trunc((months_between(sysdate, fe_nacimiento))/12) >="+padronProperties.EDAD_MIN_MADRE+" ");
		} else if (tipoPersona.equals(Persona.TipoPersona.MENOR)) {
			where.append(and + " IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(g.ti_ficha_reg)='"+padronProperties.FICHA_MENOR+"'");
		}

        parameters.add(filaFinal);
		parameters.add(filaInicio);
//		logger.info("where=" + where);
		if (datosSuficientes) {
			String query = "" +
                    "select *  from  " +
                    "(select rownum fila, t.* " +
                    "from  ( " +
					"select " +
					"nu_dni as dni, '1' as tipoDocumento, " +
					"trunc((months_between(sysdate, fe_nacimiento))/12) edad, " +
					"trunc((months_between(sysdate, fe_nacimiento))/12)||' año(s), '||trunc(mod((months_between(sysdate, fe_nacimiento)),12))||' mes(es)' edadEscrita, " +
					"decode(de_genero, 1, 'Masculino', 2, 'Femenino') genero, " +
					"ap_primer as primerApellido, ap_segundo as segundoApellido, prenom_inscrito as nombres, " +
					"nu_doc_padre as padreDni, ap_padre_primer as padrePrimerApellido, ap_padre_segundo as padreSegundoApellido, prenom_padre as padreNombres, 'PADRE' as padreTipoVinculo, " +
					"nu_doc_madre as madreDni, ap_madre_primer as madrePrimerApellido, ap_madre_segundo as madreSegundoApellido, prenom_madre as madreNombres, 'MADRE' as madreTipoVinculo, " +
					"TO_CHAR(fe_nacimiento,'dd/MM/yyyy') as fechaNacimiento, co_departamento_domicilio||', '||co_provincia_domicilio||', '||co_distrito_domicilio as ubigeo, de_direccion as direccion, " +
                    "trim(r.co_restri) as coRestri, " +
                    "trim(r.de_restri) as deRestri " +
                    "from getm_ani g " +
                    "left join getm_restriccion r on r.co_restri=g.co_restri and (r.co_restri<>' ' or r.co_restri<>'  ') " +
					"where " + where.toString() + " " +
					"order by ap_primer, ap_segundo, prenom_inscrito " +
                    ") t " +
                    "where rownum<=?)  " +
                    "where fila>=?";

            logger.debug(String.format("DAO '%s' con '%s'", query, parameters));
			List<Persona> personas = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), parameters.toArray(new Object[parameters.size()]));
			//registrarSessionRUIP(personas, usuario.getLogin(), "PADRON-NOMINAL");
			return personas;
		} else {
			return new ArrayList<Persona>();
		}
	}

	// helper de listarPorDatosEnPersona
	private void registrarSessionRUIP(List<Persona> personas, String usrApp, String appName) {
		try {
			if (personas != null && personas.size() > 0) {
				for (Persona persona : personas) {
					registrarSessionRUIP(persona.getDni(), usrApp, appName);
				}
			}
		} catch (Exception e) {
			LOG.error("Error en registrarSessionRUIP:", e);
		}
	}



    /**
     * Lista de menores por dni de la madre
     * @param dni
     * @param tipoPersona
     * @return
     */
	@Override
	public List<Persona> listarPorDniMadre(String dni, Persona.TipoPersona tipoPersona) {

		String where = "";
		if (tipoPersona.equals(Persona.TipoPersona.MENOR)) {
			where = "and IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(g.ti_ficha_reg)='"+padronProperties.FICHA_MENOR+"'  ";
		}
		String query =	"SELECT nu_dni AS dni, '1' AS tipoDocumento, " +
							"ap_primer as primerApellido, ap_segundo AS segundoApellido, prenom_inscrito as nombres, " +
							"trunc((months_between(sysdate, fe_nacimiento))/12) edad, " +
							"trunc((months_between(sysdate, fe_nacimiento))/12)||' año(s), '||trunc(mod((months_between(sysdate, fe_nacimiento)),12))||' mes(es)' edadEscrita, " +
							"decode(de_genero, 1, 'Masculino', 2, 'Femenino') genero, " +
							"TO_CHAR(fe_nacimiento,'dd/MM/yyyy') AS fechaNacimiento, " +
                        "TRIM(r.co_restri) AS coRestri , " +
                        "TRIM(r.de_restri) AS deRestri " +
						"FROM getm_ani g " +
                        "LEFT JOIN getm_restriccion r ON g.co_restri=r.co_restri AND (r.co_restri<>' ' OR r.co_restri<>'  ') " +
						"WHERE trunc(months_between(sysdate, g.fe_nacimiento)/12)<6 and nu_doc_madre=? " + where + " " +
						"ORDER BY g.fe_nacimiento DESC";

		Object[] params = new Object[]{dni};
		logger.debug(String.format("DAO '%s' con '%s'", query, params));
		List<Persona> personas =  jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
		registrarSessionRUIP(personas, usuario.getLogin(), "PADRON-NOMINAL");
		return personas;
	}

	/**
	 * Lista de menores por dni de la madre
	 * @param padronNominal
	 * @param tipoPersona
	 * @return
	 */
	@Override
	public List<Persona> listarPorDniMadre(PadronNominal padronNominal, Persona.TipoPersona tipoPersona) {

        String feAnterior = padronDao.getRangoFecha(padronNominal.getFeNacMenor(), ES_ANTERIOR); //yyyyMMdd
        String fePosterior = padronDao.getRangoFecha(padronNominal.getFeNacMenor(), ES_POSTERIOR);//yyyyMMd

		String where = "";
		if (tipoPersona.equals(Persona.TipoPersona.MENOR)) {
			where = "and IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(g.ti_ficha_reg)='"+padronProperties.FICHA_MENOR+"'  ";
		}
		String query =	"SELECT nu_dni AS dni, '1' AS tipoDocumento, ap_primer as primerApellido, ap_segundo AS segundoApellido, prenom_inscrito as nombres, \n" +
                "trunc((months_between(sysdate, fe_nacimiento))/12) edad, \n" +
                "trunc((months_between(sysdate, fe_nacimiento))/12)||' año(s), '||trunc(mod((months_between(sysdate, fe_nacimiento)),12))||' mes(es)' edadEscrita, \n" +
                "decode(de_genero, 1, 'Masculino', 2, 'Femenino') genero, TO_CHAR(fe_nacimiento,'dd/MM/yyyy') AS fechaNacimiento, TRIM(r.co_restri) AS coRestri, \n" +
                "TRIM(r.de_restri) AS deRestri FROM getm_ani g LEFT JOIN getm_restriccion r ON g.co_restri=r.co_restri AND (r.co_restri<>' ' OR r.co_restri<>'  ') \n" +
                "WHERE trunc(months_between(sysdate, g.fe_nacimiento)/12)<6 and nu_doc_madre=?\n" +
                "AND fe_nacimiento BETWEEN to_date(?,'YYYYMMDD') AND to_date(?,'YYYYMMDD')\n" +
                "ORDER BY g.fe_nacimiento DESC";

		Object[] params = new Object[]{padronNominal.getCoDniMadre(), feAnterior, fePosterior};
		logger.debug(String.format("DAO '%s' con '%s'", query, params));
		List<Persona> personas =  jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
		registrarSessionRUIP(personas, usuario.getLogin(), "PADRON-NOMINAL");
		return personas;
	}


	@Override
	public int contarRegistrosPorDatos(String apPrimer, String apSegundo, String prenombre, Persona.TipoPersona tipoPersona) {
		StringBuffer where = new StringBuffer();
		String and = "";
		boolean datosSuficientes = false;
		List<Object> parameters = new ArrayList<Object>();
		if (apPrimer != null && !apPrimer.isEmpty()) {
			where.append("ap_primer like ?");
			and = " and ";
			datosSuficientes = true;
            parameters.add(apPrimer.toUpperCase() + "%");
        }
		if (apSegundo != null && !apSegundo.isEmpty()) {
			where.append(and + "ap_segundo like ?");
			and = " and ";
			datosSuficientes = true;
            parameters.add(apSegundo.toUpperCase() + "%");
        }
		if (prenombre != null && !prenombre.isEmpty()) {
			where.append(and + "prenom_inscrito like ?");
			and = " and ";
			datosSuficientes = true;
            parameters.add("%" + prenombre.toUpperCase() + "%");
        }
		if (tipoPersona.equals(Persona.TipoPersona.MADRE)) {
//			where.append(and + "ti_ficha_reg='F' and de_genero=2");
			where.append(and + "de_genero=2 and trunc((months_between(sysdate, fe_nacimiento))/12) >="+padronProperties.EDAD_MIN_MADRE+" ");
		} else if (tipoPersona.equals(Persona.TipoPersona.MENOR)) {
			where.append(and + " IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(ti_ficha_reg)='"+padronProperties.FICHA_MENOR+"'  ");//and trunc((months_between(sysdate, fe_nacimiento))/12)<6

		}
		if (datosSuficientes) {
			String query = "select count(1) " +
					"from getm_ani " +
					"where " + where.toString();
            logger.debug(String.format("DAO '%s' con '%s'", query, parameters));
			return jdbcTemplate.queryForInt(query, parameters.toArray(new Object[parameters.size()]));
		} else {
			return 0;
		}
	}

	@Override
	public DatosCaducidadDni obtenerDatosCaducidadDni(String dni) {
		String query = "SELECT nu_dni dni, to_char(fe_caducidad, 'dd/MM/yyyy') fechaCaducidad, " +
				"nvl(trunc((months_between(fe_caducidad, sysdate))), 0) mesesParaCaducidad, " +
				"nvl(trunc(fe_caducidad-sysdate), 0) diasParaCaducidad " +
				"FROM GETM_ANI where nu_dni=? ";
		try {
			return this.jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(DatosCaducidadDni.class), new Object[]{dni});
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			//e.printStackTrace();
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Persona> listarNombresSimilares(String paterno, String materno, String nombres) {
		try {
			Map<String, Object> sonidos = jdbcTemplate.queryForMap("select pkg_snd.soundesp(?) paterno, pkg_snd.soundesp(?) materno, pkg_snd.soundesp(?) nombres from dual", paterno, materno, nombres);
			String paternoSnd = (String) sonidos.get("paterno");
			String maternoSnd = (String) sonidos.get("materno");
			String nombresSnd = (String) sonidos.get("nombres");
			if (paternoSnd != null && !paternoSnd.isEmpty() && maternoSnd != null && !maternoSnd.isEmpty() && nombres != null && !nombresSnd.isEmpty()) {
				String query = "select g.nu_dni codigoPadronNominal, g.ap_primer primerApellido, g.ap_segundo segundoApellido, g.prenom_inscrito nombres, TO_CHAR(g.FE_NACIMIENTO,'dd/mm/yyyy') fechaNacimiento " +
						"from pntx_getm_ani_sonido s " +
						"join getm_ani g on g.nu_dni=s.nu_dni " +
						"where s.ap_primer=? " +
						"and s.ap_segundo=? " +
						"and s.prenom_inscrito=?";
				return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), paternoSnd, maternoSnd, nombresSnd);
			}
			return new ArrayList<Persona>();
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Persona>();
		} catch (IncorrectResultSizeDataAccessException e) {
			return new ArrayList<Persona>();
		}
	}

    @Override
    public String obtenerDireccion(String dni) {
        String query = "" +
                "SELECT TRIM(REPLACE(REPLACE(REPLACE(DECODE(DE_PREF_DIRECCION, " +
                "                                           NULL, " +
                "                                           NULL, " +
                "                                           (SELECT DE_DOM " +
                "                                              FROM GETR_DOMINIOS " +
                "                                             WHERE NO_DOM = " +
                "                                                   'DE_PREF_DIRECCION' " +
                "                                               AND CO_DOMINIO = " +
                "                                                   DE_PREF_DIRECCION " +
                "                                               AND ES_DOM = '1')) || ' ' || " +
                "                                    DE_DIRECCION || ' ' || NU_DIRECCION || ' ' || " +
                "                                    DECODE(DE_PREF_BLOCK, " +
                "                                           NULL, " +
                "                                           NULL, " +
                "                                           (SELECT DE_DOM " +
                "                                              FROM GETR_DOMINIOS " +
                "                                             WHERE NO_DOM = 'DE_PREF_BLOCK' " +
                "                                               AND CO_DOMINIO = DE_PREF_BLOCK " +
                "                                               AND ES_DOM = '1')) || ' ' || " +
                "                                    DE_BLOCK_CHALET || ' ' || " +
                "                                    DECODE(DE_PREF_INTERIOR, " +
                "                                           NULL, " +
                "                                           NULL, " +
                "                                           (SELECT DE_DOM " +
                "                                              FROM GETR_DOMINIOS " +
                "                                             WHERE NO_DOM = " +
                "                                                   'DE_PREF_INTERIOR' " +
                "                                               AND CO_DOMINIO = " +
                "                                                   DE_PREF_INTERIOR " +
                "                                               AND ES_DOM = '1')) || ' ' || " +
                "                                    DE_INTERIOR || ' ' || " +
                "                                    DECODE(DE_PREF_URB, " +
                "                                           NULL, " +
                "                                           NULL, " +
                "                                           (SELECT DE_DOM " +
                "                                              FROM GETR_DOMINIOS " +
                "                                             WHERE NO_DOM = 'DE_PREF_URB' " +
                "                                               AND CO_DOMINIO = DE_PREF_URB " +
                "                                               AND ES_DOM = '1')) || ' ' || " +
                "                                    DE_URBANIZACION || ' ' || " +
                "                                    DECODE(DE_ETAPA, " +
                "                                           NULL, " +
                "                                           NULL, " +
                "                                           'ETAPA ' || DE_ETAPA) || ' ' || " +
                "                                    DECODE(DE_MANZANA, " +
                "                                           NULL, " +
                "                                           NULL, " +
                "                                           'MZ. ' || DE_MANZANA) || ' ' || " +
                "                                    DECODE(DE_LOTE_DIRECCION, " +
                "                                           NULL, " +
                "                                           NULL, " +
                "                                           'LT. ' || DE_LOTE_DIRECCION), " +
                "                                    '   ', " +
                "                                    ' '), " +
                "                            '  ', " +
                "                            ' '), " +
                "                    '  ', " +
                "                    ' ')) DOMI " +
                "  FROM GETM_ANI A " +
                " WHERE A.NU_DNI = ? ";
        try {
            return this.jdbcTemplate.queryForObject(query, String.class, new Object[]{dni});
        } catch (EmptyResultDataAccessException e) {
            return "";
        } catch (Exception e) {
            return "";
        }
    }

	@Override
	public String obtenerGeneroPorDniPersona(String coDniJefeFam) {
		String query = "select de_genero from getm_ani where nu_dni=?";
		Object[] params = new Object[]{coDniJefeFam};
		logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
		try {
			return this.jdbcTemplate.queryForObject(query, String.class, params);
		} catch (EmptyResultDataAccessException e) {
			return "";
		} catch (Exception e) {
			return "";
		}
	}

}