package pe.gob.reniec.padronn.logic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.gob.reniec.padronn.logic.dao.AccesoUsuarioDao;
import pe.gob.reniec.padronn.logic.dao.UsuarioExternoDao;
import pe.gob.reniec.padronn.logic.model.*;
import pe.gob.reniec.padronn.logic.model.form.SignupForm;
import pe.gob.reniec.padronn.logic.service.AbstractBaseService;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;

import javax.swing.text.StyledEditorKit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.oro.text.regex.Perl5Compiler.quotemeta;


@Service
public class UsuarioExternoServiceImpl
		extends AbstractBaseService
		implements UsuarioExternoService {

	@Autowired
	UsuarioExternoDao usuarioExternoDao;

    @Autowired
    AccesoUsuarioDao accesoUsuarioDao;

	@Override
	public Usuario getUsuario(String login) {
		try {
			return usuarioExternoDao.getUsuario(login);
		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			return null;
		}
	}

	@Override
	public Usuario getUsuario(String login, String password) {
		try {
			return usuarioExternoDao.getUsuario(login, password);
		} catch (DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			return null;
		}
	}

  @Override
  public List<Entidad> getUsuarioEntidad(String coUsuario) {
    try {
      return usuarioExternoDao.listUsuarioEntidad(coUsuario);
    } catch (DataAccessException dae) {
      log.error(dae.getMessage());
      dae.printStackTrace();
      return null;
    }
  }

  @Override
  public Usuario getUsuario(String coUsuario, String dePassword, String coEntidad) {
      try {
          String[] partes = coEntidad.split("_");
          coEntidad = partes[0];
          String coTipoEntidad = partes[1];
          return usuarioExternoDao.getUsuario(coUsuario, dePassword, coEntidad, coTipoEntidad);
      } catch (DataAccessException dae) {
          log.error(dae.getMessage());
          dae.printStackTrace();
          return null;
      } catch (Exception ex) {
          ex.printStackTrace();
          return null;
      }
  }

  @Override
  public void updateUsuarioPassword(CambioCredencial cambioCredencial) {
    try {
      usuarioExternoDao.updateUsuarioPassword(cambioCredencial);
    } catch (DataAccessException dae) {
      log.error(dae.getMessage());
      //dae.printStackTrace();
      throw dae;
    }
  }

    @Override
    public int countAll() {
        return usuarioExternoDao.countAll();
    }

    @Override
    public List<Entidad> buscarEntidad(String parteNoEntidad) {
        /*return usuarioExternoDao.listarEntidades(parteNoEntidad, tipoEntidad);*/
        return usuarioExternoDao.buscarEntidad(parteNoEntidad);
    }

    @Override
    public List<Entidad> listarEntidad(String parteNoEntidad, List<String> tipoEntidades) {
	    return usuarioExternoDao.listarEntidades(parteNoEntidad, tipoEntidades);
    }

    @Override
    public Grupo getGrupo(String coGrupo) {
        return usuarioExternoDao.getGrupo(coGrupo);
    }

    @Override
    public Entidad getEntidad(String coEntidad, String coTipoEntidad) {
        return usuarioExternoDao.getEntidad(coEntidad, coTipoEntidad);
    }

    @Override
    public String getDeEntidades(List<String> coEntidadList, List<String> tiposEntidad) {
        return usuarioExternoDao.getDeEntidades(coEntidadList, tiposEntidad);
    }

    @Override
    public String getDeGrupos(List<String> coGrupoList) {
        return usuarioExternoDao.getDeGrupos(coGrupoList);
    }

    @Override
    public boolean insert(UsuarioExterno usuarioExterno) {
        try{
            //TODO: insertar historica

            return usuarioExternoDao.insert(usuarioExterno);
        } catch (DataAccessException dae){
            log.error(dae.getMessage());
            dae.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(UsuarioExterno usuarioExterno) {
        try{
            return usuarioExternoDao.update(usuarioExterno);
        } catch(DataAccessException dae) {
            log.error(dae.getMessage());
            dae.printStackTrace();
            return false;
        }
    }


    @Override
    public List<UsuarioExterno> getAll() {
        try{
            return usuarioExternoDao.getAll();
        } catch(DataAccessException dae) {
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UsuarioExterno> getAll(int filaIni, int filaFin) {
        try{
            return usuarioExternoDao.getAll(filaIni, filaFin);
        } catch(DataAccessException dae) {
            log.error(dae.getMessage());
            dae.printStackTrace();
            return null;
        }
    }

    @Override
    public UsuarioExterno getUsuarioExterno(String coUsuario) {
        try {
            return usuarioExternoDao.getUsuarioExterno(coUsuario);
        } catch (Exception e) {
            return new UsuarioExterno();
        }
    }

    @Override
    public List<Objeto> getEntidades() {
	    return usuarioExternoDao.getEntidades();
    }

    @Override
    public List<Objeto> getGrupos() {
        return usuarioExternoDao.getGrupos();
    }

    @Override
    public List<Objeto> getTipoEntidades() {
        return usuarioExternoDao.getTipoEntidades();
    }

	@Override
	public List<String> getAuthorities(String coUsuario) {
		try	{
			List<String> res = usuarioExternoDao.getAuthorities(coUsuario);
            res.add("/recommendations.do");
            return res;
		} catch(DataAccessException dae) {
			log.error(dae.getMessage());
			dae.printStackTrace();
			//return null;
			return new ArrayList<String>();
		}
	}

    @Override
    public boolean setFeLastLogin(String coUsuario){
        return usuarioExternoDao.setFeLastLogin(coUsuario);
    }

    @Override
    public List<UsuarioExterno> buscarUsuarioExterno(String apPrimer, String apSegundo, String preNombres) {
        return usuarioExternoDao.buscarUsuarioExterno(apPrimer, apSegundo, preNombres, 1, padronProperties.PAGINA_FILAS);
    }

    @Override
    public UsuarioExterno getUsuarioAni(String dni) {
        return usuarioExternoDao.getUsuarioAni(dni);
    }

    @Override
    public boolean isDniMayor(String nuDni){
        return usuarioExternoDao.isDniMayor(nuDni);
    }

    @Override
    public boolean isDNIMadre(String nuDni) {
        return usuarioExternoDao.isDNIMadre(nuDni);
    }

    @Override
    public boolean resetPassword(String nuDni, String usModiPw) {
        return usuarioExternoDao.resetPassword(nuDni, usModiPw);
    }

    @Override
    public List<UsuarioExterno> getAllDetails() {
        try {
            return usuarioExternoDao.getAllDetails();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean guardarAccesoUsuario(Usuario usuario) {
        return accesoUsuarioDao.insert(usuario);
    }

    @Override
    public Objeto verificarUsuario(String coUsuario) {
        Objeto result = new Objeto();
	    Integer edadUsuario = usuarioExternoDao.getEdad(coUsuario);
        boolean checkUsuario = usuarioExternoDao.verificarUsuario(coUsuario);
        boolean estadoUsuarioInAni = usuarioExternoDao.verificarEstadoUsuarioInAni(coUsuario);

        if(checkUsuario) {
            result.setText("El usuario con DNI N° <strong>"+ coUsuario +"</strong> ya se encuentra registrado.");
            return result;
        }
        if(edadUsuario<18) {
            result.setText("El DNI corresponde a un menor de edad.");
            return result;
        }
        if(estadoUsuarioInAni) {
            result.setText("El DNI N° <strong>"+ coUsuario+"</strong> corresponde a una persona con restricciones.");
            return result;
        }
        return null;

    }

    @Override
    public boolean verificarDniNombres(SignupForm usuarioEnviado) {

        SignupForm usuarioEncontrado = usuarioExternoDao.getUsuarioDni(usuarioEnviado.getCoUsuario());

        if(usuarioEncontrado ==null){
            return false;
        }
        if( usuarioEnviado.getPrenombres().trim().equals(usuarioEncontrado.getPrenombres().trim()) ||
            usuarioEnviado.getApPrimer().trim().equals(usuarioEncontrado.getApPrimer().trim())||
            usuarioEnviado.getApSegundo().trim().equals(usuarioEncontrado.getApSegundo().trim()) ) {
            return true;
        }
        return false;
    }


}
