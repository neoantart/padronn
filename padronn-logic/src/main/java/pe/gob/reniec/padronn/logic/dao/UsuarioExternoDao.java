package pe.gob.reniec.padronn.logic.dao;

import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;

import java.util.List;

/**
 * Archivo UsuarioExternoDao.
 *
 * @author lmamani[at]reniec.gob.pe
 *         lmiguelmh[at]gmail[dot]com
 * @version 1.0.0
 * @since 27/05/13 09:48 AM
 */
public interface UsuarioExternoDao {

	Usuario getUsuario(String login);

	Usuario getUsuario(String login, String password);

    boolean insert(UsuarioExterno usuarioExterno);

    boolean update(UsuarioExterno usuarioExterno);

    List<UsuarioExterno> getAll();

    List<UsuarioExterno> getAll(int filaIni, int filaFin);

    List<String> getPermisoModulo();

    int getGrupoUsuario(String coUsuario);

    UsuarioExterno getUsuarioExterno(String coUsuario);

    boolean insertUsuarioGrupo(String coGrupo, String coUsuario);

    boolean insertUsuarioEntidad(String coEntidad, String coUsuario, String coTipoEntidad);

    List<String> getEntidadesUsuario(String coUsuario);

    List<String> getGruposUsuario(String coUsuario);

    List<Objeto> getEntidades();

    List<Objeto> getGrupos();

    List<Entidad> listUsuarioEntidad(String coUsuario);

    //List<Entidad> getUsuarioEntidad(String coUsuario);

    Usuario getUsuario(String coUsuario, String dePassword, String coEntidad, String coTipoEntidad);

    void updateUsuarioPassword(CambioCredencial cambioCredencial);

    List<String> getAuthorities(String coUsuario);

    int countAll();

    List<Entidad> buscarEntidad(String parteNoEntidad);

    Grupo getGrupo(String coGrupo);

    Entidad getEntidad(String coEntidad, String coTipoEntidad);

    String getDeEntidades(List<String> coEntidadList, List<String> tiposEntidad);

    List<String> getCoTipoEntidades(List<String> coTipoEntidades);

    String getDeGrupos(List<String> coGrupoList);

    boolean setFeLastLogin(String coUsuario);

    List<UsuarioExterno> buscarUsuarioExterno(String apPrimer, String apSegundo, String preNombres, int filaInicio, int filaFinal);

    UsuarioExterno getUsuarioAni(String dni);

    boolean isDniMayor(String nuDni);

    boolean isDNIMadre(String nuDni);

    boolean resetPassword(String nuDni, String usModiPw);

    List<UsuarioExterno> getAllDetails();

    List<Entidad> listarEntidades(String parteNoEntidad, List<String> tipoEntidad);

    List<Objeto> getTipoEntidades();

    List<String> getTiposEntidad(String coUsuario);

    Integer getEdad(String coUsuario);

    boolean verificarUsuario(String coUsuario);

    boolean verificarEstadoUsuarioInAni(String coUsuario);

    SignupForm getUsuarioDni(String coUsuario);
}
