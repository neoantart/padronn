package pe.gob.reniec.padronn.logic.model;


import pe.gob.reniec.commons.baseApplication.model.ApplicationUser;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vflores
 * Date: 22/01/13
 * Time: 06:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Usuario implements java.io.Serializable, ApplicationUser {
	String paterno;
	String materno;
	String nombres;
	String login;
	String password;
	long timeCreation;
	boolean rememberSession = false;
	String coEntidad;
	String deEntidad;
	String esUsuario;
	String esExpire;
	String coUbigeoInei;
	String fePassword;
    String coGrupo;
    String deGrupo;
	boolean askNewPasword;
	boolean askForExpPass;
	List<String> authorities;
	List<String> grupos;
    //Extras
    String coTipoEntidad;
    String coAccesoUsuario;
    String ipMaquina;
    String feAcceso;
    String coTipoAcceso;
	boolean entidadSelected;
	boolean reload;
	int entries;
	String esMinsa;
	String captchaText;

	public String getEsMinsa() {
		return esMinsa;
	}

	public void setEsMinsa(String esMinsa) {
		this.esMinsa = esMinsa;
	}

	public int getEntries() {
        return entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }

    public boolean isReload() {
		return reload;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}

	public boolean getEntidadSelected() {
		return entidadSelected;
	}

	public void setEntidadSelected(boolean entidadSelected) {
		this.entidadSelected = entidadSelected;
	}

	public Usuario() {
        List<String> tmp = new ArrayList<String>();
        tmp.add("/recommendations");
        authorities = tmp;
		timeCreation = System.currentTimeMillis();
		askNewPasword = false;
		askForExpPass = false;
		entries=0;
	}

	public String getCoUbigeoInei() {
		return coUbigeoInei;
	}

	public void setCoUbigeoInei(String coUbigeoInei) {
		this.coUbigeoInei = coUbigeoInei;
	}

	public String getEsUsuario() {
		return esUsuario;
	}

	public void setEsUsuario(String esUsuario) {
		this.esUsuario = esUsuario;
	}

	public String getEsExpire() { return esExpire;	}

	public void setEsExpire(String esExpire) { this.esExpire = esExpire;}
	public long getTimeCreation() {
		return timeCreation;
	}

	public void setTimeCreation(long timeCreation) {
		this.timeCreation = timeCreation;
	}

	public String getCoEntidad() {
		return coEntidad;
	}

	public void setCoEntidad(String coEntidad) {
		this.coEntidad = coEntidad;
	}

	public String getDeEntidad() {
		return deEntidad;
	}

	public void setDeEntidad(String deEntidad) {
		this.deEntidad = deEntidad;
	}

	public String getCaptchaText() {
		return captchaText;
	}

	public void setCaptchaText(String captchaText) {
		this.captchaText = captchaText;
	}

	/*
	@Override
	public String toString() {
		Gson gson = new Gson();
		String json= gson.toJson(this);
		return json;
	}
	*/

	@Override
	public boolean isValidForSession() {
		return this.login != null && !this.login.isEmpty();
		/*return paterno!=null&&materno!=null&&nombres!=null&&login!=null&&
				!paterno.isEmpty()&&!materno.isEmpty()&&!nombres.isEmpty()&&!login.isEmpty();*/
	}

	@Override
	public void invalidateSession() {
		this.paterno = "";
		this.materno = "";
		this.nombres = "";
		this.login = "";
		this.rememberSession = false;
		this.entidadSelected=false;
		this.reload=false;
		//if(this.authorities != null) {
        this.authorities.clear();
		//}
        this.coEntidad = "";
        this.deEntidad = "";
        this.coUbigeoInei = "";
        this.fePassword = "";
        this.coTipoEntidad = "";
		this.grupos = null;
		this.entries = 0;
	}

	@Override
	public void authenticate(HttpServletRequest httpServletRequest) {
		/*
		Puede retornar mediante una excepción:
		- Usuario en blanco
		- Contraseña en blanco
		- Usuario o contraseña incorrecta
		- Error desconocido (Conexión de red, error de base de datos, etc)
		 */
	}

	@Override
	public String getUserName() {
		return this.login;
	}

	@Override
	public String getFullName() {
		return (paterno != null ? paterno + " " : "") + (materno != null ? materno + " " : "") + (nombres != null ? nombres : "");
		//return this.login;
	}


	@Override
	public String getInvalidSessionDispatcher() {
		return "/loginform.do";
	}

	@Override
	public void setValues(ApplicationUser applicationUser) {
		Usuario tmp = (Usuario) applicationUser;
		this.rememberSession = tmp.isRememberSession();
		this.login = tmp.getLogin();
		this.nombres = tmp.getNombres();
		this.materno = tmp.getMaterno();
		this.paterno = tmp.getPaterno();
		this.coEntidad = tmp.getCoEntidad();
		this.deEntidad = tmp.getDeEntidad();
		this.coUbigeoInei = tmp.getCoUbigeoInei();
		this.password = tmp.getPassword();
		this.fePassword = tmp.getFePassword();
		this.askNewPasword = tmp.isAskNewPasword();
		this.askForExpPass = tmp.isAskForExpPass();
		this.authorities = tmp.getAuthorities();
        this.coTipoEntidad = tmp.getCoTipoEntidad();
        this.ipMaquina = tmp.getIpMaquina();
		this.coGrupo = tmp.getCoGrupo();
		this.grupos = tmp.getGrupos();
		this.entidadSelected=tmp.getEntidadSelected();
		this.reload = tmp.isReload();
		this.entries = tmp.getEntries();
		this.esMinsa = tmp.getEsMinsa();

	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

//	@Override
	public List<String> getAuthorities() {
        return authorities;
	 /*
    return new ArrayList<String>(){{
      add("/precotejo");
      add("/registromanual");
      add("/busquedademenor");
      add("/actas");
      add("/dominio");
      add("/mantenimiento");
      add("/signup");
      add("/busquedademenor");
      add("/consulta");
      add("/padronimg");
      add("/reportegrafico");
    }};
    */
	}

	public boolean isRememberSession() {
		return rememberSession;
	}

	public boolean isSelectedEntity(){ return entidadSelected; }

	public boolean isReloadedPage(){ return reload; }

	public void setRememberSession(boolean rememberSession) {
		this.rememberSession = rememberSession;
	}

	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}

	public void setMaterno(String materno) {
		this.materno = materno;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPaterno() {
		return paterno;
	}

	public String getMaterno() {
		return materno;
	}

	public String getNombres() {
		return nombres;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getCoDepartamento() {
		if (coUbigeoInei != null && coUbigeoInei.length() > 2)
			return coUbigeoInei.substring(0, 2);
		return "";
	}

	public boolean isAskNewPasword() {
		return askNewPasword;
	}

	public boolean isAskForExpPass() {
		return askForExpPass;
	}

	public void setAskNewPasword(boolean askNewPasword) {
		this.askNewPasword = askNewPasword;
	}

	public void setAskForExpPass(boolean askForExpPass) {
		this.askForExpPass = askForExpPass;
	}

	public String getFePassword() {
		return fePassword;
	}

	public void setFePassword(String fePassword) {
		this.fePassword = fePassword;
	}

    public String getCoTipoEntidad() {
        return coTipoEntidad;
    }

    public void setCoTipoEntidad(String coTipoEntidad) {
        this.coTipoEntidad = coTipoEntidad;
    }

    public String getCoGrupo() {
        return coGrupo;
    }

    public void setCoGrupo(String coGrupo) {
        this.coGrupo = coGrupo;
    }

    public String getDeGrupo() {
        return deGrupo;
    }

    public void setDeGrupo(String deGrupo) {
        this.deGrupo = deGrupo;
    }

    public String getCoAccesoUsuario() {
        return coAccesoUsuario;
    }

    public void setCoAccesoUsuario(String coAccesoUsuario) {
        this.coAccesoUsuario = coAccesoUsuario;
    }

    public String getIpMaquina() {
        return ipMaquina;
    }

    public void setIpMaquina(String ipMaquina) {
        this.ipMaquina = ipMaquina;
    }

    public String getFeAcceso() {
        return feAcceso;
    }

    public void setFeAcceso(String feAcceso) {
        this.feAcceso = feAcceso;
    }

    public String getCoTipoAcceso() {
        return coTipoAcceso;
    }

    public void setCoTipoAcceso(String coTipoAcceso) {
        this.coTipoAcceso = coTipoAcceso;
    }

	public List<String> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<String> grupos) {
		this.grupos = grupos;
	}

	@Override
    public String toString() {
        return "Usuario{" +
                "paterno='" + paterno + '\'' +
                ", materno='" + materno + '\'' +
                ", nombres='" + nombres + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", timeCreation=" + timeCreation +
                ", rememberSession=" + rememberSession +
                ", coEntidad='" + coEntidad + '\'' +
                ", deEntidad='" + deEntidad + '\'' +
                ", esUsuario='" + esUsuario + '\'' +
                ", coUbigeoInei='" + coUbigeoInei + '\'' +
                ", fePassword='" + fePassword + '\'' +
                ", askNewPasword=" + askNewPasword +
				", askForExpPass=" + askForExpPass +
                ", authorities=" + authorities +
                ", coTipoEntidad='" + coTipoEntidad + '\'' +
				", reload='" + reload + '\'' +
				", entidadSelected='" + entidadSelected + '\'' +
                ", entries='" + entries + '\'' +
                '}';
    }
}
