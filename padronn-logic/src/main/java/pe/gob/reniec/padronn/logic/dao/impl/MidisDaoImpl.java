package pe.gob.reniec.padronn.logic.dao.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.commons.baseApplication.dao.SimpleJdbcDaoBase;
import pe.gob.reniec.padronn.logic.dao.AbstractBaseDao;
import pe.gob.reniec.padronn.logic.dao.MidisDao;
import pe.gob.reniec.padronn.logic.dao.UbigeoDao;
import pe.gob.reniec.padronn.logic.dao.UsuarioExternoDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.ConsultaUbigeoForm;
import pe.gob.reniec.padronn.logic.model.form.ListaMenores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jfloresh on 02/12/2014.
 */
@Repository
public class MidisDaoImpl extends SimpleJdbcDaoBase implements MidisDao {
    static String queryListadoPadron =
                "SELECT P.* FROM (SELECT CO_PADRON_NOMINAL, NU_DNI_MENOR, NU_CUI, CASE WHEN TRIM(NU_DNI_MENOR) IS NULL AND  TRIM(NU_CUI) IS NULL THEN '3' WHEN TRIM(NU_CUI) IS NOT NULL AND TRIM(NU_DNI_MENOR) IS NULL THEN '2' WHEN TRIM(NU_DNI_MENOR) IS NOT NULL THEN '1' END AS TI_DOC_IDENTIDAD,   \n" +
                        "CASE WHEN TRIM(NU_DNI_MENOR) IS NULL AND  TRIM(NU_CUI) IS NULL THEN '' WHEN TRIM(NU_CUI) IS NOT NULL AND TRIM(NU_DNI_MENOR) IS NULL THEN NU_CUI WHEN TRIM(NU_DNI_MENOR) IS NOT NULL THEN NU_DNI_MENOR END CODOCUMENTOIDENTIDAD, \n" +
                        "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, CO_GENERO_MENOR, D.DE_GENERO_MENOR AS DE_GENERO_MENOR, TO_CHAR(FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, FE_NAC_MENOR AS FE_NACIMIENTO, NU_EDAD AS EDAD,   \n" +
                        "UPPER(U.DE_DEPARTAMENTO) DE_DEPARTAMENTO, UPPER(U.DE_PROVINCIA) DE_PROVINCIA, UPPER(U.DE_DISTRITO) DE_DISTRITO,   \n" +
                        "NU_EDAD||' AÑO(S), '||NU_EDAD_MESES||' MES(ES)' EDADESCRITA,NU_EDAD, NU_EDAD_MESES,   \n" +
                        "P.CO_CENTRO_POBLADO,   \n" +
                        "DECODE(P.CO_CENTRO_POBLADO, NULL, NULL, '', NULL, UPPER(CP.NO_CENTRO_POBLADO) || ', ' || UPPER(CP.NO_CATEGORIA)) AS DE_CENTRO_POBLADO,   \n" +
                        "P.DE_DIRECCION, P.CO_UBIGEO_INEI, UPPER(U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO) AS DE_UBIGEO_INEI, CO_ETNIA,   \n" +
                        "P.CO_EST_SALUD,   \n" +
                        "ES.DE_EST_SALUD AS DE_EST_SALUD,   \n" +
                        "/*TI_SEGURO_MENOR, D4.de_seguro_menor AS DE_SEGURO_MENOR,*/ NU_AFILIACION,   \n" +
                        "IE.CO_MODULAR AS CO_INST_EDUCATIVA,   \n" +
                        "IE.NO_CENTRO_EDUCATIVO DE_INST_EDUCATIVA,  \n" +
                        "CO_DNI_JEFE_FAM, AP_PRIMER_JEFE, AP_SEGUNDO_JEFE, PRENOM_JEFE, TI_VINCULO_JEFE, D1.de_vinculo AS DE_VINCULO_JEFE,   \n" +
                        "CO_DNI_MADRE, AP_PRIMER_MADRE, AP_SEGUNDO_MADRE, PRENOM_MADRE, TI_VINCULO_MADRE, D2.de_vinculo AS DE_VINCULO_MADRE,   \n" +
                        "CO_GRA_INST_MADRE, D6.DE_GRADO_INSTRUCCION AS DE_GRA_INST_MADRE, CO_LEN_MADRE, D5.DE_LENGUA AS DE_LEN_MADRE,   \n" +
                        "CO_NIVEL_POBREZA, D3.DE_DOM AS DE_NIVEL_POBREZA, " +
                        " case \n" +
                        "         when P.ti_registro='RM' THEN \n" +
                        "          'REGISTRO MANUAL' \n" +
                        "         when P.ti_registro='TR' THEN \n" +
                        "          'TRAMA' \n" +
                        "         when P.ti_registro='WS' THEN \n" +
                        "          'WEB SERVICE' \n" +
                        "         when P.ti_registro='SP' THEN \n" +
                        "          'WEB SERVICE' \n" +
                        "         when P.ti_registro='MQ' THEN \n" +
                        "          'HISMINSA' \n" +
                        " end as deTipoRegistro, \n" +
                        "CASE \n" +
                        "   WHEN P.US_CREA_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                        "   WHEN P.US_CREA_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                        "   WHEN P.US_CREA_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                        "   WHEN P.US_CREA_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                        "   ELSE P.US_CREA_REGISTRO \n" +
                        "END AS US_CREA_REGISTRO," +
                        "US_CREA_REGISTRO AS US_CREA_REGISTRO_MIDIS, " +
                        "FE_CREA_REGISTRO, " +
                        "CASE \n" +
                        "   WHEN P.US_MODI_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                        "   WHEN P.US_MODI_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                        "   WHEN P.US_MODI_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                        "   WHEN P.US_MODI_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                        "   ELSE P.US_MODI_REGISTRO \n" +
                        "END AS US_MODI_REGISTRO," +
                        "US_MODI_REGISTRO AS US_MODI_REGISTRO_MIDIS, \n" +
                        "FE_MODI_REGISTRO, \n" +
                        "(select  " +
                        "decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='0'), 1, '0, ', '') " +
                        "||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='1'), 1, '1, ', '') " +
                        "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='2'), 1, '2, ', '') " +
                        "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='3'), 1, '3, ', '') " +
                        "|| decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='4'), 1, '4, ', '') " +
                        "from dual) coTipoSeguros, " +
                        "DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '1'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') PIN, \n" +
                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '2'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') PVL, \n" +
                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '3'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') CUNA, \n" +
                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '4'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') JUNTOS, \n" +

                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '0'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') NINGUNO, \n" +

                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '7'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') CUNAMASSCD, \n" +

                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '8'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') CUNAMASSAF, \n" +

                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '5'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') QALIWARMA, \n" +
                        "P.CO_EST_SALUD_NAC, \n" +
                        "ES_NAC.DE_EST_SALUD AS DE_EST_SALUD_NAC, \n" +
                        "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                        "NVL(P.NU_CEL_MADRE,'') nuCelMadre, \n"+
                        "NVL(P.DI_CORREO_MADRE,'') diCorreoMadre, \n"+
                        "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, \n"+
                        "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', ' ') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos, \n"+
                        "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos," +
                        "decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', ' ') as deMenorVisitado" +
                        " FROM PNTV_PADRON_NOMINAL P \n" +
                        "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES ON ES.CO_EST_SALUD=P.CO_EST_SALUD \n" +
                        "LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR \n" +
                        "LEFT JOIN PNTR_VINCULO_FAMILIAR D1 ON D1.CO_VINCULO=p.TI_VINCULO_JEFE \n" +
                        "LEFT JOIN PNTR_VINCULO_FAMILIAR D2 ON D2.CO_VINCULO=p.TI_VINCULO_MADRE \n" +
                        "LEFT JOIN PNTR_FUENTE_DATOS FDM ON FDM.CO_FUENTE_DATOS=p.co_fuente_datos \n" +
                        "LEFT JOIN PNTR_DOMINIOS D3 ON D3.NO_DOM='CO_NIVEL_POBREZA' AND D3.CO_DOMINIO=CO_NIVEL_POBREZA \n" +
                        "/*LEFT JOIN PNTR_SEGURO_SALUD D4 ON D4.TI_SEGURO_MENOR=P.TI_SEGURO_MENOR*/ \n" +
                        "LEFT JOIN PNTR_LENGUA D5 ON D5.CO_LENGUA=p.CO_LEN_MADRE \n" +
                        "LEFT JOIN PNTR_GRADO_INSTRUCCION D6 ON D6.co_grado_instruccion=p.CO_GRA_INST_MADRE \n" +
                        "LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI AND U.ES_UBIGEO='1' \n" +
                        "LEFT JOIN PNTM_CENTRO_EDUCATIVO IE ON IE.CO_CENTRO_EDUCATIVO=P.CO_INST_EDUCATIVA \n" +
                        "LEFT JOIN PNTM_CENTRO_POBLADO CP ON CP.CO_CENTRO_POBLADO=P.CO_CENTRO_POBLADO  AND CP.ES_CENTRO_POBLADO='1'  \n" +
                        "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES_NAC ON ES_NAC.CO_EST_SALUD=P.CO_EST_SALUD_NAC \n" +
                        "LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA " +
                        "LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                        "WHERE NU_EDAD<6 AND ES_PADRON='1') P  \n" +
                        "WHERE '1'='1' ";

        static String queryListadoPadronResumen =
                "SELECT P.* FROM (SELECT CO_PADRON_NOMINAL, NU_DNI_MENOR, NU_CUI, TI_SEGURO_MENOR,   \n" +
                        "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, P.CO_GENERO_MENOR, TO_CHAR(FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, FE_NAC_MENOR AS FE_NACIMIENTO, NU_EDAD AS EDAD,NU_EDAD,NU_EDAD_MESES, \n" +
                        "UPPER(U.DE_DEPARTAMENTO) DE_DEPARTAMENTO, UPPER(U.DE_PROVINCIA) DE_PROVINCIA, UPPER(U.DE_DISTRITO) DE_DISTRITO,   \n" +
                        "NU_EDAD||' AÑO(S), '||NU_EDAD_MESES||' MES(ES)' EDADESCRITA, \n" +
                        "CO_UBIGEO_INEI, FE_CREA_REGISTRO, US_CREA_REGISTRO, US_CREA_REGISTRO AS US_CREA_REGISTRO_MIDIS, FE_MODI_REGISTRO, US_MODI_REGISTRO, US_MODI_REGISTRO AS US_MODI_REGISTRO_MIDIS, \n" +
                        "UPPER(U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO) AS DE_UBIGEO_INEI, \n" +
                        "DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '1'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') PIN, \n" +
                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '2'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') PVL, \n" +
                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '3'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') CUNA, \n" +
                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '4'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') JUNTOS, \n" +
                        "       DECODE((SELECT COUNT(1) \n" +
                        "                FROM PNTV_PADRON_PROGRAMA \n" +
                        "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                        "                 AND CO_PROGRAMA_SOCIAL = '5'), \n" +
                        "              1, \n" +
                        "              '1', \n" +
                        "              '0') QALIWARMA \n" +
                        "FROM PNTV_PADRON_NOMINAL P   \n" +
                        "LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR   \n" +
                        "LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI  AND U.ES_UBIGEO='1'  \n" +
                        "WHERE NU_EDAD<6 AND ES_PADRON='1' ) P \n" +
                        "WHERE '1'='1' ";

    private Map<String, Object> buildParams(ConsultaUbigeoForm consultaUbigeoForm) {
        String coUbigeo = "";
        String tiDocMenor = "";
        String coGeneroMenor = "";
        String coProgramaSocial = "";
        String tiSeguroMenor = "";
        String edades = "";
        String periodoRegistro = "";

        List<Object> params = new ArrayList<Object>();
        if (!consultaUbigeoForm.getCoUbigeo().isEmpty()) {
            if (consultaUbigeoForm.getCoUbigeo().length() == 2) {
//                coUbigeo = " and substr(p.co_ubigeo_inei,0,2)=? ";
                coUbigeo = " and p.co_ubigeo_inei like ?||'%' ";
                params.add(consultaUbigeoForm.getCoUbigeo());
            }
            if (consultaUbigeoForm.getCoUbigeo().length() == 4) {
//                coUbigeo = " and substr(p.co_ubigeo_inei,0,4)=? ";
                coUbigeo = " and p.co_ubigeo_inei like ?||'%' ";
                params.add(consultaUbigeoForm.getCoUbigeo());
            }
            if (consultaUbigeoForm.getCoUbigeo().length() == 6) {
//                coUbigeo = " and p.co_ubigeo_inei=? ";
                coUbigeo = " and p.co_ubigeo_inei like ?||'%' ";
                params.add(consultaUbigeoForm.getCoUbigeo());
            }
        //coUbigeo = " and p.co_ubigeo_inei like ? ";
        //params.add(listaMenores.getCoUbigeo()+"%");
        }
        if (consultaUbigeoForm.getTiDocMenor()!=null && !consultaUbigeoForm.getTiDocMenor().isEmpty()) {
            /*
                case when nvl2(p.nu_dni_menor, 1,0)=0 and  nvl2(p.nu_cui,1,0)=0 then '3'
                when nvl2(p.nu_cui,1,0)=1 and nvl2(p.nu_dni_menor,1,0)=0 then '2'
                when nvl2(p.nu_dni_menor,1,0)=1 then '1'
            */
            if ("1".equals(consultaUbigeoForm.getTiDocMenor())) {
                tiDocMenor = " and nvl2(p.nu_dni_menor,1,0)=1 ";
//                params.add(listaMenores.getTiDocMenor());
            } else if ("2".equals(consultaUbigeoForm.getTiDocMenor())) {
                tiDocMenor = " and nvl2(p.nu_cui,1,0)=1 and nvl2(p.nu_dni_menor,1,0)=0 ";
//                params.add(listaMenores.getTiDocMenor());
            } else if ("3".equals(consultaUbigeoForm.getTiDocMenor())) {
                tiDocMenor = " and nvl2(p.nu_dni_menor,1,0)=0 and  nvl2(p.nu_cui,1,0)=0 ";
            }
        }

        if (consultaUbigeoForm.getCoGeneroMenor()!=null && !consultaUbigeoForm.getCoGeneroMenor().isEmpty()) {
            if ("1".equals(consultaUbigeoForm.getCoGeneroMenor())) {
                coGeneroMenor = " and p.co_genero_menor='1' ";
            }
            else if ("2".equals(consultaUbigeoForm.getCoGeneroMenor())) {
                coGeneroMenor = " and p.co_genero_menor='2' ";
            }
        }
        if (consultaUbigeoForm.getCoProgramaSocial() != null && !consultaUbigeoForm.getCoProgramaSocial().isEmpty()) {
            /*1	PIN
            2	PVL
            3	CUNA+
            4	JUNTOS
            5	QALIWARMA*/
            if ("1".equals(consultaUbigeoForm.getCoProgramaSocial())) {
                coProgramaSocial = " and p.pin='1' ";
            } else if ("2".equals(consultaUbigeoForm.getCoProgramaSocial())) {
                coProgramaSocial = " and p.pvl='1' ";
            } else if ("3".equals(consultaUbigeoForm.getCoProgramaSocial())) {
                coProgramaSocial = " and p.cuna='1' ";
            } else if ("4".equals(consultaUbigeoForm.getCoProgramaSocial())) {
                coProgramaSocial = " and p.juntos='1' ";
            } else if ("5".equals(consultaUbigeoForm.getCoProgramaSocial())) {
                coProgramaSocial = " and p.QALIWARMA='1' ";
            }
        }
        /**
         1	SIS
         2	ESSALUD
         3	SANIDAD
         4	PRIVADO
         6	NINGUNO
         */
        if (consultaUbigeoForm.getTiSeguro() != null && !consultaUbigeoForm.getTiSeguro().isEmpty()) {
            if ("1".equals(consultaUbigeoForm.getTiSeguro())){
                tiSeguroMenor = " and p.ti_seguro_menor='1' ";
            }else if ("2".equals(consultaUbigeoForm.getTiSeguro())){
                tiSeguroMenor = " and p.ti_seguro_menor='2' ";
            }else if ("3".equals(consultaUbigeoForm.getTiSeguro())){
                tiSeguroMenor = " and p.ti_seguro_menor='3' ";
            }else if ("4".equals(consultaUbigeoForm.getTiSeguro())){
                tiSeguroMenor = " and p.ti_seguro_menor='4' ";
            }else if ("6".equals(consultaUbigeoForm.getTiSeguro())){
                tiSeguroMenor = " and p.ti_seguro_menor='6' ";
            }
        }

        if (consultaUbigeoForm.getDeEdad()!=null && consultaUbigeoForm.getHastaEdad()!=null && !consultaUbigeoForm.getDeEdad().isEmpty() && !consultaUbigeoForm.getHastaEdad().isEmpty()) {
            edades = " and nu_edad between ? and ? ";
            params.add(consultaUbigeoForm.getDeEdad());
            params.add(consultaUbigeoForm.getHastaEdad());
        }
        if (consultaUbigeoForm.getFeIni()!=null && consultaUbigeoForm.getFeFin()!=null && !consultaUbigeoForm.getFeIni().isEmpty() && !consultaUbigeoForm.getFeFin().isEmpty()) {
            params.add(consultaUbigeoForm.getFeIni());
            params.add(consultaUbigeoForm.getFeFin());
            if (consultaUbigeoForm.getTiRegFecha() !=null && !consultaUbigeoForm.getTiRegFecha().isEmpty()){
                if (consultaUbigeoForm.getTiRegFecha().equals("N")){
                    periodoRegistro = " and (trunc(p.fe_crea_registro) between ? and ?) and p.fe_crea_registro is not null " +
                            " and ((p.fe_modi_registro is null) or (p.fe_crea_registro = p.fe_modi_registro and " +
                            " nvl(p.US_CREA_REGISTRO_MIDIS, 'RENIEC') = nvl(p.US_MODI_REGISTRO_MIDIS, 'RENIEC')))"; //Validacion para considerar registros como nuevos
                } else if (consultaUbigeoForm.getTiRegFecha().equals("U")){
                    periodoRegistro = " and (trunc(p.fe_modi_registro) between ? and ?) and p.fe_modi_registro is not null " +
                            " and (p.fe_modi_registro <> p.fe_crea_registro or (p.fe_modi_registro = p.fe_crea_registro and " +
                            " nvl(p.US_CREA_REGISTRO_MIDIS, 'RENIEC') <> nvl(p.US_MODI_REGISTRO_MIDIS, 'RENIEC')))"; //Validacion para considerar registros como actualizados
                } else if (consultaUbigeoForm.getTiRegFecha().equals("T")){
                    params.add(consultaUbigeoForm.getFeIni());
                    params.add(consultaUbigeoForm.getFeFin());
                    params.add(consultaUbigeoForm.getFeFin());
                    periodoRegistro = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) " +
                            " and (trunc(p.fe_modi_registro) <= ? or p.fe_modi_registro is null)";
                }
            } else {
                periodoRegistro = " and ((trunc(p.fe_crea_registro) between ? and ?) or (trunc(p.fe_modi_registro) between ? and ?)) ";
                params.add(consultaUbigeoForm.getFeIni());
                params.add(consultaUbigeoForm.getFeFin());
            }
        }

        Map result = new HashMap<String, Object>();
        result.put("query", coUbigeo + tiDocMenor + coGeneroMenor + coProgramaSocial + tiSeguroMenor + edades + periodoRegistro);
        result.put("params", params);
        return result;
    }

    @Override
    public List<PadronNominal> listarPadron(ConsultaUbigeoForm consultaUbigeoForm, String esPadron) {

        Map buildParams = buildParams(consultaUbigeoForm);
        List<Object> params = (List<Object>) buildParams.get("params");

        String filtroEsPadron = "";
        if(esPadron.equals("3"))
           filtroEsPadron = " and es_padron in('1','2') "; //TODOS
        else
           filtroEsPadron = " and es_padron="+esPadron+" "; //TODOS


        String query = "SELECT P.* FROM (SELECT CO_PADRON_NOMINAL, NU_DNI_MENOR, NU_CUI, CASE WHEN TRIM(NU_DNI_MENOR) IS NULL AND  TRIM(NU_CUI) IS NULL THEN '3' WHEN TRIM(NU_CUI) IS NOT NULL AND TRIM(NU_DNI_MENOR) IS NULL THEN '2' WHEN TRIM(NU_DNI_MENOR) IS NOT NULL THEN '1' END AS TI_DOC_IDENTIDAD,   \n" +
                "CASE WHEN TRIM(NU_DNI_MENOR) IS NULL AND  TRIM(NU_CUI) IS NULL THEN '' WHEN TRIM(NU_CUI) IS NOT NULL AND TRIM(NU_DNI_MENOR) IS NULL THEN NU_CUI WHEN TRIM(NU_DNI_MENOR) IS NOT NULL THEN NU_DNI_MENOR END CODOCUMENTOIDENTIDAD, \n" +
                "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, CO_GENERO_MENOR, D.DE_GENERO_MENOR AS DE_GENERO_MENOR, TO_CHAR(FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, FE_NAC_MENOR AS FE_NACIMIENTO, NU_EDAD AS EDAD,   \n" +
                "UPPER(U.DE_DEPARTAMENTO) DE_DEPARTAMENTO, UPPER(U.DE_PROVINCIA) DE_PROVINCIA, UPPER(U.DE_DISTRITO) DE_DISTRITO,   \n" +
                "NU_EDAD||' AÑO(S), '||NU_EDAD_MESES||' MES(ES)' EDADESCRITA,NU_EDAD, NU_EDAD_MESES,   \n" +
                "P.CO_CENTRO_POBLADO, P.ES_PADRON,  \n" +
                "DECODE(P.CO_CENTRO_POBLADO, NULL, NULL, '', NULL, UPPER(CP.NO_CENTRO_POBLADO) || ', ' || UPPER(CP.NO_CATEGORIA)) AS DE_CENTRO_POBLADO,   \n" +
                "P.DE_DIRECCION, P.CO_UBIGEO_INEI, UPPER(U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO) AS DE_UBIGEO_INEI, CO_ETNIA,   \n" +
                "P.CO_EST_SALUD,   \n" +
                "ES.DE_EST_SALUD AS DE_EST_SALUD,   \n" +
                "/*TI_SEGURO_MENOR, D4.de_seguro_menor AS DE_SEGURO_MENOR,*/ NU_AFILIACION,   \n" +
                "IE.CO_MODULAR AS CO_INST_EDUCATIVA,   \n" +
                "IE.NO_CENTRO_EDUCATIVO DE_INST_EDUCATIVA,  \n" +
                "CO_DNI_JEFE_FAM, AP_PRIMER_JEFE, AP_SEGUNDO_JEFE, PRENOM_JEFE, TI_VINCULO_JEFE, D1.de_vinculo AS DE_VINCULO_JEFE,   \n" +
                "CO_DNI_MADRE, AP_PRIMER_MADRE, AP_SEGUNDO_MADRE, PRENOM_MADRE, TI_VINCULO_MADRE, D2.de_vinculo AS DE_VINCULO_MADRE,   \n" +
                "CO_GRA_INST_MADRE, D6.DE_GRADO_INSTRUCCION AS DE_GRA_INST_MADRE, CO_LEN_MADRE, D5.DE_LENGUA AS DE_LEN_MADRE,   \n" +
                "CO_NIVEL_POBREZA, D3.DE_DOM AS DE_NIVEL_POBREZA,  case \n" +
                "         when P.ti_registro='RM' THEN \n" +
                "          'REGISTRO MANUAL' \n" +
                "         when P.ti_registro='TR' THEN \n" +
                "          'TRAMA' \n" +
                "         when P.ti_registro='WS' THEN \n" +
                "          'WEB SERVICE' \n" +
                "         when P.ti_registro='SP' THEN \n" +
                "          'WEB SERVICE' \n" +
                "         when P.ti_registro='MQ' THEN \n" +
                "          'HISMINSA' \n" +
                " end as deTipoRegistro, \n" +
                "CASE \n" +
                "   WHEN P.US_CREA_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN P.US_CREA_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN P.US_CREA_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN P.US_CREA_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE P.US_CREA_REGISTRO \n" +
                "END AS US_CREA_REGISTRO,US_CREA_REGISTRO AS US_CREA_REGISTRO_MIDIS, FE_CREA_REGISTRO, CASE \n" +
                "   WHEN P.US_MODI_REGISTRO IN('SP-CNV','WSHVCNV') THEN 'SERVICIO CNV' \n" +
                "   WHEN P.US_MODI_REGISTRO IN('SP-RC','WSRCACTA','SP-DNI','SP-EDAD') THEN 'SERVICIO ACTA' \n" +
                "   WHEN P.US_MODI_REGISTRO IN('RENIECTR') THEN 'SERVICIO TRAMA' \n" +
                "   WHEN P.US_MODI_REGISTRO IN('HISMINSA') THEN 'SERVICIO HISMINSA' \n" +
                "   ELSE P.US_MODI_REGISTRO \n" +
                "END AS US_MODI_REGISTRO,US_MODI_REGISTRO AS US_MODI_REGISTRO_MIDIS, \n" +
                "FE_MODI_REGISTRO, \n" +
                "(select  decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='0'), 1, '0, ', '') ||decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='1'), 1, '1, ', '') || decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='2'), 1, '2, ', '') || decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='3'), 1, '3, ', '') || decode((select count(1) from pntv_padron_seguro where co_padron_nominal=p.co_padron_nominal and co_tipo_seguro='4'), 1, '4, ', '') from dual) coTipoSeguros, DECODE((SELECT COUNT(1) \n" +
                "                FROM PNTV_PADRON_PROGRAMA \n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                "                 AND CO_PROGRAMA_SOCIAL = '1'), \n" +
                "              1, \n" +
                "              '1', \n" +
                "              '0') PIN, \n" +
                "       DECODE((SELECT COUNT(1) \n" +
                "                FROM PNTV_PADRON_PROGRAMA \n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                "                 AND CO_PROGRAMA_SOCIAL = '2'), \n" +
                "              1, \n" +
                "              '1', \n" +
                "              '0') PVL, \n" +
                "       DECODE((SELECT COUNT(1) \n" +
                "                FROM PNTV_PADRON_PROGRAMA \n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                "                 AND CO_PROGRAMA_SOCIAL = '3'), \n" +
                "              1, \n" +
                "              '1', \n" +
                "              '0') CUNA, \n" +
                "       DECODE((SELECT COUNT(1) \n" +
                "                FROM PNTV_PADRON_PROGRAMA \n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                "                 AND CO_PROGRAMA_SOCIAL = '4'), \n" +
                "              1, \n" +
                "              '1', \n" +
                "              '0') JUNTOS, \n" +
                "       DECODE((SELECT COUNT(1) \n" +
                "                FROM PNTV_PADRON_PROGRAMA \n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                "                 AND CO_PROGRAMA_SOCIAL = '0'), \n" +
                "              1, \n" +
                "              '1', \n" +
                "              '0') NINGUNO, \n" +
                "       DECODE((SELECT COUNT(1) \n" +
                "                FROM PNTV_PADRON_PROGRAMA \n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                "                 AND CO_PROGRAMA_SOCIAL = '7'), \n" +
                "              1, \n" +
                "              '1', \n" +
                "              '0') CUNAMASSCD, \n" +
                "       DECODE((SELECT COUNT(1) \n" +
                "                FROM PNTV_PADRON_PROGRAMA \n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                "                 AND CO_PROGRAMA_SOCIAL = '8'), \n" +
                "              1, \n" +
                "              '1', \n" +
                "              '0') CUNAMASSAF, \n" +
                "       DECODE((SELECT COUNT(1) \n" +
                "                FROM PNTV_PADRON_PROGRAMA \n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL \n" +
                "                 AND CO_PROGRAMA_SOCIAL = '5'), \n" +
                "              1, \n" +
                "              '1', \n" +
                "              '0') QALIWARMA, \n" +
                "P.CO_EST_SALUD_NAC, \n" +
                "ES_NAC.DE_EST_SALUD AS DE_EST_SALUD_NAC, \n" +
                "CP.DE_AREA_CCPP, TV.DE_CAT_VIA_DET || ' ' || V.DE_VIA  AS  DE_VIA, DE_REF_DIR, \n" +
                "NVL(P.NU_CEL_MADRE,'') nuCelMadre, \n" +
                "NVL(P.DI_CORREO_MADRE,'') diCorreoMadre, \n" +
                "nvl(to_char(p.fe_visita, 'dd/mm/yyyy'),' ') as feVisita, nvl(p.in_menor_enc,' ') as coMenorEncontrado, \n" +
                "decode(p.in_menor_enc, '1', 'SI', '0', 'NO', ' ') as deMenorEncontrado, nvl(to_char(p.fe_ultima_toma_datos, 'dd/mm/yyyy'),' ') as feUltimaTomaDatos, \n" +
                "nvl(p.in_menor_visitado,'0') as inMenorVisitado, p.co_fuente_datos, nvl(FDM.de_fuente_datos, '') as deFuenteDatos,decode(p.in_menor_visitado, '1', 'VISITADO', '0', 'NO VISITADO', ' ') as deMenorVisitado FROM PNTV_PADRON_NOMINAL P \n" +
                "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES ON ES.CO_EST_SALUD=P.CO_EST_SALUD \n" +
                "LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR \n" +
                "LEFT JOIN PNTR_VINCULO_FAMILIAR D1 ON D1.CO_VINCULO=p.TI_VINCULO_JEFE \n" +
                "LEFT JOIN PNTR_VINCULO_FAMILIAR D2 ON D2.CO_VINCULO=p.TI_VINCULO_MADRE \n" +
                "LEFT JOIN PNTR_FUENTE_DATOS FDM ON FDM.CO_FUENTE_DATOS=p.co_fuente_datos \n" +
                "LEFT JOIN PNTR_DOMINIOS D3 ON D3.NO_DOM='CO_NIVEL_POBREZA' AND D3.CO_DOMINIO=CO_NIVEL_POBREZA \n" +
                "/*LEFT JOIN PNTR_SEGURO_SALUD D4 ON D4.TI_SEGURO_MENOR=P.TI_SEGURO_MENOR*/ \n" +
                "LEFT JOIN PNTR_LENGUA D5 ON D5.CO_LENGUA=p.CO_LEN_MADRE \n" +
                "LEFT JOIN PNTR_GRADO_INSTRUCCION D6 ON D6.co_grado_instruccion=p.CO_GRA_INST_MADRE \n" +
                "LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI AND U.ES_UBIGEO='1' \n" +
                "LEFT JOIN PNTM_CENTRO_EDUCATIVO IE ON IE.CO_CENTRO_EDUCATIVO=P.CO_INST_EDUCATIVA \n" +
                "LEFT JOIN PNTM_CENTRO_POBLADO CP ON CP.CO_CENTRO_POBLADO=P.CO_CENTRO_POBLADO  AND CP.ES_CENTRO_POBLADO='1'  \n" +
                "LEFT JOIN PNVM_ESTABLECIMIENTO_SALUD ES_NAC ON ES_NAC.CO_EST_SALUD=P.CO_EST_SALUD_NAC \n" +
                "LEFT JOIN PNTM_VIA V ON V.CO_VIA=P.CO_VIA LEFT JOIN PNTR_TIPO_VIA TV ON TV.CO_CAT_VIA=V.CO_CAT_VIA " +
                "WHERE NU_EDAD<6 " +
                filtroEsPadron +
                ") P  \n" +
                "WHERE '1'='1' " +
                buildParams.get("query") +
                "\n order by to_date(p.fe_nac_menor, 'dd/mm/yyyy') desc " +
                "/*order by p.co_ubigeo_inei desc , p.fe_nac_menor desc*/";
        logger.debug(String.format("DAO '%s' con '%s'", query, params.toString()));
        try {
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(PadronNominal.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<PadronNominal> listarPadron(ConsultaUbigeoForm consultaUbigeoForm, int filaIni, int filaFin, String esPadron) {
        Map buildParams = buildParams(consultaUbigeoForm);
        List<Object> params = (List<Object>) buildParams.get("params");
        String filtroEsPadron = "";

        if(esPadron.equals("3"))
            filtroEsPadron = " and es_padron in('1','2') "; //TODOS
        else
            filtroEsPadron = " and es_padron="+esPadron+" ";


        String query = "" +
                "select *  from  " +
                "(select rownum fila, t.* " +
                "from  ( " +
                "SELECT P.* FROM (SELECT CO_PADRON_NOMINAL, NU_DNI_MENOR, NU_CUI, TI_SEGURO_MENOR,\n" +
                "AP_PRIMER_MENOR, AP_SEGUNDO_MENOR, PRENOMBRE_MENOR, P.CO_GENERO_MENOR, TO_CHAR(FE_NAC_MENOR,'DD/MM/YYYY') AS FE_NAC_MENOR, FE_NAC_MENOR AS FE_NACIMIENTO, NU_EDAD AS EDAD,NU_EDAD,NU_EDAD_MESES,\n" +
                "UPPER(U.DE_DEPARTAMENTO) DE_DEPARTAMENTO, UPPER(U.DE_PROVINCIA) DE_PROVINCIA, UPPER(U.DE_DISTRITO) DE_DISTRITO,\n" +
                "NU_EDAD||' AÑO(S), '||NU_EDAD_MESES||' MES(ES)' EDADESCRITA,\n" +
                "CO_UBIGEO_INEI, FE_CREA_REGISTRO, US_CREA_REGISTRO, US_CREA_REGISTRO AS US_CREA_REGISTRO_MIDIS, FE_MODI_REGISTRO, US_MODI_REGISTRO, US_MODI_REGISTRO AS US_MODI_REGISTRO_MIDIS,\n" +
                "UPPER(U.DE_DEPARTAMENTO||', '||U.DE_PROVINCIA||', '||U.DE_DISTRITO) AS DE_UBIGEO_INEI,\n" +
                "DECODE((SELECT COUNT(1)\n" +
                "                FROM PNTV_PADRON_PROGRAMA\n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL\n" +
                "                 AND CO_PROGRAMA_SOCIAL = '1'),\n" +
                "              1,\n" +
                "              '1',\n" +
                "              '0') PIN,\n" +
                "       DECODE((SELECT COUNT(1)\n" +
                "                FROM PNTV_PADRON_PROGRAMA\n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL\n" +
                "                 AND CO_PROGRAMA_SOCIAL = '2'),\n" +
                "              1,\n" +
                "              '1',\n" +
                "              '0') PVL,\n" +
                "       DECODE((SELECT COUNT(1)\n" +
                "                FROM PNTV_PADRON_PROGRAMA\n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL\n" +
                "                 AND CO_PROGRAMA_SOCIAL = '3'),\n" +
                "              1,\n" +
                "              '1',\n" +
                "              '0') CUNA,\n" +
                "       DECODE((SELECT COUNT(1)\n" +
                "                FROM PNTV_PADRON_PROGRAMA\n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL\n" +
                "                 AND CO_PROGRAMA_SOCIAL = '4'),\n" +
                "              1,\n" +
                "              '1',\n" +
                "              '0') JUNTOS,\n" +
                "       DECODE((SELECT COUNT(1)\n" +
                "                FROM PNTV_PADRON_PROGRAMA\n" +
                "               WHERE CO_PADRON_NOMINAL = P.CO_PADRON_NOMINAL\n" +
                "                 AND CO_PROGRAMA_SOCIAL = '5'),\n" +
                "              1,\n" +
                "              '1',\n" +
                "              '0') QALIWARMA\n" +
                "FROM PNTV_PADRON_NOMINAL P\n" +
                "LEFT JOIN PNTR_GENERO_MENOR D ON D.CO_GENERO_MENOR=P.CO_GENERO_MENOR\n" +
                "LEFT JOIN PNTM_UBIGEO_INEI U ON U.CO_UBIGEO_INEI=P.CO_UBIGEO_INEI  AND U.ES_UBIGEO='1'\n" +
                "WHERE NU_EDAD<6 " +
                filtroEsPadron +
                ") P\n" +
                "WHERE '1'='1'  " +
                buildParams.get("query") +
                "\n order by to_date(p.fe_nac_menor, 'dd/mm/yyyy') desc" +
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
    public Integer countRowsListarPadron(ConsultaUbigeoForm consultaUbigeoForm, String esPadron) {
        Map builParams = buildParams(consultaUbigeoForm);
        List<Object> params = (List<Object>) builParams.get("params");

        String filtroEsPadron = "";
        if(esPadron.equals("3"))
            filtroEsPadron = " and es_padron in('1','2') "; //TODOS
        else
            filtroEsPadron = " and es_padron="+esPadron+" ";



        String query = "";
        if (consultaUbigeoForm.getCoProgramaSocial() != null && consultaUbigeoForm.getCoProgramaSocial().isEmpty()) {
            query = "select count(1) from (select p.*, p.US_CREA_REGISTRO as US_CREA_REGISTRO_MIDIS, p.US_MODI_REGISTRO as US_MODI_REGISTRO_MIDIS " +
                    "from pntv_padron_nominal p) p " +
                    "where nu_edad<6 " +
                    filtroEsPadron +
                    builParams.get("query");
        } else {
            query = "select count(1) from " +
                    "(select " +
                    "decode((select count(1) " +
                    "                from pntv_padron_programa " +
                    "               where co_padron_nominal = p.co_padron_nominal " +
                    "                 and co_programa_social = '1'), " +
                    "              1, " +
                    "              '1', " +
                    "              '0') PIN, " +
                    "       decode((select count(1) " +
                    "                from pntv_padron_programa " +
                    "               where co_padron_nominal = p.co_padron_nominal " +
                    "                 and co_programa_social = '2'), " +
                    "              1, " +
                    "              '1', " +
                    "              '0') PVL, " +
                    "       decode((select count(1) " +
                    "                from pntv_padron_programa " +
                    "               where co_padron_nominal = p.co_padron_nominal " +
                    "                 and co_programa_social = '3'), " +
                    "              1, " +
                    "              '1', " +
                    "              '0') CUNA, " +
                    "       decode((select count(1) " +
                    "                from pntv_padron_programa " +
                    "               where co_padron_nominal = p.co_padron_nominal " +
                    "                 and co_programa_social = '4'), " +
                    "              1, " +
                    "              '1', " +
                    "              '0') JUNTOS, " +
                    "       decode((select count(1) " +
                    "                from pntv_padron_programa " +
                    "               where co_padron_nominal = p.co_padron_nominal " +
                    "                 and co_programa_social = '5'), " +
                    "              1, " +
                    "              '1', " +
                    "              '0') QALIWARMA,co_ubigeo_inei,nu_dni_menor,co_genero_menor, ti_seguro_menor, nu_edad, fe_crea_registro, fe_modi_registro, nu_cui " +
                    "from pntv_padron_nominal p " +
                    "where nu_edad<6 " +
                    /*"and es_padron='1'" +*/
                    filtroEsPadron +
                    ") " +
                    "p where 1=1 " + builParams.get("query");
        }

        logger.debug(String.format("DAO '%s' con '%s'", query, params.toString()));
        try {
            return jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Menor> buscarMenor(String apPrimerMenor, String apSegundoMenor, String prenombresMenor, String codigoPadron, int filaIni, int filaFin) {
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
        if (!codigoPadron.isEmpty()) {
            where += " co_padron_nominal = ? ";
            params.add(codigoPadron);
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
                "select p.co_padron_nominal, " +
                "                       p.nu_dni_menor, " +
                "                       p.ap_primer_menor, " +
                "                       p.ap_segundo_menor, " +
                "                       p.prenombre_menor, " +
                "                       to_char(p.fe_nac_menor, 'dd/MM/yyyy') as FeNacMenor," +
                "                       upper(u.de_departamento||','||u.de_provincia||','||u.de_distrito) as de_ubigeo_inei,"+
                "                       p.es_padron esPadron " +
                "                  from pntv_padron_nominal p left join pntm_ubigeo_inei u on  p.co_ubigeo_inei = u.co_ubigeo_inei and u.es_ubigeo='1' " +
                where +
                ") t " +
                "where rownum<=?)  " +
                "where fila>=?";
        try{
            params.add(filaFin);
            params.add(filaIni);
            logger.debug(String.format("DAO '%s' con '%s'", query, params));
            return jdbcTemplate.query(query, ParameterizedBeanPropertyRowMapper.newInstance(Menor.class), params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public int countBuscarMenor(String apPrimerMenor, String apSegundoMenor, String prenombresMenor, String codigoPadron) {
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
        if (!codigoPadron.isEmpty()) {
            where += " co_padron_nominal=? and";
            params.add(codigoPadron);
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
        logger.debug(String.format("DAO '%s' con '%s'", query, params));
        return jdbcTemplate.queryForInt(query, params.toArray(new Object[params.size()]));
    }

    @Autowired
    UbigeoDao ubigeoDao;

    @Autowired
    UsuarioExternoDao usuarioExternoDao;

    @Override
    public List<PadronMovimiento> listadoPadronMovimiento(String coUbigeo, String feIni, String feFin) {
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
                "       to_char(m.fe_crea_audi, 'dd/MM/yyyy')     as feCreaAudi, " +
                "       to_char(m.fe_modi_audi, 'dd/MM/yyyy')     as feModiAudi, " +
                "       m.us_crea_audi as usCreaAudi, " +
                "       m.us_modi_audi as usModiAudi, " +
                "       m.co_centro_poblado_ant as coCentroPobladoAnt, " +
                "       m.co_centro_poblado_act as coCentroPobladoAct " +
                "  from pntv_padron_movimiento m " +
                "  left join pntv_padron_nominal p on p.co_padron_nominal = " +
                "                                     m.co_padron_nominal " +
                "  where p.es_padron='1' and trunc(m.fe_crea_audi) between ? and ? and m.co_ubigeo_inei_ant like ?||'%' " + // todo
                " order by p.fe_nac_menor desc, m.co_ubigeo_inei_act, m.nu_sec_act";
        List<PadronMovimiento> movimientos = new ArrayList<PadronMovimiento>();
        Object[] params = new Object[]{feIni, feFin, coUbigeo};
        try{
            List<PadronMovimiento> padronMovimientos = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(PadronMovimiento.class), params);
            for(PadronMovimiento padronMovimiento: padronMovimientos){
                Ubigeo ubigeoAnt = ubigeoDao.obtenerPorCodigoInei(padronMovimiento.getCoUbigeoIneiAnt());

                Entidad entidadAnt = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAnt(), Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());

                Ubigeo ubigeoAct = ubigeoDao.obtenerPorCodigoInei(padronMovimiento.getCoUbigeoIneiAct());

                Entidad entidadAct = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAct(), Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());

                CentroPoblado centroPobladoAnt = ubigeoDao.getCentroPoblado(padronMovimiento.getCoCentroPobladoAnt());

                CentroPoblado centroPobladoAct = ubigeoDao.getCentroPoblado(padronMovimiento.getCoCentroPobladoAct());

                padronMovimiento.setDeDepartamentoAnt(ubigeoAnt.getDeDepartamento());
                padronMovimiento.setDeProvinciaAnt(ubigeoAnt.getDeProvincia());
                padronMovimiento.setDeDistritoAnt(ubigeoAnt.getDeProvincia());
                padronMovimiento.setDeEntidadAnt(entidadAnt.getDeEntidad());
                if(centroPobladoAnt != null)
                    padronMovimiento.setDeCentroPobladoAnt(centroPobladoAnt.getNoCentroPoblado());
                else
                    padronMovimiento.setDeCentroPobladoAnt("");

                padronMovimiento.setDeDepartamentoAct(ubigeoAct.getDeDepartamento());
                padronMovimiento.setDeProvinciaAct(ubigeoAct.getDeProvincia());
                padronMovimiento.setDeDistritoAct(ubigeoAct.getDeDistrito());
                padronMovimiento.setDeEntidadAct(entidadAct.getDeEntidad());
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
    public List<PadronMovimiento> listadoPadronMovimiento(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin) {
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
                "                       to_char(m.fe_crea_audi, 'dd/MM/yyyy')     as feCreaAudi, " +
                "                       to_char(m.fe_modi_audi, 'dd/MM/yyyy')     as feModiAudi, " +
                "                       m.us_crea_audi as usCreaAudi, " +
                "                       m.us_modi_audi as usModiAudi, " +
                "                       m.co_centro_poblado_ant as coCentroPobladoAnt, " +
                "                       m.co_centro_poblado_act as coCentroPobladoAct " +
                "                  from pntv_padron_movimiento m " +
                "                  left join pntv_padron_nominal p on p.co_padron_nominal = " +
                "                                                     m.co_padron_nominal " +
                "                  where p.es_padron='1' and trunc(m.fe_crea_audi) between ? and ? and  m.co_ubigeo_inei_ant like ?||'%' " + //todo
                "                 order by p.fe_nac_menor desc, m.co_ubigeo_inei_act, m.nu_sec_act" +
                ") t " +
                "where rownum<=?)  " +
                "where fila>=?";

        List<PadronMovimiento> movimientos = new ArrayList<PadronMovimiento>();
        Object[] params = new Object[]{feIni, feFin, coUbigeo, filaFin, filaIni};
        try{
            logger.debug(String.format("DAO '%s' con: '%s' por ejecutar", query, ArrayUtils.toString(params)));
            List<PadronMovimiento> padronMovimientos = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(PadronMovimiento.class), params);
            for(PadronMovimiento padronMovimiento: padronMovimientos){
                Ubigeo ubigeoAnt = ubigeoDao.obtenerPorCodigoInei(padronMovimiento.getCoUbigeoIneiAnt());

                /*logger.info("padronMovimiento.getCoUbigeoIneiAnt()" + padronMovimiento.getCoUbigeoIneiAnt());
                logger.info("ubigeoAnt:"+ubigeoAnt);*/

                //solo se considera que las entidades de tipo municipio registran
                Entidad entidadAnt = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAnt(), Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());

                Ubigeo ubigeoAct = ubigeoDao.obtenerPorCodigoInei(padronMovimiento.getCoUbigeoIneiAct());

                //solo se considera que las entidades de tipo municipio registran
                Entidad entidadAct = usuarioExternoDao.getEntidad(padronMovimiento.getCoEntidadAct(), Entidad.TipoEntidad.MUNICIPIO.getCoTipoEntidad());

                CentroPoblado centroPobladoAnt = ubigeoDao.getCentroPoblado(padronMovimiento.getCoCentroPobladoAnt());

                CentroPoblado centroPobladoAct = ubigeoDao.getCentroPoblado(padronMovimiento.getCoCentroPobladoAct());

                padronMovimiento.setDeDepartamentoAnt(ubigeoAnt.getDeDepartamento());
                padronMovimiento.setDeProvinciaAnt(ubigeoAnt.getDeProvincia());
                padronMovimiento.setDeDistritoAnt(ubigeoAnt.getDeDistrito());
                padronMovimiento.setDeEntidadAnt(entidadAnt.getDeEntidad());
                if(centroPobladoAnt != null)
                    padronMovimiento.setDeCentroPobladoAnt(centroPobladoAnt.getNoCentroPoblado());
                else
                    padronMovimiento.setDeCentroPobladoAnt("");

                padronMovimiento.setDeDepartamentoAct(ubigeoAct.getDeDepartamento());
                padronMovimiento.setDeProvinciaAct(ubigeoAct.getDeProvincia());
                padronMovimiento.setDeDistritoAct(ubigeoAct.getDeDistrito());
                padronMovimiento.setDeEntidadAct(entidadAct.getDeEntidad());
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
    public int countPadronMovimientos(String coUbigeo, String feIni, String feFin) {
        String query = "select count(1) from pntv_padron_movimiento t1 left join pntv_padron_nominal t2 on t1.co_padron_nominal=t2.co_padron_nominal " +
                "where trunc(fe_crea_audi) between ? and ?  and t2.es_padron='1' and t1.co_ubigeo_inei_ant like ?||'%' ";
        Object[] params = new Object[]{feIni, feFin, coUbigeo};
//        log.debug(String.format("DAO '%s' con '%s' por ejecutar", query, ArrayUtils.toString(params)));
        return jdbcTemplate.queryForInt(query, params);
    }

    @Override
    public int countAllPadronMovimientos() {
        String query = "select count(1) from pntv_padron_movimiento";
        return jdbcTemplate.queryForInt(query);
    }

    private static String SQL_CANTIDAD_UBIGEO_PROC_SOCIAL = " select co_ubigeo_inei, de_departamento, de_provincia, de_distrito, co_consolidado, es_consolidado_ubigeo, nu_pvl, nu_cuna, nu_juntos, nu_qaliwarma from pntv_consolidado_ubigeo where es_consolidado_ubigeo='1' order by co_ubigeo_inei ";

    private static String SQL_CANTIDAD_UBIGEO_EDAD = " select co_ubigeo_inei, de_departamento, de_provincia, de_distrito, co_consolidado, es_consolidado_ubigeo, nu_edad_0, nu_edad_1, nu_edad_2, nu_edad_3, nu_edad_4, nu_edad_5 from pntv_consolidado_ubigeo  where es_consolidado_ubigeo='1' order by co_ubigeo_inei";

    private static String SQL_CANTIDAD_UBIGEO_TI_SEGURO = " select co_ubigeo_inei, de_departamento, de_provincia, de_distrito, co_consolidado, es_consolidado_ubigeo, nu_sis, nu_essalud, nu_sanidad, nu_privado, nu_ninguno from pntv_consolidado_ubigeo  where es_consolidado_ubigeo='1' order by co_ubigeo_inei";

    @Override
    public List<Ubigeo> getCantidadUbigeoProSocialDepa() {
        String sql = "select t1.de_departamento, " +
                "sum(NU_PVL) as NU_PVL, " +
                "sum(NU_CUNA) as NU_CUNA, " +
                "sum(NU_JUNTOS) as NU_JUNTOS, " +
                "sum(NU_QALIWARMA) as NU_QALIWARMA " +
                "from (" +
                SQL_CANTIDAD_UBIGEO_PROC_SOCIAL +
                ") t1 " +
                "group by t1.de_departamento " +
                "order by t1.de_departamento";
        try{
            logger.debug(String.format("DAO '%s'", sql));
            return jdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(Ubigeo.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoEdadDepa() {
        String sql = "select t1.de_departamento,  " +
                "sum(nu_edad_0) as nu_edad_0, " +
                "sum(nu_edad_1) as nu_edad_1, " +
                "sum(nu_edad_2) as nu_edad_2, " +
                "sum(nu_edad_3) as nu_edad_3, " +
                "sum(nu_edad_4) as nu_edad_4, " +
                "sum(nu_edad_5) as nu_edad_5 " +
                "from (" +
                SQL_CANTIDAD_UBIGEO_EDAD +
                ") t1 " +
                "group by t1.de_departamento " +
                "order by t1.de_departamento";

        try{
            logger.debug(String.format("DAO '%s'", sql));
            return jdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(Ubigeo.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoTiSeguroDepa() {
        String sql = "select t1.de_departamento, " +
                "sum(NU_SIS) as NU_SIS, " +
                "sum(NU_ESSALUD) as NU_ESSALUD, " +
                "sum(NU_SANIDAD) as NU_SANIDAD, " +
                "sum(NU_PRIVADO) as NU_PRIVADO, " +
                "sum(NU_NINGUNO) as NU_NINGUNO " +
                "from (" +
                SQL_CANTIDAD_UBIGEO_TI_SEGURO +
                ") t1 " +
                "group by t1.de_departamento " +
                "order by t1.de_departamento ";
        try{
            logger.debug(String.format("DAO '%s'", sql));
            return jdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(Ubigeo.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoProSocial(){
        String sql = SQL_CANTIDAD_UBIGEO_PROC_SOCIAL;
        try{
            logger.debug(String.format("DAO '%s'", sql));
            return jdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(Ubigeo.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoEdad() {
        String sql = SQL_CANTIDAD_UBIGEO_EDAD;
        try{
            logger.debug(String.format("DAO '%s'", sql));
            return jdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(Ubigeo.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Ubigeo> getCantidadUbigeoTiSeguro() {
        String sql =  SQL_CANTIDAD_UBIGEO_TI_SEGURO;
        try{
            logger.debug(String.format("DAO '%s'", sql));
            return jdbcTemplate.query(sql, ParameterizedBeanPropertyRowMapper.newInstance(Ubigeo.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

}
