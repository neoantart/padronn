package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.MovimientoPasswordDao;
import pe.gob.reniec.padronn.logic.dao.UsuarioExternoDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase UsuarioExternoDaoImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 27/05/13 09:52 AM
 */
@Repository
public class UsuarioExternoDaoImpl
        extends AbstractBaseDao
        implements UsuarioExternoDao {

    Logger logger = Logger.getLogger(getClass());

    private static String MESA_AYUDA ="3";
    @Autowired
    Usuario usuario;

    @Autowired
    MovimientoPasswordDao movimientoPasswordDao;

    @Override
    public Usuario getUsuario(String login) {

        String query = "" +
                "SELECT " +
                "UE.CO_USUARIO as login, " +
                "UE.AP_PRIMER as paterno, " +
                "UE.AP_SEGUNDO as materno, " +
                "UE.PRENOMBRES as nombres, " +
                "UE.DE_PASSWORD as password, " +
                "UE.ES_USUARIO as esUsuario," +
                "UE.FE_PASSWORD as fePassword " +
                "FROM " +
                "PNTM_USUARIO_EXTERNO UE " +
                "WHERE " +
                "CO_USUARIO=? ";

        Object[] params = new Object[]{login};
        try	{
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Usuario.class), params);

        } catch(EmptyResultDataAccessException ersdae) {
            log.error(ersdae.getMessage());
            return new Usuario();

        } catch(IncorrectResultSizeDataAccessException irsdae) {
            log.error(irsdae.getMessage());
            return null;
        }
    }

    @Override
    public Usuario getUsuario(String login, String password) {

        String query = "" +
                "SELECT " +
                "UE.CO_USUARIO as login, " +
                "UE.AP_PRIMER as paterno, " +
                "UE.AP_SEGUNDO as materno, " +
                "UE.PRENOMBRES as nombres, " +
                "UE.DE_PASSWORD as password, " +
                "UE.ES_USUARIO as esUsuario, " +
                "UE.FE_PASSWORD as fePassword, " +
                "NVL(UE.ES_EXPIRE,'0') as esExpire, " +
                "NVL(UE.ES_MINSA,'0') as esMinsa " +
                "FROM " +
                "PNTM_USUARIO_EXTERNO UE " +
                "WHERE " +
                "CO_USUARIO=? " +
                "AND DE_PASSWORD=? ";

        Object[] params = new Object[]{login, password};
        log.debug("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
        try	{
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Usuario.class), params);

        } catch(EmptyResultDataAccessException ersdae) {
            log.error(ersdae.getMessage());
            return null;

        } catch(IncorrectResultSizeDataAccessException irsdae) {
            log.error(irsdae.getMessage());
            return null;
        }
    }

    private List<String> listCoTiposEntidad(String coUsuario) {
        String query = "select co_tipo_entidad from pntm_usuario_entidad where co_usuario=?";
        try{
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(String.class), coUsuario);
        } catch (EmptyResultDataAccessException ee) {
            ee.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException ie) {
            ie.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Entidad> listUsuarioEntidad(String coUsuario) {
        String query = "" +
                "(select  " +
                "       e.co_entidad||'_'||e.co_tipo_entidad as coEntidad, " +
                "       e.de_entidad as deEntidad, " +
                "       e.co_ubigeo_inei as coUbigeoInei, " +
                "       e.co_tipo_entidad as coTipoEntidad " +
                "  from pntm_usuario_entidad ue " +
                "  join pntm_entidad e on to_char(e.co_entidad) = ue.co_entidad and ue.co_tipo_entidad=e.co_tipo_entidad and e.es_entidad='1' " +
                " where ue.co_usuario = ?) " +
                "union  " +
                "(select  " +
                "      to_char(e.co_est_salud)||'_'||? as coEntidad,        " +
                "      e.de_est_salud||' - '||e.ti_est_salud as deEntidad, " +
                "      e.co_ubigeo_inei as coUbigeoInei,  " +
                "      ? as coTipoEntidad  " +
                "from pntm_usuario_entidad ue " +
                "join pnvm_establecimiento_salud e on e.co_est_salud=ue.co_entidad " +
                "where ue.co_usuario = ?)  " +
                "union  " +
                "(select  " +
                "        to_char(d.co_disa)||'_'||? as coEntidad, " +
                "        d.de_disa as deEntidad, " +
                "        d.co_ubigeo_inei as coUbigeoInei, " +
                "        ? as coTipoEntidad " +
                "from pntm_usuario_entidad ue " +
                "join pnvm_disa d on d.co_disa=ue.co_entidad and ue.co_tipo_entidad=? " +
                "where ue.co_usuario = ?) " +
                "union " +
                "(select to_char(r.co_disa||r.co_red)||'_'||? as coEntidad, " +
                "        r.de_red as deEntidad, " +
                "        r.co_ubigeo_inei as coUbigeoInei, " +
                "        ? as coTipoEntidad " +
                "from pntm_usuario_entidad ue " +
                "join pnvm_red r on r.co_disa=substr(ue.co_entidad, 1, 2) and r.co_red=substr(ue.co_entidad, 3, 2) " +
                "where ue.co_usuario = ?) " +
                "union " +
                "(select to_char(m.co_disa||m.co_red||m.co_microred)||'_'||? as coEntidad, " +
                "        m.de_microred as deEntidad, " +
                "        m.co_ubigeo_inei as coUbigeoInei, " +
                "        ? as coTipoEntidad " +
                "from pntm_usuario_entidad ue " +
                "join pnvm_microred m on m.co_disa=substr(ue.co_entidad, 1, 2) and m.co_red=substr(ue.co_entidad, 3, 2) and m.co_microred=substr(ue.co_entidad, 5, 2) " +
                "where ue.co_usuario = ?) ";

        Object[] params = new Object[] {
                coUsuario,
                Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad(), coUsuario,
                Entidad.TipoEntidad.DISA.getCoTipoEntidad(), Entidad.TipoEntidad.DISA.getCoTipoEntidad(), Entidad.TipoEntidad.DISA.getCoTipoEntidad(), coUsuario,
                Entidad.TipoEntidad.RED.getCoTipoEntidad(), Entidad.TipoEntidad.RED.getCoTipoEntidad(), coUsuario,
                Entidad.TipoEntidad.MICRORED.getCoTipoEntidad(), Entidad.TipoEntidad.MICRORED.getCoTipoEntidad(), coUsuario
        };
        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try	{
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Entidad.class), params);
        } catch(EmptyResultDataAccessException ersdae) {
            log.error(ersdae.getMessage());
            return null;
        }
    }

    @Override
    public Usuario getUsuario(String coUsuario, String dePassword, String coEntidad, String coTipoEntidad) {
        String query = "SELECT U.CO_USUARIO as login, U.AP_PRIMER as paterno, U.AP_SEGUNDO as materno, U.PRENOMBRES as nombres, U.DE_PASSWORD as password, U.ES_USUARIO as esUsuario, NVL(U.ES_EXPIRE,'0') as esExpire, TO_CHAR(U.FE_PASSWORD,'yyyy-MM-dd') as fePassword, nvl(U.es_minsa,'0') as esMinsa, E.CO_ENTIDAD as coEntidad, E.CO_UBIGEO_INEI as coUbigeoInei, E.DE_ENTIDAD as deEntidad,E.CO_TIPO_ENTIDAD as coTipoEntidad FROM PNTM_USUARIO_EXTERNO U LEFT JOIN PNTM_USUARIO_ENTIDAD UE ON U.CO_USUARIO=UE.CO_USUARIO LEFT JOIN PNTM_ENTIDAD E ON E.CO_ENTIDAD=UE.CO_ENTIDAD and e.es_entidad='1' WHERE U.CO_USUARIO=? AND U.DE_PASSWORD=? AND E.CO_ENTIDAD=? AND E.CO_TIPO_ENTIDAD=?";
        Object[] params = new Object[]{coUsuario, dePassword, coEntidad, coTipoEntidad};
        if(coTipoEntidad.equals(Entidad.TipoEntidad.DISA.getCoTipoEntidad())) {
            query = "SELECT U.CO_USUARIO as login, U.AP_PRIMER as paterno, U.AP_SEGUNDO as materno, U.PRENOMBRES as nombres, U.DE_PASSWORD as password, U.ES_USUARIO as esUsuario, nvl(U.es_minsa,'0') as esMinsa, U.ES_EXPIRE as esExpire, TO_CHAR(U.FE_PASSWORD,'yyyy-MM-dd') as fePassword, D.CO_DISA as coEntidad, d.co_ubigeo_inei as coUbigeoInei, D.DE_DISA as deEntidad,? as coTipoEntidad FROM PNTM_USUARIO_EXTERNO U LEFT JOIN PNTM_USUARIO_ENTIDAD UE ON U.CO_USUARIO=UE.CO_USUARIO LEFT JOIN pnvm_disa D ON D.CO_DISA=UE.CO_ENTIDAD WHERE U.CO_USUARIO=? AND U.DE_PASSWORD=? AND D.CO_DISA=? ";
            params = new Object[]{coTipoEntidad, coUsuario, dePassword, coEntidad, };
        } else if(coTipoEntidad.equals(Entidad.TipoEntidad.RED.getCoTipoEntidad())) {
            query = "SELECT U.CO_USUARIO as login, U.AP_PRIMER as paterno, U.AP_SEGUNDO as materno, U.PRENOMBRES as nombres, U.DE_PASSWORD as password, nvl(U.es_minsa,'0') as esMinsa, U.ES_USUARIO as esUsuario, U.ES_EXPIRE as esExpire, TO_CHAR(U.FE_PASSWORD,'yyyy-MM-dd') as fePassword, R.CO_DISA||R.CO_RED as coEntidad, r.co_ubigeo_inei as coUbigeoInei, R.DE_RED as deEntidad,? as coTipoEntidad FROM PNTM_USUARIO_EXTERNO U LEFT JOIN PNTM_USUARIO_ENTIDAD UE ON U.CO_USUARIO=UE.CO_USUARIO LEFT JOIN pnvm_red R ON R.CO_DISA=SUBSTR(UE.CO_ENTIDAD, 1, 2) AND R.CO_RED=SUBSTR(UE.CO_ENTIDAD, 3, 2) WHERE U.CO_USUARIO=? AND U.DE_PASSWORD=? AND R.CO_DISA=SUBSTR(?,1,2) AND R.CO_RED=SUBSTR(?,3,2) ";

            params = new Object[]{coTipoEntidad, coUsuario, dePassword, coEntidad, coEntidad, };
        } else if(coTipoEntidad.equals(Entidad.TipoEntidad.MICRORED.getCoTipoEntidad())) {
            query = "SELECT U.CO_USUARIO as login, U.AP_PRIMER as paterno, U.AP_SEGUNDO as materno, U.PRENOMBRES as nombres, U.DE_PASSWORD as password, U.ES_USUARIO as esUsuario, U.ES_EXPIRE as esExpire, TO_CHAR(U.FE_PASSWORD,'yyyy-MM-dd') as fePassword, M.CO_DISA||M.CO_RED||M.CO_MICRORED as coEntidad, m.co_ubigeo_inei as coUbigeoInei, nvl(U.es_minsa,'0') as esMinsa, M.DE_MICRORED as deEntidad, ? as coTipoEntidad FROM PNTM_USUARIO_EXTERNO U LEFT JOIN PNTM_USUARIO_ENTIDAD UE ON U.CO_USUARIO=UE.CO_USUARIO LEFT JOIN pnvm_microred M ON M.CO_DISA=SUBSTR(UE.CO_ENTIDAD, 1, 2) AND M.CO_RED=SUBSTR(UE.CO_ENTIDAD, 3, 2) AND M.CO_MICRORED=SUBSTR(UE.CO_ENTIDAD, 5, 2) WHERE U.CO_USUARIO=? AND U.DE_PASSWORD=? AND M.CO_DISA=SUBSTR(?,1,2) AND M.CO_RED=SUBSTR(?,3,2) AND M.CO_MICRORED=SUBSTR(?,5,2) ";

            params = new Object[]{coTipoEntidad, coUsuario, dePassword, coEntidad, coEntidad, coEntidad, };
        } else if(coTipoEntidad.equals(Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad())) {
            query = "select u.co_usuario as login, u.ap_primer as paterno, u.ap_segundo as materno, u.prenombres as nombres, u.de_password as password, u.es_usuario as esUsuario, nvl(u.es_minsa,'0') as esMinsa, U.ES_EXPIRE as esExpire, TO_CHAR(U.FE_PASSWORD,'yyyy-MM-dd') as fePassword, e.co_est_salud as coEntidad,e.co_ubigeo_inei as coUbigeoInei, e.de_est_salud as deEntidad, ? as coTipoEntidad from pntm_usuario_externo u left join pntm_usuario_entidad ue on u.co_usuario=ue.co_usuario left join pnvm_establecimiento_salud e on e.co_est_salud=ue.co_entidad where u.co_usuario=? and u.de_password=? and e.co_est_salud=? ";
            params = new Object[]{coTipoEntidad, coUsuario, dePassword, coEntidad, };
        }
        log.debug("DAO query='" + query + "' params='"+ ArrayUtils.toString(params)+"'");
        try	{
            return jdbcTemplate.queryForObject(query, ParameterizedBeanPropertyRowMapper.newInstance(Usuario.class), params);
        } catch(EmptyResultDataAccessException ersdae) {
            log.error(ersdae.getMessage());
            return null;
        } catch(IncorrectResultSizeDataAccessException irsdae) {
            log.error(irsdae.getMessage());
            return null;
        }
    }

    @Override
    public void updateUsuarioPassword(CambioCredencial cambioCredencial) {
        String query = "" +
                "UPDATE " +
                "PNTM_USUARIO_EXTERNO " +
                "SET " +
                "DE_PASSWORD=?," +
                "FE_PASSWORD=SYSDATE " +
                "WHERE " +
                "CO_USUARIO=? ";
        Object[] params = new Object[]{cambioCredencial.getDePasswordNuevo(), cambioCredencial.getCoUsuario()};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            jdbcTemplate.update(query, params);
            MovimientoPassword movimientoPassword = MovimientoPassword.fromCambioCredencial(cambioCredencial);
            movimientoPasswordDao.insertMovimientoPw(movimientoPassword);
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.error("no se puede cambiar la contraseña");
        }
    }

    @Override
    public List<String> getAuthorities(String coUsuario) {
        List<String> grupos = getGruposUsuario(coUsuario);
        List<String> result = new ArrayList<String>();
        if(grupos.size() > 0) {
            for(String grupo: grupos){
                String query = "SELECT CO_GRUPO AS coGrupo, DE_PERMISO AS dePermiso, NO_MODULO AS noModulo FROM PNTM_GRUPO_PERMISO WHERE CO_GRUPO=? AND ES_PERMISO='1' AND ES_GRUPO_PERMISO<>'9' ";
                logger.debug(String.format("DAO '%s' con '%s'", query, grupo));
                List<GrupoPermiso> grupoPermisoList;
                try	{
                    grupoPermisoList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(GrupoPermiso.class), grupo);
                } catch(EmptyResultDataAccessException ersdae) {
                    log.error(ersdae.getMessage());
                    grupoPermisoList = null;

                } catch(IncorrectResultSizeDataAccessException irsdae) {
                    log.error(irsdae.getMessage());
                    grupoPermisoList = null;
                }
                if( grupoPermisoList != null ){
                    for(GrupoPermiso grupoPermiso: grupoPermisoList){
                        if(!result.contains(grupoPermiso.getDePermiso())){
                            result.add(grupoPermiso.getDePermiso());
                        }
                    }
                }
            }
            return result;
        }
        return result;
    }

    @Override
    public int countAll() {
        String query = "select count(1) from PNTM_USUARIO_EXTERNO where es_usuario='1'";
        return jdbcTemplate.queryForInt(query);
    }

    private static Object[] concatenarArrays(Object[] array1, Object[] array2){
        Object[] res = new Object[ array1.length + array2.length ];
        System.arraycopy( array1, 0, res, 0, array1.length );
        System.arraycopy( array2, 0, res, array1.length, array2.length );
        return res;
    }

    @Override
    public List<Entidad> listarEntidades(String parteNoEntidad, List<String> tipoEntidades) {
        String query="";
        String query1="";
        String query2="";
        String query3="";

        Object[] params = new Object[]{ };
        Object[] params1 = new Object[]{ };
        Object[] params2 = new Object[]{ };
        Object[] params3 = new Object[]{ };


        parteNoEntidad = "%" + parteNoEntidad.toUpperCase() + "%";
        if (tipoEntidades.contains("1")) {
            params1 = new Object[]{ parteNoEntidad, parteNoEntidad};

            query1 = " (SELECT TO_CHAR(e.co_entidad)||'_'|| e.co_tipo_entidad AS coEntidad, " +
                    " '' as coRenaes," +
                    " TO_CHAR(e.de_entidad_larga) AS deEntidad, " +
                    " UPPER((u.de_departamento || ', ' || u.de_provincia || ',' || u.de_distrito)) AS deUbigeoInei, " +
                    " u.co_ubigeo_inei AS coUbigeoInei, " +
                    " e.co_tipo_entidad AS coTipoEntidad  " +
                    " FROM PNTM_ENTIDAD e  " +
                    " LEFT JOIN pntm_ubigeo_inei u ON e.co_ubigeo_inei=u.co_ubigeo_inei  " +
                    " WHERE de_entidad LIKE 'MUN%' AND  (e.de_entidad_larga LIKE ?  OR e.de_entidad LIKE ?) AND u.es_ubigeo='1' AND TRIM(e.es_entidad)='1')";

        } if(tipoEntidades.contains("8")) {
            params2 = new Object[]{ Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad(),
                                   Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad(),
                                   parteNoEntidad };

            query2 = " (SELECT TO_CHAR(co_est_salud)||'_'||? as coEntidad, " +
                     " co_est_salud as coRenaes," +
                     " ti_est_salud || ' - ' || de_est_salud as deEntidad, " +
                     " (de_departamento || ', ' || de_provincia || ',' || de_distrito) as deUbigeoInei, " +
                     " co_ubigeo_inei as coUbigeoInei, ? as coTipoEntidad " +
                     " from pnvm_establecimiento_salud " +
                     " where de_est_salud like ?)";

        } if(tipoEntidades.contains("9")) {//RENIEC,MINSA,MEF,ETC.
            params3 = new Object[]{ parteNoEntidad, parteNoEntidad};

            query3 =  "(SELECT TO_CHAR(e.CO_ENTIDAD)||'_'|| e.CO_TIPO_ENTIDAD AS coEntidad, " +
                      " '' as coRenaes," +
                     " TO_CHAR(e.DE_ENTIDAD_LARGA) AS deEntidad,  " +
                     " UPPER((u.de_departamento || ', ' || u.de_provincia || ',' || u.de_distrito)) AS deUbigeoInei,   " +
                     " u.co_ubigeo_inei AS coUbigeoInei, " +
                     " e.CO_TIPO_ENTIDAD AS coTipoEntidad   " +
                     " FROM PNTM_ENTIDAD e  " +
                     " LEFT JOIN pntm_ubigeo_inei u ON e.co_ubigeo_inei=u.co_ubigeo_inei  " +
                     " WHERE de_entidad NOT LIKE 'MUN%' AND (e.DE_ENTIDAD_LARGA LIKE ?  OR e.de_entidad LIKE ?) " +
                     " AND u.es_ubigeo='1' AND TRIM(e.es_entidad)='1')";
        }
        if(tipoEntidades.size()==1) {
           if (!query1.equals("")) {
               params = params1;
               query=query1;
           }else if (!query2.equals("")){
               params = params2;
               query=query2;
           }else if (!query3.equals("")){
               params = params3;
               query=query3;
           }
        }
        if(tipoEntidades.size()==2) {
            if (!query1.equals("") && !query2.equals("")) {
                params = concatenarArrays(params1,params2);
                query = query1 + " UNION " + query2;
            }
            if (!query2.equals("") && !query3.equals("")) {
                params = concatenarArrays(params2,params3);
                query = query2 + " UNION " + query3;
            }
            if (!query1.equals("") && !query3.equals("")) {
                params = concatenarArrays(params1,params3);
                query = query1 + " UNION " + query3;
            }
        }
        if(tipoEntidades.size()==3) {
            params = concatenarArrays(concatenarArrays(params1,params2), params3);
            query = query1 + " UNION " + query2 + " UNION "+ query3;
        }

        try{
            logger.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Entidad.class), params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Objeto> getTipoEntidades() {
        String query = " select co_dominio as id, de_dom_detalle as text " +
                       " from pntr_dominios " +
                       " where no_dom ='CO_TIPO_ENTIDAD' and es_dom='1'";

        log.info("DAO: getGrupos" +  query);
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Objeto.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<Entidad> buscarEntidad(String parteNoEntidad) {
        String query = "" +
                "SELECT * " +
                "  FROM ((SELECT TO_CHAR(e.CO_ENTIDAD)||'_'|| e.CO_TIPO_ENTIDAD AS coEntidad, " +
                "                TO_CHAR(e.DE_ENTIDAD_LARGA) AS deEntidad, " +
                "                upper((u.de_departamento || ', ' || u.de_provincia || ',' || " +
                "                      u.de_distrito)) as deUbigeoInei, " +
                "                u.co_ubigeo_inei as coUbigeoInei, " +
                "               e.CO_TIPO_ENTIDAD as coTipoEntidad "+
                "           FROM PNTM_ENTIDAD e " +
//                "           left join pntm_ubigeo_inei u on u.co_ubigeo_inei = e.co_ubigeo_inei and u.es_ubigeo='1' and e.es_entidad='1' " +
                "           LEFT JOIN pntm_ubigeo_inei u on e.co_ubigeo_inei=u.co_ubigeo_inei " +
                "          WHERE (e.DE_ENTIDAD_LARGA LIKE ? " +
                "             or e.de_entidad like ?) and u.es_ubigeo='1' and trim(e.es_entidad)='1') UNION " +

                "        (select TO_CHAR(co_est_salud)||'_'||? as coEntidad, " +
                "                ti_est_salud || ' - ' || de_est_salud as deEntidad, " +
                "                (de_departamento || ', ' || de_provincia || ',' || " +
                "                de_distrito) as deUbigeoInei, " +
                "                co_ubigeo_inei as coUbigeoInei, " +
                "               ? as coTipoEntidad "+
                "           from pnvm_establecimiento_salud " +
                "          where de_est_salud like ?) union " +

                "        (select to_char(co_disa)||'_'||? as coEntidad, " +
                "                de_disa as deEntidad, " +
                "                null as deUbigeoInei, " +
                "                null as coUbigeoInei, " +
                "               ? as coTipoEntidad "+
                "           from pnvm_disa " +
                "          where de_disa like ?) union " +
                "        (select to_char(co_disa||co_red)||'_'||? as coEntidad, " +
                "                de_red as deEntidad, " +
                "                null as deUbigeoInei, " +
                "                null as coUbigeoInei, " +
                "                ? as coTipoEntidad "+
                "           from pnvm_red " +
                "          where de_red like ?) union " +
                "        (select to_char(co_disa||co_red||co_microred)||'_'||? as coEntidad, " +
                "                de_microred as deEntidad, " +
                "                null as deUbigeoInei, " +
                "                null as coUbigeoInei, " +
                "                ? as coTipoEntidad "+
                "           from pnvm_microred " +
                "          where de_microred like ?)) " +
                "where rownum < 30 " +
                " ORDER BY coUbigeoInei";
        try{
            parteNoEntidad = "%" + parteNoEntidad.toUpperCase() + "%";

            Object[] params = new Object[]{parteNoEntidad, parteNoEntidad,
                                           Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad(), parteNoEntidad,
                                           Entidad.TipoEntidad.DISA.getCoTipoEntidad(), Entidad.TipoEntidad.DISA.getCoTipoEntidad(), parteNoEntidad,
                                           Entidad.TipoEntidad.RED.getCoTipoEntidad(), Entidad.TipoEntidad.RED.getCoTipoEntidad(), parteNoEntidad,
                                           Entidad.TipoEntidad.MICRORED.getCoTipoEntidad(), Entidad.TipoEntidad.MICRORED.getCoTipoEntidad(), parteNoEntidad};

            logger.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Entidad.class), params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Grupo getGrupo(String coGrupo) {
        String query = "SELECT CO_GRUPO AS coGrupo, DE_GRUPO AS deGrupo FROM PNTM_GRUPO WHERE CO_GRUPO=? AND ES_GRUPO<>'9'";
        log.info("DAO: getGrupo" +  query + " con: " + coGrupo);
        try{
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Grupo.class), coGrupo);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getCoTipoEntidades(List<String> coTipoEntidades){
        List<String> result = new ArrayList<String>();
        if (coTipoEntidades.size()>0){
            for (int i =0 ; i< coTipoEntidades.size(); i++) {
                if(coTipoEntidades.get(i).equals("2") || coTipoEntidades.get(i).equals("3") || coTipoEntidades.get(i).equals("4")|| coTipoEntidades.get(i).equals("5")
                    || coTipoEntidades.get(i).equals("6") || coTipoEntidades.get(i).equals("7")){

                    if(!result.contains("9"))
                        result.add("9");
                }
                else {
                    if (!result.contains(coTipoEntidades.get(i))) {
                        result.add(coTipoEntidades.get(i));
                    }
                }
            }
        }
        return result;
    }


    @Override
    public String getDeEntidades(List<String> coEntidadList, List<String> tiposEntidad) {
        String result = "";
        if(coEntidadList.size() > 0) {
            for(int i=0; i<coEntidadList.size(); i++){
                Entidad entidad = getEntidad(coEntidadList.get(i), tiposEntidad.get(i));
                if(entidad!=null && entidad.getDeEntidad() != null)
                    result += entidad.getDeEntidad() + ", ";
            }
            /*for(String coEntidad: coEntidadList) {
                Entidad entidad = getEntidad(coEntidad);
                result += entidad.getDeEntidad() + ", ";
            }*/
        }
        return result;
    }

    @Override
    public Entidad getEntidad(String coEntidad, String coTipoEntidad) {

        String query = "" +
                "SELECT e.CO_ENTIDAD||'_'|| e.co_tipo_entidad AS coEntidad,  " +
                "                e.DE_ENTIDAD_LARGA AS deEntidad,  " +
                "        (u.de_departamento || ', ' ||  u.de_provincia || ',' || u.de_distrito ) as deUbigeoInei," +
                "        e.co_ubigeo_inei as coUbigeoInei," +
                "        e.co_tipo_entidad as coTipoEntidad " +
                "        FROM PNTM_ENTIDAD e  " +
                "        left join pntm_ubigeo_inei u on u.co_ubigeo_inei=e.co_ubigeo_inei and u.es_ubigeo='1' and e.es_entidad='1' " +
                "WHERE e.CO_ENTIDAD=? and u.es_ubigeo='1' ";
        Object[] params = new Object[] {coEntidad};
        if(coTipoEntidad.equals(Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad())) {
            query = "" +
                    "select TO_CHAR(co_est_salud)||'_'||? as coEntidad,  " +
                    "                ti_est_salud || ' - ' || de_est_salud as deEntidad,  " +
                    "                (de_departamento || ', ' || de_provincia || ',' ||  " +
                    "                de_distrito) as deUbigeoInei,  " +
                    "                co_ubigeo_inei as coUbigeoInei,  " +
                    "                ? as coTipoEntidad  " +
                    "           from pnvm_establecimiento_salud" +
                    "          where co_est_salud=?";
            params = new Object[]{coTipoEntidad, coTipoEntidad, coEntidad};
        } else if(coTipoEntidad.equals(Entidad.TipoEntidad.DISA.getCoTipoEntidad())) {
            query = "" +
                    "select to_char(co_disa)||'_'||? as coEntidad, " +
                    "                de_disa as deEntidad, " +
                    "                null as deUbigeoInei, " +
                    "                null as coUbigeoInei, " +
                    "                ? as coTipoEntidad " +
                    "           from pnvm_disa " +
                    "          where co_disa=?";
            params = new Object[]{coTipoEntidad, coTipoEntidad, coEntidad};
        } else if(coTipoEntidad.equals(Entidad.TipoEntidad.RED.getCoTipoEntidad())) {
            query = "" +
                    "select to_char(co_disa||co_red)||'_'||? as coEntidad, " +
                    "                de_red as deEntidad, " +
                    "                null as deUbigeoInei, " +
                    "                null as coUbigeoInei, " +
                    "                ? as coTipoEntidad " +
                    "           from pnvm_red " +
                    "          where co_disa=substr(?,1,2) and co_red=substr(?,3,2)";
            params = new Object[]{coTipoEntidad, coTipoEntidad, coEntidad, coEntidad};
        } else if(coTipoEntidad.equals(Entidad.TipoEntidad.MICRORED.getCoTipoEntidad())) {
            query = "" +
                    "select to_char(co_disa||co_red||co_microred)||'_'||? as coEntidad, " +
                    "                de_microred as deEntidad, " +
                    "                null as deUbigeoInei, " +
                    "                null as coUbigeoInei, " +
                    "                ? as coTipoEntidad " +
                    "           from pnvm_microred " +
                    "          where co_disa=substr(?,1,2) and co_red=substr(?,3,2) and co_microred=substr(?,5,2)";
            params = new Object[]{coTipoEntidad, coTipoEntidad, coEntidad, coEntidad, coEntidad};
        }
        try {
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Entidad.class), params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String getDeGrupos(List<String> coGrupoList) {
        String result = "";
        if(coGrupoList.size() > 0) {
            for(String coGrupo: coGrupoList){
                Grupo grupo = getGrupo(coGrupo);
                result += grupo.getDeGrupo() + ", ";
            }
        }
        return result;
    }

    @Override
    public boolean insert(UsuarioExterno usuarioExterno) {
        String query = "" +
                "INSERT " +
                "INTO PNTM_USUARIO_EXTERNO " +
                "  ( " +
                "    CO_USUARIO, " +
                "    AP_PRIMER, " +
                "    AP_SEGUNDO, " +
                "    PRENOMBRES, " +
                "    DE_PASSWORD, " +
                "    US_CREA_AUDI, " +
                "    FE_CREA_AUDI, " +
                "    US_MODI_AUDI, " +
                "    FE_MODI_AUDI, " +
                "    ES_USUARIO," +
                "    DE_EMAIL, " +
                "    NU_TELEFONO  " +
                "  ) " +
                "  VALUES " +
                "  (?,?,?,?,?,?,SYSDATE,?,SYSDATE,?,?,?)";

        String coUsuarioExterno = usuarioExterno.getCoUsuario();
        log.info("DAO '" + query + "' con '" + usuarioExterno + "' por ejecutar");
        if(coUsuarioExterno == null ) {
            return false;
        }
        else {
            int resultadoPrincipal = this.jdbcTemplate.update(query,
                    usuarioExterno.getCoUsuario(),
                    usuarioExterno.getApPrimer(),
                    usuarioExterno.getApSegundo(),
                    usuarioExterno.getPrenombres(),
                    usuarioExterno.getCoUsuario(), // reemplazado por dePassword()
                    usuarioExterno.getUsCreaAudi(),
                    usuarioExterno.getUsCreaAudi(),
                    usuarioExterno.getEsUsuario(),
                    usuarioExterno.getDeEmail(),
                    usuarioExterno.getNuTelefono()
            );
            int resultadoUsuarioEntidades = 0;
            int resultadoUsuarioGrupos = 0;
            List<String> usuarioEntidades = usuarioExterno.getEntidades();
            List<String> usuarioGrupos = usuarioExterno.getGrupos();
//            List<String> usuarioTiposEntidad = usuarioExterno.getTiposEntidad();
            if( usuarioEntidades != null && usuarioEntidades.size()>0 ) {
                for(int i=0; i<usuarioEntidades.size(); i++){
                    String[] split = usuarioEntidades.get(i).split("_");
                    insertUsuarioEntidad(usuarioExterno.getCoUsuario(), split[0], split[1]);
                }
            }
            if(usuarioGrupos != null && usuarioGrupos.size() > 0) {
                for(String coGrupo: usuarioGrupos)
                    insertUsuarioGrupo(coGrupo, usuarioExterno.getCoUsuario());
            }
            MovimientoPassword movimientoPassword = MovimientoPassword.fromUsuarioExterno(usuarioExterno);
            movimientoPassword.setDePassword(usuarioExterno.getCoUsuario()); //reemplazado por dePassword()
            movimientoPasswordDao.insertMovimientoPw(movimientoPassword);
            insertHistorico(usuarioExterno);
            return resultadoPrincipal == 1;
        }
    }

    public int generarNumeroSecUsuarioExterno(String coUsuario) {
        String query = "" +
                "SELECT " +
                        "NVL(MAX(NU_SEC),0)+1 " +
                        "FROM " +
                        "PNTH_USUARIO_EXTERNO " +
                        "WHERE " +
                        "CO_USUARIO=? ";

        log.info("DAO '" + query + "' con '" + coUsuario + "' por ejecutar");
        return jdbcTemplate.queryForInt(query, coUsuario);
    }

    /**
     * Metodo helper, para registrar historicos
     * @param usuarioExterno
     * @return
     */
    public boolean insertHistorico(UsuarioExterno usuarioExterno){
        usuarioExterno.setNuSec(generarNumeroSecUsuarioExterno(usuarioExterno.getCoUsuario()));
        String query = "" +
                "INSERT " +
                "INTO PNTH_USUARIO_EXTERNO " +
                "  ( " +
                "    CO_USUARIO, " +
                "    AP_PRIMER, " +
                "    AP_SEGUNDO, " +
                "    PRENOMBRES, " +
                "    DE_PASSWORD, " +
                "    US_CREA_AUDI, " +
                "    FE_CREA_AUDI, " +
                "    US_MODI_AUDI, " +
                "    FE_MODI_AUDI, " +
                "    ES_USUARIO," +
                "    DE_EMAIL, " +
                "    NU_TELEFONO,  " +
                "    NU_SEC " +
                "  ) " +
                "  VALUES " +
                "  (?,?,?,?,?,?,SYSDATE,?,SYSDATE,?,?,?, ?) ";

        String coUsuarioExterno = usuarioExterno.getCoUsuario();

        if(coUsuarioExterno == null ) {
            return false;
        }
        else {
            int resultadoPrincipal = this.jdbcTemplate.update(query,
                    usuarioExterno.getCoUsuario(),
                    usuarioExterno.getApPrimer(),
                    usuarioExterno.getApSegundo(),
                    usuarioExterno.getPrenombres(),
                    usuarioExterno.getDePassword(),
                    usuarioExterno.getUsCreaAudi(),
                    /*usuario.getLogin(),*/
                    usuarioExterno.getUsModiAudi(),
                    usuarioExterno.getEsUsuario(),
                    usuarioExterno.getDeEmail(),
                    usuarioExterno.getNuTelefono(),
                    usuarioExterno.getNuSec()
            );
            int resultadoUsuarioEntidades = 0;
            int resultadoUsuarioGrupos = 0;
            List<String> usuarioEntidades = usuarioExterno.getEntidades();
            List<String> usuarioGrupos = usuarioExterno.getGrupos();
            /*if( usuarioEntidades != null && usuarioEntidades.size()>0 ) {
                for(String coEntidad2: usuarioEntidades)
                    insertUsuarioEntidadHistorico(usuarioExterno.getCoUsuario(), coEntidad2, usuarioExterno.getNuSec());
            }*/
            if(usuarioGrupos != null && usuarioGrupos.size() > 0) {
                for(String coGrupo: usuarioGrupos)
                    insertUsuarioGrupoHistorico(coGrupo, usuarioExterno.getCoUsuario(), usuarioExterno.getNuSec());
            }
            return resultadoPrincipal == 1;
        }
    }
    /***
     * Metodo helper, para registrar usuario historico
     * @param coUsuario
     * @param coEntidad
     * @return
     */
    public boolean insertUsuarioEntidadHistorico(String coUsuario, String coEntidad, int nuSec){
        String query  = "" +
                "INSERT INTO PNTH_USUARIO_ENTIDAD " +
                "  (CO_USUARIO, CO_ENTIDAD, NU_SEC " +
                "  ) VALUES " +
                "  (?, ?, ? " +
                "  )";
        int resultInsert = jdbcTemplate.update(query, coUsuario, coEntidad, nuSec);
        return resultInsert == 1;
    }

    /**
     * Metodo helper, para registrar usuario historico
     * @param coGrupo
     * @param coUsuario
     * @return
     */
    public boolean insertUsuarioGrupoHistorico(String coGrupo, String coUsuario, int nuSec) {
        String query = "" +
                "INSERT INTO PNTH_GRUPO_USUARIO " +
                "  (CO_GRUPO, CO_USUARIO, NU_SEC" +
                "  ) VALUES " +
                "  (?,?,? " +
                "  )";
        try {
            int resultInsert = jdbcTemplate.update(query, coGrupo, coUsuario, nuSec);
            return resultInsert == 1;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return true;
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public boolean update(UsuarioExterno usuarioExterno) {
        logger.info("------------------------- inicio transaccion ----------------------------");
        UsuarioExterno usuarioExternoAnt = getUsuarioExterno(usuarioExterno.getCoUsuario());
        String dePassword = "";
        if(usuarioExterno.getDePassword()!=null && !(usuarioExterno.getDePassword().trim()).isEmpty())
            dePassword = "DE_PASSWORD=?, ";
        String query = "" +
                "UPDATE PNTM_USUARIO_EXTERNO " +
                "SET AP_PRIMER=?, " +
                    "AP_SEGUNDO=?, " +
                    "PRENOMBRES=?, " +
                    "NU_TELEFONO=?, "+
                    dePassword +
                    "ES_USUARIO=?," +
                    "DE_EMAIL=?," +
                    "DE_OBSERVACION=?,"+
                    "US_MODI_AUDI=?," +
                    "FE_MODI_AUDI=sysdate " +
                "WHERE CO_USUARIO = ?";

        int resultado;
        Object params[];
        if(usuarioExterno.getDePassword()!=null && !(usuarioExterno.getDePassword().trim()).isEmpty()) {
            params = new Object[]{
                        usuarioExterno.getApPrimer(),
                        usuarioExterno.getApSegundo(),
                        usuarioExterno.getPrenombres(),
                        usuarioExterno.getNuTelefono(),
                        usuarioExterno.getDePassword(),
                        usuarioExterno.getEsUsuario(),
                        usuarioExterno.getDeEmail(),
                        usuarioExterno.getUsModiAudi(),
                        usuarioExternoAnt.getCoUsuario()
            };
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            resultado = this.jdbcTemplate.update(query,params);

            MovimientoPassword movimientoPassword = MovimientoPassword.fromUsuarioExterno(usuarioExterno);
            movimientoPasswordDao.insertMovimientoPw(movimientoPassword);
        }
        else{
            params = new Object[]{
                usuarioExterno.getApPrimer(),
                usuarioExterno.getApSegundo(),
                usuarioExterno.getPrenombres(),
                usuarioExterno.getNuTelefono(),
                usuarioExterno.getEsUsuario(),
                usuarioExterno.getDeEmail(),
                usuarioExterno.getDeObservacion(),
                usuarioExterno.getUsModiAudi(),
                usuarioExternoAnt.getCoUsuario()
            };
            logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            resultado = this.jdbcTemplate.update(query, params);
        }

        List<String> usuarioEntidades = usuarioExterno.getEntidades();
        List<String> usuarioGrupos = usuarioExterno.getGrupos();

        logger.debug("usuarioEntidades: " + usuarioEntidades);
        if( usuarioEntidades!=null && usuarioEntidades.size()>0 ) {
            String queryDeleteEntidades = "DELETE FROM PNTM_USUARIO_ENTIDAD WHERE CO_USUARIO=?";
            logger.debug(String .format("DAO '%s' con '%s'", queryDeleteEntidades, usuarioExterno.getCoUsuario()));
            this.jdbcTemplate.update(queryDeleteEntidades, usuarioExterno.getCoUsuario());
            logger.debug("usuarioEntidades" + usuarioEntidades);
            for(int i=0; i<usuarioEntidades.size(); i++) {

                //try{
                    String[] split = usuarioEntidades.get(i).split("_");
                    insertUsuarioEntidad(usuarioExterno.getCoUsuario(), split[0], split[1]);
//                } catch (IndexOutOfBoundsException e) {
//                    continue;
//                }
            }
        }
        if( usuarioGrupos!=null && usuarioGrupos.size() > 0){
            String queryDeleteGrupos = "DELETE FROM PNTM_GRUPO_USUARIO WHERE CO_USUARIO=?";
            logger.debug(String .format("DAO '%s' con '%s'", queryDeleteGrupos, usuarioExterno.getCoUsuario()));
            this.jdbcTemplate.update(queryDeleteGrupos, usuarioExterno.getCoUsuario());
            for(String coGrupo: usuarioGrupos)
                insertUsuarioGrupo(coGrupo, usuarioExterno.getCoUsuario());
        }
        insertHistorico(usuarioExternoAnt);
        logger.info("------------------------- fin transaccion ----------------------------");
        return resultado == 1;
    }

    @Override
    public List<UsuarioExterno> getAll() {
        String query = "" +
                "SELECT CO_USUARIO AS coUsuario, " +
                "  AP_PRIMER AS apPrimer, " +
                "  AP_SEGUNDO AS apSegundo, " +
                "  PRENOMBRES AS preNombres, " +
                "  DE_PASSWORD AS dePassword, " +
                "  US_CREA_AUDI AS usCreaAudi, " +
                "  FE_CREA_AUDI AS feCreaAudi, " +
                "  US_MODI_AUDI AS usModiAudi, " +
                "  FE_MODI_AUDI AS deModiAudi, " +
                "  ES_USUARIO AS esUsuario, " +
                "  DE_EMAIL AS deEmail " +
                "FROM PNTM_USUARIO_EXTERNO ";

        //Object[] params = new Object[]{};
//        log.info("DAO '" + query + "'por ejecutar");
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class));
    }

    @Override
    public List<UsuarioExterno> getAll(int filaIni, int filaFin) {
        String query = "" +
                "SELECT * " +
                "FROM " +
                "  (SELECT t.*, " +
                "    rownum rn " +
                "  FROM " +
                "    (SELECT CO_USUARIO AS coUsuario, " +
                "      AP_PRIMER        AS apPrimer, " +
                "      AP_SEGUNDO       AS apSegundo, " +
                "      PRENOMBRES       AS preNombres, " +
                "      DE_PASSWORD      AS dePassword, " +
                "      US_CREA_AUDI     AS usCreaAudi, " +
                "      FE_CREA_AUDI     AS feCreaAudi, " +
                "      US_MODI_AUDI     AS usModiAudi, " +
                "      FE_MODI_AUDI     AS deModiAudi, " +
                "      ES_USUARIO       AS esUsuario, " +
                "      DE_EMAIL         AS deEmail " +
                "    FROM PNTM_USUARIO_EXTERNO " +
                "    WHERE ES_USUARIO='1' " +
                "    ) t " +
                "  ) r " +
                "WHERE r.rn BETWEEN ? AND ?";

//        log.info("DAO '" + query + "' con por ejecutar");
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class), filaIni, filaFin);
    }

    @Override
    public List<String> getPermisoModulo() {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getGrupoUsuario(String coUsuario) {
        String query = "SELECT CO_GRUPO AS coGrupo FROM PNTM_GRUPO_USUARIO WHERE CO_USUARIO=?";

        try{
            return jdbcTemplate.queryForInt(query, coUsuario);
        }
        catch(EmptyResultDataAccessException e){
            e.printStackTrace();
            return 0;
        }
        catch(IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public UsuarioExterno getUsuarioExterno(String coUsuario) {
        String query = "" +
                "SELECT u1.CO_USUARIO AS coUsuario,   " +
                "  u1.AP_PRIMER AS apPrimer,   " +
                "  u1.AP_SEGUNDO AS apSegundo,   " +
                "  u1.PRENOMBRES AS preNombres,   " +
                "  u1.US_CREA_AUDI AS usCreaAudi,   " +
                "  to_char(u1.FE_CREA_AUDI, 'dd/mm/yyyy') AS feCreaAudi,   " +
                "  u1.US_MODI_AUDI AS usModiAudi,   " +
                "  to_char(u1.FE_MODI_AUDI, 'dd/mm/yyyy') AS feModiAudi,   " +
                "  u1.ES_USUARIO AS esUsuario,   " +
                "  u1.DE_EMAIL AS deEmail,   " +
                "  u1.NU_TELEFONO AS nuTelefono,  " +
                "  u2.ap_primer AS apPrimerCrea,  " +
                "  u2.ap_segundo AS apSegundoCrea,  " +
                "  u2.prenombres AS prenombresCrea,    " +
                "  u2.co_usuario AS coUsuarioCrea, " +
                "  u3.ap_primer AS apPrimerModi,  " +
                "  u3.ap_segundo AS apSegundoModi,  " +
                "  u3.prenombres AS prenombresModi,    " +
                "  u3.co_usuario AS coUsuarioModi," +
                "  to_char(u1.fe_last_login, 'dd/mm/yyyy') fe_last_login " +
                "FROM PNTM_USUARIO_EXTERNO u1 left join   " +
                "PNTM_USUARIO_EXTERNO u2 on u1.US_CREA_AUDI=u2.CO_USUARIO  " +
                "left join PNTM_USUARIO_EXTERNO u3 on u3.co_usuario=u1.us_modi_audi  " +
                "WHERE u1.CO_USUARIO=?";

        logger.debug("DAO " + query + " por ejecutar con: " + coUsuario );
        try {
            UsuarioExterno usuarioExterno = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class), coUsuario);
            usuarioExterno.setEntidades(getEntidadesUsuario(coUsuario));
            /*usuarioExterno.setTipoEntidades(getTipoEntidades(coUsuario));*/
            usuarioExterno.setGrupos(getGruposUsuario(coUsuario));//lista de los grupos.

//            logger.debug("intentando antes get entidades...");
            usuarioExterno.setDeGrupos(getDeGrupos(usuarioExterno.getGrupos()));

            if (usuarioExterno.getEntidades() != null) {
                usuarioExterno.setEntidades(usuarioExterno.getEntidades());
                List<String> coEntidades = new ArrayList<String>();
                List<String> coTipoEntidades = new ArrayList<String>();
                for (String coEntidad : usuarioExterno.getEntidades()) {
                    String[] split = coEntidad.split("_");
                    coEntidades.add(split[0]);
                    coTipoEntidades.add(split[1]);
                }

                usuarioExterno.setTipoEntidades(getCoTipoEntidades(coTipoEntidades)); //filtramos los nuevos... 9=otros, 1 = minicipios,2 = eess.
                usuarioExterno.setDeTipoEntidades(getDeTipoEntidades(getCoTipoEntidades(coTipoEntidades)));
                usuarioExterno.setDeEntidades(getDeEntidades(coEntidades, coTipoEntidades));
            }

            return usuarioExterno;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        } /*catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
    }

    private String getDeTipoEntidades(List<String> coTipoEntidades) {
        String result = "";
        if(coTipoEntidades.size() > 0) {
            for(int i=0; i<coTipoEntidades.size(); i++){
                String deTipoEntidad = getDeTipoEntidad(coTipoEntidades.get(i));
                if(!deTipoEntidad.isEmpty() && deTipoEntidad != null)
                    result += deTipoEntidad + ", ";
            }
        }
        return result;
    }

    private String getDeTipoEntidad(String coTipoEntidad) {
        String query = "select DE_DOM from pntr_dominios where no_dom='CO_TIPO_ENTIDAD' and co_dominio=?";
        logger.debug("DAO: getDeTipoEntidad " + query + " con: co_dominio=" + coTipoEntidad);
        try{
            Object[]params={coTipoEntidad};
            return jdbcTemplate.queryForObject(query, params, String.class);
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
    public boolean insertUsuarioGrupo(String coGrupo, String coUsuario) {
        String query = "" +
                "INSERT INTO PNTM_GRUPO_USUARIO " +
                "  (CO_GRUPO, CO_USUARIO " +
                "  ) VALUES " +
                "  (?,? " +
                "  )";
        Object params[] = new Object[]{coGrupo, coUsuario};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        int resultInsert = jdbcTemplate.update(query, params);
        return resultInsert == 1;
    }

    @Override
    public boolean insertUsuarioEntidad(String coUsuario, String coEntidad, String coTipoEntidad) {
        String query  = "" +
                "INSERT INTO PNTM_USUARIO_ENTIDAD " +
                "  (CO_USUARIO, CO_ENTIDAD, CO_TIPO_ENTIDAD " +
                "  ) VALUES " +
                "  (?, ?, ? " +
                "  )";
        Object params[] = new Object[]{coUsuario, coEntidad, coTipoEntidad};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        int resultInsert = jdbcTemplate.update(query, params);
        return resultInsert == 1;
    }


    @Override
    public List<String> getEntidadesUsuario(String coUsuario) {
        String query = "SELECT CO_ENTIDAD||'_'||CO_TIPO_ENTIDAD FROM PNTM_USUARIO_ENTIDAD WHERE CO_USUARIO=?";
        logger.debug("DAO: getEntidadesUsuario " + query + " con: coUsuario=" + coUsuario);
        try {
            //List<String> list = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(String.class), coUsuario);
            Object[]params={coUsuario};
            List<String> list = jdbcTemplate.queryForList(query, params, String.class);
            return list;
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
    public List<String> getTiposEntidad(String coUsuario) {
        String query = "SELECT CO_TIPO_ENTIDAD FROM PNTM_USUARIO_ENTIDAD WHERE CO_USUARIO=? ORDER BY CO_ENTIDAD, CO_TIPO_ENTIDAD";
        try{
            //List<String> list = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(String.class), coUsuario);
            Object[] params={coUsuario};
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            List<String> list = jdbcTemplate.queryForList(query, params, String.class);
            return list;
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
    public Integer getEdad(String coUsuario) {
        String query = " SELECT trunc(months_between(sysdate, fe_nacimiento)/12) AS fechaNacimiento " +
                       " FROM idotabmaestra.getm_ani " +
                       " WHERE nu_dni=?";
        try {
            Object[] params = {coUsuario};
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            return jdbcTemplate.queryForObject(query,params,Integer.class);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean verificarUsuario(String coUsuario) {
        String sql = "SELECT COUNT(1) FROM pntm_usuario_externo WHERE co_usuario=?";
        Object[] params = {coUsuario};
        log.debug(String.format("DAO '%s', con '%s'", new Object[] { sql, ArrayUtils.toString(params) }));

        try {
            return ((Integer)this.jdbcTemplate.queryForObject(sql, params, Integer.class)).intValue() > 0;
        }
        catch (EmptyResultDataAccessException e) {
            log.error("Dni no existe", e);
        }
        catch (Exception e) {
            log.error("Error en la consulta", e);
        }
        return false;
    }

    @Override
    public boolean verificarEstadoUsuarioInAni(String coUsuario) {
        String query = " SELECT co_restri FROM idotabmaestra.getm_ani WHERE nu_dni=?";
        try {
            Object[] params = {coUsuario};
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
            String result = jdbcTemplate.queryForObject(query,params,String.class);
            if(result==null || result.isEmpty() || result.trim().isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return false;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public SignupForm getUsuarioDni(String coUsuario) {

        String query = "SELECT u1.AP_PRIMER AS apPrimer,   u1.AP_SEGUNDO AS apSegundo,   u1.prenom_inscrito AS preNombres \n" +
                "FROM idotabmaestra.getm_ani u1 \n" +
                "WHERE u1.nu_dni=?";

        logger.debug("DAO " + query + " por ejecutar con: " + coUsuario );
        try {
            SignupForm usuarioExterno = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(SignupForm.class), coUsuario);
            return usuarioExterno;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getGruposUsuario(String coUsuario) {
        String query = " SELECT CO_GRUPO FROM PNTM_GRUPO_USUARIO WHERE CO_USUARIO=? ";
        log.info("DAO: getGruposUsario" +  query + " con: coUsuario=" + coUsuario );

        try{
            //List<String> list = jdbcTemplate.queryForList(query, BeanPropertyRowMapper.newInstance(String.class), coUsuario);
            Object[]params={coUsuario};
            List<String> list = jdbcTemplate.queryForList(query, params, String.class);
            return list;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Objeto> getEntidades() {
        //List<Map<String, Object>> res = new
        String query = "SELECT CO_ENTIDAD AS id, DE_ENTIDAD AS text FROM PNTM_ENTIDAD where es_entidad='1' ";
//        log.info("DAO: getEntidades" +  query);
        try{
            //List<String> list = jdbcTemplate.queryForList(query, BeanPropertyRowMapper.newInstance(String.class), coUsuario);
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Objeto.class));
            //return list;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<Objeto> getGrupos() {
        String query = "SELECT CO_GRUPO AS id, DE_GRUPO AS text FROM PNTM_GRUPO WHERE ES_GRUPO<>'9'";

        log.info("DAO: getGrupos" +  query);
        try{
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Objeto.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean setFeLastLogin(String coUsuario) {
        String query = "UPDATE PNTM_USUARIO_EXTERNO SET FE_LAST_LOGIN=sysdate WHERE co_usuario=?";
        int resultado = this.jdbcTemplate.update(query, coUsuario);
        return resultado == 1;
    }

    @Override
    public List<UsuarioExterno> buscarUsuarioExterno(String apPrimer, String apSegundo, String preNombres, int filaInicio, int filaFinal) {
        String where = "";
        List<Object> parameters = new ArrayList<Object>();
        if(!apPrimer.equals("") && !apPrimer.isEmpty()) {
            where += "ap_primer like ? and ";
            parameters.add("%" +apPrimer.toUpperCase() + "%");
        }
        if(!apSegundo.equals("") && !apSegundo.isEmpty()) {
            where += "ap_segundo like ? and ";
            parameters.add("%" + apSegundo.toUpperCase() + "%");
        }
        if(!preNombres.equals("") && !preNombres.isEmpty()) {
            where += "prenombres like ? and ";
            parameters.add("%" + preNombres.toUpperCase() + "%");
        }
        parameters.add(filaInicio);
        parameters.add(filaFinal);

        if(where.equals("")){
            return new ArrayList<UsuarioExterno>();
        }
        else {
            where = where.substring(0, where.length()-4);
//            logger.info("WHERE: " + where);
            String query = "" +
                    "SELECT * FROM ( " +
                    "SELECT rownum as fila, u.co_usuario coUsuario, u.ap_primer apPrimer, u.ap_segundo apSegundo, u.prenombres prenombres, u.de_email deEmail, u.es_usuario esUsuario, u.fe_last_login feLastLogin " +
                    " FROM PNTM_USUARIO_EXTERNO u WHERE " + where +
                    " ORDER BY u.ap_primer, u.ap_segundo, u.prenombres) " +
                    " WHERE fila BETWEEN ? AND ? ";
//            logger.debug("DAO: buscarUsuarioExterno por ejecutar " + query + " con: "+ parameters.toString());
            List<UsuarioExterno> usuarioExternoList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class), parameters.toArray(new Object[parameters.size()]));
//            logger.debug("Numero de registros buscarUsuarioExterno: " + usuarioExternoList.size());
            return usuarioExternoList;
        }
    }
// Se agrero in_imprime_casada 13-04-2015
    // autor: JMB
    // fecha: 15-04-2015
    @Override
    public UsuarioExterno getUsuarioAni(String dni) {
        String query = "" +
                " SELECT nvl(nu_dni, ' ') as coUsuario,\n" +
                "                   nvl(trim(ap_primer), ' ') as apPrimer,\n" +
                "                   decode(ap_casada,\n" +
                "                          null,\n" +
                "                          nvl(trim(ap_segundo), ' '),\n" +
                "                          decode(ap_segundo,\n" +
                "                                 null,\n" +
                "                                 nvl(trim(ap_casada), ' '),\n" +
                "                                 nvl(trim(ap_segundo), ' ') || ' ' ||\n" +
                "                                 nvl(trim(ap_casada), ' '))) as ap_segundo,\n" +
                "                   nvl(trim(prenom_inscrito), ' ') as prenombres " +
                "FROM getm_ani " +
                "WHERE nu_dni = ?";
        logger.debug("DAO: " +  " por ejecutar " + query + " con: dni=" + dni);
        try	{
            UsuarioExterno usuarioExterno = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class), dni);
//            log.info("usuarioExterno: " + usuarioExterno);

            return usuarioExterno;
        } catch(EmptyResultDataAccessException ersdae) {
            log.error(ersdae.getMessage());
            return new UsuarioExterno();

        } catch(IncorrectResultSizeDataAccessException irsdae) {
            log.error(irsdae.getMessage());
            return null;
        }
    }

    @Override
    public boolean isDniMayor(String nuDni) {
        String query = "SELECT COUNT(1) FROM getm_ani WHERE IDOCAPTURA.FU_GET_TIPO_FICHA_REGISTRAL(ti_ficha_reg)='"+padronProperties.FICHA_MAYOR+"' AND nu_dni=? ";
//        logger.debug(String.format("DAO query='%s' , con nuDni= '%s'", query, nuDni));
        return this.jdbcTemplate.queryForInt(query, nuDni) == 1;
    }

    // verifica si el dni es de una persona mayor, ver padronProperties.EDAD_MIN_MADRE
    @Override
    public boolean isDNIMadre(String nuDni) {
        /*String query = "SELECT nu_dni FROM getm_ani WHERE nu_dni=? and trunc((months_between(sysdate, fe_nacimiento))/12) >= ?";*/
        String query = "SELECT (trunc((months_between(sysdate, fe_nacimiento))/12)) FROM getm_ani WHERE nu_dni=?";
        /*logger.debug(String.format("DAO query='%s' , con nuDni= '%s' edad_min_madre='%s'", query, nuDni, padronProperties.EDAD_MIN_MADRE));*/
        Object[] params = new Object[]{nuDni};
//        logger.debug(String.format("DAO: '%s' con: '%s'", query, ArrayUtils.toString(params)));

        try{
            Integer edad = this.jdbcTemplate.queryForInt(query, params);
            return (edad != null && edad >= Integer.parseInt(padronProperties.EDAD_MIN_MADRE));
        }
        catch (EmptyResultDataAccessException er){
            er.printStackTrace();
            return true;
        }
        catch (IncorrectResultSizeDataAccessException ie){
            ie.printStackTrace();
            return true;
        }


        /*return true;*/
        /*if(result.isEmpty())
            return true;*/
    }

    @Override
    public boolean resetPassword(String nuDni, String usModiPw) {
        String query = "update pntm_usuario_externo set de_password=co_usuario, fe_password=NULL where co_usuario=?";
//        logger.debug(String.format("DAO '%s' con '%s'", query, nuDni));
        MovimientoPassword movimientoPassword = MovimientoPassword.toResetPassword(nuDni, usModiPw);
        movimientoPasswordDao.insertMovimientoPw(movimientoPassword);
        return (this.jdbcTemplate.update(query, nuDni) > 0);
    }

    @Override
    public List<UsuarioExterno> getAllDetails() {

        String filtroUsuariosReniec = "";
        String filtroGrupoUsuarios = "";
        String where = "";
        // filtrar usuarios de RENIEC
        if (!"4".equals(usuario.getCoTipoEntidad())) { //<>reniec
            filtroUsuariosReniec += " where co_entidad<>'1843' ";
            filtroGrupoUsuarios = " where g.co_grupo in (1,4) \n";
            where = " and ";
        }
        else {
            where = where +" where ";
        }
        String query = "" +
                "" +
                "select * " +
                "  from ((select distinct u.co_usuario, " +
                "                         u.ap_primer, " +
                "                         u.ap_segundo, " +
                "                         u.prenombres, " +
                "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email, " +
                "                         decode(upper(u.nu_telefono), " +
                "                                'XXX', " +
                "                                null, " +
                "                                u.nu_telefono) nu_telefono, " +
                "                         u.dni_alcalde, " +
                "                         u.de_alcalde, " +
                "                         pd.de_dom_detalle as deTipoEntidad,  " +
                "                         to_char(e.co_entidad) co_entidad," +
                "                         e.de_entidad_larga, " +
                "                         decode(e.co_ubigeo_inei, " +
                "                                '00', " +
                "                                null, " +
                "                                e.co_ubigeo_inei) co_ubigeo_inei, " +
                "                         uu.de_departamento, " +
                "                         uu.de_provincia, " +
                "                         uu.de_distrito, " +
                "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login, " +
                "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso, " +
                "                         decode(u.es_usuario,'1','ACTIVO','0','INACTIVO',' ')  as esUsuario, " +
                "                         u.us_crea_audi, " +
                "                         u.us_modi_audi, " +
                "                         g.de_grupo " +
                "           from pntm_usuario_externo u " +
                "           join pntm_usuario_entidad ue " +
                "             on ue.co_usuario = u.co_usuario " +
                "           join pntm_entidad e " +
                "             on ue.co_entidad = e.co_entidad and e.es_entidad='1' " +
                "             and ue.co_tipo_entidad in ('1','2','3','4','6','7')  " +
                "           left join pntm_ubigeo_inei uu " +
                "             on uu.co_ubigeo_inei = e.co_ubigeo_inei and uu.es_ubigeo='1' " +
                "           left join pntm_grupo_usuario gu " +
                "             on gu.co_usuario = u.co_usuario " +
                "           left join pntm_grupo g " +
                "             on g.co_grupo = gu.co_grupo " +
                "             and g.es_grupo <> '9' \n" +
                "           left join pntr_dominios pd \n" +
                "             on pd.co_dominio = decode(e.co_tipo_entidad,'8','8','1','1','9') " +
                filtroGrupoUsuarios + where +
                "           pd.no_dom='CO_TIPO_ENTIDAD' \n"+
                "         /*order by co_ubigeo_inei desc*/ " +
                "         ) union " +
                "        (select distinct u.co_usuario, " +
                "                         u.ap_primer, " +
                "                         u.ap_segundo, " +
                "                         u.prenombres, " +
                "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email, " +
                "                         decode(upper(u.nu_telefono), " +
                "                                'XXX', " +
                "                                null, " +
                "                                u.nu_telefono) nu_telefono, " +
                "                         u.dni_alcalde, " +
                "                         u.de_alcalde, " +
                "                         'OTROS' as deTipoEntidad,  " +
                "                         e.co_disa co_entidad," +
                "                         e.de_disa, " +
                "                         decode(e.co_ubigeo_inei, " +
                "                                '00', " +
                "                                null, " +
                "                                e.co_ubigeo_inei) co_ubigeo_inei, " +
                "                         null, " +
                "                         null, " +
                "                         null, " +
                "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login, " +
                "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso, " +
                "                         decode(u.es_usuario,'1','ACTIVO','0','INACTIVO',' ')  as esUsuario, " +
                "                         u.us_crea_audi, " +
                "                         u.us_modi_audi, " +
                "                         g.de_grupo " +
                "           from pntm_usuario_externo u " +
                "           join pntm_usuario_entidad ue " +
                "             on ue.co_usuario = u.co_usuario " +
                "           join pnvm_disa e " +
                "             on ue.co_entidad = e.co_disa " +
                "            and ue.co_tipo_entidad = '5' " +
                "           left join pntm_grupo_usuario gu " +
                "             on gu.co_usuario = u.co_usuario " +
                "           left join pntm_grupo g " +
                "             on g.co_grupo = gu.co_grupo " +
                "            and g.es_grupo <> '9' \n" +
                filtroGrupoUsuarios +
                "         /*order by co_ubigeo_inei desc*/ " +
                "        ) union " +
                "        (select distinct u.co_usuario, " +
                "                         u.ap_primer, " +
                "                         u.ap_segundo, " +
                "                         u.prenombres, " +
                "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email, " +
                "                         decode(upper(u.nu_telefono), " +
                "                                'XXX', " +
                "                                null, " +
                "                                u.nu_telefono) nu_telefono, " +
                "                         u.dni_alcalde, " +
                "                         u.de_alcalde, " +
                "                         'OTROS' as deTipoEntidad,  " +
                "                         e.co_disa || e.co_red co_entidad," +
                "                         e.de_red, " +
                "                         decode(e.co_ubigeo_inei, " +
                "                                '00', " +
                "                                null, " +
                "                                e.co_ubigeo_inei) co_ubigeo_inei, " +
                "                         null, " +
                "                         null, " +
                "                         null, " +
                "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login, " +
                "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso, " +
                "                         decode(u.es_usuario,'1','ACTIVO','0','INACTIVO',' ')  as esUsuario, " +
                "                         u.us_crea_audi, " +
                "                         u.us_modi_audi, " +
                "                         g.de_grupo " +
                "           from pntm_usuario_externo u " +
                "           join pntm_usuario_entidad ue " +
                "             on ue.co_usuario = u.co_usuario " +
                "           join pnvm_red e " +
                "             on ue.co_entidad = e.co_disa || e.co_red " +
                "            and ue.co_tipo_entidad = '6' " +
                "           left join pntm_grupo_usuario gu " +
                "             on gu.co_usuario = u.co_usuario " +
                "           left join pntm_grupo g " +
                "             on g.co_grupo = gu.co_grupo " +
                "            and g.es_grupo <> '9' \n" +
                filtroGrupoUsuarios +
                ") " +
                "        " +
                "        union " +
                "        (select distinct u.co_usuario, " +
                "                         u.ap_primer, " +
                "                         u.ap_segundo, " +
                "                         u.prenombres, " +
                "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email, " +
                "                         decode(upper(u.nu_telefono), " +
                "                                'XXX', " +
                "                                null, " +
                "                                u.nu_telefono) nu_telefono, " +
                "                         u.dni_alcalde, " +
                "                         u.de_alcalde, " +
                "                         'OTROS' as deTipoEntidad,  " +
                "                         e.co_disa || e.co_red || e.co_microred co_entidad," +
                "                         e.de_microred, " +
                "                         decode(e.co_ubigeo_inei, " +
                "                                '00', " +
                "                                null, " +
                "                                e.co_ubigeo_inei) co_ubigeo_inei, " +
                "                         null, " +
                "                         null, " +
                "                         null, " +
                "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login, " +
                "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso, " +
                "                         decode(u.es_usuario,'1','ACTIVO','0','INACTIVO',' ')  as esUsuario, " +
                "                         u.us_crea_audi, " +
                "                         u.us_modi_audi, " +
                "                         g.de_grupo " +
                "           from pntm_usuario_externo u " +
                "           join pntm_usuario_entidad ue " +
                "             on ue.co_usuario = u.co_usuario " +
                "           join pnvm_microred e " +
                "             on ue.co_entidad = e.co_disa || e.co_red || e.co_microred " +
                "            and ue.co_tipo_entidad = '7' " +
                "           left join pntm_grupo_usuario gu " +
                "             on gu.co_usuario = u.co_usuario " +
                "           left join pntm_grupo g " +
                "             on g.co_grupo = gu.co_grupo " +
                "            and g.es_grupo <> '9' \n" +
                filtroGrupoUsuarios +
                ") union " +
                "        (select distinct u.co_usuario, " +
                "                         u.ap_primer, " +
                "                         u.ap_segundo, " +
                "                         u.prenombres, " +
                "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email, " +
                "                         decode(upper(u.nu_telefono), " +
                "                                'XXX', " +
                "                                null, " +
                "                                u.nu_telefono) nu_telefono, " +
                "                         u.dni_alcalde, " +
                "                         u.de_alcalde, " +
                "                         'ESTABLECIMIENTOS DE SALUD' as deTipoEntidad,  " +
                "                         e.co_est_salud co_entidad," +
                /*"                         e.de_est_salud||'('|| de_departamento,de_provincia,de_distrito||')' as deEntidadLarga, " +*/
                "                         e.de_microred, " +
                "                         decode(e.co_ubigeo_inei, " +
                "                                '00', " +
                "                                null, " +
                "                                e.co_ubigeo_inei) co_ubigeo_inei, " +
                "                         uu.de_departamento, " +
                "                         uu.de_provincia, " +
                "                         uu.de_distrito, " +
                "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login, " +
                "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso, " +
                "                         decode(u.es_usuario,'1','ACTIVO','0','INACTIVO',' ')  as esUsuario, " +
                "                         u.us_crea_audi, " +
                "                         u.us_modi_audi, " +
                "                         g.de_grupo " +
                "           from pntm_usuario_externo u " +
                "           join pntm_usuario_entidad ue " +
                "             on ue.co_usuario = u.co_usuario " +
                "           join pnvm_establecimiento_salud e " +
                "             on ue.co_entidad = e.co_est_salud " +
                "            and ue.co_tipo_entidad = '8' " +
                "           left join pntm_ubigeo_inei uu" +
                "             on uu.co_ubigeo_inei=e.co_ubigeo_inei " +
                "           left join pntm_grupo_usuario gu " +
                "             on gu.co_usuario = u.co_usuario " +
                "           left join pntm_grupo g " +
                "             on g.co_grupo = gu.co_grupo " +
                "            and g.es_grupo <> '9' \n" +
                filtroGrupoUsuarios +
                ") " +
                "        " +
                "       ) " +
                filtroUsuariosReniec +
                " order by de_entidad_larga ";

        logger.debug(String.format("DAO '%s'", query));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }



}