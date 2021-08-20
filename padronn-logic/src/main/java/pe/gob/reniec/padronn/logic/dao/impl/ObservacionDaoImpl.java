package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.ObservacionDao;
import pe.gob.reniec.padronn.logic.model.Observacion;

/**
 * Clase ObservacionDaoImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 0.0.1
 * @since 15/07/13 10:21 AM
 */
@Repository
public class ObservacionDaoImpl
    extends AbstractBaseDao
    implements ObservacionDao {

  @Override
  public void addObservacion(Observacion observacion) {
    String query = "" +
        "INSERT " +
        "INTO PNTL_OBSERVACION " +
        "( " +
        "CO_OBSERVACION, " +
        "CO_OBSERVACION_TIPO, " +
        "CO_ENTIDAD, " +
        "CO_USUARIO, " +
        "CO_PADRON_NOMINAL, " +
        "DE_DETALLE_ADICIONAL, " +
        "US_CREA_AUDI, " +
        "FE_CREA_AUDI, " +
        "US_MODI_AUDI, " +
        "FE_MODI_AUDI " +
        ") " +
        "SELECT " +
        "?, " +
        "?, " +
        "?, " +
        "?, " +
        "?, " +
        "?, " +
        "?, " +
        "SYSDATE, " +
        "?, " +
        "SYSDATE " +
        "FROM DUAL ";

    Object[] params = {
        getNextCoObservacion(),
        observacion.getCoObservacionTipo(),
        observacion.getCoEntidad(),
        observacion.getCoUsuario(),
        observacion.getCoPadronNominal(),
        observacion.getDeDetalleAdicional(),
        observacion.getUsCreaAudi(),
        observacion.getUsModiAudi(),
    };
    log.info("DAO query='" + query + "' params='" + ArrayUtils.toString(params)+ "'");
      try {
          jdbcTemplate.update(query, params);
      } catch (RuntimeException e) {
          e.printStackTrace();
          log.error(e.getMessage());
      }
  }

    @Override
    public String getNextCoObservacion(){
        String query = "SELECT NVL(MAX(CO_OBSERVACION)+1, 1) FROM PNTL_OBSERVACION";
        try{
//            logger.debug("DAO: " + query + " por ejecutar");
            return Integer.toString(this.jdbcTemplate.queryForInt(query));
        }
        catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
        catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getNextCoObservacionTipo() {
        String query = "SELECT NVL(MAX(co_observacion_tipo)+1, 1) FROM pntr_observacion_tipo";
        try{
//            logger.debug("DAO: " + query + " por ejecutar");
            return Integer.toString(this.jdbcTemplate.queryForInt(query));
        }
        catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
        catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

  /* TODO
  @Override
  public Observacion getObservacion(String coObservacion) {
    ;
  }
  */
}
