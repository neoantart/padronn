package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.LugarDao;
import pe.gob.reniec.padronn.logic.dao.UbigeoDao;
import pe.gob.reniec.padronn.logic.model.Lugar;

import java.util.List;

/**
 * User: jronal
 * Date: 29/05/13
 * Time: 04:31 PM
 */

@Repository
public class LugarDaoImpl extends SimpleJdbcDaoBase implements LugarDao {
    @Override
    public List<Lugar> getDepartamentos(){
        String query = "SELECT DISTINCT SUBSTR(CO_UBIGEO_INEI, 1, 2) AS coLugar, de_departamento as deLugar FROM pntm_ubigeo_inei";
//        logger.info("DAO lista getDepartamentos ");
        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Lugar.class));
    }

    @Override
    public List<Lugar> getProvincias(String coDepartamento) {
        String query = "SELECT DISTINCT SUBSTR(CO_UBIGEO_INEI, 3, 2) AS coLugar, de_provincia as deLugar FROM pntm_ubigeo_inei WHERE SUBSTR(CO_UBIGEO_INEI,1,2)=?";
        logger.info("DAO lista getProvincias coDepartamento="+coDepartamento);
        return jdbcTemplate.query(query, new Object[]{coDepartamento}, BeanPropertyRowMapper.newInstance(Lugar.class));
    }

    @Override
    public List<Lugar> getDistritos(String coDepartamento, String coProvincia){
        String query = "SELECT DISTINCT SUBSTR(CO_UBIGEO_INEI, 5, 2) AS coLugar, de_distrito as deLugar FROM pntm_ubigeo_inei WHERE SUBSTR(CO_UBIGEO_INEI,1,2)=? AND SUBSTR(CO_UBIGEO_INEI,3,2)=?";
        logger.info("DAO lista getDistritos coDepartamento="+coDepartamento + "coProvincia="+coProvincia);
        return jdbcTemplate.query(query, new Object[]{coDepartamento, coProvincia},BeanPropertyRowMapper.newInstance(Lugar.class));
    }

    @Override
    public String getDeDepartamento(String coDepartamento){
        String query = "SELECT PNPK_UBIGEO.fu_get_des_dpto_ubig(?) FROM dual";
        logger.info("DAO getDeDepartamento con coDep="+coDepartamento);
        return jdbcTemplate.queryForObject(query, new Object[]{coDepartamento}, String.class);
    }

    @Override
    public String getDeProvincia(String coDep, String coPrv) {
        String query = "SELECT PNPK_UBIGEO.FU_GET_DES_PROV_UBIG(?, ?) FROM dual";
        logger.info("DAO getDePrvUbigeo con codDep=" + coDep + "& conPrv=" + coPrv);
        return jdbcTemplate.queryForObject(query, new Object[] { coDep, coPrv }, String.class);
    }

    @Override
    public String getDeDistrito(String coDep, String coPrv, String coDist){
        String query = "SELECT PNPK_UBIGEO.FU_GET_DES_DIST_UBIG(?, ?, ?) FROM dual";
        logger.info("DAO getDeDistUbigeo con coDep =" + coDep + " & coPrv=" + coPrv + " & coDist=" + coDist);
        return jdbcTemplate.queryForObject(query, new Object[]{coDep, coPrv, coDist}, String.class);
    }
}