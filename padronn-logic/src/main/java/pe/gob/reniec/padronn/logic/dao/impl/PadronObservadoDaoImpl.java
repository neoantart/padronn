package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.xml.utils.StringBufferPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.PadronObservadoDao;
import pe.gob.reniec.padronn.logic.model.PadronObservado;
import pe.gob.reniec.padronn.logic.model.Persona;
import pe.gob.reniec.padronn.logic.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jfloresh on 15/03/2016.
 */
@Repository
public class PadronObservadoDaoImpl extends SimpleJdbcDaoBase implements PadronObservadoDao {

    private static Logger LOG = Logger.getLogger(PadronObservadoDaoImpl.class);

    @Autowired
    Usuario usuario;

    private static final String QUERY_OBTENER_PADRON_OBSERVADOS = "" +
            "SELECT ROWNUM as NU_ITEM, TA.*\n" +
            "  FROM (SELECT T1.CO_PADRON_NOMINAL,\n" +
            "               T2.NU_DNI_MENOR,\n" +
            "               T2.NU_CUI,\n" +
            "               T2.NU_CNV,\n" +
            "               T1.CO_TIPO_OBSERVACION,\n" +
            "               T1.ES_OBSERVADO,\n" +
            "               T1.DE_OBSERVADO_DETALLE,\n" +
            "               T1.US_CREA_AUDI,\n" +
            "               TO_CHAR(T1.FE_CREA_AUDI, 'DD/MM/YYYY') FE_CREA_AUDI,\n" +
            "               T1.US_MODI_AUDI,\n" +
            "               TO_CHAR(T1.FE_MODI_AUDI, 'DD/MM/YYYY') FE_MODI_AUDI,\n" +
            "               T2.AP_PRIMER_MENOR,\n" +
            "               T2.AP_SEGUNDO_MENOR,\n" +
            "               T2.PRENOMBRE_MENOR,\n" +
            "               TO_CHAR(T2.FE_NAC_MENOR, 'DD/MM/YYYY') FE_NAC_MENOR,\n" +
            "               T3.DE_TIPO_OBSERVACION,\n" +
            "               T2.AP_PRIMER_MADRE,\n" +
            "               T2.AP_SEGUNDO_MADRE,\n" +
            "               T2.PRENOM_MADRE,\n" +
            "               T2.CO_DNI_MADRE\n" +
            "          FROM PNTM_PADRON_OBSERVADO T1\n" +
            "         INNER JOIN PNTV_PADRON_NOMINAL T2\n" +
            "            ON T1.CO_PADRON_NOMINAL = T2.CO_PADRON_NOMINAL\n" +
            "         INNER JOIN PNTR_TIPO_OBSERVACION T3\n" +
            "            ON T1.CO_TIPO_OBSERVACION = T3.CO_TIPO_OBSERVACION\n" +
            "         WHERE T1.ES_OBSERVADO = '1'\n" +
            "           AND T2.ES_PADRON = '2'\n" +
            "           AND T3.ES_TIPO_OBSERVACION = '1'" +
            "           AND T2.NU_EDAD<6 \n" +
            "           AND T2.CO_UBIGEO_INEI = ?\n" +
            "         ORDER BY T2.FE_NAC_MENOR DESC, T3.NU_ORDEN ASC) TA";

    private static final String QUERY_OBTENER_PADRON_OBSERVADOS_PAGINADO = "" +
            "SELECT *\n" +
            "  FROM (SELECT ROWNUM FILA, T.*\n" +
            "          FROM (\n" +
            QUERY_OBTENER_PADRON_OBSERVADOS +
            "                   ) T\n" +
            "         WHERE ROWNUM <= ?)\n" +
            " WHERE FILA >= ?";

    private static final String QUERY_CONTAR_OBSERVADOS = "" +
            "SELECT COUNT(1)\n" +
            "  FROM PNTM_PADRON_OBSERVADO T1\n" +
            " INNER JOIN PNTV_PADRON_NOMINAL T2\n" +
            "    ON T1.CO_PADRON_NOMINAL = T2.CO_PADRON_NOMINAL\n" +
            " INNER JOIN PNTR_TIPO_OBSERVACION T3\n" +
            "    ON T1.CO_TIPO_OBSERVACION = T3.CO_TIPO_OBSERVACION\n" +
            " WHERE T1.ES_OBSERVADO = '1'\n" +
            "   AND T2.ES_PADRON = '2'\n" +
            "   AND T2.NU_EDAD<6 \n" +
            "   AND T3.ES_TIPO_OBSERVACION = '1'\n" +
            "   AND T2.CO_UBIGEO_INEI = ?";

    private static final String QUERY_INSERT_PADRON_OBSERVADO = "" +
            "INSERT INTO PNTM_PADRON_OBSERVADO\n" +
            "  (CO_PADRON_NOMINAL,\n" +
            "   CO_TIPO_OBSERVACION,\n" +
            "   ES_OBSERVADO,\n" +
            "   DE_OBSERVADO_DETALLE,\n" +
            "   US_CREA_AUDI,\n" +
            "   FE_CREA_AUDI)\n" +
            "VALUES\n" +
            "  (?,\n" +
            "   ?,\n" +
            "   ?,\n" +
            "   ?,\n" +
            "   ?,\n" +
            "   SYSDATE\n" +
            "   )";

    private static final String QUERY_OBTENER_PADRON_OBSERVADOS_CO_TIPO_OBSERVACION = "" +
            "SELECT ROWNUM as NU_ITEM, TA.*\n" +
            "  FROM (SELECT T1.CO_PADRON_NOMINAL,\n" +
            "               T2.NU_DNI_MENOR,\n" +
            "               T2.NU_CUI,\n" +
            "               T2.NU_CNV,\n" +
            "               T1.CO_TIPO_OBSERVACION,\n" +
            "               T1.ES_OBSERVADO,\n" +
            "               T1.DE_OBSERVADO_DETALLE,\n" +
            "               T1.US_CREA_AUDI,\n" +
            "               TO_CHAR(T1.FE_CREA_AUDI, 'DD/MM/YYYY') FE_CREA_AUDI,\n" +
            "               T1.US_MODI_AUDI,\n" +
            "               TO_CHAR(T1.FE_MODI_AUDI, 'DD/MM/YYYY') FE_MODI_AUDI,\n" +
            "               T2.AP_PRIMER_MENOR,\n" +
            "               T2.AP_SEGUNDO_MENOR,\n" +
            "               T2.PRENOMBRE_MENOR,\n" +
            "               TO_CHAR(T2.FE_NAC_MENOR, 'DD/MM/YYYY') FE_NAC_MENOR,\n" +
            "               T3.DE_TIPO_OBSERVACION,\n" +
            "               T2.AP_PRIMER_MADRE,\n" +
            "               T2.AP_SEGUNDO_MADRE,\n" +
            "               T2.PRENOM_MADRE,\n" +
            "               T2.CO_DNI_MADRE\n" +
            "          FROM PNTM_PADRON_OBSERVADO T1\n" +
            "         INNER JOIN PNTV_PADRON_NOMINAL T2\n" +
            "            ON T1.CO_PADRON_NOMINAL = T2.CO_PADRON_NOMINAL\n" +
            "         INNER JOIN PNTR_TIPO_OBSERVACION T3\n" +
            "            ON T1.CO_TIPO_OBSERVACION = T3.CO_TIPO_OBSERVACION\n" +
            "         WHERE T1.ES_OBSERVADO = '1'\n" +
            "           AND T2.ES_PADRON = '2'\n" +
            /*"           AND T2.ES_PADRON = '1'\n" +*/
            "           AND T3.ES_TIPO_OBSERVACION = '1'" +
            "           AND T2.NU_EDAD<6 \n" +
            "           AND T2.CO_UBIGEO_INEI = ?\n" +
            "           AND T1.CO_TIPO_OBSERVACION=?\n" +
            "         ORDER BY T2.FE_NAC_MENOR DESC, T3.NU_ORDEN ASC, T2.AP_PRIMER_MENOR,T2.AP_SEGUNDO_MENOR /*T2.FE_NAC_MENOR DESC*/) TA";

    private static final String QUERY_OBTENER_PADRON_OBSERVADOS_PAGINADO_CO_TIPO_OBSERVACION = "" +
            "SELECT *\n" +
            "  FROM (SELECT ROWNUM FILA, T.*\n" +
            "          FROM (\n" +
            QUERY_OBTENER_PADRON_OBSERVADOS_CO_TIPO_OBSERVACION +
            "                   ) T\n" +
            "         WHERE ROWNUM <= ?)\n" +
            " WHERE FILA >= ?";

    private static final String QUERY_CONTRAR_OBSERVADOS_CO_TIPO_OBSERVACION = "" +
            "SELECT COUNT(1)\n" +
            "  FROM PNTM_PADRON_OBSERVADO T1\n" +
            " INNER JOIN PNTV_PADRON_NOMINAL T2\n" +
            "    ON T1.CO_PADRON_NOMINAL = T2.CO_PADRON_NOMINAL\n" +
            " INNER JOIN PNTR_TIPO_OBSERVACION T3\n" +
            "    ON T1.CO_TIPO_OBSERVACION = T3.CO_TIPO_OBSERVACION\n" +
            " WHERE T1.ES_OBSERVADO = '1'\n" +
            "   AND T2.ES_PADRON = '2'" +
            /*"   AND T2.ES_PADRON = '1'" +*/
            "   AND T2.NU_EDAD<6 \n" +
            "   AND T3.ES_TIPO_OBSERVACION = '1' \n" +
            "   AND T2.CO_UBIGEO_INEI = ? \n" +
            "   AND T1.CO_TIPO_OBSERVACION=?\n";

    private static final String OBTENER_PADRON_OBSERVADO = "" +
            "SELECT CO_PADRON_NOMINAL,\n" +
            "       CO_TIPO_OBSERVACION,\n" +
            "       ES_OBSERVADO,\n" +
            "       DE_OBSERVADO_DETALLE,\n" +
            "       US_CREA_AUDI,\n" +
            "       TO_CHAR(FE_CREA_AUDI, 'DD/MM/YYYY') FE_CREA_AUDI,\n" +
            "       US_MODI_AUDI,\n" +
            "       TO_CHAR(FE_MODI_AUDI, 'DD/MM/YYYY') FE_MODI_AUDI,\n" +
            "       CO_PADRON_NOMINAL_DUPLI\n" +
            "  FROM PNTM_PADRON_OBSERVADO\n" +
            " WHERE ES_OBSERVADO = '1'\n" +
            "   AND CO_PADRON_NOMINAL = ?\n" +
            "   AND CO_TIPO_OBSERVACION = ?";

    private static final String QUERY_SET_ES_OBSERVADO = "" +
            "UPDATE PNTM_PADRON_OBSERVADO \n" +
            "SET ES_OBSERVADO=?, " +
            " US_MODI_AUDI=?, " +
            " FE_MODI_AUDI=SYSDATE\n" +
            "WHERE CO_PADRON_NOMINAL=? AND CO_TIPO_OBSERVACION=?";

    @Override
    public void insertPadronObservado(PadronObservado padronObservado) {
        String sql = QUERY_INSERT_PADRON_OBSERVADO;
        Object[] params = {
                padronObservado.getCoPadronNominal(),
                padronObservado.getCoTipoObservacion(),
                padronObservado.getEsObservado(),
                padronObservado.getDeObservadoDetalle(),
                padronObservado.getUsCreaAudi(),
        };
        LOG.info(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            jdbcTemplate.update(sql, params);
        } catch (Exception e) {
            LOG.error("Error:", e);
        }
    }

    @Override
    public List<PadronObservado> obtenerPadronObservados(String coUbigeoInei) {
        String sql = QUERY_OBTENER_PADRON_OBSERVADOS;
        Object[] params = new Object[]{coUbigeoInei};
        LOG.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PadronObservado.class), params);
        } catch (Exception e) {
            LOG.error("error", e);
            return null;
        }
    }


    @Override
    public List<PadronObservado> obtenerPadronObservados(String coUbigeoInei, String filaIni, String filaFin) {
        String sql = QUERY_OBTENER_PADRON_OBSERVADOS_PAGINADO;
        Object[] params = new Object[]{coUbigeoInei, filaFin, filaIni};
        LOG.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PadronObservado.class), params);
        } catch (Exception e) {
            LOG.error("error", e);
            return null;
        }
    }

    @Override
    public Integer contarPadronObservados(String coUbigeoInei) {
        LOG.info("Inicia metodo contarPadronObservados");
        String sql = QUERY_CONTAR_OBSERVADOS;
        Object[] params = new Object[]{coUbigeoInei};
        LOG.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForObject(sql, params, Integer.class);
        } catch (Exception e) {
            LOG.error("Error:", e);
            return 0;
        }
    }

    @Override
    public List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion) {
        String sql = QUERY_OBTENER_PADRON_OBSERVADOS_CO_TIPO_OBSERVACION;
        Object[] params = new Object[]{coUbigeoInei, coTipoObservacion};
        LOG.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PadronObservado.class), params);
        } catch (Exception e) {
            LOG.error("error", e);
            return null;
        }
    }

    @Override
    public List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion, String filaIni, String filaFin) {
        String sql = QUERY_OBTENER_PADRON_OBSERVADOS_PAGINADO_CO_TIPO_OBSERVACION;
        Object[] params = new Object[]{coUbigeoInei, coTipoObservacion, filaFin, filaIni};
        LOG.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PadronObservado.class), params);
        } catch (Exception e) {
            LOG.error("error", e);
            return null;
        }
    }

    @Override
    public Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion) {
        String sql = QUERY_CONTRAR_OBSERVADOS_CO_TIPO_OBSERVACION;
        Object[] params = new Object[]{coUbigeoInei, coTipoObservacion};
        LOG.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForObject(sql, params, Integer.class);
        } catch (Exception e) {
            LOG.error("Error:", e);
            return null;
        }
    }

    @Override
    public PadronObservado obtenerPadronObservado(String coPadronNominal, String coTipoObservacion) {
        String sql = OBTENER_PADRON_OBSERVADO;
        Object[] params = new Object[]{coPadronNominal, coTipoObservacion};
        LOG.debug(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(PadronObservado.class),params);
        } catch (Exception e) {
            LOG.error("Error:", e);
            return null;
        }
    }

    @Override
    public Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                              String nuCnv, String coDniMadre, String apPrimer, String apSegundo, String prenombres) {
        StringBuffer where = new StringBuffer();
        where.append(" WHERE T1.ES_OBSERVADO = '1'\n");
        where.append("   AND T2.NU_EDAD<6 \n");
        where.append("   AND T3.ES_TIPO_OBSERVACION = '1' \n");
        where.append("   AND T2.CO_UBIGEO_INEI = ? \n");
        where.append("   AND T1.CO_TIPO_OBSERVACION=?\n");


        boolean datosSuficientes = false;
        String and = "";
        List<Object> parameters = new ArrayList<Object>();

        parameters.add(coUbigeoInei);
        parameters.add(coTipoObservacion);
        if (nuCnv != null && !nuCnv.isEmpty()) {
            where.append(" AND T2.NU_CNV=?");
            datosSuficientes = true;
            parameters.add(nuCnv);
        } else if (coDniMadre != null && !coDniMadre.isEmpty()) {
            where.append(" AND T2.CO_DNI_MADRE=?");
            datosSuficientes = true;
            parameters.add(coDniMadre);
        } else {
            if (apPrimer != null && !apPrimer.isEmpty()) {
                where.append(" AND T2.AP_PRIMER_MADRE LIKE ?");
                and = " AND ";
                datosSuficientes = true;
                parameters.add(apPrimer.toUpperCase() + "%");
            }
            if (apSegundo != null && !apSegundo.isEmpty()) {
                where.append(and + " T2.AP_SEGUNDO_MADRE LIKE ?");
                and = " and ";
                datosSuficientes = true;
                parameters.add(apSegundo.toUpperCase() + "%");
            }
            if (prenombres != null && !prenombres.isEmpty()) {
                where.append(and + " T2.PRENOM_MADRE LIKE ?");
                datosSuficientes = true;
                parameters.add("%" + prenombres.toUpperCase() + "%");
            }
        }

        if (datosSuficientes) {
            String sql = "" +
                    "SELECT COUNT(1)\n" +
                    "  FROM PNTM_PADRON_OBSERVADO T1\n" +
                    " INNER JOIN PNTV_PADRON_NOMINAL T2\n" +
                    "    ON T1.CO_PADRON_NOMINAL = T2.CO_PADRON_NOMINAL\n" +
                    " INNER JOIN PNTR_TIPO_OBSERVACION T3\n" +
                    "    ON T1.CO_TIPO_OBSERVACION = T3.CO_TIPO_OBSERVACION\n"
                    + where.toString();
            LOG.debug(String.format("DAO '%s' con '%s'", sql, parameters));
            try {
                return jdbcTemplate.queryForObject(sql, parameters.toArray(new Object[parameters.size()]), Integer.class);
            } catch (Exception e) {
                LOG.error("Error:", e);
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                                             String nuCnv, String coDniMadre, String apPrimer, String apSegundo, String prenombres,
                                                                             String filaIni, String filaFin) {
        StringBuffer where = new StringBuffer();
        where.append(" WHERE T1.ES_OBSERVADO = '1'\n");
        where.append("   AND T2.NU_EDAD<6 \n");
        where.append("   AND T3.ES_TIPO_OBSERVACION = '1' \n");
        where.append("   AND T2.CO_UBIGEO_INEI = ? \n");
        where.append("   AND T1.CO_TIPO_OBSERVACION=?\n");

        boolean datosSuficientes = false;
        String and = "";
        List<Object> parameters = new ArrayList<Object>();

        parameters.add(coUbigeoInei);
        parameters.add(coTipoObservacion);
        if (nuCnv != null && !nuCnv.isEmpty()) {
            where.append(" AND T2.NU_CNV=? ");
            datosSuficientes = true;
            parameters.add(nuCnv);
        } else if (coDniMadre != null && !coDniMadre.isEmpty()) {
            where.append(" AND T2.CO_DNI_MADRE=? ");
            datosSuficientes = true;
            parameters.add(coDniMadre);
        } else {
            if (apPrimer != null && !apPrimer.isEmpty()) {
                where.append(" AND T2.AP_PRIMER_MADRE LIKE ?");
                and = " AND ";
                datosSuficientes = true;
                parameters.add(apPrimer.toUpperCase() + "%");
            }
            if (apSegundo != null && !apSegundo.isEmpty()) {
                where.append(and + " T2.AP_SEGUNDO_MADRE LIKE ?");
                and = " AND ";
                datosSuficientes = true;
                parameters.add(apSegundo.toUpperCase() + "%");
            }
            if (prenombres != null && !prenombres.isEmpty()) {
                where.append(and + " T2.PRENOM_MADRE LIKE ?");
                datosSuficientes = true;
                parameters.add("%" + prenombres.toUpperCase() + "%");
            }
        }

        if (datosSuficientes) {
            parameters.add(filaFin);
            parameters.add(filaIni);
            String sql = "" +
                    "SELECT *\n" +
                    "  FROM (SELECT ROWNUM FILA, T.*\n" +
                    "          FROM (\n" +
                    "SELECT ROWNUM as NU_ITEM, TA.*\n" +
                    "  FROM (SELECT T1.CO_PADRON_NOMINAL,\n" +
                    "               T2.NU_DNI_MENOR,\n" +
                    "               T2.NU_CUI,\n" +
                    "               T2.NU_CNV,\n" +
                    "               T1.CO_TIPO_OBSERVACION,\n" +
                    "               T1.ES_OBSERVADO,\n" +
                    "               T1.DE_OBSERVADO_DETALLE,\n" +
                    "               T1.US_CREA_AUDI,\n" +
                    "               TO_CHAR(T1.FE_CREA_AUDI, 'DD/MM/YYYY') FE_CREA_AUDI,\n" +
                    "               T1.US_MODI_AUDI,\n" +
                    "               TO_CHAR(T1.FE_MODI_AUDI, 'DD/MM/YYYY') FE_MODI_AUDI,\n" +
                    "               T2.AP_PRIMER_MENOR,\n" +
                    "               T2.AP_SEGUNDO_MENOR,\n" +
                    "               T2.PRENOMBRE_MENOR,\n" +
                    "               TO_CHAR(T2.FE_NAC_MENOR, 'DD/MM/YYYY') FE_NAC_MENOR,\n" +
                    "               T3.DE_TIPO_OBSERVACION,\n" +
                    "               T2.AP_PRIMER_MADRE,\n" +
                    "               T2.AP_SEGUNDO_MADRE,\n" +
                    "               T2.PRENOM_MADRE,\n" +
                    "               T2.CO_DNI_MADRE\n" +
                    "          FROM PNTM_PADRON_OBSERVADO T1\n" +
                    "         INNER JOIN PNTV_PADRON_NOMINAL T2\n" +
                    "            ON T1.CO_PADRON_NOMINAL = T2.CO_PADRON_NOMINAL\n" +
                    "         INNER JOIN PNTR_TIPO_OBSERVACION T3\n" +
                    "            ON T1.CO_TIPO_OBSERVACION = T3.CO_TIPO_OBSERVACION\n" +
                     where.toString() +
                    "         ORDER BY T2.FE_NAC_MENOR DESC, T3.NU_ORDEN ASC, T2.AP_PRIMER_MENOR,T2.AP_SEGUNDO_MENOR /*T2.FE_NAC_MENOR DESC*/) TA" +
                    "                   ) T\n" +
                    "         WHERE ROWNUM <= ?)\n" +
                    " WHERE FILA >= ?";
            LOG.debug(String.format("DAO '%s' con '%s'", sql, parameters));
            try {
                return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PadronObservado.class), parameters.toArray(new Object[parameters.size()]));
            } catch (Exception e) {
                LOG.error("Error:", e);
                return new ArrayList<PadronObservado>();
            }
        } else {
            return new ArrayList<PadronObservado>();
        }
    }


    @Override
    public Integer contarPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                              String nuDniMenor, String apPrimer, String apSegundo, String prenombres) {
        StringBuffer where = new StringBuffer();
        where.append(" WHERE T1.ES_OBSERVADO = '1'\n");
        where.append("   AND T2.NU_EDAD<6 \n");
        where.append("   AND T3.ES_TIPO_OBSERVACION = '1' \n");
        where.append("   AND T2.CO_UBIGEO_INEI = ? \n");



        boolean datosSuficientes = false;
        String and = "";
        List<Object> parameters = new ArrayList<Object>();

        parameters.add(coUbigeoInei);
        if (coTipoObservacion != null && !coTipoObservacion.isEmpty()) {
            where.append("   AND T1.CO_TIPO_OBSERVACION=?\n");
            parameters.add(coTipoObservacion);
        }
        if (nuDniMenor != null && !nuDniMenor.isEmpty()) {
            where.append(" AND (T2.NU_DNI_MENOR=? OR T2.CO_PADRON_NOMINAL=?)");
            datosSuficientes = true;
            parameters.add(nuDniMenor);
            parameters.add(nuDniMenor);
        } else {
            if (apPrimer != null && !apPrimer.isEmpty()) {
                where.append(" AND T2.AP_PRIMER_MENOR LIKE ?");
                and = " AND ";
                datosSuficientes = true;
                parameters.add(apPrimer.toUpperCase() + "%");
            }
            if (apSegundo != null && !apSegundo.isEmpty()) {
                where.append(and + " T2.AP_SEGUNDO_MENOR LIKE ?");
                and = " and ";
                datosSuficientes = true;
                parameters.add(apSegundo.toUpperCase() + "%");
            }
            if (prenombres != null && !prenombres.isEmpty()) {
                where.append(and + " T2.PRENOMBRE_MENOR LIKE ?");
                datosSuficientes = true;
                parameters.add("%" + prenombres.toUpperCase() + "%");
            }
        }

        if (datosSuficientes) {
            String sql = "" +
                    "SELECT COUNT(1)\n" +
                    "  FROM PNTM_PADRON_OBSERVADO T1\n" +
                    " INNER JOIN PNTV_PADRON_NOMINAL T2\n" +
                    "    ON T1.CO_PADRON_NOMINAL = T2.CO_PADRON_NOMINAL\n" +
                    " INNER JOIN PNTR_TIPO_OBSERVACION T3\n" +
                    "    ON T1.CO_TIPO_OBSERVACION = T3.CO_TIPO_OBSERVACION\n"
                    + where.toString();
            LOG.debug(String.format("DAO '%s' con '%s'", sql, parameters));
            try {
                return jdbcTemplate.queryForObject(sql, parameters.toArray(new Object[parameters.size()]), Integer.class);
            } catch (Exception e) {
                LOG.error("Error:", e);
                return 0;
            }
        } else {
            return 0;
        }


    }

    @Override
    public List<PadronObservado> obtenerPadronObservadosPorCoTipoObservacion(String coUbigeoInei, String coTipoObservacion,
                                                                             String nuDniMenor, String apPrimer, String apSegundo, String prenombres,
                                                                             String filaIni, String filaFin) {
        StringBuffer where = new StringBuffer();
        where.append(" WHERE T1.ES_OBSERVADO = '1'\n");
        where.append("   AND T2.NU_EDAD<6 \n");
        where.append("   AND T3.ES_TIPO_OBSERVACION = '1' \n");
        where.append("   AND T2.CO_UBIGEO_INEI = ? \n");

        boolean datosSuficientes = false;
        String and = "";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(coUbigeoInei);
        if (coTipoObservacion != null && !coTipoObservacion.isEmpty()) {
            where.append("   AND T1.CO_TIPO_OBSERVACION=?\n");
            parameters.add(coTipoObservacion);
        }

        if (nuDniMenor != null && !nuDniMenor.isEmpty()) {
            where.append(" AND (T2.NU_DNI_MENOR=? OR T2.CO_PADRON_NOMINAL=?)");
            datosSuficientes = true;
            parameters.add(nuDniMenor);
            parameters.add(nuDniMenor);
        } else {
            if (apPrimer != null && !apPrimer.isEmpty()) {
                where.append(" AND T2.AP_PRIMER_MENOR LIKE ?");
                and = " AND ";
                datosSuficientes = true;
                parameters.add(apPrimer.toUpperCase() + "%");
            }
            if (apSegundo != null && !apSegundo.isEmpty()) {
                where.append(and + " T2.AP_SEGUNDO_MENOR LIKE ?");
                and = " AND ";
                datosSuficientes = true;
                parameters.add(apSegundo.toUpperCase() + "%");
            }
            if (prenombres != null && !prenombres.isEmpty()) {
                where.append(and + " T2.PRENOMBRE_MENOR LIKE ?");
                datosSuficientes = true;
                parameters.add("%" + prenombres.toUpperCase() + "%");
            }
        }

        if (datosSuficientes) {
            parameters.add(filaFin);
            parameters.add(filaIni);
            String sql = "" +
                    "SELECT *\n" +
                    "  FROM (SELECT ROWNUM FILA, T.*\n" +
                    "          FROM (\n" +
                    "SELECT ROWNUM as NU_ITEM, TA.*\n" +
                    "  FROM (SELECT T1.CO_PADRON_NOMINAL,\n" +
                    "               T2.NU_DNI_MENOR,\n" +
                    "               T2.NU_CUI,\n" +
                    "               T2.NU_CNV,\n" +
                    "               T1.CO_TIPO_OBSERVACION,\n" +
                    "               T1.ES_OBSERVADO,\n" +
                    "               T1.DE_OBSERVADO_DETALLE,\n" +
                    "               T1.US_CREA_AUDI,\n" +
                    "               TO_CHAR(T1.FE_CREA_AUDI, 'DD/MM/YYYY') FE_CREA_AUDI,\n" +
                    "               T1.US_MODI_AUDI,\n" +
                    "               TO_CHAR(T1.FE_MODI_AUDI, 'DD/MM/YYYY') FE_MODI_AUDI,\n" +
                    "               T2.AP_PRIMER_MENOR,\n" +
                    "               T2.AP_SEGUNDO_MENOR,\n" +
                    "               T2.PRENOMBRE_MENOR,\n" +
                    "               TO_CHAR(T2.FE_NAC_MENOR, 'DD/MM/YYYY') FE_NAC_MENOR,\n" +
                    "               T3.DE_TIPO_OBSERVACION,\n" +
                    "               T2.AP_PRIMER_MADRE,\n" +
                    "               T2.AP_SEGUNDO_MADRE,\n" +
                    "               T2.PRENOM_MADRE,\n" +
                    "               T2.CO_DNI_MADRE\n" +
                    "          FROM PNTM_PADRON_OBSERVADO T1\n" +
                    "         INNER JOIN PNTV_PADRON_NOMINAL T2\n" +
                    "            ON T1.CO_PADRON_NOMINAL = T2.CO_PADRON_NOMINAL\n" +
                    "         INNER JOIN PNTR_TIPO_OBSERVACION T3\n" +
                    "            ON T1.CO_TIPO_OBSERVACION = T3.CO_TIPO_OBSERVACION\n" +
                    where.toString() +
                    "         ORDER BY T3.NU_ORDEN ASC, T2.FE_NAC_MENOR DESC, T2.AP_PRIMER_MENOR,T2.AP_SEGUNDO_MENOR /*T2.FE_NAC_MENOR DESC*/) TA" +
                    "                   ) T\n" +
                    "         WHERE ROWNUM <= ?)\n" +
                    " WHERE FILA >= ?";
            LOG.debug(String.format("DAO '%s' con '%s'", sql, parameters));
            try {
                return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PadronObservado.class), parameters.toArray(new Object[parameters.size()]));
            } catch (Exception e) {
                LOG.error("Error:", e);
                return new ArrayList<PadronObservado>();
            }
        } else {
            return new ArrayList<PadronObservado>();
        }
    }

    @Override
    public void setEsObservado(String esObservado, String coPadronNominal, String coTipoObservacion) {
        String sql = QUERY_SET_ES_OBSERVADO;
        Object[] params = new Object[]{esObservado, usuario.getLogin(), coPadronNominal, coTipoObservacion};
        LOG.info(String.format("DAO '%s' con '%s'", sql, ArrayUtils.toString(params)));
        try {
            jdbcTemplate.update(sql, params);
        } catch (Exception e) {
            LOG.error("Error:", e);
        }
    }

    @Override
    public boolean existeObservacionActiva(String coPadronNominal) {
        String query = "select count(1) from pntm_padron_observado where co_padron_nominal=? and es_observado='1' /*and co_tipo_observacion='5'*/";
        Object[] params = { coPadronNominal };
        logger.debug(String.format("DAO '%s' con '%s'", new Object[] { query, ArrayUtils.toString(params) }));
        return this.jdbcTemplate.queryForInt(query, params) > 0;
    }

}