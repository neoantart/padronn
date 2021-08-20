package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.*;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.ReporteEuropan;
import pe.gob.reniec.padronn.logic.util.PadronProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 22/08/13
 * Time: 06:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ReporteDaoImpl extends AbstractBaseDao implements ReporteDao {
    Logger logger = Logger.getLogger(getClass());

    @Autowired
    PadronProperties padronProperties;

    @Autowired
    PadronProgramaDao padronProgramaDao;

    @Autowired
    UbigeoDao ubigeoDao;

    @Autowired
    UsuarioExternoDao usuarioExternoDao;

    @Autowired
    Usuario usuario;

    @Autowired
    EntidadDao entidadDao;

    /**
     * @param coUbigeoInei
     * @param esPadron
     * @param feIni
     * @param feFin
     * @param args         [0] tiRegistro
     * @return
     */
    @Override
    public List<PadronNominal> listarPadrones(String coUbigeoInei, String esPadron, String feIni, String feFin, String tiRegFecha, String... args) {
        String ubigeo = coUbigeoInei + "%";
        String filtroPeriodo = "";
        String filtroTiRegistro = "";
        String filtroEdad = "";
        String deBaja = "";
        String filtroEsPadron = "";

        if (coUbigeoInei.equals("00"))
            ubigeo = "%";
        /*String esPadron = activos ? "1":"0";*/
        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")) {
                    filtroPeriodo = " and (trunc(fe_crea_registro) between ? and ?) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")) {
                    filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")) {
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) " +
                            " and (trunc(fe_modi_registro) <= ? or fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) ";
            }
        }

        if (args != null && args.length > 0 && args[0] != null) {
            if (args[0].equals("0")) // todos
                filtroTiRegistro = "";
            if (args[0].equals("1")) {// Registro manual
                filtroTiRegistro = " and p.ti_registro='RM'  ";
            }
            if (args[0].equals("2")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='TR') ";
            }
            if (args[0].equals("3")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='SP' or p.ti_registro='WS') ";
            }
        }
        filtroEsPadron = " and p.es_padron=? ";
        filtroEdad = " and p.nu_edad<6 ";
        if (esPadron.equals("0")) {
            filtroEdad = " ";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad >= 6) ";
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(p.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(p.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(p.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(p.co_padron_nominal) AS de_observacion_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal) AS de_usuario_baja ";
        }

        String query = "" +
                "select p.co_padron_nominal, p.nu_dni_menor, nu_cnv, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "p.nu_cui, es_padron, " +
                "case " +
                "when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "es_ads.de_est_salud as de_est_salud_ads, " +
                "p.co_est_salud_ads as co_est_salud_ads, " +
                "nvl(p.nu_cel_madre,' ') as nuCelMadre, " +
                "nvl(p.di_correo_madre,' ') as diCorreoMadre, " +
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "es.de_est_salud as de_est_salud, " +
                "es.co_est_salud as co_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "ie.co_modular as co_inst_educativa, " +
                "ie.no_centro_educativo as de_inst_educativa, " +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe, " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre, " +
                "co_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre, " +
                "d6.de_grado_instruccion de_gra_inst_madre," +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='8'), 1, '8, ', '') " +
                "from dual) coProgramasSociales, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal  and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                " case " +
                "     when p.ti_registro='TR' then e.de_entidad " +
                "     when p.ti_registro='MQ' then 'MINSA' " +
                "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                "     else '' " +
                " end as deFuentePrecarga, \n" +
                " case " +
                "     when p.ti_registro='TR' then 'PRECARGA/ENTIDAD' " +
                "     when p.ti_registro='MQ' then 'HISMINSA' " +
                "     when p.ti_registro in ('WS','SP') then 'PRECARGA/EN LINEA'  " +
                "     when p.ti_registro='RM' then 'REGISTRO EN LINEA' " +
                " end as deCargaRegistro, \n" +
                " case \n" +
                "     when p.ti_registro='RM' THEN \n" +
                "       'REGISTRO MANUAL' \n" +
                "     when p.ti_registro='TR' THEN \n" +
                "       'TRAMA' \n" +
                "     when p.ti_registro='WS' THEN \n" +
                "       'WEB SERVICE' \n" +
                "     when p.ti_registro='SP' THEN \n" +
                "       'WEB SERVICE' \n" +
                "     when p.ti_registro='MQ' THEN \n" +
                "       'HISMINSA' \n" +
                " end as deTipoRegistro, " +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza, " +
                "case \n" +
                "   when p.US_CREA_REGISTRO in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.US_CREA_REGISTRO in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.US_CREA_REGISTRO in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   when p.US_CREA_REGISTRO in('HISMINSA') then 'SERVICIO HISMINSA' \n" +
                "   else p.US_CREA_REGISTRO \n" +
                "end as US_CREA_REGISTRO," +
                "TO_CHAR(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, " +
                "case \n" +
                "   when p.US_MODI_REGISTRO in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.US_MODI_REGISTRO in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.US_MODI_REGISTRO in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   when p.US_MODI_REGISTRO in('HISMINSA') then 'SERVICIO HISMINSA' \n" +
                "   else p.US_MODI_REGISTRO \n" +
                "end as US_MODI_REGISTRO," +
                "TO_CHAR(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro," +
                "nvl(fa.no_frecuencia_atencion,'') as deFrecAtencion " +
                deBaja +
                " from pntv_padron_nominal p " +
                " left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                " left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                " left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe " +
                " left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre " +
                " left join PNTR_FUENTE_DATOS FDM on FDM.co_fuente_datos=p.co_fuente_datos " +
                " left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza " +
                " left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre " +
                " left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                " left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                " left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa " +
                " left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_ads on es_ads.co_est_salud=p.co_est_salud_ads and es_ads.nu_secuencia_local=p.nu_secuencia_local_ads \n" +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac \n" +
                " left join PNTM_VIA V ON V.CO_VIA=P.CO_VIA \n" +
                " left join PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA  \n" +
                " left join pntm_entidad_envio e on e.co_entidad=p.fuente_precarga " +
                " left join pntd_frecuencia_atencion fa on p.co_frecuencia_atencion=fa.co_frecuencia_atencion \n" +
                " where p.co_ubigeo_inei like ? " +
                filtroPeriodo +
                filtroTiRegistro +
                filtroEsPadron +
                filtroEdad +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc";
        params.add(esPadron);

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesRangoFecha(String coUbigeoInei, String esPadron, String feIni, String feFin, String tiRegFecha, String... args) {
        String ubigeo = coUbigeoInei + "%";
        String filtroPeriodo = "";
        String filtroTiRegistro = "";
        String filtroEdad = "";
        String deBaja = "";
        String filtroEsPadron = "";

        if (coUbigeoInei.equals("00"))
            ubigeo = "%";
        /*String esPadron = activos ? "1":"0";*/
        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")) {
                    filtroPeriodo = " and (trunc(fe_crea_registro) between ? and ?) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")) {
                    filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")) {
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) " +
                            " and (trunc(fe_modi_registro) <= ? or fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) ";
            }
        }

        if (args != null && args.length > 0 && args[0] != null) {
            if (args[0].equals("0")) // todos
                filtroTiRegistro = "";
            if (args[0].equals("1")) {// Registro manual
                filtroTiRegistro = " and p.ti_registro='RM'  ";
            }
            if (args[0].equals("2")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='TR') ";
            }
            if (args[0].equals("3")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='SP' or p.ti_registro='WS') ";
            }
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")){
            filtroEsPadron = " and p.es_padron in('1','2') "; //TODOS
        }

        filtroEdad = " and p.nu_edad<6 ";

        if (esPadron.equals("0")) {
            filtroEdad = " ";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad >= 6) ";
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(p.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(p.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(p.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(p.co_padron_nominal) AS de_observacion_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal) AS de_usuario_baja ";
        }

        String query = "" +
                "select p.co_padron_nominal, p.nu_dni_menor, nu_cnv, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "p.nu_cui, " +
                "case " +
                "when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "es_ads.de_est_salud as de_est_salud_ads, " +
                "p.co_est_salud_ads as co_est_salud_ads, " +
                "nvl(p.nu_cel_madre,' ') as nuCelMadre, " +
                "nvl(p.di_correo_madre,' ') as diCorreoMadre, " +
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "es.de_est_salud as de_est_salud, " +
                "es.co_est_salud as co_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "ie.co_modular as co_inst_educativa, " +
                "ie.no_centro_educativo as de_inst_educativa, " +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe, " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre, " +
                "co_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre, " +
                "d6.de_grado_instruccion de_gra_inst_madre," +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='8'), 1, '8, ', '') " +
                "from dual) coProgramasSociales, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal  and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                " case " +
                "     when p.ti_registro='TR' then e.de_entidad " +
                "     when p.ti_registro='MQ' then 'MINSA' " +
                "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                "     else '' " +
                " end as deFuentePrecarga, \n" +
                " case " +
                "     when p.ti_registro='TR' then 'PRECARGA/ENTIDAD' " +
                "     when p.ti_registro='MQ' then 'HISMINSA' " +
                "     when p.ti_registro in ('WS','SP') then 'PRECARGA/EN LINEA'  " +
                "     when p.ti_registro='RM' then 'REGISTRO EN LINEA' " +
                " end as deCargaRegistro, \n" +
                " case \n" +
                "     when p.ti_registro='RM' THEN \n" +
                "       'REGISTRO MANUAL' \n" +
                "     when p.ti_registro='TR' THEN \n" +
                "       'TRAMA' \n" +
                "     when p.ti_registro='WS' THEN \n" +
                "       'WEB SERVICE' \n" +
                "     when p.ti_registro='SP' THEN \n" +
                "       'WEB SERVICE' \n" +
                "     when p.ti_registro='MQ' THEN \n" +
                "       'HISMINSA' \n" +
                " end as deTipoRegistro, " +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza, " +
                "case \n" +
                "   when p.US_CREA_REGISTRO in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.US_CREA_REGISTRO in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.US_CREA_REGISTRO in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   when p.US_CREA_REGISTRO in('HISMINSA') then 'SERVICIO HISMINSA' \n" +
                "   else p.US_CREA_REGISTRO \n" +
                "end as US_CREA_REGISTRO," +
                "TO_CHAR(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, " +
                "case \n" +
                "   when p.US_MODI_REGISTRO in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.US_MODI_REGISTRO in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.US_MODI_REGISTRO in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   when p.US_MODI_REGISTRO in('HISMINSA') then 'SERVICIO HISMINSA' \n" +
                "   else p.US_MODI_REGISTRO \n" +
                "end as US_MODI_REGISTRO," +
                "TO_CHAR(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro," +
                "nvl(fa.no_frecuencia_atencion,'') as deFrecAtencion " +
                deBaja +
                " from pntv_padron_nominal p " +
                " left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                " left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                " left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe " +
                " left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre " +
                " left join PNTR_FUENTE_DATOS FDM on FDM.co_fuente_datos=p.co_fuente_datos " +
                " left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza " +
                " left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre " +
                " left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                " left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                " left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa " +
                " left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_ads on es_ads.co_est_salud=p.co_est_salud_ads and es_ads.nu_secuencia_local=p.nu_secuencia_local_ads \n" +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac \n" +
                " left join PNTM_VIA V ON V.CO_VIA=P.CO_VIA \n" +
                " left join PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA  \n" +
                " left join pntm_entidad_envio e on e.co_entidad=p.fuente_precarga " +
                " left join pntd_frecuencia_atencion fa on p.co_frecuencia_atencion=fa.co_frecuencia_atencion \n" +
                " where p.co_ubigeo_inei like ? " +
                filtroPeriodo +
                filtroTiRegistro +
                filtroEsPadron +
                filtroEdad +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc";
        if(!esPadron.equals("3")){
            params.add(esPadron);
        }
        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    /**
     * @param coUbigeoInei
     * @param esPadron
     * @param feIni
     * @param feFin
     * @param filaIni
     * @param filaFin
     * @param args         [0] tiRegistro
     * @return
     */
    @Override
    public List<PadronNominal> listarPadrones(String coUbigeoInei, String esPadron, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String... args) {
        String ubigeo = coUbigeoInei + "%";
        String filtroPeriodo = "";
        String filtroTiRegistro = "";
        String filtroEdad = "";
        /*String esPadron = activos ? "1":"0";*/
        String deBaja = "";
        String filtroEsPadron = "";

        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")) {
                    filtroPeriodo = " and (trunc(fe_crea_registro) between ? and ?) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")) {
                    filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")) {
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) " +
                            " and (trunc(fe_modi_registro) <= ? or fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and  ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) ";
            }
        }

        if (args != null && args.length > 0 && args[0] != null) {
            if (args[0].equals("0")) // todos
                filtroTiRegistro = "";
            if (args[0].equals("1")) {// Registro manual
                filtroTiRegistro = " and p.ti_registro='RM'  ";
            }
            if (args[0].equals("2")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='TR') ";
            }
            if (args[0].equals("3")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='SP' or p.ti_registro='WS') ";
            }
        }
        filtroEdad = " and p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";
        if (esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad >= 6) ";
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(p.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(p.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(p.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(p.co_padron_nominal) AS de_observacion_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal) AS de_usuario_baja ";
        }

        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  " +
                "( " +
                "select p.co_padron_nominal, p.nu_dni_menor, nu_cnv, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "nu_cui, " +
                "case " +
                "when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "p.co_est_salud, " +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'PRECARGA'\n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE'\n" +
                "       end as deTipoRegistro, " +
                "es.de_est_salud as de_est_salud " +
                deBaja +
                " from pntv_padron_nominal p " +
                " left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on  d.CO_GENERO_MENOR=p.co_genero_menor " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "where p.co_ubigeo_inei like ?" +
                filtroPeriodo +
                filtroTiRegistro +
                filtroEsPadron +
                filtroEdad +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc) t " +
                "where rownum<=? ) " +
                "where fila>=?";

        params.add(esPadron);

        params.add(filaFin);

        params.add(filaIni);
        try {
            logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesRangoFecha(String coUbigeoInei, String esPadron, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String... args) {
        String ubigeo = coUbigeoInei + "%";
        String filtroPeriodo = "";
        String filtroTiRegistro = "";
        String filtroEdad = "";
        /*String esPadron = activos ? "1":"0";*/
        String deBaja = "";
        String filtroEsPadron = "";

        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")) {
                    filtroPeriodo = " and (trunc(fe_crea_registro) between ? and ?) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")) {
                    filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")) {
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) " +
                            " and (trunc(fe_modi_registro) <= ? or fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and  ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) ";
            }
        }

        if (args != null && args.length > 0 && args[0] != null) {
            if (args[0].equals("0")) // todos
                filtroTiRegistro = "";
            if (args[0].equals("1")) {// Registro manual
                filtroTiRegistro = " and p.ti_registro='RM'  ";
            }
            if (args[0].equals("2")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='TR') ";
            }
            if (args[0].equals("3")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='SP' or p.ti_registro='WS') ";
            }
        }
        filtroEdad = " and p.nu_edad<6 ";

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")){//TODOS
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }

        if (esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad >= 6) ";
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(p.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(p.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(p.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(p.co_padron_nominal) AS de_observacion_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal) AS de_usuario_baja ";
        }

        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  " +
                "( " +
                "select p.co_padron_nominal, p.nu_dni_menor, nu_cnv, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "nu_cui, " +
                "case " +
                "when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "p.co_est_salud, " +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'PRECARGA'\n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE'\n" +
                "       end as deTipoRegistro, " +
                "es.de_est_salud as de_est_salud " +
                deBaja +
                " from pntv_padron_nominal p " +
                " left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on  d.CO_GENERO_MENOR=p.co_genero_menor " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "where p.co_ubigeo_inei like ?" +
                filtroPeriodo +
                filtroTiRegistro +
                filtroEsPadron +
                filtroEdad +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc) t " +
                "where rownum<=? ) " +
                "where fila>=?";
        if(!esPadron.equals("3")) {//TODOS
            params.add(esPadron);
        }


        params.add(filaFin);

        params.add(filaIni);
        try {
            logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }


    /**
     * @param coUbigeoInei
     * @param esPadron
     * @param feIni
     * @param feFin
     * @param args         [0] tiRegistro
     * @return
     */
    @Override
    public int countListaPadrones(String coUbigeoInei, String esPadron, String feIni, String feFin, String tiRegFecha, String... args) {
        String ubigeo = coUbigeoInei + "%";

        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroTiRegistro = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String filtroEsPadron = "";

        params.add(ubigeo);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")) {
                    filtroPeriodo = " and (trunc(fe_crea_registro) between ? and ?) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")) {
                    filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")) {
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) " +
                            " and (trunc(fe_modi_registro) <= ? or fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) ";
            }
        }
        params.add(esPadron);

        if (args != null && args.length > 0 && args[0] != null) {
            if (args[0].equals("0")) // todos
                filtroTiRegistro = "";
            if (args[0].equals("1")) {// Registro manual
                filtroTiRegistro = " and p.ti_registro='RM'  ";
            }
            if (args[0].equals("2")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and p.ti_registro='TR' ";
            }
            if (args[0].equals("3")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='WS' or p.ti_registro='SP') ";
            }

        }

        filtroEdad = " and p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";
        if (esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and ( p.es_padron=? or p.nu_edad >= 6 ) ";
        }

        String query = "" +
                "select count(1) from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on p.co_est_salud=es.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "where p.co_ubigeo_inei like ?  " +
                filtroPeriodo +
                filtroTiRegistro +
                filtroEsPadron +
                filtroEdad;
        logger.debug(String.format("DAO '%s' por ejecutar XXXXX con: '%s'", query, params.toString()));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }


    @Override
    public int countListaPadronesRangoFecha(String coUbigeoInei, String esPadron, String feIni, String feFin, String tiRegFecha, String... args) {
        String ubigeo = coUbigeoInei + "%";

        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroTiRegistro = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String filtroEsPadron = "";

        params.add(ubigeo);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha !=null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(fe_crea_registro) between ? and ?) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) " +
                            " and (trunc(fe_modi_registro) <= ? or fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) ";
            }
        }
        if(!esPadron.equals("3")) {
            params.add(esPadron);
        }
        if (args != null && args.length > 0 && args[0] != null) {
            if(args[0].equals("0")) // todos
                filtroTiRegistro = "";
            if(args[0].equals("1")) {// Registro manual
                filtroTiRegistro = " and p.ti_registro='RM'  ";
            }
            if(args[0].equals("2")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and p.ti_registro='TR' ";
            }
            if(args[0].equals("3")) { // registro de precarga incluye RC y CM
                /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
                filtroTiRegistro = " and (p.ti_registro='WS' or p.ti_registro='SP') ";
            }

        }

        filtroEdad = " and p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";

        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in ('1','2') ";//activos y activos-observados
        }

        if(esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and ( p.es_padron=? or p.nu_edad >= 6 ) ";
        }

        String query = "" +
                "select count(1) from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on p.co_est_salud=es.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "where p.co_ubigeo_inei like ?  " +
                filtroPeriodo +
                filtroTiRegistro +
                filtroEsPadron +
                filtroEdad;
        logger.debug(String.format("DAO '%s' por ejecutar XXXXX con: '%s'", query, params.toString()));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }


    @Override
    public List<PadronNominal> listarPadronesByEntidad(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String esPadron, String tiRegFecha) {
        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String deBaja = "";
        String filtroEsPadron = "";
        String filtroTiRegistro = setFiltroTiRegistro(tiRegistro);

        params.add(coUbigeoInei);
        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?))";
            }
        }

        if(!esPadron.equals("3")){
            filtroEdad = " AND p.nu_edad<6 ";
            filtroEsPadron = " and p.es_padron = ? ";
            if (esPadron.equals("0")) {
                filtroEdad = "";
                filtroEsPadron = " and ( p.es_padron=? or p.nu_edad>=6 ) ";
                deBaja = "," +
                        "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(p.co_padron_nominal) AS co_motivo_baja, " +
                        "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(p.co_padron_nominal) AS de_motivo_baja, " +
                        "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(p.co_padron_nominal) AS fecha_baja, " +
                        "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(p.co_padron_nominal) AS de_observacion_baja, " +
                        "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal) AS de_usuario_baja ";
            }

        }

        String query = "" +
                "select DISTINCT /*p.co_entidad, e.de_entidad, e.de_entidad_larga,*/ co_padron_nominal, nu_dni_menor, nu_cui, p.nu_cnv as nuCnv," +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "es_ads.de_est_salud as de_est_salud_ads, " +
                "p.co_est_salud_ads as co_est_salud_ads, " +
                "p.co_est_salud, es_padron,"+
                "nvl(p.nu_cel_madre,' ') as nuCelMadre, "+
                "nvl(p.di_correo_madre,' ') as diCorreoMadre, "+
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado," +
                "es.de_est_salud as de_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "nu_afiliacion, " +
                "ie.co_modular as co_inst_educativa, " +
                "ie.no_centro_educativo as de_inst_educativa," +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe, " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='0'), 1, '0, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='8'), 1, '8, ', '') " +
                " from dual) coProgramasSociales, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                " case when p.ti_registro='TR' then p.fuente_precarga else '' end as coFuentePrecarga, \n" +
                " case " +
                "     when p.ti_registro='TR' then e.de_entidad " +
                "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                "     else '' " +
                " end as deFuentePrecarga, \n" +
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "co_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre, " +
                "d6.de_grado_instruccion de_gra_inst_madre," +
                "case \n" +
                "    when p.ti_registro='RM' THEN \n" +
                "       'REGISTRO MANUAL' \n" +
                "    when p.ti_registro='TR' THEN \n" +
                "       'TRAMA' \n" +
                "    when p.ti_registro in ('WS','SP') THEN \n" +
                "       'WEB SERVICE' \n" +
                "end as deTipoRegistro, " +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza, " +
                "case \n" +
                "   when p.us_crea_registro in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.us_crea_registro in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.us_crea_registro in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   else p.us_crea_registro \n" +
                "end as us_crea_registro, \n" +
                "to_char(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, \n" +
                "case \n" +
                "   when p.us_modi_registro in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.us_modi_registro in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.us_modi_registro in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   else p.us_modi_registro \n" +
                "end as us_modi_registro, \n" +
                "to_char(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro, " +
                "nvl(fa.no_frecuencia_atencion,'') as deFrecAtencion\n" +
                deBaja +
                " from pntv_padron_nominal p " +
                " left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local "  +
                " left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                " left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe " +
                " left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre " +
                " left join PNTR_FUENTE_DATOS FDM on FDM.co_fuente_datos=p.co_fuente_datos " +
                " left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza " +
                " left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre " +
                " left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                " left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                " left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa " +
                " left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_ads on es_ads.co_est_salud=p.co_est_salud_ads and es_ads.nu_secuencia_local=p.nu_secuencia_local_ads " +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                " left join PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                " left join PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                " left join pntm_entidad_envio e on e.co_entidad=p.fuente_precarga " +
                " left join pntd_frecuencia_atencion fa on p.co_frecuencia_atencion=fa.co_frecuencia_atencion " +
                "where p.co_ubigeo_inei=?  " +
                filtroEsPadron +
                filtroTiRegistro +
                filtroPeriodo +
                filtroEdad +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc";

       /* params.add(esPadron);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {

        }*/
        logger.debug(String.format("DAO '%s' con DESCARGA '%s'", query, params));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByEntidad(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String esPadron, int filaIni, int filaFin, String tiRegFecha) {
        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String deBaja = "";
        String filtroEsPadron = "";
        String filtroTiRegistro = setFiltroTiRegistro(tiRegistro);

        params.add(coUbigeoInei);
        if (!esPadron.equals("3"))
            params.add(esPadron);

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and((trunc(p.fe_crea_registro) between ? and ?)or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }
        params.add(filaFin);
        params.add(filaIni);
/*
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {

            *//*and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?))*//*
        }*/
        if (!esPadron.equals("3")){
            filtroEdad = " and p.nu_edad<6 ";
            filtroEsPadron = " and p.es_padron=? ";
            if (esPadron.equals("0")) {
                filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6 ) ";
                filtroEdad = "";
            }
        }

        if(esPadron.equals("0") || esPadron.equals("3")) {
           /* filtroEdad = "";*//* Se modifico < en edad *//*
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad <= 6) ";*/
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(p.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(p.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(p.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(p.co_padron_nominal) AS de_observacion_baja, " +
                    " case when PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal)='SPDEFDNI' then 'RENIEC' else PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal) end AS de_usuario_baja ";
        }


        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select DISTINCT /*p.co_entidad, e.de_entidad, e.de_entidad_larga, */co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "p.co_est_salud, " +
                "nvl(p.nu_cel_madre,' ') as nuCelMadre, " +
                "nvl(p.di_correo_madre,' ') as diCorreoMadre, " +
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA'\n" +
                "  end as deTipoRegistro, " +
                "es.de_est_salud as de_est_salud " +
                deBaja +
                " from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                "left join PNTR_FUENTE_DATOS FDM on FDM.CO_FUENTE_DATOS=p.co_fuente_datos " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "where p.co_ubigeo_inei=?  " +
                filtroEsPadron +
                filtroPeriodo + filtroTiRegistro +
                filtroEdad +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc) t " +
                "where rownum<=?)  " +
                "where fila>=?";

        logger.debug(String.format("DAO '%s' por ejecutar con CONSULTA:: '%s'", query, params));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String setFiltroTiRegistro(String tiRegistro) {
        String filtroTiRegistro = "";
        if (tiRegistro.equals("RM")) {
            filtroTiRegistro = " and (p.ti_registro='RM' or p.us_modi_registro='SPDEFDNI') ";
        } else if(tiRegistro.equals("ALL")) {
            filtroTiRegistro = "";
        } else {
            /*filtroTiRegistro = " and (p.ti_registro='CM' or p.ti_registro='RC') ";*/
            filtroTiRegistro = " and (p.ti_registro='TR' or p.ti_registro='WS') ";
        }
        return filtroTiRegistro;
    }

    @Override
    public int countListarPadronesByEntidad(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String esPadron, String tiRegFecha) {
        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String filtroEsPadron = "";
        String filtroTiRegistro = setFiltroTiRegistro(tiRegistro);

        params.add(coUbigeoInei);
        if(!esPadron.equals("3"))
            params.add(esPadron);

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }

        if(!esPadron.equals("3")) {
            filtroEdad = " AND p.nu_edad<6 ";
            filtroEsPadron = " and p.es_padron=? ";
            if (esPadron.equals("0")) {
                filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6 ) ";
                filtroEdad = "";
            }
        }

        String query = "" +
                "select count(1) from pntv_padron_nominal p "  +
                "where p.co_ubigeo_inei=? " +
                filtroEsPadron +
                filtroPeriodo + filtroTiRegistro +
                filtroEdad;
        logger.debug(String.format("DAO '%s' por ejecutar con inactivos: '%s'", query, params.toString()));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }

    @Override
    public List<Lugar> getUbigeo(String deUbigeo) {
        String filtroEntidadUbigeo = "";
        String ubigeo = "%" + deUbigeo.toUpperCase() + "%";
        List<Object> params = new ArrayList<Object>();

        if (esEntidadFitrarUbigeo()) {
            filtroEntidadUbigeo = " WHERE coLugar like ? ";
            params.add(usuario.getCoUbigeoInei() + "%");
        }
        String query = "" +
                "SELECT * " +
                "  FROM (SELECT * " +
                "          FROM ((SELECT DISTINCT SUBSTR(co_ubigeo_inei, 1, 2) AS coLugar, " +
                "                                 de_departamento AS deLugar " +
                "                   FROM pntm_ubigeo_inei where es_ubigeo='1' ) UNION " +
                "                (SELECT DISTINCT SUBSTR(co_ubigeo_inei, 1, 4) AS coLugar, " +
                "                                 de_departamento || ',' || de_provincia AS deLugar " +
                "                   FROM pntm_ubigeo_inei where es_ubigeo='1' ) UNION " +
                "                (SELECT co_ubigeo_inei AS coLugar, " +
                "                        de_departamento || ',' || de_provincia || ',' || " +
                "                        de_distrito AS deLugar " +
                "                   FROM pntm_ubigeo_inei where es_ubigeo='1' )) " +
                filtroEntidadUbigeo +
                ") " +
                " WHERE UPPER(deLugar) LIKE ? " +
                " ORDER BY coLugar";
        params.add(ubigeo);
        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        return this.jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Lugar.class), params.toArray(new Object[params.size()]));
    }

    @Override
    public Lugar getLugar(String coUbigeo) {

        Object[] params = new Object[]{coUbigeo};
        String query = "" +
                "SELECT *  " +
                "FROM (  " +
                "  (SELECT DISTINCT SUBSTR(co_ubigeo_inei, 1, 2) AS coLugar,  " +
                "    de_departamento                             AS deLugar  " +
                "  FROM pntm_ubigeo_inei where es_ubigeo='1' " +
                "  )  " +
                "UNION  " +
                "  (SELECT DISTINCT SUBSTR(co_ubigeo_inei, 1, 4) AS coLugar,  " +
                "    de_departamento  " +
                "    || ','  " +
                "    || de_provincia AS deLugar  " +
                "  FROM pntm_ubigeo_inei where es_ubigeo='1' " +
                "  )  " +
                "UNION  " +
                "  (SELECT co_ubigeo_inei AS coLugar,  " +
                "    de_departamento  " +
                "    || ','  " +
                "    || de_provincia  " +
                "    || ','  " +
                "    || de_distrito AS deLugar  " +
                "  FROM pntm_ubigeo_inei  where es_ubigeo='1' " +
                "  )) t  " +
                "WHERE coLugar = ? ";
        return this.jdbcTemplate.queryForObject(query, params, BeanPropertyRowMapper.newInstance(Lugar.class));
    }

    @Override
    public List<Lugar> buscarEntidad(String deEntidad) {
        String fitroEntidadUbigeo = "";
        List<Object> params = new ArrayList<Object>();
        if (esEntidadFitrarUbigeo()) {
            fitroEntidadUbigeo = " WHERE coUbigeo LIKE ?";
        }

        String query = "" +
                "SELECT * " +
                "        FROM (SELECT E.CO_ENTIDAD AS coLugar, " +
                "                E.DE_ENTIDAD_LARGA AS deLugar, " +
                "                U.CO_UBIGEO_INEI AS coUbigeo, " +
                "                (U.DE_DEPARTAMENTO || ',' || U.DE_PROVINCIA || ',' || " +
                "                        U.DE_DISTRITO) AS DEUBIGEO " +
                "                FROM PNTM_ENTIDAD E " +
                "                LEFT JOIN pntm_ubigeo_inei U " +
                "                ON U.CO_UBIGEO_INEI = E.CO_UBIGEO_INEI  " +
                "                WHERE (UPPER(E.DE_ENTIDAD) LIKE ? OR " +
                "UPPER(E.DE_ENTIDAD_LARGA) LIKE ? OR UPPER(U.DE_DEPARTAMENTO) LIKE ? OR UPPER(U.DE_PROVINCIA) LIKE ? OR UPPER(U.DE_DISTRITO) LIKE ?) " +
                "AND U.ES_UBIGEO='1' AND E.ES_ENTIDAD='1' AND CO_TIPO_ENTIDAD='1'" +
                "AND E.CO_ENTIDAD NOT IN ('1843', '380', '1841', '1842')) " +
                fitroEntidadUbigeo;
        deEntidad = "%" + deEntidad.toUpperCase() + "%";
        params.add(deEntidad);
        params.add(deEntidad);
        params.add(deEntidad);
        params.add(deEntidad);
        params.add(deEntidad);
        if (esEntidadFitrarUbigeo()) {
            params.add(usuario.getCoUbigeoInei() + "%");
        }
        logger.debug("DAO: getEntidad" + query + " con: " + params.toString());
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Lugar.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Lugar getEntidad(String coEntidad) {
        String[] split = coEntidad.split("_");
        Object[] params = new Object[]{split[0]};
        String query = "SELECT CO_ENTIDAD coLugar, DE_ENTIDAD_LARGA deLugar " +
                "FROM " +
                "PNTM_ENTIDAD " +
                "WHERE CO_ENTIDAD=? and es_entidad='1' ";
        return this.jdbcTemplate.queryForObject(query, params, BeanPropertyRowMapper.newInstance(Lugar.class));
    }


    @Override
    public List<EstablecimientoSalud> buscarEstablecimientoSalud(String deEstSalud) {
        deEstSalud = "%" + deEstSalud.toUpperCase() + "%";
        List<Object> params = new ArrayList<Object>();
        String filtroEntidadUbigeo = "";
        if (esEntidadFitrarUbigeo()) {
            filtroEntidadUbigeo = " where co_ubigeo_inei like ? ";
            params.add(usuario.getCoUbigeoInei() + "%");
        }

        String query = "" +
                "select * " +
                "  from (select CO_EST_SALUD, " +
                "               NU_SECUENCIA_LOCAL, " +
                "               DE_EST_SALUD, " +
                "               TI_EST_SALUD, " +
                "               DE_DEPARTAMENTO, " +
                "               DE_PROVINCIA, " +
                "               DE_DISTRITO, " +
                "               DE_DIRECCION, " +
                "               US_CREA_AUDI, " +
                "               FE_CREA_AUDI, " +
                "               US_MODI_AUDI, " +
                "               FE_MODI_AUDI, " +
                "               CO_UBIGEO_INEI, " +
                "'"+usuario.getCoUbigeoInei() + "' co_ubigeo_us" +
                "          from pnvm_establecimiento_salud " +
                filtroEntidadUbigeo +
                ") " +
                " where ((upper(de_est_salud) like upper(?)) or de_departamento like ? or " +
                "       de_provincia like ? or de_distrito like ?) " +
                "   and rownum < 100";
        params.add(deEstSalud);
        params.add(deEstSalud);
        params.add(deEstSalud);
        params.add(deEstSalud);
        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        return this.jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class), params.toArray(new Object[params.size()] ));
    }

    @Override
    public List<EstablecimientoSalud> buscarEstablecimientoSaludRecienNacido(String deEstSalud, String coUbigeoInei) {
        deEstSalud = "%" + deEstSalud.toUpperCase() + "%";
        List<Object> params = new ArrayList<Object>();
        String filtroEntidadUbigeo = "";
        if (esEntidadFitrarUbigeo()) {
            filtroEntidadUbigeo = " where co_ubigeo_inei like ? ";
            params.add(usuario.getCoUbigeoInei() + "%");
        }

        String query = "" +
                "select * " +
                "  from (select CO_EST_SALUD, " +
                "               NU_SECUENCIA_LOCAL, " +
                "               DE_EST_SALUD, " +
                "               TI_EST_SALUD, " +
                "               DE_DEPARTAMENTO, " +
                "               DE_PROVINCIA, " +
                "               DE_DISTRITO, " +
                "               DE_DIRECCION, " +
                "               US_CREA_AUDI, " +
                "               FE_CREA_AUDI, " +
                "               US_MODI_AUDI, " +
                "               FE_MODI_AUDI, " +
                "               CO_UBIGEO_INEI " +
                "          from pnvm_establecimiento_salud " +
                filtroEntidadUbigeo +
                ") " +
                " where ((upper(de_est_salud) like upper(?))   or de_departamento like ? or " +
                "       de_provincia like ? or de_distrito like ?)and co_ubigeo_inei like ?" ;
//                "   and rownum < 100";
        params.add(deEstSalud);
        params.add(deEstSalud);
        params.add(deEstSalud);
        params.add(deEstSalud);
        params.add(coUbigeoInei+"%");
        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        return this.jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class), params.toArray(new Object[params.size()]));
    }

    @Override
    public EstablecimientoSalud getEstablecimientoSalud(String coEstSalud, String nuSecuenciaLocal) {
        Object[] params = new Object[]{coEstSalud, nuSecuenciaLocal};
        String query = "" +
                "select CO_EST_SALUD, " +
                "  NU_SECUENCIA_LOCAL, " +
                "  DE_EST_SALUD, " +
                "  TI_EST_SALUD, " +
                "  DE_DEPARTAMENTO, " +
                "  DE_PROVINCIA, " +
                "  DE_DISTRITO, " +
                "  DE_DIRECCION, " +
                "  US_CREA_AUDI, " +
                "  FE_CREA_AUDI, " +
                "  US_MODI_AUDI, " +
                "  FE_MODI_AUDI, " +
                "  CO_UBIGEO_INEI  " +
                "from pnvm_establecimiento_salud " +
                "where co_est_salud=? and nu_secuencia_local=?";
//        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        return this.jdbcTemplate.queryForObject(query, params, BeanPropertyRowMapper.newInstance(EstablecimientoSalud.class));
    }


    private List<Dominio> listarProgramas(String coPadronNominal) {
        String query = "SELECT pr.CO_PROGRAMA_SOCIAL as co_dominio, pr.DE_PROGRAMA as de_dom " +
                "FROM PNTV_PADRON_PROGRAMA p " +
                "JOIN PNTM_PROGRAMA_SOCIAL pr on pr.co_programa_social=p.CO_PROGRAMA_SOCIAL " +
                "WHERE p.co_padron_nominal=? ";
//        logger.debug(String.format("DAO '%s' por ejecutar con: '%s", query, coPadronNominal));
        return this.jdbcTemplate.query(query, new Object[]{coPadronNominal}, BeanPropertyRowMapper.newInstance(Dominio.class));
    }

    @Override
    public List<PadronNominal> listarPadronesByEstablecimientoSalud(String coEstSalud, String feIni, String feFin, String esPadron, String tiRegFecha) {
        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroEdad = "";
        String deBaja = "";
        String filtroEsPadron = "";
        List<Object> params = new ArrayList<Object>();

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }
        filtroEdad = " AND p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6) ";
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(p.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(p.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(p.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(p.co_padron_nominal) AS de_observacion_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal) AS de_usuario_baja ";
        }
        String query = "" +
                "select co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, es_padron, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "es_ads.de_est_salud as de_est_salud_ads, " +
                "p.co_est_salud_ads as co_est_salud_ads, " +
                "nvl(p.nu_cel_madre,' ') as nuCelMadre, "+
                "nvl(p.di_correo_madre,' ') as diCorreoMadre, "+
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "p.co_est_salud, "+
                "es.de_est_salud as de_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "/*ti_seguro_menor, d4.de_seguro_menor as de_seguro_menor,*/ nu_afiliacion, " +
                "ie.co_modular as co_inst_educativa, " +
                "p.co_centro_poblado, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal  and co_programa_social='0'), 1, '0, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='8'), 1, '8, ', '') " +
                "from dual) coProgramasSociales, " +

                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                " case when p.ti_registro='TR' then p.fuente_precarga else '' end as coFuentePrecarga, \n" +
                " case " +
                "     when p.ti_registro='TR' then en.de_entidad " +
                "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                "     when p.ti_registro='MQ' then 'HISMINSA' " +
                "     else '' " +
                " end as deFuentePrecarga, \n" +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "ie.no_centro_educativo de_inst_educativa," +
                "" +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe, " +
                "ie.co_modular as co_inst_educativa, " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre, " +
                "co_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre, " +
                "d6.de_grado_instruccion de_gra_inst_madre," +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'REGISTRO MANUAL'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro in ('WS','SP') THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza, " +
                "CASE \n" +
                "   WHEN US_CREA_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN US_CREA_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN US_CREA_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN US_CREA_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE US_CREA_REGISTRO \n" +
                "END AS US_CREA_REGISTRO," +
                "to_char(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, " +
                "CASE \n" +
                "   WHEN US_MODI_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN US_MODI_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN US_MODI_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN US_MODI_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE US_MODI_REGISTRO \n" +
                "END AS US_MODI_REGISTRO," +
                "to_char(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro, \n" +
                "nvl(fa.no_frecuencia_atencion,'') as deFrecAtencion " +
                deBaja +
                " from pntv_padron_nominal p " +
                " left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local "  +
                " left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                " left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe " +
                " left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre " +
                " left join PNTR_FUENTE_DATOS FDM on FDM.co_fuente_datos=p.co_fuente_datos " +
                " left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza " +
                " /*left join pntr_seguro_salud d4 on d4.ti_seguro_menor=p.ti_seguro_menor*/ " +
                " left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre " +
                " left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                " left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                " left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa " +
                " left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_ads on es_ads.co_est_salud=p.co_est_salud_ads and es_ads.nu_secuencia_local=p.nu_secuencia_local_ads " +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                " left join PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                " left join PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                " left join pntm_entidad_envio en on en.co_entidad=p.fuente_precarga " +
                " left join pntd_frecuencia_atencion fa on p.co_frecuencia_atencion=fa.co_frecuencia_atencion " +
                " where p.co_est_salud = ? " +
                filtroEsPadron +
                filtroPeriodo +
                filtroEdad +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc";
        params.add(coEstSalud);
        params.add(esPadron);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()){
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")){
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public List<PadronNominal> listarPadronesByEstablecimientoSalud(String coEstSalud, String feIni, String feFin, String esPadron, int filaIni, int filaFin, String tiRegFecha) {
        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String deBaja = "";
        String filtroEsPadron = "";

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }
        filtroEdad = " AND p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6) ";
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(p.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(p.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(p.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(p.co_padron_nominal) AS de_observacion_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(p.co_padron_nominal) AS de_usuario_baja ";
        }
        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_est_salud, " +
                "es.de_est_salud as de_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "p.co_centro_poblado, " +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado " +
                deBaja +
                " from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "where p.co_est_salud = ? " +
                filtroEsPadron +
                filtroPeriodo +
                filtroEdad+
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc) t " +
                "where rownum<=?)  " +
                "where fila>=?";
        params.add(coEstSalud);
        params.add(esPadron);

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()) {
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")) {
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }
        params.add(filaFin);

        params.add(filaIni);
        try {
            logger.debug(String.format("DAO '%s' con '%s'", query, params));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListarPadronesByEstablecimientoSalud(String coEstSalud, String feIni, String feFin, String esPadron, String tiRegFecha){
        /*String esPadron = activos?"1" : "0";*/
        String filtroPeriodo = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String filtroEsPadron = "";

        params.add(coEstSalud);
        filtroEdad = " AND p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6) ";
        }
        params.add(esPadron);

        /*if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
        }*/
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
                params.add(feIni);
                params.add(feFin);
            }
        }

        String query = "" +
                "SELECT COUNT(1) " +
                "FROM pntv_padron_nominal p left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "where p.co_est_salud = ?  " +
                filtroEsPadron +
                filtroPeriodo + filtroEdad;

        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }

    @Override
    public List<PadronNominal> listarPadronesByProgramaSocial(String coProgramaSocial, String feIni, String feFin, String coEntidad, String esPadron, String tiRegFecha) {
        Entidad entidad = entidadDao.getEntidad(coEntidad);
        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String deBaja = "";
        String filtroEsPadron = "";

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }
        filtroEdad = " AND p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6 )";
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(pp.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(pp.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(pp.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(pp.co_padron_nominal) AS de_observacion_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(pp.co_padron_nominal) AS de_usuario_baja ";
        }
        String query = "" +
                "select pp.co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor,  " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia,  " +
                "es_ads.de_est_salud as de_est_salud_ads, " +
                "p.co_est_salud_ads as co_est_salud_ads, " +
                "p.co_est_salud, "+
                "es.de_est_salud as de_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "nvl(p.nu_cel_madre,' ') as nuCelMadre, "+
                "nvl(p.di_correo_madre,' ') as diCorreoMadre, "+
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "nu_afiliacion,  " +
                "ie.co_modular as co_inst_educativa,  " +
                "p.co_centro_poblado, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal  and co_programa_social='0'), 1, '0, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='8'), 1, '8, ', '') " +
                " from dual) coProgramasSociales, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal  and in_afiliado='1' and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                " case when p.ti_registro='TR' then p.fuente_precarga else '' end as coFuentePrecarga, \n" +
                " case " +
                "     when p.ti_registro='TR' then en.de_entidad " +
                "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                "     when p.ti_registro='MQ' then 'HISMINSA' " +
                "     else '' " +
                " end as deFuentePrecarga, \n" +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "ie.no_centro_educativo de_inst_educativa," +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe,  " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre,  " +
                "co_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre,  " +
                "d6.de_grado_instruccion de_gra_inst_madre," +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'REGISTRO MANUAL'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro in ('WS','SP') THEN\n" +
                "          'WEB SERVICE' \n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza, " +
                "case \n" +
                "   when p.us_crea_registro in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.us_crea_registro in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.us_crea_registro in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   when p.us_crea_registro in('HISMINSA') then 'SERVICIO HISMINSA' \n" +
                "   else p.us_crea_registro \n" +
                "end as us_crea_registro," +
                "to_char(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, " +
                "case \n" +
                "   when p.us_modi_registro in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.us_modi_registro in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.us_modi_registro in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   when p.us_modi_registro in('HISMINSA') then 'SERVICIO HISMINSA' \n" +
                "   else p.us_modi_registro \n" +
                "end as us_modi_registro," +
                "to_char(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro, \n" +
                "nvl(fa.no_frecuencia_atencion,'')  as deFrecAtencion \n" +
                deBaja +
                " from PNTV_PADRON_PROGRAMA pp  " +
                "join pntv_padron_nominal p on p.co_padron_nominal=pp.co_padron_nominal " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local "  +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor  " +
                "left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe  " +
                "left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre  " +
                "left join PNTR_FUENTE_DATOS FDM on FDM.co_fuente_datos=p.co_fuente_datos  " +
                "left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza  " +
                " left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre  " +
                " left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                " left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                " left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa  " +
                " left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_ads on es_ads.co_est_salud=p.co_est_salud_ads and es_ads.nu_secuencia_local=p.nu_secuencia_local_ads " +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                " LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                " LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                " left join pntm_entidad_envio en on en.co_entidad=p.fuente_precarga" +
                " left join pntd_frecuencia_atencion fa on p.co_frecuencia_atencion=fa.co_frecuencia_atencion " +
                " where pp.co_programa_social = ? " +
                filtroEsPadron +
                "and p.co_ubigeo_inei=? " +
                filtroEdad + filtroPeriodo +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc" +
                "/*order by p.fe_crea_registro desc*/";
        params.add(coProgramaSocial);
        params.add(esPadron);
        params.add(entidad.getCoUbigeoInei());
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()) {
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")) {
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesByProgramaSocial(String coProgramaSocial, String feIni, String feFin, String coEntidad, String esPadron, int filaIni, int filaFin, String tiRegFecha) {
        Entidad entidad = entidadDao.getEntidad(coEntidad);
        /*String esPadron = activos ? "1":"0";*/
        String filtroPeriodo = "";
        String filtroEdad =  "";
        List<Object> params = new ArrayList<Object>();
        String deBaja = "";
        String filtroEsPadron = "";

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }
        filtroEdad = " AND p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6 )";
            deBaja = "," +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_CO_MOTIVO_BAJA(pp.co_padron_nominal) AS co_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_MOTIVO_BAJA(pp.co_padron_nominal) AS de_motivo_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_FE_CREA_BAJA(pp.co_padron_nominal) AS fecha_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_DE_OBSERVACION_BAJA(pp.co_padron_nominal) AS de_observacion_baja, " +
                    "PNPK_OBTENER_DETALLE_BAJA.FU_OBTENER_US_CREA_BAJA(pp.co_padron_nominal) AS de_usuario_baja ";
        }
        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select pp.co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor,  " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia,  " +
                "p.co_est_salud, "+
                "es.de_est_salud as de_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +

                "p.co_centro_poblado, " +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado " +
                deBaja +
                " from PNTV_PADRON_PROGRAMA pp  " +
                "left join pntv_padron_nominal p on p.co_padron_nominal=pp.co_padron_nominal " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on d.co_genero_menor=p.co_genero_menor  " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "where pp.co_programa_social = ? " +
                filtroEsPadron +
                "and p.co_ubigeo_inei=? " +
                filtroEdad +  filtroPeriodo +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc) t " +
                "where rownum<=?)  " +
                "where fila>=?";
        params.add(coProgramaSocial);
        params.add(esPadron);
        params.add(entidad.getCoUbigeoInei());
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()){
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")){
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }
        params.add(filaFin);
        params.add(filaIni);
        try {
            log.info(String.format("DAO '%s' con '%s'", query, params));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListarPadronesByProgramaSocial(String coProgramaSocial, String feIni, String feFin, String coEntidad, String esPadron, String tiRegFecha){
        Entidad entidad = entidadDao.getEntidad(coEntidad);
        /*String esPadron = activos ? "1" : "0";*/
        String filtroPeriodo = "";
        String filtroEdad = "";
        List<Object> params = new ArrayList<Object>();
        String filtroEsPadron = "";

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }
        filtroEdad = " AND p.nu_edad<6 ";
        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("0")){
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6) ";
        }
        String query = "" +
                "SELECT COUNT(1)  " +
                "FROM PNTV_PADRON_PROGRAMA  pp left join pntv_padron_nominal p on p.co_padron_nominal=pp.co_padron_nominal " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local "  +
                "where pp.co_programa_social = ? " +
                filtroEsPadron +
                "and p.co_ubigeo_inei=? " +
                filtroEdad + filtroPeriodo;

        params.add(coProgramaSocial);
        params.add(esPadron);
        params.add(entidad.getCoUbigeoInei());
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()) {
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")) {
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }
//          String[] split = coEntidad.split("_");

        log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params.toArray())));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }


    @Override
    public List<ProgramaSocial> getProgramasSociales() {
        String query =  "SELECT CO_PROGRAMA_SOCIAL, " +
                        "  DE_PROGRAMA, " +
                        "  DE_PROGRAMA_LARGA, " +
                        "  USU_CREA_AUDI, " +
                        "  FE_CREA_AUDI, " +
                        "  USU_MODI_AUDI, " +
                        "  FE_MODI_AUDI, " +
                        "  ES_PROGRAMA " +
                        "FROM PNTM_PROGRAMA_SOCIAL " +
                        "where es_programa='1' order by co_programa_social asc";

        logger.debug(String.format("DAO '%s' por ejecutar", query));
        return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(ProgramaSocial.class));
    }


    @Override
    public List<Lugar> getCantidadesDepartamento() {
        Integer coConsolidado;
        coConsolidado = jdbcTemplate.queryForInt("select co_consolidado from pntv_consolidado where es_consolidado='1'");
        if (coConsolidado != 0) {
            String query = "" +
                    "select substr(co_ubigeo_inei, 0, 2) co_ubigeo, de_departamento as de_ubigeo, sum(nu_sin_dni) as nu_sin_dni, sum(nu_con_dni) as nu_con_dni, sum(nu_total) as nu_total, sum(nu_cui) as nu_cui" +
                    "  from pntv_consolidado_ubigeo cu " +
                    "where co_consolidado=? and es_consolidado_ubigeo='1' " +
                    "group by substr(co_ubigeo_inei, 0, 2), de_departamento " +
                    "order by sum(cu.nu_total) desc";
            logger.debug(String.format("DAO '%s' con '%s'", query, coConsolidado));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Lugar.class), coConsolidado);
        }
        return null;
    }

    private String getDeDepartamento(String coDep) {
        String query = "select distinct de_departamento from pntm_ubigeo_inei where SUBSTR(co_ubigeo_inei, 1,2)=?";
        Object[] params = new Object[]{coDep};
        log.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForObject(query, String.class, params);
    }


    @Override
    public List<Lugar> getCantidadesProvincia(String coUbigeo) {
        coUbigeo = coUbigeo + "%";
        Integer coConsolidado;
        coConsolidado = jdbcTemplate.queryForInt("select co_consolidado from pntv_consolidado where es_consolidado='1'");
        if (coConsolidado != 0) {
            String query = "" +
                    "select substr(co_ubigeo_inei, 0, 4) co_ubigeo, de_provincia as de_ubigeo, sum(nu_sin_dni) as nu_sin_dni, sum(nu_con_dni) as nu_con_dni, sum(nu_total) as nu_total, sum(nu_cui) as nu_cui " +
                    "  from pntv_consolidado_ubigeo cu " +
                    "where co_consolidado=? and co_ubigeo_inei like ? and es_consolidado_ubigeo='1' " +
                    "group by substr(co_ubigeo_inei, 0, 4), de_provincia " +
                    "order by sum(cu.nu_total) desc ";
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Lugar.class), coConsolidado, coUbigeo);
        }
        return null;
    }

    private String getDeProvincia(String coUbigeo){
        coUbigeo = coUbigeo + "%";
        String query = "select distinct de_provincia from pntm_ubigeo_inei where co_ubigeo_inei LIKE ?";
        Object[] params = new Object[]{coUbigeo};
//        log.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        try{
            return jdbcTemplate.queryForObject(query, String.class, params);
        }
        catch (EmptyResultDataAccessException erdae){
            //erdae.printStackTrace();
            return "";
        }
        catch (IncorrectResultSizeDataAccessException e){
            //e.printStackTrace();
            return "";
        }
    }

    @Override
    public List<Lugar> getCantidadDistrito(String coUbigeo){
        coUbigeo = coUbigeo + "%";
        Integer coConsolidado;
        coConsolidado = jdbcTemplate.queryForInt("select co_consolidado from pntv_consolidado where es_consolidado='1'");
        if (coConsolidado != 0) {
            String query = "" +
                    "select co_ubigeo_inei co_ubigeo, de_distrito as de_ubigeo, sum(nu_sin_dni) nu_sin_dni, sum(nu_con_dni) nu_con_dni, sum(nu_total) nu_total, sum(nu_cui) nu_cui " +
                    "  from pntv_consolidado_ubigeo cu  " +
                    "where co_consolidado=? and co_ubigeo_inei like ? and es_consolidado_ubigeo='1' " +
                    "group by co_ubigeo_inei, de_distrito " +
                    "order by sum(cu.nu_total) desc ";
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Lugar.class), coConsolidado, coUbigeo);
        }
        return null;
    }

    private String getDeDistrito(String coUbigeo) {
        String query = "select distinct de_distrito from pntm_ubigeo_inei where co_ubigeo_inei=?";
        Object[] params = new Object[]{coUbigeo};
        log.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        try{
            return jdbcTemplate.queryForObject(query, String.class, params);
        } catch (EmptyResultDataAccessException er){
            er.printStackTrace();
            return "";
        } catch (IncorrectResultSizeDataAccessException ie){
            ie.printStackTrace();
            return "";
        }
    }

    @Override
    public List<Lugar> getCantidadesPorEntidad(String coUbigeoInei) {
        coUbigeoInei = coUbigeoInei + "%";
        if(coUbigeoInei.equals("00"))
            coUbigeoInei = "%";
        String query = "" +
                "SELECT t.co_entidad AS coLugar, " +
                "  (SELECT de_entidad_larga " +
                "  FROM pntm_entidad " +
                "  WHERE co_entidad=t.co_entidad and es_entidad='1' " +
                "  )    AS deLugar, " +
                "  t.cc AS cantidad " +
                "FROM ( " +
                "  (SELECT pn.co_entidad, " +
                "    COUNT(1) AS cc " +
                "  FROM pntv_padron_nominal pn " +
                "  WHERE pn.co_ubigeo_inei LIKE ? " +
                "  AND pn.es_padron='1' " +
                "  AND p.nu_edad<6 " +
                "  GROUP BY pn.co_entidad " +
                "  ) " +
                "UNION " +
                "  (SELECT co_entidad, " +
                "    0 AS cc " +
                "  FROM pntm_entidad " +
                "  WHERE co_entidad NOT IN " +
                "    (SELECT co_entidad FROM pntv_padron_nominal WHERE co_ubigeo_inei LIKE ? " +
                "    ) " +
                "  AND co_ubigeo_inei LIKE ? and es_entidad='1' " +
                "  )) t " +
                "ORDER BY cc DESC";
        Object[] params = new Object[]{coUbigeoInei};
        log.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Lugar.class));
    }


    @Override
    public List<PadronNominal> listarPadronEdad(String coUbigeoInei, String deEdad, String hastaEdad, String feIni, String feFin, String tiRegFecha, String esPadron) {
        String ubigeo = coUbigeoInei + "%";
        if(coUbigeoInei.equals("00"))
            ubigeo = "%";
        //String esPadron = activos ? "1":"0";
        String filtroPeriodo = "";
        String filtroEsPadron = "";

        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
                params.add(feIni);
                params.add(feFin);
            }
//            filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) ";
        }


        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")){
            filtroEsPadron = " and p.es_padron in('1','2') "; //TODOS
        }


        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        params.add(deEdad);
        params.add(hastaEdad);

        String query = "" +
                "select co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, fe_nac_menor as fe_nacimiento, nu_edad as edad, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                //"nu_edad||' año(s), '||nu_edad_meses||' mes(es)' edadEscrita, " +
                "trunc(MONTHS_BETWEEN(SYSDATE, fe_nac_menor)/12) ||' año(s), '|| TRUNC(MOD((MONTHS_BETWEEN(SYSDATE, fe_nac_menor)),12)) ||' mes(es), ' || trunc(sysdate - add_months(fe_nac_menor, trunc(months_between(sysdate, fe_nac_menor)))) ||' dia(s)' edadEscrita, " +
                "p.co_centro_poblado, p.es_padron, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "es_ads.de_est_salud as de_est_salud_ads, " +
                "p.co_est_salud_ads as co_est_salud_ads, " +
                "p.co_est_salud, "+
                "nvl(p.nu_cel_madre,' ') as nuCelMadre, "+
                "nvl(p.di_correo_madre,' ') as diCorreoMadre, "+
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "es.de_est_salud as de_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "nu_afiliacion, " +
                "ie.co_modular as co_inst_educativa, " +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'REGISTRO MANUAL'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro in ('WS','SP') THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='0'), 1, '0, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='8'), 1, '8, ', '') " +
                "from dual) coProgramasSociales, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                " case when p.ti_registro='TR' then p.fuente_precarga else '' end as coFuentePrecarga, \n" +
                " case " +
                "     when p.ti_registro='TR' then ent.de_entidad " +
                "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                "     when p.ti_registro='MQ' then 'HISMINSA' " +
                "     else '' " +
                " end as deFuentePrecarga, \n" +
                "ie.no_centro_educativo de_inst_educativa," +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe, " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre, " +
                "co_gra_inst_madre, d6.de_grado_instruccion as de_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre, " +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza, " +
                "CASE \n" +
                "   WHEN p.US_CREA_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE p.US_CREA_REGISTRO \n" +
                "END AS US_CREA_REGISTRO," +
                "to_char(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, " +
                "CASE \n" +
                "   WHEN p.US_MODI_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE p.US_MODI_REGISTRO \n" +
                "END AS US_MODI_REGISTRO," +
                "to_char(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro, " +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR," +
                " nvl(fa.no_frecuencia_atencion,'') as deFrecAtencion  \n" +
                " from pntv_padron_nominal p " +
                " left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud  and es.nu_secuencia_local=p.nu_secuencia_local " +
                " left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                " left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe " +
                " left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre " +
                " left join PNTR_FUENTE_DATOS FDM on FDM.co_fuente_datos=p.co_fuente_datos " +
                " left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza " +
                " /*left join pntr_seguro_salud d4 on d4.ti_seguro_menor=p.ti_seguro_menor*/ " +
                " left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre " +
                " left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                " left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                " left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa " +
                " left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                " left join pnvm_establecimiento_salud es_ads on es_ads.co_est_salud=p.co_est_salud_ads and es_ads.nu_secuencia_local=p.nu_secuencia_local_ads " +
                " left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                " LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                " LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                " left join pntm_entidad_envio ent on ent.co_entidad=p.fuente_precarga " +
                " left join pntd_frecuencia_atencion fa on p.co_frecuencia_atencion=fa.co_frecuencia_atencion " +
                " where p.co_ubigeo_inei like ? " +
                filtroPeriodo +
                filtroEsPadron +
                " and nu_edad between ? and ? " +
                //"and trunc(mod((months_between(sysdate, fe_nac_menor)),12))=? " +
                //"AND (TRUNC(p.fe_nac_menor) BETWEEN ADD_MONTHS(TRUNC(SYSDATE),-6*12) AND TRUNC(SYSDATE)) "+
                " order by fe_nacimiento desc ";

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params.toArray(new Object[params.size()]))));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronEdad(String coUbigeoInei, String deEdad, String hastaEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha, String esPadron) {
        String ubigeo = coUbigeoInei + "%";
        if(coUbigeoInei.equals("00"))
            ubigeo = "%";
        /*log.info(usuario.getCoEntidad());
        log.info(usuario.getCoTipoEntidad());*/
        String filtroPeriodo = "";
        String filtroEsPadron = "";

        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")){
            filtroEsPadron = " and p.es_padron in('1','2') "; //TODOS
        }


        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else  {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
                params.add(feIni);
                params.add(feFin);
            }
//            filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) ";
        }
        params.add(deEdad);
        params.add(hastaEdad);
        params.add(filaFin);
        params.add(filaIni);

        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, fe_nac_menor as fe_nacimiento, nu_edad as edad, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                //"nu_edad||' año(s), '||nu_edad_meses||' mes(es)' edadEscrita, " +
                "trunc(MONTHS_BETWEEN(SYSDATE, fe_nac_menor)/12) ||' año(s), '|| TRUNC(MOD((MONTHS_BETWEEN(SYSDATE, fe_nac_menor)),12)) ||' mes(es), ' || trunc(sysdate - add_months(fe_nac_menor, trunc(months_between(sysdate, fe_nac_menor)))) ||' dia(s)' edadEscrita, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_est_salud, "+
                "es.de_est_salud as de_est_salud, " +
                "p.co_est_salud_nac, " +
                "case\n" +
                "         when p.ti_registro='RM' then \n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' then \n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro='WS' then \n" +
                "          'WEB SERVICE' \n" +
                "         when p.ti_registro='SP' then \n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='MQ' then \n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "es_nac.de_est_salud de_est_salud_nac " +
                " from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "where u.co_ubigeo_inei like ? " +
                filtroEsPadron +
                filtroPeriodo +
                "and nu_edad between ? and ? " +
                "order by fe_nacimiento desc" +
                ") t " +
                "where rownum<=?)  " +
                "where fila>=? " +
                "order by fe_nacimiento desc ";
        try {
            log.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params.toArray(new Object[params.size()]))));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer countListarPadronEdad(String coUbigeoInei, String deEdad, String hastaEdad, String feIni, String feFin, String tiRegFecha, String esPadron) {
        if(coUbigeoInei.equals("00"))
            coUbigeoInei = "%";
        else
            coUbigeoInei = coUbigeoInei + "%";
        String filtroPeriodo = "";
        String filtroEsPadron = "";

        List<Object> params = new ArrayList<Object>();

        params.add(coUbigeoInei);

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') "; //TODOS
        }
        if(!esPadron.equals("3")) {
            params.add(esPadron);
        }

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
                params.add(feIni);
                params.add(feFin);
            }
        }
        params.add(deEdad);
        params.add(hastaEdad);
        String query = "" +
                "select count(1) " +
                "from pntv_padron_nominal p left join pnvm_establecimiento_salud e on e.co_est_salud=p.co_est_salud and e.nu_secuencia_local=p.nu_secuencia_local " +
                "where p.co_ubigeo_inei like ? " +
                filtroEsPadron +
                filtroPeriodo +
                " and nu_edad between ? and ? ";

        log.debug(String.format("DAO '%s' con: '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }

    @Override
    public List<PadronMovimiento> listadoPadronMovimiento(String feIni, String feFin) {
        String query = "" +
                "select m.co_padron_nominal as coPadronNominal, " +
                "       p.ap_primer_menor as apPrimerMenor, " +
                "       p.ap_segundo_menor as apSegundoMenor, " +
                "       p.prenombre_menor as prenombreMenor, " +
                "       to_char(p.fe_nac_menor, 'dd/MM/yyyy') as feNacMenor, " +
                "       m.co_ubigeo_inei_ant as coUbigeoIneiAnt, " +
                "       m.co_entidad_ant as coEntidadAnt, " +
                "       m.co_ubigeo_inei_act as coUbigeoIneiAct, " +
                "       m.co_entidad_act as coEntidadAct, " +
                "       m.co_entidad_ant_eess as coEntidadAntEess, " +
                "       m.co_entidad_act_eess as coEntidadActEess, " +
                "       to_char(m.fe_crea_audi, 'dd/MM/yyyy')     as feCreaAudi, " +
                "       to_char(m.fe_modi_audi, 'dd/MM/yyyy')     as feModiAudi, " +
                "       m.us_crea_audi as usCreaAudi, " +
                "       m.us_modi_audi as usModiAudi, " +
                "       m.co_centro_poblado_ant as coCentroPobladoAnt, " +
                "       m.co_centro_poblado_act as coCentroPobladoAct " +
                "  from pntv_padron_movimiento m " +
                "  left join pntv_padron_nominal p on p.co_padron_nominal = " +
                "                                     m.co_padron_nominal " +
                "  where p.es_padron='1' and trunc(m.fe_crea_audi) between ? and ? " +
                " order by p.fe_nac_menor desc, m.co_ubigeo_inei_act, m.nu_sec_act";
        List<PadronMovimiento> movimientos = new ArrayList<PadronMovimiento>();
        Object[] params = new Object[]{feIni, feFin};
        try{
            List<PadronMovimiento> padronMovimientos = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(PadronMovimiento.class), params);
            for(PadronMovimiento padronMovimiento: padronMovimientos){
                Entidad entidadAntEess=null;
                Entidad entidadActEess=null;
                Ubigeo ubigeoAnt = ubigeoDao.obtenerPorCodigoInei(padronMovimiento.getCoUbigeoIneiAnt());

                /*Entidad entidadAnt = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAnt(), Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());*/
                Entidad entidadAnt=null;
                String coEntidadAnt = padronMovimiento.getCoEntidadAnt();
                if(coEntidadAnt!=null && !coEntidadAnt.isEmpty()) {
                    entidadAnt = usuarioExternoDao.getEntidad(coEntidadAnt, Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());
                    if(entidadAnt==null || ( entidadAnt.getDeEntidad()==null || entidadAnt.getDeEntidad().isEmpty())) {
                        coEntidadAnt = String.format("%1$" + 8 + "s", coEntidadAnt).replace(' ', '0');
                        entidadAnt = usuarioExternoDao.getEntidad(coEntidadAnt, Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
                    }
                }
                else{
                    entidadAntEess = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAntEess(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
                }


                Ubigeo ubigeoAct = ubigeoDao.obtenerPorCodigoInei(padronMovimiento.getCoUbigeoIneiAct());

                /*Entidad entidadAct = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAct(), Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());*/

                Entidad entidadAct=null;
                String coEntidadAct = padronMovimiento.getCoEntidadAct();
                if(coEntidadAct!=null && !coEntidadAct.isEmpty()){
                    entidadAct = usuarioExternoDao.getEntidad(coEntidadAct, Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());
                    if(entidadAct==null || ( entidadAct.getDeEntidad()==null || entidadAct.getDeEntidad().isEmpty())){
                        coEntidadAct = String.format("%1$" + 8 + "s", coEntidadAct).replace(' ', '0');
                        entidadAct = usuarioExternoDao.getEntidad(coEntidadAct, Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
                    }
                }
                else{
                    entidadActEess = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadActEess(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
                }


                CentroPoblado centroPobladoAnt = ubigeoDao.getCentroPoblado(padronMovimiento.getCoCentroPobladoAnt());

                CentroPoblado centroPobladoAct = ubigeoDao.getCentroPoblado(padronMovimiento.getCoCentroPobladoAct());

                padronMovimiento.setDeDepartamentoAnt(ubigeoAnt.getDeDepartamento());
                padronMovimiento.setDeProvinciaAnt(ubigeoAnt.getDeProvincia());
                padronMovimiento.setDeDistritoAnt(ubigeoAnt.getDeProvincia());
                /*padronMovimiento.setDeEntidadAnt(entidadAnt.getDeEntidad());*/
                if(entidadAnt!=null && entidadAnt.getDeEntidad()!=null && !entidadAnt.getDeEntidad().isEmpty()) {
                    padronMovimiento.setDeEntidadAnt(entidadAnt.getDeEntidad());
                }
                else {
                    padronMovimiento.setDeEntidadAnt(entidadAntEess.getDeEntidad());
                }

                if(centroPobladoAnt != null)
                    padronMovimiento.setDeCentroPobladoAnt(centroPobladoAnt.getNoCentroPoblado());
                else
                    padronMovimiento.setDeCentroPobladoAnt("");

                padronMovimiento.setDeDepartamentoAct(ubigeoAct.getDeDepartamento());
                padronMovimiento.setDeProvinciaAct(ubigeoAct.getDeProvincia());
                padronMovimiento.setDeDistritoAct(ubigeoAct.getDeDistrito());


                if(entidadAct!=null && entidadAct.getDeEntidad()!=null && !entidadAct.getDeEntidad().isEmpty()) {
                    padronMovimiento.setDeEntidadAct(entidadAct.getDeEntidad());
                } else {
                    padronMovimiento.setDeEntidadAct(entidadActEess.getDeEntidad());
                }


                if(centroPobladoAct != null)
                    padronMovimiento.setDeCentroPobladoAct(centroPobladoAct.getNoCentroPoblado());
                else
                    padronMovimiento.setDeCentroPobladoAct("");

                movimientos.add(padronMovimiento);
            }
            return movimientos;
        } catch (EmptyResultDataAccessException ee) {
            ee.printStackTrace();
            return null;
        }

    }


    @Override
    public List<PadronMovimiento> listadoPadronMovimiento(String feIni, String feFin, int filaIni, int filaFin) {
        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select m.co_padron_nominal  as coPadronNominal, " +
                "                       p.ap_primer_menor    as apPrimerMenor, " +
                "                       p.ap_segundo_menor   as apSegundoMenor, " +
                "                       p.prenombre_menor    as prenombreMenor, " +
                "                       to_char(p.fe_nac_menor, 'dd/MM/yyyy')       as feNacMenor, " +
                "                       m.co_ubigeo_inei_ant as coUbigeoIneiAnt, " +
                "                       m.co_entidad_ant     as coEntidadAnt, " +
                "                       m.co_ubigeo_inei_act as coUbigeoIneiAct, " +
                "                       m.co_entidad_act     as coEntidadAct, " +
                "                       m.co_entidad_ant_eess as coEntidadAntEess, " +
                "                       m.co_entidad_act_eess as coEntidadActEess, " +
                "                       to_char(m.fe_crea_audi, 'dd/MM/yyyy') as feCreaAudi, " +
                "                       to_char(m.fe_modi_audi, 'dd/MM/yyyy') as feModiAudi, " +
                "                       m.us_crea_audi as usCreaAudi, " +
                "                       m.us_modi_audi as usModiAudi, " +
                "                       m.co_centro_poblado_ant as coCentroPobladoAnt, " +
                "                       m.co_centro_poblado_act as coCentroPobladoAct " +
                "                  from pntv_padron_movimiento m " +
                "                  left join pntv_padron_nominal p on p.co_padron_nominal = " +
                "                                                     m.co_padron_nominal " +
                "                  where p.es_padron='1' and trunc(m.fe_crea_audi) between ? and ?  " + //todo
                "                 order by p.fe_nac_menor desc, m.co_ubigeo_inei_act, m.nu_sec_act" +
                ") t " +
                "where rownum<=?)  " +
                "where fila>=?";

        List<PadronMovimiento> movimientos = new ArrayList<PadronMovimiento>();
        Object[] params = new Object[]{feIni, feFin, filaFin, filaIni};
        try{
            logger.debug(String.format("DAO '%s' con: '%s' por ejecutar", query, ArrayUtils.toString(params)));
            List<PadronMovimiento> padronMovimientos = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(PadronMovimiento.class), params);
            for(PadronMovimiento padronMovimiento: padronMovimientos){
                Ubigeo ubigeoAnt = ubigeoDao.obtenerPorCodigoInei(padronMovimiento.getCoUbigeoIneiAnt());
                /*Entidad entidadAnt = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAnt(), Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());*/
                Entidad entidadAntEess=null;
                Entidad entidadActEess=null;

                Entidad entidadAnt=null;
                String coEntidadAnt = padronMovimiento.getCoEntidadAnt();
                if(coEntidadAnt!=null && !coEntidadAnt.isEmpty()){
                    entidadAnt = usuarioExternoDao.getEntidad(coEntidadAnt, Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());
                    if(entidadAnt==null || ( entidadAnt.getDeEntidad()==null || entidadAnt.getDeEntidad().isEmpty())){
                        coEntidadAnt = String.format("%1$" + 8 + "s", coEntidadAnt).replace(' ', '0');
                        entidadAnt = usuarioExternoDao.getEntidad(coEntidadAnt, Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
                    }
                }
                else{
                    entidadAntEess = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAntEess(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
                }

                Ubigeo ubigeoAct = ubigeoDao.obtenerPorCodigoInei(padronMovimiento.getCoUbigeoIneiAct());
                //solo se considera que las entidades de tipo municipio registran
                Entidad entidadAct=null;
                String coEntidadAct = padronMovimiento.getCoEntidadAct();
                if(coEntidadAct!=null && !coEntidadAct.isEmpty()){
                    entidadAct = usuarioExternoDao.getEntidad(coEntidadAct, Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());
                    if(entidadAct==null || ( entidadAct.getDeEntidad()==null || entidadAct.getDeEntidad().isEmpty())){
                        coEntidadAct = String.format("%1$" + 8 + "s", coEntidadAct).replace(' ', '0');
                        entidadAct = usuarioExternoDao.getEntidad(coEntidadAct, Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
                    }
                }
                else{
                    entidadActEess = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadActEess(), Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
                }


                CentroPoblado centroPobladoAnt = ubigeoDao.getCentroPoblado(padronMovimiento.getCoCentroPobladoAnt());
                CentroPoblado centroPobladoAct = ubigeoDao.getCentroPoblado(padronMovimiento.getCoCentroPobladoAct());

                padronMovimiento.setDeDepartamentoAnt(ubigeoAnt.getDeDepartamento());
                padronMovimiento.setDeProvinciaAnt(ubigeoAnt.getDeProvincia());
                padronMovimiento.setDeDistritoAnt(ubigeoAnt.getDeDistrito());

                if(entidadAnt!=null && entidadAnt.getDeEntidad()!=null && !entidadAnt.getDeEntidad().isEmpty()) {
                    padronMovimiento.setDeEntidadAnt(entidadAnt.getDeEntidad());
                }
                else {
                    padronMovimiento.setDeEntidadAnt(entidadAntEess.getDeEntidad());
                }


                if(centroPobladoAnt != null)
                    padronMovimiento.setDeCentroPobladoAnt(centroPobladoAnt.getNoCentroPoblado());
                else
                    padronMovimiento.setDeCentroPobladoAnt("");

                padronMovimiento.setDeDepartamentoAct(ubigeoAct.getDeDepartamento());
                padronMovimiento.setDeProvinciaAct(ubigeoAct.getDeProvincia());
                padronMovimiento.setDeDistritoAct(ubigeoAct.getDeDistrito());

                if(entidadAct!=null && entidadAct.getDeEntidad()!=null && !entidadAct.getDeEntidad().isEmpty()) {
                    padronMovimiento.setDeEntidadAct(entidadAct.getDeEntidad());
                } else {
                    padronMovimiento.setDeEntidadAct(entidadActEess.getDeEntidad());
                }


                if(centroPobladoAct != null)
                    padronMovimiento.setDeCentroPobladoAct(centroPobladoAct.getNoCentroPoblado());
                else
                    padronMovimiento.setDeCentroPobladoAct("");
                movimientos.add(padronMovimiento);
            }
            return movimientos;
        } catch (EmptyResultDataAccessException ee) {
            ee.printStackTrace();
            return null;
        }
    }

    @Override
    public int countPadronMovimientos(String feIni, String feFin) {
        String query = "select count(1) from pntv_padron_movimiento t1 left join pntv_padron_nominal t2 on t1.co_padron_nominal=t2.co_padron_nominal " +
                "where trunc(fe_crea_audi) between ? and ?  and t2.es_padron='1' ";
        Object[] params = new Object[]{feIni, feFin};
//        log.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params);
    }

    @Override
    public int countAllPadronMovimientos() {
        String query = "select count(1) from pntv_padron_movimiento";
        return jdbcTemplate.queryForInt(query);
    }

    @Override
    public List<Ubigeo> listadoPadronesPorUbigeo(){
        Integer coConsolidado = jdbcTemplate.queryForInt("select co_consolidado from pntv_consolidado where es_consolidado='1'");
        String query = "" +
                "select co_ubigeo_inei, " +
                "       de_departamento, " +
                "       de_provincia, " +
                "       de_distrito, " +
                "       nu_sin_dni, " +
                "       nu_con_dni, " +
                "       nu_total," +
                "       nu_cui " +
                "  from pntv_consolidado_ubigeo " +
                "where co_consolidado=? order by co_ubigeo_inei";
        try{
            List<Ubigeo> result = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Ubigeo.class), coConsolidado);
            return result;
        } catch (EmptyResultDataAccessException er) {
            er.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronObservado> listadoObservaciones(String feIni, String feFin) {
        String query = "" +
                "select o.co_padron_nominal, \n" +
                "       p.ap_primer_menor, \n" +
                "       p.ap_segundo_menor, \n" +
                "       p.prenombre_menor, \n" +
                "       to_char(p.fe_nac_menor, 'dd/MM/yyyy') fe_nac_menor, \n" +
                "       to_char(o.fe_crea_audi, 'dd/MM/yyyy') fe_crea_audi, \n" +
                "       to_char(o.fe_modi_audi, 'dd/MM/yyyy') fe_modi_audi, \n" +
                "       e.de_entidad, \n" +
                "       tiobs.de_tipo_observacion \n" +
                "  from pntm_padron_observado o \n" +
                "  left join pntv_padron_nominal p on p.co_padron_nominal = \n" +
                "                                     o.co_padron_nominal \n" +
                "  left join pntm_entidad e on e.co_entidad = p.co_entidad and e.es_entidad='1' \n" +
                "  left join pntr_tipo_observacion tiobs on tiobs.co_tipo_observacion=o.co_tipo_observacion\n" +
                " where p.es_padron='1' and trunc(o.fe_crea_audi) between to_date(?, 'dd/mm/yyyy') and to_date(?, 'dd/mm/yyyy') \n" +
                " and (tiobs.co_tipo_observacion<>'1' and  tiobs.co_tipo_observacion<>'2')\n" +
                " order by o.fe_crea_audi desc\n";
        Object[] params = new Object[]{feIni, feFin};
        try{
            log.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronObservado.class), params);
        } catch (EmptyResultDataAccessException ee) {
            ee.printStackTrace();
            return null;
        }
    }


    @Override
    public List<PadronObservado> listadoObservaciones(String feIni, String feFin, int filaIni, int filaFin) {
        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select o.co_padron_nominal, \n" +
                "       p.ap_primer_menor, \n" +
                "       p.ap_segundo_menor, \n" +
                "       p.prenombre_menor, \n" +
                "       to_char(p.fe_nac_menor, 'dd/MM/yyyy') fe_nac_menor, \n" +
                "       to_char(o.fe_crea_audi, 'dd/MM/yyyy') fe_crea_audi, \n" +
                "       to_char(o.fe_modi_audi, 'dd/MM/yyyy') fe_modi_audi, \n" +
                "       e.de_entidad, \n" +
                "       tiobs.de_tipo_observacion \n" +
                "  from pntm_padron_observado o \n" +
                "  left join pntv_padron_nominal p on p.co_padron_nominal = \n" +
                "                                     o.co_padron_nominal \n" +
                "  left join pntm_entidad e on e.co_entidad = p.co_entidad and e.es_entidad='1' \n" +
                "  left join pntr_tipo_observacion tiobs on tiobs.co_tipo_observacion=o.co_tipo_observacion\n" +
                " where p.es_padron='2' and o.es_observado='1' and trunc(o.fe_crea_audi) between to_date(?, 'dd/mm/yyyy') and to_date(?, 'dd/mm/yyyy') \n" +
                " and (tiobs.co_tipo_observacion<>'1' and  tiobs.co_tipo_observacion<>'2')\n" +
                " order by o.fe_crea_audi desc\n" +
                ") t " +
                "where rownum<=?)  " +
                "where fila>=?";

        Object[] params = new Object[]{feIni, feFin, filaFin, filaIni};
        try{
            log.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronObservado.class), params);
        } catch (EmptyResultDataAccessException ee) {
            ee.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListadoObservaciones(String feIni, String feFin) {
        String query = "" +
                "select count(1) \n" +
                "  from pntm_padron_observado o \n" +
                "  left join pntv_padron_nominal p on p.co_padron_nominal = \n" +
                "                                     o.co_padron_nominal \n" +
                "  left join pntm_entidad e on e.co_entidad = p.co_entidad and e.es_entidad='1' \n" +
                "  left join pntr_tipo_observacion tiobs on tiobs.co_tipo_observacion=o.co_tipo_observacion \n" +
                " where p.es_padron='2' and o.es_observado='1' and trunc(o.fe_crea_audi) between to_date(?, 'dd/mm/yyyy') and to_date(?, 'dd/mm/yyyy') \n" +
                " and (tiobs.co_tipo_observacion<>'1' and  tiobs.co_tipo_observacion<>'2')\n";
        Object[] params = new Object[]{feIni, feFin};
        log.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params);
    }

    @Override
    public List<PadronNominal> listadoPadronSinDoc(String coUbigeo, String feIni, String feFin, String tiRegFecha, String esPadron) {
        if(coUbigeo.equals("00"))
            coUbigeo = "%";
        else
            coUbigeo = coUbigeo + "%";

        String filtroPeriodo = "";
        String filtroEsPadron = "";

        List<Object> params = new ArrayList<Object>();

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in ('1','2') ";
        }

        String query = "" +
                "select co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor,  " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia,  " +

                "es_ads.de_est_salud as de_est_salud_ads, " +
                "p.co_est_salud_ads as co_est_salud_ads, " +
                "nvl(p.nu_cel_madre,' ') as nuCelMadre, "+
                "nvl(p.di_correo_madre,' ') as diCorreoMadre, "+
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "p.co_est_salud, "+
                "es.de_est_salud as de_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "/*ti_seguro_menor, d4.de_seguro_menor as de_seguro_menor,*/ nu_afiliacion,  " +
                "ie.co_modular as co_inst_educativa,  " +
                "p.co_centro_poblado, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='0'), 1, '0, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='8'), 1, '8, ', '') " +
                "from dual) coProgramasSociales, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                "case when p.ti_registro='TR' then p.fuente_precarga else '' end as coFuentePrecarga, \n" +
                "case " +
                "     when p.ti_registro='TR' then en.de_entidad " +
                "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                "     when p.ti_registro='MQ' then 'HISMINSA' " +
                "     else '' " +
                "end as deFuentePrecarga, \n" +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "ie.no_centro_educativo de_inst_educativa," +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe,  " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre,  " +
                "co_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre,  " +
                "d6.de_grado_instruccion de_gra_inst_madre," +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza, " +
                "case \n" +
                "   when p.us_crea_registro in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.us_crea_registro in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.us_crea_registro in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   when p.us_crea_registro in('HISMINSA') then 'SERVICIO HISMINSA' \n" +
                "   else p.us_crea_registro \n" +
                "end as us_crea_registro," +
                "to_char(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, " +
                "case \n" +
                "   when p.us_modi_registro in('SP-CNV','WSHVCNV') then 'SERVICIO CNV' \n" +
                "   when p.us_modi_registro in('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') then 'SERVICIO ACTA' \n" +
                "   when p.us_modi_registro in('RENIECTR') then 'SERVICIO TRAMA' \n" +
                "   when p.us_modi_registro in('HISMINSA') then 'SERVICIO HISMINSA' \n" +
                "   else p.us_modi_registro \n" +
                "end as us_modi_registro, \n" +
                "to_char(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro, " +
                "es_ads.de_est_salud de_est_salud_ads, " +
                "es_nac.de_est_salud de_est_salud_nac, " +
                "case\n" +
                "    when p.ti_registro='RM' then \n" +
                "     'REGISTRO MANUAL' \n" +
                "    when p.ti_registro='TR' then \n" +
                "     'TRAMA' \n" +
                "    when p.ti_registro in ('WS','SP') then \n" +
                "     'WEB SERVICE' \n" +
                "    when p.ti_registro='MQ' then \n" +
                "     'HISMINSA' \n" +
                " end as deTipoRegistro, " +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                "p.co_est_salud_nac, " +
                "nvl(fa.no_frecuencia_atencion,'') as deFrecAtencion " +
                "from pntv_padron_nominal p  " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor  " +
                "left join PNTR_VINCULO_FAMILIAR d1 on  d1.co_vinculo=p.ti_vinculo_jefe  " +
                "left join PNTR_VINCULO_FAMILIAR d2 on  d2.co_vinculo=p.ti_vinculo_madre  " +
                "left join PNTR_FUENTE_DATOS FDM on  FDM.co_fuente_datos=p.co_fuente_datos  " +
                "left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza  " +
                "/*left join pntr_seguro_salud d4 on d4.ti_seguro_menor=p.ti_seguro_menor  */" +
                "left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre  " +
                "left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei  and u.es_ubigeo='1' " +
                "left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa  " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                "left join pnvm_establecimiento_salud es_ads on es_ads.co_est_salud=p.co_est_salud_ads and es_ads.nu_secuencia_local=p.nu_secuencia_local_ads " +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                "LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                "left join pntm_entidad_envio en on en.co_entidad=p.fuente_precarga " +
                "left join pntd_frecuencia_atencion fa on p.co_frecuencia_atencion=fa.co_frecuencia_atencion" +
                " where p.co_ubigeo_inei like ?  " +
/*                "and es_padron='1'  " +*/
                filtroEsPadron +
                "AND p.nu_edad<6  " +  filtroPeriodo +
//                " and ( nvl2(nu_dni_menor,1,0)=0 and  nvl2(nu_cui,1,0)=0 )";
                " and ( nvl2(nu_dni_menor,1,0)=0 or nu_dni_menor ='')" +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc";

        params.add(coUbigeo);

        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()) {
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")) {
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<PadronNominal> listadoPadronSinDoc(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String esPadron) {
        if(coUbigeo.equals("00"))
            coUbigeo = "%";
        else
            coUbigeo = coUbigeo + "%";
        String filtroPeriodo = "";
        List<Object> params = new ArrayList<Object>();
        String filtroEsPadron = "";

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
//            filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) ";
        }


        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }

        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor,  " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia,  " +
                "p.co_est_salud, "+
                "es.de_est_salud as de_est_salud, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "es_nac.de_est_salud de_est_salud_nac, " +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO' \n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA' \n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE' \n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE' \n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA' \n" +
                "       end as deTipoRegistro, " +
                "p.co_est_salud_nac " +
                "from pntv_padron_nominal p  " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor  " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "where p.co_ubigeo_inei like ? " +
                /*" and es_padron='1'  " +*/
                filtroEsPadron +
                "AND p.nu_edad<6  " +
                "and (nu_dni_menor is null or nu_dni_menor ='') " + filtroPeriodo +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc) t " +
                "where rownum<=?)  " +
                "where fila>=?";

        params.add(coUbigeo);
        if(!esPadron.equals("3")){
            params.add(esPadron);
        }


        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()) {
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")) {
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }
        params.add(filaFin);
        params.add(filaIni);
        try {
            log.debug(String.format("DAO '%s' con '%s'", query,params));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countListadoPadronSinDoc(String coUbigeo, String feIni, String feFin, String tiRegFecha, String esPadron) {
        if(coUbigeo.equals("00"))
            coUbigeo = "%";
        else
            coUbigeo = coUbigeo + "%";
        String filtroPeriodo = "";
        List<Object> params = new ArrayList<Object>();

        String filtroEsPadron = "";

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else  {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in ('1','2') ";
        }

        String query = "" +
                "select count(1) " +
                "  from pntv_padron_nominal p left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local "  +
                " where p.nu_edad<6 " +
                /*"   and p.es_padron = '1' " +*/
                 filtroEsPadron +
                "   and (p.nu_dni_menor is null or p.nu_dni_menor = '') " +
                "   and p.co_ubigeo_inei like ? " + filtroPeriodo ;

        if (!esPadron.equals("3")){
            params.add(esPadron);
        }

        params.add(coUbigeo);
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()) {
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")) {
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }
        logger.debug(String.format("DAO '%s' con '%s'", query, params));
        return jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }


    @Override
    public String getFechaGeneracionConsolidado() {
        Integer coConsolidado = jdbcTemplate.queryForInt("select co_consolidado from pntv_consolidado where es_consolidado='1'");
        String query = "select to_char(fe_generacion, 'dd/mm/yyyy HH24:MI:SS') from pntv_consolidado where co_consolidado=?";
        Object[] params = new Object[]{coConsolidado};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForObject(query, String.class, params);
    }

    @Override
    public List<Ubigeo> consolidadoEuropan(ReporteEuropan reporteEuropan) {
        String entidadesFiltro1 = "";
        String entidadesFiltro2 = "";
        String filtroPeriodo = "";
        String filtroEdad = "";

        List<Object> params = new ArrayList<Object>();
        if (reporteEuropan.getEntidades().contains("00")) {//TODOS
            entidadesFiltro1 = " and p.co_ubigeo_inei in (" + StringUtils.join(padronProperties.DISTRITOS_EUROPAN, ',') + ") ";
            entidadesFiltro2 = "  co_ubigeo_inei in (" + StringUtils.join(padronProperties.DISTRITOS_EUROPAN, ',') + ") and ";
        } else {
            entidadesFiltro1 = " and p.co_ubigeo_inei in (" + StringUtils.join(reporteEuropan.getEntidades(), ',') + ") ";
            entidadesFiltro2 = "  co_ubigeo_inei in (" + StringUtils.join(reporteEuropan.getEntidades(), ',') + ") and ";
        }


        if (reporteEuropan.getFeIni() != null && reporteEuropan.getFeFin() != null && !reporteEuropan.getFeIni().isEmpty() && !reporteEuropan.getFeFin().isEmpty()) {
            params.add(reporteEuropan.getFeIni());
            params.add(reporteEuropan.getFeFin());
            if (reporteEuropan.getTiRegFecha() != null && !reporteEuropan.getTiRegFecha().isEmpty()){
                if (reporteEuropan.getTiRegFecha().equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (reporteEuropan.getTiRegFecha().equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (reporteEuropan.getTiRegFecha().equals("T")){
                    params.add(reporteEuropan.getFeIni());
                    params.add(reporteEuropan.getFeFin());
                    params.add(reporteEuropan.getFeFin());
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) ";
            }
        }

        if (reporteEuropan.getDeEdad() != null && reporteEuropan.getHastaEdad() != null && !reporteEuropan.getDeEdad().isEmpty() && !reporteEuropan.getHastaEdad().isEmpty()) {
            filtroEdad = " and p.nu_edad between ? and ? ";
            params.add(reporteEuropan.getDeEdad());
            params.add(reporteEuropan.getHastaEdad());
        }

        if (reporteEuropan.getFeIni() != null && reporteEuropan.getFeFin() != null && !reporteEuropan.getFeIni().isEmpty() && !reporteEuropan.getFeFin().isEmpty()) {
            params.add(reporteEuropan.getFeIni());
            params.add(reporteEuropan.getFeFin());
            if (reporteEuropan.getTiRegFecha().equals("T")) {
                params.add(reporteEuropan.getFeIni());
                params.add(reporteEuropan.getFeFin());
                params.add(reporteEuropan.getFeFin());
            }
        }

        if (reporteEuropan.getDeEdad() != null && reporteEuropan.getHastaEdad() != null && !reporteEuropan.getDeEdad().isEmpty() && !reporteEuropan.getHastaEdad().isEmpty()) {
            params.add(reporteEuropan.getDeEdad());
            params.add(reporteEuropan.getHastaEdad());
        }
        if (reporteEuropan.getFeIni() != null && reporteEuropan.getFeFin() != null && !reporteEuropan.getFeIni().isEmpty() && !reporteEuropan.getFeFin().isEmpty()) {
            params.add(reporteEuropan.getFeIni());
            params.add(reporteEuropan.getFeFin());
            if (reporteEuropan.getTiRegFecha().equals("T")) {
                params.add(reporteEuropan.getFeIni());
                params.add(reporteEuropan.getFeFin());
                params.add(reporteEuropan.getFeFin());
            }
        }

        if (reporteEuropan.getDeEdad() != null && reporteEuropan.getHastaEdad() != null && !reporteEuropan.getDeEdad().isEmpty() && !reporteEuropan.getHastaEdad().isEmpty()) {
            params.add(reporteEuropan.getDeEdad());
            params.add(reporteEuropan.getHastaEdad());
        }

        if (reporteEuropan.getFeIni() != null && reporteEuropan.getFeFin() != null && !reporteEuropan.getFeIni().isEmpty() && !reporteEuropan.getFeFin().isEmpty()) {
            params.add(reporteEuropan.getFeIni());
            params.add(reporteEuropan.getFeFin());
            if (reporteEuropan.getTiRegFecha().equals("T")) {
                params.add(reporteEuropan.getFeIni());
                params.add(reporteEuropan.getFeFin());
                params.add(reporteEuropan.getFeFin());
            }
        }

        if (reporteEuropan.getDeEdad() != null && reporteEuropan.getHastaEdad() != null && !reporteEuropan.getDeEdad().isEmpty() && !reporteEuropan.getHastaEdad().isEmpty()) {
            params.add(reporteEuropan.getDeEdad());
            params.add(reporteEuropan.getHastaEdad());
        }

        String query = "" +
                "select t.*, " +
                "       (select count(1) " +
                "          from pntv_padron_nominal p " +
                "         where p.co_ubigeo_inei = t.co_ubigeo_inei " +
                "           and p.es_padron = '1' " +
                "           and (p.nu_dni_menor is null or p.nu_dni_menor = '') " +
                "           and p.nu_edad<6 " + filtroPeriodo +  filtroEdad +
                ") " +
                "                   as nu_sin_dni, " +
                "       (select count(1) " +
                "          from pntv_padron_nominal p" +
                "         where p.co_ubigeo_inei = t.co_ubigeo_inei " +
                "           and p.es_padron = '1' " +
                "           and (p.nu_dni_menor is not null) " +
                "           and p.nu_edad<6" + filtroPeriodo +  filtroEdad +
                ") nu_con_dni " +
                "  from ((select p.co_ubigeo_inei, " +
                "                u.de_departamento, " +
                "                u.de_provincia, " +
                "                u.de_distrito, " +
                "                count(1) as nu_total " +
                "           from pntv_padron_nominal p " +
                "           left join pntm_ubigeo_inei u on p.co_ubigeo_inei = u.co_ubigeo_inei and u.es_ubigeo='1' " +
                "          where p.nu_edad<6 " +
                "            and es_padron = '1' " + entidadesFiltro1 +  filtroPeriodo +  filtroEdad +
                "          group by p.co_ubigeo_inei, " +
                "                   u.de_departamento, " +
                "                   u.de_provincia, " +
                "                   u.de_distrito) union " +
                "        (select co_ubigeo_inei, de_departamento, de_provincia, de_distrito, 0 " +
                "           from pntm_ubigeo_inei " +
                "          where " + entidadesFiltro2 +
                "co_ubigeo_inei not in " +
                "                (select distinct p.co_ubigeo_inei " +
                "                   from pntv_padron_nominal p " +
                "                  where p.nu_edad<6 " +  filtroPeriodo +  filtroEdad +
                "                    and p.es_padron = '1')" +
                " )) t " +
                " order by co_ubigeo_inei, nu_total desc ";

        logger.debug(String.format("DAO '%s' con '%s'", query, params.toString()));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Ubigeo.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> getPadronUbigeoEuropan(String coUbigeoInei, String feIni, String feFin, String deEdad, String hastaEdad, String tiRegFecha) {
        List<Object> params = new ArrayList<Object>();
        String entidadesFiltro = "";
        String filtroPeriodo = "";
        String filtroEdad = "";
        if (coUbigeoInei!=null && !coUbigeoInei.isEmpty()) {
            entidadesFiltro = " and p.co_ubigeo_inei = ? ";
            params.add(coUbigeoInei);
        }

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if(tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                params.add(feIni);
                params.add(feFin);
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }

        if (deEdad != null && hastaEdad != null && !deEdad.isEmpty() && !hastaEdad.isEmpty()) {
            filtroEdad = " and p.nu_edad between ? and ? ";
            params.add(deEdad);
            params.add(hastaEdad);
        }

        String query = "" +
                "select co_padron_nominal, nu_dni_menor, nu_cui, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "nu_cnv, ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, fe_nac_menor as fe_nacimiento, nu_edad as edad, " +
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "trunc(MONTHS_BETWEEN(SYSDATE, fe_nac_menor)/12) ||' año(s), '|| TRUNC(MOD((MONTHS_BETWEEN(SYSDATE, fe_nac_menor)),12)) ||' mes(es)' edadEscrita, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_est_salud, "+
                "es.de_est_salud as de_est_salud, " +
                "/*ti_seguro_menor, d4.de_seguro_menor as de_seguro_menor, */nu_afiliacion, " +
                "ie.co_modular as co_inst_educativa, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='0'), 1, '0, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='8'), 1, '8, ', '') " +
                "from dual) coProgramasSociales, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                "ie.no_centro_educativo de_inst_educativa," +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe, " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre, " +
                "co_gra_inst_madre, d6.de_grado_instruccion as de_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre, " +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza, " +
                "CASE \n" +
                "   WHEN p.US_CREA_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE p.US_CREA_REGISTRO \n" +
                "END AS US_CREA_REGISTRO," +
                "to_char(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, " +
                "CASE \n" +
                "   WHEN p.US_MODI_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE p.US_MODI_REGISTRO \n" +
                "END AS US_MODI_REGISTRO," +
                "to_char(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro, " +
                "es_nac.de_est_salud de_est_salud_nac, " +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +

                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "p.co_est_salud_nac, \n " +
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, \n" +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos, \n" +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos, \n" +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado \n" +
                "from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                "left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe " +
                "left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre " +
                "left join PNTR_FUENTE_DATOS FDM on  FDM.co_fuente_datos=p.co_fuente_datos  " +
                "left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza " +
                "/*left join pntr_seguro_salud d4 on d4.ti_seguro_menor=p.ti_seguro_menor*/ " +
                "left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre " +
                "left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                "LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                "where " +
                " es_padron='1' " + entidadesFiltro +  filtroPeriodo + filtroEdad +
                "order by fe_nacimiento desc ";

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, ArrayUtils.toString(params.toArray(new Object[params.size()]))));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     *
     * @return
     */
    private Boolean esEntidadFitrarUbigeo(){
        return usuario.getCoTipoEntidad().equals(Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad()) ||
               usuario.getCoTipoEntidad().equals(Entidad.TipoEntidad.DISA.getCoTipoEntidad()) ||
               usuario.getCoTipoEntidad().equals(Entidad.TipoEntidad.RED.getCoTipoEntidad()) ||
               usuario.getCoTipoEntidad().equals(Entidad.TipoEntidad.MICRORED.getCoTipoEntidad()) ||
               usuario.getCoTipoEntidad().equals(Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad());
    }


    @Override
    public List<PadronNominal> listarPadronesNacidos(String coUbigeoInei, String coEstSalud , String esPadron, String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha, String... args) {
        String ubigeo = coUbigeoInei + "%";
        String establecimientoSalud = coEstSalud + "%";
        String filtroEdad = " and p.nu_edad < 6 ";
        String filtroPeriodo = "";
        String filtroPeriodoNac = "";
        String filtroTiRegistro = "";
        String filtroEsPadron = "";

        if(coUbigeoInei.equals("00"))
            ubigeo = "%";
        /*String esPadron = activos ? "1":"0";*/
        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        params.add(establecimientoSalud);
        if (feNacIni != null && feNacFin != null && !feNacIni.isEmpty() && !feNacFin.isEmpty()) {
            params.add(feNacIni);
            params.add(feNacFin);
            filtroPeriodoNac = " and (trunc(fe_nac_menor) between ? and ?) ";
        }
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(fe_crea_registro) between ? and ?) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) " +
                            " and (trunc(fe_modi_registro) <= ? or fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) ";
            }
        }
        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")){
            filtroEsPadron = " and p.es_padron in('1','2') ";//activos y activos-observados
        }

        if(esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6 ) ";
        }
        String query = "" +
                "select co_padron_nominal, nu_dni_menor, nu_cui, nu_cnv, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                //"nu_edad||' año(s), '||nu_edad_meses||' mes(es)' edadEscrita,"+
                "trunc(MONTHS_BETWEEN(SYSDATE, fe_nac_menor)/12) ||' año(s), '|| TRUNC(MOD((MONTHS_BETWEEN(SYSDATE, fe_nac_menor)),12)) ||' mes(es)' edadEscrita,"+
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "es.de_est_salud as de_est_salud, " +
                "es.co_est_salud as co_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "p.co_est_salud_nac as co_est_salud_nac, " +
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado," +
                "/*ti_seguro_menor, d4.de_seguro_menor as de_seguro_menor,*/ nu_afiliacion, " +
                "ie.co_modular as co_inst_educativa, " +
                //"decode(p.co_inst_educativa, null, '', ie.no_centro_educativo||' (Código Modular: '||ie.co_modular||', '||ie.de_departamento||', '||ie.de_provincia||', '||ie.de_distrito||')') as de_inst_educativa, " +
                "ie.no_centro_educativo as de_inst_educativa, " +
                "co_dni_jefe_fam, ap_primer_jefe, ap_segundo_jefe, prenom_jefe, ti_vinculo_jefe, d1.de_vinculo as de_vinculo_jefe, " +
                "co_dni_madre, ap_primer_madre, ap_segundo_madre, prenom_madre, ti_vinculo_madre, d2.de_vinculo as de_vinculo_madre, " +
                "co_gra_inst_madre, co_len_madre, d5.de_lengua as de_len_madre, " +
                "d6.de_grado_instruccion de_gra_inst_madre, " +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='MQ' THEN\n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                "(select  " +
                "decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='0'), 1, '0, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='4'), 1, '4, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='5'), 1, '5, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='7'), 1, '7, ', '') " +
                "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='8'), 1, '8, ', '') " +
                "from dual) coProgramasSociales, " +
                "(select  " +
                "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='0'), 1, '0, ', '') " +
                "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='1'), 1, '1, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='2'), 1, '2, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='3'), 1, '3, ', '') " +
                "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='4'), 1, '4, ', '') " +
                "from dual) coTipoSeguros, " +
                " case when p.ti_registro='TR' then p.fuente_precarga else '' end as coFuentePrecarga, \n" +
                " case " +
                "     when p.ti_registro='TR' then en.de_entidad " +
                "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                "     when p.ti_registro='MQ' then 'HISMINSA' " +
                "     else '' " +
                " end as deFuentePrecarga, \n" +
                "co_nivel_pobreza, d3.de_dom as de_nivel_pobreza," +
                "CASE \n" +
                "   WHEN p.US_CREA_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN p.US_CREA_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE p.US_CREA_REGISTRO \n" +
                "END AS US_CREA_REGISTRO," +
                "TO_CHAR(fe_crea_registro, 'dd/MM/yyyy') fe_crea_registro, " +
                "CASE \n" +
                "   WHEN p.US_MODI_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN p.US_MODI_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE p.US_MODI_REGISTRO \n" +
                "END AS US_MODI_REGISTRO," +
                "TO_CHAR(fe_modi_registro, 'dd/MM/yyyy') fe_modi_registro " +

                "from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local " +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                "left join PNTR_VINCULO_FAMILIAR d1 on d1.co_vinculo=p.ti_vinculo_jefe " +
                "left join PNTR_VINCULO_FAMILIAR d2 on d2.co_vinculo=p.ti_vinculo_madre " +
                "left join PNTR_FUENTE_DATOS FDM on FDM.co_fuente_datos=p.co_fuente_datos " +
                "left join pntr_dominios d3 on d3.no_dom='CO_NIVEL_POBREZA' and d3.co_dominio=co_nivel_pobreza " +
                "/*left join pntr_seguro_salud d4 on d4.ti_seguro_menor=p.ti_seguro_menor*/ " +
                "left join PNTR_LENGUA d5 on d5.co_lengua=P.co_len_madre " +
                "left join PNTR_GRADO_INSTRUCCION d6 on d6.co_grado_instruccion=p.co_gra_inst_madre " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_educativo ie on ie.co_centro_educativo=p.co_inst_educativa " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +

                "LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                "LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                " left join pntm_entidad_envio en on en.co_entidad=p.fuente_precarga " +
                " where p.co_ubigeo_inei like ? " +
                "and p.co_est_salud like ? " +
                filtroPeriodoNac +
                filtroPeriodo +
                filtroEdad +
                filtroTiRegistro +
                filtroEsPadron +
                "and p.co_padron_nominal like '9%' " +
                "and length(p.prenombre_menor)<3 " +
                "and p.prenombre_menor in (select de_dom from pntr_dominios where no_dom='CO_TIPO_SIN_NOMBRE' and es_dom='1') " +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc";

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadronesNacidos(String coUbigeoInei, String coEstSalud , String esPadron, String feNacIni, String feNacFin, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String... args) {
        String ubigeo = coUbigeoInei + "%";
        String establecimientoSalud=coEstSalud + "%";
        String filtroEdad =" and p.nu_edad <6 ";
        String filtroPeriodo = "";
        String filtroPeriodoNac = "";
        String filtroTiRegistro = "";
        String filtroEsPadron = "";

        if(coUbigeoInei.equals("00"))
            ubigeo = "%";
        /*String esPadron = activos ? "1":"0";*/

        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        params.add(establecimientoSalud);

        if (feNacIni != null && feNacFin != null && !feNacIni.isEmpty() && !feNacFin.isEmpty()) {
            params.add(feNacIni);
            params.add(feNacFin);
            filtroPeriodoNac = " and (trunc(fe_nac_menor) between ? and ?) ";
        }
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(fe_crea_registro) between ? and ?) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between ? and ?) or (trunc(fe_modi_registro) between ? and ?)) " +
                            " and (trunc(fe_modi_registro) <= ? or fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and (trunc(fe_modi_registro) between ? and ?) ";
            }
        }
        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }

        if(esPadron.equals("0")) {
            filtroEdad = "";
            filtroEsPadron = " and ( p.es_padron=? or p.nu_edad>=6 ) ";
        }
        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select co_padron_nominal, nu_dni_menor, nu_cui, nu_cnv,  " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then '3' " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then '2' " +
                "  when trim(p.nu_dni_menor) is not null then '1' " +
                "end as ti_doc_identidad, " +
                "case when trim(p.nu_dni_menor) is null and  trim(p.nu_cui) is null then null " +
                "  when trim(p.nu_cui) is not null and trim(p.nu_dni_menor) is null then p.nu_cui " +
                "  when trim(p.nu_dni_menor) is not null then p.nu_dni_menor " +
                " end as coDocumentoIdentidad, " +
                "ap_primer_menor, ap_segundo_menor, prenombre_menor, p.co_genero_menor, d.de_genero_menor as de_genero_menor, TO_CHAR(fe_nac_menor,'dd/MM/yyyy') as fe_nac_menor, " +
                //"nu_edad||' año(s), '||nu_edad_meses||' mes(es)' edadEscrita,"+
                "trunc(MONTHS_BETWEEN(SYSDATE, fe_nac_menor)/12) ||' año(s), '|| TRUNC(MOD((MONTHS_BETWEEN(SYSDATE, fe_nac_menor)),12)) ||' mes(es)' edadEscrita,"+
                "upper(u.de_departamento) de_departamento, upper(u.de_provincia) de_provincia, upper(u.de_distrito) de_distrito, " +
                "p.de_direccion, p.co_ubigeo_inei, upper(u.de_departamento||', '||u.de_provincia||', '||u.de_distrito) as de_ubigeo_inei, co_etnia, " +
                "p.co_centro_poblado, " +
                "decode(p.co_centro_poblado, null, null, '', null, upper(cp.no_centro_poblado) || ', ' || upper(cp.no_categoria)) as de_centro_poblado, " +
                "p.co_est_salud, "+
                "es.de_est_salud as de_est_salud, " +
                "es_nac.de_est_salud as de_est_salud_nac, " +
                "case\n" +
                "         when p.ti_registro='RM' THEN\n" +
                "          'MUNICIPIO'\n" +
                "         when p.ti_registro='TR' THEN\n" +
                "          'TRAMA'\n" +
                "         when p.ti_registro='WS' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='SP' THEN\n" +
                "          'WEB SERVICE'\n" +
                "         when p.ti_registro='HM' THEN\n" +
                "          'HISMINSA'\n" +
                "       end as deTipoRegistro, " +
                "p.co_est_salud_nac as co_est_salud_nac " +
                "from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on es.co_est_salud=p.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local "  +
                "left join PNTR_GENERO_MENOR d on d.CO_GENERO_MENOR=p.co_genero_menor " +
                "left join pntm_ubigeo_inei u on u.co_ubigeo_inei=p.co_ubigeo_inei and u.es_ubigeo='1' " +
                "left join pntm_centro_poblado cp on cp.co_centro_poblado=p.co_centro_poblado and cp.es_centro_poblado='1' " +
                "left join pnvm_establecimiento_salud es_nac on es_nac.co_est_salud=p.co_est_salud_nac and es_nac.nu_secuencia_local=p.nu_secuencia_local_nac " +
                "where p.co_ubigeo_inei like ? " +
                "and p.co_est_salud like ? " +
                filtroPeriodoNac +
                filtroPeriodo +
                filtroEdad +
                filtroTiRegistro +
                filtroEsPadron +
                "and p.co_padron_nominal like '9%' " +
                /*"and length(p.prenombre_menor)<3 " +*/
                "and (p.prenombre_menor in (select de_dom from pntr_dominios where no_dom='CO_TIPO_SIN_NOMBRE' and es_dom='1') and length(p.prenombre_menor) < 3) " +
                "\n order by to_date(fe_nac_menor, 'dd/mm/yyyy') desc) t " +
                "where rownum<=?) " +
                "where fila>=?";

        params.add(filaFin);
        params.add(filaIni);
        try {
            logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public int countListaPadronesNacidos(String coUbigeoInei, String coEstSalud , String esPadron, String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha, String... args) {

        String ubigeo = coUbigeoInei + "%";
        if(coUbigeoInei.equals("00"))
            ubigeo = "%";
        /*String esPadron = activos ? "1":"0";*/
        String filtroEdad = " and p.nu_edad <6 ";
        String establecimientoSalud=coEstSalud + "%";
        if(coEstSalud.equals("00"))
            establecimientoSalud = "%";
        String filtroPeriodo = "";
        String filtroPeriodoNac = "";
        String filtroTiRegistro = "";
        List<Object> params = new ArrayList<Object>();
        String filtroEsPadron = "";

        params.add(ubigeo);
        params.add(establecimientoSalud);
        if (feNacIni != null && feNacFin != null && !feNacIni.isEmpty() && !feNacFin.isEmpty()) {
            params.add(feNacIni);
            params.add(feNacFin);
            filtroPeriodoNac = " and (trunc(fe_nac_menor) between ? and ?) ";
        }

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()) {
                if (tiRegFecha.equals("N")) {
                    filtroPeriodo = " and (trunc(fe_crea_registro) between to_date(?) and to_date(?)) and fe_crea_registro is not null " +
                            " and ((fe_modi_registro is null) or (fe_crea_registro = fe_modi_registro and nvl(US_CREA_REGISTRO, 'RENIEC') = nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(fe_modi_registro) between to_date(?) and to_date(?)) and fe_modi_registro is not null " +
                            " and (fe_modi_registro <> fe_crea_registro or (fe_modi_registro = fe_crea_registro and nvl(US_CREA_REGISTRO, 'RENIEC') <> nvl(US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(fe_crea_registro) between to_date(?) and to_date(?)) or (trunc(fe_modi_registro) between to_date(?) and to_date(?))) " +
                            " and (trunc(fe_modi_registro) <= to_date(?) or fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and (trunc(fe_modi_registro) between to_date(?) and to_date(?)) ";
            }
        }

        if(!esPadron.equals("3")) {
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }

        if(esPadron.equals("0")){
            filtroEdad = "";
            filtroEsPadron = " and (p.es_padron=? or p.nu_edad>=6) ";
        }
        String query = "" +
                "select count(1) from pntv_padron_nominal p " +
                "left join pnvm_establecimiento_salud es on p.co_est_salud=es.co_est_salud and es.nu_secuencia_local=p.nu_secuencia_local "  +
                "where p.co_ubigeo_inei like ?  " +
                "and p.co_est_salud like ? " +
                filtroPeriodoNac +
                filtroPeriodo +
                filtroEdad +
                filtroTiRegistro +
                filtroEsPadron +
                "and p.co_padron_nominal like '9%' " +
                "and length(p.prenombre_menor)< 3 " +
                "and p.prenombre_menor in (select de_dom from pntr_dominios where no_dom='CO_TIPO_SIN_NOMBRE' and es_dom='1') ";

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }


    public List<UsuarioExterno> getUsuario(String coUsuario) {
        String query = "" +
                "select    " +
                "distinct   " +
                "u.co_usuario,  " +
                "u.ap_primer,  " +
                "u.ap_segundo,  " +
                "u.prenombres,  " +
                "decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
                "decode(upper(u.nu_telefono),'XXX', null,u.nu_telefono) nu_telefono,  " +
                "u.dni_alcalde,  " +
                "u.de_alcalde,  " +
                "e.de_entidad_larga,  " +
                "decode(e.co_ubigeo_inei, '00', null, e.co_ubigeo_inei) co_ubigeo_inei,  " +
                "uu.de_departamento,  " +
                "uu.de_provincia,  " +
                "uu.de_distrito,  " +
                "to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
                "to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
                "u.es_usuario,  " +
                "u.us_crea_audi,  " +
                "u.us_modi_audi,  " +
                "g.de_grupo  " +
                " from pntm_usuario_externo u  " +
                "join pntm_usuario_entidad ue on ue.co_usuario=u.co_usuario " +
                "join pntm_entidad e on ue.co_entidad=e.co_entidad  and e.es_entidad='1' " +
                "left join pntm_ubigeo_inei uu on uu.co_ubigeo_inei=e.co_ubigeo_inei  and uu.es_ubigeo='1' " +
                "left join pntm_grupo_usuario gu on gu.co_usuario = u.co_usuario  " +
                "left join pntm_grupo g on g.co_grupo = gu.co_grupo and g.es_grupo<>'9'  " +
                "where ue.co_entidad<> '380'  " +
                "order by co_ubigeo_inei desc";

        logger.debug(String.format("DAO '%s'", query));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String queryUsuariosEntidad = "" +
            "select    " +
            "distinct   " +
            "u.co_usuario,  " +
            "u.ap_primer,  " +
            "u.ap_segundo,  " +
            "u.prenombres,  " +
            "decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "decode(upper(u.nu_telefono),'XXX', null,u.nu_telefono) nu_telefono,  " +
            "u.dni_alcalde,  " +
            "u.de_alcalde,  " +
            "e.de_entidad_larga,  " +
            "decode(e.co_ubigeo_inei, '00', null, e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "uu.de_departamento,  " +
            "uu.de_provincia,  " +
            "uu.de_distrito,  " +
            "to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "u.es_usuario,  " +
            "u.us_crea_audi,  " +
            "u.us_modi_audi,  " +
            "g.de_grupo, " +
            "e.co_entidad  " +
            " from pntm_usuario_externo u  " +
            "join pntm_usuario_entidad ue on ue.co_usuario=u.co_usuario and ue.co_tipo_entidad=? " +
            "join pntm_entidad e on ue.co_entidad=e.co_entidad  and e.es_entidad='1' " +
            "left join pntm_ubigeo_inei uu on uu.co_ubigeo_inei=e.co_ubigeo_inei  " +
            "left join pntm_grupo_usuario gu on gu.co_usuario = u.co_usuario  " +
            "left join pntm_grupo g on g.co_grupo = gu.co_grupo and g.es_grupo<>'9'  " +
            "where 1=1 ";

    static String queryUsuariosDisa = "" +
            "select    " +
            "distinct   " +
            "u.co_usuario,  " +
            "u.ap_primer,  " +
            "u.ap_segundo,  " +
            "u.prenombres,  " +
            "decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "decode(upper(u.nu_telefono),'XXX', null,u.nu_telefono) nu_telefono,  " +
            "u.dni_alcalde,  " +
            "u.de_alcalde,  " +
            "e.DE_DISA de_entidad_larga,  " +
            "decode(e.co_ubigeo_inei, '00', null, e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "null de_departamento,  " +
            "null de_provincia,  " +
            "null de_distrito,  " +
            "to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "u.es_usuario,  " +
            "u.us_crea_audi,  " +
            "u.us_modi_audi,  " +
            "g.de_grupo, " +
            "e.co_disa co_entidad " +
            "from pntm_usuario_externo u  " +
            "join pntm_usuario_entidad ue on ue.co_usuario=u.co_usuario and ue.co_tipo_entidad=? " +
            "join pnvm_disa e on ue.co_entidad=e.co_disa  " +
            "left join pntm_grupo_usuario gu on gu.co_usuario = u.co_usuario  " +
            "left join pntm_grupo g on g.co_grupo = gu.co_grupo and g.es_grupo<>'9'  " +
            "where 1=1 ";

    static String queryUsuariosRed = "" +
            "select    " +
            "distinct   " +
            "u.co_usuario,  " +
            "u.ap_primer,  " +
            "u.ap_segundo,  " +
            "u.prenombres,  " +
            "decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "decode(upper(u.nu_telefono),'XXX', null,u.nu_telefono) nu_telefono,  " +
            "u.dni_alcalde,  " +
            "u.de_alcalde,  " +
            "e.DE_RED de_entidad_larga,  " +
            "decode(e.co_ubigeo_inei, '00', null, e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "null de_departamento,  " +
            "null de_provincia,  " +
            "null de_distrito,  " +
            "to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "u.es_usuario,  " +
            "u.us_crea_audi,  " +
            "u.us_modi_audi,  " +
            "g.de_grupo, " +
            "e.co_disa||e.co_red co_entidad  " +
            " from pntm_usuario_externo u  " +
            "join pntm_usuario_entidad ue on ue.co_usuario=u.co_usuario and ue.co_tipo_entidad=? " +
            "join pnvm_red e on ue.co_entidad=e.co_disa||e.co_red  " +
            "left join pntm_grupo_usuario gu on gu.co_usuario = u.co_usuario  " +
            "left join pntm_grupo g on g.co_grupo = gu.co_grupo and g.es_grupo<>'9'  " +
            "where 1=1 ";

    static String queryUsuariosMicroRed = "" +
            "select    " +
            "distinct   " +
            "u.co_usuario,  " +
            "u.ap_primer,  " +
            "u.ap_segundo,  " +
            "u.prenombres,  " +
            "decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "decode(upper(u.nu_telefono),'XXX', null,u.nu_telefono) nu_telefono,  " +
            "u.dni_alcalde,  " +
            "u.de_alcalde,  " +
            "e.DE_MICRORED de_entidad_larga,  " +
            "decode(e.co_ubigeo_inei, '00', null, e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "null de_departamento,  " +
            "null de_provincia,  " +
            "null de_distrito,  " +
            "to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "u.es_usuario,  " +
            "u.us_crea_audi,  " +
            "u.us_modi_audi,  " +
            "g.de_grupo, " +
            "e.co_disa||e.co_red||e.co_microred co_entidad  " +
            " from pntm_usuario_externo u  " +
            "join pntm_usuario_entidad ue on ue.co_usuario=u.co_usuario and ue.co_tipo_entidad=? " +
            "join pnvm_microred e on ue.co_entidad=e.co_disa||e.co_red||e.co_microred  " +
            "left join pntm_grupo_usuario gu on gu.co_usuario = u.co_usuario  " +
            "left join pntm_grupo g on g.co_grupo = gu.co_grupo and g.es_grupo<>'9'  " +
            "where 1=1 ";

    static String queryUsuariosEstSalud = "" +
            "select    " +
            "distinct   " +
            "u.co_usuario,  " +
            "u.ap_primer,  " +
            "u.ap_segundo,  " +
            "u.prenombres,  " +
            "decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "decode(upper(u.nu_telefono),'XXX', null,u.nu_telefono) nu_telefono,  " +
            "u.dni_alcalde,  " +
            "u.de_alcalde,  " +
            "e.DE_EST_SALUD de_entidad_larga,  " +
            "decode(e.co_ubigeo_inei, '00', null, e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "null de_departamento,  " +
            "null de_provincia,  " +
            "null de_distrito,  " +
            "to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "u.es_usuario,  " +
            "u.us_crea_audi,  " +
            "u.us_modi_audi,  " +
            "g.de_grupo, " +
            "e.co_est_salud  co_entidad " +
            " from pntm_usuario_externo u  " +
            "join pntm_usuario_entidad ue on ue.co_usuario=u.co_usuario  and ue.co_tipo_entidad=? " +
            "join pnvm_establecimiento_salud e on ue.co_entidad=e.co_est_salud  " +
            "left join pntm_grupo_usuario gu on gu.co_usuario = u.co_usuario  " +
            "left join pntm_grupo g on g.co_grupo = gu.co_grupo and g.es_grupo<>'9'  " +
            "where 1=1 ";

    static String queryUsuariosAll = "" +
            " " +
            "(select distinct u.co_usuario,  " +
            "                         u.ap_primer,  " +
            "                         u.ap_segundo,  " +
            "                         u.prenombres,  " +
            "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "                         decode(upper(u.nu_telefono),  " +
            "                                'XXX',  " +
            "                                null,  " +
            "                                u.nu_telefono) nu_telefono,  " +
            "                         u.dni_alcalde,  " +
            "                         u.de_alcalde,  " +
            "                         to_char(e.co_entidad) co_entidad, " +
            "                         e.de_entidad_larga,  " +
            "                         decode(e.co_ubigeo_inei,  " +
            "                                '00',  " +
            "                                null,  " +
            "                                e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "                         uu.de_departamento,  " +
            "                         uu.de_provincia,  " +
            "                         uu.de_distrito,  " +
            "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "                         u.es_usuario,  " +
            "                         u.us_crea_audi,  " +
            "                         u.us_modi_audi,  " +
            "                         g.de_grupo  " +
            "           from pntm_usuario_externo u  " +
            "           join pntm_usuario_entidad ue  " +
            "             on ue.co_usuario = u.co_usuario  " +
            "           join pntm_entidad e  " +
            "             on ue.co_entidad = e.co_entidad  and e.es_entidad='1' " +
            "             and ue.co_tipo_entidad in ('1','2','3','4','6','7')  " +
            "           left join pntm_ubigeo_inei uu  " +
            "             on uu.co_ubigeo_inei = e.co_ubigeo_inei  " +
            "           left join pntm_grupo_usuario gu  " +
            "             on gu.co_usuario = u.co_usuario  " +
            "           left join pntm_grupo g  " +
            "             on g.co_grupo = gu.co_grupo  " +
            "            and g.es_grupo <> '9'  " +
            "         /*order by co_ubigeo_inei desc*/  " +
            "         ) union  " +
            "        (select distinct u.co_usuario,  " +
            "                         u.ap_primer,  " +
            "                         u.ap_segundo,  " +
            "                         u.prenombres,  " +
            "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "                         decode(upper(u.nu_telefono),  " +
            "                                'XXX',  " +
            "                                null,  " +
            "                                u.nu_telefono) nu_telefono,  " +
            "                         u.dni_alcalde,  " +
            "                         u.de_alcalde,  " +
            "                         e.co_disa co_entidad, " +
            "                         e.de_disa,  " +
            "                         decode(e.co_ubigeo_inei,  " +
            "                                '00',  " +
            "                                null,  " +
            "                                e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "                         null,  " +
            "                         null,  " +
            "                         null,  " +
            "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "                         u.es_usuario,  " +
            "                         u.us_crea_audi,  " +
            "                         u.us_modi_audi,  " +
            "                         g.de_grupo  " +
            "           from pntm_usuario_externo u  " +
            "           join pntm_usuario_entidad ue  " +
            "             on ue.co_usuario = u.co_usuario  " +
            "           join pnvm_disa e  " +
            "             on ue.co_entidad = e.co_disa  " +
            "            and ue.co_tipo_entidad = '5'  " +
            "           left join pntm_grupo_usuario gu  " +
            "             on gu.co_usuario = u.co_usuario  " +
            "           left join pntm_grupo g  " +
            "             on g.co_grupo = gu.co_grupo  " +
            "            and g.es_grupo <> '9'  " +
            "         /*order by co_ubigeo_inei desc*/  " +
            "         ) union  " +
            "        (select distinct u.co_usuario,  " +
            "                         u.ap_primer,  " +
            "                         u.ap_segundo,  " +
            "                         u.prenombres,  " +
            "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "                         decode(upper(u.nu_telefono),  " +
            "                                'XXX',  " +
            "                                null,  " +
            "                                u.nu_telefono) nu_telefono,  " +
            "                         u.dni_alcalde,  " +
            "                         u.de_alcalde,  " +
            "                         e.co_disa || e.co_red co_entidad, " +
            "                         e.de_red,  " +
            "                         decode(e.co_ubigeo_inei,  " +
            "                                '00',  " +
            "                                null,  " +
            "                                e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "                         null,  " +
            "                         null,  " +
            "                         null,  " +
            "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "                         u.es_usuario,  " +
            "                         u.us_crea_audi,  " +
            "                         u.us_modi_audi,  " +
            "                         g.de_grupo  " +
            "           from pntm_usuario_externo u  " +
            "           join pntm_usuario_entidad ue  " +
            "             on ue.co_usuario = u.co_usuario  " +
            "           join pnvm_red e  " +
            "             on ue.co_entidad = e.co_disa || e.co_red  " +
            "            and ue.co_tipo_entidad = '6'  " +
            "           left join pntm_grupo_usuario gu  " +
            "             on gu.co_usuario = u.co_usuario  " +
            "           left join pntm_grupo g  " +
            "             on g.co_grupo = gu.co_grupo  " +
            "            and g.es_grupo <> '9')  " +
            "         " +
            "        union  " +
            "        (select distinct u.co_usuario,  " +
            "                         u.ap_primer,  " +
            "                         u.ap_segundo,  " +
            "                         u.prenombres,  " +
            "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "                         decode(upper(u.nu_telefono),  " +
            "                                'XXX',  " +
            "                                null,  " +
            "                                u.nu_telefono) nu_telefono,  " +
            "                         u.dni_alcalde,  " +
            "                         u.de_alcalde,  " +
            "                         e.co_disa || e.co_red || e.co_microred co_entidad, " +
            "                         e.de_microred,  " +
            "                         decode(e.co_ubigeo_inei,  " +
            "                                '00',  " +
            "                                null,  " +
            "                                e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "                         null,  " +
            "                         null,  " +
            "                         null,  " +
            "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "                         u.es_usuario,  " +
            "                         u.us_crea_audi,  " +
            "                         u.us_modi_audi,  " +
            "                         g.de_grupo  " +
            "           from pntm_usuario_externo u  " +
            "           join pntm_usuario_entidad ue  " +
            "             on ue.co_usuario = u.co_usuario  " +
            "           join pnvm_microred e  " +
            "             on ue.co_entidad = e.co_disa || e.co_red || e.co_microred  " +
            "            and ue.co_tipo_entidad = '7'  " +
            "           left join pntm_grupo_usuario gu  " +
            "             on gu.co_usuario = u.co_usuario  " +
            "           left join pntm_grupo g  " +
            "             on g.co_grupo = gu.co_grupo  " +
            "            and g.es_grupo <> '9') union  " +
            "        (select distinct u.co_usuario,  " +
            "                         u.ap_primer,  " +
            "                         u.ap_segundo,  " +
            "                         u.prenombres,  " +
            "                         decode(u.de_email, 'no@tiene.com', null, u.de_email) de_email,  " +
            "                         decode(upper(u.nu_telefono),  " +
            "                                'XXX',  " +
            "                                null,  " +
            "                                u.nu_telefono) nu_telefono,  " +
            "                         u.dni_alcalde,  " +
            "                         u.de_alcalde,  " +
            "                         e.co_est_salud co_entidad, " +
            "                         e.de_microred,  " +
            "                         decode(e.co_ubigeo_inei,  " +
            "                                '00',  " +
            "                                null,  " +
            "                                e.co_ubigeo_inei) co_ubigeo_inei,  " +
            "                         null,  " +
            "                         null,  " +
            "                         null,  " +
            "                         to_char(u.fe_last_login, 'dd/mm/yyyy') fe_last_login,  " +
            "                         to_char(u.fe_last_login, 'hh24:mi:ss') hora_acceso,  " +
            "                         u.es_usuario,  " +
            "                         u.us_crea_audi,  " +
            "                         u.us_modi_audi,  " +
            "                         g.de_grupo  " +
            "           from pntm_usuario_externo u  " +
            "           join pntm_usuario_entidad ue  " +
            "             on ue.co_usuario = u.co_usuario  " +
            "           join pnvm_establecimiento_salud e  " +
            "             on ue.co_entidad = e.co_est_salud  " +
            "            and ue.co_tipo_entidad = '8'  " +
            "           left join pntm_grupo_usuario gu  " +
            "             on gu.co_usuario = u.co_usuario  " +
            "           left join pntm_grupo g  " +
            "             on g.co_grupo = gu.co_grupo  " +
            "            and g.es_grupo <> '9') ";

    @Override
    public List<UsuarioExterno> buscarUsuarioPorDatos(String apPrimer, String apSegundo, String prenombres, int filaIni, int filaFin) {
        String where = "where ";
        List<Object> parameters = new ArrayList<Object>();
        if(!apPrimer.equals("") && !apPrimer.isEmpty()) {
            where += "trim(ap_primer) like ? and ";
            parameters.add(apPrimer.toUpperCase() + "%");
        }
        if(!apSegundo.equals("") && !apSegundo.isEmpty()) {
            where += "trim(ap_segundo) like ? and ";
            parameters.add(apSegundo.toUpperCase() + "%");
        }
        if(!prenombres.equals("") && !prenombres.isEmpty()) {
            where += "trim(prenombres) like ? and ";
            parameters.add("%" + prenombres.toUpperCase() + "%");
        }
        // filtrar usuarios de RENIEC
        if(!"4".equals(usuario.getCoTipoEntidad())){
            where += " co_entidad<>'1843' and ";
        }
        parameters.add(filaIni);
        parameters.add(filaFin);

        if(where.equals("where ")) {
            return new ArrayList<UsuarioExterno>();
        } else{
            // se quita and
            where = where.substring(0, where.length()-4);
        }

        String query = "" +
                "select * from (" +
                "select t.*, rownum as fila from (" +
                queryUsuariosAll +
                ") t " + where +
                ")" +
                "where fila between ? and ? ";

        logger.debug(String.format("DAO '%s' con params: '%s'", query, parameters.toString()));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class), parameters.toArray(new Object[parameters.size()]));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UsuarioExterno> buscarUsuarioPorDni(String coUsuario) {
        String filtroUsuariosReniec = "";
        // filtrar usuarios de RENIEC
        if (!"4".equals(usuario.getCoTipoEntidad())) {
            filtroUsuariosReniec += " and  co_entidad<>'1843' ";
        }
        String query = "select * from (" +
                queryUsuariosAll +
                ")" +
                "where co_usuario=?" + filtroUsuariosReniec;

        Object[] params = new Object[]{coUsuario};
        logger.debug(String.format("DAO '%s' con '%s'", query, ArrayUtils.toString(params)));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class), params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UsuarioExterno> buscaUsuarioPorEntidad(String coEntidad, String esUsuario) {
        List<Object> params = new ArrayList<Object>();
        String esUsuarioSql = "";
        if(!"9".equals(esUsuario)) esUsuarioSql = " and es_usuario=? ";

        String[] split = coEntidad.split("_");
        String coEntidadP = split[0];
        String coTipoEntidad = split[1];

        String query = "";
        if (coTipoEntidad.equals(Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad())) {
            query = "select * from (" +
                    queryUsuariosEntidad +
                    ")" +
                    "where co_entidad=? " + esUsuarioSql;
        } else if( "4".equals(usuario.getCoTipoEntidad()) && coTipoEntidad.equals(Entidad.TipoEntidad.RENIEC.getCoTipoEntidad())){
            query = "select * from (" +
                    queryUsuariosEntidad +
                    ")" +
                    "where co_entidad=? " + esUsuarioSql;
        }else if(coTipoEntidad.equals(Entidad.TipoEntidad.MEF.getCoTipoEntidad())){
            query = "select * from (" +
                    queryUsuariosEntidad +
                    ")" +
                    "where co_entidad=? " + esUsuarioSql;
        }else if(coTipoEntidad.equals(Entidad.TipoEntidad.MINSA.getCoTipoEntidad())){
            query = "select * from (" +
                    queryUsuariosEntidad +
                    ")" +
                    "where co_entidad=? " + esUsuarioSql;
        }else if(coTipoEntidad.equals(Entidad.TipoEntidad.RED.getCoTipoEntidad())){
            query = "select * from (" +
                    queryUsuariosRed +
                    ")" +
                    "where co_entidad=? " + esUsuarioSql;
        }else if(coTipoEntidad.equals(Entidad.TipoEntidad.MICRORED.getCoTipoEntidad())){
            query = "select * from (" +
                    queryUsuariosMicroRed +
                    ")" +
                    "where co_entidad=? " + esUsuarioSql;
        }else if(coTipoEntidad.equals(Entidad.TipoEntidad.ESTABLECIMIENTO_SALUD.getCoTipoEntidad())){
            query = "select * from (" +
                    queryUsuariosEstSalud +
                    ")" +
                    "where co_entidad=? " + esUsuarioSql;
        }else if(coTipoEntidad.equals(Entidad.TipoEntidad.DISA.getCoTipoEntidad())){
            query = "select * from (" +
                    queryUsuariosDisa +
                    ")" +
                    "where co_entidad=? " + esUsuarioSql;
        }

        params.add(coTipoEntidad);
        params.add(coEntidadP);
        if(!"".equals(esUsuarioSql)) params.add(esUsuario);
        logger.debug(String.format("DAO '%s' con '%s'", query, params));
        try {
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UsuarioExterno.class), params.toArray(new Object[params.size()]));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }

    //Desde aqui se modifico por Jose Vidal Flores
    @Override
    public int countListaCantNiñosEdadEESS(String coUbigeoInei, int tiEstablecimiento, String esPadron) {
        String filtroEsPadron="";
        String ubigeo = coUbigeoInei + "%";
        if(coUbigeoInei.equals("00"))
            ubigeo = "%";

        String filtroTipoEESSJoin = "";

        if (tiEstablecimiento == 1){
            filtroTipoEESSJoin = "p.co_est_salud = r.co_renaes and p.nu_secuencia_local = r.nu_secuencia_local";
        }
        if (tiEstablecimiento == 2){
            filtroTipoEESSJoin = "p.co_est_salud_nac = r.co_renaes and p.nu_secuencia_local_nac = r.nu_secuencia_local";
        }
        if (tiEstablecimiento == 3){
            filtroTipoEESSJoin = "p.co_est_salud_ads = r.co_renaes and p.nu_secuencia_local_ads = r.nu_secuencia_local";
        }

        List<Object> params = new ArrayList<Object>();

        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }

        params.add(ubigeo);

        String query = "" +
                "select count (*) from " +
                "(select r.co_renaes " +
                "    from pntv_padron_nominal p " +
                "    inner join pntm_renaes r on  " +
                filtroTipoEESSJoin +
                "    where r.es_renaes = '1' " +
                /*"    and p.es_padron = '1' " +*/
                filtroEsPadron +
                "    and r.co_ubigeo_inei LIKE ? " +
                "and p.nu_edad < 6 " +
                "and r.nu_secuencia_local = (select max(nu_secuencia_local) from pntm_renaes where co_renaes= r.co_renaes and es_renaes='1') " +
//                "having count(decode(SIGN (6 - p.nu_edad), 1, 1, null)) > 0 " +
                "group by r.co_renaes, r.nu_secuencia_local, r.de_renaes, r.de_renaes_direccion) ";

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }
    //nuevo: para frecuencia de antencion
    @Override
    public int countListaCantNiñosEdadEESSxFrecAten(String coUbigeoInei, String coFrecuenciaAtencion, String esPadron){
        String filtroFrecuenciaAtencion;
        String filtroEsPadron = "";
        List<Object> params = new ArrayList<Object>();

        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }

        String ubigeo = coUbigeoInei + "%";
        if(coUbigeoInei.equals("00"))
            ubigeo = "%";


        params.add(ubigeo);


        if(coFrecuenciaAtencion.equals("0")){ //no tiene frecuencia de atención
            filtroFrecuenciaAtencion = " and p.co_frecuencia_atencion is null ";
        }
        else if(coFrecuenciaAtencion.equals("5")){//todos
            filtroFrecuenciaAtencion = "";
        }else{
            params.add(coFrecuenciaAtencion);
            filtroFrecuenciaAtencion = " and p.co_frecuencia_atencion = ? ";
        }

        String query = "" +
                "select count (*) from " +
                "(select r.co_renaes " +
                "    from pntv_padron_nominal p " +
                "    inner join pntm_renaes r on p.co_est_salud = r.co_renaes and p.nu_secuencia_local = r.nu_secuencia_local " +
                "    where r.es_renaes = '1' " +
                /*"    and p.es_padron = '1' " +*/
                filtroEsPadron +
                "    and r.co_ubigeo_inei LIKE ? " +
                /*"    and p.co_frecuencia_atencion = ? " +*/
                 filtroFrecuenciaAtencion +
                " and p.nu_edad < 6 " +
                " and r.nu_secuencia_local = (select max(nu_secuencia_local) from pntm_renaes where co_renaes= r.co_renaes and es_renaes='1') " +
//                "having count(decode(SIGN (6 - p.nu_edad), 1, 1, null)) > 0 " +
                "group by r.co_renaes, r.nu_secuencia_local, r.de_renaes, r.de_renaes_direccion) ";

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }

    @Override
    public List<PadronEdadEESS> listarCantNiñosEdadEESS(String coUbigeoInei, int filaIni, int filaFin, int tiEstablecimiento, String esPadron) {
        String ubigeo = coUbigeoInei + "%";
        String filtroEsPadron="";

        if(coUbigeoInei.equals("00"))
            ubigeo = "%";

        String filtroTipoEESSJoin = "";
        String aliasEstSalud = "";

        if (tiEstablecimiento == 1) {
            filtroTipoEESSJoin = "p.co_est_salud = r.co_renaes and p.nu_secuencia_local = r.nu_secuencia_local";
            aliasEstSalud = "de_est_salud";
        }
        if (tiEstablecimiento == 2) {
            filtroTipoEESSJoin = "p.co_est_salud_nac = r.co_renaes and p.nu_secuencia_local_nac = r.nu_secuencia_local";
            aliasEstSalud = "de_est_salud_nac";
        }
        if (tiEstablecimiento == 3) {
            filtroTipoEESSJoin = "p.co_est_salud_ads = r.co_renaes and p.nu_secuencia_local_ads = r.nu_secuencia_local";
            aliasEstSalud = "de_est_salud_ads";
        }

        List<Object> params = new ArrayList<Object>();

        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }

        params.add(ubigeo);

        String query = "" +
                "select * from " +
                "    (select rownum fila, t.* from " +
                "        (select r.co_renaes, upper(r.de_renaes)" + aliasEstSalud + ", upper(r.de_renaes_direccion) de_renaes_direccion, " +
                "            count(decode((p.nu_edad), 0, 1, null)) as ANNO_0, " +
                "            count(decode((p.nu_edad), 1, 1, null)) as ANNO_1, " +
                "            count(decode((p.nu_edad), 2, 1, null)) as ANNO_2, " +
                "            count(decode((p.nu_edad), 3, 1, null)) as ANNO_3, " +
                "            count(decode((p.nu_edad), 4, 1, null)) as ANNO_4, " +
                "            count(decode((p.nu_edad), 5, 1, null)) as ANNO_5, " +
                "            count(decode(SIGN (6 - p.nu_edad), 1, 1, null)) as TOTAL " +
                "            from pntv_padron_nominal p " +
                "            inner join pntm_renaes r on " +
                filtroTipoEESSJoin +
                "            where r.es_renaes = '1' " +
                filtroEsPadron +
                /*"            and p.es_padron = '1' " +*/
                "            and r.co_ubigeo_inei LIKE ? " +
                "    and p.nu_edad < 6 \n" +
                "    and r.nu_secuencia_local = (select max(nu_secuencia_local) from pntm_renaes where co_renaes= r.co_renaes and es_renaes='1') \n" +
                "    group by r.co_renaes, r.nu_secuencia_local, r.de_renaes, r.de_renaes_direccion \n" +
                "    order by r.de_renaes, r.co_renaes) t \n" +
                "    where rownum <=?) \n" +
                "where fila >=? ";

        params.add(filaFin);
        params.add(filaIni);
        try {
            logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronEdadEESS.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<PadronEdadEESS> listarCantNiñosEdadEESSxFrecAten(String coUbigeoInei, int filaIni, int filaFin, String coFrecuenciaAtencion, String esPadron) {
        String ubigeo = coUbigeoInei + "%";
        String filtroEsPadron="";
        if(coUbigeoInei.equals("00"))
            ubigeo = "%";

        List<Object> params = new ArrayList<Object>();

        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }


        params.add(ubigeo);
        /*params.add(coFrecuenciaAtencion);*/
        String filtroFrecuenciaAtencion;

        if(coFrecuenciaAtencion.equals("0")){ //no tiene frecuencia de atención
            filtroFrecuenciaAtencion = " and p.co_frecuencia_atencion is null ";
        }
        else if(coFrecuenciaAtencion.equals("5")){//todos
            filtroFrecuenciaAtencion = "";
        }else{
            params.add(coFrecuenciaAtencion);
            filtroFrecuenciaAtencion = " and p.co_frecuencia_atencion = ? ";
        }

        String query = "" +
                "select * from " +
                "    (select rownum fila, t.* from " +
                "        (select r.co_renaes," +
                "            upper(r.de_renaes) de_est_salud , upper(r.de_renaes_direccion) de_renaes_direccion, " +
                "            count(decode((p.nu_edad), 0, 1, null)) as ANNO_0, " +
                "            count(decode((p.nu_edad), 1, 1, null)) as ANNO_1, " +
                "            count(decode((p.nu_edad), 2, 1, null)) as ANNO_2, " +
                "            count(decode((p.nu_edad), 3, 1, null)) as ANNO_3, " +
                "            count(decode((p.nu_edad), 4, 1, null)) as ANNO_4, " +
                "            count(decode((p.nu_edad), 5, 1, null)) as ANNO_5, " +
                "            count(decode(SIGN (6 - p.nu_edad), 1, 1, null)) as TOTAL " +
                "            from pntv_padron_nominal p " +
                "            inner join pntm_renaes r on p.co_est_salud = r.co_renaes and p.nu_secuencia_local = r.nu_secuencia_local" +
                "            where r.es_renaes = '1' " +
                /*"            and p.es_padron = '1' " +*/
                filtroEsPadron +
                "            and r.co_ubigeo_inei LIKE ? " +
                filtroFrecuenciaAtencion +
                /*"            and p.co_frecuencia_atencion = ? " +*/
                "    and p.nu_edad < 6 " +
                "    and r.nu_secuencia_local = (select max(nu_secuencia_local) from pntm_renaes where co_renaes= r.co_renaes and es_renaes='1') " +
                "    group by r.co_renaes, r.nu_secuencia_local, r.de_renaes, r.de_renaes_direccion " +
//                "            having count(decode(SIGN (6 - p.nu_edad), 1, 1, null)) > 0 " +
                "    order by r.de_renaes, r.co_renaes) t " +
                "    where rownum <=?) " +
                "where fila >=? ";

        params.add(filaFin);
        params.add(filaIni);
        try {
            logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronEdadEESS.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public int countListaNiñosEdadEESS(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, String feIni, String feFin, String tiRegFecha, String esPadron) {
        String filtroPeriodo=" ";
        String filtroEsPadron ="";
        String ubigeo = coUbigeoInei + "%";
        if(coUbigeoInei.equals("00"))
            ubigeo = "%";

        String filtroTipoEESSJoin = "";
        String filtroCodEESS = "";

        if (tiEstablecimiento == 1){
            filtroTipoEESSJoin = "p.co_est_salud = r.co_renaes and p.nu_secuencia_local = r.nu_secuencia_local";
            filtroCodEESS = "and p.co_est_salud = ?";
        }
        if (tiEstablecimiento == 2){
            filtroTipoEESSJoin = "p.co_est_salud_nac = r.co_renaes and p.nu_secuencia_local_nac = r.nu_secuencia_local";
            filtroCodEESS = "and p.co_est_salud_nac = ?";
        }
        if (tiEstablecimiento == 3){
            filtroTipoEESSJoin = "p.co_est_salud_ads = r.co_renaes and p.nu_secuencia_local_ads = r.nu_secuencia_local";
            filtroCodEESS = "and p.co_est_salud_ads = ?";
        }

        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        params.add(coEstSalud);
        params.add(nuEdad);
/*        params.add(feIni);
        params.add(feFin);*/
        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }


        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
                params.add(feIni);
                params.add(feFin);
            }
        }

        String query = "" +
                "select count (*) from " +
                "    (select r.co_ubigeo_inei from pntm_ubigeo_inei u inner join pntv_padron_nominal p " +
                "        on p.co_ubigeo_inei = u.co_ubigeo_inei " +
                "        inner join pntm_renaes r on " +
                filtroTipoEESSJoin +
                "        where r.co_ubigeo_inei LIKE ? " +
                filtroCodEESS +
                "    and p.nu_edad = ? " +
                "    and r.es_renaes ='1' " +
                "    and r.nu_secuencia_local = (select max(nu_secuencia_local) from pntm_renaes where co_renaes= r.co_renaes and es_renaes='1') " +
                filtroEsPadron +
                filtroPeriodo + ")";
                /*"    and trunc(p.fe_crea_registro) >= ? " +
                "    and trunc(p.fe_crea_registro) <= ? ) ";*/

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }

    @Override
    public int countListaNiñosEdadEESSxFrecAten(String coUbigeoInei, String coEstSalud, String coFrecuenciaAtencion, String nuEdad, String feIni, String feFin, String tiRegFecha,String esPadron) {

        String filtroPeriodo = "";
        String filtroEsPadron = "";
        String ubigeo = coUbigeoInei + "%";
        if(coUbigeoInei.equals("00"))
            ubigeo = "%";

        List<Object> params = new ArrayList<Object>();


        params.add(ubigeo);
        params.add(coEstSalud);
        params.add(nuEdad);
        /*params.add(coFrecuenciaAtencion);*/
/*        params.add(feIni);
        params.add(feFin);*/

        String filtroFrecuenciaAtencion;

        if(coFrecuenciaAtencion.equals("0")){ //no tiene frecuencia de atención
            filtroFrecuenciaAtencion = " and p.co_frecuencia_atencion is null ";
        }
        else if(coFrecuenciaAtencion.equals("5")){//todos
            filtroFrecuenciaAtencion = "";
        }else{
            params.add(coFrecuenciaAtencion);
            filtroFrecuenciaAtencion = " and p.co_frecuencia_atencion = ? ";
        }


        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    params.add(feIni);
                    params.add(feFin);
                    params.add(feFin);
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
                params.add(feIni);
                params.add(feFin);
            }
        }

        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }

        String query = "" +
                "select count (*) from " +
                "    (select r.co_ubigeo_inei from pntm_ubigeo_inei u inner join pntv_padron_nominal p " +
                "        on p.co_ubigeo_inei = u.co_ubigeo_inei " +
                "        inner join pntm_renaes r on p.co_est_salud = r.co_renaes and p.nu_secuencia_local = r.nu_secuencia_local " +
                "        where r.co_ubigeo_inei LIKE ? and p.co_est_salud = ? " +
                "    and p.nu_edad = ? \n" +
                filtroFrecuenciaAtencion +
                /*"    and p.co_frecuencia_atencion = ? \n" +*/
                "    and r.es_renaes ='1' " +  filtroPeriodo +
                /*"    and trunc(p.fe_crea_registro) >= ? and trunc(p.fe_crea_registro) <= ? " +*/
                "    and r.nu_secuencia_local = (select max(nu_secuencia_local) from pntm_renaes where co_renaes= r.co_renaes and es_renaes='1') " +
                filtroEsPadron +
                ") ";

        logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
        return this.jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }

    @Override
    public List<PadronNominal> listarNiñosEdadEESS(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, int filaIni, int filaFin, String feIni,String feFin, String tiRegFecha, String esPadron) {
        String filtroEsPadron ="";
        String ubigeo = coUbigeoInei + "%";
        String filtroPeriodo=" ";

        if(coUbigeoInei.equals("00"))
            ubigeo = "%";

        String filtroTipoEESSJoin = "";
        String filtroCodEESS = "";

        if (tiEstablecimiento == 1){
            filtroTipoEESSJoin = "p.co_est_salud = r.co_renaes and p.nu_secuencia_local = r.nu_secuencia_local";
            filtroCodEESS = "and p.co_est_salud = ?";
        }
        if (tiEstablecimiento == 2){
            filtroTipoEESSJoin = "p.co_est_salud_nac = r.co_renaes and p.nu_secuencia_local_nac = r.nu_secuencia_local";
            filtroCodEESS = "and p.co_est_salud_nac = ?";
        }
        if (tiEstablecimiento == 3){
            filtroTipoEESSJoin = "p.co_est_salud_ads = r.co_renaes and p.nu_secuencia_local_ads = r.nu_secuencia_local";
            filtroCodEESS = "and p.co_est_salud_ads = ?";
        }

        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        params.add(coEstSalud);
        params.add(nuEdad);
/*        params.add(feIni);
        params.add(feFin);*/

        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }


        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }
        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()) {
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")) {
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }

        String query = "" +
                "select * from " +
                "    (select rownum fila, t.* from " +
                "        (select r.co_ubigeo_inei, r.co_renaes co_est_salud,upper(r.de_renaes) de_est_salud, p.nu_dni_menor, p.ap_primer_menor, " +
                "            p.ap_segundo_menor, p.prenombre_menor, (p.nu_edad || decode(p.nu_edad,1, ' año y ', ' años y ') || " +
                "                    p.nu_edad_meses || decode(p.nu_edad_meses,1, ' mes', ' meses')) as EDAD, " +
                "            u.co_ubigeo_inei as CO_UBIGEO_PAD,(u.de_departamento || ' / ' || u.de_provincia || ' / ' || u.de_distrito) as DE_UBIGEO_PAD, " +
                "            p.de_direccion, p.de_ref_dir, p.co_dni_madre, p.ap_primer_madre, p.ap_segundo_madre, p.prenom_madre, p.fe_crea_registro, p.fe_modi_registro, " +
                "            p.nu_cel_madre, p.di_correo_madre," +
                "            nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "            decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "            nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "            decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado, " +
                "            nvl(fat.no_frecuencia_atencion,'')  as deFrecAtencion  " +
                "            from pntm_ubigeo_inei u inner join pntv_padron_nominal p " +
                "            on p.co_ubigeo_inei = u.co_ubigeo_inei " +
                "            left join PNTR_FUENTE_DATOS FDM ON FDM.CO_FUENTE_DATOS = P.CO_FUENTE_DATOS " +
                "            left join pntd_frecuencia_atencion fat on p.co_frecuencia_atencion=fat.co_frecuencia_atencion " +
                "            inner join pntm_renaes r on " +
                filtroTipoEESSJoin +
                "            where r.co_ubigeo_inei LIKE ? " +
                filtroCodEESS +
                "    and p.nu_edad = ? " +
                "    and r.es_renaes ='1' " +
                "    and r.nu_secuencia_local = (select max(nu_secuencia_local) from pntm_renaes where co_renaes= r.co_renaes and es_renaes='1') " +
                filtroEsPadron +
                filtroPeriodo +
/*                "    and trunc(p.fe_crea_registro) >= ? " +
                "    and trunc(p.fe_crea_registro) <= ? " +*/
                "    order by p.ap_primer_menor) t " +
                "where rownum <= ?) " +
                "where fila >= ? ";

        params.add(filaFin);
        params.add(filaIni);
        try {
            logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarNiñosEdadEESSxFrecAten(String coUbigeoInei, String coEstSalud, String nuEdad, int filaIni, int filaFin, String coFrecuenciaAtencion, String feIni, String feFin,String tiRegFecha,String esPadron) {
        String filtroPeriodo = "";
        String filtroEsPadron = "";
        String ubigeo = coUbigeoInei + "%";

        if(coUbigeoInei.equals("00"))
            ubigeo = "%";

        List<Object> params = new ArrayList<Object>();

        params.add(ubigeo);
        params.add(coEstSalud);
        params.add(nuEdad);
        /*params.add(coFrecuenciaAtencion);*/
       /* params.add(feIni);
        params.add(feFin);*/

        String filtroFrecuenciaAtencion="";

        if(coFrecuenciaAtencion.equals("0")){ //no tiene frecuencia de atención
            filtroFrecuenciaAtencion = " and p.co_frecuencia_atencion is null ";
        }
        else if(coFrecuenciaAtencion.equals("5")){//todos
            filtroFrecuenciaAtencion = "";
        }else{
            params.add(coFrecuenciaAtencion);
            filtroFrecuenciaAtencion = " and p.co_frecuencia_atencion = ? ";
        }


        if(!esPadron.equals("3")){
            params.add(esPadron);
        }

        filtroEsPadron = " and p.es_padron=? ";
        if(esPadron.equals("3")) {
            filtroEsPadron = " and p.es_padron in('1','2') ";
        }


        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            if (tiRegFecha != null && !tiRegFecha.isEmpty()){
                if (tiRegFecha.equals("N")){
                    filtroPeriodo = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (tiRegFecha.equals("U")){
                    filtroPeriodo = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (tiRegFecha.equals("T")){
                    filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                filtroPeriodo = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
            }
        }

        if (feIni != null && feFin != null && !feIni.isEmpty() && !feFin.isEmpty()) {
            params.add(feIni);
            params.add(feFin);
            if (tiRegFecha == null || tiRegFecha.isEmpty()) {
                params.add(feIni);
                params.add(feFin);
            } else if (tiRegFecha.equals("T")) {
                params.add(feIni);
                params.add(feFin);
                params.add(feFin);
            }
        }


        String query = "" +
                "select * from " +
                "    (select rownum fila, t.* from " +
                "        (select r.co_ubigeo_inei, r.co_renaes co_est_salud,upper(r.de_renaes) de_est_salud, p.nu_dni_menor, p.ap_primer_menor, " +
                "            p.ap_segundo_menor, p.prenombre_menor, (p.nu_edad || decode(p.nu_edad,1, ' año y ', ' años y ') || " +
                "                    p.nu_edad_meses || decode(p.nu_edad_meses,1, ' mes', ' meses')) as EDAD, " +
                "            u.co_ubigeo_inei as CO_UBIGEO_PAD,(u.de_departamento || ' / ' || u.de_provincia || ' / ' || u.de_distrito) as DE_UBIGEO_PAD, " +
                "            p.de_direccion, p.de_ref_dir, p.co_dni_madre, p.ap_primer_madre, p.ap_segundo_madre, p.prenom_madre, p.fe_crea_registro, p.fe_modi_registro, " +
                "            p.nu_cel_madre, p.di_correo_madre," +
                "            nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, " +
                "            decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos," +
                "            nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                "            decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado, " +
                "            nvl(fat.no_frecuencia_atencion,'')  as deFrecAtencion  " +
                "            from pntm_ubigeo_inei u inner join pntv_padron_nominal p " +
                "            on p.co_ubigeo_inei = u.co_ubigeo_inei " +
                "            left join PNTR_FUENTE_DATOS FDM ON FDM.CO_FUENTE_DATOS = P.CO_FUENTE_DATOS " +
                "            left join pntd_frecuencia_atencion fat on p.co_frecuencia_atencion=fat.co_frecuencia_atencion " +
                "            inner join pntm_renaes r on p.co_est_salud = r.co_renaes and p.nu_secuencia_local = r.nu_secuencia_local " +
                "            where r.co_ubigeo_inei LIKE ? and p.co_est_salud = ? " +
                "    and p.nu_edad = ? " +
                filtroFrecuenciaAtencion +
                /*"    and p.co_frecuencia_atencion = ? " +*/
                "    and r.es_renaes ='1' " +
                "    and r.nu_secuencia_local = (select max(nu_secuencia_local) from pntm_renaes where co_renaes= r.co_renaes and es_renaes='1') " +
                    filtroEsPadron +
                    filtroPeriodo +
                /*"    and trunc(p.fe_crea_registro) >= ? and trunc(p.fe_crea_registro) <= ? " +*/
                "    order by p.ap_primer_menor) t " +
                "where rownum <= ?) " +
                "where fila >= ? ";

        params.add(filaFin);
        params.add(filaIni);

        try {
            logger.debug(String.format("DAO '%s' por ejecutar con: '%s'", query, params.toString()));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

//Hasta aqui se modifico por Jose Vidal Flores

    private String obtenerDetalleMotivoFiltroEdad() {
        String query = "select de_dom from pntr_dominios where NO_DOM='CO_MOTIVO_BAJA' AND CO_DOMINIO='6'";
        try {
            return jdbcTemplate.queryForObject(query, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
