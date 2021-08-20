package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.ReporteEuropan;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 23/08/13
 * Time: 08:59 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ReporteService {
    List<PadronNominal> listarPadronesActivos(String coUbigeoInei, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronesActivos(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);

    List<PadronNominal> listarPadronesObservados(String coUbigeoInei, String feIni, String feFin, String tiRegFecha);

    int countListaPadronesActivos(String coUbigeoInei, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronesInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegFecha);
    List<PadronNominal> listarPadronesInactivos(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);
    List<PadronNominal> listarPadronesObservados(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);
    int countListaPadronesInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegFecha);
    int countListaPadronesObservados(String coUbigeoInei, String feIni, String feFin, String tiRegFecha);

    /*List<PadronNominal> listarPadronesByEntidadActivos(String coUbigeoInei, String tiRegistro);
    List<PadronNominal> listarPadronesByEntidadActivos(String coUbigeoInei, String tiRegistro, int filaIni, int filaFin);
    int countListarPadronesByEntidadActivos(String coUbigeoInei, String tiRegistro);


    List<PadronNominal> listarPadronesByEntidadInactivos(String coUbigeoInei, String tiRegistro);
    List<PadronNominal> listarPadronesByEntidadInactivos(String coUbigeoInei, String tiRegistro, int filaIni, int filaFin);
    int countListarPadronesByEntidadInactivos(String coUbigeoInei, String tiRegistro);*/

    List<PadronNominal> listarPadronesByEstablecimientoSaludActivos(String coEstSalud, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronesByEstablecimientoSaludObservados(String coEstSalud, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronesByEstablecimientoSaludOBservados(String coEstSalud, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);

    List<PadronNominal> listarPadronesByEstablecimientoSaludActivos(String coEstSalud, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);

    int countListarPadronesByEstablecimientoSaludObservados(String coEstSalud, String feIni, String feFin, String tiRegFecha);

    int countListarPadronesByEstablecimientoSaludActivos(String coEstSalud, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronesByEstablecimientoSaludInactivos(String coEstSalud, String feIni, String feFin, String tiRegFecha);
    List<PadronNominal> listarPadronesByEstablecimientoSaludInactivos(String coEstSalud, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);
    int countListarPadronesByEstablecimientoSaludInactivos(String coEstSalud, String feIni, String feFin, String tiRegFecha);


    List<PadronNominal> listarPadronesByProgramaSocialActivos(String coProgramaSocial, String feIni, String feFin,String coEntidad, String tiRegFecha);
    List<PadronNominal> listarPadronesByProgramaSocialActivos(String coProgramaSocial, String feIni, String feFin,String coEntidad, int filaIni, int filaFin, String tiRegFecha);
    int countListarPadronesByProgramaSocialActivos(String coProgramaSocial, String feIni, String feFin,String coEntidad, String tiRegFecha);
    int countListarPadronesByProgramaSocialObservados(String coProgramaSocial,String feIni, String feFin,String coEntidad, String tiRegFecha);

    List<PadronNominal> listarPadronesByProgramaSocialInactivos(String coEstSalud, String feIni, String feFin,String coEntidad, String tiRegFecha);
    List<PadronNominal> listarPadronesByProgramaSocialInactivos(String coEstSalud, String feIni, String feFin,String coEntidad, int filaIni, int filaFin, String tiRegFecha);
    List<PadronNominal> listarPadronesByProgramaSocialObservados(String coProgramaSocial, String feIni, String feFin, String coEntidad, int filaIni, int filaFin, String tiRegFecha);
    List<PadronNominal> listarPadronesByProgramaSocialObservados(String coProgramaSocial, String feIni, String feFin, String coEntidad, String tiRegFecha);
    int countListarPadronesByProgramaSocialInactivos(String coEstSalud, String feIni, String feFin,String coEntidad, String tiRegFecha);

    List<Lugar> getUbigeo(String deUbigeo);

    Lugar getLugar(String coUbigeo);

    List<Lugar> buscarEntidad(String deEntidad);

    Lugar getEntidad(String coEntidad);

    Entidad getEntidadUsuarios(String coEntidad);

    List<EstablecimientoSalud> buscarEstablecimientoSalud(String deEstSalud);

    List<EstablecimientoSalud> buscarEstablecimientoSaludRecienNacido(String deEstSalud, String coUbigeoInei);

    EstablecimientoSalud getEstablecimientoSalud(String coEstSalud);

    Map<String, Object> getProgramasSociales();

    Map<String, Object> getPrograma();

    List<Lugar> getCantidadesDepartamento();

    List<Lugar> getCantidadesProvincia(String coUbigeo);

    List<Lugar> getCantidadesDistrito(String coUbigeo);

    List<PadronNominal> listarPadronEdadActivo(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronEdadObservado(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronEdadTodos(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronEdadActivo(String coUbigeoInei, String edadAnios, String edadMeses, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronEdadObservado(String coUbigeoInei, String deEdad, String hastaEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronEdadTodos(String coUbigeoInei, String deEdad, String hastaEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha);

    Integer countListarPadronEdadActivo(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha);

    Integer countListarPadronEdadObservado(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha);

    Integer countListarPadronEdadTodos(String coUbigeoInei, String edadAnios, String edadMeses, String feIni, String feFin, String tiRegFecha);

    List<PadronMovimiento> listadoPadronMovimiento(String feIni, String feFin);

    List<PadronMovimiento> listadoPadronMovimiento(String feIni, String feFin, int filaIni, int filaFin);

    Integer countAllPadronMovimiento();

    Integer countPadronMovimientos(String feIni, String feFin);

    List<Ubigeo> listadoPadronesPorUbigeo();

    List<PadronObservado> listadoObservaciones(String feIni, String feFin);

    List<PadronObservado> listadoObservaciones(String feIni, String feFin, int filaIni, int filaFin);

    int countListadoObservaciones(String feIni, String feFin);

    List<PadronNominal> listadoPadronSinDoc(String coUbigeo, String feIni, String feFin, String tiRegFecha, String esPadron);

    List<PadronNominal> listadoPadronSinDoc(String coUbigeo, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha, String esPadron);

    int countListadoPadronSinDoc(String coUbigeo, String feIni, String feFin, String tiRegFecha, String esPadron);//

    List<PadronNominal> listarPadronesActivos(String coUbigeoInei, int filaIni, int filaFin);

    List<PadronNominal> listarPadronesActivos(String coUbigeoInei);

    int countListaPadronesActivos(String coUbigeoInei);

    List<PadronNominal> listarPadronesInactivos(String coUbigeoInei, int filaIni, int filaFin);

    int countListaPadronesInactivos(String coUbigeoInei);

    List<PadronNominal> listarPadronesInactivos(String coUbigeoInei);

    List<Ubigeo> consolidadoEuropan(ReporteEuropan reporteEuropan);

    List<PadronNominal> getPadronUbigeoEuropan(String coUbigeoInei, String feIni, String feFin, String deEdad, String hastaEdad, String tiRegFecha);

    List<PadronNominal> listarPadronesUbigeoActivos(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegistro, String tiRegFecha);
    List<PadronNominal> listarPadronesUbigeoObservados(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegistro, String tiRegFecha);

    int countListarPadronesUbigeoTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);
    List<PadronNominal> listarPadronesUbigeoTodos(String coUbigeoInei, String feIni, String feFin, int filaIni, int filaFin, String tiRegistro, String tiRegFecha);

    int countListarPadronesUbigeoActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);
    int countListarPadronesUbigeoObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesUbigeoActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesUbigeoObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesUbigeoTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesNacidosActivos(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);
    List<PadronNominal> listarPadronesNacidosObservados(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);
    List<PadronNominal> listarPadronesNacidosTodos(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, int filaIni, int filaFin, String tiRegFecha);

    List<PadronNominal> listarPadronesNacidosActivos(String coUbigeoInei, String coEstSalud ,String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha);
    List<PadronNominal> listarPadronesNacidosObservados(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha);
    List<PadronNominal> listarPadronesNacidosTodos(String coUbigeoInei, String coEstSalud , String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha);

    int countListarPadronesNacidosActivos(String coUbigeoInei, String coEstSalud, String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha);
    int countListarPadronesNacidosTodos(String coUbigeoInei,String coEstSalud,String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha);
    int countListarPadronesNacidosObservados(String coUbigeoInei,String coEstSalud,String feNacIni, String feNacFin, String feIni, String feFin, String tiRegFecha);

    List<PadronNominal> listarPadronesByEntidadInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesByEntidadInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, int filaIni, int filaFin, String tiRegFecha);

    List<PadronNominal> listarPadronesByEntidadTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesByEntidadTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, int filaIni, int filaFin, String tiRegFecha);

    int countListarPadronesByEntidadInactivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesByEntidadObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesByEntidadActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    List<PadronNominal> listarPadronesByEntidadObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, int filaIni, int filaFin, String tiRegFecha);

    List<PadronNominal> listarPadronesByEntidadActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, int filaIni, int filaFin, String tiRegFecha);


    int countListarPadronesByEntidadObservados(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    int countListarPadronesByEntidadActivos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    int countListarPadronesByEntidadTodos(String coUbigeoInei, String feIni, String feFin, String tiRegistro, String tiRegFecha);

    String getFechaGeneracionConsolidado();

    List<UsuarioExterno> buscarUsuarioPorDatos(String apPrimer, String apSegundo, String prenombres);

    List<UsuarioExterno> buscarUsuarioPorDni(String coUsuario);

    List<UsuarioExterno> buscaUsuarioPorEntidad(String coEntidad, String esUsuario);

    //Desde aqui se modifico por Jose Vidal Flores
    List<PadronEdadEESS> listarCantNiñosEdadEESS(String coUbigeoInei, int tiEstablecimiento, int filaIni, int filaFin, String esPadron);

    int countListarCantNiñosEdadEESS(String coUbigeoInei, int tiEstablecimiento, String esPadron);

    int countListarCantNiñosEdadEESSxFrecAten(String coUbigeoInei, int tiEstablecimiento, String coFrecuenciaAtencion, String esPadron);
    List<PadronEdadEESS> listarCantNiñosEdadEESSxFrecAten(String coUbigeoInei, int tiEstablecimiento, int filaIni, int filaFin, String coFrecuenciaAtencion, String esPadron);

    List<PadronNominal> listarNiñosEdadEESS(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, int filaIni, int filaFin, String feIni, String feFin, String tiRegFecha, String esPadron);
    int countListarNiñosEdadEESS(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, String feIni, String feFin, String tiRegFecha, String esPadron);

    int countListarNiñosEdadEESSxFrecAten(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, String coFrecuenciaAtencion, String feIni, String feFin, String tiRegFecha, String esPadron);
    List<PadronNominal> listarNiñosEdadEESSxFrecAten(String coUbigeoInei, String coEstSalud, int tiEstablecimiento, String nuEdad, int filaIni, int filaFin, String coFrecuenciaAtencion,String feIni, String feFin, String tiRegFecha, String esPadron);


}
