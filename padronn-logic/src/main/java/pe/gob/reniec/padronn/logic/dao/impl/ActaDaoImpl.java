package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.ActaDao;
import pe.gob.reniec.padronn.logic.model.Acta;
import pe.gob.reniec.padronn.logic.model.ActaArchivo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JFLORESH
 * Date: 14/08/13
 * Time: 06:44 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class ActaDaoImpl extends AbstractBaseDao implements ActaDao {

    @Override
    public String insert(Acta acta) {
        String coActa = getNextValCoActa();
        String query = "" +
                "INSERT " +
                "INTO PNTV_ACTA " +
                "  ( " +
                "    CO_ACTA, " +
                "    DE_ACTA, " +
                "    CO_ENTIDAD, " +
                "    FE_INI, " +
                "    FE_FIN, " +
                "    FE_CREA_AUDI, " +
                "    US_CREA_AUDI, " +
                "    CO_EST_SALUD, " +
                "    ES_ACTA" +
                "  ) " +
                "  VALUES " +
                "  ( " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    ?, " +
                "    sysdate, " +
                "    ?, " +
                "    ?," +
                "    '1' " +
                "  )";

        Object[] params = new Object[]{
                coActa,
                acta.getDeActa(),
                acta.getCoEntidad(),
                acta.getFeIni(),
                acta.getFeFin(),
                acta.getUsCreaAudi(),
                acta.getCoEstSalud()
        };
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            this.jdbcTemplate.update(query, params);
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
        return coActa;
    }

    @Override
    public boolean update(Acta acta) {
        String query = "" +
                "UPDATE PNTV_ACTA " +
                "SET " +
                "CO_ENTIDAD=?, " +
                "FE_INI=?, " +
                "DE_FIN=?, " +
                "FE_MODI_AUDI=sysdate, " +
                "US_MODI_AUDI=?" +
                "WHERE CO_ACTA    = ? ";

//        log.debug("DAO: " + query + " por ejecutar");
        int resultado = this.jdbcTemplate.update(query,
                acta.getCoActa(),
                acta.getFeIni(),
                acta.getFeFin(),
                acta.getUsModiAudi(),
                acta.getCoActa()
        );
        return resultado == 1;
    }

    @Override
    public Acta getActa(String coActa) {
        String query = "" +
                "SELECT a.CO_ACTA, " +
                "       a.DE_ACTA, " +
                "       a.CO_ENTIDAD, " +
                "       e.DE_ENTIDAD, " +
                "       to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                "       to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                "       to_char(a.FE_CREA_AUDI, 'dd/mm/yyyy') fe_crea_audi, " +
                "       a.US_CREA_AUDI, " +
                "       a.FE_MODI_AUDI, " +
                "       a.US_MODI_AUDI, " +
                "       u.AP_PRIMER, " +
                "       u.AP_SEGUNDO, " +
                "       u.prenombres, " +
                "       a.co_est_salud, " +
                "       es.de_est_salud||', '||es.de_direccion as de_est_salud " +
                "  FROM PNTV_ACTA a " +
                "  LEFT JOIN PNTM_ENTIDAD e ON a.co_entidad = e.co_entidad and e.es_entidad='1' " +
                "  LEFT JOIN PNTM_USUARIO_EXTERNO u ON u.co_usuario = a.us_crea_audi " +
                "  left join pnvm_establecimiento_salud es on es.co_est_salud = " +
                "                                             a.co_est_salud " +
                " WHERE a.CO_ACTA = ? /*and (a.es_acta<>'0' or a.es_acta is null )*/";

        logger.debug(String.format("DAO '%s' con '%s'", query, coActa));
        try{
            Acta acta = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Acta.class), coActa);
            //acta.setDeArchivos(getDeArchivos(coActa));
            acta.setActaArchivos(getActaArchivos(coActa));
            return acta;
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
    public List<Acta> getAll() {
        String query = "" +
                "SELECT a.CO_ACTA,  " +
                "  a.CO_ENTIDAD,  " +
                "  e.DE_ENTIDAD, " +
                "  to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                "  to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                "  to_char(a.FE_CREA_AUDI, 'dd/mm/yyyy') fe_crea_audi,  " +
                "  a.US_CREA_AUDI,  " +
                "  a.FE_MODI_AUDI,  " +
                "  a.US_MODI_AUDI,  " +
                "  a.es_acta, " +
                "  a.co_est_salud, " +
                "  es.de_est_salud" +
                "FROM PNTV_ACTA a left join PNTM_ENTIDAD e on a.co_entidad=e.co_entidad and e.es_entidad='1' " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=a.co_est_salud";

//        log.info("DAO '" + query + "'por ejecutar");
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Acta.class));
    }

    @Override
    public List<Acta> getAll(int filaIni, int filaFin) {
        String query = "" +
                "SELECT * " +
                "  FROM (SELECT t.*, rownum rn " +
                "          FROM (SELECT a.CO_ACTA, " +
                "                       a.CO_ENTIDAD, " +
                "                       e.DE_ENTIDAD, " +
                "                       to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                "                       to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                "                       to_char(a.FE_CREA_AUDI, 'dd/mm/yyyy') fe_crea_audi, " +
                "                       a.US_CREA_AUDI, " +
                "                       a.FE_MODI_AUDI, " +
                "                       a.US_MODI_AUDI, " +
                "                       a.es_acta, " +
                "                       a.co_est_salud, " +
                "                       es.de_est_salud " +
                "                  FROM PNTV_ACTA a " +
                "                  LEFT JOIN PNTM_ENTIDAD e on a.co_entidad = e.co_entidad and e.es_entidad='1' " +
                "                  left join pnvm_establecimiento_salud es on es.co_est_salud = " +
                "                                                             a.co_est_salud " +
                "                 where a.es_acta <> '0' " +
                "                    or a.es_acta is null " +
                "                 ORDER BY a.FE_CREA_AUDI DESC) t) r " +
                " WHERE r.rn BETWEEN ? AND ? ";

        Object[] params = new Object[]{filaIni, filaFin};
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        List<Acta> actaListResult = new ArrayList<Acta>();

        List<Acta> actaList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Acta.class), params);

        List<ActaArchivo> actaAchivos = new ArrayList<ActaArchivo>();

        for(Acta acta: actaList) {
            //acta.setDeArchivos(getDeArchivos(acta.getCoActa()));
            acta.setActaArchivos(getActaArchivos(acta.getCoActa()));
            actaListResult.add(acta);
        }
        return actaListResult;
    }

    @Override
    public List<Acta> getAll(String coEntidad, String feIni, String feFin, int filaIni, int filaFin) {
        String query = "" +
                "SELECT * " +
                "  FROM (SELECT t.*, rownum rn " +
                "          FROM (SELECT a.CO_ACTA, " +
                "                       a.CO_ENTIDAD, " +
                "                       e.DE_ENTIDAD, " +
                "                       to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                "                       to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                "                       to_char(a.FE_CREA_AUDI, 'dd/mm/yyyy') fe_crea_audi, " +
                "                       a.US_CREA_AUDI, " +
                "                       a.FE_MODI_AUDI, " +
                "                       a.US_MODI_AUDI, " +
                "                       u.AP_PRIMER, " +
                "                       u.AP_SEGUNDO, " +
                "                       u.PRENOMBRES, " +
                "                       a.es_acta, " +
                "                       a.co_est_salud, " +
                "                       es.de_est_salud " +
                "                  FROM PNTV_ACTA a " +
                "                  LEFT JOIN PNTM_ENTIDAD e ON a.co_entidad = e.co_entidad and e.es_entidad='1' " +
                "                  LEFT JOIN PNTM_USUARIO_EXTERNO u ON u.co_usuario = " +
                "                                                      a.US_CREA_AUDI " +
                "                  left join pnvm_establecimiento_salud es on es.co_est_salud = " +
                "                                                             a.co_est_salud " +
                "                 WHERE a.CO_ENTIDAD = ? " +
                "                   and (a.es_acta <> '0' or a.es_acta is null) " +
                "                   AND trunc(a.fe_crea_audi) between ? and ? " +
                "                 ORDER BY a.FE_CREA_AUDI DESC) t) r " +
                " WHERE r.rn BETWEEN ? AND ? ";

        Object[] params = new Object[]{coEntidad, feIni, feFin, filaIni, filaFin};
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Acta.class), params);
    }

    //para descargar excel, con mas detalle
    @Override
    public List<Acta> getAll(String coEntidad, String feIni, String feFin) {
        String query = "" +
                "select a.co_acta coActa, " +
                "       a.co_entidad coEntidad, " +
                "       e.de_entidad deEntidad, " +
                "       ub.de_departamento deDepartamento, " +
                "       ub.de_provincia deProvincia, " +
                "       ub.de_distrito deDistrito, " +
                "       to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                "       to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                "       to_char(a.fe_crea_audi, 'dd/mm/yyyy') feCreaaudi, " +
                "       a.us_crea_audi usCreaaudi, " +
                "       u.ap_primer apPrimer, " +
                "       u.ap_segundo apSegundo, " +
                "       u.prenombres prenombres, " +
                "       (select count(1) from pntv_acta_archivos where co_acta = a.co_acta) as nuArchivos, " +
                "       a.es_acta, " +
                "       a.co_est_salud, " +
                "       es.de_est_salud " +
                "  from pntv_acta a " +
                "  left join pntm_entidad e on a.co_entidad = e.co_entidad and e.es_entidad='1' " +
                "  left join pntm_usuario_externo u on u.co_usuario = a.us_crea_audi " +
                "  left join pntm_ubigeo_inei ub on ub.co_ubigeo_inei = e.co_ubigeo_inei and ub.es_ubigeo='1' " +
                "  left join pnvm_establecimiento_salud es on es.co_est_salud = " +
                "                                             a.co_est_salud " +
                " where e.co_entidad = ? " +
                "   and (a.es_acta <> '0' or a.es_acta is null) " +
                "   and trunc(a.fe_crea_audi) between ? and ? " +
                " order by e.co_ubigeo_inei ";

        Object[] params = new Object[]{coEntidad, feIni, feFin};
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Acta.class), params);
    }


    public String getNextValCoActa(){
        String query = "SELECT NVL(MAX(co_acta)+1, 1) FROM pntv_acta";
        //String query = "SELECT PNSQ_ACTA.NEXTVAL FROM DUAL";
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
    public List<String> getDeArchivos(String coActa) {
        String query = "" +
                "SELECT ar.no_acta_archivo " +
                "FROM PNTV_ACTA_ARCHIVO ar " +
                "LEFT JOIN PNTV_ACTA_ARCHIVOS ars " +
                "ON ar.co_acta_archivo=ars.co_acta_archivo " +
                "WHERE ars.co_acta    = ?";
        try{
            logger.debug("DAO: " + query + " por ejecutar con:" + coActa);
            Object[] params = {coActa};
            return this.jdbcTemplate.queryForList(query, params, String.class);
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
    public List<ActaArchivo> getActaArchivos(String coActa){
        String query = "" +
                "SELECT " +
                "ar.co_acta_archivo , " +
                "ar.no_acta_archivo, " +
                "ar.SIZE_ACTA_ARCHIVO, " +
                "ar.FE_CREA_AUDI, " +
                "ar.US_CREA_AUDI, " +
                "ar.CONTENT_TYPE, " +
                "ar.EXT_ARCHIVO  " +
                "FROM PNTV_ACTA_ARCHIVO ar " +
                "LEFT JOIN PNTV_ACTA_ARCHIVOS ars " +
                "ON ar.co_acta_archivo=ars.co_acta_archivo " +
                "WHERE ars.co_acta    = ?";
        try{
            logger.debug("DAO: " + query + " por ejecutar con:" + coActa);
            Object[] params = {coActa};
            List<ActaArchivo> actaArchivoList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(ActaArchivo.class), params);
            List<ActaArchivo> result = new ArrayList<ActaArchivo>();
            for(ActaArchivo actaArchivo: actaArchivoList) {
                actaArchivo.setSizeActaArchivoFormat(fomatFileSize(actaArchivo.getSizeActaArchivo()));
                result.add(actaArchivo);
            }
            return result;
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

    public String fomatFileSize(String sizeActaArchivo){
        long size = Long.parseLong(sizeActaArchivo);
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public int getNumActas(){
        String query = "SELECT COUNT(1) FROM PNTV_ACTA ";
        return jdbcTemplate.queryForInt(query);
    }

    @Override
    public int getNumActas(String coEntidad, String feIni, String feFin){
        String query = "" +
                "SELECT COUNT(1) " +
                "  FROM PNTV_ACTA " +
                " WHERE CO_ENTIDAD = ? " +
                "   AND TRUNC(FE_CREA_AUDI) BETWEEN ? AND ? " +
                "   and (es_acta <> '0' or es_acta is null) ";
        Object[] params = new Object[]{coEntidad, feIni, feFin};
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params);
    }

    @Override
    public List<Acta> getActasByEntidad(String coEntidad, int filaIni, int filaFin) {
        String query = "" +
                "SELECT * " +
                "  FROM (SELECT t.*, rownum rn " +
                "          FROM (SELECT a.CO_ACTA, " +
                "                       a.CO_ENTIDAD, " +
                "                       e.DE_ENTIDAD, " +
                "                       to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                "                       to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                "                       to_char(a.FE_CREA_AUDI, 'dd/mm/yyyy') fe_crea_audi, " +
                "                       a.US_CREA_AUDI, " +
                "                       a.FE_MODI_AUDI, " +
                "                       a.US_MODI_AUDI, " +
                "                       a.es_acta, " +
                "                       a.co_est_salud, " +
                "                       es.de_est_salud " +
                "                  FROM PNTV_ACTA a " +
                "                  LEFT JOIN PNTM_ENTIDAD e ON a.co_entidad = e.co_entidad and e.es_entidad='1' " +
                "                  left join pnvm_establecimiento_salud es on es.co_est_salud = " +
                "                                                             a.co_est_salud " +
                "                 WHERE a.CO_ENTIDAD = ? " +
                "                   and (a.es_acta <> '0' or a.es_acta is null) " +
                "                 ORDER BY a.FE_CREA_AUDI DESC) t) r " +
                " WHERE r.rn BETWEEN ? AND ?";


        log.info("DAO '" + query + "por ejecutar " + "con: " + coEntidad + " " + filaIni +  " " + filaFin);
        List<Acta> actaListResult = new ArrayList<Acta>();

        List<Acta> actaList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Acta.class), coEntidad, filaIni, filaFin);

        List<ActaArchivo> actaAchivos = new ArrayList<ActaArchivo>();

        for(Acta acta: actaList) {
            acta.setActaArchivos(getActaArchivos(acta.getCoActa()));
            actaListResult.add(acta);
        }
        return actaListResult;
    }


    @Override
    public List<Acta> getAll(String coEntidad, int filaIni, int filaFin) {
        String query = "" +
                        "select * " +
                        "  from (select rownum as fila, t.* " +
                        "          from (select a.co_acta coActa, " +
                        "                       a.co_entidad coEntidad, " +
                        "                       e.de_entidad deEntidad, " +
                        "                       ub.de_departamento deDepartamento, " +
                        "                       ub.de_provincia deProvincia, " +
                        "                       ub.de_distrito deDistrito, " +
                        "                       to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                        "                       to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                        "                       to_char(a.fe_crea_audi, 'dd/mm/yyyy') feCreaaudi, " +
                        "                       a.us_crea_audi usCreaaudi, " +
                        "                       u.ap_primer apPrimer, " +
                        "                       u.ap_segundo apSegundo, " +
                        "                       u.prenombres prenombres, " +
                        "                       (select count(1) " +
                        "                          from pntv_acta_archivos " +
                        "                         where co_acta = a.co_acta) as nuArchivos, " +
                        "                       a.es_acta, " +
                        "                       a.co_est_salud, " +
                        "                       es.de_est_salud " +
                        "                  from pntv_acta a " +
                        "                  left join pntm_entidad e on a.co_entidad = e.co_entidad and e.es_entidad='1' " +
                        "                  left join pntm_usuario_externo u on u.co_usuario = " +
                        "                                                      a.us_crea_audi " +
                        "                  left join pntm_ubigeo_inei ub on ub.co_ubigeo_inei = " +
                        "                                              e.co_ubigeo_inei and ub.es_ubigeo='1'" +
                        "                  left join pnvm_establecimiento_salud es on es.co_est_salud = " +
                        "                                                             a.co_est_salud " +
                        "                 where e.co_entidad = ? " +
                        "                   /*and (a.es_acta <> '0' or a.es_acta is null)*/ " +
                        "                 order by a.fe_crea_audi desc) t) " +
                        " where fila between ? and ? ";

        Object[] params = new Object[]{coEntidad, filaIni, filaFin};
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Acta.class), params);
    }

    @Override
    public int getNumActas(String coEntidad) {
        String query = "select count(1) from pntv_acta where co_entidad = ?";
        return jdbcTemplate.queryForInt(query, coEntidad);
    }

    @Override
    public List<Acta> getAllActas(String coUbigeo, String feIni, String feFin) {
        String ubigeoRestri="";
        if(!coUbigeo.isEmpty())
            ubigeoRestri = " and ub.co_ubigeo_inei like '" + coUbigeo + "%'";



        String query  = "" +
                "select a.co_acta coActa, " +
                "       a.co_entidad coEntidad, " +
                "       e.de_entidad deEntidad, " +
                "       ub.de_departamento deDepartamento, " +
                "       ub.de_provincia deProvincia, " +
                "       ub.de_distrito deDistrito, " +
                "       to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                "       to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                "       to_char(a.fe_crea_audi, 'dd/mm/yyyy') feCreaaudi, " +
                "       a.us_crea_audi usCreaaudi, " +
                "       u.ap_primer apPrimer, " +
                "       u.ap_segundo apSegundo, " +
                "       u.prenombres prenombres, " +
                "       (select count(1) from pntv_acta_archivos where co_acta = a.co_acta) as nuArchivos, " +
                "       a.es_acta, " +
                "       a.co_est_salud, " +
                "       es.de_est_salud " +
                "  from pntv_acta a " +
                "  left join pntm_entidad e on a.co_entidad = e.co_entidad and e.es_entidad='1' " +
                "  left join pntm_usuario_externo u on u.co_usuario = a.us_crea_audi " +
                "  left join pntm_ubigeo_inei ub on ub.co_ubigeo_inei = e.co_ubigeo_inei and ub.es_ubigeo='1' " +
                "  left join pnvm_establecimiento_salud es on es.co_est_salud = a.co_est_salud " +
                " where trunc(a.fe_crea_audi) between ? and ? " +
                ubigeoRestri +
                "   and (a.es_acta <> '0' or a.es_acta is null) " +
                " order by a.fe_crea_audi ";
              
        Object[] params = new Object[]{feIni, feFin};
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Acta.class), params);
    }

    @Override
    public boolean updateEsActa(String coActa, String deObservacion, String esActa) {
        String query = "" +
                "update pntv_acta " +
                "   set de_observacion = ?, es_acta = ?  " +
                " where co_acta = ?";
        int result = 0;
        Object[] params = new Object[]{deObservacion, esActa, coActa};
        try {
            logger.debug(String.format("DAO '%s con '%s'",query, params));
            result = jdbcTemplate.update(query, params);
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return result == 1;
    }


    @Override
    public List<Acta> getAllActas(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin) {
        String ubigeoRestri="";
        if(!coUbigeo.isEmpty())
           ubigeoRestri = " and ub.co_ubigeo_inei like '" + coUbigeo + "%'";

        String query = "" +
                "select * " +
                "  from (select rownum as fila, t.* " +
                "          from (select a.co_acta coActa, " +
                "                       a.co_entidad coEntidad, " +
                "                       e.de_entidad deEntidad, " +
                "                       ub.de_departamento deDepartamento, " +
                "                       ub.de_provincia deProvincia, " +
                "                       ub.de_distrito deDistrito, " +
                "                       to_char(a.fe_ini, 'dd/mm/yyyy') feIni, " +
                "                       to_char(a.fe_fin, 'dd/mm/yyyy') feFin, " +
                "                       to_char(a.fe_crea_audi, 'dd/mm/yyyy') fe_crea_audi," +
                "                       a.us_crea_audi usCreaaudi, " +
                "                       u.ap_primer apPrimer, " +
                "                       u.ap_segundo apSegundo, " +
                "                       u.prenombres prenombres, " +
                "                       (select count(1) " +
                "                          from pntv_acta_archivos " +
                "                         where co_acta = a.co_acta) as nuArchivos, " +
                "                       a.es_acta, " +
                "                       a.co_est_salud, " +
                "                       es.de_est_salud " +
                "                  from pntv_acta a " +
                "                  left join pntm_entidad e on a.co_entidad = e.co_entidad and e.es_entidad='1' " +
                "                  left join pntm_usuario_externo u on u.co_usuario = " +
                "                                                      a.us_crea_audi " +
                "                  left join pntm_ubigeo_inei ub on ub.co_ubigeo_inei = " +
                "                                              e.co_ubigeo_inei and ub.es_ubigeo='1' " +
                "                  left join pnvm_establecimiento_salud es on es.co_est_salud = " +
                "                                                             a.co_est_salud " +
                "                 where trunc(a.fe_crea_audi) between ? and ? and (a.es_acta <> '0' or a.es_acta is null)" +
                    ubigeoRestri +
                "                 order by a.fe_crea_audi) t) " +
                " where fila between ? and ? ";

        Object[] params = new Object[]{feIni, feFin, filaIni, filaFin};
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Acta.class), params);
    }

    @Override
    public int getNumAllActas(String coUbigeo, String feIni, String feFin) {
        String ubigeoRestri="";
        if(!coUbigeo.isEmpty())
            ubigeoRestri = " and ub.co_ubigeo_inei like '" + coUbigeo + "%'";


        String query = "select count(1) from pntv_acta a " +
                " left join pntm_entidad e on a.co_entidad = e.co_entidad and e.es_entidad='1'\n" +
                " left join pntm_usuario_externo u on u.co_usuario = a.us_crea_audi\n" +
                " left join pntm_ubigeo_inei ub on ub.co_ubigeo_inei = e.co_ubigeo_inei and ub.es_ubigeo='1'\n" +
                " left join pnvm_establecimiento_salud es on es.co_est_salud = a.co_est_salud " +
                " where trunc(a.fe_crea_audi) between ? and ? " +
                ubigeoRestri +
                "   and (a.es_acta <> '0' or a.es_acta is null) ";
        Object[] params = new Object[]{feIni, feFin};
        return jdbcTemplate.queryForInt(query, params);
    }


    /**
     * datos para guardar en el directorio
     * @param coActa
     * @return
     */
    @Override
    public Acta getDirectory(String coActa) {
        String query = "" +
                "select a.co_acta, " +
                "       u.de_departamento, " +
                "       u.de_provincia, " +
                "       u.de_distrito, " +
                "       to_char(a.fe_crea_audi, 'yyyy') anio, " +
                "       to_char(a.fe_crea_audi, 'MM') mes " +
                "  from pntv_acta a " +
                "  left join pntm_entidad e on e.co_entidad = a.co_entidad and e.es_entidad='1' " +
                "  left join pntm_ubigeo_inei u on u.co_ubigeo_inei = e.co_ubigeo_inei and u.es_ubigeo='1' " +
                " where a.co_acta = ?";
        Object[] params = new Object[]{coActa};
        try {
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Acta.class), params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}