/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.reniec.commons.baseApplication.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 *
 * @author crosales
 */
public abstract class SimpleJdbcDaoBase {

    protected JdbcTemplate jdbcTemplate;
    protected JdbcTemplate jdbcTemplateRC;
    protected JdbcTemplate jdbcTemplateHV;
//    private SimpleJdbcCall simpleJdbcCall;

    protected DataSource dataSource;
    protected DataSource dataSourceRC;
    //protected DataSource dataSourceHV;

	protected final Logger logger = Logger.getLogger(getClass());

    @Autowired
    public void init(@Qualifier("dataSource")DataSource dataSource,
                     @Qualifier("dataSourceRC")DataSource dataSourceRC){
        this.dataSource = dataSource;
        this.dataSourceRC = dataSourceRC;

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcTemplateRC = new JdbcTemplate(dataSourceRC);
//        this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
        //this.simpleJdbcCall.setProcedureName();
    }

}