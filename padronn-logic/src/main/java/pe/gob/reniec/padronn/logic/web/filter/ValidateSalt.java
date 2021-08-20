package pe.gob.reniec.padronn.logic.web.filter;

/**
 * Created by jfloresh on 10/09/2015.
 */

import com.google.common.cache.Cache;
import org.apache.log4j.Logger;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class ValidateSalt implements Filter {
    private static Logger logger = Logger.getLogger(ValidateSalt.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Assume its HTTP
        HttpServletRequest httpReq = (HttpServletRequest) request;

        // Get the salt sent with the request
        String salt = (String) httpReq.getParameter("_csrf");

        // Validate that the salt is in the cache
        Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>)
                httpReq.getSession().getAttribute("csrfPreventionSaltCache");

        if (csrfPreventionSaltCache != null &&
                salt != null &&
                csrfPreventionSaltCache.getIfPresent(salt) != null) {
            logger.info("salt validado : " + salt);
            // If the salt is in the cache, we move on
            chain.doFilter(request, response);
        } else {
            // Otherwise we throw an exception aborting the request flow
            throw new ServletException("Potential CSRF detected!! Inform a scary sysadmin ASAP.");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}