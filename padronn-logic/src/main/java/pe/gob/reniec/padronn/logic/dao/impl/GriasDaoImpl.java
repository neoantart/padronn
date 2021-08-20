package pe.gob.reniec.padronn.logic.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.GriasDao;
import pe.gob.reniec.padronn.logic.model.Menor;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.model.form.BuscarMenorDocumento;
import pe.gob.reniec.padronn.logic.model.form.ListaMenores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jfloresh on 23/05/2014.
 */

@Repository
public class GriasDaoImpl extends AbstractBaseDao implements GriasDao {

   static String queryListadoPadron =
               "SELECT CO_PADRON_NOMINAL, NU_DNI_MENOR, NU_CUI, CASE WHEN TI_DOC_IDENTIDAD IS NULL AND NU_CUI IS NULL THEN '3' WHEN TI_DOC_IDENTIDAD IS NULL AND NU_CUI IS NOT NULL THEN '2' ELSE TI_DOC_IDENTIDAD END AS TI_DOC_IDENTIDAD,   \n" +
                       "CASE WHEN TI_DOC_IDENTIDAD='1' THEN NU_DNI_MENOR WHEN TI_DOC_IDENTIDAD='2' THEN NU_CUI WHEN TI_DOC_IDENTIDAD IS NULL AND NU_CUI IS NULL THEN NULL WHEN TI_DOC_IDENTIDAD IS NULL AND NU_CUI IS NOT NULL THEN NU_CUI END CODOCUMENTOIDENTIDAD,  \n" +
                       "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, P.CO_GENERO_MENOR, D.DE_GENERO_MENOR AS DE_GENERO_MENOR, TO_CHAR(FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, FE_NAC_MENOR AS FE_NACIMIENTO, NU_EDAD AS EDAD,   \n" +
                       "UPPER(U.DE_DEPARTAMENTO) DE_DEPARTAMENTO, UPPER(U.DE_PROVINCIA) DE_PROVINCIA, UPPER(U.DE_DISTRITO) DE_DISTRITO,   \n" +
                       "NU_EDAD||' AÑO(S), '||NU_EDAD_MESES||' MES(ES)' EDADESCRITA,   \n" +
                       "P.CO_CENTRO_POBLADO,   \n" +
                       "DECODE(P.CO_CENTRO_POBLADO, NULL, NULL, '', NULL, UPPER(CP.NO_CENTRO_POBLADO) || ', ' || UPPER(CP.NO_CATEGORIA)) AS DE_CENTRO_POBLADO,   \n" +
                       "P.DE_DIRECCION, P.CO_UBIGEO_INEI, UPPER(U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO) AS DE_UBIGEO_INEI, CO_ETNIA,   \n" +
                       "P.CO_EST_SALUD,   \n" +
                       "NVL(P.NU_CEL_MADRE,'') as nuCelMadre, \n"+
                       "NVL(P.DI_CORREO_MADRE,'') as diCorreoMadre, \n"+
                       "ES.DE_EST_SALUD AS DE_EST_SALUD,   \n" +
                       "/*TI_SEGURO_MENOR, D4.de_seguro_menor AS DE_SEGURO_MENOR,*/ NU_AFILIACION,  ES_PADRON,  \n" +
                       "IE.CO_MODULAR AS CO_INST_EDUCATIVA,   \n" +
                       "IE.NO_CENTRO_EDUCATIVO DE_INST_EDUCATIVA,  \n" +
                       "CO_DNI_JEFE_FAM, AP_PRIMER_JEFE, AP_SEGUNDO_JEFE, PRENOM_JEFE, TI_VINCULO_JEFE, D1.de_vinculo AS DE_VINCULO_JEFE,   \n" +
                       "CO_DNI_MADRE, AP_PRIMER_MADRE, AP_SEGUNDO_MADRE, PRENOM_MADRE, TI_VINCULO_MADRE, D2.de_vinculo AS DE_VINCULO_MADRE,   \n" +
                       "CO_GRA_INST_MADRE, D6.de_grado_instruccion AS DE_GRA_INST_MADRE, CO_LEN_MADRE, D5.de_lengua AS DE_LEN_MADRE,   \n" +
                       "CO_NIVEL_POBREZA, D3.DE_DOM AS DE_NIVEL_POBREZA, " +
                       " case\n" +
                       "         when p.ti_registro='RM' THEN \n" +
                       "          'REGISTRO MANUAL'\n" +
                       "         when p.ti_registro='TR' THEN \n" +
                       "          'TRAMA'\n" +
                       "         when p.ti_registro  in ('WS','SP') THEN \n" +
                       "          'WEB SERVICE'\n" +
                       "         when p.ti_registro='MQ' THEN \n" +
                       "          'HISMINSA'\n" +
                       " end as deTipoRegistro, \n" +
                       "CASE \n" +
                       "   WHEN p.US_CREA_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                       "   WHEN p.US_CREA_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                       "   WHEN p.US_CREA_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                       "   WHEN p.US_CREA_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                       "   ELSE p.US_CREA_REGISTRO \n" +
                       "END AS US_CREA_REGISTRO," +
                       "TO_CHAR(FE_CREA_REGISTRO, 'DD/MM/YYYY') FE_CREA_REGISTRO, " +
                       "CASE \n" +
                       "   WHEN p.US_MODI_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                       "   WHEN p.US_MODI_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                       "   WHEN p.US_MODI_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                       "   WHEN p.US_MODI_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                       "   ELSE p.US_MODI_REGISTRO \n" +
                       "END AS US_MODI_REGISTRO," +
                       " case when p.ti_registro='TR' then p.fuente_precarga else '' end as coFuentePrecarga, \n" +
                       "case " +
                       "     when p.ti_registro='TR' then en.de_entidad " +
                       "     when p.ti_registro='RM' and p.co_entidad is not null and p.co_entidad_eess is null then 'MUNICIPIO' " +
                       "     when p.ti_registro='RM' and p.co_entidad_eess is not null and p.co_entidad is null then 'ESTABLECIMIENTO DE SALUD' " +
                       "     when p.ti_registro in ('WS','SP') then 'RENIEC' " +
                       "     when p.ti_registro='MQ' then 'HISMINSA' " +
                       "     else '' " +
                       "end as deFuentePrecarga, \n" +
                       "TO_CHAR(FE_MODI_REGISTRO, 'DD/MM/YYYY') FE_MODI_REGISTRO,   \n" +
                       "P.CO_EST_SALUD_NAC, \n" +
                       "ES_NAC.DE_EST_SALUD DE_EST_SALUD_NAC, \n" +
                       "P.CO_EST_SALUD_ADS, \n" +
                       "ES_ADS.DE_EST_SALUD DE_EST_SALUD_ADS, \n" +
                       "(select  " +
                       "   decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and co_programa_social='0'), 1, '0, ', '') " +
                       "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='1'), 1, '1, ', '') " +
                       "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='2'), 1, '2, ', '') " +
                       "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='3'), 1, '3, ', '') " +
                       "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='4'), 1, '4, ', '') " +
                       "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='5'), 1, '5, ', '') " +
                       "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='7'), 1, '7, ', '') " +
                       "|| decode((select count(1) from pntv_padron_programa where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_programa_social='8'), 1, '8, ', '') " +
                       "from dual) coProgramasSociales, " +

                       "(select  " +
                       "   decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='0'), 1, '0, ', '') " +
                       "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='1'), 1, '1, ', '') " +
                       "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='2'), 1, '2, ', '') " +
                       "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='3'), 1, '3, ', '') " +
                       "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and in_afiliado='1' and co_tipo_seguro='4'), 1, '4, ', '') " +
                       "from dual) coTipoSeguros, " +

                       "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                       "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, \n"+
                       "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', '') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos, \n"+
                       "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                       "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', '') as deMenorVisitado" +
                       " FROM PNTV_PADRON_NOMINAL P   \n" +
                       "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES ON ES.CO_EST_SALUD=P.CO_EST_SALUD \n" +
                       "LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR   \n" +
                       "LEFT JOIN PNTR_VINCULO_FAMILIAR D1 ON D1.co_vinculo=p.TI_VINCULO_JEFE   \n" +
                       "LEFT JOIN PNTR_VINCULO_FAMILIAR D2 ON D2.co_vinculo=p.TI_VINCULO_MADRE   \n" +
                       "LEFT JOIN PNTR_FUENTE_DATOS FDM ON FDM.co_fuente_datos=p.co_fuente_datos   \n" +
                       "LEFT JOIN PNTR_DOMINIOS D3 ON D3.NO_DOM='CO_NIVEL_POBREZA' AND D3.CO_DOMINIO=CO_NIVEL_POBREZA   \n" +
                       "/*LEFT JOIN PNTR_SEGURO_SALUD D4 ON D4.TI_SEGURO_MENOR=P.TI_SEGURO_MENOR*/ \n" +
                       "LEFT JOIN PNTR_LENGUA D5 ON D5.CO_LENGUA=CO_LEN_MADRE \n" +
                       "LEFT JOIN PNTR_GRADO_INSTRUCCION D6 ON D6.co_grado_instruccion=p.CO_GRA_INST_MADRE \n" +
                       "LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI AND U.ES_UBIGEO='1' \n" +
                       "LEFT JOIN PNTM_CENTRO_EDUCATIVO IE ON IE.CO_CENTRO_EDUCATIVO=P.CO_INST_EDUCATIVA  \n" +
                       "LEFT JOIN PNTM_CENTRO_POBLADO CP ON CP.CO_CENTRO_POBLADO=P.CO_CENTRO_POBLADO  AND CP.ES_CENTRO_POBLADO='1' \n" +
                       "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES_NAC ON ES_NAC.CO_EST_SALUD=P.CO_EST_SALUD_NAC \n" +
                       "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES_ADS ON ES_ADS.CO_EST_SALUD=P.CO_EST_SALUD_ADS \n" +
                       "LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                       "LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                       "LEFT JOIN PNTM_ENTIDAD_ENVIO en ON en.co_entidad=p.fuente_precarga " +
                       "WHERE P.NU_EDAD<6  ";


    static String queryListadoPadronResumen =
            "SELECT CO_PADRON_NOMINAL, NU_DNI_MENOR, NU_CUI,    \n" +
            "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, P.CO_GENERO_MENOR, TO_CHAR(FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, FE_NAC_MENOR AS FE_NACIMIENTO, NU_EDAD AS EDAD,   \n" +
            "UPPER(U.DE_DEPARTAMENTO) DE_DEPARTAMENTO, UPPER(U.DE_PROVINCIA) DE_PROVINCIA, UPPER(U.DE_DISTRITO) DE_DISTRITO,   \n" +
            "NU_EDAD||' AÑO(S), '||NU_EDAD_MESES||' MES(ES)' EDADESCRITA, \n" +
            "UPPER(U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO) AS DE_UBIGEO_INEI  \n" +
            "FROM PNTV_PADRON_NOMINAL P   \n" +
            "LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR   \n" +
            "LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI  AND U.ES_UBIGEO='1'  \n" +
            "WHERE NU_EDAD<6  AND ES_PADRON='1' ";

    private Map<String, Object> buildParams(ListaMenores listaMenores) {
        String coUbigeo = "";
        String tiDocMenor = "";
        String coGeneroMenor = "";
        String periodoNac = "";
        String periodoRegistro = "";
        String edades = "";
        List<Object> params = new ArrayList<Object>();
        if (!listaMenores.getCoUbigeo().isEmpty()) {
            if (listaMenores.getCoUbigeo().length() == 2) {
                coUbigeo = " AND P.CO_UBIGEO_INEI LIKE ?||'%' ";
                params.add(listaMenores.getCoUbigeo());
            }
            if (listaMenores.getCoUbigeo().length() == 4) {
                coUbigeo = " AND P.CO_UBIGEO_INEI LIKE ?||'%' ";
                params.add(listaMenores.getCoUbigeo());
            }
            if (listaMenores.getCoUbigeo().length() == 6) {
                coUbigeo = " AND P.CO_UBIGEO_INEI=? ";
                params.add(listaMenores.getCoUbigeo());
            }
        }
        if (listaMenores.getTiDocMenor()!=null && !listaMenores.getTiDocMenor().isEmpty()) {
            if ("1".equals(listaMenores.getTiDocMenor())) {
                tiDocMenor = " AND NVL2(P.NU_DNI_MENOR,1,0)=1 ";
            } else if ("2".equals(listaMenores.getTiDocMenor())) {
                tiDocMenor = " AND NVL2(P.NU_CUI,1,0)=1 AND NVL2(P.NU_DNI_MENOR,1,0)=0 ";
            } else if ("3".equals(listaMenores.getTiDocMenor())) {
                tiDocMenor = " AND NVL2(P.NU_DNI_MENOR,1,0)=0 AND  NVL2(P.NU_CUI,1,0)=0 ";
            }
        }

        if (listaMenores.getCoGeneroMenor()!=null && !listaMenores.getCoGeneroMenor().isEmpty()) {
            if ("1".equals(listaMenores.getCoGeneroMenor())) {
                coGeneroMenor = " AND P.CO_GENERO_MENOR='1' ";
            }
            else if ("2".equals(listaMenores.getCoGeneroMenor())) {
                coGeneroMenor = " AND P.CO_GENERO_MENOR='2' ";
            }
        }
        if (listaMenores.getFeNacIni()!=null && listaMenores.getFeNacFin()!=null && !listaMenores.getFeNacIni().isEmpty() && !listaMenores.getFeNacFin().isEmpty()) {
            periodoNac = " AND (TRUNC(P.FE_NAC_MENOR) BETWEEN ? AND ?) ";
            params.add(listaMenores.getFeNacIni());
            params.add(listaMenores.getFeNacFin());
        }
        if (listaMenores.getFeIni()!=null && listaMenores.getFeFin()!=null && !listaMenores.getFeIni().isEmpty() && !listaMenores.getFeFin().isEmpty()) {
            params.add(listaMenores.getFeIni());
            params.add(listaMenores.getFeFin());
            if (listaMenores.getTiRegFecha() != null && !listaMenores.getTiRegFecha().isEmpty()) {
                if (listaMenores.getTiRegFecha().equals("N")){
                    periodoRegistro = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') = nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (listaMenores.getTiRegFecha().equals("U")){
                    periodoRegistro = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and nvl(p.US_CREA_REGISTRO, 'RENIEC') <> nvl(p.US_MODI_REGISTRO, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (listaMenores.getTiRegFecha().equals("T")){
                    params.add(listaMenores.getFeIni());
                    params.add(listaMenores.getFeFin());
                    params.add(listaMenores.getFeFin());
                    periodoRegistro = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                periodoRegistro = " AND (TRUNC(P.FE_MODI_REGISTRO) BETWEEN ? AND ?) ";
            }
        }
        if (listaMenores.getDeEdad()!=null && listaMenores.getHastaEdad()!=null && !listaMenores.getDeEdad().isEmpty() && !listaMenores.getHastaEdad().isEmpty()) {
            edades = " AND P.NU_EDAD BETWEEN ? AND ? ";
            params.add(listaMenores.getDeEdad());
            params.add(listaMenores.getHastaEdad());
        }

        Map result = new HashMap<String, Object>();
        result.put("query", coUbigeo + tiDocMenor + coGeneroMenor + periodoNac + periodoRegistro + edades);
        result.put("params", params);
        return result;
    }

    @Override
    public List<PadronNominal> listarPadron(ListaMenores listaMenores, String esPadron) {
        String filtroEsPadron = "";
        if(esPadron.equals("3"))
            filtroEsPadron = " and es_padron in('1','2') "; //TODOS
        else
            filtroEsPadron = " and es_padron=" + esPadron + " ";

        Map buildParams = buildParams(listaMenores);
        List<Object> params = (List<Object>) buildParams.get("params");
        String query = queryListadoPadron +
                filtroEsPadron +
                buildParams.get("query") +
                "\n order by p.fe_nac_menor desc" +
                "/*order by p.co_ubigeo_inei desc , p.fe_nac_menor desc*/";
        logger.debug(String.format("DAO '%s' con '%s'", query, params.toString()));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadron(ListaMenores listaMenores, int filaIni, int filaFin, String esPadron) {
        String filtroEsPadron = "";
        if(esPadron.equals("3"))
            filtroEsPadron = " AND ES_PADRON IN('1','2') "; //TODOS
        else
            filtroEsPadron = " AND ES_PADRON="+ esPadron + " ";

        Map buildParams = buildParams(listaMenores);
        List<Object> params = (List<Object>) buildParams.get("params");
        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "SELECT CO_PADRON_NOMINAL, NU_DNI_MENOR, NU_CUI,    \n" +
                "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, P.CO_GENERO_MENOR, TO_CHAR(FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, FE_NAC_MENOR AS FE_NACIMIENTO, NU_EDAD AS EDAD,   \n" +
                "UPPER(U.DE_DEPARTAMENTO) DE_DEPARTAMENTO, UPPER(U.DE_PROVINCIA) DE_PROVINCIA, UPPER(U.DE_DISTRITO) DE_DISTRITO,   \n" +
                "NU_EDAD||' AÑO(S), '||NU_EDAD_MESES||' MES(ES)' EDADESCRITA, \n" +
                "UPPER(U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO) AS DE_UBIGEO_INEI  \n" +
                "FROM PNTV_PADRON_NOMINAL P   \n" +
                "LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR   \n" +
                "LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI  AND U.ES_UBIGEO='1'  \n" +
                "WHERE NU_EDAD<6  " +
                filtroEsPadron +
                buildParams.get("query") +
                "\n order by p.fe_nac_menor desc" +
                "/*order by p.co_ubigeo_inei desc , p.fe_nac_menor desc*/" +
                ") t " +
                "where rownum<=?)  " +
                "where fila>=?";
        params.add(filaFin);
        params.add(filaIni);
        logger.debug(String.format("DAO '%s' con '%s'", query, params.toString()));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Integer countRowsListarPadron(ListaMenores listaMenores, String esPadron) {
        String filtroEsPadron = "";
        if(esPadron.equals("3"))
            filtroEsPadron = " and p.es_padron in('1','2') "; //TODOS
        else
            filtroEsPadron = " and p.es_padron=" + esPadron + " ";

        Map builParams = buildParams(listaMenores);
        List<Object> params = (List<Object>) builParams.get("params");
        String query = "select count(1) " +
                       "from pntv_padron_nominal p " +
                       "where p.nu_edad<6  " +
                        filtroEsPadron +
                        builParams.get("query");
        logger.debug(String.format("DAO '%s' con '%s'", query, params.toString()));
        try {
            return jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Menor> buscarMenorGrias(String apPrimerMenor, String apSegundoMenor, String prenombresMenor, String nuDoc, String tiDoc, int filaIni, int filaFin) {
        List<Object> params = new ArrayList<Object>();
        String where = "where es_padron='1' and nu_edad<6 and";

        if (!apPrimerMenor.isEmpty()) {
            where += " ap_primer_menor like ? and";
            params.add( apPrimerMenor +"%");
        }
        if (!apSegundoMenor.isEmpty()) {
            where += " ap_segundo_menor like ? and";
            params.add( apSegundoMenor+"%" );
        }
        if (!prenombresMenor.isEmpty()) {
            where += " prenombre_menor like ? and";
            params.add("%" + prenombresMenor + "%");
        }
        if (!nuDoc.isEmpty()) {
            if (tiDoc.equals(BuscarMenorDocumento.TipoDoc.DNI.getTiDoc())) {
                where += " nu_dni_menor=? and";
                params.add(nuDoc);
            } else if (tiDoc.equals(BuscarMenorDocumento.TipoDoc.CUI.getTiDoc())) {
                where += " nu_cui=? and";
                params.add(nuDoc);
            } else if (tiDoc.equals(BuscarMenorDocumento.TipoDoc.CNV.getTiDoc())) {
                where += " nu_cnv=? and";
                params.add(nuDoc);
            } else if (tiDoc.equals(BuscarMenorDocumento.TipoDoc.CO_PADRON.getTiDoc())) {
                where += " co_padron_nominal=? and";
                params.add(nuDoc);
            }
        }

        if (where.equals("where es_padron='1' and nu_edad<6 and"))
            return null;
        if (where.endsWith("and")) {
            where = where.substring(0, where.length() - 3);
        }

        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "select                 p.co_padron_nominal, " +
                "                       p.nu_dni_menor, " +
                "                       p.ap_primer_menor, " +
                "                       p.ap_segundo_menor, " +
                "                       p.prenombre_menor, " +
                "                       to_char(p.fe_nac_menor, 'dd/MM/yyyy') as FeNacMenor," +
                "                       upper(u.de_departamento||','||u.de_provincia||','||u.de_distrito) as de_ubigeo_inei,"+
                "                       p.es_padron esPadron " +
                "                  from pntv_padron_nominal p left join pntm_ubigeo_inei u on  p.co_ubigeo_inei = u.co_ubigeo_inei and u.es_ubigeo='1' " +
                where +
                "\n order by p.fe_nac_menor desc) t " +
                "where rownum<=?)  " +
                "where fila>=?";
        try{
            params.add(filaFin);
            params.add(filaIni);
            log.debug(String.format("DAO '%s' con '%s'", query, params));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Menor.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public int countBuscarMenorGrias(String apPrimerMenor, String apSegundoMenor, String prenombresMenor, String nuDoc, String tiDoc) {
        List<Object> params = new ArrayList<Object>();

        String where = "where es_padron='1' and nu_edad<6 and";

        if (!apPrimerMenor.isEmpty()) {
            where += " ap_primer_menor like ? and";
            params.add(apPrimerMenor+"%"  );
        }
        if (!apSegundoMenor.isEmpty()) {
            where += " ap_segundo_menor like ? and";
            params.add(apSegundoMenor+"%" );
        }
        if (!prenombresMenor.isEmpty()) {
            where += " prenombre_menor like ? and";
            params.add("%" + prenombresMenor + "%");
        }

        if (!nuDoc.isEmpty()) {
            if (tiDoc.equals(BuscarMenorDocumento.TipoDoc.DNI.getTiDoc())) {
                where += " nu_dni_menor=? and";
                params.add(nuDoc);
            } else if (tiDoc.equals(BuscarMenorDocumento.TipoDoc.CUI.getTiDoc())) {
                where += " nu_cui=? and";
                params.add(nuDoc);
            } else if (tiDoc.equals(BuscarMenorDocumento.TipoDoc.CNV.getTiDoc())) {
                where += " nu_cnv=? and";
                params.add(nuDoc);
            } else if (tiDoc.equals(BuscarMenorDocumento.TipoDoc.CO_PADRON.getTiDoc())) {
                where += " co_padron_nominal=? and";
                params.add(nuDoc);
            }
        }

        if (where.equals("where es_padron='1' and nu_edad<6 and"))
            return 0;
        if (where.endsWith("and")) {
            where = where.substring(0, where.length() - 3);
        }

        String query = "" +
                "select count(1) " +
                "  from pntv_padron_nominal " +
                where;
        log.debug(String.format("DAO '%s' con '%s'", query, params));
        return jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }
}