package pe.gob.reniec.commons.baseApplication.interceptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.reniec.commons.baseApplication.model.ApplicationUser;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;

public class SessionInterceptor implements HandlerInterceptor {

    HashSet<String> noIntercept;

    Logger log = Logger.getLogger(getClass());

    @Autowired
    private ApplicationUser applicationUser;

    public void setNoIntercept(HashSet<String> noIntercept) {
        this.noIntercept = noIntercept;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String mappingPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        if (applicationUser.isValidForSession()) {
            // TODO NO FUNCIONARA ADECUADAMETNE EN CASOS "NOAJAX", para funcionara requerirá cambios en la librería js.
            if (!isAuthorized(applicationUser, mappingPath)) {
                String redirectUrl = request.getContextPath() + "/unautorized.do";
                response.setStatus(401);
                response.sendRedirect(redirectUrl);
                return false;
            } else {
            }
        }

        if (mappingPath.equals("/")) {
//            log.debug("Interceptamos la petición inicial");

                if (this.applicationUser.isValidForSession()) {
                    log.info("Hay una sesión válida actualmente");
                    RequestDispatcher dispatcher;
                    dispatcher = request.getRequestDispatcher("/mainwithsession");
                    log.debug("Petición redirigida a /mainwithsession");
                    dispatcher.forward(request, response);
                    return false;
                } else {
                    //              log.info("No hay sesión válida");
                    return true;
                }
        }

        if (noIntercept.contains(mappingPath)) {
//            log.debug("La ruta " + mappingPath + " no debe ser interceptada");
            return true;
        }
        if (applicationUser.isValidForSession()) {
//            log.debug("La información del applicationUser en sesión es válida");
            return true;
        } else {
//          log.debug("La información del applicationUser en la sesión no es válida");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/noSession");//applicationUser.getInvalidSessionDispatcher());
            dispatcher.forward(request, response);
            return false;
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private boolean isAuthorized(ApplicationUser user, String url) {
        // todos tienen acceso a la raíz, sin esto existe bucle de redireccionamiento,
        if (url.equalsIgnoreCase("/") || url.startsWith("/home") || url.startsWith("/unautorized"))
            return true;

        if (noIntercept.contains(url))
            return true;

        List<String> authorities = user.getAuthorities();
        if (authorities == null || url == null) {
            return false;
        }
        for (String authority : authorities) {
            if (url.startsWith(authority)) {
                return true;
            }
        }
        return false;
    }

}