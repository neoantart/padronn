package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.Jdbc4NativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.OracleJdbc4NativeJdbcExtractor;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.PadronImgDao;
import pe.gob.reniec.padronn.logic.model.PadronImg;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase PadronImgDaoImpl.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 25/05/13 02:23 PM
 */
@Repository
public class PadronImgDaoImpl extends AbstractBaseDao implements PadronImgDao {

	OracleLobHandler lobHandler;

	@PostConstruct
	public void start() {
		lobHandler=new OracleLobHandler();
		// thanks God! http://jkingtech.blogspot.com/2009/12/spring-cast-oracleconnection-from.html
		// corregir problema ClassCastException al usar OracleLobCreator en Pooled Connections
		lobHandler.setNativeJdbcExtractor(new OracleJdbc4NativeJdbcExtractor());
		//lobHandler.setNativeJdbcExtractor(new Jdbc4NativeJdbcExtractor());
//		logger.debug("LobHandler iniciado...");
	}

	@Override
	public byte[] obtenerFotoMenor(String coPadronNominal) {

		String query = "" +

				"SELECT " +
				"IMG_FOTO_MENOR as imgFotoMenor " +
				"FROM " +
				"PNTV_PADRON_NOMINAL_IMG " +
				"WHERE " +
				"CO_PADRON_NOMINAL=? ";

		try {
			Object[] params = {coPadronNominal};
//			log.info("DAO '" + query + "' con '" + Arrays.toString(params)+ "' por ejecutar");
			Map map = (Map)this.jdbcTemplate.queryForObject(
					query,
					new RowMapper() {
						public Object mapRow(ResultSet rs, int i)
								throws SQLException {
							Map results = new HashMap();
							byte[] blobBytes = lobHandler.getBlobAsBytes(rs, "imgFotoMenor");
							results.put("imgFotoMenor", blobBytes);
							return results;
						}
					},
					params
			);
			return (byte[])map.get("imgFotoMenor");

		} catch(EmptyResultDataAccessException erdae) {
			log.warn(erdae.getMessage());
			return new byte[0];
		}
	}

	@Override
	public PadronImg obtenerPadronImg(String coPadronNominal) {

		String query =  "" +
				"SELECT " +
				"IMG_FOTO_MENOR as imgFotoMenor, " +
				"EXT_FOTO_MENOR as extFotoMenor, " +
				"CTY_FOTO_MENOR as ctyFotoMenor " +
				"FROM " +
				"PNTV_PADRON_NOMINAL_IMG " +
				"WHERE " +
				"CO_PADRON_NOMINAL=? ";

		try {
			Object[] params = {coPadronNominal};
//			log.info("DAO '" + query + "' con '" + Arrays.toString(params)+ "' por ejecutar");
			Map map = (Map)this.jdbcTemplate.queryForObject(
					query,
					new RowMapper() {
						public Object mapRow(ResultSet rs, int i)
								throws SQLException {
							Map results = new HashMap();
							byte[] blobBytes = lobHandler.getBlobAsBytes(rs, "imgFotoMenor");
							results.put("imgFotoMenor", blobBytes);
							results.put("extFotoMenor", rs.getString("extFotoMenor"));
							results.put("ctyFotoMenor", rs.getString("ctyFotoMenor"));
							return results;
						}
					},
					params
			);

			PadronImg padronImg = new PadronImg();
			padronImg.setImgFotoMenor((byte[]) map.get("imgFotoMenor"));
			padronImg.setExtFotoMenor((String) map.get("extFotoMenor"));
			padronImg.setCtyFotoMenor((String) map.get("ctyFotoMenor"));
			return padronImg;

		} catch(EmptyResultDataAccessException erdae) {
			log.warn(erdae.getMessage());
			return null;
		}
	}

	@Override
	public void insertarPadronImg(final PadronImg padronImg) {

		String query =  "" +

				"INSERT INTO " +
				"PNTV_PADRON_NOMINAL_IMG " +
				"( " +
				"CO_PADRON_NOMINAL, " +
				"IMG_FOTO_MENOR, " +
				"USU_CREA_AUDI, " +
				"FE_CREA_AUDI, " +
				"USU_MODI_AUDI, " +
				"FE_MODI_AUDI," +
				"EXT_FOTO_MENOR, " +
				"CTY_FOTO_MENOR " +
				") " +

				"VALUES " +
				"( " +
				"?, " +
				"?, " +
				"?, " +
				"SYSDATE, " +
				"?, " +
				"SYSDATE," +
				"?," +
				"? " +
				") ";

//		log.info("DAO '" + query + "' con '" +padronImg+ "' por ejecutar");
		this.jdbcTemplate.execute(query,
				new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
					protected void setValues(PreparedStatement ps, LobCreator lobCreator)
							throws SQLException {
						int paramNo = 0;
						ps.setString(++paramNo, padronImg.getCoPadroNominal());
						lobCreator.setBlobAsBytes(ps, ++paramNo, padronImg.getImgFotoMenor());
						ps.setString(++paramNo, padronImg.getUsuCreaAudi());
						ps.setString(++paramNo, padronImg.getUsuModiAudi());
						ps.setString(++paramNo, padronImg.getExtFotoMenor());
						ps.setString(++paramNo, padronImg.getCtyFotoMenor());
					}
				}
		);
	}

	@Override
	public boolean tieneImagen(String coPadronNominal) {
		String query="select count(*) from pntv_padron_nominal_img where co_padron_nominal=?";
		int resultados=jdbcTemplate.queryForInt(query, coPadronNominal);
		return resultados==1;
	}

	@Override
	public void clearPadronImg(String coPadronNominal) {

		String query = "" +
				"DELETE FROM " +
				"PNTV_PADRON_NOMINAL_IMG " +
				"WHERE " +
				"CO_PADRON_NOMINAL=? ";

//		log.info("DAO '" + query + "' con '" +coPadronNominal+ "' por ejecutar");
		jdbcTemplate.update(query,
				coPadronNominal);
	}
}
