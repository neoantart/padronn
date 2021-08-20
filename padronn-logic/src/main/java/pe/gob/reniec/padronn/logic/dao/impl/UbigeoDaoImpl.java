package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.UbigeoDao;
import pe.gob.reniec.padronn.logic.model.CentroPoblado;
import pe.gob.reniec.padronn.logic.model.EjeVial;
import pe.gob.reniec.padronn.logic.model.Ubigeo;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class UbigeoDaoImpl extends SimpleJdbcDaoBase implements UbigeoDao {

    @Autowired
    PadronProperties padronProperties;

    /**
     * Lista de la tabla de ubigeos los elementos cuyo nombre de provincia o distrito coincide con el nombre
     * parcial indicado. Si nombreParcial es nulo o vacio se retorna una lista vacia
     *
     * @param nombreParcial      El nombre parcial para la búsqueda
     * @param cantidadResultados La cantidad de elementos a retornarse
     * @return Lista de Ubigeos que cumplen con los resultados indicados o vacio
     */
    @Override
    public List<Ubigeo> buscarPorProvinciaDistritoEnDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
        if (nombreParcial != null && !nombreParcial.isEmpty()) {
            String query = "select co_ubigeo_inei, de_departamento, de_provincia, de_distrito " +
                    "from pntm_ubigeo_inei " +
                    "where co_ubigeo_inei like ? and (upper(de_distrito) like ? or upper(de_provincia) like ?) and " +
                    "ROWNUM <= ? and es_ubigeo='1' " +
                    "order by de_departamento, de_provincia, de_distrito";
            nombreParcial = nombreParcial.toUpperCase() + "%";
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Ubigeo.class), codigoUbigeoInei + "%", nombreParcial, "%" + nombreParcial, cantidadResultados);
        }
        return new ArrayList<Ubigeo>();
    }

    @Override
    public List<Ubigeo> buscarPorProvinciaDistritoFueraDeDepartamento(String nombreParcial, String codigoUbigeoInei, int cantidadResultados) {
        if (nombreParcial != null && !nombreParcial.isEmpty()) {
            String query = "select co_ubigeo_inei, de_departamento, de_provincia, de_distrito " +
                    "from pntm_ubigeo_inei " +
                    "where co_ubigeo_inei not like ? and (upper(de_distrito) like ? or upper(de_provincia) like ?) and " +
                    "ROWNUM <= ? and es_ubigeo='1' " +
                    "order by de_departamento, de_provincia, de_distrito";
            nombreParcial = nombreParcial.toUpperCase() + "%";
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Ubigeo.class), codigoUbigeoInei + "%", nombreParcial, "%" + nombreParcial, cantidadResultados);
        }
        return new ArrayList<Ubigeo>();
    }

    @Override
    public List<Ubigeo> listarPorDepartamento(String codigoDepartamento) {
        String query = "select co_ubigeo_inei, de_departamento, de_provincia, de_distrito " +
                "from pntm_ubigeo_inei " +
                "where co_ubigeo_inei like ? and es_ubigeo='1' " +
                "order by de_departamento, de_provincia, de_distrito";
        Object[] params = new Object[]{codigoDepartamento + "%"};
//		logger.info("DAO '" + query + "' con '" + Arrays.toString(params) + "' por ejecutar");
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Ubigeo.class), params);
    }

    @Override
    public List<CentroPoblado> listaCentrosPobladosPorDepartamento(String codigoDepartamento) {
        String query = "" +
                "select co_centro_poblado, no_centro_poblado, co_ubigeo_inei, co_categoria, no_categoria, de_departamento, de_provincia, de_distrito,co_area_ccpp, de_area_ccpp " +
                "  from pntm_centro_poblado " +
                " where co_centro_poblado like ? and es_centro_poblado='1'" +
                " order by de_departamento, de_provincia, de_distrito, no_centro_poblado";
        Object[] params = new Object[]{codigoDepartamento + "%"};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CentroPoblado.class), params);
    }

    @Override
    public List<CentroPoblado> buscarCentroPoblado(String coUbigeoInei, String parteNoCentroPoblado) {
        String query = "" +
                "select co_centro_poblado, no_centro_poblado, no_categoria, co_area_ccpp, de_area_ccpp " +
                "  from pntm_centro_poblado " +
                " where co_centro_poblado like ? " +
                "   and no_centro_poblado like ? " +
                "   and rownum < 101 and /*co_area_ccpp='2' and */es_centro_poblado='1'" +
                "   order by no_categoria";
        Object[] params = new Object[]{coUbigeoInei + "%", "%" + parteNoCentroPoblado + "%"};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));

        List<CentroPoblado> cp;
        try {
            cp = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CentroPoblado.class), params);
            return cp;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CentroPoblado getCentroPoblado(String coCentroPoblado) {
        String query = "select co_centro_poblado, no_centro_poblado, de_ubigeo_inei, no_categoria, co_area_ccpp, de_area_ccpp " +
                "from pntm_centro_poblado  " +
                "where co_centro_poblado=?";
        Object[] params = new Object[]{coCentroPoblado};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(CentroPoblado.class), params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Ubigeo obtenerPorCodigoUbigeoReniec(String codigoUbigeoReniec) {
        String query = "select co_ubigeo_inei, de_departamento, de_provincia, de_distrito " +
                "from pntm_ubigeo_inei " +
                "where co_ubigeo_reniec like ? and es_ubigeo='1' " +
                "order by de_departamento, de_provincia, de_distrito";
        try {
            logger.debug(String.format("DAO '%s' con '%s'", query, codigoUbigeoReniec));
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Ubigeo.class), codigoUbigeoReniec);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public Ubigeo obtenerPorCodigoInei(String codigoInei) {
        String query = "select co_ubigeo_inei, de_departamento, de_provincia, de_distrito, es_ubigeo " +
                "from pntm_ubigeo_inei " +
                "where co_ubigeo_inei =? and es_ubigeo='1'";
        try {
            Object[] params = new Object[]{codigoInei};
            logger.info("DAO '" + query + "' con '" + Arrays.toString(params) + "' por ejecutar");
            return jdbcTemplate.queryForObject(query, params, BeanPropertyRowMapper.newInstance(Ubigeo.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new Ubigeo();
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return new Ubigeo();
        }
    }

    @Override
    public String obtenerDeUbigeoInei(String coUbigeoInei) {
        String query = "" +
                "SELECT *  FROM ( (SELECT DISTINCT SUBSTR(co_ubigeo_inei, 1, 2) AS co_ubigeo_inei,      \n" +
                "                  'Departamento('||UPPER(de_departamento)||')'  AS de_ubigeo_inei   \n" +
                "                  FROM pntm_ubigeo_inei  where es_ubigeo='1'\n" +
                "                 )  \n" +
                "                 UNION \n" +
                "                 (SELECT DISTINCT SUBSTR(co_ubigeo_inei, 1, 4) AS co_ubigeo_inei,      \n" +
                "                  'Departamento('||UPPER(de_departamento)||')' || ',' ||'Provincia('|| UPPER(de_provincia)||')' AS de_ubigeo_inei    \n" +
                "                  FROM pntm_ubigeo_inei  where es_ubigeo='1'\n" +
                "                 )  \n" +
                "                 UNION    \n" +
                "                 (SELECT co_ubigeo_inei AS co_ubigeo_inei,      \n" +
                "                  'Departamento('||UPPER(de_departamento)||')'  || ',' || 'Provincia('|| UPPER(de_provincia)||')' || ',' ||'Distrito(' ||UPPER(de_distrito)||')' AS de_ubigeo_inei    \n" +
                "                  FROM pntm_ubigeo_inei  where es_ubigeo='1'   \n" +
                "                 )\n" +
                "               ) t  \n" +
                "               WHERE co_ubigeo_inei = ?";
        try {
            Object[] params = new Object[]{coUbigeoInei};
            Ubigeo ubigeo = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Ubigeo.class), params);
            return ubigeo.getDeUbigeoInei();
        } catch (EmptyResultDataAccessException ee) {
            ee.printStackTrace();
            return "";
        } catch (IncorrectResultSizeDataAccessException ie) {
            ie.printStackTrace();
            return "";
        }
    }

    @Override
    public String obtenerDeUbigeoIneiCorto(String coUbigeoInei) {
        String query = "" +
                "SELECT *  FROM ( (SELECT DISTINCT SUBSTR(co_ubigeo_inei, 1, 2) AS co_ubigeo_inei,      \n" +
                "                  UPPER(de_departamento) AS de_ubigeo_inei   \n" +
                "                  FROM pntm_ubigeo_inei  where es_ubigeo='1'\n" +
                "                 )  \n" +
                "                 UNION \n" +
                "                 (SELECT DISTINCT SUBSTR(co_ubigeo_inei, 1, 4) AS co_ubigeo_inei,      \n" +
                "                  UPPER(de_departamento)||'/' || UPPER(de_provincia) AS de_ubigeo_inei   \n" +
                "                  FROM pntm_ubigeo_inei  where es_ubigeo='1'\n" +
                "                 )  \n" +
                "                 UNION    \n" +
                "                 (SELECT co_ubigeo_inei AS co_ubigeo_inei,      \n" +
                "                  UPPER(de_departamento)||'/'|| UPPER(de_provincia)||'/'|| UPPER(de_distrito) AS de_ubigeo_inei    \n" +
                "                  FROM pntm_ubigeo_inei  where es_ubigeo='1'   \n" +
                "                 )\n" +
                "               ) t  \n" +
                "               WHERE co_ubigeo_inei = ?";
        try {
            Object[] params = new Object[]{coUbigeoInei};
            Ubigeo ubigeo = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Ubigeo.class), params);
            return ubigeo.getDeUbigeoInei();
        } catch (EmptyResultDataAccessException ee) {
            ee.printStackTrace();
            return "";
        } catch (IncorrectResultSizeDataAccessException ie) {
            ie.printStackTrace();
            return "";
        }
    }

    @Override
    public List<Ubigeo> getDistritosEuropan() {
        String query = "" +
                "select co_ubigeo_reniec, " +
                "        co_ubigeo_inei as id, " +
                "        de_departamento, " +
                "        de_provincia, " +
                "        de_distrito, " +
                "        es_ubigeo, " +
                "        de_departamento || ', ' || de_provincia || ', ' || de_distrito  as text" +
                "   from pntm_ubigeo_inei" +
                " where co_ubigeo_inei in (" + StringUtils.join(padronProperties.DISTRITOS_EUROPAN, ',') + ") and es_ubigeo='1' ";

        logger.debug(String.format("DAO '%s'", query));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Ubigeo.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public List<CentroPoblado> buscarUbigeoInei(String parteDeUbigeo, int filaIni, int filaFin) {
        parteDeUbigeo = parteDeUbigeo.trim().equals("") ? "" : parteDeUbigeo + "%";
        String query = "" +
                "select * " +
                "  from (select rownum fila, t.* " +
                "          from ( " +
                "        (select co_centro_poblado, " +
                "               case " +
                "                 when co_categoria = '01' or co_categoria = '04' then " +
                "                  de_ubigeo_inei " +
                "                 else " +
                "                  de_ubigeo_inei || ' / ' || no_centro_poblado " +
                "               end no_centro_poblado " +
                "          from pntm_centro_poblado " +
                "         where es_centro_poblado = '1' " +
                "           and (de_departamento like ? or de_provincia like ? or de_distrito like ? or no_centro_poblado like ?) " +
                "           and (co_categoria = '01' or co_categoria = '04')) " +
                "        union all " +
                "        (select co_centro_poblado, " +
                "               case " +
                "                 when co_categoria = '01' or co_categoria = '04' then " +
                "                  de_ubigeo_inei " +
                "                 else " +
                "                  de_ubigeo_inei || ' / ' || no_centro_poblado " +
                "               end no_centro_poblado " +
                " " +
                "          from pntm_centro_poblado " +
                "         where es_centro_poblado = '1' " +
                "           and (de_departamento like ? or de_provincia like ? or de_distrito like ? or no_centro_poblado like ?) " +
                "           and (co_categoria <> '01' and  co_categoria <> '04'))" +
                ") t " +
                "         where rownum <= ?) " +
                " where fila >= ?";

        Object[] params = new Object[]{parteDeUbigeo, parteDeUbigeo, parteDeUbigeo, parteDeUbigeo, parteDeUbigeo, parteDeUbigeo, parteDeUbigeo, parteDeUbigeo, filaFin, filaIni};
        logger.debug(String.format("DAO '%s' con: '%s'", query, ArrayUtils.toString(params)));
        try {
            return this.jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CentroPoblado.class), params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer countBuscarUbigeoInei(String parteDeUbigeo) {
        parteDeUbigeo = parteDeUbigeo.trim().equals("") ? "" : parteDeUbigeo + "%";
        String query = "" +
                "select count(1) " +
                "  from pntm_centro_poblado " +
                " where es_centro_poblado = '1' " +
                "   and (de_departamento like ? or de_provincia like ? or de_distrito like ? or no_centro_poblado like ?)";
        Object[] params = new Object[]{parteDeUbigeo, parteDeUbigeo, parteDeUbigeo, parteDeUbigeo};
        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        return this.jdbcTemplate.queryForInt(query, params);
    }

    @Override
    public CentroPoblado getUbigeoInei(String coUbigeoInei) {
        String query = "" +
                "select cp.co_centro_poblado, " +
                "       case when cp.co_categoria='01' or cp.co_categoria='04' then cp.de_ubigeo_inei " +
                "       else cp.de_ubigeo_inei || ' / ' || cp.no_centro_poblado  end no_centro_poblado " +
                "  from pntm_centro_poblado cp" +
                " where cp.co_centro_poblado = ?";
        Object[] params = new Object[]{coUbigeoInei};
        logger.debug(String.format("DAO '%s' con: '%s'", query, ArrayUtils.toString(params)));
        try {
            return this.jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(CentroPoblado.class), params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * metodo que obtiene centro poblado de capital de un distrito
     *
     * @param coUbigeoInei
     * @return
     */
    @Override
    public CentroPoblado obtenerCentroPoblado(String coUbigeoInei) {
        String query = "" +
                "SELECT CO_CENTRO_POBLADO, CO_CCPP_INEI, CO_CCPP_RENIEC, NO_CENTRO_POBLADO, CO_UBIGEO_RENIEC, " +
                "CO_UBIGEO_INEI, CO_DISTRITO, DE_DISTRITO, CO_PROVINCIA, DE_PROVINCIA, CO_DEPARTAMENTO, " +
                "DE_DEPARTAMENTO, CO_CATEGORIA, NO_CATEGORIA, DE_UBIGEO_INEI, ES_CENTRO_POBLADO, CO_AREA_CCPP, " +
                "DE_AREA_CCPP, US_CREA_AUDI, FE_CREA_AUDI, US_MODI_AUDI, FE_MODI_AUDI \n" +
                "FROM PNTM_CENTRO_POBLADO\n" +
                "WHERE CO_CCPP_INEI='0001' AND ES_CENTRO_POBLADO='1' AND SUBSTR(CO_CENTRO_POBLADO, 1, 6)=?";
        Object[] params = new Object[]{coUbigeoInei};
        logger.debug(String.format("DAO '%s' con: '%s'", query, ArrayUtils.toString(params)));
        try {
            return this.jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(CentroPoblado.class), params);
        } catch (Exception e) {
            logger.error("Error:", e);
            return null;
        }
    }

    @Override
    public int countBuscarEjeVial(String parteDeVia, String coCentroPoblado) {
        parteDeVia = parteDeVia.trim().equals("") ? "" : "%" + parteDeVia.toUpperCase() + "%";
        String query = "" +
                "select count(1)\n" +
                "  from pntm_via t1 left join pntr_tipo_via t2 on t1.co_cat_via=t2.co_cat_via\n" +
                "                 where ((t2.de_cat_via || ' ' || t1.de_via) like ? or (t2.de_cat_via_det || ' ' || t1.de_via) like ?" +
                "               or (t2.de_cat_via1 || ' ' || t1.de_via) like ?  or (t2.de_cat_via2 || ' ' || t1.de_via) like ? " +
                "               or (t2.de_cat_via3 || ' ' || t1.de_via) like ? or (t2.de_cat_via4 || ' ' || t1.de_via) like ? )\n" +
                "                   AND co_centro_poblado = ? \n";
        Object[] params = new Object[]{parteDeVia, parteDeVia, parteDeVia, parteDeVia, parteDeVia, parteDeVia, coCentroPoblado};
        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        return this.jdbcTemplate.queryForInt(query, params);
    }

    @Override
    public List<EjeVial> buscarEjeVial(String parteDeVia, String coCentroPoblado, int filaIni, int filaFin) {
        parteDeVia = parteDeVia.trim().equals("") ? "" : "%" + parteDeVia.toUpperCase() + "%";
        String query = "" +
                "SELECT *\n" +
                "  FROM (SELECT ROWNUM FILA, T.*\n" +
                "          FROM (select t1.co_via,\n" +
                "                       t2.de_cat_via_det || ' ' || t1.de_via de_via,\n" +
                "                       t1.de_via_alt,\n" +
                "                       t1.co_centro_poblado,\n" +
                "                       t1.co_cat_via,\n" +
                "                       t1.es_via,\n" +
                "                       t2.de_cat_via_det,\n" +
                "                       t2.de_cat_via\n" +
                "                  from pntm_via t1\n" +
                "                  left join pntr_tipo_via t2\n" +
                "                    on t1.co_cat_via = t2.co_cat_via\n" +
                "                 where ((t2.de_cat_via || ' ' || t1.de_via) like ? or (t2.de_cat_via_det || ' ' || t1.de_via) like ?" +
                "               or (t2.de_cat_via1 || ' ' || t1.de_via) like ?  or (t2.de_cat_via2 || ' ' || t1.de_via) like ? " +
                "               or (t2.de_cat_via3 || ' ' || t1.de_via) like ? or (t2.de_cat_via4 || ' ' || t1.de_via) like ? )\n" +
                "                   AND co_centro_poblado = ?) T\n" +
                "         WHERE ROWNUM <= ?)\n" +
                " WHERE FILA >= ?\n";

        Object[] params = new Object[]{parteDeVia, parteDeVia, parteDeVia, parteDeVia, parteDeVia, parteDeVia, coCentroPoblado, filaFin, filaIni};
        logger.debug(String.format("DAO '%s' con: '%s'", query, ArrayUtils.toString(params)));
        try {
            return this.jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(EjeVial.class), params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EjeVial obtenerEjeVial(String coVia) {
        String query = "" +
                "select t1.co_via,\n" +
                "       t2.de_cat_via_det || ' ' || t1.de_via de_via,\n" +
                "       t1.de_via_alt,\n" +
                "       t1.co_centro_poblado,\n" +
                "       t1.co_cat_via,\n" +
                "       t1.es_via,\n" +
                "       t2.de_cat_via_det,\n" +
                "       t2.de_cat_via\n" +
                "  from pntm_via t1\n" +
                "  left join pntr_tipo_via t2\n" +
                "    on t1.co_cat_via = t2.co_cat_via\n" +
                " where t1.co_via = ?\n";

        Object[] params = new Object[]{coVia};
        logger.debug(String.format("DAO '%s' con: '%s'", query, ArrayUtils.toString(params)));
        try {
            return this.jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(EjeVial.class), params);
        } catch (Exception e) {
            logger.error("Error:", e);
            return null;
        }
    }

}
