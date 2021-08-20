package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.PadronProgramaDao;
import pe.gob.reniec.padronn.logic.model.PadronPrograma;

import java.util.Arrays;
import java.util.List;

/**
 * Clase PadronProgramaDaoImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 24/05/13 09:57 AM
 */
@Repository
public class PadronProgramaDaoImpl
		extends AbstractBaseDao
		implements PadronProgramaDao {

	@Override
	public void clearPadronPrograma(String coPadronNominal) {
		String query =

				"DELETE FROM " +
						"PNTV_PADRON_PROGRAMA " +
				"WHERE " +
						"CO_PADRON_NOMINAL=? ";

//		log.info("DAO '" + query + "' con '" +coPadronNominal+ "' por ejecutar");
		jdbcTemplate.update(query,	coPadronNominal);
	}

	@Override
	public void guardarPadronPrograma(PadronPrograma padronPrograma) {
		String query =

				"INSERT INTO " +
						"PNTV_PADRON_PROGRAMA " +
						"( " +
						"CO_PADRON_NOMINAL, " +
						"CO_PROGRAMA_SOCIAL, " +
						"USU_CREA_AUDI, " +
						"FE_CREA_AUDI, " +
						"USU_MODI_AUDI, " +
						"FE_MODI_AUDI, " +
						"IN_AFILIADO " +
						") " +

				"VALUES " +
						"( " +
						"?, " +
						"?, " +
						"?, " +
						"SYSDATE, " +
						"?, " +
						"SYSDATE, " +
						"'1' " +
						") ";

//		log.info("DAO '" + query + "' con '" +padronPrograma+ "' por ejecutar");
		jdbcTemplate.update(query,
				padronPrograma.getCoPadronNominal(),
				padronPrograma.getCoProgramaSocial(),
				padronPrograma.getUsuCreaAudi(),
				padronPrograma.getUsuModiAudi());
	}

	@Override
	public void guardarPadronProgramaHistorico(PadronPrograma padronPrograma) {
		String query =

				"INSERT INTO " +
						"PNTH_PADRON_PROGRAMA " +
						"( " +
						"CO_PADRON_NOMINAL, " +
						"CO_PROGRAMA_SOCIAL, " +
						"NU_SEC, " +
						"USU_CREA_AUDI, " +
						"FE_CREA_AUDI, " +
						"USU_MODI_AUDI, " +
						"FE_MODI_AUDI, " +
						"ES_PADRON_PROGRAMA " +
						") " +

				"VALUES " +
						"( " +
						"?, " +
						"?, " +
						"?, " +
						"?, " +
						"SYSDATE, " +
						"?, " +
						"SYSDATE, " +
						"'1' " +
						") ";

//		log.info("DAO '" + query + "' con '" +padronPrograma+ "' por ejecutar");
		jdbcTemplate.update(query,
				padronPrograma.getCoPadronNominal(),
				padronPrograma.getCoProgramaSocial(),
				padronPrograma.getNuSec(),
				padronPrograma.getUsuCreaAudi(),
				padronPrograma.getUsuModiAudi());
	}



	@Override
	public List<PadronPrograma> listarPadronPrograma(String coPadronNominal) {
		String query =

				"SELECT " +
						"CO_PADRON_NOMINAL, " +
						"CO_PROGRAMA_SOCIAL, " +
						"USU_CREA_AUDI, " +
						"FE_CREA_AUDI, " +
						"USU_MODI_AUDI, " +
						"FE_MODI_AUDI " +
				"FROM " +
						"PNTV_PADRON_PROGRAMA " +
				"WHERE " +
						"CO_PADRON_NOMINAL=? ";

		Object[]params={coPadronNominal};
//		log.info("DAO '" + query + "' con '"+ Arrays.toString(params)+"' por ejecutar");
		return jdbcTemplate.query(query, params, ParameterizedBeanPropertyRowMapper.newInstance(PadronPrograma.class));
	}

	@Override
	public List<String> listarCoPadronPrograma(String coPadronNominal) {
		String query =

				"SELECT CO_PROGRAMA_SOCIAL \n" +
				" FROM PNTV_PADRON_PROGRAMA \n" +
				" WHERE IN_AFILIADO='1' \n" +
				" AND CO_PADRON_NOMINAL =? ";

		Object[] params={coPadronNominal};
//		log.info("DAO '" + query + "' con '"+ Arrays.toString(params)+"' por ejecutar");
		return jdbcTemplate.queryForList(query, params, String.class);
	}

	@Override
	public List<PadronPrograma> listarPadronProgramaHistorico(String coPadronNominal, String nuSec) {
		String query =

				"SELECT " +
						"CO_PADRON_NOMINAL, " +
						"CO_PROGRAMA_SOCIAL, " +
						"NU_SEC, " +
						"USU_CREA_AUDI, " +
						"FE_CREA_AUDI, " +
						"USU_MODI_AUDI, " +
						"FE_MODI_AUDI " +
				"FROM " +
						"PNTH_PADRON_PROGRAMA " +
				"WHERE " +
						"CO_PADRON_NOMINAL=? " +
						"AND NU_SEC=?";

		Object[]params={coPadronNominal, nuSec};
//		log.info("DAO '" + query + "' con '"+ Arrays.toString(params)+"' por ejecutar");
		return jdbcTemplate.query(query, params, ParameterizedBeanPropertyRowMapper.newInstance(PadronPrograma.class));

	}
}
