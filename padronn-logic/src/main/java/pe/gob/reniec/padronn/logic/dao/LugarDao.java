package pe.gob.reniec.padronn.logic.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.model.Lugar;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrador
 * Date: 30/05/13
 * Time: 02:14 PM
 */


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.UbigeoDao;
import pe.gob.reniec.padronn.logic.model.Lugar;

import java.util.List;

/**
 * User: jronal
 * Date: 29/05/13
 * Time: 04:31 PM
 */
public interface LugarDao{
    public List<Lugar> getDepartamentos();
    public List<Lugar> getProvincias(String coDepartamento);
    List<Lugar> getDistritos(String coDepartamento, String coProvincia);
    String getDeDepartamento(String coDepartamento);
    String getDeProvincia(String coDep, String coPrv);
    String getDeDistrito(String coDep, String coPrv, String coDist);
}