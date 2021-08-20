package pe.gob.reniec.padronn.logic.web.filter;

/**
 * Created by jfloresh on 10/09/2015.
 */

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

public class LoadSalt implements Filter {

    private static Logger logger = Logger.getLogger(LoadSalt.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Assume its HTTP
        HttpServletRequest httpReq = (HttpServletRequest) request;

        // Check the user session for the salt cache, if none is present we create one
        Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>)
                httpReq.getSession().getAttribute("csrfPreventionSaltCache");

        if (csrfPreventionSaltCache == null) {
            csrfPreventionSaltCache = CacheBuilder.newBuilder()
                    .maximumSize(5000)
                    .expireAfterWrite(20, TimeUnit.MINUTES)
                    .build();

            httpReq.getSession().setAttribute("csrfPreventionSaltCache", csrfPreventionSaltCache);
        }

        // Generate the salt and store it in the users cache
        String salt = RandomStringUtils.random(20, 0, 0, true, true, null, new SecureRandom());
        csrfPreventionSaltCache.put(salt, Boolean.TRUE);

        // Add the salt to the current request so it can be used
        // by the page rendered in this request
        httpReq.setAttribute("_csrf", salt);
        logger.info("Se genero _csrf: " + salt);

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}