package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.nativejdbc.OracleJdbc4NativeJdbcExtractor;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.ActaArchivoDao;
import pe.gob.reniec.padronn.logic.model.ActaArchivo;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 14/08/13
 * Time: 06:48 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class ActaArchivoDaoImpl extends AbstractBaseDao implements ActaArchivoDao {

    OracleLobHandler lobHandler;

    /**
     * Helper
     * @return
     */
    public String getNextValCoActaArchivo() {
        String query = "SELECT NVL(MAX(to_number(co_acta_archivo))+1,1) FROM pntv_acta_archivo";
        try{
            logger.debug("DAO: " + query + " por ejecutar");
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
    public String insert(ActaArchivo actaArchivo) {
        logger.info("coActaAchivo: " + getNextValCoActaArchivo());
        String coActaArchivo = getNextValCoActaArchivo();

        String query = "INSERT " +
                "INTO PNTV_ACTA_ARCHIVO " +
                "  ( " +
                "    CO_ACTA_ARCHIVO, " +
                "    ARCHIVO, " +
                "    NO_ACTA_ARCHIVO, " +
                "    SIZE_ACTA_ARCHIVO, " +
                "    CONTENT_TYPE,   "+
                "    EXT_ARCHIVO,   "+
                "    FE_CREA_AUDI, " +
                "    US_CREA_AUDI, " +
                "    FE_MODI_AUDI, " +
                "    US_MODI_AUDI " +
                "  ) " +
                "  VALUES " +
                "  ( ?, " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    sysdate, " +
                "    ?, " +
                "    sysdate, " +
                "    ? " +
                "  )";

        Object[] params = new Object[]{};
        logger.debug("DAO: " + query + " por ejecutar con:");
        int resultado = this.jdbcTemplate.update(query,
                coActaArchivo,
                actaArchivo.getArchivo(),
                actaArchivo.getNoActaArchivo(),
                actaArchivo.getSizeActaArchivo(),
                actaArchivo.getContentType(),
                actaArchivo.getExtArchivo(),
                actaArchivo.getUsCreaAudi(),
                actaArchivo.getUsModiAudi()
        );
        return coActaArchivo;
    }

    @Override
    public boolean update(ActaArchivo actaArchivo) {
        //ActaArchivo actaArchivoAnt = getActaArchivo(actaArchivo.getCoActaArchivo()); // para el registrar historico
        String query ="" +
                "UPDATE PNTV_ACTA_ARCHIVO " +
                "SET ARCHIVO = ?, " +
                "NO_ACTA_ARCHIVO = ?, " +
                "SIZE_ACTA_ARCHIVO = ?, " +
                "FE_MODI_AUDI = sysdate, " +
                "US_MODI_AUDI = ?, " +
                "CONTENT_TYPE = ?, " +
                "EXT_ARCHIVO = ?" +
                "WHERE CO_ACTA_ARCHIVO = ? ";

        int resultado = this.jdbcTemplate.update(query,
                actaArchivo.getArchivo(),
                actaArchivo.getNoActaArchivo(),
                actaArchivo.getSizeActaArchivo(),
                actaArchivo.getUsModiAudi(),
                actaArchivo.getContentType(),
                actaArchivo.getExtArchivo(),
                actaArchivo.getCoActaArchivo()
        );
        return resultado == 1;
    }

    @PostConstruct
    public void start() {
        lobHandler=new OracleLobHandler();
        // thanks God! http://jkingtech.blogspot.com/2009/12/spring-cast-oracleconnection-from.html
        // corregir problema ClassCastException al usar OracleLobCreator en Pooled Connections
        lobHandler.setNativeJdbcExtractor(new OracleJdbc4NativeJdbcExtractor());
        //lobHandler.setNativeJdbcExtractor(new Jdbc4NativeJdbcExtractor());
//        logger.debug("LobHandler iniciado actas...");
    }

    @Override
    public ActaArchivo getActaArchivo(String coActaArchivo) {
        String query = "" +
                "SELECT CO_ACTA_ARCHIVO, " +
                //"  ARCHIVO, " +
                "  NO_ACTA_ARCHIVO, " +
                "  SIZE_ACTA_ARCHIVO, " +
                "  FE_CREA_AUDI, " +
                "  US_CREA_AUDI, " +
                "  FE_MODI_AUDI, " +
                "  US_MODI_AUDI, " +
                "  EXT_ARCHIVO, " +
                "  CONTENT_TYPE " +
                "  FROM PNTV_ACTA_ARCHIVO " +
                "  WHERE CO_ACTA_ARCHIVO=?";

//        log.info("DAO " + query + " por ejecutar con: " + coActaArchivo );
        try{
            ActaArchivo actaArchivo= jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(ActaArchivo.class), coActaArchivo);
            return actaArchivo;
        }
        catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] getActaArchivoBytes(String coActaArchivo) {
        String query = "" +
                "SELECT " +
                "  ARCHIVO as acta_pdf" +
                "  FROM PNTV_ACTA_ARCHIVO " +
                "  WHERE CO_ACTA_ARCHIVO=?";

//        log.info("DAO " + query + " por ejecutar con: " + coActaArchivo );
        try {
            Map map = (Map) this.jdbcTemplate.queryForObject(
                            query,
                            new RowMapper() {
                                public Object mapRow(ResultSet rs, int i) throws SQLException {
                                    Map results = new HashMap();
                                    byte[] blobBytes = lobHandler.getBlobAsBytes(rs, "acta_pdf");
                                    results.put("acta_pdf", blobBytes);
                                    return results;
                                }
                            },
                            coActaArchivo
                       );
            return (byte[]) map.get("acta_pdf");

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ActaArchivo> getAll() {
        String query = "" +
                " SELECT CO_ACTA_ARCHIVO, " +
                "  ARCHIVO, " +
                "  NO_ACTA_ARCHIVO, " +
                "  SIZE_ACTA_ARCHIVO, " +
                "  FE_CREA_AUDI, " +
                "  US_CREA_AUDI, " +
                "  FE_MODI_AUDI, " +
                "  US_MODI_AUDI, " +
                "  EXT_ARCHIVO, " +
                "  MIME_ARCHIVO " +
                "FROM PNTV_ACTA_ARCHIVO ";

//        log.info("DAO '" + query + "'por ejecutar");
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(ActaArchivo.class));
    }

    @Override
    public List<ActaArchivo> getAll(int filaIni, int filaFin) {
        String query = "" +
                "SELECT * " +
                "FROM " +
                "  (SELECT t.*, " +
                "    rownum rn " +
                "  FROM " +
                " (SELECT CO_ACTA_ARCHIVO, " +
                "  ARCHIVO, " +
                "  NO_ACTA_ARCHIVO, " +
                "  SIZE_ACTA_ARCHIVO, " +
                "  FE_CREA_AUDI, " +
                "  US_CREA_AUDI, " +
                "  FE_MODI_AUDI, " +
                "  US_MODI_AUDI, " +
                "  EXT_ARCHIVO, " +
                "  MIME_ARCHIVO " +
                "FROM PNTV_ACTA_ARCHIVO " +
                ") t" +
                "  ) r " +
                "WHERE r.rn BETWEEN ? AND ?";

//        log.info("DAO '" + query + "'por ejecutar");
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(ActaArchivo.class), filaIni, filaFin);
    }

}
