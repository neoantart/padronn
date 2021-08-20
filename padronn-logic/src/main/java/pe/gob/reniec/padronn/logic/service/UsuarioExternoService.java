package pe.gob.reniec.padronn.logic.service;

import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;

import java.util.List;
import java.util.Map;

import java.util.List;

/**
 * Clase UsuarioExternoService.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 27/05/13 10:12 AM
 */
public interface UsuarioExternoService {

	Usuario getUsuario(String login);

	Usuario getUsuario(String login, String password);

    boolean insert(UsuarioExterno usuarioExterno);

    boolean update(UsuarioExterno usuarioExterno);

    List<UsuarioExterno> getAll();

    List<UsuarioExterno> getAll(int filaIni, int filaFin);

    UsuarioExterno getUsuarioExterno(String coUsuario);

    List<Objeto> getEntidades();

    List<Objeto> getGrupos();

    List<Objeto> getTipoEntidades();

    List<Entidad> getUsuarioEntidad(String coUsuario);

    Usuario getUsuario(String coUsuario, String dePassword, String coEntidad);

    void updateUsuarioPassword(CambioCredencial cambioCredencial);

    int countAll();

    List<Entidad> buscarEntidad(String parteNoEntidad);

    List<Entidad> listarEntidad(String parteNoEntidad, List<String> tipoEntidades);

    Grupo getGrupo(String coGrupo);

    Entidad getEntidad(String coEntidad, String coTipoEntidad);

	List<String> getAuthorities(String coUsuario);

    String getDeEntidades(List<String> coEntidadList, List<String> tiposEntidad);

    String getDeGrupos(List<String> coGrupoList);

    boolean setFeLastLogin(String coUsuario);

    List<UsuarioExterno> buscarUsuarioExterno(String apPrimer, String apSegundo, String preNombres);

    UsuarioExterno getUsuarioAni(String dni);

    boolean isDniMayor(String nuDni);

    boolean isDNIMadre(String nuDni);

    boolean resetPassword(String nuDni, String usModiPw);


    List<UsuarioExterno> getAllDetails();

    boolean guardarAccesoUsuario(Usuario usuario);

    Objeto verificarUsuario(String coUsuario);

    boolean verificarDniNombres(SignupForm signupForm);
}
