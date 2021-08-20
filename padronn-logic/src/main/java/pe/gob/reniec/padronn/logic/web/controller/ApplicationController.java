package pe.gob.reniec.padronn.logic.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jj.play.ns.nl.captcha.Captcha;
import jj.play.ns.nl.captcha.backgrounds.FlatColorBackgroundProducer;
import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.reniec.commons.baseApplication.model.ApplicationUser;
import pe.gob.reniec.commons.baseApplication.web.controller.ApplicationBase;
import pe.gob.reniec.padronn.logic.model.CambioCredencial;
import pe.gob.reniec.padronn.logic.model.Entidad;
import pe.gob.reniec.padronn.logic.model.RecaptchaResponse;
import pe.gob.reniec.padronn.logic.model.Usuario;
import pe.gob.reniec.padronn.logic.service.PadronObservadoService;
import pe.gob.reniec.padronn.logic.service.UbigeoService;
import pe.gob.reniec.padronn.logic.service.UsuarioExternoService;
import pe.gob.reniec.padronn.logic.dao.UsuarioExternoDao;
import pe.gob.reniec.padronn.logic.util.ImagenCiudadano;
//import pe.gob.reniec.security.authorization.User;
//import pe.gob.reniec.security.authorization.openamwrapper.OpenAMWrapper;
import pe.gob.reniec.padronn.logic.util.SimpleBase64;
import pe.gob.reniec.padronn.logic.web.EntidadesException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Dyn
 * Date: 24/01/13
 * Time: 4:44
 * To change this template use File | Settings | File Templates.
 */

@Controller("applicationController")
public class ApplicationController extends ApplicationBase {

    private static Logger LOG = Logger.getLogger(ApplicationController.class);
    private static final String CO_TIPO_ENTIDAD_MUNICIPIO = "1";
    private static String url = "https://www.google.com/recaptcha/api/siteverify";
    private static String privada = "6LdSjnwUAAAAAE6_jyEan4ilfxA_SWLX22fJiuXa";

    @Autowired
    @Qualifier("applicationProps")
    Properties properties;

    @Autowired
    protected Usuario usuario;

    @Autowired
    UsuarioExternoService usuarioExternoService;

    @Autowired
    UbigeoService ubigeoService;

    @Autowired
    UsuarioExternoDao usuarioExternoDao;

    @Autowired
    PadronObservadoService padronObservadoService;

    @Autowired
    private ImagenCiudadano imagenCiudadano;

    private static ObjectMapper mapper;

    private static ObjectMapper getInstance() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    public ModelAndView getMainWithoutSession() {
        ModelAndView mv = new ModelAndView();
        captcha(mv);
        mv.setViewName("application/main");
        mv.addObject("mainBody", "mainBody.jspx");
        mv.addObject("menu", "menuWithoutSession.jspx");
        /*mv.addObject("mainContent", "login.jspx");*/
        mv.addObject("mainContent", "login.jsp");
        mv.addObject("login", this.usuario.getLogin());
        return mv;
    }

    public ModelAndView getMainWithSession() {
        this.usuario.setEntries(this.usuario.getEntries()+1);
        if (this.usuario.getEntries()==1)
            this.usuario.setReload(true);
        else
            this.usuario.setReload(false);

        if (usuario.getEntidadSelected()) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("application/main");
            mv.addObject("mainBody", "mainBody.jspx");
            mv.addObject("menu", "menuWithSession.jspx");
            mv.addObject("img", imagenCiudadano.obtenerImagen(SimpleBase64.encodeBase64(usuario.getLogin())));
            if (this.usuario.getLogin() == null || this.usuario.getLogin().isEmpty()) {captcha(mv);}
            mv.addObject("mainContent", "homeAccount.jspx");
            mv.addObject("usuario", this.usuario);
            if (this.usuario != null && this.usuario.getCoTipoEntidad() != null && this.usuario.getCoTipoEntidad().equals(CO_TIPO_ENTIDAD_MUNICIPIO))
                mv.addObject("nuRegObservados", padronObservadoService.contarPadronObservado(this.usuario.getCoUbigeoInei()));
            mv.addObject("ubigeo", ubigeoService.obtenerPorCodigoInei(this.usuario.getCoUbigeoInei()));
            return mv;
        } else {
            String coUsuario = usuario.getLogin();
            List<Entidad> entidades = usuarioExternoService.getUsuarioEntidad(coUsuario);

            ModelAndView mv = new ModelAndView();
            mv.setViewName("application/main");
            mv.addObject("entidades", entidades);
            mv.addObject("mainBody", "mainBody.jspx");
            mv.addObject("usuario", this.usuario);
            mv.addObject("img", imagenCiudadano.obtenerImagen(SimpleBase64.encodeBase64(usuario.getLogin())));
            mv.addObject("mainContent", "entidades.jspx");
            mv.addObject("menu", "menuWithSessionOnlyEntidad.jspx");
            return mv;
        }
    }

    protected ModelAndView getHomeWithoutSession() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("application/mainBody");
        mv.addObject("usuario", this.usuario);
        mv.addObject("menu", "menuWithoutSession.jspx");
        /*mv.addObject("mainContent", "login.jspx");*/
        captcha(mv);
        mv.addObject("mainContent", "login.jsp");
        return mv;
    }


    protected ModelAndView getHomeWithSession() {
        ModelAndView mv;

        if (this.usuario.isAskNewPasword()) {
            mv = new ModelAndView();
            mv.setViewName("application/mainBody");
            mv.addObject("menu", "menuWithoutSession.jspx");
            mv.addObject("mainContent", "homeAccountCambioCredencial.jspx");
            mv.addObject("cambioCredencial", new CambioCredencial(usuario.getFullName(), usuario.getLogin()));
            this.usuario.invalidateSession();
        } else if (this.usuario.isAskForExpPass()) {//for expiration Password
            mv = new ModelAndView();
            mv.setViewName("application/mainBody");
            mv.addObject("menu", "menuWithoutSession.jspx");
            mv.addObject("mainContent", "homeAccountExpirationCredencial.jspx");
            mv.addObject("cambioCredencial", new CambioCredencial(usuario.getFullName(), usuario.getLogin()));
            this.usuario.invalidateSession();
        } else {
            this.usuario.setEntries(this.usuario.getEntries()+1);
            if (this.usuario.getEntries()==1)
                this.usuario.setReload(true);
            else
                this.usuario.setReload(false);

            this.usuario.setEntidadSelected(true);
            mv = new ModelAndView();
            mv.setViewName("application/mainBody");
            mv.addObject("ubigeo", ubigeoService.obtenerPorCodigoInei(this.usuario.getCoUbigeoInei()));
            mv.addObject("usuario", this.usuario);
            mv.addObject("menu", "menuWithSession.jspx");
            mv.addObject("img", imagenCiudadano.obtenerImagen(SimpleBase64.encodeBase64(usuario.getLogin())));

            if (this.usuario != null && this.usuario.getCoTipoEntidad() != null && this.usuario.getCoTipoEntidad().equals(CO_TIPO_ENTIDAD_MUNICIPIO))
                mv.addObject("nuRegObservados", padronObservadoService.contarPadronObservado(this.usuario.getCoUbigeoInei()));
            if (usuario.getLogin() == null || usuario.getLogin().isEmpty()) {captcha(mv);}
            mv.addObject("mainContent", "homeAccount.jspx");
        }
        return mv;
    }

    protected ModelAndView noSessionPage() {
        return new ModelAndView("application/noSession");
    }

    @Override
    protected ApplicationUser getLoggedUser(HttpServletRequest httpServletRequest) {
        throw new UnsupportedOperationException("Use el otro método");
    }

    @RequestMapping("/cambioCredencialExp.do")
    public String doCambiarCredencialExp(@Valid @ModelAttribute("cambioCredencial") CambioCredencial cambioCredencial,
                                         BindingResult bindingResult, Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            return "application/homeAccountExpirationCredencial";
        }

        if (cambioCredencial.getDePasswordNuevo().equals(cambioCredencial.getCoUsuario())) {//Contraseña igual usuario
            model.addAttribute("errorPassEqual", true);
            model.addAttribute("errorPassEqualMessage", "Debe ser diferente al número de DNI.");
            return "application/homeAccountExpirationCredencial";
        }

        try {
            cambioCredencial.setCoTipoAccionPw("05");
            cambioCredencial.setUsModiPw(cambioCredencial.getCoUsuario());
            usuarioExternoService.updateUsuarioPassword(cambioCredencial);
            // la forma es javascript al devolver la página, otra es devolver a otra vista...
            String urlContextPath = httpServletRequest.getRequestURL().toString().replace(httpServletRequest.getRequestURI(), httpServletRequest.getContextPath() + "/");
            model.addAttribute("success", true);
            model.addAttribute("loginUrl", urlContextPath);
            return "application/homeAccountExpirationCredencial";

        } catch (DataAccessException dae) {
            dae.printStackTrace();
            httpServletResponse.setStatus(500);
            return null;
        }
    }

    @RequestMapping("/cambioCredencial.do")
    public String doCambiarCredencial(@Valid @ModelAttribute("cambioCredencial") CambioCredencial cambioCredencial,
                                      BindingResult bindingResult, Model model, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            return "application/homeAccountCambioCredencial";
        }

        if (cambioCredencial.getDePasswordNuevo().equals(cambioCredencial.getCoUsuario())) {//Contraseña igual usuario
            model.addAttribute("errorPassEqual", true);
            model.addAttribute("errorPassEqualMessage", "Debe ser diferente al número de DNI.");
            return "application/homeAccountCambioCredencial";
        }

        try {
            cambioCredencial.setCoTipoAccionPw("04");
            cambioCredencial.setUsModiPw(cambioCredencial.getCoUsuario());
            usuarioExternoService.updateUsuarioPassword(cambioCredencial);
            // la forma es javascript al devolver la página, otra es devolver a otra vista...
            String urlContextPath = httpServletRequest.getRequestURL().toString().replace(httpServletRequest.getRequestURI(), httpServletRequest.getContextPath() + "/");
            model.addAttribute("success", true);
            model.addAttribute("loginUrl", urlContextPath);
            return "application/homeAccountCambioCredencial";

        } catch (DataAccessException dae) {
            dae.printStackTrace();
            httpServletResponse.setStatus(500);
            return null;
        }
    }

    protected ModelAndView getHomeWithSessionEntidad() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("application/mainBody");
        return mv;
    }

    @RequestMapping(value = "/loginLast.do")
    public ModelAndView loginProcessLast(@RequestParam(value = "entidad", required = true) String coEntidad) {
        ModelAndView mv = getHomeWithSessionEntidad();

        Usuario loggedUser = new Usuario();
        Usuario usuarioBd;

        String coUsuario = usuario.getLogin();
        String dePassword = usuario.getPassword();

        if (!coEntidad.equals("0")) {
            usuarioBd = usuarioExternoService.getUsuario(coUsuario, dePassword, coEntidad);
            if (usuarioBd != null) {
                loggedUser.setLogin(usuarioBd.getLogin());
                loggedUser.setNombres(usuarioBd.getNombres());
                loggedUser.setMaterno(usuarioBd.getMaterno());
                loggedUser.setPaterno(usuarioBd.getPaterno());
                loggedUser.setPassword(usuarioBd.getPassword());
                loggedUser.setFePassword(usuarioBd.getFePassword());
                loggedUser.setEsExpire(usuarioBd.getEsExpire());
                loggedUser.setEsMinsa(usuarioBd.getEsMinsa());
                loggedUser.setGrupos(usuarioExternoDao.getGruposUsuario(usuarioBd.getLogin()));

                if (usuarioBd.getCoEntidad() != null && !usuarioBd.getCoEntidad().isEmpty()) {//La segunda LLAMADA A LOGIN.DO USUARIO,PASSWORD,COENTIDAD
                    loggedUser.setEntidadSelected(true);
                    loggedUser.setCoEntidad(usuarioBd.getCoEntidad());
                    loggedUser.setCoTipoEntidad(usuarioBd.getCoTipoEntidad());
                    loggedUser.setDeEntidad(usuarioBd.getDeEntidad());
                    loggedUser.setCoUbigeoInei(usuarioBd.getCoUbigeoInei());
                    loggedUser.setAuthorities(usuarioExternoService.getAuthorities(usuarioBd.getLogin()));
                    loggedUser.setIpMaquina(usuario.getIpMaquina());

                    usuarioExternoService.setFeLastLogin(loggedUser.getLogin());
                    logger.debug("Bienvenido: " + loggedUser);

                    loggedUser.setCoTipoAcceso(properties.getProperty("app.login_tipo_acceso"));// login
                    String coEntidadTmp = loggedUser.getCoEntidad();
                    String[] coEntidadPartes = coEntidadTmp.split("_");
                    if (coEntidadPartes[0] != null)
                        loggedUser.setCoEntidad(coEntidadPartes[0]);

                    usuarioExternoService.guardarAccesoUsuario(loggedUser);
                    ApplicationUser loggedUser1 = loggedUser;
                    this.applicationUser.setValues(loggedUser1);
                    return getHomeWithSession();
                }
            }
        } else {
            List<Entidad> entidades = usuarioExternoService.getUsuarioEntidad(coUsuario);
            mv.addObject("errorMessage", "Seleccione una entidad.");
            mv.addObject("usuario", this.usuario);
            mv.addObject("img", imagenCiudadano.obtenerImagen(SimpleBase64.encodeBase64(usuario.getLogin())));
            mv.addObject("menu", "menuWithSessionOnlyEntidad.jspx");
            mv.addObject("entidades",entidades);
            mv.addObject("mainContent", "entidades.jspx");
        }
        return mv;
    }

    @Override
    @RequestMapping(value = "/login.do")
    public ModelAndView loginProcess(HttpServletRequest httpServletRequest) {

//        ModelAndView mv = getHomeWithoutSession();
        ModelAndView mvEntidad = getHomeWithSessionEntidad();

        try {
            ApplicationUser loggedUser = getLoggedUser(httpServletRequest, /*mv,*/ mvEntidad);
            loggedUser.authenticate(httpServletRequest);
            this.applicationUser.setValues(loggedUser);
            return getHomeWithSession();
        } catch (EntidadesException e) {
            this.usuario.setEntries(this.usuario.getEntries()+1);
            if (this.usuario.getEntries()==1)
                this.usuario.setReload(true);
            else
                this.usuario.setReload(false);

            logger.error(e.getMessage());
            mvEntidad.addObject("usuario", this.usuario);
            mvEntidad.addObject("img", imagenCiudadano.obtenerImagen(SimpleBase64.encodeBase64(usuario.getLogin())));
            mvEntidad.addObject("menu", "menuWithSessionOnlyEntidad.jspx");
            mvEntidad.addObject("mainContent", "entidades.jspx");
            return mvEntidad;
        } catch (Exception e) {
            ModelAndView mv = getHomeWithoutSession();
            logger.error(e.getMessage());
            mv.addObject("errorMessage", e.getMessage());
            return mv;
        }

    }

    @Override
    @RequestMapping(value = "/logout.do")
    public ModelAndView logout(HttpServletRequest httpServletRequest) {
        try {
            logger.debug("Eliminando información de la sesión");
            if (usuario != null) {
                usuario.setCoTipoAcceso(properties.getProperty("app.logout_tipo_acceso"));
                usuarioExternoService.guardarAccesoUsuario(usuario);
            }
            this.applicationUser.invalidateSession();
            /*this.applicationUser.set*/
            return getHomeWithoutSession();
        } catch (Exception e) {
//            logger.debug("No existe session");
            //e.printStackTrace();
            return noSessionPage();
        }

    }


    protected Usuario getLoggedUser(HttpServletRequest httpServletRequest, /*ModelAndView modelAndView,*/ ModelAndView mvEntidad) throws Exception {

        Usuario loggedUser = new Usuario();
        loggedUser.setRememberSession(true);

        String coUsuario = httpServletRequest.getParameter("user");
        String dePassword = httpServletRequest.getParameter("password");
        String coEntidad = httpServletRequest.getParameter("entidad");
        String captcha = httpServletRequest.getParameter("deCaptcha");
/*        String captcha = httpServletRequest.getParameter("g-recaptcha-response");

        String respuesta = null;
        respuesta = SesionRest.post(url, privada, captcha);
        RecaptchaResponse response = getInstance().readValue(respuesta, RecaptchaResponse.class);*/

//        if (response != null && response.getSuccess()) {
        if (captcha != null && captcha.equals(usuario.getCaptchaText())) {

            Usuario usuarioBd;
            if (coEntidad != null && !coEntidad.isEmpty()) {
                usuarioBd = usuarioExternoService.getUsuario(coUsuario, dePassword, coEntidad);
            } else {
                usuarioBd = usuarioExternoService.getUsuario(coUsuario, dePassword);
            }

            if (usuarioBd != null) {
                if ("0".equals(usuarioBd.getEsUsuario())) {
                    throw new Exception("El usuario esta inactivo.");
                }

                loggedUser.setLogin(usuarioBd.getLogin());
                loggedUser.setNombres(usuarioBd.getNombres());
                loggedUser.setMaterno(usuarioBd.getMaterno());
                loggedUser.setPaterno(usuarioBd.getPaterno());
                loggedUser.setPassword(usuarioBd.getPassword());
                loggedUser.setFePassword(usuarioBd.getFePassword());
                loggedUser.setEsExpire(usuarioBd.getEsExpire());
                loggedUser.setEsMinsa(usuarioBd.getEsMinsa());

                if (usuarioExternoDao.getGruposUsuario(usuarioBd.getLogin()) != null)
                    loggedUser.setGrupos(usuarioExternoDao.getGruposUsuario(usuarioBd.getLogin()));
                /*Primera vez en logearse*/
                if (loggedUser.getFePassword() == null || loggedUser.getFePassword().isEmpty()) {//ingresa por primera vez
                    loggedUser.setAskNewPasword(true);
                    return loggedUser;
                } else {//contraseña caducada(30 dias en properties)
                    //solo determinados usuarios.
                    if (loggedUser.getEsExpire().equals("1")) {
                        long dias = this.getDiasPassword(loggedUser.getFePassword());
                        if (dias > Long.parseLong(properties.getProperty("application.daysToExpire"))) {
                            loggedUser.setAskForExpPass(true);
                            return loggedUser;
                        }
                    }
                }
                /*==========================================================================*/
                /* Obtenemos la entidad														*/
                /*==========================================================================*/
                if (usuarioBd.getCoEntidad() != null && !usuarioBd.getCoEntidad().isEmpty()) {//La segunda LLAMADA A LOGIN.DO USUARIO,PASSWORD,COENTIDAD
                    loggedUser.setCoEntidad(usuarioBd.getCoEntidad());
                    loggedUser.setCoTipoEntidad(usuarioBd.getCoTipoEntidad());
                    loggedUser.setDeEntidad(usuarioBd.getDeEntidad());
                    loggedUser.setCoUbigeoInei(usuarioBd.getCoUbigeoInei());
                } else {//PRIMERA LLAMADA A LOGIN.DO USUARIO, PASSWORD
                    List<Entidad> entidades = usuarioExternoService.getUsuarioEntidad(coUsuario);

                    if (entidades == null || entidades.size() == 0) {
                        throw new Exception("No tiene asignada entidad alguna");
                    } else if (entidades.size() > 1) {
                        loggedUser.setEntidadSelected(false);

                        String nuIp = httpServletRequest.getRemoteAddr() != null ? httpServletRequest.getRemoteAddr() : "127.0.0.1";
                        String clientIp = httpServletRequest.getHeader("ClientIP");
                        String clientIpXforwardedFor = httpServletRequest.getHeader("X-Forwarded-For");
                        if ((clientIp != null) && (clientIp.length() != 0)) {
                            loggedUser.setIpMaquina(clientIp);
                            logger.info("IP del usuario se obtiene a traves del header ClientIP");
                        } else {
                            if ((clientIpXforwardedFor != null) && (clientIpXforwardedFor.length() != 0)) {
                                loggedUser.setIpMaquina(clientIpXforwardedFor);
                                logger.info("IP del usuario se obtiene a traves del header X-Forwarded-For");
                            } else {
                                loggedUser.setIpMaquina(nuIp);
                            }
                        }
                        mvEntidad.addObject("login", usuarioBd.getUserName());
                        mvEntidad.addObject("password", usuarioBd.getPassword());
                        mvEntidad.addObject("entidades", entidades);
                        this.applicationUser.setValues(loggedUser);
                        /*throw new Exception("Seleccione una entidad para logearse");*/
                        throw new EntidadesException(1);
                    } else {
                        loggedUser.setCoEntidad(entidades.get(0).getCoEntidad());
                        loggedUser.setDeEntidad(entidades.get(0).getDeEntidad());
                        loggedUser.setCoTipoEntidad(entidades.get(0).getCoTipoEntidad());
                        loggedUser.setCoUbigeoInei(entidades.get(0).getCoUbigeoInei());
                    }
                }
                /*=================================================================*/
                loggedUser.setAuthorities(usuarioExternoService.getAuthorities(loggedUser.getLogin()));
                usuarioExternoService.setFeLastLogin(loggedUser.getLogin());
                logger.debug("Bienvenido: " + loggedUser);

                String nuIp = httpServletRequest.getRemoteAddr() != null ? httpServletRequest.getRemoteAddr() : "127.0.0.1";
                String clientIp = httpServletRequest.getHeader("ClientIP");
                String clientIpXforwardedFor = httpServletRequest.getHeader("X-Forwarded-For");
                if ((clientIp != null) && (clientIp.length() != 0)) {
                    loggedUser.setIpMaquina(clientIp);
                    logger.info("IP del usuario se obtiene a traves del header ClientIP");
                } else {
                    if ((clientIpXforwardedFor != null) && (clientIpXforwardedFor.length() != 0)) {
                        loggedUser.setIpMaquina(clientIpXforwardedFor);
                        logger.info("IP del usuario se obtiene a traves del header X-Forwarded-For");
                    } else {
                        loggedUser.setIpMaquina(nuIp);
                    }
                }

                loggedUser.setCoTipoAcceso(properties.getProperty("app.login_tipo_acceso"));// login
                String coEntidadTmp = loggedUser.getCoEntidad();
                String[] coEntidadPartes = coEntidadTmp.split("_");
                if (coEntidadPartes[0] != null)
                    loggedUser.setCoEntidad(coEntidadPartes[0]);

                usuarioExternoService.guardarAccesoUsuario(loggedUser);
                return loggedUser;
            } else {
                throw new Exception("Datos de inicio de sesión incorrectos");
            }
        } else {
            throw new Exception("Código captcha incorrecto");
        }
    }

    public long getDiasPassword(String fechaInit) {
        long dias;
        try {
            Date fechaServidor = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

            Date fechaInicial = formato.parse(fechaInit);//formato ddMMyyyy
            dias = (fechaServidor.getTime() - fechaInicial.getTime()) / Long.parseLong(properties.getProperty("application.convertTime"));
        } catch (Exception e) {
            dias = 0;
        }
        return dias;
    }

    @RequestMapping("/home.do")
    public String Home(Model model) {
        model.addAttribute("usuario", usuario);

        if(usuario != null && (usuario.getLogin() != null && !usuario.getLogin().isEmpty())){
            if (usuario.getEntidadSelected()) {
                if (this.usuario != null && this.usuario.getCoTipoEntidad() != null && this.usuario.getCoTipoEntidad().equals(CO_TIPO_ENTIDAD_MUNICIPIO))
                    model.addAttribute("nuRegObservados", padronObservadoService.contarPadronObservado(this.usuario.getCoUbigeoInei()));
                return "application/homeAccount";
            } else {
                List<Entidad> entidades = usuarioExternoService.getUsuarioEntidad(usuario.getLogin());
                model.addAttribute("entidades",entidades);
                return "application/entidades";
            }
        }else{
            return "application/login";
        }
    }

    @RequestMapping("/recommendations.do")
    public String Recomendations(Model model) {
        return "application/recomendations";
    }

    @RequestMapping("/acercade.do")
    public String acercade(Model model) {
        return "application/acercade";
    }


    @RequestMapping("/session_timer.do")
    @ResponseBody
    public String SessionTimer(Model model, HttpSession session) {
        if (usuario.getLogin() != null && !usuario.getLogin().equals("")) {
            return properties.getProperty("application.timer-session");
        } else {
            session.invalidate();
            return "00:00";
        }
    }

    @RequestMapping(value = "/close_window.do", method = RequestMethod.POST)
    @ResponseBody
    public String closeWindow(Model model, HttpSession session) {
        if (usuario != null && usuario.getLogin() != null && usuario.getIpMaquina() != null && usuario.getCoEntidad() != null &&
                usuario.getCoUbigeoInei() != null) {
            usuario.setCoTipoAcceso(properties.getProperty("app.logout_tipo_acceso"));
            usuarioExternoService.guardarAccesoUsuario(usuario);
        }
        session.invalidate();
        this.applicationUser.invalidateSession();
        logger.info("Cerrando ventana navegador!");
        return "ok";
    }

    @ResponseBody
    @RequestMapping(value = "/captcha.do", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public void captcha(ModelAndView mv) {
        Captcha captcha = new Captcha.Builder(290, 50)
                .addText()
                .addBackground(new FlatColorBackgroundProducer(Color.white))
                .addNoise()
                //.addNoise()
                //.addBorder()
                .build();
        Usuario usuario = (Usuario) applicationUser;
        usuario.setCaptchaText(captcha.getAnswer());
        BufferedImage image = captcha.getImage();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            mv.addObject("imgCaptcha", Base64.encode(imageBytes));
//            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return new byte[0];
    }

    @RequestMapping(value = "/reload_captcha.do")
    public ModelAndView reloadCaptcha() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("application/captcha");
        captcha(mv);
        return mv;
    }

    @RequestMapping(value = "/mensaje.do")
    public String mensaje(Model model) {
        return "application/mensaje";
    }

    @RequestMapping("/unautorized.do")
    public String noAutorized(Model model, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(201);
        model.addAttribute("usuario", this.usuario);
//        httpServletResponse.setStatus(401);
        return "application/unautorized";
    }
}
