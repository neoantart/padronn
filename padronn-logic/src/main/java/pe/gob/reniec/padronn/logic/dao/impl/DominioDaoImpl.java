package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.DominioDao;
import pe.gob.reniec.padronn.logic.model.Dominio;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class DominioDaoImpl implements DominioDao {

    JdbcTemplate jdbcTemplate;
    DataSource dataSource;

    //Información precargada
    List<Dominio> nivelPobrezaItemsVar;
    List<Dominio> tipoSeguroItemsVar;
    List<Dominio> tipoProgramaSocialItemsVar;
    List<Dominio> tipoGestionItemsVar;
    List<Dominio> vinculoFamiliarItemsVar;
    List<Dominio> gradoInstruccionItemsVar;
    List<Dominio> lenguajeHabitualItemsVar;
    List<Dominio> tipoDocumentoItemsVar;
    List<Dominio> generoItemsVar;
    List<Dominio> etniaList;
    List<Dominio> motivoBajaList;
    List<Dominio> motivoAltaList;
    List<Dominio> tipoEntidadesList;
    List<Dominio> coTipoSinNombre;
    List<String> deTipoSinNombre;

    @Autowired
    public DominioDaoImpl(@Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
        String query = "SELECT co_dominio, de_dom_detalle as deDom "
                + "from pntr_dominios "
                + "where no_dom='CO_NIVEL_POBREZA' and es_dom=1";
        nivelPobrezaItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT co_tipo_seguro as co_dominio, \n" +
                "CASE co_tipo_seguro WHEN 0 THEN\n" +
                "   de_seguro\n" +
                "ELSE\n" +
                "   de_seguro||'('||de_seguro_larga||')'\n" +
                "END AS de_dom\n" +
                "FROM PNTR_TIPO_SEGURO WHERE es_seguro='1' ORDER BY co_tipo_seguro ASC\n";
        tipoSeguroItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT co_programa_social as co_dominio, \n" +
                "CASE co_programa_social WHEN 0 THEN\n" +
                "   de_programa\n" +
                "ELSE\n" +
                "   de_programa_larga||' ('||de_programa||')'\n" +
                "END AS de_dom  \n" +
                "FROM PNTM_PROGRAMA_SOCIAL WHERE es_programa='1' ORDER BY co_programa_social ASC\n";

        tipoProgramaSocialItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT co_dominio, de_dom_detalle as deDom "
                + "from pntr_dominios "
                + "where no_dom='TI_GESTION_CENTRO' and es_dom='1'";
        tipoGestionItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "select co_vinculo as co_dominio, de_vinculo as deDom\n" +
                "from PNTR_VINCULO_FAMILIAR\n" +
                "where es_vinculo='1'";
        vinculoFamiliarItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "select co_grado_instruccion as co_dominio, de_grado_instruccion as de_dom \n" +
                "from PNTR_GRADO_INSTRUCCION\n" +
                "where es_grado_instruccion='1'";
        gradoInstruccionItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT CO_LENGUA AS CO_DOMINIO, DE_LENGUA AS DEDOM\n" +
                "FROM PNTR_LENGUA\n" +
                "WHERE ES_LENGUA='1'";
        lenguajeHabitualItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "select ti_doc_identidad as co_dominio,\n" +
                "de_doc_identidad as deDom\n" +
                "from pntr_doc_identidad";
        tipoDocumentoItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT CO_GENERO_MENOR AS CO_DOMINIO, DE_GENERO_MENOR AS DEDOM \n" +
                "FROM PNTR_GENERO_MENOR";
        generoItemsVar = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "select co_etnia as co_dominio, de_etnia as de_dom from pntm_etnia where es_estado = '1'";
        this.etniaList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT co_dominio, de_dom_detalle as deDom "
                + "from pntr_dominios "
                + "where no_dom='CO_MOTIVO_BAJA' and es_dom='1'"
                + "and de_dom<>'EDAD MAXIMA SUPERADA'";
        motivoBajaList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT co_dominio, de_dom_detalle as deDom " +
                "from pntr_dominios " +
                "where no_dom='CO_TIPO_ENTIDAD' and es_dom='1'";
        tipoEntidadesList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT co_dominio, de_dom_detalle as deDom "
                + "from pntr_dominios "
                + "where no_dom='CO_MOTIVO_BAJA' and de_dom='OTROS MOTIVOS' ";
        motivoAltaList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT co_dominio, de_dom as deDom \n" +
                "from pntr_dominios \n" +
                "where no_dom='CO_TIPO_SIN_NOMBRE' and es_dom='1'";
        coTipoSinNombre = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Dominio.class));

        query = "SELECT de_dom \n" +
                "from pntr_dominios \n" +
                "where no_dom='CO_TIPO_SIN_NOMBRE' and es_dom='1'";
        deTipoSinNombre = jdbcTemplate.queryForList(query, String.class);
    }

    @Override
    public List<Dominio> nivelPobrezaItems() {
        return nivelPobrezaItemsVar;
    }

    @Override
    public List<Dominio> tipoSeguroItems() {
        return tipoSeguroItemsVar;
    }

    @Override
    public List<Dominio> tipoProgramaSocialItems() {
        return tipoProgramaSocialItemsVar;
    }

    @Override
    public List<Dominio> tipoGestionItems() {
        return tipoGestionItemsVar;
    }

    @Override
    public List<Dominio> vinculoFamiliarItems() {
        return vinculoFamiliarItemsVar;
    }

    @Override
    public List<Dominio> gradoInstruccionItems() {
        return gradoInstruccionItemsVar;
    }

    @Override
    public List<Dominio> lenguajeHabitualItems() {
        return lenguajeHabitualItemsVar;
    }

    @Override
    public List<Dominio> etniaItems() {
        return this.etniaList;
    }

    @Override
    public List<Dominio> tipoDocumentoItems() {
        return tipoDocumentoItemsVar;
    }

    @Override
    public List<Dominio> generoItems() {
        return generoItemsVar;
    }

    @Override
    public List<Dominio> getMotivoBajaList() {
        return motivoBajaList;
    }

    @Override
    public List<Dominio> getMotivoAltaList() {
        return motivoAltaList;
    }

    @Override
    public List<Dominio> tipoEntidadesItems() {
        return tipoEntidadesList;
    }

    @Override
    public List<Dominio> getCoTipoSinNombre() {
        return coTipoSinNombre;
    }

    @Override
    public List<String> getDeTipoSinNombre() {
        return deTipoSinNombre;
    }

    @Override
    public Dominio obtener(String codigoDominio, String nombreDominio) {
        String query = "select co_dominio, no_dom, de_dom, de_dom_detalle, es_dom, us_crea_audi, us_modi_audi, fe_crea_audi, fe_modi_audi " +
                "from pntr_dominios " +
                "where co_dominio=? and no_dom=?";
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{codigoDominio, nombreDominio}, BeanPropertyRowMapper.newInstance(Dominio.class));
        } catch (EmptyResultDataAccessException e) {
            return new Dominio();
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return new Dominio();
        }
    }

}