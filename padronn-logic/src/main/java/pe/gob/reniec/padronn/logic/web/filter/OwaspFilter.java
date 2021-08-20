package pe.gob.reniec.padronn.logic.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OwaspFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse)response;
//        res.addHeader("X-Frame-Options","SAMEORIGIN");
        res.setHeader("X-Frame-Options","SAMEORIGIN");

        res.addHeader("X-XSS-Protection"," 1; mode=block");
        res.addHeader("X-Content-Type-Options","nosniff");
        String url = ((HttpServletRequest)request).getRequestURL().toString();
        if(url.contains(".js")){
            res.addHeader("Content-Type", "text/javascript");
        }
        if(url.contains(".css")){
            res.addHeader("Content-Type", "text/css");
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
    }

}