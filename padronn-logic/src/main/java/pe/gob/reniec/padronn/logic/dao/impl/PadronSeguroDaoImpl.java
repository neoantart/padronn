package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.PadronSeguroDao;
import pe.gob.reniec.padronn.logic.model.PadronPrograma;
import pe.gob.reniec.padronn.logic.model.PadronSeguro;

import java.util.Arrays;
import java.util.List;

/**
 * Clase PadronSeguroDaoImpl.
 *
 * @author aquispej@reniec.gob.pe
 * @date 24/05/13 09:57 AM
 */
@Repository
public class PadronSeguroDaoImpl extends AbstractBaseDao implements PadronSeguroDao {

    @Override
    public void guardarPadronSeguro(PadronSeguro padronSeguro) {
        String query = "INSERT INTO PNTV_PADRON_SEGURO(CO_PADRON_NOMINAL, CO_TIPO_SEGURO, US_CREA_AUDI, FE_CREA_AUDI, US_MODI_AUDI, FE_MODI_AUDI, IN_AFILIADO) \n" +
                       "VALUES ( ?, ?, ?, SYSDATE, ?, SYSDATE, '1') ";

		log.info("DAO '" + query + "' con '" +padronSeguro+ "' por ejecutar");
        jdbcTemplate.update(query,
                            padronSeguro.getCoPadronNominal(),
                            padronSeguro.getCoTipoSeguro(),
                            padronSeguro.getUsuCreaAudi(),
                            padronSeguro.getUsuModiAudi());
    }

    @Override
    public void guardarPadronSeguroHistorico(PadronSeguro padronSeguro) {
        String query = "INSERT INTO PNTH_PADRON_SEGURO(CO_PADRON_NOMINAL, CO_TIPO_SEGURO, NU_SEC, US_CREA_AUDI, FE_CREA_AUDI, US_MODI_AUDI, FE_MODI_AUDI, ES_PADRON_SEGURO) \n" +
                       "VALUES(?, ?, ?, ?, SYSDATE, ?, SYSDATE,'1') ";

		log.info("DAO '" + query + "' con '" +padronSeguro+ "' por ejecutar");
        jdbcTemplate.update(query,
                                padronSeguro.getCoPadronNominal(),
                                padronSeguro.getCoTipoSeguro(),
                                padronSeguro.getNuSec(),
                                padronSeguro.getUsuCreaAudi(),
                                padronSeguro.getUsuModiAudi());
    }

    @Override
    public List<PadronSeguro> listarPadronSeguro(String coPadronNominal) {
        String query = "SELECT CO_PADRON_NOMINAL, CO_TIPO_SEGURO, US_CREA_AUDI, FE_CREA_AUDI, US_MODI_AUDI, FE_MODI_AUDI \n" +
                       "FROM PNTV_PADRON_SEGURO \n" +
                       "WHERE CO_PADRON_NOMINAL=? ";

        Object[]params={coPadronNominal};
		log.info("DAO '" + query + "' con '"+ Arrays.toString(params)+"' por ejecutar");
        return jdbcTemplate.query(query, params, ParameterizedBeanPropertyRowMapper.newInstance(PadronSeguro.class));

    }

    @Override
    public List<PadronSeguro> listarPadronSeguroHistorico(String coPadronNominal, String nuSec) {
        String query = "SELECT CO_PADRON_NOMINAL, CO_TIPO_SEGURO, NU_SEC, US_CREA_AUDI, FE_CREA_AUDI, US_MODI_AUDI, FE_MODI_AUDI \n" +
                       "FROM PNTH_PADRON_SEGURO \n" +
                       "WHERE CO_PADRON_NOMINAL=? AND NU_SEC=?";

        Object[]params={coPadronNominal, nuSec};
		log.info("DAO '" + query + "' con '"+ Arrays.toString(params)+"' por ejecutar");
        return jdbcTemplate.query(query, params, ParameterizedBeanPropertyRowMapper.newInstance(PadronSeguro.class));

    }

    @Override
    public List<String> listarCoPadronSeguro(String coPadronNominal) {
        String query = " SELECT CO_TIPO_SEGURO " +
                       " FROM PNTV_PADRON_SEGURO " +
                       " WHERE IN_AFILIADO='1' " +
                       " AND CO_PADRON_NOMINAL=? ";

        Object[] params={coPadronNominal};
		log.info("DAO '" + query + "' con '"+ Arrays.toString(params)+"' por ejecutar");
        return jdbcTemplate.queryForList(query, params, String.class);

    }

    @Override
    public void clearPadronSeguro(String coPadronNominal) {
        String query = "DELETE FROM PNTV_PADRON_SEGURO \n" +
                       "WHERE CO_PADRON_NOMINAL=? ";

        jdbcTemplate.update(query,	coPadronNominal);
    }
}
