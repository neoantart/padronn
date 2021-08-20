package pe.gob.reniec.commons.baseApplication.interceptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.reniec.commons.baseApplication.model.ApplicationUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;

public class HttpRequestInterceptor implements HandlerInterceptor {

    HashSet<String> noIntercept;

    Logger log = Logger.getLogger(getClass());

    @Autowired
    private ApplicationUser applicationUser;

    public HashSet<String> getNoIntercept() {
        return noIntercept;
    }

    public void setNoIntercept(HashSet<String> noIntercept) {
        this.noIntercept = noIntercept;
    }

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String mappingPath = (String) httpServletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        if (noIntercept.contains(mappingPath)) {
//			log.debug("La ruta " + mappingPath + " no debe ser interceptada.");
            return true;
        }
        String isAjax = httpServletRequest.getHeader("X-Requested-With");
        if (isAjax == null) {
            httpServletRequest.getHeader("x-requested-with");
        }
        if (isAjax != null && isAjax.equalsIgnoreCase("XMLHttpRequest")) {
//			log.debug("La petición es tipo AJAX. La petición es "+isAjax);
            return true;
        } else {
//			log.debug("La petición no es AJAX. La petición es "+isAjax+". El usuario sera enviado a "+httpServletRequest.getContextPath());
            httpServletResponse.setStatus(405);
            //RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("redirect:/");
            //dispatcher.forward(httpServletRequest, httpServletResponse);
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
            return false;
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}