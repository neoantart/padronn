package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.RcDao;
import pe.gob.reniec.padronn.logic.model.Persona;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Map;


@Repository
public class RcDaoImpl extends SimpleJdbcDaoBase  implements RcDao {

    private SimpleJdbcCall datosCvn;
    private SimpleJdbcCall cnvMadre;


    Logger logger = Logger.getLogger(getClass());

    @Autowired
    public void setDataSource(@Qualifier(value = "dataSourceRC") DataSource dataSource) {
        String procedureNameDatosCnv = "pk_rrcc_padron.sp_datos_cnv";
        String procedureNameCnvMadre = "pk_rrcc_padron.sp_cnv_madre";

        datosCvn = new SimpleJdbcCall(dataSource)
                .withoutProcedureColumnMetaDataAccess()
                .withProcedureName(procedureNameDatosCnv)
                .useInParameterNames("v_nNuCNV")
                .declareParameters(
                        new SqlParameter("v_nNuCNV", Types.INTEGER),
                        new SqlOutParameter("v_vSegApeNac", Types.VARCHAR),
                        new SqlOutParameter("v_vMadDirecc", Types.VARCHAR),
                        new SqlOutParameter("v_vCodUbigeo", Types.VARCHAR),
                        new SqlOutParameter("v_vDesGenero", Types.VARCHAR),
                        new SqlOutParameter("v_vCodGenero", Types.VARCHAR),
                        new SqlOutParameter("v_vMadTiVinc", Types.VARCHAR),
                        new SqlOutParameter("v_vFeNacimie", Types.VARCHAR),
                        new SqlOutParameter("v_vEdad", Types.VARCHAR),
                        new SqlOutParameter("v_vEdadEscri", Types.VARCHAR),
                        new SqlOutParameter("v_vMadTipDoc", Types.VARCHAR),
                        new SqlOutParameter("v_vMadDesDoc", Types.VARCHAR),
                        new SqlOutParameter("v_vMadPriApe", Types.VARCHAR),
                        new SqlOutParameter("v_vMadSegApe", Types.VARCHAR),
                        new SqlOutParameter("v_vMadPrenom", Types.VARCHAR),
                        new SqlOutParameter("v_vMadDesTipVinc", Types.VARCHAR),
                        new SqlOutParameter("v_vMadCodTipVinc", Types.VARCHAR),
                        new SqlOutParameter("v_vMadNivelInst", Types.VARCHAR),
                        new SqlOutParameter("v_vCoEstSalud", Types.VARCHAR),
                        new SqlOutParameter("v_vNuSecuenciaLocal", Types.VARCHAR),
                        new SqlOutParameter("v_vDeEstSalud", Types.VARCHAR)
                );

        cnvMadre = new SimpleJdbcCall(dataSource)
                .withoutProcedureColumnMetaDataAccess()
                .withProcedureName(procedureNameCnvMadre)
                .useInParameterNames("v_vNuDocIde")
                .declareParameters(new SqlParameter("v_vNuDocIde", Types.VARCHAR))
                .returningResultSet("v_cResultado", BeanPropertyRowMapper.newInstance(Persona.class));
    }

    @Override
    public Persona obtenerPorCnv(String cnv) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("v_nNuCNV", Integer.parseInt(cnv));
        Map out = null;
        Persona persona = new Persona();
        try {
            out = datosCvn.execute(in);
            persona.setSegundoApellido((String) out.get("v_vMadPriApe"));
            persona.setDireccion((String) out.get("v_vMadDirecc"));
            persona.setCodigoUbigeo((String) out.get("v_vCodUbigeo"));
            persona.setGenero((String) out.get("v_vDesGenero"));
            persona.setCodigoGenero((String) out.get("v_vCodGenero"));
            persona.setMadreCodigoTipoVinculo((String) out.get("v_vMadTiVinc"));
            persona.setFechaNacimiento((String) out.get("v_vFeNacimie"));
            persona.setEdad((String) out.get("v_vEdad"));
            persona.setEdadEscrita((String) out.get("v_vEdadEscri"));
            persona.setMadreTiDoc((String) out.get("v_vMadTipDoc"));
            persona.setMadreDni((String) out.get("v_vMadDesDoc"));
            persona.setMadrePrimerApellido((String) out.get("v_vMadPriApe"));
            persona.setMadreSegundoApellido((String) out.get("v_vMadSegApe"));
            persona.setMadreNombres((String) out.get("v_vMadPrenom"));
            persona.setMadreTipoVinculo((String) out.get("v_vMadDesTipVinc"));
            persona.setMadreCodigoTipoVinculo((String) out.get("v_vMadCodTipVinc"));
            persona.setMadreGradoInstruccionCnv((String) out.get("v_vMadNivelInst"));
            persona.setCoEstSaludNac((String) out.get("v_vCoEstSalud"));
            persona.setNuSecuenciaLocalNac((String) out.get("v_vNuSecuenciaLocal"));
            persona.setDeEstSaludNac((String) out.get("v_vDeEstSalud"));
            persona.setCnv(cnv);
            /*logger.info("persona:coEstSalud=" + persona.getCoEstSaludNac());
            logger.info("persona:cnv=" + persona.getCnv());*/
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.debug(this.getClass().getName() + " Respuesta: " + out);
        return persona;
    }

    @Override
    public List<Persona> listarDatosCNVPorDniMadre(String dni) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("v_vNuDocIde", Integer.parseInt(dni));
        try {
            Map out = cnvMadre.execute(in);
            return (List<Persona>) out.get("v_cResultado");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Persona obtenerMenorDeRRCC(String coDni){

        String query =  "select distinct \n" +
                "     n.nu_acta_nacimiento numActa,\n" +
                "     n.nu_cnv cnv,\n" +
                "     n.de_primer_apellido primerApellido,\n" +
                "     n.de_segundo_apellido segundoApellido,\n" +
                "     n.de_pre_nombres nombres,\n" +
                "     n.fe_nacimiento fechaNacimiento,\n" +
                "     n.ho_nacimiento horaNacimiento,\n" +
                "     decode(n.co_sexo,'01','1','02','2') genero,\n" +
                "     n.nu_cui nuCui,\n" +
                "     p.de_primer_apellido padrePrimerApellido,\n" +
                "     p.de_segundo_apellido padreSegundoApellido,\n" +
                "     p.de_pre_nombres padreNombres,\n" +
                "     p.nu_doc_identidad padreDni,\n" +
                "     decode(p.co_vinculo_familiar,'01','1','02','2') padreCodigoTipoVinculo,\n" +
                "     m.de_primer_apellido madrePrimerApellido,\n" +
                "     m.de_segundo_apellido madreSegundoApellido,\n" +
                "     m.de_pre_nombres madreNombres,\n" +
                "     m.nu_doc_identidad madreDni,\n" +
                "     decode(m.co_vinculo_familiar,'01','1','02','2') madreCodigoTipoVinculo\n" +
                "    from rctm_nacimientos n, rctm_nacimientos_actores p, rctm_nacimientos_actores m\n" +
                "    where n.nu_cui=?\n" +
                "    and (n.co_estado_acta_nacimiento='1' or n.co_estado_acta_nacimiento='2') \n" +
                "    and n.nu_acta_nacimiento=p.nu_acta_nacimiento(+)\n" +
                "    and p.co_vinculo_familiar(+)='02'\n" +
                "    and n.nu_acta_nacimiento=m.nu_acta_nacimiento(+)\n" +
                "    and m.co_vinculo_familiar(+)='01'";

        Object[] params = new Object[]{coDni};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return this.jdbcTemplateRC.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Error: Al buscar data por CUI de menor en RRCC-data vacia"+e.toString());
            return null;
        } catch (Exception e) {
            logger.info("Error: Al buscar data por CUI de menor en RRCC-error deconocido"+e.toString());
            return null;
        }
    }

    @Override
    public String getMadrePorDniMenor(String nuDni){
        String query = "select nu_cui nuCui\n" +
                "       nu_cnv cnv \n" +
                "from rctm_nacimientos rc  \n" +
                "where rc.nu_cui=? ";

        try {
            Object[] params = new Object[]{nuDni};
            Persona nuCnv = jdbcTemplateRC.queryForObject(
                             query, BeanPropertyRowMapper.newInstance(Persona.class), params
                            );

            if(nuCnv.getCnv() != null){
                return nuCnv.getCnv();   /*si los campos son correctos*/
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            return null;
        }
        /*primero buscamos registro en RRCC y recuperamos datos de la coDniMadre, fecNacimiento, coGenero*/
    }


    /*JdbcTemplate jdbcTemplate;
    Logger logger = Logger.getLogger(getClass());
    @Autowired
    public void init(@Qualifier("dataSourceRC") DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Persona> listarDatosCNVPorDniMadre(String dni) {
        String query = "select c.nu_cnv cnv, " +
                "decode(c.co_nacido_sexo, '01', 'Masculino', '02', 'Femenino') genero, " +
                "TO_CHAR(to_date(c.fe_nacimiento,'yyyyMMdd'), 'dd/MM/yyyy') as fechaNacimiento, " +
                "trunc((months_between(sysdate, to_date(c.fe_nacimiento,'yyyyMMdd')))/12) edad, " +
                "trunc((months_between(sysdate, to_date(c.fe_nacimiento,'yyyyMMdd')))/12)||' año(s), '||trunc(mod((months_between(sysdate, to_date(c.fe_nacimiento,'yyyyMMdd'))),12))||' mes(es)' edadEscrita " +
                "from rmtm_cnv c " +
                "left join rctm_nacimientos n on n.nu_cnv=c.nu_cnv " +
                "where co_madre_tipo_doc_identidad='01' and n.nu_cnv is null " +
                "and de_madre_doc_identidad=? and co_estado_cnv='01'";
        Object[] params = new Object[]{dni};
        logger.debug(String.format("DAO => RRCC '%s', con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Persona obtenerPorCnv(String cnv) {
        String query = "select c.nu_cnv cnv, " +
                "de_madre_primer_apellido segundoApellido, " +
                "de_madre_dom_direccion direccion, " +
                "co_nacido_natural_dpto||co_nacido_natural_prov||co_nacido_natural_dist as codigoUbigeo, " +
                "decode(c.co_nacido_sexo, '01', 'Masculino', '02', 'Femenino') genero, " +
                "decode(c.co_nacido_sexo, '01', '1', '02', '2') codigoGenero, " +
                "'1' madreTipoVinculo, " +
                "TO_CHAR(to_date(c.fe_nacimiento,'yyyyMMdd'), 'dd/MM/yyyy') as fechaNacimiento, " +
                "trunc((months_between(sysdate, to_date(c.fe_nacimiento,'yyyyMMdd')))/12) edad, " +
                "trunc((months_between(sysdate, to_date(c.fe_nacimiento,'yyyyMMdd')))/12)||' año(s), '||trunc(mod((months_between(sysdate, to_date(c.fe_nacimiento,'yyyyMMdd'))),12))||' mes(es)' edadEscrita, " +
                "co_madre_tipo_doc_identidad tipoDocumento, de_madre_doc_identidad madreDni, " +
                "de_madre_primer_apellido madrePrimerApellido, de_madre_segundo_apellido madreSegundoApellido, de_madre_prenombres madreNombres, " +
                "'MADRE' as madreTipoVinculo, '1' as madreCodigoTipoVinculo " +
                "from rmtm_cnv c " +
                "where c.nu_cnv=? and co_estado_cnv='01'";

        Object[] params = new Object[]{cnv};
        logger.debug(String.format("DAO => RRCC '%s', con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Persona.class), params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}