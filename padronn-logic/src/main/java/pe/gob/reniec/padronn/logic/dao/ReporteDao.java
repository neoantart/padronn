package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.ReporteEuropan;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 22/08/13
 * Time: 05:42 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ReporteDao {
    // Listado de activos
    /*List<PadronNominal> listarPadrones(String coUbigeoInei, boolean activos, String feIni, String feFin, String tiRegFecha, String... args);*/
    List<PadronNominal> listarPadrones(String coUbigeoInei, String esPadron, String feIni, String feFin, String tiRegFecha, String... args );

    List<PadronNominal> listarPadronesRangoFecha(String coUbigeoInei, String esPadron, String feIni, String feFin, String tiRegFecha, String... args);

    /*List<PadronNominal> listarPadrones(String coUbigeoInei, boolean activos, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String... args);*/
    List<PadronNominal> listarPadrones(String coUbigeoInei, String esPadron, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String... args);
    /*int countListaPadrones(String coUbigeoInei, boolean activos, String feIni, String feFin, String tiRegFecha, String... args);*/
    int countListaPadrones(String coUbigeoInei, String esPadron, String feIni, String feFin, String tiRegFecha, String... args);

    List<PadronNominal> listarPadronesNacidos(String coUbigeoInei, String coEstSalud , String esPadron,String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha, String... args);
    List<PadronNominal> listarPadronesNacidos(String coUbigeoInei, String coEstSalud , String esPadron, String feNacIni, String feNacFin, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String... args);
    int countListaPadronesNacidos(String coUbigeoInei, String coEstSalud , String esPadron,String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha, String... args);

    List<PadronNominal> listarPadronesByEntidad(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String esPadron, String tiRegFecha);
    List<PadronNominal> listarPadronesByEntidad(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String esPadron, int filaIni, int filaFin, String tiRegFecha);
    int countListarPadronesByEntidad(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String esPadron, String tiRegFecha);


    List<Lugar> getUbigeo(String deUbigeo);

    Lugar getLugar(String coUbigeo);

    List<Lugar> buscarEntidad(String deEntidad);

    Lugar getEntidad(String coEntidad);


    List<EstablecimientoSalud> buscarEstablecimientoSalud(String deEstSalud);

    List<EstablecimientoSalud> buscarEstablecimientoSaludRecienNacido(String deEstSalud, String coUbigeoInei);

    EstablecimientoSalud getEstablecimientoSalud(String coEstSalud, String nuSecuenciaLocal);

    List<PadronNominal> listarPadronesByEstablecimientoSalud(String coEstSalud, String feIni, String feFin, String esPadron, String tiRegFecha);
    List<PadronNominal> listarPadronesByEstablecimientoSalud(String coEstSalud, String feIni, String feFin, String esPadron, int filaIni, int filaFin, String tiRegFecha);
    int countListarPadronesByEstablecimientoSalud(String coEstSalud, String feIni, String feFin, String esPadron, String tiRegFecha);



    List<PadronNominal> listarPadronesByProgramaSocial(String coProgramaSocial, String feIni, String feFin, String coEntidad, String esPadron, String tiRegFecha);

    int countListaPadronesRangoFecha(String coUbigeoInei, String esPadron, String feIni, String feFin, String tiRegFecha, String... args);
    List<PadronNominal> listarPadronesRangoFecha(String coUbigeoInei, String esPadron, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String... args);

    List<PadronNominal> listarPadronesByProgramaSocial(String coProgramaSocial, String feIni, String feFin,String coEntidad, String esPadron, int filaIni, int filaFin, String tiRegFecha);//

    int countListarPadronesByProgramaSocial(String coProgramaSocial, String feIni, String feFin, String coEntidad, String esPadron, String tiRegFecha);
    List<ProgramaSocial> getProgramasSociales();

    List<Lugar> getCantidadesDepartamento();

    List<Lugar> getCantidadesPorEntidad(String coUbigeoInei);

    List<Lugar> getCantidadesProvincia(String coUbigeo);

    List<Lugar> getCantidadDistrito(String coUbigeo);

    List<PadronNominal> listarPadronEdad(String coUbigeoInei, String deEdad, String hastaEdad, String feIni, String feFin, String tiRegFecha, String esPadron);

    List<PadronNominal> listarPadronEdad(String coUbigeoInei, String deEdad, String hastaEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha, String esPadron);

    Integer countListarPadronEdad(String coUbigeoInei, String deEdad, String hastaEdad, String feIni, String feFin, String tiRegFecha, String esPadron);

    List<PadronMovimiento> listadoPadronMovimiento(String feIni, String feFin);

    List<PadronMovimiento> listadoPadronMovimiento(String feIni, String feFin, int filaIni, int filaFin);

    int countAllPadronMovimientos();

    List<Ubigeo> listadoPadronesPorUbigeo();

    int countPadronMovimientos(String feIni, String feFin);

    List<PadronObservado> listadoObservaciones(String feIni, String feFin);

    List<PadronObservado> listadoObservaciones(String feIni, String feFin, int filaIni, int filaFin);

    int countListadoObservaciones(String feIni, String feFin);

    List<PadronNominal> listadoPadronSinDoc(String coUbigeo, String feIni, String feFin, String tiRegFecha, String esPadron);//

    List<PadronNominal> listadoPadronSinDoc(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String esPadron);

    int countListadoPadronSinDoc(String coUbigeo, String feIni, String feFin, String tiRegFecha, String esPadron);//

    List<PadronNominal> getPadronUbigeoEuropan(String coUbigeoInei, String feIni, String feFin, String deEdad, String hastaEdad, String tiRegFecha);

    String getFechaGeneracionConsolidado();

    List<Ubigeo> consolidadoEuropan(ReporteEuropan reporteEuropan);

    List<UsuarioExterno> buscarUsuarioPorDatos(String apPrimer, String apSegundo, String prenombres, int filaIni, int filaFin);

    List<UsuarioExterno> buscarUsuarioPorDni(String coUsuario);

    List<UsuarioExterno> buscaUsuarioPorEntidad(String coEntidad, String esUsuario);

    //Desde aqui se modifico por Jose Vidal Flores
    int countListaCantNiñosEdadEESS(String coUbigeoInei, int tiEstablecimiento, String esPadron);
    List<PadronEdadEESS> listarCantNiñosEdadEESS(String coUbigeoInei, int filaIni, int filaFin, int tiEstablecimiento, String esPadron);

    int countListaCantNiñosEdadEESSxFrecAten(String coUbigeoInei, String coFrecuenciaAtencion, String esPadron);
    List<PadronEdadEESS> listarCantNiñosEdadEESSxFrecAten(String coUbigeoInei, int filaIni, int filaFin, String coFrecuenciaAtencion, String esPadron);

    int countListaNiñosEdadEESS(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, String feIni, String feFin, String tiRegFecha, String esPadron);//
    List<PadronNominal> listarNiñosEdadEESS(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha, String esPadron);//

    int countListaNiñosEdadEESSxFrecAten(String coUbigeoInei, String coEstSalud, String coFrecuenciaAtencion, String nuEdad, String feIni, String feFin, String tiRegFecha, String esPadron);//
    List<PadronNominal> listarNiñosEdadEESSxFrecAten(String coUbigeoInei, String coEstSalud, String nuEdad, int filaIni, int filaFin, String coFrecuenciaAtencion, String feIni, String feFin, String tiRegFecha, String esPadron);//

}