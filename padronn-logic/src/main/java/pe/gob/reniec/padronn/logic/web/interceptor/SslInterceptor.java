package pe.gob.reniec.padronn.logic.web.interceptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pe.gob.reniec.padronn.logic.web.interceptor.util.RequestUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by jfloresh on 15/09/2014.
 */
@Component
public class SslInterceptor extends HandlerInterceptorAdapter {

    Logger logger = Logger.getLogger(getClass());

    @Value("${app.activeHTTPS}")
    private boolean activeHTTPS;


    @Value("${app.httpsPort}")
    private String httpsPort;

    @Value("${app.httpPort}")
    private String httpPort;

    /**
     * Defaults puertos para HTTP y HTTPS (WEBLOGIC).
     */
    final static int HTTP_PORT = 7001;
    final static int HTTPS_PORT = 7002;

    final static String HTTP_GET = "GET";
    final static String HTTP_POST = "POST";

//    final static String HTTP_PUT = "PUT"; todo

    final static String SCHEME_HTTP = "http";
    final static String SCHEME_HTTPS = "https";



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String scheme = request.getScheme().toLowerCase();
        String method = request.getMethod().toUpperCase();

        if (activeHTTPS) {
            if ( (HTTP_GET.equals(method) || HTTP_POST.equals(method)) && SCHEME_HTTP.equals(scheme)) {
                Integer httpsPortInt;
                httpsPortInt = httpsPort == null ? HTTPS_PORT : Integer.parseInt(httpsPort);
                URI uri = new URI(SCHEME_HTTPS, null, request.getServerName(),
                        httpsPortInt, request.getRequestURI(),
                        RequestUtil.buildQueryString(request), null);

                String uriString = uri.toString();
                if (uriString.charAt(uriString.length() - 1) != '/') {
                    uriString = uriString + "/";
                }
                //logger.info("Going to SSL mode, redirecting to " + uriString);
                response.sendRedirect(uriString);
                return false;
            }
        } else {
            if ((HTTP_GET.equals(method) || HTTP_POST.equals(method)) && SCHEME_HTTPS.equals(scheme)) {

                // initialize http port
                int httpPortInt = httpPort == null ? HTTP_PORT : Integer.parseInt(httpPort);

                URI uri = new URI(SCHEME_HTTP, null, request.getServerName(),
                        httpPortInt, request.getRequestURI(),
                        RequestUtil.buildQueryString(request), null);

                String uriString = uri.toString();
                if(uriString.charAt(uriString.length()-1) != '/' ) {
                    uriString = uriString + "/";
                }
//                logger.info("Going to non-SSL mode, redirecting to " + uriString);
                response.sendRedirect(uriString);
                return false;
            }
        }
        return true;
    }

}